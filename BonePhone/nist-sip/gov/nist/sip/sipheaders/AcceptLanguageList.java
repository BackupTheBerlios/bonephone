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
* AcceptLanguageList: Strings together a list of AcceptLanguage SIPHeaders.
*/
public class AcceptLanguageList extends SIPHeaderList {
    
        /** default constructor
         */    
	public AcceptLanguageList() {
		super("AcceptLanguageList", 
			SIPHEADERS_PACKAGE + ".AcceptLanguage",
			ACCEPT_LANGUAGE);
	}

        /** add the specified parameter
         * @param alb AcceptLanguage to set
         */        
	public void add(AcceptLanguage alb) {
		super.add(alb);
	}
	
}
	
