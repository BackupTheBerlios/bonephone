/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: SIPException.java
 * created 30-Sep-00 12:32:58 PM by mranga
 */


package gov.nist.sip.stack;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;


/** Exception that gets generated when the Stack encounters an error.
 */
public class SIPServerException extends SIPException
{
	
	/**
         * Constructor when we are given only the error code
         * @param rc Return code.
         */
	public SIPServerException ( int rc) {
		 super(rc);
		 ServerLog.logException(this);
	}
	/**
         * Constructor for when we have the error code and some error info.
         * @param rc SIP Return code 
         * @param msg Error message
         */
	public SIPServerException ( int rc, String msg) {
		super ( rc,  msg);
		ServerLog.logException(this);
	}
	/**
         * Constructor for when we have a return code and a SIPMessage.
         * @param rc SIP error code
         * @param message SIP Error message
         * @param msg Auxiliary error message
         */
	public SIPServerException ( int rc, SIPMessage message, String msg ) {
		super(rc,message,msg);
		ServerLog.logException(this);

	}
	
	/**
         * Constructor when we have a pre-formatted response.
         * @param response Pre-formatted response to send back to the
         * other end.
         */
	public SIPServerException( String response) {
		super (response);
		ServerLog.logException(this);
	}
	
	
}
