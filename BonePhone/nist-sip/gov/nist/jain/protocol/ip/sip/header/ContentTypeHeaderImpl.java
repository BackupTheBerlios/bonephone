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
import gov.nist.log.*;
import gov.nist.sip.msgparser.*;

import java.util.*;


/**
* Implementation of the ContentTypeHeader interface of jain-sip.
*/
public final class ContentTypeHeaderImpl  extends HeaderImpl
implements ContentTypeHeader, NistSIPHeaderMapping {
    
    /** Default constructor
     */    
   public ContentTypeHeaderImpl() { 
       super();
      this.headerName = name;
   }

    /** constructor
     * @param ctype ContentType to set
     */   
   public ContentTypeHeaderImpl(ContentType ctype) { 
       super(ctype);
      this.headerName = name;
   }

     /**
      * Gets media type of ContentTypeHeader
      * @return media type of ContentTypeHeader
      */
    public String getContentType() {
        ContentType contentType=(ContentType)sipHeader;
        return contentType.getContentType(); 
    }
    
    /**
     * Gets media sub-type of ContentTypeHeader
     * @return media sub-type of ContentTypeHeader
     */
    public String getContentSubType() {
        ContentType contentType=(ContentType)sipHeader;
        return contentType.getContentSubType(); 
    }
    
   /**
    * Sets value of media subtype in ContentTypeHeader
    * @param contentSubType String to set
    * @throws IllegalArgumentException if sub-type is null
    * @throws SipParseException if contentSubType is not 
    * accepted by implementation
    */
    public void setContentSubType(String contentSubType)
    throws IllegalArgumentException, SipParseException {
         ContentType contentType=(ContentType)sipHeader;
         if (contentSubType ==null)
                    throw new IllegalArgumentException
                    ("JAIN-SIP EXCEPTION : contentSubType is null");
         else  contentType.setContentSubType(contentSubType);          
    }
    
   /**
    * Sets value of media type in ContentTypeHeader
    * @param ContentType String to set
    * @throws IllegalArgumentException if type is null
    * @throws SipParseException if contentType is not accepted by implementation
    */
    public void setContentType(String ContentType) 
    throws IllegalArgumentException, SipParseException {
         ContentType contentType=(ContentType)sipHeader;
         if (ContentType ==null)
                    throw new IllegalArgumentException
                    ("JAIN-SIP EXCEPTION : type is null");
         else  contentType.setContentType(ContentType);          
    }
    
    
    /**
    * Gets Iterator of parameter names
    * (Note - objects returned by Iterator are Strings)
    * (Returns null if no parameters exist)
    * @return Iterator of parameter names
    */
    public Iterator getParameters() {
        ContentType contentType=(ContentType)sipHeader;
        Iterator iterator= contentType.getParameters();
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
         ContentType contentType=(ContentType)sipHeader;
         if (name == null) throw new
                IllegalArgumentException("JAIN-EXCEPTION: null argument");
         else return contentType.getParameter(name);
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
         ContentType contentType=(ContentType)sipHeader;
         if (name == null || value == null) 
                throw new IllegalArgumentException
                    ("JAIN-EXCEPTION: Name or value is null!");
         else contentType.setParameter(name,value);
    }
         
    
    /**
    * Gets boolean value to indicate if Parameters
    * has any parameters
    * @return boolean value to indicate if Parameters
    * has any parameters
    */
    public boolean hasParameters() { 
        ContentType contentType=(ContentType)sipHeader;
        return contentType.hasParameters();           
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
         ContentType contentType=(ContentType)sipHeader;
         if(name == null) throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
         else  return contentType.hasParameter(name);
    }

    
    /**
     * Removes specified parameter from Parameters (if it exists)
     * @param name String to set
     * @throws IllegalArgumentException if parameter is null
     */
    public void removeParameter(String name) throws IllegalArgumentException {
         ContentType contentType=(ContentType)sipHeader;
         if (name==null )
                throw new IllegalArgumentException
                ("JAIN-EXCEPTION : parameter is null");
         else  contentType.removeParameter(name);
    }

    
    /**
    * Removes all parameters from Parameters (if any exist)
    */
    public void removeParameters() {
         ContentType contentType=(ContentType)sipHeader;
         contentType.removeParameters();   
    }
      
}








