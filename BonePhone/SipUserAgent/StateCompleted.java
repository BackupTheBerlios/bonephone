/*
*
* $Id: StateCompleted.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.message.*;

/**
* class StateCompleted
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class StateCompleted extends Transaction.State
{
	private static StateCompleted _instance = null;
	private StateCompleted()
	{
	}
	
	protected static StateCompleted getInstance()
	{
		if ( _instance == null )
		{
			_instance = new StateCompleted();
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
				}
				ctx.setState(StateCompleted.getInstance());
			}
		}
		catch (SipParseException ignored)
		{
		}
	}
}

