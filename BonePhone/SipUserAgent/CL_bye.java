/*
*
* $Id: CL_bye.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;


import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.message.*;

/**
* class CL_bye
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class CL_bye extends CallLeg.State
{
	private static final CL_bye singleton = new CL_bye();
	
	private CL_bye()
	{
	}
	
	static CallLeg.State getState()
	{
		System.out.println("CallLeg.State = CL_bye");
		return singleton;
	}

	protected CallLeg.State invite(CallLeg cl)
	{
		return this.getState();
	}

	protected CallLeg.State hangup(CallLeg cl)
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
	
	protected CallLeg.State processResponse(CallLeg cl, SipEvent event)
	{
		long tid = event.getTransactionId();
		SipProvider provider = (SipProvider)event.getSource();
		Response response = (Response)event.getMessage();
		String method = response.getCSeqHeader().getMethod();
		
		try
		{
			if ( Request.BYE.equals(method) )
			{
				if ( response.getStatusCode() < Response.OK )
				{
					cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
					return this.getState();
				}
				else
				{
					// notify listener
					cl.hangupListener();
					cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
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
