package org.mbus;

import java.util.*;

/**
 * VirtualTransportLayer.java
 *
 * @author Stefan Prelle
 * @version $Id: VirtualTransportLayer.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public class VirtualTransportLayer extends TransportLayer implements MBusListener {
    
    private final static boolean DEBUG = false;

    private SimpleTransportLayer layer;
    /** Contains outstanding acknowledgements */ 
    private Hashtable ackQueue;
    /** Known virtual MBus-Entities */
    private Hashtable virtual;
    /** Known external MBus-Entities */
    private Vector external;
    /** Sequencenumber for outgoing messages */
    private int seqCounter;
    /** Address for all targets */
    private Address   allTargets;
    /** Be spyable - send all internal messages to outer mbus too */
    private boolean spyable;
    
    //------------------------------------------------------------
    public VirtualTransportLayer(boolean spy) throws MBusException {
	ackQueue = new Hashtable();
	virtual  = new Hashtable();
	external = new Vector();
	spyable  = spy;
	
	// Create broadcast-address
	String[][] voidArray={};
	allTargets=new Address(voidArray);

	layer = new SimpleTransportLayer(new Address[0], this);
	// Register the elements that are known by now
	Address[] tmp = layer.getKnownEntities();
	for (int i=0; i<tmp.length; i++)
	    external.addElement(tmp[i]);
    }

    //---------------------------------------------------------------
    private void log(String mess) {
	if (DEBUG) {
	    System.out.println(System.currentTimeMillis()+" VTL/"+Thread.currentThread().getName()+": "+mess);
	}
    }

    //------------------------------------------------------------
    public void addModule(VirtualMBusListener mod, Address addr) {
	Enumeration e = virtual.keys();
	Address key = addr;
	while (e.hasMoreElements()) {
	    Address k = (Address)e.nextElement();
	    if (k.equals(addr)) {
		key = k;
		break;
	    }
	}

	// Add module to listeners
	virtual.put(key, mod);
	layer.addInterface(addr);
    }

    //------------------------------------------------------------
    public void removeModule(Address addr) {
	Enumeration e = virtual.keys();
	Address key = addr;
	while (e.hasMoreElements()) {
	    Address k = (Address)e.nextElement();
	    if (k.equals(addr)) {
		virtual.remove(key);
		layer.shutdown(addr);
		layer.removeInterface(addr);
		return;
	    }
	}
    }

    //------------------------------------------------------------
    /**
     * Enable or disable the Wiretapping-mode of the receiver.
     *
     * @param wiretapping Wiretapping-mode on?
     */
    public void setWiretapping(boolean wiretapping) {
//  	receiver.setWiretapping(wiretapping);
    }

    //------------------------------------------------------------
    /**
     * Find all internal/virtual entities that listen to a given
     * address.
     *
     * @param addr Address to check
     * @return List of entities
     */
    private VirtualMBusListener[] getMatchingTargets(Address addr) {
	Vector results = new Vector();
	Enumeration e = virtual.keys();
	while (e.hasMoreElements()) {
	    Address k = (Address)e.nextElement();
	    if (k.matches(addr)) 
		results.addElement(virtual.get(k));
	}
	VirtualMBusListener[] ret = new VirtualMBusListener[results.size()];
	for (int i=0; i<results.size(); i++)
	    ret[i] = (VirtualMBusListener)results.elementAt(i);
	return ret;
    }

    //------------------------------------------------------------
    /**
     * Checks if the given address matches any external target.
     *
     * @param addr Address to check
     * @return True, if there is any external entity matching the
     *         target.
     */
    private boolean matchesExternal(Address addr) {
	if (spyable)
	    return true;
	
	Vector results = new Vector();
	Enumeration e = external.elements();
	while (e.hasMoreElements()) {
	    Address k = (Address)e.nextElement();
	    if (k.matches(addr)) 
		return true;
	}
	return false;
    }

    //------------------------------------------------------------
    private void distributeInternal(Message mess) {
	// Find out the virtual receipents
	Address target = mess.getHeader().getDestinationAddress();
	Address source = mess.getHeader().getSourceAddress();
	VirtualMBusListener[] virt = getMatchingTargets(target);
	// Deliver message to all virtual receipents
	for (int i=0; i<virt.length; i++) {
	    if (!virt[i].getAddress().equals(source)) {
		log(".distributeInternal to "+virt[i]);
		virt[i].incomingMessage(mess);
	    }
	}
    }

    //------------------------------------------------------------
    private void distributeExternal(Message mess) {
	Address target = mess.getHeader().getDestinationAddress();
	if (matchesExternal(target)) {
	    log("Send to external MBus too\n\n");
	    layer.send(mess);
	}
    }

    //------------------------------------------------------------
    public int send(Message mess) {
//  	log(".send: "+mess);
	int seqNum = mess.getHeader().getSequenceNumber();

	distributeInternal(mess);
	distributeExternal(mess);
	return seqNum;
    }

    //------------------------------------------------------------
    public int broadcast(Address source, Command com) {
	log(".broadcast: "+com);

	// Build the message
	int seq=getSeqCounter();
	Header h=new Header(source, allTargets, seq, false);
	Vector v=new Vector();
	v.addElement(com);
	Message m=new Message(h, v);
	
	return send(m);
    }

    //------------------------------------------------------------
    public void sendACKs(Address target, int[] ackList) {
//  	mbus.send(myAddress, m.getHeader().getSourceAddress(), i);
    }

    //------------------------------------------------------------
    public void deliveryFailed(int seqNum, Message mess) {
	MBusListener[] all = getMatchingTargets(mess.getHeader().getSourceAddress());
	for (int i=0; i<all.length; i++)
	    all[i].deliveryFailed(seqNum, mess);
    }    
                                                                      
    //---------------------------------------------------------------
    public void deliverySuccessful(int seqNum, Message mess){
//  	log("VTL.deliverySuccessful: "+mess);
	MBusListener[] all = getMatchingTargets(mess.getHeader().getSourceAddress());
//  	log("VTL: "+all.length+" matching targets for "+mess.getHeader().getSourceAddress());
	for (int i=0; i<all.length; i++)
	    all[i].deliverySuccessful(seqNum, mess);
    }
    
    //------------------------------------------------------------
    /**
     * Tell all entities that a single entity leaves the MBus.
     *
     * @param addr Address of the entity that leaves
     */
    public void close(){
	Enumeration e = virtual.keys();
	while (e.hasMoreElements()) 
	    removeModule( (Address)e.nextElement() );
	layer.close();
    }
    
    //------------------------------------------------------------
    /**
     * Shutdown an entity
     */
    public void shutdown(Address addr){
	removeModule( addr );
    }
    
    //------------------------------------------------------------
    public void removeACKs(int[] acklist){
	for(int i=0;i<acklist.length;i++){
	    try{
		Integer in=new Integer(acklist[i]);
		if(ackQueue.containsKey(in)){
		    Retransmitter rt=(Retransmitter)ackQueue.get(in);
		    rt.halt();
		    ackQueue.remove(in);
		} //else
		//  System.out.println("Mbus: Obsolete ACK message.");
		
	    }catch(Exception e){
		System.out.println("VTL. Mbus: Error while removing ACKs.");
		e.printStackTrace();
	    }
	}
    }

    //------------------------------------------------------------
    public void incomingMessage(Message mess) {
	distributeInternal(mess);
    }
    

    //------------------------------------------------------------
    /**
     * Send acknowledgements for received messages
     *
     * @param source Your Address
     * @param target The receipent of the acknowledgements
     * @param ackList List of sequencenumbers to acknowledge
     */
    public void sendACKs(Address source, Address target, int[] ackList) {
	Header h=new Header(source,
			    target, 
			    getSeqCounter(), 
			    false, 
			    ackList);
	Message m=new Message(h, new Vector());
	send(m);
    }

    //---------------------------------------------------------------
    public void newEntity(Address addr) {
	// Inform listeners
	Enumeration e = virtual.elements();
	while (e.hasMoreElements())
	    ((VirtualMBusListener)e.nextElement()).newEntity(addr);

	external.addElement(addr);
    }

    //---------------------------------------------------------------
    public void entityDied(Address addr) {
	Enumeration e = external.elements();
	while (e.hasMoreElements()) {
	    Address a = (Address)e.nextElement();
	    if (a.equals(addr)) {
		external.remove(a);

		// Inform listeners
		Enumeration ee = virtual.elements();
		while (ee.hasMoreElements())
		    ((VirtualMBusListener)ee.nextElement()).entityDied(addr);
		return;
	    }
	}
    }

    //---------------------------------------------------------------
    public void entityShutdown(Address addr) {
	Enumeration e = external.elements();
	while (e.hasMoreElements()) {
	    Address a = (Address)e.nextElement();
	    if (a.equals(addr)) {
		external.remove(a);

		// Inform listeners
		Enumeration ee = virtual.elements();
		while (ee.hasMoreElements())
		    ((VirtualMBusListener)ee.nextElement()).entityShutdown(addr);
		return;
	    }
	}
    }

    //------------------------------------------------------------
    public MBusConfiguration getConfig() {
	return layer.getConfig();
    }

    //------------------------------------------------------------
    /**
     * Returns a list of entities that are currently on the MBus.
     *
     *  @return Addresses of the entites
     */
    public Address[] getKnownEntities() {
	Address[] ext = layer.getKnownEntities();
	Address[] ret = new Address[ext.length+virtual.size()];
	int i=0;
	for (i=0; i<ext.length; i++)
	    ret[i] = ext[i];
	Enumeration e = virtual.elements();
	while (e.hasMoreElements())
	    ret[i++] = ((MessageBus)e.nextElement()).getAddress();

	return ret;
    }

} // VirtualTransportLayer
