/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) , added JAVADOC                *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
*  Content Encoding SIP header List. Keeps a list of ContentEncoding headers
*/
public final class ContentEncodingList extends SIPHeaderList {
    
        /** Default constructor.
         */    
	public ContentEncodingList () {
		super("ContentEncodingList", 
			SIPHEADERS_PACKAGE + ".ContentEncoding",
			CONTENT_ENCODING);
	}
        
}
