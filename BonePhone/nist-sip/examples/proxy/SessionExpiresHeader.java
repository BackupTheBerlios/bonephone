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
 * Implementation of the Session-Expires header, used by the
 * proxy to close firewall/NAT even when a session is not properly
 * closed by clients.
 * This is an usage example of the NIST SIP Parser extension mechanism
 */

public class SessionExpiresHeader extends SIPHeader {

    RefreshDate refreshDate;
    
    /** 
     * Constructor
     * @param hname String to set
     */

    protected SessionExpiresHeader(String hname) {
	super(hname);
    }
    
    /**
     * Set the refresh date
     * @param refreshValue an Integer to be added to the current date
     */

    protected void setRefreshDate(Integer refreshValue) {
	this.refreshDate = new RefreshDate(refreshValue);
    }

     /** Set the refresh date 
     * @param sipDate a SIPDate corresponding to the refresh date
     */
     protected void setRefreshDate(SIPDate sipDate) {
	refreshDate = new RefreshDate(sipDate);
     }


    /**
     * Return the refreshDate
     * @return the refresh date of the session
     */

    protected RefreshDate getRefreshDate() {
	return refreshDate;
    }

    /** Encode this into Canonical form.
    */
    public String encode() {
		return headerName + COLON + refreshDate.encode() + NEWLINE;
    }

}
