package bonephone;

import javax.swing.*;
import java.awt.*;

/** A PanelHolder is a JPanel with 2 usable predefined Layouts: vertical BoxLayout and vertical
 * GridLayout (one column). In a PanelHolder CallPanels are inserted (added).
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
class PanelHolder extends JPanel {

	private Dimension msize;

	// public Dimension getPreferredSize() {
	//	return msize;
	// }	

	/** Overridden to remember set MinimumSize.
	 * @param d Minimum width and height.
	*/
	public void setMinimumSize(Dimension d) {
		super.setMinimumSize(d);
		msize=d;
	}

	/** Use GridLayout for this PanelHolder.
	 * The Grid is created with One column, any rows.
	*/
	public void useGridLayout() {
		setLayout(new GridLayout(0,1));
	}	
		

	/** Use BoxLayout for this PanelHolder.
	 * The widgets are organized vertically.
	*/
	public void useBoxLayout() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}	


	/** Create with minimum size (height and width).
	 * @param m desired size.
	*/		
	PanelHolder (Dimension m) { 
		super();
		msize=m;
		// setMinimumSize(m);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.white);
	}
	
}


