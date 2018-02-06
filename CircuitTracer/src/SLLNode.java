/**
 * Simple Node class for single linked list 
 * @author Matt T
 *
 * @param <T> generic type of elements stored in queue 
 */
public class SLLNode<T>
{
	private SLLNode<T> next;		// reference to next node
	private T element;			// reference to element stored at node 
	
	/**
	 * Constructor - with given element 
	 * @param element
	 */
	public SLLNode(T element)
	{
		setElement(element);
		setNext(null);
	}

	/**
	 * Returns reference to next node
	 * @return reference to next node 
	 */
	public SLLNode<T> getNext()
	{
		return next;
	}

	/**
	 * Assign reference to next node 
	 * @param next - reference to next node 
	 */
	public void setNext(SLLNode<T> next)
	{
		this.next = next;
	}

	/**
	 * Returns reference to node stored in node 
	 * @return reference to element stored in node 
	 */
	public T getElement()
	{
		return element;
	}

	/**
	 * Sets reference to element stored at node
	 * @param element - reference to node stored in node 
	 */
	public void setElement(T element)
	{
		this.element = element;
	}
	
	
}
