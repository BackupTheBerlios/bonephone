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

/**
* Implementation of the RecordRouteHeader interface of jain-sip.
*/
public final class RecordRouteHeaderImpl  extends HeaderImpl
implements RecordRouteHeader,NistSIPHeaderMapping {

    /** Default constructor
     */    
   public RecordRouteHeaderImpl() { 
       super();
            this.headerName = name;
   }

    /** constructor
     * @param rr RecordRoute to set
     */   
   public RecordRouteHeaderImpl(RecordRoute rr) { 
       super(rr);
            this.headerName = name;
   }
    
    /**
    * Gets NameAddress of NameAddressHeader
    * (Returns null if NameAddress does not exist - i.e. wildcard ContactHeader)
    * @return NameAddress of NameAddressHeader
    */
    public NameAddress getNameAddress() {
       RecordRoute recordRoute=(RecordRoute)sipHeader;
       Address addr=recordRoute.getAddress();
       if ( addr==null) return null;
       else {
            NameAddressImpl nai=new NameAddressImpl();
            nai.setImplementationObject(addr);
            return nai;
       }
    }

    
    /**
     * Sets NameAddress of NameAddressHeader
     * @param nameAddress NameAddress to set
     * @throws IllegalArgumentException if nameAddress is null or not from the
     * same JAIN SIP implementation
     */
    public void setNameAddress(NameAddress nameAddress) 
    throws IllegalArgumentException {
        RecordRoute recordRoute=(RecordRoute)sipHeader;
        if ( nameAddress==null )
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION : nameAddress is null");
        else 
            if (nameAddress instanceof NameAddressImpl) {
               NameAddressImpl nai=(NameAddressImpl)nameAddress; 
               recordRoute.setAddress(nai.getImplementationObject());
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
    public String getParameter(String name ) 
      throws IllegalArgumentException{	
	RecordRoute recordRoute = (RecordRoute)sipHeader;
          if (name == null) { 
            throw new
                IllegalArgumentException("JAIN-EXCEPTION: null argument");
        }
	return recordRoute.getParameter(name);
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
	RecordRoute recordRoute = (RecordRoute)sipHeader;
           if (name == null || value == null) 
                throw new IllegalArgumentException
                    ("JAIN-EXCEPTION: Name or value is null!");
	recordRoute.setParameter(name,value);
    }

    /** return true if this header has parameters.
     * @return boolean
     */
    public boolean hasParameters() {
	RecordRoute recordRoute = (RecordRoute)sipHeader;
	return recordRoute.hasParameters();
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
	RecordRoute recordRoute = (RecordRoute)sipHeader;
          if(parmName == null) throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
       
	return recordRoute.hasParameter(parmName);
     }
     
     /**
     * Gets Iterator of parameter names
     * (Note - objects returned by Iterator are Strings)
     * (Returns null if no parameters exist)
     * @return Iterator of parameter names
     */
     public Iterator getParameters() {
         RecordRoute recordRoute = (RecordRoute) sipHeader;
         
          Iterator iterator=   recordRoute.getParamNames();
         if ( iterator==null) return null;
         if ( iterator.hasNext() ) return iterator;
         else return null;  
     }
	
      /**
     * Removes specified parameter from Parameters (if it exists)
     * @param <var>name</var> name of parameter
     * @throws IllegalArgumentException if parameter is null
     */
     public void removeParameter(String paramName)
     throws IllegalArgumentException{
         RecordRoute recordRoute = (RecordRoute) sipHeader;
          if(paramName == null) throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
         recordRoute.removeParameter(paramName);
     }
     
    /** remove all Parameters
     */     
     public void removeParameters() {
         RecordRoute recordRoute = (RecordRoute) sipHeader;
         recordRoute.removeParameters();
     }
     
}



