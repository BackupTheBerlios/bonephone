/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modifications by: O. Deruelle (deruelle@antd.nist.gov added JAVADOC)         *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;

import java.util.*;

/**
* WWW Authenticate SIP (HTTP ) header.
*<pre>
*
* WWW-Authenticate  = "WWW-Authenticate" ":" 1#challenge
*challenge        =  "Digest" digest-challenge
* 
*      digest-challenge  = 1#( realm | [ domain ] | nonce |
*                          [ opaque ] |[ stale ] | [ algorithm ] |
*                          [ qop-options ] | [auth-param] )
* 
* 
*      domain            = "domain" "=" <"> URI ( 1*SP URI ) <">
*      URI               = absoluteURI | abs_path
*      nonce             = "nonce" "=" nonce-value
*      nonce-value       = quoted-string
*      opaque            = "opaque" "=" quoted-string
*      stale             = "stale" "=" ( "true" | "false" )
*      algorithm         = "algorithm" "=" ( "MD5" | "MD5-sess" |
*                           token )
*      qop-options       = "qop" "=" <"> 1#qop-value <">
*      qop-value         = "auth" | "auth-int" | token     
*
* HTTP RFC 2617 
*</pre>
*i
*/

public class WWWAuthenticate extends SIPHeader {
    
         /** challenge field
          */
    protected Challenge challenge;
    
        /**
         * Default Constructor.
         */
    public WWWAuthenticate() {
        super(WWW_AUTHENTICATE);
    }
    
        /**
         * Constructor.
         * @param ch Challenge to set
         */
    public WWWAuthenticate( Challenge ch) {
        super(WWW_AUTHENTICATE);
        challenge = ch;
    }
    
        /**
         * Encode in canonical form.
         * @return canonical string.
         */
    public String encode() {
        return headerName + COLON + SP + challenge.encode() + NEWLINE;
    }
    
          /** get the challenge field
           * @return challenge field of this header.
           */
    public Challenge getChallenge() {
        return challenge;
    }
    
          /** get the Scheme field
           * @return Scheme field of this header.
           */
    public String getScheme() {
        if ( challenge==null) return null;
        else return challenge.getScheme();
    }
    
    /**
     * Get a parameter given its name.
     * @param name parameter name.
     * @return String
     */
    public String getParameter(String name) {
        if (challenge != null) return challenge.getParameter(name);
        else return null;
    }
    
        /** get the parameters.
         * @return Iterator
         */
    public Iterator getParameters() {
        if ( challenge == null) return null;
        else {
                NameValueList nvl=challenge.getAuthParams();
                if (nvl==null) return null;
                else return nvl.getNames();
        }
    }
    
        /**
         * return true if the challenge has parameters.
         * @return boolean
         */
    public boolean hasParameters() {
        return challenge != null && challenge.hasParameters();
    }
    
        /**
         * Return true if the challenge has a parameter.
         * @param param String to set
         * @return boolean
         */
    public boolean hasParameter(String param) {
        return challenge != null && challenge.hasParameter(param);
    }
    
        /** remove a parameter given its name.
         * @param name String to set
         */
    public void removeParameter(String name) {
        if (challenge != null) challenge.removeParameter(name);
    }
    
        /** Remove all parameters.
         */
    public void removeParameters() { 
        if (challenge != null) challenge.removeParameters(); 
    }
        
        /** Set the sheme.
         * @param scheme String to set
         */
    public void setScheme(String scheme) {
        if (challenge == null) challenge = new Challenge();
        challenge.setScheme(scheme);
    }
    
        /**
         * Set a parameter for the proxy auth header.
         * @param name String to set
         * @param value String to set
         */
    public void setParameter(String name, String value) {
        if (challenge == null) challenge = new Challenge();
        if (challenge.hasParameter(name) ) challenge.removeParameter(name) ;
        challenge.setParameter(name,value);
    }
      
}
