package tools.traceviewerapp ;

import java.awt.* ;
import java.awt.event.* ;
import java.net.* ;
import java.io.* ;
import traceviewer.* ;


public class TraceFrame extends Frame implements MouseListener{

	// GUI Elements we need to have the control on or
	// pass to other elements
	
	TraceCanvas traceCanvas = null ;
	TraceSessionsList traceSessionsList = null ;
	TraceSessions traceSessions = null ;
	TraceAboutBox traceAboutBox = null ;
	
	TraceSessionsDisplayer traceSessionsDisplayer = null ;
	TraceHelpBox traceHelpBox = null ;
	
	Button display = null ;
	Button help = null ;
	Button quit = null ;
	Button refresh = null ;
	Button about = null ;
	
	public TraceFrame(TraceSessions traceSessions, boolean refreshflag){

		this.traceSessions = traceSessions ;
		
		// Build the widgets in the correct order for proper references
         	TraceMessageTextArea traceMessageTextArea = new TraceMessageTextArea() ;
         	
         	traceCanvas = 
         		new TraceCanvas((TraceSession) traceSessions.elementAt(0),
         				traceMessageTextArea,
         				null) ;
         	traceSessionsList = new TraceSessionsList(traceCanvas,traceSessions) ;



         	// Buttons
         	
         	display = new Button("Display all messages") ;
         	help = new Button("Help") ;
         	quit = new Button("Quit") ;
		about = new Button("About") ;
		refresh = new Button("Refresh") ;
		refresh.setEnabled(refreshflag) ;


         	// Listeners
         	display.addMouseListener(this) ;
         	help.addMouseListener(this) ;
         	quit.addMouseListener(this) ;
         	refresh.addMouseListener(this) ;
         	about.addMouseListener(this) ;
         	
         	// Sessions Displayer
         	traceSessionsDisplayer = new TraceSessionsDisplayer(this,traceSessions) ;

		// Help
		traceHelpBox = new TraceHelpBox(this) ;

		// About
		traceAboutBox = new TraceAboutBox(this) ;

         	// The ScrollPane for the Canvas
         	ScrollPane scroll = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS) ;
         	scroll.add(traceCanvas) ;
		
		// add everything to the frame
		
		Panel viewPanel = new Panel() ;
		Panel controlsPanel = new Panel() ;
		
		controlsPanel.setLayout(new GridLayout(1,5)) ;
		controlsPanel.add(display) ;
		controlsPanel.add(refresh) ;
		controlsPanel.add(help) ;
		controlsPanel.add(about) ;
		controlsPanel.add(quit) ;
						
		
		setLayout(new BorderLayout()) ;
		add(viewPanel,BorderLayout.CENTER) ;
		add(controlsPanel,BorderLayout.NORTH) ;
		
         	viewPanel.setLayout(new BorderLayout()) ;
         	
         	Panel leftPanel = new Panel() ;
         	
         	Panel sessionsListPanel = new Panel() ;
         	Panel selectedMessageInfoPanel = new Panel() ;

		sessionsListPanel.setLayout(new BorderLayout()) ;
		sessionsListPanel.add(new Label("List of the sessions :"),BorderLayout.NORTH) ;
         	sessionsListPanel.add(traceSessionsList,BorderLayout.CENTER) ;
         	
         	selectedMessageInfoPanel.setLayout(new BorderLayout()) ;
         	selectedMessageInfoPanel.add(new Label("Selected Message Content :"),BorderLayout.NORTH) ;
         	selectedMessageInfoPanel.add(traceMessageTextArea,BorderLayout.CENTER) ;
	         	
         	leftPanel.setLayout(new GridLayout(2,1)) ;
		leftPanel.add(sessionsListPanel) ;         	
		leftPanel.add(selectedMessageInfoPanel) ;         	
		
	
		viewPanel.add(leftPanel,BorderLayout.WEST) ;
		viewPanel.add(scroll,BorderLayout.CENTER) ;
		
		addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					System.exit(1) ;
				}
			}) ;
		
	
		setSize(1024,768) ;
		setTitle("NIST SIP Trace Applet") ;
		
		validateTree() ;
	}

    // MouseListener Stuff

    public void mouseClicked(MouseEvent mouseevent){
    	
             
       	Button button = (Button)mouseevent.getComponent();
               
	if(button == quit){
		System.exit(1) ;		
	}
	if(button == help){
       		traceHelpBox.setVisible(true) ;
	}      
       	if(button == display){
		traceSessionsDisplayer.setVisible(true) ;
 	}       	
	if(button == refresh){
	
		System.out.println("Refresh required.") ;
		 
	        traceCanvas.stopAnimation();
      		traceSessions = TraceViewer.processRMI(TraceViewer.rmihost,
            							TraceViewer.rmiport);
       		traceSessionsList.setTraceSessions(traceSessions);
       		traceCanvas.setTraceSession((TraceSession)traceSessions.elementAt(0));
	 	}       	
       	if(button == about){
		traceAboutBox.setVisible(true) ;
 	}  		
    }
    
    public void mouseEntered(MouseEvent mouseevent){}

    public void mouseExited(MouseEvent mouseevent){}

    public void mousePressed(MouseEvent mouseevent){

    	
    }

    public void mouseReleased(MouseEvent mouseevent){}


}