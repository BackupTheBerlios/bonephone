package gov.nist.sip.stack;
import java.net.Socket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.DatagramPacket;
import java.io.IOException;

/**
* Sit in a loop and handle incoming udp datagram messages. For each Datagram
* packet, a new UDPMessageChannel is created. (Each UDP message is processed
* in its own thread).
*/
public class UDPMessageProcessor  implements MessageProcessor {
/** Max datagram size.
 */    
	protected  static final int MAX_DATAGRAM_SIZE = 8 * 1024;
/** Our stack (that created us).
 */        
	protected SIPStack sipStack;
/** Channel notifier (not very useful for UDP).
 */        
	protected ChannelNotifier notifier;

	/**
         * Constructor.
         * @param srv pointer to the stack.
         * @param notify channel notifier.
         */
	protected  UDPMessageProcessor( SIPStack srv, 
		ChannelNotifier notify ) {
		sipStack = srv;
		notifier = notify;
		
	}

	/**
	 * Start our processor thread.
	 */
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}


	/**
	* Thread main routine.
	*/
	public  void run() {
		try {
			// Create a new datagram socket.
			DatagramSocket sock = 
				new DatagramSocket(sipStack.udpPort); 
                		sock.setReceiveBufferSize
				(MAX_DATAGRAM_SIZE);
			int bufsize = sock.getReceiveBufferSize();
			byte message[] = new byte[bufsize]; 

			while (true) {
			        DatagramPacket packet = 
				  new DatagramPacket( message, 
				  bufsize);
				sock.receive(packet);
					// Create  asynchronous message handler 
					// for this message     
				UDPMessageChannel udpMessageChannel =
				   new UDPMessageChannel
					(packet,sipStack,notifier);
			}
		} catch (Exception ex) {
			ServerInternalError.handleException(ex);
		}
	}

	/**
	* Return the transport string.
	*@return the transport string
	*/
	public String getTransport() { 
			return "udp";
	}


    /** Get the port from which the UDPMessageProcessor is reading messages
    * @return Port from which the udp message processor is
    * reading messages.
    */        
	public int getPort() {
		return sipStack.udpPort;
	}

	/**
	* Returns the stack.
	*@return my sip stack.
	*/
	public SIPStack getSIPStack() { 
		return sipStack;
	}

	
    
}
