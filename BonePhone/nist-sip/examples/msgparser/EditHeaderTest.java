package examples.msgparser;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
import java.io.*;
/**
* Eample of how to clone and edit headers using the find and replace
* methods offered by nist-sip. Cloning creates a DEEP copy of the header
* so that the cloned header can be modified. The edited header can be encoded
* into a string and then sent on its way. This is a useful feature for
* forking proxies.
*
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

class EditHeaderTest implements SIPParseExceptionListener {
	/**
	* Ignore parse exceptions.
	*/
	static String msgBuffer = 
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
"\r\n"+
"REGISTER sip:company.com SIP/2.0\r\n"+
"To:    sip:j.user@company.com\r\n"+
"From:    sip:j.user@company.com\r\n"+
"Call-ID:  0ha0isndaksdj@10.0.2.2\r\n"+
"Contact: sip:j.user@host.company.com, sip:j.user@host.company2.com\r\n"+
"CSeq: 8    REGISTER\r\n"+
"Via: SIP/2.0/UDP 135.180.130.133\r\n"+
"Contact: tel:+1-972-555-1212\r\n"+
"Content-Length: 0\r\n"+
"\r\n"+
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
	public void handleException( SIPParseException ex) 
		throws SIPParseException  {
	   // Want to ignore header parse errors but not message parse errors.
  	   if (ex instanceof SIPHeaderParseException ) {
			System.out.println("HeaderParseException : " 
				+ ex.getMessage() + "/" + ex.getText());
	   } else {
		 ex.printStackTrace();
		 System.exit(0);
	   }
	}
	public static void main (String args[] ) 
		throws SIPException,ClassNotFoundException {
		StringMsgParser parser = new StringMsgParser();
		// parser.enableDebugFlag();
		parser.parseContent();
		parser.setParseExceptionListener( new EditHeaderTest());
	  	SIPMessage sipMsg[]  = parser.parseSIPMessage(msgBuffer);
		// Format and print out the returned result.

		StringMsgParser stringParser = new StringMsgParser();
		URI replacement = 
		    stringParser.parseSIPUrl("sip:mranga@nist.gov");

		for (int i = 0; i < sipMsg.length; i++) {
			SIPMessage sipmsg = sipMsg[i];
			System.out.println("Encoded structure = \n");
			System.out.println(sipmsg.encode());
			SIPMessage newmsg = (SIPMessage) sipMsg[i].clone();
		        // new remove all the contact headers from the
			// cloned message.
			newmsg.removeAll(Class.forName
				("gov.nist.sip.sipheaders.ContactList"));
			System.out.println
			 ("Edited Structure (without contact hdrs) = \n");
			System.out.println(newmsg.encode());
			System.out.println
			 ("Edited Structure (old message (with contact hdrs) = \n");
			System.out.println(sipMsg[i].encode());
			// Replace SIP urls with joe@company.com with
		        // sip:mranga@nist.gov
			newmsg.replace("sip:joe@company.com",replacement,true);
			System.out.println
			 ("After replacement of sip url = \n");
			System.out.println(newmsg.encode());
			System.out.println("****************************");
		}
		  

	
	}
}
