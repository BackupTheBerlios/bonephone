/**
* An interface that represents the thread that will read incoming messages
* and queue them up for processing.
*/

package gov.nist.sip.stack;
import  java.io.IOException;

/** This is the Stack abstraction for the active object that waits
 * for messages to appear on the wire and processes these messages
 * by calling the MessageFactory interface to create a ServerRequest
 * or ServerResponse object. The stack will instantiate a message
 * processor for tcp message proessing (if specified) and a message
 * processor for UDP message processing.
 */
public interface MessageProcessor  extends Runnable {
	
	/**
         * Get the transport string.
         * @return A string that indicates the transport. 
         * (i.e. "tcp" or "udp") 
          */
	public String getTransport();

	/**
	* Get the port identifier.
	*@return the port for this message processor.
	*/
	public int getPort();

	/**
	* Get the SIP Stack.
	*@return the sip stack.
	*/
	public SIPStack getSIPStack();

	/** Start our thread.
	*/
	public void start();



}
