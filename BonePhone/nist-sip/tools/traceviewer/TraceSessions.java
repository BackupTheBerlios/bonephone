package traceviewer ;

import java.util.* ;
import java.io.* ;
import java.text.* ;

public class TraceSessions extends Vector implements Serializable{

	String name = null ;

	public TraceSessions(){
		super() ;
	}
	
	public TraceSessions(String name){
		super() ;
		this.name = name ;
	}

	public void setName(String name){
		this.name = name ;	
	}
	
	public String getName(){
		return name ;
	}

	public void sortChronologically(){ 
		/* Sorts the sessions in this vector by chronological order
		   of the first Message of each session                     */
		
		try{
		
			SimpleDateFormat dF = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy") ;
		
			// Dirty quick coded sorting. Performance doesn't matter that much as one never has
			// an enourmous amount of sessions anyway
		
			for(int i = 0 ; i < this.size() ; i++){
		
				for(int j = i ; j < this.size() ; j++){
					
					TraceSession traceSession1 = (TraceSession) this.elementAt(i) ;
					TraceMessage traceMessage1 = (TraceMessage) traceSession1.elementAt(0) ;
					Date date1 = dF.parse(traceMessage1.getTime()) ;
					
					TraceSession traceSession2 = (TraceSession) this.elementAt(j) ;
					TraceMessage traceMessage2 = (TraceMessage) traceSession2.elementAt(0) ;
					Date date2 = dF.parse(traceMessage2.getTime()) ;
		
			
		
					if(date2.compareTo(date1) < 0 ){
					
						TraceSession temp = traceSession1 ;
					
						this.setElementAt(this.elementAt(j),i) ;
						this.setElementAt(temp,j) ;
					}
				}
			}
		}catch(Exception e){
			System.out.println("Exception : Non-sortable TraceSessions") ;
			System.out.println("The date is probably not formatted the right way") ;
			System.out.println("Stack Trace :") ;			
			e.printStackTrace() ;
		}
		
		
		
				
	}
}