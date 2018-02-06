import java.util.EmptyStackException;

/**
 * Simple stack class that implements IStack interface. 
 * @author Matt T
 * @author MaryJo Foster
 * @version summer 2016
 *
 * @param <T> - type of objects stored in the stack 
 */
public class Stack<T> implements IStack<T>
{
	private int size;	// number of elements in Stack 
	private int capacity; 
	private T[] stack;
	
	/**
	 * Default constructor 
	 */
	@SuppressWarnings("unchecked")
	public Stack()
	{
		size = 0;
		capacity = 10; 
		stack = (T[])(new Object[capacity]); 
	}
	
	
	/**
	 * Expand the capacity of stack when full.
	 * 
	 * @author MaryJo Foster
	 * @version Summer 2016
	 */
	public void expandCapacity()
	{
		@SuppressWarnings("unchecked")
		T[] newStack = (T[])(new Object[capacity*2]);
		
		for(int i = 0; i < this.size(); i++)
		{
			// copy current stack to newStack.
			newStack[i] = stack[i];
		}
		
		capacity *= 2;
		stack = newStack;
	}
	
	
	@Override
	/**
	 * Before pushing a new object onto the stack,
	 * it's capacity is checked and expanded if necessary.
	 * 
	 * @param value - Item being added to storage
	 * @version Summer 2016
	 * @author MaryJo Foster
	 */
	public void push(T value)
	{
		if(size == capacity)
			expandCapacity();
		stack[size] = value; 
		size++;
	}

	@Override
	public T pop()
	{
		T value = null;
		if(isEmpty())
		{
			throw new EmptyStackException();
		}
		size--;
		value = stack[size]; 
		stack[size] = null;
		
		return value;
	}

	@Override
	public T peek()
	{
		T value = null;
		if(isEmpty())
		{
			throw new EmptyStackException();
		}
		value = stack[size - 1]; 
		
		return value;
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public boolean isEmpty()
	{
		if(size == 0)
			return true;
		else
			return false;
	}

}
