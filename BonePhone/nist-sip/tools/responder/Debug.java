/*
 * Debug.java
 *
 * Created on July 25, 2001, 7:19 AM
 */

package tools.responder;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.stack.*;

/**
 * A class for debugging printf.
 * @author  M. Ranganathan
 * @version 
 */
class Debug extends Object {

    /** print soemthing. */
    static public void println(String toprint) {
	// System.out.println(toprint);
        if (ServerLog.needsLogging()) 
	    ServerLog.logMessage(toprint);
    }
    
    static public void println(SIPMessage message) {
	 if (ServerLog.needsLogging()) 
	     ServerLog.logMessage(message.encode());
    }
    
    static public void println(String msg , SIPMessage sipMessage) {
         if (ServerLog.needsLogging())
               ServerLog.logMessage(msg + sipMessage.encode());
    }

}
