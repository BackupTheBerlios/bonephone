package bonephone;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * Implementation of the "Classic" bonephone CallPanel
 *
 * @author Jens Fiedler (jfi@fokus.gmd.de)
*/
public class ClassicCallPanel extends CallPanel 
{

	private JButton button1,button2;
	private JLabel  label1, label2, label3;

	/**
	 * standard constructor.
	 *
	 * All Widgets are build here and arranged in the extended JPanel
	 * 
	 * @param c Call to manage.
	 * @param autoanswer flag if this is to be automaticall accepted.
	*/
	public ClassicCallPanel (Call c, boolean autoanswer) {
		super(c,autoanswer);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX((float)0.0);
		button1=new JButton();
		button2=new JButton();
		label1=new JLabel();
		label2=new JLabel();
		label3=new JLabel();
		button1.addActionListener(this);
		button1.setActionCommand("n");
		button2.addActionListener(this);
		button2.setActionCommand("n");
		Dimension d=new Dimension(icons[0].getIconWidth(),icons[0].getIconHeight());
		Insets ix=new Insets(0,0,0,0);
		button1.setMargin(ix);
		button2.setMargin(ix);
		add(button1);
		add(button2);
		add(label1);
		add(label2);
		add(label3);
		update(c,null);
	}


	/**
	 * Called whenever the managed call changes state. 
	 * 
	 * re-arrange Panel, re-arrange button functionality ... REDISPLAY
	 *
	 * @param o observed Call.
	*/
	public void update (Observable o, Object arg) 	{ 
		if (autoAnswer()) return;
		String name;
		int s=mycall.getState();
		    
			// try to find the SIPURL in our phonebook
			// pindex=findInPBook(a.getPeer());
			// if (pindex>=0) name=GLink.PB.getPhoneBookEntry(pindex).getName();
			// else name=a.getPeer();
			// labeltext=name;

		name=mycall.getPeer();

		switch (s) {
			case IPT_State_Call.ST_CALLING: {
				button1.setIcon(icons[IconIdx_Plugs]);
				button1.setActionCommand("cancel");
				button1.setToolTipText("Hang Up");
				button2.setEnabled(false);
				button2.setVisible(false);
				button2.setActionCommand("nothing");
				label1.setIcon(icons[IconIdx_Watch]);
				label1.setText(name);
				label1.setForeground(Color.black);
				label2.setText(null);
				break;					    	
			}
			case IPT_State_Call.ST_RINGING: {
				button1.setIcon(icons[IconIdx_Plugs]);
				button1.setActionCommand("cancel");
				button1.setToolTipText("Hang Up");
				button2.setEnabled(true);
				button2.setVisible(true);
				button2.setIcon(icons[IconIdx_Bell]);
				button2.setToolTipText("Pick Up");
				button2.setActionCommand("accept");
				label1.setForeground(Color.black);
				label1.setText(name);
				label1.setIcon(null);
				label2.setText(null);
				break;					    	
			}
			case IPT_State_Call.ST_ACTIVE: {
				button1.setIcon(icons[IconIdx_Plugs]);
				button1.setActionCommand("hangup");
				button1.setToolTipText("Hang Up");
				button2.setEnabled(true);
				button2.setVisible(true);
				button2.setIcon(icons[IconIdx_Redspot]);
				button2.setToolTipText("Mute call");
				button2.setActionCommand("mute");
				label1.setForeground(Color.black);
				label1.setText(name);
				label1.setIcon(null);
				label2.setText(null);
				break;					    	
			}
			case IPT_State_Call.ST_INACTIVE: {
				button1.setIcon(icons[IconIdx_Plugs]);
				button1.setActionCommand("hangup");
				button1.setToolTipText("Hang Up");
				button2.setEnabled(true);
				button2.setVisible(true);
				button2.setIcon(icons[IconIdx_Circle]);
				button2.setToolTipText("Unmute call");
				button2.setActionCommand("unmute");
				label1.setForeground(Color.black);
				label1.setIcon(null);
				label1.setText(name);
				label2.setText(null);
				break;	    	
			}
			case IPT_State_Call.ST_ERROR: {
				button1.setIcon(icons[IconIdx_Xbox]);
				button1.setActionCommand("cancel");
				button1.setToolTipText("Dismiss");
				button2.setEnabled(false);
				button2.setVisible(false);
				button2.setActionCommand("nothing");
				label1.setIcon(icons[IconIdx_Lightning]);
				label1.setText(mycall.getError()+" ");
				label1.setForeground(Color.red);
				label2.setForeground(Color.black);
				label2.setText(name);
				break;					    	
			}
			case IPT_State_Call.ST_DELETE: {
				setVisible(false);
				break;					    	
			}
			case IPT_State_Call.ST_MUTING: {
				button1.setIcon(icons[IconIdx_Plugs]);
				button1.setActionCommand("hangup");
				button1.setToolTipText("Hang Up");
				button2.setEnabled(false);
				button2.setVisible(false);
				button2.setActionCommand("nothing");
				label1.setIcon(icons[IconIdx_Blackspot]);
				label1.setForeground(Color.black);
				label1.setText(name);
				label2.setText(null);
				break;					    	
			}
			case IPT_State_Call.ST_MISSED: {
				button1.setIcon(icons[IconIdx_Xbox]);
				button1.setActionCommand("cancel");
				button1.setToolTipText("Dismiss");
				button2.setEnabled(true);
				button2.setVisible(true);
				button2.setIcon(icons[IconIdx_Rearrow]);
				button2.setToolTipText("Call back");
				button2.setActionCommand("callback");
				label1.setIcon(null);
				label1.setForeground(Color.black);
				label1.setText(name);
				label2.setText(null);
				break;					    	
			}
			default: {
				button1.setIcon(icons[IconIdx_Xbox]);
				button1.setActionCommand("cancel");
				button1.setToolTipText("Cancel");
				button2.setEnabled(false);
				button2.setVisible(false);
				button2.setActionCommand("nothing");
				label1.setIcon(null);
				label1.setText("Unknown state "+s+". "+mycall.getError()+" ");
				label1.setForeground(Color.red);
				label2.setForeground(Color.black);
				label2.setText(name);
		    	}
		} // switch

		setBackground(Color.white);		
	}

}
