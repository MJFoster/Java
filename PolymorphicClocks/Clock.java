import java.text.DecimalFormat;

/**
 * Abstract class that implementing the TimePiece Interface
 * Constructs a Clock object.
 *   
 * @author MaryJo Foster
 * @version Summer 2016
 */
public abstract class Clock implements TimePiece {

	// enums can't be altered at runtime, so 'public' exposure ok
	public enum ClockType {natural, mechanical, digital, quantum};
	
	// encapsulated instance variables
	private ClockType clockType;
	protected Time time;
	protected DecimalFormat numberFormatter = new DecimalFormat("0.00");
	
	/**
	 * Super constructor for all "Clock" objects
	 * @param type - ClockType representing the type of clock being constructed.
	 * @param drift - double representing an amount of time lost as Clock runs.
	 */
	public Clock (ClockType type, double drift) {
		this.clockType = type;
		this.time = new Time(0, 0, 0, drift);
	}
	
	/** Accessor method to return encapsulated Clocktype.
	 * 
	 * @return type of Clock for this object
	 */
	public ClockType getClockType() {
		return this.clockType;
	}
	
	/**
	 * @see TimePiece#reset()
	 * Reset Clock to original start time.
	 */
	@Override
	public void reset() {
		this.time.resetToStartTime();  // Method call to Clock's instance variable Time object.
	}

	/**
	 * @see TimePiece#tick()
	 * Increment time by 1 second or 'tick'.
	 */
	@Override
	public void tick() {
		this.time.incrementTime();
	}

	/**
	 * @see TimePiece#display()
	 * Abstract method to be implemented by derived classes.
	 * Demonstrates Polymorphism. 
	 */
	@Override
	public abstract void display();
	
	/**
	 * Elapses time on Clock by seconds.
	 * @param seconds
	 */
	public void elapseTime(int seconds) {
		for(int i = 0; i < seconds; i++) {
			this.tick();
		}
	}
}
