package traceviewer ;

import java.awt.* ;

public class CircleArrow extends Arrow{

	public int x = 0 ; 
	public int y = 0 ;
    	public int diameter = 0 ;

    	public CircleArrow(int x, int y, int diameter, boolean flag){
	
		super(flag,y) ;

        	this.x = x ;
        	this.y = y ;
        	this.diameter = diameter ; 
    }
	
	public int xmin(){
		return x ;	
	}
	
	public int xmax(){
		return x + diameter ;	
	}

	public void draw(TraceCanvas tC, Graphics g, TraceMessage tM){

		/* Draw the circle */
		
		g.drawOval( x, y - (diameter / 2), diameter, diameter) ;
		g.drawOval( x - 1, y - (diameter / 2) - 1, diameter + 2, diameter + 2) ;

		/* Display the first line of the message */
		int timeStringWidth = g.getFontMetrics(g.getFont()).stringWidth(tM.getTime())  ;
		int fistLineStringWidth = g.getFontMetrics(g.getFont()).stringWidth(tM.getFirstLine())  ;

		g.drawString(tM.getFirstLine(),
			x + diameter + 5 + tC.HORIZONTAL_GAP / 2 - fistLineStringWidth / 2 , y - 5) ;
				
		g.drawString(tM.getTime(),
			x + diameter + 5 + tC.HORIZONTAL_GAP / 2 - timeStringWidth / 2 ,
			y  + g.getFontMetrics(g.getFont()).getHeight()) ;
		
		/* Draw the head of the arrow */			
		
		g.drawLine(x, y, x - 3, y + 10) ;
		g.drawLine(x, y, x + 7, y + 7) ;
		
		g.drawLine(x - 1, y, x - 4, y + 10) ;
		g.drawLine(x + 1, y, x + 8, y + 7) ;
		
		g.drawLine(x - 2, y, x - 5, y + 10) ;
		g.drawLine(x + 2, y, x + 9, y + 7) ;
		
	}
}