/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov)                                *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;

import java.util.*;

/**
* WWW Authenticate SIP (HTTP ) header.
*<pre>
*
* Proxy-Authenticate  = "Proxy-Authenticate" ":" 1#challenge
*
* HTTP RFC 2616 
*</pre>
*/
public class ProxyAuthenticate extends SIPHeader {
    
    /** challenge field
     */
    protected Challenge challenge;
    
     /** Default Constructor
     */
    public ProxyAuthenticate() {
        super(PROXY_AUTHENTICATE);
    }
    
    /** Constructor
     * @param ch Challenge to set
     */
    public ProxyAuthenticate( Challenge ch) {
        super(PROXY_AUTHENTICATE);
        challenge = ch;
    }
    
        /**
         * Encode in canonical form.
         * @return String
         */
    public String encode() {
        return headerName + COLON + SP + challenge.encode() + NEWLINE;
    }
        
        /** get the challenge
         * @return challenge
         */        
        public Challenge getChallenge() {
            return challenge;
        }
        
        public Iterator getParameters() {
            if (challenge==null) return null;
            else {
                NameValueList nvl=challenge.getAuthParams();
                if (nvl==null) return null;
                else return nvl.getNames();
            }
        }
        
	/* get the Scheme
         * @return String
         */
        public String getScheme() {
            if (challenge==null) return null;
            else return challenge.getScheme();
        }
        
	/** set the challenge scheme.
	*/
	public void setScheme(String scheme) {
		if (challenge == null) challenge = new Challenge();
		challenge.setScheme(scheme);
	}

	/**
	* Set a parameter for the proxy auth header.
	*/
	public void setParameter(String name, String value) {
		if (challenge == null) challenge = new Challenge();
                else {
                        NameValueList nvl=challenge.getAuthParams();
                        if (nvl.hasNameValue(name) )
		                                removeParameter(name) ;
                }
		challenge.setParameter(name,value);
	}

	/**
	* return true if the challenge has parameters.
	*/
	public boolean hasParameters() {
		return challenge != null  && challenge.hasParameters();
	}

	/**
	* Return true if the challenge has a parameter.
	*/
	public boolean hasParameter(String param) {
		return challenge != null && challenge.hasParameter(param);
	}

	/**
	* Remove a parameter given a parameter name.
	*@param name parameter name
	*/

	public void removeParameter(String name) {
		if (challenge != null) challenge.removeParameter(name);
	}

	/** Remove all parameters.
	*/

	public void removeParameters() {
		if (challenge != null) challenge.removeParameters();
	}

	/**
	* Get a parameter given its name.
	*/
	public String getParameter(String name) {
		if (challenge != null) return challenge.getParameter(name);
		else return null;
	}
        
}
