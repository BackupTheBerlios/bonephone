/*
*
* $Id: Main.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

import SipUserAgent.*;
import java.util.*;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.address.*;
import jain.protocol.ip.sip.header.*;
import jain.protocol.ip.sip.message.*;

/**
* class Main
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
public class Main implements CallListener
{
	public Main() throws Exception
	{
		CallManager cm = new CallManager("iptel.org",this);
		// CallContext cc = cm.createCallContext("foo","sfo");
		// cm.invite(cc);
		
		System.in.read();
		System.out.println("BYE BYE BYE");
	}
	
	public void notify(CallContext cc)
	{
		System.out.println("NOTIFY()");
	}
	
	public void error(CallContext cc)
	{
		System.out.println("ERROR()");
	}
	
	public void hangup(CallContext cc)
	{
		System.out.println("HANGUP()");
	}
	
	public static void main(String args[])
	{
		try
		{
			Main m = new Main();
			
			System.in.read();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}
}

