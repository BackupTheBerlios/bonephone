package tools.traceviewerapp ;

import java.awt.* ;
import java.awt.event.* ;
import java.io.* ;

import traceviewer.* ;


public class TraceHelpBox extends Dialog{

	TextArea text = null ;
	Button ok = null ;
	File helpFile = new File("help.txt") ;
	
	public TraceHelpBox(TraceFrame tf){
		super(tf,"TraceViewer Help",true) ;
		
		setLayout(new BorderLayout()) ;
		text = new TextArea() ;
		text.setEditable(false) ;
		
		// fill the help box.
		try{
		BufferedReader buffReader  = 
			new BufferedReader(
				new InputStreamReader(
					new FileInputStream(helpFile))) ;
		String line = null ;
		
		while((line = buffReader.readLine()) != null)					
			text.append(line+"\n") ;

		
		
		}catch(Exception e){
			System.out.println("Problem while opening the help file help.txt") ;
			e.printStackTrace() ;
		}
		ok = new Button("Ok") ;
		
		add(text,BorderLayout.CENTER) ;
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
		setSize(450,300) ;				
	}


}