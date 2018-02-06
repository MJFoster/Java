import java.util.LinkedList;
import java.util.NoSuchElementException;

/*
 * Creates a single cache storage of generic
 * objects.  Inherits from the LinkedList class.
 * 
 * @version  Fall 2016
 * @author MaryJo Foster
 */
public class Cache <T> extends LinkedList <T> {
	private int hits;
	private int refs;
	private final int MAXSIZE;
	private LinkedList <T> cache;

	
	/*
	* Constructor for Cache instance.
	*/
	public Cache (int size) {
		hits = 0;
		refs = 0;
		MAXSIZE = size;   // 'final' constant, initialized once.
		cache = new LinkedList <T>();
	}

	
	/*
	 * Searches Cache for the given object.
	 * 
	 * @param - Object to find.
	 * @return - if item found, it is returned.  Otherwise, null returned.
	 */
	public T getObject(T item) {
		int index = cache.indexOf(item);
		refs++;
		if(index == -1)
			return null;
		else {
			hits++;
			return(cache.get(index));
		}
	}
	
	
	/*
	 * Adds an object to the front of this Cache.
	 * @param - Object to be added.
	 */
	public void addObject(T item) {
	 	// If cache full, remove last item before adding.
		try {
			if(cache.size() == MAXSIZE) {
				cache.removeLast();
			}
		}
		catch(NoSuchElementException e) {
			System.err.println("Cache.addObject() - Can't removeLast() on empty cache. " + e);
			System.exit(1);
		}
		cache.addFirst(item);
	}
	
	/*
	 * Assumption is made that the given item is already
	 * in Cache, this method removes it or throws an exception
	 * if the element is not currently in the Cache.
	 * 
	 * @param - Object to be removed.
	 * @throws - NoSuchElementException when given element not in Cache.
	 */
	public void removeObject(T item) throws NoSuchElementException {
		if(!cache.remove(item))
			throw new NoSuchElementException("Cache.removeObject() - can't remove item, item not found in cache.");
	}
	
	
	/*
	 * Moves the given object from it's current location to 
	 * the front of the Cache.
	 * 
	 *  @param - Object to be moved
	 */
	public void moveToFront(T item) throws NoSuchElementException {
		removeObject(item); // throws NoSuchElementException if item not in cache.
		addObject(item);
	}
	
	
	/*
	 * Removes all objects from Cache, leaving it empty. 
	 */
	public void clearCache() {
		cache.clear();
	}
	
	
	/*
	 * Getter for 'hits' instance variable.
	 * 
	 * @return - current number of hits on this cache.
	 *           'hits' represents total times a successful search
	 *           for an item in the cache occurs.
	*/
	public int getHits() {
		return hits;
	}
	
	
	/*
	 * Getter for 'refs' instance variable.
	 * 
	 * @return - current number of refs on this cache.
	 *           'refs' represents total queries to the cache.
	*/
	public int getRefs() {
		return refs;
	}
	
	
	/*
	 * Getter for current size of Cache.
	 * @return - Current size of Cache.
	 */
	public int getSize() {
		return cache.size();
	}
	
	/*
	 * Getter for maximum capacity of Cache.
	 * @return - maximum capacity of Cache.
	 */
	public int getMaxSize() {
		return MAXSIZE;
	}
	
	
	/*
	 * Generate a String of current state of Cache
	 */
	public String toString() {
		String cacheStr = "Current State of Cache:\n";
		cacheStr += cache.toString();
		return cacheStr;
	}
	
	
}