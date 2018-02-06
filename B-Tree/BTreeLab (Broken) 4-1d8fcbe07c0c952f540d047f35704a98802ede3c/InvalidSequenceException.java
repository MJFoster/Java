/*
 * Exception for invalid sequence given to SequenceReader constructor.
  * CS 321-02
 * 
 * @author Ryleigh Moore
 * @author Joe Bell
 * @author MaryJo Foster
 * @version Fall 2016
 */
@SuppressWarnings("serial")
public class InvalidSequenceException extends RuntimeException {
	
	public InvalidSequenceException(String msg)
	{
		super (msg + "\nSequence being parced is either null or has invalid sub-sequence length specified.\n");
	}
}
