/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
 * See ../../../../doc/uncopyright.html for conditions of use.                  *
 * Author: M. Ranganathan (mranga@nist.gov)                                     *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                               *
 *******************************************************************************/
package gov.nist.sip.stack;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;
import java.net.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.LinkedList;
/**
 * This is stack for TCP connections. The SIP stack starts this from
 * the main SIPStack class for each connection that it accepts. It starts
 * a message parser in its own thread and talks to the message parser via
 * a pipe. The message parser calls back via the parseError or processMessage
 * functions that are defined as part of the SIPMessageListener interface.
 * @see gov.nist.sip.msgparser.PipelinedMsgParser
 * @see gov.nist.sip.msgparser.SIPMessage
 * @version 1.0
 * @author <A href=mailto:mranga@nist.gov> M.Ranganathan  </A>
 */


public final class TCPMessageChannel
extends MessageChannel
implements SIPMessageListener, Runnable
{
    
/** Linked list of unparseable Headers.
 */
    protected LinkedList badHeaders;
/** Linked list of unparsed (unsuppored) extension headers.
 */
    protected LinkedList extensionHeaders;
/** Linked list of unparseable SDP fields.
 */
    protected LinkedList badSDPFields;
/** Channel notifier (method of this gets called on channel open/close)
 */
    protected ChannelNotifier notifier;
    
    private Socket mySock;
    private PipelinedMsgParser myParser;
    private InputStream myClientInputStream;   // just to pass to thread.
    private OutputStream myClientOutputStream;
    
    private SIPMessageFormatter messageFormatter;
    private SIPStack stack;
    
    private InetAddress senderAddress;
    
    private InetAddress receiverAddress;
    
    private int receiverPort;
    private int senderPort;
    
    
        /**
         * Constructor - gets called from the SIPStack class with a socket
         * on accepting a new client. All the processing of the message is
         * done here with the stack being freed up to handle new connections.
         * The sock input is the socket that is returned from the accept.
         * Global data that is shared by all threads is accessible in the Server
         * structure.
         * @param sock Socket from which to read and write messages. The socket
         *   is already connected (was created as a result of an accept).
         *
         * @param sipStack Ptr to SIP Stack
         * @param channelNotifier Notifier (optinal) that gets called when
         * the channel is opened or closed.
         */
    public TCPMessageChannel( Socket sock, SIPStack sipStack,
    ChannelNotifier channelNotifier ) {
        mySock = sock;
        try {
            myClientInputStream = mySock.getInputStream();
            myClientOutputStream = mySock.getOutputStream();
            senderAddress = mySock.getInetAddress();
        } catch ( IOException ex) {
            ServerInternalError.handleException(ex);
        }
        Thread mythread = new Thread(this);
        // Stash away a pointer to our stack structure.
        stack = sipStack;
        badHeaders = new LinkedList();
        badSDPFields = new LinkedList();
        extensionHeaders = new LinkedList();
        messageFormatter = new SIPMessageFormatter(stack, this);
        notifier = channelNotifier;
        mythread.start();
    }
    
        /**
         *Constructor - connects to the given inet address.
         *@param inetAddr inet address to connect to.
         *@param sipStack is the sip stack from which we are created.
         *@param channelNotifier is the channel notifier that is called
         *   when we are closed.
         *@throws IOException if we cannot connect.
         */
    public TCPMessageChannel
    ( InetAddress inetAddr,
    int port,
    SIPStack sipStack,
    ChannelNotifier channelNotifier)
    throws IOException {
        mySock = new Socket(inetAddr,port);
        stack = sipStack;
        myClientInputStream = mySock.getInputStream();
        myClientOutputStream = mySock.getOutputStream();
        badHeaders = new LinkedList();
        badSDPFields = new LinkedList();
        extensionHeaders = new LinkedList();
        messageFormatter = new SIPMessageFormatter(stack, this);
        notifier = channelNotifier;
        Thread mythread = new Thread(this);
        mythread.start();
        
    }
    
    
    
    
        /**
         * Get the headers that did not parse correctly.
         * @return Linked list of Strings each string is a
         * header that could not be parsed correctly.
         */
    
    public LinkedList getBadHeaders() {
        return this.badHeaders;
    }
    
        /**
         * Get the bad SDP Fields.
         * @return A list of Strings. Each string is an SDP
         * Field that could not be parsed correctly.
         */
    public LinkedList getBadSDPFields() {
        return this.badSDPFields;
    }
    
        /**
         * Get the list of extension headers (that are not parsed).
         * @return A list of Strings. Each string is an
         * extension header that has not been parsed.
         */
    public LinkedList getExtensionHeaders() {
        return this.extensionHeaders;
    }
    
    
        /** Get my SIP Stack.
         * @return The SIP Stack for this message channel.
         */
    public SIPStack getSIPStack() {
        return stack;
    }
    
        /** get the transport string.
         * @return "tcp" in this case.
         */
    public String getTransport() {
        return "TCP";
    }
    
        /** get the port on which messages were received
         * @return The port on which we receive messages.
         */
    public int getPort() {
        return stack.tcpPort;
    }
    
    
        /** get the hostname from the stack structure.
         * @return The host address as a string.
         */
    public String getHost() {
        return stack.stackAddress;
    }
    
        /** get the name of the client that sent the data to us.
         * @return The peer name of the entity on the other end
         * who sent us a message (causing this channel
         * to be created).
         */
    public String getSenderName() {
        return senderAddress.getHostName();
    }
    
        /** get the address of the client that sent the data to us.
         * @return Address of the client that sent us data
         * that resulted in this channel being
         * created.
         */
    public String getSenderAddress() {
        return senderAddress.getHostAddress();
    }
    
        /** Returns ptr to a message formatter structure.
         * @return SIPMessageFormatter for this MessageChannel
         */
    public SIPMessageFormatter getMessageFormatter() {
        return  messageFormatter;
    }
    
        /** Send a message to the client (who caused us to create this
         * channel in the first place). This retrieves the formatted message
         * from the MessageFormatter and sends it off.
         * @throws IOException When there is an IO exception in
         * sending the message.
         */
    public void sendMessage() throws IOException {
        sendMessage(messageFormatter);
        // clear out the existing message.
        String msg = messageFormatter.getMessage();
    }
    
    
    
        /** Return a formatted message to the client and log it.
         * @param msgFormatter Message to send.
         * @throws IOException If there is an error sending the message
         */
    public void sendMessage (SIPMessageFormatter msgFormatter)
    throws IOException {
        byte[] msg = msgFormatter.getMessageAsBytes(false);
        if (msg == null) {
            if (ServerLog.needsLogging(ServerLog.TRACE_DEBUG)) {
                ServerLog.traceMsg(ServerLog.TRACE_DEBUG,
                "Dropping null message");
                
            }
            return;
        }
        sendMessage(msg);
        if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES)) {
            ServerLog.traceMsg(ServerLog.TRACE_MESSAGES,
            "Reply to  " + getSenderName());
            logMessage(msgFormatter, senderAddress, senderPort);
        }
    }
    
        /** Send message to whoever is connected to us.
         *@param message is the message to send.
         */
    private void sendMessage(byte[] msg) throws IOException {
        String term = "\r\n\r\n";
        if (receiverAddress.equals(this.senderAddress) &&
        this.receiverPort == this.senderPort) {
            try {
                myClientOutputStream.write(msg, 0, msg.length);
                myClientOutputStream.write
                (term.getBytes(),0,term.length());
                myClientOutputStream.flush();
            } catch ( IOException ex) {
                // Try to re-connect and send the message.
                mySock = new Socket(senderAddress,senderPort);
                myClientInputStream = mySock.getInputStream();
                myClientOutputStream = mySock.getOutputStream();
                myClientOutputStream.write(msg, 0, msg.length);
                myClientOutputStream.write
                (term.getBytes(),0,term.length());
                myClientOutputStream.flush();
            }
        } else {
            Socket outputSocket = new Socket(this.receiverAddress,
                                             this.receiverPort);
            OutputStream myOutputStream = outputSocket.getOutputStream();
            myOutputStream.write(msg, 0, msg.length);
            myOutputStream.write(term.getBytes(),0,term.length());
            myOutputStream.flush();
        }
        
    }
    
        /** Return a formatted message to the client.
         * We try to re-connect with the peer on the other end if possible.
         * @param msg Message to send.
         * @throws IOException If there is an error sending the message
         */
    public void sendMessage(SIPMessage sipMessage) throws IOException {
        byte[] msg = sipMessage.encodeAsBytes();
        this.sendMessage(msg);
       
    }
    
    
/** Send a message to a specified address.
 * @param message Pre-formatted message to send.
 * @param receiverAddress Address to send it to.
 * @param receiverPort Receiver port.
 * @throws IOException If there is a problem connecting or sending.
 */
    public void sendMessage(byte message[], InetAddress receiverAddress,
    int receiverPort)
    throws IOException{
        if (message == null || receiverAddress == null)
            throw new IllegalArgumentException("Null argument");
        
        Socket socket = new Socket(receiverAddress,receiverPort);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(message);
        socket.close();
        
    }
    
        /** Exception processor for exceptions detected from the application.
         * @param ex The exception that was generated.
         */
    public void handleException( SIPServerException   ex ) {
        // Return a parse error message to the client on the other end
        // if he is still alive.
        int rc = ex.getSipRC();
        String msgString = ex.getMessage();
        if (rc != 0 ) {
            // Do we have a valid Return code ? --
            // in this case format the message.
            SIPRequest request =
            (SIPRequest) ex.getSIPMessage();
            try {
                messageFormatter.newSIPResponse
                (rc,request,msgString);
            } catch (SIPException exc) {
                ServerInternalError.handleException(exc);
            }
            try {
                sendMessage();
            } catch (IOException ioex) {
                ServerLog.logException(ioex);
            }
        } else {
            // Otherwise, message is already formatted --
            // just return it.
            try {
                sendMessage(msgString.getBytes());
            } catch (IOException ioex) {
                ServerLog.logException(ioex);
            }
        }
    }
    
    
        /** Exception processor for exceptions detected from the parser. (This
         * is invoked by the parser when an error is detected).
         * @param ex The exception generated by the parser.
         * @throws SIPParseException Thrown if we want to reject the message.
         */
    public void handleException (SIPParseException ex)
    throws SIPParseException {
        ServerLog.logException(ex);
        
        if (ex instanceof SIPHeaderParseException ) {
            if(ServerLog.needsLogging
            (ServerLog.TRACE_EXCEPTION)) {
                ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,
                "Illegal Header : " + ex.getText());
            }
            // Ignore the bad header. (if this is a required
            // header we will get a missingHeader below
            ex.rejectErrorObject();
            badHeaders.add(ex.getText());
        }  else if (ex instanceof SDPParseException ) {
            ex.rejectErrorObject();
            badSDPFields.add(ex.getText());
        } else if (ex instanceof
        SIPUnrecognizedExtensionException ) {
            if(ServerLog.needsLogging
            (ServerLog.TRACE_EXCEPTION)) {
                ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,
                "Unsupported extension " + ex.getText());
            }
            SIPUnrecognizedExtensionException ure =
            (SIPUnrecognizedExtensionException) ex;
            String hdrName = ure.getExtensionName();
            ExtensionParser parser =
            stack.getExtensionParser(hdrName);
            if (parser == null) {
                ExtensionHeader extHdr =
                new ExtensionHeader(ure.getExtensionName());
                extHdr.setInputText(ex.getText());
                ExtensionHeaderList hlist =
                new ExtensionHeaderList
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
            
        } else if ( ex instanceof
        SIPUnexpectedHeaderException ) {
            // Ignore unexpected headers.
            if(ServerLog.needsLogging
            (ServerLog.TRACE_EXCEPTION)) {
                ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,
                "Unexpected Header " + ex.getText());
            }
            ex.rejectErrorObject();
            badHeaders.add(ex.getText());
        } else {
            System.out.println
            ("Exception header "+ ex.getText());
            throw ex;
        }
        
        
    }
    
    
    
    
        /** Gets invoked by the parser as a callback on successful message
         * parsing (i.e. no parser errors).
         * @param sipMessage Message to process (this calls the application
         * for processing the message).
         */
    public void processMessage( SIPMessage sipMessage) {
        ServerLog.logMessage(sipMessage,
        mySock.getInetAddress().getHostAddress().
        toString() + SIPKeywords.COLON +
        String.valueOf(new Integer(mySock.getPort())),
        getHost() + SIPKeywords.COLON +
        String.valueOf(new Integer(getPort())),
        false);
        
        ViaList viaList = sipMessage.getViaHeaders();
        // For a request
        // first via header tells where the message is coming from.
        // For response, just get the port from the packet.
        this.senderPort = mySock.getPort();
        if (sipMessage instanceof SIPResponse) {
            this.receiverAddress = this.senderAddress;
            this.receiverPort = mySock.getPort();
        } else  {
            Via v = (Via)viaList.first();
            if (v.hasPort() ) {
                this.receiverPort = v.getPort();
            }  else this.receiverPort = SIPStack.DEFAULT_PORT;
            try {
                this.receiverAddress  = v.getSentBy().getInetAddress();
            } catch (java.net.UnknownHostException ex) {
                // Could not resolve the sender address.
                ServerLog.traceMsg(ServerLog.TRACE_EXCEPTION,
                "Rejecting message -- could not resolve Via Address");
                return;
            }
        }
        
        // Foreach part of the request header, fetch it and process it
        if ( sipMessage instanceof SIPRequest) {
            // This is a request - process the request.
            SIPRequest sipRequest = (SIPRequest)sipMessage;
            // Create a new sever side request processor for this
            // message and let it handle the rest.
            
            ServerDebug.println("----Processing Message---");
            
            SIPServerRequestInterface sipServerRequest =
            stack.newSIPServerRequest(sipRequest,this);
            try {
                sipServerRequest.processRequest();
            } catch (SIPServerException ex) {
                handleException(ex);
            }
        } else {
            // This is a response message - process it.
            SIPResponse sipResponse = (SIPResponse)sipMessage;
            SIPServerResponseInterface sipServerResponse =
            stack.newSIPServerResponse(sipResponse, this);
            try {
                sipServerResponse.processResponse();
            } catch (SIPServerException ex) {
                // Ignore errors while processing responses??
            }
        }
    }
    
        /**
         * This gets invoked when thread.start is called from the constructor.
         * Implements a message loop - reading the tcp connection and processing
         * messages until we are done or the other end has closed.
         */
    public void run() {
        String message;
        PipedOutputStream mypipe = null;
        PipedInputStream hispipe = null;
        try {
            // Create a pipeline to connect to our message parser.
            hispipe = new PipedInputStream();
            mypipe = new PipedOutputStream(hispipe);
        } catch ( IOException ex) {
            ServerInternalError.handleException(ex);
        }
        // Create a pipelined message parser to read and parse
        // messages that we write out to him.
        myParser = new PipelinedMsgParser( this ,hispipe);
        if (stack.parseSDP) myParser.parseContent();
        if (stack.disableInputTracking)
            myParser.disableInputTracking();
        // Enable the flag to parse message content.
        // Start running the parser thread.
        myParser.processInput();
        // myParser.enableDebugFlag();
        byte[] msg = new byte[8192];
        while (true) {
            try {
                int nbytes  = myClientInputStream.
                read(msg,0,8192);
                // no more bytes to read...
                if (nbytes == -1) {
                    mypipe.write("\r\n\r\n".getBytes("UTF-8"));
                    mypipe.flush();
                    try {
                        mypipe.close();
                        mySock.close();
                    } catch (IOException ioex) {}
                    return;
                }
                if (ServerLog.needsLogging
                (ServerLog.TRACE_MESSAGES))  {
                    ServerLog.traceMsg
                    (ServerLog.TRACE_MESSAGES,
                    new String(msg,0,nbytes));
                }
                mypipe.write(msg,0,nbytes);
                mypipe.flush();
            } catch ( IOException ex) {
                // Terminate the message.
                try {
                    mypipe.write("\r\n\r\n".getBytes("UTF-8"));
                    mypipe.flush();
                } catch (Exception e ) {
                    ServerInternalError.handleException(e);
                }
                
                try {
                    ServerLog.traceMsg
                    (ServerLog.TRACE_DEBUG,
                    "IOException  closing sock");
                    try {
                        mySock.close();
                        mypipe.close();
                    } catch (IOException ioex) {}
                    if (notifier != null)
                        notifier.notifyClose(this);
                } catch (Exception ex1) {
                    // Do nothing.
                }
                return;
            } catch ( Exception ex) {
                ServerInternalError.
                handleException(ex);
            }
        }
        
    }
    
        /**
         * Equals predicate.
         * @param other is the other object to compare ourselves to for equals
         */
    
    public boolean equals(Object other) {
        
        if (!this.getClass().equals(other.getClass())) return false;
        else {
            TCPMessageChannel that = (TCPMessageChannel)other;
            if (this.mySock != that.mySock) return false;
            else return true;
        }
    }
    
    
        /**
         * Get an identifying key.
         */
    public String getKey() {
        return getKey(senderAddress,mySock.getPort(),"TCP");
    }

	/** Get the host from the topmost via header.
	*/
    public String getViaHost() {
	return receiverAddress.getHostAddress();
    }

    	/** Get the port from the topmost via header.
         */
    public int getViaPort() {
	return receiverPort;
    }
    
}
