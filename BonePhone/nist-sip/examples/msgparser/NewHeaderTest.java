package examples.msgparser;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
import java.io.*;

/**
* Illustrates the extension mechanism that is incorporated int the parser.
* Shows how to parse a new (extension) header (not defined in  RFC 2514).
*
* Use as input file NewHeaderTestInput.txt
* Use the script runit.sh to run the test or 
*
* java NewHeaderTest NewHeaderTestInput.txt
*
* The output from this test case will be that the NewFangledHeaderHandler 
* will be invoked when the parser encounters the header that it does 
* not understand. At the end of this test, the header is retrieved and
* printed. Typically, the exception handler will invoke an auxiliary parser
* to parse the input string at this point.
*/

                     
class NewHeaderTest {
        static class SessionTimer extends SIPHeader { 
		SessionTimer() { super("Session-Timer"); }
		public String encode() { return inputText; }

	}

	static class UnrecognizedHeader extends SIPHeader {
		UnrecognizedHeader(String hdrName) { super(hdrName); }
		public String encode() { return inputText; }
	}


	static class NewFangledHeaderHandler implements 
		SIPParseExceptionListener {
			
		public void   handleException (SIPParseException ex  ) 
			throws SIPUnrecognizedExtensionException {
			if (ex instanceof SIPUnrecognizedExtensionException)  {
				SIPUnrecognizedExtensionException e =
				    (SIPUnrecognizedExtensionException) ex;
				String extensionHdr  = e.getText();
				System.out.println("Extension HDR " + 
						extensionHdr);
				System.out.println("Extension Name " + "[" +
						e.getExtensionName()+"]");
				if (  e.getExtensionName().
					compareToIgnoreCase 
					("Session-Timer") != 0 ) {
					SIPHeader retval = 
					    new UnrecognizedHeader
						(e.getExtensionName());
					retval.setInputText(extensionHdr);
					e.setHeader(retval);
				}  else {
				   System.out.println 
				    ("The New Fangled Header was " + 
						extensionHdr);
		 		    SIPHeader retval = new SessionTimer();
				  retval.setInputText(extensionHdr);
				  e.setHeader(retval);
				}
			}
		}

	}

	public static void main (String args[] ) throws SIPException {
		if (args.length < 1 ) {
			System.err.println
				("Please specify file name for message");
			System.exit(0);
		}
		String fileName = args[0];
		String msgBuffer = null;
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
		parser.setParseExceptionListener(new NewFangledHeaderHandler());
		// Hand it off to the parser.
	  	SIPMessage[] sipMsg  = parser.parseSIPMessage(msgBuffer);
		// Format and print out the returned result.

		String headerClassName = 
			( new SessionTimer()).getClass().getName();

		SessionTimer newFangledHeader = 
			(SessionTimer) 
				sipMsg[0].getExtensionHdr(headerClassName);
		if (newFangledHeader != null) System.out.println("Retrieved " 
				+ newFangledHeader.getInputText());

		String encodedHeader = sipMsg[0].encode();
		System.out.println("Encoded hdr = " + encodedHeader);

		
	}
}
