package bonephone;

import gov.nist.sip.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.msgparser.*;
import java.io.*;
import java.util.*;

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

class SDPManagerTester implements SIPParseExceptionListener {

	public SDPManagerTester() { }
	public void gogogo(SIPMessage sm) {
		SDPAnnounce sdpa=sm.getSdpAnnounce();
		SDPManager sdpm=new SDPManager("FritzWilli");
		try { sdpm.extractAllCodecs(sdpa); }
		catch (Exception e) { System.err.println(e.getMessage()); }
	}


	public void handleException(SIPParseException ex)
	throws SIPParseException {
		ex.printStackTrace();
		throw ex;
	}
	public static void main (String args[] ) throws SIPException {
		SDPManagerTester sdpmt = new SDPManagerTester();
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
		parser.setParseExceptionListener(sdpmt);
		SIPMessage  sipmsg[]  = parser.parseSIPMessage(msgBuffer);
		sdpmt.gogogo(sipmsg[0]);
	}
}
