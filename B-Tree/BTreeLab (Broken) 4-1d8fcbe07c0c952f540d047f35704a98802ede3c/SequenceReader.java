import java.util.ArrayList;

/*
 * Reads a given String sequence, and parses out a sub-sequence
 * String of given length.
 *
 * CS 321-02
 * 
 * @author Ryleigh Moore
 * @author Joe Bell
 * @author MaryJo Foster
 * @version Fall 2016
 */
public class SequenceReader {

	String sequence;	// Given sequence to read
	int k;				// Length of sub-sequence strings.
	int currIndex;		// Start of next un-read sequence.

	/* Constructor
	 * @param sequence - String sequence to read and parse
	 * @param subSeqLength - lenght of string to parse out.
	 * @throws InvalidSequenceException - if sequence or subSeqLength are invalid. 
	 */
	public SequenceReader(String sequence, int subSeqLength) throws InvalidSequenceException {
		if(sequence != null  && subSeqLength > 0) {
			this.sequence = sequence;
			k = subSeqLength;
			currIndex = 0;
		} else {
			throw new InvalidSequenceException("");
		}
	}


	/* Parses this instance's sequence and returns the next 
	 * valid sub-sequence of length 'k'.
	 * 
	 * @return : Next valid sub-sequence from sequence.
	 * 		   : If no valid sequences to read, return null.
	 */
	public String getSubSequence() {
		try {
			if ((currIndex + k) > sequence.length()) {	// no valid sub-sequences remaining
				return null;
			} else if ((currIndex + k) == sequence.length()) { // final sub-sequence is remainder of sequence
				currIndex++;
				return sequence.substring(currIndex-1);
			} else {									// return next sub-sequence & increment currIndex
				currIndex++;
				return sequence.substring(currIndex-1, currIndex-1+k);
			}
		} catch (IndexOutOfBoundsException e) {
			System.err.printf("SequenceReader.getSubSequence: substring() has invalid index\n%s\n", e);
			System.exit(0);
		}
		return null;
	}

}