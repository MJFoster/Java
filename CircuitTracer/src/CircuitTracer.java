import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure.  Displays output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * 1st arg: -s for stack or -q for queue
 * 2nd arg: -c for console output or -g for GUI output
 * 3rd arg: input file name 
 * 
 * @author mvail
 * @author MaryJo Foster
 * @version summer 2016
 */
public class CircuitTracer {
	private CircuitBoard board;
	private Storage<TraceState> stateStore;
	private ArrayList<TraceState> solutionsList = new ArrayList<TraceState>();
	private boolean fromTState = false;
	TraceState currState;
	
	/** 
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) 
	{
		if (    args.length != 3
				||  ( !args[0].equals("-s")  && !args[0].equals("-q") )
				||  ( !args[1].equals("-c")  &&	!args[1].equals("-g") )
		   )
		{
			printUsage();
			System.exit(1);
		}
		try
		{
			new CircuitTracer(args); //create this with args
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private static void printUsage() {
		System.out.println("Try again, all 3 parameters required in the following order: ");
		System.out.println("-s: Specifies underlying to be a Stack.");
		System.out.println("-q: Specifies underlying to be a Queue.");
		System.out.println("-c: Specifies output to be the console.");
		System.out.println("-g: Specifies output to be a GUI.");
		System.out.println("java CircuitTracer [-s / -q] [-c / -g] [filename of data file]");
	}
	
	/** 
	 * CircuitBoard constructor.
	 * 
	 * Initializes board and underlying Storage from command line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	private CircuitTracer(String[] args) {
		
		// Initialize stateStore to manage data type of underlying storage for TraceState objects.
		if(args[0].equals("-s"))  // Use STACK storage
		{
			stateStore = new Storage<TraceState>(Storage.DataStructure.stack);
		}
		else  // Use QUEUE storage
		{
			stateStore = new Storage<TraceState>(Storage.DataStructure.queue);
		}

		try 
		{	
			// Initialize original board from input file
			board = new CircuitBoard(args[2]);	
		
		} catch (FileNotFoundException e)
		{
			System.out.println("File not found (CircuitTracer - Constructor): " + args[2] + "\n" + e + "\n");
			System.exit(1);
		} catch (InvalidFileFormatException e)
		{
			System.out.println("Invalid Input File Format (CircuitTracer - Constructor): " + args[2] + "\n" + e + "\n");
			System.exit(1);
		} catch (Exception e)
		{
			System.out.println("Miscellaneous Exception (CircuitTracer - Constructor): \n" + e + "\n");
			System.exit(1);
		}
		
		// Load stateStore with INITIAL TraceState(s)
		storeNextTraceStates(fromTState);

		// Review all possible TraceStates in stateStore looking for TraceStates adjacent
		// to board endingPoint, and saving TraceStates with the shortest path.
		while( !stateStore.isEmpty())
		{
			currState = stateStore.retrieve();  // removes and returns TraceState.
			
			if(currState.isComplete())  // Solution Found, keep only shortest path solutions.
			{
				if( solutionsList.isEmpty() )
				{
					solutionsList.add(currState);
				}
				else if(currState.pathLength() == solutionsList.get(0).pathLength())
				{
					solutionsList.add(currState);
				}
				// clear solutionsList if currState is shorter than those in solutionsList
				else if(currState.pathLength() < solutionsList.get(0).pathLength())
				{
					solutionsList.clear();
					solutionsList.add(currState);
				}	
			}
			// Locate next group of TraceStates starting at end of path Point on currState.
			fromTState = true;
			storeNextTraceStates(fromTState);
		}
		
		if(args[1].equals("-c"))
		{
			for(int i = 0; i < solutionsList.size(); i++)
			{
				System.out.println(solutionsList.get(i).toString());
			}
		}
		else
		{
			System.out.println("We're sorry, our GUI output is still under construction... see console for output.\n");
			for(int i = 0; i < solutionsList.size(); i++)
			{
				System.out.println("Solution # " + (i+1) + ":\n\n" + solutionsList.get(i).toString());
			}
		}

	}
	
	
	/**
	 * Add to stateStore the next group of possible TraceStates.
	 * 
	 * @param fromTState - True if at least 1 TraceState has been saved, otherwise false.
	 *                     Determines which TraceState constructor being called.
	 * 
	 * @author MaryJo Foster
	 */
	public void storeNextTraceStates(boolean fromTState)
	{	
		Point start;

		try
		{
			// Consider all 4 possible TraceState positions, N-E-S-W, from last Point in path.
			if(fromTState)  // check for possible TraceStates starting at currState
			{			
				start = currState.getPath().get(currState.getPath().size() - 1);		
				if(start.x > 0) // Trace Adjacent NORTH position if available
				{
					if(currState.isOpen(start.x-1,start.y))
						stateStore.store(new TraceState(currState, start.x-1, start.y)); 
				}

				if(start.y < currState.getBoard().numCols()-1)  // Trace Adjacent EAST position if available
				{
					if(currState.isOpen(start.x,start.y+1))
						stateStore.store(new TraceState(currState, start.x, start.y+1));
				}

				if(start.x < currState.getBoard().numRows()-1)  // Trace Adjacent SOUTH position if available
				{
					if(currState.isOpen(start.x+1,start.y))
						stateStore.store(new TraceState(currState, start.x+1, start.y));
				}

				if(start.y > 0)  // Trace Adjacent WEST position if available
				{
					if(currState.isOpen(start.x,start.y-1))
						stateStore.store(new TraceState(currState, start.x, start.y-1));			
				}
			}
			else  		
			{	
				// Consider all 4 possible TraceState positions, N-E-S-W, from board startingPoint. 
				start = board.getStartingPoint();		
				if(start.x > 0) // Trace Adjacent NORTH position if available
				{
					if(board.isOpen(start.x-1,start.y))
						stateStore.store(new TraceState(board, start.x-1, start.y)); 
				}

				if(start.y < board.numCols()-1)  // Trace Adjacent EAST position if available
				{
					if(board.isOpen(start.x,start.y+1))
						stateStore.store(new TraceState(board, start.x, start.y+1));
				}

				if(start.x < board.numRows()-1)  // Trace Adjacent SOUTH position if available
				{
					if(board.isOpen(start.x+1,start.y))
						stateStore.store(new TraceState(board, start.x+1, start.y));
				}

				if(start.y > 0)  // Trace Adjacent WEST position if available
				{
					if(board.isOpen(start.x,start.y-1))
						stateStore.store(new TraceState(board, start.x, start.y-1));			
				}
			}
		}
		catch (OccupiedPositionException e)  
		{
			// Thrown only if Trace attempted on occupied position after 2nd check
			System.out.println(e);
		}
	}
	
	
} // class CircuitTracer
