/*
 * ExtensionMethodHandler.java
 *
 * Created on June 26, 2001, 6:02 PM
 */

package examples.proxy;
import gov.nist.sip.stack.*;
import gov.nist.sip.stack.security.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;

/**
 *  This defines an interface whereby applications can register handlers
 *  for message types other than INVITE, ACK, BYE 
 * @author  mranga
 * @version 
 */
public interface ExtensionMethodHandler {
    
    /**
     *Process a request which we do not handle natively in the stack.
     *@param request is the SIPRequest to process.
     *@param messageChannel is the message channel on which we got the request.
     *@param sipStack is our sip stack.
     */

    public void processRequest(SIPRequest request, 
            MessageChannel messageChannel, 
            ServerMain sipStack) 
        throws SIPServerException;

}
