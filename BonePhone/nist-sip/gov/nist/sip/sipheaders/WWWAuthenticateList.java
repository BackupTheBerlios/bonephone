/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modifications by: O. Deruelle (deruelle@antd.nist.gov added JAVADOC)         *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* WWWAuthenticate SIPHeader (of which there can be several?)
*/
public class WWWAuthenticateList extends SIPHeaderList 
implements AuthorizationKeywords {
     
        /**
         * constructor.
         */
    public WWWAuthenticateList () {
        super(  "WWWAuthenticate", 
        SIPHEADERS_PACKAGE+".WWWAuthenticate",
        WWW_AUTHENTICATE);
    }
        
}

