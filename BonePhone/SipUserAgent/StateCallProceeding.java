/*
*
* $Id: StateCallProceeding.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.message.*;

/**
* class StateCallProceeding
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class StateCallProceeding extends Transaction.State
{
	private static StateCallProceeding _instance = null;
	private StateCallProceeding()
	{
	}
	
	protected static StateCallProceeding getInstance()
	{
		if ( _instance == null )
		{
			_instance = new StateCallProceeding();
		}
		return _instance;
	}
	
	protected void processResponse(Transaction ctx, SipEvent event)
	{
		long tid = event.getTransactionId();
		SipProvider provider = (SipProvider)event.getSource();
		Response response = (Response)event.getMessage();
		
		try
		{
			if ( response.getStatusCode() >= Response.OK )
			{
				System.out.println("Status: " + response.getStatusCode() + " " + response.getReasonPhrase());
				String method = response.getCSeqHeader().getMethod();
				if ( Request.INVITE.equals(method) )
				{
					ctx.sendAck(tid,provider,response);
					if ( response.getStatusCode() >= 300 )
					{	
						ctx.hangupListener(event.isServerTransaction());
					}
					else
					{
						ctx.notifyListener(event.isServerTransaction());
					}
				}
				ctx.setState(StateCompleted.getInstance());
			}
		}
		catch (SipParseException ignored)
		{
		}
	}
	
	protected void sendRequest(Transaction ctx, SipEvent event)
	{
		long tid = event.getTransactionId();
		SipProvider provider = (SipProvider)event.getSource();
		Request request = (Request)event.getMessage();
		ctx.sendCancel(tid,provider);
		ctx.sendRequest(tid,provider,request);
		ctx.setState(StateCalling.getInstance());
	}
}

