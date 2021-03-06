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
* Organization SIPObject. 
*/
public class Organization extends SIPHeader {
    
        /** organization field
         */    
	protected String organization;

        /**
         * Return canonical form.
         * @return String
         */       
	public String encode() {
		return headerName + COLON + SP + organization + NEWLINE;
	}

        /** Default constructor
         */        
	public Organization() { 
		super(ORGANIZATION); 
	}
        
        /** get the organization field.
         * @return String
         */        
	public String getOrganization() {
		return organization;
	}
        
	/**
         * Set the organization member
         * @param o String to set
         */
	public void setOrganization(String o) {
            organization = o ;
        }
        
}
