/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/**
* Duplicate header exception:  thrown when there is more
* than one header of a type where there should only be one.
* The exception handler may choose to : 
* 1. discard the duplicate  by returning null
* 2. keep the duplicate by just returning it.
* 3. Discard the entire message by throwing an exception.
* @version 1.0
* @author M. Ranganathan mailto:mranga@nist.gov
*/

package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;

public class SIPDuplicateHeaderException
extends SIPIllegalMessageException {
	protected SIPMessage sipMessage;
	public SIPDuplicateHeaderException( String msg) {
		super(msg);
	}
	public SIPMessage getSIPMessage() {
		return sipMessage;
	}

}
