/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.*;
import gov.nist.sip.sipheaders.*;

/**
* Unexpected header in a message (Request or response).
* This exception is trapped when there is an unexpected header (for example
* Subject: in a response). The exception callback has the chance of 
* silently accepting the header, silently rejecting the header or kicking 
* up a fuss by throwing an exception.
*/

public class SIPUnexpectedHeaderException extends SIPIllegalMessageException {

	protected SIPUnexpectedHeaderException( String hdrText) {
		super(hdrText);
	}
	
}
