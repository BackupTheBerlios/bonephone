/*
*
* $Id: Transaction.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;

import java.util.*;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.address.*;
import jain.protocol.ip.sip.header.*;
import jain.protocol.ip.sip.message.*;

/**
* class Transaction
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
public class Transaction implements Constants
{
	protected static final Hashtable callIds;
	protected static final Hashtable runningTransactions;
	protected static final SipFactory sipFactory;
	protected static AddressFactory adrFactory = null;
	protected static HeaderFactory hdrFactory = null;
	protected static MessageFactory msgFactory = null;
	protected static SipStack sipStack = null;
	protected static long cseqNo = 0;
	static
	{
		callIds = new Hashtable();
		runningTransactions = new Hashtable();
		sipFactory = SipFactory.getInstance();
		sipFactory.setPathName("gov.nist");
		try
		{
			adrFactory = SipFactory.getInstance().createAddressFactory();
			hdrFactory = SipFactory.getInstance().createHeaderFactory();
			msgFactory = SipFactory.getInstance().createMessageFactory();
		}
		catch (SipPeerUnavailableException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	protected long transactionId = 0;
	protected SipProvider sipProvider = null;
	
	protected void processAck(SipEvent event)
	{
		getState().processAck(this,event);
	}
	
	protected void processCancel(SipEvent event)
	{
		getState().processCancel(this,event);
	}
	
	protected void processBye(SipEvent event)
	{
		getState().processBye(this,event);
	}
	
	protected void processInvite(SipEvent event)
	{
		getState().processInvite(this,event);
	}
	
	protected void processTimeOut(SipEvent event)
	{
		getState().processTimeOut(this,event);
	}
	
	protected void processResponse(SipEvent event)
	{
		getState().processResponse(this,event);
	}
	
	protected void sendRequest(SipEvent event)
	{
		getState().sendRequest(this,event);
	}
	
	protected void sendResponse(SipEvent event)
	{
		getState().sendResponse(this,event);
	}
	
	protected abstract static class State
	{
		protected void processInvite(Transaction ctx, SipEvent event)
		{
			System.out.println("processInvite() ignored");
		}

		protected void processBye(Transaction ctx, SipEvent event)
		{
			System.out.println("processBye() ignored");
		}
		
		protected void processCancel(Transaction ctx, SipEvent event)
		{
			System.out.println("processCancel() ignored");
		}
		
		protected void processTimeOut(Transaction ctx, SipEvent event)
		{
			System.out.println("processTimeOut() ignored");
		}
		
		protected void processAck(Transaction ctx, SipEvent event)
		{
			System.out.println("processAck() ignored");
		}
		
		protected void processResponse(Transaction ctx, SipEvent event)
		{
			System.out.println("processResponse() ignored");
		}
		
		protected void sendRequest(Transaction ctx, SipEvent event)
		{
			System.out.println("sendRequest() ignored");
		}
		
		protected void sendResponse(Transaction ctx, SipEvent event)
		{
			System.out.println("sendRequest() ignored");
		}
	}
	
	private State currentState = StateInitial.getInstance();
	
	protected synchronized void setState(State state)
	{
		currentState = state;
	}
	
	protected synchronized State getState()
	{
		return currentState;
	}
	
	protected void sendCancel(long tid, SipProvider provider)
	{
		try
		{
			provider.sendCancel(tid);
		}
		catch(SipException e)
		{
		}
	}
	
	protected void sendRequest(long tid, SipProvider provider, Request request)
	{
		try
		{
			request.getCSeqHeader().setSequenceNumber(++cseqNo);
			ViaHeader viaHdr = hdrFactory.createViaHeader(provider.getListeningPoint().getHost(),
														  provider.getListeningPoint().getPort(),
														  provider.getListeningPoint().getTransport());
			List viaHdrs = new ArrayList();
			viaHdrs.add(viaHdr);
			request.setViaHeaders(viaHdrs);
														  	  
			long rtid = provider.sendRequest(formatRequest(request));
			if ( transactionId == 0 )
			{
				transactionId = rtid;
				sipProvider = provider;
				runningTransactions.put(Long.toString(rtid),this);
			}
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	protected void sendAck(long tid, SipProvider provider, Response response)
	{
		try
		{
			Request request = provider.getTransactionRequest(tid,false);
			URI reqUri = request.getRequestURI();
			CallIdHeader callIdHdr = request.getCallIdHeader();
			CSeqHeader cseqHdr = hdrFactory.createCSeqHeader(request.getCSeqHeader().getSequenceNumber(),Request.ACK);
			FromHeader fromHdr = request.getFromHeader();
			ToHeader toHdr = response.getToHeader();
			ViaHeader viaHdr = hdrFactory.createViaHeader(provider.getListeningPoint().getHost(),
														  provider.getListeningPoint().getPort(),
														  provider.getListeningPoint().getTransport());
			List viaHdrs = new ArrayList();
			viaHdrs.add(viaHdr);
			
			Request ack = msgFactory.createRequest(reqUri,Request.ACK,callIdHdr,cseqHdr,fromHdr,toHdr,viaHdrs);
			provider.sendRequest(formatRequest(ack));
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	protected void sendStatus(long tid, SipProvider provider)
	{
		try
		{
			provider.sendResponse(tid,provider.getTransactionResponse(tid,true));
		}
		catch (TransactionDoesNotExistException ignored)
		{
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	protected void sendStatus(long tid, SipProvider provider, int code, Request request)
	{
		try
		{
			Response response = msgFactory.createResponse(code,request);
			provider.sendResponse(tid,formatResponse(response));
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	protected void sendStatus(long tid, SipProvider provider, int code)
	{
		try
		{
			sendStatus(tid,provider,code,provider.getTransactionRequest(tid,true));
		}
		catch (TransactionDoesNotExistException e)
		{
		}
	}
	
	protected void sendStatus(long tid, SipProvider provider, Response response)
	{
		try
		{
			provider.sendResponse(tid,formatResponse(response));
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	protected Response formatResponse(Response response)
	{
		try
		{
			response.getToHeader().setTag(LOCAL_TAG);
			DateHeader dateHdr = hdrFactory.createDateHeader(new Date());
			response.setDateHeader(dateHdr);
			NameAddress toAdr = response.getToHeader().getNameAddress();
			SipURL toUrl = (SipURL)toAdr.getAddress();
			Iterator lps = sipStack.getListeningPoints();
			List contactHdrs = new LinkedList();
			while ( lps.hasNext() )
			{
				ListeningPoint lp = (ListeningPoint)lps.next();
				SipURL contactUrl = adrFactory.createSipURL(toUrl.getUserName(),lp.getHost());
				contactUrl.setPort(lp.getPort());
				contactUrl.setTransport(lp.getTransport());
				String displayName = toAdr.getDisplayName();
				NameAddress contactAdr = adrFactory.createNameAddress(( displayName != null) ? displayName : "",toUrl);
				ContactHeader contactHdr = hdrFactory.createContactHeader(contactAdr);
				contactHdrs.add(contactHdr);
			}
			response.setContactHeaders(contactHdrs);
		}
		catch (SipParseException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return response;
	}
	
	protected Request formatRequest(Request request)
	{
		try
		{
			request.getFromHeader().setTag(LOCAL_TAG);
			DateHeader dateHdr = hdrFactory.createDateHeader(new Date());
			request.setDateHeader(dateHdr);
			NameAddress fromAdr = request.getFromHeader().getNameAddress();
			SipURL fromUrl = (SipURL)fromAdr.getAddress();
			Iterator lps = sipStack.getListeningPoints();
			List contactHdrs = new LinkedList();
			while ( lps.hasNext() )
			{
				ListeningPoint lp = (ListeningPoint)lps.next();
				SipURL contactUrl = adrFactory.createSipURL(fromUrl.getUserName(),lp.getHost());
				contactUrl.setPort(lp.getPort());
				contactUrl.setTransport(lp.getTransport());
				String displayName = fromAdr.getDisplayName();
				NameAddress contactAdr = adrFactory.createNameAddress(( displayName != null) ? displayName : "",fromUrl);
				ContactHeader contactHdr = hdrFactory.createContactHeader(contactAdr);
				contactHdrs.add(contactHdr);
			}
			request.setContactHeaders(contactHdrs);
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return request;
	}

	protected String toAckId(Request request)
	{
		StringBuffer sb = new StringBuffer(request.getCallIdHeader().getCallId());
		sb.append(request.getFromHeader().getNameAddress().getAddress().toString());
		if ( request.getFromHeader().hasTag() )
		{
			sb.append(request.getFromHeader().getTag());
		}
		sb.append(request.getToHeader().getNameAddress().getAddress().toString());
		if ( request.getToHeader().hasTag() )
		{
			sb.append(request.getToHeader().getTag());
		}
		sb.append(request.getCSeqHeader().getSequenceNumber());
		return sb.toString();
	}
	
	protected String getAckId()
	{
		String ackId = null;
		try
		{
			ackId = toAckId(sipProvider.getTransactionRequest(transactionId,true));
		}
		catch (TransactionDoesNotExistException ignored)
		{
		}
		return ackId;
	}
	
	protected String getCancelId()
	{
		String cancelId = null;
		try
		{
			cancelId = toCancelId(sipProvider.getTransactionRequest(transactionId,true));
		}
		catch (TransactionDoesNotExistException ignored)
		{
		}
		return cancelId;
	}
	
	protected String toCancelId(Request request)
	{
		StringBuffer sb = new StringBuffer(request.getCallIdHeader().getCallId());
		sb.append(request.getFromHeader().getNameAddress().getAddress().toString());
		if ( request.getFromHeader().hasTag() )
		{
			sb.append(request.getFromHeader().getTag());
		}
		sb.append(request.getToHeader().getNameAddress().getAddress().toString());
		sb.append(request.getCSeqHeader().getSequenceNumber());
		try
		{
			sb.append(request.getRequestURI().toString());
			sb.append(request.getViaHeaders().next().getValue());
		}
		catch (SipParseException ignored)
		{
		}
		return sb.toString();
	}
	
	protected void notifyListener(boolean isServerTransaction)
	{
		try
		{
			Request request = sipProvider.getTransactionRequest(transactionId,isServerTransaction);
			Object obj = callIds.get(request.getCallIdHeader().getCallId());
			if ( obj == null )
			{
				(new CallContextImpl(transactionId,sipProvider,request)).notifyListener(isServerTransaction);
			}
			else
			{
				((CallContextImpl)obj).notifyListener(isServerTransaction);
			}
		}
		catch (TransactionDoesNotExistException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	protected void hangupListener(boolean isServerTransaction)
	{
		try
		{
			Request request = sipProvider.getTransactionRequest(transactionId,isServerTransaction);
			Object obj = callIds.get(request.getCallIdHeader().getCallId());
			if ( obj != null )
			{
				((CallContextImpl)obj).hangupListener(isServerTransaction);
			}
		}
		catch (TransactionDoesNotExistException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
}

