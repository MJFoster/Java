import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Searches a given BTree file for a given list of DNA sub-sequences,
 * and returns the number of occurrences of each sub-sequence found in the BTree.
 *
 * CS 321-02
 * 
 * @author Ryleigh Moore
 * @author Joe Bell
 * @author MaryJo Foster
 * @version Fall 2016
 */
public class GeneBankSearch {
	
	private static int cache = -1;
	private static int cacheSize = -1;
	private static int debugLevel = 0;
	private static int treeK = 0;		// K value (sequence length) of given BTree file.
	
	public static void main(String[] args) {
//		long startTime = System.currentTimeMillis();
		Scanner queryScan;
		long key;		// key stored in BTreeNode

		validateCommandLineArgs(args);
		queryScan = initializeQueryFileScanner(args[2]);
		SubStringConverter converter = new SubStringConverter(treeK);
		BTree tree = null;
		try{
			tree = new BTree(args[1], cacheSize); //Create Tree to use while reading in the file to get access to search method.
			} catch (FileNotFoundException e) {
				System.err.println("File: \"" + args[1] + "\" not found.");
				e.printStackTrace();
				System.exit(1);
			}
		if (!tree.checkEmpty(tree.getRootOffset())) {
			// Scan query file, looking for all sub-sequences.
			while (queryScan.hasNextLine()) {
				String subSequence = queryScan.nextLine().toLowerCase();

				key = converter.stringToLong(subSequence);
				TreeObject found = tree.BTreeSearch(tree.getRootOffset(), key); // See if key is in the tree file.
				if (found != null) {
					System.out.println(converter.longToString(found.getKey()).toLowerCase() + ": " + found.getFreqCount());
				}
			}
//			long endTime   = System.currentTimeMillis();
//			long totalTime = endTime - startTime;
			//System.out.println(totalTime);
		}
	}
	

	/** Confirm Query file exists and can be opened		
	 * 
	 * @param - given query file name
	 * @return - Scanner for given query file. 
	 */
	public static Scanner initializeQueryFileScanner(String queryfile) {
		File query = null;
		Scanner queryScan = null;
		
		try {
			query = new File(queryfile);
			queryScan = new Scanner(query);
		} catch (FileNotFoundException e) {
			System.err.printf("file " + queryfile + " not found.\n%s\n", e);
			printUsage();
		}
		return queryScan;
	}


	/** Validates all command line arguments.
	 * 
	 * @param args - array of Strings for each command line argument.
	 */
	public static void validateCommandLineArgs(String[] args) {

		if (args.length < 3 | args.length > 5) { // must be at least 3, no more
													// than 5 args
			printUsage();
		}
		/* * * * * * * *   checking if cache selected   * * * * * *  */
		try { // 1st arg should be an int
			cache = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.err.printf("First argument must be a number.\n%s\n", e);
			printUsage();
		}
		if (cache < 0 | cache > 1) { // 1st arg must be 1 or 0
			System.err.printf("%d is an invalid entry for cache argument.\n", cache);
			printUsage();
		}
		if (cache == 0 && args.length >= 5) { // enforcing no cache size arg if
												// no cache option selected
			System.err.printf("Too many arguments entered for selecting no Cache.\n");
			printUsage();
		}
		if (cache == 0 && args.length == 4) { // if no cache and debug level
												// exists
			try {
				debugLevel = Integer.parseInt(args[3]); // get debug level
				validateDebugLevel(debugLevel);			//enforce debug level
			} catch (NumberFormatException e) {
				System.err.printf("Debug argument must be 0.\n%s\n", e);
				printUsage();
			}
		}
		if (cache == 1 && args.length < 4) { // if cache declared, but no cache
												// size
			System.err.printf("Cache size must be specified when cache requested.\n");
			printUsage();
		}
		if (cache == 1 && args.length == 4) { // if cache declared, but no debug
												// level
			debugLevel = 0;
			try {
				cacheSize = Integer.parseInt(args[3]); // get cache size
				validateCacheSize(cacheSize);			//enforce cache size
			} catch (NumberFormatException e) {
				System.err.printf("Cache size must be a number greater than 0.\n%s\n", e);
				printUsage();
			}
		}
		if (cache == 1 && args.length == 5) { // Cache & debug level specified
			try {
				cacheSize = Integer.parseInt(args[3]); // get cache size
				debugLevel = Integer.parseInt(args[4]); // get debug level
				validateCacheSize(cacheSize);
				validateDebugLevel(debugLevel);
			} catch (NumberFormatException e) {
				System.err.printf("Cache size must be a number greater than 0, and debug level must be 0.\n%s\n", e);
				printUsage();
			}
		}

		/*Makes sure the only BTree files accepted are those with our naming convention*/
		String[] fNameSplit = args[1].split(".gbk");
		if(fNameSplit[0].equals("xyz")){} //valid filename
		else {
			System.err.printf("ERROR: file name, \"%s\" is not recognized as a valid BTree file for this search program.\n", args[1]);
			printUsage();
		}
		
		/* Check to see if btree and query files have same sequence lengths. */
		Pattern pattern = Pattern.compile("[1-9]+"); // Regex pattern specified as only for numbers
														// 1-9
		Matcher matcher = pattern.matcher(args[2]); // creates a Matcher to query filename.
		int queryK = 0;  // K value (sequence length) found in given query file.
		
		// Parse out the sub-sequence length (k) in the query fileNAME
		while (matcher.find()) {
			try {
				queryK = Integer.parseInt(matcher.group()); // gets the 1 or 2 digit integer from
															// the query filename. Example, "query4" or "query16"
															// returns the "4" or "16" as an Integer
			} catch (NumberFormatException e) {
				System.err.printf("Invalid Query file name, %s, must have an integer at the end.\n%s\n", args[2], e);
				System.exit(1);
			}
		}
		
		try {
			/* Parse out the sub-sequence length (k) from the BTree fileNAME.
		       Split filename into subsStrings, storing each half in subStrings[] array.  */
			String[] subStrings = args[1].split("btree.data.");  
			if (subStrings[1].contains(".")) {  		// 't' value found at end of filename
				if(subStrings[1].charAt(1) == '.') {  	// Single digit 'k' value
					treeK = Integer.parseInt(subStrings[1].substring(0, 1));
				} else {								// Double digit 'k' value
					treeK = Integer.parseInt(subStrings[1].substring(0, 2));
				}	
			} else { // no 't' value in filename, use trailing integer for 'k'
				treeK = Integer.parseInt(subStrings[1]);
			}
		} catch (NumberFormatException e) {
			System.err.printf("Invalid BTree file name, %s, format must be \"____.gbk.btree.data.k.t\".\n%s\n", args[1], e);
			System.exit(1);
		}
		
		if (treeK != queryK) {
			System.err.println("Error - sequence size \"" + treeK + "\" of btree file does not match sequence size \""
					+ queryK + "\" of query file.");
			printUsage();
		}
	}


	/** Checks to see if the parsed debug level is valid.  Throws NumberFormatException if not.
	 * @param debugLevel :
	 * @throws NumberFormatException
	 */
	private static void validateDebugLevel(int debugLevel) throws NumberFormatException {
		if (debugLevel < 0 || debugLevel > 0) {
			throw new NumberFormatException();
		}
	}

	/** Checks to see if the parsed cache size is valid.  Throws NumberFormatException if not.
	 * @param size
	 * @throws NumberFormatException
	 */
	private static void validateCacheSize(int size) throws NumberFormatException {
		if (size <= 0) { // Bad Cache size given
			throw new NumberFormatException();
		}
	}

	/** Helper method to display usage message with invalid command line arguments.
	 */
	private static void printUsage() {
		System.out.println(
				"Usage: java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
		System.exit(1);
	}
	
}
