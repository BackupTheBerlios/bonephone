/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* - Added JAVADOC                                                              *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: TransactionRecord.java
 * created 23-Oct-00 3:52:47 PM by mranga
 */


package examples.proxy;
import  java.util.Date; 
import  java.util.LinkedList;
import  java.util.Hashtable;
import  java.util.Iterator;
import  gov.nist.sip.*;
import  gov.nist.sip.stack.*;
import  gov.nist.sip.net.*;
import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.msgparser.*;

public final class TransactionRecord { 


    protected ServerMain     sipStack;
    protected MessageChannel messageChannel;
    protected SIPRequest sipRequest;
    protected  Hashtable forwardedRequests; 
    private String transactionID;
    private String cancelId;
    private long   startTime;	
    protected  long   expiryTime;
    private String provisionalResponse;
    // Locations to which we have sent the request.
	
    /**
     * Constructor
     * Create a new transaction Record.
     * @param originalRequest The request beginning the transaction
     * @param tid A transaction ID
     * @param mchannel The message channel by which the request arrived
     */

    protected TransactionRecord(  SIPRequest originalRequest, 
				  String tid  ,  String cancelId,
				  MessageChannel mchannel ) {
	if (originalRequest == null || tid == null || mchannel == null) 
		throw new IllegalArgumentException("null argument");
	startTime = (new java.util.Date() ).getTime();
        sipStack = (ServerMain) mchannel.getSIPStack();
        int lifetime = sipStack.maxTransactionLifetime;
	expiryTime =  startTime +  lifetime * 1000 ; 
	// Requests that were sent out for this request.
	forwardedRequests = new Hashtable();
	transactionID = tid;
	this.cancelId = cancelId;
	messageChannel = mchannel; // IO channel for request
    }
	
    /**
     * Get the message channel for this transaction record.
     * @return MessageChannel 
     */

    protected MessageChannel getMessageChannel() { 
	return messageChannel; 
    } 

    protected void setMessageChannel( MessageChannel mchannel) { 
		this.messageChannel = mchannel;
    }
    
    /**
     *  return the transaction identifier for this transaction.
     * @return A transaction ID as a String
     */
	
    protected String getTransactionID() {
	return transactionID;
    }
	
    /**
     * Add a new forwarded request to the queue.
     * @param freq A forwarded request
     */

    protected synchronized 
	void addRequest( ForwardedRequest freq) {
	if (ServerLog.needsLogging()) {
		ServerLog.logMessage("Putting in forwarded table  " +
			freq.getBranchID());
		ServerLog.logMessage(freq.toString());
	}
	forwardedRequests.put(freq.getBranchID(), freq);
    }  
	
	
    /**
     *  get the provisional Response.
     * @return a provisional response as a String
     */

    protected String getProvisionalResponse() {
	return provisionalResponse;
    }
	
    /**
     * Set the provisional response field.
     * @param response A provisional response as a String
     */

    protected void setProvisionalResponse(String response) {
	provisionalResponse = response;
    }

    /** Get the forwarded request list
     *@return an iterator containing the forwarded requests.
     *  These can be indexed by branch id.
     */
     protected Iterator getForwardedRequests() 
	{ return forwardedRequests.values().iterator(); }

     /** Get the forwarded request for a given branch ID.
      */
      protected ForwardedRequest getForwardedRequest(String branchId) {
	   return (ForwardedRequest) forwardedRequests.get(branchId);
      }

    /**
     * Get the SIPStack of this transaction handler.
     */
     public SIPStack getSIPStack() { return sipStack; }
}
