/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
*  Handle Internal error failures and print a stack trace (for debugging).
*/
class InternalError extends InternalErrorHandler {
    
	/**
         * Handle an unexpected exception.
         * @param ex Exception to set
         */
	protected static void handleException ( Exception ex ) {
		InternalErrorHandler.handleException(ex);
	}
        
	/**
         * Handle an unexpected condition (and print the error code).
         * @param emsg String to set
         */
	protected static void handleException( String emsg ) {
		InternalErrorHandler.handleException(emsg);
	}
        
}
