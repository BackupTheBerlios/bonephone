/*
 * MessageFactoryImpl.java
 *
 * Created on July 3, 2001, 9:09 PM
 */

package tools.responder;
import gov.nist.sip.stack.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;

/**
 *
 * @author  mranga
 * @version 
 */
public class MessageFactoryImpl  implements SIPStackMessageFactory  {

    

    
    /**
     * Make a new SIPServerResponse given a SIPRequest and a message 
     * channel.
     */
    public SIPServerRequestInterface 
        newSIPServerRequest(SIPRequest sipRequest,
            MessageChannel msgChan) {
	Debug.println("Got a request ",sipRequest);
        CallID callID = sipRequest.getCallIdHeader();
        String cid = callID.getCallID();
        /** Get the call flow for this call id */
        CallFlow cflow = EventEngine.theStack.getCallFlow(cid);
        cflow.sipMessage = sipRequest;
        cflow.messageChannel = msgChan;
        return cflow;
        
    }
    
    /**
     * Generate a new server response for the stack.
     */
    public SIPServerResponseInterface 
        newSIPServerResponse(SIPResponse sipResponse,
            MessageChannel msgChan) {
	Debug.println("Got a response ",sipResponse);
        CallID callID = sipResponse.getCallIdHeader();
        String cid = callID.getCallID();
        /** Get the call flow for this call id */
        CallFlow cflow = EventEngine.theStack.getCallFlow(cid);
        cflow.sipMessage = sipResponse;
        cflow.messageChannel = msgChan;
        return cflow;
    }
    
}
