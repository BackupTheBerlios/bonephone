/*
*
* $Id: StateProceeding.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.message.*;

/**
* class StateProceeding
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class StateProceeding extends Transaction.State
{
	private static StateProceeding _instance = null;
	private StateProceeding()
	{
	}
	
	protected static StateProceeding getInstance()
	{
		if ( _instance == null )
		{
			_instance = new StateProceeding();
		}
		return _instance;
	}

	protected void processInvite(Transaction ctx, SipEvent event)
	{
		ctx.sendStatus(ctx.transactionId,ctx.sipProvider);
	}

	protected void processBye(Transaction ctx, SipEvent event)
	{
		ctx.sendStatus(ctx.transactionId,ctx.sipProvider);
	}
	
	protected void processCancel(Transaction ctx, SipEvent event)
	{
		Request request = (Request)event.getMessage();
		ctx.sendStatus(ctx.transactionId,ctx.sipProvider,Response.OK,request);
		ctx.sendStatus(ctx.transactionId,ctx.sipProvider,487);
		ctx.hangupListener(event.isServerTransaction());
		ctx.setState(StateFailure.getInstance());
	}
	
	protected void processTimeOut(Transaction ctx, SipEvent event)
	{
		ctx.sendStatus(ctx.transactionId,ctx.sipProvider,Response.REQUEST_TIMEOUT);
		ctx.hangupListener(event.isServerTransaction());
		ctx.setState(StateFailure.getInstance());
	}
	
	protected void sendResponse(Transaction ctx, SipEvent event)
	{
		Response response = (Response)event.getMessage();
		ctx.sendStatus(ctx.transactionId,ctx.sipProvider,response);
		ctx.setState(StateSuccess.getInstance());
	}
}

