/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: O. Deruelle (deruelle@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.address.*;
import jain.protocol.ip.sip.header.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.jain.protocol.ip.sip.*;
import gov.nist.jain.protocol.ip.sip.address.*;
import gov.nist.sip.msgparser.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
* Implementation of the RouteHeader interface of jain-sip.
*/
public final class RouteHeaderImpl extends HeaderImpl 
implements RouteHeader, NistSIPHeaderMapping {

    /** Default constructor
     */    
    public RouteHeaderImpl() {
        super();
            this.headerName = name;
    }

    /** constructor
     * @param route Route to set
     */    
    public RouteHeaderImpl(Route route) { 
        super(route);
            this.headerName = name;
    }
  
    
    /**
     * Gets NameAddress of NameAddressHeader
     * (Returns null if NameAddress does not exist -
     * i.e. wildcard ContactHeader)
     * @return NameAddress of NameAddressHeader
     */
    public NameAddress getNameAddress() {
        Route route=(Route)sipHeader;
        Address addr=route.getAddress();
        if ( addr==null) return null;
        else{
            NameAddressImpl nai=new NameAddressImpl();
            nai.setImplementationObject(addr);
            return nai;
        }
    }
    
    
    /**
     * Sets NameAddress of NameAddressHeader
     * @param nameAddress nameAddress value to set.
     * @throws IllegalArgumentException if nameAddress is null or not from the
     * same JAIN SIP implementation
     */
    public void setNameAddress(NameAddress nameAddress)
    throws IllegalArgumentException {
        Route route=(Route)sipHeader;
        if ( nameAddress==null )
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION : parameter is null");
        else
            if (nameAddress instanceof NameAddressImpl)
            {
                NameAddressImpl nai=(NameAddressImpl)nameAddress;
                route.setAddress(nai.getImplementationObject());
            }
            else
                throw new IllegalArgumentException
                ("nameAddress is not from the same JAIN-SIP implementation");
    }
    
     /**
     * Gets the value of specified parameter
     * (Note - zero-length String indicates flag parameter)
     * (Returns null if parameter does not exist)
     * @param <var>name</var> name of parameter to retrieve
     * @return the value of specified parameter
     * @throws IllegalArgumentException if name is null
     */
    public String getParameter(String name) {
        Route route = (Route)sipHeader;
          if (name == null) { 
            throw new
                IllegalArgumentException("JAIN-EXCEPTION: null argument");
        }
        return route.getParameter(name);
    }
    
      /**
     * Sets value of parameter
     * (Note - zero-length value String indicates flag parameter)
     * @param <var>name</var> name of parameter
     * @param <var>value</var> value of parameter
     * @throws IllegalArgumentException if name or value is null
     * @throws SipParseException if name or value is not accepted by implementation
     */
    public void setParameter(String name, String value) 
     throws IllegalArgumentException,SipParseException{
        Route route = (Route)sipHeader;
         if (name == null || value == null) 
                throw new IllegalArgumentException
                    ("JAIN-EXCEPTION: Name or value is null!");
        route.setParameter(name,value);
    }
    
    /** Return true if this route header has any parameters.
    * @return true if this route header has any parameters.
    */
    public boolean hasParameters() {
        Route route = (Route) sipHeader;
        return route.hasParameters();
    }
    
   /**
     * Gets boolean value to indicate if Parameters
     * has specified parameter
     * @return boolean value to indicate if Parameters
     * has specified parameter
     * @throws IllegalArgumentException if name is null
     */ 
    public boolean hasParameter(String parmName) 
      throws IllegalArgumentException {
        Route route  = (Route) sipHeader;
         if(parmName == null) throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
       
        return route.hasParameter( parmName);
    }
    
    
     /**
     * Removes specified parameter from Parameters (if it exists)
     * @param <var>name</var> name of parameter
     * @throws IllegalArgumentException if parameter is null
     */  
    public void removeParameter(String paramName)
      throws IllegalArgumentException{
        Route route = (Route)sipHeader;
          if(paramName == null) throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
        route.removeParameter(paramName);
    }
    
    /**
     *Remove all parameters.
     */
    public void removeParameters() {
        Route route = (Route)sipHeader;
        route.removeParameters();
    }
   
     
    /**
     * Gets Iterator of parameter names
     * (Note - objects returned by Iterator are Strings)
     * (Returns null if no parameters exist)
     * @return Iterator of parameter names
     */  
    public Iterator getParameters() {
        Route route = (Route)sipHeader;
    
         Iterator iterator=       route.getParameters();
         if ( iterator==null) return null;
         if ( iterator.hasNext() ) return iterator;
         else return null;  
    }
   
}






