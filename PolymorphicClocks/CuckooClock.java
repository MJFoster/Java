/**
 * A class to construct a CuckooClock, extends class Clock. 
 * @author MaryJo Foster
 * @version summer 2016
 */
public class CuckooClock extends Clock {
	/**
	 * Constructor for CuckooClock. 
	*/	
	public CuckooClock() {
		super(Clock.ClockType.mechanical,0.000694444);
	}
	
	/** Display unique to a CuckooClock, including it's total drift.
	 */
	public void display() {
		System.out.println(this.getClockType() + " cuckoo clock\t\ttime [ " 
		   + this.time.formattedReportedTime() + "],\ttotal drift = "
		   + numberFormatter.format(this.time.getTotalDrift()));
	}
}
