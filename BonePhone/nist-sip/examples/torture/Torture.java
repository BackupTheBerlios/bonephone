/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../doc/uncopyright.html for conditions of use.                     *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
*  Olivier Deruelle (deruelle@antd.nist.gov)				       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/**
* Implements a SAX parser for a SIP parser torture tester.
* @since 0.9
* @version 1.0
* Revisions:
* 1. Clearer diagnostic messages.
* 2. Added ability to specify wildcard matches for exception text.
* 3. Migrated to the apache xerces parser 
*
*/

package examples.torture;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.jain.protocol.ip.sip.*;
import gov.nist.jain.protocol.ip.sip.message.*;
import gov.nist.jain.protocol.ip.sip.header.*;
import gov.nist.jain.protocol.ip.sip.address.*;
import jain.protocol.ip.sip.*;
import java.io.*;
import antlr.*;
import java.util.Hashtable;
import java.util.Enumeration;
// import javax.xml.parsers.SAXParserFactory;  
// import javax.xml.parsers.ParserConfigurationException;  
// import org.xml.sax.helpers.ParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader; 
import org.xml.sax.SAXException; 
import org.xml.sax.SAXParseException; 
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
	

public  class Torture  extends DefaultHandler
implements SIPParseExceptionListener, TagNames,
TokenValues, ConfigurationSwitches
{

	// variables that I read from the xml file header.
	protected String 	testMessage;
	protected String 	encodedMessage;
	protected String 	testDescription;
	protected boolean	debugFlag;
	protected boolean 	abortOnFail;
	protected boolean       testJAIN;
	protected String 	failureReason;
	protected String 	statusMessage;
	protected String	exceptionHeader;
	protected String	exceptionMessage;
	protected String  	exceptionClassName;
	protected boolean	failureFlag;
	protected boolean	successFlag;
	protected boolean       outputParsedStructures;

	private PrintStream     jainLogWriter;
	private PrintStream  	testLogWriter;
	private Hashtable    	exceptionTable;
	private StringMsgParser stringParser;
	
	// These track which context the parser is currently parsing.
	private boolean messageContext;
	private boolean exceptionTextContext;
	private boolean sipHeaderContext;
	private boolean sipURLContext;
	private boolean testCaseContext;
	private boolean descriptionContext;
	private boolean expectExceptionContext;
	private boolean expectExtensionContext;
	private boolean exceptionMessageContext;
	private String  testMessageType;
	private static final String XML_DOCTYPE_IDENTIFIER = 
		 "<?xml version='1.0' encoding='us-ascii'?>";
	
	class ExpectedException {
		protected boolean fired;
		protected String  exceptionHeader; 
		protected String  exceptionMessage;
		protected Class exceptionClass;
		protected ExpectedException( String className , 
				String headerString ) {
			try {
		      		exceptionClass = Class.forName(className);
				exceptionHeader = headerString;
				fired = false;
			} catch ( ClassNotFoundException ex) {
				ex.printStackTrace();
				System.err.println(ex.getMessage());
				testLogWriter.close();
				System.exit(0);
			}
		}
	}
	
	public void handleException( SIPParseException ex) 
	throws SIPParseException 
	{
		String exceptionString = ex.getText();
		if (debugFlag) {
			System.out.println(exceptionString);
		}
		if (exceptionString == null  ) {
			System.out.println("Bad exception header (null)");
			System.out.println("Exception type = " + 
					ex.getClass().getName());
			System.out.println("Exception Header Type = " + 
					ex.getErrorObjectName());
			ex.printStackTrace();
			if (debugFlag) System.exit(0);
			else return;
		} 
		
		if (exceptionTable == null) {
		   formatFailureDiagnostic(ex, 
			"unexpected exception (no exceptions expected)");
		   failureFlag = true;
		   if(debugFlag) throw ex;
		} else {
		   ExpectedException e = (ExpectedException) 
			exceptionTable.get(ex.getText());
		   if (e == null) {
			// Check if there is an exception handler for 
			// wildcard match.
			e = (ExpectedException) exceptionTable.get("*");
		   }
		   if (e == null) {
			formatFailureDiagnostic(ex,
					"unexpected exception");
			failureFlag = true;
		   	if(debugFlag) throw ex;
		   } else if (! ex.getClass().equals(e.exceptionClass)) {
			 failureFlag = true;
			 formatFailureDiagnostic( ex,
				" got " + ex.getClass() + " expecting " + 
				e.exceptionClass.getName()) ;
		   	if(debugFlag) throw ex;
		   } else {
			e.exceptionMessage = ex.getMessage();
			e.fired = true;
			successFlag = true;
		   }
		}
		ex.rejectErrorObject();
	}
	
	public void characters( char[] ch, int start, int length) {
		String str = new String(ch, start,length);
		if (str.trim().equals("")) return;
		if (messageContext) {
			if (testMessage == null) testMessage =  str; 
			else testMessage += str;
		} else if (descriptionContext) {
			System.out.println(" testDescription = " + str);
			if (testDescription == null) testDescription = str;
			else testDescription += str;
		} else if (exceptionTextContext) {
		       if (exceptionHeader == null)  
					exceptionHeader = str;
		       else  exceptionHeader += str;
		} else if (sipHeaderContext) {
			if (testMessage == null) testMessage = str;
			else testMessage += str;
		} else if (sipURLContext) {
			if (testMessage == null) testMessage = str;
			else testMessage += str;
		} else if (exceptionMessageContext) {
			if (exceptionMessage == null)  exceptionMessage = str;
			else exceptionMessage += str;
		}
	}
	

	public void startElement ( String namespaceURI, 
		String local, String name, Attributes attrs) 
	throws SAXException
	{
		if (name.compareTo(TORTURE) == 0 ) {
			outputParsedStructures = false;
			failureFlag = false;
			debugFlag = false;
			String outputParsedString = 
					attrs.getValue(OUTPUT_PARSED_STRUCTURE);
			if (outputParsedString != null &&
		    	        outputParsedString.toLowerCase().
				compareTo(TRUE) ==0) {
			    outputParsedStructures = true;
			}
			String debugString = attrs.getValue(DEBUG);
			if (debugString != null &&
			    debugString.toLowerCase().compareTo(TRUE) ==0) {
			    debugFlag = true;
			}
			String abortString = attrs.getValue(ABORT_ON_FAIL);
			if (abortString != null 
				&& abortString.compareTo(TRUE) == 0 ) {
				abortOnFail = true;
			} else abortOnFail = false;

		        String testJAINString = attrs.getValue(TEST_JAIN);
			if (testJAINString != null 
				&& testJAINString.compareTo(TRUE) == 0) {
				testJAIN = true;
			} else testJAIN = false;

			String testlog = attrs.getValue(TESTLOG);
			if ( testlog != null) {
				try {
				    testLogWriter = 
				    new PrintStream( 
					new FileOutputStream(testlog,true));
				} catch (IOException ex) {
					System.err.println("Cannot open "+ 
							testlog + " for write");
					System.exit(0);
				}
			} else {
				testLogWriter = System.out;
			}
			
			String jainTestLog = attrs.getValue(JAIN_TESTLOG);
	
			if ( jainTestLog != null) {
			    try {
	                        jainLogWriter = 
				 new PrintStream
                                        (new FileOutputStream(jainTestLog,
						true));
			     } catch (IOException ex) {
				System.err.println("Cannot open "+ 
						jainTestLog + " for write");
					System.exit(0);
			     }
			} else {
			    jainLogWriter = System.out;
			}


			emitString(XML_DOCTYPE_IDENTIFIER);

			emitTag(TEST_OUTPUT);

		} else if (name.compareTo(TESTCASE) == 0 ) {
			// Starting a new test 
			failureReason = "";
			statusMessage = "";
			failureFlag	 = false;
			successFlag	 = false;
			messageContext = false;
			testCaseContext = false;
			messageContext = false;
			descriptionContext = false;
			expectExtensionContext = false; 
			sipHeaderContext = false;
			sipURLContext = false;
		        testDescription = null;
			testMessage = null;
			encodedMessage = null;
			exceptionMessage = null;
			exceptionTable = new Hashtable();
		} else if (name.compareTo(MESSAGE) == 0 ) {
			testMessageType = MESSAGE;
			messageContext = true;
		} else if (name.compareTo(SIP_HEADER) == 0 ) {
			testMessageType = SIP_HEADER;
			sipHeaderContext = true;
		} else if (name.compareTo(SIP_URL) == 0 ) {
			testMessageType = SIP_URL;
			sipURLContext = true;
		} else if ( name.compareTo(DESCRIPTION) == 0 ) {
			descriptionContext = true;
		} else if ( name.compareTo(EXPECT_EXCEPTION) == 0 ) {
			expectExceptionContext = true;
			exceptionClassName = attrs.getValue(EXCEPTION_CLASS);
		} else if ( name.compareTo(EXCEPTION_MESSAGE) == 0 ) {
			exceptionMessageContext = true;
		} else if (name.compareTo(EXCEPTION_TEXT) == 0 ) {
			exceptionTextContext = true;
		} else throw new SAXException("Unrecognized tag " + name);
		       
		       
	}
	
	public void error ( SAXParseException e) 
	throws SAXParseException {
		throw e;
	}

	public void ignorableWhitespace (char buf [], int offset, int len)
	    throws SAXException
    	{
        	// Ignore it
    	}


	
	public void endElement ( String namespaceURI, 
		String local, String name ) 
	throws SAXException
	{
		if (name.compareTo(TESTCASE) == 0) {
			testCaseContext = false;
			stringParser = new StringMsgParser();
			// stringParser.disableInputTracking();
			if (debugFlag) stringParser.enableDebugFlag();
			stringParser.setParseExceptionListener(this);
			SIPMessage[] sipMessage = null;
			SIPHeader sipHeader = null;
			URI	sipURL = null;
			try {
				if (testMessageType.equals(MESSAGE)) {
				  sipMessage =  
				  stringParser.parseSIPMessage(testMessage);
				  encodedMessage = sipMessage[0].encode();
				  if (testJAIN) {
                                     MessageImpl messageImpl=
					  new MessageImpl(encodedMessage );
                                     messageImpl.testGetMethods(jainLogWriter);
				     encodedMessage = messageImpl.toString();
				  }
			        } else if (testMessageType.equals(SIP_HEADER)){
			          sipHeader = 
				  stringParser.parseSIPHeader(testMessage);
				  encodedMessage = sipHeader.encode();
                                  HeaderImpl headerImpl;
				  if (testJAIN) {
                                   if (sipHeader instanceof SIPHeaderList) {
				      encodedMessage = null;
                                      SIPHeaderList list=
						(SIPHeaderList)sipHeader;
                                      SIPHeader[] array=list.toArray();
                                      for( int i=0;i<array.length;i++) {
                                         SIPHeader header=array[i];
				         try {
                                           headerImpl =
                                           HeaderMap.getJAINHeaderFromNISTHeader
                                                                (header);
			
                                           headerImpl.testGetMethods
							(jainLogWriter);
					   if (encodedMessage == null) {
					     encodedMessage = 
							headerImpl.toString();
					   } else {
					   encodedMessage +=
						headerImpl.toString();
					   }
					 } catch (IllegalArgumentException ex){
						continue;
					 }
                                      }
                                    } else { 
					 try {
                                           headerImpl=
                                           HeaderMap.getJAINHeaderFromNISTHeader
                                                                (sipHeader);
                                           headerImpl.testGetMethods
						(jainLogWriter);
					   encodedMessage = 
							headerImpl.toString();
					 } catch (IllegalArgumentException ex){
						// Ignore
				         }
                                    }
				  }
				} else if (testMessageType.equals(SIP_URL)) {
				  sipURL = 
				   stringParser.parseSIPUrl(testMessage);
				  if (testJAIN) {
                                    SipURLImpl sip=new SipURLImpl(sipURL);
                                    sip.testGetMethods(jainLogWriter);
				    encodedMessage = sip.toString();
				  } else {
				    encodedMessage = sipURL.encode();
				  }
				} else throw 
				  new SAXException("Torture: Internal error");
			} catch (SIPParseException ex) {
			     try {
				handleException(ex);
			     } catch (SIPParseException ex1) {
				  ex1.printStackTrace();
				  System.out.println("Unexpected excception!");
				  System.exit(0);
			     }
			}  catch (SipParseException ex) {
			     ex.printStackTrace();
			     System.out.println("Unexpected excception!");
			     System.exit(0);

		        }  
			Enumeration e = exceptionTable.elements();
			while (e.hasMoreElements()) {
				ExpectedException ex = 
					(ExpectedException) e.nextElement();
				if ( !ex.fired) {   
				    formatFailureDiagnostic( ex, 
					"Expected Exception not trapped ");
				}  else {
				    formatStatusMessage(ex,
					    "Expected exception generated " );
			   	}
			}
			
			emitBeginTag(TESTCASE);
			if (!failureFlag ) {
				emitAttribute(STATUS, "PASSED" );
			} else {
				emitAttribute(STATUS, "FAILED");
			}
			emitEndTag();
			emitTaggedCData(DESCRIPTION,	testDescription );
			if (encodedMessage != null) 
			        emitTaggedCData(MESSAGE,	encodedMessage 	);
			else emitTaggedCData(MESSAGE,	testMessage 	);
			if (failureFlag) {
				emitTaggedData(DIAGNOSTIC_MESSAGES, 
						failureReason);
			}
			if (successFlag) {
				emitTaggedData(DIAGNOSTIC_MESSAGES, 	
						statusMessage);
			}
		       if (outputParsedStructures )  {
			   if (sipMessage != null) {
				for (int i = 0 ; i < sipMessage.length; i++ ) {
			      	   emitTaggedCData(PARSED_OUTPUT,
				   	   sipMessage[i].toString());
				}
			   } else if ( sipHeader != null) {
		      	  	emitTaggedCData(PARSED_OUTPUT,
						sipHeader.toString());
			   } else if ( sipURL != null ) {
		      	  	emitTaggedCData(PARSED_OUTPUT,
						sipURL.toString());
			   }
		        }
			emitEndTag(TESTCASE);
			if (failureFlag && abortOnFail) {
				testLogWriter.close();
				System.exit(0);
			}
			
		} else if ( name.compareTo(DESCRIPTION) == 0 ) {
			descriptionContext = false;
		} else if (name.compareTo(MESSAGE) == 0 )  	     {
			messageContext = false;
		} else if (name.compareTo(EXCEPTION_TEXT) == 0 )  	     {
			exceptionTextContext = false;
		} else if (name.compareTo(EXPECT_EXCEPTION) == 0 )   {
			   expectExceptionContext = false;
                        ExpectedException ex = 
				new ExpectedException(exceptionClassName, 
							exceptionHeader);
			this.exceptionTable.put(exceptionHeader,ex);
			exceptionHeader = null;
		} else if (name.compareTo(EXCEPTION_MESSAGE)  == 0 ) {
			exceptionMessageContext = false;
			exceptionMessage = null;
		} else if (name.compareTo(SIP_HEADER)  == 0 ) {
			sipHeaderContext = false;
		} else if (name.compareTo(SIP_URL)  == 0 ) {
			sipURLContext = false;
		} else if (name.compareTo(TORTURE)  == 0 ) {
			emitEndTag(TEST_OUTPUT);
			testLogWriter.close();
		}
		
              
	}
	
	protected void emitBeginTag ( String tagName) {
		testLogWriter.println( LESS_THAN + tagName );
	}
	protected void emitTag ( String tagName) {
		testLogWriter.println( LESS_THAN + tagName + GREATER_THAN );
	}

	protected void emitTaggedData( String tag, String data) {
		emitTag(tag);
		testLogWriter.println(data);
		emitEndTag(tag);
	}
	
	/* A stand alone tag */
	protected void emitBeginEndTag ( String tagName ) {
		testLogWriter.println
		( LESS_THAN + tagName + SLASH +  GREATER_THAN);
	}
	protected void emitEndTag ( ) {
		testLogWriter.println( GREATER_THAN);
	}
	
	protected void emitEndTag ( String tagName ) {
		testLogWriter.println(LESS_THAN + SLASH + 
				tagName + GREATER_THAN );
	}

	protected void emitTaggedCData ( String str ) {
		testLogWriter.println("<![CDATA[" + str + "]]>" );
	
	}
	protected void emitTaggedCData ( String tag,  String str ) {
		emitTag(tag);
		testLogWriter.println("<![CDATA[" + str + "]]>" );
		emitEndTag(tag);
	
	}

	protected void emitString( String str) {
		testLogWriter.println(str);
	}
	
	protected void emitAttribute (String attrName, String attrval ) {
		testLogWriter.println(attrName + EQUALS + "\"" + 
			attrval + "\"" );
	}
	

	protected String emitStringAttribute(String attrName, String attrval) {
		return attrName + EQUALS  + "\"" + attrval + "\"" + "\n";
	}

	protected String emitStringTag(String tag) {
		return "<" + tag  + ">" + "\n";
	}

	protected String emitStringBeginTag(String tag) {
		return "<" + tag + "\n";
	}
	protected String emitStringEndTag() {
		return ">"+ "\n";
	}

	protected String emitStringEndTag(String tag) {
		return "</" + tag + ">\n";
	}

	protected String emitStringCData(String msg) {
		return	"<![CDATA[" + msg + "]]>\n" ;
	}
	

	protected String emitStringCData( String tag, String msg) {
		return  emitStringTag(tag) +
			emitStringCData( msg ) +
			emitStringEndTag(tag);
	}
	
	protected void formatFailureDiagnostic( SIPParseException ex,
				String message) {

		failureReason += emitStringBeginTag(DIAGNOSTIC);
	 	failureReason += emitStringAttribute( REASON, message );
		failureReason += emitStringEndTag();

		failureReason += emitStringAttribute(EXCEPTION_CLASS,
				ex.getClass().getName() );


		failureReason += emitStringCData(EXCEPTION_TEXT,
				    ex.getText());

		// DataOutputStream dos = new DataOutputStream(bos); 
		 ByteArrayOutputStream bos = 
				new ByteArrayOutputStream(4096);
		ex.printStackTrace(new PrintStream(bos,true));
		failureReason += 
				emitStringCData(STACK_TRACE, bos.toString());
	   	failureReason += emitStringCData(MESSAGE,ex.getMessage());
	   	failureReason += emitStringEndTag(DIAGNOSTIC);
	}

	protected void formatFailureDiagnostic( String hdr,
				String reason) {
		failureReason += emitStringBeginTag(DIAGNOSTIC);
	 	failureReason += emitStringAttribute(STATUS, "BAD" );
	 	failureReason += emitStringAttribute( REASON, reason );
		failureReason += emitStringEndTag();
		failureReason += emitStringCData(EXCEPTION_TEXT, hdr);
	   	failureReason += emitStringEndTag(DIAGNOSTIC);
	}

	protected void formatFailureDiagnostic (ExpectedException ex,
					String reason)  {
		failureReason += emitStringBeginTag(DIAGNOSTIC);
	 	failureReason += emitStringAttribute(STATUS, "BAD" );
	 	failureReason += emitStringAttribute( REASON, reason );
	 	failureReason += emitStringAttribute( EXCEPTION_CLASS, 
				ex.exceptionClass.getName());
		failureReason += emitStringEndTag();
		failureReason += emitStringCData(EXCEPTION_TEXT, 
						ex.exceptionHeader);
	   	failureReason += emitStringEndTag(DIAGNOSTIC);
	}

	protected void formatStatusMessage ( String hdr,
				String reason) {
		statusMessage += emitStringBeginTag(DIAGNOSTIC);
	 	statusMessage += emitStringAttribute( STATUS, "OK" );
	 	statusMessage += emitStringAttribute( REASON, reason );
		statusMessage += emitStringEndTag();
		statusMessage += emitStringCData(EXCEPTION_TEXT, hdr);
	   	statusMessage += emitStringEndTag(DIAGNOSTIC);
	}

	protected void formatStatusMessage ( ExpectedException ex ,
				String reason) {
		statusMessage += emitStringBeginTag(DIAGNOSTIC);
	 	statusMessage += emitStringAttribute( STATUS, "OK" );
	 	statusMessage += emitStringAttribute( REASON, reason );
	 	statusMessage += emitStringAttribute( EXCEPTION_CLASS, 
				ex.exceptionClass.getName());
		statusMessage += emitStringEndTag();
		statusMessage += emitStringCData(EXCEPTION_TEXT, 
						ex.exceptionHeader);
	   	statusMessage += emitStringEndTag(DIAGNOSTIC);
	}

	public static void main (String argv [])
        {
        	if (argv.length != 1) {
            		System.err.println ("Usage: cmd filename");
            		System.exit (1);
        	}
        	try {
			XMLReader saxParser = (XMLReader)Class.forName
			    ("org.apache.xerces.parsers.SAXParser").
			    newInstance();
			saxParser.setContentHandler(new Torture());
			saxParser.setFeature
			  ("http://xml.org/sax/features/validation",true);
            		saxParser.parse (argv [0]);
        	} catch (SAXParseException spe) {
           		// Error generated by the parser
           		System.out.println ("\n** Parsing error" 
              		+ ", line " + spe.getLineNumber ()
              		+ ", uri " + spe.getSystemId ());
           		System.out.println("   " + spe.getMessage() );
           		// Use the contained exception, if any
           		Exception  x = spe;
           		if (spe.getException() != null)
              			 x = spe.getException();
           		x.printStackTrace();
        	} catch (SAXException sxe) {
           		// Error generated by this application
           		// (or a parser-initialization error)
           		Exception  x = sxe;
           		if (sxe.getException() != null) x = sxe.getException();
           		x.printStackTrace();
        	} catch (IOException ioe) {
           		// I/O error
           		ioe.printStackTrace();
        	} catch (Exception pce) {
            		// Parser with specified options can't be built
            		pce.printStackTrace();
        	}
        	System.exit (0);
    	}
               

}
