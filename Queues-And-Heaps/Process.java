import java.util.Random;

/*
 * Class that manages all methods and instance variables 
 * pertaining to a Process, including a priority level 
 * used by a Priority Queue.
 * 
 * @version  Fall 2016
 * @author MaryJo Foster
 */
public class Process implements Comparable <Process> {
	private int priorityLevel;  // Current priority level of Process
	private int requiredProcessingTime;  // Time span to Process
	private int timeRemaining;  // Time remaining to finish processing
	private int arrivalTime;    // Start time of Process 
	int timeNotProcessed;  // Time passed while waiting to start Process
	private Random rand = new Random();
	
	/*
	 * Constructor for new Process.
	 */
	public Process(int currTime, int maxProcTime, int maxLvl) {
		priorityLevel = rand.nextInt(maxLvl) + 1;
		requiredProcessingTime = rand.nextInt(maxProcTime) + 1;
		timeRemaining = requiredProcessingTime;
		arrivalTime = currTime;  // Same as "Job #"
		timeNotProcessed = 0;
	}
	
	
	/*
	 * Compares the priority level of the given Process against this Process'
	 * priority level, and returns -1, 0, 1 respectively if this
	 * Process' priority level is <, ==, or > the
	 * given priority.
	 * NOTE:  If priority levels are equal, the 1st to arrive gets highest priority.
	 * 
	 * @param Process - a Process being compared to this process.
	 */
	public int compareTo(Process p) {
		
		if(priorityLevel < p.getPriority())
			return -1;
		else if (priorityLevel == p.getPriority()) { 
			if(arrivalTime <= p.getArrivalTime())
				return 1;
			else
				return -1;
		} else
			return 1;
	}
	
	
	/*
	 * Getter for arrivalTime
	 * @return int - Time Process started
	 */
	public int getArrivalTime() {
		return arrivalTime;
	}
	
	
	/*
	 * Getter for priority level.
	 * @return int - Current priority level of Process
	 */
	public int getPriority() {
		return priorityLevel;
	}
	
	
	/*
	 * Setter for priority level.
	 * @param int - new priority level.
	 */
	public void setPriority(int keyValue) {
		priorityLevel = keyValue;
	}
	
	
	/*
	 * Getter for timeRemaining in this Process.
	 */
	public int getTimeRemaining() {
		return timeRemaining;
	}
	
	
	/*
	 * Decrements timeRemaining variable by 1.
	 */
	public void reduceTimeRemaining() {
		timeRemaining--;
	}
	
	
	/*
	 * Determines if processing time of this
	 * Process has elapsed and Process is finished.
	 */
	public boolean finish() {
		if (timeRemaining == 0)
			return true;
		else
			return false;
	}
	
	
	/*
	 * Resets timeNotProcessed to 0.
	 */
	public void resetTimeNotProcessed() {
		timeNotProcessed = 0;
	}
}
