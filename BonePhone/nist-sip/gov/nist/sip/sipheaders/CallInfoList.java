/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* A list of CallInfo headers (there can be multiple in a message).
*/
public class CallInfoList extends SIPHeaderList {
    
        /** Default constructor
         */    
	public CallInfoList() {
		super("CallInfoList", SIPHEADERS_PACKAGE+".CallInfo",CALL_INFO);
	}

}
