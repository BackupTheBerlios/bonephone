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
* Connectin Field of the SDP request.
*/
public class ConnectionField extends SDPField {
	protected String nettype;
	protected String  addrtype;
	protected ConnectionAddress address;

	public ConnectionField () {
		super(SDPFieldNames.CONNECTION_FIELD);
	}
	public	 String getNettype() 
 	 	{ return nettype ; } 
	public	 String getAddrtype() 
 	 	{ return addrtype ; } 
	public	 ConnectionAddress getAddress() 
 	 	{ return address ; } 
	/**
	* Set the nettype member  
	*/
	public	 void setNettype(String n) 
 	 	{ nettype = n ; } 
	/**
	* Set the addrtype member  
	*/
	public	 void setAddrtype(String a) 
 	 	{ addrtype = a ; } 
	/**
	* Set the address member  
	*/
	public	 void setAddress(ConnectionAddress a) 
 	 	{ address = a ; } 
	/**
	* Get the string encoded version of this object
	* @since v1.0
	*/
	public String encode() {
	    String encoded_string = CONNECTION_FIELD;
	    if (nettype != null) encoded_string += nettype;
	    if (addrtype != null) encoded_string += Separators.SP + addrtype;
	    if (address != null) encoded_string += Separators.SP + 
							address.encode();
	    return encoded_string += Separators.NEWLINE;
	}

}
