/*
 * SipProviderImpl.java
 *
 * Created on May 7, 2001, 4:14 PM
 */

package gov.nist.jain.protocol.ip.sip;
import  jain.protocol.ip.sip.*;
import  gov.nist.sip.stack.*;
import  gov.nist.sip.msgparser.*;
import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.net.*;
import  gov.nist.jain.protocol.ip.sip.message.*;
import  gov.nist.jain.protocol.ip.sip.header.*;
import  jain.protocol.ip.sip.message.*;
import  jain.protocol.ip.sip.header.*;
import  java.util.Hashtable;
import  java.util.LinkedList;
import  java.util.ListIterator;
import  java.util.Iterator;
import  java.io.IOException;
import  java.net.InetAddress;
import  gov.nist.log.*;

/** Implementation of the SipProvider interface.
 *
 * @author  M. Ranganathan
 * @version
 */
public class SipProviderImpl implements SipProvider,Runnable  {
    
    
    protected SipStackImpl sipStack;
    
    protected ListeningPointImpl listeningPoint;
    
    private LinkedList  sipListeners;
    
    private LinkedList  pendingEvents;
    
    protected boolean isActive;
    
    
    /**
     *This is the event processor thread. All pending events are enqueued
     *here and processed in the context of a single thread so the listers do
     *not need to do any locking.
     */
    
    public void run() {
        while(true) {
            ListIterator li = null;
            SipEvent sipEvent = null;
            // We have to synchronize on this because we want to
            // prevent addition of new listeners while we are processing
            // the listener list.
            synchronized(this) {
                pendingEvents.clear();
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    LogWriter.logMessage("Interrupted!" );
                    continue;
                }
                // Test if we are being asked to exit.
                // Get this event out of the event list so it is
                // never processed twice.
                ListIterator iterator = pendingEvents.listIterator();
                while (iterator.hasNext()) {
                    sipEvent = (SipEvent) iterator.next();
                    if (LogWriter.needsLogging()) {
                        LogWriter.logMessage("Processing " + sipEvent +
                        "nevents "  + pendingEvents.size());
                    }
                    if (sipEvent == null) {
                        if(LogWriter.needsLogging())
                            LogWriter.logMessage("Exiting provider thread!" );
                        listeningPoint.removeSipProvider(this);
                        return;
                    }
                    
                    li = sipListeners.listIterator();
                    // Deliver the event to each of our listeners.
                    while (li.hasNext()) {
                        SipListener sipListener = (SipListener) li.next();
                        // We need to syhronize on the listener because
                        // the listener may be registered with multiple
                        // providers.
                        synchronized (sipListener) {
                            if (LogWriter.needsLogging())
                                LogWriter.logMessage
                                ("Processing: " + sipEvent +
                                " sipListener = " + sipListener+"/"  +
                                sipEvent.getEventId());
                            int eventId = sipEvent.getEventId();
                            if (eventId == SipEvent.RESPONSE_RECEIVED)  {
                                sipListener.processResponse(sipEvent);
                                long transactionId =
                                sipEvent.getTransactionId();
                                boolean flag =
                                sipEvent.isServerTransaction();
				try {
				   Transaction transaction = 
				   sipStack.getTransaction(transactionId,
					flag);
				   transaction.setResponse
				    ((ResponseImpl)sipEvent.getMessage());
				} catch (TransactionDoesNotExistException ex){
				   continue;
			        }
                                sipStack.unlockTransaction
                                (transactionId,flag);
                            } else
                                if (eventId == SipEvent.REQUEST_RECEIVED) {
                                    sipListener.processRequest(sipEvent);
                                    long transactionId =
                                    sipEvent.getTransactionId();
                                    boolean flag =
                                    sipEvent.isServerTransaction();
                                    sipStack.unlockTransaction
                                    (transactionId,flag);
                                    if (LogWriter.needsLogging())
                                        LogWriter.logMessage
                                        ("Processing done : " + sipEvent +
                                        " sipListener = " + sipListener );
                                } else
                                    if (eventId == SipEvent.TRANSACTION_TIMEOUT) {
                                        boolean flag =
                                        sipEvent.isServerTransaction();
                                        long transactionId =
                                        sipEvent.getTransactionId();
                                        Transaction transaction;
                                        try {
                                            transaction = sipStack.getTransaction
                                            (transactionId,flag);
                                        } catch
                                        (TransactionDoesNotExistException ex) {
                                            continue;
                                        }
                                        // If this event is just for garbage
                                        // collection....
                                        if (!transaction.isPendingDelete()) {
                                            sipListener.processTimeOut(sipEvent);
                                        }
                                        sipStack.unlockTransaction
                                        (transactionId,flag);
                                        if (transaction.hasTimedOut())
                                            sipStack.removeTransaction
                                            (transactionId,flag);
                                    } else {
                                        System.err.println
					("Unexpected event ID! " + eventId);
                                        System.exit(-1);
                                    }
                         }
                    }
                }
            }
        }
    }
    
    
    /**
     * Adds SipListener to list of registered SipListeners of this
     * SipProvider
     * @param sipListener SIP Listener to add.
     * @throws IllegalArgumentException if SipListener is null
     * @throws TooManyListenersException if a limit has placed on number of
     * registered SipListeners for this SipProvider,
     * and this limit has been reached.
     * @throws SipListenerAlreadyRegisteredException if SipListener is
     * already registered with this SipProvider
     */
    public synchronized void  addSipListener(SipListener sipListener)
    throws IllegalArgumentException,
    SipListenerAlreadyRegisteredException {
        if (sipListener == null)
            throw new IllegalArgumentException("Null arg!");
        if (sipListeners.contains(sipListener))
            throw new SipListenerAlreadyRegisteredException();
        if (LogWriter.needsLogging()) {
            LogWriter.logMessage("Adding listener " + sipListener);
        }
        
        sipListeners.add(sipListener);
    }
    
    /**
     * Removes SipListener from list of registered SipListeners of
     * this SipProvider
     * @param sipListener SipListener to remove
     * @throws IllegalArgumentException if SipListener is null
     * @throws SipListenerNotRegisteredException if SipListener is not
     * registered with this SipProvider
     */
    public synchronized void removeSipListener(SipListener sipListener)
    throws IllegalArgumentException,
    SipListenerNotRegisteredException {
        if (LogWriter.needsLogging()) {
            LogWriter.logMessage("Removing listener " + sipListener);
        }
        if (sipListener == null)
            throw new IllegalArgumentException("Null sipListener arg!");
        if (! sipListeners.remove(sipListener) ) {
            throw new
            SipListenerNotRegisteredException("Listener not registered!");
        }
    }
    
    /**
     * Returns SipStack that this SipProvider is attached to.
     * @return the attached SipStack.
     */
    public SipStack getSipStack() {
        return sipStack;
    }
    
    
    /**
     * Returns ListeningPoint of this SipProvider
     * @return ListeningPoint of this SipProvider
     */
    public ListeningPoint getListeningPoint() {
        return listeningPoint;
    }
    
    
    /**
     * Sends specified Response for specified server transaction
     * @param <var>serverTransactionId</var> server transaction
     * id (0 to send Response
     * independently of existing server transaction)
     * @param <var>response</var> the Response to send
     * @throws IllegalArgumentException if response is null or
     * if not from same JAIN SIP implementation
     * @throws TransactionDoesNotExistException if serverTransactionId does not
     * correspond to any server transaction
     * @throws SipException if implementation cannot send response for
     *  any other reason
     */
    public void sendResponse(long serverTransactionId,
    Response response) throws IllegalArgumentException,
    TransactionDoesNotExistException, SipException {
        if (response == null ) {
            throw new IllegalArgumentException("null arg");
        } else if(serverTransactionId <= 0) {
            throw new IllegalArgumentException("bad transaction ID");
        }
        
        ResponseImpl responseImpl;
        try {
            responseImpl = (ResponseImpl) response;
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Bad implementation");
        }
        Transaction transaction =
        sipStack.getTransaction(serverTransactionId,true);
        if (transaction == null) {
            throw new IllegalArgumentException("Bad transaction ID");
        }
        MessageChannel messageChannel = transaction.getMessageChannel();
        SIPResponse sipResponse =
        (SIPResponse) responseImpl.getImplementationObject();
        try {
            messageChannel.sendMessage(sipResponse);
        } catch(IOException ex) {
	    if(LogWriter.needsLogging()){
		LogWriter.logException(ex);
	    }
            SipException sipException = new SipException(ex.getMessage());
            sipException.fillInStackTrace();
            throw sipException;
        }
        sipStack.putResponse(serverTransactionId,responseImpl,true);
    }
    
    /**
     * Sends automatically generated Response for specified server transaction
     * @param <var>serverTransactionId</var> server transaction id
     * @param <var>statusCode</var> the status code of Response
     * @throws TransactionDoesNotExistException if serverTransactionId does not
     * correspond to any server transaction
     * @throws SipParseException if statusCode is not accepted by implementation
     * @throws SipException if implementation cannot send response for any
     * other reason
     */
    public void sendResponse(long serverTransactionId,
    int statusCode)
    throws TransactionDoesNotExistException,
    SipParseException,
    SipException {
        Transaction transaction =
        sipStack.getTransaction(serverTransactionId,true);
        RequestImpl request = transaction.getRequest();
        MessageChannel messageChannel = transaction.getMessageChannel();
        if (request == null) {
            throw new TransactionDoesNotExistException
            ("transaction not found " + serverTransactionId);
        }
        try {
            SIPRequest siprequest = (SIPRequest)
            request.getImplementationObject();
            SIPMessageFormatter msgFormatter =
            new SIPMessageFormatter(sipStack,messageChannel);
            msgFormatter.newSIPResponse(statusCode,siprequest,null);
            String response = msgFormatter.getMessage(false);
            ResponseImpl responseImpl = new ResponseImpl(response);
            messageChannel.sendMessage(msgFormatter);
            sipStack.putResponse(responseImpl,true);
        } catch (SIPException ex) {
            throw new SipParseException(ex.getMessage());
        } catch (Exception ex ) {
	    if(LogWriter.needsLogging()){
		LogWriter.logException(ex);
	    }
            SipException sipException = new SipException(ex.getMessage());
            sipException.fillInStackTrace();
            throw sipException;
        }
    }
    
    /**
     * Sends automatically generated Response for specified server transaction
     * @param <var>serverTransactionId</var> server transaction id
     * @param <var>statusCode</var> the status code of Response
     * @param <var>body</var> body of Response
     * @param <var>bodyType</var> body type used to create ContentTypeHeader of
     * Response
     * @param <var>bodySubType</var> body sub-type used to create
     * ContentTypeHeader of Response
     * @throws IllegalArgumentException if body, bodyType
     * or bodySubType are null
     * @throws TransactionDoesNotExistException if serverTransactionId does not
     * correspond to any server transaction
     * @throws SipParseException if statusCode, body, bodyType or bodySubType
     * are not accepted by implementation
     * @throws SipException if implementation cannot send response for
     * any other reason
     */
    public void sendResponse(long serverTransactionId,
    int statusCode,
    byte[] body,
    String bodyType,
    String bodySubType)
    throws IllegalArgumentException,
    TransactionDoesNotExistException,
    SipParseException,
    SipException {
        Transaction transaction =
        sipStack.getTransaction(serverTransactionId,true);
        RequestImpl request = transaction.getRequest();
        MessageChannel messageChannel = transaction.getMessageChannel();
        if (body == null || bodyType == null)
            throw new IllegalArgumentException("null args!");
        if (request == null)
            throw new TransactionDoesNotExistException
            ("Transactin not found " + serverTransactionId);
        try {
            SIPRequest sipRequest =
            (SIPRequest) request.getImplementationObject();
            SIPMessageFormatter msgformatter =
            new SIPMessageFormatter(sipStack,messageChannel);
            
            msgformatter.newSIPResponse
               (statusCode,sipRequest,body,bodyType,bodySubType);
	    String response = msgformatter.getMessage(false);
	    if (LogWriter.needsLogging()) {
		LogWriter.logMessage("response = " + response);
	    }
            messageChannel.sendMessage(msgformatter);
            ResponseImpl responseImpl = new ResponseImpl(response);
            sipStack.putResponse(responseImpl,true);
        } catch (SIPException ex) {
            throw new SipParseException("Illegal status code");
        }  catch (Exception ex) {
	    if(LogWriter.needsLogging()){
		LogWriter.logException(ex);
	    }
            throw new
            SipException(ex.getMessage());
        }
        
    }
    
    /**
     * Returns Request associated with specified transaction
     * @param <var>transactionId</var> transaction id
     * @param <var>isServerTransaction</var> boolean value to indicate if
     * transactionId represents a server transaction
     * @return Request associated with specified transaction
     * @throws TransactionDoesNotExistException if transactionId does not
     * correspond to a transaction
     */
    public Request getTransactionRequest(long transactionId,
    boolean isServerTransaction)
    throws TransactionDoesNotExistException {
        RequestImpl message =
        sipStack.getRequest(transactionId,isServerTransaction);
        if (message == null) {
            throw new
            TransactionDoesNotExistException("Message not found!");
        }
        return message;
    }
    
    
    /**
     * Sends specified Request and returns ID of implicitly created
     * client transaction.
     * @return client transaction id (unique to underlying SipStack)
     * @param request Request to send
     * @throws IllegalArgumentException if request is null or not from
     * same SIP implementation as SipProvider
     * @throws SipException if implementation cannot send request for any
     * other reason
     */
    public long sendRequest(Request request)
    throws IllegalArgumentException, SipException {
        if (request == null)
            throw new IllegalArgumentException("null arg");
        NistJAINMessage nmessage;
        try {
            nmessage = (NistJAINMessage) request;
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("bad implementation");
        }
        SIPRequest sip_message = (SIPRequest)nmessage.getImplementationObject();
        // Put this into the client transaction table.
        RequestLine requestLine = sip_message.getRequestLine();
        if (requestLine == null) {
            throw new SipException("No request line!");
        }
        gov.nist.sip.net.URI requestURI = requestLine.getUri();
        if (requestURI == null) {
            throw new SipException("Null request URI!");
        }
        // Check if the cseq number is monotonically increasing.
        // and set it if it is so....
        long seqno = sip_message.getCSeqHeader().getSeqno();
        sipStack.setCSeq(seqno);
        
        // This policy method returns a list of locations to try in
        // succession. The default one just returns the address in
        // the SIP if it is directly indicated, followed by a proxy
        // address. The list is in decreasing order of priority.
        Iterator iterator = sipStack.getNextHop(sip_message);
        
        if (iterator == null)
            throw new
            SipException("Could not find route: check configuration!");
        
        boolean done = false;
        MessageChannel mchan = null;
        while(iterator.hasNext()) {
            try {
                Hop hop = (Hop) iterator.next();
                InetAddress addr = InetAddress.getByName(hop.getHost());
                if (LogWriter.needsLogging()) {
                    LogWriter.logMessage("Trying to send to " +
                    addr  + " " + hop.getPort());
                }
                mchan = sipStack.createMessageChannel(addr,hop.getPort(),
                hop.getTransport());
                mchan.sendMessage(sip_message,addr,hop.getPort());
                done = true;
                break;
            } catch (IOException ex) {
                // Ignore the error and carry on bravely.
		if (LogWriter.needsLogging()) {
		    LogWriter.logException(ex);
	        }
                continue;
                
            }
            
        }
        
        
        if ( ! done) {
            throw new SipException("Could not send out message!");
        }
        
        long tid = sipStack.makeTransaction
        ((RequestImpl)request,this.listeningPoint,mchan,false);
        return tid;
    }
    
    /**
     * Sends automatically generated ACK Request to the recipient of the
     * INVITE Request associated with specified client transaction
     * @param <var>clientTransactionId</var> client transaction id
     * @return client transaction id (unique to the underlying SipStack)
     * @throws TransactionDoesNotExistException if clientTransactionId does not
     * correspond to any client transaction
     * @throws SipException if implementation cannot send ack for any
     * other reason
     */
    public long sendAck(long clientTransactionId)
    throws TransactionDoesNotExistException, SipException {
        
        Transaction transaction =
        sipStack.getTransaction(clientTransactionId,false);
        RequestImpl request = transaction.getRequest();
        MessageChannel messageChannel = transaction.getMessageChannel();
	ResponseImpl response = transaction.getResponse();
        SIPRequest sipRequest =
            (SIPRequest) request.getImplementationObject();
	SIPResponse sipResponse =
		(SIPResponse) response.getImplementationObject();
        try {
            
            SIPMessageFormatter msgFormatter =
            new SIPMessageFormatter(sipStack, messageChannel);
	    String tid;
	    if (response.hasContactHeaders()) {
		 Contact contact = (Contact) sipResponse.getContactHeaders().getFirst();
		 URI uri = contact.getAddress().getAddrSpec();
		 tid = msgFormatter.newSIPRequest(sipResponse,
			Request.ACK,
			uri.encode(),
			null,sipStack.getRecordRouteFlag(), 
			null, null);
	    } else {
               tid = msgFormatter.newSIPRequest(sipRequest,
            	     Request.ACK,sipStack.getRecordRouteFlag());
	    }
            // Retrieve the message from the returned transaction id.
            String ack = msgFormatter.getMessage(tid,false);
            // Parse the generated message (so we can put it in the
            // transaction table.
            RequestImpl requestImpl = new RequestImpl(ack);
            // make a client transaction record for this message.
            long retval = sipStack.makeTransaction
            (requestImpl,this.listeningPoint,
            messageChannel, false);
            // send the message to the recipient.
            messageChannel.sendMessage(msgFormatter);
            return retval;
        } catch (Exception ex) {
            SipException sipException = new SipException(ex.getMessage());
            sipException.fillInStackTrace();
            throw sipException;
        }
        
        
    }
    
    /**
     * Sends automatically generated Response for specified server transaction
     * @param <var>serverTransactionId</var> server transaction id
     * @param <var>statusCode</var> the status code of Response
     * @param <var>body</var> body of Response
     * @param <var>bodyType</var> body type used to create ContentTypeHeader of
     * Response
     * @param <var>bodySubType</var> body sub-type used to create
     *   ContentTypeHeader of Response
     * @throws IllegalArgumentException if body, bodyType
     * or bodySubType are null
     * @throws TransactionDoesNotExistException if serverTransactionId does not
     * correspond to any server transaction
     * @throws SipParseException if statusCode, body, bodyType or bodySubType
     * are not accepted by implementation
     * @throws SipException if implementation cannot send response
     * for any other reason
     */
    public void sendResponse(long serverTransactionId,
    int statusCode,
    String body,
    String bodyType,
    String bodySubType)
    throws IllegalArgumentException,
    TransactionDoesNotExistException,
    SipParseException,
    SipException {
        
        if (body == null  || bodyType == null || bodySubType == null) 
		throw new IllegalArgumentException("null arg");
        if (serverTransactionId <= 0)
            throw new IllegalArgumentException("bad server TID");
        Transaction transaction =
        sipStack.getTransaction(serverTransactionId,true);
        MessageChannel messageChannel = transaction.getMessageChannel();
        RequestImpl request = transaction.getRequest();
        
        SIPMessageFormatter sipMsgFormatter =
        new SIPMessageFormatter(sipStack,messageChannel);
        SIPRequest sipRequest =
        (SIPRequest)request.getImplementationObject();
        try {
            sipMsgFormatter.newSIPResponse(statusCode,sipRequest,null);
        } catch (SIPException ex) {
            throw new SipParseException(ex.getMessage());
        }
	if (! body.equals("") ) {
           sipMsgFormatter.addContentTypeHeader(bodyType,bodySubType);
           sipMsgFormatter.addMessageContent(body);
	}
        try {
            messageChannel.sendMessage(sipMsgFormatter);
        } catch (IOException ex) {
            SipException sipException = new SipException(ex.getMessage());
            sipException.fillInStackTrace();
            throw sipException;
        }
    }
    
    /**
     * Sends automatically generated ACK Request to the recipient
     * of the INVITE Request
     * associated with specified client transaction
     * @param <var>clientTransactionId</var> client transaction id
     * @param <var>body</var> body of AckMessage
     * @param <var>bodyType</var> body type used to create ContentTypeHeader of
     * AckMessage
     * @param <var>bodySubType</var> body sub-type used to create
     * ContentTypeHeader of AckMessage
     * @return client transaction id (unique to the underlying SipStack)
     * @throws IllegalArgumentException if body, bodyType
     * or bodySubType are null
     * @throws TransactionDoesNotExistException if clientTransactionId does not
     * correspond to any client transaction
     * @throws SipParseException if body, bodyType or
     * bodySubType are not accepted by implementation
     * @throws SipException if implementation cannot send ack for any
     *   other reason
     */
    public long sendAck(long clientTransactionId,
    String body,
    String bodyType,
    String bodySubType)
    throws IllegalArgumentException,
    TransactionDoesNotExistException,
    SipParseException, SipException {
        if (bodyType == null || bodySubType == null || body == null ) {
            throw new IllegalArgumentException("null arg");
        }
        
        Transaction transaction =
        sipStack.getTransaction(clientTransactionId,false);
        MessageChannel messageChannel = transaction.getMessageChannel();
        RequestImpl requestImpl = transaction.getRequest();
        SIPRequest sipRequest =
        (SIPRequest) requestImpl.getImplementationObject();
	ResponseImpl responseImpl = transaction.getResponse();
	SIPResponse sipResponse =
        (SIPResponse) responseImpl.getImplementationObject();
        try {
            SIPMessageFormatter msgFormatter =
            new SIPMessageFormatter(sipStack, messageChannel);
	    String tid;
	    if (responseImpl.hasContactHeaders()) {
		 Contact contact = 
			(Contact) sipResponse.getContactHeaders().getFirst();
		 URI uri = contact.getAddress().getAddrSpec();
		 tid = msgFormatter.newSIPRequest(sipResponse,
			Request.ACK,
			uri.encode(),
			null,sipStack.getRecordRouteFlag(), 
			null, null);
	    } else {
               tid = msgFormatter.newSIPRequest(sipRequest,
            	     Request.ACK,sipStack.getRecordRouteFlag());
	    }


            msgFormatter.addContentTypeHeader(bodyType,bodySubType);
            msgFormatter.addMessageContent(body);
            // Retrieve the message from the returned transaction id.
            String ack = msgFormatter.getMessage(tid,false);
            // Generate a MessageImpl object.
            RequestImpl ackImpl = new RequestImpl(ack);
            // Generate a new server transaction record.
            long retval = sipStack.makeTransaction(ackImpl,
            this.listeningPoint,messageChannel,false);
            // send the message to the recipient.
            messageChannel.sendMessage(msgFormatter);
            return retval;
        } catch (Exception ex) {
            SipException sipException = new SipException(ex.getMessage());
            sipException.fillInStackTrace();
            throw sipException;
        }
        
        
        
    }
    
    /**
     * Returns new CallIdHeader (unique to SipProvider)
     * @return new CallIdHeader (unique to SipProvider)
     * @throws SipException if SipProvider cannot generate a new CallIdHeader
     */
    public CallIdHeader getNewCallIdHeader() throws SipException {
        SIPMessageFormatter messageFormatter =
        new SIPMessageFormatter(sipStack,null);
        messageFormatter.addCallIdHeader();
        String callIdString = messageFormatter.getHeaders().trim();
        StringMsgParser stringParser = new StringMsgParser();
        try {
            SIPHeader cidHeader = stringParser.parseSIPHeader(callIdString);
            CallIdHeaderImpl cidHeaderImpl = new CallIdHeaderImpl();
            cidHeaderImpl.setImplementationObject(cidHeader);
            return cidHeaderImpl;
        } catch (SIPParseException ex) {
            ex.printStackTrace();
            System.exit(0);
            return null;
        }
    }
    
    /**
     * Sends automatically generated ACK Request to the recipient of
     * the INVITE Request
     * associated with specified client transaction
     * @param <var>clientTransactionId</var> client transaction id
     * @param <var>body</var> body of AckMessage
     * @param <var>bodyType</var> body type used to create ContentTypeHeader of
     * AckMessage
     * @param <var>bodySubType</var> body sub-type used to create
     * ContentTypeHeader of
     * AckMessage
     * @return client transaction id (unique to the underlying SipStack)
     * @throws IllegalArgumentException if body, bodyType
     * or bodySubType are null
     * @throws TransactionDoesNotExistException if clientTransactionId does not
     * correspond to any client transaction
     * @throws SipParseException if body, bodyType or
     * bodySubType are not accepted by implementation
     * @throws SipException if implementation cannot send ack for any other
     *         reason
     */
    public long sendAck(long clientTransactionId,byte[] body,
    String bodyType,
    String bodySubType) throws
    IllegalArgumentException,
    TransactionDoesNotExistException,
    SipParseException, SipException {
        if (body == null || bodyType == null || bodySubType == null)
            throw new IllegalArgumentException ("null arg!");
        Transaction transaction =
        sipStack.getTransaction(clientTransactionId,false);
        MessageChannel messageChannel = transaction.getMessageChannel();
        RequestImpl requestImpl = transaction.getRequest();
        SIPRequest sipRequest =
        (SIPRequest) requestImpl.getImplementationObject();
	ResponseImpl responseImpl = transaction.getResponse();
	SIPResponse sipResponse = 
	(SIPResponse) responseImpl.getImplementationObject();
        try {
            SIPMessageFormatter msgFormatter =
            new SIPMessageFormatter(sipStack, messageChannel);

	    String tid;
	    if (responseImpl.hasContactHeaders()) {
		 Contact contact = (Contact) 
			sipResponse.getContactHeaders().getFirst();
		 URI uri = contact.getAddress().getAddrSpec();
		 tid = msgFormatter.newSIPRequest(sipResponse,
			Request.ACK,
			uri.encode(),
			null,sipStack.getRecordRouteFlag(), 
			null, null);
	    } else {
               tid = msgFormatter.newSIPRequest(sipRequest,
            	     Request.ACK,sipStack.getRecordRouteFlag());
	    }

            msgFormatter.addMessageContent(bodyType,bodySubType,body);
            // Retrieve the message from the returned transaction id.
            String ack = msgFormatter.getMessage(tid,false);
            // Generate a MessageImpl object.
            RequestImpl ackImpl = new RequestImpl(ack);
            // Generate a new server transaction record.
            long retval = sipStack.makeTransaction(ackImpl,
            this.listeningPoint,messageChannel,false);
            // send the message to the recipient.
            messageChannel.sendMessage(msgFormatter);
            return retval;
        } catch (Exception ex) {
            SipException sipException = new SipException(ex.getMessage());
            sipException.fillInStackTrace();
            throw sipException;
        }

    }
    
    /**
     * Returns most recent Response associated with specified transaction
     * (Returns null if there is no Response associated with specified
     * transaction).
     * @param <var>transactionId</var> transaction id
     * @return the Request associated with server transaction
     * @param <var>isServerTransaction</var> boolean value to indicate if
     * transactionId represents a server transaction
     * @throws TransactionDoesNotExistException if transactionId does not
     * correspond to a transaction
     */
    public Response getTransactionResponse(long transactionId,
    boolean isServerTransaction)
    throws TransactionDoesNotExistException {
        return sipStack.getResponse(transactionId,isServerTransaction);
    }
    
    /**
     * Sends automatically generated CANCEL Request to the recipient
     * of the Request associated with specified client transaction
     * @param <var>clientTransactionId</var> client transaction id
     * @throws TransactionDoesNotExistException if clientTransactionId does not
     * correspond to any client transaction
     * @throws SipException if implementation cannot send
     *  cancel for any other reason
     */
    public long sendCancel(long clientTransactionId)
    throws TransactionDoesNotExistException, SipException {
        if (LogWriter.needsLogging()) {
            LogWriter.logMessage("sendCancel  clientTransactionId  = "  +
            clientTransactionId);
        }
        
        Transaction transaction =
        sipStack.getTransaction(clientTransactionId, false);
        
        RequestImpl request = transaction.getRequest();
        MessageChannel messageChannel = transaction.getMessageChannel();
        SIPRequest sipRequest = (SIPRequest) request.getImplementationObject();
        
        
        try {
            SIPMessageFormatter msgFormatter =
            new SIPMessageFormatter(sipStack,messageChannel);
            String tid =
            msgFormatter.newSIPRequest(sipRequest,Request.CANCEL,
            sipStack.getRecordRoute());
            String cancel = msgFormatter.getMessage(tid,false);
            RequestImpl requestImpl = new RequestImpl(cancel);
            if (LogWriter.needsLogging()) {
                LogWriter.logMessage("Cancel = " + requestImpl);
            }
            // generate a new server transaction id for this
            // request.
            long retval =
            sipStack.makeTransaction(requestImpl,
            this.listeningPoint,messageChannel,false);
            messageChannel.sendMessage(msgFormatter);
            return retval;
        } catch (Exception ex) {
            ex.printStackTrace();
            SipException sipException = new SipException
            (ex.getClass().getName()  + ":" + ex.getMessage());
            sipException.fillInStackTrace();
            throw sipException;
        }
    }
    
    
    /**
     * Sends automatically generated BYE Request based on specified client or
     * server transaction (Note it is assumed that the specified transaction is
     * the most recent one of the call-leg)
     * @param <var>transactionId</var> transaction id
     * @param <var>isServertransactionId</var> boolean value indicating if
     * transactionId represents a server transaction
     * @return client transaction id (unique to the underlying SipStack)
     * @throws TransactionDoesNotExistException if transactionId does not
     * correspond to client or server transaction
     * @throws SipException if implementation cannot send bye for any
     * other reason
     */
    public long sendBye(long transactionId,
    boolean isServerTransaction)
    throws TransactionDoesNotExistException, SipException {
        
        if (LogWriter.needsLogging()) 
		LogWriter.logMessage
		("sendBye (transactionId,isServerTransaction) " 
		+ transactionId + " , " + isServerTransaction );
        Transaction transaction =
        	sipStack.getTransaction(transactionId,isServerTransaction);

        if (LogWriter.needsLogging()) 
		LogWriter.logMessage("transaction " + transaction );
        
        RequestImpl request = transaction.getRequest();
        
        MessageChannel messageChannel = transaction.getMessageChannel();
        
        SIPRequest sipRequest = (SIPRequest) 
		request.getImplementationObject();
	ResponseImpl responseImpl = transaction.getResponse();
	SIPResponse sipResponse = (SIPResponse) 
		responseImpl.getImplementationObject();
        try {
            SIPMessageFormatter msgFormatter =
            new SIPMessageFormatter(sipStack,messageChannel);
	    String tid;
	    if (responseImpl.hasContactHeaders()) {
		 Contact contact = (Contact) 
			sipResponse.getContactHeaders().getFirst();
		 URI uri = contact.getAddress().getAddrSpec();
		 tid = msgFormatter.newSIPRequest(sipResponse,
			Request.BYE,
			uri.encode(),
			null,sipStack.getRecordRouteFlag(), 
			null, null);
	    } else {
               tid = msgFormatter.newSIPRequest(sipRequest,
            	     Request.BYE,sipStack.getRecordRouteFlag());
	    }


            String bye = msgFormatter.getMessage(tid,false);
            RequestImpl requestImpl = new RequestImpl(bye);
            // generate a new client transaction id for this
            // request.
	    if (LogWriter.needsLogging()) {
		String messageToSend = msgFormatter.getMessage(tid,false);
		LogWriter.logMessage("messageToSend = " + messageToSend);
	    }
            long retval = sipStack.makeTransaction
            (requestImpl,this.listeningPoint,messageChannel,
            isServerTransaction);
            messageChannel.sendMessage(msgFormatter);
            return retval;
        } catch (Exception ex) {
            SipException sipException = new SipException(ex.getMessage());
            sipException.fillInStackTrace();
            throw sipException;
        }
    }
    
    
    ////////////////////////////////////////////////////////////////
    // Following are implementation specific protected methods.
    
    /**
     * Constructor.
     */
    protected  SipProviderImpl
    ( ListeningPointImpl lPoint, SipStackImpl stack ) {
        this.listeningPoint = lPoint;
        this.sipStack = stack;
        this.pendingEvents = new LinkedList();
        this.isActive = true;
        this.sipListeners = new LinkedList();
        // Start our event thread running.
        Thread eventThread = new Thread(this);
        eventThread.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
        }
        
    }
    
    protected synchronized  void exit() {
        this.isActive = false;
        pendingEvents.add(null);
        this.notify();
    }
    
    
    
    /**
     *Set the sipStack member.
     *@param <var>sipStack</var> SipStack to set.
     */
    protected void setSipStack(SipStackImpl stack ) {
        sipStack = stack;
    }
    
    /**
     *handle a transaction timeout event.
     *Enqueues a message in our event queue and notifies the event processor
     *thread.
     */
    protected void handleEvent(SipEvent sipEvent) {
        if (LogWriter.needsLogging()) {
            LogWriter.logMessage("handleEvent " + sipEvent + " / " +
            sipEvent.getEventId());
            LogWriter.logStackTrace();
        }
        if ( (sipEvent.getEventId() == SipEvent.REQUEST_RECEIVED ||
        sipEvent.getEventId() == SipEvent.RESPONSE_RECEIVED ) &&
        sipEvent.getMessage() == null ) {
            if ( LogWriter.needsLogging() )  {
                LogWriter.logStackTrace();
                System.exit(0);
            }
            new Exception().printStackTrace();
            System.exit(0);
        }
        synchronized (this) {
	    if (LogWriter.needsLogging())
              LogWriter.logMessage("handleEvent " + pendingEvents.size() );
            pendingEvents.add(sipEvent);
            this.notify();
        }
    }
    
    
}
