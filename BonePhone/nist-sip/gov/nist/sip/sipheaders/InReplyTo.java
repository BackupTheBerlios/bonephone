/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                   
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

/**
* Class for the in-reply-to  SIP Header
*@see InReplyToList for a list of InReplyTo headers.
*
*<pre>
*  In-Reply-To  =  "In-Reply-To" ":" 1# callid 
*</pre>
*/
public class InReplyTo extends SIPHeader {

        CallIdentifier callId;

        /** Default constructor
         */        
	public InReplyTo() {
		super(IN_REPLY_TO);
	}

        /** constructor
         * @param cid CallIdentifier to set
         */        
	public InReplyTo(CallIdentifier cid) {
		super(IN_REPLY_TO);
		callId = cid;
	}

	/**
         * Generate canonical form of the header.
         * @return String
         */
	public String encode() {
		return headerName + COLON + SP + callId.encode() + NEWLINE;
	}
        
}
