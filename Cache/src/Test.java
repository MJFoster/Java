import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Test class for Cache class.  This class will read in a text
 * file, parse out words, and add those words to either 1 or 2 
 * Cache instances.  When complete, an output string of statistics
 * will be displayed on the screen.  
 *  
 * @param array of command line arguments (see above)
 * @version  Fall 2016
 * @author MaryJo Foster
 */
public class Test {

	public static void main(String args[]) throws NumberFormatException {
		int numCache = 0;
		int cache1Size = 0;
		int cache2Size = 0;
		int totalRefs = 0;  // Total queries made, only on cache1
		int totalHits = 0;  // cache1.hits  +  cache2.hits
		double globalHR = 0.0;
		double cache1HR = 0.0;
		double cache2HR = 0.0;
		String currLine = "";
		String currWord = "";
		String filename = "";
		Scanner fileScanner = null;
		final String DELIMITERS = " \t";
		Cache <String> cache1 = null;
		Cache <String> cache2 = null;
		
		// Validate command line arguments.
		if(args.length < 3) {
			String errorMsg = "Not enough command line arguments given: Test ";
			for(String s: args) {
				errorMsg = errorMsg.concat(s).concat(" ");
			}
			exitUsageError(errorMsg);
		} else {
			try {
				// Validate cache1 command line arguments
				numCache = Integer.parseInt(args[0]);  // throws NumberFormatException
				cache1Size = Integer.parseInt(args[1]); 
				if(numCache < 1  ||  numCache > 2) {
					throw new NumberFormatException("Use 1 or 2 cache levels.");
				}
				
				// Validate cache2 command line arguments
				if(numCache == 1) {
					filename = args[2];
				} else {
					filename = args[3];
					cache2Size = Integer.parseInt(args[2]);
					if(cache1Size > cache2Size) {
						throw new NumberFormatException("cache1 must be <= cache2.");
					}
				}
				
				// Open input file
				fileScanner = new Scanner(new File(filename));
			}
			catch (NumberFormatException e) {
				exitUsageError(e.toString());
			}
			catch (FileNotFoundException e) {
				exitUsageError("Test.main() - File" + e.toString());
			}
			catch (Exception e) // Handler for any other unknown exceptions. 
			{
				System.err.println("\n" + e + ": Miscellaneous Exception caught\n");
				System.exit(1);
			}
		}
		
		// Initialize and load Cache, catching Exceptions if any.
		try {
			cache1 = new Cache <String> (cache1Size);
			if (numCache == 2) {
				cache2 = new Cache <String> (cache2Size);
			}
			
			// Parse out words from input file using StringTokenizer class.
			while(fileScanner.hasNextLine()) {
				currLine = fileScanner.nextLine().trim();
				StringTokenizer tokenizer = new StringTokenizer(currLine, DELIMITERS);
				while(tokenizer.hasMoreTokens()) {
					currWord = tokenizer.nextToken();
					if (cache1.getObject(currWord) == null) { // NOT in cache1
						cache1.addObject(currWord);
						if(numCache == 2) {
							if(cache2.getObject(currWord) == null) { // NOT in cache2
								cache2.addObject(currWord);
							} else { // cache2 hit
								cache2.moveToFront(currWord);
							}
						}		
					} else { // cache1 hit 
						cache1.moveToFront(currWord);
						if(numCache == 2) {
							cache2.moveToFront(currWord);
						}
					}
				} // end of "while"
			}
		}
		catch (NoSuchElementException e) {
			System.out.println("Element not found in input file or file is empty, please retry with new file.\n");
			System.exit(1);
		}
		finally {  // Before exit, close file Scanner for leaks.
			fileScanner.close();
		}
				
		// Output hit ratios and stats.
		String statsStr = "First level cache with " + cache1Size + " entries has been created\n";
		if(numCache == 2) {
			statsStr += "Second level cache with " + cache2Size + " entries has been created\n";
		}
		statsStr += "..............................\n";
		
		totalRefs = cache1.getRefs();  // Just cache1
		totalHits = cache1.getHits();
		if(numCache == 2) {
			totalHits += cache2.getHits();
		}
		statsStr += "Total number of references: " + totalRefs + "\n"; 
		statsStr += "Total number of cache hits: " + totalHits + "\n";
		
		globalHR = (double) totalHits / totalRefs; 
		statsStr += "The global hit ratio\t\t\t: " + globalHR + "\n";
		
		statsStr += "Number of 1st-level cache hits:  " + cache1.getHits() + "\n";
		cache1HR = (double) cache1.getHits() / cache1.getRefs();
		statsStr += "1st-level cache hit ratio\t\t: " + cache1HR + "\n";
		
		if(numCache == 2) {
			statsStr += "Number of 2st-level cache hits:  " + cache2.getHits() + "\n";
			cache2HR = (double) cache2.getHits() / cache2.getRefs();
			statsStr += "2st-level cache hit ratio\t\t: " + cache2HR + "\n";
		}
		System.out.println(statsStr);

		cache1.clearCache();
		if(numCache == 2) {
			cache2.clearCache();
		}
		
	}  // end of main()
	
	
	/*
	 * Helper method for command line validation 
	 * 
	 * @param - Optional message to append to default "Usage" message
	 */
	static void exitUsageError(String errorMsg) {
		System.err.printf("Usage: Java <1|2> <1st Level Cache Size | 2nd Level Cache Size> <input textfile name>\n%s\n\n", errorMsg);
		System.exit(1);
	}
	
}

