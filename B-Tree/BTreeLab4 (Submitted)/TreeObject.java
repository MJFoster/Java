/**
 * Object to store a generic object and frequency count representing the number
 * of occurrences this object is found in it's BTree. Nodes in the BTree contain
 * TreeObject instances.
 *
 * CS 321-02
 * 
 * @author Ryleigh Moore
 * @author Joe Bell
 * @author MaryJo Foster
 * @version Fall 2016
 */
public class TreeObject {
	private Object key; // generic item used as the key value for this object
	private int frequencyCount = 0; // this object's value and a counter to
									// identify the number of times this key is
									// used.

	
	/**
	 * TreeObject constructor
	 * 
	 * @param key - object to be used as the key
	 */
	public TreeObject(Object key) {
		this.key = key;
		frequencyCount = 1;
	}

	
	/**
	 * Method increases the frequency count by 1.
	 */
	public void incFreqCount() {
		frequencyCount++;
	}

	
	/**
	 * Method sets the frequency count of the object.
	 * 
	 * @param count - the frequency count
	 */
	public void setFreqCount(int count) {
		frequencyCount = count;
	}

	
	/**
	 * Method returns the object's frequency count
	 * 
	 * @return - the frequency count
	 */
	public int getFreqCount() {
		return frequencyCount;
	}

	
	/**
	 * Method sets the value of the key for this object.
	 * 
	 * @param key - Currently written for a key of type long.
	 */
	public void setKey(long key) {
		this.key = key;
	}

	
	/**
	 * Method returns the object's key of type long.
	 * 
	 * @return - the key, type long.
	 * @throws - TypeNotPresentException when given key is not a "Long" datatype.
	 */
	public long getKey() {
		long retKey = 0;
		
		if (key instanceof Long) {
			retKey = (long) key;
		} else {
			throw new TypeNotPresentException("Long", null);
		}
		return retKey;
	}

	
	@Override
	public String toString() {
		return "" + key + " " + frequencyCount;
	}

}
