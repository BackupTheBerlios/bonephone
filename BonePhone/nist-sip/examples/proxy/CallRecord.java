/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov) 			               *
* Modifications: M. Ranganathan mragna@nist.gov				       *
* 	added transaction record.					       *
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
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Calendar;

/**
 * Store call data 
 */

public class CallRecord {

    private Hashtable peerRecords;
    private int capacity;
    private RefreshDate refreshDate;
    private TransactionRecord transactionRecord;
    

    /**
     * Constructor
     * Create a new empty CallRecord
     * 2 is the default numebr of peers (peer to peer call)
     */

    public CallRecord(TransactionRecord transactionRecord) {
	this(2);
	this.transactionRecord = transactionRecord;
    }

    /**
     * Constructor
     * Create a new empty CallRecord of a given number of peers
     * @param capacity is the number of peers 
     */

    public CallRecord(int capacity) {
	peerRecords = new Hashtable(capacity);
	this.capacity = capacity;
    }

    /** 
     * Create a new peer record for a given address
     * @param address The address of the peer
     * @return the PeerRecord created
     */

    public PeerRecord setPeerRecord(String address) {
	PeerRecord peerRecord = new PeerRecord();
	peerRecords.put(address, peerRecord);
	return peerRecord;
    }
    
    /**
     * Set the refresh date
     * @param refreshDate a RefreshDate taken from a Session-Expires header
     */

    public void setRefreshDate(RefreshDate refreshDate) {
	this.refreshDate = refreshDate;
    }

    /**
     * Get the peer record corresponding to an address
     * @param address The address of the peer 
     * @return The PeerRecord corresponding
     */

    public PeerRecord getPeerRecord(String address) {
	return (PeerRecord) peerRecords.get(address);
    }

    /** 
     * Get the number of peers for the call
     * @return int
     */
    
    public int getNumberOfPeers() {
	return capacity;
    }

    /**
     * Get the current refresh date for the call
     * @return Calendar
     */

    public RefreshDate getRefreshDate() {
	return refreshDate;
    }

    /**
     * True if there is a peer record is stored for the given address
     * @param address The address of the peer
     * @return boolean
     */
    
    public boolean hasPeerRecord(String address) {
	return (peerRecords.containsKey(address));
    }

    /**
     * Remove the peer record corresponding to the given address
     * @param address the address of the peer
     */

    public void removePeerRecord(String address) {
	peerRecords.remove(address);
    }

    /**
     * Convenience function
     * Return the list of peer records as an Enumeration
     * @return Enumeration
     */

    public Enumeration elements() {
	return peerRecords.elements();
    }

    /**
     * True is every peer has left the call
     * @return boolean
     */

    public boolean isFinished() {
	boolean leaving = true;

	for (Enumeration e = elements(); e.hasMoreElements() && leaving;) {
	    PeerRecord peerRecord = (PeerRecord) e.nextElement();
	    leaving = peerRecord.hasLeft();
	}

	return (leaving);
    }
    
    /** 
     * True if all the peers have their data recorded
     * @return boolean
     */

    public boolean isFull() {
	return (peerRecords.size() == capacity);
    }

    /**
     * True the list of peer records is empty
     * @return boolean
     */

    public boolean isEmpty() {
	return peerRecords.isEmpty();
    }

    /** Get the transaction record for this call record.
    */
    public TransactionRecord getTransactionRecord() { 
	  return transactionRecord;
    }

}    
