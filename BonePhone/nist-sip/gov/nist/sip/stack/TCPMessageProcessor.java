package gov.nist.sip.stack;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;


/**
* Sit in a loop waiting for incoming tcp connections and start a 
* new thread to handle each new connection. This is the active 
* object that creates new TCP MessageChannels (one for each new
* accept socket). 
*/
public class TCPMessageProcessor implements MessageProcessor  {
/** The SIP Stack Structure.
 */    
	protected SIPStack sipStack;
    /** Optional channel notifier (method gets invoked on channel open
     * and close).
     */        
	protected ChannelNotifier notifier;

    /** Constructor.
    * @param srv SIPStack structure.
    * @param notify Optional channel notifier.
    */        
	protected TCPMessageProcessor (SIPStack srv, ChannelNotifier notify ) {
		sipStack = srv;
		notifier = notify;
	}

    /**
    * Start the processor.
    */
     public void start() {
	Thread thread = new Thread(this);
	thread.start();
     }

    /** Run method for the thread that gets created for each accept
    * socket.
    */        
	public void run() {
		try {
			ServerSocket sock = new ServerSocket(sipStack.tcpPort);
			// Accept new connectins on our socket.
			while(true) {
				Socket newsock = sock.accept();
				ServerLog.traceMsg
						(ServerLog.TRACE_DEBUG,
					"Accepting new connection!");
				// Start  asynchronous thread to handle 
				// this message.
			        TCPMessageChannel tcpMessageChannel = 
				  new TCPMessageChannel
					(newsock,sipStack,notifier);
				if (notifier != null) {
				  notifier.notifyOpen(tcpMessageChannel);
				}
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
			return "tcp";
	}

	/** Returns the port that we are listening on.
         * @return Port address for the tcp accept.
         */

	public int getPort() {
		return sipStack.tcpPort;
	}

	/**
	* Returns the stack.
	*@return my sip stack.
	*/
	public SIPStack getSIPStack() { 
		return sipStack;
	}

		
}
	
