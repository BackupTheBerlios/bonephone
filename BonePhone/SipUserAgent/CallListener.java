/*
*
* $Id: CallListener.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;


/**
* interface CallListener
*
* @author	$Author: Psycho $
* @version	$Revision: 1.1 $
**/

public interface CallListener
{
	public void notify(CallContext cc);
	
	public void error(CallContext cc);
	
	public void hangup(CallContext cc);
}
