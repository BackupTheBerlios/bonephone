package bonephone;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;



// -------------------- GUI ----------------------------------

/**
 * The GUI class is the top most class of the whole user interface. It implements all
 * necessary interfaces to communicate with the underlying Phone, Phonebook.
 * The GUI has a state: IDLE, PBOOK, CC.<br>
 * IDLE: The splash screen is displayed, no calls at all present.<br>
 * PBOOK: Use has openend the Phonebook to select an entry/dial an address.<br>
 * CC: Calls are present and need to be displayed/managed.<br>
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
public class GUI implements PhoneBookListener, UINotifyable, ActionListener {

	final public static int MAJOR_VERSION = 0 ;
	final public static int MINOR_VERSION = 8 ;
	final public static int SUB_VERSION = 2 ;
	final public static String versionstring="Version "+MAJOR_VERSION+"."+MINOR_VERSION+"."+SUB_VERSION+" alpha";

	final public static int Style_Large  = 0;
	final public static int Style_Classic = 1;

	int style;

	JPanel            ButtonPane;
	JPanel            SubjectPane;
	JPanel            VolumePane;
	JPanel            ToPane;
	JTextField        SubjectField;   // Subject Text Field
	JTextField        ToField;        // To Text field for manual dialing
	IPT_State_GUI     state;
	JFrame            fr1;

	private PaneOpener        spo; // Subject pane
	private PaneOpener        vpo; // volume pane
	private PaneOpener        tpo; // to pane
	private AudioControlPanel actl;
  
	private JButton        left,right;
	private JPanel         ButtonPanel;    // Holds the bottom line buttons 
	private JPanel         ListPanel;      // Holds the List and scrollbars
	private JScrollPane    ListScrollPane;  

	private PhoneController PC;
	private PhoneBook       PB;
	private Configuration   CFG;

	private PanelHolder    ph_calls;
	private PanelHolder    ph_pbook;
	private PanelHolder    ph_splash;
	private PanelHolder    ph_current; // PanelHolder which is currently displayed
  
	private HashMap callPanelMap;      // Call -> CallPanel


	/**
	 * set the crrent panelholder for the main display area. 
	*/
	private void setPanelHolder(PanelHolder ph) {
		if (ph_current == ph) return;
		ListScrollPane.setViewportView(ph);
		ph_current=ph;
	}	

	/** Decide which panel and buttons to show. */
	private void sync() {
		switch (state.get()) {
			case IPT_State_GUI.ST_IDLE: {  
				left.setEnabled(false);
				left.setVisible(false);
				// left.setEnabled(true);
				// left.setVisible(true);
				// left.setText("Fake");
				right.setText("Phonebook");
				spo.close();
				tpo.close();
				vpo.close();
				setPanelHolder(ph_splash);
				break;
			}	
			case IPT_State_GUI.ST_PBOOK: {
				left.setEnabled(true);
				left.setVisible(true);
				left.setText("Dial");
				right.setText("Cancel");
				tpo.open(1);
				spo.open(2);
				vpo.close();
				setPanelHolder(ph_pbook);
				fr1.getContentPane().validate();
				break;
			}
			case IPT_State_GUI.ST_CC: {
				left.setVisible(true);
				left.setEnabled(true);
				left.setText("Audio");
				right.setText("Phonebook");
				spo.close();
				tpo.close();
				setPanelHolder(ph_calls);
				break;
			}
		}
	}


	/** Handle action for left button according to current state. */
	private void handleLeftButton(String actioncommand) {
		if (state.get() == IPT_State_GUI.ST_PBOOK) {
			PC.issueCall(ToField.getText(),SubjectField.getText());
			return;
		}
		if (state.get() == IPT_State_GUI.ST_IDLE) {
			System.out.println("Faking Call");
			PC.fakeCall();
			return;
		}
		if (state.get() == IPT_State_GUI.ST_CC) {
			// open/close Volume Control Panel				
			if (vpo.isOpen()) {
				vpo.close();
			} else {
				vpo.open(1);
			}
			return;
		}
	}


	/** Handle action for right button according to current state. */
	private void handleRightButton (String actioncommand) {
		switch (state.get()) {
		case IPT_State_GUI.ST_IDLE: {    // SPLASH
			state.set(IPT_State_GUI.ST_PBOOK);
			break;
	  	}
		case IPT_State_GUI.ST_PBOOK: {   // CANCEL
			// check if Calls are present, then set state to ST_IDLE or ST_CC
			if (PC.getCalls().size()>0) {
				state.set(IPT_State_GUI.ST_CC);
			}
			else {
				state.set(IPT_State_GUI.ST_IDLE);
			}
			break;
	  	}
		case IPT_State_GUI.ST_CC: {  // PHONEBOOK
			vpo.close();
			state.set(IPT_State_GUI.ST_PBOOK);
			break;
		}
		}
		sync();
	}


	/** Build a Call panel according to the users preference setting. */
	private CallPanel makeCallPanel(Call c) {
		CallPanel cp;
		boolean aa=CFG.getAsInt("AutoAnswer")!=0;
		switch (style) {
			case Style_Large:  { cp=new LargeCallPanel(c,aa); break; }
			case Style_Classic: { cp=new ClassicCallPanel(c,aa); break; }
			default:            { cp=new ClassicCallPanel(c,aa); break; }
		}
		ph_calls.add(cp);
		callPanelMap.put(c,cp);
		return cp;
	}


	/** when the user changes the call panel style, this routine does the update. */
	private void syncStyle() {
		VectorRead calls=PC.getCalls();
		int i;
		for (i=0; i<calls.size(); i++) {		
			Call c=(Call)(calls.elementAt(i));
			CallPanel cp=(CallPanel)(callPanelMap.get(c));
			ph_calls.remove(cp);
			cp.detach();
			callPanelMap.remove(c);
		}
		for (i=0; i<calls.size(); i++) {		
			Call c=(Call)(calls.elementAt(i));
			makeCallPanel(c);
		}
		switch (style) {
			case Style_Large:  { ph_calls.useGridLayout(); break; }	
			case Style_Classic: { ph_calls.useBoxLayout(); break; }	
		}
		// ph_calls.validate();
		ListPanel.validate();
	}
	
	///////////////////////////
	// ActionListener interface
	/** any action performed by any widget in the gui is noticed here. Further decision on
	 * action is done by looking at the class of the firing widget and its action command.
	*/
	public void actionPerformed(ActionEvent e) {
		String actioncommand=e.getActionCommand();
		Object o=e.getSource();
		if (o instanceof JButton) {
			JButton src=(JButton)o;
			if (src == right) { handleRightButton(actioncommand); }
			if (src == left)  { handleLeftButton(actioncommand); }
		}
		if (o instanceof JCheckBoxMenuItem) {
			char c=actioncommand.charAt(0);
			switch (c) {
			case 'a': {
					CFG.set("AutoAnswer",""+((CFG.getAsInt("AutoAnswer")!=0)?0:1));
					break;
				}
			}
			return;
		}
		if (o instanceof JRadioButtonMenuItem) {
			char c=actioncommand.charAt(0);
			String p=actioncommand.substring(1);
			switch (c) {
			case 's': { // set style
					int sty;
					try { sty=Integer.parseInt(p); } catch (Exception ex) { break; }
					style=sty;
					CFG.set("CallPanelStyle",""+style);
					syncStyle();
					break;
				}
			case 'i': { // set input channel
					CFG.set("InputChannel",p);
					PC.setInputPort(p);
					break;
				}
			case 'o': { // set output channel
					CFG.set("OutputChannel",p);
					PC.setOutputPort(p);
					break;
				}
			case 'u': { // set upstream profile
					int i;
					CodecProfileManager CPM=PC.getCodecProfileManager();
					try { i=Integer.parseInt(p); } catch (Exception ex) { break; }
					CFG.set("UpstreamProfile",""+i);
					Vector pr=CPM.getProfile(i);
					CPM.setUpstreamProfile(pr);
					break;
				}
			case 'd': { // set upstream profile
					int i;
					CodecProfileManager CPM=PC.getCodecProfileManager();
					try { i=Integer.parseInt(p); } catch (Exception ex) { break; }
					CFG.set("DownstreamProfile",""+i);
					Vector pr=CPM.getProfile(i);
					CPM.setDownstreamProfile(pr);
					break;
				}
//			case 'v': { // set IP version to use (4 or 6)
//					if (p.equals("4")) main.useIPv(4);
//					if (p.equals("6")) main.useIPv(6);
//					break;
//				}
			}		
			return;
		}
	}


	//////////////////////////////
	// PhoneBookListener interface
	/** Callback routine when an entry in the phone is selected (highlighted).*/
	public void selectEntry (PhoneBookEntry pbe) {
		// Load URL to the ToField
		ToField.setText(pbe.getSIPURL());
		// SubjectField.setText("");
   	}

	/** Callback routine when an entry is to be used (double click).*/
	public void useEntry (PhoneBookEntry pbe) {
		PC.issueCall(ToField.getText(),SubjectField.getText());
	}


	/////////////////////////
	// UINotifyable interface
	/** empty function, this part of the GUI is not interested in codec changes. */
	public void codecChanged (CodecDescriptor Codec) {}  // Re-Display current codec

	/** Callback routine when a new Call shows up in the PhoneController. */
	public void newCall (Call c) {    
		makeCallPanel(c);
		state.set(IPT_State_GUI.ST_CC);
		// handle AutoAnswer here
		sync();
	}

	/** Callback routine when a Call is removed from the PhoneController. */
	public void deletedCall (Call c) {
		CallPanel cp=(CallPanel)(callPanelMap.get(c));
		ph_calls.remove(cp);
		int n=PC.getCalls().size();
		int s=state.get();
		if (n==0 && s==IPT_State_GUI.ST_CC) { state.set(IPT_State_GUI.ST_IDLE); }
		sync();
	}


	//////////////
	// Constructor
	/** Build the GUI with predefined PhoneController, PhoneBook and Configuration. */
	public GUI (PhoneController PC, PhoneBook PB, Configuration CFG) {

		callPanelMap=new HashMap();
		Dimension d=new Dimension(310,220);
		int i;    

		this.PC=PC;
		this.PB=PB;
		this.CFG=CFG;

		CFG.setUnset("CallPanelStyle",""+Style_Classic);
		style=CFG.getAsInt("CallPanelStyle");

	    state=new IPT_State_GUI(IPT_State_GUI.ST_IDLE);
		fr1 = new JFrame("Bone Phone V"+MAJOR_VERSION+"."+MINOR_VERSION+"."+SUB_VERSION);

		ph_splash=new PanelHolder(d);
		ph_pbook =new PanelHolder(d);
		ph_calls =new PanelHolder(d);

		Vector splash=new Vector(5);
		splash.add(" "+CFG.getAsString("LogoLabel"));
		splash.add(new ImageIcon(CFG.getAsString("LogoImage")));
		splash.add(" ");
		splash.add(new ImageIcon("images/BonePhoneLogo.gif"));
		splash.add(versionstring);
		Vector devs=PC.getAudioDevices();
		for (i=0; i<devs.size(); i++) {
			splash.add("Device: "+(String)(devs.elementAt(i)));
		}
		//splash.add(" ");
		ph_splash.add(new SplashPanel(splash));

		ph_pbook.add(new PhoneBookPanel(PB,this));

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exc) { 
			System.err.println ("Can't set Look&Feel, aborting");
			System.exit(-1);
		}

		ButtonPanel= new JPanel();
		ButtonPanel.setLayout(new BoxLayout(ButtonPanel, BoxLayout.X_AXIS));
		left  = new JButton("Left");
		right = new JButton("Right");
		left.addActionListener(this);
		right.addActionListener(this);

		ButtonPanel.add(left);
		ButtonPanel.add(Box.createHorizontalGlue());
		ButtonPanel.add(right);
    
		ListScrollPane=new JScrollPane(ph_splash);
		ListScrollPane.setMinimumSize(d);
		// ListScrollPane.setMaximumSize(d);
		ListScrollPane.setPreferredSize(d);
		ListPanel=new JPanel();
		ListPanel.setBackground(Color.white);
		ListPanel.setLayout(new BoxLayout(ListPanel, BoxLayout.X_AXIS));
		ListPanel.add(ListScrollPane);
    
		SubjectPane=new JPanel();
		SubjectPane.setLayout(new BoxLayout(SubjectPane, BoxLayout.X_AXIS));
		SubjectPane.add(new JLabel("Subject:"));
		SubjectField=new JTextField();
		// SubjectField.addActionListener(guic.displ);
		SubjectPane.add(SubjectField);
		spo=new PaneOpener(fr1.getContentPane(),300,SubjectPane);

		actl=new AudioControlPanel(PC,CFG);
		vpo=new PaneOpener(fr1.getContentPane(),300,actl);
    
		ToPane=new JPanel();
		ToPane.setLayout(new BoxLayout(ToPane, BoxLayout.X_AXIS));
		ToPane.add(new JLabel("Call:"));
		ToField=new JTextField();
		ToPane.add(ToField);
		tpo=new PaneOpener(fr1.getContentPane(),300,ToPane);

		Container ContentPane;
		ContentPane=fr1.getContentPane();
		ContentPane.setLayout(new BoxLayout(ContentPane, BoxLayout.Y_AXIS));
		ContentPane.add(ListPanel);
		ContentPane.add(ButtonPanel);
		fr1.addWindowListener(new WindowAdapter () {
			public void windowClosing(WindowEvent e) {
				try { GUI.this.CFG.writeOut(); } catch (Exception ex) {}
				System.out.println("Tschuess !!!");
				System.exit(0);
			}
		}
		);

		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rb1,rb2,rb;
		menuBar = new JMenuBar();
		fr1.setJMenuBar(menuBar);

		menu = new JMenu("Connectivity");
		menu.setMnemonic(KeyEvent.VK_R);
		menu.getAccessibleContext().setAccessibleDescription("Set up/down codec profiles");
		menuBar.add(menu);

		JMenu upmenu = new JMenu("Upstream");
		JMenu downmenu = new JMenu("Downstream");
		ButtonGroup upgroup = new ButtonGroup();
		ButtonGroup downgroup = new ButtonGroup();
		ButtonGroup group = new ButtonGroup();
		CodecProfileManager CPM=PC.getCodecProfileManager();
		for (i=0; i<CPM.getNumberOfProfiles(); i++) {
			rb1 = new JRadioButtonMenuItem(CPM.getDisplayName(i));
			rb1.setActionCommand("u"+i);
			rb1.addActionListener(this);
			if (CFG.getAsInt("UpstreamProfile")==i) rb1.setSelected(true);
			rb2 = new JRadioButtonMenuItem(CPM.getDisplayName(i));
			rb2.setActionCommand("d"+i);
			rb2.addActionListener(this);
			if (CFG.getAsInt("DownstreamProfile")==i) rb2.setSelected(true);
			upgroup.add(rb1);
			upmenu.add(rb1);
			downgroup.add(rb2);
			downmenu.add(rb2);
		}
		menu.add(upmenu);
		menu.add(downmenu);
		menu.addSeparator();
		
		JCheckBoxMenuItem cbmi=new JCheckBoxMenuItem("Auto-answer");
		cbmi.setActionCommand("a");
		cbmi.addActionListener(this);
		cbmi.setSelected(CFG.getAsInt("AutoAnswer")!=0);
		menu.add(cbmi);

//		rb1 = new JRadioButtonMenuItem("use IPv4");
//		rb1.setActionCommand("v4");
//		rb1.addActionListener(this);
//		menu.add(rb1);
//		group.add(rb1);
//		rb2 = new JRadioButtonMenuItem("use IPv6");
//		rb2.setActionCommand("v6");
//		rb2.addActionListener(this);
//		menu.add(rb2);
//		group.add(rb2);
//		int ipv=CFG.getAsInt("IPVersion");
//		if (main.checkIPv6()) {
//			if (ipv==4)	rb1.setSelected(true);  
//			if (ipv==6)	rb2.setSelected(true);  
//		} else {
//			rb1.setSelected(true);
//			// rb2.setEnabled(false);
//		}
	
		menu = new JMenu("Ports");
		menu.setMnemonic(KeyEvent.VK_P);
		menu.getAccessibleContext().setAccessibleDescription("Set the audio in-/output port to use");
		menuBar.add(menu);
		group= new ButtonGroup();

		Vector p;  String s;
		p=PC.getOutputPorts();
		for (i=0; i<p.size(); i++) {
			s=(String)(p.elementAt(i));
			rb = new JRadioButtonMenuItem(s);
			rb.setActionCommand("o"+s);
			rb.addActionListener(this);
			if (CFG.getAsString("OutputChannel").equals(s)) {
				rb.setSelected(true);
			}
			group.add(rb);
			menu.add(rb);
		}

		menu.addSeparator();
		group=new ButtonGroup();

		p=PC.getInputPorts();
		for (i=0; i<p.size(); i++) {
			s=(String)(p.elementAt(i));
			rb = new JRadioButtonMenuItem(s);
			rb.setActionCommand("i"+s);
			rb.addActionListener(this);
			if (CFG.getAsString("InputChannel").equals(s)) {
				rb.setSelected(true);
			}
			group.add(rb);
			menu.add(rb);
		}

		menu = new JMenu("Style");
		menu.setMnemonic(KeyEvent.VK_S);
		menu.getAccessibleContext().setAccessibleDescription("Set the call rendering style");
		menuBar.add(menu);
		group=new ButtonGroup();
		JRadioButtonMenuItem mi;
		mi=new JRadioButtonMenuItem("Classic");
		mi.addActionListener(this);
		mi.setActionCommand("s"+Style_Classic);
		if (style==Style_Classic) mi.setSelected(true);
		group.add(mi);
		menu.add(mi);
		mi=new JRadioButtonMenuItem("Large");
		mi.addActionListener(this);
		mi.setActionCommand("s"+Style_Large);
		if (style==Style_Large) mi.setSelected(true);
		group.add(mi);
		menu.add(mi);

		sync();
		syncStyle();

		fr1.pack();
		fr1.setVisible(true);

		PC.addPhoneObserver(this);    
	} 

}  // class

