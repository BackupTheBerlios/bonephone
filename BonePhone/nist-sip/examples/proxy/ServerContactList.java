/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov) 			       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: ServerContactList.java
 * created 10-Sep-00 11:42:59 PM by mranga
 */

package examples.proxy;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import gov.nist.sip.stack.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;

/**
* The server maintains a hash table of these records - one for every user. 
* External contact methods are also registered here. 
* These external methods are trusted and configured
* when the server starts.
*/

public class ServerContactList {
	
    protected Hashtable addressHash; 	
				// To find contact records by address.
    protected LinkedList contactList; 	
				// A list of contact records for the user
    protected ServerMain sipStack; 
				// Ptr back to the server main structure.
				// user
	
    /**
     * Get the contacts as an array of contact bodies.
     * @return ServerContactRecord[]
     */
    protected ServerContactRecord[] getContacts() {
	ServerContactRecord[] retval = new ServerContactRecord
	    [contactList.size()];
	return (ServerContactRecord[]) contactList.toArray(retval);
    }
    
    /**
     * Get the size of the contact list
     * @return int
     */
    
    protected int getSize() {
	return contactList.size();
    }
    
    /**
     * Constructor for server side list of contacts.
     * @param server The stack
     */
    
    protected ServerContactList(ServerMain server) {
	addressHash = new Hashtable();
	contactList = new LinkedList();
	sipStack = server;
    }
    
    /**
     * Add a new contact  to list of contacts for the given user. 
     * @param contact A Contact to be added
     * @return boolean True if it succeeds
     * @throws SIPException if contact could not be added.
     */

    protected synchronized boolean add(Contact contact) throws SIPException {
	String addr = contact.getAddress().getAddrSpec().encode();
	
	ServerContactRecord sr = (ServerContactRecord) addressHash.get(addr);
	ServerContactRecord newContact = 
	    new ServerContactRecord(contact,sipStack);
	if (sr == null) {
	    if (sipStack.allowMultipleRegistration ||
		contactList.size() == 0) {
		ServerLog.traceMsg(ServerLog.TRACE_DEBUG, 
				   "Adding contact record");
		addressHash.put(addr,newContact);
		contactList.add(newContact);
	    } else {
	    // See if there is an action conflict.
	    String newAction =  contact.getAction();
	    if (newAction != null) {
		String oldAction = sr.action;
		if (oldAction.compareTo(newAction) != 0 ) {
		    ServerLog.traceMsg (ServerLog.TRACE_DEBUG, 
					"oldAction = " + oldAction + 
					" newAction = " + newAction );
		    throw new SIPException("Conflicting Actions");
		}
	    }
	    // Remove the existing record
	    addressHash.remove(addr);
	    contactList.remove(sr);
	    // Add this record to the linked list and hash table.
	    addressHash.put(addr,newContact);
	    contactList.add(newContact);
		
		return true;
	    }
	} else {
	    // If this is a deletion request (signified by 0 expiry time)
	    ServerLog.traceMsg
		(ServerLog.TRACE_DEBUG, "Replacing contact record");
	    SIPDateOrDeltaSeconds expires =  contact.getExpires();
	    if (expires != null && expires.isDeltaSeconds()){
		DeltaSeconds ds = (DeltaSeconds) expires;
		int expirytime = (int) ds.getDeltaSeconds();
		if (expirytime == 0) {
		    ServerLog.traceMsg
		       (ServerLog.TRACE_DEBUG, "Deleting contact record");
		    addressHash.remove(addr);
		    contactList.remove(sr);
		    return true;
		}
	    }
	    // See if there is an action conflict.
	    String newAction =  contact.getAction();
	    if (newAction != null) {
		String oldAction = sr.action;
		if (oldAction.compareTo(newAction) != 0 ) {
		    ServerLog.traceMsg (ServerLog.TRACE_DEBUG, 
					"oldAction = " + oldAction + 
					" newAction = " + newAction );
		    throw new SIPException("Conflicting Actions");
		}
	    }
	    // Remove the existing record
	    addressHash.remove(addr);
	    contactList.remove(sr);
	    // Add this record to the linked list and hash table.
	    addressHash.put(addr,newContact);
	    contactList.add(newContact);
	}
	return true;
    }
	
    /**
     * Remove a contact record from the server contact list given an address
     * @param address The address for which the corresponding Contact
     * has to be removed
     */
    
    protected synchronized void remove(Address address) {
	String addr = address.getAddrSpec().encode();
	ServerContactRecord cr = 
	    (ServerContactRecord) addressHash.get(addr);
	if (cr != null) {
	    addressHash.remove(addr);
	    contactList.remove(cr);
	} 
    }
    
    /**
     *  Remove a contact record from the server contact list
     * @param cr A ServerContactRecord to remove
     */
    
    protected synchronized void remove(ServerContactRecord cr )  {
	if (cr == null) 
	    throw new IllegalArgumentException ("Null contact record!");
	contactList.remove(cr);
	String addr = cr.contact.getAddress().getAddrSpec().encode();
	if (addressHash.containsKey(addr) ) { 
	    addressHash.remove(addr);
	} 
    }

    /**
     * Remove all the addresses given a list of contact addresses.
     * @param clist A contact list
     */
    
    protected synchronized void remove (ContactList clist) {
	Contact c;
	boolean wildCardFlag = false;
	
	for (c = (Contact) clist.first(); c != null; c = 
		 (Contact)clist.next()) {
	    remove (c.getAddress());
	}
    }

    /**
     * Initialize and return a list iterator for this list of contact 
     * records.
     * @return listIterator
     */
    
    protected ListIterator getIterator() {
	return contactList.listIterator();
    }
    
    /**
     * Get the first contact record on this list of contact records. 
     * @param li A listIterator
     * @return ServerContactRecord
     */
    
    protected ServerContactRecord next(ListIterator li) {
	try {
	    ServerContactRecord scr = (ServerContactRecord) li.next();
	    return scr;
	} catch (NoSuchElementException ex) {
	    return null;
	}
    }
    
    /**
     * Return a linked list of expired contacts.
     * @return LinkedList
     */

    protected LinkedList getExpiredContacts() {
	long currentTime  = System.currentTimeMillis();
	int i = 0;
	LinkedList recordList = new LinkedList();
	for (int j = 0 ; j < contactList.size(); j++) {
	    ServerContactRecord contact  = (ServerContactRecord) 
		contactList.get(j);
	    long expiryTime = contact.expiryTimeMilis;
	    if (currentTime >= expiryTime) {
		recordList.add(contact);
	    }
	}
	return recordList;
    }

    /**
     * Return true if the contact list is empty (all contacts expired).
     * @return boolean
     */

    protected boolean isEmpty() {
	return contactList.isEmpty();
    }
				
}
