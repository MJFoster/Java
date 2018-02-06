/**
 * Driver class that demonstrates an array implementation of an
 * unordered list.  This list is then used as a 'container' to keep
 * track of different types of Clock objects that are polymorphically created.
 * 
 * Each child Clock:
 *  - Is derived from class 'Clock'.
 *  - Utilizes the super constructor for it's own constructor.
 *  - Implements the abstract display() method found in the abstract "Clock" class.
 *  
 * Bag - Container holding different types of Clock objects.
 * 
 * @author MaryJo Foster
 * @version Summer 2016
 */
public class ClockSimulation {
	
	public static void main(String[] args) {
		
		final int SECONDS_PER_MINUTE = 60;
		final int MINUTES_PER_HOUR = 60;
		final int HOURS_PER_DAY = 24;
		final int SECONDS_PER_DAY = HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE;
		
		// Create an empty bag that holds 'Clock' objects.
		Bag<Clock> myBag = new Bag<Clock>();
		
		myBag.add(new Sundial());
		myBag.add(new CuckooClock());
		myBag.add(new GrandfatherClock());
		myBag.add(new AtomicClock());
		myBag.add(new WristWatch());
		
		int bagSize = myBag.getSize(); 
		
		// Display initial starting times for each clock
		System.out.println("Times before start:");
		for(int i = 0; i < bagSize; i++) {
			Clock curClock = myBag.get(i);
			curClock.display();
			curClock.reset();
		};
		
		// Simulate 1 day
		System.out.println("\nAfter one day:");
		for(int i = 0; i < bagSize; i++) {
			Clock curClock = myBag.get(i);
			curClock.elapseTime(SECONDS_PER_DAY);
			curClock.display();
			curClock.reset();
		};
		
		// Simulate 1 week
		System.out.println("\nAfter one week:");
		for(int i = 0; i < bagSize; i++) {
			Clock curClock = myBag.get(i);
			curClock.elapseTime(SECONDS_PER_DAY * 7);
			curClock.display();
			curClock.reset();
		}
		
		// Simulate 1 month
		System.out.println("\nAfter one month:");
		for(int i = 0; i < bagSize; i++) {
			Clock curClock = myBag.get(i);
			curClock.elapseTime(SECONDS_PER_DAY * 30);
			curClock.display();
			curClock.reset();
		}
		
		// Simulate 1 year
		System.out.println("\nAfter one year:");
		for(int i = 0; i < bagSize; i++) {
			Clock curClock = myBag.get(i);
			curClock.elapseTime(SECONDS_PER_DAY * 365);
			curClock.display();
			curClock.reset();
		}		
	}
}
