package tools.responder;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Hashtable;
import java.net.*;
import gov.nist.sip.*;
import gov.nist.sip.stack.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.stack.security.*;
import java.util.Collection;
import java.util.Iterator;


/**
 *This is the main event engine. It constructs a call flow graph by reading the
 *XML specification that it is handed and implements a mesage processor
 *via the SIPStack that it extends.
 */

public class EventEngine extends SIPStack implements Runnable {
    
    protected static final int DEFAULT_REPEAT_INTERVAL = 2;
    protected static final String DEFAULT_LOG_FILE = "serverlog";
    protected static final int DEFAULT_LOG_LEVEL = 0;
    
    protected static EventEngine theStack;
    protected static String eventFile;
    protected static String agentFile;
    protected static String peerAddress;
    protected static MessageChannel defaultChannel;
    protected static String   requestURI; // URI for requests originating here
    protected static String   proxyURI;    // URI for the proxy
    protected static Hashtable transactionTable;
    protected static LinkedList deferredSendList;
    
    /** Configuration parameters **/
    protected static int repeatInterval = DEFAULT_REPEAT_INTERVAL;
    protected static String logFileName = DEFAULT_LOG_FILE;
    protected static int    logLevel = DEFAULT_LOG_LEVEL;
    
        /** A table of call flows (indexed by callID etc.) **/
    protected Hashtable callFlowTable;
    
    private static void setDefaultChannel()
    throws Exception {
        Hop defaultRoute = theStack.getDefaultRoute();
        String host = defaultRoute.getHost();
        int port = defaultRoute.getPort();
        String transport = defaultRoute.getTransport();
        
        InetAddress hostAddr = InetAddress.getByName(host);
        Debug.println("default channel " + hostAddr
        + ":" + port + "/" + transport);
        if (transport.equals(SIPKeywords.TCP)){
            defaultChannel =
            new TCPMessageChannel(hostAddr,port,theStack,null);
        } else {
            
            defaultChannel =
            new UDPMessageChannel(hostAddr,port,theStack);
        }
        
    }
    
      
         /** Get the default channel.
          */
    protected MessageChannel getDefaultChannel() {
        return defaultChannel;
    }
        /** Constructor.
         */
    
    public EventEngine(MessageFactoryImpl messageFactory) {
        super(messageFactory);
        callFlowTable = new Hashtable();
        transactionTable = new Hashtable();
        deferredSendList = new LinkedList();
        Thread mythread = new Thread(this);      
    }
    
    
        /** Remove a transaction given a tid.
         */
    protected synchronized void removeTransaction(String tid) {
	if (ServerLog.needsLogging()) 
	    ServerLog.logMessage("Removing " + tid);
	Object retval = transactionTable.get(tid);
	if (retval == null) {
	     if (ServerLog.needsLogging()) 
		 ServerLog.logMessage("Not found " + tid );
	     return;
	}

	transactionTable.remove(tid);
	Transaction transaction = (Transaction) retval;

	String id = transaction.transactionID;
	transactionTable.remove(id);

	id = transaction.cancelID;
	transactionTable.remove(id);
    }
    
        /**
         *Get the call flow graph for a given callID.
         *@param callId is the callID for which we want to get the
         * call flow graph.
         */
    
    protected synchronized  CallFlow getCallFlow(String id ) {
	Debug.println("Getting call flow for " + id);
        CallFlow retval = (CallFlow) callFlowTable.get(id);
        if (retval != null) {
	   Debug.println("Got Call flow ");
	   return retval;
	}
        XMLContentHandler ch = new XMLContentHandler
        (eventFile,agentFile);
        CallFlow callFlow =  ch.getCallFlow();
        // When do we clean out this table??
        callFlowTable.put(id,callFlow);
        return callFlow;
        
    }
    
        /** Put a new call flow spec into our call flow table.
         */
    
    protected synchronized void putCallFlow(String id, CallFlow cflow) {
        callFlowTable.put(id,cflow);
    }
    
        /**
         *Authentication is not enabled for this stack.
         */
    public AuthenticationMethod getAuthMethod( String name) {
        return null;
    }
    
        
        /** Get the transaction repeat interval. 
         */
    protected int getRepeatInterval() {
        return this.repeatInterval;
    }
        /** returns the default authentication method (null)
         */
    
    public String getDefaultAuthMethodName() { return null; }
    
        /**
         *Main entry point.
         */
    public static void main(String[] args) {
        
        
        theStack = new EventEngine(new MessageFactoryImpl());
        
        
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-eventSpec")) {
                    eventFile = args[i+1];
                    i++;
                } else if (args[i].equals("-agentSpec")) {
                    agentFile = args[i+1];
                    i++;
                } else if (args[i].equals("-enableTCP")) {
                    String portString = args[i+1];
                    int port = Integer.parseInt(portString);
                    theStack.setTCPPort(port);
                    i++;
                } else if (args[i].equals("-enableUDP")) {
		    System.out.println("-enableUDP");
                    String portString = args[i+1];
                    int port = Integer.parseInt(portString);
		    System.out.println(">>>>>UDP enabled on " + port);
                    theStack.setUDPPort(port);
                    i++;
                } else if (args[i].equals("-stackAddress")) {
                    String stackAddress = args[i+1];
                    theStack.setHostAddress(stackAddress);
                    i++;
                } else if (args[i].equals("-stackName")) {
                    String stackHostName = args[i+1];
                    theStack.setHostName(stackHostName);
                    i++;
                } else if ( args[i].equals("-peerAddress") ) {
                    // This is either the proxy address or the address
                    // of another peer (such as a peer UserAgent) that
                    // we want to test. This has to have the form
                    // ipAddress:PORT/TRANSPORT
                    peerAddress = args[i+1];
                    theStack.setDefaultRoute(peerAddress);
                    theStack.setDefaultChannel();
                    Hop hop = theStack.getDefaultRoute();                    
                    EventEngine.proxyURI = 
                            "sip:"+hop.getHost()+":"+hop.getPort();
                    
                    i++;
                } else if (args[i].equals("-requestURI")) {
                    requestURI = args[i+1];
                    i++;
                } else if (args[i].equals("-traceLevel")) {
                    logLevel = Integer.parseInt(args[i+1]);
                    i++;
                } else if (args[i].equals("-logFile")) {
                    logFileName = args[i + 1]; i++;
                  
                } else if (args[i].equals("-repeatInterval")) {
                    repeatInterval = Integer.parseInt(args[i+1]);
                    i++;
                } else {
                    System.out.println("Unexpected switch " + args[i]);
                    System.exit(0);
                }
                
            }
            if (peerAddress == null) {
                System.out.println("please specify peer address");
                System.exit(0);
            }
	    if (agentFile == null) {
		System.out.println("Please specify agentSpec");
		System.exit(0);
	    }

	    if (eventFile == null) {
		System.out.println("Please specify eventSpec");
		System.exit(0);
	    }
            ServerLog.setLogFileName(logFileName);
            ServerLog.setTraceLevel(logLevel);
            // See if there are new calls to be generated.
            // Should run this in a separate sending thread...
            XMLContentHandler xmlContentHandler =
            new XMLContentHandler(eventFile, agentFile);
            CallFlow callFlow = xmlContentHandler.getCallFlow();
            LinkedList messages = callFlow.checkStartNodes();
            ListIterator li = messages.listIterator();
            callFlow.sendMessages(li);
            // Start the threads of the server.
            Thread mythread = new Thread(EventEngine.theStack);
            mythread.start();
            theStack.serverMain(null);
            
        } catch ( Exception ex) {
            System.err.println("Bad command line parameter ");
            ex.printStackTrace();
            System.exit(0);
            
        }
        if (eventFile == null) {
            System.err.println("specify -eventSpec eventSepcFile");
            System.exit(0);
        }
        
        
    }
    
    protected synchronized void putTransaction
    (Transaction transaction) {
        this.transactionTable.put(transaction.transactionID,transaction);
	this.transactionTable.put(transaction.cancelID,transaction);
	if (ServerLog.needsLogging()) 
	   ServerLog.logMessage("putTransaction TID = " + 
				transaction.transactionID
				+ "\nCID = " + 
				transaction.cancelID);
    }

    protected synchronized Transaction getTransaction(String tid) {
		return (Transaction) this.transactionTable.get(tid);
    }
    
    /** Put a retransmission record in the retransmission table */
    protected synchronized void putDeferredSend(DeferredSend send) {
	Debug.println("putDeferredSend ");
        this.deferredSendList.add(send);
    }
    
    
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                continue;
            }
            synchronized(this) {
                // Debug.println("Transaction thread is running!");
                // Scan the transaction table and re-process transactions
                // that have timed out.
                ListIterator iterator = deferredSendList.listIterator();

		LinkedList deleteList = new LinkedList();
                
                while(iterator.hasNext()) {
                    DeferredSend dsend  = (DeferredSend) iterator.next();
                    String msg = dsend.getMessageToSend();
                    CallFlow cflow = dsend.callFlow;
                    if (msg != null) {
			Debug.println("Adding to send from the deferred list");
			cflow.addToSend(msg);
			deleteList.add(dsend);
		    }
                }
		iterator = deleteList.listIterator();
		while(iterator.hasNext()) {
			DeferredSend dsend = (DeferredSend) iterator.next();
			deferredSendList.remove(dsend);
		}

                Collection values = transactionTable.values();
                Iterator it = values.iterator();
                while(it.hasNext()) {
                    Transaction transaction = (Transaction) it.next();
                    String msg = transaction.getMessage();
                    Debug.println("Getting " + msg);
                    if (msg != null) {
                        CallFlow cflow = transaction.callFlow;
                        cflow.addToSend(msg);
                    }
                }
                values = callFlowTable.values();
                it = values.iterator();
                deleteList = new LinkedList();
                while(it.hasNext()) {
                    CallFlow callFlow = (CallFlow) it.next();
                    try {
                        callFlow.sendMessages();
                    } catch (SIPServerException ex) {
                        deleteList.add(callFlow);
                    }
                }
                
                // Remove the flows for which there was a problem sending.
                it = deleteList.listIterator();
                while(it.hasNext()) {
                    CallFlow cf = (CallFlow) it.next();
                    callFlowTable.remove(cf);
                }
                
            }
        }
    }
    
}
