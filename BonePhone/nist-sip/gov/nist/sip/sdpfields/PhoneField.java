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
* Phone Field SDP header
*/
public class PhoneField extends SDPField {
	protected String name;
	protected String phoneNumber;

	public PhoneField() {
		super(PHONE_FIELD);
	}

	public	 String getInputText() 
 	 	{ return inputText ; } 
	public	 String getName() 
 	 	{ return name ; } 
	public	 String getPhoneNumber() 
 	 	{ return phoneNumber ; } 
	/**
	* Set the name member  
	*/
	public	 void setName(String n) 
 	 	{ name = n ; } 
	/**
	* Set the phoneNumber member  
	*/
	public	 void setPhoneNumber(String p) 
 	 	{ phoneNumber = p ; } 

    /**
     *  Get the string encoded version of this object
     * @since v1.0
     * Here, we implement only the "name <phoneNumber>" form
     * and not the "phoneNumber (name)" form
     */
    public String encode() {
        String encoded_string;
	encoded_string = PHONE_FIELD;
	if (name != null) {
	    encoded_string += name + Separators.LESS_THAN;
	}
	encoded_string += phoneNumber;
	if (name != null) {
	    encoded_string +=  Separators.GREATER_THAN;
	}
	encoded_string += Separators.NEWLINE;
	return encoded_string;
    }

}
