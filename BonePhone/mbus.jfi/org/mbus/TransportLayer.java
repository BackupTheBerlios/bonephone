package org.mbus;

import java.util.Vector;
import java.net.*;

/**
 * This interface defines the functions of a transport-layer of the
 * Mbus. 
 * <p>
 * With additions from Roman Kurmanowytsch <romank@infosys.tuwien.ac.at><br>
 *
 * @author Stefan Prelle <prelle@tzi.de>
 * @version $Id: TransportLayer.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public abstract class TransportLayer implements MessageSender {
    
    /** Sequencenumber for outgoing messages */
    private int seqCounter;

    //------------------------------------------------------------
    public TransportLayer() {
	seqCounter = 0;
    }

    //------------------------------------------------------------
    protected synchronized int getSeqCounter(){
	return seqCounter++;
    }

    //------------------------------------------------------------
    /**
     * Enable or disable the Wiretapping-mode of the receiver.
     *
     * @param wiretapping Wiretapping-mode on?
     */
    public abstract void setWiretapping(boolean wiretapping);

    //------------------------------------------------------------
    /**
     * Send one Command unreliably.
     *
     */
    public int send(Command com, Address source, Address target){
	return send(com, source, target, false);
    }

    //------------------------------------------------------------
    /**
     * Send one Command.
     * 
     * @param c      Command to send
     * @param source Address of the sender
     * @param destination Destinationaddress
     * @param reliable Shall be set to true if the message shall be
     *                 sent reliable.
     * @return The sequence number of this message.
     */
    public int send(Command c, Address source, Address destination, boolean reliable){
	int seq=getSeqCounter();
	Header h=new Header(source, destination, seq, reliable);
	Vector v=new Vector();
	v.addElement(c);
	Message m=new Message(h, v);
	return send(m);
    }

    //------------------------------------------------------------
    /**
     * Send a message with several commands.
     * 
     * @param c      Array of command to send
     * @param source Address of the sender
     * @param destination Destinationaddress
     * @param reliable Shall be set to true if the message shall be
     *                 sent reliable.
     * @return The sequence number of this message.
     */
    public int send(Command c[], Address source, Address destination,
		    boolean reliable){
	int seq  = getSeqCounter();
	Header h = new Header(source, destination, seq, reliable);
	Vector v = new Vector();
	for (int i=0; i<c.length; ++i) {
	    v.addElement(c[i]);
	}
	Message m = new Message(h, v);
	return send(m);
    }

    //------------------------------------------------------------
    public abstract int broadcast(Address sender, Command com);
    
    //------------------------------------------------------------
    /**
     * Tell all entities that a single entity leaves the MBus.
     *
     * @param addr Address of the entity that leaves
     */
    public abstract void shutdown(Address addr);

    //------------------------------------------------------------
    /**
     * Log off all entities/interfaces and terminate the layer
     */
    public abstract void close();
    
    //------------------------------------------------------------
    /** 
     * Get an instance for use with the mbus address of an mbus
     * entity.
     */
    public static String getID(){
	// Create entityID
	long time=System.currentTimeMillis();
	String instance=""+time;
	int i=instance.length();
	String rand=new Long
	    (Math.abs(new java.util.Random().nextLong())).toString();
	int r=rand.length();
	String part1 = instance.substring(i-7, i-2)+rand.substring(r-3, r);
	String part2 = rand.substring(0, 2);
	try {
	    return part1+"-"+part2+"@"+InetAddress.getLocalHost().getHostAddress();
	} catch (UnknownHostException uhe) {
	    return part1+"-"+part2+"@127.0.0.1";
	}
    }

    //------------------------------------------------------------
    /**
     * Get the current configuration of the Mbus.
     *
     * @return Configuration-object
     */
    public abstract MBusConfiguration getConfig();
    
    //------------------------------------------------------------
    /**
     * Returns a list of entities that are currently on the MBus.
     *
     *  @return Addresses of the entites
     */
    public abstract Address[] getKnownEntities();
	
} // TransportLayer
