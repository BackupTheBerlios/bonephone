/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import gov.nist.sip.net.*;

/**
* CallInfo SIPHeader.
* <pre>
* 6.14 Call-Info
* 
*    The Call-Info general header field provides additional information
*    about the caller or callee, depending on whether it is found in a
*    request or response. The purpose of the URI is described by the
*    "purpose" parameter. "icon" designates an image suitable as an iconic
*    representation of the caller or callee; "info" describes the caller
*    or callee in general, e.g., through a web page; "card" provides a
*    business card (e.g., in vCard [30] or LDIF [31] formats).
* 
* 
* 
*         Call-Info   =  "Call-Info" ":" # ( "<" URI ">" *( ";" info-param) )
*         info-param  =  "purpose" "=" ( "icon" | "info" | "card" | token )
*                    |   generic-param
* 
* 
*    Example:
* 
*    Call-Info: <http://wwww.example.com/alice/photo.jpg> ;purpose=icon,
*      <http://www.example.com/alice/> ;purpose=info
* </pre>
*
*/

public final class CallInfo  extends SIPHeader implements CallInfoKeywords {
    
        /** purpose field
         */    
	protected String purpose;

        /** uri field
         */        
        protected URI	uri;
  
        /** Default constructor
         */        
	public CallInfo() {
		super(CALL_INFO);
	}
        
	/**
         * Return canonical representation.
         * @return String 
         */
	public String encode() {
		return headerName + COLON + SP + LESS_THAN + uri.encode() +
				GREATER_THAN + SEMICOLON + PURPOSE + EQUALS
				+ purpose + NEWLINE;
	}

        /** get the purpose field
         * @return String
         */        
	public String getPurpose () {
            return purpose;
        }

        /** get the URI field
         * @return URI
         */        
	public URI getUri() {
            return uri;
        }

        /** set the purpose field
         * @param p String to set
         */        
	public void setPurpose( String p ) {
            purpose = p ;
        }

        /** set the URI field
         * @param u URI to set
         */        
	public void setUri( URI u ) {
            uri = u;
        }
        
}
