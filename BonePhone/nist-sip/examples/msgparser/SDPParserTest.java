package examples.msgparser;
import gov.nist.sip.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.msgparser.*;
import java.io.*;

/**
* Example illustrating the parsing sdp fields from a buffer.
*
* Build using gmake SDPParserTest.class
*
* Run using 
* 
*  java SDPParserTest SDPParserTestInput.txt
*
* Expected output is the parsed SDP structure printed to the screen.
*
*/

class SDPParserTest implements SIPParseExceptionListener {
	public void handleException(SIPParseException ex)
	throws SIPParseException {
		ex.printStackTrace();
		throw ex;
	}
	public static void main (String args[] ) throws SIPException {
		if (args.length < 1 ) {
			System.err.println
				("Please specify file name for message");
			System.exit(0);
		}
		String fileName = args[0];
		String msgBuffer = "";
		try {
			// Read the file into a buffer.
			File file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			int bufferLength = (int) file.length();
			char inputBuffer[] = new char[bufferLength];
			fileReader.read(inputBuffer,0,bufferLength);
			msgBuffer = new String(inputBuffer);
		} catch (FileNotFoundException ex) {
			System.err.println("File not found " + fileName);
			ex.printStackTrace();
			System.exit(0);
		} catch (IOException ex) {
			System.err.println("Error reading file " + fileName);
			ex.printStackTrace();
			System.exit(0);
		}
		StringMsgParser parser = new StringMsgParser();
		// enable parsing of message content.
		parser.parseContent();
		// parser.enableDebugFlag();
		parser.setParseExceptionListener(new SDPParserTest());
		SIPMessage  sipmsg[]  = parser.parseSIPMessage(msgBuffer);
		System.out.println("Parsed structure = \n" + sipmsg[0]);
		String encodedMsg = sipmsg[0].encode();
		System.out.println("encoded structure = \n" + encodedMsg);
	}
}
