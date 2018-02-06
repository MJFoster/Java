/**
 * Interface for a simple queue class. 
 * @author Matt T
 * @version Summer 16
 * @param <T> - type of objects stored in the queue
 */
public interface IQueue<T>
{
	/**
	 * Places given value at back of a queue
	 * @param value - object of type T put at back of  a queue
	 */
	public void enqueue(T value);
	
	/**
	 * Removes value from front of a queue and returns it.
	 * @throws EmptyQueueException - if a queue is empty
	 * @return - object of type T 
	 */
	public T dequeue();

	/**
	 * Returns first value in a queue without removing it.
	 * @throws EmptyQueueException  - if a queue is empty
	 * @return - reference to object of type T at front of a queue 
	 */
	public T front();

	/**
	 * Returns number of elements in a queue
	 * @return - number of elements on a queue 
	 */
	public int size();
	
	/**
	 * Returns true if a queue has no elements
	 * @return - true if empty, false otherwise
	 */
	public boolean isEmpty();


}
