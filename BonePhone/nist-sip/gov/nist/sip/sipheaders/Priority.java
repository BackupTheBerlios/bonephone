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
*  Priority SIPSIPObject 
*/
public class Priority extends SIPHeader implements PriorityKeywords {
    
        /** priority field
         */    
	protected String priority;

        /** Default constructor
         */        
        public Priority() { 
            super(PRIORITY);
        }

        /**
         * Encode into canonical form.
         * @return String
         */
	public String encode() {
		return headerName + COLON + SP + priority + NEWLINE;
	}
        
	/**
         * get the priority value.
         * @return String
         */
	public String getPriority() {
		return priority;
	}

	/**
         * Set the priority member
         * @param p String to set
         */
	public void setPriority(String p) { 
            priority = p ;
        } 

}
