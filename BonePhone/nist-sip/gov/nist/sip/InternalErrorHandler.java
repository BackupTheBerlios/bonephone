/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../doc/uncopyright.html for conditions of use.                     *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip;
/**
*  Handle Internal error failures and print a stack trace (for debugging).
*/
public class InternalErrorHandler {
	/**
	* Handle an unexpected exception.
	*/
 	protected static void handleException ( Exception ex ) {
		try {
		    throw ex;
	        } catch ( Exception e) {
	 	    System.err.println("Unexpected exception : " + e);
		    System.err.println("Error message is " + ex.getMessage());
		    System.err.println("*************Stack Trace ************");
		    e.printStackTrace(System.err);
		    System.exit(0);
		}
	}
	/**
	* Handle an unexpected condition (and print the error code).
	*/

	protected static void handleException( String emsg ) {
		try {
		   throw new Exception(emsg);
		} catch ( Exception ex) {
		     ex.printStackTrace();
		     System.exit(0);
		}
	}
}
