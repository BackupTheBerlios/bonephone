 /*
 * HeaderFactoryImpl.java
 *
 * Created on May 08, 2001, 4:06 PM
 */

package gov.nist.jain.protocol.ip.sip.header;

import  jain.protocol.ip.sip.header.*;
import  jain.protocol.ip.sip.*;
import  jain.protocol.ip.sip.address.*;
import  gov.nist.jain.protocol.ip.sip.*;

import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.stack.*;

import  java.net.*;
import  java.util.*;
import gov.nist.log.*;

/**
 *
 * @author  olivier Deruelle ( deruelle@nist.gov )
 * @version 
 */
public class HeaderFactoryImpl implements HeaderFactory {
    
    /**
     * Creates an AcceptHeader based on the specified type and subType
     * @param <var>type</var> media type
     * @param <var>subType</var> media sub-type
     * @throws IllegalArgumentException if type or sub-type is null
     * @throws SipParseException if contentType or contentSubType is not
     * accepted by implementation
     */
    public AcceptHeader createAcceptHeader
    (String contentType,String contentSubType)
    throws IllegalArgumentException,SipParseException {
        if ( contentType==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : contentType is null ");
        if ( contentSubType==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : contentSubType is null ");
        AcceptHeaderImpl acceptHeaderImpl=new AcceptHeaderImpl();
        acceptHeaderImpl.setContentType(contentType);
        acceptHeaderImpl.setContentSubType(contentSubType);
        
        return acceptHeaderImpl;
    }
    
    /**
     * Creates AcceptLanguageHeader based on given language-range
     * @param <var>languageRange</var> language-range
     * @throws IllegalArgumentException if languageRange is null
     * @throws SipParseException if languageRange is not accepted by 
     * implementation
     */
    public AcceptLanguageHeader createAcceptLanguageHeader(String languageRange)
    throws IllegalArgumentException,SipParseException {
          if ( languageRange==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : languageRange is null ");
          AcceptLanguageHeaderImpl acceptLanguageHeaderImpl=
                    new AcceptLanguageHeaderImpl();
          acceptLanguageHeaderImpl.setLanguageRange(languageRange);
         
          return acceptLanguageHeaderImpl;
    }
    
    /**
     * Creates an AllowHeader based on given method
     * @param <var>method</var> method
     * @throws IllegalArgumentException if method is null
     * @throws SipParseException if method is not accepted by implementation
     */
    public AllowHeader createAllowHeader(String method)
    throws IllegalArgumentException,SipParseException {
         if ( method==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : method is null ");
          AllowHeaderImpl allowHeaderImpl= new AllowHeaderImpl();
          allowHeaderImpl.setMethod(method);
        
          return allowHeaderImpl;
    }
    
    /**
     * Creates a TimeStampHeader based on given timestamp
     * @param <var>timeStamp</var> time stamp
     * @throws SipParseException if timestamp is not accepted by implementation
     */
    public TimeStampHeader createTimeStampHeader(float timeStamp)
    throws SipParseException {
          TimeStampHeaderImpl timeStampHeaderImpl= new TimeStampHeaderImpl();
          timeStampHeaderImpl.setTimeStamp(timeStamp);
         
          return timeStampHeaderImpl;
    }
    
    /**
     * Creates a ViaHeader based on given host
     * @param <var>host</var> host
     * @throws IllegalArgumentException if host is null
     * @throws SipParseException if host is not accepted by implementation
     */
    public ViaHeader createViaHeader(String host)
    throws IllegalArgumentException,SipParseException {
       
	  if (LogWriter.needsLogging())
	      LogWriter.logMessage
		(LogWriter.TRACE_DEBUG,"createViaHeader " + host);
          if ( host==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : host is null ");
          ViaHeaderImpl viaHeaderImpl= new ViaHeaderImpl();
          viaHeaderImpl.setHost(host);
        
          return viaHeaderImpl;
    }
    
    /**
     * Creates a WarningHeader based on given code, agent and text
     * @param <var>code</var> code
     * @param <var>host</var> agent
     * @param <var>text</var> text
     * @throws IllegalArgumentException if agent or text are is null
     * @throws SipParseException if code, agent or text are not accepted by
     * implementation
     */
    public WarningHeader createWarningHeader(int code,String agent,String text)
    throws IllegalArgumentException,SipParseException {
          if ( agent==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : agent is null ");
          if ( text==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : text is null ");
          WarningHeaderImpl warningHeaderImpl= new WarningHeaderImpl();
          warningHeaderImpl.setText(text);
          warningHeaderImpl.setCode(code);
          warningHeaderImpl.setAgent(agent);
          
          return warningHeaderImpl;
    }
    
    /**
     * Creates a RequireHeader based on given option tag
     * @param <var>optionTag</var> option tag
     * @throws IllegalArgumentException if optionTag is null
     * @throws SipParseException if optionTag is not accepted by implementation
     */
    public RequireHeader createRequireHeader(String optionTag)
    throws IllegalArgumentException,SipParseException {
         if ( optionTag==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : optionTag is null ");
         RequireHeaderImpl requireHeaderImpl= new RequireHeaderImpl();
	 requireHeaderImpl.setOptionTag(optionTag);
       
         return requireHeaderImpl;
    }
    
    /**
     * Creates a RetryAfterHeader based on given date string
     * @param <var>date</var> date string
     * @throws IllegalArgumentException if date is null
     * @throws SipParseException if date is not accepted by implementation
     */
    public RetryAfterHeader createRetryAfterHeader(String date)
    throws IllegalArgumentException,SipParseException {
         if ( date==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : date is null ");
         RetryAfterHeaderImpl retryAfterHeaderImpl= new RetryAfterHeaderImpl();
         retryAfterHeaderImpl.setDate(date);
        
         return retryAfterHeaderImpl;
    }
    
    /**
     * Creates an AuthorizationHeader based on given scheme
     * @param <var>scheme</var> authentication scheme
     * @throws IllegalArgumentException if scheme is null
     * @throws SipParseException if scheme is not accepted by implementation
     */
    public AuthorizationHeader createAuthorizationHeader(String scheme)
    throws IllegalArgumentException,SipParseException {
         if ( scheme==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : scheme is null ");
         AuthorizationHeaderImpl authorizationHeaderImpl= 
                            new AuthorizationHeaderImpl();
         authorizationHeaderImpl.setScheme(scheme);
        
         return authorizationHeaderImpl;
    }
    
    /**
     * Creates a ViaHeader based on given host and transport
     * @param <var>host</var> host
     * @param <var>transport</var> transport
     * @throws IllegalArgumentException if host or transport are null
     * @throws SipParseException if host or transport are not accepted by 
     * implementation
     */
    public ViaHeader createViaHeader(String host, String transport)
    throws IllegalArgumentException,SipParseException {
         
	if (LogWriter.needsLogging()) 
	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,"createViaHeader " + host
		+ " transport " + transport );
        if ( host==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : host is null ");
        if ( transport==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : transport is null ");
        ViaHeaderImpl viaHeaderImpl=new ViaHeaderImpl();
       
        viaHeaderImpl.setHost(host);
         
        viaHeaderImpl.setTransport(transport);
      
	if (LogWriter.needsLogging()) 
            LogWriter.logMessage
		(LogWriter.TRACE_DEBUG, viaHeaderImpl.toString());
        return viaHeaderImpl;
    }
    
    /**
     * Creates a CallIdHeader based on given Call-Id
     * @param <var>callId</var> call-id
     * @throws IllegalArgumentException if callId is null
     * @throws SipParseException if callId is not accepted by implementation
     */
    public CallIdHeader createCallIdHeader(String callId)
    throws IllegalArgumentException,SipParseException {
        if ( callId==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : callId is null ");
        CallIdHeaderImpl callIdHeaderImpl=new CallIdHeaderImpl();
        callIdHeaderImpl.setCallId(callId);
        
        return callIdHeaderImpl;
    }
    
    /**
     * Creates a ProxyRequireHeader based on given option tag
     * @param <var>optionTag</var> option tag
     * @throws IllegalArgumentException if optionTag is null
     * @throws SipParseException if scheme is not accepted by implementation
     */
    public ProxyRequireHeader createProxyRequireHeader(String optionTag)
    throws IllegalArgumentException,SipParseException {
        if ( optionTag==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : optionTag is null ");
        ProxyRequireHeaderImpl proxyImpl=new ProxyRequireHeaderImpl();
	proxyImpl.setOptionTag(optionTag);
       
        return proxyImpl;
    }
    
    /**
     * Creates a ContactHeader based on given NameAddress
     * @param <var>nameAddress</var> NameAddress
     * @throws IllegalArgumentException if nameAddress is null or not from same
     * JAIN SIP implementation
     */
    public ContactHeader createContactHeader(NameAddress nameAddress)
    throws IllegalArgumentException,SipParseException {
        if ( nameAddress==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : nameAddress is null ");
        ContactHeaderImpl contactImpl=new ContactHeaderImpl();
        contactImpl.setNameAddress(nameAddress);
       
        return contactImpl;
    }
    
    /**
     * Creates a RetryAfterHeader based on given number of delta-seconds
     * @param <var>deltaSeconds</var> number of delta-seconds
     * @throws SipParseException if deltaSeconds is not accepted by 
     * implementation
     */
    public RetryAfterHeader createRetryAfterHeader(long deltaSeconds)
    throws SipParseException {
        RetryAfterHeaderImpl retryImpl=new RetryAfterHeaderImpl();
        retryImpl.setDeltaSeconds(deltaSeconds);
       
        return retryImpl;
    }
    
    /**
     * Creates a wildcard ContactHeader. This is used in RegisterMessages
     * to indicate to the server that it should remove all locations the
     * at which the user is currently available
     */
    public ContactHeader createContactHeader() {
        ContactHeaderImpl contactImpl=new ContactHeaderImpl();
        return contactImpl;
    }
    
    /**
     * Creates a ServerHeader based on given List of products
     * (Note that the Objects in the List must be Strings)
     * @param <var>products</var> products
     * @throws IllegalArgumentException if products is null, empty, or contains
     * any null elements, or contains any non-String objects
     * @throws SipParseException if any element of products is not accepted by
     * implementation
     */
    public ServerHeader createServerHeader(List products)
    throws IllegalArgumentException,SipParseException {
        ServerHeaderImpl serverImpl=new ServerHeaderImpl();
        serverImpl.setProducts(products);
       
        return serverImpl;
    }
    
    /**
     * Creates a ContentEncodingHeader based on given content-encoding
     * @param <var>contentEncoding</var> content-encoding
     * @throws IllegalArgumentException if contentEncoding is null
     * @throws SipParseException if contentEncoding is not accepted by 
     * implementation
     */
    public ContentEncodingHeader createContentEncodingHeader
    (String contentEncoding) throws IllegalArgumentException,SipParseException {
        if ( contentEncoding==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : contentEncoding is null ");
        ContentEncodingHeaderImpl contentEncodingImpl=
                        new ContentEncodingHeaderImpl();
        contentEncodingImpl.setEncoding(contentEncoding);
        return contentEncodingImpl;
    }
    
    /**
     * Creates a UnsupportedHeader based on given option tag
     * @param <var>optionTag</var> option tag
     * @throws IllegalArgumentException if optionTag is null
     * @throws SipParseException if optionTag is not accepted by implementation
     */
    public UnsupportedHeader createUnsupportedHeader(String optionTag)
    throws IllegalArgumentException,SipParseException {
        if ( optionTag==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : optionTag is null ");
        UnsupportedHeaderImpl unsupportedImpl=
                        new UnsupportedHeaderImpl();
        unsupportedImpl.setOptionTag(optionTag);
        return unsupportedImpl;
    }
    
    /**
     * Creates a ContentLengthHeader based on given content-length
     * @param <var>contentLength</var> content-length
     * @throws SipParseException if contentLength is not accepted by 
     * implementation
     */
    public ContentLengthHeader createContentLengthHeader(int contentLength)
    throws SipParseException {
        ContentLengthHeaderImpl contentLengthImpl=
                        new ContentLengthHeaderImpl();
        contentLengthImpl.setContentLength(contentLength);    
        return contentLengthImpl;
    }
    
    /**
     * Creates a ViaHeader based on given host and port
     * @param <var>host</var> host
     * @param <var>port</var> port
     * @throws IllegalArgumentException if host is null
     * @throws SipParseException if host or port is not accepted by 
     * implementation
     */
    public ViaHeader createViaHeader(int port, String host)
    throws IllegalArgumentException,SipParseException {

	 if (LogWriter.needsLogging())
	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,"createViaHeader " + host
		+ " port " + port );
         
        if ( host==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : host is null ");
        ViaHeaderImpl viaImpl= new ViaHeaderImpl();
        
        viaImpl.setPort(port);
         
        viaImpl.setHost(host);

	if (LogWriter.needsLogging())
            LogWriter.logMessage(LogWriter.TRACE_DEBUG,viaImpl.toString());
       
        return viaImpl;
    }
    
    /**
     * Creates a ContentTypeHeader based on given media type and sub-type
     * @param <var>type</var> media type
     * @param <var>subType</var> media sub-type
     * @throws IllegalArgumentException if type or subtype are null
     * @throws SipParseException if contentType or contentSubType is not 
     * accepted by implementation
     */
    public ContentTypeHeader createContentTypeHeader
    (String contentType, String contentSubType)
    throws IllegalArgumentException,SipParseException {
        if ( contentType==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : contentType is null ");
        if ( contentSubType==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : contentSubType is null ");
        ContentTypeHeaderImpl contentTypeHeaderImpl=new ContentTypeHeaderImpl();
        contentTypeHeaderImpl.setContentType(contentType);
        contentTypeHeaderImpl.setContentSubType(contentSubType);
        
        return contentTypeHeaderImpl;
    }
    
    /**
     * Creates a ViaHeader based on given host, port and transport
     * @param <var>host</var> host
     * @param <var>port</var> port
     * @param <var>transport</var> transport
     * @throws IllegalArgumentException if host or transport are null
     * @throws SipParseException if host, port or transport are not accepted by
     * implementation
     */
    public ViaHeader createViaHeader(InetAddress host,int port,String transport)
    throws IllegalArgumentException,SipParseException {
       
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"createViaHeader " + host +
			" port = " + port +
			" transport = " + transport);

        if ( host==null)
            throw new IllegalArgumentException ("host is null ");
        if ( transport==null)
            throw new IllegalArgumentException ("transport is null ");

	if (!transport.equals(ViaHeaderImpl.UDP) && 
	    !transport.equals(ViaHeaderImpl.TCP))
	    throw new SipParseException("Bad transport string");

        ViaHeaderImpl viaHeaderImpl=new ViaHeaderImpl();
        
        viaHeaderImpl.setHost(host.getHostAddress());
        viaHeaderImpl.setPort(port);
        viaHeaderImpl.setTransport(transport);
          
  	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG)) 
	    LogWriter.logMessage(LogWriter.TRACE_DEBUG, 
				viaHeaderImpl.toString());
		      
        return viaHeaderImpl;
    }
    
    /**
     * Creates a CSeqHeader based on given sequence number and method
     * @param <var>sequenceNumber</var> sequence number
     * @param <var>method</var> method
     * @throws IllegalArgumentException if method is null
     * @throws SipParseException if sequenceNumber or method are not accepted 
     * by implementation
     */
    public CSeqHeader createCSeqHeader(long sequenceNumber, String method)
    throws IllegalArgumentException,SipParseException {
         if ( method==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : method is null ");
        CSeqHeaderImpl cseqImpl= new CSeqHeaderImpl();
        cseqImpl.setSequenceNumber(sequenceNumber);
        cseqImpl.setMethod(method);
        return cseqImpl;
    }
    
    /**
     * Creates an AcceptEncodingHeader with given content-encoding
     * @param <var>contentEncoding</var> the content-cenoding
     * @throws IllegalArgumentException if contentEncoding is null
     * @throws SipParseException if contentEncoding is not accepted by 
     * implementation
     */
    public AcceptEncodingHeader createAcceptEncodingHeader
    (String contentEncoding)
    throws IllegalArgumentException,SipParseException {
        if ( contentEncoding==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : contentEncoding is null ");
        AcceptEncodingHeaderImpl acceptImpl= new AcceptEncodingHeaderImpl();
        acceptImpl.setEncoding(contentEncoding);
       
        return acceptImpl;
    }
    
    /**
     * Creates a DateHeader based on given date string
     * @param <var>date</var> date string
     * @throws IllegalArgumentException if date string is null
     * @throws SipParseException if date is not accepted by implementation
     */
    public DateHeader createDateHeader(String date)
    throws IllegalArgumentException,SipParseException {
        if ( date==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : date is null ");
        DateHeaderImpl dateHeaderImpl= new DateHeaderImpl();
        dateHeaderImpl.setDate(date);
        
        return dateHeaderImpl;
    }
    
    /**
     * Creates a RecordRouteHeader based on given NameAddress
     * @param <var>nameAddress</var> NameAddress
     * @throws IllegalArgumentException if nameAddress is null or not from same
     * JAIN SIP implementation
     */
    public RecordRouteHeader createRecordRouteHeader(NameAddress nameAddress)
    throws IllegalArgumentException,SipParseException {
         if ( nameAddress==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : nameAddress is null ");
        RecordRouteHeaderImpl recordImpl= new RecordRouteHeaderImpl();
        recordImpl.setNameAddress(nameAddress);
        
        return recordImpl;
    }
    
    /**
     * Creates an DateHeader based on given Date
     * @param <var>date</var> Date
     * @throws IllegalArgumentException if date is null
     * @throws SipParseException if date is not accepted by implementation
     */
    public DateHeader createDateHeader(Date date)
    throws IllegalArgumentException,SipParseException {
           if ( date==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : date is null ");
        DateHeaderImpl dateHeaderImpl= new DateHeaderImpl();
        dateHeaderImpl.setDate(date);
        
        return dateHeaderImpl;
    }
    
    /**
     * Creates a ResponseKeyHeader based on given scheme
     * @param <var>scheme</var> scheme
     * @throws IllegalArgumentException if scheme is null
     * @throws SipParseException if scheme is not accepted by implementation
     */
    public ResponseKeyHeader createResponseKeyHeader(String scheme)
    throws IllegalArgumentException,SipParseException {
        if ( scheme==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : scheme is null ");
        ResponseKeyHeaderImpl responseHeaderImpl= new ResponseKeyHeaderImpl();
        responseHeaderImpl.setScheme(scheme);
        
        return responseHeaderImpl;
    }
    
    /**
     * Creates an EncryptionHeader based on given scheme
     * @param <var>scheme</var> scheme
     * @throws IllegalArgumentException if scheme is null
     * @throws SipParseException if scheme is not accepted by implementation
     */
    public EncryptionHeader createEncryptionHeader(String scheme)
    throws IllegalArgumentException,SipParseException {
        if ( scheme==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : scheme is null ");
        EncryptionHeaderImpl encryptionHeaderImpl= new EncryptionHeaderImpl();
        encryptionHeaderImpl.setScheme(scheme);
        
        return encryptionHeaderImpl;   
    }
    
    /**
     * Creates a RetryAfterHeader based on given date
     * @param <var>date</var> date
     * @throws IllegalArgumentException if date is null
     * @throws SipParseException if date is not accepted by implementation
     */
    public RetryAfterHeader createRetryAfterHeader(Date date)
    throws IllegalArgumentException,SipParseException {
        if ( date==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : date is null ");
        RetryAfterHeaderImpl retryAfterHeaderImpl= new RetryAfterHeaderImpl();
        retryAfterHeaderImpl.setDate(date);
        
        return retryAfterHeaderImpl;
    }
    
    /**
     * Creates an ExpiresHeader based on given number of delta-seconds
     * @param <var>deltaSeconds</var> delta-seconds
     * @throws SipParseException if deltaSeconds is not accepted by 
     * implementation
     */
    public ExpiresHeader createExpiresHeader(long deltaSeconds)
    throws SipParseException {
        ExpiresHeaderImpl expiresHeaderImpl= new ExpiresHeaderImpl();
        expiresHeaderImpl.setDeltaSeconds(deltaSeconds);    
       
        return expiresHeaderImpl;
    }
    
    /**
     * Creates a RouteHeader based on given NameAddresss
     * @param <var>nameAddress</var> NameAddress
     * @throws IllegalArgumentException if nameAddress is null or not from same
     * JAIN SIP implementation
     */
    public RouteHeader createRouteHeader(NameAddress nameAddress)
    throws IllegalArgumentException,SipParseException {
        if ( nameAddress==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : nameAddress is null ");
        RouteHeaderImpl routeImpl= new RouteHeaderImpl();
        routeImpl.setNameAddress(nameAddress);
        
        return routeImpl;
    }
    
    /**
     * Creates an ExpiresHeader based on given date
     * @param <var>date</var> date
     * @throws IllegalArgumentException if date is null
     * @throws SipParseException if date is not accepted by implementation
     */
    public ExpiresHeader createExpiresHeader(Date date)
    throws IllegalArgumentException,SipParseException{
        if ( date==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : date is null ");
        ExpiresHeaderImpl expiresHeaderImpl= new ExpiresHeaderImpl();
        expiresHeaderImpl.setDate(date);
        
        return expiresHeaderImpl;
    }
    
    /**
     * Creates a SubjectHeader based on given subject
     * @param <var>subject</var> subject
     * @throws IllegalArgumentException if subject is null
     * @throws SipParseException if subject is not accepted by implementation
     */
    public SubjectHeader createSubjectHeader(String subject)
    throws IllegalArgumentException,SipParseException {
        if ( subject==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : subject is null ");
        SubjectHeaderImpl subjectHeaderImpl= new SubjectHeaderImpl();
        subjectHeaderImpl.setSubject(subject);
        
        return subjectHeaderImpl;   
    }
    
    /**
     * Creates an ExpiresHeader based on given date string
     * @param <var>date</var> date string
     * @throws IllegalArgumentException if date is null
     * @throws SipParseException if date is not accepted by implementation
     */
    public ExpiresHeader createExpiresHeader(String date)
    throws IllegalArgumentException,SipParseException {
        if ( date==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : date is null ");
        ExpiresHeaderImpl expiresHeaderImpl= new ExpiresHeaderImpl();
        expiresHeaderImpl.setDate(date);
        
        return expiresHeaderImpl;   
    }
    
    /**
     * Creates a ToHeader based on given NameAddress
     * @param <var>nameAddress</var> NameAddress
     * @throws IllegalArgumentException if nameAddress is null or not from same
     * JAIN SIP implementation
     */
    public ToHeader createToHeader(NameAddress nameAddress)
    throws IllegalArgumentException,SipParseException {
        if ( nameAddress==null)
            throw new IllegalArgumentException ("nameAddress is null ");
        ToHeaderImpl toImpl=new ToHeaderImpl();
        toImpl.setNameAddress(nameAddress);
       
        return toImpl;
    }
    
    /**
     * Creates a FromHeader based on given NameAddress
     * @param <var>nameAddress</var> NameAddress
     * @throws IllegalArgumentException if nameAddress is null or not from same
     * JAIN SIP implementation
     */
    public FromHeader createFromHeader(NameAddress nameAddress)
    throws IllegalArgumentException,SipParseException {
        if ( nameAddress==null)
            throw new IllegalArgumentException ("nameAddress is null ");
        FromHeaderImpl fromImpl=new FromHeaderImpl();
        fromImpl.setNameAddress(nameAddress);
       
        return fromImpl;
    }
    
    /**
     * Creates a UserAgentHeader based on given List of products
     * (Note that the Objects in the List must be Strings)
     * @param <var>products</var> products
     * @throws IllegalArgumentException if products is null, empty, or contains
     * any null elements, or contains any non-String objects
     * @throws SipParseException if any element of products is not accepted by 
     * implementation
     */
    public UserAgentHeader createUserAgentHeader(List products)
    throws IllegalArgumentException,SipParseException {
        UserAgentHeaderImpl userAgentImpl=new UserAgentHeaderImpl();
        userAgentImpl.setProducts(products);
       
        return userAgentImpl;
    }
    
    /**
     * Creates a Header based on given token and value
     * @param <var>name</var> name
     * @param <var>value</var> value
     * @throws IllegalArgumentException if name or value are null
     * @throws SipParseException if name or value are not accepted by 
     * implementation
     */
    public Header createHeader(String name, String value)
    throws IllegalArgumentException,SipParseException {
        if ( name==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : name is null ");
        if ( value==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : value is null ");
        HeaderImpl headerImpl=new HeaderImpl(name,value);
        
        return headerImpl;
    }
    
    /**
     * Creates a ViaHeader based on given host
     * @param <var>host</var> host
     * @throws IllegalArgumentException if host is null
     * @throws SipParseException if host is not accepted by implementation
     */
    public ViaHeader createViaHeader(InetAddress host)
    throws IllegalArgumentException,SipParseException {
        
        if ( host==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : host is null ");
	if (LogWriter.needsLogging())
	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"createViaHeader " + host);
        ViaHeaderImpl viaHeaderImpl=new ViaHeaderImpl();
        String hostName=host.getHostName();
        viaHeaderImpl.setHost(host);
        viaHeaderImpl.setTransport("UDP");
	if (LogWriter.needsLogging())
	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"createViaHeader " + viaHeaderImpl.toString());
        return viaHeaderImpl;
    }
    
    /**
     * Creates a HideHeader based on hide value
     * @param <var>hide</var> hide value
     * @throws IllegalArgumentException if hide is null
     * @throws SipParseException if hide is not accepted by implementation
     */
    public HideHeader createHideHeader(String hide)
    throws IllegalArgumentException,SipParseException {
        if ( hide==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : hide is null ");
        HideHeaderImpl hideHeaderImpl=new HideHeaderImpl();
        hideHeaderImpl.setHide(hide);
        
        return hideHeaderImpl;
    }
    
    /**
     * Creates a ViaHeader based on given host and port
     * @param <var>host</var> host
     * @param <var>port</var> port
     * @throws IllegalArgumentException if host is null
     * @throws SipParseException if host or port is not accepted by 
     * implementation
     */
    public ViaHeader createViaHeader(int port, InetAddress host)
    throws IllegalArgumentException,SipParseException {
	if (LogWriter.needsLogging()) 
  	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			" createViaHeader host =" + host +
			" port " + port);
        if ( host==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : host is null ");
        ViaHeaderImpl viaHeaderImpl=new ViaHeaderImpl();
        
        viaHeaderImpl.setHost(host);
        
        viaHeaderImpl.setPort(port);

	// Default transport.
        viaHeaderImpl.setTransport("UDP");
      
         
        return viaHeaderImpl;
    }
    
    /**
     * Creates a MaxForwardsHeader based on given number of max-forwards
     * @param <var>maxForwards</var> number of max-forwards
     * @throws SipParseException if maxForwards is not accepted by 
     * implementation
     */
    public MaxForwardsHeader createMaxForwardsHeader(int maxForwards)
    throws SipParseException {
        MaxForwardsHeaderImpl maxImpl=new MaxForwardsHeaderImpl();
        maxImpl.setMaxForwards(maxForwards);
         
        return maxImpl;
    }
    
    /**
     * Creates a ViaHeader based on given host and transport
     * @param <var>host</var> host
     * @param <var>transport</var> transport
     * @throws IllegalArgumentException if host or transport are null
     * @throws SipParseException if host or transport are not accepted by 
     * implementation
     */
    public ViaHeader createViaHeader(InetAddress host, String transport)
    throws IllegalArgumentException,SipParseException {
        
	if (LogWriter.needsLogging())
	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"createViaHeader " + host 
			+ " transport " + transport);
        if ( host==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : host is null ");
        if ( transport==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : transport is null ");
        ViaHeaderImpl viaHeaderImpl=new ViaHeaderImpl();
        viaHeaderImpl.setHost(host);
        viaHeaderImpl.setTransport(transport);
	if (LogWriter.needsLogging())
	    LogWriter.logMessage
		(LogWriter.TRACE_DEBUG, viaHeaderImpl.toString());
        return viaHeaderImpl;
    }
    
    /**
     * Creates an OrganizationHeader based on given organization
     * @param <var>organization</var> organization
     * @throws IllegalArgumentException if organization is null
     * @throws SipParseException if organization is not accepted by 
     * implementation
     */
    public OrganizationHeader createOrganizationHeader(String organization)
    throws IllegalArgumentException,SipParseException {
        if ( organization==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : organization is null ");
        OrganizationHeaderImpl organizationImpl=new OrganizationHeaderImpl();
        organizationImpl.setOrganization(organization);
        
        return organizationImpl;
    }
    
    /**
     * Creates a ViaHeader based on given host, port and transport
     * @param <var>host</var> host
     * @param <var>port</var> port
     * @param <var>transport</var> transport
     * @throws IllegalArgumentException if host or transport are null
     * @throws SipParseException if host, port or transport are not accepted by
     * implementation
     */
    public ViaHeader createViaHeader(String host, int port, String transport)
    throws IllegalArgumentException,SipParseException {
	if (LogWriter.needsLogging()) 
  	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"createViaHeader " + host 
			+ " port " + port 
			+ " transport " + transport);
        
        if ( host==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : host is null ");
        if ( transport==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : transport is null ");
	if (port < 0 ) 
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : bad port");
        ViaHeaderImpl viaHeaderImpl=new ViaHeaderImpl();
        
        viaHeaderImpl.setHost(host);
        viaHeaderImpl.setPort(port);
        viaHeaderImpl.setTransport(transport);
        
	if (LogWriter.needsLogging()) 
	    LogWriter.logMessage
		(LogWriter.TRACE_DEBUG, viaHeaderImpl.toString());
        return viaHeaderImpl;
    }
    
    /**
     * Creates a PriorityHeader based on given priority
     * @param <var>priority</var> priority
     * @throws IllegalArgumentException if priority is null
     * @throws SipParseException if priority is not accepted by implementation
     */
    public PriorityHeader createPriorityHeader(String priority)
    throws IllegalArgumentException,SipParseException {
        if ( priority==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : priority is null ");
        PriorityHeaderImpl priorityImpl=new PriorityHeaderImpl();
        priorityImpl.setPriority(priority);
        
        return priorityImpl;
    }
    
    /**
     * Creates a WWWAuthenticateHeader based on given scheme
     * @param <var>scheme</var> authentication scheme
     * @throws IllegalArgumentException if scheme is null
     * @throws SipParseException if scheme is not accepted by implementation
     */
    public WWWAuthenticateHeader createWWWAuthenticateHeader(String scheme)
    throws IllegalArgumentException,SipParseException {
        if ( scheme==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : scheme is null ");
        WWWAuthenticateHeaderImpl wwwImpl=new WWWAuthenticateHeaderImpl();
        wwwImpl.setScheme(scheme);
        return wwwImpl;
    }
    
    /**
     * Creates a ProxyAuthenticateHeader based on given scheme
     * @param <var>scheme</var> authentication scheme
     * @throws IllegalArgumentException if scheme is null
     * @throws SipParseException if scheme is not accepted by implementation
     */
    public ProxyAuthenticateHeader createProxyAuthenticateHeader(String scheme)
    throws IllegalArgumentException,SipParseException {
        if ( scheme==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : scheme is null ");
        ProxyAuthenticateHeaderImpl proxyImpl=new ProxyAuthenticateHeaderImpl();
	proxyImpl.setScheme(scheme);
        
        return proxyImpl;
    }
    
    /**
     * Creates a ProxyAuthorizationHeader based on given scheme
     * @param <var>scheme</var> authentication scheme
     * @throws IllegalArgumentException if scheme is null
     * @throws SipParseException if scheme is not accepted by implementation
     */
    public ProxyAuthorizationHeader 
	createProxyAuthorizationHeader(String scheme)
        throws IllegalArgumentException,SipParseException {
            System.out.println("ok");
        if ( scheme==null)
            throw new IllegalArgumentException
                            ("JAIN-SIP EXCEPTION : scheme is null ");
        ProxyAuthorizationHeaderImpl 
		proxyImpl=new ProxyAuthorizationHeaderImpl();
        proxyImpl.setScheme(scheme);
        
        return proxyImpl;
    }

    /**
     * Default constructor.
     */
     public HeaderFactoryImpl() {
/**
         try {
            SipStackImpl sipStackImpl=new SipStackImpl();
         } catch (SipPeerUnavailableException i){
            i.printStackTrace();
         }
**/
     }
    
}
