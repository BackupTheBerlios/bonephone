/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) ,added JAVADOC                 *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* SIPHeader names that are supported by this parser 
*
*@version 1.0
*@since 0.9
*
*/
public interface SIPHeaderNames {
    
        /** constant ERROR_INFO field.
         */
	public static final String ERROR_INFO = "Error-Info"; 
        
         /** constant ALSO field.
         */
	public static final String ALSO = "Also"; // Tentative in Bis 02
        
         /** constant MIME_VERSION field.
         */
	public static final String MIME_VERSION="Mime-Version";
        
         /** constant IN_REPLY_TO field.
         */
	public static final String IN_REPLY_TO="In-Reply-To";
        
         /** constant ALLOW field.
         */
	public static final String ALLOW="Allow";
        
         /** constant CONTENT_LANGUAGE field.
         */
	public static final String CONTENT_LANGUAGE="Content-Language";
        
         /** constant CALL_INFO field.
         */
	public static final String CALL_INFO="Call-Info";
        
         /** constant CSEQ field.
         */
	public static final String CSEQ="CSeq";
        
         /** constant ALERT_INFO field.
         */
	public static final String ALERT_INFO="Alert-Info";
        
         /** constant ACCEPT_ENCODING field.
         */
	public static final String ACCEPT_ENCODING="Accept-Encoding";
        
         /** constant ACCEPT field.
         */
	public static final String ACCEPT="Accept";
        
         /** constant ENCRYPTION field.
         */
	public static final String ENCRYPTION="Encryption";
        
         /** constant ACCEPT_LANGUAGE field.
         */
	public static final String ACCEPT_LANGUAGE="Accept-Language";
        
         /** constant RECORD_ROUTE field.
         */
	public static final String RECORD_ROUTE="Record-Route";
        
         /** constant TIMESTAMP field.
         */
	public static final String TIMESTAMP="Timestamp";
        
         /** constant TO field.
         */
	public static final String TO="To";
        
         /** constant VIA field.
         */
	public static final String VIA="Via";
        
         /** constant  FROM field.
         */
	public static final String FROM="From";
        
         /** constant CALL_ID field.
         */
	public static final String CALL_ID="Call-Id";
        
         /** constant AUTHORIZATION field.
         */
	public static final String AUTHORIZATION="Authorization";
        
         /** constant PROXY_AUTHENTICATE field.
         */
	public static final String PROXY_AUTHENTICATE="Proxy-Authenticate";
        
         /** constant SERVER field.
         */
	public static final String SERVER="Server";
        
         /** constant UNSUPPORTED field.
         */
	public static final String UNSUPPORTED="Unsupported";
        
         /** constant RETRY_AFTER field.
         */
	public static final String RETRY_AFTER="Retry-After";
        
         /** constant CONTENT_TYP field.
         */
	public static final String CONTENT_TYPE="Content-Type";

         /** constant CONTENT_ENCODING field.
         */
        public static final String CONTENT_ENCODING="Content-Encoding";
        
        /** constant CONTENT_LENGTH field.
         */
	public static final String CONTENT_LENGTH="Content-Length";
        
        /** constant  HIDE field.
         */
	public static final String HIDE="Hide";
	
        /** constant ROUTE field.
         */
        public static final String ROUTE="Route";
	
        /** constant CONTACT field.
         */
        public static final String CONTACT="Contact";
        
        /** constant WWW_AUTHENTICATE field.
         */
	public static final String WWW_AUTHENTICATE="WWW-Authenticate";
        
        /** constant MAX_FORWARDS field.
         */
	public static final String MAX_FORWARDS="Max-Forwards";
        
        /** constant ORGANIZATION field.
         */
	public static final String ORGANIZATION="Organization";
        
        /** constant PROXY_AUTHORIZATION field.
         */
	public static final String PROXY_AUTHORIZATION="Proxy-Authorization";
        
        /** constant PROXY_REQUIRE field.
         */
	public static final String PROXY_REQUIRE="Proxy-Require";
        
        /** constant REQUIRE  field.
         */
	public static final String REQUIRE="Require";
        
        /** constant CONTENT_DISPOSITION field.
         */
	public static final String CONTENT_DISPOSITION="Content-Disposition";
        
         /** constant SUBJECT field.
         */
	public static final String SUBJECT="Subject";
        
        /** constant USER_AGENT field.
         */
	public static final String USER_AGENT="User-Agent";
        
        /** constant WARNING field.
         */
	public static final String WARNING="Warning";
        
        /** constant PRIORITY field.
         */
	public static final String PRIORITY="Priority";
        
        /** constant DATE field.
         */
	public static final String DATE="Date";
        
        /** constant EXPIRES field.
         */
	public static final String EXPIRES="Expires";
        
        /** constant RESPONSE_KEY field.
         */
	public static final String RESPONSE_KEY="Response-Key";
        
        /** constant WARN_AGENT field.
         */
	public static final String WARN_AGENT="Warn-Agent";
        
        /** constant SUPPORTED field.
         */
	public static final String SUPPORTED = "Supported";
        
        /** constant REQUEST_LINE field.
         */
	// The following two are just for uniformity (not really Headers)
	public static final String REQUEST_LINE="Request-Line"; 
        
        /** constant STATUS_LINE field.
         */
	public static final String STATUS_LINE ="Status-Line";
        
}

