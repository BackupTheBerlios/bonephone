/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: ServerLog.java
 * created 26-Sep-00 2:41:19 PM by mranga
 */


package gov.nist.sip.stack;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

/**
*  Log System Errors and messages.
*/

public class ServerLog
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
	 * (default is null)
 	*/        
	private static String   logFileName;
	/** Name of the log directory in which the messages are written out
 	*/        
	private static MessageLogTableImpl messageLogTable;
	/** Print writer that is used to write out the log file.
 	*/        
	protected static PrintWriter printWriter;
	/** print stream for writing out trace
 	*/        
	protected static PrintStream traceWriter = System.out;

	/**
	*  Debugging trace stream.
	*/
	private    static  PrintStream trace = System.out;
	/** default trace level
 	*/        
	protected     static int traceLevel = TRACE_MESSAGES;
        
	public static void checkLogFile()  {
		if (logFileName == null) return;
		try {
			File  logFile = new File(logFileName);
			if (! logFile.exists()) {
				logFile.createNewFile();
				printWriter = null;
			}
			// Append buffer to the end of the file.
			if (printWriter == null) {
	   			FileWriter fw = 
					new FileWriter(logFileName,true);
				printWriter = new PrintWriter(fw,true);
			}
		} catch (IOException ex) {
			ServerInternalError.handleException(ex);
		}
	}

        /**
         *Check to see if logging is enabled at a level (avoids
         * unecessary message formatting.
         *@param logLevel level at which to check.
         */
        public static boolean needsLogging(int logLevel) {
            return traceLevel >= logLevel;
        }

	public static boolean needsLogging() {
	  return false;
	   // return true;
	}

	/** Set the log file name 
	*@param name is the name of the log file to set. 
	*/
	public static void setLogFileName(String name) {
		logFileName = name;
	}

	/** return the name of the log file.
	*/
	public static String getLogFileName() { return logFileName; }
	

	/** Log a message into the log file.
         * @param message message to log into the log file.
         */
	public static void logMessage( String message) {
		String tname = Thread.currentThread().getName();
		checkLogFile();
		if (printWriter == null) {
		   System.out.println(tname + ":" + message);
		} else {
		   printWriter.println(tname + ": " + message);	
		}
	}
	
	/** Log a message into the log directory.
	 * @param message a SIPMessage to log
         * @param from from header of the message to log into the log directory
         * @param to to header of the message to log into the log directory
         * @param cseq cseq header of the message to log into the log directory
         * @param sender is the server the sender
         * @param callId CallId of the message to log into the log directory.
         */
        public synchronized static void logMessage(String message, 
						   String from, String to, 
						   String cseq, 
						   boolean sender, 
						   String callId,
						   String firstLine) {
	    if (needsLogging()) {
	        logMessage("----------------------------------------");
	        logMessage("from = " + from + "\n to = " + to +
			"\n message = " + message);
	        logMessage("----------------------------------------");
	    }

	    if (messageLogTable != null) {
		MessageLogList logList;
		String time = GregorianCalendar.getInstance().
		    getTime().toString(); 
		MessageLog log = new MessageLog(message, from, to, time, 
						sender, cseq, firstLine);
		logList = messageLogTable.get(callId);
		if (logList == null) {
		    logList = new MessageLogList(callId);
		    messageLogTable.add(callId, logList);
		}
		logList.addLast(log);
	    }
	}
    
	/** Log a message into the log directory.
	 * @param message a SIPMessage to log
         * @param from from header of the message to log into the log directory
         * @param to to header of the message to log into the log directory
         * @param sender is the server the sender
         */
        public static void logMessage(SIPMessage message, String from,
				      String to, boolean sender) {
	    checkLogFile();
	    String cseq = message.getCSeqHeader().encodeBody();
	    CallID cid = message.getCallIdHeader();
	    String callId = null;
	    if (cid != null) callId = message.getCallIdHeader().getCallID();
	    String firstLine = message.getMessageAsEncodedStrings(0).trim();
	    String inputText = message.encode();
	    logMessage( inputText , from, to, cseq, sender, 
		       callId, firstLine);
	}


	/** Log a message into the log file.
         * @param msgLevel Logging level for this message.
         * @param tracemsg message to write out.
         */
	public static void traceMsg (int msgLevel, String tracemsg) {
		if (needsLogging(msgLevel))   {
			traceWriter.println(tracemsg);
			logMessage(tracemsg);
		}	
	}

	/** Log an exception stack trace.
        * @param ex Exception to log into the log file
        */

	public static void logException(Exception ex) {
		if (traceLevel >= TRACE_EXCEPTION) {
		   checkLogFile();
		   ex.printStackTrace();
		   if (printWriter != null) ex.printStackTrace(printWriter);		  
		}
	}
	
       /** Initialize the table for RMI access to the log file.
	*/
	public static void initMessageLogTable(
				String stackAddress,
				int rmiRegistryPort, 
				int rmiPort,
				int traceLifeTime ) 
	throws RemoteException {
	   messageLogTable = new MessageLogTableImpl(rmiPort);
           messageLogTable.init(stackAddress, rmiRegistryPort,
                                               traceLifeTime);
	}

	/** Return the message log table.
	*/
	public static MessageLogTableImpl getMessageLogTable() {
		return messageLogTable;
	}

       /** print a line into the trace stream.
       * @param s String to print out.
       */        
	public static void println( String s) {
		if (traceLevel == TRACE_DEBUG) System.out.println(s);
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
