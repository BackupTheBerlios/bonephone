/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;

import java.util.*;

/**
* Expires SIP Header.
*<pre>
*6.25 Expires
*
*   The Expires entity-header field gives the date and time after which
*   the message content expires.
*
*   This header field is currently defined only for the REGISTER and
*   INVITE methods. For REGISTER, it is a request and response-header
*   field. In a REGISTER request, the client indicates how long it wishes
*   the registration to be valid. In the response, the server indicates
*   the earliest expiration time of all registrations. The server MAY
*
*
*
*Handley/Schulzrinne/Schooler/Rosenberg                       [Page 62]
*
*Internet Draft                    SIP                  November 24, 2000
*
*
*   choose a shorter time interval than that requested by the client, but
*   SHOULDNOT choose a longer one.  If a registration updates an existing
*   registration, the Expires value of the most recent registration is
*   used, even if it is shorter than the earlier registration.
*
*   For INVITE requests, it is a request and response-header field. In a
*   request, the caller can limit the validity of an invitation, for
*   example, if a client wants to limit the time duration of a search or
*   a conference invitation. A user interface MAY take this as a hint to
*   leave the invitation window on the screen even if the user is not
*   currently at the workstation. This also limits the duration of a
*   search. If the request expires before the search completes, the proxy
*   returns a 408 (Request Timeout) status. In a 302 (Moved Temporarily)
*   response, a server can advise the client of the maximal duration of
*   the redirection.
*
*   Note that the expiration time does not affect the duration of the
*   actual session that may result from the invitation. Session
*   description protocols may offer the ability to express time limits on
*   the session duration, however.
*
*   The value of this field can be either a SIP-date or an integer number
*   of seconds (in decimal), measured from the receipt of the request.
*   The latter approach is preferable for short durations, as it does not
*   depend on clients and servers sharing a synchronized clock.
*   Implementations MAY treat values larger than 2**32-1 (4294967295 or
*   136 years) as equivalent to 2**32-1.
*
*
*
*        Expires  =  "Expires" ":" ( SIP-date | delta-seconds )
*
*
*   Two examples of its use are
*
*     Expires: Thu, 01 Dec 1994 16:00:00 GMT
*    Expires: 5
*
*
*
*</pre>
*
*/
public class Expires extends SIPHeader {
    
        /** expiryTime field
         */    
	protected SIPDateOrDeltaSeconds expiryTime;

        /** default constructor
         */        
	public Expires() {
            super(EXPIRES);
        }

        /**
         * Return canonical form.
         * @return String
         */        
        public String encode() {
		return headerName + COLON + SP + expiryTime.encode() + NEWLINE;
	}
        
         /**
          * Gets date of DateHeader
          * @return null if date does not exist
          */ 
        public Date getDate() {
             if ( expiryTime.isSIPDate() ) {
                    Calendar c=( (SIPDate)expiryTime ).getJavaCal();
                    return c.getTime();
            }
            else return null;  
        }
        
        /** get the ExpiryTime field.
         * @return SIPDateOrDeltaSeconds
         */        
	public SIPDateOrDeltaSeconds getExpiryTime() {
            return expiryTime;
        }

         /*  Gets boolean value to indicate if expiry value of ExpiresHeader
         * is in date format
         */
        public boolean isDate() {
            if ( expiryTime==null) return false;
            else return expiryTime.isSIPDate();
        }
        
	/**
         * Set the expiryTime member
         * @param e SIPDateOrDeltaSeconds to set
         */
	public void setExpiryTime(SIPDateOrDeltaSeconds e) {
            expiryTime = e ;
        }
	
}
