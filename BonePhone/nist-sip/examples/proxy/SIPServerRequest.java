/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).       *
 * See ../../../../doc/uncopyright.html for conditions of use.                 *
 * Author: M. Ranganathan (mranga@nist.gov)                                    *
 * Modified by: Marc Bednarek (bednarek@nist.gov)                              *
 *  -- added firewall support and debugged.   				       *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                              *
 *******************************************************************************/
/******************************************************
 * File: ServerRequest.java
 * created 29-Aug-00 5:53:31 PM by M. Ranganathan (mranga@nist.gov)
 * Modified by: Marc Bednarek (bednarek@nist.gov)
 */


package examples.proxy;
import gov.nist.sip.stack.*;
import gov.nist.sip.stack.security.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.msgparser.*;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.Enumeration;
import java.util.Iterator;
import java.net.InetAddress;
import java.util.StringTokenizer;
import  java.io.*;

/**
 * This class has methods that that handle SIPRequests.
 * This is class that implements functions common to both the UDP and
 * the TCP transports.
 *@author <A href=mailto:mranga@nist.gov> M. Ranganathan </A>
 */
class SIPServerRequest  implements
SIPRequestTypes, SIPErrorCodes, SIPServerRequestInterface
{
    protected  SIPRequest 		request;
    protected  MessageChannel   	messageChannel;
    protected  ServerMain		sipStack;
    
        /**
         *Handle a sipStack request that is sent to us for processing.
         *In the case of TCP requests, this is called with a TCPMessageChannel
         *In the case of UDP requests, this is called with a UDPMessageChannel
         *@param siprequest is the SIPRequest structure that we have to proces
         *@param mchannel is the transport abstraction (UDP/TCP) that we call
         *		to dispatch messages etc.
         */
    
    public
    SIPServerRequest( SIPRequest siprequest, MessageChannel mchannel )
    {
        
        if (mchannel == null || siprequest == null) {
            throw new IllegalArgumentException ("null arg ");
        }
        sipStack = (ServerMain) mchannel.getSIPStack();
        messageChannel = mchannel;
        request = siprequest;
    }
    
        /**
         * Process the current request.
         * @throws SIPServerException If there was
         * an error processing the request.
         */
    public void processRequest() throws SIPServerException {
        if ( ServerLog.needsLogging(ServerLog.TRACE_MESSAGES) ) {
             ServerLog.logMessage(request, 
		messageChannel.getViaHost() + ":"
		 + messageChannel.getViaPort(),
	         sipStack.getHostAddress()  + ":" + 
	         sipStack.getPort(messageChannel.getTransport()), false);
	}
        RequestLine rl = request.getRequestLine();
        String method = rl.getMethod();
        if (method.compareTo(REGISTER) == 0 ) {
            handleRegistrationRequest();
        } else if (method.compareTo(INVITE) == 0) {
            handleInviteRequest();
        } else if (method.compareTo(ACK) == 0) {
            handleAckRequest();
        } else if (method.compareTo(BYE) == 0 ) {
            handleByeRequest();
        } else if (method.compareTo(CANCEL) == 0 ) {
            handleCancelRequest();
        }  else {
            // send back a not (501) implemented response
            handleUnknownRequest();
        }
    }
    
    
        /**
         * Add a list of contact records to the header given a
         * 	ServerContactList
         * @param clist Contact lists to add
         * @param mformatter Message formatter that contains the (partially)
         * formatted message.
         */
    protected void addContactHeaders
    (ServerContactList clist, SIPMessageFormatter mformatter ) {
        if (clist == null) return;
        ServerContactRecord cr;
        ListIterator li = clist.getIterator();
        try {
            for(cr = (ServerContactRecord)li.next();
            cr != null; cr = (ServerContactRecord)li.next() ) {
                long currentTime = new java.util.Date().getTime();
                long expiryTimeSec = ( cr.expiryTimeMilis -
                currentTime )/1000;
                if (expiryTimeSec < 0 ) expiryTimeSec = 0;
                Address contactAddress =
                cr.getContact().getAddress();
                mformatter.addContactHeader(contactAddress,
                expiryTimeSec, cr.action);
            }
        } catch ( NoSuchElementException ex) {
            return;
        }
    }
    
        /**
         * handle the BYE request.
         * @throws SIPServerException If there was any trouble processing
         *  the BYE request
         */
    protected void
    handleByeRequest() throws SIPServerException {
        
        // close the opened ports in the firewall
        String destination = getRawIpDestinationAddress(sipStack);
        String source = messageChannel.getRawIpSourceAddress();

	CallRecord cr = sipStack.getCallRecord(request);
	if (cr == null) {
	   if (ServerLog.needsLogging()) 
		ServerLog.logMessage 
		("Dropping request transaction not found." );
	   return;
	}
	// Get the transaction recrod
	TransactionRecord tr = cr.getTransactionRecord();
	if (ServerLog.needsLogging()) 
		ServerLog.logMessage("Corresponding Invite Transaction ID "  +
			tr.getTransactionID());
	
	Iterator li = tr.getForwardedRequests();
        SIPMessageFormatter mformatter =
            new SIPMessageFormatter(sipStack,messageChannel);
	String tid = mformatter.newSIPRequest
		(request,null,sipStack.getRecordRoute(),
			null,null);

	int senderPort = messageChannel.getViaPort();
	String senderHost = messageChannel.getViaHost();
        MessageChannel inviteChannel = tr.getMessageChannel();
	String inviteHost = inviteChannel.getViaHost();
	int     invitePort = inviteChannel.getViaPort();

	TransactionRecord newTransaction = 
		sipStack.transactionHandler.makeRecievedQueue
			(request,this.messageChannel);
	sipStack.transactionHandler.putInForwardedQueue(tid,newTransaction);

	String nrequest = mformatter.getMessage(false);
	String cseq = mformatter.getCSeq();
	String callId = mformatter.getCallId();
	String firstLine = mformatter.getFirstLine();

	if (ServerLog.needsLogging()) {
		ServerLog.logMessage("senderHost/port = " + senderHost + 
			"/" + senderPort);
		ServerLog.logMessage("inviteHost/port = " + inviteHost + 
			"/" + invitePort);
	}

	try {
	   // Send the BYE to the originator of the INVITE.
	   if ( !(inviteHost.equals(senderHost)) || invitePort != senderPort ) {
	        if (ServerLog.needsLogging()) {
		    ServerLog.logMessage("Sending BYE " 
			+ tr.getMessageChannel().getKey());
	        }
	        inviteChannel.sendMessage(mformatter);
	       // Make a new transaction queue for the bye request (because the
	        // BYE receives an OK later).
	         ForwardedRequest freq = new ForwardedRequest();
	        freq.newRequest = nrequest;
	        freq.branchID = mformatter.getBranchId();
	        freq.originalRequest = request;
		HostPort hp = inviteChannel.getViaHostPort();
	        freq.sentTo = hp;
		newTransaction.addRequest(freq);
	   }
	} catch (IOException ex) {}



	
	// Send it to each place where the original invite was forwarded.
	while (li.hasNext()) {
		ForwardedRequest fwr = (ForwardedRequest) li.next();
		HostPort hp = fwr.getSentTo();
	        String transport = fwr.getTransport();
                String hisHost = hp.getHost().encode();
	        int hisPort = ! hp.hasPort() ? 5060: hp.getPort();
	        try {
		   if (ServerLog.needsLogging()) {
			ServerLog.logMessage("hisHost/port = " + hisHost + "/"
				+ hisPort);
		   }
		   if ( ( ! senderHost.equals(hisHost) ) ||  
			senderPort != hisPort ) {
		        IOHandler.sendRequest(hp,transport,nrequest);
	                // Log the message for the trace viewer.
		        if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES)) {
	                    String myAddress = sipStack.getHostAddress() + ":" +
				sipStack.getPort(messageChannel.getTransport());
			    String hisAddress = hisHost + ":" + hisPort;
			    ServerLog.logMessage(nrequest,
				myAddress, hisAddress, 
				cseq,false,callId,firstLine);
	                 }
	                 ForwardedRequest freq = new ForwardedRequest();
	                 freq.newRequest = nrequest;
	                 freq.branchID = mformatter.getBranchId();
	                 freq.originalRequest = request;
	                 freq.sentTo = hp;
		         newTransaction.addRequest(freq);
		    }
		} catch (IOException ioex) {
		   if (ServerLog.needsLogging(ServerLog.TRACE_EXCEPTION)) {
			ioex.printStackTrace();
			ServerLog.logException(ioex);
		   }
		   throw new SIPServerException(ioex.getMessage());
		}
	}

	// Get rid of the Call record.
        sipStack.sessionClosing(source, destination, request);
        
    }
    
        /**
         * Handle the ACK request. Just forward the request.
         * @throws SIPServerException If there was a problem processing
         * the ACK request
         */
    protected void
    handleAckRequest() throws SIPServerException {
	/** Get the record corresponding to the invite */
	TransactionRecord tr = 
		sipStack.transactionHandler.getTransactionRecord(request);
	if (tr == null) {
	   if (ServerLog.needsLogging()) 
		ServerLog.logMessage
		("Dropping request transaction not found." );
	   throw new SIPServerException("Dropping ACK Transaction not found ");
	}
	Iterator li = tr.getForwardedRequests();
        SIPMessageFormatter mformatter =
            new SIPMessageFormatter(sipStack,messageChannel);
	mformatter.newSIPRequest(request,null,sipStack.getRecordRoute(),
			null,null);
	String nrequest = mformatter.getMessage();
	String cseq = mformatter.getCSeq();
	String callId = mformatter.getCallId();
	String firstLine = mformatter.getFirstLine();
	String myAddress = sipStack.getHostAddress() + ":" + 
			   sipStack.getPort(messageChannel.getTransport());
	while (li.hasNext()) {
		ForwardedRequest fwr = (ForwardedRequest) li.next();
		HostPort hp = fwr.getSentTo();
		String hisaddress = hp.getHost().getIpAddress();
		// Create a call record and possibly open up the firewall
		// if needed.
                sipStack.sessionOpening(messageChannel.getRawIpSourceAddress(),
		        hisaddress, request, tr);
	        String transport = fwr.getTransport();
	        try {
		   IOHandler.sendRequest(hp,transport,nrequest);
	           // Log the message
		   if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES)) {
	               String hisAddress = hp.encode();
	               if (!hp.hasPort()) {
			      hisAddress += ":" + 5060;
			}
			ServerLog.logMessage(nrequest,
				myAddress, hisAddress, 
				cseq,false,callId,firstLine);
	            }

		} catch (IOException ioex) {
		   throw new SIPServerException(ioex.getMessage());
		}
	}
	if (ServerLog.needsLogging()) 
	    ServerLog.logMessage("Invite MChannel = "  +
			tr.getMessageChannel().getSenderAddress() + "/" +
			tr.getMessageChannel().getSenderPort() );
    }
    
    /**
     * Handle the CANCEL request. Send back an OK and forward the request.
     * @throws SIPServerException If there was a
     * problem handling the request.
     */
    protected void handleCancelRequest() throws SIPServerException  {
        
        // format the OK message
        SIPMessageFormatter mformatter =
        new SIPMessageFormatter(sipStack,messageChannel);
        try {
            mformatter.newSIPResponse(INFORMATIONAL_OK,request,null);
        } catch (SIPException ex) {
            ServerInternalError.handleException(ex);
        }
        
        // send the OK message
        try {
            messageChannel.sendMessage(mformatter);
        } catch (IOException ex) {
            // Ignore??
        }
        
        // close the opened ports in the firewall
        String destination = getRawIpDestinationAddress(sipStack);
        String source = messageChannel.getRawIpSourceAddress();
        sipStack.sessionClosing(source, destination, request);
        
        // forward the CANCEL message
        TransactionRecord tr =
        sipStack.transactionHandler.getTransactionRecord(request);
        
        if (tr != null) {
	    synchronized(tr) {
              // found a transaction record for this transaction.
              Iterator li = tr.getForwardedRequests();
              while(li.hasNext()) {
                 // Remove the request from all the locations where it has
                 // been forwarded to.
                 ForwardedRequest fr = (ForwardedRequest)li.next();
                 String transport = fr.transport;
                 HostPort hostPort = fr.sentTo;
                 String nrequest = request.encode();
                 try {
		    if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES)) {
			String toString = hostPort.encode();
			String fromString = sipStack.getHostAddress() + ":" 
			    + sipStack.getPort(messageChannel.getTransport());
			if (!hostPort.hasPort()) toString += ":" + 5060;
        	        ServerLog.logMessage(request, 
			              fromString, toString, true);
		    }
                    IOHandler.sendRequest(hostPort,transport,nrequest);
                    return;
                 } catch (IOException ex) {
                    forwardRequest();
                 }
              }
	    }
        } else {
            // Try sending the request out.
            forwardRequest();
        }
        
        sipStack.transactionHandler.removeTransactionRecord(request);
    }
    
        /**
         * This method handles registration requests.
         * A client uses the REGISTER method to register the address
         * 	listed in the to header field with a SIP sipStack.
         */
    protected
    void handleRegistrationRequest() throws SIPServerException {
        // The request URI names the destination of the request i.e. the
        // domain name of the registrar.
        // TODO If we are not the target of the registration,
        // see if we can find a registration record for the target
        // machine and forward the registration
        // to that maachine. If we cannot find a
        // registration record for the target machine,send back  error.
        
        ServerLog.traceMsg(ServerLog.TRACE_DEBUG, "handleRegistration ");
        SIPMessageFormatter mformatter =
        new SIPMessageFormatter(sipStack,messageChannel);
        RequestLine requestLine = request.getRequestLine();
        URI requestURI = requestLine.getUri();
        From from = request.getFromHeader();
        To   to   = request.getToHeader();
        ContactList clist = request.getContactHeaders();
	// No contact headers so drop request.
	if (clist == null) 
		throw new SIPServerException(CLIENT_ERROR_BAD_REQUEST,
			request,"Missing contacts");
        URI fromURI;
        URI toURI;
        Authorization authHeader = request.getAuthorizationHeader();
        ViaList viaList = request.getViaHeaders();
        Via firstViaHeader = (Via) viaList.first();
        if (firstViaHeader == null) {
            throw new SIPServerException
            (SIPServerException.CLIENT_ERROR_BAD_REQUEST,
            request, "No Via Header!");
        }
        Protocol sentProtocol = firstViaHeader.getSentProtocol();
        String transport = sentProtocol.getTransport();
        
        toURI = to.getAddress().getAddrSpec();
        
        if (from != null) {
            fromURI =  from.getAddress().getAddrSpec() ;
        } else {
            fromURI = toURI;
        }
        
        // If dont support third party reg, then from and to
        // must be the same URI
        if (! fromURI.equals(toURI) &&
        ! sipStack.supportThirdPartyReg )   {
            // Send a reply denying the registration request.
            throw new SIPServerException
            (CLIENT_ERROR_FORBIDDEN,
            request, "Third Party Reg. ist Verboten");
        }
        
        String user = toURI.getUser();
        String userAtHost = toURI.getUserAtHost();
        // Authentication is disabled so allow anybody to register.
        if (sipStack.authenticationEnabled ) {
            AuthenticationMethod authenticationMethod = null;
            // If there is no authorization header send back
            // a 401 unauthorized response.
            try {
                if (authHeader != null ) {
                    String authMethodName =
                    authHeader.getScheme().toLowerCase();
                    authenticationMethod =  (AuthenticationMethod)
                    sipStack.getAuthMethods().get(authMethodName);
                    // Let him try again if I dont support method
                    if (authenticationMethod == null) {
                        try {
                            mformatter.newSIPResponse
                            (CLIENT_ERROR_UNAUTHORIZED, request,
                            "Auth method " + authMethodName +
                            " not supported");
                        } catch (SIPException ex) {
                            ServerInternalError.handleException(ex);
                        }
                        // Append WWWAuth header with the
                        // default auth method.
                        mformatter.addWWWAuthenticateHeader
                        (null, requestURI.encode());
                        ServerLog.traceMsg
                        (ServerLog.TRACE_DEBUG,
                        "Auth method " + authMethodName +
                        " not supported");
                        throw  new
                        SIPServerException
                        (mformatter.getMessage());
                    }
                }
                try {
                    if ( authHeader == null ||
                    ! authenticationMethod.doAuthenticate
                    (user, authHeader,requestLine)) {
                        try {
                            mformatter.newSIPResponse
                            (CLIENT_ERROR_UNAUTHORIZED,
                            request,null);
                        } catch (SIPException ex) {
                            ServerInternalError.handleException(ex);
                        }
			if (ServerLog.needsLogging())
                            ServerLog.traceMsg
                            (ServerLog.TRACE_DEBUG,
                             "URI = " + requestURI.encode());
                        mformatter.addWWWAuthenticateHeader
                        (authHeader, requestURI.encode());
                        throw new SIPServerException
                        ( mformatter.getMessage());
                    }
                } catch ( SIPAuthenticationException ex) {
                    try {
                        mformatter.newSIPResponse
                        (CLIENT_ERROR_UNAUTHORIZED,
                        request,ex.getMessage());
                    } catch (SIPException sipEx) {
                        ServerInternalError.handleException(sipEx);
                    }
                    mformatter.addWWWAuthenticateHeader
                    (authHeader, requestURI.encode());
                    throw
                    new SIPServerException(mformatter.getMessage());
                }
            } catch ( SIPServerException ex ) {
                ex.setSIPMessage((SIPMessage) request);
                throw   ex;
            }
        }
        ServerContactList contactList = null;
        Hashtable contactTable = sipStack.contactTable;
        synchronized(sipStack) {
            // Check for deletion of contact records.
            Contact c;
            Expires expires = request.getExpiresHeader();
            if (expires != null) {
                SIPDateOrDeltaSeconds expiryTime =
                expires.getExpiryTime();
                // Check if the user wants deletion of records
                if (expiryTime instanceof DeltaSeconds) {
                    DeltaSeconds ds = (DeltaSeconds) expiryTime;
                    if (ds.getDeltaSeconds() == 0 ) {
                        boolean wildCardFlag = false;
                        for (c = (Contact)
                        clist.first(); c != null;
                        c = (Contact) clist.next()){
                            if (c.getWildCardFlag() ) {
                                wildCardFlag = true;
                                break;
                            }
                        }
                        if (wildCardFlag) {
                            sipStack.deleteContacts(userAtHost);
                        } else {
                            contactList = sipStack.getContactList
                            (userAtHost);
                            if (contactList != null) {
                                contactList.remove(clist);
                            }
                        }
                        
                        try {
                            mformatter.newSIPResponse
                            (INFORMATIONAL_OK,request,null);
                        } catch (SIPException ex) {
                            ServerInternalError.handleException(ex);
                        }
                        contactList = (ServerContactList)
                        contactTable.get(userAtHost);
                        addContactHeaders(contactList,
                        mformatter);
                        try {
                            messageChannel.sendMessage
                            (mformatter);
                        } catch (IOException ex) {
                            // Ignore?
                        }
                        return;
                        
                    }
                }
            }
            // threads could be calling it simultaneously.
            // Get the current contact list for the user.
            // If one does not exist,  create it.
            contactList = (ServerContactList)
            contactTable.get(userAtHost);
            if (contactList == null) {
                contactList = new ServerContactList(sipStack);
                
                Hashtable stackAddresses = sipStack.stackAddresses;
                String registerHost = toURI.getHostPort().encode();
                StringTokenizer st = new StringTokenizer(registerHost,":");
                registerHost = st.nextToken();
                if (stackAddresses.containsKey(registerHost)) {
                    // We are in the case of a registration on the
                    // proxy itself. We put the registration under any
                    // of the proxy addresses
                    Enumeration en = stackAddresses.keys();
                    while (en.hasMoreElements()) {
                        String key = toURI.getUser() + "@" + en.nextElement();
                        contactTable.put(key, contactList);
                    }
                } else {
                    contactTable.put(userAtHost, contactList);
                }
                
            }
            
            // Add the non-ambigous contacts (and remove the
            //  ones that have a 0 expiry time).
            
            for (c = (Contact) clist.first(); c!= null;
            c =  (Contact) clist.next() ) {
                try {
                    if (contactList.add(c)) {
                        try {
                            mformatter.newSIPResponse(INFORMATIONAL_OK,
                            request, null);
                        } catch (SIPException ex) {
                            ServerInternalError.handleException(ex);
                        }
                        mformatter.addExpiresHeader(sipStack.
                        registrationTimeout);
                        contactList = (ServerContactList)
                        contactTable.get(userAtHost);
                        addContactHeaders(contactList,mformatter);
                    } else {
                        try {
                            mformatter.newSIPResponse
                            (CLIENT_ERROR_FORBIDDEN, request, null);
                        } catch (SIPException ex) {
                            ServerInternalError.handleException(ex);
                        }
                    }
                } catch (SIPException ex) {
                    ServerLog.logException(ex);
                    throw new SIPServerException
                    (CLIENT_ERROR_AMBIGUOUS,
                    request, "action confict");
                }
            }
        }
        try {
            messageChannel.sendMessage(mformatter);
        } catch (IOException ex) {
            // Ignore?
        }
        
    }
    
    /**
     * Handle an unknown request type. If handler is registered, then
     * call the handler to prcess the request.
     * Otherwise (return 501) response.
     */
    protected void
    handleUnknownRequest()
    throws SIPServerException {
        RequestLine requestLine = request.getRequestLine();
        String method = requestLine.getMethod();
        ExtensionMethodHandler handler =
        sipStack.getExtensionMethodHandler(method);
        if (handler != null) {
            handler.processRequest(request,messageChannel,sipStack);
        } else {
            // Cannot handle this method so reply back with an error.
            
            SIPMessageFormatter mformatter =
            new SIPMessageFormatter(sipStack,messageChannel);
            
            
            try {
                
                mformatter.newSIPResponse(
                SIPErrorCodes.SERVER_ERROR_NOT_IMPLEMENTED,
                request,method);
                messageChannel.sendMessage(mformatter);
            } catch (SIPException ex) {
                ex.printStackTrace();
                System.exit(0);
            } catch (IOException ex) {
                // Ignore.
            }
            
        }
        
    }
    
    
    
    /**
     *   Handle the INVITE request.
     */
    protected void
    handleInviteRequest()
    throws SIPServerException {
	if (ServerLog.needsLogging()) 
	    ServerLog.logMessage("sender host /port " +
			messageChannel.getSenderAddress() + "/" +
			messageChannel.getSenderPort());
        SIPMessageFormatter mformatter =
        new SIPMessageFormatter(sipStack,messageChannel);
        TransactionRecord tr;
        
        ServerLog.traceMsg(ServerLog.TRACE_DEBUG,"handleInviteRequest " );
        
        // check is there is a firewall/NAT transversal and
        // do whatever is needed
        String source = messageChannel.getRawIpSourceAddress();
        String destination = getRawIpDestinationAddress(sipStack);
            
        // Forward the request to the targetted host and
        // start up a timeout for the host.
        tr = forwardRequest();
            
        if (tr != null) {
            sipStack.sessionOpening(source, destination, request, tr);
            try {
                mformatter.newSIPResponse(INFORMATIONAL_TRYING,
                		request, null);
            } catch (SIPException ex) {
                ServerInternalError.handleException(ex);
            }
            try {
                messageChannel.sendMessage(mformatter);
            } catch (IOException ex) {
                // Ignore?
            }
	    tr.setMessageChannel(this.messageChannel);
	}
    }
    
        /**
         * Forward the  INVITE request to the next hop.
         * The forwarding policy can be sipStack-specific.
         * Ours is as follows:
         * (1)  Check if we have a registration record for
         * 		the user where we can forward this  request.
         *	If so, send it there.
         * (2) (Optional) Check if there is an external
         * 		(i.e. location sipStack) which can find
         * 		the user  for us and if so, send it there.
         * (3) If 1 and 2 fail, forward the request directly to
         * the address in the requestURI.
         * (4) If this fails, then send to the default location.
         */
    private TransactionRecord
    forwardRequest() throws SIPServerException {
        // Loop detection
        SIPHeader[] headers = request.getHeaders();
        String stackAddress = sipStack.getViaHeaderStackAddress();
        boolean noLoop = true;
        for(int i = 0; i < headers.length && noLoop; i++) {
            SIPHeader sipHeader = headers[i];
            if (sipHeader instanceof ViaList  ) {
                ViaList vlist = (ViaList)sipHeader;
                ListIterator iterator = vlist.listIterator();
                while(iterator.hasNext()) {
                    Via v = (Via) iterator.next();
                    HostPort hostPort = v.getSentBy();
                    String host = hostPort.getHost().getHostname();
                    if (stackAddress.equals(host)) {
                        String transport = v.getSentProtocol()
                        .getTransport();
                        int port = hostPort.getPort();
                        if (sipStack.comparePort(port, transport)) {
                            ServerLog.traceMsg(ServerLog.TRACE_DEBUG,
                            "WARNING: Loop detected. Dropping message.");
                            noLoop = false;
                            // create a 482 Error Message
                            SIPMessageFormatter mformatter =
                            new SIPMessageFormatter(sipStack,
                            messageChannel);
                            try {
                                mformatter.newSIPResponse
                                (CLIENT_ERROR_LOOP_DETECTED,
                                request, null);
                            } catch (SIPException ex) {
                                ServerInternalError.handleException(ex);
                            }
                            
                            // find where to send in order to
                            // avoid the loop.
                            v = (Via) vlist.next();
                            if (v == null) {
                                for(int j = i+1; j < headers.length &&
                                v == null; j++) {
                                    sipHeader = headers[j];
                                    if (sipHeader instanceof ViaList  ) {
                                        vlist = (ViaList)sipHeader;
                                        v = (Via) vlist.first();
                                    }
                                }
                            }
                            hostPort = v.getSentBy();
                            host = hostPort.getHost().getHostname();
                            port = hostPort.getPort();
                            if (port == -1) {
                                port = SIPStack.DEFAULT_PORT;
                            }
                            
                            // send the message
                            try {
                                InetAddress receiverAddress =
                                InetAddress.getByName(host);
                                messageChannel.sendMessage
                                (mformatter, receiverAddress, port);
                            } catch (Exception ex) {
                                ServerInternalError.handleException(ex);
                            }
                            return null;
                        }
                    }
                }
            }
        }
        RequestLine requestLine = request.getRequestLine();
        URI requestURI = requestLine.getUri();
        TransactionRecord tr = null;
        RouteList routeList = request.getRouteHeaders();
        
        // If there is an explicit route for forwarding the request
        // then return it here.
        
        if (routeList != null) {
            try {
                Route route = (Route) routeList.getFirst();
                routeList.removeFirst(); // Get rid of the top most route
                Address address = route.getAddress();
                tr = sipStack.transactionHandler.forwardToAddress
                (address,request,messageChannel);
            } catch (IOException ex) {
                ServerLog.logException(ex);
                throw
                new SIPServerException
                (CLIENT_ERROR_NOT_FOUND,
                request," IOError: Explicit Route Failed");
                
            }
        }
        
        // No explicit route for request specified.
        // If a registration has been made for the address send it there.
        tr = sipStack.transactionHandler.
        forwardByContactList(requestURI,request, messageChannel);
        if (tr == null) {
            if (sipStack.locationSearchEnabled) {
                tr = sipStack.transactionHandler.
                forwardByLocationSearch
                (requestURI, request, messageChannel);
            }
        }
        
        // Did not succeed in sending out the requests to the contacts.
        // Consult the default routing algorithm.
        // Note that the default routing algorithm returns a route
        // that is constructed by looking at the requestURI and the
        // proxy address.
        
        if (tr == null) {
            Iterator iterator = sipStack.getNextHop(request);
            if (iterator == null)
                throw new SIPServerException
                (CLIENT_ERROR_NOT_FOUND,
                request,"No forwarding path");
            // BUGBUG -- this can currently only handle one forwarding address
            // It should hande multiple transaction ids (TBD)
            while(iterator.hasNext() ) {
                Hop hop = (Hop)iterator.next();
                String host = hop.getHost();
                int port = hop.getPort();
                String transport = hop.getTransport();
                try {
                    tr = sipStack.transactionHandler.
                    forwardToHostPort
                    (host,port,transport,request,messageChannel);
                    // Successfully sent out the message so break out.
                    if (tr != null) break;
                } catch (IOException ex) {
                    continue;
                }
            }
        }
        if (tr == null) throw new SIPServerException
        (CLIENT_ERROR_NOT_FOUND, request,"No forwarding path");
        return tr;
    }

    /** Get the message channel on which this message arrived.
     */
     public MessageChannel
     getResponseChannel() { return this.messageChannel; }
    
    /**
     * Convenience function to get the raw IP destination address
     * of a SIP message as a String. THe address is extracted from the
     * RequestURI if the message is a SIPRequest and from the From
     * header if the messge is a SIPResponse.
     * @param stack The stack
     * @return Destination address
     */
    public String getRawIpDestinationAddress(ServerMain stack) {
        HostPort hostPort = null;
        URI requestURI = request.getRequestURI();
        
        // Registration lookup
        String user = requestURI.getUserAtHost();
        if (user != null) {
            ServerContactList clist = (ServerContactList)
            stack.contactTable.get(user);
            if (clist != null) {
                ListIterator li = clist.getIterator();
                // Here, we do not deal with the case where
                // multiple registrations for the same user are allowed
                ServerContactRecord c = clist.next(li);
                hostPort = c.getContact().getAddress().getHostPort();
            }
        }
        
        // We do not deal with the case where location search is enabled
        
        // No registration found, just look at the request URI
        if (hostPort == null) {
            AuthorityServer authority =
            (AuthorityServer) requestURI.getAuthority();
            hostPort = authority.getHostPort();
        }
        
        Host host = hostPort.getHost();
        return host.getIpAddress();
    }
    
}
