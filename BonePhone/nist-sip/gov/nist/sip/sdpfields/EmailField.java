/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;
/**
* email field in the SDP announce parser.
*/
public class EmailField extends SDPField {
    protected EmailAddress emailAddress;
    
    public EmailField() {
	super(SDPFieldNames.EMAIL_FIELD);
    }
    
    public	 EmailAddress getEmailAddress() 
    { return emailAddress ; } 
    /**
     * Set the emailAddress member  
     */
    public	 void setEmailAddress(EmailAddress e) 
    { emailAddress = e ; } 
    
    /**
     *  Get the string encoded version of this object
     * @since v1.0
     */
    public String encode() {
	return EMAIL_FIELD + emailAddress.encode() + Separators.NEWLINE;
    }

}
