/**
 * RuntimeException sub-class for empty queues. 
 * 
 * @author Matt T
 */
public class EmptyQueueException extends RuntimeException
{
	private static final long serialVersionUID = -6673687609785459515L;

	public EmptyQueueException(String msg)
	{
		super(msg);
	}
}
