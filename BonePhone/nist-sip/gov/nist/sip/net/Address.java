/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../docs/api/uncopyright.html for conditions of use.             *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov), 			       *
* O Deruelle (deruelle@nist.gov) added JAVADOC.                                *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;
import gov.nist.sip.*;

/**
* Address structure.
*@since 0.9
*@version 1.0
*Revision history:
* Added a flag to identify the address type. ( 1/10/01 mranga@nist.gov )
* Moved over to gov.nist.sip.net package
*/
public final class Address extends NetObject  {
    
    /** Constant field.
     */    
    public static final int NAME_ADDR   = 1;
  
    /** constant field.
     */    
    public static final int ADDRESS_SPEC = 2;
    
    private int    addressType;
    
    /** displayName field
     */    
    protected String displayName;
    
    /** addrSpec field
     */    
    protected URI	 addrSpec;
    
        /**
         * Encode the address as a string and return it.
         * @return String canonical encoded version of this address.
         */
      public String encode() {
        String encoding = "";
        if (displayName != null) {
            encoding += Separators.DOUBLE_QUOTE + displayName  +
			Separators.DOUBLE_QUOTE + 
				Separators.SP;
        }
	if (addrSpec != null) {
	 if (addressType == NAME_ADDR || displayName != null) 
          	encoding += LESS_THAN;
          encoding += addrSpec.encode();
	 if (addressType == NAME_ADDR || displayName != null) 
		encoding += GREATER_THAN;
	}
        return encoding;
      }
    
        /**
         * Get the address type;
         * @return int 
         */
    public int getAddressType() { 
        return addressType;
    }

	/** get the user at host:port from the embedded URI if it exists.
	*@returns user at host:port string or just host:port if user is
  	* not specified.
	*/
	public String getUserAtHostPort() {
		return addrSpec.getUserAtHost();
	}
    
        /**
         * Set the address type.
         * @param atype int to set
         */
    public void setAddressType( int atype) {
        addressType = atype;
    }
    
        /**
         * get the display name
         * @return String
         */
    public String getDisplayName() { 
        return displayName ;
    }
    
        /**
         * get the address spec part of the route header.
         * @return URI
         */
    public URI getAddrSpec() { 
        return addrSpec ;
    }
    
        /**
         * Conveniance function to get the HostPort structure from the address.
         * @return HostPort
         */
    public HostPort getHostPort() {
	if (addrSpec == null) return null;
        AuthorityServer authServer =
        (AuthorityServer) addrSpec.getAuthority();
	if (authServer == null) return null;
        return authServer.getHostPort();
    }
    
        /**
         * convenience function to get a host name from the address.
         * Beware - could thow a class cast exception.
         * @return String 
         */
    public String getHost() {
	if (this.addrSpec == null) return null;
        AuthorityServer authServer = (AuthorityServer)
        addrSpec.getAuthority();
        return authServer.getHostPort().getHost().getHostname();
    }
    
        /**
         * convenience function to get the address string from the address.
         * Beware - could thow a class cast exception.
         * @return String
         */
    public String getAddressString() {
	if (this.addrSpec == null) return null;
        AuthorityServer authServer = (AuthorityServer)
        	addrSpec.getAuthority();
        return authServer.getHostPort().getHost().getAddress();
    }
    
        /**
         * get the port from the address.
         * conveniance function to follow ptrs and get the port from the 
         * address. Beware - could thow a class cast exception.
         * @return int
         *
         */
    public int getPort() {
        AuthorityServer authServer = (AuthorityServer)
        addrSpec.getAuthority();
        return authServer.getHostPort().getPort();
    }
    
        /**
         * get the transport parameter.
         * @return String
         */
    public String getTransport() {
	if (this.addrSpec == null) return null;
        else return (String)
        this.addrSpec.getParm(URIKeywords.TRANSPORT);
    }
    
        /**
         * Get a parameter from the URI
         * @param parmName String to set
         * @return Object
         */
    public Object getParm(String parmName) {
	if (this.addrSpec == null) return null;
        return addrSpec.getParm(parmName);
    }

	 /**
          * Return the parameter list.
          * @return NameValueList
          */
     public NameValueList getParms() {
	if (this.addrSpec == null) return null;
	return addrSpec.getUriParms();
     }
     
        /**
         * Set the displayName member
         * @param d String to set
         */
    public void setDisplayName(String d) { 
	displayName = d ;
    }

	/**
	* Set the name of the user in the URI.
	*@param uname Name of the user to set in the URI.
	*/
    public void setUserName(String uname) {
	if (this.addrSpec == null) {
		this.addrSpec = new URI();
	}
	this.addrSpec.setUser(uname);
    }
    
        /**
         * Set the addrSpec member
         * @param a URI to set
         */
    public void setAddrSpec(URI a) { 
        addrSpec = a ;
    }
    
    /**
     * Compare two address specs for equality.
     * @param other Object to set
     * @return boolean
     */
    public boolean equals(Object other) {
        if (! this.getClass().equals(other.getClass())) {
            return false;
        }
        Address that = (Address) other;
        // Ignore the display name; only compare the address spec.
        return this.addrSpec.equals((that.addrSpec));
    }
        
        /** return true if DisplayName exist.
         * @return boolean
         */    
    public boolean hasDisplayName() {
        return (displayName!=null);
    }
    
        /** remove the displayName field
         */    
    public void removeDisplayName() {
        displayName=null;
    }

        /** set the specified parameter
         * @param name String to set
         * @param value Object to set
         */    
    public void setParam(String name, Object value) {
	this.addrSpec.setUriParm(name,value);	
    }
    
        /** return true if the specified parameter exist.
         * @param name String to set
         * @return boolean
         */    
    public boolean hasParm(String name) {
        return this.addrSpec.hasParameter(name);
    }
    
        /** remove the specified parameter
         * @param name of the parameter to remove.
         */    
    public void removeParameter(String name) {
	Debug.println("removeParameter " + name);
        this.addrSpec.removeParameter(name);
    }
    
        /** remove all parameters
         */    
    public void removeParameters() {
        this.addrSpec.clearUriParms();
    }
 
}
