/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

/**
* Illegal Status line exception for the SIP Message.
*/

package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import antlr.ANTLRException;
import gov.nist.sip.sipheaders.*;

public class SIPIllegalStatusLineException 
extends SIPIllegalMessageException {
	protected StatusLine statusLine;
	protected SIPIllegalStatusLineException( String msg) {
		super(msg);
	}


	protected SIPIllegalStatusLineException(ANTLRException ex) {
		super(ex);
	}

	public StatusLine getStatusLine() 
		{ return (StatusLine) super.getErrorObject(); }


}
