package bonephone;

import java.util.*;
import org.mbus.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MShell.java
 *
 * @author Jens Fiedler
 * @version $Id: MShell.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public class MShell extends Thread implements MBusListener, ActionListener {

    // Taken from "MBus - Messages and Procedures" p.8
    public final static int T_HELLO  = 1000;
    public final static int T_DITHER = 100;
    private final static int N_DEAD  = 5;
    
	private  static String[][] address = {{"app","MShell"},
	                                      {"media","control"},
	                                      {"module","engine"},
	                                      {"instance",TransportLayer.getID()}};

	private  static String[][] empty = {};
    // Used for virtual transport-layer
    public static VirtualTransportLayer virtLayer = null;

    private TransportLayer layer;
    private Address        myaddress;
    private boolean        showHellos;
    
    //--------------------------------------------------------------
    public MShell(boolean showHellos) throws MBusException {

		myaddress=new Address(address);
		initMShellGUI();

		this.showHellos = showHellos;
	
		SimpleTransportLayer ml=new SimpleTransportLayer(myaddress, this);
		ml.setPacketDump(true);
		layer=ml;
		layer.setWiretapping(true);
	
    }


	///////////////////////////
	// ActionListener interface
	public void actionPerformed(ActionEvent ev) {
		String ac=ev.getActionCommand();
		if (ac.equals("QUIT")) { System.exit(0); }
		if (ac.equals("SEND")) { send(); return; }
		if (ac.equals("CLEAR")) { clear(); return; }
		if (ac.equals("XFER")) { xfer(); return; }
	}


	private JTextArea ta1,ta2;
	private JTextField ttf,ftf,ctf;
	private Vector pcom;	

	private void send() {
//		Address fa=new Address(ftf.getText());
//		System.out.println("BLEEP:"+ftf.getText());
//		Address ta=new Address(ttf.getText());
		MultipleCommandSender mcs=new MultipleCommandSender(myaddress,myaddress,layer);
		int i;
		for (i=0; i<pcom.size(); i++) {
			String com=(String)(pcom.elementAt(i));
			System.out.println("adding "+com);
			mcs.add(com);
		}
		mcs.send();
		clear();
	}
	private void clear() {
		ta2.setText(null);
		pcom.removeAllElements();
	}
	private void xfer() {
		String com=ctf.getText();
		if (com==null || com.equals("")) { send();  return; }
		pcom.addElement(com);
		ta2.append(com+"\n");
		ctf.setText(null);
	}




	/////////////////////////////////////
	// MShell GUI
	private void initMShellGUI() {
		String titles[]={ "CLEAR","QUIT","SEND" };
		pcom=new Vector(4);

		JScrollPane scf;
		JFrame fr=new JFrame("BonePhones MShell");
		JPanel bp=new JPanel();
		bp.setLayout(new BoxLayout(bp,BoxLayout.X_AXIS));
		Container cp=fr.getContentPane();
		cp.setLayout(new BoxLayout(cp,BoxLayout.Y_AXIS));
		JButton b;   int i;
		for (i=0; i<titles.length; i++) {
			b=new JButton(titles[i]);
			b.addActionListener(this);
			b.setActionCommand(titles[i]);
			bp.add(b);
		}		
		ta1=new JTextArea(30,100);    ta1.setEditable(false);
		scf=new JScrollPane(ta1);
		ftf=new JTextField(80);     ftf.setText(myaddress.toString());
		ttf=new JTextField(80);     ttf.setText("(*)");
		ctf=new JTextField(80);     ctf.addActionListener(this);   ctf.setActionCommand("XFER");
		ta2=new JTextArea(4,80);    ta2.setEditable(false);
		JPanel frp=new JPanel();   frp.setLayout(new BoxLayout(frp,BoxLayout.X_AXIS));
		JPanel top=new JPanel();   top.setLayout(new BoxLayout(top,BoxLayout.X_AXIS));
		JPanel cop=new JPanel();   cop.setLayout(new BoxLayout(cop,BoxLayout.X_AXIS));
		frp.add(new JLabel("From:"));        frp.add(ftf);
		top.add(new JLabel("To:"));          top.add(ttf);
		cop.add(new JLabel("Command:"));     cop.add(ctf);
		cp.add(scf);
		cp.add(frp);
		cp.add(top);
		cp.add(cop);
		cp.add(ta2);
		cp.add(bp);
		
        fr.addWindowListener(new WindowAdapter () {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        }
        );

		fr.pack();    fr.setVisible(true);
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
		String d=""+dat.getTime();
		int l=d.length()+1;
		int i;
		String sp="";
		for (i=0; i<l; i++) sp=sp+" ";
	    System.out.println(d+": FROM "+m.getHeader().getSourceAddress());
		ta1.append(d+": FROM "+m.getHeader().getSourceAddress()+"\n");
	    System.out.println(sp+" TO "+m.getHeader().getDestinationAddress());
		ta1.append(sp+" TO "+m.getHeader().getDestinationAddress()+"\n");
		while(e.hasMoreElements()) {
	    	Command com = (Command)e.nextElement();
			System.out.println(sp+" "+com);
			ta1.append(sp+" "+com+"\n");
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
	    MShell mbus = new MShell(false);
	} catch (MBusParsingException mpe) {
	    System.err.println(mpe.getMessage());
	} catch (MBusException me) {
	    System.err.println("--> "+me.getMessage());
	}
    }
    
} // MShell

