package org.mbus.apps;

import java.awt.*;
import javax.swing.*;

public class ExJDialog extends JDialog {

    Container dummy;
    

    public final static Font ENTER  = new Font("Courier",Font.PLAIN,12);
    public final static Font HEADING= new Font("Helvetica",Font.BOLD+Font.ITALIC,14);
	
    //-------------------------------------------------------------------------
    /** 
     * Erzeugt einen Dialog mit einem GridBagLayout 
     */
    public ExJDialog(Frame parent) {
	super(parent);
	dummy = this.getContentPane();
	dummy.setLayout(new GridBagLayout());
    }
    //-------------------------------------------------------------------------
    /** 
     * Erzeugt einen Dialog mit einem GridBagLayout 
     */
    public ExJDialog(Frame parent, boolean modal) {
	super(parent, modal);
	dummy = this.getContentPane();
	dummy.setLayout(new GridBagLayout());
    }
    //-------------------------------------------------------------------------
    /** 
     * Erzeugt einen Dialog mit einem GridBagLayout 
     */
    public ExJDialog(Frame parent,String title) {
	super(parent, title);
	dummy = this.getContentPane();
	dummy.setLayout(new GridBagLayout());
    }
    //-------------------------------------------------------------------------
    /** 
     * Erzeugt einen Dialog mit einem GridBagLayout 
     */
    public ExJDialog(Frame parent, String title,boolean modal) {
	super(parent, title, modal);
	dummy = this.getContentPane();
	dummy.setLayout(new GridBagLayout());
    }
    
    
    //-------------------------------------------------------------------------
    /** 
     * Plaziert eine Komponente in einem GridBag-Layoutmanager 
     */
    public void place(Component co, int x, int y, int w, int h, int a) {
	GridBagConstraints c = new GridBagConstraints();
	c.gridx     = x;
	c.gridy     = y;
	c.gridwidth = w;
	c.gridheight= h;
	c.fill      = GridBagConstraints.NONE;
	c.anchor    = a;
	c.weightx   = 0.0;
	c.weighty   = 0.0;
	c.insets = new Insets(3,3,3,3);
	((GridBagLayout)dummy.getLayout()).setConstraints(co, c);
	dummy.add(co);
    }
    //-------------------------------------------------------------------------
    /** 
     * Plaziert eine Komponente in einem GridBag-Layoutmanager 
     * und ermöglicht das Bestimmten der Ränder 
     */
    public void place(Component co, int x, int y, int w, int h, int a, int ob, int un,
		      int li, int re) {
	GridBagConstraints c = new GridBagConstraints();
	c.gridx     = x;
	c.gridy     = y;
	c.gridwidth = w;
	c.gridheight= h;
	c.fill      = GridBagConstraints.NONE;
	c.anchor    = a;
	c.weightx   = 0.0;
	c.weighty   = 0.0;
	c.insets = new Insets(ob,li,un,re);
	((GridBagLayout)dummy.getLayout()).setConstraints(co, c);
	dummy.add(co);
    }
    
}




