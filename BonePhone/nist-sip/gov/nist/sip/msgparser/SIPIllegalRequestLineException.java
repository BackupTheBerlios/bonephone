/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/**
* Illegal request line exception for the SIP Message.
*/

package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import antlr.ANTLRException;

public class SIPIllegalRequestLineException 
extends SIPIllegalMessageException {
	public SIPIllegalRequestLineException( String msg) {
		super(msg);
	}
	public SIPIllegalRequestLineException(ANTLRException ex) {
		super(ex);
	}

}
