/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan, Christophe Chazeau 			               *
* (mranga@nist.gov, chazeau@antd.nist.gov)  Created on April 18, 2001, 2:26 PM *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/


package gov.nist.jain.protocol.ip.sip.message;

import  gov.nist.sip.msgparser.*;
import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.PackageNames;
import  gov.nist.jain.protocol.ip.sip.header.*;
import  gov.nist.sip.PackageNames;
import  gov.nist.log.LogWriter;
import  gov.nist.sip.sipheaders.*;

import  jain.protocol.ip.sip.message.*;
import  jain.protocol.ip.sip.header.*;
import  jain.protocol.ip.sip.*;

import  java.util.List;
import  java.util.Hashtable;
import  java.util.Iterator;
import  java.util.ListIterator;
import  java.util.LinkedList;
import  java.lang.reflect.*;
import  java.util.NoSuchElementException;
import  java.io.*;

/**
 *
 * @author  M. Ranganathan (mranga@nist.gov)
 * @version 1.0
 */
public class MessageImpl implements Message, NistJAINMessage {
    
    protected SIPMessage sipMessage;


    public MessageImpl() {
		sipMessage = null;
    }

     
     /** Creates a new MessageImpl from the given string. (Parses it
      * and initializes the internal structures).
      *@parm sip_message is the string representation of the sip message.
      */
     public MessageImpl(String sip_message) throws SipParseException {
         StringMsgParser smp = new StringMsgParser();
         try {
            SIPMessage messages[]  = smp.parseSIPMessage(sip_message);
            sipMessage = messages[0];
         } catch (SIPParseException ex) {
	     if (LogWriter.needsLogging()) 
		 LogWriter.logException(ex);
             throw new SipParseException("Bad message! " + 
				ex.getMessage() + ":" + 
				ex.getText());
         }
     }

    /** Creates new MessageImpl 
     *@param <var> sip_message </var> SIPMessage structure from which this 
     * is created (note -- this is not part of the JAIN implementation).
     */
    public MessageImpl(SIPMessage sip_message) {
        sipMessage = sip_message;
    }

    /**
     * Adds list of Headers to top/bottom of Message's header list.
     * Note that the Headers are added in same order as in List.
     * @param <var>headers</var> List of Headers to be added
     * @param <var>top</var> indicates if Headers are to be added at top/bottom
     * of Message's header list
     * @throws IllegalArgumentException if headers is null, empty, contains any
     * null objects, or contains any objects that are not Header objects from
     * the same JAIN SIP implementation as this Message
     */
    public void addHeaders(String headerName,List headers,boolean top) 
        throws IllegalArgumentException {

        if (headerName == null || headers == null || headers.isEmpty() )
        	throw new IllegalArgumentException
		("setHeaders : headerName or header null or empty");
        Iterator it = headers.iterator();
        NistSIPHeaderMapping map;
        while (it.hasNext()) {
            map = (NistSIPHeaderMapping)it.next();
            SIPHeader sipheader = map.getImplementationObject();
            try {
                if (sipheader != null) 
			sipMessage.attachHeader(sipheader,false,top);
            } catch (Exception ex) {
		if (LogWriter.needsLogging()) 
		    LogWriter.logException(ex);
                throw new IllegalArgumentException("Duplicate header");
            } 
        }
            
    }
    
    /**
     * Adds Header to top/bottom of Message's header list
     * @param <var>header</var> Header to be added
     * @param <var>top</var> indicates if Header is to be added at top/bottom
     * @throws IllegalArgumentException if header is null or is not from
     * the same JAIN SIP implementation as this Message
     */
    public void addHeader(Header header,boolean top) 
        throws IllegalArgumentException {
	    if (header == null || header.getName() == null  ) {
		throw new IllegalArgumentException("null  or bad header");
	    }
	    if(LogWriter.needsLogging()) 
		LogWriter.logMessage("addHeader : header = " 
			+ header + "name  = " + header.getName() +
		        " class = " + header.getClass().getName() 
			+ " top = " + top);
		
            try {
                NistSIPHeaderMapping map = (NistSIPHeaderMapping) header;
                SIPHeader siphdr = map.getImplementationObject();
		if (siphdr != null) {
		    sipMessage.attachHeader(siphdr,false,top);
		} else throw new IllegalArgumentException("Bad header");
            } catch (Exception ex) {
		if (LogWriter.needsLogging()) 
		    LogWriter.logException(ex);
                throw new IllegalArgumentException(ex.getMessage());
            }
	   if (LogWriter.needsLogging()) {
	       LinkedList hdrlist = sipMessage.getHeaders(header.getName());
	       ListIterator li = hdrlist.listIterator();
	       LogWriter.logMessage("headers = ");
	       while(li.hasNext()) {
	           LogWriter.logMessage( ((SIPHeader) li.next()) . toString());
		}
	       LogWriter.logMessage("*****");
	   }
		
    }
    
    /**
     * Sets all Headers of specified name in header list.
     * Note that this method is equivalent to invoking .
     * removeHeaders(headerName) followed by addHeaders(headers, top)
     * @param <var>headerName</var> name of Headers to set
     * @param <var>headers</var> List of Headers to be set
     * @throws IllegalArgumentException if 
     * headerName or headers is null, if headers is empty,
     * contains any null elements, or contains any objects that are not 
     * Header objects from the same JAIN SIP implementation as this 
     * Message, or contains any Headers that
     * don't match the specified header name
     */
     
    public void setHeaders(String headerName,List headers) 
    throws IllegalArgumentException {
        if (headerName == null || headers == null || headers.isEmpty())
        	throw new IllegalArgumentException
			("setHeaders : headerName or header null") ;
	Iterator it = headers.iterator();
	try {
	  while(it.hasNext()) {
            HeaderImpl header =  (HeaderImpl) it.next();
	    if (!header.getName().equals(headerName)) 
		  throw new IllegalArgumentException("Name mismatch");
	  }
	} catch (Exception ex) {
		throw new IllegalArgumentException(ex.getMessage());
	}
        
        try {
        	it = headers.iterator();
	        sipMessage.removeAll(headerName);
        	while (it.hasNext() ) {
        	    HeaderImpl header =  (HeaderImpl) it.next();
            	    SIPHeader sipheader = header.getImplementationObject();
       	            sipMessage.attachHeader(sipheader,false);
           	}
         }catch(Exception ex) {
		if (LogWriter.needsLogging()) LogWriter.logException(ex);
         	throw new IllegalArgumentException(ex.getMessage()) ;
         }
    }
        
    
    /**
     * Sets the first/last Header of header's name in Message's Header list.
     * Note that this method is equivalent to invoking 
     *   removeHeader(headerName, first)
     *   followed by addHeader(header, first)
     * @param <var>header</var> Header to set
     * @param <var>first</var> indicates if first/last Header is to be set
     * @throws IllegalArgumentException if header is null or is not from
     * the same JAIN SIP implementation as this Message
     */
    public void setHeader(Header header,boolean first) 
    throws IllegalArgumentException {
    	if(header == null)
    		throw new 
		IllegalArgumentException("header is null") ;
        
	this.removeHeader(header.getName(),first);
	this.addHeader(header,first);
    }
    
    /**
     * Removes first (or last) Header of specified name from Message's 
     * Header list.
     * Note if no Headers of specified name exist the method has no effect
     * @param <var>headerName</var> name of Header to be removed
     * @param <var>first</var> indicates whether first or last Header of 
     *  specified name is to be removed
     * @throws IllegalArgumentException if headerName is null
     */
    public void removeHeader(String headerName,boolean first) 
        throws IllegalArgumentException {
            if (headerName == null)
            	throw new IllegalArgumentException
			("remove Header : headerName null") ;
            	
	    if (LogWriter.needsLogging()) 
		LogWriter.logMessage("removeHeader : " + headerName + 
					"," + first);
	    sipMessage.removeHeader(headerName,first);
	    if (LogWriter.needsLogging()) {
		LogWriter.logMessage("After removing header : ");
		LinkedList llist = sipMessage.getHeaders(headerName);
		ListIterator li = llist.listIterator();
		while (li.hasNext()) {
			SIPHeader sh = (SIPHeader) li.next();
			LogWriter.logMessage(sh.toString());
		}
	     }
    }
    
    /**
     * Removes all Headers of specified name from Message's Header list.
     * Note if no Headers of specified name exist the method has no effect
     * @param <var>headername</var> name of Headers to be removed
     * @throws IllegalArgumentException if headerType is null
     */
    public void removeHeaders(String headerName) 
        throws IllegalArgumentException {
           if (headerName == null)
            	throw new 
		IllegalArgumentException
			("remove Header : headerName null") ;
	    if (LogWriter.needsLogging()) 
		LogWriter.logMessage("removeHeaders : " + headerName );
	    sipMessage.removeAll(headerName);
    }
    
    /**
     * Gets HeaderIterator of all Headers in Message.
     * Note that order of Headers in HeaderIterator is same as order in
     * Message (Returns null if no Headers exist).
     * @return HeaderIterator of all Headers in Message
     */
    public HeaderIterator getHeaders() {
       if (LogWriter.needsLogging()) LogWriter.logMessage("getHeaders");
       HeaderIteratorImpl hil = 
		new HeaderIteratorImpl(sipMessage.getHeaderIterator());
       return hil;               
    }
    
    /**
     * Gets HeaderIterator of all Headers of specified name in Message.
     * Note that order of Headers in HeaderIterator is same as order 
     * they appear in Message
     * (Returns null if no Headers of specified name exist)
     * @param <var>headerName</var> name of Headers to return
     * @return HeaderIterator of all Headers of specified name in Message
     * @throws IllegalArgumentException if headerName is null
     */
    public HeaderIterator getHeaders(String headerName) 
        throws IllegalArgumentException {
            if (LogWriter.needsLogging()) 
	      LogWriter.logMessage("getHeaders : " + headerName);
            if (headerName == null) {
                throw new IllegalArgumentException(headerName);
            }
            LinkedList llist = sipMessage.getHeaders(headerName);
            if (llist == null) return null;
            HeaderIterator hi = new HeaderIteratorImpl(llist);
            return hi;
    }
    
    /**
     * Gets first (or last) Header of specified name in Message
     * (Returns null if no Headers of specified name exist)
     * @param <var>headerName</var> name of Header to return
     * @param <var>first</var> indicates whether the first or 
     *  last Header of specified name is required
     * @return first (or last) Header of specified name in Message's Header list
     * @throws IllegalArgumentException if headername is null
     * @throws HeaderParseException if implementation could not parse 
     * 		header value
     */
    public Header getHeader(String headerName,boolean first) 
        throws IllegalArgumentException, HeaderParseException {
             if (LogWriter.needsLogging()) 
	         LogWriter.logMessage("getHeader: " + headerName +
			"first = " + first );
             Header retval;
             if (headerName == null ) 
                throw new IllegalArgumentException("null header name");
            LinkedList llist  = sipMessage.getHeaders(headerName);
            if (llist == null) {
		 return null;
	    }
	    if (first) {
		SIPHeader sipheader = (SIPHeader) llist.getFirst();
                retval  = HeaderMap.getJAINHeaderFromNISTHeader(sipheader);
	    } else {
		SIPHeader sipheader = (SIPHeader) llist.getLast();
                retval  = HeaderMap.getJAINHeaderFromNISTHeader(sipheader);
	    }

            return retval;
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has any headers
     * @return boolean value to indicate if Message
     * has any headers
     */
    public boolean hasHeaders() {
	if (LogWriter.needsLogging()) 
	    LogWriter.logMessage("hasHeaders()");
        return sipMessage.getHeaders() != null;
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has any headers of specified name
     * @return boolean value to indicate if Message
     * has any headers of specified name
     * @param <var>headerName</var> header name
     * @throws IllegalArgumentException if headerName is null
     */
    public boolean hasHeaders(String headerName) 
        throws IllegalArgumentException {
	    if (LogWriter.needsLogging()) 
	        LogWriter.logMessage("hasHeaders() " + headerName);
	    if (headerName == null) 
		throw new IllegalArgumentException("null arg");
            return  sipMessage.hasHeader(headerName);
            
    }
    
    /**
     * Gets CallIdHeader of Message.
     * (Returns null if no CallIdHeader exists)
     * @return CallIdHeader of Message
     */
    public CallIdHeader getCallIdHeader() {
        SIPHeader sipHeader = sipMessage.getCallIdHeader();
        if (sipHeader==null) return null; 
        else {
                CallIdHeaderImpl retval = (CallIdHeaderImpl) 
                HeaderMap.getJAINHeaderFromNISTHeader(sipHeader);
                return retval;
        }
    }
    
    /**
     * Sets CallIdHeader of Message.
     * @param <var>callIdHeader</var> CallIdHeader to set
     * @throws IllegalArgumentException if callIdHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setCallIdHeader(CallIdHeader callIdHeader) 
        throws IllegalArgumentException {
            if(callIdHeader == null)
            	throw new IllegalArgumentException
			("setCallIdHeader : callIdHeader null !") ;
            try {
                CallIdHeaderImpl ci = (CallIdHeaderImpl) callIdHeader;
                SIPHeader sipHdr = ci.getImplementationObject();
                sipMessage.attachHeader(sipHdr,true);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                    IllegalArgumentException
			("setCallIdHeader : Duplicate Header!");
            }
    }
    
    /**
     * Gets CSeqHeader of Message.
     * (Returns null if no CSeqHeader exists)
     * @return CSeqHeader of Message
     */
    public CSeqHeader getCSeqHeader() {
        CSeq cseq = sipMessage.getCSeqHeader();
        if (cseq == null) return null;
        else return (CSeqHeader) HeaderMap.getJAINHeaderFromNISTHeader(cseq); 
        
    }
    
    /**
     * Sets CSeqHeader of Message.
     * @param <var>cSeqHeader</var> CSeqHeader to set
     * @throws IllegalArgumentException if cSeqHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setCSeqHeader(CSeqHeader cSeqHeader)
        throws IllegalArgumentException {
            
            if(cSeqHeader == null)
            	throw new IllegalArgumentException
		("setCSeqHeader : CSeqHeader null !") ;
            try {
                CSeqHeaderImpl ci = (CSeqHeaderImpl) cSeqHeader;
                SIPHeader sipHdr = ci.getImplementationObject();
                sipMessage.attachHeader(sipHdr,true);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
		throw new 
                    IllegalArgumentException
		("setCSeqHeader : Duplicate Header!");            }
    }
    
    /**
     * Gets ToHeader of Message.
     * (Returns null if no CSeqHeader exists)
     * @return ToHeader of Message
     * @throws HeaderNotSetException if no ToHeader exists
     */
    public ToHeader getToHeader() {
    	To to = sipMessage.getToHeader();
        if (to == null) return null;
        else return (ToHeader) HeaderMap.getJAINHeaderFromNISTHeader(to);     
    }
    
    /**
     * Sets ToHeader of Message.
     * @param <var>toHeader</var> ToHeader to set
     * @throws IllegalArgumentException if toHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setToHeader(ToHeader toHeader) 
        throws IllegalArgumentException {
            if(toHeader == null)
            	throw new IllegalArgumentException
			("setToHeader : toHeader null !") ;
            try {
                ToHeaderImpl ti = (ToHeaderImpl) toHeader;
                SIPHeader sipHdr = ti.getImplementationObject();
                sipMessage.attachHeader(sipHdr,true);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                    IllegalArgumentException("setToHeader : Duplicate Header!");
            }        	
        	
    }
    
    /**
     * Gets FromHeader of Message.
     * (Returns null if no CSeqHeader exists)
     * @return FromHeader of Message
     * @throws HeaderNotSetException if no FromHeader exists
     */
    public FromHeader getFromHeader() {
    	From from = sipMessage.getFromHeader();
        if (from == null) return null;
        else return (FromHeader) HeaderMap.getJAINHeaderFromNISTHeader(from);    
    }
    
    /**
     * Sets FromHeader of Message.
     * @param <var>fromHeader</var> FromHeader to set
     * @throws IllegalArgumentException if fromHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setFromHeader(FromHeader fromHeader) 
        throws IllegalArgumentException {
            
            if(fromHeader == null)
            	throw new 
		IllegalArgumentException("setFromHeader : fromHeader null !") ;
            try {
                FromHeaderImpl fi = (FromHeaderImpl) fromHeader;
                SIPHeader sipHdr = fi.getImplementationObject();
                sipMessage.attachHeader(sipHdr,true);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                 IllegalArgumentException("setFromHeader : Duplicate Header!");
            }        	
    }
    
    /**
     * Gets HeaderIterator of ViaHeaders of Message.
     * (Returns null if no ViaHeaders exist)
     * @return HeaderIterator of ViaHeaders of Message
     */
    public HeaderIterator getViaHeaders() {
    	HeaderIteratorImpl hi;
        ViaList viaList=sipMessage.getViaHeaders();
    	if (viaList==null) return null;
        else {
                hi=new HeaderIteratorImpl(viaList);
                return hi;
        }
    }
    
    /**
     * Sets ViaHeaders of Message.
     * @param <var>viaHeaders</var> List of ViaHeaders to set
     * @throws IllegalArgumentException if viaHeaders is null, empty, contains
     * any elements that are null or not ViaHeaders from the same
     * JAIN SIP implementation
     */
    public void setViaHeaders(List viaHeaders) 
        throws IllegalArgumentException {
	if(viaHeaders == null)
       	 throw new 
	  IllegalArgumentException("setViaHeaders : viaHeaders null") ;        	
    	setHeaders(ViaHeader.name,viaHeaders) ;
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has ViaHeaders
     * @return boolean value to indicate if Message
     * has ViaHeaders
     */
    public boolean hasViaHeaders() {
    	return sipMessage.getViaHeaders() != null ;
    }
    
    /**
     * Removes ViaHeaders from Message (if any exist).
     */
    public void removeViaHeaders() {
    	try{
    	    sipMessage.removeAll(Class.forName
                (PackageNames.SIPHEADERS_PACKAGE + ".ViaList")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Gets ContentTypeHeader of Message.
     * (Returns null if no ContentTypeHeader exists)
     * @return ContentTypeHeader of Message
     * @throws HeaderParseException if implementation could not parse 
     * header value
     */
    public ContentTypeHeader getContentTypeHeader() 
        throws HeaderParseException {
 
 	ContentType contentType = sipMessage.getContentTypeHeader();
        if (contentType == null) return null;
        else return (ContentTypeHeader) 
	   HeaderMap.getJAINHeaderFromNISTHeader(contentType);
    }
    
    /**
     * Sets ContentTypeHeader of Message.
     * @param <var>contentTypeHeader</var> ContentTypeHeader to set
     * @throws IllegalArgumentException if contentTypeHeader is null
     * or not from same JAIN SIP implementation
     * @throws SipException if Message does not contain body
     */
    public void setContentTypeHeader(ContentTypeHeader contentTypeHeader) 
        throws IllegalArgumentException, SipException {

            if(contentTypeHeader == null)
            	throw new IllegalArgumentException
			("setContentTypeHeader : contentTypeHeader null !") ;
            try {
                ContentTypeHeaderImpl ci = (ContentTypeHeaderImpl) 
			contentTypeHeader;
                SIPHeader sipHdr = ci.getImplementationObject();
                sipMessage.attachHeader(sipHdr,true);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException
			("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                    IllegalArgumentException
			("setContentTypeHeader : Duplicate Header!");
            }
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has ContentTypeHeader
     * @return boolean value to indicate if Message
     * has ContentTypeHeader
     */
    public boolean hasContentTypeHeader() {
    	return sipMessage.getContentTypeHeader() != null ;
    }
    
    /**
     * Removes ContentTypeHeader from Message (if it exists)
     */
    public void removeContentTypeHeader() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".ContentType")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Gets DateHeader of Message.
     * (Returns null if no DateHeader exists)
     * @return DateHeader of Message
     * @throws HeaderParseException if implementation could not parse 
     *   header value
     */
    public DateHeader getDateHeader() 
        throws HeaderParseException {

       	SIPDateHeader dateHeader = sipMessage.getDateHeader();
        if (dateHeader == null) return null;
        else return (DateHeader) 
		HeaderMap.getJAINHeaderFromNISTHeader(dateHeader); 
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has DateHeader
     * @return boolean value to indicate if Message
     * has DateHeader
     */
    public boolean hasDateHeader() {
    	return sipMessage.getDateHeader() != null ;
    }
    
    /**
     * Sets DateHeader of Message.
     * @param <var>dateHeader</var> DateHeader to set
     * @throws IllegalArgumentException if dateHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setDateHeader(DateHeader dateHeader) 
        throws IllegalArgumentException {

            if(dateHeader == null)
            	throw new IllegalArgumentException
			("setDateHeader : dateHeader null !") ;
            try {
                DateHeaderImpl di = (DateHeaderImpl) dateHeader;
                SIPHeader sipHdr = di.getImplementationObject();
                sipMessage.attachHeader(sipHdr,true);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                    IllegalArgumentException
			("setDateHeader : Duplicate Header!");
            }        	
    }
    
    /**
     * Removes DateHeader from Message (if it exists)
     */
    public void removeDateHeader() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".SIPDateHeader")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Gets EncryptionHeader of Message.
     * (Returns null if no EncryptionHeader exists)
     * @return EncryptionHeader of Message
     * @throws HeaderParseException if implementation could not parse 
     *  	header value
     */
    public EncryptionHeader getEncryptionHeader() throws HeaderParseException {

	Encryption encryption = sipMessage.getEncryptionHeader();
        if (encryption == null) return null;
        else return (EncryptionHeader) 
	   HeaderMap.getJAINHeaderFromNISTHeader(encryption);
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has EncryptionHeader
     * @return boolean value to indicate if Message
     * has EncryptionHeader
     */
    public boolean hasEncryptionHeader() {
    	return sipMessage.getEncryptionHeader() != null ;
    }
    
    /**
     * Sets EncryptionHeader of Message.
     * @param <var>encryptionHeader</var> EncryptionHeader to set
     * @throws IllegalArgumentException if encryptionHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setEncryptionHeader(EncryptionHeader encryptionHeader)
        throws IllegalArgumentException {
            
            if(encryptionHeader == null)
            	throw new IllegalArgumentException
			("setEncryptionHeader : encryptionHeader null !") ;
            try {
                EncryptionHeaderImpl ei = 
			(EncryptionHeaderImpl) encryptionHeader ;
                SIPHeader sipHdr = ei.getImplementationObject();
                sipMessage.attachHeader(sipHdr,false);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException
			("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                    IllegalArgumentException
			("setEncryptionHeader : Duplicate Header!");
            }     	
    }
    
    /**
     * Removes EncryptionHeader from Message (if it exists)
     */
    public void removeEncryptionHeader() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".Encryption")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Gets UserAgentHeader of Message.
     * (Returns null if no UserAgentHeader exists)
     * @return UserAgentHeader of Message
     * @throws HeaderParseException if implementation could not 
     *    parse header value
     */
    public UserAgentHeader getUserAgentHeader() 
        throws HeaderParseException {
	
	UserAgent userAgent = sipMessage.getUserAgentHeader();
        if (userAgent == null) return null;
        else return (UserAgentHeader) 
		HeaderMap.getJAINHeaderFromNISTHeader(userAgent);
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has UserAgentHeader
     * @return boolean value to indicate if Message
     * has UserAgentHeader
     */
    public boolean hasUserAgentHeader() {
    	return sipMessage.getUserAgentHeader() != null ;
    }
    
    /**
     * Sets UserAgentHeader of Message.
     * @param <var>userAgentHeader</var> UserAgentHeader to set
     * @throws IllegalArgumentException if userAgentHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setUserAgentHeader(UserAgentHeader userAgentHeader) 
		throws IllegalArgumentException {
			
            if(userAgentHeader == null)
            	throw new IllegalArgumentException
			("setUserAgentHeader : dateHeader null !") ;
            try {
                UserAgentHeaderImpl ui = (UserAgentHeaderImpl) userAgentHeader;
                SIPHeader sipHdr = ui.getImplementationObject();
                sipMessage.attachHeader(sipHdr,false);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                    IllegalArgumentException
			("setUserAgentHeader : Duplicate Header!");
            }        	
    }
    
    /**
     * Removes UserAgentHeader from Message (if it exists)
     */
    public void removeUserAgentHeader() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".UserAgent")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Gets TimeStampHeader of Message.
     * (Returns null if no TimeStampHeader exists)
     * @return TimeStampHeader of Message
     * @throws HeaderParseException if implementation could not parse 
     *  	header value
     */
    public TimeStampHeader getTimeStampHeader() throws HeaderParseException {

	TimeStamp ts = sipMessage.getTimestampHeader();
        if (ts == null) return null;
        else return (TimeStampHeader) HeaderMap.getJAINHeaderFromNISTHeader(ts);           
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has TimeStampHeader
     * @return boolean value to indicate if Message
     * has TimeStampHeader
     */
    public boolean hasTimeStampHeader() {
    	return sipMessage.getTimestampHeader() != null ;
    }
    
    /**
     * Removes TimeStampHeader from Message (if it exists)
     */
    public void removeTimeStampHeader() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".TimeStamp")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Sets TimeStampHeader of Message.
     * @param <var>timeStampHeader</var> TimeStampHeader to set
     * @throws IllegalArgumentException if timeStampHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setTimeStampHeader(TimeStampHeader timeStampHeader) 
        throws IllegalArgumentException {
        	
            if(timeStampHeader == null)
            	throw new IllegalArgumentException
		("setTimeStampHeader : timeStampHeader null !") ;
            try {
                TimeStampHeaderImpl ti = (TimeStampHeaderImpl) timeStampHeader;
                SIPHeader sipHdr = ti.getImplementationObject();
                sipMessage.attachHeader(sipHdr,false);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException
			("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                    IllegalArgumentException
			("setTimeStampHeader : Duplicate Header!");
            }        	
        	

    }
    
    /**
     * Gets HeaderIterator of ContentEncodingHeaders of Message.
     * (Returns null if no ContentEncodingHeaders exist)
     * @return HeaderIterator of ContentEncodingHeaders of Message
     */
    public HeaderIterator getContentEncodingHeaders() {
        HeaderIteratorImpl hi;
        ContentEncodingList contentList=sipMessage.getContentEncodingHeaders();
    	if (contentList==null) return null;
        else {
                hi=new HeaderIteratorImpl(contentList);
                return hi;
        }             
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has ContentEncodingHeaders
     * @return boolean value to indicate if Message
     * has ContentEncodingHeaders
     */
    public boolean hasContentEncodingHeaders() {
    	return sipMessage.getContentEncodingHeaders() != null ;
    }
    
    /**
     * Removes ContentEncodingHeaders from Message (if any exist)
     */
    public void removeContentEncodingHeaders() {
    	try{
    		sipMessage.removeAll(Class.forName
                  (PackageNames.SIPHEADERS_PACKAGE + ".ContentEncodingList")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Sets ContentEncodingHeaders of Message.
     * @param <var>contentEncodingHeaders</var> List of 
     * 		ContentEncodingHeaders to set
     * @throws IllegalArgumentException if contentEncodingHeaders is null, 
     *  	empty, contains any elements that are null or not 
     * ContentEncodingHeaders from the same JAIN SIP implementation
     */
    public void setContentEncodingHeaders(List contentEncodingHeaders)
        throws IllegalArgumentException, SipException {
	if(contentEncodingHeaders == null)
        	throw new 
		IllegalArgumentException
		("setContentEncodingHeaders : contentEncodingHeaders null") ; 
    	setHeaders(ContentEncodingHeader.name,contentEncodingHeaders) ;
    }
    
    
    /**
     * Gets ContentLengthHeader of Message.
     * (Returns null if no ContentLengthHeader exists)
     * @return ContentLengthHeader of Message
     * @throws HeaderParseException if implementation could not parse 
     *  	header value
     */
    public ContentLengthHeader getContentLengthHeader() 
        throws HeaderParseException {

	ContentLength cl = sipMessage.getContentLengthHeader();
        if (cl == null) return null;
        else return 
	   (ContentLengthHeader) HeaderMap.getJAINHeaderFromNISTHeader(cl); 
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has ContentLengthHeader
     * @return boolean value to indicate if Message
     * has ContentLengthHeader
     */
    public boolean hasContentLengthHeader() {
    	return sipMessage.getContentLengthHeader() != null ;
    }
    
    /**
     * Removes ContentLengthHeader from Message (if it exists)
     */
    public void removeContentLengthHeader() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".ContentLength")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Sets ContentLengthHeader of Message.
     * @param <var>contentLengthHeader</var> ContentLengthHeader to set
     * @throws IllegalArgumentException if contentLengthHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setContentLengthHeader
        (ContentLengthHeader contentLengthHeader) 
            throws IllegalArgumentException {

          if(contentLengthHeader == null)
            	throw new IllegalArgumentException
		("setContentLengthHeader : contentLengthHeader null !") ;
            try {
                ContentLengthHeaderImpl ci = 
			(ContentLengthHeaderImpl) contentLengthHeader;
                SIPHeader sipHdr = ci.getImplementationObject();
                sipMessage.attachHeader(sipHdr,false);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException
			("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                    IllegalArgumentException
			("setContentLengthHeader : Duplicate Header!");
            }
    }
    
    /**
     * Gets HeaderIterator of AcceptHeaders of Message.
     * (Returns null if no AcceptHeaders exist)
     * @return HeaderIterator of AcceptHeaders of Message
     */
    public HeaderIterator getAcceptHeaders() {
        HeaderIteratorImpl hi;
        AcceptList acceptList=sipMessage.getAcceptHeaders();
    	if (acceptList==null) return null;
        else {
                hi=new HeaderIteratorImpl(acceptList);
                return hi;
        }            
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has AcceptHeaders
     * @return boolean value to indicate if Message
     * has AcceptHeaders
     */
    public boolean hasAcceptHeaders() {
    	return sipMessage.getAcceptHeaders() != null ;
    }
    
    /**
     * Removes AcceptHeaders from Message (if any exist)
     */
    public void removeAcceptHeaders() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".AcceptList")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Sets AcceptHeaders of Message.
     * @param <var>acceptHeaders</var> List of AcceptHeaders to set
     * @throws IllegalArgumentException if acceptHeaders is null, empty, 
     * contains any elements that are null or not AcceptHeaders from the same
     * JAIN SIP implementation
     */
    public void setAcceptHeaders(List acceptHeaders) 
        throws IllegalArgumentException {
        if(acceptHeaders == null)
        	throw new IllegalArgumentException
			("setAcceptHeaders : acceptHeaders null") ;
        setHeaders(AcceptHeader.name,acceptHeaders) ;
    }
    
    /**
     * Gets HeaderIterator of AcceptEncodingHeaders of Message.
     * (Returns null if no AcceptEncodingHeaders exist)
     * @return HeaderIterator of AcceptEncodingHeaders of Message
     */
    public HeaderIterator getAcceptEncodingHeaders() {
        HeaderIteratorImpl hi;
        AcceptEncodingList acceptList=sipMessage.getAcceptEncodingHeaders();
    	if (acceptList==null) return null;
        else {
                hi=new HeaderIteratorImpl(acceptList);
                return hi;
        }           
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has AcceptEncodingHeaders
     * @return boolean value to indicate if Message
     * has AcceptEncodingHeaders
     */
    public boolean hasAcceptEncodingHeaders() {
    	return sipMessage.getAcceptEncodingHeaders() != null ;
    }
    
    /**
     * Removes AcceptEncodingHeaders from Message (if any exist)
     */
    public void removeAcceptEncodingHeaders() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".AcceptEncodingList")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Sets AcceptEncodingHeaders of Message.
     * @param <var>acceptEncodingHeaders</var> List of AcceptEncodingHeaders 
     * 		to set
     * @throws IllegalArgumentException if acceptEncodingHeaders is null, 
     * 	empty, contains any elements that are null or not 
     * AcceptEncodingHeaders from the same JAIN SIP implementation
     */
    public void setAcceptEncodingHeaders(List acceptEncodingHeaders) 
        throws IllegalArgumentException { 
       	
       	if(acceptEncodingHeaders == null)
        	throw new IllegalArgumentException
		("setAcceptEncodingHeaders : acceptEncodingHeaders null") ;
       	setHeaders(AcceptEncodingHeader.name,acceptEncodingHeaders) ;
    }
    
    /**
     * Gets HeaderIterator of AcceptLanguageHeaders of Message.
     * (Returns null if no AcceptLanguageHeaders exist)
     * @return HeaderIterator of AcceptLanguageHeaders of Message
     */
    public HeaderIterator getAcceptLanguageHeaders() {
        HeaderIteratorImpl hi;
        AcceptLanguageList acceptList=sipMessage.getAcceptLanguageHeaders();
    	if (acceptList==null) return null;
        else {
                hi=new HeaderIteratorImpl(acceptList);
                return hi;
        }      
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has AcceptLanguageHeaders
     * @return boolean value to indicate if Message
     * has AcceptLanguageHeaders
     */
    public boolean hasAcceptLanguageHeaders() {
    	return sipMessage.getAcceptLanguageHeaders() != null ;
    }
    
    /**
     * Removes AcceptLanguageHeaders from Message (if any exist)
     */
    public void removeAcceptLanguageHeaders() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + 
		".AcceptLanguageList")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Sets AcceptLanguageHeaders of Message.
     * @param <var>acceptLanguageHeaders</var> List of AcceptLanguageHeaders 
     *   to set
     * @throws IllegalArgumentException if acceptLanguageHeaders is null, 
     *  empty, contains
     * any elements that are null or not AcceptLanguageHeaders from the same
     * JAIN SIP implementation
     */
    public void setAcceptLanguageHeaders(List acceptLanguageHeaders) 
        throws IllegalArgumentException {
    	
    	if(acceptLanguageHeaders == null)
        	throw new IllegalArgumentException
		("setAcceptLanguageHeaders : acceptLanguageHeaders null") ;
    	setHeaders(AcceptLanguageHeader.name,acceptLanguageHeaders) ;
    }
    
    /**
     * Gets ExpiresHeader of Message.
     * (Returns null if no ExpiresHeader exists)
     * @return ExpiresHeader of Message
     * @throws HeaderParseException if implementation could not parse 
     * 		header value
     */
    public ExpiresHeader getExpiresHeader() 
        throws HeaderParseException {

	Expires expires = sipMessage.getExpiresHeader();
        if (expires == null) return null;
        else return (ExpiresHeader) 
		HeaderMap.getJAINHeaderFromNISTHeader(expires);     
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has ExpiresHeader
     * @return boolean value to indicate if Message
     * has ExpiresHeader
     */
    public boolean hasExpiresHeader() {
    	return sipMessage.getExpiresHeader() != null ;
    }
    
    /**
     * Removes ExpiresHeader from Message (if it exists)
     */
    public void removeExpiresHeader() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".Expires")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Sets ExpiresHeader of Message.
     * @param <var>expiresHeader</var> ExpiresHeader to set
     * @throws IllegalArgumentException if expiresHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setExpiresHeader(ExpiresHeader expiresHeader) 
	throws IllegalArgumentException {
            
            if(expiresHeader == null)
            	throw new IllegalArgumentException
			("setExpiresHeader : expiresHeader null !") ;
            try {
                ExpiresHeaderImpl ei = (ExpiresHeaderImpl) expiresHeader ;
                SIPHeader sipHdr = ei.getImplementationObject();
                sipMessage.attachHeader(sipHdr,false);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                    IllegalArgumentException
			("setExpiresHeader : Duplicate Header!");
            }
    }
    
    /**
     * Gets HeaderIterator of ContactHeaders of Message.
     * (Returns null if no ContactHeaders exist)
     * @return HeaderIterator of ContactHeaders of Message
     */
    public HeaderIterator getContactHeaders() {
    	HeaderIteratorImpl hi;
        ContactList contactList=sipMessage.getContactHeaders();
    	if (contactList==null) return null;
        else {
                hi=new HeaderIteratorImpl(contactList);
                return hi;
        }
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has ContactHeaders
     * @return boolean value to indicate if Message
     * has ContactHeaders
     */
    public boolean hasContactHeaders() {
    	return sipMessage.getContactHeaders() != null ;
    }
    
    /**
     * Removes ContactHeaders from Message (if any exist)
     */
    public void removeContactHeaders() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".ContactList")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Sets ContactHeaders of Message.
     * @param <var>contactHeaders</var> List of ContactHeaders to set
     * @throws IllegalArgumentException if contactHeaders is null, empty, 
     * contains any elements that are null or not ContactHeaders from the same
     * JAIN SIP implementation
     */
    public void setContactHeaders(List contactHeaders) 
	throws IllegalArgumentException {
	
	if(contactHeaders == null)
        	throw new IllegalArgumentException
		("setContactHeaders : contactHeaders null") ;
	setHeaders(ContactHeader.name,contactHeaders) ;        	
    }
    
    /**
     * Gets OrganizationHeader of Message.
     * (Returns null if no OrganizationHeader exists)
     * @return OrganizationHeader of Message
     * @throws HeaderParseException if implementation could not parse 
     * 	header value
     */
    public OrganizationHeader getOrganizationHeader() 
	throws HeaderParseException {
		
	Organization organization = sipMessage.getOrganizationHeader();
        if (organization == null) return null;
        else return (OrganizationHeader) 
		HeaderMap.getJAINHeaderFromNISTHeader(organization);     
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has OrganizationHeader
     * @return boolean value to indicate if Message
     * has OrganizationHeader
     */
    public boolean hasOrganizationHeader() {
    	return sipMessage.getOrganizationHeader() != null ;
    }
    
    /**
     * Removes OrganizationHeader from Message (if it exists)
     */
    public void removeOrganizationHeader() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".Organization")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Sets OrganizationHeader of Message.
     * @param <var>organizationHeader</var> OrganizationHeader to set
     * @throws IllegalArgumentException if organizationHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setOrganizationHeader(OrganizationHeader organizationHeader) 
	throws IllegalArgumentException {
		
            if(organizationHeader == null)
            	throw new IllegalArgumentException
		("setOrganizationHeader : organizationHeader null !") ;
            try {
                OrganizationHeaderImpl oi = 
			(OrganizationHeaderImpl) organizationHeader ;
                SIPHeader sipHdr = oi.getImplementationObject();
                sipMessage.attachHeader(sipHdr,false);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException
			("Implemetation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                    IllegalArgumentException
			("setOrganizationHeader : Duplicate Header!");
            }
    }
    
    /**
     * Gets HeaderIterator of RecordRouteHeaders of Message.
     * (Returns null if no RecordRouteHeaders exist)
     * @return HeaderIterator of RecordRouteHeaders of Message
     */
    public HeaderIterator getRecordRouteHeaders() {
    	HeaderIteratorImpl hi;
        RecordRouteList recordList=sipMessage.getRecordRouteHeaders();
    	if (recordList==null) return null;
        else {
                hi=new HeaderIteratorImpl(recordList);
                return hi;
        }             
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has RecordRouteHeaders
     * @return boolean value to indicate if Message
     * has RecordRouteHeaders
     */
    public boolean hasRecordRouteHeaders() {
    	return sipMessage.getRecordRouteHeaders() != null ;
    }
    
    /**
     * Removes RecordRouteHeaders from Message (if any exist)
     */
    public void removeRecordRouteHeaders() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".RecordRouteList")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Sets RecordRouteHeaders of Message.
     * @param <var>recordRouteHeaders</var> List of RecordRouteHeaders to set
     * @throws IllegalArgumentException if recordRouteHeaders is null, 
     * empty, contains any elements that are null or not RecordRouteHeaders 
     * from the same JAIN SIP implementation
     */
    public void setRecordRouteHeaders(List recordRouteHeaders) 
       throws IllegalArgumentException {
	if(recordRouteHeaders == null)
        	throw new IllegalArgumentException
		("setRecordRouteHeaders : recordRouteHeaders null") ;        	
    	setHeaders(RecordRouteHeader.name,recordRouteHeaders) ;       	
    }
    
    /**
     * Gets RetryAfterHeader of Message.
     * (Returns null if no RetryAfterHeader exists)
     * @return RetryAfterHeader of Message
     * @throws HeaderParseException if implementation could not parse 
     * header value
     */
    public RetryAfterHeader getRetryAfterHeader() throws HeaderParseException {
    	
        RetryAfter retryAfter = sipMessage.getRetryAfterHeader();
        if (retryAfter == null) return null;
        else return (RetryAfterHeader) 
		HeaderMap.getJAINHeaderFromNISTHeader(retryAfter);     
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has RetryAfterHeader
     * @return boolean value to indicate if Message
     * has RetryAfterHeader
     */
    public boolean hasRetryAfterHeader() {
    	return sipMessage.getRetryAfterHeader() != null ;
    }
    
    /**
     * Removes RetryAfterHeader from Message (if it exists)
     */
    public void removeRetryAfterHeader() {
    	try{
    		sipMessage.removeAll(Class.forName
                    (PackageNames.SIPHEADERS_PACKAGE + ".RetryAfter")) ;
    	}catch(ClassNotFoundException ex){
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
    
    /**
     * Sets RetryAfterHeader of Message.
     * @param <var>retryAfterHeader</var> RetryAfterHeader to set
     * @throws IllegalArgumentException if retryAfterHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setRetryAfterHeader(RetryAfterHeader retryAfterHeader) 
	throws IllegalArgumentException {
		
            if(retryAfterHeader == null)
            	throw new IllegalArgumentException
			("setRetryAfterHeader : retryAfterHeader null !") ;
            try {
                RetryAfterHeaderImpl ri = 
			(RetryAfterHeaderImpl) retryAfterHeader ;
                SIPHeader sipHdr = ri.getImplementationObject();
                sipMessage.attachHeader(sipHdr,false);
            } catch (ClassCastException ex) {
                throw new 
                    IllegalArgumentException("Implementation Class mismatch !");
            } catch ( SIPDuplicateHeaderException ex) {
                 throw new 
                    IllegalArgumentException
			("setRetryAfterHeader : Duplicate Header!");
            }
    }
    
    /**
     * Gets body of Message as String
     * (Returns null if no body exists). If a language is specified
     * in the content Type header, it is used to encode the message
     * into a string.
     * @return body of Message as String
     */
    public String getBodyAsString() {
	try {
	  if (sipMessage.getMessageContent() == null) return null;
	  return sipMessage.getMessageContent();
	} catch (UnsupportedEncodingException ex) { 
	  return null;
	}
    }
    
    /**
     * Gets body of Message as byte array
     * (Returns null if no body exists).
     * @return body of Message as byte array
     */
    public byte[] getBodyAsBytes() {
	if (sipMessage == null) return null;
        else return sipMessage.getContentAsBytes();
    }
    
    /**
     * Gets boolean value to indicate if Message
     * has body
     * @return boolean value to indicate if Message
     * has body
     */
    public boolean hasBody() {
	return sipMessage.hasContent();
    }
    
    /**
     * Sets body of Message (with ContentTypeHeader)
     * @param <var>body</var> body to set
     * @param <var>contentTypeHeader</var> ContentTypeHeader
     * @throws IllegalArgumentException if body or contentTypeHeader is null, or
     * contentTypeHeader is not from same JAIN SIP implementation
     * @throws SipParseException if body is not accepted by implementation
     */
    public void setBody(String body,ContentTypeHeader contentTypeHeader) 
	throws IllegalArgumentException, SipParseException {
    
    if (body == null  || contentTypeHeader == null) {
	  throw new IllegalArgumentException("null body or content type!");
	}
	ContentTypeHeaderImpl cti = (ContentTypeHeaderImpl) contentTypeHeader;
	SIPHeader sipHeader = cti.getImplementationObject();
	if (sipHeader == null) {
		throw new IllegalArgumentException("Illegal contentType hdr!");
	}
	try {
		sipMessage.attachHeader(sipHeader,true);
	} catch ( SIPDuplicateHeaderException ex) {
		throw new 
		IllegalArgumentException("contentTypeHdr already exists!");
	}
	sipMessage.setMessageContent(body) ;
    }
    
    /**
     * Sets body of Message (with ContentTypeHeader)
     * @param <var>body</var> body to set
     * @param <var>contentTypeHeader</var> ContentTypeHeader
     * @throws IllegalArgumentException if body or contentTypeHeader is null, or
     * contentTypeHeader is not from same JAIN SIP implementation
     * @throws SipParseException if body is not accepted by implementation
     */
    public void setBody(byte[] body,ContentTypeHeader contentTypeHeader) 
	throws IllegalArgumentException, SipParseException {
	if (body == null  || contentTypeHeader == null) {
	  throw new IllegalArgumentException("null body or content type!");
	}
	ContentTypeHeaderImpl cti = (ContentTypeHeaderImpl) contentTypeHeader;
	SIPHeader sipHeader = cti.getImplementationObject();
	if (sipHeader == null) {
		throw new IllegalArgumentException("Illegal contentType hdr!");
	}
	try {
		sipMessage.attachHeader(sipHeader,true);
	} catch ( SIPDuplicateHeaderException ex) {
		throw new 
		IllegalArgumentException("contentTypeHdr already exists!");
	}
	sipMessage.setMessageContent(body) ;
    }
    
    /**
     * Removes body from Message and contentType header from body
     * (if body exists)
     */
    public void removeBody() {
	try {
	  sipMessage.removeAll
		(Class.forName(PackageNames.SIPHEADERS_PACKAGE + 
			".ContentType"));
    	   sipMessage.removeMessageContent() ;
	} catch (ClassNotFoundException ex) {
	    if (LogWriter.needsLogging()) {
		LogWriter.logException(ex);
	    }
	    ex.printStackTrace();
	    System.exit(0);

	}
    }
    
    /**
     * Gets version major of Message.
     * @return version major of Message
     * @throws SipParseException if implementation could not parse 
     *   version major
     */
    public int getVersionMajor() throws SipParseException {
	if (sipMessage == null) {
	   throw new SipParseException("No SIP message!");
	}
	if (sipMessage instanceof SIPRequest) {
	  SIPRequest sipRequest = (SIPRequest) sipMessage;
	  RequestLine requestLine = sipRequest.getRequestLine();
	  if (requestLine == null) 
	    throw new SipParseException("No request Line!");
	  String major = requestLine.getVersionMajor();
	  if (major == null) throw new SipParseException("Bad Major Number");
	  try {
	    int retval = Integer.parseInt(major);
	    return retval;
	   } catch (NumberFormatException ex) {
		throw new SipParseException(ex.getMessage());
	   }
	} else {
	   SIPResponse sipResponse = (SIPResponse) sipMessage;
	   StatusLine statusLine = sipResponse.getStatusLine();
	   if (statusLine == null) 
	      throw new SipParseException("No request Line!");
	   String minor = statusLine.getVersionMajor();
	   if (minor == null) throw new SipParseException("Bad Major Number");
	   try {
	    int retval = Integer.parseInt(minor);
	    return retval;
	   } catch (NumberFormatException ex) {
		throw new SipParseException(ex.getMessage());
	   }
	}
    }
    
    /**
     * Gets version minor of Message.
     * @return version minor of Message
     * @throws SipParseException if implementation could not parse version minor
     */
    public int getVersionMinor() throws SipParseException {
	if (sipMessage == null) {
	   throw new SipParseException("No SIP message!");
	}
	if (sipMessage instanceof SIPRequest) {
	  SIPRequest sipRequest = (SIPRequest) sipMessage;
	  RequestLine requestLine = sipRequest.getRequestLine();
	  if (requestLine == null) 
	    throw new SipParseException("No request Line!");
	  String major = requestLine.getVersionMinor();
	  if (major == null) throw new SipParseException("Bad Major Number");
	  try {
	    int retval = Integer.parseInt(major);
	    return retval;
	   } catch (NumberFormatException ex) {
		throw new SipParseException(ex.getMessage());
	   }
	} else {
	   SIPResponse sipResponse = (SIPResponse) sipMessage;
	   StatusLine statusLine = sipResponse.getStatusLine();
	   if (statusLine == null) 
	      throw new SipParseException("No request Line!");
	   String minor = statusLine.getVersionMinor();
	   if (minor == null) throw new SipParseException("Bad Major Number");
	   try {
	    int retval = Integer.parseInt(minor);
	    return retval;
	   } catch (NumberFormatException ex) {
		throw new SipParseException(ex.getMessage());
	   }
	}
    }
    
    /**
     * Sets version of Message. Note that the version defaults to 2.0.
     * (i.e. version major of 2 and version minor of 0)
     * @param <var>versionMajor</var> version major
     * @param <var>versionMinor</var> version minor
     * @throws SipParseException if versionMajor or versionMinor are not 
     * accepted by implementation
     */
    public void setVersion(int versionMajor,int versionMinor) 
    throws SipParseException {
	if (sipMessage == null) {
		throw new SipParseException("SIPMessage not defined!");
	}
	String versionString = "SIP/"+versionMajor+"."+versionMinor;
	if (sipMessage instanceof SIPRequest) {
		SIPRequest sipRequest = (SIPRequest) sipMessage;
		RequestLine requestLine = sipRequest.getRequestLine();
		if (requestLine == null) {
		  throw new SipParseException("RequestLine not defined!");
	        }
		requestLine.setSipVersion(versionString);
	} else {
	       SIPResponse sipResponse = (SIPResponse) sipMessage;
	       StatusLine statusLine = sipResponse.getStatusLine();
	       if (statusLine == null) {
		  throw new SipParseException("RequestLine not defined!");
	       }
	       statusLine.setSipVersion(versionString);
	}
    }
    
    /**
     * Returns boolean value to indicate if Message is a Request.
     * @return boolean value to indicate if Message is a Request
     */
    public boolean isRequest() {
	return sipMessage instanceof SIPRequest;
    }
    
    /**
     * Returns start line of Message
     * @return start line of Message
     */
    public String getStartLine() {
	if (sipMessage instanceof SIPRequest) {
		SIPRequest siprequest = (SIPRequest) sipMessage;
	        RequestLine requestLine = siprequest.getRequestLine();
		return requestLine.encode();	
	} else {
		SIPResponse sipresponse = (SIPResponse) sipMessage;
		StatusLine statusLine = sipresponse.getStatusLine();
		return statusLine.encode();
	}
    }
    
    /**
     * Indicates whether some other Object is "equal to" this Message
     * (Note that obj must have the same Class as this Message - 
     * this means that it must be from the same JAIN SIP implementation)
     * @param <var>obj</var> the Object with which to compare this Message
     * @returns true if this Message is "equal to" the obj
     * argument; false otherwise
     */
    public boolean equals(Object object) {
        if (object == null) return false;
        if (! ( object instanceof MessageImpl)) return false;
        MessageImpl that = (MessageImpl) object;
        if (this.sipMessage.equals(that.sipMessage)) {
            return true;
        } else return false;
    }
    
    /**
     * Creates and returns copy of Message
     * @returns copy of Message
     */
    public Object clone() {
        MessageImpl retval = this instanceof RequestImpl ? 
	        (MessageImpl) new RequestImpl():
		(MessageImpl) new ResponseImpl() ;
        SIPMessage clonemsg = (SIPMessage) sipMessage.clone();
        retval.sipMessage = clonemsg;
        return retval;
    }
    
    /**
     * Gets string representation of Message
     * @return string representation of Message
     */
    public String toString() {
        return sipMessage.encode();
    }

   /**
    * Set the nist-sip message object. (Caution: not part of JAIN)
    *@param SIPMessage the imbedded NIST-SIP message object to set.
    *@see gov.nist.sip.msgparser.SIPMessage
    */
    public void setImplementationObject(SIPMessage msg) {
	this.sipMessage = msg;
    }

    /**
    * Get the NIST-SIP implementation object. (Caution: Not part of JAIN)
    *@see gov.nist.sip.msgparser.SIPMessage
    */
    public SIPMessage getImplementationObject() {
	return this.sipMessage;
    }
    
   /** 
    * This method checks if the "get" methods of the JAIN-SIP headers ( headers 
    * of this message ) are implemented right. So, we use the java.lang.reflect 
    * package to do some generic tests. 
    * Note that this is not part of the jain specificaiton (just a cheap
    * way of getting some limited amount of testing done). 
    * See examples/torure/Torture.java for how these tests get used.
    */    
    public void testGetMethods(PrintStream testResults) {
        Class c= this.getClass();
        Method[] theMethods = c.getMethods();
        
        try {
            // test the get<Header> methods:
            for (int i = 0; i < theMethods.length; i++) {
                String methodString = theMethods[i].getName();
                Class classType= theMethods[i].getReturnType();  
                String returnString=classType.getName();
            
                if ( methodString.startsWith("get") &&
                     ( methodString.endsWith("Headers") ||
                       methodString.endsWith("Header") 
                     ) &&
                     ! methodString.equals("getHeader") &&
                     ! methodString.equals("getHeaders") 
                ) {
                   
                    Object returnObject = theMethods[i].invoke(this,null);
                   
                    if ( returnObject != null ) {
                        testResults.print
                            ("Test.... Name method : " + methodString);
                        if ( methodString.endsWith("Header") ) {
                            //  test method call of the HeaderImpl class
                            //  for testing some others "get" methods linked
                            //  to the Header.
                            testResults.println("          TEST OK");
                            testResults.println();
                            ((HeaderImpl)returnObject).testGetMethods
					(testResults);
                        }
                        if ( methodString.endsWith("Headers") ) {
                            // for HeaderIterator returnType
                            HeaderIteratorImpl iterator=(HeaderIteratorImpl)
                                                                returnObject;
                            testResults.println("          TEST OK");
                            testResults.println();
                            while ( iterator.hasNext() ) {
                               ((HeaderImpl)iterator.next()).
					testGetMethods(testResults);
                            }
                        }
                    }
                    else { 
                     //  String content=methodString.substring
                       //                             (3,methodString.length());
                      //  testResults.println();
                      //  testResults.println
                       //   ("                       NO "+ content +" TO TEST");
                      //  testResults.println();
                    }
                }
            }
        }
        catch (IllegalAccessException e) {
              testResults.println(e);
        }
        catch (InvocationTargetException e) {
               testResults.println(e);
               e.printStackTrace();
               Throwable t=e.getTargetException(); 
               testResults.println(((SipParseException)t).getUnparsable());
        }
        catch (HeaderParseException e) {
              testResults.println(e);
        }
        catch (NoSuchElementException e) {
              testResults.println(e);
        }
    }
    
    /** 
    * This method checks if the "set" methods of the JAIN-SIP headers ( headers 
    * of this message ) are implemented right. So, we use the java.lang.reflect 
    * package to do some generic tests.
    */    
    public void testSetMethods(PrintStream testResults) {
        Class c= this.getClass();
        Method[] theMethods = c.getMethods();
        try {
            // test the set<Header> methods:
            for (int i = 0; i < theMethods.length; i++) {
                String methodString = theMethods[i].getName();
              
                if ( methodString.startsWith("set") &&
                     ( methodString.endsWith("Header") ||
                       methodString.endsWith("Headers")
                     ) &&
                     ! methodString.equals("setHeader") &&
                     ! methodString.equals("setHeaders") 
                     //methodString.equals("setWarningHeaders")
                ) {
                    testResults.print("Name method : " + methodString+"(...)");
                    Class [] parameterTypes= theMethods[i].getParameterTypes();
                    
                    if ( methodString.endsWith("Headers") ) {
                        String content=methodString.substring
                                                    (3,methodString.length()-1);
                        Object [] arguments = new Object[1];
                        LinkedList list=new LinkedList();
                        
                        Class classHeaderImpl=Class.forName
                        ("gov.nist.jain.protocol.ip.sip.header."+content+"Impl");
                        Object object=classHeaderImpl.newInstance(); 
                        list.add(object);
                        arguments[0]=list;
                        Object returnObject = theMethods[i].invoke(this,arguments);
                        testResults.println("        TEST OK");
                    }
                    else {
                        // set<Header> methods :
                        Object [] arguments = new Object[1];
                        Object object=HeaderMap.getJAINHeaderImplFromJAINHeader
                                                        ( parameterTypes[0] );
                   
                        arguments[0]=object;                    
                        Object returnObject = theMethods[i].invoke(this,arguments);
                        testResults.println("        TEST OK");
                    }
                }
            }
            testResults.println();
            
            // test the get<Header> methods:
            for (int i = 0; i < theMethods.length; i++) {
                String methodString = theMethods[i].getName();
                Class classType= theMethods[i].getReturnType();  
                String returnString=classType.getName();
            
                if ( methodString.startsWith("get") &&
                     ( methodString.endsWith("Headers") ||
                       methodString.endsWith("Header") 
                     ) &&
                     ! methodString.equals("getHeader") &&
                     ! methodString.equals("getHeaders") 
                     //methodString.equals("getWarningHeaders") 
                ) {
                   
                    Object returnObject = theMethods[i].invoke(this,null);
                   
                    if ( returnObject != null ) {
                        testResults.print
                            ("Test.... Name method : " + methodString);
                        if ( methodString.endsWith("Header") ) {
                            //  test method call of the HeaderImpl class
                            //  for testing some others "get" methods linked
                            //  to the Header.
                            testResults.println("          TEST OK");
                            testResults.println();
                            ((HeaderImpl)returnObject).testSetMethods(testResults);
                        }
                        if ( methodString.endsWith("Headers") ) {
                            // for HeaderIterator returnType
                            HeaderIteratorImpl iterator=(HeaderIteratorImpl)
                                                                returnObject;
                            testResults.println("          TEST OK");
                            testResults.println();
                            while ( iterator.hasNext() ) {
                              ((HeaderImpl)iterator.next()).testSetMethods(testResults);
                            }
                        }
                    }
                    else { 
                        String content=methodString.substring
                                                    (3,methodString.length());
                        testResults.println();
                        testResults.println
                        ("                       NO "+ content +" TO TEST");
                        testResults.println();
                    }
                }
            }
        }
        catch (IllegalAccessException e) {
              testResults.println(e);
        }
        catch (InvocationTargetException e) {
               testResults.println(e);
               e.printStackTrace();
               Throwable t=e.getTargetException(); 
               testResults.println(((SipParseException)t).getUnparsable());
        }
        catch (HeaderParseException e) {
              testResults.println(e);
        }
        catch (NoSuchElementException e) {
              testResults.println(e);
        }
        catch (ClassNotFoundException e) {
                testResults.println(e);
                e.printStackTrace(); 
        }
        catch (InstantiationException e) {
                testResults.println(e);
                e.printStackTrace(); 
        }
    }
    
    
    
    public SIPMessage getSIPMessage() {
        return sipMessage;
    }
    
}
