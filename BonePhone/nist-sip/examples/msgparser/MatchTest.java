
/** An example that illustrates template matching on SIP headers and messages.
* In this example we match against the message in MatchTest.txt
*/
package examples.msgparser;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
import java.io.*;

public class MatchTest {
static final String message1 = "INVITE sip:joe@company.com SIP/3.0\r\n"+
"To: sip:joe@company.com\r\n"+
"From: sip:caller@university.edu ;tag=1234\r\n"+
"Call-ID: 0ha0isnda977644900765@10.0.0.1\r\n"+
"CSeq: 9 INVITE\r\n"+
"Via: SIP/2.0/UDP 135.180.130.133\r\n"+
"Content-Type: application/sdp\r\n"+
"\r\n"+
"v=0\r\n"+
"o=mhandley 29739 7272939 IN IP4 126.5.4.3\r\n" +
"c=IN IP4 135.180.130.88\r\n" +
"m=video 3227 RTP/AVP 31\r\n" +
"m=audio 4921 RTP/AVP 12\r\n" +
"a=rtpmap:31 LPC\r\n";

static final String message2 = "SIP/2.0 200 OK\r\n"+
"Via: SIP/2.0/UDP 129.6.55.18:5060\r\n"+
"From: \"3ComIII\" <sip:13019768226@129.6.55.78>;tag=e13b4296\r\n"+
"To: \"3ComIII\" <sip:13019768226@129.6.55.78>\r\n"+
"Call-Id: c5ab5808@129.6.55.18\r\n"+
"CSeq: 49455 REGISTER\r\n"+
"Expires: 1200\r\n"+
"Contact: <sip:13019768226@129.6.55.18>;expires=1199;action=proxy\r\n"+
"Content-Length: 0\r\n"+
"\r\n";

	public static void main(String args[]) {
		SIPRequest template = new SIPRequest();
		RequestLine requestLine  = new RequestLine();
		requestLine.setMethod(SIPKeywords.INVITE);
		template.setRequestLine(requestLine);
		From from = new From();
		CallID callId = new CallID();
		try {
		  CallIdentifier callid = new CallIdentifier();
		  callid.setHost("10.0.0.2");
		  callId.setCallIdentifier(callid);
		  template.attachHeader(callId,false);
		} catch (SIPException se) {
		  se.printStackTrace();
		  System.exit(0);
		}
		try {
		    StringMsgParser smp = new StringMsgParser();
		    smp.disableInputTracking();
		    SIPMessage[] messages = smp.parseSIPMessage
			(new String(message1));
		    System.out.println("Match returned " +
				 messages[0].match(template));

		} catch (Exception ex) {
		   ex.printStackTrace();
		   System.exit(0);
		}

		StatusLine statusLine = new StatusLine();
		statusLine.setStatusCode(200);
		SIPResponse responseTemplate = new SIPResponse();
		responseTemplate.setStatusLine(statusLine);
		try {
		    StringMsgParser smp = new StringMsgParser();
		    smp.disableInputTracking();
		    SIPMessage[] messages = smp.parseSIPMessage
			(new String(message2));
		    System.out.println("Match returned " +
				 messages[0].match(responseTemplate));

		} catch (Exception ex) {
		   ex.printStackTrace();
		   System.exit(0);
		}

		
	}
}
