package bonephone;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/** Panel to display the title screen.
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
class SplashPanel extends JPanel
{
	/** @param items Vector of String or Icon objects to display in the title screen.
	 * The items are displayed in one column and are centered in their row.
	*/
	public SplashPanel (Vector items) {

		int i;
		JLabel l;

		setAlignmentX(Container.LEFT_ALIGNMENT);
		setAlignmentY(Container.TOP_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		for (i=0; i<items.size(); i++) {
			Object o=items.elementAt(i);
			l=null;
			if (o instanceof String) { l=new JLabel((String)o); }
			if (o instanceof Icon)   { l=new JLabel((Icon)o); }
			if (l==null) continue;
			l.setForeground(Color.black);
			l.setBackground(Color.white);
			l.setAlignmentX(Container.CENTER_ALIGNMENT);
			add(l);
		}
		setBackground(Color.white);
	}

}
