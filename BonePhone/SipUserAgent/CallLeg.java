/*
*
* $Id: CallLeg.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
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

import gov.nist.sip.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.msgparser.*;

/**
* class CallLeg
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
public class CallLeg extends SipListenerImpl implements CallContext
{
	protected static long cseqNo = 0;
	
	protected final FromHeader fromHdr;
	protected final ToHeader toHdr;
	protected final SipProvider defaultSipPrv;
	protected final ListeningPoint defaultLstPnt;
	protected final CallIdHeader callIdHdr;
	protected final ViaHeader defaultViaHdr;
	protected final ContentTypeHeader contentTypeHdr;
	protected final HashSet listeners = new HashSet();
	protected final CallListener listener;
	
	private long curTID = 0;
	private long curCSeqNo = 0;
	private URI curRequestURI = null;
	protected State currentState = CL_initial.getState();
	private SubjectHeader subjectHdr = null;
	private int curStatus = Response.OK;
	private String curReason = "OK";
	private SDPAnnounce localSDPAnnounce = null;
	private SDPAnnounce remoteSDPAnnounce = null;
	
	protected boolean isServerTransaction = false;
	protected URI replyURI = null;
	
	public CallLeg(FromHeader fromHdr, ToHeader toHdr, CallListener listener) throws SipException
	{
		this.fromHdr = fromHdr;
		this.toHdr = toHdr;
		this.defaultSipPrv = (SipProvider)sipStack.getSipProviders().next();
		this.defaultLstPnt = defaultSipPrv.getListeningPoint();
		this.callIdHdr = defaultSipPrv.getNewCallIdHeader();
		this.defaultViaHdr = hdrFactory.createViaHeader(defaultLstPnt.getHost(),defaultLstPnt.getPort(),defaultLstPnt.getTransport());
		this.curRequestURI = toHdr.getNameAddress().getAddress();
		this.contentTypeHdr = hdrFactory.createContentTypeHeader("application","sdp");
		this.listener = listener;
		this.isServerTransaction = false;
		setSubject("(empty)");
		this.replyURI = toHdr.getNameAddress().getAddress();
		System.out.println("******** REPLY URI: " + replyURI.toString() + " ************");
	}
	
	public CallLeg(Request request, CallListener listener) throws SipException
	{
		this.fromHdr = request.getFromHeader();
		this.toHdr = request.getToHeader();
		this.callIdHdr = request.getCallIdHeader();
		this.curRequestURI = request.getRequestURI();
		this.defaultSipPrv = (SipProvider)sipStack.getSipProviders().next();
		this.defaultLstPnt = defaultSipPrv.getListeningPoint();
		this.defaultViaHdr = hdrFactory.createViaHeader(defaultLstPnt.getHost(),defaultLstPnt.getPort(),defaultLstPnt.getTransport());
		this.contentTypeHdr = hdrFactory.createContentTypeHeader("application","sdp");
		this.curCSeqNo = request.getCSeqHeader().getSequenceNumber();
		this.listener = listener;
		setSubject("(empty)");
		this.isServerTransaction = true;
		if ( request.hasContactHeaders() )
		{
			ContactHeader contactHdr = (ContactHeader)request.getContactHeaders().next();
			this.replyURI = contactHdr.getNameAddress().getAddress();
		}
		else
		{
			this.replyURI = fromHdr.getNameAddress().getAddress();
		}
		
		System.out.println("******** REPLY URI: " + replyURI.toString() + " ************");
	}
	
	public String getCallLegId()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(callIdHdr.getCallId());
		/*
		sb.append(fromHdr.getNameAddress().getAddress().getSchemeData());
		if ( fromHdr.hasTag() )
		{
			sb.append(fromHdr.getTag());
		}
		sb.append(toHdr.getNameAddress().getAddress().getSchemeData());
		if ( toHdr.hasTag() )
		{
			sb.append(toHdr.getTag());
		}
		*/
		return sb.toString();
	}
	
	public static String toCallLegId(Message msg)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(msg.getCallIdHeader().getCallId());
		/*
		sb.append(msg.getFromHeader().getNameAddress().getAddress().getSchemeData());
		if ( msg.getFromHeader().hasTag() )
		{
			sb.append(msg.getFromHeader().getTag());
		}
		sb.append(msg.getToHeader().getNameAddress().getAddress().getSchemeData());
		if ( msg.getToHeader().hasTag() )
		{
			sb.append(msg.getToHeader().getTag());
		}
		*/
		return sb.toString();
	}
	
	public synchronized void addListener(CallListener listener)
	{
		listeners.add(listener);
	}
	
	public synchronized void removeListener(CallListener listener)
	{
		listeners.remove(listener);
	}
	
	/**
	* Return CallId string
	**/
	public String getCallId()
	{
		return callIdHdr.getCallId();
	}
		
	/**
	* Return From address string
	**/
	public String getFromAddress()
	{
		return fromHdr.getNameAddress().getAddress().toString();
	}
	
	/**
	* Return To address string
	**/
	public String getToAddress()
	{
		return toHdr.getNameAddress().getAddress().toString();
	}
	
	/**
	* Return subject
	**/
	public String getSubject()
	{
		synchronized(this)
		{
			return ( subjectHdr == null ) ? "(empty)" : subjectHdr.getSubject();
		}
	}
	
	/**
	* Set subject
	**/
	public void setSubject(String subject)
	{
		synchronized(this)
		{
			try
			{
				subjectHdr = hdrFactory.createSubjectHeader(subject);
			}
			catch (SipParseException e)
			{
			}
		}
	}
	
	/**
	* Return status of the last transaction
	**/
	public int getStatusCode()
	{
		synchronized(this)
		{
			return curStatus;
		}
	}
	
	/**
	* Return reason phrase of the last transaction
	**/
	public String getReasonPhrase()
	{
		synchronized(this)
		{
			return curReason;
		}
	}
	
	protected void setStatus(int code, String reason)
	{
		synchronized(this)
		{
			curStatus = code;
			curReason = reason;
		}
	}
	
	/**
	* Set local SDP announce
	**/	
	public void setLocalSDP(SDPAnnounce sdp)
	{
		synchronized(this)
		{
			localSDPAnnounce = sdp;
		}
	}
		
	/**
	* Return local SDP announce
	**/
	public SDPAnnounce getLocalSDP()
	{
		synchronized(this)
		{
			return localSDPAnnounce;
		}
	}
	
	/**
	* Set remote SDP announce
	**/	
	public void setRemoteSDP(SDPAnnounce sdp)
	{
		synchronized(this)
		{
			remoteSDPAnnounce = sdp;
		}
	}
		
	/**
	* Return remote SDP announce
	**/
	public SDPAnnounce getRemoteSDP()
	{
		synchronized(this)
		{
			return remoteSDPAnnounce;
		}
	}

	public void invite()
	{
		currentState = currentState.invite(this);
	}
	
	public void hangup()
	{
		currentState = currentState.hangup(this);
	}
	
	public void accept()
	{
		currentState = currentState.accept(this);
	}
	
	public void decline()
	{
		currentState = currentState.decline(this);
	}
	
	public void processResponse(SipEvent event)
	{
		currentState = currentState.processResponse(this,event);
	}
	
	public void processInvite(SipEvent event)
	{
		currentState = currentState.processInvite(this,event);
	}
	
	public void processBye(SipEvent event)
	{
		currentState = currentState.processBye(this,event);
	}
	
	public void processCancel(SipEvent event)
	{
		currentState = currentState.processCancel(this,event);
	}
	
	public void processAck(SipEvent event)
	{
		currentState = currentState.processAck(this,event);
	}
	
	protected abstract static class State extends JainSipEnv
	{
		protected abstract State invite(CallLeg cl);
		protected abstract State hangup(CallLeg cl);
		protected abstract State accept(CallLeg cl);
		protected abstract State decline(CallLeg cl);
		protected abstract State processResponse(CallLeg cl, SipEvent event);
		protected abstract State processInvite(CallLeg cl, SipEvent event);
		protected abstract State processBye(CallLeg cl, SipEvent event);
		protected abstract State processCancel(CallLeg cl, SipEvent event);
		protected abstract State processAck(CallLeg cl, SipEvent event);
	}
	
	protected synchronized Iterator getListeners()
	{
		return listeners.iterator();
	}
	
	protected URI getCurrentRequestURI()
	{
		return curRequestURI;
	}
	
	public long nextCSeqNo()
	{
		cseqNo += 10;
		return (Math.max(curCSeqNo,cseqNo) + 1);
	}
	
	protected void setCurrentTID(long tid)
	{
		curTID = tid;
		transactionListeners.put(Long.toString(tid),this);
	}
	
	protected void addTID(long tid)
	{
		transactionListeners.put(Long.toString(tid),this);
	}
	
	protected long getCurrentTID()
	{
		return curTID;
	}
	
	protected ContactHeader getServerContactHeader(String userName, String displayName)
	{
		ContactHeader contactHdr = null;
		
		try
		{
			SipURL contactUrl = adrFactory.createSipURL(userName,defaultLstPnt.getHost());
			contactUrl.setPort(defaultLstPnt.getPort());
			contactUrl.setTransport(defaultLstPnt.getTransport());
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
	
	protected void setToTag(String tag)
	{
		try
		{
			if ( ! toHdr.hasTag() )
			{
				toHdr.setTag(tag);
			}
		}
		catch (SipParseException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	protected void setToTag(Response response)
	{
		try
		{
			if ( response.getToHeader().hasTag() )
			{
				toHdr.setTag(response.getToHeader().getTag());
			}
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	protected boolean isServerURL(SipURL url)
	{
		if ( url.getHost().toLowerCase().indexOf("[") >= 0 )
		{
			return true;
		}
		
		String s = sipStack.getStackName();
		return ( s.indexOf(url.getHost().toLowerCase()) >= 0 );
	}
	
	protected Response formatResponse(Response response)
	{
		try
		{
			response.getToHeader().setTag(LOCAL_TAG);
			response.setDateHeader(hdrFactory.createDateHeader(new Date()));
			String userName = ((SipURL)response.getToHeader().getNameAddress().getAddress()).getUserName();
			String displayName = response.getToHeader().getNameAddress().getDisplayName();
			displayName = ( displayName == null ) ? "Unknown" : displayName;
			List contactHdrs = new ArrayList();
			contactHdrs.add(getServerContactHeader(userName,displayName));
			response.setContactHeaders(contactHdrs);
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return response;
	}

	protected void detachMessageBody(Request msg)
	{
		if ( msg.hasBody() )
		{
			SDPAnnounce sdpa = null;
			try
			{
				StringMsgParser parser = new StringMsgParser();
				sdpa = parser.parseSDPAnnounce(msg.getBodyAsString());
			}
			catch (SIPParseException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
			
			if ( sdpa != null )
			{
				setRemoteSDP(sdpa);
			}
		}
	}

	protected void detachMessageBody(Response msg)
	{
		if ( msg.hasBody() )
		{
			SDPAnnounce sdpa = null;
			try
			{
				StringMsgParser parser = new StringMsgParser();
				sdpa = parser.parseSDPAnnounce(msg.getBodyAsString());
			}
			catch (SIPParseException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
			
			if ( sdpa != null )
			{
				setRemoteSDP(sdpa);
			}
		}
	}
	
	protected Request attachMessageBody(Request msg)
	{
		SDPAnnounce sdpa = getLocalSDP();
		if ( sdpa != null )
		{
			try
			{
				msg.setBody(sdpa.encode(),contentTypeHdr);
			}
			catch (SipParseException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
		return msg;
	}

	protected Response attachMessageBody(Response msg)
	{
		SDPAnnounce sdpa = getLocalSDP();
		if ( sdpa != null )
		{
			try
			{
				msg.setBody(sdpa.encode(),contentTypeHdr);
			}
			catch (SipParseException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
		return msg;
	}
	
	protected void notifyListener()
	{
		listener.notify(this);
	}
	
	protected void errorListener()
	{
		listener.error(this);
	}
	
	protected void hangupListener()
	{
		listener.hangup(this);
	}
	
	protected boolean loopDetected(Request request)
	{
		HeaderIterator it = request.getViaHeaders();
		while ( it.hasNext() )
		{
			try
			{
				ViaHeader viaHdr = (ViaHeader)it.next();
				if ( sipStack.getStackName().indexOf(viaHdr.getHost()) >= 0 )
				{
					return true;
				}
			}
			catch (HeaderParseException e)
			{
			}
		}
		return false;
	}
}
