/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import antlr.ANTLRException;

/**
* Exception gets thrown when the message is missing a 
* required header (such as TO, FROM, VIA ...)
*@since v0.9
*@version 1.0
*Revision Log:
* Version 1.0:
*  Added getText method (overrides the superclass). (mranga@nist.gov)
*  Changed constructor.				    (mranga@nist.gov)
*/

public class SIPMissingHeaderException extends SIPParseException {
	protected SIPMissingHeaderException( String hdrName) {
		super("Missing Header " + hdrName);
		setHeaderName(hdrName);
	}

	protected void setHeaderName( String hdr) {
		super.setErrorObjectName(hdr);
	}

	public String getHeaderName() {
		return super.getErrorObjectName();
	}
	public String getText() {
		return super.getErrorObjectName();
	}
}
