/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                   
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

/**
* Corresponds to the In-Reply-To SIP header. Keeps a list of CallIdentifiers
* @see CallIdentifier
*/
public final class InReplyToList extends SIPHeaderList {
    
        /** Default constructor
         */    
	public InReplyToList() {
		super("InReplyTo", SIPHEADERS_PACKAGE + ".InReplyTo", 
			IN_REPLY_TO);
	}
        
}
