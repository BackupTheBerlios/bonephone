package org.mbus.apps;

import java.util.*;
import java.text.*;
import java.net.InetAddress;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import org.mbus.*;

/**
 * MTool.java
 *
 * @author Stefan Prelle
 * @version $id: MTool.java 1.0 Fri Oct  1 17:55:50 1999 prelle Exp $
 */

public class MTool extends ExJFrame implements ActionListener, MBusListener, IncomingLogger, OutgoingLogger, GeneralLogger {
    
    private static String[][] address = {{"app","mtool"},
					 {"media","control"},
					 {"module","engine"},
					 {"id",TransportLayer.getID()}};
    private final static Font COURIER = new Font("monospaced", Font.PLAIN, 12);
    private final static boolean DEBUG = false;

    private Vector         addresses;
    private SimpleTransportLayer layer;
    private Address        myAddress, allTargets;
    private boolean        showHellos;
    
    private AddressTable adTab;
    private JTable addr_ta;
    private JTextField command_t;
    private JCheckBox  reliable_cb;
    private JButton    dumpLog_b;

    private Vector log;

    //---------------------------------------------------------------
    public MTool() throws MBusException {
	super("MBus-Tool");
	addresses = new Vector();
	log       = new Vector();

	adTab = new AddressTable();
	addr_ta = new JTable(adTab);
	JScrollPane addr_sp = new JScrollPane(addr_ta);
	addr_sp.setPreferredSize(new Dimension(400,138));
	addr_ta.setFont(COURIER);

	command_t = new JTextField(40);
	command_t.setFont(COURIER);
	
	reliable_cb = new JCheckBox("Send reliable", false);

	dumpLog_b = new JButton("Dump Log");

	place(addr_sp    , 0,0, 1,1, GridBagConstraints.NORTH);
	place(reliable_cb, 0,1, 1,1, GridBagConstraints.NORTHWEST);
	place(command_t  , 0,2, 1,1, GridBagConstraints.NORTH);
	place(dumpLog_b  , 0,3, 1,1, GridBagConstraints.NORTH);
	
	command_t.addActionListener(this);
	dumpLog_b.addActionListener(this);

	pack();
	setVisible(true);
	// Enable MBus
	myAddress=new Address(address);
	layer=new SimpleTransportLayer(myAddress, this);
	layer.setWiretapping(true);
	layer.addIncomingLogger(this);
	layer.addOutgoingLogger(this);
	layer.addGeneralLogger(this);
    }
    
    //------------------------------------------------------------
    public void incomingMessage(Message mess) {
//  	if (!mess.isHello()) 
//  	    addToLog(new LogEntry(LogEntry.RECEIVED, mess));
	System.out.println("RECEIVED: "+mess);
    }
                                                                      
    //---------------------------------------------------------------
    public void deliverySuccessful(int seqNum, Message mess){
	System.out.println("MTool: Delivery of "+seqNum+" successful.");
    }
    
    //------------------------------------------------------------
    public void deliveryFailed(int seqNum, Message mess) {
	System.out.println("MTool: Delivery of "+seqNum+" failed.");
    }
    
    //------------------------------------------------------------
    public void newEntity(Address addr) {
	addToLog(new LogEntry("NEW ENTITY: "+addr));
	addresses.addElement(addr);
	adTab.addAddress(addr);
	repaint();
    }
    
    //------------------------------------------------------------
    public void entityDied(Address addr) {
	addToLog(new LogEntry("LOST ENTITY: "+addr));
	for (int i=0; i<addresses.size(); i++) {
	    Address comp = (Address)addresses.elementAt(i);
	    if (addr.equals(comp)) {
		addresses.removeElement(comp);
		adTab.removeAddress(comp);
		repaint();
		return;
	    }
	}
    }
    
    //------------------------------------------------------------
    public void entityShutdown(Address addr) {
	addToLog(new LogEntry("ENTITY SHUTDOWN: "+addr));
	for (int i=0; i<addresses.size(); i++) {
	    Address comp = (Address)addresses.elementAt(i);
	    if (addr.equals(comp)) {
		addresses.removeElement(comp);
		adTab.removeAddress(comp);
		repaint();
		return;
	    }
	}
    }
    
    //------------------------------------------------------------
    public void actionPerformed(ActionEvent ae) {
	if (ae.getSource()==command_t) {
	    // Send command
	    try {
		Command comm = new Command(command_t.getText());
		// Reliable ??
		boolean reliable = reliable_cb.isSelected();

		// Targets
		int[] targs = addr_ta.getSelectedRows();
//  		System.out.print("Selected rows: [");
//  		for (int i=0; i<targs.length; i++) System.out.print(targs[i]+",");
//  		System.out.println("]");
		Vector addr = adTab.getAddressesInRows(targs);
		System.out.println("Valid targets: "+addr.size());
		if (addr.size()==0) {
		    layer.broadcast(myAddress, comm);
		} else {
		    for (int i=0; i<addr.size(); i++) {
			Address dest = (Address)addr.elementAt(i);
			layer.send(comm, myAddress, dest, reliable);
		    }
		}
	    } catch (NoSuchElementException nse) {
		System.out.println("Invalid command");
	    }
	} else if (ae.getSource()==dumpLog_b) {
	    dumpLog();
	} else
	    System.out.println(ae);
    }
    
    //------------------------------------------------------------
    public void addToLog(LogEntry le) {
	System.out.println("AddToLog: "+le);
	synchronized (log) {
	    log.addElement(le);
	}
    }
    
    //------------------------------------------------------------
    public void dumpLog() {
	for (int i=0; i<log.size(); i++)
	    System.out.println( log.elementAt(i) );
    }
    
    //------------------------------------------------------------
    public void helloReceived(Message m, InetAddress ip, int port) {
	if (DEBUG) System.out.println("Incoming Hello");
    }
    
    //------------------------------------------------------------
    public void byeReceived(Message m, InetAddress ip, int port) {
	if (DEBUG) System.out.println("Incoming Bye");
    }
    
    //------------------------------------------------------------
    public void messageReceived(Message m, InetAddress ip, int port) {
	addToLog(new LogEntry(LogEntry.RECEIVED,
			      m.getHeader().getTimeStamp(),
			      m));
    }
    
    //------------------------------------------------------------
    public void helloSent(Message m, InetAddress ip, int port) {
	if (DEBUG) System.out.println("Outgoing Hello");
    }
    
    //------------------------------------------------------------
    public void byeSent(Message m, InetAddress ip, int port) {
    }
    
    //------------------------------------------------------------
    public void messageSent(Message m, InetAddress ip, int port) {
	addToLog(new LogEntry(LogEntry.SENT,
			      m.getHeader().getTimeStamp(),
			      m));
    }
    
    //------------------------------------------------------------
    public void logMessage(String text) {
	LogEntry le = new LogEntry(text);
	addToLog(le);
    }
    
    
    //---------------------------------------------------------------
    public final static void main(String[] argv) {
	try {
	    MTool mtool = new MTool();
	} catch (MBusException me) {
	    me.printStackTrace();
	}
    }

} // MTool

//---------------------------------------------------------------
//---------------------------------------------------------------
class AddressTable extends DefaultTableModel {

    public final static String[] NAME= {"app","module","media","id"};

    Vector addresses;

    //---------------------------------------------------------------
    public AddressTable() {
	addresses = new Vector();
	setColumnIdentifiers(NAME);
    }
    
    public int getColumnCount() { return 4; }
    public int getRowCount() { return 7;}

    //---------------------------------------------------------------
    public Object getValueAt(int row, int col) { 
	if (row>=addresses.size())
	    return new String("");
	try {
	    Address ad = (Address)addresses.elementAt(row);
	    return ad.getValue(NAME[col]);
	} catch (Exception e) {
  	    System.err.println(e);
	}
	return new String("");
      };

    //---------------------------------------------------------------
    public void addAddress(Address ad) {
	addresses.addElement(ad);
    }

    //---------------------------------------------------------------
    public void removeAddress(Address ad) {
	addresses.removeElement(ad);
    }

    //---------------------------------------------------------------
    public Vector getAddressesInRows(int[] rows) {
	Vector ret = new Vector();
	for (int i=0; i<rows.length; i++) {
	    if (rows[i]<addresses.size()) 
		ret.addElement(addresses.elementAt(rows[i]));
	}
	return ret;
    }
    
}
