package org.mbus.apps;

import java.awt.*;
import javax.swing.*;

public class ExJPanel extends JPanel {

    public final static Font ENTER  = new Font("Courier",Font.PLAIN,12);
    public final static Font HEADING= new Font("Helvetica",Font.BOLD+Font.ITALIC,14);
	
    //-------------------------------------------------------------------------
    /** 
     * Erzeugt ein Panel mit einem GridBagLayout 
     */
    public ExJPanel() {
	super();
	this.setLayout(new GridBagLayout());
    }
    
    //-------------------------------------------------------------------------
    /** 
     * Erzeugt ein Panel mit einem bel. Layoutmanager 
     */
    public ExJPanel(LayoutManager lm) {
	super(lm);
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
	((GridBagLayout)this.getLayout()).setConstraints(co, c);
	this.add(co);
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
	((GridBagLayout)this.getLayout()).setConstraints(co, c);
	this.add(co);
    }

    //-------------------------------------------------------------------------
    /** 
     * Plaziert eine Komponente in einem GridBag-Layoutmanager 
     * und ermöglicht das Bestimmten der Ränder 
     */
    public void place(Component co, int x, int y, int w, int h, int a, int ob, int un,
		      int li, int re, int fill, double wx, double wy) {
	GridBagConstraints c = new GridBagConstraints();
	c.gridx     = x;
	c.gridy     = y;
	c.gridwidth = w;
	c.gridheight= h;
	c.fill      = fill;
	c.anchor    = a;
	c.weightx   = wx;
	c.weighty   = wy;
	c.insets = new Insets(ob,li,un,re);
	((GridBagLayout)this.getLayout()).setConstraints(co, c);
	this.add(co);
    }
    
}
