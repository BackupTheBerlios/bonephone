package traceviewer ;
import java.awt.* ;

abstract class Arrow{

	public boolean visible = true ;
	public int y = 0 ;

	abstract int xmin() ;
	abstract int xmax() ;

	abstract void draw(TraceCanvas tC, Graphics g, TraceMessage tM) ;
	
	public Arrow(boolean flag , int y){
		this.y = y ;
		visible = flag ;
	}
}
