/*
 * Exception for invalid subString length given to SubStringConverter constructor.
 * 
 * CS 321-02
 * 
 * @author Ryleigh Moore
 * @author Joe Bell
 * @author MaryJo Foster
 * @version Fall 2016
 */
@SuppressWarnings("serial")
public class InvalidSubSequenceLengthException extends RuntimeException {

	public InvalidSubSequenceLengthException(String msg)
	{
		super (msg + "\nSubStringConverter CONSTRUCTOR: Sub-sequence length: %d, must be 1-31 inclusive.\n");
	}
}
