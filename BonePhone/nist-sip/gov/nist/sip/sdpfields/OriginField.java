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
*   Origin Field SDP header
*/
public class OriginField extends SDPField {
	protected String  username;
	protected long    sessId;
	protected long	  sessVersion;	
	protected String  nettype;  // IN
	protected String  addrtype; // IPV4/6
	protected Host    address;


	public OriginField() {
		super(ORIGIN_FIELD);
	}

	/**
	* Get the username member.
	*/
	public	 String getUsername() 
 	 	{ return username ; } 
	/**
	* Get the sessionID member.
	*/
	public	 long getSessId() 
 	 	{ return sessId ; } 
	/**
	* Get the sessionVersion member.
	*/
	public	 long getSessVersion() 
 	 	{ return sessVersion ; } 
	/**
	* Get the netType member.
	*/
	public	 String getNettype() 
 	 	{ return nettype ; } 
	/**
	* Get the address type member.
	*/
	public	 String getAddrtype() 
 	 	{ return addrtype ; } 
	/**
	* Get the host member.
	*/
	public	 Host getAddress() 
 	 	{ return address ; } 
	/**
	* Set the username member  
	*/
	public	 void setUsername(String u) 
 	 	{ username = u ; } 
	/**
	* Set the sessId member  
	*/
	public	 void setSessId(long s) 
 	 	{ sessId = s ; } 
	/**
	* Set the sessVersion member  
	*/
	public	 void setSessVersion(long s) 
 	 	{ sessVersion = s ; } 
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
	public	 void setAddress(Host a) 
 	 	{ address = a ; } 

    /**
     *  Get the string encoded version of this object
     * @since v1.0
     */
    public String encode() {
	return ORIGIN_FIELD + username + Separators.SP 
	    + sessId + Separators.SP
	    + sessVersion + Separators.SP
	    + nettype +	Separators.SP
	    + addrtype + Separators.SP
	    + address.encode() + Separators.NEWLINE;
    }

}
