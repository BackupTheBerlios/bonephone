/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modifications by: O. Deruelle (deruelle@antd.nist.gov added JAVADOC)         *                                                                                   
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
*   List of Unsupported headers.
*/
public class UnsupportedList extends SIPHeaderList {
    
    /** Default Constructor
     */
    public UnsupportedList () {
        super( "Unsupported",
        SIPHEADERS_PACKAGE + ".Unsupported",
        UNSUPPORTED);
	}
        
}

