/*
*
* $Id: CL_calling.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;


import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.message.*;

/**
* class CL_calling
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class CL_calling extends CallLeg.State
{
	private static final CL_calling singleton = new CL_calling();
	
	private CL_calling()
	{
	}
	
	static CallLeg.State getState()
	{
		System.out.println("CallLeg.State = CL_calling");
		return singleton;
	}

	protected CallLeg.State invite(CallLeg cl)
	{
		try
		{
			Request request = cl.defaultSipPrv.getTransactionRequest(cl.getCurrentTID(),false);
			cl.setCurrentTID(cl.defaultSipPrv.sendRequest(request));
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return this.getState();
	}
	
	protected CallLeg.State hangup(CallLeg cl)
	{
		return CL_terminated.getState();
	}
	
	protected CallLeg.State processResponse(CallLeg cl, SipEvent event)
	{
		long tid = event.getTransactionId();
		SipProvider provider = (SipProvider)event.getSource();
		Response response = (Response)event.getMessage();
		String method = response.getCSeqHeader().getMethod();
		
		try
		{
			if ( response.getToHeader().hasTag() )
			{
				cl.toHdr.setTag(response.getToHeader().getTag());
			}
			
			if ( Request.INVITE.equals(method) )
			{
				// Reason -> Subject
				cl.setSubject(response.getReasonPhrase());
				
				if ( response.getStatusCode() < Response.OK )
				{
					cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
					// notify listeners ?
					return CL_call_proceed.getState();
				}
				else if ( response.getStatusCode() < Response.MULTIPLE_CHOICES )
				{
					provider.sendAck(tid);
					cl.detachMessageBody(response);
					cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
					cl.notifyListener();
					return CL_established.getState();
				}
				else
				{
					provider.sendAck(tid);
					cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
					cl.hangupListener();
					return CL_terminated.getState();
				}
			}
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
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
		return this.getState();
	}
	
	protected CallLeg.State processAck(CallLeg cl, SipEvent event)
	{
		return this.getState();
	}

	protected CallLeg.State accept(CallLeg cl)
	{
		return this.getState();
	}
	
	protected CallLeg.State decline(CallLeg cl)
	{
		return this.getState();
	}
}

