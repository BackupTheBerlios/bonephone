/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import antlr.ANTLRException;
import gov.nist.sip.*;
import gov.nist.sip.sipheaders.*;

/**
* This exception is given to the exception handler callback when 
* an individual header is bad, that is  when we are able to identify the 
* header type but are unable to complete the parse becuase of a parsing error. 
* A partial parse may be available and can be retrieved using getHeader().
* A forgiving implementation has the possiblity of patching up the parse
* and proceeding on.
*@since v0.9
*@author <A href=mailto:mranga@nist.gov > M. Ranganathan </A>
*/

public class SIPHeaderParseException extends SIPParseException {

	protected SIPHeaderParseException( String msg) {
	 	super(msg);
	}

	protected SIPHeaderParseException( String msg, SIPHeader hdr) {
	 	super(msg);
		super.setErrorObject(hdr);
	}

	protected SIPHeaderParseException( ANTLRException ex ) {
		super(ex);
	}


	/**
	* Get the SIPHeader structure (may be partially filled) for the error.
	* @return a partially parsed sip header 
	*/
	public SIPHeader  getHeader() 
	{ return (SIPHeader) super.getErrorObject(); }

	/**
	* Reject the SIPHeader structure.
	*/
	public void rejectHeader() 
	{ super.rejectErrorObject(); }

	/**
	* Get the header name that generated the exception.
	* @return the header name for which the parse failed.
	*/
	public String getHeaderName() 
	{ return super.getErrorObjectName(); }
}

