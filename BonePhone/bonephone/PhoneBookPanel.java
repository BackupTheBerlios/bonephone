package bonephone;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/** GUI class to display the phonebook and handle user requests on it by 
 * calling interface function on a PhoneBookListener.
*/
class PhoneBookPanel extends JPanel implements MouseListener
{
	private HashMap lab;

	private PhoneBook PB;
	private PhoneBookListener PBL;
  
	/** Standard constructor.
	 * @param PB PhoneBook to display.
	 * @param PBL Listener to notify on use/select events. Only on PhoneBookListener
     * is supported.
	*/
	public PhoneBookPanel (PhoneBook PB, PhoneBookListener PBL) {
		int i;
		JLabel l;
		PhoneBookEntry pbe;

		this.PB=PB;
		this.PBL=PBL;
		lab=new HashMap();

		setAlignmentX(Container.LEFT_ALIGNMENT);
		setAlignmentY(Container.TOP_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));		
		
		setBackground(Color.white);

		for (i=0; i<PB.size(); i++) {
			pbe=PB.getPhoneBookEntry(i);
			l=new JLabel(""+(i+1)+". "+pbe.getName());	
			l.addMouseListener(this);
			l.setForeground(Color.black);
			l.setBackground(Color.white);
			l.setAlignmentX(Container.LEFT_ALIGNMENT);
			lab.put(l,pbe);
			add(l);
		}
	}


	/** part of the MouseListener Interface to gain mouse events. Empty function. */
	public void mouseEntered (MouseEvent e) {}

	/** part of the MouseListener Interface to gain mouse events. Empty function. */
	public void mouseExited  (MouseEvent e) {}

	/** part of the MouseListener Interface to gain mouse events. Empty function. */
	public void mousePressed (MouseEvent e) {}

	/** part of the MouseListener Interface to gain mouse events. Empty function. */
	public void mouseReleased(MouseEvent e) {}

	/** part of the MouseListener Interface to gain mouse events. Only function used from that interface. */
	public void mouseClicked (MouseEvent e) {
    	int i,clickcount;
    	JLabel l;
		Component c=e.getComponent();
    	if (!(c instanceof JLabel)) { return; }
		l=(JLabel)c;
    	clickcount=e.getClickCount();
    	if (clickcount==1) { // mark the row
    		int j;
			Component[] cs = getComponents();
    		for (j=0; j<cs.length; j++) {
				if (!(cs[j] instanceof JLabel)) continue;
    			if (cs[j]==c) {
    				c.setBackground(Color.black);
    				c.setForeground(Color.red);
    			} else {
    				cs[j].setBackground(Color.white);
    				cs[j].setForeground(Color.black);
    			}
    		}
			PhoneBookEntry p=(PhoneBookEntry)(lab.get(l));
			PBL.selectEntry(p);
    	}
    	if (clickcount==2) {
			PhoneBookEntry p=(PhoneBookEntry)(lab.get(l));
			PBL.useEntry(p);
    	}
    	
    }

}
