
package gov.nist.jain.protocol.ip.sip;
import gov.nist.sip.stack.*;
import gov.nist.sip.msgparser.*;
import jain.protocol.ip.sip.*;
import gov.nist.jain.protocol.ip.sip.message.*;
import java.util.LinkedList;
import java.util.ListIterator;
import gov.nist.log.*;

/**
 * An adapter class from the JAIN implementation objects to the NIST-SIP stack.
 * This is the class that is instantiated by the NistSipMessageFactory to
 * create a new SIPServerRequest or SIPServerResponse.
 * Note that this is not part of the JAIN-SIP spec (it does not implement
 * a JAIN-SIP interface). This is part of the glue that ties together the
 * NIST-SIP stack and event model with the JAIN-SIP stack. Implementors
 * of JAIN services need not concern themselves with this class.
 */
public class NistSipMessageHandlerImpl implements
SIPServerRequestInterface,
SIPServerResponseInterface
{
    
    protected  MessageChannel messageChannel;
    protected SIPRequest sipRequest;
    protected SIPResponse sipResponse;
    protected SipStackImpl sipStack;
    protected ListeningPointImpl listeningPoint;
    /**
     * Process a request.
     *@exception SIPServerException is thrown when there is an error processing
     * the request.
     */
    public void processRequest() throws SIPServerException {
        // Generate the wrapper JAIN-SIP object.
        if (LogWriter.needsLogging())
            LogWriter.logMessage
            ("Got a request " + sipRequest.getTransactionId());
        if (listeningPoint == null) {
            if (LogWriter.needsLogging())
                LogWriter.logMessage
                ("Dropping message: No listening point registered!");
            return;
        }
        RequestImpl reqImpl = new RequestImpl(sipRequest);
        SipStackImpl sipStack = (SipStackImpl) messageChannel.getSIPStack();
        // Look for the registered SIPListener for the message channel.
        synchronized (listeningPoint) {
            LinkedList sipProviders = listeningPoint.getSipProviders();
            ListIterator li = sipProviders.listIterator();
            Transaction transaction = null;
            long tidLong;
            
            if (sipRequest.getRequestLine().
            getMethod().equals(SIPKeywords.ACK)  ) {
                String tidString = sipRequest.getCancelID();
                try {
                    transaction =
                    sipStack.lockAndMarkForDeletion(tidString, false);
                    tidLong = transaction.getTidLong();
                } catch (TransactionDoesNotExistException ex ) {
                    // Could not find transaction. Generate an event
                    // with a null transaction identifier.
                    if (LogWriter.needsLogging())
                        LogWriter.logMessage
                        ("Dropping request: transaction does not exist "
                        + tidString);
                    tidLong = 0;
                }
            } else if ( sipRequest.getRequestLine().getMethod().equals
            (SIPKeywords.CANCEL) ) {
                String tidString = sipRequest.getCancelID();
                try {
                    transaction =
                    sipStack.lockAndMarkForDeletion(tidString, false);
                    tidLong = transaction.getTidLong();
                } catch (TransactionDoesNotExistException ex ) {
                    // Could not find transaction. Generate an event
                    // with a null transaction identifier.
                    if (LogWriter.needsLogging())
                        LogWriter.logMessage
                        ("Dropping request: transaction does not exist");
                    tidLong = 0;
                }
            } else {
                tidLong = sipStack.makeTransaction(reqImpl,listeningPoint,
                			messageChannel,true);
            }
            
            //BUGBUG Stray request handling ??
            while(li.hasNext()) {
                SipProviderImpl sipProvider = (SipProviderImpl) li.next();
                SipEvent sipEvent = new SipEvent(sipProvider,tidLong,reqImpl);
                sipProvider.handleEvent(sipEvent);
            }
        }
    }
    
    /**
     *Process the response.
     *@exception SIPServerException is thrown when there is an error processing
     * the response
     */
    public void processResponse() throws SIPServerException {
        // Generate the wrapper JAIN-SIP object.
        ResponseImpl responseImpl = new ResponseImpl(sipResponse);
        if (LogWriter.needsLogging())  {
            LogWriter.logMessage("Got a response " + sipResponse.encode());
        }
        
        try {
            if (listeningPoint == null) {
                if (LogWriter.needsLogging())
                    LogWriter.logMessage
                    ("Dropping message: No listening point registered!");
                return;
            }
            long tidInt = sipStack.putResponse(responseImpl,false);
            
            synchronized(listeningPoint) {
                ListIterator li =
                listeningPoint.getSipProviders().listIterator();
                while(li.hasNext()) {
                    SipProviderImpl sipProvider = (SipProviderImpl) li.next();
                    SipEvent sipEvent =
                    new SipEvent(sipProvider,tidInt,responseImpl);
                    sipProvider.handleEvent(sipEvent);
                }
            }
        } catch (TransactionDoesNotExistException ex) {
            // Could not find matching request so discard the
            // response.
            return;
        }
        
    }
    /** Get the sender channel.
     */
     public MessageChannel getRequestChannel() { return this.messageChannel; }

     /** Get the channel if we want to initiate a new transaction to the sender of
      *  a response.
      */
      public MessageChannel getResponseChannel() { return this.messageChannel; }
    
}
