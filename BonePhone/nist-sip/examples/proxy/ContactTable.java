/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov) 			               *
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
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * This class store contact data
 */

public class ContactTable extends Hashtable implements Cleanable {

    protected  ServerMain		sipStack;

    /**
     * Constructor
     * @param sipStack The stack
     */

    public ContactTable(ServerMain sipStack) {
	super();
	this.sipStack = sipStack;
    }

    /*
     * This function is used by the Janitor
     */

    public void clean() {
	ServerLog.traceMsg(ServerLog.TRACE_DEBUG, 
			   "Registration cleaning");
	synchronized (this) {
	    Enumeration keys = keys();
	    while (keys.hasMoreElements()) {
		String user = (String) keys.nextElement();
		ServerLog.traceMsg(ServerLog.TRACE_DEBUG, 
				   "Testing " + user);
		ServerContactList contactList = (ServerContactList) get(user);
		LinkedList recordList = contactList.getExpiredContacts();
		if (recordList != null)  {
		    for (ListIterator li = recordList.listIterator(0);
			 li.hasNext();) {
			ServerContactRecord contactRecord = 
			    (ServerContactRecord) li.next();
			contactList.remove(contactRecord.getContact().
					   getAddress());
		    }
		    if (contactList.isEmpty())  {
			remove(user);
		    }
		}
	    }
	}
    }

}
