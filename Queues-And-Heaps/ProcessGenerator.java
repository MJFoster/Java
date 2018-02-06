/*
 * Manages the generation of new processes based
 * on the given probability. A new Process is
 * generated and returned if the query() method
 * generates a random value that is within the 
 * given probability range.
 * 
 * @version  Fall 2016
 * @author MaryJo Foster
 */
public class ProcessGenerator {
	double probability;
	
	/*
	 * Constructor
	 */
	public ProcessGenerator(double prob) {
		probability = prob;
	}
	
	/*
	 * Determines whether a new Process
	 * should be generated, based on the value
	 * of 'probability'.
	 * 
	 * @return true if a new Process is to be generated,
	 *         false otherwise.
	 */
	public boolean query() {
		if(Math.random() <= probability)
			return true;
		else
			return false;
	}
	
	
	public Process getNewProcess(int currTime, int maxProcTime, int maxPriorityLevel) {
		return new Process(currTime, maxProcTime, maxPriorityLevel);
	}
}
