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

/**
 * This class implements the NAT transversal
 */

public class NATDevice extends NetworkBoundaryDevice {

    /** Constructor
     * @param stack The SIP stack
     */

    public NATDevice(ServerMain stack) {
	this.stack = stack;
	active = false;
    }

    /** 
     * Do whatever is needed when a message is transversing NAT
     * @param message Message to modify
     */

    protected void transversal(String source,
			       String destination,
			       SIPMessage message) {
	if (active && msgComingFromInside(source, destination)) {
	    // Change the address in the SDP Origin field
	    Host originAddress = stack.getOriginAddress(message);
	    if (originAddress != null) {
		String rawIpOriginAddress = originAddress.getIpAddress();
		if (isInInnerNetwork(rawIpOriginAddress)) {
		    originAddress.setAddress(deviceAddress);
		}
	    }
	    
	    // Change the address in the SDP Connection field
	    Host connectionAddress = stack.getConnectionAddress(message);
	    if (connectionAddress != null) {
		String rawIpConnectionAddress = connectionAddress.
		    getIpAddress();
		if (isInInnerNetwork(rawIpConnectionAddress)) {
		    connectionAddress.setAddress(deviceAddress);
		}
	    }
	}
    }

}

