/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author : C. Chazeau (chazeau@antd.nist.gov)                                  *
* Modified by: M. Ranganathan   (mranga@nist.gov)                              *
*  - added Server and ProxyAuthroization support                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package gov.nist.jain.protocol.ip.sip.message;

import  jain.protocol.ip.sip.message.*;
import  jain.protocol.ip.sip.header.*;
import  jain.protocol.ip.sip.address.*;
import  jain.protocol.ip.sip.*;

import  gov.nist.sip.msgparser.*;
import  gov.nist.jain.protocol.ip.sip.header.*;
import  gov.nist.jain.protocol.ip.sip.message.*;
import  gov.nist.jain.protocol.ip.sip.address.*;
import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.*;
import  gov.nist.sip.PackageNames;
import  java.util.*;

/**
 *Wrapper object that implements the JAIN Response interface.
 * Each Response has a corresponding embedded SIPResponse structure.
 * @author  Christophe Chazeau
 * @version  1.0
 */
public class ResponseImpl extends MessageImpl implements Response{


    
    /**
     *Constructor.
     */
    public ResponseImpl() {
        sipMessage = new SIPResponse();
        StatusLine statusLine = new StatusLine();
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        sipResponse.setStatusLine(statusLine);
        super.sipMessage = sipResponse ;
    }
    
    /**
     *Constructor given a message string.
     */
    public ResponseImpl (String responseString) 
        throws SipParseException {
        super(responseString);
    }
        
    /**
     *Constructor.
     */
    public ResponseImpl(SIPResponse sip_response) {
        sipMessage = (SIPMessage) sip_response ;
    }
        
    /**
     * Gets HeaderIterator of AllowHeaders of Response.
     * (Returns null if no AllowHeaders exist)
     * @return HeaderIterator of AllowHeaders of Response
 */
    public HeaderIterator getAllowHeaders() {
	SIPResponse sipResponse = (SIPResponse) sipMessage;
        AllowList allowList =
                                sipResponse.getAllowHeaders();
        if (allowList==null)   return null;
        else {
             HeaderIteratorImpl headerIterator=
                        new HeaderIteratorImpl(allowList);
             return headerIterator;
        }           
    }
    
    /**
     * Gets boolean value to indicate if Response
     * has AllowHeaders
     * @return boolean value to indicate if Response
     * has AllowHeaders
 */
    public boolean hasAllowHeaders() {
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        AllowList allowList =
                                sipResponse.getAllowHeaders();
       return (allowList!=null);
    }
    
    /**
     * Sets AllowHeaders of Response.
     * @param <var>allowHeaders</var> List of AllowHeaders to set
     * @throws IllegalArgumentException if allowHeaders is null, empty, contains
     * any elements that are null or not AllowHeaders from the same
     * JAIN SIP implementation
 */
    public void setAllowHeaders(List allowHeaders) 
        throws IllegalArgumentException {
	
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        if ( allowHeaders==null )
            throw new IllegalArgumentException
            (" allowHeaders is null ");
        if ( allowHeaders.isEmpty() )
            throw new IllegalArgumentException
            (" allowHeaders is empty ");
        AllowList allowList = new AllowList();
        for (int i=0;i<allowHeaders.size();i++)
        {
            Object obj = allowHeaders.get(i);
            if (obj==null)
                throw new IllegalArgumentException
                (" allowHeaders contains null elements ");
            if (!(obj instanceof AllowHeaderImpl )) 
                throw new IllegalArgumentException
                ("allowHeaders is not from the same implementation");
            allowList.add(((HeaderImpl)obj).getImplementationObject());
        }
        sipResponse.setHeader(allowList);
    }
    
    /**
     * Removes AllowHeaders from Response (if any exist)
 */
    public void removeAllowHeaders() {
       SIPResponse sipResponse = (SIPResponse) sipMessage;
       try {
         sipResponse.removeAll(Class.forName
			(PackageNames.SIPHEADERS_PACKAGE + 
				".AllowList"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }
    }
    
    /**
     * Gets ProxyAuthenticateHeader of Response.
     * (Returns null if no ProxyAuthenticateHeader exists)
     * @return HideHeader of Response
     * @throws HeaderParseException if implementation cannot parse header value
    */
    public ProxyAuthenticateHeader getProxyAuthenticateHeader(){ 
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        ProxyAuthenticateList proxyAuthList = 
                sipResponse.getProxyAuthenticateHeaders();
        if (proxyAuthList != null) {
            ProxyAuthenticate pa = (ProxyAuthenticate) proxyAuthList.getFirst();
            ProxyAuthenticateHeaderImpl pah = new ProxyAuthenticateHeaderImpl();
            pah.setImplementationObject(pa);
            return pah;
        } else return null;
          
   }
    
    /**
     * Gets boolean value to indicate if Response
     * has ProxyAuthenticateHeader
     * @return boolean value to indicate if Response
     * has ProxyAuthenticateHeader
 */
    public boolean hasProxyAuthenticateHeader() {

        SIPResponse sipResponse = (SIPResponse) sipMessage;
        ProxyAuthenticateList proxyAuthenticateList =
                                sipResponse.getProxyAuthenticateHeaders();
       return (proxyAuthenticateList!=null);
    }        

    
    /**
     * Removes ProxyAuthenticateHeader from Response (if it exists)
 */
    public void removeProxyAuthenticateHeader() {

       SIPResponse sipResponse = (SIPResponse) sipMessage;
       try {
         sipResponse.removeAll(Class.forName
			(PackageNames.SIPHEADERS_PACKAGE + 
				".ProxyAuthenticateList"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }        
  
    }
    
    /**
     * Sets ProxyAuthenticateHeader of Response.
     * @param <var>ProxyAuthenticateHeader</var> ProxyAuthenticateHeader to set
     * @throws IllegalArgumentException if proxyAuthenticateHeader is null
     * or not from same JAIN SIP implementation
 */
    public void setProxyAuthenticateHeader
        (ProxyAuthenticateHeader proxyAuthenticateHeader) 
            throws IllegalArgumentException {
		if (proxyAuthenticateHeader == null) 
		   throw new IllegalArgumentException("null arg");
		if (!(proxyAuthenticateHeader
			instanceof ProxyAuthenticateHeaderImpl) )
		    throw new IllegalArgumentException
			("bad header class (wrong implementation)");
                ProxyAuthenticateHeaderImpl proxyAuthHeaderImpl =
                        (ProxyAuthenticateHeaderImpl) proxyAuthenticateHeader;
                ProxyAuthenticate proxyAuthenticate = 
                   (ProxyAuthenticate)  
                   proxyAuthHeaderImpl.getImplementationObject();
                ProxyAuthenticateList proxyAuthList = 
                    new ProxyAuthenticateList();
                proxyAuthList.add(proxyAuthenticate);
                SIPResponse sipResponse = (SIPResponse) sipMessage;
                sipResponse.setHeader(proxyAuthList);
    }
    
    /**
     * Gets HeaderIterator of WWWAuthenticateHeaders of Response.
     * (Returns null if no WWWAuthenticateHeaders exist)
     * @return HeaderIterator of WWWAuthenticateHeaders of Response
     */
    public HeaderIterator getWWWAuthenticateHeaders() {
	
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        WWWAuthenticateList wwwAuthenticateList =
                                sipResponse.getWWWAuthenticateHeaders();
        if (wwwAuthenticateList==null)   return null;
        else {
             HeaderIteratorImpl headerIterator=
                        new HeaderIteratorImpl(wwwAuthenticateList);
             return headerIterator;
        }           
    }
    
    /**
     * Gets boolean value to indicate if Response
     * has WWWAuthenticateHeaders
     * @return boolean value to indicate if Response
     * has WWWAuthenticateHeaders
 */
    public boolean hasWWWAuthenticateHeaders() {
       
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        WWWAuthenticateList wwwAuthenticateList =
                                sipResponse.getWWWAuthenticateHeaders();
       return (wwwAuthenticateList!=null);
    }
    
    /**
     * Removes WWWAuthenticateHeaders from Response (if any exist)
    */
    public void removeWWWAuthenticateHeaders() {
       
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        sipResponse.removeAll
            (HeaderMap.getNISTHeaderClassFromJAINHeader
		(WWWAuthenticateHeader.name));
    }
    
    /**
     * Sets WWWAuthenticateHeaders of Response.
     * @param <var>wwwAuthenticateHeaders</var> List of WWWAuthenticateHeaders to set
     * @throws IllegalArgumentException if wwwAuthenticateHeaders is null, empty, contains
     * any elements that are null or not WWWAuthenticateHeaders from the same
     * JAIN SIP implementation
    */
    public void setWWWAuthenticateHeaders(List wwwAuthenticateHeaders) 
        throws IllegalArgumentException {
   
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        if ( wwwAuthenticateHeaders==null )
            throw new IllegalArgumentException
            (" wwwAuthenticateHeaders is null ");
        if ( wwwAuthenticateHeaders.isEmpty() )
            throw new IllegalArgumentException
            (" wwwAuthenticateHeaders is empty ");
        WWWAuthenticateList wwwAuthenticateList = new WWWAuthenticateList();
        for (int i=0;i<wwwAuthenticateHeaders.size();i++)
        {
            Object obj = wwwAuthenticateHeaders.get(i);
            if (obj==null)
                throw new IllegalArgumentException
                (" wwwAuthenticateHeaders contains null elements ");
            if (!(obj instanceof WWWAuthenticateHeaderImpl )) 
                throw new IllegalArgumentException
                ("wwwAuthenticateHeaders is not from the same implementation");
           wwwAuthenticateList.add(((HeaderImpl)obj).getImplementationObject());
        }
        sipResponse.setHeader(wwwAuthenticateList);
    }
    
    /**
     * Gets ServerHeader of Response.
     * (Returns null if no ServerHeader exists)
     * @return ServerHeader of Response
     * @throws HeaderParseException if implementation cannot parse header value
    */
    public ServerHeader getServerHeader()
        throws HeaderParseException {
            SIPResponse sipResponse = (SIPResponse) sipMessage;
            Server server = sipResponse.getServerHeader();
            if (server == null )  return null;
            ServerHeaderImpl serverHeader = new ServerHeaderImpl();
            serverHeader.setImplementationObject(server);
            return serverHeader;    
   }
    
    /**
     * Gets boolean value to indicate if Response
     * has ServerHeader
     * @return boolean value to indicate if Response
     * has ServerHeader
     */
    public boolean hasServerHeader() {

        SIPResponse sipResponse = (SIPResponse) sipMessage;
        Server server= sipResponse.getServerHeader();
       return (server !=null);
    }
    
    /**
     * Removes ServerHeader from Response (if it exists)
 */
    public void removeServerHeader() {
        
       SIPResponse sipResponse = (SIPResponse) sipMessage;
       sipResponse.removeAll(
            HeaderMap.getNISTHeaderClassFromJAINHeader(ServerHeader.name));               
    }
    
    /**
     * Sets ServerHeader of Response. Note -- according to the RFC, 
     * there could be several Server headers but the JAIN spec allows only
     * one, so we add it to the top of the list.
     * @param <var>serverHeader</var> ServerHeader to set
     * @throws IllegalArgumentException if serverHeader is null
     * or not from same JAIN SIP implementation
    */
    public void setServerHeader(ServerHeader serverHeader) 
        throws IllegalArgumentException {
	    if (serverHeader == null) 
		throw new IllegalArgumentException("null arg");
	    else if (! (serverHeader instanceof ServerHeaderImpl)) 
		throw new IllegalArgumentException("Bad implementation");
		
            SIPResponse sipResponse = (SIPResponse)sipMessage;
            ServerHeaderImpl serverImpl = (ServerHeaderImpl)serverHeader;
            Server server = (Server) serverImpl.getImplementationObject();
            sipResponse.setHeader(server);
     }
    
    /**
     * Gets HeaderIterator of UnsupportedHeaders of Response.
     * (Returns null if no UnsupportedHeaders exist)
     * @return HeaderIterator of UnsupportedHeaders of Response
     */
    public HeaderIterator getUnsupportedHeaders() {
        
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        UnsupportedList unsupportedList =
                                sipResponse.getUnsupportedHeaders();
        if (unsupportedList==null)   return null;
        else {
             HeaderIteratorImpl headerIterator=
                        new HeaderIteratorImpl(unsupportedList);
             return headerIterator;
        }        
    }
    
    /**
     * Gets boolean value to indicate if Response
     * has UnsupportedHeaders
     * @return boolean value to indicate if Response
     * has UnsupportedHeaders
    */
    public boolean hasUnsupportedHeaders() {

        SIPResponse sipResponse = (SIPResponse) sipMessage;
        UnsupportedList unsupportedList =
                                sipResponse.getUnsupportedHeaders();
       return (unsupportedList!=null);
    }
    
    /**
     * Removes UnsupportedHeaders from Response (if any exist)
     */
    public void removeUnsupportedHeaders() {

       SIPResponse sipResponse = (SIPResponse) sipMessage;
       sipResponse.removeAll(
            HeaderMap.getNISTHeaderClassFromJAINHeader
			(UnsupportedHeader.name));      
    }
    
    /**
     * Sets UnsupportedHeaders of Response.
     * @param <var>unsupportedHeaders</var> List of UnsupportedHeaders to set
     * @throws IllegalArgumentException if unsupportedHeaders is null, empty, 
     * contains any elements that are null or not UnsupportedHeaders 
     * from the same JAIN SIP implementation
     */
    public void setUnsupportedHeaders(List unsupportedHeaders) 
        throws IllegalArgumentException {

        SIPResponse sipResponse = (SIPResponse) sipMessage;
        if ( unsupportedHeaders==null )
            throw new IllegalArgumentException
            (" unsupportedHeaders is null ");
        if ( unsupportedHeaders.isEmpty() )
            throw new IllegalArgumentException
            (" unsupportedHeaders is empty ");
        UnsupportedList unsupportedList = new UnsupportedList();
        for (int i=0;i<unsupportedHeaders.size();i++)
        {
            Object obj = unsupportedHeaders.get(i);
            if (obj==null)
                throw new IllegalArgumentException
                (" unsupportedHeaders contains null elements ");
            if (!(obj instanceof UnsupportedHeaderImpl )) 
                throw new IllegalArgumentException
                ("unsupportedHeaders is not from the same implementation");
            unsupportedList.add(((HeaderImpl)obj).getImplementationObject());
        }
        sipResponse.setHeader(unsupportedList);
    }
    
    /**
     * Gets HeaderIterator of WarningHeaders of Response.
     * (Returns null if no WarningHeaders exist)
     * @return HeaderIterator of WarningHeaders of Response
    */
    public HeaderIterator getWarningHeaders() {

        SIPResponse sipResponse = (SIPResponse) sipMessage;
        WarningList warningList =
                                sipResponse.getWarningHeaders();
        if (warningList==null)   return null;
        else {
             HeaderIteratorImpl headerIterator=
                        new HeaderIteratorImpl(warningList);
             return headerIterator;
        }                
    }
    
    /**
     * Gets boolean value to indicate if Response
     * has WarningHeaders
     * @return boolean value to indicate if Response
     * has WarningHeaders
    */
    public boolean hasWarningHeaders() {
       
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        WarningList warningList =
                                sipResponse.getWarningHeaders();
       return (warningList!=null);   
    }
    
    /**
     * Removes WarningHeaders from Response (if any exist)
     */
    public void removeWarningHeaders() {
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        sipResponse.removeAll
            (HeaderMap.getNISTHeaderClassFromJAINHeader(WarningHeader.name));
    }
    
    /**
     * Sets WarningHeaders of Response.
     * @param <var>warningHeaders</var> List of WarningHeaders to set
     * @throws IllegalArgumentException if warningHeaders is null, empty, 
     * contains any elements that are null or not WarningHeaders from the same
     * JAIN SIP implementation
     */
    public void setWarningHeaders(List warningHeaders) 
       throws IllegalArgumentException {

        SIPResponse sipResponse = (SIPResponse) sipMessage;
        if ( warningHeaders==null )
            throw new IllegalArgumentException
            (" warningHeaders is null ");
        if ( warningHeaders.isEmpty() )
            throw new IllegalArgumentException
            (" warningHeaders is empty ");
        WarningList warningList = new WarningList();
        for (int i=0;i<warningHeaders.size();i++)
        {
            Object obj = warningHeaders.get(i);
            if (obj==null)
                throw new IllegalArgumentException
                (" warningHeaders contains null elements ");
            if (!(obj instanceof WarningHeaderImpl )) 
                throw new IllegalArgumentException
                ("warningHeaders is not from the same implementation");
            warningList.add(((HeaderImpl)obj).getImplementationObject());
        }
        sipResponse.setHeader(warningList);         
    }
    
    /**
     * Gets status code of Response.
     * @return status code of Response
     * @throws SipParseException if implementation cannot parse status code
    */
    public int getStatusCode() throws SipParseException {
 	
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        StatusLine statusLine = sipResponse.getStatusLine();
        if (statusLine == null)
            throw new SipParseException("Status line not set");
        return statusLine.getStatusCode();  
    }
    
    /**
     * Sets status code of Response.
     * @param <var>statusCode</var> status code to set
     * @throws SipParseException if statusCode is not accepted by implementation
     */
    public void setStatusCode(int statusCode) 
        throws SipParseException {
        
        SIPResponse sipResponse = (SIPResponse) sipMessage;
	sipResponse.setStatusCode(statusCode);
    }
    
    /**
     * Gets reason phrase of Response.
     * @return reason phrase of Response
     * @throws SipParseException if implementation cannot 
     * parse reason phrase
     */
    public String getReasonPhrase() throws SipParseException {

        SIPResponse sipResponse = (SIPResponse) sipMessage;
        StatusLine statusLine = sipResponse.getStatusLine();
        return sipResponse.getReasonPhrase();  
    }
    
    /**
     * Sets reason phrase of Response.
     * @param <var>reasonPhrase</var> reason phrase to set
     * @throws IllegalArgumentException if reasonPhrase is null
     * @throws SipParseException if reasonPhrase is not accepted 
     * by implementation
     */
    public void setReasonPhrase(String reasonPhrase) 
        throws IllegalArgumentException, SipParseException {
        if(reasonPhrase == null)
               throw new
               IllegalArgumentException("setReasonPhrase : reasonPhrase null") ;
        SIPResponse sipResponse = (SIPResponse) sipMessage;
        StatusLine statusLine = sipResponse.getStatusLine();
        statusLine.setReasonPhrase(reasonPhrase);              
    }

    /**
     * Removes first ViaHeader from Response's ViaHeaders.
     */
    public void removeViaHeader(){
        SIPResponse sipResponse = (SIPResponse) sipMessage;   
        
        ViaList viaList = sipResponse.getViaHeaders() ;
        if (viaList == null) return;
        viaList.removeFirst() ;
    }
}
