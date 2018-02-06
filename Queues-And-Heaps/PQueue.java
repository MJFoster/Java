/*
  * Priority Queue implemented with a MaxHeap.
  *  
  * @version  Fall 2016
  * @author MaryJo Foster
 */
public class PQueue {

	private MaxHeap mxHeap;
	
	/*
	 * Constructor for PQueue
	 */
	public PQueue() {
		mxHeap = new MaxHeap();
	}
	
	
	/*
	 * Adds an element to the priority queue, and ensures
	 * it is placed correctly according to it's priorityLevel.
	 * @param Process - Process to add to PQueue
	 */
	public void enPQueue(Process process) {
		mxHeap.insertNode(process);
	}
	
	
	/*
	 * Removes and returns the element with the highest priority
	 * level from the priority queue.
	 * 
	 * @return Process 
	 */
	public Process dePQueue() {		
		return mxHeap.extractMax();
	}
	
	
	/*
	 * For each Process in PQueue, increment time Process has
	 * been waiting to be started.  If Process has been "starved",
	 * increase it's priorityLevel, reset timeNotProcessed, and
	 * re-order MaxHeap accordingly.
	 */
	public void update(int timeToIncrement, int maxPriority) {
		
		int newPriorityLvl;
		Process currProcess;
		
		for(int i = 1; i <= mxHeap.getHeapSize(); i++) {
			
			currProcess = mxHeap.nodes.get(i);
			newPriorityLvl = currProcess.getPriority() + 1;
			
			if(newPriorityLvl >= maxPriority)  // keep priorityLevel within maxPriority
				newPriorityLvl = maxPriority;
			
			currProcess.timeNotProcessed++;
			if(currProcess.timeNotProcessed >= timeToIncrement) {
				currProcess.timeNotProcessed = 0;
				mxHeap.increaseKey(i,newPriorityLvl);
			}
		}

	}
	
	
	/*
	 * Determines if the priority queue is empty or not.
	 * @return boolean - True of PQueue is empty, else false returned.
	 */
	public boolean isEmpty() {
		if(mxHeap.getHeapSize() == 0)
			return true;
		else
			return false;
	}
}
