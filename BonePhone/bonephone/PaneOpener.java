package bonephone;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;



// --------------- Common Panel Opener -------------------

/** Class to open/close extra panels in the GUI. i.e. Audio control, "To:" Field, "Subject:" Field.
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
class PaneOpener {
	
	private     boolean open;
	private     JPanel      pane;
	private	    int width;
	private		Container con;

	/** Open (insert) the hosted JPanel in the Container.
	 * @param pos index in the Container where to insert the panel.
	*/
	public void open(int pos) {
		if (open) return;
		Dimension d=pane.getMinimumSize();
		d.width=width;
		pane.setMaximumSize(d);
		pane.setMinimumSize(d);
		pane.setPreferredSize(d);
   		con.add(pane,pos);  // add at postion pos
   		con.validate();
		open=true;
	}
	
	/** Close the panel (remove from Container). */
	public void close() {
		if (!open) return;
   		con.remove(pane);
   		con.validate();
   		open=false;
	}

	/** Check if the panel is currently open.
	 * @return true if panel is open, otherwise false.
	*/
	public boolean isOpen() { return open; }

	/** Standard Contructor.
	 * @param con Container to host the panel when opened.
	 * @param width Width to set for hosted panel when opening.
	 * @param pane JPanel to host in Container.
	*/
	PaneOpener(Container con, int width, JPanel pane) { 
		this.con=con; 
		this.width=width; 
		this.pane=pane;
		open=false; 
	}

}

