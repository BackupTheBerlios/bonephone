/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov)                                    *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package examples.proxy;
import gov.nist.sip.stack.security.*;
import gov.nist.sip.stack.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.net.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.io.*;

/**
 * This class implements the firewall transversal
 */

public class FirewallDevice extends NetworkBoundaryDevice {

    protected Hashtable currentCalls;
    protected Vector portTranslation;
    protected String portOpeningScript;
    protected String portClosingScript;

    /** Constructor
     * @param stack The SIP stack
     */

    public FirewallDevice(ServerMain stack) {
	this.stack = stack;
	active = false;
	currentCalls = new Hashtable();
    }

    /**
     * Set the port opening script to call when a session is opening
     * @param portOpeningScript The script as a String
     */

    public void setPortOpeningScript(String portOpeningScript) {
	this.portOpeningScript = portOpeningScript;
    }

    /**
     * Set the port closing script to call when a session is closing
     * @param portClosingScript The script as a String
     */

    public void setPortClosingScript(String portClosingScript) {
	this.portClosingScript = portClosingScript;
    }


    /** 
     * Set the port translation list.
     * Used to direct all the media streaming through a specific
     * set of ports.
     * @param st A StringTokenizer on a String containing a list of ports
     */

    public void setPortTranslation(StringTokenizer st) {
	portTranslation = new Vector();
	for (int i = 0 ; st.hasMoreTokens(); i++) {
	    portTranslation.addElement(new Integer(st.nextToken()));
	}
    }

    /**
     * Function called when an opening sesion message is coming
     * @param source The source of the message as a String
     * @param destination The destination of the message as a String
     * @param message The SIPMessage itself
     * @param callRecord The call data
     */

    public void sessionOpening(String source, 
			       String destination,
			       SIPMessage message,
			       CallRecord callRecord) {

	boolean isFromInside = msgComingFromInside(source, destination);
	boolean isFromOutside = msgComingFromOutside(source, destination);
	String callId = message.getCallIdHeader().getCallIdentifer().encode();
	
	if (isActive()) {
	    if (isFromInside || isFromOutside) {
		// there is a firewall transversal
		
		if (callRecord.isFull()) {
		    // We have all the peers data
		    if (! currentCalls.containsKey(callId)) {
			// Open the firewall
			sendCommands(portOpeningScript, callRecord);
			currentCalls.put(callId, callRecord);
		    }
		}
		if (portTranslation != null) {
		    stack.setMediaPortList(message, portTranslation);
		}
	    }
	}
    }

    /**
     * Function called when an closing sesion message is coming
     * @param callId The call Id
     * @param callRecord The call data
     */

    public void sessionClosing(String callId,
			       CallRecord callRecord) {
	if (isActive()) {
	    if (currentCalls.containsKey(callId)) {
		// Close the firewall
		sendCommands(portClosingScript, callRecord);
		currentCalls.remove(callId);
	    }
	}
    }

    /** 
     * Send commands to the firewall
     * @param script The firewall control script to be executed
     * @param callRecord The data for the call
     */

    public void sendCommands(String script, CallRecord callRecord) {
	String[] commandLine = new String[callRecord.getNumberOfPeers() + 1];

	// get the script to be runned
	commandLine[0] = script;

	int i = 1;
	for (Enumeration peerRecords = callRecord.elements();
	     peerRecords.hasMoreElements(); i++) {

	    PeerRecord peerRecord = (PeerRecord) peerRecords.nextElement();

	    Vector list = peerRecord.getMediaConnectionAddressList();
	    // BUG BUG, here we take only the first element of the list
	    commandLine[i] = (String) list.firstElement();
	    commandLine[i] += ":";
	    list = peerRecord.getMediaPortList();
	    // BUG BUG, here we take only the first element of the list
	    commandLine[i] += String.valueOf(list.firstElement());

	}

	// run the script
	runScript(commandLine);
    }

    /** 
     * Convenience function for running a script
     * @param commandLine A command to be executed given as list of String 
     */

    private void runScript(String[] commandLine) {
	try {
	    Process portOpeningProcess = Runtime.getRuntime().
		exec(commandLine);
	    try {
		BufferedReader inStream = new BufferedReader
		    (new InputStreamReader(portOpeningProcess.
					   getInputStream())); 
		String line = inStream.readLine();
		while (line != null) {
		    ServerLog.traceMsg(ServerLog.
				       TRACE_DEBUG, "Output from " + 
				       commandLine[0] + ": " +
				       line);
		    line = inStream.readLine();
		}
	    } catch(IOException e) {
		ServerLog.traceMsg
		    (ServerLog.TRACE_DEBUG,"WARNING: Cannot read output from "
		     + commandLine[0]);
	    }
	} catch(IOException e) {
	    ServerLog.traceMsg(ServerLog.TRACE_DEBUG, 
			       "WARNING: execution of "
			       + commandLine[0]+
			       " failed");
	}
    }

    /**
     * Print out device's data
     */

    public void print() {
	super.print();
	if (active) {
	    System.out.println("   Port opening script = " + 
			       portOpeningScript);
	    System.out.println("   Port closing script = " + 
				       portClosingScript);
	    if (portTranslation != null) {
		System.out.println("   Port translation = ");
		for (int i = 0 ; i < portTranslation.size(); i++) {
		    System.out.println("      " + portTranslation.
				       elementAt(i));
		}
	    }
	}
    }

}
