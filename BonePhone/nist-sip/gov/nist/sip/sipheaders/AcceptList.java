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
* Accept List of  SIP headers. 
* There can be multiple accept headers in a message so these are strung together
* in a single AcceptList header list.
*@see Accept
*/
public class AcceptList extends SIPHeaderList {

        /** default constructor
         */    
        public AcceptList() {
		super("AcceptList", SIPHEADERS_PACKAGE+".Accept",ACCEPT);
	}

        /** add the specified parameter
         * @param ac Accept to set
         */        
	public void add(Accept ac) {
            super.add(ac);
        }
        
}
