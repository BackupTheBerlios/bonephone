/*
*
* $Id: CL_proceed.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;


import java.util.*;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.address.*;
import jain.protocol.ip.sip.header.*;
import jain.protocol.ip.sip.message.*;

/**
* class CL_proceed
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class CL_proceed extends CallLeg.State
{
	private static final CL_proceed singleton = new CL_proceed();
	
	private CL_proceed()
	{
	}
	
	static CallLeg.State getState()
	{
		System.out.println("CallLeg.State = CL_proceed");
		return singleton;
	}
	
	protected CallLeg.State invite(CallLeg cl)
	{
		return this.getState();
	}
	
	protected CallLeg.State hangup(CallLeg cl)
	{
		return this.getState();
	}
	
	protected CallLeg.State processResponse(CallLeg cl, SipEvent event)
	{
		return this.getState();
	}

	protected CallLeg.State processInvite(CallLeg cl, SipEvent event)
	{
		return this.getState();
	}
	
	protected CallLeg.State processBye(CallLeg cl, SipEvent event)
	{
		return this.getState();
	}
	
	protected CallLeg.State processCancel(CallLeg cl, SipEvent event)
	{
		long tid = event.getTransactionId();
		SipProvider provider = (SipProvider)event.getSource();
		Request request = (Request)event.getMessage();
		
		try
		{
			Response response = msgFactory.createResponse(Response.OK,request);
			provider.sendResponse(cl.getCurrentTID(),cl.formatResponse(response));
			Request trequest = provider.getTransactionRequest(cl.getCurrentTID(),true);
			Response response2 = msgFactory.createResponse(487,trequest);
			provider.sendResponse(cl.getCurrentTID(),cl.formatResponse(response2));
			// provider.sendResponse(cl.getCurrentTID(),487);
			// notify Listener
			cl.hangupListener();
			return CL_failed.getState();
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		return this.getState();
	}
	
	protected CallLeg.State processAck(CallLeg cl, SipEvent event)
	{
		return this.getState();
	}

	protected CallLeg.State accept(CallLeg cl)
	{
		try
		{
			Request request = cl.defaultSipPrv.getTransactionRequest(cl.getCurrentTID(),true);
			Response response = msgFactory.createResponse(Response.OK,request);
			response = cl.attachMessageBody(response);
			cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
			cl.defaultSipPrv.sendResponse(cl.getCurrentTID(),cl.formatResponse(response));
			return CL_success.getState();
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		return this.getState();
	}
	
	protected CallLeg.State decline(CallLeg cl)
	{
		try
		{
			Request request = cl.defaultSipPrv.getTransactionRequest(cl.getCurrentTID(),true);
			Response response = msgFactory.createResponse(Response.DECLINE,request);
			cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
			cl.defaultSipPrv.sendResponse(cl.getCurrentTID(),cl.formatResponse(response));
			return CL_failed.getState();
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return this.getState();
	}
}

