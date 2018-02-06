import java.io.FileNotFoundException;

public class HashTest
{
	static float load;			// load factor for HashTable
	static int dataType;		// Type of input data
	static int tableType;		// "linear" or "double" style HashTable (command line argument)
	static int debug = 0; 		// debug level initialized at default (command line argument)
	static String filename = "word-list.txt";	// Text file used as input for dataType 3
	
	public static void main(String[] args)
	{
		double avgLinearTblProbes;	// Avg # of Unique Probes in table
		double avgDblHashTblProbes;	// Avg # of Unique Probes in table
		HashTable linearTbl = null;		// "Linear Probed" HashTable
		HashTable dblHashTbl = null;		// "Double Hashed" HashTable
		/* 
		 * Based on validated command line arguments, create each type
		 * of HashTable, calculate the number of elements needed to fill
		 * it to the given load factor, and insert elements from the given 
		 * data source.
		 */
		try
		{
			validateCommandLineArgs(args);
			linearTbl = new HashTable("linear", load, dataType, filename);
			linearTbl.fillTable(load);
			
			dblHashTbl = new HashTable("double", load, dataType, filename);
			dblHashTbl.fillTable(load);
			
			// Calculate average # unique probes / table
			avgLinearTblProbes = (double) linearTbl.getProbes() / linearTbl.getNumElements();
			avgDblHashTblProbes = (double) dblHashTbl.getProbes() / dblHashTbl.getNumElements();

			// Output HashTest summary stats
			String dataSource;
			if(dataType == 1)
			{
				dataSource = "random number";			
			}
			else if (dataType == 2)
			{
				dataSource = "current time";
			}
			else
			{
				dataSource = filename;
			}

			System.out.printf("A good table size is found: %d\nData source type:  %s\n\n",
					linearTbl.getSize(), dataSource);
			System.out.printf("Using Linear Hashing ....\n"
					+         "Inserted %d elements of which %d duplicates\n"
					+         "load factor = %.2f, Avg. no. of probes %.16f\n\n",
					linearTbl.getNumElements(), linearTbl.getDups(), load, avgLinearTblProbes);
			System.out.printf("Using Double Hashing ....\n"
					+         "Inserted %d elements of which %d duplicates\n"
					+         "load factor = %.2f, Avg. no. of probes %.16f\n\n",
					linearTbl.getNumElements(), linearTbl.getDups(), load, avgDblHashTblProbes);					

			// Output non-null entries in tables based on debug level
			if(debug != 0)
			{
				System.out.printf("Linear Probed Table ...\n");
				linearTbl.toString(debug);
				System.out.printf("\nDouble Hashed Table ...\n");
				dblHashTbl.toString(debug);				
			}

		} catch (NumberFormatException ex)
		{
			System.err.printf("HashTest : Main\nUSAGE: HashTest <input type> <load factor> [<debug level>]\n%s", ex);
		} catch (Exception ex)
		{
			System.err.printf("HashTest : Main\nUnknown Exception.\n%s", ex);
		} 
	}


	/*
	 * Helper method to read and validate command line arguments.
	 * 
	 * @params args - String[] from command line 'args' array
	 * @throws NumberFormatException - If arguments are out of range or not numbers
	 */
	private static void validateCommandLineArgs(String[] args) throws NumberFormatException, FileNotFoundException 
	{
		// Output usage message ...  throw NumberFormatException()
		if(args.length >= 2  &&  args.length <= 3)
		{
			dataType = Integer.parseInt(args[0]);
			if(dataType < 1  ||  dataType > 3)
				throw new NumberFormatException("Specify input data type of 1, 2, or 3 only.");
			
			if(dataType != 3)
				filename = null;
			
			load = Float.parseFloat(args[1]);
			if(load <= 0 || load > 1.0)
			{
				throw new NumberFormatException("Load must be > 0.0, and <= 1.0");				
			}

			if (args.length == 3)  // Debug
			{
				debug = Integer.parseInt(args[2]);
				if(debug < 0  || debug > 2)
				{
					throw new NumberFormatException("Debug level is 0, 1, or 2 only.");
				}
			}
		} else
		{
			throw new NumberFormatException("Incorrect number of command line arguments.");
		}
	}
}
