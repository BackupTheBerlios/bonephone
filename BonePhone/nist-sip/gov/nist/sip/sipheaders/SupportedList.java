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
* A list of supported headers.
*@version 1.0
*@see Supported
*/

public class SupportedList extends SIPHeaderList {
    
        /** Default Constructor
         */    
	public SupportedList () {
		super( "Supported", 
			SIPHEADERS_PACKAGE + ".Supported", 
			SUPPORTED);
	}

}
