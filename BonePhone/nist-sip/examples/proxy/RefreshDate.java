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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

/**
 * This implements the refresh values for Session-Expires headers
 * A refresh value can either of 'deltaseconds' type or of 'SIPDate'type
 */

class RefreshDate {

    private long expiryTime;
    
    /**
     * Constructor
     * @param value A SIPDate corresponding to the refresh date
     */

    protected RefreshDate(SIPDate date) {
	this.expiryTime = date.getJavaCal().getTime().getTime();
    }

    /**
     * Constructor
     * @param value An Integer added to the current date
     */

    protected RefreshDate(Integer value) {
	if (value == null) throw new IllegalArgumentException("Null arg");
	expiryTime  = new Date().getTime() + value.intValue() * 1000;

    }

    /** 
     * True if the refresh date is after the current date
     * @return boolean
     */
    
    protected boolean isOver() {
	return new Date().getTime() > expiryTime;

    }

    /**
     * Get the String value
     * @return String
     */
    
    public String toString() {
	return new Long(expiryTime).toString();
    }

    protected String encode() {
	return new Date(expiryTime).toString();
    }
	


}
