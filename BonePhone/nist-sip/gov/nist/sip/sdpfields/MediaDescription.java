/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;
import gov.nist.sip.*;
/**
* Media Description SDP header
*/
public class MediaDescription  extends SDPField {
	protected MediaField		mediaField;
	protected InformationField	informationField;
	protected ConnectionField	connectionField;
	protected BandwidthField   	bandwidthField;
	protected KeyField		keyField;
	protected AttributeFields	attributeFields;

	/**
	* Encode to a canonical form.
	*@since v1.0
	*/
	public String encode() {
		String retval = "";

		if (mediaField != null) 
			retval =  mediaField.encode();

		if (informationField != null) 
			retval += informationField.encode();

		if(connectionField != null) 
			retval += connectionField.encode();

		if (bandwidthField != null) 
			retval += bandwidthField.encode();

		if (keyField != null) 
			retval += keyField.encode();

		if (attributeFields != null) 
			retval += attributeFields.encode();

		return retval;
	}
			
			
	

	public MediaDescription() 
		{ super(MEDIA_FIELD); }
	public	 MediaField getMediaField() 
 	 	{ return mediaField ; } 
	public	 InformationField getInformationField() 
 	 	{ return informationField ; } 
	public	 ConnectionField getConnectionField() 
 	 	{ return connectionField ; } 
	public	 BandwidthField getBandwidthField() 
 	 	{ return bandwidthField ; } 
	public	 KeyField getKeyField() 
 	 	{ return keyField ; } 
	public	 AttributeFields getAttributeFields() 
 	 	{ return attributeFields ; } 
	/**
	* Set the mediaField member  
	*/
	public	 void setMediaField(MediaField m) 
 	 	{ mediaField = m ; } 
	/**
	* Set the informationField member  
	*/
	public	 void setInformationField(InformationField i) 
 	 	{ informationField = i ; } 
	/**
	* Set the connectionField member  
	*/
	public	 void setConnectionField(ConnectionField c) 
 	 	{ connectionField = c ; } 
	/**
	* Set the bandwidthField member  
	*/
	public	 void setBandwidthField(BandwidthField b) 
 	 	{ bandwidthField = b ; } 
	/**
	* Set the keyField member  
	*/
	public	 void setKeyField(KeyField k) 
 	 	{ keyField = k ; } 
	/**
	* Set the attributeFields member  
	*/
	public	 void setAttributeFields(AttributeFields a) 
 	 	{ attributeFields = a ; } 
}
