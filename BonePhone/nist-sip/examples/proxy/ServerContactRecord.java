/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* - Added JAVADOC                                                              *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: ServerContactRecord.java
 * created 04-Sep-00 4:32:59 AM by mranga
 */

package examples.proxy;
import java.util.Date;
import java.util.Calendar;
import java.net.Socket;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import gov.nist.sip.*;
import gov.nist.sip.stack.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;


/**
* Server side registration record. This records the time in the record so that
* it can subsequently be scanned and removed if it expires.
* We also record the transport parameter from the sip request line here 
*/

public class ServerContactRecord  implements SIPKeywords {
    ServerMain 		sipStack;
    protected long 		timeReceived;
    protected long 		expiryTimeMilis; // Time to expiry
    protected String    	action;
    protected Contact  	contact; // Contains info on transport host etc.
    protected boolean   	requestOutstanding; 
    // Do we expect a reply back from him
    protected long	timeForwarded;  // Time that we forwarded the  request 
    protected long  expectedTimeOfReply; // Time at which we expect a reply.
    
    /**
     * Retrieve the contact record.
     * @return Contact 
     */

    protected Contact getContact() { 
	return contact; 
    }
	
    /**
     * Constructor
     * Set up a Contact Record from the given Contact Header
     * @param ct A Contact Header
     * @param the server stack
     */

    protected ServerContactRecord(Contact ct, ServerMain server ) {
	requestOutstanding = false;
	contact = ct;
	timeReceived  = ( new java.util.Date()).getTime();
	sipStack  = server;
	int maxExpiry = server.registrationTimeout;
	SIPDateOrDeltaSeconds exp = 
	    (SIPDateOrDeltaSeconds) contact.getExpires();
	if (exp != null) {
	    if (exp.isSIPDate()){
		// This expiration record is a date record
		SIPDate sipDate = (SIPDate) exp;
		Calendar cal = sipDate.getJavaCal();
		// Get the epoch time offset
		Date time  =  cal.getTime();
		long expiryTimeMilis = time.getTime(); 
		// Expiry time in milis
	    }  else {
		// Expiry time is specified in delta seconds.
		DeltaSeconds delta = (DeltaSeconds) exp;
		int deltaSeconds = (int) delta.getDeltaSeconds();
		expiryTimeMilis = timeReceived + deltaSeconds*1000;
	    } 
	} else {
	    // No expiration time has been specified set to the default
	    // expiry time.
	    expiryTimeMilis = timeReceived + 
		sipStack.registrationTimeout * 1000;
	}
	// If the request expiry time is too much, then set it to 
	// our max value. We will return the contact list with the
	// reply so our client is aware of what we set his expiry 
	// times to be.
	if (expiryTimeMilis > timeReceived + 
	    sipStack.maxRegistrationTimeout*1000 ){
	    expiryTimeMilis = timeReceived + 
		sipStack.maxRegistrationTimeout*1000;
	}
	// If this contact record does not have an action, 
	// assign it a default one.
	if (contact.getAction() == null) {
	    action = PROXY;
	} else {
	    action = contact.getAction();
	}
    }
    
}
