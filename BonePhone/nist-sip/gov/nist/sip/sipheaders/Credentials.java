/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov), added JAVADOC                      *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* Credentials part of the AuthSIPObject.
*/
public class Credentials extends SIPObject {

        /** sheme field.
         */    
        protected String scheme;

        /** credentials list.
         */        
        protected NameValueList credentials;

        /** Default constructor
         */        
        public Credentials() {
	    credentials = new NameValueList("credentials");
        }
    
        /** get the credentials list.
         * @return NameValueList
         */        
	public NameValueList getCredentials() {
		return credentials;
	}

        /** get the scheme field.
         * @return String.
         */        
	public String getScheme() {
		return scheme;
	}
        
	/**
         * Set the scheme member
         * @param s String to set
            */
	public void setScheme(String s) {
            scheme = s ;
        }
        
	/**
         * Set the credentials member
         * @param c NameValueList to set.
         */
	public void setCredentials(NameValueList c) {
            credentials = c ;
        } 

	public String encode() {
	   String retval = scheme ;
	   if (! credentials.isEmpty()) {
		retval += SP + credentials.encode();
	   }
	   return retval;
	}
        
}
