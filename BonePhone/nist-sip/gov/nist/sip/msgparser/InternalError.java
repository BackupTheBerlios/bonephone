/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
/**
*  Handle Internal error failures and print a stack trace (for debugging).
*/
class InternalError {
	/**
	* Handle an unexpected exception.
	*/
	static void handleException ( Exception ex ) {
	    System.err.println("Unexpected exception : ");
	    System.err.println("Error message is " + ex.getMessage());
	    System.err.println("*************Stack Trace ************");
	    new Exception().printStackTrace();
	    System.exit(0);
	}

	/**
	* Handle an unexpected condition (and print the error code).
	*/

	static void handleException( String emsg ) {
	      System.out.println("Fatal error:");
	      new Exception(emsg).printStackTrace();
	      System.exit(0);
	}
}
