package bonephone;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * abstract base class to realize basic functions for a Custom Call panel.
 * A Call panel is ab object class which renders a call for display and provides
 * user functions (buttons) to send events to a call.
 *
 * @author Jens Fiedler (jfi@fokus.gmd.de)
*/
abstract public class CallPanel extends JPanel implements Observer, ActionListener
{
	/**
	 * reference to the call this CallPanel (its subclass) is displaying.
	*/
	protected Call mycall;

	/**
	 * is this call to be automatically accepted ?
	*/
	protected boolean autoanswer;

	/**
	 * detach() is called by a GUI to tell this CallPanel that it is no more
	 * responsible for its call object. This is done when a GUI wishes to 
     * have another CallPanel instance manage the Call.
	*/
	public void detach() {
		mycall.deleteObserver(this);
		setVisible(false);
	}

	/**
	 * Standard constructor. Load all defined icons (see Fields).
	 *
	 * @param c Call to manage.
	 * @param autoanswer flag if this call is automatically accepted.
	*/
	protected CallPanel (Call c, boolean autoanswer) {
		super();
		this.autoanswer=autoanswer;
		if (icons==null) {
			int i;
			int n=iconindices.length;
			icons=new ImageIcon[n];
			for (i=0; i<n; i++) {
				icons[iconindices[i]]=new ImageIcon(iconpaths[i]);
			}
		}
		mycall=c;
		c.addObserver(this);
	}


	/**
	 * This function handles the button callbacks (ActionListener).
	 *
	 * When implementing a subclass for CallPanel, define your buttons to 
	 * use these ActionCommands (for the ActionEvent):<br>
	 * "cancel" to cancel a call that is not established right now.<br>	
	 * "hangup" to hang up an established call.<br>
	 * "mute" to mute a call (peer-to-peer).<br>
	 * "unmute" to unmute an inactive call (peer-to-peer).<br>
	 * "accept" to accept a ringing incoming call.<br>
	 * "callback" to call the caller of a missed call.<br>
	*/
	public void actionPerformed(ActionEvent e) {
		String ac=e.getActionCommand();
		if (ac.equals("cancel"))   { mycall.cancel(); }
		if (ac.equals("hangup"))   { mycall.hangup(); }
		if (ac.equals("mute"))     { mycall.mute(); }
		if (ac.equals("unmute"))   { mycall.unmute(); }
		if (ac.equals("accept"))   { mycall.accept(); }
		if (ac.equals("callback")) { mycall.callback(); }
	}
	
	
	/**
	 * All CallPanel classes observe their call objects to notice state changes.
	 * So any subclass should overwrite this function and query its calls state in it and
	 * react in a redisplay.
	 * 
	 * @param o The observed call.
	*/
	abstract public void update (Observable o, Object arg);


	/**
	 * handle AutoAnswer. subclasses must call this function to handle AutoAnswer !!!
	 *
	 * @return true on auto answered, false on not auto replied.
	*/
	protected boolean autoAnswer () {
		if (mycall.getState()==IPT_State_Call.ST_RINGING && autoanswer) {
			mycall.accept();
			return true;
		}
		return false;
	}

	/** index of the "blank" icon in the icons array */
	protected final static int IconIdx_Blank           = 0;   

	/** index of the "plugs" icon in the icons array */
	protected final static int IconIdx_Plugs           = 1;   

	/** index of the "xbox" icon in the icons array */
	protected final static int IconIdx_Xbox            = 2;

	/** index of the "redspot" icon in the icons array */
	protected final static int IconIdx_Redspot         = 3;

	/** index of the "circle" icon in the icons array */
	protected final static int IconIdx_Circle          = 4;

	/** index of the "blacksppt" icon in the icons array */
	protected final static int IconIdx_Blackspot       = 5;

	/** index of the "whitebarredspot" icon in the icons array */
	protected final static int IconIdx_Whitebarredspot = 6;

	/** index of the "bell" icon in the icons array */
	protected final static int IconIdx_Bell            = 7;

	/** index of the "watch" icon in the icons array */
	protected final static int IconIdx_Watch           = 8;

	/** index of the "sanduhr" icon in the icons array */
	protected final static int IconIdx_Sanduhr         = 9;

	/** index of the "lightning" icon in the icons array */
	protected final static int IconIdx_Lightning       = 10;

	/** index of the "re-arrow" icon in the icons array */
	protected final static int IconIdx_Rearrow         = 11;

	/** index of the "portrait" icon in the icons array */
	protected final static int IconIdx_Portrait        = 12;

	/** index of the "M" icon in the icons array */
	protected final static int IconIdx_M               = 13;

	/** index of the "phonered" icon (hangup) in the icons array */
	protected final static int IconIdx_Phonered        = 14;

	/** index of the "phonegreen" icon (pick up) in the icons array */
	protected final static int IconIdx_Phonegreen      = 15;

	/** index of the "lips" icon (speak))in the icons array */
	protected final static int IconIdx_Lips            = 16;

	/** index of the "crosslips" icon (mute) in the icons array */
	protected final static int IconIdx_Crosslips       = 17;

	/** Array of ImageIcons for subclasses to acces by the above IconIdx_ indices */
	protected static ImageIcon icons[]=null;


	private static int iconindices[]={IconIdx_Plugs,
	                                    IconIdx_Xbox,
	                                    IconIdx_Blank,
	                                    IconIdx_Redspot,
	                                    IconIdx_Circle,
	                                    IconIdx_Blackspot,
	                                    IconIdx_Whitebarredspot,
	                                    IconIdx_Bell,
	                                    IconIdx_Watch,
	                                    IconIdx_Sanduhr,
	                                    IconIdx_Lightning,
	                                    IconIdx_Rearrow,
	                                    IconIdx_Portrait,
	                                    IconIdx_M,
	                                    IconIdx_Phonered,
	                                    IconIdx_Phonegreen,
	                                    IconIdx_Crosslips,
	                                    IconIdx_Lips
	                                   };

	private static String iconpaths[]={ 
	                                      "images/plugs.gif",
	                                      "images/xbox.gif",
	                                      "images/blank.gif",
	                                      "images/redspot.gif",
	                                      "images/circle.gif",
	                                      "images/blackspot.gif",
	                                      "images/whitebarredspot.gif",
	                                      "images/bell.gif",
	                                      "images/watch.gif",
	                                      "images/sanduhr.gif",
	                                      "images/lightning.gif",
	                                      "images/rearrow.gif",
	                                      "images/unknownguy2.gif",
	                                      "images/m.gif",
	                                      "images/phonered.gif",
	                                      "images/phonegreen.gif",
	                                      "images/crosslips.gif",
	                                      "images/lips.gif"
	                                    };

}
