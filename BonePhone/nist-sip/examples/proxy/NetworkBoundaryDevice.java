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
import java.util.StringTokenizer;

/**
 * This is the parent class for FirewallDevice and NATDevice
 */

public abstract class NetworkBoundaryDevice {

    protected ServerMain stack;
    protected String deviceAddress;
    protected boolean active;
    protected String[] innerNetworkPrefix;
    
    /** 
     * Set the address of the device
     * @param deviceAddress The address of the device
     */

    public void setDeviceAddress(String deviceAddress) {
	this.deviceAddress = deviceAddress;
    }

    /**
     * Return the address of the device
     * @return the address as a String
     */

    public String getDeviceAddress() {
	return deviceAddress;
    }

    /**
     * Mark the device as active
     */

    public void activate() {
	active = true;
    }

    /**
     * Mark the device as unactive
     */
    
    public void desactivate() {
	active = false;
    }

    /** 
     * True if the device is active
     */
    
    public boolean isActive() {
	return active;
    }
    
    /** 
     * Set the list of inner network address prefixes
     * @param st A StringTokenizer on a String containing the
     * address prefixes
     * @return The list of String created
     */

    public String[] setInnerNetwork(StringTokenizer st) {
	innerNetworkPrefix = new String[st.countTokens()];
	for (int i = 0; st.hasMoreTokens(); i++) {
	    innerNetworkPrefix[i] = st.nextToken();
	}
	return innerNetworkPrefix;
    }

    /** 
     * Set the list of inner network address prefixes
     * @param innerNetworkPrefix A list of String
     */

    public void setInnerNetwork(String[] innerNetworkPrefix) {
	this.innerNetworkPrefix = innerNetworkPrefix;
    }

    /** True for a given address if it corresponds to any of
     * the inner network address prefixes.
     * @return boolean
     */

    public boolean isInInnerNetwork(String address) {
	if (innerNetworkPrefix != null) {
	    if (!stack.stackAddresses.containsKey(address)) {
		for ( int i = 0 ; i < innerNetworkPrefix.length; i++) {
		    if (address.startsWith(innerNetworkPrefix[i])) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    /**
     * Convenience function 
     * True if a request is transversing the device and
     * coming from outside
     * @param source The source address as a String
     * @param destination The destination address as a String
     * @return boolean
     */

    public boolean msgComingFromOutside(String source, String destination) {
	return msgTransversing(source, destination);
    }

    /**
     * Convenience function 
     * True if a request is transversing the device and
     * coming from inside
     * @param source The source address as a String
     * @param destination The destination address as a String
     * @return boolean
     */

    public boolean msgComingFromInside(String source, String destination) {
	return msgTransversing(destination, source);
    }

    /**
     * Convenience function 
     * True if the two peers are apart from the device
     * with peerA inside and peerB outside
     * To be used by msgComingFromInside() and msgComingFromOutside()
     * @param peerA An address as a String
     * @param peerB An address as a String
     * @return boolean
     */

    private boolean msgTransversing(String peerA, String peerB) {
	if (! isInInnerNetwork(peerA)) {
	    // peerA is outside
	    if (isInInnerNetwork(peerB)) {
		// peerB is inside
		return true;
	    }
	}
	return false;
    }

    /**
     * Print out device's data
     */

    public void print() {
	System.out.println("   Active = " + active);
	if (active) {
	    System.out.println("   Address = " + deviceAddress);
	    if (innerNetworkPrefix != null) {
		System.out.println("   innerNetworkPrefix = ");
		for (int i = 0 ; i < innerNetworkPrefix.length; i++) {
		    System.out.println("      " + innerNetworkPrefix[i]);
		}
	    }
	}
    }

}
