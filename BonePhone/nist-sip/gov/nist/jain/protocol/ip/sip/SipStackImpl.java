/*
 * SipStackImpl.java
 *
 * Created on April 17, 2001, 12:00 PM
 */

package gov.nist.jain.protocol.ip.sip;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.message.*;
import gov.nist.jain.protocol.ip.sip.*;
import gov.nist.jain.protocol.ip.sip.message.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.stack.*;
import gov.nist.sip.stack.security.*;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Collection;
import java.util.LinkedList;
import java.io.IOException;
import java.net.InetAddress;
import gov.nist.log.*;

/**
 *
 * @author  M. Ranganathan (mranga@nist.gov)
 * @version 1.0
 *This implementation uses the NIST-SIP corresponding structure for
 *building the SipStack. The mapping is as follows:
 *TCPMesssageProcessor -> ListeningPoint
 *UDPMessageProcessor -> ListeningPoint.
 *MessageChannel      -> SipProviderImpl
 *<p> JAIN does not define a standard way in which to configure the
 * stack. We opt for a simple keyword-value based configuration method
 * (clearly an xml-based scheme would be cleaner and we will probably
 * get around to replacing the parser with an SAX parser).
 *<p> The file "configfile" in the directory where the main class
 * (i.e. application) resides has configuration parameters for the stack:
 *
 * <pre>
 * authenticationMethod methodName methodClass  [parms]
 * specifies a supported authentication method.
 *
 * traceLevel level
 * Specifies the debug trace level (32 is the max trace level)
 *
 * enableUDP port#
 * Specifies that UDP is supported on port#
 *
 * enableTCP port#
 * Specifies that TCP is supported on port#
 *
 * stackHostName name
 * Specify a DNS name for this stack.
 *
 * stackAddress address
 * Address of host where stack resides.
 * This is a required parameter.
 *
 * serverLog logfileName
 * File name where the log is written.
 *
 * enableRecordRoute
 * Add the record route header when forwarding requests.
 *
 * accessLogViaRMI
 * Log all messages that pass through the stack and make the log
 * accessible via RMI. Default is false.
 *
 * rmiRegistryPort portNumber
 * The port on which the rmi registry runs.
 *
 * rmiPort portNumber
 * The port on which the service runs.
 *
 * traceLifeTime seconds
 * Seconds for which to keep the trace around for remote access.
 *
 * defaultRoute string
 *  Set the default route (used by gov.nist.sip.stack.DefaultRouter)
 *  This has the form hostName:port/Transport
 *  The router is consulted to get a list of addresses in order of priority
 *  that are tried in order to send an outgoing message.
 *
 * defaultRouter className
 *  set a default router
 *
 * Here is an example configfile:
 *
 * traceLevel 32
 * stackHostName nist.gov
 * authenticationMethod Digest gov.nist.sip.stack.security.DigestAuthenticationMethod passwords
 * serverLog myLogfile
 * stackAddress 129.6.55.64
 * enableUDP 5061
 * enableUDP 5060
 * defaultRoute is2.antd.nist.gov:5060/UDP
 * accessLogViaRMI
 *
 * </pre>
 */

public class SipStackImpl extends SIPStack implements SipStack, Runnable  {
    
    protected LinkedList listeningPoints;
    protected ListeningPointImpl tcpListeningPoint;
    protected ListeningPointImpl udpListeningPoint;
    protected LinkedList sipProviders;
    protected  static final int    DEFAULT_RMI_PORT =  0;
    protected  static final int    DEFAULT_RMI_REGISTRY_PORT = 1099;
    protected  static final int    DEFAULT_TRACE_LIFETIME = 3600;
    protected  static final String CONFIGURATION_FILE_NAME = "configfile";
    protected  static final String DEFAULT_AUTH_METHOD = "Digest";
    protected  static final String DEFAULT_STACK_NAME =
    "JAIN-SIP For the People!";
    protected  static final int DEFAULT_UDP_PORT = 5060;
    protected  static final int DEFAULT_TCP_PORT = 5061;
    protected  static final int DEFAULT_TRANSACTION_TIMEOUT = 10;
    // Time in seconds for a transaction to be declared timed-out
    // max timeout for responses from UAS
    protected  static final String DEFAULT_SERVER_LOG = null;
    protected  boolean authenticationEnabled;
    // Max time for which we hold registrations
    protected  boolean authenticationRequired;
    protected String authenticationMethod;
    protected  Hashtable authMethods;
    protected  String  serverLog;
    protected  long     transactionCount;
    // Table of  string -> integer mappings for transaction identification.
    protected  Hashtable serverTransactionTable;
    protected  Hashtable clientTransactionTable;
    // Stack name (Why does this exist? how is this used??)
    protected String stackName;
    protected  int transactionLifetime;
    protected  int transactionTimeout;
    // parameters for remote access to the trace.
    protected boolean accessLogViaRMI;
    protected int traceLifeTime;
    
    protected String defaultAuthMethod;
    /** Table of providers indexed by protocol:address:port */
    protected  Hashtable   providerTable;
    /** A table of available outgoing message channels */
    protected  Hashtable   messageChannelTable;
    
    /** Static variables that allow us to access the stack address for
     * default parameter setting. */
    
    public static SipStackImpl theStack;
    
    
    
    /**
     *Put this message into a transaction table and return an id for it.
     *@param requestImpl is request implementation for which to make this
     *  transaction record.
     */
    protected
    long makeTransaction( RequestImpl requestImpl,
    ListeningPointImpl listeningPoint, MessageChannel mchan,
    boolean isServerTransaction) {
        if (requestImpl == null || listeningPoint == null || mchan == null)
            throw new IllegalArgumentException("null arg ");
        SIPMessage sipMessage = requestImpl.getImplementationObject();
        String tid = sipMessage.getTransactionId();
        // check if the transaction record already exists.
        
        
        Hashtable ttable = (isServerTransaction? serverTransactionTable:
            clientTransactionTable);
            
            //  see if a transaction record pops up for this transaction.
            synchronized(ttable) {
                Transaction transaction = (Transaction) ttable.get(tid);
                if (transaction != null) {
                    // if a record for the transaction already exists,
                    // just return a pointer to it.
                    if (LogWriter.needsLogging())
                        LogWriter.logMessage
                        ("found transaction record for :" + tid);
                    transaction.lock();
                    return transaction.getTidLong();
                }
                transaction = new Transaction(requestImpl,listeningPoint,
                this,isServerTransaction,mchan);
                transactionCount++;
                long tlong = transactionCount;
                Long tidLong = new Long(tlong);
                
                transaction.lock();
                transaction.setTid(tlong);
                transaction.setTid(tid);
                ttable.put(tidLong.toString(),transaction);
                ttable.put(tid,transaction);
                SIPRequest sipRequest = (SIPRequest) sipMessage;
                if (( ! isServerTransaction)  &&
                ( ! sipRequest.getMethod().equals(Request.ACK)) &&
                ( ! sipRequest.getMethod().equals(Request.BYE))
                )  {
                    String cid = sipRequest.getCancelID();
                    transaction.setCID(cid);
                    ttable.put(cid,transaction);
                    if (LogWriter.needsLogging())
                        LogWriter.logMessage("putting transaction " + tlong +
                        " tid/cid = " + tid  + " /  " + cid +
                        " isServerTransaction = " + isServerTransaction);
                } else {
                    if (LogWriter.needsLogging())
                        LogWriter.logMessage("putting transaction " + tlong +
                        " tid = " + tid   +
                        " isServerTransaction = " + isServerTransaction);
                }
                return tlong;
            }
            
            
    }
    
    protected void unlockTransaction
    (long tid,
    boolean isServerTransaction) {
        Hashtable ttable =
        (isServerTransaction?
        this.serverTransactionTable: this.clientTransactionTable);
        Transaction transaction;
        synchronized(ttable) {
            transaction = (Transaction)
            ttable.get(new Long(tid).toString());
        }
        if (transaction != null) transaction.unlock();
    }
    
    
    
    /**
     *Match the response with the client transaction record and update
     *Transaction table.
     */
    protected
    long putResponse(ResponseImpl response,
    boolean isServerTransaction)
    throws TransactionDoesNotExistException {
        SIPMessage sipMessage = response.getImplementationObject();
        String tid = sipMessage.getTransactionId();
        Hashtable ttable = (isServerTransaction? serverTransactionTable:
            clientTransactionTable);
            
            Transaction transaction;
            synchronized(ttable) {
                transaction = (Transaction) ttable.get(tid);
            }
            if (transaction == null)
                throw new TransactionDoesNotExistException
                ("Transaction not found tid = " + tid +
                " isServerTransaction = " + isServerTransaction);
            transaction.setResponse(response);
            RequestImpl request = transaction.getRequest();
            try {
                // Look for final responses to terminate transactions.
                if (SIPResponse.isFinalResponse(response.getStatusCode()))
                    transaction.markForDelete();
            } catch (SipException ex) {
                throw new TransactionDoesNotExistException("bad message!");
            }
            return transaction.getTidLong();
    }
    
    /** Put a response into the transaction table.
     */
    
    protected
    void putResponse(long transactionId,
    ResponseImpl response,
    boolean isServerTransaction )
    throws TransactionDoesNotExistException {
        Long tidLong = new Long(transactionId);
        Hashtable ttable = (isServerTransaction? this.serverTransactionTable:
            clientTransactionTable);
            Transaction transaction;
            synchronized(ttable) {
                transaction = (Transaction)
                ttable.get(tidLong.toString());
            }
            if (transaction == null)  {
                TransactionDoesNotExistException ex =
                new TransactionDoesNotExistException
                ("Transaction not found tid = " + transactionId);
                if (LogWriter.needsLogging())
                    LogWriter.logException(ex);
                throw ex;
                
            }
            transaction.setResponse(response);
    }
    
    /**
     *Get the transaction response for a given transaction id.
     */
    protected
    ResponseImpl getResponse(long tid, boolean isServerTransaction)
    throws TransactionDoesNotExistException {
        Transaction transaction = this.getTransaction(tid,isServerTransaction);
        return transaction.getResponse();
    }
    
    /**
     *Get the transaction record for the given tid.
     *@param tid is the transaction identifier.
     *@param isServerTransaction tells if it is a server/client transaction.
     *@throws TransactionDoesNotExistException if the transaction is not
     *   found.
     */
    protected
    Transaction getTransaction( long tid, boolean isServerTransaction)
    throws TransactionDoesNotExistException {
        if (ServerLog.needsLogging())
            ServerLog.logMessage
            ("getTransaction " + tid + " isServerTransaction = "
            + isServerTransaction);
        
        Long tidLong = new Long(tid);
        Hashtable ttable;
        ttable =
        (isServerTransaction? serverTransactionTable:
            clientTransactionTable);
            Transaction transaction = null;
            synchronized(ttable) {
                transaction =  (Transaction) ttable.get(tidLong.toString());
            }
            if (transaction == null)   {
                TransactionDoesNotExistException ex =
                new TransactionDoesNotExistException
                ("Transaction not found for tid = " + tid +
                " isServerTransaction  = "
                + isServerTransaction );
                if (LogWriter.needsLogging())
                    LogWriter.logException(ex);
                throw ex;
                
            }
            return transaction;
            
    }
    
    /** Get the transaction given the string form of the transaction id.
     */
    protected
    Transaction getTransaction(String tid, boolean isServerTransaction)
    throws TransactionDoesNotExistException {
        Hashtable ttable = (isServerTransaction? serverTransactionTable:
            clientTransactionTable);
            
            Transaction transaction;
            synchronized(ttable) {
                transaction = (Transaction) ttable.get(tid);
            }
            if (transaction == null)
                throw new  TransactionDoesNotExistException
                ( "Could not find transaction for " + tid
                + " isServerTransaction = "  + isServerTransaction );
            return transaction;
            
    }
    
    /**
     *remove this transaction record.
     *@param transaction is the transaction to remove.
     */
    protected void removeTransaction( Transaction transaction) {
        Hashtable ttable;
        if(transaction.isLocked()) return;
        if (transaction.IsServerTransaction()) {
            if (LogWriter.needsLogging())
                LogWriter.logMessage("Removing server transaction " +
                transaction.getTidLong());
            ttable = this.serverTransactionTable;
        } else {
            if (LogWriter.needsLogging())
                LogWriter.logMessage("Removing client transaction " +
                transaction.getTidLong());
            ttable = this.clientTransactionTable;
        }
        
        synchronized(ttable) {
            String stringKey = transaction.getTidString();
            ttable.remove(stringKey);
            Long intKey = new Long(transaction.getTidLong());
            ttable.remove(intKey.toString());
            if (transaction.getCID() != null) {
                ttable.remove(transaction.getCID());
            }
        }
    }
    
     /** Remove a transaction given a transaction ID.
      *@param transactionID is the identifier of the transaction to remove.
      *@param isServerTransaction is a boolean set to true for server
      *   transactions.
      *@throws TransactionDoesNotExistException if a transaction matching
      *  the given transactionId does not exist.
      *@return the transaction record (removed from the corresponding
      * 	transaction table).
      */
    protected  Transaction
    lockAndMarkForDeletion(String transactionId,
    boolean isServerTransaction)
    throws TransactionDoesNotExistException {
        Hashtable ttable = isServerTransaction? serverTransactionTable:
            clientTransactionTable;
            Transaction transaction =  (Transaction)
            ttable.get(transactionId);
            if (transaction == null)
                throw new TransactionDoesNotExistException
                (transactionId + " Not Found");
            transaction.lock();
            transaction.markForDelete();
            return transaction;
            
    }
    
     /**Remove this transaction record.
      *@param transaction is the transaction to remove.
      *@param isServerTransaction set to true if we are referring to a
      * server transaction.
      */
    protected void
    removeTransaction( long tid, boolean isServerTransaction) {
        Hashtable ttable = (isServerTransaction? serverTransactionTable:
            clientTransactionTable);
            synchronized(ttable) {
                Transaction transaction = (Transaction) ttable.get
                (new Long(tid).toString());
                if ( transaction  == null || transaction.isLocked()) return;
                if (transaction.isPendingDelete()) {
                    if (LogWriter.needsLogging())
                        LogWriter.logMessage("Removing server transaction " +
                        transaction.getTidLong() + " " +
                        "isServerTransaction = " +
                        isServerTransaction);
                    
                    String stringKey = transaction.getTidString();
                    ttable.remove(stringKey);
                    Long intKey = new Long(transaction.getTidLong());
                    ttable.remove(intKey.toString());
                    if (transaction.getCID() != null) {
                        ttable.remove(transaction.getCID());
                    }
                }
            }
    }
    
    
    
    /**
     *Get the sipmessage corresponding to a long transaction id.
     *@param tid is the transaction identifier.
     *@param isServerTransaction set to true if this is a server transaction.
     *@throws TransactionDoesNotExistException if the transaction cannot be
     * found.
     */
    
    protected
    RequestImpl getRequest(long tid,
    boolean isServerTransaction)
    throws TransactionDoesNotExistException {
        Long tidLong = new Long(tid);
        Hashtable ttable = (isServerTransaction? serverTransactionTable:
            clientTransactionTable);
            
            Transaction transaction;
            synchronized(ttable) {
                transaction = (Transaction) ttable.get(tidLong.toString());
            }
            if (transaction == null)
                throw new TransactionDoesNotExistException
                ("Transaction not found for tid = " + tid +
                " isServerTransaction " + isServerTransaction );
            else return transaction.getRequest();
    }
    
    /**
     *Get the SIPRequest corresponding to a transaction id.
     *@param tid is the string form of the transaction identifier.
     */
    protected
    RequestImpl getRequest(String tid,
    boolean isServerTransaction)
    throws TransactionDoesNotExistException {
        Hashtable ttable = (isServerTransaction? serverTransactionTable:
            clientTransactionTable);
            Transaction transaction;
            synchronized(ttable) {
                transaction = (Transaction) ttable.get(tid);
            }
            if (transaction == null)  throw new
            TransactionDoesNotExistException
            ("Transaction not found " + tid);
            else return transaction.getRequest();
    }
    
    
    /** Create an outgoing message channel.
     *@param address is the address for which we want to create a channel.
     *@param port is the port for which we want to create the channel.
     *@param transport is the transport string TCP or UDP.
     */
    synchronized protected MessageChannel
    createMessageChannel ( InetAddress address, int port, String transport)
    throws IOException {
        String key = MessageChannel.getKey(address,port,transport);
        MessageChannel retval = (MessageChannel) messageChannelTable.get(key);
        if (retval == null) {
            if (transport.compareTo("TCP") == 0) {
                // BUGBUG - need a channel notifier here.
                retval = new TCPMessageChannel(address,port,this,null);
            } else {
                retval = new UDPMessageChannel(address,port,this);
            }
            messageChannelTable.put(key,retval);
            
        }
        return retval;
        
    }
    
    /** Delete a messsage channel (relevant for TCP)
     *@param mchan is the message channel to remove.
     */
    synchronized protected void deleteMessageChannel( MessageChannel mchan) {
        String key = mchan.getKey();
        messageChannelTable.remove(key);
    }
    
    
    
    
    
    /**
     * Returns an Iterator of ListeningPoints available to this stack
     * (Returns null if no ListeningPoints exist)
     * @return an Iterator of ListeningPoints available to this stack
     */
    public Iterator getListeningPoints() {
        return listeningPoints.listIterator();
    }
    
    
    /**
     * Creates a new Peer (vendor specific) <CODE>SipProvider</CODE>
     * that is attached to this SipStack on a specified ListeningPoint
     * and returns a reference to it.
     * <i>Note to developers:</i> The implementation of this method
     * should add the newly created <CODE>SipProvider</CODE> to the
     * <a href="SipStack.html#getProviderList()">providerList</a> once
     * the <CODE>SipProvider</CODE> has been successfully created.
     * Initially, there will be at most two listening points (one for the
     * tcp port and one for the udp port specified in the configfile). If you
     * call createSipProvider() with either of these listening points, your
     * application will block until a message is received. If you want to
     * create a provider for the purpose of sending out messages, you can
     * add additional listening points using the
     * <a href="SipStack.html#createListeningPoint()">createListeningPoint</a>
     * below and use thes in the call to createSipProvider. This will
     * return immediately with a provider that you can use to send out messages.
     * @return Peer JAIN Sip Provider attached to this SipStack on specified
     * ListeningPoint.
     * @param <var>listeningPoint</var> the ListeningPoint the Provider
     * is to be attached to
     * @throws <var>IPPeerUnavailableException</var> thrown if the
     * Peer class could not be found
     * @throws <var>ListeningPointUnavailableException</var> thrown if
     *   the ListeningPoint specified is not
     *   owned by this SipStack, or if another Provider is already using
     * the ListeningPoint
     * @throws IllegalArgumentException if listeningPoint is null
     */
    public SipProvider createSipProvider(ListeningPoint listeningPoint)
    throws IllegalArgumentException,
    ListeningPointUnavailableException {
        if (listeningPoint == null) {
            throw new IllegalArgumentException("null listening pt");
        }
        if (!(listeningPoint instanceof ListeningPointImpl))
            throw new IllegalArgumentException("Bad implementation");
        ListeningPointImpl limp =
        (ListeningPointImpl) listeningPoint;
        if (listeningPoints.indexOf(listeningPoint) == -1)  {
            throw new
            IllegalArgumentException
            ("Not a registered listeningpoint");
        }
        
        SipProviderImpl sipProvider =  limp.getSipProvider();
        this.sipProviders.add(sipProvider);
        return sipProvider;
        
    }
    
    
    /**
     * Deletes the specified Peer JAIN SIP Provider attached to this SipStack.
     * <i>Note to developers:</i> The implementation of this method
     * should remove the specified Peer JAIN SIP Provider
     * from the <a href="#providerList">providerList</a>. <P>
     * @param <var>providerToBeDeleted</var> the Peer JAIN SIP Provider
     * to be deleted from this SipStack.
     * @exception <var>UnableToDeleteProviderException</var> thrown if
     * the specified Peer JAIN SIP Provider cannot be deleted. This may be
     * because the Peer JAIN SIP Provider has already been deleted,
     * or because the Peer JAIN SIP Provider is currently in use.
     * @throws IllegalArgumentException if providerToBeDeleted is null
     */
    public void deleteSipProvider(SipProvider sipProvider)
    throws UnableToDeleteProviderException, IllegalArgumentException {
        if (sipProvider == null)
            throw new IllegalArgumentException("Null provider");
        if (! (sipProvider instanceof SipProviderImpl) )
            throw new IllegalArgumentException("Bad implementation!");
        SipProviderImpl sipProviderImpl = (SipProviderImpl) sipProvider;
        sipProviderImpl.exit();
        sipProviders.remove(sipProvider);
        
    }
    
    /**
     * Returns an Iterator of existing Peer JAIN SIP Providers that
     * have been created by this SipStackImpl.
     * All of the Peer JAIN SIP Providers of this SipStack
     * will be proprietary objects belonging to the same stack vendor.
     * (Returns null if no SipProviders exist)
     * @return an Iterator containing all existing Peer JAIN SIP
     * Providers created by this SipStack.
     */
    public Iterator getSipProviders() {
        if (sipProviders == null) return null;
        else if (sipProviders.isEmpty()) return null;
        else return this.sipProviders.listIterator();
    }
    
    /**
     * Gets the name of this SipStack instance.
     * @return a string describing the stack instance
     */
    public String getStackName() {
        return stackName;
    }
    
    /**
     * Sets the name of this SipStack instance.
     * This name should be unique to this instance
     * of a vendor's implementation and optionally include a
     * means to identify what listening points
     * the stack owns.
     * @param <var>stack_name/var> the stack name.
     * @throws IllegalArgumentException if stackName is null or zero-length
     */
    public void setStackName(String stack_name)
    throws IllegalArgumentException {
        if (stack_name == null || stack_name.length() == 0 ) {
            throw new IllegalArgumentException("Null stack name!");
        }
        stackName = stack_name;
    }
    
    /**
     *Retrieve an authentication method given its name.
     */
    public AuthenticationMethod getAuthMethod( String name) {
        return (AuthenticationMethod) authMethods.get(name);
    }
    
    /**
     * Get the default authentication method name.
     */
    public String getDefaultAuthMethodName() { return defaultAuthMethod; }
    
     /**
      * Set the default authentication method name (default
      * value is Digest). This is used for WWW-Authenticate and
      * Proxy-Authenticate.
      *@param authMethod authentication method name to assign as default.
      */
    public void setDefaultAuthenticationMethod(String authMethod) {
        defaultAuthMethod = authMethod.toLowerCase();
    }
    
    
    /**
     * Set authentication method.
     *@param name authentication method name.
     *@param authMethod authentication method.
     */
    public void setAuthenticationMethod
    (String name, AuthenticationMethod authMethod){
        authMethods.put(name.toLowerCase(),authMethod);
    }
    
     /**
      * Disable authentication (default - enabled).
      */
    public void disableAuthentication() {
        authenticationEnabled = false;
    }
    
     /** return true if an authentication method is supported.
      *@param method is the method we want to look for.
      */
    
    public boolean isAuthenticationMethodSupported(String method) {
        
        return authenticationEnabled &&
        authMethods.containsKey(method.toLowerCase());
    }
    
     /**
      * Enable UDP processing (default enabled on 5060).
      * @param port Port on which to enable udp.
      */
    public void enableUDP( int port) {
        udpFlag = true;
        udpPort = port;
    }
    
    
      /**
       * Enable TCP processing (default disabled).
       * @param port port on which to listen for connections.
       */
    public void enableTCP(int port) {
        tcpFlag = true;
        tcpPort = port;
    }
    
        /**
         * Set the debug trace level. Note that this is not
         * part of the JAIN interfaces.
         *@param traceLevel trace level to set for the stack.
         */
    public void setTraceLevel(int level) {
        ServerLog.setTraceLevel(level);
    }
    
        /** Set the server log file name.
         */
    public void setServerLog(String name) {
        ServerLog.setLogFileName(name);
    }
    
        /**
         * Enable record route header automatically being added to messages
         * (default disabled).
         */
    public void enableRecordRoute() {
        recordRoute = true;
    }
    
        /** Get the record route flag.
         */
    public boolean getRecordRouteFlag() { return recordRoute; }
    
        /**
         * Set the transaction lifetime (default 1 second).
         *@param lifetime transaction lifetime (max).
         */
    public void setTransactionLifetime(int lifetime) {
        transactionLifetime = lifetime;
    }
    
        /** Set the stack address.
         */
    public void setStackAddress(String address) {
        stackAddress = address;
    }
    
    
    
    
    /**
     *  parse the configuration file and fill in the server config parms.
     * I should probably redo this using an XML specificiation.
     */
    
    protected void parseConfigFile(String configFileName) {
        int linenum = 0;
        try {
            BufferedReader bis =
            new BufferedReader( new FileReader(configFileName));
            
            if (bis == null) throw new IOException("Could not create reader!");
            while ( true) {
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
                    String portnum = st.nextToken();
                    int udpPort = Integer.parseInt(portnum);
		    if (udpPort <= 0) 
			throw new IllegalArgumentException("Bad udpPort");
                    this.enableUDP(udpPort);
                } else if (tok.compareTo
                ("disableAuthentication") == 0) {
                    this.disableAuthentication();
                } else if (tok.compareTo("enableTCP") == 0 ) {
                    String portnum = st.nextToken();
                    int tcpPort = Integer.parseInt(portnum);
		    if (tcpPort <= 0) 
			throw new IllegalArgumentException("Bad tcpPort");
                    this.enableTCP(tcpPort);
                } else if (tok.compareTo("router") == 0) {
                    String routerClassName = st.nextToken();
                    Class routerClass =
                    Class.forName(routerClassName);
                    Router router =
                    (Router) routerClass.newInstance();
                    this.setRouter(router);
                } else if (tok.compareTo
                ("authenticationMethod") == 0) {
                    // Class name of authentication Method
                    String authMethodName =
                    st.nextToken().toLowerCase();
                    String authMethodClassName =
                    st.nextToken();
                    Class authMethClass =
                    Class.forName(authMethodClassName);
                    AuthenticationMethod
                    authenticationMethod
                    = (AuthenticationMethod)
                    authMethClass.newInstance();
                    String passwordFile = st.nextToken();
                    authenticationMethod.initialize(passwordFile);
                    this.setAuthenticationMethod
                    (authMethodName,authenticationMethod);
                } else if (tok.compareTo
                ("defaultAuthenticationMethod") == 0) {
                    String  defaultAuthMethod =
                    st.nextToken().toLowerCase();
                    this.setDefaultAuthenticationMethod(defaultAuthMethod);
                } else if (tok.compareTo
                ("transactionLifetime") == 0) {
                    int transactionlifetime =
                    Integer.parseInt(st.nextToken());
                    this.setTransactionLifetime(transactionlifetime);
                } else if (tok.compareTo("serverLog") == 0    ){
                    String serverlog = st.nextToken();
                    this.setServerLog(serverlog);
                } else if (tok.compareTo("accessLogViaRMI") == 0  ){
                    accessLogViaRMI = true;
                } else if (tok.compareTo("rmiPort") == 0 ) {
                    rmiPort = Integer.parseInt(st.nextToken());
		    if (rmiPort <= 0) 
			throw new  IllegalArgumentException
			("Bad parameter: rmiPort");
                } else if (tok.compareTo("rmiRegistryPort") == 0 ) {
                    rmiRegistryPort = Integer.parseInt(st.nextToken());
		    if (rmiRegistryPort <= 0) 
			throw new  IllegalArgumentException
			("Bad parameter: rmiRegistryPort");
                } else if (tok.compareTo("traceLevel") == 0   ){
                    int traceLevel = Integer.parseInt(st.nextToken());
                    this.setTraceLevel(traceLevel);
                } else if (tok.compareTo("stackName") == 0     ){
                    String stackname = st.nextToken();
                    this.setStackName(stackname);
                } else if (tok.compareTo("stackHostName") == 0) {
                    String stackname = st.nextToken();
                    this.setStackHostName(stackname);
                }    else if (tok.compareTo("enableRecordRoute") == 0) {
                    this.enableRecordRoute();
                } else if (tok.compareTo("stackAddress")== 0   ){
                    String stackaddress = st.nextToken();
                    setStackAddress(stackaddress);
                } else if (tok.compareTo("parseSDP") == 0) {
                    enableParseSDP();
                } else if (tok.compareTo("transactionTimeout") == 0) {
                    transactionTimeout = Integer.parseInt(st.nextToken());
		    if (transactionTimeout <= 0) 
			throw new  IllegalArgumentException
			("Bad transactionTimeout");
                } else if (tok.compareTo("defaultRoute") == 0) {
                    String proxyAddr = st.nextToken();
                    this.setDefaultRoute(proxyAddr);
                } else {
                    throw new SipException
                    ("Invalid token in config file " +
                    ln + " Line " + linenum);
                }
                
            }
            if (authenticationRequired &&
            this.defaultAuthMethod == null)  {
                throw new Exception
                ("Auth enabled but defaultAuthMethod not specified");
            }
            if (accessLogViaRMI) {
                try {
                    if (ServerLog.getTraceLevel() < ServerLog.TRACE_MESSAGES)
                        this.setTraceLevel(ServerLog.TRACE_MESSAGES);
                    ServerLog.initMessageLogTable
                    (stackAddress,rmiRegistryPort,rmiPort, traceLifeTime);
                } catch (java.rmi.RemoteException ex) {
                    System.err.println
			("Please start RMI registry and " +
			 " make sure your classpath includes the stack!");
                    System.err.println("Exception :" + ex.getMessage());
                    ex.printStackTrace();
                }
            }

	    if (transactionLifetime <= transactionTimeout) 
		throw new IllegalArgumentException
			("transactionLifetime ("+transactionLifetime 
			  +") <= transactionTimeout "+ transactionTimeout );
        } catch (Exception ex) {
            System.err.println
            ("Error Parsing Config file : " + configFileName   +
             "\n line " + linenum + 
	     "\n errorMessage: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(0);
        }
    }
    
    
    /**
     *get the transaction lifetime.
     */
    protected int getTransactionLifetime() { return transactionLifetime; }
    
    
    /** Get the tiemout interval.
     */
    
    protected int getTransactionTimeout() { return transactionTimeout; }
    
    
    /** Set up our listening points.
     */
    private void initializeListeningPoints() {
        // If we are accepting messages through a tcp connection
        if (tcpFlag) {
            TCPMessageProcessor tcpMessageProcessor =
            this.tcpMessageProcessor;
            tcpListeningPoint =
            new ListeningPointImpl(tcpMessageProcessor);
            listeningPoints.add(tcpListeningPoint);
        }
        
        // If we are accepting messages through a udp connection.
        
        if (udpFlag) {
            UDPMessageProcessor udpMessageProcessor =
            this.udpMessageProcessor;
            udpListeningPoint =
            new ListeningPointImpl(udpMessageProcessor);
            listeningPoints.add(udpListeningPoint);
        }
    }
    
    
   /**
    * set up our defaults.
    */
    private void initializeStackDefaults() {
        
        udpPort = DEFAULT_UDP_PORT;
        tcpPort = 0;
        udpFlag = true;
        tcpFlag = false;
        // Enable authentication.
        authenticationRequired = true;
        stackName = DEFAULT_STACK_NAME;
        // Default authentication method is Digest.
        defaultAuthMethod = DEFAULT_AUTH_METHOD;
        serverLog = DEFAULT_SERVER_LOG;
        transactionLifetime = MAX_TRANSACTION_LIFETIME;
        
        accessLogViaRMI = false;
        rmiRegistryPort = DEFAULT_RMI_REGISTRY_PORT;
        rmiPort = DEFAULT_RMI_PORT;
        traceLifeTime = DEFAULT_TRACE_LIFETIME;
        
        transactionTimeout = DEFAULT_TRANSACTION_TIMEOUT;
        
        debugFlag = false;
        authenticationEnabled = true;
        
        
        messageChannelTable = new Hashtable();
        Thread timeOutThread = new Thread(this);
        timeOutThread.start();
    }
    
    
    
    /**
     *Constructor (note that we are using the NIST-SIP stack abstractions
     *here).
     *@param msgFactory is the NIST-SIP request and response factory impl.
     */
    public SipStackImpl(NistSipMessageFactoryImpl msgFactory)
    throws SipPeerUnavailableException {
        super(msgFactory);
        if (SipStackImpl.theStack != null )
            throw new SipPeerUnavailableException
            ("Only one stack instance is supported!");
        //Unfortunately, I have to make this global because some
        //of the headerFormatting stuff needs access to default parameters.
        SipStackImpl.theStack = this;
        authMethods = new Hashtable();
        // Initialize the transaction mapping table.
        clientTransactionTable = new Hashtable();
        serverTransactionTable = new Hashtable();
        sipProviders = new LinkedList();
        listeningPoints = new LinkedList();
        providerTable = new Hashtable();
        messageChannelTable = new Hashtable();
        
        this.initializeStackDefaults();
        this.parseConfigFile(CONFIGURATION_FILE_NAME);
        this.serverMain(null);
        this.initializeListeningPoints();
        
    }
    
    /**
     *Default constructor.
     *The configuration parameters are specified in the file "configfile"
     * in the directory where the application resides.
     */
    public SipStackImpl()
    throws SipPeerUnavailableException {
        super();
        if (SipStackImpl.theStack != null )
            throw new SipPeerUnavailableException
            ("Only one stack instance is supported!");
        //Unfortunately, I have to make this global, because
        //of the headerFormatting stuff needs access to default parameters.
        SipStackImpl.theStack = this;
        NistSipMessageFactoryImpl msgFactory =
        new NistSipMessageFactoryImpl(this);
        authMethods = new Hashtable();
        // Initialize the transaction mapping table.
        clientTransactionTable = new Hashtable();
        serverTransactionTable = new Hashtable();
        sipProviders = new LinkedList();
        listeningPoints = new LinkedList();
        providerTable = new Hashtable();
        messageChannelTable = new Hashtable();
        
        this.setStackMessageFactory(msgFactory);
        
        this.initializeStackDefaults();
        this.parseConfigFile(CONFIGURATION_FILE_NAME);
        this.serverMain(null);
        this.initializeListeningPoints();
        
    }
    
    
    
    /**
     * Method that scans queues looking for exipred transaction records and
     * generating transaction timeout events to the listening points.
     */
    
    public void run() {
        while (true) {
            LinkedList garbageList;
            synchronized (serverTransactionTable) {
                Collection values = serverTransactionTable.values();
                garbageList = new LinkedList();
                Iterator iterator = values.iterator();
                while(iterator.hasNext()) {
                    Transaction t = (Transaction) iterator.next();
                    if (t.isPendingDelete() || 
			t.hasTimedOut()) garbageList.add(t);
                }
            }
            Iterator iterator = garbageList.iterator();
            while(iterator.hasNext()) {
                Transaction t = (Transaction) iterator.next();
                t.handleTimeOut();
            }
            
            synchronized (clientTransactionTable) {
                /** Scan the client transaction table queue */
                Collection values = clientTransactionTable.values();
                garbageList = new LinkedList();
                iterator = values.iterator();
                while(iterator.hasNext()) {
                    Transaction t = (Transaction) iterator.next();
                    if (  t.isPendingDelete() || t.hasTimedOut()) 
			garbageList.add(t);
                }
            }
            iterator = garbageList.iterator();
            while(iterator.hasNext()) {
                Transaction t = (Transaction) iterator.next();
                t.handleTimeOut();
            }
            
            try {
                Thread.sleep(transactionTimeout * 1000);
            } catch (InterruptedException ex) {
                // do nothing.
            }
            
            
        }
        
    }
    
    
}
