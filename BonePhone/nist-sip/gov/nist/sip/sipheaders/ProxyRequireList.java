/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) ,added JAVADOC                 *                                                                                     
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* Proxy Require SIPSIPObject (list of option tags)
*/
public class ProxyRequireList extends SIPHeaderList {
    
        /** Default Constructor
         */    
	public ProxyRequireList () {
		super( "ProxyRequire", 
			SIPHEADERS_PACKAGE+".ProxyRequire", 
			PROXY_REQUIRE);
	}
        
        /** Constructor
         * @param sip SIPObjectList to set
         */    
	public ProxyRequireList (SIPObjectList sip) {
		super(sip, PROXY_REQUIRE);
	}
        
}
