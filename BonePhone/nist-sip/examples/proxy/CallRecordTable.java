/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov) 			               *
* Re-write : M. Ranganathan (mranga@nist.gov)
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package examples.proxy;
import gov.nist.sip.stack.*;
import gov.nist.sip.stack.security.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.msgparser.*;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * This class stores call (leg) data
 */

public class CallRecordTable extends Hashtable implements Cleanable {

    protected  ServerMain		sipStack;

    /**
     * Constructor
     * @param sipStack The stack
     */

    public CallRecordTable(ServerMain sipStack) {
	super();
	this.sipStack = sipStack;
    }
  
    /** Set up this record to be deleted into the future. We let this
     *  linger around for a while to handle re-transmissions.
     */
    public synchronized void markForDeletion(String callId) {
	   CallRecord callRecord = (CallRecord) this.get(callId);
	   if (callRecord != null) 
	       callRecord.setRefreshDate(new RefreshDate(new Integer(60)));
     }

    public synchronized Object put ( Object key, Object value) {
	if (ServerLog.needsLogging()) 
	    ServerLog.logMessage("Putting " + key);
	return super.put(key,value);
	
     }
	

     /** A synchronized version of the superclass method...
     */
     public synchronized Object get(Object key) {
	if (ServerLog.needsLogging()) 
	    ServerLog.logMessage("Getting " + key);
	return super.get(key);
     }

     /** A synchronized version of the superclass method..
     */
     public synchronized boolean containsKey(Object key) {
		return super.containsKey(key);
     }
     
     /** A synchronized version of the superclass method...
     */
     public synchronized Object remove(Object key) {
	if (ServerLog.needsLogging())  {
	    ServerLog.logMessage("Removing " + key);
	}
	
	return super.remove(key);
     }
	


    /***
     * This function is used by the Janitor
     */

    public synchronized void clean() {
	    Enumeration keys = keys();
	    while (keys.hasMoreElements()) {
		String callId = (String) keys.nextElement();
		CallRecord callRecord = (CallRecord) get(callId);
		RefreshDate refreshDate = callRecord.getRefreshDate();
		if (refreshDate.isOver()) {
		    ServerLog.traceMsg(ServerLog.TRACE_DEBUG, 
				       "Call " + callId +
				       " has expired. Removing.");
		    if (sipStack.firewallDevice != null) 
		        sipStack.firewallDevice.sessionClosing
			(callId, callRecord);
		    remove(callId);
		}
	    }
    }

}
