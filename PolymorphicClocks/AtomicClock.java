/**
 * A class to construct an AtomicClock, extends class Clock. 
 * @author MaryJo Foster
 * @version summer 2016
 */
public class AtomicClock extends Clock{
	/**
	 * Constructor for AtomicClock. 
	*/
	public AtomicClock() {
		super(Clock.ClockType.quantum, 0.0);
	}
	
	/** Display unique to an AtomicClock, including it's total drift.
	 */
	public void display() {
		System.out.println(this.getClockType() + " atomic clock\t\ttime [ " 
		   + this.time.formattedReportedTime() + "],\ttotal drift = "
		   + numberFormatter.format(this.time.getTotalDrift()));
	}
}
