/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov) 			               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package examples.proxy;
import gov.nist.sip.stack.*;
import gov.nist.sip.stack.security.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.msgparser.*;
import java.util.Vector;

/**
 * Store call data for a peer
 */

public class PeerRecord {

    private Vector mediaPortList, mediaConnectionAddressList;
    private boolean hasLeft;

    /**
     * Constructor
     */

    public PeerRecord() {
    }

    /**
     * Store the list of media port used by the peer
     * @param list A Vector of port numbers
     */

    public void setMediaPortList(Vector list) {
	mediaPortList = list;
    }

    /**
     * Store the list of connection addresses used by the peer
     * @param list A Vector of addresses
     */

    public void setMediaConnectionAddressList(Vector list) {
	mediaConnectionAddressList = list;
    }

    /** 
     * Mark the peer as leaving
     */

    public void leavingCall() {
	hasLeft = true;
    }
    
    /**
     * Return the list of media ports used by the peer
     * @return A Vector of port numbers
     */
    
    public Vector getMediaPortList() {
	return mediaPortList;
    }
    
    /**
     * Return the list of connection addresses used by the peer
     * @return A Vector of addresses
     */
    
    public Vector getMediaConnectionAddressList() {
	return mediaConnectionAddressList;
    }

    /**
     * True if the peer has left the call
     * @return boolean
     */
    
    public boolean hasLeft() {
	return hasLeft;
    }

    /**
     * True if the data of the peer are already stored
     * @return boolean
     */

    public boolean hasData() {
	return (mediaPortList != null && mediaConnectionAddressList != null);
    }
}
