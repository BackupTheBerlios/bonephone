package tools.traceviewerapp ;

import java.awt.* ;
import java.awt.event.* ;
import traceviewer.* ;


public class TraceSessionsList extends List implements ItemListener{

	TraceCanvas traceCanvas = null ;
	TraceSessions traceSessions = null ;

	public TraceSessionsList(TraceCanvas traceCanvas,TraceSessions traceSessions){
		
		this.traceSessions = traceSessions ;
		this.traceCanvas = traceCanvas ;

		
		for(int i = 0 ; i < traceSessions.size() ; i++)
			add(((TraceSession)traceSessions.elementAt(i)).getName()) ;		
		addItemListener(this) ;
		
		if(traceSessions.size() != 0)
			select(0) ;
	}
	
	public void setTraceSessions(TraceSessions traceSessions){
		removeAll() ;
		this.traceSessions = traceSessions ;

		for(int i = 0 ; i < traceSessions.size() ; i++)
			add(((TraceSession)traceSessions.elementAt(i)).getName()) ;		

		if(traceSessions.size() != 0) ;
			select(0) ;
	}
	
	public void itemStateChanged(ItemEvent e){
		int index = ((Integer)e.getItem()).intValue() ;
		traceCanvas.setTraceSession((TraceSession) traceSessions.elementAt(index)) ;
	}
	
}