/*
*
* $Id: CallProvider.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;


/**
* interface CallProvider
*
* @author	$Author: Psycho $
* @version	$Revision: 1.1 $
**/

public interface CallProvider
{
	public void invite(CallContext cc);
	
	public void accept(CallContext cc);
	
	public void decline(CallContext cc);
	
	public void hangup(CallContext cc);
}
