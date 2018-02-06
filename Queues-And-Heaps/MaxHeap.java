import java.util.ArrayList;

/*
 * A maximum heap, implemented as an ArrayL, of type Process.
 * 
 * @version  Fall 2016
 * @author MaryJo Foster
 */
public class MaxHeap {
	
	ArrayList <Process> nodes;
	private int heapSize;
	
	
	/*
	 * Constructor for MaxHeap
	 */
	public MaxHeap() {
		// NOTE: heapSize is always 1 less than nodes.size() since the 1st element is ignored.
		nodes = new ArrayList<Process>();  // Empty Heap created
		heapSize = 0;
		nodes.add(0, null);  // Unused element for algorithm simplicity
	}
	
	
	/*
	 * @return number of elements currently in the heap.
	 */
	public int getHeapSize() {
		return heapSize;
	}
	
	
	/*
	 * Ensures the heap stays in 'max heap' order after
	 * every update to the heap.  Traversal of the heap
	 * starts at the given index, and moves DOWN to the
	 * heap's leaves.
	 * 
	 * @param starting index 'i'
	 */
	public void maxHeapify(int i) {
		Process tmpNode;
		int left = leftChildIndex(i);
		int right = rightChildIndex(i);
		int highestPriorityNode = 0;  // Just to initialize variable.
		
		// Determine which node has the highest priority level, check left child then right child.
		if (left <= getHeapSize()  &&  nodes.get(left).compareTo(nodes.get(i)) > 0)		
			highestPriorityNode = left;
		else
			highestPriorityNode = i;
		if (right <= getHeapSize()  &&  nodes.get(right).compareTo(nodes.get(highestPriorityNode)) > 0)
			highestPriorityNode = right;
	
		/* Recursively traverse DOWN through the MaxHeap correctly positioning
		   the highestPriorityNode
		 */
		if (highestPriorityNode != i) {
			// Exchange elements in ArrayList/MaxHeap with nodes at 'i' & 'highestPriority'
			tmpNode = nodes.get(i);
			nodes.set(i, nodes.get(highestPriorityNode));
			nodes.set(highestPriorityNode, tmpNode);
			maxHeapify(highestPriorityNode);
		}
	}
	
	
	/*
	 * Increases the priority level to the given keyValue,
	 * then ensures the heap stays in 'max heap' order.
	 * Traversal of the heap starts at index 'i' and moves
	 * UP through the heap until the root is reached.  When
	 * a 'parent' node is less than a child node, their positions
	 * in the heap are exchanged.
	 * 
	 * @param starting index 'i'
	 * @param new priority level to give existing Process at 'i' 
	 */
	public void increaseKey(int i, int keyValue) {
		Process tmpNode;
		
		if (keyValue < nodes.get(i).getPriority()) {
			System.err.printf("MaxHeap.increaseKey - Error: New Key is smaller than current key.\n");
			System.exit(1);
		}
		
		// Update priorityLevel of node at index 'i' with 'keyValue'
		nodes.get(i).setPriority(keyValue);
		
		/* Starting at given node 'i', moving up towards root, exchange positions of parent
		 * and child IF priorityLevel is out of order. Stop when parent priorityLevel 
		 * is >= child priorityLevel. 
		 * 
		 * NOTE ... if priorityLevels are the same, earliest arrival time gets highest priority.*/
		// while ( i > 1  &&  (nodes.get(parentIndex).getPriority() < nodes.get(i).getPriority()) ) {
		while ( i > 1  &&  (nodes.get(parentIndex(i)).compareTo(nodes.get(i)) < 0) ) {			
			tmpNode = nodes.get(parentIndex(i));
			nodes.set(parentIndex(i), nodes.get(i));
			nodes.set(i, tmpNode);
			i = parentIndex(i);   // Traverse UP the MaxHeap
		}
	}

	
	/*
	 * Retrieves parent index of given index.
	 * @return int - Index of parent node.
	 * @param int - current node index.
	 */
	public int parentIndex(int i) {
		return i / 2;
	}
	
	
	/*
	 * Returns left child index.
	 */
	public int leftChildIndex(int i){
		return 2 * i;
	}
	
	
	/*
	 * Returns right child index.
	 */
	public int rightChildIndex(int i) {
		return (i * 2) + 1;
	}
	
	
	/*
	 * Adds a new Process to the MaxHeap, and positions
	 * it in the MaxHeap according to it's priorityLevel.
	 * @param Process to be added
	 */
	public void insertNode(Process p) {
		nodes.add(p);
		heapSize++;
		int key = p.getPriority();
		increaseKey(heapSize, key);
	}


	/*
	 * Removes and returns the Process with the
	 * highest priorityLevel, then rebuilds the now
	 * smaller MaxHeap from the root down.
	 * 
	 * @return Process with highest priorityLevel
	 */
	public Process extractMax() {
		Process max;
		
		if(getHeapSize() < 1) {
			System.err.printf("MaxHeap.extractMax - Error: Heap underflow.\n");
			System.exit(1);
		}
		
		max = nodes.get(1);
		nodes.set(1, nodes.get(getHeapSize()));
		nodes.remove(getHeapSize());
		heapSize--;
		maxHeapify(1);  // rebuild maxHeap
		return max;
	}
	
	
	/*
	 * Generate string representing the contents of the MaxHeap.
	 */
	public String toString() {
		String heapString = "Heap priority values from top down: ";
		for(int i = 1; i <= getHeapSize(); i++) {
			heapString += nodes.get(i).getPriority() + " ";
		}
		return heapString;
	}
	
}
