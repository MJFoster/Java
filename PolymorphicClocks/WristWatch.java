/**
 * A class to construct a WristWatch, extends class Clock. 
 * @author MaryJo Foster
 * @version summer 2016
 */
public class WristWatch extends Clock{
	/**
	 * Constructor for WristWatch. 
	*/
	public WristWatch() {
		super(Clock.ClockType.digital, 0.000034722);
	}

	/** Display unique to a WristWatch, including it's total drift.
	 */
	public void display() {
		System.out.println(this.getClockType() + " wrist watch\t\ttime [ " 
		   + this.time.formattedReportedTime() + "],\ttotal drift = "
		   + numberFormatter.format(this.time.getTotalDrift()));
	}
}
