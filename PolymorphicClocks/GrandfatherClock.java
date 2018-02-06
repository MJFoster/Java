/**
 * A class to construct a GrandfatherClock, extends class Clock. 
 * @author MaryJo Foster
 * @version summer 2016
 */
public class GrandfatherClock extends Clock {
	/**
	 * Constructor for GrandfatherClock. 
	*/
	public GrandfatherClock() {
		super(Clock.ClockType.mechanical,0.000347222);
	}
	
	/** Display unique to an GrandfatherClock, including it's total drift.
	 */
	public void display() {
		System.out.println(this.getClockType() + " grandfather clock\ttime [ " 
		   + this.time.formattedReportedTime() + "],\ttotal drift = "
		   + numberFormatter.format(this.time.getTotalDrift()));
	}
}
