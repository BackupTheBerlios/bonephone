package traceviewer ;

import java.awt.* ;

public class StraightArrow extends Arrow{

	public int x1 = 0 ;
	public int x2 = 0;
    	public int y = 0 ;

    	public StraightArrow(int x1, int x2, int y, boolean flag){
	
		super(flag,y) ;
        
	        this.x1 = x1 ;
        	this.x2 = x2 ;
        	this.y = y ;
    }

	public int xmin(){
		return Math.min(x1,x2) ;	
	}
	
	public int xmax(){
		return Math.max(x1,x2) ;	
	}

	public void draw(TraceCanvas tC, Graphics g, TraceMessage tM){

		g.drawLine(x1,y,x2,y) ;
		g.drawLine(Math.min(x1,x2) + 2,y-1,Math.max(x1,x2) - 2 ,y-1) ;
		g.drawLine(Math.min(x1,x2) + 2,y+1,Math.max(x1,x2) - 2,y+1) ;
		
		int timeStringWidth = g.getFontMetrics(g.getFont()).stringWidth(tM.getTime())  ;
		int fistLineStringWidth = g.getFontMetrics(g.getFont()).stringWidth(tM.getFirstLine())  ;
			
		if(x2 > x1){
			g.drawString(tM.getFirstLine(),
			x1 + tC.HORIZONTAL_GAP / 2 - fistLineStringWidth / 2 , y - 5) ;
				
			g.drawString(tM.getTime(),
				x1 + tC.HORIZONTAL_GAP / 2 - timeStringWidth / 2 ,
				y  + g.getFontMetrics(g.getFont()).getHeight()) ;


			g.drawLine(x2,y,x2 - 10,y - 5) ;
			g.drawLine(x2 - 1 ,y,x2 - 11,y - 5) ;
			g.drawLine(x2 - 2 ,y,x2 - 12,y - 5) ;
			g.drawLine(x2,y,x2 - 10,y + 5) ;
			g.drawLine(x2 - 1 ,y,x2 - 11,y + 5) ;
			g.drawLine(x2 - 2 ,y,x2 - 12,y + 5) ;
		}else{
			g.drawString(tM.getFirstLine(),
				x2 + tC.HORIZONTAL_GAP / 2 - fistLineStringWidth / 2 ,
				y - 2) ;
				
			g.drawString(tM.getTime(),
				x2 + tC.HORIZONTAL_GAP / 2 - timeStringWidth / 2 ,
				y + 2 + g.getFontMetrics(g.getFont()).getHeight()) ;
			
			g.drawLine(x2,y,x2 + 10,y + 5) ;
			g.drawLine(x2 + 1 ,y,x2 + 11,y + 5) ;
			g.drawLine(x2 + 2 ,y,x2 + 12,y + 5) ;
			g.drawLine(x2,y,x2 + 10,y - 5) ;	
			g.drawLine(x2 + 1 ,y,x2 + 11,y - 5) ;
			g.drawLine(x2 + 2 ,y,x2 + 12,y - 5) ;		
		} // else	
	}

}