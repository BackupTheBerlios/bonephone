/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).       *
 * See ../../../../doc/uncopyright.html for conditions of use.                 *
 * Author: M. Ranganathan (mranga@nist.gov)                                    *
 * Modified by: Marc Bednarek (bednarek@nist.gov)                              *
 *  -- added support for logging.				       	       *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                              *
 *******************************************************************************/
/******************************************************
 * File: MessageChannel.java
 * created 04-Sep-00 12:02:26 AM by mranga
 */

package gov.nist.sip.stack;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.stack.security.*;
import java.io.UnsupportedEncodingException;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A class that handles formatting of headers and messages.
 * It accumulates headers by appending strings to a message accumulator and
 * returns the accumulated message when the user issues a getMessage
 * This class is highly thread unsafe! (extreme peril!)
 * @author <A href=mailto:mranga@antd.nist.gov> M. Ranganathan </A>
 */

public final class SIPMessageFormatter implements SIPKeywords
{
    // An accumulator object for formatting SIP messages.
    class MessageAccumulator {
        protected StringBuffer headers;
        protected LinkedList body;
        MessageAccumulator() {
            headers = new StringBuffer("");
            body = new LinkedList();
        }
        MessageAccumulator(String start) {
            headers = new StringBuffer(start);
            body = new LinkedList();
        }
        
        private String generateContentLengthHeader(int length) {
            return CONTENT_LENGTH + COLON + SP + length
            + Separators.NEWLINE;
        }
        
        void appendToHeaders(String header) {
            headers.append(header);
        }
        
        
        void terminateHeader() {
            headers.append(Separators.NEWLINE);
        }
        
        void addContent(String body) {
            this.body.add(body);
            
        }
        
        void addContent(byte[] body) {
            this.body.add(body);
        }
        
        
	/** Get just the headers portion of this message.
	* @return a String containing the partially formatted messge.
	*/
         String getHeaders() {
              return headers.toString();
        }
        
                /** Get the message content as a byte array.
                 */
         byte[] getMessageAsBytes() {
            int length = 0;
            ListIterator li = body.listIterator();
            Object item;
            // Compute the needed size.
            while(li.hasNext()) {
                item = li.next();
                if (item != null) {
                    if (item instanceof String) {
                        String itemString = (String)item;
                        length += itemString.length();
                    } else if (item instanceof byte[]) {
                        length += ( (byte[]) item ).length;
                    }
                }
            }
            
            if (length == 0) {
                String hdrs = headers.toString() +
                this.generateContentLengthHeader(length) +
                Separators.NEWLINE ;
                byte[] retval = null;
                try {
                    retval = hdrs.getBytes("UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    ServerInternalError.handleException(ex);
                }
                return retval;
            } else {
                String hdrs =
                headers.toString() + 
                this.generateContentLengthHeader(length) +
                Separators.NEWLINE  ;
                byte[] retval =  new byte[length + hdrs.length()];
                try {
                    byte[] hdrBytes = hdrs.getBytes("UTF-8");
                    int i;
                    for (i = 0; i < hdrBytes.length; i++) {
                        retval[i] = hdrBytes[i];
                    }
                    li = body.listIterator();
                   
                    // Compute the needed size.
                    while(li.hasNext()) {
                        item = li.next();
                        if (item != null) {
                            if (item instanceof String) {
                                String itemString = (String)item;
                                byte[] bytes = null;
                                bytes = itemString.getBytes("UTF-8");
                                
                                for(int k = 0; k < bytes.length; k++,i++) {
                                    retval[i] = bytes[k];
                                }
                            } else if (item instanceof byte[]) {
                                byte[] bytes = (byte[]) item;
                                for(int k = 0; k < bytes.length; k++,i++) {
                                    retval[i] = bytes[k];
                                }
                            }
                        }
                    }
                    return retval;
                } catch (UnsupportedEncodingException ex) {
                    ServerInternalError.handleException(ex);
                }
                return null;
            }
            
        }
        
                /** Get message content as a string.
                 */
        String getMessage() {
            ListIterator li = body.listIterator();
            int length = 0;
            Object item;
            StringBuffer buffer = new StringBuffer();
            while(li.hasNext()) {
                item = li.next();
                if (item != null) {
                    if (item instanceof String) {
                        String itemString = (String)item;
                        length += itemString.length();
                        buffer.append(itemString);
                    } else if (item instanceof byte[]) {
                        byte[] bytes = (byte[]) item;
                        try {
                            String itemString =
                            new String(bytes,"UTF-8");
                            buffer.append(itemString);
                            length += itemString.length();
                        } catch (UnsupportedEncodingException ex){
                            ServerInternalError.
                            handleException(ex);
                        }
                    }
                }
            }
	    String headerString = headers.toString();
	    if (headerString.equals("")) return null;
            if (length == 0) return 
		headers.toString() +
            		this.generateContentLengthHeader(length) +
            		Separators.NEWLINE ;
            else return 
		headers.toString()  +
            		this.generateContentLengthHeader(length)+ 
			Separators.NEWLINE +
            		buffer.toString();
        }
        
        
        
        
    }
    
    private   boolean topmostViaAdded ;
    private   MessageAccumulator message;
    private   SIPStack  stack;
    private   MessageChannel messageChannel;
    private   MessageDigest messageDigest;
    private   String callIdBody;
    private   String  cseqBody;
    private   String  firstLine;
    private   String  lastTid;
    private   String  requestMethod;
    private   String  uriString;
    private   String  topmostViaBody;
    private   String  branchId;
    private   long    cseqSeqno;
    private   String  toBody;
    private   String  toTag;
    private   String  fromTag;
    private   String  fromBody;
    
    
    // A look-up table for transaction ids when we generate these from
    // messages. this is passed back to the caller who can later retrieve
    // it from the getMessage or getMessage(tid,booelan)
    private  Hashtable clientTransactionTable;
    
        /** Put the message in a temporary holding place for later retreval.
         */
    private void put(String id) {
        lastTid = id;
        clientTransactionTable.put(id,message);
        
    }
    
        /** Extract information for later logging.
         * This is for generating nicely formatted trace messages.
         */
    private void stashLoggingInformation(SIPMessage sipMessage, 
		boolean switchHeaders ) {
        // Stow away stuff for later message logging.
        if (sipMessage.getCallIdHeader() != null)
            this.callIdBody = sipMessage.getCallIdHeader().getCallID();
        
        if (sipMessage.getFromHeader() != null)  {
	    if (switchHeaders) 
               this.toBody = sipMessage.getFromHeader().getUserAtHostPort();
	    else
               this.fromBody = sipMessage.getFromHeader().getUserAtHostPort();
	    this.fromTag = sipMessage.getFromHeader().getTag();
        }
        
        if (sipMessage.getToHeader() != null)  {
	    if (switchHeaders) 
               this.fromBody = sipMessage.getToHeader().getUserAtHostPort();
	    else 
                this.toBody = sipMessage.getToHeader().getUserAtHostPort();
	    this.toTag  = sipMessage.getToHeader().getTag();
        }
        
        if (sipMessage.getCallIdHeader() != null) {
            this.callIdBody = sipMessage.getCallIdHeader().getCallID();
        }
        
        CSeq cseq = sipMessage.getCSeqHeader();
        if (cseq != null) {
            cseqSeqno = cseq.getSeqno();
            this.cseqBody =
            new Long (cseq.getSeqno()).toString() +
            COLON + cseq.getMethod();
        }
    }

    
    
        /**
         * return the current transaction identifier from the generated
         * message.
         */
    public String getTransactionId() {
        return  this.fromBody + COLON + this.toBody + COLON +
	// (toTag != null ? toTag : "") +
        this.callIdBody + COLON + this.cseqBody +
        COLON + this.topmostViaBody;
        
    }

	/** Get the branch ID of the generated message.
	*@return the generated branch ID 
	*/
    public String getBranchId() {
	  return this.branchId;
    }
    
        /**
         *Get the current message String.
	 *@return A string containing the formatted message.
         */
    public String getMessage() {
	topmostViaAdded = false;
        return getMessage(true);
    }
        /**
         *Get the current message as an array of bytes..
	 *@return A string containing the formatted message.
         */
	public byte[] getMessageAsBytes() {
	    	topmostViaAdded = false;
        	return getMessageAsBytes(true);
	}

	/** Get just the headers portion of this message.
	* @return a String containing the partially formatted messge.
	*/
         public String getHeaders() {
              return message.getHeaders();
        }
    
        /**
         *Get the current message String.
         *@param destroyOnRead is set to true if we want to delete the message
	 *@return the message as String
         */
    public String  getMessage(boolean destroyOnRead) {
        String retval =  message.getMessage();
        if (destroyOnRead) {
	    topmostViaAdded = false;
            message = new MessageAccumulator ();
        }
        
        if (lastTid != null) clientTransactionTable.remove(lastTid);
        return retval;
    }

        /**
         *Get the current message String.
         *@param destroyOnRead is set to true if we want to delete the message
	 *@return the message as String
         */
    public byte[]  getMessageAsBytes(boolean destroyOnRead) {
        byte[] retval =  message.getMessageAsBytes();
        if (destroyOnRead) {
	    topmostViaAdded = false;
            message = new MessageAccumulator ();
        }
        
        if (lastTid != null) clientTransactionTable.remove(lastTid);
        return retval;
    }
    
        /**
         *Get the associated stack
         * @return The stack
         */
    public SIPStack getStack() {
        return stack;
    }

	/** Get the generated CALL Leg ID
	*@return the call leg id for the generated message
	*/
	public String getCallLegID() {
		String retval = this.callIdBody;
		if (fromTag != null) {
			retval += COLON + fromTag;
	        }
		if (toTag != null) {
			retval += COLON + toTag;
		}
		return retval;
	}
    
    
        /** Get the generated CANCEL id.
         *@return the cancel identifier (for subsequent canceling of messages).
         */
    
    public String getCancelID() {
	String thisUri = null;
	if (this.uriString == null 	|| 
	      callIdBody == null 	||
	      toBody == null 		|| 
	      fromBody == null 		|| 
	       topmostViaBody == null ) return null;
	try {
	    StringMsgParser stringMsgParser = new StringMsgParser();
	    URI uri  = stringMsgParser.parseSIPUrl(this.uriString);
	    thisUri = uri.encode();
	} catch ( SIPParseException ex) {
		return null;
	}
        return ( callIdBody + COLON + toBody + COLON + fromBody +
                 COLON + topmostViaBody + COLON +  cseqSeqno).toLowerCase();
        
    }
    
        /**
         * Initialize message formatter ( set the message string to null)
         * @param sipStack - pointer to the structure where all our global data
         *	is stored.
         * @param msgchannel is the message channel
         * ( I/O mechanism) to return the message.
         */
    public SIPMessageFormatter
    ( SIPStack sipStack, MessageChannel msgchannel ) {
        message = new MessageAccumulator();
        stack = sipStack;
        messageChannel = msgchannel;
        clientTransactionTable = new Hashtable();
        uriString = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch ( NoSuchAlgorithmException ex ) {
            ServerLog.logMessage("Algorithm not found " + ex);
            ServerInternalError.handleException(ex);
        }
    }
    
        /**
         * Generate a response from a response (for proxying on).
         * Strip off the topmost via header (assuming it belongs to us)
         * and append the rest of the response unchanged.
	 *@param statusCode is the new status code for the response.
         *@param sipResponse is the response coming into the proxy server
         *	that we are using to generate a new response.
         *@return string encoding of message.
         */
    
    public void
    newSIPResponse( int statusCode, SIPResponse sipResponse ) {
        if (sipResponse == null)
            throw new IllegalArgumentException("null arg!");
        Via via = (Via)sipResponse.getViaHeaders().first();
        HostPort sentBy = via.getSentBy();
        String stackname = sentBy.getHost().getHostname();
        String transport = via.getSentProtocol().getTransport();
        
        stashLoggingInformation(sipResponse,false);
        
        if ( stackname.compareTo
        (stack.getViaHeaderStackAddress()) != 0  ) {
            ServerLog.traceMsg(ServerLog.TRACE_DEBUG,
            "proxy not on via list.");
            return;
        } else if (transport.compareTo(UDP) == 0 &&
        sentBy.getPort() != stack.udpPort  ) {
            ServerLog.traceMsg(ServerLog.TRACE_DEBUG,
            "Dropping reply (wrong UDP port).");
            return;
        } else if ( transport.compareTo(TCP) == 0 &&
        sentBy.getPort() != stack.tcpPort) {
            ServerLog.traceMsg(ServerLog.TRACE_DEBUG,
            "Dropping reply (wrong TCP port).");
            return;
        }
	
	StatusLine newStatusLine = 
		(StatusLine)(sipResponse.getStatusLine().clone());
	newStatusLine.setStatusCode(statusCode);
        message =  new MessageAccumulator (newStatusLine.encode());
        firstLine = message.getHeaders().trim();
        
        SIPHeader[] sipHeaders = sipResponse.getHeaders();
        for (int i = 0; i < sipHeaders.length; i++) {
            SIPHeader sipHeader = sipHeaders[i];
            if (sipHeader instanceof ViaList ) {
                // Strip off the first header.
                ViaList vlist = (ViaList) sipHeader;
                ViaList clone = (ViaList) vlist.clone();
                clone.removeFirst();
                if (clone.isEmpty()) continue;
                appendToHeaders(clone.encode());
            } else  if (sipHeader instanceof ContentLength) {
                // add content length header separately below.
                continue;
	    } else if (sipHeader instanceof ContentType) {
                // add content type header separately below.
		continue;
            } else appendToHeaders(sipHeader.encode());
        }
        
        addMessageBody( sipResponse);
    }
    
        /**
         * Generate a response from a response (for proxying on).
         * Strip off the topmost via header (assuming it belongs to us)
         * and append the rest of the response unchanged.
         *@param sipResponse is the response coming into the proxy server
         *	that we are using to generate a new response.
         *@return string encoding of message.
         */
    
    public void
    newSIPResponse( SIPResponse sipResponse) {
        if (sipResponse == null)
            throw new IllegalArgumentException("null arg!");
        Via via = (Via)sipResponse.getViaHeaders().first();
        HostPort sentBy = via.getSentBy();
        String stackname = sentBy.getHost().getHostname();
        String transport = via.getSentProtocol().getTransport();
        
        stashLoggingInformation(sipResponse,false);
        
        if ( stackname.compareTo
        (stack.getViaHeaderStackAddress()) != 0  ) {
            ServerLog.traceMsg(ServerLog.TRACE_DEBUG,
            "proxy not on via list.");
            return;
        } else if (transport.compareTo(UDP) == 0 &&
        sentBy.getPort() != stack.udpPort  ) {
            ServerLog.traceMsg(ServerLog.TRACE_DEBUG,
            "Dropping reply (wrong UDP port).");
            return;
        } else if ( transport.compareTo(TCP) == 0 &&
        sentBy.getPort() != stack.tcpPort) {
            ServerLog.traceMsg(ServerLog.TRACE_DEBUG,
            "Dropping reply (wrong TCP port).");
            return;
        }
        message =  new MessageAccumulator
        (sipResponse.getStatusLine().encode());
        firstLine = message.getHeaders().trim();
        
        SIPHeader[] sipHeaders = sipResponse.getHeaders();
        for (int i = 0; i < sipHeaders.length; i++) {
            SIPHeader sipHeader = sipHeaders[i];
            if (sipHeader instanceof ViaList ) {
                // Strip off the first header.
                ViaList vlist = (ViaList) sipHeader;
                ViaList clone = (ViaList) vlist.clone();
                clone.removeFirst();
                if (clone.isEmpty()) continue;
                appendToHeaders(clone.encode());
            } else  if (sipHeader instanceof ContentLength) {
                // add content length header separately below.
                continue;
	    } else if (sipHeader instanceof ContentType) {
                // add content type header separately below.
		continue;
            } else appendToHeaders(sipHeader.encode());
        }
        
        addMessageBody( sipResponse);
    }
    
        /** add the message body. Currently, this assumes that
         * the message is encoded as a string.
         *@param sipMessage is the sipMessage from which we want to
         *   generate the body.
         */
    private void addMessageBody(SIPMessage sipMessage) {
       if (sipMessage.getSdpAnnounce() != null) {
           String encodedBody = sipMessage.getSdpAnnounce().encode();
           addMessageContent("application","sdp", encodedBody);
	   return;
        } 
        byte[] body =  sipMessage.getContentAsBytes();
        if (body != null ) {
            ContentType contentType = sipMessage.getContentTypeHeader();
	    if (contentType == null) return;
            addMessageContent( contentType.getContentType(),
                              contentType.getContentSubType(), body);
        } 

    }
    
    
        /**
         * Generate a response for a request. If there is a request specified,
         * we try to attach the callID and the client sequence number
         * information along with this message
         * so that the client can match the response with the request that was
         * previously sent out. 
	 * Use <A href=SIPMesageFormatter.html#getMessage>getMessage()</A>
	 * to retrieve the message.
         *
         *@param rc is the return code
         *@param sipMessage is the sip message for which we are generating a
         *	response.
         *@param errorInfo is the error information (details)
	 * 
         */
    public void
    newSIPResponse(int rc,
    SIPRequest sipRequest,
    String errorInfo)
    throws SIPException {
        
        ServerDebug.Assert (sipRequest != null, "null Arg");
        
        String msgstring = SIPException.getMessageString(rc);
        
        if (msgstring == null)
            throw new SIPException("Bad rc " + rc);
        stashLoggingInformation(sipRequest,false);
        
        
        message = new MessageAccumulator
        ( SIP_VERSION_STRING + SP + rc + SP + msgstring);
        if (errorInfo != null) {
            message.appendToHeaders(":" + errorInfo);
        }
        
        terminateHeader();
        // Stow away the first line for later logging.
        firstLine = message.getHeaders().trim();
        
        // Now generate the rest of the reply.
         int count = 0;
         ViaList viaList = sipRequest.getViaHeaders();
	 ListIterator it = viaList.listIterator();
	 while( it.hasNext()) {
	       Via v = (Via) it.next();
               count ++;
               HostPort hostPort = v.getSentBy();
               String host = hostPort.getHost().getHostname();
               String sender =
               messageChannel.getSenderAddress();
               if (sender.compareTo(host) != 0 &&
                  count == 1 ) {
                  String inp = v.encode();
                  int len = inp.length();
                  // strip off the crlf at the end.
                  // (encode adds crlf to the end)
                  String nv = inp.substring(0,len-2);
                  appendToHeaders(nv + SEMICOLON
                   + RECEIVED + EQUALS + sender );
                   terminateHeader();
               } else {
                  appendToHeaders(v.encode());
               }
        }
        From from = sipRequest.getFromHeader();
        if (from != null) {
            appendToHeaders(from.encode());
        }
        
        To to = sipRequest.getToHeader();
        if (to != null) {
            appendToHeaders(to.encode());
        }
        
        CallID cid = sipRequest.getCallIdHeader();
        if (cid != null) {
            appendToHeaders(cid.encode());
        }
        
        CSeq cseq = sipRequest.getCSeqHeader();
        if (cseq != null) {
            appendToHeaders(cseq.encode());
        }
	
        
    }
    
        /**
         *Generate a SIP response from a SIPRequest and append body type
         *and subtype where the body is given as a string.
	 *@param rc return code.
	 *@param sipRequest the request for which we want to generate response
	 *@param bodyType body type for the response.
	 *@param bodySubtype body subtype.
	 *@throws SIPException if there is a problem with the parameters.
         */
    public void newSIPResponse(int rc,
    SIPRequest sipRequest,
    String body,
    String bodyType,
    String bodySubtype)
    throws SIPException {
        newSIPResponse(rc, sipRequest, null);
        addMessageContent(bodyType,bodySubtype,body);
    }

        /**
         *Generate a SIP response from a SIPRequest and append body type
         *and subtype where the body is given as a string.
	 *@param rc return code.
	 *@param sipRequest the request for which we want to generate response
	 *@param bodyType body type for the response.
	 *@param bodySubtype body subtype.
	 *@throws SIPException if there is a problem with the parameters.
         */
    public void newSIPResponse(int rc,
    SIPRequest sipRequest,
    byte[] body,
    String bodyType,
    String bodySubtype)
    throws SIPException {
        newSIPResponse(rc, sipRequest, null);
        addMessageContent(bodyType,bodySubtype,body);
    }
    
    
        /**
         * Add a parameter to the request header.
         * @param name is the name of the parameter
         * @param value is the value of the parameter to add
         * @param separator is the separator to use (usually ;)
         *	that precedes the name/val pair
         */
    private void addParam( String name, String value, String separator ) {
        appendToHeaders(separator);
        appendToHeaders(name + EQUALS + value);
    }
    
    
        /** add a parameter to the current request header.
         *@param nameValue is the namevalue parameter.
         *@param separator is the separator
         */
    
    private void addParam(NameValue nv, String separator) {
        addParam(nv.getName(),(String)nv.getValue(),separator);
    }
    
        /** Add a list of parameters.
         *@param paramList is the list of parameters to add.
         *@param separator is the separator.
         */
    private void addParamList(NameValueList paramList, String separator) {
        if (paramList == null) return;
        ListIterator li = paramList.listIterator();
        while(li.hasNext()) {
            NameValue nv = (NameValue) li.next();
            addParam(nv,separator);
        }
        
    }
    
        /**
         * Add a parameter to the request header.
         * @param name is the name of the parameter
         * @param value is the value of the parameter to add
         * separator is assumed to be a semicolon.
         */
    private void addParam( String name, String value) {
        appendToHeaders(SEMICOLON);
        appendToHeaders(name + EQUALS + value);
    }
    
        /**
         *  Add a crlf at the end of a header.
         */
    private void terminateHeader() {
        appendToHeaders(NEWLINE);
    }
    
        /**
         *  Add a WWW-Authenticate header.
         *@param authHeader is the authorization header for which we want to add
         *	a WWWAuthentication header.
         *@param resource is the resource for which we want to add the
         *	header (typically the requestURI).
         */
    
    public void addWWWAuthenticateHeader(Authorization authHeader,
    String resource ) {
        String separator = "";
        AuthenticationMethod authenticationMethod = null;
        
        if (authHeader == null) {
            authenticationMethod =
            stack.getAuthMethod(stack.getDefaultAuthMethodName());
        } else {
            authHeader.dbgPrint();
            authenticationMethod =
            stack.getAuthMethod
            (authHeader.getScheme().toLowerCase());
            if (authenticationMethod == null) {
                authenticationMethod =
                (AuthenticationMethod)
                stack.getAuthMethod(stack.getDefaultAuthMethodName());
            }
        }
        
        appendToHeaders(WWW_AUTHENTICATE +  COLON +
        authenticationMethod.getScheme() + SP );
        String realm = null;
        realm = authenticationMethod.getRealm(resource);
        if (realm != null && realm != "") {
            addParam(REALM, ServerUtils.getQuotedString(realm),
            separator);
            separator = COMMA;
        }
        String domain = null;
        if (authHeader != null) {
            domain =   authHeader.getDomain();
        } else {
            domain = authenticationMethod.getDomain();
        }
        if (domain != null && domain != "") {
            addParam(DOMAIN,ServerUtils.getQuotedString(domain),
            separator);
            separator = COMMA;
        }
        String nonce = authenticationMethod.generateNonce();
        if ( nonce != null) {
            addParam(NONCE, ServerUtils.getQuotedString(nonce),
            separator);
            separator = COMMA;
        }
                /*
                String algorithm = authenticationMethod.getAlgorithm();
                if (algorithm != null) {
                        addParam(ALGORITHM,
                                ServerUtils.getQuotedString(algorithm),
                                                        separator);
                        separator = COMMA;
                }
                 */
        terminateHeader(); // Put a crlf at the end of the header
        
        
    }
    
    
        /**
         * Add a content type header.
         */
    public void addContentTypeHeader( String type, String subtype)
    throws IllegalArgumentException
    {
        if (type == null )
            throw new IllegalArgumentException("null type");
        
        String ttype;
        
        if (subtype != null) {
            ttype = type + SLASH + subtype;
        } else ttype = type;
        appendToHeaders(CONTENT_TYPE + COLON + SP + ttype );
        terminateHeader();
    }
    
    
        /**
         * Add an expires header.
         * @param deltaSeconds is the time (in seconds) to be
         *	specified in the expires header.
         */
    public void addExpiresHeader(int deltaSeconds) {
        appendToHeaders(EXPIRES + COLON + SP + deltaSeconds);
        terminateHeader();
    }
    
        /**
         * Add a contact record.
         * @param cr is the stack side notion of the contact record
         *	to be specified in the	header.
         */
    public void addContactHeader
    (Address contactAddr, long expiryTimeSec, String action ) {
        // Expiry tiime is in miliisecodns in the contact record.
        // Header needs for it to be in seconds.
        String expires = new Long(expiryTimeSec).toString();
        appendToHeaders(CONTACT + COLON + SP + contactAddr.encode());
        addParam(EXPIRE, expires);
        addParam(ACTION, action);
        terminateHeader();
    }
    
        /** Add a Contact record when the address is provided as a string.
         *@param displayName is the display name for the contact
         *@param address a string giving the contact address.
         *@param expiryTime Expiry time in seconds.
         *@param action keyword (proxy or redirect)
         */
    public void addContactHeader(
        String displayName,
        String address, 
        long expiryTimeSec, String action) {
         if (expiryTimeSec < 0  || address == null ) 
                throw new IllegalArgumentException("bad arg");
          String expires = new Long(expiryTimeSec).toString();
        if (displayName == null) {
            String contact = CONTACT + COLON + SP + LESS_THAN + address + 
                    GREATER_THAN;
            appendToHeaders(contact);
        } else {
            String contact = CONTACT + COLON + SP + Separators.DOUBLE_QUOTE 
                + displayName + Separators.DOUBLE_QUOTE + SP + LESS_THAN +
                 address + GREATER_THAN;
            appendToHeaders(contact);
        }
        addParam(EXPIRE, expires);
        if (action != null) addParam(ACTION, action);
        terminateHeader();
    }
    
      
    
    
    
        /**
         * Generate a request line with the given sipURI. (no correctness
         * checking is done for the given sipURI).
         * Note that this just starts the request (this is not a completed
         * request).
         * @param method is the method for the new request.
         * @param sipURI is the string representation of the
         *	SIP URI for the new request.
         */
    public String newSIPRequest ( String method, String sipURI) {
        message = new MessageAccumulator
        (method + SP + sipURI + SP + SIP_VERSION_STRING);
        this.uriString = sipURI;
        this.requestMethod = method;
	topmostViaAdded = false;
        terminateHeader();
        // stash logging information.
        firstLine = message.getHeaders().trim();
        return message.getHeaders();
    }
    
        /** Generate a new SIP Request for the given method
         * (directed to where the messageChannel points).
         *@param method SIP Method.
         */
    public void newSIPRequest(String method) {
        String uri = SIP + COLON + messageChannel.getHost() +
        COLON + messageChannel.getPort();
        addParam(TRANSPORT,messageChannel.getTransport());
        
	topmostViaAdded = false;
        message = new MessageAccumulator(
        method + SP + uri + SP + SIP_VERSION_STRING);
        terminateHeader();
        
    }
    
        /** Generate a request line givne the sipURI and  parameters.
         * @param method is the method for the new request.
         * @param sipURI is the string representation of the
         *	SIP URI for the new request.
         *@param uriParms is NameValue list of URI parameters for the sip
         * 	URI.
         */
    public String newSIPRequest ( String method,
    String sipURI, NameValueList uriParms ) {
        message =  new MessageAccumulator(
        method + SP + sipURI + SEMICOLON +
        uriParms.encode() +  SP + SIP_VERSION_STRING);
	topmostViaAdded = false;
        this.uriString = sipURI;
        this.requestMethod = method;
        terminateHeader();
        // stash logging information.
        firstLine = message.getHeaders().trim();
        return message.getHeaders().trim();
    }
    
    
    
        /**
         * Generate a new SIP Request for a given response. This
         * Extracts the relevant fields from the response and generates
         * a new sip request.
         * If we come across a via header, then do the receiver tagging stuff.
         * Add our own via header before others.
         * @param response is the SIPResponse in response to which we
         *	are constructing a new request.
         * @param newMethod is the new method for the new request.
         *	if null IllegalArgumentException is thrown.
         * @param requestURI encoded request URI (without parameters).
         * @param uriParms namevalue list of uri parameters.
         * @param recordRouteFlag set to true if we want to add a record route
         *    header in the newly generated request.
         * @param requireList is a list of methods for require headers.
         * @param proxyRequireList is a list of methods for 
	 *        ProxyRequire headers.
         * @return A transaction identifier for the newly generated request.
         *   (the request itself can be retrieved from the tid later with
         *  <A href="SIPMessageFormatter.html#getMessage()">getMessage()</A> )
         *@throws IllegalArgumentException if the method or requestURI is null.
         */
    
    public String newSIPRequest
    (  SIPResponse response	,
       String newMethod		,
       String requestURI	,
       NameValueList uriParms	,
       boolean recordRouteFlag	,
       LinkedList requireList	,
       LinkedList proxyRequireList  )  {
        if (newMethod == null)
            throw new IllegalArgumentException("null method!");
        if (uriParms == null) {
            firstLine = newMethod + SP + requestURI + SP +
            SIPVersion.SIP_VERSION_STRING + NEWLINE;
        } else {
            firstLine = newMethod + SP + requestURI + SEMICOLON +
            uriParms.encode() + SP + SIP_VERSION_STRING
            + NEWLINE;
            
        }
        appendToHeaders(firstLine);
        firstLine = firstLine.trim();
        this.requestMethod = newMethod;
        this.uriString = requestURI;
        String tid =  newSIPRequest
        	( response, recordRouteFlag,
	 	  true, requireList,proxyRequireList);
        put(tid);
        return tid;
    }
    
        /**
         * Generate a new SIP request that is formed by taking the
         * current SIP request and just wrapping it, adding our via information
         * before others.
         * If we come across a via header, then do the receiver tagging stuff.
         * Add our own via header before others.
         * @param request is the SIPRequest in response to which we
         *	are constructing a new request.
	 * @param uriString  is the new request uri to send this request to.
         * @param newMethod is the new method for the new request.
         *	  if null, the method in the given sipRequest is used.
         * @param recordRouteFlag set to true if we want to add a record route
         *    header in the newly generated request.
         * @param requireList is a list of methods for require headers.
         * @param proxyRequireList is a list of methods for 
	 *         ProxyRequire headers.
         * @return A transaction identifier for the newly generated request.
         *   (the request itself can be retrieved from the tid later).
         *
         */
    public  String
    newSIPRequest
    (SIPRequest request 		,
    String uriString ,
    String newMethod		,
    boolean recordRouteFlag	,
    LinkedList requireList       ,
    LinkedList proxyRequireList )
    {
	if (newMethod == null || uriString == null) 
		throw new  IllegalArgumentException("null arg");
        message = new MessageAccumulator();
        RequestLine requestLine = request.getRequestLine();
        firstLine = newMethod.toUpperCase() + SP +
            uriString + SP + requestLine.getSipVersion()+ NEWLINE;
        appendToHeaders(firstLine);
        firstLine = firstLine.trim();
        String newmethod = (newMethod == null ?
        requestLine.getMethod(): newMethod.toUpperCase());
        this.requestMethod = newmethod;
        String tid =  newSIPRequest
          ( request, recordRouteFlag,
	    true, requireList,proxyRequireList);
        // Put the request into the transaction table for later
        // retrieval.
        put(tid);
        // return the generated transaction ID.
        return tid;
        
    }
    
        /**
         * Generate a new SIP request that is formed by taking the
         * current SIP request and just wrapping it, adding our via information
         * before others.
         * If we come across a via header, then do the receiver tagging stuff.
         * Add our own via header before others.
         * @param request is the SIPRequest in response to which we
         *	are constructing a new request.
         * @param newMethod is the new method for the new request.
         *	  if null, the method in the given sipRequest is used.
         * @param recordRouteFlag set to true if we want to add a record route
         *    header in the newly generated request.
         * @param requireList is a list of methods for require headers.
         * @param proxyRequireList is a list of methods for ProxyRequire headers.
         * @return A transaction identifier for the newly generated request.
         *   (the request itself can be retrieved from the tid later).
         *
         */
    public  String
    newSIPRequest
    (SIPRequest request 	 ,
    String newMethod		 ,
    boolean recordRouteFlag	 ,
    LinkedList requireList       ,
    LinkedList proxyRequireList )
    {
        message = new MessageAccumulator();
        RequestLine requestLine = request.getRequestLine();
        String newmethod = (newMethod == null ?
        requestLine.getMethod(): newMethod.toUpperCase());
        this.requestMethod = newmethod;
	if (newmethod.compareToIgnoreCase(request.getMethod()) == 0) {
        	this.uriString = requestLine.getUri().encode();
	} else {
	    if (newmethod.compareToIgnoreCase("CANCEL") == 0 ) {
	           this.uriString = request.getRequestURI().encode();
	    } else {
	       // Replying to a request (for example send BYE to an INVITE)
	       if (request.getContactHeaders() != null) {
		   Contact contact = 
		     (Contact)request.getContactHeaders().first();
		   this.uriString = contact.getAddress().getAddrSpec().encode();
	       } else {
	           this.uriString = request.getRequestURI().encode();
	       }
	    }
	}

        if (newMethod == null) {
            firstLine = requestLine.encode();
        } else {
            firstLine = newMethod.toUpperCase() + SP +
              this.uriString + SP + requestLine.getSipVersion()+ NEWLINE;
        }
        appendToHeaders(firstLine);
        firstLine = firstLine.trim();
        String tid =  newSIPRequest
        		(request, recordRouteFlag, true,
           		requireList,proxyRequireList);
        // Put the request into the transaction table for later
        // retrieval.
        put(tid);
        // return the generated transaction ID.
        return tid;
        
    }
    
        /**
         * A private function that is called to generate the message
         * after the first line has been generated
         *@param sipMessage is the message from which we are generating
         * 	this message.
         *@param recordRouteFlag if true, then append RecordRoute header.
         *@param requireList a list of require headers.
         *@param proxyRequireList a list of proxyRequireHeaders.
         *@return the transactionId
         */
    private String newSIPRequest
    ( SIPMessage sipMessage,
      boolean recordRouteFlag,
      boolean branchIdFlag,
      LinkedList requireList,
      LinkedList proxyRequireList) {
        
        
        SIPHeader[] headers = sipMessage.getHeaders();

	if (sipMessage instanceof SIPRequest) {
	   SIPRequest sipRequest = (SIPRequest) sipMessage;
	   if (this.requestMethod.compareToIgnoreCase("BYE") == 0 &&
	       sipRequest.getMethod().compareToIgnoreCase("ACK")== 0){
               stashLoggingInformation(sipMessage,true);
	   } else stashLoggingInformation(sipMessage,false);
	} else stashLoggingInformation(sipMessage,false);
        
        // Add our VIA header to the partially formatted message.
        
         addViaHeader(sipMessage,branchIdFlag);
        
        
        // Add ourselves to the beginning of the Record route list if we
        // are just forwarding the request otherwise we just add a route 
        // request.
         if (sipMessage instanceof SIPRequest) {
            SIPRequest sipRequest = (SIPRequest) sipMessage;
            if (recordRouteFlag && 
                this.requestMethod.compareToIgnoreCase(sipRequest.getMethod())
                 == 0) {
                this.addRecordRouteHeader(stack.getStackURI());
             }
            // Generating a new request from an original request that is not of
            // the same kind (for example a BYE from an invite)
            if (this.requestMethod.compareToIgnoreCase
                (sipRequest.getMethod()) != 0) {
                 RecordRouteList rr = sipMessage.getRecordRouteHeaders();
                 if (rr != null) {
		     if (ServerLog.needsLogging()) {
			ServerLog.logMessage("adding route list");
		     }
                     RouteList rlist = rr.getRouteList();
                     this.addHeader(rlist);
                 }
            }
         } else {
             // Generating a new request from a response.
             if (recordRouteFlag) {
                 this.addRecordRouteHeader(stack.getStackURI());
             }
             RecordRouteList rr = sipMessage.getRecordRouteHeaders();
             if (rr != null) {
                 RouteList rlist = rr.getRouteList();
                 this.addHeader(rlist);
             }
         }
        
        
                 
            
        if (requireList != null) {
            ListIterator iterator = requireList.listIterator();
            while (iterator.hasNext()) {
                String require = (String) iterator.next();
                addRequireHeader(require);
            }
        }
        
        if (proxyRequireList != null) {
            ListIterator iterator = proxyRequireList.listIterator();
            while (iterator.hasNext()) {
                String require = (String) iterator.next();
                addProxyRequireHeader(require);
            }
        }
        
        for(int i = 0; i < headers.length; i++) {
            SIPHeader sipHeader = headers[i];
            if (sipHeader instanceof ContentLength) {
                // We will add content length separately.
                continue;
	    } else if (sipHeader instanceof ContentType) {
                // We will add content type separately.
		continue;
            } else if (sipHeader instanceof CSeq) {
                CSeq cseq = sipMessage.getCSeqHeader();
		
		if (sipMessage instanceof SIPResponse)  {
		    if ( this.requestMethod.compareToIgnoreCase("ACK") != 0  &&
			 this.requestMethod.compareToIgnoreCase("CANCEL") != 0 )
		     { 
			addCSeqHeader(stack.getCSeq(),this.requestMethod); 
		     } else addCSeqHeader(cseq.getSeqno(),this.requestMethod);
	        } else  {
		    SIPRequest req = (SIPRequest) sipMessage;
		    if ( this.requestMethod.equals(req.getMethod())) {
                        addCSeqHeader(cseq.getSeqno(),this.requestMethod);
		    } else {
			// Generate a new CSeq header for this  (this is a new
		        // transaction)
			if (this.requestMethod.compareToIgnoreCase("ACK")  == 0
			    || this.requestMethod.compareToIgnoreCase("CANCEL")
			     == 0 ) {
			    addCSeqHeader(cseq.getSeqno(),this.requestMethod);
			} else if (this.requestMethod.compareToIgnoreCase
				(req.getMethod()) != 0 ) {
			   addCSeqHeader(stack.getCSeq(),this.requestMethod);
			} else {
			   // Just forwarding request.
			   addCSeqHeader(cseq.getSeqno(),this.requestMethod);
			}
		    }

		}
            } else if (sipHeader instanceof ContentType ) {
                // Will add message content below
                continue;
            } else if (sipHeader instanceof ViaList  ) {
	        if (sipMessage instanceof SIPRequest )  {
		    SIPRequest sipRequest = (SIPRequest) sipMessage;
		     // If forwarding a Request then just append
		     // the existing via headers.
		    if (this.requestMethod.compareToIgnoreCase
			 (sipRequest.getMethod()) == 0 ) {
                	ViaList vlist = (ViaList)sipHeader;
                	ListIterator listIterator =
                		vlist.listIterator();
		    	while (listIterator.hasNext()) {
                        	Via v = (Via)listIterator.next();
				appendToHeaders(v.encode());
		     	}
		     } 
		 } 
	   } else if (sipHeader instanceof From) {
		if (sipMessage instanceof  SIPRequest ) {
		   // Generating BYE from ACK.
		   SIPRequest sipRequest = (SIPRequest)sipMessage;
		   if (this.requestMethod.compareToIgnoreCase("BYE") == 0 &&
			sipRequest.getMethod().compareToIgnoreCase("ACK")== 0){
		        To to = new To((From) sipHeader);
			appendToHeaders(to.encode());
		   } else appendToHeaders(sipHeader.encode());
		} else appendToHeaders(sipHeader.encode());
	   } else if (sipHeader instanceof To ) {
		if (sipMessage instanceof  SIPRequest ) {
		   // Generating BYE from ACK.
		   SIPRequest sipRequest = (SIPRequest)sipMessage;
		   if (this.requestMethod.compareToIgnoreCase("BYE") == 0 &&
		       sipRequest.getMethod().compareToIgnoreCase("ACK")== 0){
		       From from = new From((To) sipHeader);
		       appendToHeaders(from.encode());
		   } else appendToHeaders(sipHeader.encode());
		} else appendToHeaders(sipHeader.encode());
           } else {
                appendToHeaders(sipHeader.encode());
           }
        }
        addMessageBody(sipMessage);
        if (ServerLog.needsLogging() ) {
            ServerLog.logMessage( "Generated message = " + 
					message.getMessage());
            ServerLog.logMessage( "Original message = " + 
					sipMessage.encode());
        }
        // Note -- do not put this in the table.
        return this.getTransactionId();
    }
    
    
        /**
         *Generate a new SIPRequest from this request.
         *@param sipRequest from which to generate a new request.
         *@param newMethod is the new method to use for the new request
         *   generated.
         *@param recordRoute is set to true if we want to add a record route
         *  header to this sip request.
         */
    public String newSIPRequest(SIPRequest request, String newMethod,
    				boolean recordRoute ) {
        if (request == null || newMethod == null )
            throw new IllegalArgumentException("null arg");
        this.requestMethod = newMethod;
	if (request.getMethod().equals(newMethod)) {
            this.uriString = request.getRequestLine().getUri().encode();
	} else {
	    // Generating a cancel request.
	    if (newMethod.compareToIgnoreCase("CANCEL") == 0) {
	       // The cancel request has the same request URI as the
  	       // request being cancelled.
	       this.uriString = request.getRequestURI().encode();
	    } else {
	       // Replying to a request (for example send BYE to an INVITE)
	       if (request.getContactHeaders() != null) {
		   Contact contact = 
			(Contact)request.getContactHeaders().first();
		   this.uriString = contact.getAddress().getAddrSpec().encode();
	       } else {
	          this.uriString = request.getRequestURI().encode();
	       }
	   }
	}
        return this.newSIPRequest(request,newMethod,recordRoute,
        		null, null);
    }
    
         /**
          *Generate a new SIPRequest from this request. (The method that is
          * used is the same as the old method.
          *@param sipRequest from which to generate a new request.
          *@param recordRoute is set to true if we want to add a record route
          *  header to this sip request.
          *@param requireHeaders is the set of require method names to tack on
          */
    public String newSIPRequest(SIPRequest request, boolean recordRoute,
    LinkedList requireHeaders ) {
        if (request == null ) throw new
        IllegalArgumentException("null arg!");
        this.requestMethod = request.getRequestLine().getMethod();
	if (this.uriString == null)
            this.uriString = request.getRequestLine().getUri().encode();
        return this.newSIPRequest(request,null,recordRoute,
        	requireHeaders, null);
    }
    
        /**
         * Add a default via header with the branch identifier given.
         *@param params is a name value list giving the parameters.
         */
    
    public void addViaHeader(NameValueList params) {
        String host = stack.getViaHeaderStackAddress();
        String transport;
        if (messageChannel != null)  {
            transport = messageChannel.getTransport();
        } else transport = UDP;
        
        int	port = stack.getPort(transport);
		
	String bid = null;
	if (params != null) bid =  (String) params.getValue(SIPKeywords.BRANCH);
 	if (!topmostViaAdded) {
	     topmostViaBody = host + COLON + port;
             this.branchId = bid;
	     topmostViaAdded = true;
	}
		
        
        appendToHeaders(VIA+ COLON+ SP + SIP_VERSION_STRING +
        SLASH + transport + SP + host + COLON + port);
        if (params != null) addParamList(params,SEMICOLON);
        terminateHeader();
    }
    
    
    
    
        /**
         * Generate a new Via header (add ourselves to the via list).
         * If this is a SIPRequest we are responding to then we generate
         * a brance identifier provided we are processing an INVITE,
         * otherwise, we do not add a branch parameter.
         *@param sipMessage is the original sip message for which we want
         *   to generate the via header.
         *@param addBranchId is a flag which indicates whether we want
         *  to generate a branch id for this via header.
         */
    private  void  addViaHeader(SIPMessage sipMessage,
    boolean addBranchId ) {
        
	topmostViaAdded = true;
        String host = stack.getViaHeaderStackAddress();
        String transport = null;
        if (messageChannel != null) {
            transport = messageChannel.getTransport();
        } else transport = UDP;
        
        int	port = stack.getPort(transport);
        
        appendToHeaders(VIA+ COLON+ SP + SIP_VERSION_STRING +
        SLASH + transport + SP + host + COLON + port);
        if (sipMessage instanceof SIPRequest) {
            SIPRequest request = (SIPRequest) sipMessage;
            if (addBranchId) {
                String branchID = request.getBranchIdentifier();
                byte bid[] = messageDigest.digest(branchID.getBytes());
                String bidString = ServerUtils.toHexString(bid);
                String branch = bidString + DOT + stack.getNewTag();
                addParam(BRANCH,branch);
                terminateHeader();
                topmostViaBody = host + COLON + port ;
		this.branchId = branch;
            } else {
                terminateHeader();
                topmostViaBody = host + COLON + port;
            }
        } else {
            terminateHeader();
            topmostViaBody = host + COLON + port;
        }
        
    }
    
        /**
         * Add a record route header, recording our own address in it.
         *@param uri is the uri to add for the route header.
         */
    public void addRecordRouteHeader(String uri) {
        appendToHeaders(RECORD_ROUTE + COLON + SP + LESS_THAN +
        uri + SEMICOLON + MADDR +
        EQUALS + stack.stackAddress +
        GREATER_THAN);
        terminateHeader();
    }
    
        /**
         * Add a 'Require' header
         *@param hdrname is parameter for the Require header.
         */
    
    public void addRequireHeader(String hdrname ) {
        appendToHeaders(REQUIRE + COLON + SP + hdrname);
        terminateHeader();
    }
    
        /**
         * Add a ProxyRequire header.
         *@param hdrname is parameter for the Require header.
         */
    public void addProxyRequireHeader(String hdrname ) {
        appendToHeaders(PROXY_REQUIRE + COLON + SP + hdrname);
        terminateHeader();
    }
    
    
        /**
         * Add a Server header (experimental, for hacking purposes)
         */
    public void addServerHeader(String serverString) {
        appendToHeaders(SERVER + COLON + SP + serverString);
        terminateHeader();
    }
    
    
        /** Add a To header given displayName, userName, host.
         *@param displayName Display name.
         *@param userName user name.
         *@param host host address.
         */
    public void addToHeader (String displayName, String userName,
    	String host) {
	String body;
        if (displayName != null) {
            body = Separators.DOUBLE_QUOTE + displayName +
            Separators.DOUBLE_QUOTE + SP + LESS_THAN +
            SIP + COLON +
            userName + AT + host + GREATER_THAN;
        } else {
            body = SIP + COLON + userName + AT + host;
        }
        
	toBody = userName + AT + host;
	toTag =   null;
        appendToHeaders (TO + COLON + SP + body);
        terminateHeader();
    }
        /** Add a To header given displayName, userName, host.
         *@param displayName Display name.
         *@param userName user name.
         *@param host host address.
         *@param tag  tag parameter.
         */
    public void addToHeader (String displayName, String userName,
    	String host, String tag ) {
	if (userName == null || host == null || tag == null)
		throw new IllegalArgumentException("null argument");
	String body;
        if (displayName != null) {
            body = Separators.DOUBLE_QUOTE + displayName +
            Separators.DOUBLE_QUOTE + SP + LESS_THAN +
            SIP + COLON +
            userName + AT + host + GREATER_THAN;
        } else {
            body = SIP + COLON + userName + AT + host;
        }
	body += SEMICOLON + SIPKeywords.TAG + EQUALS + tag;
        
	toBody = userName + AT + host ;
	toTag = tag;
        appendToHeaders (TO + COLON + SP + body);
        terminateHeader();
    }
    
        /** Add a To header given displayName, userName, host,port.
         *@param displayName Display name.
         *@param userName user name.
         *@param host host address.
         */
    public void addToHeader (String displayName, String userName,
    	String host, int port) {
	String body;
        if (displayName != null) {
            body = Separators.DOUBLE_QUOTE + displayName +
            Separators.DOUBLE_QUOTE + SP + LESS_THAN +
            SIP + COLON +
            userName + AT + host + COLON + port + GREATER_THAN;
        } else {
            body = SIP + COLON + userName + AT + host + COLON + port;
        }
        
	toBody = userName + AT + host + COLON + port;
	toTag = null;
        appendToHeaders (TO + COLON + SP + body);
        terminateHeader();
    }
        /** Add a To header given displayName, userName, host,port.
         *@param displayName Display name.
         *@param userName user name.
         *@param host host address.
         *@param tag tag parameter.
         */
    public void addToHeader (String displayName, String userName,
    	String host, int port, String tag) {
	if (userName == null || host == null || tag == null || port <= 0){
		throw new IllegalArgumentException("bad or null arg");
	}
	String body;
        if (displayName != null) {
            body = Separators.DOUBLE_QUOTE + displayName +
            Separators.DOUBLE_QUOTE + SP + LESS_THAN +
            SIP + COLON +
            userName + AT + host + COLON + port + GREATER_THAN;
        } else {
            body = SIP + COLON + userName + AT + host + COLON + port;
        }
	body += SEMICOLON + SIPKeywords.TAG + EQUALS + tag;
        
	toBody = userName + AT + host + COLON + port;
	toTag = tag;
        appendToHeaders (TO + COLON + SP + body);
        terminateHeader();
    }
    
        /** Add a FROM header given displayName, userName, host.
         *@param displayName Display name.
         *@param userName user name.
         *@param host host address.
         */
    public void addFromHeader (String displayName, String userName,
    String host) {
	String body;
        if (displayName != null) {
	    body =
            Separators.DOUBLE_QUOTE + displayName +
            Separators.DOUBLE_QUOTE + SP + LESS_THAN +
            SIP + COLON +
            userName + AT + host + GREATER_THAN;
        } else {
            body = SIP + COLON + userName + AT + host;
        }
        
	fromBody = userName + AT + host;
        appendToHeaders (FROM + COLON + SP + body);
        terminateHeader();
    }
    
        /** Add a FROM header given displayName, userName, host,port
         *@param displayName Display name.
         *@param userName user name.
         *@param host host address.
         */
    public void addFromHeader (String displayName, String userName,
    String host, int port ) {
	String body;
        if (displayName != null) {
	    body =
            Separators.DOUBLE_QUOTE + displayName +
            Separators.DOUBLE_QUOTE + SP + LESS_THAN +
            SIP + COLON +
            userName + AT + host + COLON + port + GREATER_THAN;
        } else {
            body = SIP + COLON + userName + AT + host + COLON + port;
        }
        
	fromBody = userName + host + COLON + port ;
        appendToHeaders (FROM + COLON + SP + body);
        terminateHeader();
    }
    
        /**
         * Append the body and the content length of the body.
         */
    public void addMessageContent( String content) {
        message.addContent(content);
    }

    
        /**
         * Append the body and the content length of the body.
         */
    public void addMessageContent( byte[] content) {
        message.addContent(content);
    }
    
        /**
         * Generate a new Call ID and add it to this message.
         * Generate a new local identifier that is unique to this host.
         */
    public void  addCallIdHeader() {
        String date = (new Date()).toString() + 
			new Double(Math.random()).toString();
        byte cid[] = messageDigest.digest(date.getBytes());
        String cidString = ServerUtils.toHexString(cid);
        String myaddr  = stack.getHostAddress();
	// (sfo) omit hostname from CallIdHeader
	this.callIdBody = cidString + AT + "localhost";
        //this.callIdBody = cidString + AT + myaddr;
        appendToHeaders(CALL_ID + COLON + SP + this.callIdBody);
        terminateHeader();
        
    }
    
        /** add a supported header.
         *@param supported Supported parameter to add.
         */
    public void addSupportedHeader(String supported) {
        appendToHeaders(SUPPORTED + COLON + SP + supported);
        terminateHeader();
    }
    
        /** Add an unsupported header.
         *@param unsupported unsupported parameter to add.
         */
    public void addUnsupportedHeader(String unsupported) {
        appendToHeaders(UNSUPPORTED + COLON + SP + unsupported);
        terminateHeader();
    }
    
    
        /**
         * Add a Route header to the partially formatted header.
         *@param routeAddr is the URI string for the Route header.
         */
    public void addRouteHeader(String routeAddr ) {
        appendToHeaders(ROUTE + COLON + SP+ routeAddr);
        terminateHeader();
        
    }
    
    
        /**
         * Add an encoded SIPHeader.
         *@param SIPHeader to add.
         */
    public void addHeader(SIPHeader sipHeader) {
        appendToHeaders(sipHeader.encode());
    }
    
        /** Add an arbitrary header (given in the form of a string).
         *@param headerName header name.
         *@param headerBody headerBody.
         */
    public void addHeader(String headerName, String headerBody) {
        appendToHeaders (headerName + COLON + headerBody);
        terminateHeader();
    }
    
        /** Add content. Note - this appends an extra LF before
         * adding the content.
         *@param contentType is the content type of the content to be added.
         *@param contentSubType is the content subtype.
         *@param content is the content to be added (an automatically generated
         *  contentLength header is added to the message.
         */
    public void addMessageContent(String contentType,
    String contentSubType, String content) {
        if (content == null || contentType == null )
            throw new IllegalArgumentException("Null arg!");
        addContentTypeHeader(contentType,contentSubType);
        addMessageContent(content);
    }
    
        /** Add content as a byte array.
         *@param contentType is the content type.
         *@param contentSubType is the content subtype.
         *@param content is a byte array containing the mesage content.
         * An automatically generated content length header is appended
         * to the message.
         */
    public void addMessageContent(String contentType,
    String contentSubType, byte[] content) {
        if (content == null || contentType == null )
            throw new IllegalArgumentException("Null arg!");
        addContentTypeHeader(contentType,contentSubType);
        addMessageContent(content);
        
    }
    
    
    
    
        /** add an accept header.
         *@param acceptType accept type.
         *@param acceptSubtype accept subtype.
         */
    
    public void addAcceptHeader(String acceptType,
    String acceptSubtype) {
        appendToHeaders(ACCEPT + COLON + SP +
        acceptType + SLASH + acceptSubtype);
        terminateHeader();
        
    }
        /** Add a CSeq header.
         */
    public void addCSeqHeader(long cseq, String method) {
        // Stash away the cseq for later processing.
        this.cseqSeqno = cseq;
        this.cseqBody = new Long(cseq).toString() + COLON + method;
        appendToHeaders(CSEQ + COLON + SP + cseq + SP + method);
        terminateHeader();
    }
    
        /** Add an automatcially generated CSeqHeader (increments the CSeq
         * counter.
         */
    public void addCSeqHeader(String method) {
        addCSeqHeader(stack.getCSeq(),method);
        
    }
    
    
        /**
         * Append a string to our message buffer.
         * Header must be terminated with a \n
         * @param str is the string to append to the accumulator.
         */
    private void appendToHeaders( String str ) {
        message.appendToHeaders(str);
    }
    
        /**
         *Get a formatted message from the transaction table
         *(or null if none exists).
         *@param tid is the transaction id that was previously generated.
         *@param destroyOnRead is set to true if we want to delete from the
         *  transaction table on read.
         */
    public String  getMessage(String tid, boolean destroyOnRead) {
	if (ServerLog.needsLogging() )
	    ServerLog.traceMsg(ServerLog.TRACE_DEBUG,"getting " + tid);
        MessageAccumulator retval =
        (MessageAccumulator)this.clientTransactionTable.get(tid);
        if ( retval != null && destroyOnRead) {
            lastTid = null;
            this.clientTransactionTable.remove(tid);
            message = new MessageAccumulator();
	    this.uriString = null;
        }
	if (retval != null) return retval.getMessage();
	else return null;
    }
        /**
         *Get a formatted message from the transaction table
         *(or null if none exists).
         *@param tid is the transaction id that was previously generated.
         *@param destroyOnRead is set to true if we want to delete from the
         *  transaction table on read.
         */
    public byte[]  getMessageAsBytes(String tid, boolean destroyOnRead) {
	if (ServerLog.needsLogging() )
	    ServerLog.traceMsg(ServerLog.TRACE_DEBUG,"getting " + tid);
        MessageAccumulator retval =
        (MessageAccumulator)this.clientTransactionTable.get(tid);
        if ( retval != null && destroyOnRead) {
            lastTid = null;
            this.clientTransactionTable.remove(tid);
            message = new MessageAccumulator();
	    this.uriString = null;
        }
	if (retval != null) return retval.getMessageAsBytes();
	else return null;
    }
    
    
        /**
         * Return the CSeq header from the formatted message
         * @return The CSeq header as a String (does not include headerName:)
         */
    public String getCSeq() {
        return cseqBody;
    }
    
    
        /**
         * Return the CallId header from the formatted message
         * @return The CallId header as a String (does not include headerName:)
         */
    public String getCallId() {
        return callIdBody;
    }
    
        /**
         * Return the requestURI string.
         */
    public String getRequestURI() {
        return uriString;
    }
    
    
        /**
         * Return the first line from the formatted message
         * @return The first line of the formatted message a String
         */
    public String getFirstLine() {
        return firstLine;
    }

	/** 
	* Get the request method.
	*/
    public String getRequestMethod() {
	return requestMethod;
    }
    
    
    
}
