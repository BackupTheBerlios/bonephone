/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov) 			               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package examples.proxy;
import gov.nist.sip.stack.*;
import java.rmi.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * This is an example of message log access via RMI
 * Takes a name of the machine where the proxy runs
 * (and optionnaly a port number)
 * Print the traces out on the screen and in files in /tmp
 * named after their call Id.
 * Usage example:
 * java -Djava.security.policy=test.policy examples.proxy.TraceBrowser caribou
 */
public class TraceBrowser {

    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            String name = "//" + args[0];
	    if (args.length > 1) {
		name += ":" + args[1];
	    }
	    name += "/gov.nist.sip.stack.MessageLogTableImpl";
            MessageLogTable table = (MessageLogTable) Naming.lookup(name);
	    StringTokenizer st = new StringTokenizer(table.keys());
	    while (st.hasMoreTokens()) {
		String key = (String) st.nextElement();
		System.out.println("Trace for " + key);
		String trace = table.flush(key);
		System.out.println(trace);
		String fileName = "/tmp/" + key + ".xml";
		File logFile = new File(fileName);
		try {
		    FileWriter fw = new FileWriter(fileName, true);
		    PrintWriter pw = new PrintWriter(fw, true);
		    pw.println(trace);
		} catch (IOException ex) {
		    ServerInternalError.handleException(ex);
		}
	    }
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
	    e.printStackTrace();
	}
    }   

}
