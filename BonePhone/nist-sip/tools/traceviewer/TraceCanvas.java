
/* This class displays the trace logged by the server
 * Here is a description of the properties
 *
 *   -------------------------------------------------------  ^
 *   |                                                     |  |
 *   |       Actor 1          Actor 2         Actor 3      |  | 3
 *   |							   |  |
 *   |------------------------------------------------------  v ^
 *   |           |               |               |         |    | 4
 *   |           |-------------->|               |         |    v
 *   |           |               |               |         |
 *   |           |               |               |         |
 *   |           |               |-------------->|         |  ^  
 *   |           |               |               |         |  | 5
 *   |           |               |               |         |  |
 *   |           |               |<--------------|         |  v
 *   |           |               |               |         |
 *   |           |               |               |         |
 *   |           |<--------------|               |         |  ^
 *   |           |               |               |         |  | 6
 *   -------------------------------------------------------  v
 *                                                    7
 *         1                             2       <--------->
 *   <----------->               <--------------->
 *
 *
 *  1 : FIRST_ACTOR_GAP
 *  2 : HORIZONTAL_GAP
 *  3 : ACTORS_STRIPE
 *  4 : FIRST_ARROW_GAP
 *  5 : VERTICAL_GAP
 *  6 : LAST_ARROW_GAP
 *  7 : LAST_ACTOR_GAP
 */

package traceviewer ;

import java.awt.* ;
import java.awt.event.* ;
import java.util.* ;

public class TraceCanvas extends Canvas implements MouseListener , MouseMotionListener{
	
	// public properties

	public int FIRST_ACTOR_GAP = 100 ;
	public int HORIZONTAL_GAP = 350 ;
	public int ACTORS_STRIPE =70 ;	
	public int FIRST_ARROW_GAP = 30 ;
	public int VERTICAL_GAP = 40 ;
	public int LAST_ARROW_GAP = 30 ;
	public int LAST_ACTOR_GAP = 100 ;

	public Color BACKGROUND_COLOR = Color.white ;
	public Color FOREGROUND_COLOR = Color.black ;
	public Color SELECTED_COLOR = Color.red ;
	public Color ARROWS_COLOR = Color.blue ;
	
	public Image COMPUTER_IMAGE = null ;
	public Image BACKGROUND_IMAGE = null ;

	public boolean isAnimated = false ;
	
	// Variables

	Cursor overArrowCursor = new Cursor(Cursor.HAND_CURSOR) ;
	Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR) ;

	TraceAnimationThread traceAnimationThread = null ;

	TraceSession traceSession = null ;
	TextArea traceMessageTextArea = null ;
	TextArea traceSessionInfoTextArea = null ;
	
	Hashtable actors = new Hashtable() ;
	Vector arrows = new Vector() ;
	int indexSelected = 0;
	

	public TraceCanvas(TraceSession traceSession,
			 TextArea traceMessageTextArea,
			 TextArea traceSessionInfoTextArea){
		
		traceAnimationThread = new TraceAnimationThread(this) ;
		
		this.traceSessionInfoTextArea = traceSessionInfoTextArea ;
		this.traceMessageTextArea = traceMessageTextArea ;
		addMouseListener(this) ;
		addMouseMotionListener(this) ;
		setTraceSession(traceSession) ;
	} // Constructor
	
	public void setTraceSession(TraceSession traceSession){

		stopAnimation() ;
		this.traceSession = traceSession ;
		if(traceSessionInfoTextArea != null)
			traceSessionInfoTextArea.setText(traceSession.getInfo()) ;
		constructActors() ;
		constructArrows() ;
		
		indexSelected = 0 ;
		selectMessage(FIRST_ACTOR_GAP + HORIZONTAL_GAP / 2,ACTORS_STRIPE + FIRST_ARROW_GAP) ;
	
		repaint() ;
		if((getParent() != null)){
			if(getParent() instanceof ScrollPane)
				((ScrollPane) getParent()).setScrollPosition(0,0) ;
			getParent().validate() ;
		}


	} // setTraceSession

	public void constructArrows(){
		
		arrows.removeAllElements() ;
		
		for(int i = 0 ; i < traceSession.size() ; i++){
			TraceMessage tM = (TraceMessage) traceSession.elementAt(i) ;
			int x1 = ((Integer)actors.get(tM.getFrom())).intValue() ;
			int x2 = ((Integer)actors.get(tM.getTo())).intValue() ;
			int y = i * VERTICAL_GAP + ACTORS_STRIPE + FIRST_ARROW_GAP ;
			
			if(x1 == x2) // This is a loop !!
				arrows.addElement(new CircleArrow(x1, y, VERTICAL_GAP - 10,true)) ;
			else	// This is a straight arrow
				arrows.addElement(new StraightArrow(x1, x2, y, true)) ;				
			
		}

	} // getArrows
	
	public void constructActors(){
		
		actors.clear() ;
		
		for(int i = 0 ; i < traceSession.size() ; i++){
			String from = ((TraceMessage) traceSession.elementAt(i)).getFrom() ;
			String to = ((TraceMessage) traceSession.elementAt(i)).getTo() ;
			
			
			
			if(actors.get(from) == null){
				actors.put(from,new Integer(actors.size() * HORIZONTAL_GAP + FIRST_ACTOR_GAP)) ;
			}
			if(actors.get(to) == null){
				actors.put(to,new Integer(actors.size() * HORIZONTAL_GAP + FIRST_ACTOR_GAP)) ;

			}
		}
	} // getActors
	
	public void selectMessage(int x,int y){
		
		if(y > ACTORS_STRIPE && 
			y < ACTORS_STRIPE + FIRST_ARROW_GAP + (arrows.size() -1) * VERTICAL_GAP + LAST_ARROW_GAP){

			int index = Math.round((float) (y - ACTORS_STRIPE - FIRST_ARROW_GAP) / (float) VERTICAL_GAP)  ;
			
			
			Arrow arrow = (Arrow) arrows.elementAt(index) ;
			int xmin = arrow.xmin() ;	
			int xmax = arrow.xmax() ;	
				
			
			if(x > xmin && x < xmax && arrow.visible){
				indexSelected = index ;
				if(traceMessageTextArea != null)
					traceMessageTextArea.setText(((TraceMessage)traceSession.elementAt(indexSelected)).getMessageString()) ;
				
				repaint() ;
			}
		}
	} // selectMessage
	
	
	public boolean isOnArrow(int x,int y){
		boolean retval = false ;

		if(y > ACTORS_STRIPE && 
			y < ACTORS_STRIPE + FIRST_ARROW_GAP + (arrows.size() - 1) * VERTICAL_GAP + LAST_ARROW_GAP){
			int index = Math.round((float) (y - ACTORS_STRIPE - FIRST_ARROW_GAP) / (float) VERTICAL_GAP)  ;
			
			
			if((index <= (arrows.size() - 1)) && (index >= 0)){
			
				Arrow arrow = (Arrow) arrows.elementAt(index) ;
				int xmin = arrow.xmin() ;	
				int xmax = arrow.xmax() ;
				
				if(x > xmin && x < xmax && arrow.visible)
					retval = true ;
				else 
					retval = false ;
			}
			else
				retval = false ;
				
		}
		else
			retval = false ;
		
		return retval ;
	}
	
	public int getWidth(){
		return (actors.size() - 1) * HORIZONTAL_GAP + FIRST_ACTOR_GAP + LAST_ACTOR_GAP ;
	} // getWidth
	
	public int getHeight(){
		return ACTORS_STRIPE + FIRST_ARROW_GAP + (arrows.size() - 1) * VERTICAL_GAP + LAST_ARROW_GAP ;
	} // getWidth
	
	public Vector getArrows(){
		return arrows ;
	} // getArrows
	
	public void animate(){
		isAnimated = true ;
		setAllArrowsVisible(false) ;
		traceAnimationThread.start() ;
		repaint() ;
	} // animate
	
	public void stopAnimation(){
		isAnimated = false ;
		traceAnimationThread.stop() ;
		setAllArrowsVisible(true) ;	
		repaint() ;		
		
	} // stopAnimation
	
	public void setAllArrowsVisible(boolean visible){
		for(int i = 0 ; i < arrows.size() ; i++){
			Arrow a = (Arrow) arrows.elementAt(i) ;
			a.visible = visible ;
		}
	} // setAllArrowsVisible
	
	public void setAnimationDelay(int delay){
		traceAnimationThread.setDelay(delay) ;
	} // setAnimationDelay
	
	public int getAnimationDelay(){
		return traceAnimationThread.getDelay() ;
	} // setAnimationDelay
	
	public void paint(Graphics g){
		
		// set the size with what we have
		Dimension d = new Dimension((actors.size() - 1) * HORIZONTAL_GAP + FIRST_ACTOR_GAP + LAST_ACTOR_GAP,
			ACTORS_STRIPE + FIRST_ARROW_GAP + (arrows.size() - 1) * VERTICAL_GAP + LAST_ARROW_GAP) ;
		
		setSize(Math.max((actors.size() - 1) * HORIZONTAL_GAP + FIRST_ACTOR_GAP + LAST_ACTOR_GAP,getParent().getSize().width),
			Math.max(ACTORS_STRIPE + FIRST_ARROW_GAP + (arrows.size() - 1) * VERTICAL_GAP + LAST_ARROW_GAP,getParent().getSize().height) + LAST_ARROW_GAP) ;
		
		if(getParent() != null){
			getParent().doLayout() ;
			getParent().validate() ;
		}
		
		// if we have a BACKGROUND_IMAGE, fill the back with it
		// otherwise fill with the background color
		if(BACKGROUND_IMAGE != null && 
			BACKGROUND_IMAGE.getWidth(this) != -1 &&
			BACKGROUND_IMAGE.getHeight(this) != -1 ){
			
			int imWidth = BACKGROUND_IMAGE.getWidth(this) ;
			int imHeight = BACKGROUND_IMAGE.getHeight(this) ;
			
			int nbImagesX = Math.max(getSize().width,d.width) / imWidth + 1 ;
			int nbImagesY = Math.max(getSize().height,d.height) / imHeight + 1 ;
			
			for(int i = 0 ; i < nbImagesX ; i++)
				for(int j = 0 ; j < nbImagesY ; j++)
					g.drawImage(BACKGROUND_IMAGE,i * imWidth, j * imHeight,this) ;
		}else{
			g.setColor(BACKGROUND_COLOR) ;
			g.fillRect(0,0,getSize().width,getSize().height) ;
		}
		
		
		// separation line
		g.setColor(FOREGROUND_COLOR) ;
		g.drawLine(0,ACTORS_STRIPE,d.width,ACTORS_STRIPE) ;

		// draw the actors above the separation line and their vertical line
		Enumeration e  = actors.keys() ;
		while(e.hasMoreElements()){
			Object o = e.nextElement() ;
			int x = ((Integer)actors.get(o)).intValue() ;
				
			// if we have an image for the actors display it
			// otherwise just do nothing
			
			g.drawString((String)o,
					x - getFontMetrics(g.getFont()).stringWidth((String) o) / 2 ,
					ACTORS_STRIPE - 5) ;
			
			if(COMPUTER_IMAGE != null)
				g.drawImage(COMPUTER_IMAGE,x - COMPUTER_IMAGE.getWidth(this) / 2 ,
				ACTORS_STRIPE - getFontMetrics(g.getFont()).getHeight() - COMPUTER_IMAGE.getHeight(this) - 10 ,
				this) ;
			
			// Vertical line
			g.drawLine(x,ACTORS_STRIPE,x,d.height) ;
		}
			
		// draw the arrows and information
		for(int i = 0 ; i < traceSession.size() ; i++){
			if(i == indexSelected)
				g.setColor(SELECTED_COLOR) ;
			else
				g.setColor(ARROWS_COLOR) ;

			Arrow a = (Arrow) arrows.elementAt(i) ;
			if(a.visible){
				TraceMessage tM = (TraceMessage) traceSession.elementAt(i) ;
				a.draw(this,g,tM) ;
			} // if
		
		}// for
	
	} // paint
	
	public void update(Graphics g){
        	paint(g);
    	} // update

	// MouseListener stuff

	public void mouseClicked(MouseEvent e){}

	public void mouseEntered(MouseEvent e){}

	public void mouseExited(MouseEvent e){}

	public void mousePressed(MouseEvent e){
		selectMessage(e.getX(),e.getY()) ;
	}

	public void mouseReleased(MouseEvent e){}
	
	
	//MouseMotionListener
	
	public void mouseDragged(MouseEvent e){
	}
          
 	public void mouseMoved(MouseEvent e){
 			if(isOnArrow(e.getX(),e.getY())) 
 				setCursor(overArrowCursor) ;
 			else
 				setCursor(defaultCursor) ;
 	}
          


}
