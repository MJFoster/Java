
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/** Read in gbk file and create the BTree.
 *
 * CS 321-02
 *
 * @author Ryleigh Moore
 * @author Joe Bell
 * @author MaryJo Foster
 * @version Fall 2016
 */
public class GeneBankCreateBTree {

	private static int debugLevel = 0;			// Given debug level requested.
	private static int cacheSize = 0;			// Given size of cache requested.
	private static Scanner fileScanner = null;	// Scanner to be read given file.
	private static int k = 0;					// Given sub-sequence String length.
	private static int degreeT = 0;				// Given degree for BTree.


	@SuppressWarnings("unused")
	public static void main(String args[]) {
		long startTime = System.currentTimeMillis();
		ArrayList<String> finalSequences = null;	// List of all SEQUENCES in given input file.
		String subSequence = null;					// SUB-sequence returned from sequenceReader.
		SequenceReader seqReader = null;  			// Reads a SUB-sequence from a sequence String.
		SubStringConverter converter = null;		// Converts sub-sequences to long keys, and visa-versa.
		BTree btree = null;
		String dumpFile = "dump"; 					// Name of text output file.
		FileWriter outputFile;						// FileWriter for output file.
		
		try {
			validateCommandLineArgs(args);
			finalSequences = parseOutSequences();
			btree = new BTree(degreeT, k, cacheSize);	// Instantiate new empty BTree
			converter = new SubStringConverter(k);

			//For each finalSequence, parse out a subsequence String, and insert it into the BTree
			for(int i = 0; i < finalSequences.size(); i++) { // Repeat for each sequence
				seqReader = new SequenceReader(finalSequences.get(i), k);  // Parses out sub-sequences
				subSequence = seqReader.getSubSequence();

				while(subSequence != null) {  // Repeat until all valid subSequences are parsed
					long key = converter.stringToLong(subSequence);
					TreeObject obj = new TreeObject(key);
					btree.insert(obj);
					subSequence = seqReader.getSubSequence();
				}
			}
			
			// If debug enabled, generate dump file
        	if(debugLevel == 1) {
    			outputFile = new FileWriter(dumpFile);
        		btree.inOrderTraverse(btree.getNodeFromDisk(btree.getRootOffset()),0, outputFile);
        		outputFile.close();
        	}
			
		} catch (NumberFormatException e) {
			System.err.printf("Numeric command Line aruments must be numbers.\n%s\n", e);
			printUsage();
			System.exit(0);
		} catch (FileNotFoundException e) {
			System.err.printf("File specified not found.\n%s\n", e);
			printUsage();
			System.exit(0);
		} catch (IllegalStateException e) {
			System.err.printf("Scanner not open to read file.\n%s\n", e);
			System.exit(0);
		} catch (NegativeArraySizeException e) {
			System.err.printf("Scanner found empty file.\n%s\n", e);
		} catch (InvalidSubSequenceLengthException e) {
			System.err.printf("GeneBankCreateBTree.main.\n%s\n", e);
		} catch (InvalidSequenceException e) {
			System.err.printf("GeneBankCreateBTree.main.\n%s\n", e);
		} catch (Exception e) {
			System.err.printf("Unknown Exception:\n%s\n", e);
			printUsage();
			System.exit(0);
		} finally {
			fileScanner.close();
			//btree.closefile();
		}
//		btree.printMeta();
//		for(int i=16;i<=99600;i+=224)
//			System.out.println(btree.getNodeFromDisk(i));
//		long endTime   = System.currentTimeMillis();
//		long totalTime = endTime - startTime;
//		System.out.println(totalTime);

	}


	/** Helper method to create an ArrayList of sequences that
	 * consist of only alphabetic characters found between
	 * "ORIGIN" and "//" or any series of 'n' characters in the file.
	 * There may be more than one sequence in the file.
	 * Method assumes a scanner is already initialized to read a 
	 * valid file specified from the command line.
	 * 
	 * @returns ArrayList<String> - Sequences parsed from input file.
	 * @throws IllegalStateException, NegativeArraySizeException
	 */
	public static ArrayList<String> parseOutSequences()
			throws IllegalStateException, NegativeArraySizeException {

		ArrayList<String> origintoBackslashSequences = new ArrayList<String>();
		ArrayList<String> finalSequences = new ArrayList<String>();
		String[] splitSequences = null;	// Temp array for storing sequences split at 'n'
//		int k;							// Length of sub-sequence strings. 
		String onLine = "";				// Current line
		String keep = "";				// Current VALID character sequence
		String token = null;			// Current whitespace delimited string read by scanner.
		String str = null;				// Temp string used in parsing.    	

		/* Read file until EOF, storing each sequence into
		 * an ArrayList. At this stage, if 'n' character(s) found,
		 * a single 'n' delimeters will be included in that sequence.*/
		while(fileScanner.hasNextLine()) {
			keep = "";
			onLine = fileScanner.nextLine();
			if (onLine.toLowerCase().trim().equals("origin")) {  // Sequence found
				token = fileScanner.nextLine().trim();
				while(!token.equals("//")) {
					token = token.replaceAll("[^A-Za-z]+", ""); // Retain only alphabetic characters
					token = token.replaceAll("n+", "n"); // Replace all n, nn, nnn, ...n... with a single n for splitting later.
					keep += token;
					token = fileScanner.nextLine(); 
				}
				origintoBackslashSequences.add(keep); // Save the cleaned sequence in ArrayList   
			}
		}        

		//Split each sequence in ArrayList at any 'n' delimeter, and save each portion in final ArrayList. 
		for (int i = 0; i < origintoBackslashSequences.size(); i++) { // For each origin text section we found above
			str = origintoBackslashSequences.get(i); // get the next section from the arraylist
			splitSequences = new String[str.length()-1];
			splitSequences = str.split("n");	// Each sequence, split by 'n', stored in splitSequences array sequentially.
			for (int r = 0; r < splitSequences.length; r++) { //Save new split sequences in final ArrayList.
				finalSequences.add(splitSequences[r]);
			}
		}
		// All sequences in ArrayList now consist of just 'a', 'c', 't', and 'g' characters.
		return finalSequences; 
	}


	/** Helper method to validate all command line args, and throw
	 * exceptions when invalid.
	 * 
	 * @param args - Array of command Line arguments
	 * @throws NumberFormatException, FileNotFoundException
	 */
	public static void validateCommandLineArgs(String[] args) 
			throws NumberFormatException, FileNotFoundException {

		//Check args length
		if (args.length < 4 || args.length > 6) {
			System.err.println("Please use at least 4 arguments with the option of a 5th and 6th as follows: <input type> <load factor> [<debug level>].");
			printUsage();
			System.exit(0);
		}
		//Check 1st argument
		if (!args[0].equals("0") && !args[0].equals("1")) {
			System.err.println("Please make sure the first argument (input type) is 0 or 1.");
			printUsage();
			System.exit(0);
		}

		//Check 2nd argument
		if (Integer.parseInt(args[1]) < 0 || args[1].equals("1")) {
			System.err.println("Please check second argument.");
			printUsage();
			System.exit(0);
		} else {
			degreeT = Integer.parseInt(args[1]);
		}

		//Check 3rd argument
		File file = new File(args[2]);
		fileScanner = new Scanner(file);

		//Check 4th argument
		if (Integer.parseInt(args[3]) < 1 || Integer.parseInt(args[3]) > 31) {
			System.err.println("Please make sure the fourth input 1 <= k <= 31.");
			printUsage();
			System.exit(0);
		} else {
			k = Integer.parseInt(args[3]);
		}

		//Check 5th & 6th arguments
		if (args[0].equals("0")) {  // NO CACHE
			if (args.length > 5) {  // no Cache, too many args
				System.err.println("Too many arguments input for selecting no Cache.");
				printUsage();
				System.exit(0);
			} else {  // no Cache, check for legitimate debug level
				if (args.length == 5) {
					if (args[4].equals("0") || args[4].equals("1")) {
						debugLevel = Integer.parseInt(args[4]);
					} else {
						System.err.println("Debug level must be 0 or 1.");
						printUsage();
						System.exit(0);
					}
				}
			}
		} else if (args[0].equals("1")) { //  Cache is requested
			if (args.length < 5) {  // No cache size specified
				System.err.println("Cache requested, but no cache size specified.");
				printUsage();
				System.exit(0);
			} else {
				cacheSize= Integer.parseInt(args[4]);
			}

			if (args.length == 6) {
				if (args[5].equals("0") || args[5].equals("1")) {
					debugLevel = Integer.parseInt(args[5]);
				} else {
					System.err.println("Debug level must be 0 or 1.");
					printUsage();
					System.exit(0);
				}
			}
		}
	}

	
	/** Helper method to display usage message when command line argument(s) are invalid.
	 */
	private static void printUsage() {
		System.err.println("GeneBankCreateBTree <0/1(no/with Cache)> <degree 1 <= k <= 31> <gbk file> <sequence length>\n" +
				"[<cache size>] [<debug level>]\n");
	}

}
