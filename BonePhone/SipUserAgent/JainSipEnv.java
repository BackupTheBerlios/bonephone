/*
*
* $Id: JainSipEnv.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;


import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.address.*;
import jain.protocol.ip.sip.header.*;
import jain.protocol.ip.sip.message.*;

/**
* class JainSipEnv
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
public abstract class JainSipEnv
{
	public static String JAIN_SIP_PATH = "gov.nist";
	
	protected static final SipFactory sipFactory;
	protected static AddressFactory adrFactory = null;
	protected static HeaderFactory hdrFactory = null;
	protected static MessageFactory msgFactory = null;
	protected static SipStack sipStack = null;
	static
	{
		sipFactory = SipFactory.getInstance();
		sipFactory.setPathName(JAIN_SIP_PATH);
		
		try
		{
			adrFactory = sipFactory.createAddressFactory();
			hdrFactory = sipFactory.createHeaderFactory();
			msgFactory = sipFactory.createMessageFactory();
			sipStack = sipFactory.createSipStack();
		}
		catch (SipException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}
	
}

