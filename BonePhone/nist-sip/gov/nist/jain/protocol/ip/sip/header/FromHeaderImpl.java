/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: O. Deruelle (deruelle@nist.gov)                                     *
* Modified by : M. Ranganathan (mranga@Nist.gov) 			       *
* 	Changed the inheritance hierarchy.				       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.address.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.jain.protocol.ip.sip.address.*;
import gov.nist.sip.msgparser.*;

import java.util.Iterator;

/**
* Implementation of the FromHeader interface of jain-sip.
*/
public final class FromHeaderImpl  extends HeaderImpl
implements FromHeader,NistSIPHeaderMapping {

   
    /** Default constructor
     */    
   public FromHeaderImpl() { 
       super(); 
      this.headerName = name;
   }

    /** constructor
     * @param from From to set
     */   
   public FromHeaderImpl(From from) { 
       super(from); 
      this.headerName = name;
   }   
    
        /**
         * Gets tag of EndPointHeader
         * (Returns null if tag does not exist)
         * @return tag of EndPointHeader
         */
    public String getTag() {
        
        From from=(From)sipHeader;
     
        if (hasTag())
             return from.getTag(); 
        else return null;
    }
    
    /**
     * Gets boolean value to indicate if EndPointHeader
     * has tag
     * @return boolean value to indicate if EndPointHeader
     * has tag
     */
    public boolean hasTag() {
        
        From from=(From)sipHeader;
        return from.hasTag();
    }
    
    /**
     * Sets tag of EndPointHeader
     * @param tag Tag to set
     * @throws IllegalArgumentException if tag is null
     * @throws SipParseException if tag is not accepted by implementation
     */
    public void setTag(String tag) 
    throws IllegalArgumentException, SipParseException {
        
        From from=(From)sipHeader;
        if( tag==null)
           throw new IllegalArgumentException
           ("JAIN-SIP EXCEPTION : tag is null");
        else from.setTag(tag);
    }
    
    /**
     * Removes tag from EndPointHeader (if it exists)
     */
    public void removeTag() {
       
        From from=(From)sipHeader;
        from.removeTag();
    }   
    
    
    /**
    * Gets NameAddress of NameAddressHeader
    * (Returns null if NameAddress does not exist - i.e. wildcard ContactHeader)
    * @return NameAddress of NameAddressHeader
    */
    public NameAddress getNameAddress() {
      
       From from=(From)sipHeader;
        
       Address addr=from.getAddress();
       if ( addr==null) return null;
       else{
            NameAddressImpl nai=new NameAddressImpl();
            nai.setImplementationObject(addr);
            return nai;
       }
    }

    
    /**
     * Sets NameAddress of From Header
     * @param nameAddress nameAddress to set.
     * @throws IllegalArgumentException if nameAddress is null or not from the
     * same JAIN SIP implementation
     */
    public void setNameAddress(NameAddress nameAddress) 
    throws IllegalArgumentException {
         
        From from=(From)sipHeader;
         
        if ( nameAddress==null )
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION : nameAddress is null");
        else 
            if (nameAddress instanceof NameAddressImpl)
            {
               NameAddressImpl nai=(NameAddressImpl)nameAddress; 
               from.setAddress(nai.getImplementationObject());
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
    public String getParameter(String name) 
      throws IllegalArgumentException{
       
        From from = (From) sipHeader;
         
     
        if (name == null) { 
            throw new
                IllegalArgumentException("JAIN-EXCEPTION: null argument");
           
        }
        String s=(String) from.getParameter(name);
     
            return s;
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
        
        From from = (From)sipHeader;
       
          if (name == null || value == null) 
                throw new IllegalArgumentException
                    ("JAIN-EXCEPTION: Name or value is null!");
        from.setParameter(name,value);
    }
    
    /**
     * Return true if the from header has any parameters.
     * @return True if we have any parameters false otherwise
     */
    public boolean hasParameters() {
        
        From from = (From) sipHeader;
        
        return from.hasParameters();
    }
    
   /**
     * Gets boolean value to indicate if Parameters
     * has specified parameter
     * @return boolean value to indicate if Parameters
     * has specified parameter
     * @throws IllegalArgumentException if name is null
     */
    public boolean hasParameter(String paramName)
    throws IllegalArgumentException {
      
        From from = (From) sipHeader;
        if(paramName == null) throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
       
        return from.hasParameter(paramName);
    }
    
     /**
     * Removes specified parameter from Parameters (if it exists)
     * @param <var>name</var> name of parameter
     * @throws IllegalArgumentException if parameter is null
     */
    public void removeParameter(String parmName)
     throws IllegalArgumentException{
         
        From from = (From) sipHeader;
      
      if(parmName == null) throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
        from.removeParameter(parmName);
    }
    
    /**
     *Remove all parameters.
     */
    public void removeParameters() {
       
        From from = (From)sipHeader;
       
        from.removeParameters();
    }
    
    /**
     * Gets Iterator of parameter names
     * (Note - objects returned by Iterator are Strings)
     * (Returns null if no parameters exist)
     * @return Iterator of parameter names
     */
    public Iterator getParameters() {
       
        From from = (From)sipHeader;
      
         Iterator iterator=   from.getParmNames();
         if ( iterator==null) return null;
         if ( iterator.hasNext() ) return iterator;
         else return null;  
    }  
    
}


