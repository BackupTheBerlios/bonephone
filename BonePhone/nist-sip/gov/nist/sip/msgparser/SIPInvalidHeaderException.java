
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import antlr.ANTLRException;

/**
* This exception is given to the exception handler callback when 
* we are unable to identify the header at all (no colon delimiter
* so there is no way for it to be a valid header).
*
*@since v1.0
*/


public class SIPInvalidHeaderException extends SIPParseException {

	protected SIPInvalidHeaderException ( String msg) {
	 	super(msg);
	}
	protected SIPInvalidHeaderException (ANTLRException ex) {
		super(ex);
	}
	protected void setHeaderText( String text) {
		super.setText(text);
	}

	/**
	* Set the header object.
	*/
	public void setHeader( SIPHeader header) {
		super.setErrorObject(header);
	}

	/**
	* get the header object 
	*/
	protected SIPHeader getHeader() {
		return (SIPHeader) super.getErrorObject();
	}
}
