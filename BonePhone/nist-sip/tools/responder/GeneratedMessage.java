/******************************************************
 * File: GeneratedMessage.java
 * created 29-Jan-01 6:54:03 PM by mranga
 */


package tools.responder;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.stack.*;
import gov.nist.sip.*;
import java.util.LinkedList;
import gov.nist.sip.net.*;
import org.xml.sax.SAXException;



/**
 *A generated message in our simulator.
 */
public class GeneratedMessage
{
    /** A delay counter. */
    protected int delay;
    
    /**
     *SIPRequest or SIPResponse.
     */
    protected String messageType;

    
    /**
     *The message (once it has been generated)
     */
    protected String sipMessage;
    
    
    /**
     *Call flow from which we are created.
     */
    protected CallFlow callFlow;
    
    /**
     *Message id of this message.
     */
    protected String messageId;
    
    
    /**
     *RequestURI if we want to generate a message with a specific requestURI
     */
    protected String requestURI;
    
    /**
     *Destination for the generated message (specify if different from the
     *proxy address.
     */
    protected String destination;
    
    
    /**
     *statusCode (if this is a SIP response)
     */
    protected int statusCode;
    
    
    /** Request type if this is a SIP request */
    protected String method;
    
    /** recordRoute flag (set to true if recordRouting is enabled) */
    protected boolean recordRouteFlag;
    
    /** A linked list of require headers to send with this message */
    protected LinkedList requireList;
    
    /** A linked list of proxy require headers to send with the generated msg*/
    protected LinkedList proxyRequireList;
    
    
    /** SIPMessageFormatter for formatting outgoing messages. */
    protected SIPMessageFormatter msgFormatter;
    
    /** My expect node. */
    protected Expect expectNode;

    /** My retransmit until expression */
    protected String retransmitUntil;
    
    /** # of times to repeat this message */
    protected int repeats;
    
    protected GeneratedMessage(String id,
		CallFlow cflow, 
		Expect expect,
		String retransmitExpression) 
	throws IllegalArgumentException {
        messageType = Attr.SipRequest;
        callFlow = cflow;
        messageId = id;
        retransmitUntil = retransmitExpression;
        this.expectNode = expect;

    }
    
    
    /** Add a call id header.
     */
    protected void addCallIdHeader() {
        this.msgFormatter.addCallIdHeader();
    }
    
    /** Add a request line.
     */
    protected void addRequestLine(String method, String uri)
        throws SAXException {
	if (method == null) throw new IllegalArgumentException("Null method");
        this.messageType = Attr.SipRequest;
        Debug.println("Add request line " + method + " " + uri + 
		" message ID = " + messageId) ;
	this.method = method;
        if (this.expectNode.enablingEvent == null) {
	    // Adding a request line to send out the initial message.
            if (uri == null) {
		if (method.equals(SIPKeywords.REGISTER)) {
		   this.requestURI = EventEngine.theStack.proxyURI;
		} else {
                   this.requestURI = EventEngine.theStack.requestURI;
		}
            } else {
                this.requestURI = uri;
            }
            if (this.requestURI == null) 
               throw new SAXException
               ("specify requestURI with -requestURI switch or attribute");
            this.msgFormatter = new
                SIPMessageFormatter(EventEngine.theStack,
                    EventEngine.theStack.getDefaultChannel());
            this.msgFormatter.newSIPRequest(method,requestURI);
            this.msgFormatter.addViaHeader(null);
            this.msgFormatter.addCSeqHeader(method);
            this.callFlow.messageChannel = 
               EventEngine.theStack.getDefaultChannel();
            if (method.equals(SIPKeywords.INVITE)) {
                this.msgFormatter.addCallIdHeader();
                EventEngine.theStack.putCallFlow(this.msgFormatter.getCallId(),
                this.callFlow);
            }
        } else {
            // Stash away for processing later when the event occurs.
            this.requestURI = uri;
        }
    }
    
    /** Add a status line.
     */
    protected void addStatusLine(int statusCode)
    throws SAXException {
        if (this.expectNode.enablingEvent == null && 
		this.expectNode.triggerMessage == null) 
                throw new SAXException("Illegal Tag placement:" +
		"need enabling event or triggerMessage in Expect node " +
		"node ID = " + this.expectNode.nodeId);
	this.messageType = Attr.SipResponse;
        Debug.println("add status line " + statusCode);
        this.statusCode = statusCode;
    }
    /** Add a from header to the partially formatted message
     */
    protected void addFromHeader(String displayName,
    String userName, String address) {
        Debug.println("Add From header : " + displayName + " " 
        + userName + " " + address);
        msgFormatter.addFromHeader(displayName,userName,address);
    }
    
    protected void addToHeader(String displayName, String userName,
    String address) {
        Debug.println("Add To header : " + displayName + " " 
        + userName + " " + address);
        msgFormatter.addToHeader(displayName,userName,address);
    }
    
    protected void addHeader(String headerName, String headerBody) {
        msgFormatter.addHeader(headerName,headerBody);
    }
    
    /** Add a contact header to the partially formatted message.
     */
    
    protected void addContactHeader(String displayName, 
        String address, long expiryTime, String action) {
            msgFormatter.addContactHeader(displayName,address,
				expiryTime,action);
    }

    
    
    /**
     *Generate a message from the given message.
     *@param triggerMessage is the message that triggers us (i.e. causes us
     *   to generate a new message).
     */
    protected String generateMessage
    (SIPMessage triggerMessage) {
        // Generating message for the client.
	Debug.println("Generating message for " + expectNode.nodeId);
        if (triggerMessage == null) {
           String tid = msgFormatter.getTransactionId();
           String cid = msgFormatter.getCancelID();
           String retval = msgFormatter.getMessage(false);
	   if (msgFormatter.getRequestMethod().equals("INVITE")) 
	   	callFlow.uriString = msgFormatter.getRequestURI();
           if (   retransmitUntil != null &&
		! this.retransmitUntil.equals("false") && 
		  messageType.equals(Attr.SipRequest)) {
                Transaction transaction =
                    new Transaction(null,retval,callFlow,delay,
				false,expectNode,retransmitUntil);
		transaction.cancelID = cid;
		transaction.transactionID = tid;
		EventEngine.theStack.putTransaction(transaction);
           }
           if (delay == 0) return retval;
           else {
               DeferredSend dsend = new DeferredSend
                                            (retval,callFlow,delay,expectNode);
                EventEngine.theStack.putDeferredSend(dsend);
		return null;
	   }
        }
        String message = null;
        Debug.println("Generating message from " + triggerMessage.encode());
        Debug.println("New method = " + method);
        SIPMessageFormatter msgFormatter =
        new SIPMessageFormatter(EventEngine.theStack,callFlow.messageChannel);
	String uriString;
        
	if (requestURI != null) {
		uriString = requestURI;
	} else if (this.destination != null) {
		uriString = SIPKeywords.SIP+ Separators.COLON + 
			this.destination;
	} else if (triggerMessage.getContactHeaders() != null) {
		Contact contact = (Contact) 
			triggerMessage.getContactHeaders().getFirst();
		uriString = contact.getAddress().getAddrSpec().encode();
		callFlow.uriString = uriString;
	} else if (triggerMessage instanceof SIPRequest) {
		SIPRequest siprequest = (SIPRequest) triggerMessage;
		uriString = siprequest.getRequestURI().encode();
		if (siprequest.getMethod().equals("INVITE")) 
		   callFlow.uriString = uriString;
	} else {
		uriString = callFlow.uriString;
	}
		
            
        if (messageType.equals(Attr.SipRequest)) {
                if (triggerMessage instanceof SIPRequest) {
                    SIPRequest sipRequest = (SIPRequest) triggerMessage;
                    String tid =
                    msgFormatter.newSIPRequest(sipRequest,method,
                    recordRouteFlag, requireList, proxyRequireList);
                    message = msgFormatter.getMessage(tid,false);
		    String cid = msgFormatter.getCancelID();
                   
                    if ( retransmitUntil != null &&
			! this.retransmitUntil.equals("false")) {
                        Transaction transaction = 
                            new Transaction(sipRequest,message,
                                    callFlow,delay,true,expectNode,
				    this.retransmitUntil);
		        transaction.transactionID = tid;
			transaction.cancelID = cid;
                        EventEngine.theStack.putTransaction(transaction);
                    }
                } else {
                    SIPResponse response = (SIPResponse) triggerMessage;
                    String tid = msgFormatter.newSIPRequest(response,
                    		      method,uriString, null,recordRouteFlag,
                    		      requireList,proxyRequireList);
                    message = msgFormatter.getMessage(tid,false);

                }
        } else {
		if (triggerMessage instanceof SIPResponse) 
                    msgFormatter.newSIPResponse
                        (statusCode,(SIPResponse)triggerMessage); 
	        else  {
		   try {
		      msgFormatter.newSIPResponse(statusCode,
				(SIPRequest)triggerMessage,null);
		    } catch (SIPException ex) {
			ServerLog.logException(ex);
			ex.printStackTrace();
			System.exit(0);
		    }
		}
			
	        message = msgFormatter.getMessage();
        }
	
	// If we want to send this later put it in the deferred send queue.
        if (delay != 0) {
            DeferredSend dsend = new DeferredSend
                                       (message,callFlow,delay,expectNode);
            EventEngine.theStack.putDeferredSend(dsend);
       }
            
        if (delay == 0) return message;
        else return null;
            
    }
    
    
}








