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
*  Proxy-Require SIP Header
*<pre>
* Require  =  "Require" ":" 1#option-tag    
*</pre>
*/
public class Require extends SIPHeader {
    
        /** optionTag field
         */    
	protected String optionTag;
	
         /** Default constructor
         * @param s String to set
         */        
	public Require() {
		super(REQUIRE);
	}
        
        /** constructor
         * @param s String to set
         */        
	public Require( String s) {
		super(REQUIRE);
		optionTag = s;
	}
	
        /**
         * Encode in canonical form.
         * @return String
         */
	public String encode() {
		return headerName + COLON + SP + optionTag + NEWLINE;
	}
        
        /** get the optiontag field
         * @return String
         */        
	public String getOptionTag() {
            return optionTag ;
        }
        
	/**
         * Set the option member
         * @param o String
         */
	public void setOptionTag(String o) {
            optionTag = o ;
        } 
	
}
