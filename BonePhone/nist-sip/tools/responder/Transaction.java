/*
 * Transaction.java
 *
 * Created on August 13, 2001, 3:20 PM
 */

package tools.responder;
import java.util.*;
import gov.nist.sip.msgparser.*;

/**
 *
 * @author  M. Ranganathan
 * @version
 */
public class Transaction extends Object {
    protected boolean isServerTransaction;
    protected SIPMessage sipMessage;
    protected String     generatedMessage;
    protected String     cancelID;
    protected String     transactionID;
    protected long       timeCreated;
    protected CallFlow    callFlow;
    protected int        delay;
    protected int        repeatInterval;
    protected  Expect     expectNode;
    private   long        nextSend;
    private   int 	  counter;
    private   String      retransmitExpression;
    
    /** Creates new Transaction */
    public Transaction(SIPRequest sipMessage,
    String generatedMessage,
    CallFlow callFlow,
    int  delay,
    boolean isServerTransaction,
    Expect expectNode,
    String retransmitExpression ) {
        Debug.println("Creating new transaction for " + generatedMessage +
        " delay = " + delay);
        if (callFlow == null) 
            throw new IllegalArgumentException("null callflow");
	// Default retransmission interval for transactions 
	if (delay <= 0) delay = EventEngine.DEFAULT_REPEAT_INTERVAL;
	if (sipMessage != null) {
	    this.transactionID = sipMessage.getTransactionId();
	    this.cancelID  = sipMessage.getCancelID();
	}
        this.isServerTransaction =  isServerTransaction;
        this.timeCreated = new Date().getTime();
        this.callFlow = callFlow;
        this.generatedMessage = generatedMessage;
        this.delay = delay;
        this.repeatInterval = EventEngine.theStack.getRepeatInterval() * 1000;
        this.nextSend = timeCreated + delay * 1000;
	this.expectNode = expectNode;
	this.retransmitExpression = retransmitExpression;
	counter = 1;
        
    }

    
    public String getMessage() {
        Debug.println("Checking transaction " + transactionID);
	if (! retransmit()) return null;
        long currentTime = new Date().getTime();
        if (currentTime >= nextSend) {
            nextSend = currentTime  + repeatInterval * counter;
	    // Exponential backoff.
	    counter = 2* counter;
            return generatedMessage;
        } else return null;
    }

    public boolean retransmit() {
	Debug.println("checking for retransmit " + retransmitExpression);
	if (retransmitExpression == null) return false;
	if (retransmitExpression.equals("true")) return true;
	else if (retransmitExpression.equals("false")) return false;
	JythonInterp jythonInterp = callFlow.getJythonInterp();
	return jythonInterp.evalBoolean(retransmitExpression);
    }

    
    
}
