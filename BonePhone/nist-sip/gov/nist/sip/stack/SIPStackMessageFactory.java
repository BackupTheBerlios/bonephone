/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.stack;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;

/**
* An interface for generating new requests and responses. This is implemented
* by the application and called by the stack for processing requests
* and responses. When a Request comes in off the wire, the stack calls
* newSIPServerRequest which is then responsible for processing the request.
* When a response comes off the wire, the stack calls newSIPServerResponse
* to process the response. 
*/

public interface SIPStackMessageFactory {
	/**
	* Make a new SIPServerResponse given a SIPRequest and a message 
	* channel.
	*/
	public SIPServerRequestInterface
	newSIPServerRequest(SIPRequest sipRequest, MessageChannel msgChan);

	/**
	* Generate a new server response for the stack.
	*/
	public SIPServerResponseInterface
         newSIPServerResponse 
		(SIPResponse sipResponse, MessageChannel msgChan);
}
