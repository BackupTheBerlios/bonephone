/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                   
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* Allow SIPHeader.
*
* <pre>
*
*6.10 Allow
*
*   The Allow header field lists the set of methods supported by the
*   resource identified by the Request-URI. The purpose of this field is
*   strictly to inform the recipient of valid methods associated with the
*   resource. An Allow header field MUST be present in a 405 (Method Not
*   Allowed) response, SHOULD be present in an OPTIONS response SHOULD be
*   present in the 200 (OK) response to the initial INVITE for a call and
*   MAY be present in final responses for other methods.  All methods,
*   including ACK and CANCEL, understood by the UAS are included.
*
*   The Allow header field MAY also be included in requests, to indicate
*   the requestor's capabilities for this Call-ID.
*
*
*        Supplying an Allow header in responses to methods other
*        than OPTIONS cuts down on the number of messages needed.
*
*
*
*        Allow  =  "Allow" ":" 1#Method
*
*
*Handley/Schulzrinne/Schooler/Rosenberg                       [Page 49]
*
*Internet Draft                    SIP                  November 24, 2000
*
*
*</pre>
*/
public class Allow extends SIPHeader {
    
        /** method field
         */    
	protected String method;

        /** default constructor
         */        
        public Allow() { 
            super(ALLOW);
        }
	
        /** constructor
         * @param m String to set
         */        
        public Allow(String m ) { 
            super(ALLOW);
            method = m;
        }

        /** get the method field
         * @return String
         */        
	public	 String getMethod() { 
            return method ;
        } 

	/**
         * Set the method member
         * @param m String to set
         */
	public	 void setMethod(String m) {
            method = m ;
        } 
		
        /** Return canonical header.
         * @return String
         */        
	public String encode() {
		return headerName + COLON + SP + method + NEWLINE;
	}
        
}
