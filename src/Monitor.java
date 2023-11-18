/** 
 * Class Monitor 
 * To synchronize dining philosophers. 
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca  
 */
public class Monitor   
{
	private boolean[] chopsticks;
	/*
	 * ------------    
	 * Data members 
	 * ------------
	 */


	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers

		chopsticks = new boolean[piNumberOfPhilosophers];
		
		for (int i = 0; i < chopsticks.length; i++) {
			chopsticks[i] = true;
		}
		
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		int leftChopstick = piTID;
		int rightChopstick = (piTID + 1) % chopsticks.length;
		
		while(!(chopsticks[leftChopstick] && chopsticks[rightChopstick])) {
			try {
				wait();
			} catch (InterruptedException  e) {
				System.err.println("Philosopher.pickUp():");
				DiningPhilosophers.reportException(e);
				System.exit(1);
			}
		} 
			chopsticks[leftChopstick] = false;
			chopsticks[rightChopstick] = false;
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		int leftChopstick = piTID;
		int rightChopstick = (piTID + 1) % chopsticks.length;
		
		chopsticks[leftChopstick] = true;
		chopsticks[rightChopstick] = true;	
		notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		// ...
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		// ...
	}
}

// EOF
