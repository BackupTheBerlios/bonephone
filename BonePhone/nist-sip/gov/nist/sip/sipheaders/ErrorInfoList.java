/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.net.*;

/**
* Error Info sip header.
*@since v1.0
*@see ErrorInfoList
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
public class ErrorInfoList extends SIPHeaderList {
    
        /** Default constructor.
         */    
	public ErrorInfoList() {
		super("ErrorInfo",  
			SIPHEADERS_PACKAGE+".ErrorInfo", ERROR_INFO);
	}

        /** add an ErrorInfo to the list.
         * @param einfo ErrorInfo to set.
         */        
	public void add(ErrorInfo einfo) {
		super.add(einfo);
	}

}

