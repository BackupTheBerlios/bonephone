/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
* File: SIPServerResponse.java
* created 05-Jan-01 11:49:27 AM by mranga
*/


/**
* Package around a SIPResponse 
* 
*/

package examples.proxy;

import gov.nist.sip.*;
import gov.nist.sip.stack.*;
import gov.nist.sip.net.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;
import java.io.IOException;


public class SIPServerResponse 
implements SIPKeywords, SIPServerResponseInterface
{
	protected SIPResponse sipResponse;
	protected MessageChannel senderChannel;
	protected ServerMain stack;
	
	/**
	*  Constructor
	*@param resp is the SIPResponse structure that we have to proces
	*@param mchannel is the transport abstraction (UDP/TCP) that we call
	*		to dispatch messages etc.
	*/
	public SIPServerResponse (SIPResponse resp, 
		MessageChannel msgchan) {
		sipResponse = resp;
		senderChannel = msgchan;
		stack = (ServerMain) msgchan.getSIPStack();
	}

	/** This gets called back when a response comes in.
	*/
	
	public void processResponse() 
	throws SIPServerException {
		TransactionHandler thandler = stack.transactionHandler;
		// Remove it from the transaction queue.
		// (Should  schedule for removal but not actually remove
		//  in case the message gets lost )
		TransactionRecord trec = 
			thandler.getTransactionRecord(sipResponse);
		// Find the request for which this is a response.
		// If nothing exists then carry on.
		if (trec == null) {
		   if (ServerLog.needsLogging()) 
		   ServerLog.logMessage
			( "Dropping reply could not find transaction for  " +
			 sipResponse.getTransactionId());
		   return; 
		} else {
		   if (ServerLog.needsLogging()) 
		        ServerLog.logMessage( "Found transaction "  );
		}
	
	        // Get the forwarded request for this 
		ViaList viaList  = sipResponse.getViaHeaders();
		if (viaList == null) {
		   if (ServerLog.needsLogging()) 
		       ServerLog.logMessage
			("Dropping message -- could not get via header!");
		   return;
		}
		Via via = (Via) viaList.getFirst();
		String branchId = via.getBranch();
		if (branchId == null) {
		    if (ServerLog.needsLogging())
			ServerLog.logMessage("Dropping - no branch ID!");
		    return;
		}
		ForwardedRequest frequest = 
			trec.getForwardedRequest(branchId);

	        if (frequest == null) {
		    if (ServerLog.needsLogging())
			ServerLog.logMessage
				("Dropping - unrecognized branch!");
		     return;
		}
			

	        if (ServerLog.needsLogging(ServerLog.TRACE_MESSAGES)) {
		      String target = stack.getHostAddress() + ":" +
				stack.getPort(senderChannel.getTransport());
		      String sender = frequest.getSentTo().encode();
		      if ( ! frequest.getSentTo().hasPort()) 
			  sender += ":" + 5060;
		      ServerLog.logMessage(sipResponse,sender,target,false);

		}

		// NAT  and firewall Transversal
		String source = senderChannel.getRawIpSourceAddress();
		String destination = getRawIpDestinationAddress(trec);
		int statusCode = sipResponse.getStatusLine().getStatusCode();
		String method = sipResponse.getCSeqHeader().getMethod();

		// Check if the topmost entry on the via list is me and if not
		// abandon the reply.
		MessageChannel mchan = trec.messageChannel;
		SIPMessageFormatter mformatter = 
			new SIPMessageFormatter(stack,mchan);
		boolean recordRouteFlag = stack.getRecordRoute();
		mformatter.newSIPResponse(sipResponse);
		
		try {
		   mchan.sendMessage(mformatter);
		} catch (IOException ex) {
			// Ignore?
		}
				
	}

    /** Get the sender message channel.
     */
     public MessageChannel
     getRequestChannel() { return this.senderChannel; }

    /**
     * Convenience function to get the raw IP destination address
     * of a SIP message as a String. The address is extracted from the
     * transaction record.
     * @param trec a TransactionRecord for the message
     * @return Destination address
    */
    public String getRawIpDestinationAddress(TransactionRecord trec) {
	return trec.messageChannel.getRawIpSourceAddress();
    }

}
