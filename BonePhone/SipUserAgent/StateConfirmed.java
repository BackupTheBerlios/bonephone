/*
*
* $Id: StateConfirmed.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;

import jain.protocol.ip.sip.*;

/**
* class StateConfirmed
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class StateConfirmed extends Transaction.State
{
	private static StateConfirmed _instance = null;
	private StateConfirmed()
	{
	}
	
	protected static StateConfirmed getInstance()
	{
		if ( _instance == null )
		{
			_instance = new StateConfirmed();
		}
		return _instance;
	}
	
	protected void processTimeOut(Transaction ctx, SipEvent event)
	{
		ctx.runningTransactions.remove(Long.toString(event.getTransactionId()));
		ctx.setState(StateCompleted.getInstance());
	}
}

