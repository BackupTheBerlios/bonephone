
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author:  O .Deruelle (deruelle@antd.nist.gov)	                               *
* Modificiations: M. Ranganathan (mranga@nist.gov)			       *
*   Used the sipMessage memeber of the superclass                              *
*   Modified set methods for all list arguments 		               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/*
 * RequestImpl.java
 *
 * Created on April 25, 2001, 3:33 PM
 */

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
import  gov.nist.sip.PackageNames;
import  java.util.*;
import  gov.nist.log.*;

/**
 * Implementation of the jain.protocol.ip.sip.Request interface.
 * Each request/response has an embedded NIST request object.
 * @author   O. Deruelle (deruelle@nist.gov)                
 * @version 1.0
 */
public class RequestImpl extends MessageImpl 
    implements Request {

    
    /** Creates new RequestImpl */
    public RequestImpl() {
        SIPRequest sipRequest = new SIPRequest();
        RequestLine requestLine = new RequestLine();
        sipRequest.setRequestLine(requestLine);
	super.sipMessage = sipRequest;
    }
    
    /**
     * Create a Request from a NIST-SIP request.
     */
    public RequestImpl(SIPRequest req) {
	super();
        this.sipMessage = (SIPMessage) req;
    }
    
    
    /**
     *Create a request from a string.
     */
    public RequestImpl(String request)
    throws SipParseException {
	if (LogWriter.needsLogging()) 
		LogWriter.logMessage("request = " + request);
        StringMsgParser stringMsgParser = new StringMsgParser();
	try {
           SIPMessage messages[] = stringMsgParser.parseSIPMessage(request);
           sipMessage = messages[0];
	} catch ( SIPParseException ex) {
		throw new SipParseException("bad request");

	}
    }
        

    /**
     * Adds ViaHeader to top of Request's ViaHeaders.
     * @param <var>viaHeader</var> ViaHeader to add
     * @throws IllegalArgumentException if viaHeader is null or not from same
     * JAIN SIP implementation
    */
    public  void addViaHeader(ViaHeader viaHeader) 
        throws IllegalArgumentException {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
	if (LogWriter.needsLogging()) 
	    LogWriter.logMessage("addViaHeader " + viaHeader);

        if (viaHeader == null) 
                throw new IllegalArgumentException("null arg!");
        ViaHeaderImpl vhi = (ViaHeaderImpl) viaHeader;
        Via via = (Via) vhi.getImplementationObject();
        if (via == null) 
                throw new IllegalArgumentException("null header");
        ViaList viaList = new ViaList();
        viaList.add(via);
        try {
            sipRequest.attachHeader(viaList,false);
        } catch (SIPDuplicateHeaderException ex) {
            // Should not happen.
	    LogWriter.logException(ex);
            System.exit(0);
        }
    }
    
    /**
     * Gets method of Request.
     * @return method of Request
     * @throws SipParseException if implementation cannot parse method
    */
    public String getMethod() throws SipParseException {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
        return sipRequest.getMethod();  
    }
    
    /**
     * Sets method of Request.
     * @param <var>method</var> method set
     * @throws IllegalArgumentException if method is null
     * @throws SipParseException if implementation cannot parse method
    */
    public void setMethod(String method) 
        throws IllegalArgumentException, SipParseException {
	    if (method == null) 
		throw new IllegalArgumentException("null method");
	    SIPRequest sipRequest = (SIPRequest) sipMessage;
            sipRequest.setMethod(method);
    }
    
    /**
     * Gets Request URI of Request.
     * @return Request URI of Request
     * @throws SipParseException if implementation cannot parse Request URI
     */
    public URI getRequestURI() throws SipParseException {
        SIPRequest sipRequest = (SIPRequest) sipMessage;
        gov.nist.sip.net.URI uri = sipRequest.getRequestURI();
	if (uri.getScheme().equals(SIPKeywords.SIP)) return new SipURLImpl(uri);
	else return new URIImpl(uri);
    }
    
    /**
     * Sets RequestURI of Request.
     * @param <var>requestURI</var> Request URI to set
     * @throws IllegalArgumentException if requestURI is null or not from same
     * JAIN SIP implementation
    */
    public void setRequestURI(URI requestURI) 
        throws IllegalArgumentException {
	    if (requestURI == null) 
		throw new IllegalArgumentException("null arg");
	    SIPRequest sipRequest = (SIPRequest) sipMessage;
	    if (requestURI instanceof URIImpl) {
                URIImpl uriImpl = (URIImpl) requestURI;
                gov.nist.sip.net.URI uri = uriImpl.getImplementationObject();
                sipRequest.setRequestURI(uri);
	    } else if (requestURI instanceof SipURLImpl) {
                SipURLImpl sipUrl = (SipURLImpl) requestURI;
                gov.nist.sip.net.URI uri = sipUrl.getImplementationObject();
                sipRequest.setRequestURI(uri);
	    } else 
		throw new IllegalArgumentException
				("URI not from same implementation.");
    }
    
    /**
     * Gets AuthorizationHeader of Request.
     * (Returns null if no AuthorizationHeader exists)
     * @return AuthorizationHeader of Request
     * @throws HeaderParseException if implementation cannot parse header value
     */
    public  AuthorizationHeader getAuthorizationHeader() 
    throws HeaderParseException {
        SIPRequest sipRequest = (SIPRequest) sipMessage;
        Authorization authorization=sipRequest.getAuthorizationHeader();
        if (authorization==null)   return null;
        else {
             AuthorizationHeaderImpl authImpl=new AuthorizationHeaderImpl
					(authorization);
             return authImpl;
        }
    }
    
    /**
     * Gets boolean value to indicate if Request
     * has AuthorizationHeader
     * @return boolean value to indicate if Request
     * has AuthorizationHeader
    */
    public  boolean hasAuthorizationHeader() {
	  SIPRequest sipRequest = (SIPRequest) sipMessage;
          Authorization authorization=sipRequest.getAuthorizationHeader();
          return  (authorization!=null);
    }
    
    /**
     * Sets AuthorizationHeader of Request.
     * @param <var>authorizationHeader</var> AuthorizationHeader to set
     * @throws IllegalArgumentException if authorizationHeader is null
     * or not from same JAIN SIP implementation
     */
    public void 
	setAuthorizationHeader(AuthorizationHeader authorizationHeader)
        throws IllegalArgumentException {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
        if (authorizationHeader==null)
            throw new IllegalArgumentException
            ("authorizationHeader is null ");
        else {
              if ( authorizationHeader instanceof AuthorizationHeaderImpl) 
              {
                AuthorizationHeaderImpl authImpl=(AuthorizationHeaderImpl)
                                                        authorizationHeader;
                Authorization authorization=(Authorization)
                                            authImpl.getImplementationObject();
                sipRequest.setHeader(authorization);
              }
              else     throw new IllegalArgumentException
 		("authorizationHeader is not from the same implementation");
        }  
    }
    
    /**
     * Removes AuthorizationHeader from Request (if it exists)
     */
    public  void removeAuthorizationHeader() {
       SIPRequest sipRequest = (SIPRequest) sipMessage;
       if (LogWriter.needsLogging()) 
	   LogWriter.logMessage("removeAuthorizationHeader");
       try {
         sipRequest.removeAll(Class.forName
			(PackageNames.SIPHEADERS_PACKAGE + 
				".Authorization"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }
    }
    
    /**
     * Gets HideHeader of Request.
     * (Returns null if no AuthorizationHeader exists)
     * @return HideHeader of Request
     * @throws HeaderParseException if implementation cannot parse header value
     */
    public HideHeader getHideHeader() throws HeaderParseException {
	  SIPRequest sipRequest = (SIPRequest) sipMessage;
          Hide hide=sipRequest.getHideHeader();
        if (hide==null)   return null;
        else {
             HideHeaderImpl hideImpl=new HideHeaderImpl(hide);
             return hideImpl;
        }
    }
    
    /**
     * Gets boolean value to indicate if Request
     * has HideHeader
     * @return boolean value to indicate if Request
     * has HideHeader
     */
    public boolean hasHideHeader() {
	  SIPRequest sipRequest = (SIPRequest) sipMessage;
          Hide hide=sipRequest.getHideHeader();
          return  (hide!=null);
    }
    
    /**
     * Sets HideHeader of Request.
     * @param <var>hideHeader</var> HideHeader to set
     * @throws IllegalArgumentException if hideHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setHideHeader(HideHeader hideHeader)
    throws IllegalArgumentException {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
        if (hideHeader==null)
            throw new IllegalArgumentException
            (" hideHeader is null ");
        else {
              if ( hideHeader instanceof HideHeaderImpl) 
              {
                HideHeaderImpl hideImpl=(HideHeaderImpl)
                                                      hideHeader;
                Hide hide=(Hide) hideImpl.getImplementationObject();
                sipRequest.setHeader(hide);
              }
              else     throw new IllegalArgumentException
        	("hideHeader is not from the same implementation");
        }  
    }
    
    /**
     * Removes HideHeader from Request (if it exists)
     */
    public void removeHideHeader() {
       SIPRequest sipRequest = (SIPRequest) sipMessage;
       try {
         sipRequest.removeAll(Class.forName
			(PackageNames.SIPHEADERS_PACKAGE + ".Hide"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }
    }
    
    /**
     * Gets MaxForwardsHeader of Request.
     * (Returns null if no MaxForwardsHeader exists)
     * @return MaxForwardsHeader of Request
     * @throws HeaderParseException if implementation cannot parse header value
     */
    public MaxForwardsHeader getMaxForwardsHeader() 
	throws HeaderParseException {
	 SIPRequest sipRequest = (SIPRequest) sipMessage;
        MaxForwards maxForwards=sipRequest.getMaxForwardsHeader();
        if (maxForwards==null)   return null;
        else {
             MaxForwardsHeaderImpl maxForwardsImpl=
			new MaxForwardsHeaderImpl(maxForwards);
             return maxForwardsImpl;
        } 
    }
    
    /**
     * Gets boolean value to indicate if Request
     * has MaxForwardsHeader
     * @return boolean value to indicate if Request
     * has MaxForwardsHeader
     */
    public boolean hasMaxForwardsHeader() {
	  SIPRequest sipRequest = (SIPRequest) sipMessage;
          MaxForwards maxForwards=sipRequest.getMaxForwardsHeader();
          return  (maxForwards!=null);
    }
    
    /**
     * Sets MaxForwardsHeader of Request.
     * @param <var>maxForwardsHeader</var> MaxForwardsHeader to set
     * @throws IllegalArgumentException if maxForwardsHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setMaxForwardsHeader(MaxForwardsHeader maxForwardsHeader)
    throws IllegalArgumentException {
	 SIPRequest sipRequest = (SIPRequest) sipMessage;
         if (maxForwardsHeader==null)
            throw new IllegalArgumentException
            (" maxForwardsHeader is null ");
        else {
              if ( maxForwardsHeader instanceof MaxForwardsHeaderImpl) 
              {
                MaxForwardsHeaderImpl maxForwardsImpl=(MaxForwardsHeaderImpl)
                                                      maxForwardsHeader;
                MaxForwards maxForwards=(MaxForwards)
                                    maxForwardsImpl.getImplementationObject();
                sipRequest.setHeader(maxForwards);
              }
              else     throw new IllegalArgumentException
  		("maxForwardsHeader is not from the same implementation");
        }   
    }
    
    /**
     * Removes MaxForwardsHeader from Request (if it exists)
     */
    public void removeMaxForwardsHeader() {
       SIPRequest sipRequest = (SIPRequest) sipMessage;
       try {
         sipRequest.removeAll(Class.forName
			(PackageNames.SIPHEADERS_PACKAGE + ".MaxForwards"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }
    }
    
    /**
     * Gets ProxyAuthorizationHeader of Request.
     * (Returns null if no ProxyAuthorizationHeader exists)
     * @return ProxyAuthorizationHeader of Request
     * @throws HeaderParseException if implementation cannot parse header value
     */
    public ProxyAuthorizationHeader getProxyAuthorizationHeader() 
    throws HeaderParseException {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
        ProxyAuthorization proxyAuthorization =
                                sipRequest.getProxyAuthorizationHeader();
        if (proxyAuthorization==null)   return null;
        else {
             ProxyAuthorizationHeaderImpl proxyAuthorizationImpl=
                            new ProxyAuthorizationHeaderImpl
				(proxyAuthorization);
             return proxyAuthorizationImpl;
        }  
    }
    
    /**
     * Gets boolean value to indicate if Request
     * has ProxyAuthorizationHeader
     * @return boolean value to indicate if Request
     * has ProxyAuthorizationHeader
     */
    public boolean hasProxyAuthorizationHeader() {
	 SIPRequest sipRequest = (SIPRequest) sipMessage;
         ProxyAuthorization proxyAuthorization=
                            sipRequest.getProxyAuthorizationHeader();
          return  (proxyAuthorization!=null);
    }
    
    /**
     * Sets ProxyAuthorizationHeader of Request.
     * @param <var>proxyAuthorizationHeader</var> ProxyAuthorizationHeader 
     * to set
     * @throws IllegalArgumentException if proxyAuthorizationHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setProxyAuthorizationHeader
    (ProxyAuthorizationHeader proxyAuthorizationHeader) 
    throws IllegalArgumentException {
	 SIPRequest sipRequest = (SIPRequest) sipMessage;
         if (proxyAuthorizationHeader==null)
            throw new IllegalArgumentException
            (" proxyAuthorizationHeader is null ");
        else {
           if ( proxyAuthorizationHeader 
		instanceof ProxyAuthorizationHeaderImpl) 
              {
                ProxyAuthorizationHeaderImpl proxyImpl=
                      (ProxyAuthorizationHeaderImpl)proxyAuthorizationHeader;
                ProxyAuthorization proxyAuthorization=(ProxyAuthorization)
                                   proxyImpl.getImplementationObject();
                sipRequest.setHeader(proxyAuthorization);
              }
              else     throw new IllegalArgumentException
		("proxyAuthorizationHeader not from the same implementation");
        }   
    }
    
    /**
     * Removes ProxyAuthorizationHeader from Request (if it exists)
     */
    public void removeProxyAuthorizationHeader() {
       SIPRequest sipRequest = (SIPRequest) sipMessage;
       try {
         sipRequest.removeAll(Class.forName
			(PackageNames.SIPHEADERS_PACKAGE +
					 ".ProxyAuthorization"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }
    }
    
    /**
     * Gets HeaderIterator of ProxyRequireHeaders of Request.
     * (Returns null if no ProxyRequireHeaders exist)
     * @return HeaderIterator of ProxyRequireHeaders of Request
     */
    public HeaderIterator getProxyRequireHeaders() {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
        ProxyRequireList proxyRequireList =
                                sipRequest.getProxyRequireHeaders();
        if (proxyRequireList==null)   return null;
        else {
             HeaderIteratorImpl headerIterator=
                        new HeaderIteratorImpl(proxyRequireList);
             return headerIterator;
        }   
    }
    
    /**
     * Gets boolean value to indicate if Request
     * has ProxyRequireHeaders
     * @return boolean value to indicate if Request
     * has ProxyRequireHeaders
     */
    public boolean hasProxyRequireHeaders() {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
       ProxyRequireList proxyRequireList =
                                sipRequest.getProxyRequireHeaders();
       return (proxyRequireList!=null);
    }
    
    /**
     * Sets ProxyRequireHeaders of Request.
     * @param <var>proxyRequireHeaders</var> List of ProxyRequireHeaders to set
     * @throws IllegalArgumentException if proxyRequireHeaders is null, empty,
     * contains any elements that are null or not ProxyRequireHeaders from the 
     * same JAIN SIP implementation
     */
    public void setProxyRequireHeaders(List proxyRequireHeaders)
    throws IllegalArgumentException {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
        if ( proxyRequireHeaders==null )
            throw new IllegalArgumentException
            (" proxyRequireHeaders is null ");
        if ( proxyRequireHeaders.isEmpty() )
            throw new IllegalArgumentException
            (" proxyRequireHeaders is empty ");
        ProxyRequireList proxyRequireList = new ProxyRequireList();
        for (int i=0;i<proxyRequireHeaders.size();i++)
        {
            Object obj = proxyRequireHeaders.get(i);
            if ( obj==null)
                throw new IllegalArgumentException
                (" routeHeaders contains null elements ");
            if (!(obj instanceof ProxyRequireHeaderImpl )) 
                throw new IllegalArgumentException
                ("routeHeaders is not from the same implementation");
            proxyRequireList.add(((HeaderImpl)obj).getImplementationObject());
        }
        sipRequest.setHeader(proxyRequireList);
    }
    
    /**
     * Removes ProxyRequireHeaders from Request (if any exist)
     */
    public void removeProxyRequireHeaders() {
       SIPRequest sipRequest = (SIPRequest) sipMessage;
       try {
         sipRequest.removeAll(Class.forName
			(PackageNames.SIPHEADERS_PACKAGE + 
				".ProxyRequireList"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }
    }
    
    /**
     * Gets HeaderIterator of RequireHeaders of Request.
     * (Returns null if no RequireHeaders exist)
     * @return HeaderIterator of RequireHeaders of Request
     */
    public HeaderIterator getRequireHeaders() {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
        RequireList requireList =
                                sipRequest.getRequireHeaders();
        if ( requireList==null)   return null;
        else {
             HeaderIteratorImpl headerIterator=
                        new HeaderIteratorImpl(requireList);
             return headerIterator;
        }   
    }
    
    /**
     * Gets boolean value to indicate if Request
     * has RequireHeaders
     * @return boolean value to indicate if Request
     * has RequireHeaders
     */
    public boolean hasRequireHeaders() {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
       RequireList requireList = sipRequest.getRequireHeaders();
       return (requireList!=null);
    }
    
    /**
     * Sets RequireHeaders of Request.
     * @param <var>requireHeaders</var> List of RequireHeaders to set
     * @throws IllegalArgumentException if requireHeaders is null, empty, 
     * contains any elements that are null or not RequireHeaders from the same
     * JAIN SIP implementation
     */
    public void setRequireHeaders(List requireHeaders)
    throws IllegalArgumentException {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
       if ( requireHeaders==null )
            throw new IllegalArgumentException
            (" requireHeaders is null ");
        if ( requireHeaders.isEmpty() )
            throw new IllegalArgumentException
            (" requireHeaders is empty ");
	RequireList requireList = new RequireList();
        for (int i=0;i<requireHeaders.size();i++)
        {
            Object obj=requireHeaders.get(i);
            if ( obj==null)
                throw new IllegalArgumentException
           (" requireHeaders contains null elements ");
            if (!(obj instanceof RequireHeaderImpl)) 
                throw new IllegalArgumentException
           ("requireHeaders is not from the same implementation");
            requireList.add(((HeaderImpl)obj).getImplementationObject());
        }
        sipRequest.setHeader(requireList); 
    }
    
    /**
     * Removes RequireHeaders from Request (if any exist)
     */
    public void removeRequireHeaders() {
       SIPRequest sipRequest = (SIPRequest) sipMessage;
       try {
         sipRequest.removeAll(Class.forName
		(PackageNames.SIPHEADERS_PACKAGE + ".RequireList"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }
    }
    
    /**
     * Gets HeaderIterator of RouteHeaders of Request.
     * (Returns null if no RouteHeaders exist)
     * @return HeaderIterator of RouteHeaders of Request
     */
    public HeaderIterator getRouteHeaders() {
        SIPRequest sipRequest = (SIPRequest) sipMessage;
        RouteList routeList = sipRequest.getRouteHeaders();
        if ( routeList==null)   return null;
        else {
             HeaderIteratorImpl headerIterator=
                        new HeaderIteratorImpl(routeList);
             return headerIterator;
        }   
    }
    
    /**
     * Gets boolean value to indicate if Request
     * has RouteHeaders
     * @return boolean value to indicate if Request
     * has RouteHeaders
     */
    public boolean hasRouteHeaders() {
        SIPRequest sipRequest = (SIPRequest) sipMessage;
       RouteList routeList = sipRequest.getRouteHeaders();
       return (routeList!=null);
    }
    
    /**
     * Sets RouteHeaders of Request.
     * @param <var>routeHeaders</var> List of RouteHeaders to set
     * @throws IllegalArgumentException if routeHeaders is null, empty, contains
     * any elements that are null or not RouteHeaders from the same
     * JAIN SIP implementation
     */
    public void setRouteHeaders(List routeHeaders)
    throws IllegalArgumentException {
        SIPRequest sipRequest = (SIPRequest) sipMessage;
        if ( routeHeaders==null )
            throw new IllegalArgumentException
            (" routeHeaders is null ");
        if ( routeHeaders.isEmpty() )
            throw new IllegalArgumentException
            (" routeHeaders is empty ");
        RouteList routeList = new RouteList();
        for (int i=0;i<routeHeaders.size();i++)
        {
            Object obj = routeHeaders.get(i);
            if ( obj==null)
                throw new IllegalArgumentException
                (" routeHeaders contains null elements ");
            if (!(obj instanceof RouteHeaderImpl )) 
                throw new IllegalArgumentException
                ("routeHeaders is not from the same implementation");
            RouteHeaderImpl routeImpl = (RouteHeaderImpl) obj;
            Route route = (Route) routeImpl.getImplementationObject();
            routeList.add(route);
        }
        sipRequest.setHeader(routeList); 
  }
    
    /**
     * Removes RouteHeaders from Request (if any exist)
     */
    public void removeRouteHeaders() {
       SIPRequest sipRequest = (SIPRequest) sipMessage;
       try {
         sipRequest.removeAll(Class.forName(PackageNames.SIPHEADERS_PACKAGE + 
		".RouteList"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }
    }
    
    /**
     * Gets ResponseKeyHeader of Request.
     * (Returns null if no ResponseKeyHeader exists)
     * @return ResponseKeyHeader of Request
     * @throws HeaderParseException if implementation cannot parse header value
     */
    public ResponseKeyHeader getResponseKeyHeader() 
	throws HeaderParseException {
        SIPRequest sipRequest = (SIPRequest) sipMessage;
        ResponseKey responseKey=sipRequest.getResponseKeyHeader();
        if (responseKey==null)   return null;
        else {
             ResponseKeyHeaderImpl responseKeyImpl=
		new ResponseKeyHeaderImpl(responseKey);
             return responseKeyImpl;
        } 
    }
    
    /**
     * Gets boolean value to indicate if Request
     * has ResponseKeyHeader
     * @return boolean value to indicate if Request
     * has ResponseKeyHeader
     */
    public boolean hasResponseKeyHeader() {
        SIPRequest sipRequest = (SIPRequest) sipMessage;
         ResponseKey responseKey=sipRequest.getResponseKeyHeader();
         return  (responseKey!=null);
    }
    
    /**
     * Sets ResponseKeyHeader of Request.
     * @param <var>responseKeyHeader</var> ResponseKeyHeader to set
     * @throws IllegalArgumentException if responseKeyHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setResponseKeyHeader(ResponseKeyHeader responseKeyHeader)
    throws IllegalArgumentException {
        SIPRequest sipRequest = (SIPRequest) sipMessage;
        if (responseKeyHeader==null)
            throw new IllegalArgumentException
            (" responseKeyHeader is null ");
        else {
              if ( responseKeyHeader instanceof ResponseKeyHeaderImpl) 
              {
                ResponseKeyHeaderImpl responseKeyImpl=
                    (ResponseKeyHeaderImpl) responseKeyHeader;
                ResponseKey responseKey=
                    (ResponseKey)responseKeyImpl.getImplementationObject();
                sipRequest.setHeader(responseKey);
              }
              else throw new IllegalArgumentException
    		("responseHeader is not from the same implementation");
        }  
    }
    
    /**
     * Removes ResponseKeyHeader from Request (if it exists)
     */
    public void removeResponseKeyHeader() {
       SIPRequest sipRequest = (SIPRequest) sipMessage;
       try {
         sipRequest.removeAll(Class.forName(PackageNames.SIPHEADERS_PACKAGE
		+ ".ResponseKey"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }
    }
    
    /**
     * Gets PriorityHeader of Request.
     * (Returns null if no PriorityHeader exists)
     * @return PriorityHeader of Request
     * @throws HeaderParseException if implementation cannot parse header value
     */
    public PriorityHeader getPriorityHeader() 
	throws HeaderParseException {
       
        SIPRequest sipRequest = (SIPRequest) sipMessage;
        Priority priority = sipRequest.getPriorityHeader();
        if ( priority==null)   return null;
        else {
             PriorityHeaderImpl priorityImpl=new PriorityHeaderImpl(priority);
             return priorityImpl;           
        }     
    }
    
    /**
     * Gets boolean value to indicate if Request
     * has PriorityHeader
     * @return boolean value to indicate if Request
     * has PriorityHeader
     */
    public boolean hasPriorityHeader() {
        SIPRequest sipRequest = (SIPRequest) sipMessage;
         Priority priority=sipRequest.getPriorityHeader();
         return  (priority!=null);
    }
    
    /**
     * Sets PriorityHeader of Request.
     * @param <var>priorityHeader</var> PriorityHeader to set
     * @throws IllegalArgumentException if priorityHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setPriorityHeader(PriorityHeader priorityHeader) 
    throws IllegalArgumentException {
        SIPRequest sipRequest = (SIPRequest) sipMessage;
        if (priorityHeader==null)
            throw new IllegalArgumentException
            (" priorityHeader is null ");
        else {
              if ( priorityHeader instanceof PriorityHeaderImpl) 
              {
                PriorityHeaderImpl priorityImpl=
                    (PriorityHeaderImpl) priorityHeader;
                Priority priority=
                    (Priority)priorityImpl.getImplementationObject();
                sipRequest.setHeader(priority);
              }
              else     throw new IllegalArgumentException
  		("priorityHeader is not from the same implementation");
        }  
    }
    
    /**
     * Removes PriorityHeader from Request (if it exists)
     */
    public void removePriorityHeader() {
       SIPRequest sipRequest = (SIPRequest) sipMessage;
       try {
         sipRequest.removeAll(Class.forName
			(PackageNames.SIPHEADERS_PACKAGE + ".Priority"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }
    }
    
    /**
     * Gets SubjectHeader of InviteMessage.
     * (Returns null if no SubjectHeader exists)
     * @return SubjectHeader of InviteMessage
     * @throws HeaderParseException if implementation cannot parse header value
     */
    public SubjectHeader getSubjectHeader() throws HeaderParseException {
        SIPRequest sipRequest = (SIPRequest) sipMessage;
       Subject subject= sipRequest.getSubjectHeader();
        if ( subject==null)   return null;
        else {
             SubjectHeaderImpl subjectImpl=new SubjectHeaderImpl(subject);
             return subjectImpl; 
        }      
    }
    
    /**
     * Gets boolean value to indicate if Request
     * has SubjectHeader
     * @return boolean value to indicate if Request
     * has SubjectHeader
     */
    public boolean hasSubjectHeader() {
	 SIPRequest sipRequest = (SIPRequest) sipMessage;
         Subject subject=sipRequest.getSubjectHeader();
         return  (subject!=null);  
    }
    
    /**
     * Sets SubjectHeader of InviteMessage.
     * @param <var>subjectHeader</var> SubjectHeader to set
     * @throws IllegalArgumentException if subjectHeader is null
     * or not from same JAIN SIP implementation
     */
    public void setSubjectHeader(SubjectHeader subjectHeader) 
    throws IllegalArgumentException {
	SIPRequest sipRequest = (SIPRequest) sipMessage;
        if (subjectHeader==null)
            throw new IllegalArgumentException
            ("subjectHeader is null ");
        else {
              if ( subjectHeader instanceof SubjectHeaderImpl) 
              {
                SubjectHeaderImpl subjectImpl=
                    (SubjectHeaderImpl) subjectHeader;
                Subject subject=
                    (Subject)subjectImpl.getImplementationObject();
                sipRequest.setHeader(subject);
              }
              else     throw new IllegalArgumentException
  		("subjectHeader is not from the same implementation");
        }  
    }
    
    /**
     * Removes SubjectHeader from Request (if it exists)
     */
    public void removeSubjectHeader() {
       SIPRequest sipRequest = (SIPRequest) sipMessage;
       try {
         sipRequest.removeAll(Class.forName
			(PackageNames.SIPHEADERS_PACKAGE + ".Subject"));
       } catch (ClassNotFoundException ex) {
	   System.out.println("Internal Error!");
	   ex.printStackTrace();
	   System.exit(0);
       }
    }
  
}
