/*
*
* $Id: CL_modify_proceed.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;


import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.message.*;

/**
* class CL_modify_proceed
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class CL_modify_proceed extends CallLeg.State
{
	private static final CL_modify_proceed singleton = new CL_modify_proceed();
	
	private CL_modify_proceed()
	{
	}
	
	static CallLeg.State getState()
	{
		System.out.println("CallLeg.State = CL_modify_proceed");
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
			cl.addTID(cl.defaultSipPrv.sendCancel(cl.getCurrentTID()));
			return CL_cancel.getState();
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
				
				cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
				if ( response.getToHeader().hasTag() )
				{
					cl.setToTag(response.getToHeader().getTag());
				}
			
				if ( response.getStatusCode() < Response.OK )
				{
					return this.getState();
				}
				else if ( response.getStatusCode() < Response.MULTIPLE_CHOICES )
				{
					provider.sendAck(tid);
					cl.setStatus(response.getStatusCode(),response.getReasonPhrase());
					cl.detachMessageBody(response);
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

