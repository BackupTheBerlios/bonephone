/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;
import gov.nist.log.*;
/**
*   A class to do debug printfs
*/

class Debug {
	protected static  boolean debug;
	static { debug = LogWriter.needsLogging(); } 
	protected static void print (String s ) {
		if (debug) LogWriter.logMessage(s);
	}
	protected static void println (String s ) {
	    if (debug) LogWriter.logMessage(s);
	}
	protected static void printStackTrace(Exception ex) {
		if (debug) {
		    LogWriter.logException(ex);
		}
	}
	
	protected static void Abort(Exception e) {
	    System.out.println("Fatal error");
	     e.printStackTrace();
	     if (debug) {
		    LogWriter.logException(e);
	     }
	     System.exit(0);
	}

	protected static void Assert(boolean b) {
		if ( ! b) {
		   System.out.println("Assertion failure !");
		    new Exception().printStackTrace();
		    if (debug) LogWriter.logStackTrace();
		    System.exit(0);
		}
	}
}
