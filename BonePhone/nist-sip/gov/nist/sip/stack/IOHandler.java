/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

/******************************************************
 * File: IOHandler.java
 * created 20-Oct-00 12:45:36 PM by mranga
 */


package gov.nist.sip.stack;
import gov.nist.sip.msgparser.*;
import java.io.*; 
import java.net.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;

/**
*  Class that is used for forwarding SIP requests. 
*/

public class IOHandler
{
	/**
         * Forward a given request to the address given. 
         * The address has information on
         * the type of transport etc. used to talk to it.
         * @param addr is the address to which to send the request to
         * @param request is the request that is being forwarded
         * If the address does not specify a transport, we 
         * try UDP first and if this  fails, then try TCP.
         * @throws IOException If the message could not be sent for any
         * reason
         */
	public static void sendRequest(Address addr, 
       					String request) throws IOException 
	{
		HostPort hostPort = addr.getHostPort();
		String transport = addr.getTransport();
		if (transport == null) {
	       	transport = SIPKeywords.UDP;
			try {
				sendRequest(hostPort,transport,request); 
			} catch (IOException ex) {
				ServerLog.logException(ex);
				ServerLog.traceMsg(ServerLog.TRACE_DEBUG, 
						"UDP Send failed!" );
				sendRequest(hostPort, SIPKeywords.TCP, request);
			}
		} else { 
			sendRequest(hostPort,transport,request);
		}
				
	}
	
		
	/**
         * Forward a given request to the address given, and log it.
         * The address has information on
         * the type of transport etc. used to talk to it.
         * @param addr is the address to which to send the request.
         * @param mformatter is the message formatter containing the message.
         * @param tid A transaction Id corresponding to the message.
         * For udp we do a connect and a send as specified in tbe RFC 
         * so that an error is returned immediately if the other end is 
         * not listening
         * @throws IOException If the message could not be sent for any reason
         */
	
        public static void sendRequest(Address addr, 
				       SIPMessageFormatter mformatter,
				       String tid) 
	    throws IOException {
	    String transport = addr.getTransport();
	    if (transport == null) {
		transport = SIPKeywords.UDP;
	    }
	    if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES) )
	        logMessage(addr.getHostPort(), transport, mformatter, tid);
	    sendRequest(addr, mformatter.getMessage(tid, false));
	}

	/**
         * Forward a given request to the address given. 
         * The address has information on
         * the type of transport etc. used to talk to it.
         * @param addr is the address to which to send the request.
         * @param transport is the transport string udp or tcp.
         * @param nrequest is the request that is being forwarded	    
         * For udp we do a connect and a send as specified in tbe RFC 
         * so that an error is returned immediately if the other end is 
         * not listening
         * @throws IOException If the message could not be sent for any reason
         */
	
	public static void sendRequest(HostPort addr, 
				String transport,
				String nrequest)  throws IOException 
	{      
		String request = nrequest;

		String hostName = addr.getHost().getHostname();
		InetAddress inaddr =  InetAddress.getByName(hostName);
		int contactPort = addr.getPort(); 		
		if (contactPort == -1) {
		    contactPort = 5060;
		}
		sendRequest(inaddr,contactPort,transport,request);
	}
	/**
         * Forward a given request to the address given. 
         * The address has information on
         * the type of transport etc. used to talk to it.
         * @param inaddr is the address to which to send the request.
         * @param port is the port to send to.
         * @param transport is the transport string udp or tcp.
         * @param nrequest is the request that is being forwarded	    
         * For udp we do a connect and a send as specified in tbe RFC 
         * so that an error is returned immediately if the other end is 
         * not listening
         * @throws IOException If the message could not be sent for any reason
         */

	public static void sendRequest(InetAddress inaddr, 
			int contactPort, 
                        String transport, 
                        String request) 
        throws IOException {
		int length = request.getBytes().length;
		byte bytes[] = request.getBytes();
                // Log some debugging information.
                if (ServerLog.needsLogging(ServerLog.TRACE_DEBUG) ) {
                    ServerLog.traceMsg(ServerLog.TRACE_DEBUG,"sendRequest: " 
			+  inaddr + ":"+  contactPort + "/" + 
			  transport + "length" + length);
		    ServerLog.traceMsg(ServerLog.TRACE_DEBUG, request );
		    ServerLog.traceMsg(ServerLog.TRACE_DEBUG,
				"\n---------------------------------");
                }
		// Server uses TCP transport.
		if (transport.compareTo(SIPKeywords.TCP) == 0 ) {
			Socket clientSock = new Socket(inaddr,contactPort);
			OutputStream outputStream  = 
					clientSock.getOutputStream();
			outputStream.write(bytes,0,length);
			clientSock.close();
		} else {
			// This is a UDP transport...
			DatagramSocket datagramSock = new DatagramSocket();
			datagramSock.connect(inaddr, contactPort);
			DatagramPacket dgPacket = 
				new DatagramPacket
				  (bytes, 0, length, inaddr, contactPort);
			datagramSock.send(dgPacket);
			datagramSock.close();
		}
		
	}

	/**
         * Forward a given request to the address given, and log it. 
         * The address has information on
         * the type of transport etc. used to talk to it.
         * @param addr is the address to which to send the request.
         * @param transport is the transport string udp or tcp.
         * @param msgFormatter is the messageFormatter from which we 
	 *  retrieve the message that is being forwarded	    
	 * @param tid A transaction Id corresponding to the message
         * For udp we do a connect and a send as specified in tbe RFC 
         * so that an error is returned immediately if the other end is 
         * not listening
         * @throws IOException If the message could not be sent for any reason
         */
	
        public static void sendRequest(HostPort addr, 
				       String transport,
				       SIPMessageFormatter msgFormatter,
				       String tid) 
	    throws IOException {      
	    if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES))
	        logMessage(addr, transport, msgFormatter, tid);
	    sendRequest(addr, transport, 
			msgFormatter.getMessage(tid, true));
	}

	/** Send a request when you have a host and port string 
        *@param host is the host name/address
        *@param port is the port
        *@param stack is the sipStack from where this message is
        *   originating (for logging purposes).
        *@param message is the SIP message that we are forwardiong.
	*/

	public static void sendRequest(String host, int port, String 
				transport, SIPStack stack,
				SIPMessage message) 
        throws IOException {
	   
	   String firstLine = null;
           if (message instanceof SIPRequest) {
               SIPRequest request = (SIPRequest) message;
               firstLine = request.getRequestLine().encode();
           } else {
               SIPResponse response = (SIPResponse) message;
               firstLine = response.getStatusLine().encode();
           }
	   InetAddress inetAddr = InetAddress.getByName(host);
	   sendRequest(inetAddr,port,transport,message.encode());
	   if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES)) {
	       ServerLog.logMessage(message.encode(), 
				    stack.getHostAddress() + ":" +
				     stack.getPort(transport), 
				     host+":" +transport +port,
				     message.getCSeqHeader().encodeBody(),
				     true,
				     message.getCallIdHeader().encodeBody(),
				     firstLine);
	   }

	}

        /**
	 * Log the message
	 * @param addr Address and port of the receiver.
	 * @param transport Transport protocol
	 * @param msgFormatter Message to log.
	 * @param tid Transaction Id corresponding to the message
	 */
        public static void logMessage(HostPort addr, 
				      String transport,
				      SIPMessageFormatter msgFormatter,
				      String tid) {
	    int port;
	    if (! addr.hasPort())  port = 5060;
	    else port = addr.getPort();
	    SIPStack stack = msgFormatter.getStack();
	    if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES)) 
	        ServerLog.logMessage(msgFormatter.getMessage(tid, false), 
				 stack.getHostAddress() + SIPKeywords.COLON + 
				     stack.getPort(transport),
				 addr.getHost().getHostname() + 
				 SIPKeywords.COLON + port,
				 msgFormatter.getCSeq(),
				 true, msgFormatter.getCallId(),
				 msgFormatter.getFirstLine());
	}


}
