/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) ,added JAVADOC                 * 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/


package gov.nist.sip.sipheaders;

/** SIP Exception class
 */
public class SIPParameterNotSetException extends Exception {
    
	/**
         * Thrown when you try to access a memeber that has not been set
         * or cannot be retrieved because of a class mismatch.
         * @param exceptionString String to set
         */
	public SIPParameterNotSetException(String exceptionString) {
		super(exceptionString);
	}

}
