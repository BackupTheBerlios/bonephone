/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import  gov.nist.sip.net.*;
import  gov.nist.sip.*;
import  gov.nist.sip.sipheaders.*;
import  antlr.ANTLRException;
import  antlr.RecognitionException;
/**
* Parse exception thrown to the outside world.
*/
public class SIPParseException extends SIPException 
{ 
	protected ANTLRException antlrException;
	protected String headerText;
	protected String errorObjectName;
	protected GenericObject errorObject;
	
	public  SIPParseException (String msg ) {
		super(CLIENT_ERROR_BAD_REQUEST,msg);
	}

	protected  SIPParseException(ANTLRException ae ) {
		super(CLIENT_ERROR_BAD_REQUEST,ae.getMessage());
		antlrException = ae;
	}

	public ANTLRException getANTLRException() { return antlrException; }

	public void printStackTrace() {
		super.printStackTrace();
		if (antlrException != null) {
		        System.err.println("--------------------------------");
			antlrException.printStackTrace();
		}
	}

	/**
	* Set the text for the line that generated this error.
	* This is never exported out of this package.
	*/
	void  setText( String hdr) { headerText = hdr; }

	protected void 	 setErrorObjectName( String hname) 
		{ errorObjectName = hname; }
	/**
	* Set parse structure for the object on which the error waas detected
	*/
	protected void 	 setErrorObject ( GenericObject error_obj ) 
		{ errorObject = error_obj; }

	
	/**
	* Get  parse structure for the object on which the error was detected.
	*/
	protected GenericObject getErrorObject () { return errorObject; }

	/**
	* Get the parsed text for the header that generated the error.
	*/
	public    String getText() { return headerText; }

	/**
	* Get the name of the header that generated the error.
	*/
	public    
	String getErrorObjectName() { return errorObjectName; }

	/**
	* Reject parsed structure (and set it to null in the message object).
	* This is how the error handler rejects lines and allows the parse to
	* continue.
	*/

	public    void  rejectErrorObject() { errorObject = null; }
	
}
