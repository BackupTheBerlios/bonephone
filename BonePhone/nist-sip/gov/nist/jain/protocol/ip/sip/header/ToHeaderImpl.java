/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: O. Deruelle (deruelle@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;
import jain.protocol.ip.sip.address.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.jain.protocol.ip.sip.address.*;
import gov.nist.sip.msgparser.*;
import java.util.Iterator;

/**
* Implementation of the ToHeader interface of jain-sip.
*/
public final class ToHeaderImpl  extends HeaderImpl
implements ToHeader,NistSIPHeaderMapping {

   
    /** Default constructor
     */    
    public ToHeaderImpl() { 
        super();
            this.headerName = name;
    }

    /** constructor
     * @param to To to set
     */    
    public ToHeaderImpl(To to) { 
        super(to);
            this.headerName = name;
    }
   
    /**
    * Gets tag of EndPointHeader
    * (Returns null if tag does not exist)
    * @return tag of EndPointHeader
    */
    public String getTag() {
            To to=(To)sipHeader;
            return to.getTag();
    }

    
    /**
    * Gets boolean value to indicate if EndPointHeader
    * has tag
    * @return boolean value to indicate if EndPointHeader
    * has tag
    */
    public boolean hasTag() {
         To to=(To)sipHeader;
         return to.hasTag();
    }

    
    /**
     * Sets tag of EndPointHeader
     * @param tag String to set
     * @throws IllegalArgumentException if tag is null
     * @throws SipParseException if tag is not accepted by implementation
     */
    public void setTag(String tag) throws IllegalArgumentException,
    SipParseException {
            To to=(To)sipHeader;
            if (tag==null)
                    throw new IllegalArgumentException
                    ("JAIN-SIP EXCEPTION: tag is null");
            else  to.setTag(tag);
    }

    
    /**
    * Removes tag to EndPointHeader (if it exists)
    */
    public void removeTag() {
         To to=(To)sipHeader;
         to.removeTag();
    }
    
 
    /**
    * Gets NameAddress of NameAddressHeader
    * (Returns null if NameAddress does not exist - i.e. wildcard ContactHeader)
    * @return NameAddress of NameAddressHeader
    */
    public NameAddress getNameAddress() {
       To to=(To)sipHeader;
       Address addr=to.getAddress();
       if ( addr==null) return null;
       else{
            NameAddressImpl nai=new NameAddressImpl();
            nai.setImplementationObject(addr);
            return nai;
       }
    }

    
    /**
     * Sets NameAddress of NameAddressHeader
     * @param nameAddress NameAddress to set
     * @throws IllegalArgumentException if nameAddress is null or not to the
     * same JAIN SIP implementation
     */
    public void setNameAddress(NameAddress nameAddress) 
    throws IllegalArgumentException {
        To to=(To)sipHeader;
        if ( nameAddress==null )
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION : nameAddress is null");
        else 
            if (nameAddress instanceof NameAddressImpl) {
               NameAddressImpl nai=(NameAddressImpl)nameAddress; 
               to.setAddress(nai.getImplementationObject());
            }
             else
                 throw new IllegalArgumentException
               ("nameAddress is not to the same JAIN-SIP implementation"); 
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
        To to = (To) sipHeader;
      
         if (name == null) throw new
                IllegalArgumentException("JAIN-EXCEPTION: null argument");
        return (String) to.getParameter(name);
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
    throws IllegalArgumentException, SipParseException{
        To to = (To)sipHeader;
       
         if (name == null || value == null) {
                throw new 
                    IllegalArgumentException
                    ("JAIN-EXCEPTION: Name or value is null!");
                }
            else
        to.setParameter(name,value);
    }
    
    /**
     * Return true if the to header has any parameters.
     * @return True if we have any parameters false otherwise
     */
    public boolean hasParameters() {
       
        To to = (To) sipHeader;
     
        return to.hasParameters() ;
    }
    
   /**
     * Gets boolean value to indicate if Parameters
     * has specified parameter
     * @return boolean value to indicate if Parameters
     * has specified parameter
     * @param name String to set
     * @throws IllegalArgumentException if name is null
     */
    public boolean hasParameter(String name)  throws IllegalArgumentException{
        To to = (To) sipHeader;
      
         if(name == null) throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
        return to.hasParameter(name);
    }
    
    /**
     * Removes specified parameter from Parameters (if it exists)
     * @param name String to set
     * @throws IllegalArgumentException if parameter is null
     */
    public void removeParameter(String name) 
     throws IllegalArgumentException{
       
        To to = (To) sipHeader;
         if(name==null)
                throw new IllegalArgumentException
                                        ("JAIN-EXCEPTION : name is null");
        to.removeParameter(name);
    }
    
    /**
     * Remove all parameters.
     */
    public void removeParameters() {
        To to = (To)sipHeader;
      
        to.removeParameters();
    }
    
   /**
    * Gets Iterator of parameter names
    * (Note - objects returned by Iterator are Strings)
    * (Returns null if no parameters exist)
    * @return Iterator of parameter names
    */ 
    public Iterator getParameters() {
        To to = (To)sipHeader;
      
         Iterator iterator=  to.getParmNames();
         if ( iterator==null) { 
            
             return null;
         }
         if ( iterator.hasNext() ) {
             
             return iterator;}
         else {
           
             return null;
         }
    }    
    
}



