/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
/**
*   Key field part of an SDP header
*/
public final class KeyField  extends SDPField {
	protected String 	   type;
	protected String 	   keyData;
	protected URI		   uri;
	
	public KeyField() { super(KEY_FIELD); }
	public String toString() {
	   super.initSprint();
           super.sprint("keyField =");
	   super.sprint("{");
           super.sprint("inputText = " + inputText);
	   super.sprint("type = " + type);
           super.sprint("keyData  = " + keyData);
           if (uri != null) {
              super.sprint(uri.toString());
           }
           sprint("}");
	   return getStringRepresentation();
        }


	public String getType()    
		{ return type; }

	public String getKeyData()     
        { 
	  return keyData;
        }

	public	 URI getUri() 
	{  
	  return uri;
	}
	/**
	* Set the type member  
	*/
	public	 void setType(String t) 
 	 	{ type = t ; } 
	/**
	* Set the keyData member  
	*/
	public	 void setKeyData(String k) 
 	 	{ keyData = k ; } 
	/**
	* Set the uri member  
	*/
	public	 void setURI(URI u) 
 	 	{ uri = u ; } 

    /**
     *  Get the string encoded version of this object
     * @since v1.0
     */
    public String encode() {
        String encoded_string; 
	encoded_string = KEY_FIELD + type;
	if (type.compareTo(SDPKeywords.PROMPT) == 0) {
	    encoded_string += Separators.COLON;
	    if (type.compareTo(SDPKeywords.URI) == 0) {
		encoded_string += uri.encode();
	    } else {
		encoded_string += keyData;
	    }
	}
	encoded_string += Separators.NEWLINE;
	return encoded_string;
    }
}
