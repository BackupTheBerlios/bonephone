/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: O. Deruelle (deruelle@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;

import java.util.*;


/**
* Implementation of the ProxyAuthorizationHeader interface of jain-sip.
*/
public final class ProxyAuthorizationHeaderImpl extends HeaderImpl 
implements ProxyAuthorizationHeader, NistSIPHeaderMapping {

    
    /** Default constructor
     */    
   public ProxyAuthorizationHeaderImpl() { 
       super();
            this.headerName = name;
   }

    /** constructor
     * @param prh ProxyAuthorization to set
     */   
   public ProxyAuthorizationHeaderImpl(ProxyAuthorization prh) {
       super(prh);
            this.headerName = name;
   }
    
    /**
     * Method used to set the scheme
     * @param scheme String to set
     * @throws IllegalArgumentException if scheme is null
     * @throws SipParseException if scheme is not accepted by implementation
     */
    public void setScheme(String scheme)
    throws IllegalArgumentException, SipParseException {
        ProxyAuthorization proxyAuthorization=(ProxyAuthorization)sipHeader;
        if ( scheme==null)
                throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION :scheme is null "); 
        else proxyAuthorization.setScheme(scheme);
    }

    
    /**
    * Method used to get the scheme
    * @return the scheme
    */
    public String getScheme() {
        ProxyAuthorization proxyAuthorization=(ProxyAuthorization)sipHeader;
        return proxyAuthorization.getScheme();
    }
    
    /**
    * Gets Iterator of parameter names
    * (Note - objects returned by Iterator are Strings)
    * (Returns null if no parameters exist)
    * @return Iterator of parameter names
    */
    public Iterator getParameters() {
         ProxyAuthorization proxyAuthorization=(ProxyAuthorization)sipHeader;
       
           Iterator iterator=  proxyAuthorization.getParameters();
            if ( iterator==null) return null;
            if ( iterator.hasNext() ) return iterator;
            else return null;  
    }

    
    /**
     * Gets the value of specified parameter
     * (Note - zero-length String indicates flag parameter)
     * (Returns null if parameter does not exist)
     * @return the value of specified parameter
     * @param name String to set
     * @throws IllegalArgumentException if name is null
     */
    public String getParameter(String name) throws IllegalArgumentException {
         ProxyAuthorization proxyAuthorization=(ProxyAuthorization)sipHeader;
          if (name == null) throw new
                IllegalArgumentException("JAIN-EXCEPTION: null argument");
          else 
                return proxyAuthorization.getParameter(name);
    }

          
    /**
     * Sets value of parameter
     * (Note - zero-length value String indicates flag parameter)
     * @param name String to set
     * @param value String to set
     * @throws IllegalArgumentException if name or value is null
     * @throws SipParseException if name or value is not accepted
     * by implementation
     */
    public void setParameter(String name, String value) 
    throws IllegalArgumentException, SipParseException {
         ProxyAuthorization proxyAuthorization=(ProxyAuthorization)sipHeader;
          if (name == null || value == null) {
                throw new 
                    IllegalArgumentException
                    ("JAIN-EXCEPTION: Name or value is null!");
                }
            else  proxyAuthorization.setParameter(name,value);
    }
         
    
    /**
    * Gets boolean value to indicate if Parameters
    * has any parameters
    * @return boolean value to indicate if Parameters
    * has any parameters
    */
    public boolean hasParameters() {
         ProxyAuthorization proxyAuthorization=(ProxyAuthorization)sipHeader;
         return proxyAuthorization.hasParameters();           
    }

    
    /**
     * Gets boolean value to indicate if Parameters
     * has specified parameter
     * @return boolean value to indicate if Parameters
     * has specified parameter
     * @param name String to set
     * @throws IllegalArgumentException if name is null
     */
    public boolean hasParameter(String name) throws IllegalArgumentException {
         ProxyAuthorization proxyAuthorization=(ProxyAuthorization)sipHeader;
           if(name == null) throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
            return proxyAuthorization.hasParameter(name);
    }

    
    /**
     * Removes specified parameter from Parameters (if it exists)
     * @param name String to set
     * @throws IllegalArgumentException if parameter is null
     */
    public void removeParameter(String name) throws IllegalArgumentException {
         ProxyAuthorization proxyAuthorization=(ProxyAuthorization)sipHeader;
         if (name==null)
           throw new IllegalArgumentException("JAIN-EXCEPTION: null parameter");
         else proxyAuthorization.removeParameter(name);
    }

    
    /**
    * Removes all parameters from Parameters (if any exist)
    */
    public void removeParameters() {
        ProxyAuthorization proxyAuthorization=(ProxyAuthorization)sipHeader;
        proxyAuthorization.removeParameters();   
    }
     
}


