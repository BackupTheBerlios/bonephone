package org.mbus.apps;

import java.util.*;
import org.mbus.*;

/**
 * MSpy.java
 *
 * @author Stefan Prelle
 * @version $Id: MSpy.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public class MSpy extends Thread implements MBusListener {

    // Taken from "MBus - Messages and Procedures" p.8
    public final static int T_HELLO  = 1000;
    public final static int T_DITHER = 100;
    private final static int N_DEAD   = 5;
    
    private  static String[][] address = {{"app","newmspy"},
					  {"media","control"},
					  {"module","engine"},
					  {"instance",TransportLayer.getID()}};

    // Used for virtual transport-layer
    public static VirtualTransportLayer virtLayer = null;

    private TransportLayer layer;
    private Address        myaddress, allTargets;
    private boolean        showHellos;
    
    //--------------------------------------------------------------
    public MSpy(boolean showHellos) throws MBusException {
	this.showHellos = showHellos;
	
	myaddress=new Address(address);
	layer=new SimpleTransportLayer(myaddress, this);
	layer.setWiretapping(true);
	
    }

    //---------------------------------------------------------------
    /**
     * Handle an incoming MBus-Message
     *
     * @param m Incoming Message
     */
    public void incomingMessage(Message m){
	Date dat = new Date();
	Enumeration e = m.getCommands();
	while(e.hasMoreElements()) {
	    Command com = (Command)e.nextElement();
	    StringBuffer buf = new StringBuffer(""+dat.getTime());
	    buf.append(": FROM "+m.getHeader().getSourceAddress());
	    buf.append(" TO "+m.getHeader().getDestinationAddress());
	    buf.append(" COMMAND "+com);
	    System.out.println(buf.toString());
	}    
    }
                                                                      
    //---------------------------------------------------------------
    public void deliverySuccessful(int seqNum, Message mess){
    }

    //---------------------------------------------------------------
    public void deliveryFailed(int seqnum, Message mess){
	System.out.println("Message "+seqnum+" could not be delivered. ");
    }

    //------------------------------------------------------------
    public Address getAddress() {
	return myaddress;
    }

    //---------------------------------------------------------------
    public void newEntity(Address addr) {
	System.out.println("NEW ENTITY: "+addr);
    }

    //---------------------------------------------------------------
    public void entityDied(Address addr) {
	System.out.println("ENTITY DIED: "+addr);
    }

    //---------------------------------------------------------------
    public void entityShutdown(Address addr) {
	System.out.println("ENTITY SHUTDOWN: "+addr);
    }

    //---------------------------------------------------------------
    public final static void main(String[] argv) {
	try {
	    MSpy mbus = new MSpy(false);
	} catch (MBusParsingException mpe) {
	    System.err.println(mpe.getMessage());
	} catch (MBusException me) {
	    System.err.println("--> "+me.getMessage());
	}
    }
    
} // MSpy

