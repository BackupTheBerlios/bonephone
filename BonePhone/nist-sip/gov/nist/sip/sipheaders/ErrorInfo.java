/******************************************************
 * File: ErrorInfo.java
 * created 30-Jan-01 11:44:45 AM by mranga
 */


package gov.nist.sip.sipheaders;
import gov.nist.sip.net.*;
import gov.nist.sip.*;

/**
* Error Info sip header.
*@since v1.0
*<pre>
*
* 6.24 Error-Info
*
*   The Error-Info response header provides a pointer to additional
*   information about the error status response. This header field is
*   only contained in 3xx, 4xx, 5xx and 6xx responses.
*
*
*     
*       Error-Info  =  "Error-Info" ":" # ( "<" URI ">" *( ";" generic-param ))
*</pre>
*
*/
public final class ErrorInfo extends SIPHeader {
    
	URI uri;
        
	NameValueList parms;
	
        /** Default constructor.
         */        
	public ErrorInfo() {
		super (ERROR_INFO);
		parms = new NameValueList("errorInfoParms");
	}
	
	/**
         * Encode into canonical form.
         * @return String
         */
	public String encode() {
	  String retval  = headerName + COLON + SP + 
	  			LESS_THAN + uri.encode() + GREATER_THAN ;
	   if (! parms.isEmpty() ) {
		   retval += SEMICOLON + parms.encode();
	   }
	   return retval + NEWLINE;
	}

	/**
         * Add a parameter.
         * @param nv NameValue to set.
         */
	public void addParam(NameValue nv) {
		parms.add(nv);
	}

	/**
         * get the uri field.
         * @return URI
         */
	public URI getURI () {
		return uri;
	}

        /** set the URI.
         * @param u URI to set
         */        
	public void setURI ( URI u ) {
		uri = u;
	}

	/**
         * get a specific parameter.
         * @param name String to set.
         * @return String
         */
	public String getParm(String name) {
		NameValue nv = parms.getNameValue(name);
		if (nv == null) return null;
		else return (String) nv.getValue();
	}

	/**
         * get the entire parameter list.
         * @return NameValueList
         */
	public NameValueList getParms() {
		return parms;
	}

}
