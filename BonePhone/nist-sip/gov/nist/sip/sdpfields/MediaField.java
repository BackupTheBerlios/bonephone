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
*    Media field SDP header
*/
public class MediaField extends SDPField {
	protected String 	  media;
	protected int		  port;
	protected int     	  nports;
	protected String  	  proto;
	protected AttributeFields attributeFields;
	protected FormatList   	  fmt;

	public MediaField() {
	 	super(SDPFieldNames.MEDIA_FIELD); 
		attributeFields = new AttributeFields();
	}

	public	 String getMedia() 
 	 	{ return media ; } 
	public	 int getPort() 
 	 	{ return port ; } 
	public	 int getNports() 
 	 	{ return nports ; } 
	public	 String getProto() 
 	 	{ return proto ; } 
	public	 AttributeFields getAttributeFields() 
 	 	{ return attributeFields ; } 
	public	 FormatList getFmt() 
 	 	{ return fmt ; } 
	/**
	* Set the media member  
	*/
	public	 void setMedia(String m) 
 	 	{ media = m ; } 
	/**
	* Set the port member  
	*/
	public	 void setPort(int p) 
 	 	{ port = p ; } 
	/**
	* Set the nports member  
	*/
	public	 void setNports(int n) 
 	 	{ nports = n ; } 
	/**
	* Set the proto member  
	*/
	public	 void setProto(String p) 
 	 	{ proto = p ; } 
	/**
	* Set the attributeFields member  
	*/
	public	 void setAttributeFields(AttributeFields a) 
 	 	{ attributeFields = a ; } 
	/**
	* Set the fmt member  
	*/
	public	 void setFmt(FormatList f) 
 	 	{ fmt = f ; } 

    /**
     *  Get the string encoded version of this object
     * @since v1.0
     */
    public String encode() {
        String encoded_string;
	encoded_string = MEDIA_FIELD ;
	if (media  != null) encoded_string += media + Separators.SP + port;
	if (nports != 0) encoded_string += Separators.SLASH + nports; 
	if (proto  != null) encoded_string += Separators.SP + proto;
	if (fmt    != null) encoded_string  += Separators.SP + fmt.encode() ; 
	encoded_string += Separators.NEWLINE;
	if (attributeFields != null) 
		encoded_string  += attributeFields.encode();
	return encoded_string;
    }
}
