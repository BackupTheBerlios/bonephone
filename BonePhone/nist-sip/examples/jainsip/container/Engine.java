package examples.jainsip.container;
import jain.protocol.ip.sip.*; 
import jain.protocol.ip.sip.address.*; 
import jain.protocol.ip.sip.header.*; 
import jain.protocol.ip.sip.message.*; 
import java.util.*; 
import java.io.*;
  

/**
* This is an event engine that is built using the JAIN-SIP specification.
* This example illustrates my idea on what a JAIN service platform ought 
* to do. 
*/ 

public class Engine {
     protected SipFactory sipFactory = null; 
     protected AddressFactory addressFactory = null; 
     protected HeaderFactory headerFactory = null; 
     protected MessageFactory messageFactory = null; 
     protected SipStack sipStack = null; 
     protected SipProvider sipProvider = null; 
     protected Iterator listeningPoints = null; 
	
     protected Hashtable serviceTable;

     protected  void registerServices( String filename) {
	serviceTable = new Hashtable();

	try {
	   BufferedReader bis = new BufferedReader(new FileReader(filename));
	   if (bis == null)  throw new 
		IOException("Could not read config file");
	   while (true) {
		String ln  = bis.readLine();
		if (ln == null) break;
		String line = ln.trim();
		if (line.compareTo("") == 0) continue;
		else if (line.charAt(0) == '#') continue;
		StringTokenizer st = new StringTokenizer(line);
		String tok = st.nextToken();
		// Load service module for TCP connections.
		if (tok.compareToIgnoreCase("tcp") == 0) {
		    String tcpService = st.nextToken();
		    Class serviceClass = Class.forName(tcpService);
		    serviceTable.put(tok,serviceClass);
		} else if (tok.compareToIgnoreCase("udp") == 0) {
		    String udpService = st.nextToken();
		    Class serviceClass = Class.forName(udpService);
		    serviceTable.put(tok,serviceClass);
		}
	   }
	} catch (IOException ex) {
		System.err.println("File not found or not readable " 
					+ filename);
		System.exit(0);
	} catch (ClassNotFoundException ex) {
		System.err.println("Service class not found " +
				ex.getMessage());
		System.exit(0);
	} 

     }


    class Listener implements Runnable {
	protected JAINService service;
	protected Thread myThread;
	protected ListeningPoint listeningPoint;

	protected Listener(ListeningPoint lpoint ) {
		System.out.println("creating new Listener!");
		try {
		   listeningPoint = lpoint;
		   String transport = listeningPoint.getTransport();
		   Class serviceClass =
				(Class) serviceTable.get(transport);
		   if (serviceClass == null) {
			System.out.println("No service registered for " +
					transport);
			return;
		   }
		   service = (JAINService) serviceClass.newInstance();
		   service.setListeningPoint(listeningPoint);
		} catch (ClassCastException ex) {
		   System.err.println("Bad class for service");
		   ex.printStackTrace();
		} catch (InstantiationException ex) {
		   System.err.println("Could not instatiate service");
		} catch  (IllegalAccessException ex) {
		   System.err.println("Could not create service " + 
				ex.getMessage());

		}
	        // Create a new thread for this service.
		myThread = new Thread(this);

	}

	
	protected void start() { myThread.start(); }

	public void run() {
	  try {
	     sipProvider = sipStack.createSipProvider(listeningPoint);
	     service.setSipProvider(sipProvider);
	     sipProvider.addSipListener(service);
	     service.setSipStack(sipStack);
	   } catch(ListeningPointUnavailableException e) { 
	      System.err.println("Could not start service thread");
              System.err.println(e.getMessage()); 
           } catch(TooManyListenersException e) {
             // A limit has been reached on the number of
             // Listeners allowed per provider
             System.err.println(e.getMessage());
           } catch(SipListenerAlreadyRegisteredException e) {
             // Already been added as SipListener
             System.err.println(e.getMessage());
           }

	}

    }
	
     

     protected Engine(String servicesFile) { 
        
	 // Register services 
	 this.registerServices(servicesFile);
	 System.out.println("Registered Services!!");
         // Obtain an instance of the singleton SipFactory 
         sipFactory = SipFactory.getInstance(); 

         // Set path name of SipFactory to implementation 
         sipFactory.setPathName("gov.nist"); 

         try { 
         // Create SipStack object 
             sipStack = (SipStack)sipFactory.createSipStack(); 
         } catch(SipPeerUnavailableException e) { 
             // could not find gov.nist.ri.jain.protocol.ip.sip.SipStackImpl 
             // in the classpath 
             System.err.println(e.getMessage()); 
             System.exit(-1); 
         } catch(SipException e) { 
             // could not create SipStack for some other reason 
             System.err.println(e.getMessage()); 
             System.exit(-1); 
         } 
         sipStack.setStackName("JAIN-SIP Service Engine"); 
         try { 

             // Get SipStack's ListeningPoints 
             listeningPoints = sipStack.getListeningPoints(); 

	     System.out.println("Got the listeningPoints");
		
	     while (listeningPoints.hasNext()) {
		ListeningPoint li = (ListeningPoint) listeningPoints.next();
		Listener listener = new Listener(li);
		listener.start();
	     }

         } catch(NullPointerException e) { 
             System.err.println("Stack has no ListeningPoints"); 
             System.exit(-1); 
         } 
      }

      public static void main(String[] args) {
	 String servicesFile = args[0];
	 Engine engine = new Engine(servicesFile);
      }

}

     

