/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle (deruelle@nist.gov), added JAVADOC            *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* List of Require headers.
* <pre>
* Require  =  "Require" ":" 1#option-tag 
* </pre>
*/
public final class RequireList extends SIPHeaderList {

        /** Default constructor
         */    
	public RequireList () {
		   super("Require", 
			 SIPHEADERS_PACKAGE+".Require",
			 REQUIRE);
	}

         /** Constructor
         * @param sip SIPObjectList to set
         */    
	public RequireList (SIPObjectList sip) {
		super(sip, REQUIRE);
	}
        
        /** add the Header Require
         * @param op Require to set
         */        
	public void add(Require op ) {
		hlist.add(op);
	}

}
