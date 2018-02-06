import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail
 */
public class CircuitBoard {
	private char[][] board;
	private Point startingPoint; // object w/ (int x, int y) values
	private Point endingPoint;  // object w/ (int x, int y) values

	//constants you may find useful
	private final int ROWS; //initialized in constructor
	private final int COLS; //initialized in constructor
	private final char OPEN = 'O'; //capital 'o'
	private final char CLOSED = 'X';
	private final char TRACE = 'T';
	private final char START = '1';
	private final char END = '2';
	private final String ALLOWED_CHARS = "OXT12";
	
	private Scanner fileScan;
	private Scanner tokenScan;

	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot read the file
	 * @throws InvalidFileFormatException for any other format or content issue that prevents reading a valid input file
	 */
	public CircuitBoard(String filename) throws FileNotFoundException, InvalidFileFormatException {
		fileScan = new Scanner(new File(filename));  // throws FileNotFoundException if no file found.
		char currentChar;
		String currentRow = fileScan.nextLine();

		// Scanner for currentRow
		tokenScan = new Scanner(currentRow);
		
		if(tokenScan.hasNext())
		{
			try
			{				
				ROWS = Integer.parseInt(tokenScan.next());
			}
			catch (NumberFormatException e)
			{
				closeScanners();
				throw new InvalidFileFormatException("CircuitBoard - Invalid columns/rows on 1st line: "
						                            + currentRow + "\n");
			}

			if(tokenScan.hasNext())
				try
				{
					COLS = Integer.parseInt(tokenScan.next());
				}
				catch (NumberFormatException e)
				{
					closeScanners();
					throw new InvalidFileFormatException("CircuitBoard - Invalid columns/rows on 1st line: "
														+ currentRow + "\n");
				}
			else
			{
				COLS = 0;
				closeScanners();
				throw new InvalidFileFormatException("CircuitBoard - Invalid columns/rows on 1st line.\n");
			}
		}
		else
		{
			ROWS = 0;
			COLS = 0;
			closeScanners();
			throw new InvalidFileFormatException("CircuitBoard - Invalid columns/rows on 1st line.\n");
		}
		
		if(ROWS == 0  ||  COLS == 0)
		{
			closeScanners();
			throw new InvalidFileFormatException("CircuitBoard - Duplicate ending point.\n");
		}
		else
			// Instantiate board with dimensions from input file.
			board = new char[ROWS][COLS];
		
		// Parse file to populate board
		while(fileScan.hasNext())
		{
			for(int i = 0; i < ROWS; i++)
			{
				currentRow = fileScan.nextLine().trim();
				tokenScan = new Scanner(currentRow);

				for(int j = 0; j < COLS; j++)
				{
					currentChar = tokenScan.next().charAt(0);

					// if curentChar invalid, throw exception
					if( ! (ALLOWED_CHARS.contains(Character.toString(currentChar))) )
					{
						closeScanners();
						throw new InvalidFileFormatException("CircuitBoard - Invalid input data.\n"
								+ "Only allowable characters: O, X, 1, 2\n");
					}
					else
						board[i][j] = currentChar; 

					// Check for startingPoint
					if(currentChar == START)
					{
						// Set startingPoint position if not yet set.
						if(startingPoint != null)
						{
							closeScanners();
							throw new InvalidFileFormatException("CircuitBoard - Duplicate starting point.\n");
						}
						else
							startingPoint = new Point(i,j);
					}
					
					// Check for endingPoint
					if(currentChar == END)
					{
						// Set endingPoint position if not yet set.
						if(endingPoint != null)
						{
							closeScanners();
							throw new InvalidFileFormatException("CircuitBoard - Duplicate ending point.\n");
						}
						else
							endingPoint = new Point(i,j);
					}

					// check for invalid columns
					if(     ( j < COLS-1  &&  !tokenScan.hasNext())
							||  ( j+1 == COLS &&  tokenScan.hasNext())  )
					{
						closeScanners();
						throw new InvalidFileFormatException("CircuitBoard - Invalid # columns.\n");
					}
				}

				// check for invalid rows
				if(     ( i < ROWS-1  &&  !fileScan.hasNext())
						|| ( i+1 == ROWS &&  fileScan.hasNext())   )
				{
					closeScanners();
					throw new InvalidFileFormatException("CircuitBoard - Invalid # rows.\n");
				}
			}
		}
		closeScanners();
		if(startingPoint == null || endingPoint == null)
			throw new InvalidFileFormatException("CircuitBoard - Both start and end points required.\n");
	}
	
	
	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	
	/** utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}
	
	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}
	
	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}
	
	/** @return starting Point */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}
	
	/** @return ending Point */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}
	
	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}
	
	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}
	
	
	/**
	 * Helper method to close all open class scanners.
	 *
	 * @version summer 2016
	 * @author MaryJo Foster
	 */
	private void closeScanners()
	{
		tokenScan.close();
		fileScan.close();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
}// class CircuitBoard
