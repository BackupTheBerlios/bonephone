/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import antlr.ANTLRException;

public class SDPParseException extends SIPParseException {

	public SDPParseException ( String msg) {
                super(msg);
        }
        public SDPParseException (ANTLRException ex) {
		super(ex);
        }
	

}         

