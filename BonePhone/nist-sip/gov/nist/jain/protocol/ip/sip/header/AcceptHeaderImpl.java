/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: M. Ranganathan (mranga@nist.gov)                                    *
* Modified by: O. Deruelle (deruelle@nist.gov)                                 *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.log.*;

import java.util.Iterator;

/**
* Implementation of the AcceptHeader interface of jain-sip.
*/
public final class AcceptHeaderImpl  extends HeaderImpl
implements AcceptHeader, NistSIPHeaderMapping {
        
    /** Default constructor
     */    
    public AcceptHeaderImpl() { 
        super();
      this.headerName = name;
    }

    /** constructor
     * @param accept Accept to set
     */    
    public AcceptHeaderImpl(Accept accept) { 
        super(accept);
      this.headerName = name;
    }
    
    /**
    * Gets boolean value to indicate if the AcceptHeader
    * allows all media types (i.e. content type is "*")
    * @return boolean value to indicate if the AcceptHeader
    * allows all media types
    */
    public boolean allowsAllContentTypes() { 
      
	Accept accept = (Accept) sipHeader;
	return accept.allowsAllContentTypes(); 
    }

   /**
    * Gets boolean value to indicate if the AcceptHeader
    * allows all media sub-types (i.e. content sub-type is "*")
    * @return boolean value to indicate if the AcceptHeader
    * allows all media sub-types
    */   
    public boolean allowsAllContentSubTypes() { 
        
	Accept accept = (Accept) sipHeader; 
	return accept.allowsAllContentSubtypes(); 
    }

   /**
    * Sets q-value for media-range in AcceptHeader
    * Q-values allow the user to indicate the relative degree of
    * preference for that media-range, using the qvalue scale from 0 to 1.
    * (If no q-value is present, the media-range 
    * should be treated as having a q-value of 1.)
    * @param qValue float to set
    * @throws SipParseException if qValue is not accepted by implementation
    */
    public void setQValue(float qValue) throws SipParseException {
        
	Accept accept = (Accept) sipHeader;
        if (qValue < 0.0) throw new SipParseException
            ("JAIN-EXCEPTION: Invalid Q Value < 0");
        else if (qValue > 1.0) 
             throw new SipParseException
             ("JAIN-EXCEPTION: Invalid Q value > 1.0");
        try {
            accept.setQValue(qValue);
        } 
        catch (Exception ex) {
            throw new SipParseException("Invalid q value");
        }
    }

   /**
    * Gets q-value of media-range in AcceptHeader
    * (Returns negative float if no q-value exists)
    * @return q-value of media-range
    */
    public float getQValue() {
         
	Accept accept = (Accept)sipHeader;
        return (float) accept.getQValue();
    }

    /**
    * Gets boolean value to indicate if AcceptHeader
    * has q-value
    * @return boolean value to indicate if AcceptHeader
    * has q-value
    */
    public boolean hasQValue() {
        
	Accept accept = (Accept)sipHeader; 
        return accept.hasQValue();
    }

    /**
    * Removes q-value of media-range in AcceptHeader (if it exists)
    */
    public void removeQValue() {
       
	Accept accept = (Accept)sipHeader;
        accept.removeQValue();
    }

    /**
     * Gets media type of ContentTypeHeader
     * @return media type of ContentTypeHeader
    */
    public String getContentType() {
        
	Accept accept = (Accept)sipHeader;
        return accept.getContentType();
    }
    
    /**
     * Gets media sub-type of ContentTypeHeader
     * @return media sub-type of ContentTypeHeader
     */
    public String getContentSubType() {
       
	Accept accept = (Accept)sipHeader;
        return accept.getContentSubType();
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
       
	Accept accept = (Accept)sipHeader;
        if (contentSubType == null) 
            throw new IllegalArgumentException("null content sub-type!");
        
        accept.setContentSubType(contentSubType);  
    }
    
    /**
     * Sets value of media type in ContentTypeHeader
     * @param contentType String to set
     * @throws IllegalArgumentException if type is null
     * @throws SipParseException if contentType is not 
     * accepted by implementation
     */
    public void setContentType(String contentType) 
    throws IllegalArgumentException, SipParseException {
       
	Accept accept = (Accept)sipHeader;
        if (contentType == null) 
            throw new IllegalArgumentException("null content type!");
        accept.setContentType(contentType);
    }
    
    
    /**
     * Gets Iterator of parameter names
     * (Note - objects returned by Iterator are Strings)
     * (Returns null if no parameters exist)
     * @return Iterator of parameter names
     */
    public Iterator getParameters() {
       
       Accept accept = (Accept)sipHeader;
       Iterator iterator=accept.getParameters();
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
        Accept accept = (Accept)sipHeader;
        if (name == null) throw new
            IllegalArgumentException("JAIN-EXCEPTION: null argument");
        return (String) accept.getParameter(name);
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
    public void setParameter(String name,String value) throws  
        IllegalArgumentException, SipParseException {
            
            Accept accept = (Accept)sipHeader;
            if (name == null || value == null) 
                throw new IllegalArgumentException
                    ("JAIN-EXCEPTION: Name or value is null!");
            else accept.setParameter(name,value);
    }
    
    /**
     * Gets boolean value to indicate if Parameters
     * has any parameters
     * @return boolean value to indicate if Parameters
     * has any parameters
     */
    public boolean hasParameters() {
         
        Accept accept = (Accept)sipHeader;
        return accept.hasParameters();
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
       
        Accept accept = (Accept)sipHeader;
        if(name == null) throw new IllegalArgumentException
            ("JAIN-EXCEPTION: null parameter");
        return accept.hasParameter(name);
    }
    
    /**
     * Removes specified parameter from Parameters (if it exists)
     * @param name String to set
     * @throws IllegalArgumentException if parameter is null
     */
    public void removeParameter(String name) throws IllegalArgumentException {
        Accept accept = (Accept)sipHeader;
        if( name == null) {
            throw new IllegalArgumentException
                ("JAIN-EXCEPTION : parameter is null");
        }
        accept.removeParameter(name);
    }
    
    /**
     * Removes all parameters from Parameters (if any exist)
    */
    public void removeParameters() {
        
        Accept accept = (Accept)sipHeader;
        accept.removeParameters();
    }
    
}
