/*
*
* $Id: CL_modify.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;


import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.message.*;

/**
* class CL_modify
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class CL_modify extends CallLeg.State
{
	private static final CL_modify singleton = new CL_modify();
	
	private CL_modify()
	{
	}
	
	static CallLeg.State getState()
	{
		System.out.println("CallLeg.State = CL_modify");
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
		try
		{
			cl.addTID(cl.defaultSipPrv.sendBye(cl.getCurrentTID(),false));
			return CL_bye.getState();
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
					// event. tag merken
					cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
					return CL_modify_proceed.getState();
				}
				else if ( response.getStatusCode() < Response.MULTIPLE_CHOICES )
				{
					provider.sendAck(tid);
					cl.detachMessageBody(response);
					cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
					// notify listeners
					cl.notifyListener();
					return CL_established.getState();
				}
				else
				{
					provider.sendAck(tid);
					cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
					// notify listeners
					cl.errorListener();
					return CL_established.getState();
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

