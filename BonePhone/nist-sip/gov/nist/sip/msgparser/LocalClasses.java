/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
/**
* local class that is used by sip_msgparser.g
* @version 1.0
* @author M. Ranganathan
*/

class MyDate {
	String	 inputText; 
	int day;
	String  month;
	int year;
}
/**
*  Our local representation of time (never gets referenced outside this package)
*/
class MyTime {
	String	 inputText; 
	int hour;
	int minute;
	int second;
}
