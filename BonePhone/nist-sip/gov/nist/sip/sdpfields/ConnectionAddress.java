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
* Connection Address of the SDP header (appears as part of the Connection field).
*/
public class ConnectionAddress extends SDPObject {
	protected Host address;
	protected int ttl;
	protected int port;

	public	 Host getAddress() 
 	 	{ return address ; } 
	public	 int getTtl() 
 	 	{ return ttl ; } 
	public	 int getPort() 
 	 	{ return port ; } 
	/**
	* Set the address member  
	*/
	public	 void setAddress(Host a) 
 	 	{ address = a ; } 
	/**
	* Set the ttl member  
	*/
	public	 void setTtl(int t) 
 	 	{ ttl = t ; } 
	/**
	* Set the port member  
	*/
	public	 void setPort(int p) 
 	 	{ port = p ; } 

	/**
	*  Get the string encoded version of this object
	* @since v1.0
	*/
	public String encode() {
	    String encoded_string = "";

	    if (address != null) encoded_string = address.encode();
	    if (ttl != 0 && port != 0) {
		encoded_string += Separators.SLASH + 
				ttl + Separators.SLASH + port;
	    }
	    return encoded_string; 
	}

}
