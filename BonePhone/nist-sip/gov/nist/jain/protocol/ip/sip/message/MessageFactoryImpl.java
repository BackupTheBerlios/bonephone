/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).       *
 * See ../../../../doc/uncopyright.html for conditions of use.                 *
 * Author:  C. Chazeau (chazeau@antd.nist.gov)	                               *
 * Modificiations: M. Ranganathan (mranga@nist.gov)                            *
 *  Added support for generating SIP responses. from Requests.                 *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                              *
 *******************************************************************************/

/*
 * MessageFactoryImpl.java
 *
 * Created on May 4, 2001, 3:10 PM
 */

package gov.nist.jain.protocol.ip.sip.message;

import  gov.nist.sip.msgparser.*;
import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.stack.*;
import  jain.protocol.ip.sip.message.*;
import  jain.protocol.ip.sip.header.*;
import  jain.protocol.ip.sip.*;
import  java.util.List;
import  java.util.Hashtable;
import  java.util.Iterator;
import  java.util.ListIterator;
import  gov.nist.jain.protocol.ip.sip.header.*;
import  gov.nist.log.LogWriter;
import  gov.nist.sip.PackageNames;
import  jain.protocol.ip.sip.address.URI ;
import  gov.nist.jain.protocol.ip.sip.address.*;

/**
 *Implelentation of the jain.protocol.ip.sip.message.MesageFactory.
 * @author  Christophe Chazeau
 * @version 1.0
 */
public class MessageFactoryImpl implements MessageFactory  {
    
    /** Creates new MessageFactoryImpl */
    public MessageFactoryImpl() {}
    
    /**
     * Creates Request with body
     * @param <var>requestURI</var> Request URI
     * @param <var>method</var> Request method
     * @param <var>callIdHeader</var> CallIdHeader
     * @param <var>cSeqHeader</var> CSeqHeader
     * @param <var>fromHeader</var> FromHeader
     * @param <var>toHeader</var> ToHeader
     * @param <var>viaHeaders</var> ViaHeaders
     * @param <var>body</var> body of Request
     * @param <var>contentTypeHeader</var> ContentTypeHeader
     * @throws IllegalArgumentException if method or body are null,
     * if requestURI, callIdHeader, cSeqHeader, fromHeader, toHeader or
     * contentTypeHeader are null or
     * not from same JAIN SIP implementation, or if viaHeaders is null, empty,
     * contains any null elements, or contains any objects that are
     * not ViaHeaders from the same JAIN SIP implementation
     * @throws SipParseException if method or body are not accepted by
     * implementation
     */
    public Request createRequest(URI requestURI,
    String method,
    CallIdHeader callIdHeader,
    CSeqHeader cSeqHeader,
    FromHeader fromHeader,
    ToHeader toHeader,
    List viaHeaders,
    String body,
    ContentTypeHeader contentTypeHeader)
    throws IllegalArgumentException, SipParseException {
        
        if (requestURI == null       ||
        method == null           ||
        body == null             ||
        callIdHeader == null     ||
        cSeqHeader == null       ||
        fromHeader == null       ||
        toHeader == null         ||
        viaHeaders == null       ||
        viaHeaders.isEmpty()     ||
        contentTypeHeader == null)
            throw new IllegalArgumentException("null argument");
        
        for (int i=0;i<viaHeaders.size();i++){
            
            Object obj = viaHeaders.get(i);
            if (obj==null)
                throw new IllegalArgumentException
                (" viaHeaders contains null elements ");
            if (!(obj instanceof ViaHeaderImpl ))
                throw new IllegalArgumentException
                ("viaHeaders is not from the same implementation");
        }
        
        URIImpl uri;
        CallIdHeaderImpl cidHdr;
        CSeqHeader csHdr ;
        FromHeaderImpl fromHdr;
        ToHeaderImpl toHdr;
        ContentTypeHeaderImpl contentTypeHdr;
        
        try {
            uri = (URIImpl) requestURI;
            cidHdr = (CallIdHeaderImpl) callIdHeader;
            csHdr = (CSeqHeaderImpl) cSeqHeader ;
            fromHdr = (FromHeaderImpl) fromHeader;
            toHdr = (ToHeaderImpl) toHeader;
            contentTypeHdr = (ContentTypeHeaderImpl) contentTypeHeader;
        } catch (ClassCastException ex) {
            throw new
            IllegalArgumentException
            ("header not from this implementation");
        }
        
        RequestImpl reqImpl = new RequestImpl();
        SIPRequest sr = new SIPRequest() ;
        reqImpl.setImplementationObject(sr) ;
        reqImpl.setRequestURI(requestURI) ;
	reqImpl.setMethod(method);
        reqImpl.setCSeqHeader(csHdr) ;
        reqImpl.setCallIdHeader(callIdHeader) ;
        reqImpl.setFromHeader(fromHdr) ;
        reqImpl.setToHeader(toHdr) ;
        reqImpl.setViaHeaders(viaHeaders) ;
        reqImpl.setBody(body,contentTypeHdr) ;
        
        return reqImpl ;
    }
    
    /**
     * Creates Request with body
     * @param <var>requestURI</var> Request URI
     * @param <var>method</var> Request method
     * @param <var>callIdHeader</var> CallIdHeader
     * @param <var>cSeqHeader</var> CSeqHeader
     * @param <var>fromHeader</var> FromHeader
     * @param <var>toHeader</var> ToHeader
     * @param <var>viaHeaders</var> ViaHeaders
     * @param <var>body</var> body of Request
     * @param <var>contentTypeHeader</var> ContentTypeHeader
     * @throws IllegalArgumentException if method or body are null, 
     * if requestURI, callIdHeader, cSeqHeader, fromHeader, toHeader 
     * or contentTypeHeader are null or not from same JAIN SIP implementation, 
     * or if viaHeaders is null, empty, contains any null elements, 
     * or contains any objects that are not ViaHeaders
     * from the same JAIN SIP implementation
     * @throws SipParseException if method or body are 
     * not accepted by implementation.
     */
    public Request createRequest(URI requestURI,
    String method,
    CallIdHeader callIdHeader,
    CSeqHeader cSeqHeader,
    FromHeader fromHeader,
    ToHeader toHeader,
    List viaHeaders,
    byte body[],
    ContentTypeHeader contentTypeHeader)
    throws IllegalArgumentException, SipParseException {
	if (LogWriter.needsLogging()) {
		LogWriter.logMessage("requestURI = " + requestURI +
				     "\n method = "+method +
				     "\n body = " + body +
				     "\n cSeqHeader = " + cSeqHeader +
				     "\n fromHeader = " + fromHeader +
				     "\n toHeader = " + toHeader +
				     "\n viaHeader = " + viaHeaders +
				     "\n contentTypeHeader = " 
						+ contentTypeHeader);
	}
				     

        if (requestURI == null       ||
        method == null           ||
        body == null             ||
        callIdHeader == null     ||
        cSeqHeader == null       ||
        fromHeader == null       ||
        toHeader == null         ||
        viaHeaders == null       ||
        viaHeaders.isEmpty()     ||
        contentTypeHeader == null)
            throw new IllegalArgumentException("null argument");
        

        for (int i=0;i<viaHeaders.size();i++){
            Object obj = viaHeaders.get(i);
            if (obj==null)
                throw new IllegalArgumentException
                (" viaHeaders contains null elements ");
            if (!(obj instanceof ViaHeaderImpl ))
                throw new IllegalArgumentException
                ("viaHeaders is not from the same implementation");
        }
        
        URIImpl uri;
        CallIdHeaderImpl cidHdr;
        CSeqHeader csHdr ;
        FromHeaderImpl fromHdr;
        ToHeaderImpl toHdr;
        ContentTypeHeaderImpl contentTypeHdr;
        String strBody = new String(body) ;
        
        try {
            uri = (URIImpl) requestURI;
            cidHdr = (CallIdHeaderImpl) callIdHeader;
            csHdr = (CSeqHeaderImpl) cSeqHeader ;
            fromHdr = (FromHeaderImpl) fromHeader;
            toHdr = (ToHeaderImpl) toHeader;
            contentTypeHdr = (ContentTypeHeaderImpl) contentTypeHeader;
        } catch (ClassCastException ex) {
            throw new
            IllegalArgumentException
            ("header not from this implementation");
        }
        
        RequestImpl reqImpl = new RequestImpl();
        SIPRequest sr = new SIPRequest() ;
        reqImpl.setImplementationObject(sr) ;
	try {
          reqImpl.setRequestURI(requestURI) ;
	  reqImpl.setMethod(method);
          reqImpl.setCSeqHeader(csHdr) ;
          reqImpl.setCallIdHeader(callIdHeader) ;
          reqImpl.setFromHeader(fromHdr) ;
          reqImpl.setToHeader(toHdr) ;
          reqImpl.setViaHeaders(viaHeaders) ;
          reqImpl.setBody(body,contentTypeHdr) ;
	} catch (Exception ex) {
	   if (LogWriter.needsLogging()) LogWriter.logException(ex);
	}
        
        return reqImpl ;
        
    }
    
    /**
     * Creates Response without body
     * @param <var>statusCode</var> status code
     * @param <var>callIdHeader</var> CallIdHeader
     * @param <var>cSeqHeader</var> CSeqHeader
     * @param <var>fromHeader</var> FromHeader
     * @param <var>toHeader</var> ToHeader
     * @param <var>viaHeaders</var> ViaHeaders
     * @throws IllegalArgumentException if callIdHeader,
     * cSeqHeader, fromHeader or toHeader are null or
     * not from same JAIN SIP implementation, or if viaHeaders is null, empty,
     * contains any null elements, or contains any objects that are not ViaHeaders
     * from the same JAIN SIP implementation
     * @throws SipParseException if statusCode is not accepted by implementation
     */
    public Response createResponse(int statusCode,
    CallIdHeader callIdHeader,
    CSeqHeader cSeqHeader,
    FromHeader fromHeader,
    ToHeader toHeader,
    List viaHeaders)
    throws IllegalArgumentException, SipParseException {
        
        if (callIdHeader == null ||
        cSeqHeader == null      ||
        fromHeader == null      ||
        toHeader == null        ||
        viaHeaders == null      ||
        viaHeaders.isEmpty())
            throw new IllegalArgumentException("null argument");
        
        for (int i=0;i<viaHeaders.size();i++){
            
            Object obj = viaHeaders.get(i);
            if (obj==null)
                throw new IllegalArgumentException
                (" viaHeaders contains null elements ");
            if (!(obj instanceof ViaHeaderImpl ))
                throw new IllegalArgumentException
                ("viaHeaders is not from the same implementation");
        }
        
        CallIdHeaderImpl cidHdr ;
        CSeqHeaderImpl csHdr ;
        FromHeaderImpl fromHdr ;
        ToHeaderImpl toHdr ;
        
        try {
            cidHdr = (CallIdHeaderImpl) callIdHeader;
            csHdr = (CSeqHeaderImpl) cSeqHeader ;
            fromHdr = (FromHeaderImpl) fromHeader;
            toHdr = (ToHeaderImpl) toHeader;
        } catch (ClassCastException ex) {
            throw new
            IllegalArgumentException
            ("header not from this implementation");
        }
        ResponseImpl resImpl = new ResponseImpl();
        SIPResponse sr = new SIPResponse() ;
        resImpl.setImplementationObject(sr) ;
        
        resImpl.setStatusCode(statusCode) ;
        resImpl.setCallIdHeader(cidHdr) ;
        resImpl.setCSeqHeader(csHdr) ;
        resImpl.setFromHeader(fromHdr) ;
        resImpl.setToHeader(toHdr) ;
        resImpl.setViaHeaders(viaHeaders) ;
        
        return resImpl ;
    }
    
    /**
     * Creates Request without body
     * @param <var>requestURI</var> Request URI
     * @param <var>method</var> Request method
     * @param <var>callIdHeader</var> CallIdHeader
     * @param <var>cSeqHeader</var> CSeqHeader
     * @param <var>fromHeader</var> FromHeader
     * @param <var>toHeader</var> ToHeader
     * @param <var>viaHeaders</var> ViaHeaders
     * @throws IllegalArgumentException if method is null, or if requestURI,
     * callIdHeader, cSeqHeader, fromHeader or toHeader are null or
     * not from same JAIN SIP implementation, or if viaHeaders is null, empty,
     * contains any null elements, or contains any objects that are not ViaHeaders
     * from the same JAIN SIP implementation
     * @throws SipParseException if method is not accepted by implementation
     */
    public Request createRequest(URI requestURI,
    String method,
    CallIdHeader callIdHeader,
    CSeqHeader cSeqHeader,
    FromHeader fromHeader,
    ToHeader toHeader,
    List viaHeaders)
    throws IllegalArgumentException, SipParseException {
        
        if (requestURI == null       ||
        method == null           ||
        callIdHeader == null     ||
        cSeqHeader == null       ||
        fromHeader == null       ||
        toHeader == null         ||
        viaHeaders == null       ||
        viaHeaders.isEmpty())
            throw new IllegalArgumentException("null argument");
        
        for (int i=0;i<viaHeaders.size();i++){
            
            Object obj = viaHeaders.get(i);
            if (obj==null)
                throw new IllegalArgumentException
                (" viaHeaders contains null elements ");
            if (!(obj instanceof ViaHeaderImpl ))
                throw new IllegalArgumentException
                ("viaHeaders is not from the same implementation");
        }
        
        URIImpl uri;
        CallIdHeaderImpl cidHdr;
        CSeqHeader csHdr ;
        FromHeaderImpl fromHdr;
        ToHeaderImpl toHdr;
        
        try {
            uri = (URIImpl) requestURI;
            cidHdr = (CallIdHeaderImpl) callIdHeader;
            csHdr = (CSeqHeaderImpl) cSeqHeader ;
            fromHdr = (FromHeaderImpl) fromHeader;
            toHdr = (ToHeaderImpl) toHeader;
        } catch (ClassCastException ex) {
            throw new
            IllegalArgumentException
            ("header not from this implementation");
        }
        
        RequestImpl reqImpl = new RequestImpl();
        SIPRequest sr = new SIPRequest() ;
        reqImpl.setImplementationObject(sr) ;
        reqImpl.setRequestURI(requestURI) ;
	reqImpl.setMethod(method);
        reqImpl.setCSeqHeader(csHdr) ;
        reqImpl.setCallIdHeader(callIdHeader) ;
        reqImpl.setFromHeader(fromHdr) ;
        reqImpl.setToHeader(toHdr) ;
        reqImpl.setViaHeaders(viaHeaders) ;
        
        return reqImpl ;
    }
    
    /**
     * Creates Response with body
     * @param <var>statusCode</var> status code
     * @param <var>callIdHeader</var> CallIdHeader
     * @param <var>cSeqHeader</var> CSeqHeader
     * @param <var>fromHeader</var> FromHeader
     * @param <var>toHeader</var> ToHeader
     * @param <var>viaHeaders</var> ViaHeaders
     * @param <var>body</var> body of Request
     * @param <var>contentTypeHeader</var> ContentTypeHeader
     * @throws IllegalArgumentException if body is null, or if callIdHeader,
     * cSeqHeader, fromHeader, toHeader or contentTypeHeader are null or
     * not from same JAIN SIP implementation, or if viaHeaders is null, empty,
     * contains any null elements, or contains any objects that are not
     *  ViaHeaders from the same JAIN SIP implementation
     * @throws SipParseException if statusCode or body are not accepted by
     *  implementation
     */
    public Response createResponse(int statusCode,
    CallIdHeader callIdHeader,
    CSeqHeader cSeqHeader,
    FromHeader fromHeader,
    ToHeader toHeader,
    List viaHeaders,
    String body,
    ContentTypeHeader contentTypeHeader)
    throws IllegalArgumentException, SipParseException {
        
        if (callIdHeader == null ||
        cSeqHeader == null      ||
        fromHeader == null      ||
        toHeader == null        ||
        viaHeaders == null      ||
        viaHeaders.isEmpty()    ||
        body == null            ||
        contentTypeHeader == null)
            throw new IllegalArgumentException("null argument");
        
        for (int i=0;i<viaHeaders.size();i++){
            
            Object obj = viaHeaders.get(i);
            if (obj==null)
                throw new IllegalArgumentException
                (" viaHeaders contains null elements ");
            if (!(obj instanceof ViaHeaderImpl ))
                throw new IllegalArgumentException
                ("viaHeaders is not from the same implementation");
        }
        
        CallIdHeaderImpl cidHdr ;
        CSeqHeaderImpl csHdr ;
        FromHeaderImpl fromHdr ;
        ToHeaderImpl toHdr ;
        ContentTypeHeaderImpl ctHdr ;
        
        try {
            cidHdr = (CallIdHeaderImpl) callIdHeader;
            csHdr = (CSeqHeaderImpl) cSeqHeader ;
            fromHdr = (FromHeaderImpl) fromHeader;
            toHdr = (ToHeaderImpl) toHeader;
            ctHdr = (ContentTypeHeaderImpl) contentTypeHeader ;
        } catch (ClassCastException ex) {
            throw new
            IllegalArgumentException
            ("header not from this implementation");
        }
        
        ResponseImpl resImpl = new ResponseImpl();
        SIPResponse sr = new SIPResponse() ;
        resImpl.setImplementationObject(sr) ;
        
        resImpl.setStatusCode(statusCode) ;
        resImpl.setCallIdHeader(cidHdr) ;
        resImpl.setCSeqHeader(csHdr) ;
        resImpl.setFromHeader(fromHdr) ;
        resImpl.setToHeader(toHdr) ;
        resImpl.setViaHeaders(viaHeaders) ;
        resImpl.setBody(body,ctHdr) ;
        
        return resImpl ;
        
    }
    
    /**
     * Creates Response without body based on specified Request
     * @param <var>statusCode</var> status code
     * @param <var>request</var> Request to base Response on
     * @throws IllegalArgumentException if request is null or
     * not from same JAIN SIP implementation
     * @throws SipParseException if statusCode is not accepted by implementation
     */
    public Response createResponse(
        int statusCode,
        Request request)
    throws IllegalArgumentException, SipParseException {
        
        SIPResponse sipResponse = new SIPResponse();
        StatusLine statusLine = new StatusLine();
        statusLine.setSipVersion(SIPVersion.SIP_VERSION_STRING);
        statusLine.setStatusCode(statusCode);
        statusLine.setReasonPhrase(SIPException.getMessageString(statusCode));
        sipResponse.setStatusLine(statusLine);
        RequestImpl requestImpl = (RequestImpl) request;
        SIPRequest sipRequest =(SIPRequest)
        requestImpl.getImplementationObject();
        ViaList viaList = sipRequest.getViaHeaders();
        sipResponse.setHeader(viaList);
        From  from = sipRequest.getFromHeader();
        sipResponse.setHeader(from);
        To to = sipRequest.getToHeader();
        sipResponse.setHeader(to);
        CallID callId = sipRequest.getCallIdHeader();
        sipResponse.setHeader(callId);
        CSeq cseq = sipRequest.getCSeqHeader();
        sipResponse.setHeader(cseq);
        ResponseImpl responseImpl = new ResponseImpl(sipResponse);
        return responseImpl;
        
        
    }
    
    /**
     * Creates Response with body based on specified Request
     * @param <var>statusCode</var> status code
     * @param <var>request</var> Request to base Response on
     * @param <var>body</var> body of Request
     * @param <var>contentTypeHeader</var> ContentTypeHeader
     * @throws IllegalArgumentException if body is null, or if request or
     * contentTypeHeader are null or not from same JAIN SIP implementation
     * @throws SipParseException if statusCode or body are not
     * accepted by implementation
     */
    public Response createResponse(int statusCode,
        Request request,
        byte[] body,
        ContentTypeHeader contentTypeHeader)
    throws IllegalArgumentException, SipParseException {
        
        return createResponse(statusCode, request, new String(body), 
                            contentTypeHeader);
    }
    
    /**
     * Creates Response with body based on specified Request
     * @param <var>statusCode</var> status code
     * @param <var>request</var> Request to base Response on
     * @param <var>body</var> body of Request
     * @param <var>contentTypeHeader</var> ContentTypeHeader
     * @throws IllegalArgumentException if body is null, or if request or
     * contentTypeHeader are null or not from same JAIN SIP implementation
     * @throws SipParseException if statusCode or body are not accepted by implementation
     */
    public Response createResponse(int statusCode,
    Request request,
    String body,
    ContentTypeHeader contentTypeHeader)
    throws IllegalArgumentException, SipParseException {
            ResponseImpl response = (ResponseImpl)
                        this.createResponse(statusCode, request);
            SIPResponse sipResponse = (SIPResponse) 
                            response.getImplementationObject();
            ContentTypeHeaderImpl contentType = 
                    (ContentTypeHeaderImpl)contentTypeHeader;
            sipResponse.setHeader(contentType.getImplementationObject());
            int length = body.length();
            ContentLength contentLength = new ContentLength(length);
            sipResponse.setHeader(contentLength);
            sipResponse.setMessageContent(body);
            return response;        
        
    }
    
    /**
     * Creates Response with body
     * @param <var>statusCode</var> status code
     * @param <var>callIdHeader</var> CallIdHeader
     * @param <var>cSeqHeader</var> CSeqHeader
     * @param <var>fromHeader</var> FromHeader
     * @param <var>toHeader</var> ToHeader
     * @param <var>viaHeaders</var> ViaHeaders
     * @param <var>body</var> body of Request
     * @param <var>contentTypeHeader</var> ContentTypeHeader
     * @throws IllegalArgumentException if body is null, or if callIdHeader,
     * cSeqHeader, fromHeader, toHeader or contentTypeHeader are null or
     * not from same JAIN SIP implementation, or if viaHeaders is null, empty,
     * contains any null elements, or contains any objects that are not ViaHeaders
     * from the same JAIN SIP implementation
     * @throws SipParseException if statusCode or body are not accepted by implementation
     */
    public Response createResponse(int statusCode,
    CallIdHeader callIdHeader,
    CSeqHeader cSeqHeader,
    FromHeader fromHeader,
    ToHeader toHeader,
    List viaHeaders,
    byte[] body,
    ContentTypeHeader contentTypeHeader)
    throws IllegalArgumentException, SipParseException {
        
        
        if (callIdHeader == null ||
        cSeqHeader == null      ||
        fromHeader == null      ||
        toHeader == null        ||
        viaHeaders == null      ||
        viaHeaders.isEmpty()    ||
        body == null            ||
        contentTypeHeader == null)
            throw new IllegalArgumentException("null argument");
        
        for (int i=0;i<viaHeaders.size();i++){
            
            Object obj = viaHeaders.get(i);
            if (obj==null)
                throw new IllegalArgumentException
                (" viaHeaders contains null elements ");
            if (!(obj instanceof ViaHeaderImpl ))
                throw new IllegalArgumentException
                ("viaHeaders is not from the same implementation");
        }
        
        CallIdHeaderImpl cidHdr ;
        CSeqHeaderImpl csHdr ;
        FromHeaderImpl fromHdr ;
        ToHeaderImpl toHdr ;
        ContentTypeHeaderImpl ctHdr ;
        
        try {
            cidHdr = (CallIdHeaderImpl) callIdHeader;
            csHdr = (CSeqHeaderImpl) cSeqHeader ;
            fromHdr = (FromHeaderImpl) fromHeader;
            toHdr = (ToHeaderImpl) toHeader;
            ctHdr = (ContentTypeHeaderImpl) contentTypeHeader ;
        } catch (ClassCastException ex) {
            throw new
            IllegalArgumentException
            ("header not from this implementation");
        }
        
        ResponseImpl resImpl = new ResponseImpl();
        SIPResponse sr = new SIPResponse() ;
        resImpl.setImplementationObject(sr) ;
        String strBody = new String(body) ;
        
        resImpl.setStatusCode(statusCode) ;
        resImpl.setCallIdHeader(cidHdr) ;
        resImpl.setCSeqHeader(csHdr) ;
        resImpl.setFromHeader(fromHdr) ;
        resImpl.setToHeader(toHdr) ;
        resImpl.setViaHeaders(viaHeaders) ;
        resImpl.setBody(body,ctHdr) ;
        
        return resImpl ;
    }
    
}
