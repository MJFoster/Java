import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utility class providing static helper methods to class MagicSquare.
 * Methods included provide functionality to:
 *   - Create Magic Squares of odd dimensions.
 *   - Check or validate any Square to be a Magic Square.
 *   - Handle all i/o operations for reading and writing files.
 *   - Display a square on the console.
 *   - Miscellaneous helper methods.
 *        
 * @version Summer 2016
 * @author MaryJo Foster
 *
 */
public class MagicSquareUtilities {
	static int[][] magicSquare;
	static boolean isValid = true;
	
	/*
	 * Utility method to read a text file, then validate it as a "Magic Square".
	 * If a "Magic Square" exists, a validation message will be displayed on 
	 * the console.
	 * 
	 * @param  filename of file to read and validate as a "Magic Square".
	 * 
	 */
	public static void checkMagicSquare(String filename){
		Scanner fScanner = null;
		
		fScanner = initFileScanner(filename);
		loadMagicSquare(fScanner, filename);
		displayMagicSquare();  // validates 1st, then displays
	}
	
	
/**
	 * Utility method to create a "Magic Square" of given odd size, and 
	 * write it to a text file. Creates only "Magic Square" of odd dimensional
	 * sizes. Attempts to create even dimensional size square results in
	 * a usage message and exection stops.
	 * 
	 * Format of text file is as follows:
	 * 1st row: 	 Integer value representing the square's dimension (rows & columns).
	 * 2nd - n rows: Integer values to be stored in each cell of the "Magic Square"
	 *               with white space between columns.
	 * 
	 * @param filename  name of disk file to store the Magic Square in.
	 * @param size      dimensions of row/column of the Magic Square to create.
	 */
	public static void createMagicSquare(String filename, String size) {
		
		PrintWriter pWriter;
		int row,col, oldRow, oldCol;
		int dim = Integer.parseInt(size);
		
		// Check for invalid Magic Square dimensions, exit(1) status.
		if((dim%2 == 0)  || (dim < 3)) {  
			String msg = "MagicSquare dimensions must be odd, " + dim + " is invalid."; 
			MagicSquare.invalidUsage(msg);  // Helper method called here.
		}
		
		// load magicSquare
		magicSquare = new int[dim][dim];
		row = dim - 1;
		col = dim / 2;
		for(int i = 1; i <= Math.pow(dim, 2); i++) {
			magicSquare[row][col] = i;
			oldRow = row;
			row++;
			oldCol = col;
			col++;
			if(row == dim)
				row = 0;
			if(col == dim)
				col = 0;
			if(magicSquare[row][col] != 0) {
				row = oldRow;
				col = oldCol;
				row--;
			}
		}
		
		// Write magicSquare to file
		pWriter = initPrintWriter(filename);  // handles checked exceptions
		pWriter.printf("%d\n", dim);   // 1st line is magicSquare dimension
		for(row = 0; row < magicSquare.length; row++) {
			for(col = 0; col < magicSquare.length; col++) {
				pWriter.print(magicSquare[row][col] + " ");
			};
			pWriter.print("\n");
		};
		pWriter.close();
	}

	
	/**
	 * Initializes file scanner and catches FileNotFoundExceptions
	 * 
	 * @param inputFile		file to be scanned.
	 * @return Scanner 		scanner for given filename.
	 */
	private static Scanner initFileScanner(String inputFile) {
		Scanner fScan = null;
		try {
			fScan = new Scanner(new File(inputFile));
		}
		catch (FileNotFoundException fnfe) {
			System.out.println(inputFile + ": " + fnfe);
			System.exit(1);
		};
		return fScan;
	}
	
	
	/**
	 * Loads a 2D array with data from a given file scanner of specific format.
	 * File format checked for non-number data, plus too many/few columns/rows.
	 * All checked exceptions thrown from i/o operations are handled in this method. 
	 * 
	 * @param fScan  Scanner initialized to scan a specific text file.
	 * @param fname  file to be scanned and stored in magicSquare array.
	 * 
	 */
	private static void loadMagicSquare(Scanner fScan, String fname) {
		Scanner intScan = null;
		int squareDim = 0;
		
		try {
			squareDim = Integer.parseInt(fScan.nextLine());
			magicSquare = new int[squareDim][squareDim];
			
			if(fScan.hasNext()) {
				for(int row = 0; row < squareDim; row++) {
					intScan = new Scanner(fScan.nextLine());
					for(int col = 0; col < squareDim; col++) {
						if(intScan.hasNextInt()) {
							magicSquare[row][col] = intScan.nextInt();
						}
						else { // too few columns
							handleInputMismatch(intScan);						
						}
					};
					if(intScan.hasNextInt()) { // // too many columns
						handleInputMismatch(intScan);	
					}
				};
				if(fScan.hasNext()) { // too many rows
					handleInputMismatch(fScan);
				}
			} else { // too few rows
				handleInputMismatch(fScan);
			}

			intScan.close();
			fScan.close();
		}		
		catch (NumberFormatException nfe) {
			System.out.println(fname + ": " + nfe + ": For input string \"X\"\nINVALID\nNON-Integer Data found.");
			System.exit(1);
		}
		catch (InputMismatchException imme) {
			System.out.println(fname + ": " + imme + ": Incomplete Square.\nINVALID\nDimensions don't match data.");
			System.exit(1);
		}
		catch (Exception e) {
			System.out.println(fname + ": " + e + ": Miscellaneous Exception caught\nINVALID");
			System.exit(1);
		}
	}
	
	
	/**
	 * Utility method to close scanners then throw InputMismatchException.
	 * Used for checking formats of text file containing magicSquare.
	 * 
	 * @param scan  					scanner to close
	 * @throws InputMismatchException  	for too many/few columns/rows in text file.
	 */
	private static void handleInputMismatch(Scanner scan) throws InputMismatchException {
		scan.close();
		throw new InputMismatchException();			
	}
	
	
	/**
	 * Checks the given 2D integer array to determine if it's an
	 * actual MagicSquare.
	 * 
	 * A MagicSquare must meet the following conditions:
	 *  - All integers from 1 to the total number of cells in the square
	 *    must be found in the square, without duplicates.
	 *  - For a square of dimension "n x n", the sum of values of each row,
	 *    column, and diagonal must add up the same total based on the
	 *    following formula:
	 *		total = (n * ((n*n) + 1)) / 2
	 *
	 * Static class variable "isValid" updated to reflect magicSquare state.
	 * 
	 */
	private static void validateMagicSquare() {

		int row, col;
		
		isValid = true;
		boolean diagsChecked = false;
		boolean intsChecked = false;
		
		// validate all rows
		row = 0;
		while (isValid && (row < magicSquare.length)) {
			validateMSRowColumn("row", row);
			row ++;
		};
		
		// validate all columns
		col = 0;
		while (isValid && (col < magicSquare.length)) {
			validateMSRowColumn("col", col);
			col++;
		};
		
		// validate all diagonals
		while (isValid  && !diagsChecked) {
			diagsChecked = validateMSDiagonals();
		};
		
		// validate for all integers existing once in square.
		while(isValid && !intsChecked) {
			intsChecked = validateMSIntegers();
		};
	}


	/**
	 * Validate whether a given row/column adds up to the correct sum.
	 * 
	 * If n = magicSquare dimension.
	 * 		Sum = (n * ((n*n) + 1)) / 2
	 *  
	 * Static class variable "isValid" updated to reflect magicSquare state.
	 * 
	 * @param rowCol  String specifying if a row or column is to be validated
	 * @param index   row/column in magicSquare to verify
	 * 
	 */
	private static void validateMSRowColumn(String rowCol, int index) {		
		int col, row, sum;
		
		int len = magicSquare.length;
		int validSum = (int) (len * (Math.pow(len, 2) + 1)) / 2;
		
		col = 0;
		row = 0;
		sum = 0;
		if(rowCol.equals("row")) {
			while(isValid  &&  (col < len)) {
				sum += magicSquare[index][col];
				if(sum > validSum) {
					isValid = false;
				}
				col++;
			}
		} else if(rowCol.equals("col")) {
			while(isValid  &&  (row < len)) {
				sum += magicSquare[row][index];
				if(sum > validSum) {
					isValid = false;
				}
				row++;
			}
		};
		if (sum != validSum) {
			isValid = false;
		}
	}

	
	/**
	 * Validate diagonals of a magicSquare add up to the correct sum.
	 * 
	 * If n = magicSquare dimension.
	 * 		Sum = (n * ((n*n) + 1)) / 2
	 * 
	 * Static class variable "isValid" updated to reflect magicSquare state.
	 * 
	 * @return boolean  Completion status after checking diagonals
	 */
	private static boolean validateMSDiagonals() {
		int col, row, sum;
		
		int len = magicSquare.length;
		int validSum = (int) (len * (Math.pow(len, 2) + 1)) / 2;
		
		col = 0;
		row = 0;
		sum = 0;
		while(isValid  &&  (col < len)  &&  (row < len)) {
			sum += magicSquare[row][col];
			if(sum > validSum)
				isValid = false;
			row++;
			col++;
		};
		if (sum != validSum)
			isValid = false;
		
		return true;  // Validation completed.
	}
	
	
	/**
	 * Validates that all integers from 1-n inclusively exist in the
	 * magicSquare.  'n' is the product of the magicSquare dimensions.
	 * 
	 * Static class variable "isValid" updated to reflect magicSquare state.
	 * 
	 * @return  boolean  Completion status after checking diagonals
	 */
	private static boolean validateMSIntegers() {
		int len = magicSquare.length;
		boolean found;
		
		for(int i = 1; i <= Math.pow(len, 2); i++) {
			found = false;
			for(int row = 0; (row < len) && !found; row++) {	
				for(int col = 0; (col < len) && !found; col++) {
					if(magicSquare[row][col] == i) {
						found = true;
					}
				}
			};
			if(!found){
				isValid = false;
				break;  // exit if any expected value not found.
			}
		}
		return true;  // Validation completed.
	}

	
	/**
	 * 
	 * Validates magicSquare, then displays at the console the 
	 * magic square being checked along with a message stating 
	 * it's state.
	 * 
	 */
	private static void displayMagicSquare() {
		validateMagicSquare();
		System.out.println("The matrix:\n");
		for(int row = 0; row < magicSquare.length; row++) {
			System.out.print("\t");
			for(int col = 0; col < magicSquare.length; col++) {
				System.out.print(magicSquare[row][col] + " ");
			}
			System.out.println("");
		};
		
		if(isValid)
			System.out.println("\nis a Magic Square.");
		else
			System.out.println("\nis not a Magic Square.");
	}
		
	
	/**
	 * Initializes Print Writer and catches checked Exceptions.
	 * 
	 * 
	 * @param filename of file to write to.
	 * @return PrintWriter  output stream object to write data to text file.
	 */
	private static PrintWriter initPrintWriter(String filename) {
		PrintWriter pWrite = null;
		
		try{
			File file = new File(filename);
			pWrite = new PrintWriter(new FileWriter(file));
			// Leave pWrite open for use in caller methods.
		} catch(NullPointerException npe) {
			System.out.println(filename + ": " + npe + ": Can't write to file, bad pathname.\nINVALID");
			System.exit(1);
		} catch(IOException ioe) {
			System.out.println(filename + ": " + ioe + ": Can't write to file, inaccessible.\nINVALID");
			System.exit(1);
		} catch(Exception e) {
			System.out.println(filename + ": " + e + ": Miscellaneous Exception caught\nINVALID");
			System.exit(1);
		};
		
		return pWrite;
	}

}
