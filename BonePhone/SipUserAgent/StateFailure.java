/*
*
* $Id: StateFailure.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;

import jain.protocol.ip.sip.*;

/**
* class StateFailure
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class StateFailure extends Transaction.State
{
	private static StateFailure _instance = null;
	private StateFailure()
	{
	}
	
	protected static StateFailure getInstance()
	{
		if ( _instance == null )
		{
			_instance = new StateFailure();
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

