/***************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).    *
* See ../../../../doc/uncopyright.html for conditions of use.              *
* Author: M. Ranganathan (mranga@nist.gov)                                 *
* Questions/Comments: nist-sip-dev@antd.nist.gov                           *
***************************************************************************/
/******************************************************
 * File: ServerLog.java
 * created 26-Sep-00 2:41:19 PM by mranga
 */


package gov.nist.log;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

/**
*  Log System Errors.
*/

public class LogWriter
{
	
/** Dont trace
 */    
	public static int TRACE_NONE = 0;
/** Trace initialization code
 */        
	public static int TRACE_INITIALIZATION = 1;
/** Trace authentication sequences
 */        
	public static int TRACE_AUTHENTICATION = 2;
/** Trace message processing
 */        
	public static int TRACE_MESSAGES = 16;
/** Trace exception processing
 */        
	public static int TRACE_EXCEPTION = 17;
/** Debug trace level (all tracing enabled).
 */        
	public static int TRACE_DEBUG = 32;
/** Name of the log file in which the trace is written out
 * (default is /tmp/sipserverlog.txt)
 */        
	public static String   logFileName = "logfile.txt";
/** Print writer that is used to write out the log file.
 */        
	public static PrintWriter printWriter;
/** print stream for writing out trace
 */        
	public static PrintStream traceWriter = System.out;

	/**
	*  Debugging trace stream.
	*/
	private    static  PrintStream trace = System.out;
	/** trace level
	 */        
	 // protected     static int traceLevel = TRACE_DEBUG;
	protected     static int traceLevel = TRACE_NONE;

        
	public static boolean needsLogging() {
		 return false;
	//	 return needsLogging(traceLevel);
	}

        /**
         *Check to see if logging is enabled at a level (avoids
         * unecessary message formatting.
         *@param logLevel level at which to check.
         */
        public static boolean needsLogging(int logLevel) {
            return traceLevel >= logLevel;
        }

	/** log a stack trace..
	*/
	public static void logStackTrace(int level) {
		if (needsLogging(level)) {
		   checkLogFile();
		   new Exception().printStackTrace(printWriter);
		}
	}

	/** log a stack trace..
	*/
	public static void logStackTrace() {
		if (needsLogging()) {
		   checkLogFile();
		   new Exception().printStackTrace(printWriter);
		}
	}


	/** Log an excption.
	*/
	public static void logException(Exception ex) {
	     checkLogFile();
	     ex.printStackTrace(printWriter);
	}


	/** Set the log file name 
	*@param name is the name of the log file to set. 
	*/
	public static void setLogFileName(String name) {
		logFileName = name;
	}
	
	private static void checkLogFile() {
		
		if (printWriter != null) return;
		if (logFileName == null) return;
		try {
			File  logFile = new File(logFileName);
			if (! logFile.exists()) {
				logFile.createNewFile();
				printWriter = null;
			}
			// Append buffer to the end of the file.
			if (printWriter == null) {
	   			FileWriter fw = new FileWriter(logFileName,true);
				printWriter = new PrintWriter(fw,true);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
	}

	/** Log a message into the log file.
         * @param message message to log into the log file.
         */
	public static void logMessage(int level, String message) {
		if (! needsLogging(level)) return;
		checkLogFile();
		String tname = Thread.currentThread().getName();
		if (printWriter == null) {
			System.out.println(tname + ":" + message);
		} else {
		   printWriter.println(tname + ": " + message);	
		}
	}
	/** Log a message into the log file.
         * @param message message to log into the log file.
         */
	public static void logMessage(String message) {
		checkLogFile();
		String tname = Thread.currentThread().getName();
		if (printWriter == null) {
			System.out.println(tname + ":" + message);
		} else {
		   printWriter.println(tname + ": " + message);	
		}
	}
	
    
	
        /** Set the trace level for the stack.
         */
        public static void setTraceLevel(int level) {
            traceLevel = level;
        }
        
        /** Get the trace level for the stack.
         */
        public static int getTraceLevel() { return traceLevel; }
	

}
