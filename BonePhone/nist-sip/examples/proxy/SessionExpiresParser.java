/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov)                                    *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package examples.proxy;
import gov.nist.sip.stack.security.*;
import gov.nist.sip.stack.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.net.*;
import java.util.StringTokenizer;

/**
 * Implementation of the Session-Expires parser
 * This is an usage example of the NIST SIP Parser extension mechanism
 */

public class SessionExpiresParser implements ExtensionParser {

    /**
     * Constructor
     */

    public SessionExpiresParser() {
    }

    /**
     * Parse the extension header and return a Header of 
     * the appropriate class.
     * @return A Session Expires Header
     */

    public SIPHeader parseExtensionHeader(String extension)
	throws SIPParseException {
	// get the header name and value
	StringTokenizer st = new StringTokenizer(extension, ":");
	String header = st.nextToken();
	String value = st.nextToken();
	// remove the white spaces
	st = new StringTokenizer(value);
	String refreshValue = st.nextToken();
	SessionExpiresHeader sessionExpiresHeader = new 
	    SessionExpiresHeader(header);
	/* Here, we assume that the value is a 'delta-seconds' and not
	 * a 'SIP-date'. 
	 */
	try {
	    sessionExpiresHeader.setRefreshDate(new Integer(refreshValue));
	} catch (NumberFormatException ex) {
	   try {
	     StringMsgParser stringMsgParser  = new StringMsgParser();
	     SIPDate sipdate = stringMsgParser.parseSIPDate(value);
	     sessionExpiresHeader.setRefreshDate(sipdate);
	   } catch (SIPParseException ex1) {
		throw new SIPParseException ("Bad sip date. " + value);
	   }
	}
	return sessionExpiresHeader;
    }
}
