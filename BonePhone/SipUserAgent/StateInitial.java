/*
*
* $Id: StateInitial.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.message.*;

/**
* class StateInitial
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class StateInitial extends Transaction.State
{
	private static StateInitial _instance = null;
	private StateInitial()
	{
	}
	
	protected static StateInitial getInstance()
	{
		if ( _instance == null )
		{
			_instance = new StateInitial();
		}
		return _instance;
	}
	
	protected void processInvite(Transaction ctx, SipEvent event)
	{
		long tid = event.getTransactionId();
		SipProvider provider = (SipProvider)event.getSource();
		Request request = (Request)event.getMessage();
		ctx.transactionId = tid;
		ctx.sipProvider = provider;
		ctx.runningTransactions.put(Long.toString(tid),ctx);
		ctx.sendStatus(tid,provider,Response.RINGING,request);
		ctx.notifyListener(event.isServerTransaction());
		ctx.setState(StateProceeding.getInstance());
	}
	
	protected void processBye(Transaction ctx, SipEvent event)
	{
		long tid = event.getTransactionId();
		SipProvider provider = (SipProvider)event.getSource();
		Request request = (Request)event.getMessage();
		ctx.transactionId = tid;
		ctx.sipProvider = provider;
		ctx.runningTransactions.put(Long.toString(tid),ctx);
		ctx.sendStatus(tid,provider,Response.OK,request);
		ctx.hangupListener(event.isServerTransaction());
		ctx.setState(StateProceeding.getInstance());
	}
	
	protected void sendRequest(Transaction ctx, SipEvent event)
	{
		long tid = event.getTransactionId();
		SipProvider provider = (SipProvider)event.getSource();
		Request request = (Request)event.getMessage();
		ctx.sendRequest(tid,provider,request);
		ctx.setState(StateCalling.getInstance());
	}
}

