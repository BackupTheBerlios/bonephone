/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: C. Chazeau (christophe.chazeau@nist.gov)                            *
* Modifications: M. Ranganathan (mranga@nist.gov)			       *
*  Added constructors							       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.log.*;
import gov.nist.sip.net.*;
import gov.nist.jain.protocol.ip.sip.*;
import gov.nist.sip.msgparser.*;

import java.util.Iterator ;
import java.util.Date ;
import java.util.Calendar ;

/**
* Implementation of the ContactHeader interface of jain-sip.
* Builds a wapper around the NIST-SIP Contact class.
*@see gov.nist.sip.Contact
*/
public final class AuthorizationHeaderImpl extends HeaderImpl implements 
AuthorizationHeader,NistSIPHeaderMapping {
    
    /**
    * Default constructor.
    */
    public AuthorizationHeaderImpl() { 
        super();
      this.headerName = name;
    }
    
    /**
    * Constructor.
    *@param auth imbedded NIST-SIP implementation object.
    */
    public AuthorizationHeaderImpl(Authorization auth) { 
        super(auth);
      this.headerName = name;
    }

   /**
    * Gets Iterator of parameter names
    * (Note - objects returned by Iterator are Strings)
    * (Returns null if no parameters exist)
    * @return Iterator of parameter names
    */
    public Iterator getParameters() {
	 Authorization authorization = (Authorization) sipHeader;
         Iterator iterator=authorization.getParameters();
         if ( iterator==null) return null;
         if ( iterator.hasNext() ) return iterator;
         else return null;  
    }
    
   /**
    * Gets the value of specified parameter
    * (Note - zero-length String indicates flag parameter)
    * (Returns null if parameter does not exist)
    * @param <var>name</var> name of parameter to retrieve
    * @return the value of specified parameter
    * @throws IllegalArgumentException if name is null
    */
    public String getParameter(String name) throws IllegalArgumentException {
	    Authorization authorization = (Authorization) sipHeader;
            if (name == null) throw new
                IllegalArgumentException("JAIN-EXCEPTION: null argument");
            return (String) authorization.getParameter(name);
    }

   /**
    * Sets value of parameter
    * (Note - zero-length value String indicates flag parameter)
    * @param <var>name</var> name of parameter
    * @param <var>value</var> value of parameter
    * @throws IllegalArgumentException if name or value is null
    * @throws SipParseException if name or value is not accepted 
    * by implementation
    */
    public void setParameter(String name,String value) throws 
        IllegalArgumentException, SipParseException {
	    Authorization authorization = (Authorization) sipHeader;
            if (name == null || value == null) 
                throw new IllegalArgumentException
                    ("JAIN-EXCEPTION: Name or value is null!");
            else   authorization.setParameter(name,value);
     }
	 
    /**
     * Gets boolean value to indicate if Parameters
     * has any parameters
     * @return boolean value to indicate if Parameters
     * has any parameters
     */
     public boolean hasParameters() {
	    Authorization authorization = (Authorization) sipHeader;
            return authorization.hasParameters();
     }
	 
    /**
     * Gets boolean value to indicate if Parameters
     * has specified parameter
     * @return boolean value to indicate if Parameters
     * has specified parameter
     * @throws IllegalArgumentException if name is null
     */
     public boolean hasParameter(String name) throws IllegalArgumentException {
	    Authorization authorization = (Authorization) sipHeader;
            if(name == null) throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
            return authorization.hasParameter(name);
     }
	
    /**
     * Removes specified parameter from Parameters (if it exists)
     * @param <var>name</var> name of parameter
     * @throws IllegalArgumentException if parameter is null
     */
     public void removeParameter(String name) throws IllegalArgumentException {
	    Authorization authorization = (Authorization) sipHeader;
            if ( name == null) 
                throw new IllegalArgumentException("JAIN-EXCEPTION:name is null");
            authorization.removeParameter(name);
     }
	
    /**
     * Removes all parameters from Parameters (if any exist)
     */
     public void removeParameters() {
	    Authorization authorization = (Authorization) sipHeader;
            authorization.removeParameters();
     }

    /**
     * Method used to set the scheme
     * @param String the scheme
     * @throws IllegalArgumentException if scheme is null
     * @throws SipParseException if scheme is not accepted by implementation
     */
	public void setScheme(String scheme)
	 throws IllegalArgumentException, SipParseException{
		Authorization authorization = (Authorization) sipHeader;
	 	if (scheme == null) 
                    throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
	 	authorization.setScheme(scheme) ;
	 }

    /**
     * Method used to get the scheme
     * @return the scheme
     */
    public String getScheme(){
	Authorization authorization = (Authorization) sipHeader;
    	return authorization.getScheme() ;
    }
    
}
