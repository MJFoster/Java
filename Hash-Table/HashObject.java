import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardUpLeftHandler;

public class HashObject <T>
{

	private T obj;		// Generic object
	private int linearDuplicates;		// # duplicates in linear probed table
	private int doubleDuplicates;		// # duplicates in double hashed table
	private int linearProbes;	// # linear probed HashTable probes to insert or search
	private int doubleProbes;	// # double hashed HashTable probes to insert or search	

	/*
	 * Constructor
	 */
	public HashObject (T obj)
	{
		this.obj =  obj;
		linearDuplicates = 0;
		doubleDuplicates = 0;
		linearProbes = 0;
		doubleProbes = 0;		
	}
	
	
	/*
	 * Getter for generic object 'hashObj'
	 * 
	 * @return object stored in HashObject instance
	 */
	public T getObj() {
		
		return this.obj;
	}


	/*
	 * Getter for total number of probes based on the type of 
	 * HashTable being probed.
	 * 
	 * @return total number of probes for initial insertion.
	 */
	public int getProbes(String tblType)
	{
		if(tblType == "linear")
			return linearProbes;
		else
			return doubleProbes;
	}


	/*
	 * Getter for duplicates counter.
	 * 
	 * @return int - duplicates count.
	 */
	public int getDups(String tblType)
	{
		if(tblType == "linear")
			return linearDuplicates;
		else
			return doubleDuplicates;
	}


	/*
	 * @return key - A positive value representing a unique identifier
	 *               for this object.  Used in HashTable functions.
	 */
	public long getKey()
	{
		long key = 0;

		if(this.obj instanceof String)
		{
			key = obj.hashCode();
		}
		else if(this.obj instanceof Integer)
		{
			key = ( (Integer) this.obj).intValue();
		}
		else if(this.obj instanceof Long)
		{
			key = ( (Long) this.obj).longValue();
		}
		
		if (key < 0.0 )
			key = key * (long) -1;
		
		return key;

	}


	/*
	 * Increments total # probes based on the type of 
	 * HashTable being probed.
	 * 
	 * @params tblType - HashTable type, "linear" or "double"
	 */
	public void addProbe(String tblType, int probCount) {
		if(tblType == "linear")
			linearProbes += probCount;
		else
			doubleProbes += probCount;
	}


	/*
	 * Increments duplicates counter of this HashObject
	 */
	public void incrementDups(String tblType)
	{
		if(tblType == "linear")
			linearDuplicates++;
		else
			doubleDuplicates++;
	}
	
	
	@SuppressWarnings("unused")
	/*
	 * Determines if a generic object is equal to this object by checking
	 * hashCode().  If objects are String objects, compare the actual String
	 * objects themselves to determine equality.
	 * 
	 * @params currObj Generic <T> object to compare against this object
	 * @Override  - Overrides the Object.equals() method
	 */
	public boolean equals(HashObject <T> currObj)
	{
		return this.obj.equals(currObj.getObj());
	}


	/*
	 * @return String of this object.
	 * 
	 * @return string representation of the generic object
	 * this HashObject contains.
	 */
	public String toString()
	{
		 return obj.toString();
	}
	
}
