/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import antlr.ANTLRException;
import gov.nist.sip.sipheaders.*;

/**
* This exception is thrown when the message is illegally 
* formatted (example -- two CSEQ fields, bad URL in the Request line).
*@author <A href=mailto:mranga@nist.gov > M. Ranganathan </A>
*@version 1.0
*/

public class SIPIllegalMessageException extends SIPParseException {
	protected SIPIllegalMessageException(ANTLRException ex) {
		super(ex);
	}
	protected SIPIllegalMessageException( String emsg) {
		super(emsg);
	}

	/**
	* Set the header that causes trouble.
	*@param sh is the SIPHeader that caused the bad parse.
	*/
	protected void setHeader ( SIPHeader sh) 
		{ super.setErrorObject(sh); }


	/**
	* Return the header that caused the exception to be thrown.
	*@return SIPHeader that caused the trouble.
	*/
	public  SIPHeader getHeader() 
	{ return (SIPHeader) super.getErrorObject(); }

	/**
	* Reject the header that caused the trouble (wil be set to null)
	* in the corresponding structure.
	*/
	public void rejectHeader() { super.rejectErrorObject(); } 

	/**
	* Get the header text.
	*@return the string that failed to parse.
	*/

	public void getHeaderText() { super.getText(); }

	/**
	* Set the header text.
	*@param text is the text that failed to parse.
	*/
	protected void setHeaderText(String text ) { super.setText(text); }

	/**
	* Set the name of the object that caused the exception.
	*@param hdrName is the name of the header that failed to parse.
	*/
	protected void setHeaderName( String hdrName) {
		super.setErrorObjectName(hdrName);
	}

	/**
	* Get the name of the header that caused the exception.
	*@return the name of the header that failed to parse.
	*/

	public String getHeaderName() {
		return super.getErrorObjectName();
	}
	
}
