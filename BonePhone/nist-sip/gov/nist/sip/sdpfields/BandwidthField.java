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
* Bandwidth field of a SDP header.
*/

public class BandwidthField extends SDPField {
	protected String bwtype;
	protected int 	bandwidth;
	public BandwidthField() {
		super(SDPFieldNames.BANDWIDTH_FIELD);
	}
	public	 String getBwtype() 
 	 	{ return bwtype ; } 
	public	 int getBandwidth() 
 	 	{ return bandwidth ; } 
	/**
	* Set the bwtype member  
	*/
	public	 void setBwtype(String b) 
 	 	{ bwtype = b ; } 
	/**
	* Set the bandwidth member  
	*/
	public	 void setBandwidth(int b) 
 	 	{ bandwidth = b ; } 

	/**
	*  Get the string encoded version of this object
	* @since v1.0
	*/
	public String encode() {
	   String encoded_string = BANDWIDTH_FIELD;

	   if (bwtype != null) encoded_string += bwtype + Separators.COLON;
	   return encoded_string + bandwidth + Separators.NEWLINE; 
	}

}
