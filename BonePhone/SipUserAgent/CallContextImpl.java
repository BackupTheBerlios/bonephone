/*
*
* $Id: CallContextImpl.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
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
* class CallContextImpl
*
* @author	$Author: Psycho $
* @version	$Revision: 1.1 $
**/

public class CallContextImpl extends Transaction implements CallContext
{
	/**
	* Create new CallContext
	**/
	public CallContextImpl(CallIdHeader callIdHdr, FromHeader fromHdr, ToHeader toHdr, SubjectHeader subjHdr)
	{
		this.callIdHdr = callIdHdr;
		this.fromHdr = fromHdr;
		this.toHdr = toHdr;
		this.subjHdr = subjHdr;
		callIds.put(callIdHdr.getCallId(),this);
	}
	
	public CallContextImpl(long tid, SipProvider provider, Request request)
	{
		this.transactionId = tid;
		this.sipProvider = provider;
		this.callIdHdr = request.getCallIdHeader();
		this.fromHdr = request.getFromHeader();
		this.toHdr= request.getToHeader();
		if ( request.hasSubjectHeader() )
		{
			try
			{
				this.subjHdr = request.getSubjectHeader();
			}
			catch (HeaderParseException ignored)
			{
			}
		}
		callIds.put(callIdHdr.getCallId(),this);
		setState(StateProceeding.getInstance());
	}
	
	/**
	* Return CallId string
	**/
	public String getCallId()
	{
		return this.callIdHdr.getCallId();
	}
	
	/**
	* Return CallId header
	**/
	public CallIdHeader getCallIdHeader()
	{
		return this.callIdHdr;
	}

	/**
	* Return From address string
	**/
	public String getFromAddress()
	{
		return ((SipURL)this.fromHdr.getNameAddress().getAddress()).toString();
	}
	
	/**
	* Return FromHeader
	**/
	public FromHeader getFromHeader()
	{
		return this.fromHdr;
	}

	/**
	* Return To address string
	**/
	public String getToAddress()
	{
		return ((SipURL)this.toHdr.getNameAddress().getAddress()).toString();
	}
	
	/**
	* Return ToHeader
	**/
	public ToHeader getToHeader()
	{
		return this.toHdr;
	}

	/**
	* Return subject
	**/
	public String getSubject()
	{
		return ( this.subjHdr != null ) ? this.subjHdr.getSubject() : "";
	}
	
	/**
	* Return SubjectHeader
	**/
	public SubjectHeader getSubjectHeader()
	{
		return this.subjHdr;
	}
	
	/**
	* Set subject
	**/
	public void setSubject(String subject) 
	{
		try
		{
			this.subjHdr.setSubject(subject);
		}
		catch (SipParseException ignored)
		{
		}
	}
	
	/**
	* Set local SDP announce
	**/	
	public void setLocalSDP(SDPAnnounce sdp)
	{
		this.localSDPAnnounce = sdp;
	}
	
	/**
	* Return local SDP announce
	**/
	public SDPAnnounce getLocalSDP()
	{
		return this.localSDPAnnounce;
	}

	/**
	* Set remote SDP announce
	**/	
	public void setRemoteSDP(SDPAnnounce sdp)
	{
		this.remoteSDPAnnounce = sdp;
	}
	
	/**
	* Return remote SDP announce
	**/
	public SDPAnnounce getRemoteSDP()
	{
		return this.remoteSDPAnnounce;
	}
	
	/**
	* Return status of the last transaction
	**/
	public int getStatusCode()
	{
		return this.statusCode;
	}
	
	/**
	* Set status of the last transaction
	**/
	public void setStatus(int statusCode, String reasonPhrase)
	{
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}
	
	/**
	* Return reason phrase of the last transaction
	**/
	public String getReasonPhrase()
	{
		return this.reasonPhrase;
	}
	
	public void invite()
	{
		callIds.put(callIdHdr.getCallId(),this);
		sendRequest(new SipEvent((SipProvider)sipStack.getSipProviders().next(),0,toInviteRequest()));
	}
	
	public void accept()
	{
		try
		{
			Request request = sipProvider.getTransactionRequest(transactionId,true);
			Response response = null;
			if ( localSDPAnnounce != null )
			{
				ContentTypeHeader ctHdr = hdrFactory.createContentTypeHeader("application","sdp");
				response = msgFactory.createResponse(Response.OK,request,localSDPAnnounce.encode(),ctHdr);
			}
			else
			{
				response = msgFactory.createResponse(Response.OK,request);
			}
			sendResponse(new SipEvent(sipProvider,transactionId,response));
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	public void decline()
	{
		try
		{
			Request request = sipProvider.getTransactionRequest(transactionId,true);
			Response response = msgFactory.createResponse(Response.DECLINE,request);
			sendResponse(new SipEvent(sipProvider,transactionId,response));
		}
		catch (SipException e)
		{
		}
	}
	
	public void hangup()
	{
		sendRequest(new SipEvent((SipProvider)sipStack.getSipProviders().next(),0,toByeRequest()));
	}
	
	protected void notifyListener(boolean isServerTransaction)
	{
		try
		{
			Request trequest = sipProvider.getTransactionRequest(transactionId,isServerTransaction);
			Response tresponse = sipProvider.getTransactionResponse(transactionId,isServerTransaction);
			setStatus(tresponse.getStatusCode(),tresponse.getReasonPhrase());
			
			if ( statusCode <= Response.OK )
			{
				fromHdr.setNameAddress(trequest.getFromHeader().getNameAddress());
				fromHdr.setTag(trequest.getFromHeader().getTag());
				toHdr.setNameAddress(tresponse.getToHeader().getNameAddress());
				if ( tresponse.getToHeader().hasTag() )
				{
					toHdr.setTag(tresponse.getToHeader().getTag());
				}
				if ( trequest.hasSubjectHeader() )
				{
					subjHdr.setSubject(trequest.getSubjectHeader().getSubject());
				}
				
				if ( isServerTransaction )
				{
					if ( trequest.hasBody() )
					{
						SDPAnnounce sdpa = toSDPAnnounce(trequest.getBodyAsString());
						remoteSDPAnnounce = ( sdpa == null ) ? remoteSDPAnnounce : sdpa;
					}
					if ( tresponse.hasBody() )
					{
						SDPAnnounce sdpa = toSDPAnnounce(tresponse.getBodyAsString());
						localSDPAnnounce = ( sdpa == null ) ? localSDPAnnounce : sdpa;
					}
				}
				else
				{
					if ( trequest.hasBody() )
					{
						SDPAnnounce sdpa = toSDPAnnounce(trequest.getBodyAsString());
						localSDPAnnounce = ( sdpa == null ) ? localSDPAnnounce : sdpa;
					}
					if ( tresponse.hasBody() )
					{
						SDPAnnounce sdpa = toSDPAnnounce(tresponse.getBodyAsString());
						remoteSDPAnnounce = ( sdpa == null ) ? remoteSDPAnnounce : sdpa;
					}
				}
			}
			callListener.notify(this);
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	protected void hangupListener(boolean isServerTransaction)
	{
		callListener.hangup(this);
	}
	
	protected Request toInviteRequest()
	{
		Request request = null;
		try
		{
			SipURL requestUrl = adrFactory.createSipURL(((SipURL)toHdr.getNameAddress().getAddress()).getUserName(),
														((SipURL)toHdr.getNameAddress().getAddress()).getHost());
			CSeqHeader cseqHdr = hdrFactory.createCSeqHeader(1,Request.INVITE);
			ViaHeader viaHdr = hdrFactory.createViaHeader("localhost",ListeningPoint.DEFAULT_PORT,ListeningPoint.TRANSPORT_UDP);		
			List viaHdrs = new ArrayList();
			viaHdrs.add(viaHdr);
			request = msgFactory.createRequest(requestUrl,Request.INVITE,callIdHdr,cseqHdr,fromHdr,toHdr,viaHdrs);
			request.setSubjectHeader(subjHdr);
			
			if ( localSDPAnnounce != null )
			{
				ContentTypeHeader ctHdr = hdrFactory.createContentTypeHeader("application","sdp");
				request.setBody(localSDPAnnounce.encode(),ctHdr);
			}
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return request;
	}
	
	protected Request toByeRequest()
	{
		Request request = null;
		try
		{
			SipURL requestUrl = adrFactory.createSipURL(((SipURL)toHdr.getNameAddress().getAddress()).getUserName(),
														((SipURL)toHdr.getNameAddress().getAddress()).getHost());
			CSeqHeader cseqHdr = hdrFactory.createCSeqHeader(1,Request.BYE);
			ViaHeader viaHdr = hdrFactory.createViaHeader("localhost",ListeningPoint.DEFAULT_PORT,ListeningPoint.TRANSPORT_UDP);		
			List viaHdrs = new ArrayList();
			viaHdrs.add(viaHdr);
			request = msgFactory.createRequest(requestUrl,Request.BYE,callIdHdr,cseqHdr,fromHdr,toHdr,viaHdrs);
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return request;
	}
	
	protected SDPAnnounce toSDPAnnounce(String msg)
	{
		try
		{
			StringMsgParser parser = new StringMsgParser();
			SDPAnnounce sdpa = parser.parseSDPAnnounce(msg);
			return sdpa;
		}
		catch (SIPParseException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	protected static CallListener callListener = null;
	
	public static void setCallListener(CallListener listener)
	{
		callListener = listener;
	}
	
	protected final CallIdHeader callIdHdr;
	protected final FromHeader fromHdr;
	protected final ToHeader toHdr;
	protected SubjectHeader subjHdr;
	
	protected SDPAnnounce localSDPAnnounce = null;
	protected SDPAnnounce remoteSDPAnnounce = null;	
	
	protected int statusCode = 200;
	protected String reasonPhrase = "OK";
	protected boolean established = false;
}
