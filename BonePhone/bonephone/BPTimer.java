package bonephone;

import java.util.*;

/**
 * Timer class which extends Thread to realize cyclic 
 * actions.
 *
 * @author Jens Fiedler (jfi@fokus.gmd.de)
*/
public class BPTimer extends Thread {

	private BPTimerTaskIF action;
	private long millis,rest;
	private boolean firebefore;
	private Boolean running;

	/**
	 * Stops the timer. The timer Thread will terminate upon next timer expiration.
	*/
	public void stopTimer() {
		synchronized (running) {
			running=new Boolean(false);
		}
		super.interrupt();
	}

	public void run() {
		Date dt1, dt2;
		long diff;
		while (running.booleanValue()) {
			synchronized (running) {
				if (running.booleanValue()) 
					if (firebefore) action.action();
			}
			rest=millis;
			while (rest>0) {
				dt1=new Date();
				try {
					sleep(rest);	
				} catch (InterruptedException ie) {
					if (! running.booleanValue()) break;					
				}
				dt2=new Date();
				diff=dt2.getTime()-dt1.getTime();	
				rest=rest-diff;
			}
			synchronized (running) {
				if (running.booleanValue()) 
					if (!firebefore) action.action();
			}
		}
	}

	/**
	 * Standard constructor. 
	 *
	 * @param action action to take before/after timer expires.
	 * @param millis cycle time in milliseconds.
	 * @param firebefore true if action has to be taken before timer expires, 
	 *                   false if action must be taken after timer expiration.
	*/
	BPTimer(BPTimerTaskIF action, long millis, boolean firebefore) {
		this.action=action;
		this.millis=millis;
		this.firebefore=firebefore;
		running=new Boolean(true);
	}
}
