/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: C. Chazeau (christophe.chazeau@nist.gov)                            *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.address.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.log.*;
import gov.nist.sip.net.*;
import gov.nist.jain.protocol.ip.sip.*;
import gov.nist.jain.protocol.ip.sip.address.*;
import gov.nist.sip.msgparser.*;

import java.util.Iterator ;
import java.util.Date;


/**
 * Implementation of the ContactHeader interface of jain-sip.
 * Builds a wapper around the NIST-SIP Contact class.
 *@see gov.nist.sip.Contact
 */
public final class ContactHeaderImpl
extends HeaderImpl implements
ContactHeader,NistSIPHeaderMapping {
    
    /** Default constructor
     */    
    public ContactHeaderImpl() { 
        super();
      this.headerName = name;
    }

    /** constructor
     * @param contact Contact to set
     */    
    public ContactHeaderImpl(Contact contact) { 
        super(contact);
      this.headerName = name;
    }

   /**
    * Gets Iterator of parameter names
    * (Note - objects returned by Iterator are Strings)
    * (Returns null if no parameters exist)
    * @return Iterator of parameter names
    */
    public Iterator getParameters() {
        Contact contact = (Contact) sipHeader;
          Iterator iterator=contact.getParameters();
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
        Contact contact = (Contact) sipHeader;
        if (name == null) throw new
              IllegalArgumentException("JAIN-EXCEPTION: null argument");
        return (String) contact.getParameter(name);
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
        Contact contact = (Contact) sipHeader;
        if (name == null || value == null) {
            throw new
            IllegalArgumentException
            ("JAIN-EXCEPTION: Name or value is null!");
        } else  {
	   if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
	      LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setParameter " +
			name + " value = " + value);
	    contact.setParameter(name,value);
	}
    }
    
    /**
     * Gets boolean value to indicate if Parameters
     * has any parameters
     * @return boolean value to indicate if Parameters
     * has any parameters
     */
    public boolean hasParameters() {
        Contact contact = (Contact) sipHeader;
        return contact.hasParameters();
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
        Contact contact = (Contact) sipHeader;
        if(name == null) throw new IllegalArgumentException
        ("JAIN-EXCEPTION: null parameter");
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
	  LogWriter.logMessage(LogWriter.TRACE_DEBUG,"hasParameter " +
			name);
        return contact.hasParameter(name);
    }
    
    /**
     * Removes specified parameter from Parameters (if it exists)
     * @param name String to set
     * @throws IllegalArgumentException if parameter is null
     */
    public void removeParameter(String name) throws IllegalArgumentException {
        Contact contact = (Contact) sipHeader;
	
        if ( name==null ) 
            throw new IllegalArgumentException("JAIN-EXCEPTION: name is null");
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
	  LogWriter.logMessage(LogWriter.TRACE_DEBUG,"removeParameter " +
			name);
        contact.removeParameter(name);
    }
    
    /**
     * Removes all parameters from Parameters (if any exist)
     */
    public void removeParameters() {
        Contact contact = (Contact) sipHeader;
        contact.removeParameters();
    }
    
    
    /**
     * Gets NameAddress of NameAddressHeader
     * (Returns null if NameAddress does not exist - i.e.
     * 	wildcard ContactHeader)
     * @return NameAddress of NameAddressHeader
     */
    public NameAddress getNameAddress(){
        Contact contact = (Contact) sipHeader;
        Address addr=contact.getAddress();
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
     * @throws IllegalArgumentException if nameAddress is null or not from the
     * same JAIN SIP implementation
     */
    public void setNameAddress(NameAddress nameAddress)
    throws IllegalArgumentException{
        Contact contact = (Contact) sipHeader;
        if ( nameAddress==null )
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION : nameAddress is null");
        else
            if (nameAddress instanceof NameAddressImpl)
            {
                NameAddressImpl nai=(NameAddressImpl)nameAddress;
                contact.setAddress(nai.getImplementationObject());
            } else
                throw new IllegalArgumentException
                ("nameAddress is not from same JAIN-SIP implementation");
    }
    
    /**
     * Returns boolean value indicating whether ContactHeader is a wild card
     * @return boolean value indicating whether ContactHeader is a wild card
     */
    public boolean isWildCard(){
        Contact contact = (Contact) sipHeader;
        return contact.getWildCardFlag() ;
    }
    
   /**
    * Sets ContactHeader to wild card (replaces NameAddress with "*")
    */
    public void setWildCard(){
        Contact contact = (Contact) sipHeader;
        contact.setWildCardFlag(true) ;
    }
    
  /**
   * Gets comment of ContactHeader
   * (Returns null if comment does not exist)
   * @return comment of ContactHeader
   */
    public String getComment(){
        Contact contact = (Contact) sipHeader;
        return contact.getComment() ;
    }
    
    /**
     * Gets boolean value to indicate if ContactHeader
     * has comment
     * @return boolean value to indicate if ContactHeader
     * has comment
     */
    public boolean hasComment(){
        Contact contact = (Contact) sipHeader;
        return contact.hasComment();
    }
    
   /**
    * Sets comment of ContactHeader
    * @param comment String to set
    * @throws IllegalArgumentException if comment is null
    * @throws SipParseException if comment is not accepted by implementation
    */
    public void setComment(String comment)
    throws IllegalArgumentException, SipParseException{
        Contact contact = (Contact) sipHeader;      
        if (comment == null) throw new IllegalArgumentException
            ("JAIN-EXCEPTION: comment null !!");
        contact.setComment(comment) ;
    }
    
   /**
    * Removes comment from ContactHeader (if it exists)
    */
    public void removeComment(){
        Contact contact = (Contact) sipHeader;
        contact.removeComment() ;
    }
    
    /**
     * Gets q-value of ContactHeader
     * (Returns negative float if comment does not exist)
     * @return q-value of ContactHeader
     */
    public float getQValue(){
        Contact contact = (Contact) sipHeader;
        return contact.getQValue() ;
    }
    
    /**
     * Gets boolean value to indicate if ContactHeader
     * has q-value
     * @return boolean value to indicate if ContactHeader
     * has q-value
     */
    public boolean hasQValue(){
        Contact contact = (Contact) sipHeader;
        return contact.hasQValue() ;
    }
    
    /**
     * Sets q-value of ContactHeader
     * @param qValue float to set
     * @throws SipParseException if qValue is not accepted by implementation
     */
    public  void setQValue(float qValue) throws SipParseException {
        Contact contact = (Contact) sipHeader;
        contact.setQValue(qValue) ;
    }
    
   /**
    * Removes q-value from ContactHeader (if it exists)
    */
    public void removeQValue(){
        Contact contact = (Contact) sipHeader;
        contact.removeQValue() ;
    }
    
    /**
     * Sets action of ContactHeader
     * @param action String to set
     * @throws IllegalArgumentException if action is null
     * @throws SipParseException if action is not accepted by implementation
     */
    public void setAction(String action)
    throws IllegalArgumentException, SipParseException{
        Contact contact = (Contact) sipHeader;
        if(action == null)
            throw new IllegalArgumentException
            ("setAction : action is null") ;  
        contact.setAction(action) ;
    }
    
    /**
     * Gets boolean value to indicate if ContactHeader
     * has action
     * @return boolean value to indicate if ContactHeader
     * has action
     */
    public boolean hasAction(){
        Contact contact = (Contact) sipHeader;
        return contact.hasAction();
    }
    
    /** remove the Action field
     */    
    public void removeAction(){
        Contact contact = (Contact) sipHeader;
        contact.removeAction() ;
    }
    
    /** get the action field
     * @return String
     */    
    public String getAction(){
        Contact contact = (Contact) sipHeader;
        return contact.getAction() ;
    }
    
   /**
    * Sets expires of ContactHeader to a number of delta-seconds
    * @param expiryDeltaSeconds long to set
    * @throws SipParseException if expiryDeltaSeconds 
    * 	is not accepted by implementation
    */
    public void setExpires(long expiryDeltaSeconds) throws SipParseException{
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setExpires " +
			expiryDeltaSeconds);
	if (expiryDeltaSeconds < 0 ) 
		throw new SipParseException("value not legal");
        Contact contact = (Contact) sipHeader;
        contact.setExpires(expiryDeltaSeconds) ;
    }
    
   /**
    * Sets expires of ContactHeader to a date
    * @param expiryDate Date to set
    * @throws IllegalArgumentException if expiryDate is null
    * @throws SipParseException if expiryDate is not accepted by implementation
    */
    public void setExpires(Date expiryDate) 
    throws IllegalArgumentException, SipParseException{
        Contact contact = (Contact) sipHeader;
        if(expiryDate == null)
            throw new IllegalArgumentException
		("setExpires date parameter is null") ;
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
	  LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setExpires " +
			expiryDate.toString());
        contact.setExpires(expiryDate) ;
    }
    
   /**
    * Gets expires as delta-seconds of ContactHeader
    * (returns negative long if expires does not exist)
    * @return expires as delta-seconds of ContactHeader
    */
    public long getExpiresAsDeltaSeconds(){
        Contact contact = (Contact) sipHeader;   
        SIPDateOrDeltaSeconds date ;
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"getExpiresAsDeltaSeconds() ");
        date = contact.getExpires() ;
        if (date==null) return -1;
	long retval;
        if (date.isSIPDate()) {
            long exptime =  ((SIPDate) date).getJavaCal().getTime().getTime();
	    long currentTime = new Date().getTime();
	    retval = (exptime - currentTime)/1000;
        } else {
            retval =  ((DeltaSeconds) date).getDeltaSeconds() ;
	}
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"getExpiresAsDeltaSeconds() " + retval);
	return retval;
	
    }
    
   /**
    * Gets expires as date of ContactHeader
    * (Returns null if expires value does not exist)
    * @return expires as date of ContactHeader
    */
    public Date getExpiresAsDate(){
        LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"getExpiresAsDate() ");
        Contact contact = (Contact) sipHeader;
        SIPDateOrDeltaSeconds date ;
        date = contact.getExpires() ;
        if (date==null) return null;
	Date retval;
        if(date.isSIPDate()) {
            retval =  ((SIPDate) date).getJavaCal().getTime() ; 
        } else  {
            retval =  new Date(
		new Date().getTime() + 
		((DeltaSeconds) date).getDeltaSeconds() * 1000) ;
	}
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
	  LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"getExpiresAsDate() " + retval.toString());
	return retval;
	
    }
    
   /**
    * Gets boolean value to indicate if ContactHeader
    * has expires
    * @return boolean value to indicate if ContactHeader
    * has expires
    */
    public boolean hasExpires(){
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"hasExipires() ");
        Contact contact = (Contact) sipHeader;
        return contact.hasExpires() ;
    }
    
   /**
    * Removes expires from ContactHeader (if it exists)
    */
    public void removeExpires(){
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"removeExipires() ");
        Contact contact = (Contact) sipHeader;
        contact.removeExpires() ;
    }
    
}
