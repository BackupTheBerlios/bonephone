package org.mbus;

import java.util.*;
import java.net.*;
import java.io.*;
import java.security.*;

/**
 * This class represents a single mbus entity. Mbus-applications
 * use this, to communicate via mbus.
 * <p>
 * The SimpleTransportLayer reads the <tt>.mbus</tt>-File from the
 * user's home-directory. If the following system-properties are
 * set they override the data read from the file:
 * <ul>
 * <li><tt>mbus.HASHKEY</tt> 
 * <li><tt>mbus.ENCRYPTIONKEY</tt>  
 * <li><tt>mbus.PRESENCE</tt> (unused)
 * <li><tt>mbus.SCOPE</tt> - LINKLOCAL or HOSTLOCAL (default: HOSTLOCAL)
 * <li><tt>mbus.PORT</tt> - (default: 47000)
 * <li><tt>mbus.ADDRESS</tt> - (default: 224.255.222.239)
 * <li><tt>mbus.CHECK_DIGEST</tt> - enable digestchecking (default: true)
 * <li><tt>mbus.SEND_UNICAST</tt> - enable unicast optimization  (default: true)
 * <li><tt>mbus.TIMER_R</tt> - Milliseconds between message retransmissions (default: 100)
 * <li><tt>mbus.RETRIES</tt> - number of retries to retransmit a message (default: 3)
 * </ul>
 *
 * @authorStefan Prelle
 * @version $Revision: 1.1 $ $Date: 2002/02/04 13:23:34 $
 */


public class SimpleTransportLayer extends TransportLayer implements ReceiverListener, RetransmitterListener {

    // Taken from "MBus - Messages and Procedures" p.8
    final static int T_HELLO  = 1000;
    final static int T_DITHER = 100;
    final static int N_DEAD   = 5;
    
    private final static boolean DEBUG= false;

    // Configuraton
    private MBusConfiguration config;
    private Crypter           crypter;
    private Authenticator     authenticator;

    private InetAddress     ipAddress; 
    private MulticastSocket mcastSocket; 
    private MulticastSocket socket; 
    private MBusListener    listener;
    private Vector          myAddresses;
    private Hashtable       AckQueue;
    
    private Receiver  mcReceiver, dgReceiver;
    private Announcer announcer;
    private Address   allTargets;

    private String userhome=System.getProperty("user.home");

    private AgingThread    ager;
    private Hashtable      knownTargets, unicast;

    /** Logging-entities */
    Vector inLogger, outLogger, genLogger;

    //------------------------------------------------------------
    /**
     * Create a new TransportLayer 
     *
     * @param interface Address the transport-layer shall listen to
     * @param listener  Class that shall receive the messages
     * @exception MBusException An error occured initializign the MBus.
     */
    public SimpleTransportLayer(Address interf, MBusListener listener) throws MBusException {
	try {
	    Address[] ad = {interf};
	    init(ad, listener);
	} catch (FileNotFoundException fnf) {
	    if (true) fnf.printStackTrace();
	    throw new MBusException(fnf.toString());
	} catch (MBusParsingException mpe) {
	    if (true) mpe.printStackTrace();
	    throw new MBusException(mpe.getMessage());
	} catch (UnknownHostException uhe) {
	    if (true) uhe.printStackTrace();
	    throw new MBusException("Invalid multicastgroup: "+uhe.getMessage());
	} catch (IOException ioe) {
	    if (true) ioe.printStackTrace();
	    throw new MBusException(ioe.toString());
	} catch (NoSuchAlgorithmException nsa) {
	    if (true) nsa.printStackTrace();
	    throw new MBusException("Unknown or wrong spelled Algorithm: "+nsa.getMessage());
	}
    }

    //------------------------------------------------------------
    /**
     * Create a new TransportLayer 
     *
     * @param interfaces List of addresses the transport-layer shall 
     *                   listen to
     * @param listener  Class that shall receive the messages
     * @exception MBusException An error occured initializign the MBus.
     */
    SimpleTransportLayer(Address[] interfaces, MBusListener listener) throws MBusException {
	try {
	    init(interfaces, listener);
	} catch (FileNotFoundException fnf) {
	    if (true) fnf.printStackTrace();
	    throw new MBusException(fnf.toString());
	} catch (MBusParsingException mpe) {
	    if (true) mpe.printStackTrace();
	    throw new MBusException(mpe.getMessage());
	} catch (UnknownHostException uhe) {
	    if (true) uhe.printStackTrace();
	    throw new MBusException("Invalid multicastgroup: "+uhe.getMessage());
	} catch (IOException ioe) {
	    if (true) ioe.printStackTrace();
	    throw new MBusException(ioe.toString());
	} catch (MBusException me) {
	    if (true) me.printStackTrace();
	    throw me;
	} catch (NoSuchAlgorithmException nsa) {
	    if (true) nsa.printStackTrace();
	    throw new MBusException("Unknown or wrong spelled Algorithm: "+nsa.getMessage());
	}
    }

    //---------------------------------------------------------------
    private void log(String mess) {
	if (DEBUG) {
	    System.out.println(System.currentTimeMillis()+" STL/"+Thread.currentThread().getName()+": "+mess);
	}
    }

    //---------------------------------------------------------------
    public void addIncomingLogger(IncomingLogger il) {
	inLogger.addElement(il);
    }

    //---------------------------------------------------------------
    public void addOutgoingLogger(OutgoingLogger ol) {
	outLogger.addElement(ol);
    }

    //---------------------------------------------------------------
    public void addGeneralLogger(GeneralLogger gl) {
	genLogger.addElement(gl);
    }

    //---------------------------------------------------------------
    public void removeIncomingLogger(IncomingLogger il) {
	inLogger.removeElement(il);
    }

    //---------------------------------------------------------------
    public void removeOutgoingLogger(OutgoingLogger ol) {
	outLogger.removeElement(ol);
    }

    //---------------------------------------------------------------
    public void removeGeneralLogger(GeneralLogger gl) {
	genLogger.removeElement(gl);
    }

    //---------------------------------------------------------------
    public void logIncoming(Message m, InetAddress ip, int port) {
	Enumeration e = inLogger.elements();
	while (e.hasMoreElements()) {
	    IncomingLogger il = (IncomingLogger)e.nextElement();
	    if (m.isHello()) 
		il.helloReceived(m, ip, port);
	    else if (m.isBye()) 
		il.byeReceived(m, ip, port);
	    else 
		il.messageReceived(m, ip, port);
	}   
    }

    //---------------------------------------------------------------
    public void logOutgoing(Message m, InetAddress ip, int port) {
	Enumeration e = outLogger.elements();
	while (e.hasMoreElements()) {
	    OutgoingLogger ol = (OutgoingLogger)e.nextElement();
	    if (m.isHello()) 
		ol.helloSent(m, ip, port);
	    else if (m.isBye()) 
		ol.byeSent(m, ip, port);
	    else 
		ol.messageSent(m, ip, port);
	}   
    }

    //---------------------------------------------------------------
    public void logGeneral(String text) {
	Enumeration e = genLogger.elements();
	while (e.hasMoreElements()) {
	    ((GeneralLogger)e.nextElement()).logMessage(text);
	}   
    }

    //------------------------------------------------------------
    /**
     * Initialize TransportLayer 
     *
     * @param interfaces List of addresses the transport-layer shall 
     *                   listen to
     * @param listener   Class that shall receive the messages
     * @exception java.io.FileNotFoundException Could not find the
     *            .mbus-file
     * @exception java.io.IOException Error parsing .mbus-file
     * @exception java.security.NoSuchAlgorithmException One of the
     *            algorithms in the MBus-File is unknown (or not
     *            spelled correct)
     * @exception MBusParsingException Error parsing .mbus-file
     * @exception MBusException Error setting keys
     */
    void init(Address[] interfaces, MBusListener listener) 
		throws FileNotFoundException, IOException, NoSuchAlgorithmException, MBusParsingException, MBusException {

	this.listener=listener;
	knownTargets=new Hashtable();
	unicast     =new Hashtable();
	AckQueue    =new Hashtable();
	myAddresses = new Vector();

	inLogger = new Vector();
	outLogger= new Vector();
	genLogger= new Vector();
	
	// Choose instance-ids for interfaces
	for (int i=0; i<interfaces.length; i++)
	    interfaces[i].setID(getID());

	config = new MBusConfiguration();
//    	if (true) System.out.println(config);

	// Create encryption-unit
	crypter = new Crypter(config);

	// Create Authentication
	authenticator = new Authenticator(config);
	
	// Create broadcast-address
	String[][] voidArray={};
	allTargets=new Address(voidArray);

	// Create Multicast-Socket
	mcastSocket = new MulticastSocket(config.getPort());
	mcastSocket.joinGroup(config.getMulticastGroup());

	// Create Datagram-Socket
	socket = new MulticastSocket();

	// Prepare to receive data
	dgReceiver = new Receiver(this, interfaces, socket, authenticator, crypter);
	dgReceiver.start();
	mcReceiver = new Receiver(this, interfaces, mcastSocket, authenticator, crypter);
	mcReceiver.start();

	// Start announcing our presence
	announcer = new Announcer(this, interfaces);
	announcer.start();

	ager = new AgingThread(this);
	ager.start();

	log(config.toString());

    }

    //------------------------------------------------------------
    /**
     * Enable or disable the Wiretapping-mode of the receiver.
     *
     * @param wiretapping Wiretapping-mode on?
     */
    public void setWiretapping(boolean wiretapping) {
	mcReceiver.setWiretapping(wiretapping);
	dgReceiver.setWiretapping(wiretapping);
	logGeneral(wiretapping?"Enable":"Disable"+" wiretapping.");
    }


	//------------------------------------------------------------
	/** 
	 * Enable or disable the packet dump mode of the receiver.
	 *
	 * @param dump Packet dump on/off
	 */
	public void setPacketDump(boolean dump) {
		mcReceiver.setDump(dump);
		dgReceiver.setDump(dump);
	}


    //------------------------------------------------------------
    void addInterface(Address addr) {
	log("Add interface: "+addr);
	logGeneral("Add interface: "+addr);
	myAddresses.addElement(addr);
	dgReceiver.addInterface(addr);
	mcReceiver.addInterface(addr);
	announcer.addInterface(addr);
    }

    //------------------------------------------------------------
    void removeInterface(Address addr) {
	log("Remove interface: "+addr);
	logGeneral("Remove interface: "+addr);
	for (int i=0; i<myAddresses.size(); i++) {
	    Address ad = (Address)myAddresses.elementAt(i);
	    if (ad.equals(addr)) {
		myAddresses.removeElement(ad);
		return;
	    }
	}
	mcReceiver.removeInterface(addr);
	dgReceiver.removeInterface(addr);
	announcer.removeInterface(addr);
    }

    //------------------------------------------------------------
    /**
     * Send a Message. Only for internal use, because the sequence number is
     * given by the mbus itself.
     */
    public int send(Message m){
//		System.out.println("Send "+m.getHeader().getSequenceNumber());
		if (true) {
		    Enumeration e= m.getCommands();
		    while (e.hasMoreElements()) {
			Command c = (Command)e.nextElement();
			if (!c.getCommand().equals("mbus.hello")) 
		    	log(".send("+m+")");
		    }
		}
		// Get the destination
		Address dest = m.getHeader().getDestinationAddress();
		// Build bytes
		byte[] data = prepareMessage(m);
		// If message is reliable add sequence number to wait-list
		Retransmitter rt = null;
		if(m.isReliable()){
		    rt=new Retransmitter(this, dest, data, m, config);
	    	AckQueue.put(new Integer(m.getHeader().getSequenceNumber()), rt);
		}
		// Send it
		sendMessage(dest, data, m);
		int seqNum = m.getHeader().getSequenceNumber();
		// For reliable messages now start the retransmitter
		if(m.isReliable()){
	    	rt.start();
		}
		return seqNum;
    }

    //------------------------------------------------------------
    /**
     * Builds a Message Digest and encrypts it if necessary.
     * The result is returned as a bytebuffer that can be sent.
     */
    private byte[] prepareMessage(Message m) {
	// Build message-digest
	String digest=authenticator.getBase64Digest(m.getWithoutDigest());
	String msg=digest+"\n"+m.toString();
	// Do encryption
//  	crypter.testIt(msg);
	byte[] buf=crypter.encrypt(msg);
	return buf;
    }

    //------------------------------------------------------------
    /**
     * This method helps to avoid a retransmitter-explosion.
     */
    void sendMessage(Address dest, byte[] buf, Message m){
	DatagramPacket dp = null;	
	// Check, if we know a unicast-ip
	InetAddress ip = null;
	int port = 0;
	if (config.getSendUnicast()) {
	    Enumeration e = knownTargets.keys();
	    while (e.hasMoreElements()) {
		Object o = e.nextElement();
		Address ad = (Address)o;
		if (ad.equals(dest)) {
		    Object[] oo = (Object[])unicast.get(ad);
		    if (oo==null) 
			System.err.println("Strange - I have no unicast info for "+ad+" - using multicast.");
		    else {
			ip = (InetAddress)oo[0];
			port = ((Integer)oo[1]).intValue();
			//  		    System.out.println(dest+" ist auf "+ip+" port "+port);
		    }
		    break;
		}
	    }
	}

	if (port==0 || port==config.getPort()) {
	    dp = new DatagramPacket(buf, buf.length, 
				    config.getMulticastGroup(), 
				    config.getPort());
//  	    log("Multicast to "+dp.getAddress()+", Port "+dp.getPort());
	} else {
	    dp = new DatagramPacket(buf, buf.length, ip, port);
//  	    log("Unicast to "+dp.getAddress()+", Port "+dp.getPort());
	}
	
	try{
	    logOutgoing(m, dp.getAddress(), dp.getPort());
		// System.out.println("MBUS: SENDING "+dp.getAddress()+":"+dp.getPort());
		// System.out.println(HexDumpMaker.makeHexDump(dp.getData(),buf.length));
		socket.setTimeToLive(config.getScope());  // REPLACEMENT
		socket.send(dp);                          // REPLACEMENT
	    // socket.send(dp, config.getScope());    // REPLACED

	}catch(IOException ioe){
	    System.out.println("Mbus: IOException while sending multicast data.");
	}
	
	// Try to decode again
//  	String all = null;
//  	try {
//  	    all = new String(crypter.decrypt(buf));
//  	    // Prepare Digest-checking
//  	    String digt = all.substring(0, all.indexOf('\n'));
//  	    String body = all.substring(all.indexOf('\n')+1, all.length());
//  	    Message me=new Message(all);
//  	}catch(Exception e){
//  	    System.out.println("STL.Shouldn't happen: Failed decoding my encoded message.");
//  	    System.out.println(all);
//  	    e.printStackTrace();
//  	}    
    }

    //------------------------------------------------------------
    /**
     * Send the message unicast
     */
    private void sendToMulticastSocket(DatagramPacket dp, InetAddress ip, int port) {
	try{
		socket.setTimeToLive(config.getScope());  // REPLACEMENT
		socket.send(dp);                          // REPLACEMENT
		// socket.send(dp, config.getScope());    // REPLACED
	}catch(IOException ioe){
	    System.out.println("Mbus: IOException while sending multicast data.");}
    }
    

    //------------------------------------------------------------
    /**
     * This method is called by the Retransmitter to indicate a
     * delivery failure.
     * <br>You shouldn't call this method directly.
     *
     */
    public void deliveryFailed(int seqnum, Message mess){
	int[] i={seqnum};
	StringBuffer buf = new StringBuffer();
	for (int j=0; j<i.length; j++)
	    buf.append(i[j]+" ");
	logGeneral("Failed delivering: "+buf);
	log("#######Delivery Failed: "+seqnum+"#################");
	removeACKs(i, mess, false);
	listener.deliveryFailed(seqnum, mess);
    }

    //------------------------------------------------------------
    public void removeACKs(int[] acklist, Message mess, boolean successful){
	for(int i=0;i<acklist.length;i++){
	    try{
		Integer in=new Integer(acklist[i]);
		log("removeACKs: "+acklist[i]+" in "+AckQueue+" = "+AckQueue.containsKey(in));
		if(AckQueue.containsKey(in)){
		    Retransmitter rt=(Retransmitter)AckQueue.get(in);
		    rt.halt();
		    AckQueue.remove(in);
		    if (successful) 
			listener.deliverySuccessful(acklist[i], rt.getMessage());
		} //else
		//  System.out.println("Mbus: Obsolete ACK message.");
		
	    }catch(Exception e){
		System.out.println("STL:Mbus: Error while removing ACKs.");
		e.printStackTrace();
	    }
	}
    }

    //------------------------------------------------------------
    public int broadcast(Address sender, Command com) {
	return send(com, sender, allTargets, false);
    }
    
    //------------------------------------------------------------
    /**
     * Tell all entities that a single entity leaves the MBus.
     *
     * @param addr Address of the entity that leaves
     */
    public void shutdown(Address addr){
	Command bye=new Command("mbus.bye", new MDataType[0]);
	broadcast(addr, bye);
    }
    
    //------------------------------------------------------------
    /**
     * Log off all entities/interfaces
     */
    public void close(){
	for (int i=0; i<myAddresses.size(); i++)
	    shutdown( (Address)myAddresses.elementAt(i) );
	announcer.halt();
	dgReceiver.halt();
	mcReceiver.halt();
    }

    //------------------------------------------------------------
    /**
     * This method is called by the Receiver to deliver incoming
     * messages. 
     * <br>You shouldn't call this method directly.
     *
     */
    public void incomingMessage(Message mess, Address target) {
//  	// Indicate successful deliveries
//  	int[] acks = mess.getHeader().getAckList();
//  	if (acks.length>0) {
//  	    log("incMess: Have found "+acks.length+" acks.");
//  	    listener.deliverySuccessful(acks, mess.getHeader().getDestinationAddress());
//  	}
	// Handle commands
	Enumeration e = mess.getCommands();
	boolean handled = true;
	while(e.hasMoreElements()) {
	    Command com = (Command)e.nextElement();
	    if (com.getCommand().equals("mbus.hello")) {
		handleHELLO(mess);
	    } else if (com.getCommand().equals("mbus.bye")) {
		handleBYE(mess);
		return;
	    } else
		handled = false;
	}
	if (!handled) {
	    log("################STL: Received message \n"+mess+"######################\n");
	    listener.incomingMessage(mess);
	}
    }

    //------------------------------------------------------------
    /**
     * Send acknowledgements for received messages
     * <br>You shouldn't call this method directly.
     *
     * @param source Your Address
     * @param target The receipent of the acknowledgements
     * @param ackList List of sequencenumbers to acknowledge
     */
    public void sendACKs(Address source, Address target, int[] ackList) {
	Header h=new Header(source, target, getSeqCounter(), 
			    false, 
			    ackList);
	Message m=new Message(h, new Vector());
	send(m);
    }

    //------------------------------------------------------------
    /**
     * Returns a list of entities that are currently on the MBus.
     *
     *  @return Addresses of the entites
     */
    public Address[] getKnownEntities() {
	Address[] ret = new Address[knownTargets.size()];
	int i=0;
	Enumeration e = knownTargets.elements();
	while (e.hasMoreElements()) {
	    ret[i++] = (Address)e.nextElement();
	}
	return ret;
    }

    //------------------------------------------------------------
    /**
     * See if we already know this address. 
     */
    public void checkAddress(Address src, InetAddress ip, int port) {
	Enumeration e = knownTargets.keys();
	while (e.hasMoreElements()) {
	    Address chk = (Address)e.nextElement();
	    if (chk.equals(src)) 
		return;
	}
	knownTargets.put(src, new Integer(N_DEAD));
	newAddress(src, ip, port);
	listener.newEntity(src);
    }

    //------------------------------------------------------------
    /**
     * Get the list of known Mbus-entities.
     *
     * @return Enumeration of Addresses
     */
    public Enumeration getKnownAddresses() {
	return unicast.elements();
    }

    //------------------------------------------------------------
    /**
     * Add an address of an mbus entity for unicast optimization.
     * <br>You shouldn't call this method directly.
     *
     * @param ad Address of entity to add
     */
    public void newAddress(Address ad, InetAddress ip, int port) {
	log("New address: "+ad+" at "+ip.getHostAddress()+", "+port);
	Object[] foo = {ip, new Integer(port)};
	unicast.put(ad, foo);
    }
    
    //------------------------------------------------------------
    /**
     * Remove an address of an mbus entity for unicast optimization.
     * <br>You shouldn't call this method directly.
     *
     * @param ad Address of entity to remove
     */
    public void delAddress(Address ad) {
	log("Remove address: "+ad);
	Enumeration e = unicast.keys();
	while (e.hasMoreElements()) {
	    Address dst = (Address)e.nextElement();
	    if (dst.equals(ad)) {
		unicast.remove(dst);
		return;
	    }
	}
    }	
    
    //---------------------------------------------------------------
    private void handleHELLO(Message m) {
	// Who send the HELLO-Message?
	Address source = m.getHeader().getSourceAddress();
	// Do we already know it?

	Enumeration e= knownTargets.keys();
	while (e.hasMoreElements()) {
	    Address a = (Address)e.nextElement();
	    if (a.equals(source)) {
		// Yes, so get it's counter
		int n = ((Integer)knownTargets.get(a)).intValue();
		// Set the new counter
		knownTargets.put(a, new Integer(N_DEAD));
		return;
	    }
	}
	// No, so register it
	log("stl.handleHELLO - : new entity"+source);
	knownTargets.put(source, new Integer(N_DEAD));
	listener.newEntity(source);
    }
    
    //---------------------------------------------------------------
    private void handleBYE(Message m) {
	// Who send the HELLO-Message?
	Address source = m.getHeader().getSourceAddress();

	Enumeration e= knownTargets.keys();
	while (e.hasMoreElements()) {
	    Address a = (Address)e.nextElement();
	    if (a.equals(source)) {
		// Yes, so get it's counter
		knownTargets.remove(a);
		// Remove the target from the unicast-list
		delAddress(a);
		listener.entityShutdown(a);
		return;
	    }
	}
    }
    
    //---------------------------------------------------------------
    void tick() {
	Enumeration e= knownTargets.keys();
	while (e.hasMoreElements()) {
	    Address a = (Address)e.nextElement();
	    int n = ((Integer)knownTargets.get(a)).intValue();
	    n--;
	    knownTargets.put(a, new Integer(n));
	    if (n==0) {
		knownTargets.remove(a);
		// Remove the target from the unicast-list
		delAddress(a);
		listener.entityDied(a);
	    }		
	}
    }

    //------------------------------------------------------------
    /**
     * Get the current configuration of the Mbus.
     *
     * @return Configuration-object
     */
    public MBusConfiguration getConfig() {
	return config;
    }

}




//------------------------------------------------------------
//------------------------------------------------------------
class AgingThread extends Thread {
    
    SimpleTransportLayer stl;
    private boolean stopped;
    
    //------------------------------------------------------------
    public AgingThread(SimpleTransportLayer stl) {
	this.stl = stl;
    }

    //------------------------------------------------------------
    public void run() {
	stopped = false;
	while (!stopped) {
	    try {
		sleep(SimpleTransportLayer.T_HELLO+SimpleTransportLayer.T_DITHER);
		stl.tick();
	    } catch (InterruptedException ie) {
	    }
	}
    }
    
    //------------------------------------------------------------
    public void halt() {
	stopped = true;
    }
}
