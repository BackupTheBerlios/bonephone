package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import java.util.Hashtable;

/** A mapping class that returns the SIPHeader for a given header name.
*/
public class NameMap implements SIPHeaderNames,PackageNames {
	static Hashtable nameMap;
	static Hashtable reverseMap;
	static { initializeNameMap(); }

	protected static void putNameMap(String headerName, String className) {
		nameMap.put(headerName.toLowerCase(),
			SIPHEADERS_PACKAGE + "." + className);
	}

	public static Class getClassFromName(String headerName) {
		String className = (String) 
			nameMap.get(headerName.toLowerCase());
		if (className == null) return null;
		else {
			try {
			   return  Class.forName(className);
			} catch (ClassNotFoundException ex) {
			    return null;
			}
		}
	}

	private static void initializeNameMap() {
		nameMap = new Hashtable();

		putNameMap(
		ERROR_INFO, "ErrorInfo"
		);
	
		putNameMap(
		ALSO ,"Also"
		);
	
		putNameMap(
		MIME_VERSION,"MimeVersion"
		);
	
		putNameMap(
		IN_REPLY_TO,"InReplyTo"
		);
	
		putNameMap(
		ALLOW,"Allow"
		);
	
		putNameMap(
		CONTENT_LANGUAGE,"ContentLanguage"
		);

		putNameMap(
		CALL_INFO,"CallInfo"
		);
	
		putNameMap(
		CSEQ,"CSeq"
		);

		putNameMap(
		ALERT_INFO,"AlertInfo"
		);
	
		putNameMap(
		ACCEPT_ENCODING,"AcceptEncoding"
		);
	
		putNameMap(
		ACCEPT,"Accept"
		);
	
		putNameMap(
		ENCRYPTION,"Encryption"
		);
	
		putNameMap(
		ACCEPT_LANGUAGE,"AcceptLanguage"
		);
	
		putNameMap(
		RECORD_ROUTE,"RecordRoute"
		);
	
		putNameMap(
		TIMESTAMP,"Timestamp"
		);
	
		putNameMap(
		TO,"To"
		);

		putNameMap(
		VIA,"Via"
		);

		putNameMap(
		FROM,"From"
		);

		putNameMap(
		CALL_ID,"CallId"
		);

		putNameMap(
		AUTHORIZATION,"Authorization"
		);

		putNameMap(
		PROXY_AUTHENTICATE,"ProxyAuthenticate"
		);

		putNameMap(
		SERVER,"Server"
		);

		putNameMap(
		UNSUPPORTED,"Unsupported"
		);

		putNameMap(
		RETRY_AFTER,"RetryAfter"
		);

		putNameMap(
		CONTENT_TYPE,"ContentType"
		);

		putNameMap(
        	CONTENT_ENCODING,"ContentEncoding"
		);

		putNameMap(
		CONTENT_LENGTH,"ContentLength"
		);

		putNameMap(
		HIDE,"Hide"
		);

		putNameMap(
        	ROUTE,"Route"
		);

		putNameMap(
        	CONTACT,"Contact"
		);

		putNameMap(
		WWW_AUTHENTICATE,"WWWAuthenticate"
		);

		putNameMap(
		MAX_FORWARDS,"MaxForwards"
		);

		putNameMap(
		ORGANIZATION,"Organization"
		);

		putNameMap(
		PROXY_AUTHORIZATION,"ProxyAuthorization"
		);

		putNameMap(
		PROXY_REQUIRE,"ProxyRequire"
		);

		putNameMap(
		REQUIRE,"Require"
		);

		putNameMap(
		CONTENT_DISPOSITION,"ContentDisposition"
		);

		putNameMap(
		SUBJECT,"Subject"
		);

		putNameMap(
		USER_AGENT,"UserAgent"
		);

		putNameMap(
		WARNING,"Warning"
		);

		putNameMap(
		PRIORITY,"Priority"
		);

		putNameMap(
		DATE,"SIPDateHeader"
		);

		putNameMap(
		EXPIRES,"Expires"
		);

		putNameMap(
		RESPONSE_KEY,"ResponseKey"
		);

		putNameMap(
		WARN_AGENT,"Warn-Agent"
		);

		putNameMap(
		SUPPORTED, "Supported"
		);
        
	}

}
