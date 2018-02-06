/**
 * Interface for any type of time piece.
 * Promised methods:
 *  - reset() - Resets TimePiece to initial time of 00:00:00
 *  - tick() - Adds 1 second, or 'tick', of time to TimePiece instance variable.
 *  - display() - TimePiece stats to standard output: type, name, current time, & total drift.
 *  
 * @author MaryJo Foster
 * @version summer 2016
 */
public interface TimePiece {
	public void reset();
	public void tick();
	public void display();
}
