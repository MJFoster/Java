/**
 * The SubStringConverter class creates an object that can convert DNA
 * substrings (Strings) into their key (long) form or vice versa. 
 * 
 * CS 321-02
 * 
 * @author Ryleigh Moore
 * @author Joe Bell
 * @author MaryJo Foster
 * @version Fall 2016
 */
public class SubStringConverter {
	
	private final int a = 0b00; //binary value of a
	private final int t = 0b11;	//binary value of t
	private final int c = 0b01;	//binary value of c
	private final int g = 0b10;	//binary value of g
	private final int k;		//length of the substring

	/**Class constructor takes in the expected substring length.
	 * 
	 * @param subStringLength - the expected DNA substring length.
	 * @throws InvalidSubSequenceLengthException - If given length <= 0  OR >= 32
	 */
	public SubStringConverter(int subStringLength) throws InvalidSubSequenceLengthException {
		this.k = subStringLength;
		if(subStringLength <= 0  || subStringLength >= 32){
			throw new InvalidSubSequenceLengthException(null);
		}
	}
	
	/**Takes in a DNA subsequence given in string format and returns it as a unique long value.
	 * @param DNAsubSequence - String representation of the subsequence
	 * @return - long value representing given String
	 */
	public long stringToLong(String DNAsubSequence) {
		
		long dnaBinValue = 0;
		int kCopy = k;
		int charIndex = 0;
		while(kCopy > 0){									//while haven't reached end of string...
			char currentChar = Character.toUpperCase(DNAsubSequence.charAt(charIndex));				//get first character of sequence

			dnaBinValue = dnaBinValue<<2;					//shift	binary value left by 2
			switch (currentChar) {
		            case 'A':	dnaBinValue = dnaBinValue | a;	//depending on case, will "bitwise logical or" the binary
		                    break;								//values associated with the DNA base
		            case 'T':	dnaBinValue = dnaBinValue | t;	//this essentially adds those values to the
		                    break;								//right side of the binary value.	
		            case 'C':	dnaBinValue = dnaBinValue | c;
		            		break;
		            case 'G':	dnaBinValue = dnaBinValue | g;
		            		break;
			}
			charIndex++;
			kCopy--;
		}
		
		return dnaBinValue;
	}
	
	
	/**
	 * Takes in a long value representing the key value of the DNA substring and
	 * converts it into the string representation of the DNA subsequence.
	 * 
	 * @param DNAkeyValue - the long representing the DNA subsequence
	 * @return	- the string form of the DNA subsequence.
	 */
	public String longToString(long DNAkeyValue) {
		
		String retStr = "";							//return string initially empty
		int kCopy = k;								//a copy of the desired string length
		while(kCopy > 0){							//while haven't reached desired length...
			long DNAbaseL = (DNAkeyValue & 3);		//"bitwise logical and" operation is
													//performed between passed in value
													//and a mask of 0b11 i.e. 3 in base 10 
			int DNAbaseInt = (int) DNAbaseL;		//result of previous op is cast to int.
			DNAkeyValue = DNAkeyValue>>2;			//shift	passed in binary value right by 2
			switch (DNAbaseInt) {
		            case a:	retStr ="A" + retStr;	//compares the two bit binary value with those
		                    break;					// assigned to the encoding of each base.
		            case t:	retStr ="T" + retStr;	// depending on case, will add that character
		                    break;					// to the string.
		            case c:	retStr ="C" + retStr;
		            		break;
		            case g:	retStr ="G" + retStr;
		            		break;
			}
			kCopy--;
		}
		
		return retStr;
	}
	
}
