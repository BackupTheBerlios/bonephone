package examples.msgparser;
import java.io.*;
import gov.nist.sip.net.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
/**
* Example for the StreamParser:   Read input from std in, parses it and outputs 
* the parse structure to  stdout. 
*
* Build using 
* gmake PipelinedParserTest.class
*
* Run using
* java PipelinedParserTest
*
* Cut and paste a SIP message to the window where you started the test and witness a fascinating
* dump of the generated data structure.
*/

class PipelinedParserTest implements SIPMessageListener {
	/**
	* On successful parse do the following.
	*/
	public void processMessage(SIPMessage sipmsg) {
	    // just dump the message.
	    new Exception().printStackTrace();
	    System.out.println(sipmsg);
	}

        class CiscoExtension extends SIPHeaderList { 
		CiscoExtension() { super("CiscoExtension"); }

	}

	/**
	* On parse exception, do the following...
	* If this is a header parse exception, then ignore it.
	* If this is a message parse exception, then abort the parse
	*/
	public void handleException( SIPParseException ex) 
	throws SIPParseException {
	     if (ex instanceof SIPHeaderParseException) {
	     	System.out.println("\nHeader Parse error " + ex.getMessage());
	     } else if (ex instanceof SIPUnrecognizedExtensionException ) {
	     	System.out.println("\nUnrecognized Extension " + ex.getText());
		SIPUnrecognizedExtensionException e = 
				(SIPUnrecognizedExtensionException) ex;
		CiscoExtension newHdr = new CiscoExtension();
		newHdr.setInputText(ex.getText());
		e.setHeader(newHdr);
	     } else {
		ex.printStackTrace();
		throw ex;
	    }
	}
	
	/**
	* Entry point -- create a parser, that reads from stdin and parses 
	* input.
	*/
	public static void main( String args[] ) {
	    // Just read from stdin for the test....
	    InputStream in = System.in;
	    PipelinedMsgParser parser = 
		new PipelinedMsgParser( new PipelinedParserTest(), in,false);
	    parser.parseContent();
	    parser.processInput();
	}
}
	
