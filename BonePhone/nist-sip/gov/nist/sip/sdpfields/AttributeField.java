/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;

public final class AttributeField extends SDPField {
	protected NameValue attribute;

	public NameValue getAttribute() { return attribute; }

	public AttributeField() {
		super(ATTRIBUTE_FIELD);
	}
	/**
	* Set the attribute member  
	*/
	public	 void setAttribute(NameValue a) { 
	    attribute = a ; 
	    attribute.setSeparator(Separators.COLON); 
	} 

	/**
	*  Get the string encoded version of this object
	* @since v1.0
	*/
	public String encode() {
	    String encoded_string = ATTRIBUTE_FIELD;
	    if (attribute != null) encoded_string += attribute.encode();
	    return  encoded_string + Separators.NEWLINE; 
	}

}
