package org.mbus;

import java.util.*;

/**
 * The announcer broadcasts mbus.hello() messages over the mbus
 * to advertise the presence of its entity.
 *
 * @author Ralf Kollmann, Stefan Prelle
 * @version $Revision: 1.1 $ $Date: 2002/02/04 13:23:34 $
 */

public class Announcer extends Thread{
    
    private MessageSender sender;
    private Command       hello;

    // timer values from mbus-semantics draft
    private int t_hello = 1000;
    private int t_dither = 100;
    private int n_dead = 5;
    private boolean ever=true;

    /** Addresses the Receiver listens to */
    private Vector        myAddresses;

    //------------------------------------------------------------
    public Announcer(MessageSender sender, Address[] interfaces){
	this.sender = sender;
	hello = new Command("mbus.hello", new MDataType[0]);
	myAddresses = new Vector();
	for (int i=0; i<interfaces.length; i++)
	    addInterface(interfaces[i]);
    }

    //------------------------------------------------------------
    /**
     * Add an address which the Announcer should use to identify
     * himself.
     *
     * @param addr Address to add
     */
    public void addInterface(Address addr) {
	myAddresses.addElement(addr);
    }

    //------------------------------------------------------------
    /**
     * Remove an address which the Announcer should no longer use
     * to identify himself.
     *
     * @param addr Address to remove
     */
    public void removeInterface(Address addr) {
	for (int i=0; i<myAddresses.size(); i++) {
	    Address ad = (Address)myAddresses.elementAt(i);
	    if (ad.equals(addr)) {
		myAddresses.removeElement(ad);
		return;
	    }
	}
    }
    
    //------------------------------------------------------------
    public void run(){
	for(;ever;){
	    int t=(int)(new Random().nextDouble()*t_dither);
	    try{
		sleep(t_hello);
	    }catch(InterruptedException ie) {
	    	System.out.println("Announcer has insomnia.");
	    }
	    // Let every entity send a Hello-message
	    for (int i=0; i<myAddresses.size(); i++) {
		sender.broadcast((Address)myAddresses.elementAt(i), hello);
	    }
	}
    }

    //------------------------------------------------------------
    public void halt(){ever=false;}
}

