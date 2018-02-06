import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class HashTable
{

	private HashObject[] hTable;	// Array of HashObjects
	private int tableSize;			// 'm' value
	private int n;					// Expected # elements to be inserted
	int elementsInserted;			// Actual # elements inserted - linear table
	private int tblProbes;			// Total # probes made while inserting
	private int tblDups;			// Total # duplicate entries
	private String tableType;		// "linear" or "double"
	private int dataType;			// Type of data being stored = 1, 2, or 3 for int, time, or string objects
	
	HashObject <?> obj = null;		// new generic object being stored
	private final int MIN_RANGE = 95500; 
	private final int MAX_RANGE = 96000;

	private Random rGen = new Random();	// Instantiate Random # generator
	
	static Scanner scan;  			// Scanner for input strings

	private String filename = null;	// Text file used as input for dataType 3
	
	
	/*
	 * Constructor - Chooses tableSize & max number of elements to 
	 * insert efficiently, and initializes instance variables.
	 * 
	 * @params 	- tblType: Either "linear" or "double" for linear probing vs.
	 * 			           double Hashing style hash table type.
	 * 			- load: % of table that will be occupied.
	 * 			- typeOfData:  type of input data. 1 = int, 2 = long, 3 = String
	 *          - dataFile: filename of data file for strings, null if none
	 */
	public HashTable (String tblType, float load, int typeOfData, String dataFile)
	{
		try
		{
			tableSize = calcTableSize(load);
			hTable = new HashObject[tableSize];
			n = (int) Math.ceil(load * tableSize);
			elementsInserted = 0;
			tblProbes = 0;
			tblDups = 0;
			tableType = tblType;
			dataType = typeOfData;
			filename = dataFile;
			scan = null;
		} catch (PrimeNotFoundException ex)
		{
			System.err.printf("HashTable : CONSTRUCTOR - No Twin Primes In Range, Can't generate HashTable\n%s", ex);
		} catch (Exception ex)
		{
			System.err.printf("HashTable : CONSTRUCTOR - Unknown Exception\n%s", ex);
		}
	}
	
	
	/*
	 * Calculates the optimal HashTable size (capacity)
	 * based on a given load.
	 * 
	 * @params - load: % of table that will be occupied. 
	 * @return - size of table
	 * @throws - PrimeNotFoundException
	 */
	private int calcTableSize (float load) throws PrimeNotFoundException
	{
		boolean found = false;
		int size= MIN_RANGE;

		if(size % 2 == 0)	// Start checking for prime #'s w/an odd number
			size++;	// MIN_RANGE is even		
		
		while (size <= MAX_RANGE  &&  !found)
		{
			if( (size % 3) != 0  &&  (size % 5) != 0  &&  (size % 7) != 0)
			{
				if( isPrime(size) )
				{
					if( isPrime(size + 2) )
					{
						found = true;
					}
				}
			}
			size += 2;		// Check next odd value
		}
		if (size > MAX_RANGE)
		{
			throw new PrimeNotFoundException("HashTable : calcTableSize - ");
		}
		return size;
	}
	
	
	/*
	 * Helper method that determines if a given int is a prime number.
	 * 
	 * @params p - number to be assessed.
	 * @return boolean - true if given int is a prime #, otherwise false.
	 */
	private boolean isPrime(int p)
	{
		int r;
		double result = 0;
		String str;
		Random rGenerator = new Random();
		
		for (int i = 0; i < 3; i++)		// Test 3 different random numbers to conclude isPrime
		{
			r = rGenerator.nextInt(p-3) + 2;	// random number from 2 ... p-1
			result = r;
			str = Integer.toBinaryString(p-1);	// convert to binary String for parsing in algorithm below.
			
			for (int j = 1; j < str.length(); j++)
			{
				if (str.substring(j,j+1).equals("1"))
				{
					result = ( Math.pow(result, 2) * r) % p;
				}
				else
				{
					result = Math.pow(result,2) % p;
				}
			}
			if (result != 1)
				return false;	// Conclusively NOT a prime number, exit tests
		}						// Test again
		
		// 3 tests completed
		if(result != 1)
			return false;
		else
			return true;
	}
	
	
	/*
	 * Inserts the given HashObject into the HashTable if, and only if, it is
	 * not yet inserted.  If the object already exists in the HashTable, space
	 * will be conserved by simply incrementing the duplicates counter at that 
	 * index instead of inserting a duplicate object into the HashTable.
	 * 
	 * @return boolean - true if HashObject successfully inserted, otherwise false.
	 * @params obj - The object being inserted.
	 */
	@SuppressWarnings("rawtypes")
	public boolean insert (HashObject obj)
	{
		boolean inserted = false;
		boolean done = false;
		long key = obj.getKey();
		int h1, h2;
		
		h1 =   (int) key % tableSize;  // 1st Hash 
	
		switch (occupied(h1, obj))
		{
		case -1:		// Vacant, ok to insert
			obj.addProbe(tableType, 1);  // Update 'probes' count on the HashObject
			hTable[h1] = obj;
			tblProbes++;
			inserted = true;
			break;
		case 0:			// Duplicate found
			hTable[h1].incrementDups(tableType);  // Increment object's duplicate counter
			tblDups++;  // Increment table's duplicate counter

			break;
		case 1:			// Collision encountered
			for (int i = 1; (i < (tableSize-1)  &&  !done); i++)
			{
				h2 = (h1 + (int) (calcStep(tableType, obj, i))) % tableSize;  // Additional Hashes
				switch (occupied(h2, obj))
				{
				case -1:	// Vacant, ok to insert
					hTable[h2] = obj;
					obj.addProbe(tableType, i + 1);		// Update 'probes' count on HashObject
					tblProbes += i + 1;
					inserted = true;
					done = true;
					break;
				case 0:		// Duplicate found, hash again
					hTable[h2].incrementDups(tableType);
					tblDups++;
					done = true;

					break;
				}
			}

		}
		return inserted;
	}
	
	
	/*
	 * Calculates the 2nd hash function based on the given
	 * table type of "linear" or "double".
	 * 
	 * @return int - number of indexes to move forward by.
	 * @param tblType - HashTable type that determines which hash function to use.
	 * @param obj - The HashObject containing the key that's used in the hash function.
	 * @param i - Integer counter for current additional probe count
	 */
	@SuppressWarnings("rawtypes")
	private int calcStep(String tblType, HashObject obj, int i)
	{
		int step = 0;
		
		switch (tblType)
		{
		case "linear":
			step = i % tableSize;
			break;
		case "double": 
			step = (i * (1 + (int) (Math.abs(obj.getKey()) % (tableSize - 2) ))) % tableSize;
			break;
		}
		
		return step;
	}


	/*
	 * Helper method that determines if an index is occupied, contains a 
	 * duplicate, or is vacant.
	 * 
	 * @param index - Index into HashTable to compare against.
	 * @param obj - HashObject to compare against for duplicates.
	 * 
	 * @return int  (-1): vacant, (0): duplicate found, (1): occupied w/ non-duplicate object.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int occupied(int index, HashObject obj)
	{
		if (hTable[index] == null)
		{
			return -1;			
		}
		else if(hTable[index].equals(obj))
		{
			return 0;
		}else
		{			
			return 1;
		}
	}


	/*
	 * Fills HashTable to given capacity
	 * @param capacity percentage to fill table up to.
	 */
	public void fillTable(float capacity)
	{
		if(dataType == 3)
		{
			 try {
				 scan = new Scanner(new File(filename));
			} catch (FileNotFoundException ex)
			{
				System.err.printf("HashTable : fillTable\nUnable to open file%s\n%s\n",filename, ex);
			}
		}
		while (elementsInserted < n)
		{
			switch (dataType)
			{
			case 1:
				obj = new HashObject <Integer> (rGen.nextInt());
				break;
			case 2:
				obj = new HashObject <Long> (System.currentTimeMillis());
				break;
			case 3:
				 obj = new HashObject <String> (scan.nextLine());
				break;
			}
			
			if(insert(obj))		// increment counter IFF obj successfully inserted!
			{
				elementsInserted++;				
			}
		}
		if(scan != null)
				scan.close();

	}
	
	
	/*
	 * Getter for tableSize
	 * @return - size of HashTable
	 */
	public int getSize()
	{
		return tableSize;
	}


	/*
	 * Getter returning the number of elements successfully inserted into HashTable.
	 * 
	 * @return int - number of elements successfully inserted.
	 */
	public int getNumElements()
	{
		return elementsInserted;
	}


	/*
	 * Getter returning the total number of probes made for all
	 * initial insertions.
	 * 
	 * @return int - total number of probes of all initial insertions.
	 */
	public int getProbes()
	{
		return tblProbes;
	}
	
	
	/*
	 * Getter for table duplicates counter
	 */
	public int getDups()
	{
		return tblDups;
	}


	/*
	 * Overloaded toString() to output the table's
	 * non-null entries in 1 of 2 different formats.
	 * 
	 * @params - debug level 1: output non-null entries and # duplicates.
	 *           debug level 2: output non-null entries and # duplicates,
	 *                          and the # probes / entry. 
	 * @throws NumberFormatException - if debug != 1 or 2
	 */
	public void toString(int debug) throws NumberFormatException
	{
		if(debug > 2  ||  debug < 0)	
			throw new NumberFormatException("HashTable : toString(int) - Invalid debug level.\n");
		
		if(debug == 1  ||  debug == 2)
			{
				for(int i = 0; i < tableSize; i++)
				{
					if(hTable[i] != null)
					{
						if(debug == 1)
							System.out.printf("table[%d]: %s %d\n", i, hTable[i].toString(),
									hTable[i].getDups(tableType));
						else 
							System.out.printf("table[%d]: %s %d %d\n", i, hTable[i].toString(),
									hTable[i].getDups(tableType),
									hTable[i].getProbes(tableType));
					}
				}
			}
	}
	
}
