
package gov.nist.jain.protocol.ip.sip;
import gov.nist.sip.stack.*;
import gov.nist.sip.msgparser.*;
import jain.protocol.ip.sip.*;
import gov.nist.jain.protocol.ip.sip.message.*;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Implements all the support classes that are necessary for the nist-sip
 * stack on which the jain-sip stack has been based.
 * NOTE: This is not part of the JAIN-SIP specification.
 * This is a mapping class to map from the NIST-SIP abstractions to
 * the JAIN abstractions. It is the glue code that ties
 * the NIST-SIP event model and the JAIN-SIP event model together.
 * When a SIP Request or SIP Response is read from the corresponding
 * messageChannel, the NIST-SIP stack calls the SIPStackMessageFactory 
 * implementation that has been registered with it to process the request.
 *
 * @author  M. Ranganathan
 * @version 1.0
 */
public class NistSipMessageFactoryImpl
implements SIPStackMessageFactory
{
    
    protected	SipStackImpl theStack;
    
    
    /**
     *Construct a new SIP Server Request.
     *@param sipRequest is the SIPRequest from which the SIPServerRequest
     * is to be constructed.
     *@param messageChannel is the MessageChannel abstraction for this
     * 	SIPServerRequest.
     */
    public SIPServerRequestInterface
    newSIPServerRequest
    ( SIPRequest sipRequest, MessageChannel messageChannel ) {
	
        if (messageChannel == null || sipRequest == null ) 
                throw new IllegalArgumentException("Null Arg!");
        
        NistSipMessageHandlerImpl retval = new NistSipMessageHandlerImpl();
        retval.sipRequest = sipRequest;
        retval.messageChannel = messageChannel;
        retval.sipStack = theStack;
	if (messageChannel instanceof TCPMessageChannel) {
	    retval.listeningPoint = theStack.tcpListeningPoint;
	} else retval.listeningPoint = theStack.udpListeningPoint;
        return  retval;
    }
    
    /**
     * Generate a new server response for the stack.
     *@param sipResponse is the SIPRequest from which the SIPServerRequest
     * is to be constructed.
     *@param messageChannel is the MessageChannel abstraction for this
     * 	SIPServerResponse
     */
    public SIPServerResponseInterface
    newSIPServerResponse
    (SIPResponse sipResponse, MessageChannel messageChannel) {
        NistSipMessageHandlerImpl retval = new NistSipMessageHandlerImpl();
        retval.sipResponse = sipResponse;
        retval.messageChannel = messageChannel;
        retval.sipStack = theStack;
	if (messageChannel instanceof TCPMessageChannel) {
	    retval.listeningPoint = theStack.tcpListeningPoint;
	} else retval.listeningPoint = theStack.udpListeningPoint;
        return  retval;
    }


    protected NistSipMessageFactoryImpl(SipStackImpl ourStack) {
		theStack = ourStack;
    }

}
