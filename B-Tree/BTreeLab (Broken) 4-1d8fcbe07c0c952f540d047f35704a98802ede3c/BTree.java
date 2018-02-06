import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.LinkedList;

/** BTree data structure that provides fast access to generic data objects
 * that are stored in BTreeNode objects.  BTreeNode is an inner class.
 *
 * CS 321-02
 * 
 * @author Ryleigh Moore
 * @author Joe Bell
 * @author MaryJo Foster
 * @version Fall 2016
 */
public class BTree {

	private int degreeT;            // Degree of BTree "t"
	private int seqLengthK;      	// Sequence length "k"
	private int nodeSize;			//the number of bytes a node spans
	private final int BLOCKSIZE = 4096;
	private final int META = 8;			//the size of the meta data for each node(in bytes)
	private final int PTR = 4;			//the size of the pointers(in bytes)
	private final int OBJ = 12;			//the size of the object(in bytes)
	private int rootOffset;			//root offset  ..(not currently used...will need to be written last as root can change)
	private long nextWriteIndex;	//next index location to write to
	private BTreeNode rootNode;		//pointer to current root node
	private BTreeNode childNode;	//temp node holder/pointer
	private BTreeNode leftChildNode;	//temp node holder/pointer actively used
	private BTreeNode RightChildNode;	//temp node holder/pointer actively used
	private RandomAccessFile file;		//file read/write object
	private String filePath = "xyz.gbk.btree.data.";	// Initial name of BTree file that gets appended by seqLengthK and degreeT values.
	private SubStringConverter converter = null;	// Converts DNA subSequence strings to-from long keys
	private Cache cache;
	private Boolean cacheUse = false;

	/**
	 * BTreeNode class is an inner class of the BTree class which stores the
	 * objects(keys) and pointers to other nodes
	 *
	 */
	private class BTreeNode {  // Inner class of BTree

		int n;              // CURRENT number of keys in BTreeNode
		int thisNodeOffset;	// Offset of this node...not written to file
		int parentPtr = -11;      //  Offset to parent 
		TreeObject[] treeObjects = new TreeObject[2*degreeT -1];	//this node's objects/keys
		int[] childPtrs = new int[2*degreeT];  // list of offsets that point to child nodes
		boolean leaf;		//boolean to indicate if node is a leaf


		/**BTreeNode constructor takes in parent's location and creates a new empty node.
		 */
		public BTreeNode(long writeIndex) { // Constructor
			n = 0;						//new node size set to 0
			leaf = true;				//boolean identifying node as leaf
			thisNodeOffset = (int) writeIndex; //dont like this cast could cause future problems?
			for(int i = 0; i < ((2 * degreeT) -1); i++){
				TreeObject blankTreeObj = new TreeObject((long)-1);
				treeObjects[i] = blankTreeObj;
			}
		}

		@Override
		public String toString() {
			return "BTreeNode [n:" + n + ", Idx=" + thisNodeOffset + ", treeObjects=" + Arrays.toString(treeObjects) + ", childPtrs=" + Arrays.toString(childPtrs)
			+ ", parentPtr= " +parentPtr + ", leaf=" + leaf + " thisNodeOffset= " + thisNodeOffset+ "]";
		}
	}
	
	/* * * * * *  End of BTreeNode  * * * * * */ 
	
	
	/**Method creates new empty node and determines its disk write location.
	 * @return - a new empty node
	 */
	private BTreeNode allocateNode(){
		BTreeNode newNode = new BTreeNode(nextWriteIndex);
		nextWriteIndex+=nodeSize;	//sets up the offset for the next node to be written. (allocates space)
		return newNode;
	}

	
	/**BTree Constructor
     * @param fileName - the fileName containing the binary form of the BTree
     * @param cacheSize - the size of the cache
     * @throws FileNotFoundException
     */
    public BTree(String fileName, int cacheSize) throws FileNotFoundException {  // Constructor
		file = new RandomAccessFile(fileName, "r");    //creates new RandomAccessFile object
		readTreeMetaFromDisk();
		nodeSize = calculateNodeSize();
		if (cacheSize > 0 ){
			cache = new Cache(cacheSize);
			cacheUse=true;
		}
    }
    
    
	/**BTree Constructor
	 * @param degreeT - the degree specified for the BTree
	 * @param seqLengthK - the length of DNA base strings this tree will be storing.
	 * @param cacheSize - the size of the cache
	 */
	public BTree(int degreeT, int seqLengthK, int cacheSize) {  // Constructor
		this.degreeT = degreeT;
		this.seqLengthK = seqLengthK;
		nodeSize = calculateNodeSize();
		filePath += seqLengthK + "." + degreeT;	 //name of treefile to write
		if (cacheSize > 0 ){
			cache = new Cache(cacheSize);
			cacheUse=true;
		}

			try {
				file = new RandomAccessFile(filePath, "rw");    //creates new RandomAccessFile object
				writeTreeMetaToDisk();    //writes the metadata to disk
				nextWriteIndex = (int) file.getFilePointer();
			} catch (FileNotFoundException e) {
				System.err.printf("This should not throw an error as it is creating a file to write to\n%s\n", e);
			} catch (IOException e) {
				System.err.printf("Trouble reading current file pointer location \n%s\n", e);
				e.printStackTrace();
			}			
			converter = new SubStringConverter(this.seqLengthK);

			BTreeNode x = allocateNode();  //creates new empty node...parent offset of -1
			x.parentPtr = -1;
			x.leaf = true;
			x.n = 0;    //redundant with node constructor
			rootNode = x;
			saveNodeToDisk(x);
		
	}


	/**
	 * Method writes the Tree's metadata to disk
	 */
	private void writeTreeMetaToDisk() {
		try{
			file.seek(0);
			file.writeInt(degreeT); //write degree of tree at 0
			file.writeInt(seqLengthK); //write length of DNA subsequences at 4
			file.writeInt(nodeSize); //write size of each node at 8
			rootOffset = 16;
			file.writeInt(rootOffset); //write the value of the root offset at 12
		}
		catch (IOException e) {
			System.err.printf("Trouble writing metadata to location %d \n%s\n",0, e);
			e.printStackTrace();
		}
		//the root node location will have to be updated in metadata.
	}

	
	  /**
     * reads the meta data from a binary BTree file.
     */
    private void readTreeMetaFromDisk() {
    	try{
    		file.seek(0);
    		degreeT = file.readInt();
    		seqLengthK = file.readInt();
    		nodeSize = file.readInt();
    		rootOffset = file.readInt();
    	}
    	catch (IOException e) {
			System.err.printf("Trouble reading metadata\n%s\n", e);
			e.printStackTrace();
    	}
	}
	
    
    /**returns the current root offset
     * @return - the root offset
     */
    protected int getRootOffset(){
    	return rootOffset;
    }
    
    
	/** Calculates node size using the tree's degree.  If degree is 0,
	 * the function first determines the optimal degree based on the
	 * BLOCKSIZE and then uses that to calculate the node's size.
	 *
	 * @return - size needed for BTreeNode of the tree's degree.
	 */
	private int calculateNodeSize() {
		int nSize = -1;
		if(degreeT <= 0){	//if degree passed in is 0
			degreeT = (BLOCKSIZE - META - PTR + OBJ) / ((2 * PTR) + (2 * OBJ));	//calculates new optimal degree
		}
		nSize = META + (OBJ * ((2 * degreeT) - 1)) + (PTR * ((2 * degreeT) + 1));	//calculates size of node based on degree
		return nSize;  
	}


	/**  Splits given node in half, inserting middle key into parent node.
	 *
	 * @param parent - BTreeNode to be split.
	 * @param i - the index
	 */
	private void splitChild(BTreeNode parent, int i) {
		RightChildNode = allocateNode();	//creates new empty node
		leftChildNode = childNode;	//child node previously read from disk in insertNonFull()
		RightChildNode.leaf = leftChildNode.leaf;	//new child will be same as other child; a leaf or not a leaf
		RightChildNode.n = degreeT - 1;	//assign size to new child
		RightChildNode.parentPtr = parent.thisNodeOffset;
		leftChildNode.parentPtr = parent.thisNodeOffset;

		//has to update right half of children so they know who new parent is...
		if(!leftChildNode.leaf) {
			for (int j = degreeT; j <= leftChildNode.n; j++) {
				childNode = getNodeFromDisk(leftChildNode.childPtrs[j]);
				childNode.parentPtr = RightChildNode.thisNodeOffset;
				saveNodeToDisk(childNode);
			}
		}

		for(int j = 0; j < degreeT - 1; j++) {	//j = index 0 up to t-1
			RightChildNode.treeObjects[j] = leftChildNode.treeObjects[j + degreeT];	//copy right half of leftchild's keys into 0 and up of right child
		}
		if(!leftChildNode.leaf) {	//if left is not a leaf
			for(int j = 0; j < degreeT; j++){	//j = index - up to t-2
				RightChildNode.childPtrs[j] = leftChildNode.childPtrs[j + degreeT];	//copy right half of leftchild's pointers to index 0 & up of rightchild's pointers
			}
		}
		leftChildNode.n = degreeT - 1;	//redefine size of left child....keep in mind previous data is still there.
		for(int j = parent.n; j > i; j--){	//j = the largest child index goes to zero if necessary
			parent.childPtrs[j + 1] = parent.childPtrs[j];	//move all Parent's child pointers right one
		}
		parent.childPtrs[i + 1] = RightChildNode.thisNodeOffset;
		for(int j = parent.n; j > i; j--){	//j = the largest object index down to 1 if necessary
			parent.treeObjects[j] = parent.treeObjects[j - 1];	//shift all Parent's objects right one
		}
		parent.treeObjects[i] = leftChildNode.treeObjects[degreeT - 1];	//parent's key at i is replaced by leftchild's lastkey(previously parent's middle key)
		parent.n = parent.n + 1;	//increase the size of parent
		saveNodeToDisk(leftChildNode);
		saveNodeToDisk(RightChildNode);
		saveNodeToDisk(parent);
	}
	
	
	/** Inserts new object(key) into BTree
	 * 
	 * @param k - new BTreeObject to be inserted.
	 */
	protected void insert(TreeObject k) {

		BTreeNode r = this.rootNode;	//r is alias for root
		if(r.n == 2*degreeT - 1){	//if root is full...
			//check for existing key in full node
			TreeObject temp = binSearch(r, k); //perform binary search on node returns a TreeObject if have matching keys; null otherwise
			if(temp != null){ //if a match was found
				temp.incFreqCount(); //increase the frequency count
				saveNodeToDisk(r); //save to disk
			}
			else{
				BTreeNode s = allocateNode();	//create new node that will become new root.
				rootOffset = s.thisNodeOffset;	//updates tree metadata with new root node offset.
				this.rootNode = s;	//global rootNode pointer now points to s
				s.leaf = false;		//new root node is not a leaf
				s.n = 0;				//size of new root starts at 0
				s.parentPtr = r.parentPtr; //transfers parent pointer from one root to next "-1"
				s.childPtrs[0] = r.thisNodeOffset;	//new root's leftChild pointer now points to previous root offset.**
				childNode = r;	//childNode points to previous root.
				splitChild(s,0);//splits child node; passes parent node and index to promote to
				insertNonFull(s,k);	//insert the key into the new root using insertNonFull();It will find it is not a leaf and determine which child to insert to.
			}
		}
		else{
			insertNonFull(r,k);	//if root not full just insert into the non full root.

		}
	}


	/**Helper method for insert method.  Inserts an object into a non-full node.
	 * @param node - the node to insert into
	 * @param k	- the object(key) to insert
	 */
	private void insertNonFull(BTreeNode node, TreeObject k) {
		int i = node.n - 1;

		try {
			// Handles case when node is a leaf node.
			if (node.leaf) {    // 1st, check if matching key found in the leaf
				TreeObject temp = binSearch(node, k); //perform binary search on node returns a TreeObject if have matching keys; null otherwise
				if(temp != null){ //if a match was found
					temp.incFreqCount(); //increase the frequency count
					saveNodeToDisk(node); //save to disk
					return;
				}

				/* Make room for new key by shifting treeObjects right one 
				 * index, starting from last array index, and decreasing 
				 * until key < current index key, or index < 0. 
				 */
				while(i >= 0 && k.getKey() < node.treeObjects[i].getKey()){
					node.treeObjects[i + 1] = node.treeObjects[i];
					i = i - 1;
				}
				
				//insert the key object at next index & save node to disk
				node.treeObjects[i + 1] = k;
				node.n = node.n + 1;
				saveNodeToDisk(node);
			} else {  //determine the child of node to which the recursion descends
				while (i >= 0 && k.getKey() < node.treeObjects[i].getKey()){	//index starts at largest possible key index; while given key is less than index key...
					i = i - 1;
				}

				// Checks for duplicate key
				if(i >= 0 && k.getKey() == node.treeObjects[i].getKey()){
					node.treeObjects[i].incFreqCount();  //increases the frequency count
					saveNodeToDisk(node); //saves the node to disk
					return;
				}

				//index could go to -1; increases index pointer by one
				i = i + 1;

				//same index# is now used to get a node offset from childPointer array.
				childNode = getNodeFromDisk(node.childPtrs[i]);

				//detects if recursion would descend into a full child
				if(childNode.n == 2 * degreeT - 1){	//if child is full...
					// Checks for duplicate key before splitting
					TreeObject temp = binSearch(childNode, k);
					if(temp != null){
						temp.incFreqCount();
						saveNodeToDisk(childNode);
					}
					else{
						splitChild(node, i);  //split the child, but pass this node as a parent
						//determines which of two children is correct one to descend to
						if(k.getKey() > node.treeObjects[i].getKey()){	//split has taken place. node is different now containing new key from child at index i; if passed in key is grater than promoted node at index i
							insertNonFull(RightChildNode, k);	//insert the node into the right child
						}
						else{
							insertNonFull(leftChildNode, k);		//else insert the node into the left child
						}
					}
				}
				else {
					insertNonFull(childNode, k); //if it wasn't full, no split took place, just insert it into childNode
				}
			}
		} catch (TypeNotPresentException e) {
			System.err.printf("BTree.insertNonFull() - Key being inserted is not of type long.\n%s\n", e);
			System.exit(1);
		} catch (Exception e) {
			System.err.printf("BTree.insertNonFull() - Unknown Exception.\n%s\n", e);
			System.exit(1);
		}
	}


	/**
	 * Saves node to secondary storage disk file.
	 * 
	 * @param node
	 *            - BTreeNode to be saved in file.
	 */
	private void saveNodeToDisk(BTreeNode node) {
		if (cacheUse) {
			cache.addNode(node);
		}
		try {
			if(node == rootNode){	//(if this works) updates pointer to root node
				file.seek(12); //Resave new root offset.
				file.writeInt(node.thisNodeOffset);
			}
			file.seek(node.thisNodeOffset);
			file.writeInt(node.n); // write meta data
			file.writeBoolean(node.leaf); // write meta data
			file.skipBytes(3);
			file.writeInt(node.parentPtr); // write parent offset
			for (int i = 0; i < 2*degreeT; i++) {
				file.writeInt(node.childPtrs[i]); // write all children offsets
			}
			for (int i = 0; i < 2*degreeT -1; i++) { // write all node.treeObjects.length
				// objects
				file.writeLong(node.treeObjects[i].getKey()); // write key
				// values
				file.writeInt(node.treeObjects[i].getFreqCount()); // write
				// frequency
				// count
			}
		} catch (IOException e) {
			System.err.printf("Trouble writing to location %d \n%s\n",node.thisNodeOffset, e);
			System.exit(1);
		} catch (TypeNotPresentException e) {
			System.err.printf("BTree.SaveNodeToDisk() - key being written is not of type Long.\n%s\n", e);
			System.exit(1);
		} catch (Exception e) {
			System.err.printf("BTree.SaveNodeToDisk() - Unknown Exception.\n%s\n", e);
			System.exit(1);
		}
	}


	/** Read node from secondary storage disk file.
	 *
	 * @param offset - offset in disk file where BTreeNode is stored.
	 * @return BTreeNode - Node from secondary storage at specified offset or null if error occurs while reading from disk.
	 */
	public BTreeNode getNodeFromDisk(int offset) {
		BTreeNode gotNode = new BTreeNode((long) offset);

		if (cacheUse) {
			gotNode = cache.getNode(offset);
			if (gotNode != null) {
				return gotNode;
			}
		}

		try {
			gotNode = new BTreeNode((long) offset);
			file.seek((long)offset);
			gotNode.thisNodeOffset = offset;
			gotNode.n = file.readInt(); // read meta data
			gotNode.leaf = file.readBoolean(); // read meta data
			file.skipBytes(3);
			gotNode.parentPtr = file.readInt(); // read parent offset
			for (int i = 0; i < (2 * degreeT); i++) {
				gotNode.childPtrs[i] = file.readInt(); // read all children offsets
			}
			for (int i = 0; i < ((2 * degreeT) - 1); i++) { // read all
				// objects
				gotNode.treeObjects[i].setKey(file.readLong()); // read key
				// values
				gotNode.treeObjects[i].setFreqCount(file.readInt());// read
				// frequency
				// count
			}
		} catch (IOException e) {
			System.err.printf("Trouble reading from location %d \n%s\n",offset, e);
			e.printStackTrace();
		} catch (Exception e) {
			System.err.printf("Unable to get node from location %d \n%s\n",offset, e);
			return null;
		}

		return gotNode;
	}

	
	/**Method uses a binary search on a given node to find matching keys.
	 * @param node - the node to search
	 * @param obj - object (keys) to match
	 * @return the TreeObject containing matching keys; otherwise null.
	 */
	private TreeObject binSearch(BTreeNode node, TreeObject obj) {
		TreeObject retObj = null;
		int lft = 0;
		int rt = node.n - 1;
		while (lft<=rt){
			int m = (lft+rt)/2;
			try {
				if(node.treeObjects[m].getKey() < obj.getKey()){
					lft = m+1;
				}
				else if(node.treeObjects[m].getKey() > obj.getKey()){
					rt = m-1;
				}
				else if(node.treeObjects[m].getKey() == obj.getKey()) {
					retObj = node.treeObjects[m];
					return retObj;
				}
			} catch (TypeNotPresentException e) {
				System.err.printf("BTree.binSearch() - Key being searched is not of type long.\n%s\n", e);
				System.exit(1);
			}
		}
		return retObj;
	}


	/** Determines if node is in the tree.
	 * @param - rootOffset - The address of the Btree's root node that's being searched.
	 * @param - searchKey - Key that is being searched for in the BTree
	 * @return - TreeObject - If found, otherwise null.
	 */
	public TreeObject BTreeSearch(int rootOffset, long searchkey){
		BTreeNode rootx = getNodeFromDisk(rootOffset);
		//n number of keys in current node/ size
		TreeObject treeObject = null;
		int i=0;
		int offset=0;

		try {
			//Find the first key >= to searchkey
			while(i < rootx.n && searchkey > rootx.treeObjects[i].getKey() && rootx.treeObjects[i].getKey()!=-1) {
				i = i + 1;
			}

			//if found the correct one, return it.
			if((i < rootx.n && searchkey == rootx.treeObjects[i].getKey())) {
				treeObject=rootx.treeObjects[i];
			} else if(rootx.leaf) { 		// If this is a leaf node, we stop.
				treeObject = null;
			} else { 					//Get child
				offset = rootx.childPtrs[i];
				treeObject = BTreeSearch(offset, searchkey);
			}
		} catch (TypeNotPresentException e) {
			System.err.printf("BTree.BTreeSearch() - key being written is not of type Long.\n%s\n", e);
			System.exit(1);
		} catch (Exception e) {
			System.err.printf("BTree.BTreeSearch() - Unknown Exception.\n%s\n", e);
			System.exit(1);
		}
		return treeObject;
	}


	/** Helper method to print the BTree's meta data from memory,
	 *  as well as reading it from the binary file. 
	 */
	public void printMeta() {
		// Move read head to front of BTree file on disk.
		try {
			file.seek((long)0);
		} catch (IOException e) {
			System.err.printf("BTree.printMeta() - Unable to move file read to position 0.\n%s\n",e);
			System.exit(1);
		}

		// Print out meta data from MEMORY.
		System.out.println("degreeT= " + degreeT);
		System.out.println("seqLengthK= "+ seqLengthK);
		System.out.println("nodeSize= "+nodeSize);
		System.out.println("rootOffset= "+rootOffset);
		try {
			// Print out meta data currently stored in binary file.
			System.out.printf("degreeT= %d\n",file.readInt());
			System.out.printf("seqLengthK= %d\n", file.readInt());
			System.out.println("nodeSize= "+file.readInt());
			System.out.println("rootOffset="+file.readInt()+"\n");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	/**
	 * Checks to see if the BTree is empty
	 * @param - rootOffset - The address of the Btree's root node that's being searched.
	 * @return - boolean true if empty and false otherwise.
	 */
	boolean checkEmpty(int rootOffset){
		if (getNodeFromDisk(rootOffset).treeObjects[0].getKey()==-1){
			return true;
		}
		else return false;
	}

	
	/** In-order traversal, left-child, root, then right-child, order. At each
	 * leaf, output all DNA subsequence(s) found.  Output is saved in a text
	 * file named 'dump' written to the main project folder.
	 * 
	 * @param node - Current root of subtree being traversed.
	 * @param keyIndex - Current KEY index of root being traversed.
	 * @param outputFile - Disk file stream to save traversal data in.
	 */
	public void inOrderTraverse(BTreeNode node, int keyIndex, FileWriter outputFile) {

		try {
			if (keyIndex < 0 || keyIndex >= ((2 * degreeT) - 1)) {
				throw new IndexOutOfBoundsException();
			}
			// "Base Case" of recursion, dump all keys in this node
			if(node.leaf) {
				traverseKeys(node, 0, node.n, outputFile);
			} else {
				// Traverse deeper into the tree until a leaf is reached.
				if (keyIndex == 0) {  // Only on 1st key in node, recursively traverse down leftmost child.
					inOrderTraverse(getNodeFromDisk(node.childPtrs[0]), 0, outputFile);
				}
				
				// Iterate through each key, outputing itself followed by recursive iterations down it's RIGHT child.
				for (int i = 0; i < node.n; i++) {
					traverseKeys(node, i, i+1, outputFile);
					inOrderTraverse(getNodeFromDisk(node.childPtrs[i+1]),i, outputFile);
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.err.printf("BTree.inOrderTraverse(): a keyIndex of %d is out of bounds.\n%s\n", keyIndex, e);
			System.exit(1);
		} catch (NullPointerException e) {
			System.err.printf("BTree.inOrderTraverse(): BTree node is null.\n%s\n", e);
			System.exit(1);
		}		
	}

	
	/** Helper method from inOrderTraverse().  Writes all keys within 
	 * a given node to a text file "dump". One key per line with it's 
	 * associated freqency value.
	 * 
	 * Format written:  <DNA string, converted from key>: <frequency> 
	 * 
	 * @param node - Current node being traversed down into.
	 * @param startIdx - Starting index of the keys to be written.
	 * @param endIdx - Ending index of the keys to be written.
	 * @param outputFile - Disk file stream to save traversal data in.
	 * @throws - NullPointerException - When null node passed in
	 * @throws - IndexOutOfBounds - if startIdx or endIdx are unreachable.
	 */
	private void traverseKeys(BTreeNode node, int startIdx, int endIdx, FileWriter outputFile)  throws IndexOutOfBoundsException, NullPointerException {
		
		if (node == null) {
			throw new NullPointerException("BTree.traverseKeys: BTree node is null.\n");
		}
		
		if (startIdx < 0  ||  endIdx < 0  ||  startIdx > endIdx  ||  endIdx > node.n) {
			throw new IndexOutOfBoundsException("BTree.traverseKeys(): invalid startIdx: " + startIdx + ", or endIdx: " + endIdx + "\n");
		}
		try {
			String str;
			TreeObject obj = null;
			for (int i = startIdx; i < endIdx; i++) {
				obj = node.treeObjects[i];
				str = converter.longToString(obj.getKey()).toLowerCase() + ": ";
				str += obj.getFreqCount() + "\n";
				outputFile.write(str);
			}
		} catch (IOException e) {
			System.err.printf("BTree.traverseKeys() - Error writing to or closing dump file.\n%s\n", e);
			System.exit(1);
		} catch (TypeNotPresentException e) {
			System.err.printf("BTree.traverseKeys() - Key found is not of type long.\n%s\n", e);
			System.exit(1);
		} catch (NullPointerException e) {
			System.err.printf("BTree.traverseKeys() - Misc. Null Pointer Exception Caught.\n%s\n", e);
			System.exit(1);
		} catch (Exception e) {
			System.err.printf("BTree.traverseKeys() - Unknown Exception.\n%s\n", e);
			System.exit(1);
		}
	}	
	

	/** 
	 * @return the full filename and path of the BTree disk file.
	 */
	public String getFilePath() {
		return this.filePath;
	}
	
	
	/**
	 *  Cache object for the BTree
	 */
	private class Cache {
		LinkedList<BTreeNode> list = new LinkedList<>();
		LinkedList<Integer> keys = new LinkedList<>();
		int size;
		/**
		 *  Cache object for the BTree
		 *  @param - size of the cache
		 */
		public Cache(int size) {
			this.size = size;
		}
		
		
		/**
		 *  get a node from the cache at certain offset
		 *  @param - int offset of node wanted
		 *  @return - found node or null if not found
		 */
		protected BTreeNode getNode(int offset) {
			if(keys.contains(offset)) { //Check key arraylist for offset
				return list.get(keys.indexOf(offset)); //Return corresponding node from list
			}
			else return null;
		}
		
		
		/**
		 *  Cache object for the BTree
		 *  @param - node to add to cache
		 */
		private void addNode(BTreeNode node) {
			if (!keys.contains(node.thisNodeOffset)) {
				list.add(0, node);
				keys.add(0, node.thisNodeOffset);
				if (list.size() > size) {
					list.removeLast();
					keys.removeLast();
				}
			}
		}
	}
}

