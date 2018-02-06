import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 *@param T  Generic placeholder representing any data type.  When instantiated,
 *          the IUDoubleLinkedList will contain objects of the data type that
 *          the list is instantiate with.
 *             
 * @author MaryJo Foster
 * @version Summer 2016
 * 
 */
public class IUDoubleLinkedList<T> implements IndexedUnorderedList<T> {

	private DLLNode<T> head;
	private DLLNode<T> tail;
	private int count;
	private int modCount;
	
	
	/**
	 * Constructor, no data, head & tail -> null
	 */
	public IUDoubleLinkedList() {
		head = null;
		tail = null;
		count = 0;
		modCount = 0;
	}

	
	@Override
	public T removeFirst() {
		if(isEmpty())
			throw new EmptyCollectionException("IUDoubleLinkedList - removeFirst()");
		
		return remove(0);  // removes element, decrements count, increments modCount
	}

	
	@Override
	public T removeLast() {
		if(isEmpty())
			throw new EmptyCollectionException("IUDoubleLinkedList - removeLast()");	
		
		return remove(size()-1);  // removes element, decrements count, increments modCount
	}

	
	@Override
	public T remove(T element) {
		int index = indexOf(element);
		if (index == -1)
			throw new ElementNotFoundException("IUDoubleLinkedList - remove: " + element);
		
		return remove(index);       // removes element at index, decrements count, increments modCount
	}

	
	@Override
	public T first() {
		if(isEmpty())
			throw new EmptyCollectionException("IUDoubleLinkedList");
		return head.getElement();
	}

	
	@Override
	public T last() {
		if(isEmpty())
			throw new EmptyCollectionException("IUDoubleLinkedList");
		return tail.getElement();
	}

	
	@Override
	public boolean contains(T target) {
		DLLNode<T> current;
		
		current = head;
		while(current != null  &&  current.getElement() != target) {
			current = current.getNext();
		}
		
		if(current == null)
			return false;
		else
			return true;
	}

	
	@Override
	public boolean isEmpty() {
		if (count == 0)
			return true;
		else
			return false;
	}

	
	@Override
	public int size() {
		return count;
	}

	
	@Override
	/**
	 * Method also synchronizes this list's Iterator 
	 * modCount with the collection's modCount
	 * to ensure the integrity of the list.
	 */ 
	public Iterator<T> iterator() {
		return (Iterator<T>) (new IUDLLIterator());
	}

	
	@Override
	public void add(int index, T element) {
		DLLNode<T> newNode;
		DLLNode<T> current;
		
		if(index < 0  ||  index > size())  // out of bounds index
			throw new IndexOutOfBoundsException("IUDoubleLinkedList - add(index,element): " + index);
		else if (index == 0) 
			addToFront(element); // adds to front, increments count & modCount
		else if (index == size())
			addToRear(element);  // adds to rear, increments count & modCount
		else {                   // adds to middle, increments count & modCount
			newNode = new DLLNode<T>(element);
			current = head;
			for(int i = 0; i < index-1; i++){
				current = current.getNext();
			}
			newNode.setNext(current.getNext());
			newNode.getNext().setPrev(newNode);
			newNode.setPrev(current);
			current.setNext(newNode);
			
			count++;
			modCount++;
		}
	}

	
	@Override
	public void set(int index, T element) {
		DLLNode<T> current;
		
		if(index < 0  ||  index >= size())
			throw new IndexOutOfBoundsException("IUDoubleLinkedList - get(index) : " + index);
		
		current = head;
		for(int i = 0; i < index; i++) {
			current = current.getNext();
		}
		
		current.setElement(element);
		modCount++;
	}

	
	@Override
	public void add(T element) {
		DLLNode<T> newNode = new DLLNode<T>(element);
		if(isEmpty()) {
			head = newNode;
			tail = newNode;
		} else {
			tail.setNext(newNode);
			newNode.setPrev(tail); // next pointer already null
			tail = newNode;
		}
		count++;
		modCount++;
	}

	
	@Override
	public T get(int index) {
		DLLNode<T> current = head;
		if(index < 0  ||  index >= size())
			throw new IndexOutOfBoundsException("IUDoubleLinkedList - get(index) : " + index);
		
		for(int i = 0; i < index; i++) {
			current = current.getNext();
		}
		return current.getElement();
	}

	
	@Override
	public int indexOf(T element) {
		int index;
		DLLNode<T> current;
		
		current = head;           // start searching from front of list
		index = 0;
		while(current != null  && current.getElement() != element) {
			current = current.getNext();
			index++;
		}
		
		if(current == null)
			return -1;
		else
			return index;
	}

	
	@Override
	/** 
	 * Removes element at "index".
	 */ 
	public T remove(int index) {
		DLLNode<T> current = head;
		T retVal = null;
		
		if(index < 0  ||  index >= size())     // out of bounds index
			throw new IndexOutOfBoundsException("IUDoubleLinkedList - remove(index): " + index);
		else if (size() == 1) { // reset head, tail.
			retVal = head.getElement();
			head = null;
			tail = null;
		} else {   					// size() >= 2 here
				for (int i = 0; i < index; i++) {  // iterate until current -> index
					current = current.getNext();
				}
				retVal = current.getElement();
				if (index == 0)	{				// remove 1st element
					head = head.getNext();
					head.setPrev(null);
					current.setNext(null);
				} else if (index == size()-1) { // remove last element
					tail = current.getPrev();
					current.setPrev(null);
					tail.setNext(null);
				} else {                        // remove middle element
					current.getPrev().setNext(current.getNext());
					current.getNext().setPrev(current.getPrev());
					current.setPrev(null);
					current.setNext(null);	
				}
		}
		count--;	
		modCount++;
		return retVal;
	}

	
	@Override
	public void addToFront(T element) {
		DLLNode<T> newNode = new DLLNode<T>(element);
		if(isEmpty()){
			head = newNode;
			tail = newNode;			
		} else {
			newNode.setNext(head);
			head.setPrev(newNode);
			head = newNode;			
		}
		
		count++;
		modCount++;
	}

	
	@Override
	public void addToRear(T element) {
		add(element);  // Adds element to rear, increments count & modCount	
	}

	
	@Override
	public void addAfter(T element, T target) {
		int index = indexOf(target);
		if (index == -1)
			throw new ElementNotFoundException("IUDoubleLinkedList : " + target);
		else {
			add(index+1,element);  // adds element, increments count & modCount
		}
	}

	
	@Override
	public ListIterator<T> listIterator() {
		return new IUDLLIterator();
	}


	// *************************  PRIVATE INNER CLASS  *************************
		
		/**
		 * Private Inner class to provide an encapsulated ListIterator
		 * for any type of IUDoubleLinkedList collection.
		 * 
		 * @author MaryJo Foster
		 * @version Summer 2016
		 *
		 */
		private class IUDLLIterator implements ListIterator<T> {
			
			private DLLNode<T> cursor;  // current position of this Iterator
			private DLLNode<T> lastReturned; // node iterated over by prev or next. Used in 'set()'
			private int itrModCount;   // Syncs w/ lists "ModCount"
			private int itrIndex;     // current index of iterator
			private boolean canRemoveNext;  // validates if next() called prior to remove()
			private boolean canRemovePrev;  // validates if previous() called prior to remove()

			
			/**
			 * Constructor for IUSLLIterator 
			 */
			public IUDLLIterator() {
				
				cursor = head;
				lastReturned = null;
				itrModCount = modCount;  // Initial sync w/ underlying 'list' modCount
				itrIndex = -1;
				canRemoveNext = false;
				canRemovePrev = false;
			}


			@Override
			public void add(T e) {   // always add before cursor
				DLLNode<T> newNode = new DLLNode<T>(e);
				if(isEmpty()) {
					tail = newNode;
					head = newNode;
					cursor = newNode;
				} else if(!hasPrevious()) { // add at front of list
					newNode.setNext(head);
					head.setPrev(newNode);
					head = newNode;
				} else if (!hasNext()){   // add to end of list
					newNode.setPrev(tail);
					tail.setNext(newNode);
					tail = newNode;
				} else {                 // add to middle
					newNode.setNext(cursor);
					newNode.setPrev(cursor.getPrev());
					newNode.getPrev().setNext(newNode);
					newNode.getNext().setPrev(newNode);
				}
				
				canRemoveNext = false;
				canRemovePrev = false;
				lastReturned = null;
				count++;
				modCount++;				
				itrModCount++;
				itrIndex++;
			}


			@Override
			/** Returns true only if elements exist in the underlying
			 *  list after the current ListIterator position. 
			 */
			public boolean hasNext() {
				if(isEmpty() || itrIndex == size() - 1 )
					return false;
				else
					return true;
			}


			@Override
			/** Returns true only if elements exist in the underlying
			 *  list before the current ListIterator position. 
			 */
			public boolean hasPrevious() {
				if(itrIndex == -1)
					return false;
				else
					return true;

			}


			@Override
			/**
			 * @throws ConcurrentModificationException - if the underlying list
			 *  has been modified without the Iterator recognizing it.
			 */
			public T next() throws ConcurrentModificationException {
				if(itrModCount != modCount)
					throw new ConcurrentModificationException("IUDLLIterator - next()");
				if(itrIndex == size()-1)
					throw new NoSuchElementException("IUDLLIterator - next()");
				else {
					lastReturned = cursor;      // save node for 'set' method
					cursor = cursor.getNext();
					itrIndex++;
					canRemoveNext = true;
					return lastReturned.getElement();				
				}
			}


			@Override
			public int nextIndex() {
				if(cursor == null)  // at end of list
					return size();
				else
					return itrIndex + 1;
			}


			@Override
			/**
			 * @throws ConcurrentModificationException - if the underlying list
			 *               has been modified without the Iterator recognizing it.
			 */
			public T previous() throws ConcurrentModificationException {

				if(itrModCount != modCount)
					throw new ConcurrentModificationException("IUDLLIterator - next()");
				
				if (itrIndex == -1)  // Empty list OR ListIterator sitting BEFORE 1st element
					throw new NoSuchElementException("IUDLLIterator - next()");						
				else if (itrIndex == 0  &&  !hasNext()){
					// ListIterator sitting AFTER 1st element in single element list
					cursor = tail;
					lastReturned = cursor;
				} else if (cursor == null  && itrIndex == size()-1) {
					// ListIterator sitting AFTER last element
					cursor = tail;
					lastReturned = cursor;
				} else {
					// ListIterator sitting between 2 elements
					lastReturned = cursor.getPrev();
					cursor = cursor.getPrev();
				}								
				itrIndex--;
				canRemovePrev = true;
				return lastReturned.getElement();
			}


			@Override
			public int previousIndex() {
				return itrIndex;
			}


			@Override
			public void remove() {  
				if(canRemoveNext  ||  canRemovePrev) { // empty lists are caught here also
					// remove from list size of 1
					if (size() == 1) {
						head = null;
						tail = null;
						cursor = head;
					}
					else  {  // list length >= 2)
						if(canRemoveNext  &&  (cursor == head.getNext())) {
							// remove FRONT after next()
							head.setNext(null);
							cursor.setPrev(null);
							head = cursor;
						} else if (canRemovePrev  &&  (cursor == head)) { 
							// remove FRONT after previous()
							head = head.getNext();  
							head.setPrev(null);
							cursor.setNext(null);
							cursor = head;
						} else if ((canRemoveNext && (cursor == null  &&  itrIndex == size()-1))  ||
						           (canRemovePrev && (cursor.getNext() == null))) { 
							// remove tail after next() or previous()
							cursor = tail.getPrev();
							cursor.setNext(null);
							tail.setPrev(null);
							tail = cursor;
						} else {  
							// remove middle after next() or previous()
							DLLNode<T> delNode = cursor.getPrev();
							delNode.getPrev().setNext(delNode.getNext());
							cursor.setPrev(delNode.getPrev());
							delNode.setPrev(null);
							delNode.setNext(null);	
						}
					} 
					
				} else  // neither next() nor previous() have been called, or list is empty
					throw new IllegalStateException("IUDLLIterator - remove()");
				
				canRemoveNext = false;
				canRemovePrev = false;
				lastReturned = null;
				count--;
				modCount++;
				itrModCount++;
				itrIndex--;
			}


			@Override
			public void set(T e) {
				if(lastReturned == null)
					throw new IllegalStateException("IUDLLIterator - set()");
				else {
					lastReturned.setElement(e);
				}
					
				
			}
		}

}
