package examples.jainsip.container;
import jain.protocol.ip.sip.*; 
import jain.protocol.ip.sip.address.*; 
import jain.protocol.ip.sip.header.*; 
import jain.protocol.ip.sip.message.*; 
import java.util.*; 

/**
* This is an example service that extends the service container.
*/

public class TCPService extends JAINService {

	public TCPService() {
		System.out.println("initialized!");
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
         System.out.println
	   ("\n\nRequest received with server transaction id " + 
		serverTransactionId + ":\n" + request); 
         try { 
             // If request is not an ACK then try to send an OK Response 
             if((!Request.ACK.equals(request.getMethod())) && 
		(!Request.REGISTER.equals(request.getMethod()))) { 

		 String body = request.getBodyAsString();
                 getSipProvider().
			sendResponse(serverTransactionId, Response.OK, 
			body, "application", "sdp"); 
             } 
         } catch(TransactionDoesNotExistException e) { 
	     e.printStackTrace();
             System.out.println(e.getMessage()); 
             System.exit(-1); 
         } catch(SipParseException e) { 
	     e.printStackTrace();
             System.out.println(e.getMessage()); 
             System.exit(-1); 
         } catch(SipException e) { 
	     e.printStackTrace();
             System.out.println(e.getMessage()); 
             System.exit(-1); 
         } 
	 System.out.println("Completed processing request!");
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

                 getSipProvider().sendAck(clientTransactionId); 
             } 
         } catch(SipException e) { 
	     e.printStackTrace();
             System.err.println(e.getMessage()); 
             System.exit(-1); 
         } 
     } 

}
