/*
*
* $Id: CallContext.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;

import gov.nist.sip.msgparser.SDPAnnounce;

/**
* interface CallContext
*
* @author	$Author: Psycho $
* @version	$Revision: 1.1 $
**/

public interface CallContext
{
	/**
	* Return CallId string
	**/
	public String getCallId();
		
	/**
	* Return From address string
	**/
	public String getFromAddress();
	
	/**
	* Return To address string
	**/
	public String getToAddress();
	
	/**
	* Return subject
	**/
	public String getSubject();
	
	/**
	* Set subject
	**/
	public void setSubject(String subject);
	
	/**
	* Return status of the last transaction
	**/
	public int getStatusCode();
	
	/**
	* Return reason phrase of the last transaction
	**/
	public String getReasonPhrase();
	
	/**
	* Set local SDP announce
	**/	
	public void setLocalSDP(SDPAnnounce sdp);
		
	/**
	* Return local SDP announce
	**/
	public SDPAnnounce getLocalSDP();
	
	/**
	* Set remote SDP announce
	**/	
	public void setRemoteSDP(SDPAnnounce sdp);
		
	/**
	* Return remote SDP announce
	**/
	public SDPAnnounce getRemoteSDP();
	
}
