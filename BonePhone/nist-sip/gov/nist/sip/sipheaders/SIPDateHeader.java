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
* SIP Date header.
*
<pre>
* From SIP Rfc 2543 
*6.22 Date
*
*   Date is a general-header field. Its syntax is:
*
*
*
*        Date      =  "Date" ":" SIP-date
*        SIP-date  =  rfc1123-date
*
*
*   See [H14.18] for a definition of rfc1123-date. Note that unlike
*   HTTP/1.1, SIP only supports the most recent RFC 1123 [34] formatting
*   for dates.  As in [H3.3], SIP restricts the timezone in SIP-date to
*   "GMT", while RFC 1123 allows any timezone.
*
*        The consistent use of GMT between Date, Expires and Retry-
*        After headers allows implementation of simple clients that
*        do not have a notion of absolute time.  Note that rfc1123-
*        date is case-sensitive.
*
*   The Date header field reflects the time when the request or response
*   is first sent. Thus, retransmissions have the same Date header field
*   value as the original.
*
*   Registrars MUST include this header in REGISTER responses if they use
*   absolute expiration times and SHOULD include it for all responses.
*
*
*        The Date header field can be used by simple end systems
*        without a battery-backed clock to acquire a notion of
*        current time. However, in its GMT-form, it requires clients
*        to know their offset from GMT.
*</pre>
*
*@version 1.0 Changed the name to get rid of an annoying name clash with
*	JAIN-SIP.
*/
public class SIPDateHeader extends SIPHeader {
    
        /** date field
         */    
	protected SIPDate date;

        /** Default constructor.
         */        
	public SIPDateHeader() { 
            super(DATE);
        }

        /** Encode the header into a String.
         * @return String
         */        
	public String encode() {
		return headerName + COLON + SP + date.encode() + NEWLINE;
	}
        
        /** get the date field.
         * @return SIPDate
         */        
	public SIPDate getDate() { 
            return date;
        }

	/**
         * Set the date member
         * @param d SIPDate to set
         */
	public void setDate(SIPDate d) {
            date = d ;
            
        } 
        
}
