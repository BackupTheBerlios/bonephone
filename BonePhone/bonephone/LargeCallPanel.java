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
public class LargeCallPanel extends CallPanel 
{

	private JButton button1,button2;
	private JLabel  label1, label2, label3;
	private JPanel  sp=null;
	private JPanel  ssp1,ssp2,ssp3;
	private JLabel  portrait;


	/**
	 * Overriding setBackground from JPanel.
	 *
	 * @param c Color to use.
	*/
	public void setBackground(Color c) {
		super.setBackground(c);
		if (sp==null) return;
		sp.setBackground(c);
		ssp1.setBackground(c);
		ssp2.setBackground(c);
		ssp3.setBackground(c);
	}


    /**
     * standard constructor.
     *
     * All Widgets are build here and arranged in the extended JPanel
     *
     * @param c Call to manage.
     * @param autoanswer flag if this is to be automaticall accepted.
    */
	public LargeCallPanel (Call c, boolean autoanswer) {
		super(c,autoanswer);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX((float)0.0);
		sp=new JPanel();
		ssp1=new JPanel();
		ssp2=new JPanel();
		ssp3=new JPanel();
		button1=new JButton();
		button2=new JButton();
		label1=new JLabel();
		label2=new JLabel();
		label3=new JLabel();
		label1.setAlignmentX(Container.LEFT_ALIGNMENT);
		label1.setIconTextGap(2);
		label2.setIconTextGap(2);
		portrait=new JLabel();
		button1.addActionListener(this);
		button1.setActionCommand("n");
		button2.addActionListener(this);
		button2.setActionCommand("n");
		Insets ix=new Insets(0,0,0,0);
		button1.setMargin(ix);
		button2.setMargin(ix);
		ssp3.setLayout(new BoxLayout(ssp3, BoxLayout.X_AXIS));
		ssp3.add(label1);
		ssp2.setLayout(new BoxLayout(ssp2, BoxLayout.X_AXIS));
		ssp2.add(new JLabel(icons[IconIdx_Blank]));
		ssp2.add(button1);
		ssp2.add(Box.createRigidArea(new Dimension(30,0)));
		ssp2.add(button2);
		ssp1.setLayout(new BoxLayout(ssp1, BoxLayout.X_AXIS)); 
		ssp1.add(label2);
		ssp1.add(Box.createRigidArea(new Dimension(5,0)));
		ssp1.add(label3);
		ssp1.setAlignmentX(Container.LEFT_ALIGNMENT);
		ssp2.setAlignmentX(Container.LEFT_ALIGNMENT);
		ssp3.setAlignmentX(Container.LEFT_ALIGNMENT);
		sp.setLayout(new BoxLayout(sp, BoxLayout.Y_AXIS));
		setBackground(Color.white);
		sp.add(ssp1);
		sp.add(ssp3);
		sp.add(ssp2);
		add(portrait);
		add(sp);

		label1.setText("Subject: "+mycall.getSubject());
		label1.setIcon(icons[IconIdx_Blank]);
		label1.setForeground(Color.blue);
		label2.setForeground(Color.yellow);
		label3.setForeground(Color.black);
		String name=mycall.getPeer();
		if (name.substring(0,4).equalsIgnoreCase("sip:")) name=name.substring(4);
		label3.setText(name);
		portrait.setIcon(icons[IconIdx_Portrait]);

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
		int s=mycall.getState();
		    
		switch (s) {
			case IPT_State_Call.ST_CALLING: {
				button1.setIcon(icons[IconIdx_Phonered]);
				button1.setActionCommand("cancel");
				button1.setToolTipText("Hang Up");
				button2.setEnabled(false);
				button2.setVisible(false);
				button2.setActionCommand("nothing");
				label2.setIcon(icons[IconIdx_Sanduhr]);
				label2.setText(null);
				label2.setForeground(Color.black);
				setBackground(Color.white);
				break;					    	
			}
			case IPT_State_Call.ST_RINGING: {
				button1.setIcon(icons[IconIdx_Phonered]);
				button1.setActionCommand("cancel");
				button1.setToolTipText("Hang Up");
				button2.setEnabled(true);
				button2.setVisible(true);
				button2.setIcon(icons[IconIdx_Phonegreen]);
				button2.setToolTipText("Pick Up");
				button2.setActionCommand("accept");
				label2.setIcon(icons[IconIdx_Bell]);
				setBackground(Color.white);
				break;					    	
			}
			case IPT_State_Call.ST_ACTIVE: {
				button1.setIcon(icons[IconIdx_Phonered]);
				button1.setActionCommand("hangup");
				button1.setToolTipText("Hang Up");
				button2.setEnabled(true);
				button2.setVisible(true);
				button2.setIcon(icons[IconIdx_Crosslips]);   
				button2.setToolTipText("Mute call");
				button2.setActionCommand("mute");
				label2.setIcon(icons[IconIdx_Redspot]);
				setBackground(Color.green);
				break;					    	
			}
			case IPT_State_Call.ST_INACTIVE: {
				button1.setIcon(icons[IconIdx_Phonered]);
				button1.setActionCommand("hangup");
				button1.setToolTipText("Hang Up");
				button2.setEnabled(true);
				button2.setVisible(true);
				button2.setIcon(icons[IconIdx_Lips]);
				button2.setToolTipText("Unmute call");
				button2.setActionCommand("unmute");
				label2.setIcon(icons[IconIdx_Crosslips]);
				setBackground(Color.gray);
				break;	    	
			}
			case IPT_State_Call.ST_ERROR: {
				button1.setIcon(icons[IconIdx_Xbox]);
				button1.setActionCommand("cancel");
				button1.setToolTipText("Dismiss");
				button2.setEnabled(false);
				button2.setVisible(false);
				button2.setActionCommand("nothing");
				label2.setIcon(icons[IconIdx_Lightning]);
				label2.setText(mycall.getError());
				label2.setForeground(Color.yellow);
				setBackground(Color.red);
				break;					    	
			}
			case IPT_State_Call.ST_DELETE: {
				setVisible(false);
				break;					    	
			}
			case IPT_State_Call.ST_MUTING: {
				button1.setIcon(icons[IconIdx_Phonered]);
				button1.setActionCommand("hangup");
				button1.setToolTipText("Hang Up");
				button2.setEnabled(false);
				button2.setVisible(false);
				button2.setActionCommand("nothing");
				label2.setIcon(icons[IconIdx_Blackspot]);
				setBackground(Color.white);
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
				label2.setIcon(icons[IconIdx_M]);
				setBackground(Color.cyan);
				break;					    	
			}
			default: {
				button1.setIcon(icons[IconIdx_Xbox]);
				button1.setActionCommand("cancel");
				button1.setToolTipText("Cancel");
				button2.setEnabled(false);
				button2.setVisible(false);
				button2.setActionCommand("nothing");
				label2.setIcon(icons[IconIdx_Lightning]);
				label2.setText("Unknown state "+s+". "+mycall.getError()+" ");
				setBackground(Color.red);
	    	}
		} // switch

	}

}
