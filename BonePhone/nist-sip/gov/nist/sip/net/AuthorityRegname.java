/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;
import gov.nist.sip.*;

/**
* Authority part of a URI structure when specified as a 
* registry name (Section 3.2.1 RFC 2396).
*/
public class AuthorityRegname  extends Authority {

    /** regName field
     */    
    protected String regName;

       /**
        * Encode this as a string.
        * @return String
        */
    public String encode() { 
            return regName;
        }
         
    /** get the RegName field
     * @return String
     */        
    public String getRegName() { 
        return regName ;
    }
    
	/**
         * Set the regName member
         * @param r String to set
         */
    public void setRegName(String r) { 
        regName = r ;
    }
    
}
