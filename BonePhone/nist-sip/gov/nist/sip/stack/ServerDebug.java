/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: ServerDebug.java
 * created 05-Jan-01 5:41:46 PM by mranga
 */


package gov.nist.sip.stack;


public class ServerDebug
{
	public static void println(String s ) { 
	     if (ServerLog.needsLogging(ServerLog.TRACE_DEBUG)) 
			System.out.println(s); 
	}
		
	/** If condition is not true then print stack trace and die 
	* (if the trace level is high enough).
	*/

	public static void Assert(boolean condition, Exception ex ) 
		throws IllegalArgumentException {
	     if (condition) return;
	     if (ServerLog.needsLogging(ServerLog.TRACE_DEBUG)) {
		 ex.printStackTrace();
		 System.exit(0);
	     } else throw new IllegalArgumentException( ex.getMessage());

	}

	public static void Assert(boolean condition, String msg) 
	  throws IllegalArgumentException {
	     if (condition) return;
	     IllegalArgumentException ex = new IllegalArgumentException(msg);
	     if (ServerLog.needsLogging(ServerLog.TRACE_DEBUG)) {
		 ex.printStackTrace();
		 System.exit(0);
	     } else throw ex;
	}

}
