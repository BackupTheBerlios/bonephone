/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).       *
 * See ../../../../doc/uncopyright.html for conditions of use.                 *
 * Author: M. Ranganathan (mranga@nist.gov)                                    *
 * Modified by: Marc Bednarek (bednarek@nist.gov) 			       *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                              *
 *******************************************************************************/
/******************************************************
 * File: TransactionHandler.java
 * created 20-Oct-00 12:27:43 PM by mranga
 */


package examples.proxy;
import  java.util.Hashtable;
import  java.util.ListIterator;
import  java.util.LinkedList;
import  java.io.IOException;
import  java.util.NoSuchElementException;
import  java.security.MessageDigest;
import  java.security.NoSuchAlgorithmException;
import  gov.nist.sip.stack.*;
import  gov.nist.sip.*;
import  gov.nist.sip.net.*;
import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.msgparser.*;
import java.util.StringTokenizer;
import java.util.Enumeration;


/**
 *  Handles transactions for SIP Stateful proxies.
 *  This is organized as follows: We keep two hashtables - one for received
 *  requests and one for forwarded requests. 
 *  The forwardedRequest table is indexed by the transaction ID of the
 *  received message that was forwarded.
 */

public class TransactionHandler implements Runnable  {
    protected  ServerMain stack;
    protected  Hashtable receivedRequests;  // a table of received req.
    protected  Hashtable forwardedRequests; // A table of forwarded reqs.
    
    /**
     * Makes a new transaction queue for the given request
     *	and returns a pointer to it..
     * @param request a SIPRequest
     * @param senderChannel The message channel associatd to the request
     * @return the TransactionRecord created
     */
    
    protected synchronized
    TransactionRecord makeRecievedQueue(SIPRequest request,
    MessageChannel senderChannel)
    {
        String tag = request.getTransactionId();
	String canceltag = request.getCancelID();
        if (receivedRequests.containsKey(tag) ) {
            return  (TransactionRecord) receivedRequests.get(tag);
        } else {
	    if (ServerLog.needsLogging()) {
               ServerLog.logMessage("Creating Transaction Record : " + tag);
               ServerLog.logMessage("CancelTag : " + canceltag);
               ServerLog.logMessage(request.encode());
               ServerLog.logMessage( "----------------------------------");
	    }
            TransactionRecord tr = new TransactionRecord(request, tag,
		canceltag,
                senderChannel);
            receivedRequests.put(tag, tr);
	    receivedRequests.put(canceltag,tr);
            return tr;
        }
    }
    
    /**
     * Return a transaction record if there is one pending.
     * @param sipResponse A SIPResponse
     * @return a TransactionRecord corresponding to the response
     */
    
    protected synchronized TransactionRecord
    getTransactionRecord(SIPResponse sipResponse) {
        String tag = sipResponse.getTransactionId();
	if (ServerLog.needsLogging())  {
            ServerLog.logMessage
	    ("Retrieving Response Transaction Record : " + tag);
	}
        return (TransactionRecord) forwardedRequests.get(tag);
    }
    
    /**
     * Put the transaction in the forwarded queue.
     * @param tid A transaction Id
     * @param tr the transaction record to be put in the queue
     */
    
    protected synchronized void
    putInForwardedQueue
    ( String tid,
    TransactionRecord tr ) {
	if (ServerLog.needsLogging()) 
            ServerLog.logMessage( "Putting in forwarded queue " + tid);
        forwardedRequests.put(tid,tr);
    }
    
    /**
     * Remove a transaction record for the given request.
     * @param sipreq a SIPRequest structure
     * @return the TransactionRecord removed
     */
    
    protected synchronized TransactionRecord
    removeTransactionRecord(SIPRequest sipreq) {
        String tid = sipreq.getTransactionId();
	String cancelId = sipreq.getCancelID();
        TransactionRecord tr =
        (TransactionRecord) receivedRequests.remove(tid);
	receivedRequests.remove(cancelId);
        return tr;
    }
    
    /**
     * Remove a transaction record for the given response.
     * @param sipresponse a SIPResponse structure
     * @return the TransactionRecord removed
     */
    
    protected synchronized TransactionRecord
    removeTransactionRecord(SIPResponse sipresponse) {
        String tid = sipresponse.getTransactionId();
        TransactionRecord retval = (TransactionRecord)
        receivedRequests.remove(tid);
        return retval;
    }
    
    /**
     *  Return the transaction record corresponding to a SIP request.
     * @param sipRequest a SIPRequest structure
     * @return the corresponding TransactionRecord
     */
    
    protected synchronized TransactionRecord
    getTransactionRecord(SIPRequest sipRequest) {
	if (sipRequest.getMethod().compareToIgnoreCase("ACK") == 0  ||
	    sipRequest.getMethod().compareToIgnoreCase("CANCEL") == 0 ) {
           String tag = sipRequest.getCancelID();
	   if (ServerLog.needsLogging()) 
               ServerLog.logMessage
		("Retrieving Transaction Reycord : " + tag);
           return (TransactionRecord) receivedRequests.get(tag);
	} else {
           String tag = sipRequest.getTransactionId();
	   if (ServerLog.needsLogging()) 
               ServerLog.logMessage
		("Retrieving Transaction Reycord : " + tag);
            return (TransactionRecord) receivedRequests.get(tag);
	}
    }
    
    /**
     * Forward the request directly to the host and port
     * embedded in the Request URI.
     * @param URI a request URI
     * @param request the request to be forwarded
     * @param sender_chan the message channel associated to the request
     * @return the TransactionRecord created
     */
    
    protected TransactionRecord forwardToRequestURI(URI uri,
    			SIPRequest request,
    			MessageChannel sender_chan) throws IOException {
        // Forward to the URI that is in the request
	if (uri == null || request == null || sender_chan == null) 
		throw new IllegalArgumentException("bad arg!");
        AuthorityServer authority = (AuthorityServer) uri.getAuthority();
        String transport = (String) uri.getParm(SIPKeywords.TRANSPORT);
        if (transport == null) transport = SIPKeywords.UDP;
        HostPort hostPort = authority.getHostPort();
        return forwardToHostPort(hostPort,transport, request, sender_chan);
    }
    
    /**
     *  Forward the request by doing a lookup via the lookup
     *	stack to find where the user is located.
     *	Returns a transaction structure that caches the
     *	messages if successfully forwarded.
     * @param requestURI is the location where we want to forward
     * @param request is the sip request corresponding to the forwarded
     *	  message.
     * @param message is the message to forward.
     * @param senderChan is the channel through which the message was
     *	  received.
     * @return the TransactionRecord created
     */
    
    protected TransactionRecord 
       forwardByLocationSearch 
	 ( URI  	        requestURI 	,
    	   SIPRequest     request	        ,
           MessageChannel senderChan      	) {
         TransactionRecord tr = null;
         if (stack.defaultContactMethod != null)  {
            LinkedList locationList = null;
            try {
                locationList =
                stack.defaultContactMethod.getUserLocation(requestURI);
                if (locationList == null) return null;
            } catch (Exception ex) {
                return null;
            }
            ListIterator iterator = locationList.listIterator();
            while (true) {
                HostPort hostPort;
                UserLocation userLocation;
                try {
                    userLocation = (UserLocation) iterator.next();
                } catch (NoSuchElementException ex) {
                    break;
                }
                hostPort = userLocation.hostPort;
                String transport = userLocation.transport;
                try {
                    tr = forwardToHostPort
                    (hostPort, transport, request, senderChan);
                } catch (IOException ex) {
                    // Ignore
                    ServerLog.logMessage
                    ("Exception occured while locating user " + ex);
                }
            }
        }
        return tr;
    }
    
    /**
     *  Forward a request to a the registered contact addresses
     *  given the user name (provided that such a contact address exists).
     * @param URI a request URI
     * @param request the request to be forwarded
     * @param senderChan the message channel associated to the request
     * @return the TransactionRecord created
     */
    
    protected TransactionRecord
       forwardByContactList (URI requestURI,
      SIPRequest request,
      MessageChannel senderChan) {
        TransactionRecord tr = null;
        String user = requestURI.getUserAtHost();
        StringTokenizer st = new StringTokenizer(user, ":");
        user = st.nextToken();
        if (user == null) {
	    if(ServerLog.needsLogging()) 
               ServerLog.logMessage
               ("Could not find user in request URI" +
               requestURI.encode());
            return null;
        }
        ServerContactList clist = (ServerContactList)
        stack.contactTable.get(user);
        LinkedList deleteList = new LinkedList();
	
        if (clist != null) {
	    synchronized (clist) {
	        if (ServerLog.needsLogging())
                    ServerLog.logMessage
		    ("forwarding to contact list for URI "+ 
			requestURI.encode());

                ListIterator li = clist.getIterator();
                ServerContactRecord c;
                for (c = clist.next(li); c != null; c = clist.next(li)) {
                   try {
                      tr = forwardToContactAddress(c,request,senderChan);
                  } catch (IOException ex) {
                     ServerLog.logException(ex);
                     // Contact is bad, so get it out of our list
                     deleteList.add(clist);
                  }
               }
               li = deleteList.listIterator();
               while(li.hasNext()) {
                  c = (ServerContactRecord) li.next();
                  clist.remove(c);
	          if (ServerLog.needsLogging())
                      ServerLog.logMessage ("Removed contact " 
			+ c.contact.getAddress());
                }
	    }
        }
        
        return tr;
    }
    
    /**
     * Forward a request given an address
     * @param addr an address to which to forward the request
     * @param request the request to be forwarded
     * @param senderChan the message channel associated to the request
     * @return the TransactionRecord created
     */

    protected TransactionRecord 
	forwardToContactAddress(ServerContactRecord addr, 
				SIPRequest originalRequest,
				MessageChannel senderChan) throws IOException {
	SIPMessageFormatter msgFormatter = new SIPMessageFormatter
	    (stack,senderChan);
	ForwardedRequest forwardedRequest = new ForwardedRequest();
	LinkedList requireList = null;
	if (stack.requireTimer) {
		requireList = new LinkedList();
		requireList.add("Timer");
	} 
	HostPort hp = addr.getContact().getAddress().getHostPort();
	String transport = 
		addr.getContact().getAddress().getTransport();
	if (transport == null) transport = "UDP";
	String newUrl = "sip:"+hp.encode()+Separators.SEMICOLON + 
			"transport=" + transport;

	String tid = msgFormatter.newSIPRequest(originalRequest, 
						newUrl,
						originalRequest.getMethod(),
						stack.getRecordRoute(),
						requireList,null);
	String new_req = msgFormatter.getMessage(tid, false);
	forwardedRequest.newRequest = new_req;
	forwardedRequest.branchID = msgFormatter.getBranchId();
	// Message from which this forwarded request was generated.
	forwardedRequest.originalRequest = originalRequest;
	Address contactAddress = addr.getContact().getAddress();
	IOHandler.sendRequest(contactAddress, msgFormatter, tid);
	TransactionRecord tr = makeRecievedQueue(originalRequest, senderChan);
	forwardedRequest.sentTo = addr.getContact().getAddress().getHostPort();
	forwardedRequest.transport = 
		addr.getContact().getAddress().getTransport();
	if (forwardedRequest.transport == null) 
		forwardedRequest.transport = SIPKeywords.UDP;
	tr.addRequest(forwardedRequest);
	putInForwardedQueue(tid,tr);
	return tr;
    }
    
    /**
     *Forward the request to the given Address.
     *@param address Address to forward it to.
     *@param request Request to forward.
     *@param senderChannel Where the request came from.
     *@return the TransactionRecord created
     *@exception IOException if there was a problem sending the request.
     */
    protected TransactionRecord
    forwardToAddress(Address address,
    	SIPRequest request,
    	MessageChannel senderChannel)
    throws IOException
    {
	if (address == null || request == null || senderChannel == null)
		throw new IllegalArgumentException("Null Arg");
	String transport = address.getTransport();
	if (transport == null) transport = SIPKeywords.UDP;
        return forwardToHostPort(address.getHostPort(),
		transport, request, senderChannel);
    }

    /**
     * Forward a request given a host and port
     * @param hostport a HostPort structure containing the host and the port
     * @param transport the transport protocol
     * @param originalReq the request to be forwarded
     * @param senderChan the message channel associated to the request
     * @return the TransactionRecord created
     *@exception IOException if there was a problem sending the request.
     */
    
    protected TransactionRecord forwardToHostPort(HostPort hostport, 
						  String transport,
						  SIPRequest originalReq,
						  MessageChannel senderChan) 
	throws IOException {

	if (hostport == null )
		throw new IllegalArgumentException("Null hostport!");
	else if (transport == null)
		throw new IllegalArgumentException("Null transport!");
	else if (senderChan == null)
		throw new IllegalArgumentException("Null senderChan!");

	if (ServerLog.needsLogging()) {
	    ServerLog.logMessage( "forwardToHostPort: HostPort = " +
			    hostport.encode() + 
			    " Transport " + transport);
	}

	// avoid the proxy to forward messages to itself
	int port = hostport.getPort();
	if (stack.comparePort(port, transport)) {
	    if (ServerLog.needsLogging())  {
	        ServerLog.logMessage
	        ("WARNING, proxy was forwarding the message to itself.");
	        ServerLog.logMessage ("Dropping the message."); 
	    }
	    return null;
	}

	SIPMessageFormatter msgFormatter = 
	    new SIPMessageFormatter(stack, senderChan);

	ForwardedRequest forwardedRequest = new ForwardedRequest();
	LinkedList requireList = null;
	if (stack.requireTimer) {
		requireList = new LinkedList();
		requireList.add("Timer");
	} 

	String tid = msgFormatter.newSIPRequest(originalReq,
						stack.getRecordRoute(),
						requireList);

	String new_req = msgFormatter.getMessage(tid, false);
	// Log the generated message.
	port = stack.getPort(transport);
	forwardedRequest.newRequest = new_req;
	forwardedRequest.branchID = msgFormatter.getBranchId();
	String newRequest = forwardedRequest.getNewRequest();
	IOHandler.sendRequest(hostport, transport, msgFormatter, tid);
	TransactionRecord tr = makeRecievedQueue(originalReq, senderChan);
	forwardedRequest.sentTo = hostport;
	forwardedRequest.transport = transport;
	tr.addRequest(forwardedRequest);
	putInForwardedQueue(tid, tr);
	return tr;
    }
    
    /** Forward to a host, port when these are given as strings.
     *@param host host
     *@param port integer port
     *@param request.
     *@param siprequest is the request which we recieved (and from which
     *  we generate a new message).
     *@param MesageChannel is the channel from which we received this message
     *  (could be null).
     */
     public TransactionRecord forwardToHostPort(String host, int port,
     String transport,SIPRequest siprequest, MessageChannel mchan) 
     throws IOException {
         HostPort hostPort = new HostPort();
         Host h = new Host();
         h.setAddress(host);
         hostPort.setPort(port);
         hostPort.setHost(h);
         return forwardToHostPort(hostPort,transport,siprequest,mchan);
     }
    
    /**
     *  Constructor.
     * @param srv the stack
     */
    
    protected TransactionHandler(ServerMain srv) {
        stack = srv;
        receivedRequests = new Hashtable();
        forwardedRequests = new Hashtable();
        Thread timeoutThread  = new Thread(this);
        timeoutThread.start();
    }
    
    /**
     *  Store a provisional response.
     * @param request a SIPRequest structure to which the response
     * is associated
     * @param response the provisional response
     */
    
    protected void
    setProvisionalResponse(SIPRequest request,
    String response)
    throws TransactionNotFoundException {
        TransactionRecord tr = getTransactionRecord(request);
        if (tr != null) {
            tr.setProvisionalResponse(response);
        } else {
	    if (ServerLog.needsLogging()) {
                ServerLog.logMessage("Transaction not found for \n" +
                                    request.encode());
	    }
            throw new TransactionNotFoundException();
        }
    }
    
    /**
     * Get the provisional response.
     * @param request a SIPRequest structure
     * @return the provisional response associated to the request
     */
    
    protected synchronized String
    getProvisionalResponse(SIPRequest request)
    throws TransactionNotFoundException {
        TransactionRecord tr = getTransactionRecord(request);
        if (tr != null) return tr.getProvisionalResponse();
        else {
	    if (ServerLog.needsLogging()) 
                ServerLog.logMessage( "Transaction not found for \n" + 
			request.encode());
            throw new TransactionNotFoundException();
        }
    }
    
    /**
     *A thread that scans the transaction queue, removing exipred transaction
     *records. This thread runs once every 5 seconds.
     */
    public void run() {
        while(true) {
            synchronized(this) {
                long currentTime = (new java.util.Date()).getTime();
                    Enumeration keys  = receivedRequests.keys();
                   
                    while(keys.hasMoreElements()) {
                        String key = (String) keys.nextElement();
                        TransactionRecord transactionRecord = 
                            (TransactionRecord) receivedRequests.get(key);
                        if (currentTime > transactionRecord.expiryTime) 
                            receivedRequests.remove(key);
                    }
                    keys = forwardedRequests.keys();
                    while(keys.hasMoreElements()) {
                        String key = (String) keys.nextElement();
                        TransactionRecord transactionRecord = 
                            (TransactionRecord) forwardedRequests.get(key);
                        if (currentTime > transactionRecord.expiryTime) 
                            forwardedRequests.remove(key);
                    }
            }
	    try {
                    Thread.sleep(2000);
            } catch (InterruptedException ex) {
                  // do nothing.
            }
        }
        
    }
    
}
