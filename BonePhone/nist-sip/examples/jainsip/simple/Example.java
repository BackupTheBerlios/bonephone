package examples.jainsip.simple;
import jain.protocol.ip.sip.*; 
import jain.protocol.ip.sip.address.*; 
import jain.protocol.ip.sip.header.*; 
import jain.protocol.ip.sip.message.*; 
import java.util.*; 
  

/**
* This is the example straight off the JAIN SIP 1.0 example.
*/

public class Example implements SipListener { 
     private SipFactory sipFactory = null; 
     private AddressFactory addressFactory = null; 
     private HeaderFactory headerFactory = null; 
     private MessageFactory messageFactory = null; 
     private SipStack sipStack = null; 
     private SipProvider sipProvider = null; 
     private Iterator listeningPoints = null; 

     // Main 
     public static void main(String[] args) { 
         Example example = new Example(); 
         example.sendMessages(); 
     } 

     public Example() { 
         setup(); 
     } 

     private void setup() { 
        
         // Obtain an instance of the singleton SipFactory 
         sipFactory = SipFactory.getInstance(); 

         // Set path name of SipFactory to reference implementation 
         // (not needed - default path name) 
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

         // Set name of SipStack 


         sipStack.setStackName("NIST Implementation SIP stack"); 

         try { 

             // Get SipStack's ListeningPoints 
             listeningPoints = sipStack.getListeningPoints(); 

             // Create SipProvider based on the first ListeningPoint 
             // Note that this call will block until somebody sends us
	     // a message because we dont know what IP address and
	     // port to assign to outgoing messages from this provider
	     // at this point. 
             sipProvider = sipStack.createSipProvider
		((ListeningPoint)listeningPoints.next()); 

         } catch(NullPointerException e) { 
             System.err.println("Stack has no ListeningPoints"); 
             System.exit(-1); 
         } catch(ListeningPointUnavailableException e) { 
             System.err.println(e.getMessage()); 
             System.exit(-1); 
         } 
         System.out.println("##############") ;

         // Register this application as a SipListener of the SipProvider 
         try { 
             sipProvider.addSipListener(this); 
         } catch(TooManyListenersException e) { 
             // A limit has been reached on the number of 
	     // Listeners allowed per provider 
	     e.printStackTrace();
             System.err.println(e.getMessage()); 
             System.exit(-1); 
         } catch(SipListenerAlreadyRegisteredException e) { 
             // Already been added as SipListener 
	     e.printStackTrace();
             System.err.println(e.getMessage()); 
             System.exit(-1); 
         } 
     } 

     // Process transaction timeout 
     public void processTimeOut(jain.protocol.ip.sip.SipEvent 
		transactionTimeOutEvent) { 
         if(transactionTimeOutEvent.isServerTransaction()) { 
             System.out.println("Server transaction " + 
		transactionTimeOutEvent.getTransactionId() + " timed out"); 
         } else { 
             System.out.println("Client transaction " + 
		transactionTimeOutEvent.getTransactionId() + " timed out"); 
         } 
     } 

     // Process Request received 
     public void processRequest
	(jain.protocol.ip.sip.SipEvent requestReceivedEvent) { 
         Request request = (Request)requestReceivedEvent.getMessage(); 
         long serverTransactionId = requestReceivedEvent.getTransactionId(); 
         try { 
            System.out.println
	      ("\n\nRequest " + request.getMethod() + 
		" received with server transaction id " + 
		serverTransactionId + ":\n" ); 
             // If request is not an ACK then try to send an OK Response 
             if((!Request.ACK.equals(request.getMethod())) && 
		(!Request.CANCEL.equals(request.getMethod()))) { 

		 String body = request.getBodyAsString();
		 if (body == null) body = "";
                 sipProvider.sendResponse(serverTransactionId, Response.OK, 
			body, "application", "sdp"); 
             } 
         } catch(TransactionDoesNotExistException e) { 
             System.out.println(e.getMessage()); 
             System.exit(-1); 
         } catch(SipParseException e) { 
             System.out.println(e.getMessage()); 
             System.exit(-1); 
         } catch(SipException e) { 
             System.out.println(e.getMessage()); 
             System.exit(-1); 
         } 
     } 

     // Process Response received 
     public void processResponse(jain.protocol.ip.sip.SipEvent 
		responseReceivedEvent) { 
         Response response = (Response)responseReceivedEvent.getMessage(); 
         long clientTransactionId = responseReceivedEvent.getTransactionId(); 
         System.out.println("Response received with client transaction id " 
		+ clientTransactionId + ":\n" + response); 

         try { 
             // Get method of response 
             String method = response.getCSeqHeader().getMethod(); 

             // Get status code of response 
             int statusCode = response.getStatusCode(); 
             // If response is a 200 INVITE response, try to send an ACK 
             if((statusCode == Response.OK) && 
			(method.equals(Request.INVITE))) { 

                 sipProvider.sendAck(clientTransactionId); 
             } 
         } catch(SipException e) { 
	     e.printStackTrace();
             System.err.println(e.getMessage()); 
             System.exit(-1); 
         } 
     } 

     public void sendMessages() { 
         try { 

             // Create AddressFactory 
             addressFactory = sipFactory.createAddressFactory(); 
		
	       URI uri = addressFactory.createURI("tel", "0705950794");
	        uri = addressFactory.createURI("tel", "+0705950794");

             // Create HeaderFactory 
             headerFactory = sipFactory.createHeaderFactory(); 

             // Create MessageFactory 
             messageFactory = sipFactory.createMessageFactory(); 
         } catch(SipPeerUnavailableException e) { 
	     e.printStackTrace();
             System.err.println(e.getMessage()); 
             System.exit(-1); 
         }  catch (SipParseException ex) {
		ex.printStackTrace();
		System.exit(-1);
	 }

         SipURL fromAddress = null; 
         NameAddress fromNameAddress = null; 
         FromHeader fromHeader = null; 
         SipURL toAddress = null; 
         NameAddress toNameAddress = null; 
         ToHeader toHeader = null; 
         SipURL requestURI = null; 
         CallIdHeader callIdHeader = null; 
         CSeqHeader cSeqHeader = null; 
         ViaHeader viaHeader = null; 
         ArrayList viaHeaders = null; 
         ContentTypeHeader contentTypeHeader = null; 
         Request invite = null; 
         Request options = null; 
         Request register = null; 

         try { 
             // create From Header 

             fromAddress = addressFactory.createSipURL
			("caller", sipProvider.getListeningPoint().getHost()); 
             fromAddress.setPort(sipProvider.getListeningPoint().getPort()); 
             fromNameAddress = addressFactory.createNameAddress
				("Caller", fromAddress); 

             fromHeader = headerFactory.createFromHeader(fromNameAddress); 
	     fromHeader.setTag("6789-1234");

             // create To Header 
             toAddress = addressFactory.createSipURL("callee", 
			sipProvider.getListeningPoint().getHost()); 
             toAddress.setPort(sipProvider.getListeningPoint().getPort()); 
             toNameAddress = addressFactory.createNameAddress
				("Callee", toAddress); 
             toHeader = headerFactory.createToHeader(toNameAddress); 
	     toHeader.setTag("12345-6789");

             // create Request URI 
             requestURI = (SipURL)toAddress.clone(); 
             requestURI.setTransport
			(sipProvider.getListeningPoint().getTransport()); 

             // Create ViaHeaders 
             String transport = sipProvider.getListeningPoint().getTransport(); 
             if(transport.equals(ListeningPoint.TRANSPORT_UDP)) { 
             	transport = ViaHeader.UDP; 
             } else if(transport.equals(ListeningPoint.TRANSPORT_TCP)) { 
             	transport = ViaHeader.TCP; 
             } 
             viaHeader = headerFactory.createViaHeader
		(sipProvider.getListeningPoint().getHost(), 
		sipProvider.getListeningPoint().getPort(), transport); 
             viaHeaders = new ArrayList(); 
             viaHeaders.add(viaHeader); 

             // Create ContentTypeHeader 
             contentTypeHeader = headerFactory.createContentTypeHeader
			("application", "sdp"); 

             // Create and send INVITE Request 
             callIdHeader = sipProvider.getNewCallIdHeader(); 
             cSeqHeader = headerFactory.createCSeqHeader(1, Request.INVITE); 


             invite = messageFactory.createRequest(requestURI, 
			Request.INVITE, callIdHeader, cSeqHeader, 
			fromHeader, toHeader, viaHeaders); 

 	     SipURL su1 = addressFactory.createSipURL("mranga","nist.gov");
             NameAddress a1 = 
		 addressFactory.createNameAddress("M. Ranganathan",su1);
             ContactHeader c1 = headerFactory.createContactHeader(a1);
             c1.setExpires(new Date());
	     LinkedList clist = new LinkedList();
	     clist.add(c1);

	     invite.setContactHeaders(clist);


             sipProvider.sendRequest(invite); 
	     System.out.println("Sent request " + Request.INVITE);

             // Create and send OPTIONS Request 
             callIdHeader = sipProvider.getNewCallIdHeader(); 
             cSeqHeader = headerFactory.createCSeqHeader(2, Request.OPTIONS); 
             options = messageFactory.createRequest(requestURI, 
			Request.OPTIONS, callIdHeader, cSeqHeader, 
			fromHeader, toHeader, viaHeaders); 
             sipProvider.sendRequest(options); 

             // Create and send REGISTER Request 
             callIdHeader = sipProvider.getNewCallIdHeader(); 
             cSeqHeader = headerFactory.createCSeqHeader(3, Request.REGISTER); 
             register = messageFactory.createRequest(requestURI, 
		Request.REGISTER, callIdHeader, cSeqHeader, fromHeader, 
		toHeader, viaHeaders); 
             long clientTransactionId = sipProvider.sendRequest(register); 

	     System.out.println("Sent request " + Request.REGISTER + " " +  
			clientTransactionId);
             sipProvider.sendCancel(clientTransactionId); 

             // send BYE Request 
	     try {
	     Thread.sleep(10000);
	     } catch (InterruptedException ex) {}
	     System.out.println("Sending a bye request");
             sipProvider.sendBye(clientTransactionId, false); 
         } catch(SipParseException e) { 
             // Implementation was unable to parse a value 
	     e.printStackTrace();
             System.err.println(e.getMessage() + "[" + e.getUnparsable() + "]"); 
             System.exit(-1); 
         } catch(SipException e) { 
             // Another exception occurred 
	     e.printStackTrace();
             System.err.println(e.getMessage()); 
             System.exit(-1); 
         } 
     } 
}

