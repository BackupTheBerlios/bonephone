package examples.jainsip.shootist;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;
import jain.protocol.ip.sip.message.*;
import gov.nist.jain.protocol.ip.sip.message.*;
import gov.nist.jain.protocol.ip.sip.*;
import java.util.*;
import java.io.*;
import java.net.*;

/**
* This class, named after a popular John Wayne movie illustrates how to
* create a message using JAIN and send it up to a server. 
* Make sure the defaultRoute configfile line matches the location where 
* you want to shoot the message to....
*/
public class Shootist {
	protected static final String usageString =  
     "java examples.jainsip.Shootist msgFile";
	
	private static void usage() {
	   System.out.println(usageString);
	   System.exit(0);

	}

	public static void main(String args[]) {
	        SipFactory sipFactory = null;
	 	SipStackImpl 	 sipStack = null;
	 	SipProvider	 sipProvider = null;
		sipFactory = SipFactory.getInstance();
		sipFactory.setPathName("gov.nist");
		try {
         	     // Create SipStack object
             	     sipStack = (SipStackImpl)sipFactory.createSipStack();
                } catch(SipPeerUnavailableException e) {
                  // could not find 
		  // gov.nist.ri.jain.protocol.ip.sip.SipStackImpl
                  // in the classpath
                  System.err.println(e.getMessage());
                  System.exit(-1);
               } catch(SipException e) {
                 // could not create SipStack for some other reason
                 System.err.println(e.getMessage());
                 System.exit(-1);
               }
               sipStack.setStackName("JAIN-SIP Shootist");

	       try {
		    
		    Iterator listeningPoints = sipStack.getListeningPoints();
		    sipProvider = sipStack.createSipProvider
				((ListeningPoint)listeningPoints.next());
		    String msgFile = args[0];
		    File file = new File(msgFile);
		    FileInputStream fis = new FileInputStream(file);
		    byte[] b = new byte[4096];
		    fis.read(b);
		    String str = new String(b);
		    System.out.println("String = " + str);
		    RequestImpl request  = new RequestImpl(str);
		    for (int i = 0; i < 10; i++)  {
		      long tid =  sipProvider.sendRequest(request);
		      System.out.println("Send request tid = "+tid);
		    }
	        } catch (Exception ex) {
		   System.out.println(ex.getMessage());
		   ex.printStackTrace();
		   usage();
	         }

	}

}
