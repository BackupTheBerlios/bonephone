/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.stack;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;

/**
* An interface for a genereic message processor for SIP Request messages.
* This is implemented by the application. The stack calls the message
* factory with a pointer to the parsed structure to create one of these
* and then calls processRequest on the newly created SIPServerRequest
* It is the applications responsibility to take care of what needs to be
* done to actually process the request.
*/
public interface SIPServerRequestInterface {
        /** Get the channel to where to send the response.
	*/
        public MessageChannel getResponseChannel();
	/**
         * Process the message.
         * @throws SIPServerException Exception that gets thrown by 
	 * this processor when an exception is encountered in the
         * message processing.
         */
	public void processRequest() throws SIPServerException ;
}
