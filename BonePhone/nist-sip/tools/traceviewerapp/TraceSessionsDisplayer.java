package tools.traceviewerapp ;

import java.awt.* ;
import java.awt.event.* ;
import traceviewer.* ;


public class TraceSessionsDisplayer extends Dialog implements ItemListener{

	TraceSessions traceSessions = null ;
	TextArea text = null ;
	List sessions = null ;
	Button ok = new Button("Ok") ;
	
	public TraceSessionsDisplayer(TraceFrame tf,TraceSessions traceSessions){
		super(tf,"Sessions Displayer",true) ;
		this.traceSessions = traceSessions ;
		
		Panel mainPanel = new Panel() ;
		
		setLayout(new BorderLayout()) ;
		
		mainPanel.setLayout(new GridLayout(1,2)) ;
		text = new TextArea() ;
		text.setEditable(false) ;
		
		sessions = new List() ;
		
		mainPanel.add(sessions) ;
		mainPanel.add(text) ;
		
		add(mainPanel,BorderLayout.CENTER) ; 
		add(ok,BorderLayout.SOUTH) ;
		
		ok.addMouseListener(new MouseAdapter(){

					public void mouseClicked(MouseEvent e){
							setVisible(false) ;														
						}
					}
					) ;
		
		addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent e){
						setVisible(false) ;
					}
				}) ;
		
		for(int i = 0 ; i < traceSessions.size() ; i++)
			sessions.add(((TraceSession)traceSessions.elementAt(i)).getName()) ;		
		
		sessions.addItemListener(this) ;
		
		text.append("Select a session in the list to view its messages") ;
		
		Toolkit tk = Toolkit.getDefaultToolkit() ;
		setBounds(100,100,tk.getScreenSize().width - 200,tk.getScreenSize().height - 200) ;				
	}


	// Item Listener stuff
	
	public void itemStateChanged(ItemEvent e){
		int index = ((Integer)e.getItem()).intValue() ;
		
		text.setText("") ;
		
		TraceSession tS = (TraceSession)traceSessions.elementAt(index) ;
		
		for(int i = 0 ; i < tS.size() ; i++){
			TraceMessage tM = (TraceMessage) tS.elementAt(i) ;
			
			text.append("Message "+i+" from "+tM.getFrom()+" to "+tM.getTo()) ;
			text.append("\n\n") ;
			text.append(tM.getMessageString()) ;
			text.append("\n") ;
		}
		text.select(0,0) ;
	}

}