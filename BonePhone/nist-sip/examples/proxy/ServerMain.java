/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
*  - added Firewall and NAT support and debugged.			       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: ServerMain.java
 * created 05-Sep-00 5:04:21 PM by mranga
 */


package examples.proxy;
import gov.nist.sip.stack.security.*;
import gov.nist.sip.stack.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.net.*;
import java.util.Hashtable;
import java.util.Vector;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.Vector;
import java.util.ListIterator;

/**
*  This is the server main. It does little more than provide a Main routine and
* calls the Server class for all further processing.
*/

public class ServerMain extends SIPStack {
    protected  static final String DEFAULT_AUTH_METHOD = "Digest";
    protected  static final String DEFAULT_LOGFILE_NAME = "serverlog";  
    protected  static final int DEFAULT_UDP_PORT = 5060;
    protected  static final int DEFAULT_TCP_PORT = 5060;
    protected  static final int DEFAULT_RMI_REGISTRY_PORT = 1099;
    protected  static final int DEFAULT_RMI_PORT = 0;
    protected  static final int DEFAULT_REGISTRATION_TIMEOUT = 30*60; 
    // 10 minute session timeout.
    protected  static final int DEFAULT_SESSION_TIMEOUT = 10 * 60;
    // 30 min.
    protected  static final int MAX_REGISTRATION_TIMEOUT = 60 * 60 ; 
    protected  static final String DEFAULT_ROUTER = 
		"gov.nist.sip.stack.DefaultRouter";
    
    // max timeout for responses from UAS
    protected  static final String DEFAULT_SERVER_LOG = 
			"/tmp/sipserverlog.txt";
    protected  int registrationTimeout; // Default if no expires is present
    protected  boolean supportThirdPartyReg;
    protected  boolean authenticationEnabled;
    protected  int maxRegistrationTimeout; 
    
    protected  boolean isForkingProxy; //Is this server a forking proxy.
    protected  boolean authenticationRequired; 
    // Authorization is required. (default TRUE)
    protected  boolean locationSearchEnabled;
    protected  boolean allowMultipleRegistration;
    // Allow multiple registrations for the same user
    protected  ContactMethod defaultContactMethod; 
    // Used to talk to external location server
    protected  NATDevice natDevice;
    protected  FirewallDevice firewallDevice;
    protected  Hashtable stackAddresses;
    protected  boolean removeContactHeaders;
    protected  Hashtable contactMethodTable; 
    protected  ContactTable contactTable;  
    // Table of contacts -- indexed by contact address
    protected String  serverLog;
    protected TransactionHandler transactionHandler; 
    protected CallRecordTable callRecordTable;
    protected String 	defaultAuthMethod;   
    protected Hashtable 	authMethods;
    protected Integer defaultSessionTimeOut;
    protected int 	maxTransactionLifetime;
    protected int 	traceLifeTime;
    protected boolean requireTimer;
    protected boolean accessLogViaRMI;
    // A table of handlers for extension methods.
    protected Hashtable extensionMethodsTable;
    protected Janitor janitor;
    protected Integer janitorSleepingPeriod;
    // Where to send the request if no route can be found.
    // This is passed (uniterpreted) as an argument to the router.
    protected String defaultRouteString;
    protected String routerClassName;
   
	
    /**
     *  A conveniance function that dumps server initialization parms.
     */
    
    protected void dumpConfig() {
	System.out.println("parseSDP = " + parseSDP);
	System.out.println("udpPort = " + udpPort);
	System.out.println("tcpPort = " + tcpPort);
	System.out.println("rmiRegistryPort = " + rmiRegistryPort);
	System.out.println("rmiPort = " + rmiPort);
	System.out.println("registrationTimeout = " + registrationTimeout);
	System.out.println("maxRegistrationTimeout = " 
			   + maxRegistrationTimeout);
	System.out.println("traceLifeTime = " + traceLifeTime);
	System.out.println("locationSearchEnabled = " + locationSearchEnabled);
	System.out.println("requireTimer = " + requireTimer);
	System.out.println("allowMultipleRegistration = " + 
			   allowMultipleRegistration);
	System.out.println("defaultContactMethod = " + defaultContactMethod);
	System.out.println("defaultSessionTimeOut = " + defaultSessionTimeOut);
	System.out.println("serverLog = " + ServerLog.getLogFileName());
	System.out.println("stackName = " + stackHostName);
	System.out.println("stackAddress = ");
	Enumeration en = stackAddresses.keys();
	while (en.hasMoreElements()) {
	    System.out.println("   " + en.nextElement());
	}
	System.out.println("router = " + routerClassName);
        System.out.println("defaultRoute = " + this.defaultRouteString);
	System.out.println("traceLevel = " + ServerLog.getTraceLevel());
	System.out.println("recordRoute = " + recordRoute);
	System.out.println("maxTransactionLifetime = " + 
			maxTransactionLifetime);
	if (natDevice != null)  {
		System.out.println("NAT Device = ");
		natDevice.print();
	} else System.out.println("Nat Disabled");
	if (firewallDevice != null) {
		System.out.println("Firewall Device  ");
		firewallDevice.print();
	} else System.out.println("Firewall Disabled ");
	System.out.println("removeContactHeaders = " + removeContactHeaders);
    }

    /**
     * Parse the configuration file and fill in the server config parms.
     * @param configFileName A configuration file
     */
    
    protected void parseConfigFile(String configFileName ) {
	int linenum = 0;
	try {
	    BufferedReader bis = 
		new BufferedReader( new FileReader(configFileName));
	    
	    while ( true) {
		if (bis == null) break; 
		// EOF reached.
		linenum++;
		String ln = bis.readLine(); 
		if (ln == null) break; // EOF reached
		String line = ln.trim(); 
		if (line.compareTo("") == 0 ) continue; 
		// Ignore empty lines 
		else if (line.charAt(0) == '#') continue; 
		// Ignore comments.
		StringTokenizer st = new StringTokenizer(line);
		String tok = st.nextToken();
		if (tok.compareTo("enableUDP") == 0 ) {
		    udpFlag = true;
		    String portnum = st.nextToken();
		    udpPort = Integer.parseInt(portnum);
		} else if (tok.compareTo("disableAuthentication") == 0) {
		    authenticationEnabled = false;
		} else if (tok.compareTo("locationSearchEnabled") == 0) {
		    locationSearchEnabled = true;
		} else if (tok.compareTo("allowMultipleRegistration") == 0) {
		    allowMultipleRegistration = true;
		} else if (tok.compareTo("requireTimer") == 0) {
		    requireTimer = true;
		} else if (tok.compareTo("enableTCP") == 0 ) {
		    tcpFlag  = true;
		    String portnum = st.nextToken();
		    tcpPort = Integer.parseInt(portnum);
		} else if (tok.compareTo("rmiRegistryPort") == 0 ) {
		    rmiRegistryPort = Integer.parseInt(st.nextToken());
		} else if (tok.compareTo("rmiPort") == 0 ) {
		    rmiPort = Integer.parseInt(st.nextToken());
		} else if (tok.compareTo("traceLifeTime") == 0 ) {
		    traceLifeTime = Integer.parseInt(st.nextToken());
		} else if (tok.compareTo("janitorSleepingPeriod") == 0 ) {
		    janitorSleepingPeriod = new Integer(st.nextToken());
		} else if (tok.compareTo("registrationTimeout") == 0 ) {
		    String timeout = st.nextToken();
		    registrationTimeout = Integer.parseInt(timeout);
		} else if (tok.compareTo("maxRegistrationTimeout") == 0 ) {
		    String timeout = st.nextToken();
		    maxRegistrationTimeout = Integer.parseInt(timeout);
		} else if (tok.compareTo("defaultSessionTimeOut") == 0 ) {
		    String timeout = st.nextToken();
		    defaultSessionTimeOut = new Integer(timeout);
                } else if (tok.compareTo("defaultRoute") == 0) {
                    String nexttok = st.nextToken();
                    super.setDefaultRoute(nexttok);
		} else if (tok.compareTo("authenticationMethod") == 0) {
		    // Class name of authentication Method
		    String authMethodName = st.nextToken().toLowerCase();
		    String authMethodClassName = st.nextToken();
		    Class authMethClass = Class.forName(authMethodClassName);
		    AuthenticationMethod authenticationMethod
			= (AuthenticationMethod) authMethClass.newInstance();
		    String passwordFile = st.nextToken();
		    authenticationMethod.initialize(passwordFile);
		    ServerLog.traceMsg(ServerLog.TRACE_INITIALIZATION,
				       tok + ":" + authMethodName
				       + ":"+ authMethodClassName + ":"
				       + passwordFile);
		    authMethods.put(authMethodName,authenticationMethod);
		} else if (tok.compareTo("router") == 0) {
		    String routerClassName = st.nextToken();
		    Class routerClass = 
                      Class.forName(routerClassName);
		    Router router = 
			(Router) routerClass.newInstance();
		    this.setRouter(router);
		} else if (tok.compareTo("defaultAuthenticationMethod") == 0) {
		    defaultAuthMethod = st.nextToken().toLowerCase();
		} else if (tok.compareTo("serverLog") == 0) {
		    serverLog = st.nextToken();
		    ServerLog.setLogFileName(serverLog); 
		} else if (tok.compareTo("accessLogViaRMI") == 0) {
		    accessLogViaRMI = true;
		    if (ServerLog.getTraceLevel() < ServerLog.TRACE_MESSAGES)
		        ServerLog.setTraceLevel(ServerLog.TRACE_MESSAGES);
		} else if (tok.compareTo("traceLevel") == 0) {
		    ServerLog.setTraceLevel(Integer.parseInt(st.nextToken()));
		} else if (tok.compareTo("maxTransactionLifetime") == 0) {
		    maxTransactionLifetime = Integer.parseInt(st.nextToken());
		} else if (tok.compareTo("stackName") == 0) {
		    stackHostName = st.nextToken();
		} else if (tok.compareTo("gatewayAddress") == 0) {
		    String gwAddr = st.nextToken();
		    if (natDevice == null) 
			natDevice = new NATDevice(this);
		    natDevice.setDeviceAddress(gwAddr);
		    if ( firewallDevice == null) 
				firewallDevice = new FirewallDevice(this);
		    firewallDevice.setDeviceAddress(gwAddr);
		} else if (tok.compareTo("stackAddress") == 0){
		    stackAddress = st.nextToken();
		    stackAddresses = new Hashtable();
		    stackAddresses.put(stackAddress, new Boolean(true));
		    for (int i = 0 ; st.hasMoreTokens(); i++) {
			stackAddresses.put(st.nextToken(), new Boolean (true));
		    }
		} else if (tok.compareTo("innerNetworkPrefix") == 0){
		    if (natDevice == null) 
			natDevice = new NATDevice(this);
		    String[] innerNetwork = natDevice.setInnerNetwork(st);
		    if ( firewallDevice == null) 
				firewallDevice = new FirewallDevice(this);
		    firewallDevice.setInnerNetwork(innerNetwork);
		} else if (tok.compareTo("firewallTransversal") == 0) {
		    if ( firewallDevice == null) 
			 firewallDevice = new FirewallDevice(this);
		    firewallDevice.activate();
		    firewallDevice.setPortOpeningScript(st.nextToken());
		    firewallDevice.setPortClosingScript(st.nextToken());
		    enableParseSDP();
		} else if (tok.compareTo("firewallPortTranslation") == 0) {
		    if ( firewallDevice == null) 
			 firewallDevice = new FirewallDevice(this);
		    firewallDevice.setPortTranslation(st);
		    parseSDP = true;
		} else if (tok.compareTo("enableDebug") == 0) {
		    debugFlag = true;
		} else if (tok.compareTo("removeContactHeaders") == 0) {
		    removeContactHeaders = true;
		} else if (tok.compareTo("natTransversal") == 0) {
		    if (natDevice != null) natDevice.activate();
		} else if (tok.compareTo("enableRecordRoute") == 0){
		    recordRoute = true;
		} else if (tok.compareTo("parseSDP") == 0 ) {
		    enableParseSDP();
		} else if (tok.compareTo("extensionParser") == 0 ) {
		     String extensionName = st.nextToken();
		     String className = st.nextToken();
		     Class parserClass = Class.forName(className);
		     ExtensionParser parser = (ExtensionParser) 
				parserClass.newInstance();
		     this.registerExtensionParser
				(extensionName,parser);
                } else if (tok.compareTo("extensionMethod") == 0) {
                    // Register an extension method
                    String extensionMethod = st.nextToken();
                    String className = st.nextToken();
                    Class extensionHandlerClass = Class.forName(className);
                    ExtensionMethodHandler handler = (ExtensionMethodHandler)
                        extensionHandlerClass.newInstance();
                    this.registerExtensionMethodHandler
                        (extensionMethod,handler);
                } else {
		    throw new SIPException("Invalid token in config file " +
					   ln + " Line " + linenum);
		}
	    }
			
	    // See if the authentication method specified can be
	    // loaded.
	    if (authenticationEnabled &&
		defaultAuthMethod == null) {
		throw new SIPException 
		 ("Auth enabled but defaultAuthMethod not specified!");
	    }

	    if (authenticationEnabled && 
		authMethods.get(defaultAuthMethod) == null) {
		throw new SIPException
		    ("Could not find default Auth method " + 
		     defaultAuthMethod);
	    }
	    
	    if (registrationTimeout > maxRegistrationTimeout) {
		throw new SIPException
		    ("defaultRegistrationTimeout > maxRegistrationTimeout");
	    }
	} catch (Exception ex) {
	    System.err.println 
		("Error Parsing Config file : " + configFileName   + 
		 " in line " + linenum );
	    ex.printStackTrace();
	    System.exit(0);
	}
	// dump the configuration.
	dumpConfig();
    }
    
    /**
     *Register an extension method handler.
     */
    public void registerExtensionMethodHandler(String method,
        Object handler) {
        this.extensionMethodsTable.put(method,handler);
    }
    
    
    /**
     *Get the extension method handler for a given extension method.
     *@param method is the method for which we want to get the handler.
     */
    public ExtensionMethodHandler getExtensionMethodHandler(String method) {
        ExtensionMethodHandler handler = (ExtensionMethodHandler) 
                extensionMethodsTable.get(method);
        return handler;
    }
        
    
    
    
    
    /** Get the require timer flag.
    */
    public boolean getRequireTimer() { return requireTimer; }

    /**
     * Get the authentication methods as a map.
     * @return Hashtable
     */

    protected Hashtable getAuthMethods() {
	return authMethods;
    }
    
    
    /**
     * Get the contact list for a user and host combination.
     * @param userAtHost a 'user@host' combination
     * @return A contact list
     */
    
    protected ServerContactList getContactList( String userAtHost) {
	return (ServerContactList) contactTable.get(userAtHost);
    }
    
    
    /**
     * get a given authentication method from its name.
     * @param name An authentication method name
     * @return The authentication method itself
     */

    public AuthenticationMethod getAuthMethod( String name) {
	return (AuthenticationMethod) authMethods.get(name);
    }
    
    /**
     * get the default authentication method name
     * @return An authentication method name
     */

    public String getDefaultAuthMethodName() {
	return defaultAuthMethod;
    }
    
    /**
     * Constructor
     * @param sipreq An interface for generating new requests and responses
     */
    
    public ServerMain(SIPStackMessageFactoryImpl sipreq) {
	super(sipreq);
	authMethods = new Hashtable();
	tcpPort = 5060;
	udpPort = 5060;
	udpPort = DEFAULT_UDP_PORT; 
	tcpPort = 0;
	rmiRegistryPort = DEFAULT_RMI_REGISTRY_PORT;
	rmiPort = DEFAULT_RMI_PORT;
	udpFlag = true;  
	tcpFlag = false;      
        maxTransactionLifetime = MAX_TRANSACTION_LIFETIME;
	registrationTimeout = DEFAULT_REGISTRATION_TIMEOUT;
	maxRegistrationTimeout = MAX_REGISTRATION_TIMEOUT;
	// Only support users registering for themselves.
	supportThirdPartyReg = false;

	isForkingProxy  = false;	// This is not a forking proxy.
	// Table of contact addresses for this user.
	contactTable = new ContactTable(this);
	// Requests that have been forwarded.
	transactionHandler = new TransactionHandler(this);
	// In case no registrations are found, this is how we try to 
	// contact the user.
	defaultContactMethod = new DefaultContactMethod();
	// Enable authentication.
	authenticationRequired = true;
	// Default authentication method is Digest.
	defaultAuthMethod = DEFAULT_AUTH_METHOD;
	serverLog = DEFAULT_SERVER_LOG;
	debugFlag = false;
	authenticationEnabled = true;
	// Disable the record route option
	serverLog = "sipserverlog";
	defaultSessionTimeOut = new Integer(DEFAULT_SESSION_TIMEOUT);
	callRecordTable = new CallRecordTable(this);
	routerClassName = DEFAULT_ROUTER;
	// This is a placeholder BUGBUG
	disableInputTracking = false;
    }

    /**
     * Remove all contacts for a given user and host.
     * @param userAtHost a 'user@host' combination
     */
    
    protected void deleteContacts( String userAtHost) {
	ServerLog.traceMsg(ServerLog.TRACE_DEBUG, 
			"Removing contacts for " + userAtHost);
	synchronized(contactTable) {
	  contactTable.remove(userAtHost);
	}
    }
    
    /**
     * Do whatever is needed when an opening session message is processed
     * i.e. keeping track of meaningful data, and firewall opening and
     * NAT transversal if necessary.
     * @param source Source of the message
     * @param destination Destination of the message
     * @param message The message itself
     */

    protected synchronized void sessionOpening(String source,
				  String destination,
				  SIPRequest message, 
				  TransactionRecord transaction ) {
	
	CallRecord callRecord;
	PeerRecord peerRecord = null;
	Vector list;
	String callId = message.getCallLegID();
	if (ServerLog.needsLogging())  {
	    ServerLog.logMessage("putting in call record table " + 
			callId + "    " + transaction.getTransactionID());
	}

	// get the call data
	if (callRecordTable.containsKey(callId)) {
	    callRecord = (CallRecord) callRecordTable.get(callId);
	    peerRecord = callRecord.getPeerRecord(source);
	} else {
	    callRecord = new CallRecord(transaction);
	    callRecord.setRefreshDate(new RefreshDate(defaultSessionTimeOut));
	    callRecordTable.put(callId, callRecord);
	}
	
	// update the refresh date
	SessionExpiresHeader sessionExpires = (SessionExpiresHeader) 
	    message.getExtensionHdr("SessionExpiresHeader");
	if (sessionExpires != null) {
	    callRecord.setRefreshDate(sessionExpires.getRefreshDate());
	}

	// fill the peer data
	if (peerRecord == null) {
	    peerRecord = callRecord.setPeerRecord(source);
	    list = getMediaPortList(message);
	    peerRecord.setMediaPortList(list);
	    list = getMediaConnectionAddressList(message);
	    peerRecord.setMediaConnectionAddressList(list);
	}

	// Firewall and NAT stuff
	if (firewallDevice != null) 
		firewallDevice.sessionOpening
		(source, destination, message, callRecord);
	if (natDevice != null) 
		natDevice.transversal(source, destination, message);
	
	if (removeContactHeaders) removeContactHeader(message);
    }
	
    /**
     * Do whatever is needed when a closing session message is processed
     * i.e. mark the call as ended and firewall closing if necessary
     * @param source Source of the message
     * @param destination Destination of the message
     * @param message The message itself
     */

    protected synchronized void sessionClosing(String source,
				  String destination,
				  SIPRequest message) {
	
	String callId = message.getCallLegID();
	
	if (ServerLog.needsLogging() ) {
	    ServerLog.logMessage("Getting transaction  for call record " +
			callId);
	}
	
	if (callRecordTable.containsKey(callId)) {
	    
	    // get the call data
	    CallRecord callRecord = (CallRecord) callRecordTable.get(callId);
	    PeerRecord peerRecord = callRecord.getPeerRecord(source);
	    
	    // mark for deletion
	    if (peerRecord != null) peerRecord.leavingCall();
	    
	    if (callRecord.isFinished()) {
		// The session is ending since the last peer has left
		if (firewallDevice != null) 
			firewallDevice.sessionClosing(callId, callRecord);
		// callRecordTable.remove(callId);
		callRecordTable.markForDeletion(callId);
	    }
	}
    }

     /** Get the transaction record for this call leg.
     *@param sipRequest is the ACK or bye request.
     */
     protected synchronized CallRecord 
		getCallRecord(SIPRequest sipRequest) {

	String callId = sipRequest.getCallLegID();
	if (ServerLog.needsLogging()) {
	    ServerLog.logMessage("getting Call Record for " +
			callId);
	}
	CallRecord callRecord = (CallRecord) callRecordTable.get(callId);
	return callRecord;
     }

    /**
     * Remove Contact header to force proxying
     * Warning: this is against the RFC, use it carefully
     * @param message A message where to remove the Contact Header
     */

    protected void removeContactHeader(SIPMessage message) {
	if (removeContactHeaders) {
	    ContactList contactHeader = message.getContactHeaders();
	    if (contactHeader != null) {
		message.removeAll(contactHeader.getClass());
	    }
	}
    }

    /**
     * Convenience function 
     * Extract the origin address from the SDP content 
     * @param message Message for which we need the origin address.
     * @return The origin address.
     */

    protected static Host getOriginAddress(SIPMessage message) {
	SDPAnnounce sdp = message.getSdpAnnounce();
	OriginField originField = sdp.getOriginField();
	if (originField != null) {
	    return originField.getAddress();
	}
	return null;
    }

    /**
     * Convenience function 
     * Extract the connection address from the SDP content 
     * @param message Message for which we need the connection address 
     * from the SDP fields
     * @return Connection address from the SDP fields
     */

    protected Host getConnectionAddress(SIPMessage message) {
	SDPAnnounce sdp = message.getSdpAnnounce();
	ConnectionField connectionField = sdp.getConnectionField();
	if (connectionField != null) {
	    ConnectionAddress connectionAddress = 
		connectionField.getAddress();
	    if (connectionAddress != null) {
		return connectionAddress.getAddress();
	    }
	}
	return null;
    }

    /**
     * Convenience function 
     * Extract the media port list from the SDP content of SIP request 
     * @param message SIPMessage (request or response)
     * @return The media port list.
     */

    protected static Vector getMediaPortList(SIPMessage message) {
	SDPAnnounce sdp = message.getSdpAnnounce();
	if (Debug.debug) {
	  Debug.Assert(sdp != null,"SDP Announce missing!");
	  Debug.println("Munging =" +  message.encode());
	}
	Vector mediaPort = new Vector();
	if (sdp == null) return mediaPort;
	MediaDescriptionList mediaDescriptions = sdp.getMediaDescriptions();
	ListIterator iterator = mediaDescriptions.listIterator();
	while (iterator.hasNext()) {
	    MediaDescription mediaDescription = 
			(MediaDescription)iterator.next();
	    mediaPort.addElement(new Integer
		(mediaDescription.getMediaField().getPort()));
	}
	return(mediaPort);
    }

    /**
     * Convenience function 
     * Set the media port list in the SDP content of SIP request 
     * @param message SIPMessage (request or response)
     * @param mediaPort Vector
    */

    public static void setMediaPortList(SIPMessage message, 
					  Vector mediaPort) {
	SDPAnnounce sdp = message.getSdpAnnounce();
	for (MediaDescription mediaDescription = 
		 (MediaDescription) sdp.getMediaDescriptions().first();
	     mediaDescription != null; 
	     mediaDescription = (MediaDescription) 
		 sdp.getMediaDescriptions().next()) {
	    // BUG BUG, here we take only the first element
	    Integer port = (Integer) mediaPort.firstElement();
	    mediaDescription.getMediaField().setPort(port.intValue());
	}
    }

    /**
     * Convenience function 
     * Extract the media connection addresses list from the SDP content 
     * of SIP request
     * @param message SIPMessage for which we need to extract the 
     * media connection address list.
     * @return Media connection address list.
    */

    protected static Vector getMediaConnectionAddressList(SIPMessage message) {
	SDPAnnounce sdp = message.getSdpAnnounce();
	Vector mediaConnection = new Vector();
	if (sdp == null) return mediaConnection;
	for (MediaDescription mediaDescription = 
		 (MediaDescription) sdp.getMediaDescriptions().first();
	     mediaDescription != null; 
	     mediaDescription = (MediaDescription) 
		 sdp.getMediaDescriptions().next()) {
	    ConnectionField connectionField = mediaDescription.
		getConnectionField();
	    if (connectionField == null) {
		connectionField = sdp.getConnectionField();
	    }
	    mediaConnection.addElement(connectionField.getAddress()
				       .getAddress().getAddress());
	}
	return(mediaConnection);
    }

    /**
     * Convenience function 
     * compare a couple port/transport to the stack's own port/transport
     * @param port Port number
     * @param transport Transport protocol
     * @return boolean
     */

    protected boolean comparePort(int port, String transport) {
	if (port == -1) {
	    port = SIPStack.DEFAULT_PORT;
	}
	return (port == getPort(transport));
    }

    /**
     * Main function
     */

    protected static void main( String[] args ) {
	String configFileName = null ;
	for (int i = 0; i < args.length; i++ ) {
	    if (args[i].compareTo("-configFile") == 0) {
		configFileName = args[i+1];
		i++;
	    }
	}
	if (configFileName == null) {
	    System.err.println
		("Please specify configuration file with -configFile ");
	    System.exit(0);
	}
	ServerMain server = 
	    new ServerMain(new SIPStackMessageFactoryImpl());
	server.initProxy(server, configFileName);
    }

    /** Initialize the proxy
     * @param server The stack
     * @param configFileName A configuration file
     */ 

    protected void initProxy(ServerMain server, String configFileName) {

	// init extensions
	extensionMethodsTable = new Hashtable();


	// Parse config file
	server.parseConfigFile(configFileName);		

	// Start the janitor
	janitor = new Janitor(janitorSleepingPeriod);
	janitor.register(callRecordTable);
	janitor.register(contactTable);

	// init access via RMI
	if (accessLogViaRMI) {
	    try {
		ServerLog.initMessageLogTable
			(stackAddress,rmiRegistryPort,rmiPort,traceLifeTime);
		janitor.register(ServerLog.getMessageLogTable());
	    } catch (Exception e) {
		System.err.println("Please start RMI registry!");
		System.err.println("Exception: " + e.getMessage());
		e.printStackTrace();
	    }
	}

	// Start the server listener threads.
	server.serverMain(null);
    }
    
}
