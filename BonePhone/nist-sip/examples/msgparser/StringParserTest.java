/**
* An example that illustrates reading the contents of a message into a 
* buffer and parsing input from that buffer. 
* The main class implements the SIPParseExceptionListener interface which 
* allows it to register a parse exception handler that can handle 
* mangled headers in a specialized fashion.
*
* Build the program using 
*
* gmake StringParserTest
*
* Run this program using 
*
* java  StringParserTest StringParserTestInput.txt
* 
* (or use the make target stringparse).
*/

package examples.msgparser;
import gov.nist.sip.msgparser.*;
import java.io.*;
import java.util.*;

class StringParserTest implements SIPParseExceptionListener {
	static String msgBuffer = 
"SIP/2.0 200 OK\r\n"+
"CSeq: 1 INVITE\r\n"+
"Contact: sip:mranga@localhost:6060\r\n"+
"Via: SIP/2.0/udp 127.0.0.1:5060;branch=411ab34aab6be4c0a82649076d3b239f.127.0.0.1\r\n"+
"Via: SIP/2.0/UDP 192.168.1.2:7060;received=stinkbug\r\n"+
"From: sip:vikram@localhost\r\n"+
"Call-ID: 618548932@192.168.1.2\r\n"+
"Record-Route: <sip:mranga@localhost;maddr=127.0.0.1>\r\n"+
"To: sip:mranga@localhost; tag=33\r\n"+
"Content-Length: 0\r\n"+
"\r\n"+
"SIP/2.0 200 OK\r\n"+
"Via: SIP/2.0/UDP ss2.wcom.com:5060;branch=721e418c4.1\r\n"+
"Via: SIP/2.0/UDP here.com:5060\r\n"+
"Record-Route: <sip:UserB@everywhere.com;maddr=ss2.wcom.com>\r\n"+
"From: BigGuy <sip:UserA@here.com>\r\n"+
"To: Little Guy <sip:UserB@there.com>;tag=314159\r\n"+
"Call-ID: 12345600@here.com\r\n"+
"CSeq: 2 INVITE\r\n"+
"Contact: LittleGuy <sip:UserB@everywhere.com>\r\n"+
"Content-Type: application/sdp\r\n"+
"Content-Length: 151\r\n"+
"\r\n"+
"v=0\r\n"+
"o=UserB 2890844527 2890844527 IN IP4 everywhere.com\r\n"+
"s=Session SDP\r\n"+
"c=IN IP4 111.112.113.114\r\n"+
"t=0 0\r\n"+
"m=audio 3456 RTP/AVP 0\r\n"+
"a=rtpmap:0 PCMU/8000\r\n"+
"\r\n"+
"REGISTER sip:company.com SIP/2.0\r\n"+
"To:    sip:j.user@company.com\r\n"+
"From:    sip:j.user@company.com\r\n"+
"Call-ID:  0ha0isndaksdj@10.0.2.2\r\n"+
"Contact: sip:j.user@host.company.com, sip:j.user@host.company2.com\r\n"+
"CSeq: 8    REGISTER\r\n"+
"Via: SIP/2.0/UDP 135.180.130.133\r\n"+
"Contact: tel:+1-972-555-1212\r\n"+
"Accept-Language: en-gb;q=0.8, en;q=0.7\r\n" +
"Content-Length: 0\r\n"+
"\r\n"+
"REGISTER sip:company.com SIP/2.0\r\n"+
"To: sip:user@company.com\r\n"+
"From: sip:user@company.com\r\n"+
"Contact: sip:user@host.company.com\r\n"+
"Call-ID: k345asrl3fdbv@10.0.0.1\r\n"+
"CSeq: 1 REGISTER\r\n"+
"Via: SIP/2.0/UDP 135.180.130.133\r\n"+
"Contact: sip:user@example.com?Route=%3Csip:sip.example.com%3E\r\n"+
"Content-Length: 0\r\n"+
"\r\n"+
"INVITE sip:joe@company.com SIP/3.0\r\n"+
"To: sip:joe@company.com\r\n"+
"From: sip:caller@university.edu;tag=1234\r\n"+
"Call-ID: 0ha0isnda977644900765@10.0.0.1\r\n"+
"CSeq: 9 INVITE\r\n"+
"Via: SIP/2.0/UDP 135.180.130.133\r\n"+
"Content-Type: application/sdp\r\n"+
"\r\n"+
"v=0\r\n"+
"o=mhandley 29739 7272939 IN IP4 126.5.4.3\r\n"+
"c=IN IP4 135.180.130.88\r\n"+
"m=video 3227 RTP/AVP 31\r\n"+
"m=audio 4921 RTP/AVP 12\r\n"+
"a=rtpmap:31 LPC\r\n";
	/**
	* Ignore parse exceptions.
	*/
	public void handleException( SIPParseException ex) 
		throws SIPParseException  {
	    System.out.println("Error line = " +  ex.getText());

	    // ex.printStackTrace();
	   // Want to ignore header parse errors but not message parse errors.
  	   if (ex instanceof SIPHeaderParseException ) {
			System.out.println("HeaderParseException : " 
				+ ex.getMessage() + "/" + ex.getText());
		   ex.rejectErrorObject();
	   } else if (ex instanceof SIPDuplicateHeaderException )  {
		System.out.println("Ignoring duplicate hader" +
				ex.getMessage() );
		ex.rejectErrorObject();
	   } else if (ex instanceof SIPMissingHeaderException )  {
		System.out.println("Missing required header : " +
				ex.getMessage() );
		ex.rejectErrorObject();
	   } else {
		 ex.printStackTrace();
		 throw ex;
	   }
	}
	public static void main (String args[] ) throws SIPException {
		StringMsgParser parser = new StringMsgParser();
		// parser.enableDebugFlag();
		parser.parseContent();
		parser.setParseExceptionListener( new StringParserTest());
	  	SIPMessage sipMsg[]  = parser.parseSIPMessage(msgBuffer);
		// Format and print out the returned result.
		for ( int i = 0; i < sipMsg.length ; i++) 
		  System.out.println("Parsed structure = \n" + sipMsg[i] );

		for ( int i = 0; i < sipMsg.length ; i++)  {
			SIPMessage sipmsg = sipMsg[i];
			System.out.println("Encoded structure \n");
			System.out.println(sipmsg.encode());
			System.out.println("**********************\n");
			LinkedList ll = sipmsg.getMessageAsEncodedStrings();
		        ListIterator li = ll.listIterator();
			while(li.hasNext()) {
			   String msgText = (String) li.next();
			   System.out.println(msgText);

			}
		}

	        // Parse the message as an array of bytes.
	        System.out.println("Parsing message as an array of bytes");
	  	sipMsg  = parser.parseSIPMessage(msgBuffer.getBytes());
		for ( int i = 0; i < sipMsg.length ; i++)  {
			SIPMessage sipmsg = sipMsg[i];
			System.out.println("Encoded structure \n");
			System.out.println
			  (new String (sipmsg.encodeAsBytes()));
			System.out.println("**********************\n");
			LinkedList ll = sipmsg.getMessageAsEncodedStrings();
		        ListIterator li = ll.listIterator();
			while(li.hasNext()) {
			   String msgText = (String) li.next();
			   System.out.println(msgText);

			}
		}
	
	}
}
