/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.stack;
import java.util.Hashtable;
import java.io.IOException;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.stack.security.*;
import java.util.Vector;
import java.util.Map;
import java.util.Iterator;
import java.util.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
*  This is the server class that is instantiated by ServerMain to do all 
*  its work  ServerMain just passes all the command line args etc to be 
*  processed by this class  This class stores pointers to global data 
*  structures such as the registration table and handles access to these 
*  global data structures via synchronized access methods.
*  A general note about the handler structures -- handlers are expected to 
*  returnResponse  for successful message processing and throw 
*  SIPServerException for unsuccessful message processing.
*/

public abstract class SIPStack implements SIPKeywords  {

/** UDP Port of the stack
 */        
	protected   int    	udpPort; 
/** TCP port on which the stack listens
 */        
	protected   int 	tcpPort;
/** RMI port on which one can access the message log
 */        
	protected   int 	rmiPort;
/** RMI registry port on which the proxy registers its object
 */        
	protected   int 	rmiRegistryPort;
/** IS TCP Enabled?
 */        
	protected   boolean 	tcpFlag;    // The server is a TCP based server
/** Is UDP Emabled?
 */        
	protected   boolean 	udpFlag;
/** Internal flag for debugging
 */        
	protected   boolean 	debugFlag;
/** A global counter to generate stack-unique tags
 */        
	protected   int 	tagCounter;     
/** Name of this stack (usually hostname of the machine where stack resides)
 */        
	protected   String  	stackHostName;   // My DNS Name (or pseudonym)
/** IP address of stack
 */        
	protected   String  	stackAddress; // My host address.
/** Request factory interface (to be provided by the application)
 */        
	protected   SIPStackMessageFactory sipMessageFactory;

/** Ptr to active object that processes messages received on the TCP port.
 */        
	protected   TCPMessageProcessor tcpMessageProcessor;
/** Ptr to the active object that processes messages received on UDP port.
 */        
	protected   UDPMessageProcessor udpMessageProcessor;
/** Default timeout for transactions (in seconds)
*/
	protected   static final int 	MAX_TRANSACTION_LIFETIME = 32;

/** Default UDP port (5060)
 */        
	public      static   final int DEFAULT_PORT = 5060;

/** A flag that enables/disables tracking of input in the parser (more info
* is available to exception handler if this is enabled, but it is a 
* performance dawg.
*/
	protected boolean disableInputTracking;

/** A flag that indicates whether to parse SDP content.
*/

	protected boolean parseSDP;


/** Router to determine where to forward the request.
*/
	protected Router	router;

/** Record route flag.
*/
	protected boolean recordRoute;


/**  Table of registered exception header names and classes.
*/
	protected Hashtable extensionTable;


/** A sequence number counter. */
	protected long cSeq;

	/** Get the cseq counter  */
	protected synchronized long getCSeq() {
		cSeq ++;
		return cSeq;
	}

	/** Set the cseq number provided that it is monontonically increasing.
	*@param cseq is the sequence number to set.
	*@return the sequence number set
	*/
	public synchronized long setCSeq(long cseq)  {
		if (cseq < this.cSeq)  {
			return this.cSeq;
		} else {
			cSeq = cseq;
			return this.cSeq;
		}
 	}




	
	/** set the address of the default proxy.
	*@param route is an un-interpreted string that is passsed on to
        * the router. The default router interprets it to be
        *  host:port/TRANSPORT (example: jitterbug.antd.nist.gov:5070/UDP)
	*/
	public void setDefaultRoute(String route) {
		System.out.println("setNextHop " + route);
                router.setNextHop(route);
	}

	
        /** Get the default route string.
         *@param sipRequest is the request for which we want to compute
         *  the next hop.
         */
        public Iterator getNextHop(SIPRequest sipRequest) {
            return router.getNextHop(sipRequest);
        }

	/**
         * Construcor for the stack. Registers the request and response
         * factories for the stack.
         * @param messageFactory User-implemented factory for processing 
	 * 		messages.
         */
	public SIPStack ( SIPStackMessageFactory messageFactory ) {
	        this();
		sipMessageFactory = messageFactory;
	}

	/**
	* Set the server Request and response factories.
	*@param messageFactory User-implemented factory for processing
	*    messages.
	*/
	public void setStackMessageFactory
		( SIPStackMessageFactory  messageFactory) {
		sipMessageFactory = messageFactory;
	}

	
	/**
         * Generate a new unique tag
         * @return A new tag that is uniqe to this stack.
        */
	protected synchronized String getNewTag() {
		tagCounter ++;
		return new Integer(tagCounter).toString();
	}
	
	/**
         * Get my server address.
         * @return A string containing the host address for this
         * stack (that is set by the configuration file).
        */
        public String getHostAddress() { return this.stackAddress; }
	
	/**
         * Get my server name
         * @return A string containing the host name for this 
         * stack (that is set during configuration).
         */
        public String getHostName() { return this.stackHostName; }

	/** Set my server name.
	*@param hostName my DNS name.
	*/
	public void setHostName(String hostName) { 
		this.stackHostName = hostName;
	}

	/** Set my address.
	*@param A string containing the address to set.
	*/
	public void setHostAddress( String address ) {
		this.stackAddress = address;
	}
        
        
        /** Get the default port 
         */
        public int getDefaultPort() {
            if (this.udpFlag) return udpPort;
            else return tcpPort;
        }
        
        
        /** Get the default transport.
         */
        public String getDefaultTransport() {
            if (this.udpFlag) return SIPKeywords.UDP;
            else return SIPKeywords.TCP;
        }

	

	/** A conveniance function:
         * Get the stack address as it appears in the Via Headers.
         * For consistency purpose, all the functions writing
         * in the Via headers, or performing lookup on it, in
         * order to find the stack address have to call this
         * single function.
         * @return Stack address as it appears in the Via Headers
         */
        public String getViaHeaderStackAddress() {
	   return stackAddress;
        }

	/**
	* Get my stack URL.
        * @return a string giving the URL of the stack.
	*/
	public String getStackURI()  { 
            if (this.udpFlag)
	        return SIP+COLON+this.stackAddress + COLON+ this.udpPort +
		   SEMICOLON + SIPKeywords.TRANSPORT + EQUALS +
		   SIPKeywords.UDP ; 
            else return SIP+COLON+this.stackAddress + COLON+ this.tcpPort +
		   SEMICOLON + SIPKeywords.TRANSPORT + EQUALS +
                   SIPKeywords.TCP ; 
	}

       /** Get the record route flag.
       */
       public boolean getRecordRoute() { return recordRoute; }
	
	
	/**
	* get my port (based on the transport)
	*/
	public int getPort( String transport) {
		if (transport.compareTo(UDP) == 0) return udpPort;
		else return tcpPort;
	}

	/** Get my TCP port.
	*@return tcpPort
	*/
 	public int getTCPPort() { return tcpPort; }

	/** Get my UDP port.
	*@return udpPort
	*/
	public int getUDPPort() { return udpPort; }

	/** Set my tcp port and enable tcp communication for the stack.
	*@param port port on which tcp messages will be recieved by
	* this stack.
	*/
	protected void setTCPPort ( int port) {
		tcpPort = port;
		tcpFlag = true;
	}

	/** set my udp port and enable udp commmuncation for the stack.
	*@param port port on which udp messages will be received by
	* this stack.
	*/
	protected void setUDPPort(int port) {
		udpPort = port;
		udpFlag = true;
	}

	/** Test if tcp is enabled.
	*/
	public boolean isTCPEnabled() { return tcpFlag; }


	/** Test if UDP is enabled.
	*/
	public boolean isUDPEnabled() { return udpFlag; }
	

	/** Return the AuthenticationMethod given a string 
         * (null if none exists).
         * @param authMethod Authentication method name (for example,
         * Basic or Digest)
         * @return An object that implements the 
         * authentication method for the given
         * authentication method name.
	 */
	public abstract AuthenticationMethod getAuthMethod(String authMethod);
	

	/** Return the name of the default authentication method.
         * @return The default authentication method for 
         * this stack (null if none exsits)
 	 */

	public abstract String getDefaultAuthMethodName();
		

	
	/**
	* Default constructor.
	*/
	public SIPStack() {
	   extensionTable = new Hashtable();
           router = new DefaultRouter();
	   disableInputTracking = true;
	   parseSDP = false;
	}

	/** Enable SDP parsing. (Default is disabled).
	*/
	public void enableParseSDP() { parseSDP = true; }


	/**
         * Generate a new SIPSeverRequest from the given SIPRequest. A 
         * SIPServerRequest is generated by the application 
         * SIPServerRequestFactoryImpl. The application registers the
         * factory implementation at the time the stack is initialized.
         * @param siprequest SIPRequest for which we want to generate
         * thsi SIPServerRequest.
         * @param msgchan Message channel for the request for which
         * we want to generate the SIPServerRequest
         * @return Generated SIPServerRequest.
 	*/	
	protected SIPServerRequestInterface 
	newSIPServerRequest (SIPRequest siprequest, MessageChannel msgchan)  {
		return sipMessageFactory.newSIPServerRequest
			(siprequest,msgchan);
	}

	/**
         * Generate a new SIPSeverResponse from the given SIPResponse.
         * @param sipresponse SIPResponse from which the SIPServerResponse 
         * is to be generated. Note - this just calls the factory interface
         * to do its work. The factory interface is provided by the user.
         * @param msgchan Message channel for the SIPServerResponse
         * @return SIPServerResponse generated from this SIP
         * Response
         */
        protected SIPServerResponseInterface 
	newSIPServerResponse(SIPResponse sipresponse, MessageChannel msgchan)  {
		return sipMessageFactory.newSIPServerResponse
			(sipresponse,msgchan);
	}

	/**
	* Register a class to assign for a given header name.
	* @param extensionName is the extension name for which the 
	* 	extensionParser is implemented.
	* @param extensionParser implements the ExtensionParser interface
	*    and returns a message of the appropriate class corresponding
	*	to the extension.
	*/
	public void registerExtensionParser (String extensionName,
			ExtensionParser extensionParser) {
		extensionTable.put(extensionName,extensionParser);
	}
        
        /**
         * Get an extension parser for a given extension name.
         * @param extensionName is the name of the extension for which we want 
         * to get the registered parser.
         * @return An implementation of an extension parser for
         * the given extension name.
         */
        public ExtensionParser getExtensionParser(String extensionName) {
            return (ExtensionParser) extensionTable.get(extensionName);
        }

	/** Set the router algorithm.
	*@param router A class that implements the Router interface.
	*/
	public void setRouter(Router route) { router = route; }

	/** Set the hostname of the stack. (i.e. the DNS name).
	*@param hostname The hostname of the stack,
	*/
	public void setStackHostName(String hostname) {
		stackHostName = hostname;
	}

	/** Get the stack host name.
	*/
	public String getStackHostName() { return stackHostName; }

	/** Get the default route.
	*/
	public Hop getDefaultRoute() { return this.router.getDefaultRoute(); }

									
	/**
         * SIPStack main routine. All the action begins here.
         * To instantiate a SIP server, you create this class and 
         * call serverMain with the command line arguments.
         * @param notifier An implementation of an (optional) channel 
         * notifier (registered by the application)
         * that gets called when TCP connections are
         * closed.
         */
	public void serverMain (ChannelNotifier notifier ) {
		if (stackAddress == null ) {
			System.err.println
			("stackAddress not set!");
			System.exit(0);
		}
		if (! udpFlag  && ! tcpFlag ) {
			System.err.println
		       ("Transport not specified: must specify UDP and/or TCP");
			System.exit(0);
		}

		
		if (udpFlag) {
			udpMessageProcessor = 
				new UDPMessageProcessor(this,notifier);
			udpMessageProcessor.start();
		}
		// Can support conenncted operation  from this server.
		if(tcpFlag) {
		        tcpMessageProcessor = 
				new TCPMessageProcessor(this,notifier);
			tcpMessageProcessor.start();
		}
		// Dont worry, be happy, 
		// JVM will not exit here. (At least not sun's JVM)
	}

}
