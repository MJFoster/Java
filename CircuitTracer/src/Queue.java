/**
 * Simple queue class that implements the IQueue interface. 
 * @author Matt T 
 *
 * @param <T> - type of objects stored in the queue
 */
public class Queue<T> implements IQueue<T>
{
	SLLNode<T> head, tail;
	int size; 	// number of elements in the Queue
	
	/**
	 * Default constructor. 
	 */
	public Queue()
	{
		head = null;
		tail = null;
		size = 0;
	}

	@Override
	public void enqueue(T value)
	{
		SLLNode<T> newNode = new SLLNode<T>(value);
		
		if(isEmpty())
		{
			head = newNode;
		}
		else
		{
			tail.setNext(newNode);
		}
		
		tail = newNode;
		size++;
	}

	@Override
	public T dequeue()
	{
		if(isEmpty())
		{
			throw new EmptyQueueException("Queue - front"); 
		}
		
		SLLNode<T> temp = head;
		head = head.getNext();
		temp.setNext(null);
		size--;
		
		if(isEmpty())
		{
			tail = null;
		}
		
		return temp.getElement();
	}

	@Override
	public T front()
	{
		if(isEmpty())
		{
			throw new EmptyQueueException("Queue - front"); 
		}
		
		return head.getElement();
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public boolean isEmpty()
	{
		return (size== 0);
	}
	
	
}
