package examples.msgparser;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.net.*;
import gov.nist.sip.*;
import gov.nist.sip.sipheaders.*;
import java.io.*;
/**
* Illustrats the parsing of individual SIP Headers.
* 
* Build using gmake SIPHeaderParserTest.class
*
* Run using 
*
* java SIPHeaderParserTest SIPHeaderParserTestInput.txt
*
*/

class SIPHeaderParserTest {
	public static void main( String args[] ) throws SIPException {
		if (args.length < 1 ) {
			System.err.println
				("Please specify file name for message");
			System.exit(0);
		}
		String fileName = args[0];
		try {
			// Read the file into a buffer.
			File file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = 
					new BufferedReader(fileReader);
			StringMsgParser parser = new StringMsgParser();
			// parser.enableDebugFlag();
			while (true) {
				String msgBuffer = bufferedReader.readLine();
				if (msgBuffer == null) break;
				SIPHeader hdr =  
					parser.parseSIPHeader(msgBuffer);
				System.out.println("Header = ");
				System.out.println(hdr);
			}
		} catch (FileNotFoundException ex) {
			System.err.println("File not found " + fileName);
			ex.printStackTrace();
			System.exit(0);
		} catch (IOException ex) {
			System.err.println("Error reading file " + fileName);
			ex.printStackTrace();
			System.exit(0);
		}

	}
}
