/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import  gov.nist.sip.net.*;

/**
* A list of also headers.
* @since 1.0
*/
public final class AlsoList extends SIPHeaderList {

        /** default constructor
         */    
	public AlsoList() {
		super("AlsoList",  SIPHEADERS_PACKAGE+".Also", ALSO);
	}
	
}
