/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: M. Ranganathan (mranga@nist.gov) Created on April 19, 2001, 6:31 PM *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package gov.nist.jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.header.*;
import gov.nist.jain.protocol.ip.sip.header.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import java.util.Hashtable;
import gov.nist.log.*;

/**
 * Map JAIN headers to NIST-SIP headers and vice versa.
 * @author  mranga
 * @version 1.0
 */
public class HeaderMap  implements PackageNames {

    protected static String JAIN_PACKAGE = "jain.protocol.ip.sip.header";
    protected static String JAIN_IMPL_PACKAGE = 
				"gov.nist.jain.protocol.ip.sip.header";
    protected static Hashtable forwardNameMap;
    protected static Hashtable forwardClassMap;
    
    protected static Hashtable reverseClassMap;
    protected static Hashtable jainImplementationMap;
    protected static Hashtable jainForwardNameMap;
    static {
	initializeReverseClassMap();
        jainInitializeImplementationMap(); 
	initializeForwardNameMap();
	initializeForwardClassMap();
    }

    private static void putClassMap(Hashtable map,
	String keyClassName, String objectClassName) {
	try {
	   Class key = Class.forName(keyClassName);
	   Class value = Class.forName(objectClassName);
	   if (map == null) throw new IllegalArgumentException("null!!");
		map.put(key,value);
	} catch (ClassNotFoundException ex) {
		LogWriter.logException(ex);
		System.exit(0);
	}

    }


   /** 
    * Initialize the map that maps NIST-SIP header classes to JAIN classes.
    */
    private static void initializeReverseClassMap() {
            reverseClassMap = new Hashtable();
            try {
				
                reverseClassMap.put(SIPHEADERS_PACKAGE+".AcceptEncoding", 
                    Class.forName(JAIN_IMPL_PACKAGE + 
					".AcceptEncodingHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Accept", 
                    Class.forName(JAIN_IMPL_PACKAGE + 
					".AcceptHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".AcceptLanguage", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".AcceptLanguageHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Allow", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".AllowHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Authorization", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".AuthorizationHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".CSeq", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".CSeqHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".CallID", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".CallIdHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Contact", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ContactHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".ContentEncoding", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+".ContentEncodingHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".ContentLength", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ContentLengthHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".ContentType", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ContentTypeHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".SIPDateHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".DateHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Encryption", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".EncryptionHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Expires", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ExpiresHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".From", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".FromHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Hide", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".HideHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".MaxForwards", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".MaxForwardsHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Organization", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".OrganizationHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Priority", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".PriorityHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".ProxyAuthenticate", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ProxyAuthenticateHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".ProxyAuthorization", 
                    Class.forName(JAIN_IMPL_PACKAGE 
					+ ".ProxyAuthorizationHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".ProxyRequire", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ProxyRequireHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".RecordRoute", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RecordRouteHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Require", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RequireHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".ResponseKey", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ResponseKeyHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".RetryAfter", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RetryAfterHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Route", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RouteHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Server", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ServerHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Subject", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".SubjectHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".TimeStamp", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".TimeStampHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".To", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ToHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Unsupported", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".UnsupportedHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".UserAgent", 
                    Class.forName(JAIN_IMPL_PACKAGE
				+".UserAgentHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Via", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ViaHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".WWWAuthenticate", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+".WWWAuthenticateHeaderImpl"));
                reverseClassMap.put(SIPHEADERS_PACKAGE+".Warning",
                    Class.forName(JAIN_IMPL_PACKAGE +".WarningHeaderImpl"));
                
            } catch (ClassNotFoundException ex) {
		LogWriter.logException(ex);
                System.exit(0);
            }
    }

   private static void putForwardMap(String jainName, Class nistClass ) {
		// A map from jain header name to nist class.
		forwardNameMap.put(jainName,nistClass);
   }

   /**
    * Initialize name map.
    * (A table that maps JAIN header names to correspondign NIST-SIP Classes)
    */
    private static void initializeForwardNameMap() {
                try {
                    forwardNameMap = new Hashtable();
		    jainForwardNameMap = new Hashtable();
                    putForwardMap(AcceptEncodingHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".AcceptEncoding"));
                    putForwardMap(AcceptHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".Accept"));
                    putForwardMap(AcceptLanguageHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".AcceptLanguage"));
                    putForwardMap(AllowHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".Allow"));
                    putForwardMap(AuthorizationHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".Authorization"));
                    putForwardMap(CSeqHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".CSeq"));
                    putForwardMap(CallIdHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".CallID")); 
                    putForwardMap(ContactHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".Contact")); 
                    putForwardMap(ContentEncodingHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE 
						+ ".ContentEncoding")); 
                    putForwardMap(ContentLengthHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".ContentLength")); 
                    putForwardMap(ContentTypeHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".ContentType")); 
                    putForwardMap(DateHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".SIPDateHeader")); 
                    putForwardMap(EncryptionHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".Encryption")); 
                    putForwardMap(ExpiresHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".Expires")); 
                    putForwardMap(FromHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".From")); 
                    putForwardMap(HideHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".Hide"));
                    putForwardMap(MaxForwardsHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE + ".MaxForwards"));
                    putForwardMap(OrganizationHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".Organization"));
                    putForwardMap(PriorityHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".Priority"));
                    putForwardMap(ProxyAuthenticateHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  +
                                            ".ProxyAuthenticate"));
                    putForwardMap(ProxyAuthorizationHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + 
                                            ".ProxyAuthorization"));
                    putForwardMap(ProxyRequireHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".ProxyRequire"));
                    putForwardMap(RecordRouteHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".RecordRoute"));
                    putForwardMap(RequireHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".Require"));
                    putForwardMap(ResponseKeyHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".ResponseKey")); 
                    putForwardMap(RetryAfterHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".RetryAfter"));
                    putForwardMap(RouteHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".Route"));
                    putForwardMap(ServerHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".Server"));
                    putForwardMap(SubjectHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".Subject"));
                    putForwardMap(TimeStampHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".TimeStamp"));
                    putForwardMap(ToHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".To"));
                    putForwardMap(UnsupportedHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".Unsupported"));
                    putForwardMap(UserAgentHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + 
                                           ".UserAgent"));
                    putForwardMap(ViaHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  + ".Via"));
                    putForwardMap(WWWAuthenticateHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  +".WWWAuthenticate"));
                    putForwardMap(WarningHeader.name.toLowerCase(),
                        Class.forName(SIPHEADERS_PACKAGE  +".Warning"));
                } catch (ClassNotFoundException ex) {
		    LogWriter.logException(ex);
                    System.exit(0);
                }
    }

    /**
    * Initialize the class mapping table. (a table that maps JAIN header
    * implementation classes to the corresponding NIST-SIP classes).
    */
    private static void initializeForwardClassMap() {
	    forwardClassMap = new Hashtable();
	    putClassMap
               (forwardClassMap, 
		JAIN_IMPL_PACKAGE + ".AcceptEncodingHeaderImpl",
	        SIPHEADERS_PACKAGE + ".AcceptEncoding"
	     );
	    putClassMap
               (forwardClassMap, 
		JAIN_IMPL_PACKAGE + ".AcceptHeaderImpl", 
		SIPHEADERS_PACKAGE + ".Accept"
	     );
            putClassMap
	     (forwardClassMap,
		JAIN_IMPL_PACKAGE + ".AcceptLanguageHeaderImpl",
		SIPHEADERS_PACKAGE + ".AcceptLanguage"
	     );
             putClassMap
	     (forwardClassMap,
		JAIN_IMPL_PACKAGE + ".AllowHeaderImpl",
		SIPHEADERS_PACKAGE + ".Allow"
	     );
             putClassMap
	     (forwardClassMap,
		JAIN_IMPL_PACKAGE + ".AuthorizationHeaderImpl", 
		SIPHEADERS_PACKAGE + ".Authorization"
	     );
            putClassMap
	     (forwardClassMap,
                JAIN_IMPL_PACKAGE + ".CSeqHeaderImpl", 
		SIPHEADERS_PACKAGE + ".CSeq"
	     );
             putClassMap
	     (forwardClassMap,
                JAIN_IMPL_PACKAGE + ".CallIdHeaderImpl",
		SIPHEADERS_PACKAGE + ".CallID" 
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".ContactHeaderImpl", 
	       SIPHEADERS_PACKAGE + ".Contact" 
	     );
             putClassMap
	     (forwardClassMap,
                JAIN_IMPL_PACKAGE + ".ContentEncodingHeaderImpl",
		SIPHEADERS_PACKAGE + ".ContentEncoding"
	     );
             putClassMap
	     (forwardClassMap,
                 JAIN_IMPL_PACKAGE + ".ContentLengthHeaderImpl", 
		SIPHEADERS_PACKAGE + ".ContentLength"
	     );
             putClassMap
	     (forwardClassMap,
                JAIN_IMPL_PACKAGE + ".ContentTypeHeaderImpl", 
		SIPHEADERS_PACKAGE + ".ContentType"
	     );
             putClassMap
	     (forwardClassMap,
                JAIN_IMPL_PACKAGE + ".DateHeaderImpl", 
		SIPHEADERS_PACKAGE + ".SIPDateHeader" 
	     );
             putClassMap
	     (forwardClassMap,
                JAIN_IMPL_PACKAGE + ".EncryptionHeaderImpl",
		SIPHEADERS_PACKAGE + ".Encryption"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".ExpiresHeaderImpl",  
	       SIPHEADERS_PACKAGE + ".Expires"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".FromHeaderImpl", 
		SIPHEADERS_PACKAGE + ".From"
	     );
             putClassMap
	     (forwardClassMap,
		JAIN_IMPL_PACKAGE + ".HideHeaderImpl", 
		SIPHEADERS_PACKAGE + ".Hide"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".MaxForwardsHeaderImpl",
		SIPHEADERS_PACKAGE + ".MaxForwards"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".OrganizationHeaderImpl",
	       SIPHEADERS_PACKAGE + ".Organization"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".PriorityHeaderImpl", 
		SIPHEADERS_PACKAGE + ".Priority"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".ProxyAuthenticateHeaderImpl", 
		SIPHEADERS_PACKAGE + ".ProxyAuthenticate"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".ProxyAuthorizationHeaderImpl",
		SIPHEADERS_PACKAGE + ".ProxyAuthorization"
	     );
             putClassMap
	     (forwardClassMap,
              JAIN_IMPL_PACKAGE + ".ProxyRequireHeaderImpl", 
		SIPHEADERS_PACKAGE + ".ProxyRequire"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".RecordRouteHeaderImpl",
		SIPHEADERS_PACKAGE + ".RecordRoute"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".RequireHeaderImpl", 
		SIPHEADERS_PACKAGE + ".Require"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".ResponseKeyHeaderImpl", 
		SIPHEADERS_PACKAGE + ".ResponseKey"
	     );
             putClassMap
	     (forwardClassMap,
              JAIN_IMPL_PACKAGE + ".RetryAfterHeaderImpl", 
	      SIPHEADERS_PACKAGE + ".RetryAfter"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".RouteHeaderImpl",  
	       SIPHEADERS_PACKAGE + ".Route"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".ServerHeaderImpl", 
	       SIPHEADERS_PACKAGE + ".Server"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".SubjectHeaderImpl",
	       SIPHEADERS_PACKAGE + ".Subject"
	     );
             putClassMap
	     (forwardClassMap,
               JAIN_IMPL_PACKAGE + ".TimeStampHeaderImpl", 
	       SIPHEADERS_PACKAGE + ".TimeStamp"
	     );
             putClassMap
	     (forwardClassMap,
                JAIN_IMPL_PACKAGE + ".ToHeaderImpl",
	        SIPHEADERS_PACKAGE + ".To"
	     );
             putClassMap
	     (forwardClassMap,
                JAIN_IMPL_PACKAGE + ".UnsupportedHeaderImpl", 
		SIPHEADERS_PACKAGE + ".Unsupported"
	     );
             putClassMap
	     (forwardClassMap,
                JAIN_IMPL_PACKAGE + ".UserAgentHeaderImpl", 
		SIPHEADERS_PACKAGE + ".UserAgent"
	     );
             putClassMap
	     (forwardClassMap,
                 JAIN_IMPL_PACKAGE + ".ViaHeaderImpl", 
		SIPHEADERS_PACKAGE + ".Via"
	     );
             putClassMap
	     (forwardClassMap,
                JAIN_IMPL_PACKAGE + ".WWWAuthenticateHeaderImpl", 
		SIPHEADERS_PACKAGE + ".WWWAuthenticate"
	     );
             putClassMap
	     (forwardClassMap,
                 JAIN_IMPL_PACKAGE + ".WarningHeaderImpl",
		SIPHEADERS_PACKAGE + ".Warning"
	     );

    }


    /** Get the NIST header for a given JAIN header.
    *@param jainHeader is the jain header class for which we want the NIST
    *  header class.
    */
    public static Class getNISTHeaderClassFromJAINHeader(Class jainHeader) {
	return (Class) forwardClassMap.get(jainHeader);
    }

    
    /**
    * Create an istance of a NIST-SIP header from a jain header.
    * @param jainHeader is the jain header class for which we want to 
    *   instantiate a sip header object.
    */
    public static SIPHeader getNISTHeaderFromJAINHeader(Class jainHeader) {

	    Class nistClass = (Class) forwardClassMap.get(jainHeader);
	    if (nistClass == null) {
		return null;
	   }


	    try {
		Object retval = nistClass.newInstance();
		return (SIPHeader) retval;
	    } catch (Exception ex) {
	        LogWriter.logException(ex);
		System.exit(0);
	   }
	   return null;
    }


    /** Get the NIST header class from the given JAIN header name.
    *@param name is the name of the JAIN Header.
    */
    public static 
	Class getNISTHeaderClassFromJAINHeader( String name) {
        
            Class retval = (Class) forwardNameMap.get(name.toLowerCase());
            return retval;        
    }

    /**
     * Construct a JAIN header from the corresponding SIP Header.
     *@param <var> nistHeader </var> 
     * The object for which we want to construct a JAIN header.
     */

    public static 
       HeaderImpl getJAINHeaderFromNISTHeader(SIPHeader nistHeader) {

	HeaderImpl retval = null;
	if (nistHeader instanceof ExtensionHeader) {
	    retval = new HeaderImpl();
	    try {
	       retval.setImplementationObject(nistHeader);
	    } catch (Exception ex) {
		if (LogWriter.needsLogging()) LogWriter.logException(ex);
		System.out.println("Fatal error!");
		ex.printStackTrace();
		System.exit(0);
	    }
	} else {
	    Class hdrclass = nistHeader.getClass();
	    retval = (HeaderImpl) getJAINHeaderFromNISTHeader(hdrclass);
	}
	retval.setImplementationObject(nistHeader);
	return retval;
    }

   
    
    /** 
    * Initialize the map that maps JAIN-SIP header classes to JAIN 
    * implementation classes.
    */
    private static void jainInitializeImplementationMap() {
            jainImplementationMap = new Hashtable();
            try {
                jainImplementationMap.put(JAIN_PACKAGE+".AcceptEncodingHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE + 
					".AcceptEncodingHeaderImpl"));
                jainImplementationMap.put(AcceptEncodingHeader.name,
                    Class.forName(JAIN_IMPL_PACKAGE + 
					".AcceptEncodingHeaderImpl"));


                jainImplementationMap.put(JAIN_PACKAGE+".AcceptHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE + 
					".AcceptHeaderImpl"));
                jainImplementationMap.put(AcceptHeader.name,
                    Class.forName(JAIN_IMPL_PACKAGE + 
					".AcceptHeaderImpl"));


                jainImplementationMap.put(JAIN_PACKAGE+".AcceptLanguageHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".AcceptLanguageHeaderImpl"));
                jainImplementationMap.put(AcceptLanguageHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".AcceptLanguageHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".AllowHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".AllowHeaderImpl"));

		// BUGBUG -- problem with the JAIN interface

                jainImplementationMap.put("Allow", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".AllowHeaderImpl"));


                jainImplementationMap.put(JAIN_PACKAGE+".AuthorizationHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".AuthorizationHeaderImpl"));
                jainImplementationMap.put(AuthorizationHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".AuthorizationHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".CSeqHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".CSeqHeaderImpl"));
                jainImplementationMap.put(CSeqHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".CSeqHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".CallIdHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".CallIdHeaderImpl"));
                jainImplementationMap.put(CallIdHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".CallIdHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".ContactHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ContactHeaderImpl"));
                jainImplementationMap.put(ContactHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ContactHeaderImpl"));


                jainImplementationMap.put
		    (JAIN_PACKAGE+".ContentEncodingHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+".ContentEncodingHeaderImpl"));
                jainImplementationMap.put
		    (ContentEncodingHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+".ContentEncodingHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".ContentLengthHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ContentLengthHeaderImpl"));
                jainImplementationMap.put(ContentLengthHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ContentLengthHeaderImpl"));


                jainImplementationMap.put(JAIN_PACKAGE+".ContentTypeHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ContentTypeHeaderImpl"));
                jainImplementationMap.put(ContentTypeHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ContentTypeHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".DateHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".DateHeaderImpl"));
                jainImplementationMap.put(DateHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".DateHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".EncryptionHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".EncryptionHeaderImpl"));
                jainImplementationMap.put(EncryptionHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".EncryptionHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".ExpiresHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ExpiresHeaderImpl"));
                jainImplementationMap.put(ExpiresHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ExpiresHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".FromHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".FromHeaderImpl"));
                jainImplementationMap.put(FromHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".FromHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".HideHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".HideHeaderImpl"));
                jainImplementationMap.put(HideHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".HideHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".MaxForwardsHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".MaxForwardsHeaderImpl"));
                jainImplementationMap.put(MaxForwardsHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".MaxForwardsHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".OrganizationHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".OrganizationHeaderImpl"));
                jainImplementationMap.put(OrganizationHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".OrganizationHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".PriorityHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".PriorityHeaderImpl"));
                jainImplementationMap.put(PriorityHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".PriorityHeaderImpl"));

                jainImplementationMap.put
			(JAIN_PACKAGE+".ProxyAuthenticateHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
			+ ".ProxyAuthenticateHeaderImpl"));
                jainImplementationMap.put
			(ProxyAuthenticateHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
			+ ".ProxyAuthenticateHeaderImpl"));


                jainImplementationMap.put
			(JAIN_PACKAGE+".ProxyAuthorizationHeader", 
                         Class.forName(JAIN_IMPL_PACKAGE 
					+ ".ProxyAuthorizationHeaderImpl"));
                jainImplementationMap.put
			(ProxyAuthorizationHeader.name, 
                         Class.forName(JAIN_IMPL_PACKAGE 
					+ ".ProxyAuthorizationHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".ProxyRequireHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ProxyRequireHeaderImpl"));
                jainImplementationMap.put(ProxyRequireHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ProxyRequireHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".RecordRouteHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RecordRouteHeaderImpl"));
                jainImplementationMap.put(RecordRouteHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RecordRouteHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".RequireHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RequireHeaderImpl"));
                jainImplementationMap.put(RequireHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RequireHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".ResponseKeyHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ResponseKeyHeaderImpl"));
                jainImplementationMap.put(ResponseKeyHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ResponseKeyHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".RetryAfterHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RetryAfterHeaderImpl"));
                jainImplementationMap.put(RetryAfterHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RetryAfterHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".RouteHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RouteHeaderImpl"));
                jainImplementationMap.put(RouteHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".RouteHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".ServerHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ServerHeaderImpl"));
                jainImplementationMap.put(ServerHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ServerHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".SubjectHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".SubjectHeaderImpl"));
                jainImplementationMap.put(SubjectHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".SubjectHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".TimeStampHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".TimeStampHeaderImpl"));
                jainImplementationMap.put(TimeStampHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".TimeStampHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".ToHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ToHeaderImpl"));
                jainImplementationMap.put(ToHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ToHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".UnsupportedHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".UnsupportedHeaderImpl"));
                jainImplementationMap.put(UnsupportedHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".UnsupportedHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".UserAgentHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE
				+".UserAgentHeaderImpl"));
                jainImplementationMap.put(UserAgentHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE
				+".UserAgentHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".ViaHeader", 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ViaHeaderImpl"));
                jainImplementationMap.put(ViaHeader.name, 
                    Class.forName(JAIN_IMPL_PACKAGE 
				+ ".ViaHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".WWWAuthenticateHeader",
                    Class.forName(JAIN_IMPL_PACKAGE 
				+".WWWAuthenticateHeaderImpl"));
                jainImplementationMap.put(WWWAuthenticateHeader.name,
                    Class.forName(JAIN_IMPL_PACKAGE 
				+".WWWAuthenticateHeaderImpl"));

                jainImplementationMap.put(JAIN_PACKAGE+".WarningHeader",
                    Class.forName(JAIN_IMPL_PACKAGE +".WarningHeaderImpl"));
                jainImplementationMap.put(WarningHeader.name,
                    Class.forName(JAIN_IMPL_PACKAGE +".WarningHeaderImpl"));
                
            } catch (ClassNotFoundException ex) {
		LogWriter.logException(ex);
                System.exit(0);
            }
    }

     /** Get the JAIN Header class for a given NIST header.
      *@param nistHeader Nist haeder class that we want to map.
      *@return the corresponding JAIN Class.
      */
     public static Class getJAINHeaderClassFromNISTHeader( Class nistHeader) {
		Class jainClass = (Class) reverseClassMap.get
				(nistHeader.getName());
		return jainClass;
     }
    
    /**
     *Get the JAIN Class from the corresponding nist-sip header class.
     *@param <var> nistHeader </var>  The class of the 
     *  NIST header that we want the corresponding JAIN header interface for.
     */
    public static 
	HeaderImpl getJAINHeaderFromNISTHeader(Class nistHeader) {
       // System.out.println(nistHeader.getName());
        Class jainClass = (Class) reverseClassMap.get(nistHeader.getName());
        HeaderImpl jainHeader = null;
        try {
            if ( jainClass==null) {
		throw new IllegalArgumentException
                        ("Mapping not defined for " + nistHeader.getName());
            } else jainHeader = (HeaderImpl) jainClass.newInstance(); 
            
        } catch (InstantiationException ex) {
	    LogWriter.logException(ex);
            System.exit(0);
        } catch (ClassCastException ex) {
	    LogWriter.logException(ex);
            System.exit(0);
        } catch (IllegalAccessException ex) {
	    LogWriter.logException(ex);
            System.exit(0);
        }
      
        return jainHeader;
    }

    /**
     * Get the JAIN Implementation Class 
     *  from the corresponding JAIN header class.
     * @param <var> jainHeader </var>  The class of the Jain header
     *   that we want the corresponding JAIN 
     *   implementation header interface for.
     */
    public static 
	HeaderImpl getJAINHeaderImplFromJAINHeader(Class jainHeader) {
	Class jainClass = null;
	// BUGBUG -- Compensate for bug in the JAIN interface.
	if (jainHeader.getName().equals("jain.protocol.ip.sip.header.Allow")) {
		try {
		   jainClass = Class.forName(JAIN_IMPL_PACKAGE + "AllowHeaderImpl");
		} catch (ClassNotFoundException ex) {
			LogWriter.logException(ex);
			System.exit(0);
		}
	} else {
	     jainClass = 
		(Class) jainImplementationMap.get(jainHeader.getName());
	}
        HeaderImpl jainHeaderImpl = null;
        try {
            jainHeaderImpl = (HeaderImpl) jainClass.newInstance();
        } catch (InstantiationException ex) {
	    LogWriter.logException(ex);
            System.exit(0);
        } catch (ClassCastException ex) {
	    LogWriter.logException(ex);
            System.exit(0);
        } catch (IllegalAccessException ex) {
	    LogWriter.logException(ex);
            System.exit(0);
        }
        return jainHeaderImpl;
    }

    /** Get the JAIN Implementation class from the Jain interface class.
    * @param name is the name of the SIP header for which we want the 
    * JAIN Implementation class.
    *@return a class that is the JAIN implementation class for the 
    * header name we want to map.
    */
    public static Class getJAINImplementationClassFromName
		(String name) {
	Class retval = (Class) forwardNameMap.get(name.toLowerCase());
	return retval;
    }
    
}
