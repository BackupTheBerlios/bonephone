/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import gov.nist.sip.net.*;

/**
* RequestLine of SIP Request.
*/
public class RequestLine  extends SIPHeader implements SIPRequestTypes {
    
        /** uri field
         */    
	protected URI 		 uri;
        
        /** method field
         */        
	protected String  	 method;
        
        /** sipVersion field
         */        
	protected String  	 sipVersion;
        
        /** Default constructor
         */        
	public RequestLine() {
            super(REQUEST_LINE);
	    sipVersion = "SIP/2.0";
        }
        
        /**
         * Encode the request line as a String
         * @return String
         */
 	public String encode() {
		return method + Separators.SP + uri.encode() + 
			Separators.SP + sipVersion  + Separators.NEWLINE;
	}
        
	/**
         * get the Request-URI
         * @return URI
         */
	public URI getUri() { 
            return uri ;
        } 

	/**
         * Get the Method
         * @return String
         */
	public String getMethod() { 
            return method ;
        } 

	/**
         * Get the SIP version.
         * @return String
         */
	public String getSipVersion() {
            return sipVersion ;
        } 

	/**
         * Set the uri member
         * @param u URI to set
         */
	public void setUri(URI u) { 
            uri = u ;
        } 

	/**
         * Set the method member
         * @param m String to set
         */
	public void setMethod(String m) { 
            method = m ;
        } 

	/**
         * Set the sipVersion member
         * @param s String to set
         */
	public void setSipVersion(String s) { 
            sipVersion = s ;
        } 

	/**
	* Get the major verrsion number.
	*@return String major version number
	*/
     public String getVersionMajor() {
	if (sipVersion == null) return null;
	String major = null;
	boolean slash = false;
	for (int i = 0; i < sipVersion.length(); i++) {
	  	if (sipVersion.charAt(i) == '.') break;
		if (slash) {
		  if (major == null) 
			major = "" + sipVersion.charAt(i);
		   else major += sipVersion.charAt(i);
	         }	
		 if (sipVersion.charAt(i) == '/') slash = true;
	}
	return major;
      }

        /**
         * Get the minor version number.
	*@return String minor version number
	 */
     public String getVersionMinor() {
        if (sipVersion == null) return null;
        String minor = null;
	boolean dot = false;
	for (int i = 0; i < sipVersion.length(); i++) {
		if (dot) {
		  if (minor == null) 
			minor  = "" +  sipVersion.charAt(i);	
		   else minor += sipVersion.charAt(i);
	         }	
		 if (sipVersion.charAt(i) == '.') dot = true;
	}
	return minor;
      }

	/**
	* Compare for equality.
	*@param other object to compare with. We assume that all fields 
	* are set.
	*/
	public boolean equals(Object other) {
	    if ( ! other.getClass().equals(this.getClass()) ) return false;
	    RequestLine that = (RequestLine) other;
	    try {
	      return this.method.equals(that.method)
		&& this.uri.equals(that.uri) 
		&& this.sipVersion.equals(that.sipVersion);
	    } catch (NullPointerException ex) { 
		return false; 
	    }

	}

}
