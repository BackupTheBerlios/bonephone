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
* Proxy-Require  =  "Proxy-Require" ":" 1#option-tag    
*</pre>
*/
public class ProxyRequire extends SIPHeader {
    
        /** optiontag field
         */    
	protected String optionTag;
	
         /** Default  Constructor
         * @param s String to set
         */        
	public ProxyRequire() {
		super(PROXY_REQUIRE);
	}
        
        /** Constructor
         * @param s String to set
         */        
	public ProxyRequire( String s) {
		super(PROXY_REQUIRE);
		optionTag = s;
	}
	
        /**
         * Encode in canonical form.
         * @return String
         */
	public String encode() {
		return headerName + COLON + SP + optionTag + NEWLINE;
	}
        
        /** get the optionTag field
         * @return String
         */        
	public String getOptionTag() 	{
            return optionTag ;
        }
        
	/**
         * Set the option member
         * @param o String to set
         */
	public void setOptionTag(String o) { 
            optionTag = o ;
        } 
		
}
