/*
 * Transaction.java
 *
 * Created on May 7, 2001, 2:12 PM
 * This is just an itnernal class for storing transaction records.
 *
 * Revisions:
 * Added exponential backoff for retransmissions
 * ( bug pointed out by Chris Mills <chrmills@nortelnetworks.com>
 *   of Nortel Networks ).
 */

package gov.nist.jain.protocol.ip.sip;
import gov.nist.sip.msgparser.*;
import gov.nist.jain.protocol.ip.sip.message.*;
import gov.nist.jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.*;
import java.util.LinkedList;
import java.util.ListIterator;
import gov.nist.sip.stack.*;
import gov.nist.log.*;
import gov.nist.sip.sipheaders.*;


/**
 * @author  M. Ranganathan
 * @version 1.0
 *
 */
class Transaction {
    private boolean isServerTransaction;
    private String tidString;
    private long   tidLong;
    private long   timeCreated; // Time at which this transaction was
                                  // put on the queue.
    private long    expiryTime;    // Time at which this transaction 
				     // generates timeouts.
    private long    maxLifetime;   // Time at which to remove this transaction.
    private RequestImpl  request;
    private ResponseImpl response;
    private ListeningPointImpl listeningPoint;
    private SipStackImpl  sipStack;
    private boolean 	    locked;
    private MessageChannel messageChannel;
    private boolean	pendingDelete;
    // ID for cancel requests.
    private String    cid;
    private   int 	counter;
    
    /** Creates new Transaction */
    protected  Transaction(RequestImpl sipRequest, 
	ListeningPointImpl lPoint,
	SipStackImpl stack,
        boolean serverTransactionFlag, 
	MessageChannel mchan ) {

	if (sipRequest == null || lPoint == null 
		|| stack == null || mchan == null)
		throw new IllegalArgumentException("null arg");
        request = sipRequest;
	listeningPoint = lPoint;
	sipStack = stack;
        isServerTransaction = serverTransactionFlag;
        timeCreated = System.currentTimeMillis();
        // Transaction lifetime is in seconds.
        expiryTime = timeCreated +  sipStack.getTransactionTimeout()*1000;
	maxLifetime = timeCreated + sipStack.getTransactionLifetime()*1000;
	if (LogWriter.needsLogging()) {
		LogWriter.logMessage("Max lifetime = " + 
			sipStack.getTransactionLifetime());
	}
	messageChannel = mchan;
	counter = 1;
    }
    
    /* return the message channel member */
    protected MessageChannel getMessageChannel()  { 
            return this.messageChannel; 
    }
        
        
    protected void setTid (long tid) {
        tidLong = tid;
    }
    
    protected void setTid(String tid) {
        tidString = tid;
    }
    
    protected RequestImpl getRequest() {
        return request;
    }
    
    protected ResponseImpl getResponse() {
        return response;
    }
    
    protected void setResponse(ResponseImpl responseImpl) {
        response = responseImpl;
    }

	
    protected void markForDelete() { 
	if (LogWriter.needsLogging()) {
		LogWriter.logMessage
		   ("marForDelete " + tidLong + " isServerTr " +
			isServerTransaction);
		 LogWriter.logStackTrace();
	}
	pendingDelete = true; 
    }

    protected boolean isPendingDelete() { return pendingDelete; }
    
    protected String getCID() { return cid; }

    protected void setCID(String c) { cid = c; }
    
    protected long getTidLong() { return tidLong; }

    protected String getTidString() { return tidString; }
    
    protected boolean IsServerTransaction() { return isServerTransaction; }

    protected boolean hasTimedOut()  {
        long currentTime = System.currentTimeMillis();
	if (currentTime > this.maxLifetime) {
		this.markForDelete();
		return true;
        } else if (currentTime > this.timeCreated  +  
		       counter*sipStack.getTransactionTimeout()*1000 ) {
		// Exponential backoff.
		 if (counter < 8 ) counter = counter * 2;
		// Do not mark for deletion in this case!
		 return true;
	} else return false;
    }
    
    protected synchronized void handleTimeOut() {
	   if (this.locked) return;
	   // Request is busy.
	   ListIterator li = listeningPoint.getSipProviders().listIterator();
	   while(li.hasNext()) {
		SipProviderImpl sipProvider = (SipProviderImpl) li.next();
		SipEvent sipEvent = new SipEvent(sipProvider,this.tidLong, 
				this.isServerTransaction);
           	sipProvider.handleEvent(sipEvent);
	   }
    }

    protected void lock() { 
	if (LogWriter.needsLogging()) 
	    LogWriter.logMessage("Locking " + this.tidLong + 
		" isServerTransaction " + this.isServerTransaction );
	locked = true; 
    }

    protected  boolean isLocked() { return locked; }

    protected void unlock() { 
	if (LogWriter.needsLogging()) 
	    LogWriter.logMessage("unlocking " + this.tidLong);
	locked = false; 
    }

    
     
}
