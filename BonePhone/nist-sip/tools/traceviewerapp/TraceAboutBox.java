package tools.traceviewerapp ;

import java.awt.* ;
import java.awt.event.* ;
import traceviewer.* ;


public class TraceAboutBox extends Dialog implements WindowListener{

	TraceCanvas traceCanvas = null ;
	
	public TraceAboutBox(TraceFrame tf){
		super(tf,"About the NIST SIP TraceViewerApp",true) ;
		setResizable(false) ;
		addWindowListener(this) ;
		
		TraceSession tS = new TraceSession() ;
		
		TraceMessage tM1 = new TraceMessage() ;
		tM1.setFrom("NIST") ;
		tM1.setTo("SIP") ;
		tM1.setFirstLine("Ranga") ;
		tM1.setTime("Project Leader") ;
		tS.addElement(tM1) ;
		
		TraceMessage tM2 = new TraceMessage() ;
		tM2.setFrom("SIP") ;
		tM2.setTo("TraceViewerApp") ;
		tM2.setFirstLine("Marc Bednarek") ;
		tM2.setTime("Coder") ;
		tS.addElement(tM2) ;
		
		TraceMessage tM3 = new TraceMessage() ;
		tM3.setFrom("TraceViewerApp") ;
		tM3.setTo("SIP") ;
		tM3.setFirstLine("Olivier Deruelle") ;
		tM3.setTime("Coder") ;
		tS.addElement(tM3) ;
		
		TraceMessage tM4 = new TraceMessage() ;
		tM4.setFrom("SIP") ;
		tM4.setTo("NIST") ;
		tM4.setFirstLine("Christophe Chazeau") ;
		tM4.setTime("Coder") ;
		tS.addElement(tM4) ;
         	
         	traceCanvas = 
         		new TraceCanvas(tS,null,null) ;

		try{
			Toolkit tk = Toolkit.getDefaultToolkit() ;
		
			traceCanvas.COMPUTER_IMAGE = tk.createImage("comp.gif") ;
			traceCanvas.BACKGROUND_IMAGE = tk.createImage("faces.jpg") ;
			
			
			traceCanvas.repaint() ;
		}catch(Exception e){
			System.out.println("\nError when retrieving image comp.gif or faces.gif\n") ;
		}
			traceCanvas.setAnimationDelay(1) ;


         	// The ScrollPane for the Canvas
         	ScrollPane scroll = new ScrollPane(ScrollPane.SCROLLBARS_NEVER) ;
         	scroll.add(traceCanvas) ;
		
		// add everything to the frame
		
		setLayout(new BorderLayout()) ;
		add(scroll,BorderLayout.CENTER) ;


		Toolkit tk = Toolkit.getDefaultToolkit() ;

		setBounds((tk.getScreenSize().width - traceCanvas.getWidth()) / 2,
			(tk.getScreenSize().height - traceCanvas.getHeight()) / 2,
			traceCanvas.getWidth(),
			traceCanvas.getHeight() + 50 ) ;
		
		validateTree() ;
		
	}	
	// WindowListener stuff

	public void windowClosing(WindowEvent e){
		setVisible(false) ;
		traceCanvas.stopAnimation() ;
	}

	public void windowOpened(WindowEvent e){}
		
	public void windowActivated(WindowEvent e){
		traceCanvas.animate() ;		
		Toolkit tk = Toolkit.getDefaultToolkit() ;
		
		setBounds((tk.getScreenSize().width - traceCanvas.getWidth()) / 2,
			(tk.getScreenSize().height - traceCanvas.getHeight()) / 2,
			traceCanvas.getWidth(),
			traceCanvas.getHeight() + 50 ) ;
	}
        public void windowClosed(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	
}