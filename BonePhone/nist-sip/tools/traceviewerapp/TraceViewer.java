package tools.traceviewerapp ;

import java.awt.* ;
import java.awt.event.* ;
import java.net.* ;
import java.io.* ;
import java.util.* ;
import java.rmi.*;
import traceviewer.* ;
import gov.nist.sip.stack.*;


public class TraceViewer{

		static String rmiport = "1099" ;
		static String rmihost = "127.0.0.1" ;


/*
	public static String getRMIPort(){
		return rmiport ;	
	}

	public static String getRMIHost(){
		return rmihost ;	
	}
*/	
	public static void main(String args[]){

		// Crappy param parsing.

		
		if(args.length == 0){
			System.out.println("Using default parameters:") ;
			TraceSessions tss = processRMI(rmihost,rmiport) ;
			System.out.println("### Everything is Ok ... Launching browser ###") ;
			TraceFrame tF = new TraceFrame(tss,true) ;
			tF.setVisible(true) ;

		}
		else if(args[0].equals("-f")){
			
			System.out.println("View from files, processing ...") ;
			TraceSessions tss = processFiles(args) ;
			System.out.println("### Everything is Ok ... Launching browser ###") ;
			TraceFrame tF = new TraceFrame(tss,false) ;
			tF.setVisible(true) ;
	
		}
		else if(args[0].equals("-rmihost")||
			args[0].equals("-rmiport")){
			
			if(args.length != 2 &&
				args.length != 4)
				usage() ;
			else{
				/* really dirty */
				
				if(args[0].equals("-rmihost"))
					rmihost = args[1] ;
				if(args[0].equals("-rmiport"))
					rmiport = args[1] ;
				
				if(args.length == 4){
				
					if(args[2].equals("-rmihost"))
						rmihost = args[3] ;
				
					if(args[2].equals("-rmiport"))
						rmiport = args[3] ;
				}
				TraceSessions tss = processRMI(rmihost,rmiport) ;
				System.out.println("### Everything is Ok ... Launching browser ###") ;
				TraceFrame tF = new TraceFrame(tss,true) ;
				tF.setVisible(true) ;

			}			
		}else
			usage() ;
	}
	
	public static TraceSessions processFiles(String args[]){
		TraceSessions retval = null ;
		
		try{
		
			TraceFileParser parser = new TraceFileParser(new FileInputStream(args[1])) ;
			TraceSessions tSs = new TraceSessions() ;
			TraceSession tS = parser.parse() ;
			tS.setName(args[1]) ;
			tSs.add(tS) ;
			
			for(int i = 1 ; i < args.length ; i++){
				
				System.out.println("Parsing file :"+args[i]) ;							
				parser.setXMLsource(new FileInputStream(args[i])) ;
				tS = parser.parse() ;
				tS.setName(args[i]) ;
				tSs.add(tS) ;
			}

		retval = tSs ;


		}catch(Exception e){
			System.out.println("Exception parsing problem ...") ;
			e.printStackTrace() ;
		}
		retval.sortChronologically() ;
		return retval ;
	}
	
	public static TraceSessions processRMI(String host,String port){
		
		System.out.println("Getting the logs via RMI with those parameters :") ;
		System.out.println("	rmihost : "+host) ;
		System.out.println("	rmiport : "+port) ;
		
		TraceSessions retval = null ;
		
  		try { 
	    		String name = "//" + host ; 
	    		name += ":" + port;
	    		name += "/gov.nist.sip.stack.MessageLogTableImpl";

                        System.out.println("Path to registry : ") ;
                        System.out.println(name) ;

                  	MessageLogTable table = (MessageLogTable) Naming.lookup(name);
          
	                StringTokenizer st = new StringTokenizer(table.keys());

		     	TraceFileParser tfp = new TraceFileParser("") ; 
		     	TraceSessions tss = new TraceSessions() ;
		      	TraceSession ts = null ;

	             	if(st.hasMoreTokens()){

		             	while (st.hasMoreTokens()) {

				    String key = (String) st.nextElement();

	    			    System.out.println("*************************************");
		    		    System.out.println("*** Trace for " + key+" requested ***");
		    	    	    System.out.println("*************************************");

	    
				    String trace = table.flush(key);
			    	    System.out.println("### "+key+" ###");

        			    System.out.println("@@@ trace @@@");
		    
			    	    System.out.println(trace) ;
		    
		    		    tfp.setXMLsource(trace) ;
		    	    	    ts = tfp.parse() ;
		    	    	    ts.setName(key) ;
	         	    	    tss.add(ts) ;
		    	    	    tss.setName("Test") ;
		}
			
		retval = tss ;
	    }
	    else{
	        
		System.out.println("****************************************************") ;
		System.out.println("*** Nothing logged in the registry yet, relaunch ***") ;
		System.out.println("****************************************************") ;
		System.exit(0) ;
	    }


        } catch (Exception e) {

		System.out.println("**************************************************************") ;
		System.out.println("*** Exception while retrieving trace from the RMI registry ***") ;
		System.out.println("**************************************************************") ;
		e.printStackTrace() ;
            	System.exit(0) ;
	}
	retval.sortChronologically() ;
	return retval ;
	}

	public static void usage(){
		System.out.println("***************************************") ;	
		System.out.println("*** missing or incorrect parameters ***") ;	
		System.out.println("*************************************\n") ;	
		System.out.println("Usage (if classpath correctly set) :") ;
		System.out.println(" --> java tools.traceviewerapp.TraceViewer -f files") ;	
		System.out.println(" --> java tools.traceviewerapp.TraceViewer -rmihost rmihost -rmiport rmiport") ;	
		System.out.println("The values are defaulted to rmihost: 127.0.0.1 and rmiport: 1099") ;	
		System.out.println("If no parameters are set, it is equivalent to run that command :") ;	
		System.out.println("java tools.traceviewerapp.TraceViewer -rmihost 127.0.0.1 -rmiport 1099") ;	

		System.exit(0) ;	
	}


}