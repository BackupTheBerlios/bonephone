/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov)                                    *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package gov.nist.sip.stack;
import gov.nist.sip.stack.*;
import gov.nist.sip.stack.security.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.msgparser.*;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * This class is in charge of periodical cleaning.
 * Objects which require periodical cleaning register here.
 * They must implement the Cleanable interface.
 * 
 * Design note: this replaces the old Manager classes.
 * Each of them was in charge of periodical cleaning for
 * a specific class and was running its own thread. Now, there
 * is a single thread running, which is in charge of all cleaning.
 */

public class Janitor implements Runnable {
    
    private LinkedList subscribers;
    private int        sleepingTime;
    private Thread     janitor;

    /**
     * Constructor
     * @param sleepingTime The period between each cleaning
     */
 
    public Janitor(Integer sleepingTime) {
	subscribers = new LinkedList();
	janitor = new Thread(this);
	janitor.start();
	if (sleepingTime != null) {
	    this.sleepingTime = sleepingTime.intValue() * 1000;
	} else {
	    this.sleepingTime = 10000; /* 10 sec */
	} 
    }

    /**
     * Add a new object to be periodically cleaned
     * @param object The object to be cleaned (must implement Cleanable)
     */

    public synchronized void register(Cleanable object) {
	subscribers.addLast(object);
    }

    /**
     * Remove from the list an object to be cleaned
     * @param object The object to remove 
     */

    public synchronized  void unregister(Cleanable object) {
	subscribers.remove(object);
    }

    /** 
     * This is the code that periodically clean the objects
     */

    public void run() {
	while (true) {
	    try {
		janitor.sleep(sleepingTime);
	    } catch (InterruptedException ex) {
		// Ignore
	    }
	    synchronized(this) {
	      ListIterator iterator = subscribers.listIterator();
	      while (iterator.hasNext()) {
		Cleanable object = (Cleanable) iterator.next();
		// Joyfully cleans the mess
		object.clean();
	      }
	    }
	}
    }

}

