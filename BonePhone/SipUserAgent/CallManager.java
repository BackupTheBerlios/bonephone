/*
*
* $Id: CallManager.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
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
* class CallManager
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
public class CallManager extends SipStackListener implements CallProvider
{
	protected final Hashtable callLegs = new Hashtable();
	protected final String dialDomain;
	protected final CallListener callListener;
	 
	public CallManager(String dialDomain, CallListener listener) throws SipException
	{
		this.dialDomain = dialDomain;
		this.callListener = listener;
	}
	
	public void Login(String userName, String displayName) throws SipException
	{
		SipURL usrURL = adrFactory.createSipURL(userName,dialDomain);
		NameAddress usrAdr = adrFactory.createNameAddress(displayName,usrURL);
		FromHeader fromHdr = hdrFactory.createFromHeader(usrAdr);
		fromHdr.setTag(LOCAL_TAG);
		ToHeader toHdr = hdrFactory.createToHeader(usrAdr);
		SipProvider provider = (SipProvider)sipStack.getSipProviders().next();
		ListeningPoint lp = provider.getListeningPoint();
		CallIdHeader callIdHdr = provider.getNewCallIdHeader();
		CSeqHeader cseqHdr = hdrFactory.createCSeqHeader(1,Request.REGISTER);
		ViaHeader viaHdr = hdrFactory.createViaHeader(lp.getHost(),lp.getPort(),lp.getTransport());
		List viaHdrs = new ArrayList();
		viaHdrs.add(viaHdr);
		List contactHdrs = new ArrayList();
		contactHdrs.add(getServerContactHeader(userName,displayName,lp));
		ExpiresHeader expiresHdr = hdrFactory.createExpiresHeader(3600);
		SipURL reqURL = adrFactory.createSipURL(dialDomain);
		// HACK ON
		reqURL.setPort(5061);
		// HACK OFF
		Request request = msgFactory.createRequest(reqURL,Request.REGISTER,callIdHdr,cseqHdr,fromHdr,toHdr,viaHdrs);
		request.setContactHeaders(contactHdrs);
		request.setExpiresHeader(expiresHdr);
		Thread t = new Registration(provider,request,3600);
		registers.put(userName,t);
		t.start();
	}
	
	protected static final Hashtable registers = new Hashtable();
	
	public void Logout(String userName)
	{
		if ( registers.containsKey(userName) )
		{
			Thread t = (Thread)registers.get(userName);
			t.interrupt();
		}
	}
	
	public static boolean isRegistered(String userName)
	{
		// return registers.containsKey(userName);
		return true;
	}
	
	public Iterator getUserNames()
	{
		return registers.keySet().iterator();
	}
	
	public String getDisplayName(String userName)
	{
		if ( registers.containsKey(userName) )
		{
			Registration r = (Registration)registers.get(userName);
			return r.getDisplayName();
		}
		return null;
	}
	
	public NameAddress getNameAddress(String userName)
	{
		if ( registers.containsKey(userName) )
		{
			Registration r = (Registration)registers.get(userName);
			return r.getNameAddress();
		}
		return null;
	}
	
	public CallContext createCallContext(String from, String to) throws SipException
	{
		// parse "from" argument
		SipURL fromURL = null;
		String s = from.toLowerCase().trim();
		s = s.startsWith("sip:") ? s.substring(4) : s;
		int at = s.indexOf("@");
		if ( at < 0 )
		{
			fromURL = adrFactory.createSipURL(s,this.dialDomain);
		}
		else
		{
			fromURL = adrFactory.createSipURL(s.substring(0,at),this.dialDomain);
		}
		String dpn = getDisplayName(fromURL.getUserName());
		dpn = ( dpn == null ) ? "" : dpn;
		FromHeader fromHdr = hdrFactory.createFromHeader(adrFactory.createNameAddress(dpn,fromURL));
		fromHdr.setTag(LOCAL_TAG);
		
		// parse "to" argument
		SipURL toURL = null;
		s = to.toLowerCase().trim();
		s = s.startsWith("sip:") ? s.substring(4) : s;
		at = s.indexOf("@");
		if ( at < 0 )
		{
			toURL = adrFactory.createSipURL(s,this.dialDomain);
		}
		else
		{
			toURL = adrFactory.createSipURL(s.substring(0,at),s.substring(at + 1));
		}
		ToHeader toHdr = hdrFactory.createToHeader(adrFactory.createNameAddress("",toURL));
		
		CallLeg cl = new CallLeg(fromHdr,toHdr,callListener);
		cl.setSubject("new session");
		callLegs.put(cl.getCallLegId(),cl);
		
		return cl;
	}
	
	public void invite(CallContext cc)
	{
		((CallLeg)cc).invite();
	}
	
	public void accept(CallContext cc)
	{
		((CallLeg)cc).accept();
	}
	
	public void decline(CallContext cc)
	{
		((CallLeg)cc).decline();
	}
	
	public void hangup(CallContext cc)
	{
		((CallLeg)cc).hangup();
	}

	protected void processInvite(SipEvent event)
	{
		long tid = event.getTransactionId();
		Request request = (Request)event.getMessage();
	
		String id = CallLeg.toCallLegId(request);
		
		if ( callLegs.containsKey(id) )
		{
			CallLeg cl = (CallLeg)callLegs.get(id);
			transactionListeners.put(Long.toString(tid),cl);
			cl.processInvite(event);
		}
		else
		{
			Iterator cls = callLegs.values().iterator();
			boolean found = false;
			CallLeg cl = null;
			while ( cls.hasNext() )
			{
				cl = (CallLeg)cls.next();
				if ( id.equalsIgnoreCase(cl.getCallLegId()) )
				{
					found = true;
					transactionListeners.put(Long.toString(tid),cl);
					cl.processInvite(event);
					break;
				}
			}
	
			if ( ! found )
			{
				try
				{
					cl = new CallLeg(request,callListener);
					callLegs.put(cl.getCallLegId(),cl);
					transactionListeners.put(Long.toString(tid),cl);
					cl.processInvite(event);
				}
				catch (SipException e)
				{
					e.printStackTrace();
					System.err.println(e.getMessage());
				}
			}
		}
	}
	
	protected void processBye(SipEvent event)
	{
		long tid = event.getTransactionId();
		SipProvider provider = (SipProvider)event.getSource();
		Request request = (Request)event.getMessage();
		
		String id = CallLeg.toCallLegId(request);
		
		if ( callLegs.containsKey(id) )
		{
			CallLeg cl = (CallLeg)callLegs.get(id);
			transactionListeners.put(Long.toString(tid),cl);
			cl.processBye(event);
		}
		else
		{
			Iterator cls = callLegs.values().iterator();
			while ( cls.hasNext() )
			{
				CallLeg cl = (CallLeg)cls.next();
				if ( id.equalsIgnoreCase(cl.getCallLegId()) )
				{
					transactionListeners.put(Long.toString(tid),cl);
					cl.processBye(event);
					break;
				}
			}
		}
	}
	
	protected void processCancel(SipEvent event)
	{
		long tid = event.getTransactionId();
		Request request = (Request)event.getMessage();

		String id = CallLeg.toCallLegId(request);
		
		if ( callLegs.containsKey(id) )
		{
			CallLeg cl = (CallLeg)callLegs.get(id);
			transactionListeners.put(Long.toString(tid),cl);
			cl.processCancel(event);
		}
		else
		{
			Iterator cls = callLegs.values().iterator();
			while ( cls.hasNext() )
			{
				CallLeg c = (CallLeg)cls.next();
				if ( id.equalsIgnoreCase(c.getCallLegId()) )
				{
					transactionListeners.put(Long.toString(tid),c);
					c.processCancel(event);
					break;
				}
			}
		}
	}
	
	protected void processAck(SipEvent event)
	{
		long tid = event.getTransactionId();
		Request request = (Request)event.getMessage();

		String id = CallLeg.toCallLegId(request);
		
		if ( callLegs.containsKey(id) )
		{
			CallLeg cl = (CallLeg)callLegs.get(id);
			transactionListeners.put(Long.toString(tid),cl);
			cl.processAck(event);
		}
		else
		{
			Iterator cls = callLegs.values().iterator();
			while ( cls.hasNext() )
			{
				CallLeg cl = (CallLeg)cls.next();
				if ( id.equalsIgnoreCase(cl.getCallLegId()) )
				{
					transactionListeners.put(Long.toString(tid),cl);
					cl.processAck(event);
					break;
				}
			}
		}
	}

	protected ContactHeader getServerContactHeader(String userName, String displayName, ListeningPoint lp)
	{
		ContactHeader contactHdr = null;
		
		try
		{
			SipURL contactUrl = adrFactory.createSipURL(userName,lp.getHost());
			contactUrl.setPort(lp.getPort());
			contactUrl.setTransport(lp.getTransport());
			NameAddress contactAdr = adrFactory.createNameAddress((displayName == null) ? "Unknown" : displayName, contactUrl);
			contactHdr = hdrFactory.createContactHeader(contactAdr);
		}
		catch (SipParseException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return contactHdr;
	}
	
	protected class Registration extends Thread
	{
		protected final SipProvider provider;
		protected final Request request;
		protected final long timeout;
		
		public Registration(SipProvider provider, Request request, long timeout)
		{
			this.provider = provider;
			this.request = request;
			this.timeout = timeout;
		}
		
		public String getDisplayName()
		{
			return request.getFromHeader().getNameAddress().getDisplayName();
		}
		
		public NameAddress getNameAddress()
		{
			return request.getFromHeader().getNameAddress();
		}
		
		public void run()
		{
			try
			{
				for(;;)
				{
					request.getCSeqHeader().setSequenceNumber(request.getCSeqHeader().getSequenceNumber() + 1);
					provider.sendRequest(request);
					provider.sendRequest(request);
					provider.sendRequest(request);
					Thread.sleep(timeout * 1000);
				}
			}
			catch (SipException ignored)
			{
			}
			catch (InterruptedException ignored)
			{
			}
		}
	}
}
