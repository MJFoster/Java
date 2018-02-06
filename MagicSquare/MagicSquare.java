/**
 * Driver class to validate or create a "Magic Square" read from or written to 
 * a text file.  A "Magic Square" is a 2D grid of integers that has the same 
 * number of rows as columns.  A "Magic Square" is validated by meeting the 
 * following criteria:
 *  	+ Values must be all integers 1-n inclusively where 'n' is the product of
 *        the grid dimensions. Example: If the "Magic Square" is a 5x5, values that
 *        must be present in the grid are all integers 1-25 inclusively.
 *      + Values must all exist exactly once, no duplicates.
 *      + Every row, column, and both diagonals must add up the the same value
 *        determined by the following formula:   (n*(n^2 + 1)) / 2
 *        
 * @param args[] array of command line arguments.
 * @version Summer 2016
 * @author MaryJo Foster
 *
 */
public class MagicSquare {

	public static void main(String[] args) {
		int dim;  // dimension of Magic Square
		if (args.length < 2) {
			invalidUsage("\"-check filename\" or \"-create filename size\"  must be specified on the command line.");  // not enough arguments issued
		} else if (args[0].equals("-check")) {
			MagicSquareUtilities.checkMagicSquare(args[1]);	
			// Code to expedite testing and iterate through multiple files in one run.
			// for(int i = 1; i < args.length; i++) {
			// 	MagicSquareUtilities.checkMagicSquare(args[i]);				
		} else if (args[0].equals("-create")) {
			if (args.length < 3)
				invalidUsage("MagicSquare size must be specified.");	
			else {
				dim = Integer.parseInt(args[2]);
				if((dim%2 == 0)  || (dim < 3)) {  // Invalid Magic Square dimensions
					String msg = "MagicSquare dimensions must be odd, " + dim + " is invalid."; 
					invalidUsage(msg);
				} else
					MagicSquareUtilities.createMagicSquare(args[1], args[2]);
			}
		} else {
			invalidUsage("Invalid command line argument: ".concat(args[0]));
		}
	}


	/**
	 * Helper method to display a usage message when command line arguments are
	 * invalid.  System exits after messages displayed.
	 * 
	 * @param msg  String  Additional error message displayed on console. Empty string ok.
	 */
	public static void invalidUsage(String msg) {
		System.out.println("Usage: java MagicSquare [-check | -create] [filename] [ | ODD square size]\n");
		if (!msg.isEmpty())
			System.out.println(msg);
		System.exit(1);  // Exit with error
	}
	
}
