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
* email address field of the SDP header
*@see EmailField
*/
public class EmailAddress extends SDPObject {
    protected String displayName;
    protected Email  email;

    public String getDisplayName() 
    { return displayName ; } 
    /**
     * Set the displayName member  
     */
    public void setDisplayName(String d) 
    { displayName = d ; } 
    /**
     * Set the email member  
     */
    public void setEmail(Email e) 
    { email = e ; } 

    /**
     *  Get the string encoded version of this object
     * @since v1.0
     * Here, we implement only the "displayName <email>" form
     * and not the "email (displayName)" form
     */
    public String encode() {
	String encoded_string;
    
	if (displayName != null) {
	    encoded_string = displayName + Separators.LESS_THAN;
	} else {
	    encoded_string = "";
	}
	encoded_string += email.encode();
	if (displayName != null) {
	    encoded_string +=  Separators.GREATER_THAN;
	}
	return encoded_string;
    }

}
