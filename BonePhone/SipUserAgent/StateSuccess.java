/*
*
* $Id: StateSuccess.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;

import jain.protocol.ip.sip.*;

/**
* class StateSuccess
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class StateSuccess extends Transaction.State
{
	private static StateSuccess _instance = null;
	private StateSuccess()
	{
	}
	
	protected static StateSuccess getInstance()
	{
		if ( _instance == null )
		{
			_instance = new StateSuccess();
		}
		return _instance;
	}
	
	protected void processTimeOut(Transaction ctx, SipEvent event)
	{
		// ctx.runningTransactions.remove(Long.toString(event.getTransactionId()));
		ctx.setState(StateCompleted.getInstance());
	}
	
	protected void processAck(Transaction ctx, SipEvent event)
	{
		ctx.setState(StateConfirmed.getInstance());
	}
}

