/*
*
* $Id: SipStackListener.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;


import java.util.*;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.message.*;

/**
* class SipStackListener
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
public abstract class SipStackListener extends SipListenerImpl
{
	protected SipStackListener()
	{
		StringBuffer sb = new StringBuffer();
		Iterator lps = sipStack.getListeningPoints();
		while ( lps.hasNext() )
		{
			try
			{
				ListeningPoint lp = (ListeningPoint)lps.next();
				SipProvider sp = sipStack.createSipProvider(lp);
				sp.addSipListener(this);
				sb.append(lp.getHost()).append(" ");
			}
			catch (ListeningPointUnavailableException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
			catch (TooManyListenersException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
			catch (SipListenerAlreadyRegisteredException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
			sipStack.setStackName(sb.toString());
		}
	}
	
	protected void finalize() throws Throwable
	{
		Iterator sps = sipStack.getSipProviders();
		while ( sps.hasNext() )
		{
			try
			{
				SipProvider sp = (SipProvider)sps.next();
				sp.removeSipListener(this);
			}
			catch (SipListenerNotRegisteredException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
		
		super.finalize();
	}
	
    /**
     * Processes a Response received on one of the SipListener's ListeningPoints.
     * @param <var>responseReceivedEvent</var> SipEvent received because Response was received
     */
    public void processResponse(SipEvent responseReceivedEvent)
    {
		long tid = responseReceivedEvent.getTransactionId();
		Response response = (Response)responseReceivedEvent.getMessage();
		
		if ( ! responseReceivedEvent.isServerTransaction() )
		{
			if ( Request.REGISTER.equals(response.getCSeqHeader().getMethod()) )
			{
				return;
			}
			if ( transactionListeners.containsKey(Long.toString(tid)) )
			{
				SipListener sl = (SipListener)transactionListeners.get(Long.toString(tid));
				sl.processResponse(responseReceivedEvent);
			}
		}
    }
    
    /**
     * Processes the time out of a transaction specified by
     * the transactionId.
     * @param <var>transactionTimeOutEvent</var> SipEvent received because transaction timed out
     */
    public void processTimeOut(SipEvent transactionTimeOutEvent)
    {
    }
    
    /**
     * Processes a Request received on one of the SipListener's ListeningPoints.
     * @param <var>requestReceivedEvent</var> SipEvent received because Request was received
     */
    public void processRequest(SipEvent requestReceivedEvent)
    {
		long tid = requestReceivedEvent.getTransactionId();
		SipProvider provider = (SipProvider)requestReceivedEvent.getSource();
		Request request = (Request)requestReceivedEvent.getMessage();
		Response response = null;
		
		try
		{
			response = provider.getTransactionResponse(tid,true);
			if ( response != null )
			{
				provider.sendResponse(tid,response);
			}
		}
		catch (SipException ignored)
		{
		}
		
		try
		{
			if ( Request.CANCEL.equals(request.getMethod()) )
			{
				processCancel(requestReceivedEvent);
			}
			else if ( Request.ACK.equals(request.getMethod()) )
			{
				processAck(requestReceivedEvent);
			}
			else if ( Request.BYE.equals(request.getMethod()) )
			{
				if ( transactionListeners.containsKey(Long.toString(tid)) )
				{
					SipListener sl = (SipListener)transactionListeners.get(Long.toString(tid));
					sl.processRequest(requestReceivedEvent);
				}
				else
				{
					processBye(requestReceivedEvent);
				}
			}
			else if ( Request.INVITE.equals(request.getMethod()) )
			{
				if ( transactionListeners.containsKey(Long.toString(tid)) )
				{
					SipListener sl = (SipListener)transactionListeners.get(Long.toString(tid));
					sl.processRequest(requestReceivedEvent);
				}
				else
				{
					processInvite(requestReceivedEvent);
				}
			}
			else
			{
				response = msgFactory.createResponse(Response.NOT_IMPLEMENTED,request);
				response.getToHeader().setTag(LOCAL_TAG);
				provider.sendResponse(tid,response);
			}
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
    }
	
	protected abstract void processInvite(SipEvent event);
	
	protected abstract void processBye(SipEvent event);
	
	protected abstract void processCancel(SipEvent event);
	
	protected abstract void processAck(SipEvent event);
}

