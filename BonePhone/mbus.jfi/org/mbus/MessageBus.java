package org.mbus;
 
import java.util.*;

/**
 * This class defines the second Mbus-layer that allows an easier
 * usage of the MessageBus. This layer passes only those
 * incoming messages that it was configured to. Therefore you need
 * to tell this module what commands it should support.<p>
 *
 * The second and probably more important benefit of this layer is
 * support for synchronous communication. There are methods which
 * allow to send or broadcast a request and wait for a reply or
 * collect all replies within a certain time. For this feature to
 * work you need Mbus-commands that have an identifier of type
 * {@see org.tzi.mbus.MString MString} - in the query as well as the reply. 
 * This also must be configured before using the Mbus.
 * <p>
 * A third feature is that this module starts a thread for every
 * incoming message that isn't an outstanding reply for a query.
 * To make this work you need to give this module a factory that
 * produces <tt>IncomingMBusThread</tt>s.
 * <p>
 * Here is a example for a simple usage:<pre>
 *
 *   private static String[][] address = {{"app","mdb"},
 *                                        {"module", "gui"},
 *                                        {"media","h323"},
 *                                        {"id", "gui1"}};
 *   private static String[] notifications = {"conf.call-control.call",
 *                                            "conf.call-control.ringing",
 *                                            "conf.call-control.connected",
 *                                            "conf.call-control.rejected",
 *                                            "conf.call-control.disconnected",
 *                                            "conf.call-control.incoming-call"};
 *
 *   MessageBus mbus = new MessageBus(address);
 *   mbus.addCommands(notifications, -1);
 *   mbus.start();
 *
 *   MessageHandler myHandler = new MyMessageHandler(mbus);
 *
 * </pre>
 *
 * @author Stefan Prelle
 * @version $Id: MessageBus.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public class MessageBus extends Thread implements VirtualMBusListener {
    
    private final static boolean DEBUG = false;
    private final static boolean PROFILE = false;
    private final static boolean SPYABLE = true;
    
 
    // Taken from "MBus - Messages and Procedures" p.8
    private final static int T_HELLO  = 1000;
    private final static int T_DITHER = 100;
    private final static int N_DEAD   = 5;

    private static long TIMEOUT = 300;

    // Entity signals
    private static final int NEW_ENTITY  = 0;
    private static final int ENTITY_DIED = 1;
    private static final int ENTITY_SHUTDOWN = 2;

    // Used for virtual transport-layer
    public static VirtualTransportLayer virtLayer = null;

    private Address        myaddress, allTargets;
    private MessageHandler handler;
    private Hashtable      knownCommands;
    private boolean        ignoreHellos;
    private Hashtable      sendList;
    private Hashtable      waitForAckList;
    private Hashtable      values;
    private TransportLayer layer;
    /** The list of incoming messages */
    private Vector incoming;
    /** This variable is true, as long as the MBus runs */
    private boolean ever = true;

    // A few statistics
    private transient int sent_reliable = 0;
    private transient int undeliverable = 0;
    private transient int timeouts      = 0;
    private transient int received_total= 0;
    private transient int received_intime= 0;
    private transient int unused_replies= 0;
    private static transient Hashtable messageTimes;
    
    //------------------------------------------------------------
    /**
     * Start a new module that listens to MBus-Messages
     *
     * @param address The Mbus-Address of this entity
     * @param factory Class that produces specialized Threads for
     *                handling incoming messages
     */
    public MessageBus(String[][] address) throws MBusException {
	super("Unnamed MBus");
	handler = null;
	incoming = new Vector();
	
	// Build our own address
	for (int i=0; i<address.length; i++) 
	    if (address[i][0].equals("id"))
		address[i][1] = layer.getID();
	myaddress = new Address(address);
	log("MessageBus: My address = "+myaddress);

	// Create an MBus
	if (virtLayer==null) 
	    virtLayer = new VirtualTransportLayer(SPYABLE);
	virtLayer.addModule(this, myaddress);
	layer = virtLayer;
	
        String[][] voidArray = {};
        allTargets = new Address(voidArray);

        knownCommands=new Hashtable();
	addCommand("mbus.hello");

	ignoreHellos = true;
	sendList = new Hashtable();
	values   = new Hashtable();
	waitForAckList = new Hashtable();

	// For profiling-reasons
	if (messageTimes==null) 
	    messageTimes = new Hashtable();
	    
    }

    //------------------------------------------------------------
    /**
     * Start a new module that listens to MBus-Messages
     *
     * @param address The Mbus-Address of this entity
     * @param shared Set to true, if you want to use a shared MBus
     */
    public MessageBus(String[][] address, boolean shared) throws MBusException {
	super("Unnamed MBus");
	handler  = null;
	incoming = new Vector();

	// Build our own address
	address[3][1] = SimpleTransportLayer.getID();
	myaddress = new Address(address);
	log("MessageBus: My address = "+myaddress);

	// Create an MBus
	if (shared) {
	    if (virtLayer==null) 
		virtLayer = new VirtualTransportLayer(SPYABLE);
	    layer = virtLayer;
	    ((VirtualTransportLayer)layer).addModule(this, myaddress);
	} else {
	    Address[] ad = {myaddress};
	    layer = new SimpleTransportLayer(ad, this);
	}
	
        String[][] voidArray = {};
        allTargets = new Address(voidArray);

        knownCommands=new Hashtable();
	addCommand("mbus.hello");

	ignoreHellos = true;
	sendList = new Hashtable();
	values   = new Hashtable();

	// For profiling-reasons
	if (messageTimes==null) 
	    messageTimes = new Hashtable();
    }
    
    //---------------------------------------------------------------
    private void log(String mess) {
	if (DEBUG) {
	    System.out.println(System.currentTimeMillis()+" MBus/"+Thread.currentThread().getName()+": "+mess);
	}
    }
    
    //---------------------------------------------------------------
    public void shutdown() {
	ever = false;
	layer.close();
    }
    
    //---------------------------------------------------------------
    private void storeMessageAndTime(Command com, long time) {
	if (!PROFILE)
	    return;
	log("%%%%%%%%%%%%%% MBusMessage needed "+time+" milliseconds %%%%%%%%%%%%%%");	
	String cname = com.getCommand();
	if (cname.equals("mbus.poll")) {
	    MDataType[] param = com.getParameters();
	    cname+=  " "+((MSymbol)param[1]).toString();
	}

	Long[] tmp = null;
	Enumeration e = messageTimes.keys();
	while (e.hasMoreElements()) {
	    String key = (String)e.nextElement();
	    if (key.equals(cname)) {
		tmp = (Long[])messageTimes.get(key);
		if (time<tmp[0].longValue()) 
		    tmp[0]=new Long(time);
		if (time>tmp[1].longValue()) 
		    tmp[1]=new Long(time);
		messageTimes.put(key, tmp);
		System.out.println(getTimingDump());
		return;
	    }
	}
	tmp = new Long[2];
	tmp[0]=new Long(time);
	tmp[1]=new Long(time);
	messageTimes.put(cname, tmp);

	System.out.println(getTimingDump());
    }
    
    //---------------------------------------------------------------
    public String getTimingDump() {
	StringBuffer ret = new StringBuffer();
	
	String key;
	Long[] tmp;
	Enumeration e = messageTimes.keys();
	while (e.hasMoreElements()) {
	    key = (String)e.nextElement();
	    tmp = (Long[])messageTimes.get(key);
	    ret.append(key+" \t "+tmp[0]+"  "+tmp[1]+"\n");
	}
	return ret.toString();
    }    

    //------------------------------------------------------------
    /*
     * Set the class that should handle incoming messages.
     *
     * @param handler Handler-Class 
     */
    void attachMessageHandler(MessageHandler handler) {
	setName("MBus of "+handler.getClass().getName());
	this.handler = handler;
    }

    //------------------------------------------------------------
    public Address getAddress() {
	return myaddress;
    }

    //---------------------------------------------------------------
    /**
     * Add a new supported command to the MessageBus. Give the position
     * of the ID in the command.
     *
     * @param comm String with the MBus-Command
     * @param posID Position of parameter containing the ID. Negative
     *              values indicate that there is no ID.
     */
    public void addCommand(String comm, int posID) {
	synchronized (knownCommands) {
	    knownCommands.put(comm, new Integer(posID));
	}
	log(".. Adding command "+comm+((posID>-1)?(" with ID at position "+posID):""));
    }
    
    //---------------------------------------------------------------
    /**
     * Set the maximum of milliseconds we wait for replies.
     *
     * @param millis Timeout in milliseconds.
     */
    public void setTimeout(int millis) {
	TIMEOUT = millis;
    }
    
    //---------------------------------------------------------------
    /**
     * Set the maximum of milliseconds we wait for replies.
     *
     * @param millis Timeout in milliseconds.
     */
    public int getTimeout() {
	return (int)TIMEOUT;
    }
    
    //---------------------------------------------------------------
    /**
     * Add a new supported command that does not contain an ID to the
     * MessageBus.
     *
     * @param comm String with the MBus-Command
     */
    public void addCommand(String comm) {
	addCommand(comm, -1);
    }
    
    //---------------------------------------------------------------
    public void addCommands(String[] comm, int posID) {
	for (int i=0; i<comm.length; i++)
	    addCommand(comm[i], posID);
    }
    
    //---------------------------------------------------------------
    public void addCommands(String[] comm, int[] posID) {
	for (int i=0; i<comm.length; i++)
	    addCommand(comm[i], posID[i]);
    }
    
    //---------------------------------------------------------------
    public void addCommands(Object[][] comm) {
	for (int i=0; i<comm.length; i++) {
	    String com = (String)comm[i][0];
	    int    id  = ((Integer)comm[i][1]).intValue();
	    addCommand(com, id);
	}
    }
    
    //---------------------------------------------------------------
    public void setIgnoreHellos(boolean ignore) {
	ignoreHellos = ignore;
    }
    
    //---------------------------------------------------------------
    public Enumeration listKnownCommands() {
	return knownCommands.keys();
    }
    
    //---------------------------------------------------------------
    String dumpAllCommands() {
	StringBuffer buf = new StringBuffer("[");
	Enumeration e = knownCommands.keys();
	while (e.hasMoreElements()) {
	    buf.append(e.nextElement());
	    if (e.hasMoreElements())
		buf.append(",");
	}
	buf.append("]");
	return buf.toString();
    }
    
    //---------------------------------------------------------------
    protected Integer getIDFor(String command) {
	if (knownCommands==null)
	    return null;
	Enumeration keys = knownCommands.keys();
	while (keys.hasMoreElements()) {
	    String key = (String)keys.nextElement();
	    if (key.equals(command)) 
		return (Integer)knownCommands.get(key);
	}
	return null;
    }
    
    //---------------------------------------------------------------
    protected Boolean getCollectAll(String lock) {
	Enumeration keys = sendList.keys();
	while (keys.hasMoreElements()) {
	    String key = (String)keys.nextElement();
	    if (key.equals(lock)) 
		return (Boolean)sendList.get(key);
	}
	return null;
    }
    
    //---------------------------------------------------------------
    protected Vector getValueFor(String lock) {
	Enumeration keys = values.keys();
	while (keys.hasMoreElements()) {
	    String key = (String)keys.nextElement();
	    if (key.equals(lock)) 
		return (Vector)values.get(key);
	}
	return null;
    }
    
    //---------------------------------------------------------------
    protected String getLockObject(String lock) {
	Enumeration keys = values.keys();
	while (keys.hasMoreElements()) {
	    String key = (String)keys.nextElement();
	    if (key.equals(lock)) 
		return key;
	}
	return null;
    }
    
    //---------------------------------------------------------------
    /**
     * Deliver an incoming message into the input-queue and wake
     * the thread that waits on it. 
     *
     * @param m Incoming Message
     */
    public void incomingMessage(Message m) {
	synchronized (incoming) {
	    incoming.addElement(m);
	    incoming.notify();
	}
    }

    //---------------------------------------------------------------
    /**
     * As long as the MessageBus isn't stopped wait for new messages
     * in the input-queue and perform the commands.
     */
    public void run(){
	for (;ever;) {
	    try {
	    Message m;
	    int event = 0;
	    Address addr = null;
	    synchronized (incoming) {
		while (incoming.size()==0) {
		    try {
			incoming.wait();
		    } catch (InterruptedException ie) {
			System.err.println("MessageBus: Waiting interrupted.");
		    }
		}
		// Check if it was a message or a change concerning
		// connected entities
		Object o = incoming.firstElement();
		if (o instanceof Object[]) {
		    // It was an event
		    m = null;
		    Object[] oo = (Object[])o;
		    event = ((Integer)oo[0]).intValue();
		    addr  = (Address)oo[1];
		} else {
	 	    m = (Message)o;
		    event = -1;
		    addr  = null;
		}

		incoming.removeElementAt(0);
	    }
	    
	    if (m==null) {
		// Event
		log("Entity Event: "+event);
		switch (event) {
		case NEW_ENTITY     : handler.newEntity(addr); break;
		case ENTITY_DIED    : handler.entityDied(addr); break;
		case ENTITY_SHUTDOWN: handler.entityShutdown(addr); break;
		default:
		    log("Unknown event "+event+" for address "+addr);
		}
	    } else {
		
		// We are sure that we only receive messages that we
		// are valid receipents for.
		boolean isSupportedCommand = false;
		boolean isIncomingCommand  = true;
		
		Enumeration c = m.getCommands();
		while (c.hasMoreElements()) {		
		    Command comm = (Command)c.nextElement();
		    
		    String com = comm.getCommand();
		    // Was it a HELLO we want to Ignore?
		    if (com.equalsIgnoreCase("mbus.hello"))
			if (ignoreHellos)
			    break;
		    
		    // Let's check if it is a message we know
		    Integer withID = getIDFor(com);
		    if (withID==null) {
			log(" Received message with unsupported command:\n"+m);
			break;
		    }
		    // At least this command is supported
		    isSupportedCommand = true;
		    
		    // We know it - does it contain an ID?
		    int posID = withID.intValue();
		    if (posID>-1) {
			// Yes
			String lock = null;
			try {
			    lock = getLockObject(((MString)comm.getParameters()[posID]).getData());
			    // If it was an answer lock is != null.
			    if (lock!=null) isIncomingCommand = false;
			} catch (ArrayIndexOutOfBoundsException aib) {
			    System.err.println(com+" should have an Identifier as parameter "+(posID+1)+", but there aren't enough parameters.");
			    return;
			} catch (ClassCastException cce) {
			    System.err.println("Expected a MString as parameter "+(posID+1)+" but did find a "+comm.getParameters()[posID].getClass().getName());
			    return;
			}
			// Look for locking-object of the request
			// (if we find one - it was an answer)
			String t_id = null;
			Boolean collectAll = getCollectAll(lock);
			
			if (collectAll!=null) {
			    ((Vector)getValueFor(lock)).addElement(m);
			    if (collectAll.booleanValue()) {
				log(" We received an answer but we wait for further replies.");
			    } else {
				synchronized (lock) {
				    log("Received a reply we are waiting for");
				    lock.notify();
				} 
			    }
			    break;
			}
			// There was no thread waiting for this message - so
			// it was an incoming request
		    } // if (posID>-1)
		    
		}  // while hasMoreCommands
		
		// If it was an unexpected command we need to start a handler
		if (isIncomingCommand) {
		    
		    // When we are here we either received a message without
		    // an Identifier in it or it was an incoming request and 
		    // not an answer
		    if (isSupportedCommand) {
			try {
			    synchronized (knownCommands) {
				if (handler!=null) {
				    handler.handleMessage(m);
				} else
				    log(" Missing Handler - can't handle message");
			    } // synchronized knownCommands
			} catch (Exception e) {
			    e.printStackTrace();
			    System.err.println("MessageBus: continue operating");
			}
		    } else {
			//  		    log("No supported command in that message.");
		    }
		} // if isIncoming
	    }
	    } catch (Exception e) {
		e.printStackTrace();
		System.out.println("... continue operating");
	    } // try
	} // for (;ever;)
    }
                                                                      
    //---------------------------------------------------------------
    public void deliveryFailed(int seqnum, Message mess){
        log(" Message "+seqnum+" could not be delivered. ");
	handler.deliveryFailed(seqnum, mess);
    }
                                                                      
    //---------------------------------------------------------------
    public void deliverySuccessful(int seqNums, Message mess){
	log("MB.deliverySuccessful..... "+seqNums);
	log("MB.deliverySuccessful..... "+waitForAckList);
//  	for (int i=0; i<seqNums.length; i++) log("MB.deliverySuccessful: "+seqNums[i]);
	// Make sure that no other thread is putting his data
	// in the list
	boolean found = false;
	synchronized (waitForAckList) {		
	    Enumeration e = waitForAckList.keys();
	    while (e.hasMoreElements()) {
		Integer sn = (Integer)e.nextElement();
		// Does anybody of the ACKs match this number ?
		//  	    for (int i=0; i<seqNums.length; i++)
		log(".. Does "+sn.intValue()+" match "+seqNums+" ?");
		if (sn.intValue()==seqNums) {
		    // Yes
		    log("MB.deliverySuccessful ..... put in AckList -"+waitForAckList);
		    waitForAckList.put(sn, new Boolean(true));
		    log("MB.deliverySuccessful ..... notify - "+waitForAckList);
		    synchronized (sn) {
			sn.notify();
		    }
		    found = true;
		    log("MB.deliverySuccessful2..... after notify - "+waitForAckList);
		}
	    }
	} // synchronized
	if (!found)
	    log("UNEXPECTED ACKNOWLEDGEMENT!");
    }
                                                            
    //---------------------------------------------------------------
    public int send (Command com, Address target, boolean reliable) {
	log("*********> Send to "+target+": "+com);
	return layer.send(com, myaddress, target, reliable);
    }
                                                            
    //---------------------------------------------------------------
    /**
     * Sends a reliable message to the contorl-module.
     *
     * @param com Command to send
     */
    public void sendToControl (Command com) {
	String[][] target = {{"media","control"}};
	Address destGrp = new Address(target);
	if (DEBUG) {
	    Date dat = new Date();
	    StringBuffer buf = new StringBuffer("**> "+dat.getTime());
	    buf.append(": FROM "+myaddress);
	    buf.append(" TO "+destGrp);
	    buf.append(" COMMAND "+com.getCommand());
	    System.out.println(buf.toString());
	}
	layer.send(com, myaddress, destGrp, false);
    }
                                                            
    //---------------------------------------------------------------
    /**
     * This method model helps modeling synchronized communication on
     * the Message Bus. Basically it sends a command containing an 
     * identifier and waits for incoming messages containing the same
     * identifier. The exact behavoir depends on the parameter 
     * <i>collectAll</i>.<ul>
     * <li><b>collectAll = false</b><br>The method blocks until the
     * first reply is received and then returns with an one-element-array
     * containing the received message.
     * <li><b>collectAll = true</b><br>The method blocks for some time
     * specified by the {@link #setTimeout(int) setTimeout}-Method of 
     * this module.  After this all replies to the Query-Command are 
     * collected and returned.
     * </ul>
     * <table width="100%"><tbody><tr><td bgcolor="#c0c0c0"><i>
     * <font size="+1"><b>Note!</b></font>Usually you don't use this
     * method directly but use 
     * {@link #sendAndWaitForReply(org.mbus.Command,org.mbus.Address,boolean) sendAndWaitForReply} or
     * {@link #broadcastAndCollectReplies(org.mbus.Command) broadcastAndCollectReplies} instead.
     * </i></td></tr></tbody></table>
     *
     * @param com Command to send - must be known to this module.
     * @param target MBus-Targetaddress
     * @param reliable Shall message be sent reliable
     * @param collectAll See above
     * @exception MBusException An error occured that made it impossible
     *            to send a query-command that allows replies or to evaluate
     *            the answers. This is most probably a misconfiguration
     *            of this module.
     */
    public Message[] sendAndWaitForReplies(Command com, Address target, boolean reliable, boolean collectAll) throws MBusException{
	// For performance measuring
	long start = (new Date()).getTime();
	
	Integer pos = getIDFor(com.getCommand());
	if (pos==null)
	    throw new MBusException(com.getCommand()+" doesn't have any Identifier that allows replies.");
	
	int posID = pos.intValue();
	String lock = null;
	try {
	    lock = ((MString)com.getParameters()[pos.intValue()]).getData();
	    sendList.put(lock, new Boolean(collectAll));
	    values.put(lock, new Vector());
	    synchronized(lock) {
		try {
		    if (DEBUG) {
			Date dat = new Date();
			StringBuffer buf = new StringBuffer("**> "+dat.getTime());
			buf.append(": FROM "+myaddress);
			buf.append(" TO "+target);
			buf.append(" COMMAND "+com.getCommand());
			log(buf.toString());
		    }
		    layer.send(com, myaddress, target, reliable);
		    lock.wait(TIMEOUT);
		} catch (InterruptedException ie) {
		    if (DEBUG) System.out.println("Insomnia");
		    ie.printStackTrace();
		}
	    }
	} catch (ClassCastException cce) {
	    throw new MBusException("Couldn't find ID in answer: Wrong datatype");
	} catch (ArrayIndexOutOfBoundsException aeb) {
	    throw new MBusException("Couldn't find ID in answer: Not enough parameters.");
	}
	synchronized (sendList) {
	    sendList.remove(lock);
	}
	synchronized (values) {
	    Vector o = getValueFor(lock);
	    
	    if (o.size()==0)
		log("No answer for broadcasted "+com.getCommand()+" (ID="+lock+") within "+TIMEOUT+" milliseconds");
	    else 
		log("Got reply from MBus");
	    Message[] ret = new Message[o.size()];
	    for (int i=0; i<ret.length; i++)
		ret[i] = (Message)o.elementAt(i);
	    // For performance-measuring
	    long diff = (new Date()).getTime()-start;
	    storeMessageAndTime(com, diff);
	    return ret;
	}
    }
                                                            
    //---------------------------------------------------------------
    /**
     * Models synchronous communication through sending a message and
     * waiting for the first answer and returning these answer. Both -
     * the request and the reply-command need to have an identifier 
     * field that was made known to this module on startup.
     *
     * @param com Command to send
     * @param target MBus-Targetaddress
     * @param reliable Shall message be sent reliable
     * @exception MBusException An error occured that made it impossible
     *            to send a query-command that allows replies or to evaluate
     *            the answers. This is most probably a misconfiguration
     *            of this module.
     * @exception TimeoutException There was no reply within the given
     *            timeout.
     */
    public Message sendAndWaitForReply(Command com, Address target, boolean reliable) throws MBusException, TimeoutException{
	Message[] foo = sendAndWaitForReplies(com, target, reliable, false);
	if (foo.length==0) {
	    // No replies in time
	    timeouts++;
	    throw new TimeoutException("No answer within "+TIMEOUT+" milliseconds.");
	}
	received_intime++;
	return foo[0];
    }
    
                                                            
    //---------------------------------------------------------------
    /**
     * Sends a reliable message and blocks until the destination indicates
     * the receipt of the message.
     *
     * @param com Command to send
     * @param target MBus-Targetaddress
     * @return true, if delivery was successful
     */
    public boolean sendAndWaitForReceipt(Command com, Address target) {
	// To prevent that an ack is received before the sequence
	// number is put in the list lock it
	Integer seqnum = null;
	synchronized (waitForAckList) {
	    seqnum = new Integer(layer.send(com, myaddress, target, true));
	    waitForAckList.put(seqnum, new Boolean(false));
	}
	// Wait for a reply
	log("sendAndWaitForReceipt1: AckList = "+waitForAckList);
	log("sendAndWaitForReceipt1: Wait now "+TIMEOUT+" milliseconds.");
	synchronized (seqnum) {
	    try {
		seqnum.wait(TIMEOUT);
	    } catch (InterruptedException ie) {
		System.err.println("Waiting for receipt-confirmation interrupted.");
	    }
	}
	log("sendAndWaitForReceipt2: AckList = "+waitForAckList);
	// What reply did we get ?
	boolean ret = ((Boolean)waitForAckList.get(seqnum)).booleanValue();
	waitForAckList.remove(seqnum);
	log("sendAndWaitForReceipt3: ------------------> "+ret);
	return ret;
    }
    
    //---------------------------------------------------------------
    /**
     * Broadcasts a message to all MBus-entities.
     *
     * @param com Command to broadcast
     */
    public void broadcast(Command comm) {
	layer.broadcast(myaddress, comm);
    }
                                                            
    //---------------------------------------------------------------
    /**
     * Models some kind of synchronous broadcast-communication through 
     * sending a message and collecting all replies within the time
     * defined by {@link #setTimeout(int) setTimeout}. Both - the
     * request and the reply-command need to have an identifier field
     * that was made known to this module on startup.
     *
     * @param com Command to broadcast
     * @return Array of all replies received. May be empty.
     * @exception MBusException An error occured that made it impossible
     *            to send a query-command that allows replies or to evaluate
     *            the answers. This is most probably a misconfiguration
     *            of this module.
     */
    public Message[] broadcastAndCollectReplies(Command comm) throws MBusException {
	return sendAndWaitForReplies(comm, allTargets, false, true);
    }

    //---------------------------------------------------------------
    /**
     * Replies to a MBus-Request
     *
     * @param reply Reply
     * @param req   Original Request
     */
    public void sendReply(Message req, Command reply) {
        send(reply, req.getHeader().getSourceAddress(), false);
    }
                                                            

    //---------------------------------------------------------------
    public String getStatistics() {
	StringBuffer tmp = new StringBuffer();
	tmp.append("\nStatistics for "+TIMEOUT+" ms Timeout");
	tmp.append("\n=============================");
	tmp.append("\nReliable messages sent: "+sent_reliable);
	tmp.append("\n         Undeliverable: "+undeliverable);
	tmp.append("\nQuery replies received: "+received_total);
	tmp.append("\n               In time: "+received_intime);
	tmp.append("\n              TimeOuts: "+timeouts);
	tmp.append("\nReplies that came late: "+unused_replies);
	return tmp.toString();
    }

    //---------------------------------------------------------------
    public void setWiretapping(boolean wiretapping) {
	layer.setWiretapping(wiretapping);
    }

    //---------------------------------------------------------------
    public MString generateTransactionID() {
	int t_id = Calendar.getInstance().get(Calendar.SECOND)*1000+
	    Calendar.getInstance().get(Calendar.MILLISECOND);
	return new MString(""+t_id);
    }

    //---------------------------------------------------------------
    public void newEntity(Address addr) {
	Object[] tmp = {new Integer(NEW_ENTITY), addr};
	synchronized (incoming) {
	    incoming.addElement(tmp);
	    incoming.notify();
	}
//  	handler.newEntity(addr);
    }

    //---------------------------------------------------------------
    public void entityDied(Address addr) {
	Object[] tmp = {new Integer(ENTITY_DIED), addr};
	synchronized (incoming) {
	    incoming.addElement(tmp);
	    incoming.notify();
	}
//  	handler.entityDied(addr);
    }

    //---------------------------------------------------------------
    public void entityShutdown(Address addr) {
	Object[] tmp = {new Integer(ENTITY_SHUTDOWN), addr};
	synchronized (incoming) {
	    incoming.addElement(tmp);
	    incoming.notify();
	}
//  	handler.entityShutdown(addr);
    }

    //---------------------------------------------------------------
    public String toString() {
	return getName();
	
//  	if (handler!=null)
//  	    return "MBus with "+handler.getClass().getName();
//  	return "MBus without handler";
    }

    //------------------------------------------------------------
    /**
     * Returns a list of entities that are currently on the MBus.
     *
     *  @return Addresses of the entites
     */
    public Address[] getKnownEntities() {
	return layer.getKnownEntities();
    }

} // MessageBus
