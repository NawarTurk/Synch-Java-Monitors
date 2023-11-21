import java.util.LinkedList;
import java.util.Queue;

/** 
 * Class Monitor 
 * To synchronize dining philosophers. 
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca  
 */
public class Monitor   
{
	private boolean[] chopsticks;
    private boolean isTalking = false;
    private Queue<Integer> talkQueue = new LinkedList<>();
    private Queue<Integer> pickUpQueue = new LinkedList<>();

	
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
		
		pickUpQueue.add(piTID);
		
		while(!(chopsticks[leftChopstick] && chopsticks[rightChopstick]) || pickUpQueue.peek() != piTID) {
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
			pickUpQueue.remove();
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
	 * @throws InterruptedException 
	 */
	public synchronized void requestTalk(int piTID) throws InterruptedException
	{
		talkQueue.add(piTID);
		
		while (isTalking || talkQueue.peek() != piTID) {
			wait();
		}
		
		isTalking = true;
		talkQueue.remove();

	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		isTalking = false;
		notifyAll();
	}
}

// EOF
