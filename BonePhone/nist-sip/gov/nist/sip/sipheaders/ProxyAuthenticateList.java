/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                   
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* List of ProxyAuthenticate headers.
*/
public class ProxyAuthenticateList extends SIPHeaderList {
    
        /** Default constructor
         */    
	public ProxyAuthenticateList() {
		super("ProxyAuthenticateList",
			SIPHEADERS_PACKAGE+".ProxyAuthenticate",
				PROXY_AUTHENTICATE);
	}
        
}
