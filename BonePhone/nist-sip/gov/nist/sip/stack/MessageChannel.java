/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov) 			       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: MessageChannel.java
 * created 05-Sep-00 7:00:40 AM by mranga
 */


package gov.nist.sip.stack;

import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.net.*;
import java.util.LinkedList;
import java.net.InetAddress;
import java.io.IOException;

/**
* Message channel abstraction for the SIP stack.
*/

public abstract class MessageChannel
{
/** Get the SIPStack object from this message channel.
 * @return SIPStack object of this message channel
 */    
	public abstract SIPStack getSIPStack();
/** Get the host of this message channel.
 * @return host of this messsage channel.
 */        
	public abstract String 	getHost();
/** Get port of this message channel.
 * @return Port of this message channel.
 */        
	public abstract int 	getPort();
/** sender port (port of the sender as a result of which this channel was
 * created);
 */    
    protected  int senderPort;

/** Get transport string of this message channel.
 * @return Transport string of this message channel.
 */        
	public abstract String 	getTransport();
/** Send the message (after it has been formatted)
 * @param sipMessage Message to send.
 */        
	public abstract void sendMessage(SIPMessage sipMessage)
		throws IOException;

/** Send the message and log it (after it has been formatted)
 * @param msgFormatter messageFormatter to retrieve message to send.
 */        
        public abstract void sendMessage (SIPMessageFormatter msgFormatter) 
	    throws IOException;

 /** Get a linked list consisting of extension headers that are not 
  * supported natively in the message parser.
  *@return LinkedList extensionHeaders.
  */
     public abstract LinkedList getExtensionHeaders();
        
/** Send the message (after it has been formatted), to a specified
 * address and a specified port
 * @param message Message to send.
 * @param receiverAddress Address of the receiver.
 * @param receiverPort Port of the receiver.
 */        
   	protected abstract void  sendMessage(byte[] message, 
					  InetAddress receiverAddress,
					  int receiverPort) throws IOException;

/** Send the message and log it (after it has been formatted), to a specified
 * address and a specified port
 * @param msgFormatter Message formatter from which to send.
 * @param receiverAddress Address of the receiver.
 * @param receiverPort Port of the receiver.
 */        
 public void sendMessage(SIPMessageFormatter msgFormatter, 
			InetAddress receiverAddress,
			int receiverPort) throws IOException {
    logMessage(msgFormatter, receiverAddress, receiverPort);
    sendMessage(msgFormatter.getMessageAsBytes(false), 
		receiverAddress, receiverPort);
}

/** Send a message given SIP message.
*@param sipMessage is the messge to send.
*@param receiverAddress is the address to which we want to send
*@param receiverPort is the port to which we want to send
*/
public void sendMessage(SIPMessage sipMessage, 
			InetAddress receiverAddress,
			int receiverPort) throws IOException {
    logMessage(sipMessage, receiverAddress, receiverPort);
    byte[] bytes = sipMessage.encodeAsBytes();
    sendMessage(bytes, receiverAddress, receiverPort);
}


/** Get the peer address of the machine that sent us this message.
 * @return  a string contianing the ip address or host name of the sender
 *  of the message.
 */        
	public abstract String 	getSenderAddress();

/** Get the name of the machine that sent us this message.
 * @return  a string contianing the ip address or host name of the sender
 *  of the message.
 */        
	public abstract String 	getSenderName();

/**
 * Get the headers that resulted in parse errors. These are the headers
 * that are not extension headers and did not parse correctly.
 * @return a LinkedList containing the bad header lines.
 */
	public abstract LinkedList getBadHeaders();

/** Get the text of the sdp fields that did not parse correctly
 *@return a linked list containing the sdp lines that did not parse
 *coeectly.
 */        
	public abstract LinkedList getBadSDPFields();

    /**
     * Convenience function to get the raw IP source address
     * of a SIP message as a String.
     */

    public String getRawIpSourceAddress() {
	String sourceAddress = getSenderAddress();
	String rawIpSourceAddress = null;
	try {
	    InetAddress sourceInetAddress = InetAddress.
		getByName(sourceAddress);
	    rawIpSourceAddress = sourceInetAddress.getHostAddress();
	} catch (Exception ex) {
	    ServerInternalError.handleException(ex);
	}
	return rawIpSourceAddress;
    }

    /** Get the sender port.
    */
    public int getSenderPort() { return senderPort; }

    /**
     *generate a key given the inet address port and transport.
     */
    
    public static String 
        getKey(InetAddress inetAddr, int port, String transport){
        return transport+":"+inetAddr+":"+port;
    }
    
    /** Generate a key which identifies the message channel.
     */
    public abstract String getKey();

    /** Get the hostport structure of this message channel.
    */
     public HostPort getHostPort() {
		HostPort retval  = new HostPort();
		retval.setHost(new Host(this.getHost()));
		retval.setPort(this.getPort());
		return retval;
    }

    /** Get the sender host and port.
    */
    public HostPort getSenderHostPort() {
		HostPort retval = new HostPort();
		retval.setHost(new Host(this.getSenderAddress()));
		retval.setPort(this.getSenderPort());
		return retval;
    }

    /** Get the via header host:port structure.
    * This is extracted from the topmost via header of the request.
    */

    public HostPort getViaHostPort() {
		HostPort retval = new HostPort();
		retval.setHost(new Host(this.getViaHost()));
		retval.setPort(this.getViaPort());
		return retval;
    }

    /** Get the response address (from the top of the via list)
    */
    public abstract String getViaHost();
	

    /** Get the response port (from the top of the via list).
    */

    public abstract int getViaPort();

   
    /** Handle an exception.
    */
    public abstract void handleException(SIPServerException ex);
    
	
    /**
     * Log the message
     * @param msgFormatter Message formatter from which to extract message to 
     *  log.
     * @param address Address of the receiver.
     * @param port Port of the receiver.
     */
    protected void logMessage(SIPMessageFormatter msgFormatter, 
	InetAddress address, int port) {
	// Default port.
	if (port == -1) port = 5060;
	if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES))
	    ServerLog.logMessage(msgFormatter.getMessage(false), 
			     getHost() + SIPKeywords.COLON + getPort(),
			     address.getHostAddress().toString() + 
			     SIPKeywords.COLON + port,
			     msgFormatter.getCSeq(), 
			     true, msgFormatter.getCallId(), 
			     msgFormatter.getFirstLine());
    }

    protected void logMessage (SIPMessage sipMessage,
	InetAddress address, int port) {
	String firstLine = sipMessage.getMessageAsEncodedStrings(0);
	CSeq cseq = sipMessage.getCSeqHeader();
	CallID callid = sipMessage.getCallIdHeader();
	String cseqBody = cseq.encodeBody();
	String callidBody = callid.encodeBody();
	// Default port.
	if (port == -1) port = 5060;
	if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES))
	    ServerLog.logMessage(sipMessage.encode(), 
			     getHost() + SIPKeywords.COLON + getPort(),
			     address.getHostAddress().toString() + 
			     SIPKeywords.COLON + port,
			     cseqBody, true, callidBody, 
			     firstLine);
    }

}

