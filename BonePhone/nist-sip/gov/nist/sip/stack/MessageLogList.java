/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov)                                    *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package gov.nist.sip.stack;

import java.util.*;
import java.io.*;

/**
 * This class stores a list message
 * Used to log messages.
 */

public class MessageLogList extends LinkedList {
    PrintWriter printWriter;
    String logFileName;
    private static boolean logToFile = false;

    private void checkLogFile() {
		
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
	   			FileWriter fw = 
					new FileWriter(logFileName,true);
				printWriter = new PrintWriter(fw,true);
			}
		} catch (IOException ex) {
			ServerInternalError.handleException(ex);
		}
	}

    public MessageLogList(String callId) {
	super();
	if (logToFile) {
           logFileName = "trace"+callId+".xml";
  	   checkLogFile();
	   printWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	   printWriter.println("<messages>");
	}
    }

    public void addLast(Object obj) {
	super.addLast(obj);
/**
        MessageLog messageLog = (MessageLog) obj;
        ServerLog.logException(new Exception());
        ServerLog.logMessage(messageLog.flush());
**/
    }
	

    /**
     * Get an XML String for this message list
     */
 
    public String flush() {
	String log;

	// Header
	log = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	log += "<messages>\n";

	// Body
	ListIterator li = listIterator(0);
	while (li.hasNext()) {
	    log += ((MessageLog)li.next()).flush();
	}

	// Footer
	log += "</messages>\n";
	if (logToFile) {
	    printWriter.println("</messages>");
	}
	return log;
    }

}
