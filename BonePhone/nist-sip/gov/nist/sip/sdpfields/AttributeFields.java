/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;
import java.util.ListIterator;
/**
* A list of SDP attribute fields.
*/

public class AttributeFields extends SDPFieldList {
	protected SDPObjectList attributes;

	public AttributeFields() {
		super("AttributeField", SDPFIELDS_PACKAGE + ".AttributeField");
		attributes = new SDPObjectList("attributes");
	}

	public void  add( AttributeField a) {
		super.add(a);
		attributes.add(a);
	}

	/**
	* Get the attribute given a name. Just use linear search here 
	* (attribute lists are likely to be short).
	*/
	public String getValue (String name) {
		AttributeField a;
		for (a = (AttributeField) attributes.first();
		     a != null;
		     a = (AttributeField) attributes.next()) {
			if (name.equals(a.attribute.getName())) {
			   return (String) a.attribute.getValue();
			}
		}
		return (String) null;
	}

	/**
	* Set the attributes member  
	*/
	public	 void setAttributes(SDPObjectList a) 
 	 	{ attributes = a ; } 

    /**
     *  Get the string encoded version of this object
     * @since v1.0
     */
    public String encode() {
        ListIterator iterator; 
        String encoded_string;
        AttributeField attribute;
	
/**
	encoded_string = "";
	iterator = attributes.listIterator(0);
	while (iterator.hasNext()) {
	    attribute = (AttributeField) iterator.next();
	    encoded_string += attribute.encode();
	}
	return encoded_string;
*/
	return attributes.encode();
    }

    /**
    * Equality checking predicate.
    */
    public boolean equals(Object other) {
	if (! other.getClass().equals(this.getClass()) ) return false;
	AttributeFields that = (AttributeFields) other;
	return this.attributes.equals(that.attributes);
    }

}
