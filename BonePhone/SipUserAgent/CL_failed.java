/*
*
* $Id: CL_failed.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
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
* class CL_failed
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
final class CL_failed extends CallLeg.State
{
	private static final CL_failed singleton = new CL_failed();
	
	private CL_failed()
	{
	}
	
	static CallLeg.State getState()
	{
		System.out.println("CallLeg.State = CL_failed");
		return singleton;
	}
	
	protected CallLeg.State invite(CallLeg cl)
	{
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

	protected CallLeg.State processInvite(CallLeg cl, SipEvent event)
	{
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
		return CL_terminated.getState();
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
