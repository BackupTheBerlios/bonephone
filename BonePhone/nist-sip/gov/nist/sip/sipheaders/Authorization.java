/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By: Christophe Chazeau, Olivier Deruelle (deruelle@nist.gov)        *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;

import java.util.*;

/**
* Authorization SIP header 
* @see ProxyAuthorization
*
*<pre>
* 3.2.2 The Authorization Request Header
* 
*   The client is expected to retry the request, passing an Authorization
*   header line, which is defined according to the framework above,
*   utilized as follows.
* 
*       credentials      = "Digest" digest-response
*       digest-response  = 1#( username | realm | nonce | digest-uri
*                       | response | [ algorithm ] | [cnonce] |
*                       [opaque] | [message-qop] |
*                           [nonce-count]  | [auth-param] )
* 
*       username         = "username" "=" username-value
*       username-value   = quoted-string
*       digest-uri       = "uri" "=" digest-uri-value
*       digest-uri-value = request-uri   ; As specified by HTTP/1.1
*       message-qop      = "qop" "=" qop-value
*       cnonce           = "cnonce" "=" cnonce-value
*       cnonce-value     = nonce-value
*       nonce-count      = "nc" "=" nc-value
*       nc-value         = 8LHEX
*       response         = "response" "=" request-digest
*       request-digest = <"> 32LHEX <">
*       LHEX             =  "0" | "1" | "2" | "3" |
*                           "4" | "5" | "6" | "7" |
*                           "8" | "9" | "a" | "b" |
*                           "c" | "d" | "e" | "f"
* </pre>
*/
public class Authorization extends SIPHeader implements AuthorizationKeywords {
    
        /** scheme field
         */    
	protected String scheme;

        /** credentials list
         */        
        protected NameValueList credentials;
        
        /** Default constructor
         */        
	public Authorization() {
		super(AUTHORIZATION);
		credentials = new NameValueList("credentials");
		credentials.setSeparator(Separators.COMMA);
	}

        /** constructor
         * @param hname String to set
         */        
	public Authorization(String hname) {
		super(hname);
		credentials = new NameValueList("credentials");
		credentials.setSeparator(Separators.COMMA);
	}
	        
        /**
         * Add a parameter.
         * @param nv NameValue to set
         */
	public void addParam (NameValue nv ) {
		credentials.add(nv);
	}      
               
	/**
         * return the canonical form of the header.
         * @return String 
         */
	public String encode() {
		return  headerName + COLON + SP + scheme + 
				SP + credentials.encode() + NEWLINE;
	}

	/**
         * Conveniance function to retrieve the values from the parameter list.
         * @param parmName String to set
         * @return String
         */
	public String getValue(String parmName) {
		String retval = (String) credentials.getValue(parmName);
		return retval;
	}

        /** get the scheme field
         * @return String
         */        
	public	 String getScheme() {
            return scheme ;
        } 
		
	/**
         * Get the domain value.
         * @return String
         */
	public String getDomain() {
            return getValue(DOMAIN);
        }

	/**
         * return the credentials as a NameValue list.
         * @return nameValueList
         */
	public	 NameValueList getCredentials() {
            return credentials ;
        } 

	/**
         * Get the realm value (or null if it does not exist).
         * @return String
         */
	public String getRealm() {
            return getValue(REALM);
        }

	/**
         * Get the algorithm string (or null if it does not exist).
         * @return String
         */
	public String getAlgorithm() {
            return getValue(ALGORITHM);
        }

	/**
         * Get the nonce string (or null if it does not exist).
         * @return String
         */
	public String getNonce() {
            return getValue(NONCE);
        }

	/**
         * Get the URI (or null if it does not exist).
         * @return String
         */
	public String getURI() {
            return getValue(URI);
        }

	/**
         * Get the OPAQUE value (or null if it does not exist).
         * @return String
         */
	public String getOpaque() {
            return getValue(OPAQUE);
        }

	/**
         * Get the RESPONSE value (or null if it does not exist).
         * @return String
         */
	public String getResponse() {
            return getValue(RESPONSE);
        }

       /** Get a parameter.
         * @param name String to set
         * @return String
         */
	public String getParameter(String name) {
		if (credentials == null) return null;
		else return (String)credentials.getValue(name);
	}

	/** get the parameters.
         * @return Iterator
         */
	public Iterator getParameters() {
		if ( credentials==null) return null;
                else return credentials.getNames();
	}
        
        /** boolean function
         * @return true if this header has parameters, false otherwise.
         */        
        public boolean hasParameters() {
             if ( credentials==null) return false;
             else  return !credentials.isEmpty();
        }
       
        /** Boolean function
         * @param name String to set
         * @return true if this header has the specified parameter, false
         * otherwise.
         */        
        public boolean hasParameter(String name) {
             if ( credentials==null) return false;
             else  return credentials.getNameValue(name)!=null; 
        }
               
	/**
	* Remove the parameters.
	* @since 1.0
	*/
         public void removeParameters() {
             credentials = new NameValueList("credentials");
             credentials.setSeparator(Separators.COMMA);
	}       
        
        /** delete the specified parameter
         * @param name String to set
         * @return true if the specified parameter has been removed,
         * false otherwise.
         */        
        public boolean removeParameter (String name) {
              if ( credentials==null) return false;
	      return credentials.delete(name);
	}
        
	/**
         * Set the scheme member
         * @param s String to set
         */
	public	 void setScheme(String s) {
            scheme = s ;
        }
        
	/**
         * Set the credentials member
         * @param c NameValueList to set
         */
	public	 void setCredentials(NameValueList c) {
            credentials = c ;
        } 

        /** set the specified parameter.
         * @param name String to set
         * @param value String to set
         */        
        public void setParameter(String name, String value)  {
		NameValue nv = new NameValue(name,value);
                if ( credentials == null) {
                        credentials = new NameValueList("credentials");
                        credentials.setSeparator(Separators.COMMA);
                }
		credentials.add(nv);
	}         
              
}
