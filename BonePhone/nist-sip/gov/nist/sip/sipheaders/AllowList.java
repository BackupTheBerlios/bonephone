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
* List of ALLOW headers. The sip message can have multiple Allow headers
*/
public class AllowList extends SIPHeaderList {
    
        /** default constructor
         */   
	public AllowList() {
	    super("AllowList", SIPHEADERS_PACKAGE+".Allow",ALLOW);
	}

}
