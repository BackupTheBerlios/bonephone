/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD)        *
 * See ../../../../doc/uncopyright.html for conditions of use                  *
 * Author: M. Ranganathan (mranga@nist.gov)                                    *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                              *
 ******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import java.io.*;
import antlr.*;
import gov.nist.sip.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

/**
 * Parse SIP message and parts of SIP messages such as URI's etc 
 * from memory and return a structure.
 * Intended use:  UDP message processing.
 * This class is used when you have an entire SIP message or SIPHeader
 * or SIP URL in memory and you want to generate a parsed structure from
 * it. For SIP messages, the payload can be binary or String. 
 * If you have a binary payload,
 * use parseSIPMessage(byte[]) else use parseSIPMessage(String)
 * The payload is accessible from the parsed message using the getContent and
 * getContentBytes methods provided by the SIPMessage class. If SDP parsing
 * is enabled using the parseContent method, then the SDP body is also parsed
 * and can be accessed from the message using the getSDPAnnounce method.
 * Currently only eager parsing of the message is supported (i.e. the
 * entire message is parsed in one feld swoop).
 *@version 1.0
 *@author <A href=mailto:mranga@nist.gov> M. Ranganathan </A>
 */

public class StringMsgParser extends MsgParser
implements SIPErrorCodes   {
    
    private String rawMessage;
    // Unprocessed message  (for error reporting)
    private String rawMessage1;
    // Unprocessed message  (for error reporting)
    private String currentMessage;
    // the message being parsed. (for error reporting)
    private SIPParseExceptionListener phandler;
    
    private Vector messageHeaders; // Message headers
    
    private int bufferPointer;
	
    private boolean bodyIsString;

    private byte[] currentMessageBytes;
    
    
    
    
    /**
     *@since v0.9
     */
    public StringMsgParser() {
        input_stream = null;
        parseMessageContent = false;
        trackInput  = true;
        messageHeaders = new Vector(10,10);
	Debug.debug = false;
    }
    public void enableDebugFlag() {
        super.setDebugFlag();
    }
    
   /**
    *Constructor (given a parse exception handler).
    *@since 1.0
    *@param exhandler is the parse exception listener for the message parser.
    */
    public StringMsgParser( SIPParseExceptionListener exhandler) {
        phandler = exhandler;
        input_stream = null;
        parseMessageContent = false;
        trackInput  = true;
        messageHeaders = new Vector(10,10);
        bufferPointer = 0;
    }
    
    
  /**
   * Set up the parser to skip over badly formatted headers.
   * @param ex SIPParseException to handle.
   */
    
    protected  void
    handleParseException( SIPParseException ex )
    throws SIPParseException {
        // System.out.println("Handle Parse exception:");
        // ex.printStackTrace();
        if (phandler != null) {
            phandler.handleException(ex);
        } else {
            // default action (silently reject the error).
            super.handleParseException(ex);
        }
    }
    
   /**
    * Flag that inicates that parsing of a message is done.
    * Unused here but is used in the pipelined parser and
    * this is abstract in the base class so it has to be
    * defined.
    */
    protected void messageDone(SIPMessage sipmsg) {}
    
    
    /** Get the message body.
    */
    protected String getMessageBody() {
	if (Debug.debug) Debug.println("Content Length = " + contentLength);
        if (this.contentLength == 0 ) {
	  return null;
        } else {
            int endIndex = bufferPointer + this.contentLength;
	    String body;
            // guard against bad specifications.
            if (endIndex > currentMessage.length()) {
               endIndex = currentMessage.length();
               body = currentMessage.substring(bufferPointer,endIndex);
	       bufferPointer = endIndex;
	    } else{
                  body = currentMessage.substring(bufferPointer,endIndex);
		 bufferPointer = endIndex + 1;
	    }
            this.contentLength =  0;
            return body;
        }
        
    }

   /** Get the message body as a byte array.
   */
   protected byte[] getBodyAsBytes() {
	if (Debug.debug) Debug.println("Content Length = " + contentLength);
        if (this.contentLength == 0 ) {
	  return null;
        } else {
            int endIndex = bufferPointer + this.contentLength;
            // guard against bad specifications.
            if (endIndex > currentMessageBytes.length) {
               endIndex = currentMessageBytes.length;
	    } 
	    byte[] body = new byte[endIndex - bufferPointer];
	    for (int i = bufferPointer, k = 0; i < endIndex; i++,k++) {
	      body[k] = currentMessageBytes[i];
	    }
	    bufferPointer = endIndex;
            this.contentLength =  0;
            return  body;
        }
        
    }
	

    
   /** Return the contents till the end of the buffer (this is useful when
    * you encounter an error.
    */
    protected String readToEnd() {
        String body = currentMessage.substring(bufferPointer);
        bufferPointer += body.length();
        return body;
    }

    /** Return tbe bytes to the end of the message.
    * This is invoked when the parser is invoked with an array of bytes
    * rather than with a string.
    */
   protected byte[] readBytesToEnd() {
	byte[] body = new byte[currentMessageBytes.length - bufferPointer];
	int endIndex = currentMessageBytes.length;
	for (int i = bufferPointer, k = 0; i < endIndex; i++,k++) {
	    body[k] = currentMessageBytes[i];
	}
	bufferPointer = endIndex;
        this.contentLength =  0;
        return  body;
   }

    
  /**
   * add a handler for header parsing errors.
   * @param  pexhadler is a class
   *  	that implements the SIPParseExceptionListener interface.
   */
    
    public void setParseExceptionListener
    ( SIPParseExceptionListener pexhandler ) {
        phandler = pexhandler;
    }

    /** Return true if the body is encoded as a string. 
    * If the parseSIPMessage(String) method is invoked then the body
    * is assumed to be a string.
    */
    protected boolean isBodyString() { return bodyIsString; }


  /** Parse a buffer containing a single SIP Message where the body
   * is an array of un-interpreted bytes. This is intended for parsing
   * the message from a memory buffer when the buffer.  
   * @param msgBuffer a byte buffer containing the messages to be parsed.
   *   This can consist of multiple SIP Messages concatenated together.
   * @return a SIPMessage[] structure (request or response)
   * 			containing the parsed SIP message.
   * @exception SIPIllegalMessageException is thrown when an
   * 			illegal message has been encountered (and
   *			the rest of the buffer is discarded).
   * @see SIPParseExceptionListener
   */
   public SIPMessage[] parseSIPMessage(byte[] msgBuffer) 
   throws SIPParseException {
     bufferPointer = 0;
     bodyIsString = false;
     Vector retval = new Vector();
     currentMessageBytes = msgBuffer;
     while (bufferPointer < msgBuffer.length) {
	int s;
        // Squeeze out leading CRLF
	for (s = bufferPointer; s < msgBuffer.length  ; s++) {
		if ((char)msgBuffer[s] != '\r'  &&
		    (char)msgBuffer[s] != '\n') break;
	}
	if (s == msgBuffer.length) break;

	// Find the end of the SIP message.
	int  f;
	for (f = s ; f < msgBuffer.length -4 ; f ++) {
		if ( (char) msgBuffer[f]   == '\r' && 
		     (char) msgBuffer[f+1] == '\n' &&
		     (char) msgBuffer[f+2] == '\r' && 
		     (char) msgBuffer[f+3] == '\n') {
		     break;
		} 
	}
	if (f < msgBuffer.length) f +=4;
	else {
	     // Could not find CRLFCRLF end of message so look for LFLF
	     for (f = s; f < msgBuffer.length -2 ; f++) {
		if ((char)msgBuffer[f] == '\n' &&
		    (char)msgBuffer[f] == '\n') break;
	     }
	     if (f < msgBuffer.length) f += 2;
	     else throw new SIPParseException("Message not terminated");
	}

	// Encode the body as a UTF-8 string.
	String messageString = null;
	try {
	   messageString = new String(msgBuffer,s, f - s, "UTF-8");
        } catch( UnsupportedEncodingException ex) {
		throw new SIPParseException("Bad message encoding!");
	}
	bufferPointer = f;
	StringBuffer message = new StringBuffer(messageString);
	int length = message.length();
	   // Get rid of CR to make it uniform for the parser.
            for ( int k = 0; k < length ; k++ ) {
                if (message.charAt(k) == '\r' ) {
                    message.deleteCharAt(k);
                    length --;
                }
            }
            
            
            if (debugFlag == 1 ) {
                for (int k = 0 ; k < length; k++) {
                    rawMessage1 = rawMessage1 + "[" + message.charAt(k) +"]";
                }
            }
            
            // The following can be written more efficiently in a single pass
            // but it is somewhat tricky.
            StringTokenizer tokenizer = new StringTokenizer
            (message.toString(),"\n",true);
            StringBuffer cooked_message = new StringBuffer();
            try {
                while( tokenizer.hasMoreElements() ) {
                    String nexttok = tokenizer.nextToken();
                    // Ignore blank lines with leading spaces or tabs.
                    if (nexttok.trim().equals("")) cooked_message.append("\n");
                    else cooked_message.append(nexttok);
                }
            } catch (NoSuchElementException ex) {
            }
            
            String message1 = cooked_message.toString();
            length = message1.indexOf("\n\n") + 2;
            
            // Handle continuations - look for a space or a tab at the start
            // of the line and append it to the previous line.
            
            
            for ( int k = 0 ; k < length - 1 ;  ) {
                if (cooked_message.charAt(k) == '\n') {
                    if ( cooked_message.charAt(k+1) == '\t' ||
                    cooked_message.charAt(k+1) == ' ') {
                        cooked_message.deleteCharAt(k);
                        cooked_message.deleteCharAt(k);
                        length --;
                        length --;
                        if ( k == length) break;
                        continue;
                    }

                    if ( cooked_message.charAt(k+1) == '\n') {
                        cooked_message.insert(k,'\n');
                        length ++;
                        k ++;
                    }
                }
                k++;
            }
            cooked_message.append("\n\n");
            
            // Separate the string out into substrings for
            // error reporting.
            currentMessage = cooked_message.toString();
            SIPMessage sipmsg = this.parseMessage(currentMessage);
	    retval.add(sipmsg);
        }
        SIPMessage[] sipMsgArray = new SIPMessage[retval.size()];
        for( int i = 0; i < retval.size(); i++) {
            sipMsgArray[i] = (SIPMessage) retval.elementAt(i);
        }
        return sipMsgArray;



   }

  /**
   * Parse a buffer containing one or more SIP Messages  and return an array of
   * SIPMessage parsed structures. Note that the current limitation is that
   * this does not handle content encoding properly. The message content is
   * just assumed to be encoded using the same encoding as the sip message 
   * itself (i.e. binary encodings such as gzip are not supported).
   * @param sipMessages a String containing the messages to be parsed.
   *   This can consist of multiple SIP Messages concatenated together.
   * @return a SIPMessage[] structure (request or response)
   * 			containing the parsed SIP message.
   * @exception SIPIllegalMessageException is thrown when an
   * 			illegal message has been encountered (and
   *			the rest of the buffer is discarded).
   * @see SIPParseExceptionListener
   */
    
    public SIPMessage[]  parseSIPMessage(String sipMessages )
    throws SIPParseException {
        // Handle line folding and evil DOS CR-LF sequences
        // System.out.println(pmessage);
        rawMessage = sipMessages;
        Vector retval = new Vector();
        String pmessage = sipMessages.trim();
	bodyIsString = true;
        
        while(true) {
            this.contentLength = 0;
            if (pmessage.trim().equals("")) break;
            
            pmessage += "\n\n";
            StringBuffer message = new StringBuffer(pmessage);
            // squeeze out the leading crlf sequences.
            while(message.charAt(0) == '\r' || message.charAt(0) == '\n') {
                bufferPointer ++;
                message.deleteCharAt(0);
            }
            
            // squeeze out the crlf sequences and make them uniformly CR
            String message1 = message.toString();
            int length;
            length = message1.indexOf("\r\n\r\n");
            if (length > 0 ) length += 4;
            if (length == -1) {
                length = message1.indexOf("\n\n");
                if (length == -1)
                    throw new SIPParseException("no trailing crlf");
            } else length += 2;
            
            
	   // Get rid of CR to make it uniform.
            for ( int k = 0; k < length ; k++ ) {
                if (message.charAt(k) == '\r' ) {
                    message.deleteCharAt(k);
                    length --;
                }
            }
            
            
            if (debugFlag == 1 ) {
                for (int k = 0 ; k < length; k++) {
                    rawMessage1 = rawMessage1 + "[" + message.charAt(k) +"]";
                }
            }
            
            // The following can be written more efficiently in a single pass
            // but it is somewhat tricky.
            StringTokenizer tokenizer = new StringTokenizer
            (message.toString(),"\n",true);
            StringBuffer cooked_message = new StringBuffer();
            try {
                while( tokenizer.hasMoreElements() ) {
                    String nexttok = tokenizer.nextToken();
                    // Ignore blank lines with leading spaces or tabs.
                    if (nexttok.trim().equals("")) cooked_message.append("\n");
                    else cooked_message.append(nexttok);
                }
            } catch (NoSuchElementException ex) {
            }
            
            message1 = cooked_message.toString();
            length = message1.indexOf("\n\n") + 2;
            
            // Handle continuations - look for a space or a tab at the start
            // of the line and append it to the previous line.
            
            
            for ( int k = 0 ; k < length - 1 ;  ) {
                if (cooked_message.charAt(k) == '\n') {
                    if ( cooked_message.charAt(k+1) == '\t' ||
                    cooked_message.charAt(k+1) == ' ') {
                        cooked_message.deleteCharAt(k);
                        cooked_message.deleteCharAt(k);
                        length --;
                        length --;
                        if ( k == length) break;
                        continue;
                    }
                    if ( cooked_message.charAt(k+1) == '\n') {
                        cooked_message.insert(k,'\n');
                        length ++;
                        k ++;
                    }
                }
                k++;
            }
            cooked_message.append("\n\n");
            
            
            // Separate the string out into substrings for
            // error reporting.
            
            
            currentMessage = cooked_message.toString();
	    if (Debug.debug) Debug.println(currentMessage);
	    bufferPointer = currentMessage.indexOf("\n\n") + 3 ;
            SIPMessage sipmsg = this.parseMessage(currentMessage);
            retval.addElement(sipmsg);
	    Debug.print("buffer pointer = " + bufferPointer);
	    try {
              pmessage = currentMessage.substring(bufferPointer);
	    } catch (Exception ex) {
		if (Debug.debug) ex.printStackTrace();
		break;
	    }
        }
        SIPMessage[] sipMsgArray = new SIPMessage[retval.size()];
        for( int i = 0; i < retval.size(); i++) {
            sipMsgArray[i] = (SIPMessage) retval.elementAt(i);
        }
        return sipMsgArray;
        
    }
    
    
   /** This is called repeatedly by parseSIPMessage to parse
    * the contents of a message buffer. This assumes the message
    * already has continuations etc. taken care of.
    * prior to its being called.
    */
    private SIPMessage parseMessage(String currentMessage )
    throws  SIPParseException {
        // position line counter at the end of the
        // sip messages.
        int sip_message_size = 0; // # of lines in the sip message
        StringTokenizer tokenizer = new StringTokenizer
        (currentMessage,"\n",true);
        messageHeaders = new Vector(); // A list of headers for error reporting
        try {
            boolean flag = true;
            while( tokenizer.hasMoreElements() ) {
                String nexttok = tokenizer.nextToken();
                if (nexttok.equals("\n")) {
                    String nextnexttok = tokenizer.nextToken();
                    if (nextnexttok.equals("\n") ) {
                        messageHeaders.add(nexttok);
                        messageHeaders.add(nextnexttok);
                        flag = false;
                    } else messageHeaders.add(nextnexttok);
                } else messageHeaders.add (nexttok);
                if (flag) sip_message_size ++;
            }
        } catch (NoSuchElementException ex) {
        }
        currentLine = 0;
        currentHeader = (String) messageHeaders.elementAt(currentLine);
        DataInputStream dis = new DataInputStream
            (new ByteArrayInputStream(currentMessage.getBytes()));
        input_stream = dis;
        initParser(dis);
        sip_messageparser = new sip_messageParser(selector);
        sip_messageparser.parserMain = this;
        
        SIPMessage sipmsg = null;
        super.setEOLLexer("method_keywordLexer");
        select("method_keywordLexer");
        try {
            // Invoke the parser to parse the sip message.
            sipmsg = null;
            sipmsg = sip_messageparser.sip_message();
            // This should probably be done in a more
            // "generalized" fashion (i.e. with
            // pluggable media content handlers etc.)
            if (sipmsg != null &&   this.parseMessageContent &&
            sipmsg.contentTypeHeader != null &&
            sipmsg.contentTypeHeader.compareMediaRange
            (MimeTypes.application_sdp)  == 0 ) {
                String ncontent = null;
                ContentLength clength = sipmsg.getContentLengthHeader();
		try {
                  if (clength != null &&
                      clength.getContentLength() != 0) {
                      String content = sipmsg.getMessageContent();
                      if (content != null) ncontent = content.trim();
                  } else if (clength == null) {
                    ncontent = sipmsg.getMessageContent();
                    
                  }
		} catch (UnsupportedEncodingException ex) { /* Ignore*/ }
                if ( ncontent != null && ! ncontent.equals("") ) {
                    StringMsgParser smp = new StringMsgParser();
                    smp.phandler = this.phandler;
                    smp.messageHeaders = this.messageHeaders;
                    
                    smp.currentLine = sip_message_size + 2;
                    sipmsg.sdpAnnounce = smp.parseSDPAnnounce(ncontent);
                }
            }
        } catch ( antlr.RecognitionException e) {
            if(debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
                System.err.println
                ("*********** Raw Message ***************");
                System.err.println(rawMessage);
                System.err.println
                ("*********** Msg text ***************");
                System.err.println(rawMessage1);
                System.err.println
                ("*********** Processed Message**************");
                System.err.println(currentMessage);
            }
            SIPIllegalMessageException ex =
            new SIPIllegalMessageException(e.getMessage());
            ex.setHeaderText(getCurrentHeader());
            ex.fillInStackTrace();
            throw ex;
        } catch (antlr.TokenStreamException e) {
            if(debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
                System.err.println
                ("*********** Raw Message ***************");
                System.err.println(rawMessage);
                System.err.println
                ("*********** Msg text ***************");
                System.err.println(rawMessage1);
                System.err.println
                ("***********Msg text***************");
                System.err.println(currentMessage);
            }
            SIPIllegalMessageException ex =
            new SIPIllegalMessageException(e.getMessage());
            ex.setHeaderText(getCurrentHeader());
            ex.fillInStackTrace();
            throw ex;
        } catch (antlr.ANTLRException e) {
            if(debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
                System.err.println
                ("*********** Raw Message ***************");
                System.err.println(rawMessage);
                System.err.println
                ("***********Msg text***************");
                System.err.println(rawMessage1);
                System.err.println
                ("*********** Processed Message**************");
                System.err.println(currentMessage);
                // InternalError.handleException(e);
            }
        }
        if (sipmsg == null) {
            try {
                input_stream.close();
            } catch ( IOException ex) {
                InternalError.handleException(ex);
            }
        }
        
        return sipmsg;
        
    }
    
    
  /**
   * Parse an SDP announce message and return a SDPAnnounce message
   * parsed structure.
   * @param sdpAnnounceMessage a string containing the SDP message to be parsed.
   * @return an SDPAnnounce structure containing the SDP message
   * @see SDPAnnounce
   * @exception SIPIllegalMessageException  if there was an error parsing the
   *		message.
   */
    public SDPAnnounce parseSDPAnnounce (String sdpAnnounceMessage)
    throws SIPParseException {
	// Clean up the message -- replace \r\n with \n
	StringBuffer sbuf = new StringBuffer(sdpAnnounceMessage);
	int length = sbuf.length();
	for (int i = 0; i < length; i++) {
		if (sbuf.charAt(i) == '\r') {
			sbuf.deleteCharAt(i);
			length --;
		}
	}
        // add a few trailing CRLF's to demarkate the end of msg.
        sbuf.append("\n\n");
	String nmessage = sbuf.toString();
        DataInputStream dis =
        new DataInputStream(new ByteArrayInputStream(nmessage.getBytes()));
        input_stream = dis;
        initParser(dis);
        super.setEOLLexer("sdpLexer");
        sdp_announceparser = new sdp_announceParser(selector);
        sdp_announceparser.parserMain = this;
        SDPAnnounce sdp = null;
        select("sdpLexer");
        try {
            sdp = sdp_announceparser.sdp_announce();
        } catch ( ANTLRException e ) {
            if(debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
            }
            throw new SIPIllegalMessageException(e.getMessage());
        }
        return sdp;
    }
    
    
  /**
   * Parse a sip date strucutre (useful for extensions).
   *@param date is a String that is the SIP Date  structure.
   *@return SIPDate is the parsed SIPDate structre.
   *@since 1.0
   *@exception SIPParseException when the date is badly formatted.
   */
    public SIPDate parseSIPDate (String date)
    throws SIPParseException
    {
        ByteArrayInputStream bis =
        new  ByteArrayInputStream(date.getBytes());
        DataInputStream dis = new DataInputStream(bis);
        input_stream = dis;
        initParser(dis);
        SIPDate retval = null;
        sip_messageParser sip_messageparser =
        new sip_messageParser(selector);
        select("dateLexer");
        try {
            retval = sip_messageparser.sip_date();
        } catch (ANTLRException e) {
            if (debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
            }
            throw new SIPParseException(e.getMessage());
        }
        return retval;
        
    }
    
    
    
  /**
   * Parse an address (nameaddr or address spec)  and return and address
   * structure.
   * @param address is a String containing the address to be parsed.
   * @return a parsed address structure.
   * @since v1.0
   * @exception  SIPParseException when the address is badly formatted.
   */
    
    public Address parseAddress (String address)
    throws SIPParseException
    {
        ByteArrayInputStream bis =
        new  ByteArrayInputStream(address.getBytes());
        DataInputStream dis = new DataInputStream(bis);
        input_stream = dis;
        initParser(dis);
        Address retval = null;
        sip_messageParser sip_messageparser =
        new sip_messageParser(selector);
        select("charLexer");
        try {
            retval = sip_messageparser.address();
        } catch (ANTLRException e) {
            if (debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
            }
            throw new SIPParseException(e.toString());
        }
        return retval;
    }
    
    
  /**
   * Parse an authority string  and return a parsed structure
   * (added in support of jain-sip).
   * @param schemedata is a String containing the scheme data part of the url.
   * @return a parsed Authority structure.
   * @since v1.0
   * @exception throws a SIPParseException when the address is badly formatted.
   */
    
    public Authority parseAuthority(String schemedata)
    throws SIPParseException {
        ByteArrayInputStream bis =
        new  ByteArrayInputStream(schemedata.getBytes());
        DataInputStream dis = new DataInputStream(bis);
        input_stream = dis;
        initParser(dis);
        sip_urlParser sip_urlparser = new sip_urlParser(selector);
        sip_urlparser.parserMain = this;
        select("charLexer");
        Authority auth = null;
        try {
	    if (schemedata.indexOf('.') == -1) {
		auth = sip_urlparser.reg_name();
	    } else auth = sip_urlparser.server_h();
        } catch ( ANTLRException e) {
            if (debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
            }
            throw new SIPParseException(e.toString());
        }
        return auth;
    }
    
  /**
   * Parse a host:port and return a parsed structure.
   * @param hostport is a String containing the host:port to be parsed
   * @return a parsed address structure.
   * @since v1.0
   * @exception throws a SIPParseException when the address is badly formatted.
   */
    public HostPort parseHostPort (String hostport )
    throws SIPParseException
    {
        ByteArrayInputStream bis =
        new  ByteArrayInputStream(hostport.getBytes());
        DataInputStream dis = new DataInputStream(bis);
        input_stream = dis;
        initParser(dis);
        HostPort retval = null;
        host_nameParser hostname_parser =
        new host_nameParser(selector);
        hostname_parser.parserMain = this;
        select("charLexer");
        try {
            retval = hostname_parser.host_port();
        } catch (ANTLRException e) {
            if (debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
            }
            throw new SIPParseException(e.toString());
        }
        return retval;
        
    }
    
  /**
   * Parse a host name and return a parsed structure.
   * @param host is a String containing the host name to be parsed
   * @return a parsed address structure.
   * @since v1.0
   * @exception throws a SIPParseException when the hostname is badly formatted.
   */
    public Host parseHost (String host )
    throws SIPParseException
    {
        ByteArrayInputStream bis =
        new  ByteArrayInputStream(host.getBytes());
        DataInputStream dis = new DataInputStream(bis);
        input_stream = dis;
        initParser(dis);
        Host retval = null;
        host_nameParser hostname_parser =
        new host_nameParser(selector);
        hostname_parser.parserMain = this;
        select("charLexer");
        try {
            retval = hostname_parser.host();
        } catch (ANTLRException e) {
            if (debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
            }
            throw new SIPParseException(e.toString());
        }
        return retval;
        
    }
    
    
  /**
   * Parse a telephone number return a parsed structure.
   * @param telphone_number is a String containing the telephone # to be parsed
   * @return a parsed address structure.
   * @since v1.0
   * @exception throws a SIPParseException when the address is badly formatted.
   */
    public TelephoneNumber parseTelephoneNumber (String telephone_number )
    throws SIPParseException
    {
        ByteArrayInputStream bis =
        new  ByteArrayInputStream(telephone_number.getBytes());
        DataInputStream dis = new DataInputStream(bis);
        input_stream = dis;
        initParser(dis);
        TelephoneNumber retval = null;
        tel_Parser tel_parser =
        new tel_Parser (selector);
        tel_parser.parserMain = this;
        select("charLexer");
        try {
            retval = tel_parser.telephone_subscriber();
        } catch (ANTLRException e) {
            if (debugFlag == 0) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
            }
            throw new SIPParseException(e.toString());
        }
        return retval;
        
    }
    
    
  /**
   * Parse a  SIP url from a string and return a URI structure for it.
   * @param sipUrl a String containing the URI structure to be parsed.
   * @return A parsed URI structure
   * @exception SIPParseException  if there was an error parsing the message.
   */
    
    public URI parseSIPUrl (String sipUrl)
    throws SIPParseException {
	if (sipUrl == null) 
		throw new IllegalArgumentException("null arg");
        ByteArrayInputStream bis =
        new  ByteArrayInputStream(sipUrl.getBytes());
        DataInputStream dis = new DataInputStream(bis);
        input_stream = dis;
        initParser(dis);
        sip_urlParser sip_urlparser = new sip_urlParser(selector);
        sip_urlparser.parserMain = this;
        select("sip_urlLexer");
        URI uri = null;
        try {
            uri = sip_urlparser.sip_url();
        } catch ( ANTLRException e) {
            if (debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
            }
            throw new SIPParseException(e.toString());
        }
        return uri;
    }
    
  /**
   * Parse an individual SIP message header from a string.
   * @param header String containing the SIP header.
   * @return a SIPHeader structure.
   * @exception SIPParseException  if there was an error parsing the message.
   */
    public SIPHeader parseSIPHeader(String header )
    throws SIPParseException {
        header += "\n\n";
        // Handle line folding.
        String nmessage = "";
        int counter = 0;
        // eat leading spaces and carriage returns (necessary??)
        int i = 0;
        while( header.charAt(i) == '\n' || header.charAt(i) == '\t'
        || header.charAt(i) == ' ') i++;
        for ( ; i < header.length(); i++) {
            if ( i < header.length() - 1 &&
            ( header.charAt(i) == '\n' && ( header.charAt(i+1) == '\t'
            || header.charAt(i+1) == ' ') ) ) {
                nmessage += ' ';
                i++;
            } else {
                nmessage += header.charAt(i);
            }
        }
        
        nmessage += "\n";
        DataInputStream dis =
        new DataInputStream(new ByteArrayInputStream(nmessage.getBytes()));
        input_stream = dis;
        initParser(dis);
        super.setEOLLexer("command_keywordLexer");
        sip_messageparser = new sip_messageParser(selector);
        sip_messageparser.parserMain = this;
        this.trapParseErrors = true; // change the default to throw an exception
        SIPHeader siphdr = null;
        select("command_keywordLexer");
        try {
            siphdr = sip_messageparser.message_header();
        } catch ( ANTLRException e) {
            if(debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                System.err.println("Header: " + nmessage);
                e.printStackTrace(System.err);
            }
            throw new SIPParseException(e.toString());
        }
        return siphdr;
    }
    
  /**
   * Parse the SIP Request Line
   * @param  message a String  containing the request line to be parsed.
   * @return  a RequestLine structure that has the parsed RequestLine
   * @exception SIPParseException  if there was an error parsing the requestLine.
   */
    
    public RequestLine parseSIPRequestLine( String message)
    throws SIPParseException {
        message += "\n";
        DataInputStream dis =
        new DataInputStream(new ByteArrayInputStream(message.getBytes()));
        input_stream = dis;
        initParser(dis);
        sip_messageparser = new sip_messageParser(selector);
        sip_messageparser.parserMain = this;
        RequestLine reqline = null;
        super.setEOLLexer("method_keywordLexer");
        select("method_keywordLexer");
        try {
            reqline = sip_messageparser.request_line();
        } catch ( ANTLRException e) {
            if(debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
            }
            throw new SIPParseException(e.toString());
        }
        return reqline;
    }
    
   /**
    * Parse the SIP Response message status line
    * @param message a String containing the Status line to be parsed.
    * @return StatusLine class corresponding to message
    * @exception SIPParseException  if there was an error parsing
    * @see StatusLine
    */
    
    public StatusLine parseSIPStatusLine (String message)
    throws SIPParseException {
        message += "\n";
        DataInputStream dis =
        new DataInputStream(new ByteArrayInputStream(message.getBytes()));
        input_stream = dis;
        initParser(dis);
        sip_messageparser = new sip_messageParser(selector);
        sip_messageparser.parserMain = this;
        StatusLine sline = null;
        super.setEOLLexer("method_keywordLexer");
        select("method_keywordLexer");
        try {
            sline = sip_messageparser.status_line();
        } catch ( ANTLRException e) {
            if(debugFlag == 1) {
                System.err.println("Current Lexer = " +
                getCurrentLexerName());
                System.err.println("exception: "+e);
                e.printStackTrace(System.err);
            }
            throw new SIPParseException(e.toString());
        }
        return sline;
    }
    
    /**
     * Increment the line counter
     */
    protected void newLine() {
        super.newLine();
        if (currentLine < messageHeaders.size()) {
            currentHeader = (String) messageHeaders.elementAt(currentLine);
        }
    }
    
    
    /**
     * Get the current header.
     */
    public String getCurrentHeader() {
        return currentHeader;
    }
    
    /**
     * get the headers as an array of strings.
     */
    
    public String[] getHeaders() {
        String [] hdrs = new String[messageHeaders.size()];
        messageHeaders.toArray(hdrs);
        return hdrs;
    }
    
   /**
    * Get the current line number.
    */
    public int getCurrentLineNumber() { return currentLine; }
    
    /**
     * Parse the message content (if it is SDP -- only sdp is currently
     * supported natively in the parser).
     */
    public void parseMessageContent() { super.parseMessageContent = true; }
    
}

