/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: AddressList.java
 * created 20-Sep-00 4:45:55 PM by mranga
 */


package examples.proxy;
import  java.util.LinkedList;
import  gov.nist.sip.*;
import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.sdpfields.*;
import  gov.nist.sip.msgparser.*;

/**
*  This is a list of addresses where the request has been forwarded. 
*  Each element is time-stamped with the time at which it was sent out. 
*  A scanning thread 
*  periodically scans this list and removes records that have timed out.
*/

public class AddressList
{
	SIPRequest request;
	// Linked list of addresses where the request was forwaded and the time
	// at which it was forwarded there.
	LinkedList forwardedList;
	
}
