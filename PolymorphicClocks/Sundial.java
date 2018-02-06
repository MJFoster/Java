/**
 * A class to construct a Sundial, extends class Clock. 
 * @author MaryJo Foster
 * @version summer 2016
 */
public class Sundial extends Clock {
	/**
	 * Constructor for Sundial. 
	*/
	public Sundial() {
		super(Clock.ClockType.natural, 0.0);
	}
	
	/** Display unique to a Sundial, including it's total drift.
	 */
	public void display() {
		System.out.println(this.getClockType() + " Sun dial\t\ttime [ " 
		   + this.time.formattedReportedTime() + "],\ttotal drift = "
		   + numberFormatter.format(this.time.getTotalDrift()));
	}
}

