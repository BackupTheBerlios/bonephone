/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).       *
 * See ../../../../doc/uncopyright.html for conditions of use.                 *
 * Author: M. Ranganathan (mranga@nist.gov)                                    *
 * Modified by: Marc Bednarek (bednarek@nist.gov) 			       *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                              *
 *******************************************************************************/
/******************************************************
 * File: UDPMessageChannel.java
 * created 28-Aug-00 4:27:43 PM by mranga
 */


package gov.nist.sip.stack;
import java.net.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;
import java.util.LinkedList;
import java.io.IOException;
import java.lang.String;

/**
 *  This is the UDP Message handler that gets created when a UDP message
 *  needs to be processed. The message is processed by creating a String
 *  Message parser and invoking it on the message read from the UPD socket.
 *  The parsed structure is handed off via a SIP stack request for further
 *  processing. This stack structure isolates the message handling logic
 *  from the mechanics of sending and recieving messages (which could
 *  be either udp or tcp.
 *@see gov.nist.sip.msgparser.StringMsgParser
 *@see gov.nist.sip.stack.SIPServerRequestInterface
 *@author <A href=mailto:mranga@nist.gov> M. Ranganathan </A>
 *@since v1.0
 */


public class UDPMessageChannel
extends  MessageChannel
implements  SIPParseExceptionListener,
SIPKeywords, Runnable
{
    
/** Channel notifier (gets called on new channel creates).
 */    
    protected ChannelNotifier notifier;
/** SIP Stack structure for this channel.
 */    
    protected SIPStack stack;
/** The parser we are using for messages received from this channel.
 */    
    protected StringMsgParser myParser;
/** Message formatter.
 */    
    private SIPMessageFormatter messageFormatter;
/** Sender address (from getPeerName())
 */    
    private InetAddress senderAddress;
/** Receiver address (extracted from the via list)
*/
    private InetAddress receiverAddress;
/** Reciever port.
*/
    private int receiverPort;

/** Linked list of unparseable headers.
 */    
    protected LinkedList badHeaders;
/** Linked list of bad sdp fields.
 */    
    protected LinkedList badSDPFields;
/** linked list of unparsed unsuppored extension headers.
 */    
    protected LinkedList extensionHeaders;

    private  byte[] msgBytes;
	

    private int packetLength;


    private DatagramPacket incomingPacket;
    
    /**
     * Constructor - takes a datagram packet and a stack structure
     * Extracts the address of the other from the datagram packet and
     * stashes away the pointer to the passed stack structure.
     * @param packet is the UDP Packet that contains the request.
     * @param srv stack is the shared SIPStack structure
     * @param notifier Channel notifier (not very useful for UDP).
     *
     */
    public UDPMessageChannel(DatagramPacket packet, SIPStack srv,
    ChannelNotifier notifier ) {
	incomingPacket = packet;
        badHeaders = new LinkedList();
        badSDPFields = new LinkedList();
        extensionHeaders = new LinkedList();
	Thread mythread = new Thread(this);
        stack = srv;
        senderAddress = packet.getAddress();
	if (ServerLog.needsLogging())
            ServerLog.traceMsg(ServerLog.TRACE_DEBUG,
        	"UDPMessageChannel: senderAddress = " +
        	senderAddress + "/" + packet.getPort());
        packetLength = packet.getLength();
	if (ServerLog.needsLogging()) {
            ServerLog.traceMsg(ServerLog.TRACE_DEBUG,
			"Length = " + packetLength);
	}
        byte[] bytes =  packet.getData();
	msgBytes = new byte[packetLength];
	for (int i = 0; i < packetLength; i++) {
		msgBytes[i] = bytes[i];
	}
	if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES)) {
           String msgString = new String(msgBytes,0,packetLength);
	   ServerLog.traceMsg(ServerLog.TRACE_MESSAGES,msgString);
	}
	mythread.start();
    }
    

     /** Constructor. We create one of these when we send out a message.
     *@param targetAddr INET address of the place where we want to send
     *		messages.
     *@param port target port (where we want to send the message).
     *@param stack our SIP Stack.
     */
    public UDPMessageChannel(InetAddress targetAddr, int port, 
		SIPStack sipStack) {
	if (ServerLog.needsLogging(ServerLog.TRACE_DEBUG)) {
	  ServerLog.logMessage( "Creating message channel " +
			targetAddr + "/" + port);
	}
        badHeaders = new LinkedList();
        badSDPFields = new LinkedList();
        extensionHeaders = new LinkedList();
	senderAddress = targetAddr;
	senderPort = port;
	receiverAddress = targetAddr;
	receiverPort = port;
	stack = sipStack;
    }

    /**
    * Run method specified by runnnable.
    */

    public void run() {
        SIPMessage[] sipMessages = null;
        // Create a new string message parser to parse the list of messages.
        // This is a huge performance hit -- need to optimize by pre-create
        // parser when one is needed....
        myParser = new StringMsgParser();
	if (stack.parseSDP) myParser.parseContent();
	if (stack.disableInputTracking) myParser.disableInputTracking();
        myParser.setParseExceptionListener(this);
        // messages that we write out to him.
        
        if (stack.debugFlag)  myParser.enableDebugFlag();

        messageFormatter = new SIPMessageFormatter(stack, this);
        try {
            sipMessages  = myParser.parseSIPMessage(msgBytes);
        } catch ( SIPParseException ex) {
            ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,
				"Rejecting message! ");
            ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,"Header " +
            ex.getText());
            ServerLog.logException(ex);
            return;
        }
        // Notify application that a new message channel has been created.
        if (notifier != null ) {
            notifier.notifyOpen(this);
        }
        // No parse exception.
        for ( int i = 0 ; i < sipMessages.length; i++) {
            SIPMessage sipMessage = sipMessages[i] ;
	    if (sipMessage == null) continue;
            ViaList viaList = sipMessage.getViaHeaders();
	   // For a request first via header tells where the message 
	   // is coming from.
	   // For response, just get the port from the packet.
	    this.senderPort = incomingPacket.getPort();
	    if (sipMessage instanceof SIPResponse) {
		   InetAddress inetAddr = incomingPacket.getAddress();
		   this.senderAddress   = inetAddr;
		   this.receiverAddress = inetAddr;
		   this.receiverPort = this.senderPort;
	    } else {
               Via v = (Via)viaList.first();
               if (v.hasPort() ) {
		   if (sipMessage instanceof SIPRequest)  {
                	this.receiverPort = v.getPort();
		   } 
               }  else this.receiverPort = SIPStack.DEFAULT_PORT;
               try {
                this.receiverAddress = v.getSentBy().getInetAddress();
               } catch (java.net.UnknownHostException ex) {
                   // Could not resolve the sender address.
                   ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,
                        "Rejecting message -- could not resolve Via Address");
                   continue;
               }
	    }
            if (sipMessage instanceof SIPRequest) {
                SIPRequest sipRequest = (SIPRequest) sipMessage;
                // This is a request - process it.
                SIPServerRequestInterface sipServerRequest =
                stack.newSIPServerRequest(sipRequest,this);
                try {
                    sipServerRequest.processRequest();
                } catch (SIPServerException ex) {
                    handleException(ex);
                }
            } else {
                // Handle a SIP Reply message.
                SIPResponse sipResponse = (SIPResponse) sipMessage;
                SIPServerResponseInterface sipServerResponse =
                stack.newSIPServerResponse(sipResponse,this);
                try {
                    sipServerResponse.processResponse();
                } catch (SIPServerException ex) {
                    ServerLog.logException(ex);
                }
            }
        }
     }
    
    /**
     * Get the list of bad headers.
     * @return The list of bad header strings.
     */
    public LinkedList getBadHeaders() {
        return badHeaders;
    }
    
    /**
     * Get the list of bad SDP Fields.
     * @return The list of bad sdp field strings.
     */
    public LinkedList getBadSDPFields() {
        return badSDPFields;
    }
    
    
    /**
     * Get the list of extension headers (that have not yet been parsed)
     *@return a list of extension headers for which no parsers have been
     *  registered.
     */
    public LinkedList getExtensionHeaders() {
        return extensionHeaders;
    }
    
    
    /**
     * Implementation of the ParseExceptionListener interface.
     * @param ex Exception that is given to us by the parser.
     * @throws SIPParseException If we choose to reject the header or message.
     */
    
    public void handleException( SIPParseException ex)
    throws SIPParseException {
        if (ex instanceof SIPHeaderParseException ) {
            ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,
            "Illegal Header : " + ex.getText());
            // Ignore the bad header. (if this is required
            // header we will get a missingHeader  below
            ex.rejectErrorObject();
            badHeaders.add(ex.getText());
        }  else if (ex instanceof SDPParseException) {
            ex.rejectErrorObject();
            badSDPFields.add(ex.getText());
        } else if (ex instanceof SIPUnrecognizedExtensionException ) {
            ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,
            "Unsupported extension " + ex.getText());
            ex.rejectErrorObject();
            SIPUnrecognizedExtensionException ure =
            (SIPUnrecognizedExtensionException) ex;
            String hdrName = ure.getExtensionName();
            
            ExtensionParser parser =
            stack.getExtensionParser(hdrName);
            if (parser == null) {
                ExtensionHeader extHdr =
                new ExtensionHeader(ure.getExtensionName());
                extHdr.setInputText(ex.getText());
	        ExtensionHeaderList hlist = new ExtensionHeaderList
					(ure.getExtensionName());
	        hlist.add(extHdr);
	        ure.setHeader(hlist);
                extensionHeaders.add(ex.getText());
            } else {
                try {
                    SIPHeader hdr =
                    parser.parseExtensionHeader(ex.getText());
                    ure.setHeader(hdr);
                } catch (SIPParseException e) {
                    badHeaders.add(ex.getText());
                    throw e;
                }
            }
        } else if ( ex instanceof SIPUnexpectedHeaderException ) {
            // Ignore unexpected headers.
            badHeaders.add(ex.getText());
            ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,
            "Unexpected Header " + ex.getText());
            ex.rejectErrorObject();
        } else if ( ex instanceof SIPInvalidHeaderException ) {
            badHeaders.add(ex.getText());
            ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,
            "IllegalHeader " + ex.getText());
            ex.rejectErrorObject();
        } else {
            ex.rejectErrorObject();
            ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,
            ex.getClass().getName() + ":" + ex.getText());
            throw ex;
        }
    }
    
    
   /** Return a reply from a pre-constructed reply. This sends the message
    * back to the entity who caused us to create this channel in the
    * first place. 
    * @param msg Message string to send.
    * @throws IOException If there is a problem with sending the
    * message.
    */
    public void sendMessage (SIPMessage sipMessage) throws IOException {
	byte[] msg = sipMessage.encodeAsBytes();
	if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES)) 
		logMessage(sipMessage,receiverAddress,receiverPort);
        sendMessage(msg, receiverAddress, receiverPort);
    }
    
   /** Return a reply from a pre-constructed reply. This sends the message
    * back to the entity who caused us to create this channel in the
    * first place. Also logs the message
    * @param msgFormatter message formatter  from which the message is
    *	to be retrieved and sent.
    * @throws IOException If there is a problem with sending the
    * message.
    */
    public void sendMessage (SIPMessageFormatter msgFormatter) 
	throws IOException {
	if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES)) 
	    logMessage(msgFormatter, receiverAddress, receiverPort);
        sendMessage(msgFormatter.getMessageAsBytes(false));
    }
    
/** Send a message to a specified receiver address and log it.
 * @param msgFormatter message formatter from which to extract message and
 * send.
 * @param receiverAddress Address of the place to send it to.
 * @param receiverPort the port to send it to.
 * @throws IOException If there is trouble sending this message.
 */    
    public void sendMessage(SIPMessageFormatter msgFormatter, 
			    InetAddress receiverAddress,
			    int receiverPort) throws IOException {
	if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES)) 
	    logMessage(msgFormatter, receiverAddress, receiverPort);
	sendMessage(msgFormatter.getMessageAsBytes(false), 
		receiverAddress, receiverPort);
    }

/** Send a message to a specified receiver address.
 * @param msg message string to send.
 * @param receiverAddress Address of the place to send it to.
 * @param receiverPort the port to send it to.
 * @throws IOException If there is trouble sending this message.
 */    
    protected void sendMessage(byte[] msg, InetAddress receiverAddress,
    int receiverPort) throws IOException {
        // msg += "\r\n\r\n";
        // Via is not included in the request so silently drop the reply.
        if (receiverPort == -1) {
            ServerLog.traceMsg(ServerLog.TRACE_MESSAGES,
            getClass().getName()+":sendMessage: Dropping reply!");
            throw new IOException("Receiver port not set ");
            // if (stack.debugFlag)  {
            //   ServerInternalError.handleException("DroppingReply");
            // }
        } else {
            ServerLog.traceMsg(ServerLog.TRACE_MESSAGES,
            getClass().getName()+":sendMessage " + receiverAddress + "/" +
            receiverPort + "\n" + new String(msg));
            ServerLog.traceMsg(ServerLog.TRACE_MESSAGES,
            "*******************\n");
        }
        DatagramPacket reply = new DatagramPacket(msg, 
		msg.length, receiverAddress, receiverPort);
        try {
            DatagramSocket sock = new DatagramSocket();
            sock.send(reply);
            sock.close();
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            ServerInternalError.handleException(ex);
        }
    }
    
   /** get the stack pointer.
    * @return The sip stack for this channel.
    */
    public SIPStack getSIPStack() {
        return stack;
    }
    
   /**
    * Return a transport string.
    * @return the string "udp" in this case.
    */
    
    public String getTransport() {
        return UDP;
    }
    
   /**
    * get the stack address for the stack that received this message.
    * @return The stack address for our stack.
     */
    public String getHost() {
        return stack.stackAddress;
    }
   /** get the port.
    * @return Our port (on which we are getting datagram
    * packets).
    */
    public int getPort() {
        return stack.udpPort;
    }
    
    
   /** Handle an exception - construct a sip reply and send it back to the 
    * caller.
    * @param ex The exception thrown at us by our 
    * application.
    */
    
    public void handleException (SIPServerException ex) {
        // Return a parse error message to the client on the other end
        // if he is still alive.
        int rc = ex.getSipRC();
        SIPRequest request = (SIPRequest) ex.getSIPMessage();
        String msgString = ex.getMessage();
        if (rc != 0) {
            try {
                messageFormatter.newSIPResponse(rc,request,msgString);
            } catch (SIPException exc) {
                ServerInternalError.handleException(exc);
            }
            try {
                sendMessage(messageFormatter.getMessageAsBytes(false));
            } catch (IOException ioex) {
                ServerLog.logException(ioex);
            }
        }  else {
            // Assume that the message has already been formatted.
            try {
                sendMessage(msgString);
            } catch (IOException ioex) {
                ServerLog.logException(ioex);
            }
        }
    }
    
   /** get the name (address) of the host that sent me the message
    * @return The name of the sender (from 
    * the datagram packet).
    */
    public String getSenderName () {
        return senderAddress.getHostName();
    }
    
   /**
    * get the address of the host that sent me the message
    * @return The senders ip address.
    */
    public String getSenderAddress () {
        return senderAddress.getHostAddress();
    }
    
   /**
    * get our message formatter.
    */
    public SIPMessageFormatter getMessageFormatter() 
	{ return messageFormatter; }

    /** Compare two UDP Message channels for equality.
    *@param other The other message channel with which to compare oursleves.
    */
    public boolean equals (Object other) {
      
        if (other == null ) return false;
	boolean retval;
	if (!this.getClass().equals(other.getClass())) {
		retval =  false;
	} else {
	   UDPMessageChannel that = (UDPMessageChannel) other;
	   retval =  this.senderAddress.equals(that.senderAddress);
	}
	
	return retval;
    }
    
      
	
    public String getKey() {
	return getKey(senderAddress,senderPort,"UDP");
    }


    private void sendMessage(String msg) 
	throws IOException
    {
	sendMessage(msg.getBytes(),receiverAddress,receiverPort);
    }

    private void sendMessage(byte[] msg) 
	throws IOException
    {
	sendMessage(msg,receiverAddress,receiverPort);
    }
    
    /** Get the logical originator of the message (from the top via header).
     *@return topmost via header sentby field
     */
    public String getViaHost() { 
	return this.receiverAddress.getHostAddress();
    }

    /** Get the logical port of the message orginator (from the top via hdr).
    *@return the via port from the topmost via header.
    */
    public int getViaPort() { return this.receiverPort; }
	
}
