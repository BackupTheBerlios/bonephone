/******************************************************
 * File: SIPAuthenticationException.java
 * created 30-Nov-00 6:54:23 PM by mranga
 */


package gov.nist.sip.stack.security;
import gov.nist.sip.msgparser.SIPErrorCodes;
import gov.nist.sip.msgparser.SIPException;
import gov.nist.sip.stack.SIPServerException;


public class SIPAuthenticationException extends 
	SIPServerException implements SIPErrorCodes
{

	public SIPAuthenticationException( String msg) {
		super(CLIENT_ERROR_UNAUTHORIZED, msg); 
	}
}
