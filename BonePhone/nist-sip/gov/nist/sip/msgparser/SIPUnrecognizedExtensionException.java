/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import  antlr.RecognitionException;
import gov.nist.sip.sipheaders.*;

/**
* This exception is thrown when a SIP Header is unreognizied. 
* Extension headers that are not directly supported by the parser 
* are handled this way (see examples/msgparser/ for an example of how to 
* do this). 
*/
public class SIPUnrecognizedExtensionException extends SIPParseException {
	protected SIPUnrecognizedExtensionException (String msghdr) {
		super("Unrecognized Header");
		setErrorObjectName(msghdr);
	}
	protected SIPUnrecognizedExtensionException
			(RecognitionException ex){
		super(ex);
	}

	/**
	* Exception handler can parse the header and set the header value
	* using this method. The header is stored in the SIPMessage structure.
	*@param <var> h </var> is the sipheader that is passed back to 
	* the parser and that appears in the SIPMessage structure. 
	*/
	public void setHeader(SIPHeader h) {
		super.setErrorObject(h);
	}

	/**
        * Get the header that the parser generated so far.
	*/
	protected SIPHeader getHeader() 
	{ return (SIPHeader) super.getErrorObject(); }

	protected void setExtensionName(String hdrName) {
		super.setErrorObjectName(hdrName);
	}

	/**
	* Get the extension name. (for example, 
	* NewFangledExtension: foo bar com
	* will return "NewFangledExtension" when you retrieve this from 
	* the exception object).
	*/
	public String getExtensionName() { return super.getErrorObjectName(); }


	/**
        * Set the text of the extension header.
	* @param <var> text </var> is the text that we want to set.
	*/
	protected void setHeaderText( String text) {
		super.setText(text);
	}
}
