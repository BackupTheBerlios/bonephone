
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.stack;
import gov.nist.sip.msgparser.*;

/**
* An interface for a genereic message processor for SIP Response messages.
* This is implemented by the application. The stack calls the message
* factory with a pointer to the parsed structure to create one of these
* and then calls processResponse on the newly created SIPServerResponse
* It is the applications responsibility to take care of what needs to be
* done to actually process the response.
*/
public interface SIPServerResponseInterface {
	/**
         * Process the Response.
         * @throws SIPServerException Exception that gets thrown by 
	 * this processor when an exception is encountered in the
         * message processing.
         */
         public void processResponse() throws SIPServerException ;

	/** Get the Channel for the sender. 
 	*@return the MessageChannel through which you can send a
	* new request to the responder.
	*/
	public MessageChannel getRequestChannel();
}
