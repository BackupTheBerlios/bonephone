/*
 * DeferredSend.java
 *
 * Created on August 13, 2001, 10:09 PM
 */

package tools.responder;
import java.util.*;

/**
 * @author  M. Ranganathan
 * @version 
 */
 class DeferredSend extends Object {
    protected String toSend;
    protected long timeToSend;
    protected int delay;
    protected CallFlow callFlow;
    protected Expect expect;

    /** Creates new DeferredSend */
    protected DeferredSend(String msg, CallFlow callFlow, 
		int delay, Expect expect) {
        this.toSend = msg;
        long ctime = new Date().getTime();
	this.delay = delay;
        this.timeToSend = ctime + this.delay*1000;
        this.callFlow = callFlow;
	this.expect = expect;
        
    }
    
    /** Return the message to send null if the timeout has not happened.
     */
    protected String getMessageToSend() {
        long ctime = new Date().getTime();
        if (ctime > this.timeToSend) { 
            return toSend;
        } else return null;
    }
    
    

}
