package bonephone;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;



// ----------------- Listener for Audio popin -------------------
/**
 * Class to display and control audio device volume and muting. Also display current codec in use
 * for transmission. 
 *
 * @author Jens Fiedler (jfi@fokus.gmd.de)
*/
class AudioControlPanel extends JPanel implements ActionListener, ChangeListener, UINotifyable {

	private boolean active[];
	private final static int Idx_MuteIn     =  0;
	private final static int Idx_MuteOut    =  1;
	private final static int NIcons         =  2;
	private ImageIcon icons[];

	private Configuration CFG;
	private PhoneController PC;
	
	private JLabel  currentcodec;
	private JButton buttons[];
	private JSlider volin,volout;  


	/** 
	 * Called whenever a volume slider changes state (Callback function).
	*/
	public void stateChanged(ChangeEvent e) { 
		int volume;
		Object o=e.getSource();
		if (!(o instanceof JSlider)) return;
		JSlider s=(JSlider) o;
		volume=s.getValue();
		if (s==volout) { 
			PC.setOutputVolume(volume);
			return;
		}
		if (s==volin) {
			PC.setInputVolume(volume);
		}
		return;
	}


	/** 
	 * Called whenever a mute button is pressed (Callback function).
	*/
	public void actionPerformed(ActionEvent e) {
		Object o=e.getSource();
		if (!(o instanceof JButton)) return;
		JButton b=(JButton) o;
		int i;
		for (i=0; i<NIcons; i++) 
			if (b==buttons[i]) break;
		toggle(i);
	}        		


	/** To mute/disable:  act=false
	    To unmute/enable: act=true  
	**/
	private void onoff (int media, boolean act) {
		if (media>=NIcons) return;
		active[media]=act;
		buttons[media].setIcon(icons[media+(act?0:NIcons)]);
		switch (media) {
			case Idx_MuteOut : {
				PC.setOutputMute(!act);
				break;
			}
			case Idx_MuteIn : {
				PC.setInputMute(!act);
				break;
			}
		}
	}


	private void toggle (int media) {
		if (media>=NIcons) return;
		if (active[media]) onoff (media, false);
		else onoff (media, true);
	}


	/////////////////////////
	// UINotifyable interface

	/** 
	 * Called by underlying PhoneController to indicate a codec change (upon call establishment).
	 *
	 * @param Codec new codec in use by the audio engine.
	*/
    public void codecChanged (CodecDescriptor Codec) {   // Re-Display current codec
		currentcodec.setText(Codec.toString());		
	}

	/** 
	 * Function is empty.
	*/
    public void newCall (Call c) {}

	/** 
	 * Function is empty.
	*/
    public void deletedCall (Call c) {}


	/**
	 * Standard constructor.
	 *
	 * @param PC PhoneController to use to set volume/muting.
	 * @param CFG Configuration to take defaults from.
	*/
	AudioControlPanel (PhoneController PC, Configuration CFG) { 
		int i;
		Dimension d;
		int cvi; // config volume input
		int cvo; // config volume output

		this.CFG=CFG;
		this.PC=PC;
		
		icons=new ImageIcon[NIcons*2];
		icons[Idx_MuteIn        ]=new ImageIcon("images/muteina.gif");
		icons[Idx_MuteIn+NIcons ]=new ImageIcon("images/muteini.gif");
		icons[Idx_MuteOut       ]=new ImageIcon("images/muteouta.gif");
		icons[Idx_MuteOut+NIcons]=new ImageIcon("images/muteouti.gif");

		active=new boolean[NIcons];
		active[Idx_MuteIn]=CFG.getAsInt("InputMute")==0 ? true : false;
		active[Idx_MuteOut]=CFG.getAsInt("OutputMute")==0 ? true : false;

		setAlignmentX(Container.CENTER_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel v1=new JPanel();
		JPanel v2=new JPanel();
		JPanel v3=new JPanel();
		v1.setAlignmentX(Container.CENTER_ALIGNMENT);
		v2.setAlignmentX(Container.CENTER_ALIGNMENT);
		v3.setAlignmentX(Container.CENTER_ALIGNMENT);
		v1.setLayout(new BoxLayout(v1, BoxLayout.X_AXIS));	
		v2.setLayout(new BoxLayout(v2, BoxLayout.X_AXIS));	
		v3.setLayout(new BoxLayout(v3, BoxLayout.Y_AXIS));	
		buttons= new JButton[NIcons];
		Insets ix=new Insets(0,0,0,0);
		for (i=0; i<NIcons; i++) {
			buttons[i]=new JButton(icons[i]);
			// d=new Dimension(icons[i].getIconWidth(),icons[i].getIconHeight());
			buttons[i].setMargin(ix);
			// buttons[i].setPreferredSize(d);
			// buttons[i].setMaximumSize(d);
			// buttons[i].setMinimumSize(d);
			buttons[i].addActionListener(this);
		}
		volin=new JSlider(0,100);
		volin.setMajorTickSpacing(50);
		volin.setMinorTickSpacing(10);
		volin.setPaintTicks(true);
		volin.setPaintLabels(false);
		volin.setValue(cvi=CFG.getAsInt("InputVolume"));
		volin.addChangeListener(this);
		volout=new JSlider(0,100);
		volout.setMajorTickSpacing(50);
		volout.setMinorTickSpacing(10);
		volout.setPaintTicks(true);
		volout.setPaintLabels(false);
		volout.addChangeListener(this);
		volout.setValue(cvo=CFG.getAsInt("OutputVolume"));
		PC.setInputVolume(cvi);
		PC.setOutputVolume(cvo);
		currentcodec=new JLabel("none");
		currentcodec.setAlignmentX(Container.LEFT_ALIGNMENT);
		v1.add(buttons[Idx_MuteIn]);
		v1.add(volin);
		v2.add(buttons[Idx_MuteOut]);
		v2.add(volout);
		v3.add(currentcodec);
		add(v1);
		add(v2);
		add(v3);

		for (i=0; i<NIcons; i++) onoff(i,active[i]);

		PC.addPhoneObserver(this);
	}


}
