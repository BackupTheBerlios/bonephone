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
*  Subject SIPHeader
*/

public class Subject extends SIPHeader {

        /** subject field
         */    
	protected String subject;
        
        /** Default Constructor.
         */        
	public Subject() {
		super(SUBJECT);
	}
        
        /**
         * Generate the canonical form.
         * @return String.
         */
	public String encode() {
		if (subject != null)  {
		  return headerName + COLON + SP + subject + NEWLINE;
		} else {
		  return headerName + COLON + NEWLINE;
		}
		
	}       
        
        /** get the subject of the header
         * @return Suject field
         */        
	public String getSubject() {
            return subject;
        }
        
	/**
         * Set the subject member
         * @param s String to set
         */
	public void setSubject(String s) { 
            subject = s ;
        }
       
}
