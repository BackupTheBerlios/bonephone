/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* Challenge part of the Auth header.
* <pre>
*  auth-scheme    = token
*  auth-param     = token "=" ( token | quoted-string )   
*  challenge   = auth-scheme 1*SP 1#auth-param    
* </pre>
*/
public class Challenge extends SIPObject implements AuthorizationKeywords  {
    
        /** scheme field
         */    
	protected String    	scheme;

        /** authParms list
         */        
        protected NameValueList authParams;
	
        /** Default constructor     
         */        
        public Challenge() {
		authParams = new NameValueList("authParams");
	}
        
        /**
         * Encode the challenge in canonical form.
         * @return String
         */
        public String encode() {
		String retval = scheme ;
		if (scheme.compareTo(BASIC) == 0) {
			if (authParams.getValue(REALM) != null)  {
			  retval += 
				SP + authParams.getNameValue(REALM).encode();
			}
			
			if (authParams.getValue(PASSWORD) != null)  {
			   retval += (String) authParams.getValue(PASSWORD);
			}
		} else {
		     retval += authParams.encode();
		}
		return retval;
	}
        
        /** get the scheme field
         * @return String
         */        
        public	 String getScheme() {
            return scheme ;
        } 
            
        /** get AuthParms list.
         * @return NameValueList
         */        
        public	 NameValueList getAuthParams() {
            return authParams ;
        }
        
	/** get the password
         * For basic authentication.
         * @return String
         */
	public String getPassword() {
		return (String) authParams.getValue(PASSWORD);
	}

        /** get the domain
         * @return String
         */        
	public String getDomain() {
		return (String) authParams.getValue(DOMAIN);
	}

        /** get the URI field
         * @return String
         */        
	public String getURI() {
		return (String) authParams.getValue(URI);
	}

        /** get the Opaque field
         * @return String
         */        
	public String getOpaque() {
		return (String) authParams.getValue(OPAQUE);
	}

        /** get QOP value
         * @return String
         */        
	public String getQOP() {
		return (String) authParams.getValue(QOP);
	}

        /** get the Algorithm value.
         * @return String
         */        
	public String getAlgorithm()  {
		return (String) authParams.getValue(ALGORITHM);
	}

        /** get the State value.
         * @return String
         */        
	public String getStale() {
		return (String) authParams.getValue(STALE);
	}

        /** get the Signature value.
         * @return String
         */        
	public String getSignature() {
		return (String) authParams.getValue(SIGNATURE);
	}

        /** get the signedBy value.
         * @return String
         */        
	public String getSignedBy() {
		return (String) authParams.getValue(SIGNED_BY);
	}

        /** get the Response value.
         * @return String
         */        
	public String getResponse() {
		return (String) authParams.getValue(RESPONSE);
	}

        /** get the realm value.
         * @return String.
         */        
	public String getRealm() {
		return (String) authParams.getValue(REALM);
	}
        
        /** get the specified parameter
         * @param name String to set
         * @return String to set
         */        
	public String getParameter (String name ) {
		return (String) authParams.getValue(name);
	}
      
        /** boolean function
         * @param name String to set
         * @return true if this header has the specified parameter, false otherwise.
         */        
        public boolean hasParameter(String name) {
                return authParams.getNameValue(name)!=null; 
        }
        
        /** Boolean function
         * @return true if this header has some parameters.
         */        
        public boolean hasParameters() {
                return authParams.size()!=0;
        }
       
         /** delete the specified parameter
         * @param name String
         * @return true if the specified parameter has been removed, false
         * otherwise.
         */        
	public boolean removeParameter (String name) {
		return authParams.delete(name);
	}
        
        /** remove all parameters
         */        
        public void removeParameters() {
             authParams = new NameValueList("authParams");
        }       
        
        /** set the specified parameter
         * @param nv NameValue to set
         */        
	public void setParam(NameValue nv) {
		authParams.add(nv);
	}

        /** set the specified parameter
         * @param name String to set
         * @param value String to set
         */        
	public void setParameter(String name, String value)  {
	        NameValue nv = authParams.getNameValue(name.toLowerCase());
		if (nv == null) {
		  nv = new NameValue(name,value);
		  authParams.add(nv);
		} else nv.setValue(value);
	}

        /**
         * Set the scheme member
         * @param s String to set
         */
	public	 void setScheme(String s) {
            scheme = s ;
        }
        
	/**
         * Set the authParams member
         * @param a NameValueList to set
         */
	public	 void setAuthParams(NameValueList a) {
            authParams = a ;
        } 
       
}
