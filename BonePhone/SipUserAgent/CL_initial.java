/*
*
* $Id: CL_initial.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
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
* class CL_initial
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class CL_initial extends CallLeg.State
{
	private static final CL_initial singleton = new CL_initial();
	
	private CL_initial()
	{
	}
	
	static CallLeg.State getState()
	{
		System.out.println("CallLeg.State = CL_initial");
		return singleton;
	}
	
	protected CallLeg.State invite(CallLeg cl)
	{
		try
		{
			cl.setCurrentTID(cl.defaultSipPrv.sendRequest(createInviteRequest(cl)));
			return CL_calling.getState();
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return this.getState();
	}
	
	protected CallLeg.State hangup(CallLeg cl)
	{
		return this.getState();
	}
	
	protected CallLeg.State processResponse(CallLeg cl, SipEvent event)
	{
		return this.getState();
	}
	
	protected Request createInviteRequest(CallLeg cl)
	{
		Request request = null;
		
		try
		{
			CSeqHeader cseqHdr = hdrFactory.createCSeqHeader(cl.nextCSeqNo(),Request.INVITE);
			List viaHdrs = new LinkedList();
			viaHdrs.add(cl.defaultViaHdr);
			List contactHdrs = new LinkedList();
			String userName = ((SipURL)cl.fromHdr.getNameAddress().getAddress()).getUserName();
			String displayName = cl.fromHdr.getNameAddress().getDisplayName();
			contactHdrs.add(cl.getServerContactHeader(userName,displayName));
			request = msgFactory.createRequest(cl.getCurrentRequestURI(),Request.INVITE,
											   cl.callIdHdr,cseqHdr,
											   cl.fromHdr,cl.toHdr,
											   viaHdrs);
			request.setContactHeaders(contactHdrs);
			if ( cl.getSubject() != null )
			{
				request.setSubjectHeader(hdrFactory.createSubjectHeader(cl.getSubject()));
			}
			cl.attachMessageBody(request);   
		}
		catch (SipParseException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return request;
	}

	protected CallLeg.State processInvite(CallLeg cl, SipEvent event)
	{
		long tid = event.getTransactionId();
		SipProvider provider = (SipProvider)event.getSource();
		Request request = (Request)event.getMessage();
		cl.setCurrentTID(tid);
		
		try
		{
			// loop check
			if ( cl.loopDetected(request) )
			{
				Response response = msgFactory.createResponse(Response.LOOP_DETECTED,request);
				provider.sendResponse(tid,cl.formatResponse(response));
				return CL_failed.getState();
			}
			
			if ( cl.isServerURL((SipURL)request.getRequestURI()) )
			{
				// user lookup !!
				Response response = null;
				if ( CallManager.isRegistered(((SipURL)request.getRequestURI()).getUserName()) )
				{
					cl.detachMessageBody(request);
					if ( request.hasSubjectHeader() )
					{
						cl.setSubject(request.getSubjectHeader().getSubject());
					}
					cl.notifyListener();
					response = msgFactory.createResponse(Response.RINGING,request);
					provider.sendResponse(tid,cl.formatResponse(response));
					return CL_proceed.getState();
				}
				else
				{
					response = msgFactory.createResponse(Response.NOT_FOUND,request);
					provider.sendResponse(tid,cl.formatResponse(response));
					return CL_failed.getState();
				}
			}
			
			Response response = msgFactory.createResponse(Response.NOT_FOUND,request);
			provider.sendResponse(tid,cl.formatResponse(response));
			return CL_terminated.getState();
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
			
		return this.getState();
	}
	
	protected CallLeg.State processBye(CallLeg cl, SipEvent event)
	{
		return this.getState();
	}
	
	protected CallLeg.State processCancel(CallLeg cl, SipEvent event)
	{
		return this.getState();
	}
	
	protected CallLeg.State processAck(CallLeg cl, SipEvent event)
	{
		return this.getState();
	}
	
	protected CallLeg.State accept(CallLeg cl)
	{
		return this.getState();
	}
	
	protected CallLeg.State decline(CallLeg cl)
	{
		return this.getState();
	}
}

