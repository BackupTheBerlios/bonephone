/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: UserLocation.java
 * created 21-Sep-00 11:32:54 AM by mranga
 */


package examples.proxy;
import  gov.nist.sip.*;
import  gov.nist.sip.net.*;
import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.msgparser.*;

/**
 *  Store a user location and its transport protocol
 */ 

public class UserLocation {
    protected HostPort hostPort; // Location where the user resides.
    protected String   transport; 
    // Transport to use to talk to the user. (including multicast);
}
