/**
* A program that illustrates parsing of SIP URLs using the SIP Message parser.
*
* To build this program (assuming you have installed gnu make), 
*
* gmake SIPURLParserTest.class
*
* To run this program, set CLASSPATH to include antlrall.jar and msgparser.jar :
*
* java SIPURLParserTest SIPURLParserTestInput.txt
* 
* and watch a fascinating data structure dumped to the screen
*/
package examples.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.msgparser.*;
import java.io.*;

class SIPURLParserTest {
static private String urls[] = 
{ "sip:1-301-975-3664@foo.bar.com;user=phone", "sip:129.6.55.181",
  "sip:herbivore.ncsl.nist.gov:5070;maddr=129.6.55.251",
  "sip:j.doe@big.com", "sip:j.doe:secret@big.com;transport=tcp",
  "sip:j.doe@big.com?subject=project", 
  "sip:+1-212-555-1212:1234@gateway.com;user=phone",
  "sip:1212@gateway.com",
  "sip:alice@10.1.2.3",
  "sip:alice@example.com",
  "sip:alice",
  "sip:alice@registrar.com;method=REGISTER",
  "sip:sfo@[fe80::204:76ff:fe0b:b858]:5061"
};
	public static void main( String args[] ) 
		throws SIPException {
		// Read the file into a buffer.
		StringMsgParser parser = new StringMsgParser();
		for (int i = 0 ; i < urls.length; i++) {
			String msgBuffer =  urls[i];
			if (msgBuffer == null) break;
			URI uri =  parser.parseSIPUrl(msgBuffer);
			System.out.println("URI = " + uri);
		}

	}
}
