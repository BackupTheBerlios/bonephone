/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: ServerInternalError.java
 * created 04-Sep-00 3:13:56 AM by mranga
 */

package gov.nist.sip.stack;
import  gov.nist.sip.*;

 /**
 *  Implements a conveniance class for flagging server internal errors.
 */
public class ServerInternalError extends InternalErrorHandler
{
    /**
    * Handle an internal error - prints a stack trace on the standard error
    */
    public static void handleException( String exMessage) {
        try {
            throw new Exception(exMessage);
        } catch ( Exception ex) {
          ex.printStackTrace();
          System.exit(0);
        }
    }
    public static void handleException( Exception ex) {
	 // Cannot log exception here !!
        ex.printStackTrace();
        System.exit(0);
    }

}
