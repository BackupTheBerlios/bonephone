header {
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/**
* Revisions Since v0.9  release:
*
* 1. addes support for also header 
*
* 2. Fixed a bug that would report the wrong line for error if 
*	error occured in the address rule (parserMain.newline() is called
*	only when consuming a RETURN token)
*
* 3. Added error recovery mechanism for dealing with Malformed headers.
*     i.e. headers where you cant even tell what the type is.
*    (non-terminals request_body and repy_body have an additional case)
*
* 4. Made changes in the from and to rules in accordance with the following:
*    The Contact, From and To header fields contain a URL. If the URL
*    contains a comma, question mark or semicolon, the URL MUST be
*    enclosed in angle brackets (< and >). Any URL parameters are
*    contained within these brackets. If the URL is not enclosed in angle
*    brackets, any semicolon-delimited parameters are header-parameters,
*    not URL parameters.
*
* 5. added error_info
*
* 6. Revisions to reflect new (regularized) classes for sip headers.
*/

package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import java.lang.reflect.*;
import gov.nist.sip.*;
import gov.nist.sip.sipheaders.*;
}

class sip_messageParser extends sip_urlParser;

options {
	k=1;
	defaultErrorHandler=false;
	importVocab = sip_urlParser;
	exportVocab = sip_messageParser;
	genHashLines = true;
}




sip_version returns [ String v ] 
{
	String s = "";
	v = null;
}
: t1:SIP { s += t1.getText(); }  t2:SLASH { s += t2.getText(); } 
(( t3:DIGIT { s += t3.getText(); } t4:DOT { s += t4.getText(); } 
   t5:DIGIT  { s += t5.getText(); })
| (t6:ID { 	
   if (t6.getText().length() != 3) {
	throw new RecognitionException("Invalid Version String");
    } else {
	s += t6.getText(); 
    }
  }
))
{
	v = s.toUpperCase(); // Return in canonical form.
}
;

sip_message returns [ SIPMessage m ] 
{ 
	m = null;
	selectLexer("method_keywordLexer");
	setEOLLexer("method_keywordLexer");
	while (true) {
	   try {
	      // Strange token in the input stream.
	      if (LT(1).getText() == null) return null;
	      // Consume leading RETURNs 
	      else if (LT(1).getText().equals("\n")) consume();
	      // Reached the end of file.
	      else if (LA(1) == Token.EOF_TYPE) return null;
	      else break;
	   } catch ( TokenStreamException ex ) {
	 	return null;
	   }
	}
	// Select the lexical analyser to pick when hitting EOL.
	setEOLLexer("command_keywordLexer");
	// Reset the line counter here (otherwise we will report errors
	// on the wrong line).
	// Enable line folding support in the pre-processor.
	parserMain.setHandleContinuations(true);
	// Set the content length to some arbitrary number.
	startTracking();
}
: NULL 
{ stopTracking(); m = null; } 
| ( (SIP) => (m = response) | (m = request) ) 
{  
	if (m != null) {
	  parserMain.messageDone(m);
	  m.setInputText(stopTracking()); 
	}
}
;
exception  
catch [ RecognitionException ex ] {
	// Enable if debugging is on...
	ex.fillInStackTrace();
	parserMain.dbgprint("RecognitionException :" + ex.getMessage());
	parserMain.dbgprint(ex);
	// Eat all tokens until the blank line.
	selectLexer("skipLexer");
	try {
          while(LA(1) != Token.EOF_TYPE && LA(2) != Token.EOF_TYPE) {
	     if (LA(1) == RETURN && LA(2) == RETURN) break;
	     else consume();
         }
	} catch (ANTLRException e) { /* IGNORE  */ } 
        throw ex;
}
catch [ TokenStreamException ex ] {
	// added v 1.0
	// Enable if debugging is on...
	ex.fillInStackTrace();
	parserMain.dbgprint("TokenStreamException :" + ex.getMessage());
	parserMain.dbgprint(ex);
	// Eat all tokens until the blank line.
	selectLexer("skipLexer");
	try {
	  while(LA(1) != Token.EOF_TYPE && LA(2) != Token.EOF_TYPE) {
	    if (LA(1) == RETURN && LA(2) == RETURN) break;
	    else consume();
          }
	} catch (ANTLRException e) { /* IGNORE  */ }
	throw ex;
}
	



response returns [ SIPResponse m ]
{
        m = new SIPResponse();
        StatusLine s;
	SIPHeader hdr;
	resetLineCounter();
        startTracking();
}
:   s = status_line ( (RETURN)  |
        (hdr = response_body {
          // store a direct ptr in the header for efficient processing later.
	   try {
              if (hdr != null) m.attachHeader(hdr, false,false); 
	   } catch (SIPDuplicateHeaderException ex) {
		ex.fillInStackTrace();
		try {
	            parserMain.handleParseException (ex);
		    // user wants to reject message.
		    if (ex.getHeader() != null) {
			 m.attachHeader(hdr,true,false);
		    }
		} catch (SIPParseException e ) {
			RecognitionException rex = 
			 new RecognitionException(e.getMessage());
			rex.fillInStackTrace();
			m = null;
			throw rex;
		}
	   }
	  })+  RETURN )  {
	  m.statusLine = s;
	  try {
	       // if (parserMain.checkURI) m.checkURI();
		m.checkHeaders();
	  } catch ( SIPParseException ex) {
	     ex.fillInStackTrace();
	     try {
	    	parserMain.handleParseException(ex);
	     } catch ( SIPParseException e ) {
		RecognitionException rex = 
		   new RecognitionException(e.getMessage());
		rex.fillInStackTrace();
		parserMain.setContentLength(0);
		throw rex;
	     }
	 }
          m.setInputText(stopTracking());
	  m.messageContent = null;
	  // Disable line folding support in the preprocessor.
	  parserMain.setHandleContinuations(false);
          // Deal with the message payload.
	  
          if ( m.contentLengthHeader != null ) { 
	    int clength = m.contentLengthHeader.getContentLength();
	    parserMain.setContentLength(clength);
	    if (clength != 0) {
	       if (parserMain instanceof StringMsgParser) {
		  StringMsgParser parser = (StringMsgParser) parserMain;
		  if (parser.isBodyString()) 
			m.messageContent = parser.getMessageBody();
		  else m.messageContentBytes = parser.getBodyAsBytes();
	       } else {
	          // For pipelined parser we store the body as a byte array.
		  PipelinedMsgParser parser = (PipelinedMsgParser) parserMain;
		  m.messageContentBytes = parser.getBodyAsBytes();
	       }
	     } else m.messageContentBytes = null;
         } else if ( parserMain instanceof StringMsgParser)  {
		// For a string parser, we read to the end of the message.
	        // and compute the size of the message accordingly.
		StringMsgParser parser = (StringMsgParser) parserMain;
		if (parser.isBodyString()) 
		  m.messageContent = parser.readToEnd().trim();
		else m.messageContentBytes = parser.readBytesToEnd();
	} else {
		// For a piplelined parser, we set the content to 0 and
		// assume that we have seen a message boundary.
	        m.messageContent = null;
		m.messageContentBytes = null;
	        parserMain.setContentLength(0);
	}

  }
;



request returns [ SIPRequest m ]
{
  m = new SIPRequest ();
  String mtag;
  RequestLine rl = null;
  SIPHeader hdr = null;
  resetLineCounter();
  startTracking();
}
: rl = request_line  
( ( RETURN) |
  ( hdr = request_body { 
	  try {
	     if (hdr != null) m.attachHeader(hdr,false,false);  
	   } catch ( SIPDuplicateHeaderException ex) {
		ex.fillInStackTrace();
		try {
	       		parserMain.handleParseException(ex);
			// User returned null --  discard the duplicate
	     		if (ex.getHeader() != null) {
				 m.attachHeader(hdr,true,false);  
			}
		} catch ( SIPParseException e ) {
			RecognitionException rex = 
			    new RecognitionException(e.getMessage());
			rex.fillInStackTrace();
			throw rex;
		}
	   }
	}
	)+  RETURN  ) {
	m.requestLine = rl;
	// m.setDefaults();
	try {
	        // if (parserMain.checkURI) m.checkURI();
		m.checkHeaders();
	} catch ( SIPParseException ex) {
	     ex.fillInStackTrace();
	     try {
	    	parserMain.handleParseException(ex);
	     } catch ( SIPParseException e ) {
		RecognitionException rex = 
		   new RecognitionException(e.getMessage());
		rex.fillInStackTrace();
		parserMain.setContentLength(0);
		m = null;
		throw rex;
	     }
	 }

        m.setInputText(stopTracking());
	m.messageContent = null;
	parserMain.setHandleContinuations(false);

	// Deal with payload...
        if ( m.contentLengthHeader != null ) { 

	    int clength = m.contentLengthHeader.getContentLength();
	    parserMain.setContentLength(clength);
	    if (clength != 0) {
	       if (parserMain instanceof StringMsgParser) {
		  StringMsgParser parser = (StringMsgParser) parserMain;
		  if (parser.isBodyString()) 
			m.messageContent = parser.getMessageBody();
		  else m.messageContentBytes = parser.getBodyAsBytes();
	       } else {
	          // For pipelined parser we store the body as a byte array.
		  PipelinedMsgParser parser = (PipelinedMsgParser) parserMain;
		  m.messageContentBytes = parser.getBodyAsBytes();
	       }
	     } else m.messageContentBytes = null;

         } else if ( parserMain instanceof StringMsgParser)  {
		// For a string parser, we read to the end of the message.
	        // and compute the size of the message accordingly.
		StringMsgParser parser = (StringMsgParser) parserMain;
		if (parser.isBodyString()) 
		  m.messageContent = parser.readToEnd().trim();
		else m.messageContentBytes = parser.readBytesToEnd();
	} else {
		// For a piplelined parser, we set the content to 0 and
		// assume that we have seen a message boundary.
	        m.messageContent = null;
		m.messageContentBytes = null;
	        parserMain.setContentLength(0);
	}


}
;

// Parser rule for request body.
// Note: I had to add a large number of syntactic predicates because of
// error handling (the last rule is causing the ambiguity).

request_body returns [ SIPHeader rb ] 
{ 
  String msg = null;
  rb = null; 
}
:     
(SUPPORTED_COLON|CALL_INFO_COLON|ACCEPT_ENCODING_COLON|
  ACCEPT_LANGUAGE_COLON|CALL_ID_COLON|MIME_VERSION_COLON|
  CSEQ_COLON|DATE_COLON|ENCRYPTION_COLON|FROM_COLON|
  RECORD_ROUTE_COLON|TIMESTAMP_COLON|TO_COLON|ACCEPT_COLON|
  VIA_COLON|ORGANIZATION_COLON|REQUIRE_COLON|USER_AGENT_COLON|CONTACT_COLON)=>
   rb = general_header |  

(ALERT_INFO_COLON|ALSO_COLON|IN_REPLY_TO_COLON|AUTHORIZATION_COLON|
 HIDE_COLON|MAX_FORWARDS_COLON|PRIORITY_COLON|PROXY_AUTHORIZATION_COLON|
 PROXY_REQUIRE_COLON|ROUTE_COLON|RESPONSE_KEY_COLON|SUBJECT_COLON)=>
   rb = request_header  |  


(CONTENT_DISPOSITION_COLON|CONTENT_LANGUAGE_COLON|
EXPIRES_COLON|ALLOW_COLON|CONTENT_TYPE_COLON|CONTENT_ENCODING_COLON|
CONTENT_LENGTH_COLON)=> 
rb = entity_header  |  

(ERROR_INFO_COLON|PROXY_AUTHENTICATE_COLON|SERVER_COLON|
 UNSUPPORTED_COLON|RETRY_AFTER_COLON|WARNING_COLON|WWW_AUTHENTICATE_COLON)=>
 rb = response_header 
{
	// It may look a bit strange to have response_header as an alternative
	//  here but the idea is to allow the stack to forgive the mistake.
	SIPUnexpectedHeaderException e =
		new SIPUnexpectedHeaderException(rb.getInputText());
	e.setHeaderName(rb.getHeaderName());
	e.setText(rb.getTrimmedInputText());
	e.setErrorObject(rb);
	try {
	   parserMain.handleParseException(e);
	   rb = (SIPHeader) e.getHeader();
	} catch (SIPParseException ex) {
		throw new RecognitionException("Unexpected Header : " +
			rb.getInputText());
	}

}

|  (HEADER_NAME_COLON)=> rb = extension_header 
|  msg = byte_string RETURN 
{
  // Added v1.0
  // No colon or other delimiter found before the EOL cannot be a valid header
  // This is an error condition -- we try to let the application recover
  // by giving him the error text (He always has the option of 
  // re-throwing the exception).
  selectLexer("command_keywordLexer"); 
  SIPInvalidHeaderException e = new SIPInvalidHeaderException(msg);
  e.setHeaderText(msg);
  try {
     parserMain.handleParseException(e);
     rb = (SIPHeader) e.getHeader();
  } catch (SIPParseException ex) {
	throw new RecognitionException("Invalid Header " + msg);
  }
}
;


extension_header returns [ SIPHeader rb ] 
{
	startTracking();
	rb = null;
	String s = null;
}
: t:HEADER_NAME_COLON   s = byte_string RETURN
{
	String inputText = stopTracking();
     	selectLexer("command_keywordLexer"); 
	SIPUnrecognizedExtensionException e = 
		new SIPUnrecognizedExtensionException(t.getText());
   	e.setHeader(null);
        e.fillInStackTrace();
	String hname = t.getText();
        e.setExtensionName(hname.substring(0,hname.lastIndexOf(":")));
	e.setHeaderText(t.getText() + s );
	try {
           parserMain.handleParseException(e);
	   rb =  (SIPHeader) e.getHeader();
	} catch (SIPParseException ex) {
	   throw new RecognitionException("Unrecognized extension " +
			t.getText());
	}
}
;


response_body  returns [ SIPHeader m ]  
{m = null;  String msg = null; } 
: 
(SUPPORTED_COLON|CALL_INFO_COLON|ACCEPT_ENCODING_COLON|
ACCEPT_LANGUAGE_COLON|CALL_ID_COLON|MIME_VERSION_COLON|
CSEQ_COLON|DATE_COLON|ENCRYPTION_COLON|FROM_COLON|RECORD_ROUTE_COLON|
TIMESTAMP_COLON|TO_COLON|ACCEPT_COLON|VIA_COLON|ORGANIZATION_COLON|
REQUIRE_COLON|USER_AGENT_COLON|CONTACT_COLON)=>
m  = general_header | 

(ERROR_INFO_COLON|PROXY_AUTHENTICATE_COLON|
SERVER_COLON|UNSUPPORTED_COLON|RETRY_AFTER_COLON|WARNING_COLON|
WWW_AUTHENTICATE_COLON)=> m  = response_header | 

(CONTENT_DISPOSITION_COLON|CONTENT_LANGUAGE_COLON|
EXPIRES_COLON|ALLOW_COLON|CONTENT_TYPE_COLON|CONTENT_ENCODING_COLON|
CONTENT_LENGTH_COLON)=> m  = entity_header | 


(ALERT_INFO_COLON|ALSO_COLON|IN_REPLY_TO_COLON|AUTHORIZATION_COLON|
HIDE_COLON|MAX_FORWARDS_COLON|PRIORITY_COLON|PROXY_AUTHORIZATION_COLON|
PROXY_REQUIRE_COLON|ROUTE_COLON|RESPONSE_KEY_COLON|SUBJECT_COLON)=>
m  = request_header  
{
	// It may look a bit strange to have request_header here
	// but the idea is to allow the stack to forgive the mistake.
	SIPUnexpectedHeaderException e =
		new SIPUnexpectedHeaderException(m.getInputText());
	e.setHeaderName(m.getHeaderName());
	e.setText(m.getTrimmedInputText());
	e.setErrorObject(m);
	try {
	   parserMain.handleParseException(e);
	   m = (SIPHeader) e.getHeader();
	} catch (SIPParseException ex) {
		throw new RecognitionException("Unexpected Header : " +
			m.getInputText());
	}

}
|  (HEADER_NAME_COLON)=> m = extension_header 
| msg = byte_string RETURN
{
  // Added v1.0
  // No colon or other delimiter found before the EOL cannot be a valid header
  // This is an error condition -- we try to let the application recover
  // by giving him the error text.
  selectLexer("command_keywordLexer"); 
  SIPInvalidHeaderException e = new SIPInvalidHeaderException(msg);
  e.setHeaderText(msg);
  try {
     parserMain.handleParseException(e);
     m = (SIPHeader) e.getHeader();
  } catch (SIPParseException ex) {
	throw new RecognitionException("Invalid Header " + msg);
  }
} 
;


message_header returns  [ SIPHeader m ] 
{
	 m = null;
}
: (  m = content_type  	
|    m = from   		
|    m = contact 	
|    m = via    		
|    m = cseq  
|    m = authorization  
|    m = hide  
|    m = max_forwards
|    m = organization 	
|    m = priority
|    m = proxy_authorization
|    m = proxy_require 
|    m = route 		
|    m = require
|    m = response_key
|    m = subject 
|    m = user_agent
|    m = www_authenticate
|    m = accept 	
|    m = accept_encoding 
|    m = accept_language
|    m = call_id 
|    m = date 
|    m = encryption
|    m = expires 
|    m = record_route 
|    m = timestamp 
|    m = to 	
|    m = content_length 
|    m = proxy_authenticate 
|    m = allow 		
|    m = retry_after 	
|    m = server 
|    m = warning
|    m = unsupported
|    m = alert_info 
|    m = call_info 
|    m = content_language
|    m = in_reply_to
|    m = mime_version
|    m = also
|    m = error_info
|    m = supported
)
;


general_header returns [ SIPHeader r ] 
{
	r = null;
} 
: ( r = accept 		
| r = accept_encoding 
| r = accept_language 
| r = require 
| r = call_id	
| r = contact 
| r = cseq 
| r = date
| r = encryption 
| r = from 
| r = to 
| r = record_route
| r = timestamp 
| r = organization	 
| r = call_info
| r = user_agent
| r = via 	
| r = mime_version
| r = supported
) 
;

entity_header returns [ SIPHeader r ]
{
	r = null;
}
: ( r = content_encoding 	
| r = allow   	
| r = content_length  
| r = expires 	
| r = content_type 
| r = content_language
| r = content_disposition
) 
;

request_header returns [ SIPHeader m ]
{
	m = null;
}
: ( m = authorization  
| m = hide 	
| m  = max_forwards 		
| m = priority 		
| m = proxy_authorization
| m = proxy_require 	
| m = alert_info 
| m = route 	
| m = response_key 
| m = subject 	
| m = in_reply_to
| m = also
) 
;

response_header  returns [ SIPHeader b ] 
{
	b = null;
}
: ( b = proxy_authenticate   
| b = retry_after 	
| b = server  
| b = unsupported 	
| b = warning
| b = www_authenticate
| b = error_info
)
;



//-----------------------------------------------------------------------//
status_line returns [ StatusLine s ] 
{
     s = new StatusLine();
     String v = null;
     int    scode = 0;
     String rp = null ;
     startTracking();
}
: v = sip_version SP
{s.setSipVersion(v); }
scode = status_code SP 
{ s.setStatusCode(scode); }
rp = reason_phrase RETURN
{ 
     s.setReasonPhrase(rp); 
     s.setInputText(stopTracking()); // Gather the match portion.
     selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	 s = (StatusLine) 
	 handleParseException(ex, ILLEGAL_STATUS_LINE_EXCEPTION,
			s, SIPHeaderNames.STATUS_LINE, true);
}
catch [ RecognitionException ex ] {
	 s = (StatusLine) 
	 handleParseException(ex, ILLEGAL_STATUS_LINE_EXCEPTION,
			s, SIPHeaderNames.STATUS_LINE, true);
}


// TODO Need to check for valid status code...
status_code returns [ int sc ]
{
	sc = 0;
}
: d1:DIGIT d2:DIGIT d3:DIGIT
{
	String s = d1.getText() + d2.getText() + d3.getText();
	sc = Integer.parseInt(s);
}
| t:ID
{
	String s = t.getText() ;
	sc = Integer.parseInt(s);
	selectLexer("charLexer");
}
;

reason_phrase returns [ String s ] 
{
	s = "";
	Assert( getCurrentLexerName().compareTo("charLexer") == 0 );
}
: ( d:~(RETURN) { s += d.getText(); } )+
;


//-----------------------------------------------------------------------//
content_disposition returns [ ContentDisposition disp ]
{
	startTracking();
	disp = new ContentDisposition();
	NameValue dp;
	String dt;
	
}
: CONTENT_DISPOSITION_COLON
  dt = disposition_type  { disp.setDispositionType(dt); } (SP|HT)* 
 ( 
   SEMICOLON 
   (SP|HT)*
   dp = disposition_param 
   (SP|HT)* { disp.addDispositionParam(dp); }
  )* 
RETURN
{
	selectLexer("command_keywordLexer");
	disp.setInputText(stopTracking());
}
;
exception 
catch [ RecognitionException ex] {
	disp = (ContentDisposition) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		disp, SIPHeaderNames.CONTENT_DISPOSITION, true);
}
catch [ TokenStreamException ex] {
	disp = (ContentDisposition) 
        handleParseException(ex,HEADER_PARSE_EXCEPTION,
		disp,SIPHeaderNames.CONTENT_DISPOSITION, true);
}

disposition_type returns [ String dt ] 
{
	dt = null;
}
:	RENDER  { dt = ContentDisposition.RENDER; }
|	SESSION { dt = ContentDisposition.SESSION; }
|	ICON	{ dt = ContentDisposition.ICON; }
|	ALERT	{ dt = ContentDisposition.ALERT; }
|	t:ID	{ dt = t.getText(); }
;

disposition_param returns [ NameValue dp ] 
{
	dp = null;
	startTracking();
}
:( HANDLING (SP|HT)* EQUALS (SP|HT)* 
( OPTIONAL 
{ dp = new NameValue( ContentDisposition.HANDLING,
			 ContentDisposition.OPTIONAL) ; 
  dp.setInputText(stopTracking());
} 
| REQUIRED 
{ dp = new NameValue( ContentDisposition.HANDLING,
		 	 ContentDisposition.REQUIRED) ; 
  dp.setInputText(stopTracking());
} 
) )
| dp = generic_param { dp.setInputText(stopTracking()); }
;


	
//-----------------------------------------------------------------------//
supported returns [ SupportedList sl ]
{
	sl = new SupportedList();
	Supported sp = null;
	String ot  = null;
}
:SUPPORTED_COLON ot = option_tag 
	{ sp = new Supported(ot);  sl.add(sp); } (SP|HT)* 
	(
	  COMMA 
	  ( options { greedy = true; } : SP|HT)* 
	  ot = option_tag 
	  { sp = new Supported(ot);  sl.add(sp); } 
	  ( options { greedy = true; } : SP|HT)* 
	)*
RETURN 
;
exception 
catch [ RecognitionException ex ] {
	sl = (SupportedList) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		sl, SIPHeaderNames.SUPPORTED, true);
}
catch [ TokenStreamException ex ] {
	sl = (SupportedList) 
        handleParseException(ex,HEADER_PARSE_EXCEPTION,
		sl,SIPHeaderNames.SUPPORTED, true);
}


//-----------------------------------------------------------------------//

content_language returns [ ContentLanguageList cl ]
{
	startTracking();
	cl = new ContentLanguageList();
	ContentLanguage la = null;
	
}
: CONTENT_LANGUAGE_COLON
   la = language_tag { cl.add(la); } (SP|HT)*
  ( 
	COMMA 
	(options{greedy = true; } : SP|HT)* 
	la = language_tag { cl.add(la); } 
	(SP|HT)*
  )* 
RETURN
{
	selectLexer("command_keywordLexer");
	cl.setInputText(stopTracking());
}
;
exception 
catch [ RecognitionException ex ] {
	cl = (ContentLanguageList) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		cl, SIPHeaderNames.CONTENT_LANGUAGE, true);
}
catch [ TokenStreamException ex ] {
	cl = (ContentLanguageList) 
        handleParseException(ex,HEADER_PARSE_EXCEPTION,
		cl,SIPHeaderNames.CONTENT_LANGUAGE, true);
}

language_tag returns [ ContentLanguage la ]
{
	la = null;
	String s = null;
}
: s = ttoken
{
	la = new ContentLanguage(s);
}
;

//-----------------------------------------------------------------------//
error_info returns [ ErrorInfoList eil ]
{	
	eil = new ErrorInfoList();
	ErrorInfo ei;
	startTracking();
}
:ERROR_INFO_COLON ei = error_infobody {eil.add(ei); } (SP|HT)* 
(COMMA (SP|HT)*  ei = error_infobody {eil.add(ei); } (SP|HT)* )* 
RETURN 
{
	eil.setInputText(stopTracking());
}
;
exception 
catch [ RecognitionException ex ] {
	eil = (ErrorInfoList) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		eil, SIPHeaderNames.ERROR_INFO, true);
}
catch [ TokenStreamException ex ] {
	eil = (ErrorInfoList) 
        handleParseException(ex,HEADER_PARSE_EXCEPTION,
		eil,SIPHeaderNames.ERROR_INFO, true);
}

error_infobody returns [ ErrorInfo ei ]
{
 startTracking();
 ei = new ErrorInfo();
 NameValue gp;
 URI u;
}
:LESS_THAN  u = uri_reference {ei.setURI(u); }
GREATER_THAN { selectLexer("charLexer"); }
(options {greedy = true; }: SP|HT)*  
( 
  SEMICOLON 
  (options{greedy = true; }: SP|HT)* 
  gp = generic_param 
  { ei.addParam(gp); }  
  (options{greedy = true; }: SP|HT)* 
)*

{
	ei.setInputText(stopTracking());
}
;
	

//-----------------------------------------------------------------------//

call_info returns [ CallInfoList ci ]
{
	startTracking();
	ci = new CallInfoList();
	CallInfo cinfo;

}
: CALL_INFO_COLON
cinfo = call_info_body { ci.add(cinfo); } 
(options{greedy = true;}: SP|HT)* 
 ( 
     COMMA 
     (options{greedy = true;}: SP|HT)* 
     cinfo = call_info_body 
     { ci.add(cinfo); } 
     (options{greedy = true; }: SP|HT)* 
 )* 
RETURN
{
	ci.setInputText(stopTracking());
	selectLexer("command_keywordLexer");
}
;
exception 
catch [ RecognitionException ex] {
	ci = (CallInfoList) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		ci,SIPHeaderNames.CALL_INFO, true);
}
catch [ TokenStreamException ex] {
	ci = (CallInfoList) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		ci, SIPHeaderNames.CALL_INFO, true);
}

call_info_body returns [ CallInfo ci ]
{
	ci =  new CallInfo();
	URI u;
	String p;
	NameValue gp;
	startTracking();
}
: LESS_THAN (SP|HT)* 
	u = uri_reference { ci.setUri(u); } (SP|HT)* GREATER_THAN
	{ selectLexer("call_infoLexer", call_infoLexer.PARMS_LHS_STATE ); }
	(options{greedy = true; }: SP|HT)* 
	( 
	(SEMICOLON) => SEMICOLON 
	{ selectLexer("call_infoLexer", call_infoLexer.PARMS_LHS_STATE ); }
	(SP|HT)* PURPOSE (SP|HT)* EQUALS (SP|HT)* p = purpose
	{ ci.setPurpose(p); } 
	) ? 
{
	ci.setInputText(stopTracking());
}
;

purpose returns [ String p ] 
{
	p = null;
}
: ICON { p = CallInfoKeywords.ICON; }
| INFO { p = CallInfoKeywords.INFO; }
| CARD { p = CallInfoKeywords.CARD; }
| t:ID   { p = t.getText(); }
;

//-----------------------------------------------------------------------//
	
	

alert_info returns [ AlertInfoList hl ] 
{
	hl = new AlertInfoList();
	URI u = null;
	NameValue gp = null;
	startTracking();
	AlertInfo ai = null;
}
: ALERT_INFO_COLON
  ( { ai = new AlertInfo(); }  
	LESS_THAN u = uri_reference { ai.setUri(u); } GREATER_THAN 
	( SEMICOLON gp = generic_param {  ai.getParms().add(gp); } )* 
	{ hl.add(ai); } )+ 
( SP|HT )* RETURN
{
	hl.setInputText(stopTracking());
	selectLexer("command_keywordLexer");
}
;
exception 
catch [ RecognitionException ex] {
	hl = (AlertInfoList) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		hl, SIPHeaderNames.ALERT_INFO, true);
}
catch [ TokenStreamException ex] {
	hl = (AlertInfoList) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		hl,SIPHeaderNames.ALERT_INFO, true);
}

//-----------------------------------------------------------------------//

// This is from the HTTP RFC 2616
accept_encoding returns [ AcceptEncodingList clist ] 
{
	clist = new AcceptEncodingList ();
	startTracking();
	AcceptEncoding c;
	
} 
:ACCEPT_ENCODING_COLON
    c = content_preference 
    { clist.add(c); } 
    (options{greedy=true;}: SP|HT)*
    (COMMA 
      (options{greedy = true;}: SP|HT)*  
       c = content_preference { clist.add(c); } 
       (options{greedy=true;}: SP|HT)*
     )*
RETURN 
{ 
	clist.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
     }
;
exception 
catch [ RecognitionException ex] {
	clist = (AcceptEncodingList) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
	   	clist,SIPHeaderNames.ACCEPT_ENCODING, true);
}
catch [ TokenStreamException ex] {
	clist = (AcceptEncodingList) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		clist,SIPHeaderNames.ACCEPT_ENCODING,true);
}

content_preference returns [ AcceptEncoding cc ]
{
	cc = new AcceptEncoding();
	String c;
	double qv = 1.0;
	startTracking();
}
: c = content_coding
{ cc.setContentCoding(c); } (options{greedy = true; }: SP|HT)*
( (SEMICOLON)=>  
	SEMICOLON { selectLexer("accept_languageLexer"); }  (SP|HT)*
        Q (SP|HT)* EQUALS (SP|HT)*  qv = qvalue  
 { 
   cc.setQvalue(qv); 
   selectLexer("charLexer"); 
 } )? 
{ cc.setInputText(stopTracking()); }
;

content_coding returns [ String c ] 
: c = ttoken 
;


//-----------------------------------------------------------------------//
// This is from the HTTP rfc 2616

accept_language returns [ AcceptLanguageList a ] 
{
	a = new AcceptLanguageList();
	String s;
	double qv;
	AcceptLanguage al;
	startTracking();
	
}
: ACCEPT_LANGUAGE_COLON
   al =  accept_language_body (SP|HT)* 
  { a.add(al); }
  ( COMMA 
     (options{greedy = true;}: SP|HT)* 
      al = accept_language_body 
      { a.add(al); } 
     (options{greedy = true;}: SP|HT)* 
   )*
RETURN
{ 
  a.setInputText(stopTracking());
  selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	a = (AcceptLanguageList) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		a,SIPHeaderNames.ACCEPT_LANGUAGE, true);
}
catch [ TokenStreamException ex] {
	a = (AcceptLanguageList) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		a, SIPHeaderNames.ACCEPT_LANGUAGE, true);
}

accept_language_body returns [ AcceptLanguage a ]
{
	a = new AcceptLanguage();
	String s  = null;
	startTracking();
	double qv;
}
: s = language_range { a.setLanguageRange(s); } 
   ( (SEMICOLON)=> SEMICOLON 
     { selectLexer("accept_languageLexer"); }
        Q 
	(SP|HT)*  
	EQUALS 
	(SP|HT)* 
	qv = qvalue 
	{ a.setQValue(qv); } 
   )?  
{
	a.setInputText(stopTracking());
}
;

language_range returns [ String s ]
{
	s = "";
}
:
	( d1:ALPHA { s += d1.getText(); } )+ 
	( d2:MINUS { s += d2.getText(); } 
	( d3:ALPHA { s += d3.getText(); } )+ )* | STAR  { s = "*"; }
;


qvalue returns [ double f ] :
	f = fpnum
;

//-----------------------------------------------------------------------//

call_id returns [ CallID c ] 
{
	c = new CallID();
	String h = null;
	String s;
	CallIdentifier cid = null;
	startTracking();
}
: CALL_ID_COLON
	cid = call_identifier
(SP|HT)* RETURN
{ 
	c.setCallIdentifier(cid);
	c.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	c = (CallID)
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		c, SIPHeaderNames.CALL_ID, true);
}
catch [ TokenStreamException ex] {
	c = (CallID)
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		c, SIPHeaderNames.CALL_ID, true);
}


call_identifier returns [ CallIdentifier cid ] 
{
	startTracking();
	 cid = new CallIdentifier();
	String s = null;
	String h = null;

}
: s = ttoken ((AT)=> AT h = ttoken )?  
{

	cid.setLocalId(s);
	if (h != null) cid.setHost(h);
	cid.setInputText(stopTracking());
}
;

//-----------------------------------------------------------------------//
// This is a tentative addition in the BIS 02 spec
also returns  [ AlsoList a ]
{
	a = new AlsoList();
	Address addr;
	startTracking();
}
: ALSO_COLON addr = address { a.add( new Also(addr)); } 
	(SP|HT)* 
	(
	  COMMA (options{ greedy = true; } : SP|HT)* addr = address 
		{ a.add(new Also(addr)); } 
	  (SP|HT)*
	)*
RETURN
{
	a.setInputText(stopTracking());
	selectLexer("command_keywordLexer");
}
;
exception 
catch [ RecognitionException ex] {
	a  = (AlsoList )
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		a, SIPHeaderNames.ALSO, true);
}
catch [ TokenStreamException ex] {
	a  = (AlsoList)
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		a, SIPHeaderNames.ALSO, true);
}

//-----------------------------------------------------------------------//

in_reply_to returns [ InReplyToList inreply ] 
{

	inreply = new InReplyToList();
	CallIdentifier cid;
	startTracking();
	
}
: IN_REPLY_TO_COLON cid = call_identifier { inreply.add(cid); } (SP|HT)* 
	( 
	    COMMA (options { greedy = true; }: SP|HT)* 
	    cid = call_identifier (SP|HT)* 
	    { inreply.add(new InReplyTo(cid)); }  
	)* 
RETURN
{
	inreply.setInputText(stopTracking()); 
    	selectLexer("command_keywordLexer"); 

}
;
exception 
catch [ RecognitionException ex] {
	inreply = (InReplyToList )
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		inreply, SIPHeaderNames.IN_REPLY_TO, true);
}
catch [ TokenStreamException ex] {
	inreply = (InReplyToList)
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		inreply, SIPHeaderNames.IN_REPLY_TO, true);
}

//-----------------------------------------------------------------------//

mime_version returns [ MimeVersion mv ] 
{
	mv = new MimeVersion();
	int major;
	int minor;
	startTracking();
}
: MIME_VERSION_COLON major = intnumber DOT minor = intnumber 
(SP|HT)* RETURN
{
	mv.setMajorNumber(major);
	mv.setMinorNumber(minor);
	mv.setInputText(stopTracking());
    	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	mv  = (MimeVersion )
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		mv, SIPHeaderNames.MIME_VERSION, true);
}
catch [ TokenStreamException ex] {
	mv = (MimeVersion)
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		mv, SIPHeaderNames.MIME_VERSION, true);
}


//-----------------------------------------------------------------------//

cseq returns [ CSeq cs ]
{
	long sno = 0;
	String s = "";
	String m;
        cs = new CSeq();
	startTracking();
}
: CSEQ_COLON
  (d:DIGIT { s += d.getText(); } )+  
  { selectLexer("method_keywordLexer"); } 
  (SP|HT)+ m = method (SP|HT)* RETURN
{ 
    sno = Long.parseLong(s);
    cs.setMethod(m);
    cs.setSeqno(sno);
    cs.setInputText(stopTracking());
    selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	cs = (CSeq)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		cs,SIPHeaderNames.CSEQ, true);
}
catch [ TokenStreamException ex] {
	cs = (CSeq)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		cs,SIPHeaderNames.CSEQ, true);
}

//-----------------------------------------------------------------------//

date returns [ SIPDateHeader dt ] 
{
	dt = new SIPDateHeader();
	SIPDate d;
	startTracking();
}
: DATE_COLON
   d = sip_date (SP|HT)* 
  RETURN
{ 
    dt.setDate(d);
    dt.setInputText(stopTracking());
    selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	dt = (SIPDateHeader )
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		dt,SIPHeaderNames.DATE, true);
}
catch [ TokenStreamException ex] {
	dt = (SIPDateHeader )
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		dt,SIPHeaderNames.DATE, true);
}


//-----------------------------------------------------------------------//

encryption returns [ Encryption e ]
{
	e = new Encryption();
	String s;
	NameValue p;
	startTracking();

}
: ENCRYPTION_COLON
s = encryption_scheme (SP|HT)+  
	{ e.setEncryptionScheme(s); }
 	p = encryption_params (SP|HT)*
	{e.getEncryptionParms().add(p); }
       (  COMMA (SP|HT)* p = encryption_params  (SP|HT)*
	  { e.getEncryptionParms().add(p); } )*   
RETURN
{ 
	e.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	e = (Encryption)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		e, SIPHeaderNames.ENCRYPTION, true);
}
catch [ TokenStreamException ex] {
	e = (Encryption)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		e,SIPHeaderNames.ENCRYPTION,true);
}

encryption_scheme returns [ String s ] :
	s = ttoken
;

encryption_params returns [ NameValue nv ]
{
	nv = new NameValue();
	String n;
	String v;
	startTracking();
}
:    n = ttoken  (SP|HT)* EQUALS  (SP|HT)* v = quoted_string_or_ttoken
{
	nv.setName(n);
	nv.setValue(v);
	nv.setInputText(stopTracking());
}
;


quoted_string_or_ttoken returns [ String s ]:
	s = quoted_string | s = ttoken
;


//-----------------------------------------------------------------------//

expires returns [ Expires exp ]
{
	exp = new Expires();
	SIPDateOrDeltaSeconds e; 
	startTracking();
}
: EXPIRES_COLON
   	e = date_or_delta_seconds   (SP|HT)*  
RETURN
{ 
    exp.setExpiryTime(e);
    exp.setInputText(stopTracking()); 
    selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	exp = (Expires)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		exp,SIPHeaderNames.EXPIRES, true);
}
catch [ TokenStreamException ex] {
	exp = (Expires)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		exp,SIPHeaderNames.EXPIRES, true);
}

//-----------------------------------------------------------------------//

from returns [ From t ]
{
	t = new From();
	Address a = null;
	startTracking();
	NameValue ae;
	String tag;
	
}
:  FROM_COLON 
    a  = address   { selectLexer("addr_parmsLexer"); } 
    (SP|HT)* ( SEMICOLON (SP|HT)*  
	(  (TAG) =>  tag = tag_param { t.setTag(tag); }  |
	  ae = generic_param { t.setParameter(ae); } ) (SP|HT)* )*
    RETURN
{ 
  // If this is an address spec then the parameters belong to me (and not
  // to the url.
   t.setAddress(a);
   //
   // The Contact, From and To header fields contain a URL. If the URL
   // contains a comma, question mark or semicolon, the URL MUST be
   // enclosed in angle brackets (< and >). Any URL parameters are
   // contained within these brackets. If the URL is not enclosed in angle
   // brackets, any semicolon-delimited parameters are header-parameters,
   // not URL parameters.
   //
   if (a.getAddressType() == Address.ADDRESS_SPEC) {
	t.getParms().concatenate(a.getAddrSpec().getUriParms());
	a.getAddrSpec().clearUriParms();
   }
   // The SIP-URL MUST NOT contain the "transport-param", "maddr-param",
   // "ttl-param", or "headers" elements. A server that receives a SIP-URL
   // with these elements ignores them.
   if (parserMain.strict) {
      try {
         CheckConstraints.check(t);
      } catch ( IllegalArgumentException ex) {
	throw new RecognitionException(ex.getMessage());
      }
   } else {
      a.removeParameter(SIPKeywords.TTL);
      a.removeParameter(SIPKeywords.TRANSPORT);
      a.removeParameter(SIPKeywords.MADDR);
   }
   t.setInputText(stopTracking());
   selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	t = (From)
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		t, SIPHeaderNames.FROM, true);
}
catch [ TokenStreamException ex] {
	t = (From)
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		t,SIPHeaderNames.FROM, true);
}



extension_params returns [ NameValue nv ] 
{
	nv = new NameValue();
	String n;
	String v = null;
	startTracking();
}
:  t:ID ((SP|HT)* EQUALS { selectLexer("charLexer"); } 
	 (SP|HT)* v = extension_value )?
{
	nv.setName(t.getText());
	nv.setValue(v);
	nv.setInputText(stopTracking());
}
;


extension_value returns [ String v ]:
	v = ttoken
;

//-----------------------------------------------------------------------//

record_route returns [ RecordRouteList al ] 
{
	Address a = null;
	al = new RecordRouteList();
	startTracking();
	NameValue nv = null;
	RecordRoute rr = null;
}
: RECORD_ROUTE_COLON
	a = name_addr (SP|HT)*  
	{
		rr = new RecordRoute(a);
		al.add(rr);  
	}
	(
	  SEMICOLON (SP|HT)* nv = generic_param (SP|HT)* 
	  { 
		rr.addParam(nv);
	  } 
	)*
	(  COMMA ( options { greedy = true; } : SP|HT)* a = name_addr (SP|HT)* 
	   {
	     rr = new RecordRoute(a); 
	     al.add(rr) ; 
	   }
	  (  
            SEMICOLON (SP|HT)* nv = generic_param (SP|HT)* 
	     { 
	       rr.addParam(nv);
	       nv = null;
	     }  
	  )*
	)* 
RETURN
{ 
       al.setInputText(stopTracking());
       selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	al = (RecordRouteList)
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		al,SIPHeaderNames.RECORD_ROUTE, true);
}
catch [ TokenStreamException ex] {
	al = (RecordRouteList)
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		al,SIPHeaderNames.RECORD_ROUTE, true);
}

//-----------------------------------------------------------------------//

timestamp returns [ TimeStamp ts ] 
{
	double f = 0;
	ts = new TimeStamp();
	startTracking();

}
: TIMESTAMP_COLON
	{selectLexer ("charLexer"); }
	f = delay
(SP|HT)* RETURN
{ 
	float fdelay = new Double(f).floatValue();
	ts.setTimeStamp(fdelay);
	ts.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	ts = (TimeStamp)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		ts,SIPHeaderNames.TIMESTAMP, true);
}
catch [ TokenStreamException ex] {
	ts = (TimeStamp)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		ts,SIPHeaderNames.TIMESTAMP, true);
}

delay returns [ double f ] :
	f = fpnum
;
	

//-----------------------------------------------------------------------//

to returns [ To t ]
{
	t = new To();
	Address a = null;
	startTracking();
	NameValue ae;
	String tag;
	boolean semicolonSeen = false;
	
}
:  TO_COLON
    a  = address   { selectLexer("addr_parmsLexer"); }
    (SP|HT)* ( SEMICOLON (SP|HT)*  
	(  (TAG) =>  tag = tag_param { t.setTag(tag); }  |
	  ae = generic_param  {t.setParameter(ae);} ) (SP|HT)* )*
    RETURN
{ 
   t.setAddress(a);

   // Thus spake RFC2543 bis 04:
   // The Contact, From and To header fields contain a URL. If the URL
   // contains a comma, question mark or semicolon, the URL MUST be
   // enclosed in angle brackets (< and >). Any URL parameters are
   // contained within these brackets. If the URL is not enclosed in angle
   // brackets, any semicolon-delimited parameters are header-parameters,
   // not URL parameters.

   if (a.getAddressType() == Address.ADDRESS_SPEC) {
	t.getParms().concatenate(a.getAddrSpec().getUriParms());
	a.getAddrSpec().clearUriParms();
   }

   // Thus spake RFC2543 bis 04:
   // The SIP-URL MUST NOT contain the "transport-param", "maddr-param",
   // "ttl-param", or "headers" elements. A server that receives a SIP-URL
   // with these elements ignores them.

   if (parserMain.strict) {
     try {
        CheckConstraints.check(t);
      } catch ( IllegalArgumentException ex) {
	throw new RecognitionException(ex.getMessage());
      }
   } else {
     a.removeParameter(SIPKeywords.TTL);
     a.removeParameter(SIPKeywords.TRANSPORT);
     a.removeParameter(SIPKeywords.MADDR);
   }
   t.setInputText(stopTracking());
   selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	t = (To)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		t, SIPHeaderNames.TO, true);
}
catch [ TokenStreamException ex] {
	t = (To)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		t,SIPHeaderNames.TO, true);
}


addr_extension returns [ NameValue ae ] 
: ae = generic_param
;

tag_param returns [ String  s ] 
:  TAG { selectLexer("charLexer"); } 
	  (SP|HT)* EQUALS (SP|HT)* s = ttoken 
;


//-----------------------------------------------------------------------//

accept returns [ AcceptList al ] 
{
	al = new AcceptList();
	Accept a;
	startTracking();
}
: ACCEPT_COLON
      { selectLexer("charLexer"); }
      a =  accept_args {al.add(a); } (SP|HT)*
      ( COMMA (SP|HT)* a = accept_args { al.add(a); } (SP|HT)* )*  
 RETURN
    { 
	al.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
     }
;
exception 
catch [ RecognitionException ex] {
	al = (AcceptList)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		al,SIPHeaderNames.ACCEPT, true);
}
catch [ TokenStreamException ex] {
	al = (AcceptList)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		al,SIPHeaderNames.ACCEPT, true);
}

accept_args returns [ Accept a ] 
{
	a = new Accept();
	MediaRange m ;
	AcceptParams p = null;
	startTracking();
}
:
	  m = media_range  ( (SEMICOLON) => SEMICOLON 
		{ selectLexer("accept_languageLexer"); } p =  accept_params )?
{
	a.setMediaRange(m);
	a.setAcceptParams(p);
	a.setInputText(stopTracking());
}
;


media_range returns [ MediaRange r ] 
{
	r = new MediaRange();
	NameValue nv;
	String t;
	String s;
	startTracking();
}
:    t = type   SLASH  s = subtype  
     (options{greedy = true;}: SP|HT)*
     ( options{greedy = true;}:  
        SEMICOLON 
        (options{greedy = true;}: SP|HT)*  nv = parameter 
	{ r.getParameters().add(nv); } 
       (options{greedy = true;}: SP|HT)*   
     )*
{ 
    r.setType(t); 
    r.setSubtype(s);  
    r.setInputText(stopTracking()); 
    if (r.getType().compareTo("*") == 0  && 
		r.getSubtype().compareTo("*")  != 0) {
	throw new RecognitionException ("bad media range spec");
    }
    if (r.getSubtype().compareTo("*") == 0 &&  r.getParameters().isEmpty()) {
	throw new RecognitionException ("bad media range spec");
    }
}
;


type returns [ String t ]:
	t = ttoken
;


parameter returns [ NameValue nv ] 
{
	nv = new NameValue();
	startTracking();
	String n;
	String v;
	
}
: n = attribute (SP|HT)*  EQUALS (SP|HT)*  v = value 
{
	nv.setName(n);
	nv.setValue(v);
	nv.setInputText(stopTracking());

}
;


accept_params returns [AcceptParams p ] 
{
	p = new AcceptParams();
	double qv;
	NameValue ex = null;
	startTracking();
}
: 	 Q EQUALS qv = qvalue 
	 (  SEMICOLON ex = accept_extension 
			{p.addExtension(ex); } )*
{
	p.setQValue(qv);
	p.setInputText(stopTracking());
}
;

accept_extension returns [ NameValue ex ] 
{
	String n = null;
	String v = null;
	ex = null;
	startTracking();

}
:   n = ttoken 
	(options{greedy = true;}: SP|HT)* 
	( (EQUALS)=>  EQUALS (SP|HT)*  v = quoted_string_or_ttoken )?
{
	ex = new NameValue();
	ex.setName(n);
	ex.setValue(v);
	ex.setInputText(stopTracking());
}
;


//-----------------------------------------------------------------------//


via returns [ ViaList vl ]
{

	vl = new ViaList();
	Via v = null;
	startTracking();
}
:       VIA_COLON
	v = via_body { vl.add(v); } 
	(options{greedy  = true;}: SP|HT)*  
        (  
	  COMMA 
	  (options{greedy = true;}: SP|HT)* 
	  v = via_body { vl.add(v); } 
	  (options{greedy = true;}: SP|HT)* 
	 )*
	RETURN
{
	vl.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	vl = (ViaList)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		vl,SIPHeaderNames.VIA, true);
}
catch [ TokenStreamException ex] {
	vl = (ViaList)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		vl,SIPHeaderNames.VIA, true);
}

via_body returns [ Via v ] 
{
	v = new Via();
	Protocol p;
	NameValue  vp;
	String comm;
	HostPort s ;
	startTracking();
}
: p = sent_protocol 
	(SP|HT)+  
	s = sent_by 
	(options{greedy = true;}: SP|HT)*
	 (  SEMICOLON 
	     { selectLexer("via_parmsLexer"); parserMain.initLexer(); }
             (options{greedy = true; }: SP|HT)* 
	     vp = via_params  
	     {v.getViaParms().add(vp); } 
	     (options{greedy = true;}: SP|HT)*  
	  )* 
        ( (LPAREN)=> comm = comment { v.setComment(comm); } 
	  (options{greedy = true;}: SP|HT)* )?   
{ 
	v.setSentProtocol(p);
	v.setSentBy(s);
	v.setInputText(stopTracking());
}
;

sent_protocol returns [ Protocol p ]
{
	String n;
	String v;
	String t;
	p  = new Protocol();
	startTracking();
}
:  n = protocol_name (SP|HT)* SLASH (SP|HT)* 
	v = protocol_version  (SP|HT)* SLASH (SP|HT)* t = transport
{
	p.setProtocolName(n);
	p.setProtocolVersion(v);
	p.setTransport(t);
	p.setInputText(stopTracking());
}
;

protocol_name returns [ String s ] : 
	s = ttoken 
	{ /* TODO "SIP" is a special case - need to handle in code */ }
;

protocol_version returns [ String s ]:
	s = ttoken
;

transport returns [ String s ]:
	  s = ttoken 
 { 
   if (s.compareToIgnoreCase(SIPKeywords.TCP) != 0 
	&& s.compareToIgnoreCase(SIPKeywords.UDP) != 0 ) {
	throw new RecognitionException("Invalid Transport String " + s);
  }
}
;


sent_by returns [ HostPort h ] 
{
	h = null;
}
:    h = host_port   
;



via_params returns [ NameValue v ] 
: v = via_hidden | v= via_ttl | v = via_maddr | v =via_received |
                        v = via_branch
;

via_hidden returns [ NameValue v ]
{
	v = null;
	startTracking();
}
:HIDDEN
{
	v = new NameValue(ViaKeywords.HIDDEN, null);
	v.setInputText(stopTracking());
}
;

via_ttl returns [ NameValue v ]
{
	int t;
	v = null;
	startTracking();
}
: TTL (SP|HT)* EQUALS (SP|HT)*   t = ttl_val
{
	v = new NameValue(ViaKeywords.TTL, new Integer(t));
	v.setInputText(stopTracking());
}
;

ttl_val returns [ int ttl ] 
{
   ttl = 0;
   String result = "";
}
: ( d1:DIGIT { result += d1.getText(); } )+
{
	ttl = Integer.parseInt(result);
}
;


via_maddr  returns [ NameValue v ]
{
	v =  null;
	Host h;
	startTracking();
}
: MADDR (SP|HT)* EQUALS (SP|HT)*  h = host
{
	v = new NameValue(ViaKeywords.MADDR,h);
	v.setInputText(stopTracking());
}
;


via_received returns [ NameValue v ] 
{
	v = null;
	String s;
	startTracking();
}
: RECEIVED (SP|HT)* EQUALS (SP|HT)*  s = ttoken
{
	v  = new NameValue(ViaKeywords.RECEIVED, s);
	v.setInputText(stopTracking());
}
;

via_branch returns [ NameValue v ]
{
	v = null;
	String s;
	startTracking();
}
: BRANCH (SP|HT)* EQUALS (SP|HT)*  s = ttoken
{
	v  = new NameValue(ViaKeywords.BRANCH,s);
	v.setInputText(stopTracking());
}
;


//-------------------------------------------------------------------------//



allow returns [ AllowList m ] 
{
	m = new AllowList();
	String meth;
	startTracking();
}
:ALLOW_COLON
	 meth = method  
	 {
		m.add(new  Allow(meth));
	 } 
	(SP|HT)*
	( 
	 COMMA (SP|HT)*  meth = method 
	  { 
		m.add(new Allow(meth)); 
	  } (SP|HT)*  
	)* 
	RETURN
	{ 
	  m.setInputText(stopTracking());
	  selectLexer("command_keywordLexer"); 
	}
;
exception 
catch [ RecognitionException ex] {
	m = (AllowList)
	handleParseException(ex, HEADER_PARSE_EXCEPTION,
				m,SIPHeaderNames.ALLOW, true);
}
catch [ TokenStreamException ex] {
	m = (AllowList)
	handleParseException(ex, HEADER_PARSE_EXCEPTION,
				m,SIPHeaderNames.ALLOW, true);
}

//-----------------------------------------------------------------------//
authorization returns [ Authorization auth ]  
{
	auth = null;
	startTracking();
} 
: AUTHORIZATION_COLON
	 auth = auth_body
	(SP|HT)* RETURN
 { 
    auth.setInputText(stopTracking());
    selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	auth = (Authorization)
	handleParseException(ex, HEADER_PARSE_EXCEPTION,
			auth,SIPHeaderNames.AUTHORIZATION, true);
}
catch [ TokenStreamException ex] {
	auth = (Authorization)
	handleParseException(ex, HEADER_PARSE_EXCEPTION,
			auth,SIPHeaderNames.AUTHORIZATION, true);
}


auth_body returns [ Authorization auth ] 
{
	String cookie;
	auth  = new Authorization();
	startTracking();
	Credentials cr;
	NameValue nv;
}
: ( (BASIC)=> BASIC (SP|HT)+ cookie = basic_cookie 
{ 
	auth.setScheme(AuthorizationKeywords.BASIC);
	nv = new NameValue();
	nv.setValue(cookie);
	nv.setInputText(cookie);
	auth.getCredentials().add(nv);

}
| (PGP)=> PGP { auth.setScheme(AuthorizationKeywords.PGP); }
	 (SP|HT)+ 
	 nv = pgp_response { auth.addParam(nv); } 
	 (options{greedy = true;}: SP|HT)*
         (
 	   COMMA 
	   (options{greedy = true;}: SP|HT)* 
           nv = pgp_response { auth.addParam(nv);} 
           (options{greedy = true;}: SP|HT)* 
         )*
| (DIGEST)=> DIGEST 
	{ auth.setScheme(AuthorizationKeywords.DIGEST); } 
        (SP|HT)+ 
        nv = digest_response { auth.addParam(nv); } 
        (options{greedy = true;}:SP|HT)*
        (
          COMMA 
	  (options{greedy = true;}: SP|HT)* 
           nv = digest_response {auth.addParam(nv);} 
	  (options{greedy = true;}: SP|HT)*
         )*
| cr = credentials  
{ auth.setScheme(cr.getScheme()); auth.setCredentials(cr.getCredentials()); } )
{ 
	auth.setInputText(stopTracking()); 
}
;

digest_response returns [ NameValue nv ]
{ 
  nv = null; 
  parserMain.setEnclosingLexer("digest_Lexer"); 
}
: nv = username
| nv = digest_uri
| nv = qop_value
| nv = stale
| nv = algorithm
| nv = domain
| nv = dresponse
| nv = noncecount
| nv = nonce
| nv = cnonce
| nv = realm
| nv = opaque
;


cnonce returns [ NameValue nv ]
{ String u = null;  nv = null; }
:CNONCE (SP|HT)* EQUALS (SP|HT)*  u = ttoken
{ nv = new NameValue(AuthorizationKeywords.CNONCE,u); }
;

username returns [ NameValue nv ]
{ String u = null;  nv = null; }
:USERNAME (SP|HT)* EQUALS (SP|HT)*  u = quoted_string
{ nv = new NameValue(AuthorizationKeywords.USERNAME,u); }
;

digest_uri returns [ NameValue nv ]
{ nv = null;  String u; }
:URI (SP|HT)* EQUALS (SP|HT)*  u = quoted_string
{ nv = new NameValue(AuthorizationKeywords.URI,u); }
;

dresponse returns [ NameValue nv ]
{ nv = null;  String u; }
:RESPONSE (SP|HT)* EQUALS (SP|HT)* u = quoted_string
{ nv = new NameValue(AuthorizationKeywords.RESPONSE,u); }
;

noncecount returns [ NameValue nv ]
{ nv = null;  String u;  String nc = ""; }
:NC (SP|HT)* EQUALS (SP|HT)*  (u = hexdigit { nc += u; } )+
{ nv = new NameValue(AuthorizationKeywords.NC,nc);  }
;


pgp_response returns  [ NameValue nv ]
{ nv = null;  startTracking(); }
: ( nv = realm
| nv = pgp_version
| nv = pgp_signature
| nv = signed_by
| nv = nonce )
{ nv.setInputText(stopTracking()); }

;


signed_by returns [ NameValue nv ]
{
	String u;
	startTracking();
	nv = null;
}
:SIGNED_BY (SP|HT)* EQUALS  (SP|HT)*  u = quoted_string
{
	nv = new NameValue(AuthorizationKeywords.SIGNED_BY,u);
	nv.setInputText(stopTracking());
}
;

pgp_signature returns [ NameValue nv ]
{
	nv  = new NameValue();
	String s = null;
}
:SIGNATURE (SP|HT)* EQUALS (SP|HT)*  s = quoted_string 
{
	nv.setName(AuthorizationKeywords.SIGNATURE); 
	nv.setValue(s);
}
;

credentials returns [ Credentials creds ] 
{
	creds = new Credentials();
	String s ;
	NameValue nv;
	startTracking();
}
:t:ID 
( options{generateAmbigWarnings = false; }:
  (options{greedy = true;}:SP|HT)+ 
  nv = auth_params 
  (options{greedy = true;}:  
    COMMA 
    (options{greedy = true;}: SP|HT)* 
     nv =   auth_params 
    (options{greedy = true;}: SP|HT)*
    { creds.getCredentials().add(nv);} 
  )*  
)?
{
     creds.setScheme(t.getText());
     creds.setInputText(stopTracking());
}
;


basic_cookie returns [ String s ]
{ s = null; }
: s = base64string
;


auth_params returns [ NameValue nv ]
: nv = generic_param
;

string returns [ String s ]:
	s = quoted_string_or_ttoken
;



//--------------------------------------------------------------------//
// Revised v1.0

proxy_authenticate returns [ ProxyAuthenticateList cl ]
{
	cl = new ProxyAuthenticateList();
	Challenge c;
	startTracking();
}
:PROXY_AUTHENTICATE_COLON
( PGP  (SP|HT)+  c = pgp_challenge { cl.add(new ProxyAuthenticate(c));  }
| BASIC  (SP|HT)+ c = basic_challenge { cl.add(new ProxyAuthenticate(c)); }
| DIGEST  (SP|HT)+ c = digest_challenge { cl.add(new ProxyAuthenticate(c)); }
| t:ID (SP|HT)+ c = challenge 
	{ c.setScheme(t.getText()); cl.add (new ProxyAuthenticate(c)); } )
(SP|HT)* RETURN
{ 
  cl.setInputText(stopTracking());
  selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	cl = (ProxyAuthenticateList)
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		cl,SIPHeaderNames.PROXY_AUTHENTICATE,true);
}
catch [ TokenStreamException ex] {
	cl = (ProxyAuthenticateList)
		handleParseException(ex,HEADER_PARSE_EXCEPTION,
		cl,SIPHeaderNames.PROXY_AUTHENTICATE,true);
}
//-------------------------------------------------------------------------//
// Revised v1.0
// Revised again ... Can only be a single server header in a message!
//

server returns [ Server s ]
{
     s = null;
     startTracking();
}
: SERVER_COLON (options { greedy = true; }: SP|HT)*  s = server_body  (SP|HT)*
RETURN 
{ 
    s.setInputText(stopTracking());
    selectLexer("command_keywordLexer"); 
}
;
exception catch [ RecognitionException ex] {
	s = (Server)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		s,SIPHeaderNames.SERVER, true);
}
catch [ TokenStreamException ex] {
	s = (Server)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		s,SIPHeaderNames.SERVER, true);
}


server_body returns [ Server s ] 
{
    startTracking();
     Product p;
     String com;
     s = null;
}
: (LPAREN)=> com = comment { 
    s = new Server(); 
    s.setComment(com);
    s.setInputText(stopTracking());
} 
| p = product { 
    s = new Server();
    s.setProduct(p);
    s.setInputText(stopTracking());
}
;

product returns [ Product p ] 
{
	p = new Product();
	String pt = null;
}
: pt = product_token  
{ p.setProductToken(pt); } 
;

product_token returns [ String s ]
: s = byte_string_no_comma
;

/**
* Product token strings seem to be free form strings in many implementations.
* This is not quite what the RFC says but it is not a core header so we
* let it pass.
*
* 
*  product_token returns [ ProductToken p ]
* {
*	p = new ProductToken();
*	String  name;
*	String  version = null;
*	startTracking();
* }
* : name = ttoken_allow_space 
*	( (SLASH)=> SLASH (SP|HT)* version = product_version )?
* {
* 	p.setName(name);
* 	p.setVersion(version);
*	p.setInputText(stopTracking());
* }
* ;
*
* product_version returns [ String v ] 
* : 	v = ttoken
* ;
*
**/

//------------------------------------------------------------------//
option_tag returns [ String op ]
{
	op = null;
}
:
     op = ttoken
;

unsupported returns [ UnsupportedList ot ] 
{
	String opt = null;
	ot = new UnsupportedList();
	startTracking();
}
: 	UNSUPPORTED_COLON
	opt = option_tag  { ot.add(new Unsupported(opt)); }
        (SP|HT)* (  COMMA (SP|HT)* opt = option_tag 
         { ot.add( new Unsupported(opt)); } (SP|HT)* )*  
	RETURN 
{ 
  ot.setInputText(stopTracking());
  selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	ot = (UnsupportedList)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		ot,SIPHeaderNames.UNSUPPORTED, true);
}
catch [ TokenStreamException ex] {
	ot = (UnsupportedList)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		ot,SIPHeaderNames.UNSUPPORTED, true);
}

//-------------------------------------------------------------------//

retry_after returns [ RetryAfter r ] 
{
	r = new RetryAfter();
	SIPDateOrDeltaSeconds d ;
	String c = null;
	DeltaSeconds  du = null;
	startTracking();
}
: RETRY_AFTER_COLON
	d = date_or_delta_seconds 
	(options{greedy=true;}: SP|HT)*
	{ r.setExpiryDate(d); }
	((LPAREN) => c = comment { r.setComment(c); } )? 
	(options{greedy=true;}: SP|HT)*
	( (SEMICOLON)=> 
	  SEMICOLON  
	  {  selectLexer("retry_afterLexer"); } 
  	  DURATION 
	  (SP|HT)* 
	  EQUALS 
	  (SP|HT)* du =  delta_seconds 
	)?
 (SP|HT)* RETURN 
{ 
	r.setDuration(du);
	r.setInputText(stopTracking());
   	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ RecognitionException ex] {
	r = (RetryAfter)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		r,SIPHeaderNames.RETRY_AFTER, true);
}
catch [ TokenStreamException ex] {
	r = (RetryAfter)
	handleParseException(ex,HEADER_PARSE_EXCEPTION,
		r,SIPHeaderNames.RETRY_AFTER, true);
}

//---------------------------------------------------------------------//


date_or_delta_seconds returns [ SIPDateOrDeltaSeconds  e ]
: (   e = sip_date   | e = delta_seconds  )
;

date_or_delta_seconds1 returns [ SIPDateOrDeltaSeconds e]
{
	// This is the same as the rule above but there is an extra
	// quote (for some strange reason, the RFC specifies contact
	// headers this way).
}
: (    DOUBLEQUOTE { selectLexer("dateLexer"); }  e = sip_date DOUBLEQUOTE   
   | e = delta_seconds  )
;



sip_date returns [ SIPDate d ] :
     d = rfc1123date
;



rfc1123date returns [ SIPDate d ] 
{
	d = new SIPDate();
	String  w;
	MyDate  d1;
	MyTime  t;
	startTracking();
}
: w = wkday COMMA SP d1 = date1 SP  t = time SP GMT
{
	d.setInputText(stopTracking());
	try {
		d.setWkday(w);
		d.setDay(d1.day);
		d.setMonth(d1.month);
		d.setYear(d1.year);
		d.setHour(t.hour);
		d.setMinute(t.minute);
		d.setSecond(t.second);
	} catch (IllegalArgumentException ex) {
		throw new RecognitionException("Illegal Date " 
				+ ex.getMessage());
	}
}
;

wkday  returns [ String w ] 
{ w = null ; }
:MON { w = DateKeywords.MON; } |
 TUE { w = DateKeywords.TUE; } | 
 WED { w = DateKeywords.WED; } | 
 THU { w = DateKeywords.THU; } | 
 FRI { w = DateKeywords.FRI; } | 
 SAT { w = DateKeywords.SAT; } | 
 SUN { w = DateKeywords.SUN; } 
;

date1 returns [ MyDate d ]
{
	d = new MyDate();
	int day = 0 ;
	int yr = 0;
	String mo;
}
:
	d1:DIGIT d2:DIGIT 
{ 
	day = Integer.parseInt(d1.getText() + d2.getText()); 
}       
	SP mo = month SP d3:DIGIT d4:DIGIT d5:DIGIT d6:DIGIT
{ 	
	  yr = Integer.parseInt(d3.getText() + d4.getText() + d5.getText() +
				d6.getText()); 
	  d.day = day;
	  d.month = mo;
	  d.year = yr;
}
;

month returns [ String  w ] 
{
	w = null;
}
:
 ( JAN { w = DateKeywords.JAN; } | 
   FEB { w = DateKeywords.FEB; } | 
   MAR { w = DateKeywords.MAR; } | 
   APR { w = DateKeywords.APR; } | 
   MAY { w = DateKeywords.MAY; } | 
   JUN { w = DateKeywords.JUN; } | 
   JUL { w = DateKeywords.JUL; } | 
   AUG { w = DateKeywords.AUG; } | 
   SEP { w = DateKeywords.SEP; } | 
   OCT { w = DateKeywords.OCT; } | 
   NOV { w = DateKeywords.NOV; } | 
   DEC { w = DateKeywords.DEC; } )
;

time returns [ MyTime t ] 
{
	int hr = 0;
	int min = 0;
	int sec = 0;
	t = new MyTime();
	String hr_string = "";
	String min_string = "";
	String sec_string = "";

}
: (d1:DIGIT { hr_string += d1.getText(); } )+ 
  COLON ( d2:DIGIT { min_string += d2.getText(); } )+  
  COLON ( d3:DIGIT { sec_string += d3.getText(); } )+
{
	hr  = Integer.parseInt(hr_string);
	min = Integer.parseInt(min_string);
	sec = Integer.parseInt(sec_string);
	t.hour = hr;
	t.minute = min;
	t.second = sec;
	
}
;


comment returns [ String c ] 
{
	c = null;
}
: LPAREN {
	// Returns the comment string excluding the parenthesis at the ends.
	// nested comments are allowed.
	pushLexer("charLexer");
	int  counter = 1;
	c = "";
	Token tok = null;
	while(true) {
	  tok = LT(1);
	  if (tok == null) 
		throw new RecognitionException("Unterminated Comment");
	  consume();
	  if (tok.getType() == LPAREN) {
		c += tok.getText();
		counter++;
	  } else if (tok.getType() == charLexerTokenTypes.BACKSLASH) {
		c += tok.getText();
		tok = LT(1);
		consume();
		c += tok.getText();
 	  } else if ( tok.getType() == RPAREN) {
		counter -- ;
		if (counter == 0 ) break;
		c += tok.getText();
	  }
	}
	track( c + tok.getText());
	popLexer();
}
;


//----------------------------------------------------------------------------//


request_line  returns [ RequestLine req_line ]
{
	URI u;
	req_line = new RequestLine();
	String m;
	String v;
	startTracking();
}
:      m = method SP 
	{ selectLexer("sip_urlLexer", sip_urlLexer.INIT_STATE); }
	u = request_uri SP { 
		req_line.setMethod(m);
		req_line.setUri(u);
		// Note the RETURN is below as we can throw an exception here!
		try {
			CheckConstraints.checkRequestURI(u,m);
		} catch (IllegalArgumentException ex) {
			throw new RecognitionException(ex.getMessage());
		}
		// Check for request line restrictions on URI
		selectLexer("method_keywordLexer"); 
	} 
	v = sip_version 
	RETURN 
{ 
	req_line.setSipVersion(v);
	req_line.setInputText(stopTracking());
        selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	req_line = (RequestLine)
	handleParseException(ex,ILLEGAL_REQUEST_LINE_EXCEPTION, 
		req_line,SIPHeaderNames.REQUEST_LINE, true);
}
catch [ RecognitionException ex ] {
	req_line = (RequestLine)
	handleParseException(ex,ILLEGAL_REQUEST_LINE_EXCEPTION, 
		req_line,SIPHeaderNames.REQUEST_LINE, true);
}

request_uri returns [ URI  u ]
{
	u = null;
}
: u = sip_url | u = absolute_uri
;



//----------------------------------------------------------------------------//

content_type returns [ ContentType ct ]
{
	ct = new ContentType();
	MediaRange r = null;
	startTracking();
}
: CONTENT_TYPE_COLON
	r = media_type (SP|HT)* RETURN
{ 
	ct.setInputText(stopTracking());
	ct.setMediaRange(r);
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	ct = (ContentType)
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
		ct,SIPHeaderNames.CONTENT_TYPE, true);
}
catch [ RecognitionException ex ] {
	ct = (ContentType)
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
		ct,SIPHeaderNames.CONTENT_TYPE, true);
}

media_type returns [ MediaRange  r ]
{
	String t;
	String s;
	NameValue p;
	r = new MediaRange();
	startTracking();
}
:   t = type SLASH s = subtype 
   (options{greedy = true; }: SP|HT)*
    ( 
	SEMICOLON 
	(options{greedy = true; }: SP|HT)*  
	p = parameter 
	{r.getParameters().add(p);} 
	(options{greedy = true; }: SP|HT)* 
    )*
{
	r.setType(t);
	r.setSubtype(s);
	r.setInputText(stopTracking());
}
;


subtype returns [ String s ]:
        s =  ttoken 
;


attribute returns [ String s ]:
        s = ttoken
;

value returns [ String s ] :
        s =  quoted_string_or_ttoken
;


//----------------------------------------------------------------------------//

content_encoding returns [ ContentEncodingList c ]
{
	c = new ContentEncodingList ();
	startTracking();
	String enc;
}
: CONTENT_ENCODING_COLON
     enc = content_coding { c.add(new ContentEncoding(enc)); }  (SP|HT)*
    (COMMA (SP|HT)* enc = content_coding 
     { c.add(new ContentEncoding(enc)); } (SP|HT)*  )*  RETURN
{ 
	// Set the encoding with which to
	// read the message content (only works for pipelined parser).
	parserMain.setContentCoding(enc);
	c.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	c = (ContentEncodingList)
	handleParseException(ex, HEADER_PARSE_EXCEPTION, 
		c,SIPHeaderNames.CONTENT_ENCODING, true);

}
catch [ RecognitionException ex ] {
	c = (ContentEncodingList)
	handleParseException(ex, HEADER_PARSE_EXCEPTION, 
			c,SIPHeaderNames.CONTENT_ENCODING, true);
}


//----------------------------------------------------------------------------//
content_length returns [ ContentLength cl ] 
{
	cl = new ContentLength();
	String s = "";
	int i = 0;
	startTracking();
}
:    CONTENT_LENGTH_COLON
	( d:DIGIT { s += d.getText(); } )+
	(SP|HT)* RETURN
{ 
	i = Integer.parseInt(s);
	cl.setContentLength(i);
	cl.setInputText(stopTracking());
        selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	cl = (ContentLength)
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			cl,SIPHeaderNames.CONTENT_LENGTH, true);
}
catch [ RecognitionException ex ] {
	cl = (ContentLength)
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			cl,SIPHeaderNames.CONTENT_LENGTH, true);
}


//----------------------------------------------------------------------------//


hide returns [ Hide h ] 
{
	h = new Hide();
	String flag = null;
	startTracking();
}
: HIDE_COLON
	(ROUTE { h.setHide(SIPKeywords.ROUTE); } | 
		HOP { h.setHide(SIPKeywords.HOP); } ) 
	(SP|HT)* RETURN
{ 
	h.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	h = (Hide)
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
		h,SIPHeaderNames.HIDE, true);
}
catch [ RecognitionException ex ] {
	h = (Hide)
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			h,SIPHeaderNames.HIDE, true);
}


//----------------------------------------------------------------------------//

max_forwards returns [ MaxForwards mf ] 
{
	mf = new MaxForwards();
	startTracking();
	String s = "";
	int m = 0;
}
:   MAX_FORWARDS_COLON
	(d:DIGIT { s += d.getText(); } )+ 
    (SP|HT)* RETURN
{ 
    m = Integer.parseInt(s);
    mf.setMaxForwards(m);
    mf.setInputText(stopTracking());
    selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	mf = (MaxForwards)
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			mf,SIPHeaderNames.MAX_FORWARDS, true);
}
catch [ RecognitionException ex ] {
	mf = (MaxForwards)
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			mf,SIPHeaderNames.MAX_FORWARDS, true);
}

//----------------------------------------------------------------------------//

organization returns [ Organization s ] 
{
	String r = null;
	s = new Organization();
	startTracking();
}
: ORGANIZATION_COLON
	r = textUTF8
(SP|HT)* RETURN
{ 
	s.setOrganization(r);
	s.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	s = (Organization)
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			s,SIPHeaderNames.ORGANIZATION, true);
}
catch [ RecognitionException ex ] {
	s = (Organization)
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			s,SIPHeaderNames.ORGANIZATION, true);
}

//----------------------------------------------------------------------------//

priority returns [ Priority pri ] 
{
	pri = new Priority();
	String p;
	startTracking();
}
: PRIORITY_COLON
	p = priority_value 
(SP|HT)* RETURN
{ 
	pri.setPriority(p);
	pri.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	pri = (Priority)
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			pri,SIPHeaderNames.PRIORITY, true);
}
catch [ RecognitionException ex ] {
	pri = (Priority)
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
		pri,SIPHeaderNames.PRIORITY, true);
}

priority_value returns [ String p ]  
{
	p = null;
} :     EMERGENCY  { p = PriorityKeywords.EMERGENCY ; 	} 	| 
	URGENT     { p = PriorityKeywords.URGENT;  	}  	| 
	NORMAL     { p = PriorityKeywords.NORMAL; 	}	| 
	NON_URGENT { p = PriorityKeywords.NON_URGENT; 	}
;
	

//----------------------------------------------------------------------//

proxy_authorization returns [ ProxyAuthorization pa ] 
{
	pa = new ProxyAuthorization();
	Credentials cr;
	startTracking();
}
: PROXY_AUTHORIZATION_COLON
	cr = credentials (SP|HT)* 
  RETURN
{ 
	pa.setScheme(cr.getScheme());
	pa.setCredentials(cr.getCredentials());
	pa.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	pa = (ProxyAuthorization) 
   	handleParseException
		(ex,HEADER_PARSE_EXCEPTION, 
		pa,SIPHeaderNames.PROXY_AUTHORIZATION, true);
}
catch [ RecognitionException ex ] {
	pa = (ProxyAuthorization) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			pa,SIPHeaderNames.PROXY_AUTHORIZATION, true);
}

//--------------------------------------------------------------------//

proxy_require  returns [ ProxyRequireList pr ] 
{
	pr = new ProxyRequireList ();
	startTracking();
	String op ;
}
: PROXY_REQUIRE_COLON
	( op = option_tag ) (SP|HT)*  { pr.add( new ProxyRequire(op)); } 
	( COMMA (SP|HT)* op = option_tag  (SP|HT)* 
		{ pr.add(new ProxyRequire(op)); } )*
 RETURN
	{ 
	pr.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
	}
;
exception 
catch [ TokenStreamException ex ] {
	pr = (ProxyRequireList) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			pr,SIPHeaderNames.PROXY_REQUIRE, true);
}
catch [ RecognitionException ex ] {
	pr = (ProxyRequireList) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			pr,SIPHeaderNames.PROXY_REQUIRE, true);
}

//------------------------------------------------------------------------//

route returns [ RouteList al ] 
{
	al = new RouteList();
	startTracking();
	Address a;
	Route r = null;
	NameValue nv = null;
}
: ROUTE_COLON
	a = name_addr (SP|HT)*
        {
	   r = new Route(); 
	   r.setAddress(a); 
	   al.add(r);    
        }
	(
	 SEMICOLON (SP|HT)* nv = generic_param (SP|HT)*
	 { 
	    r.addParam(nv);
	    nv = null;
	  } 
	)* 
	( 
	  COMMA (options { greedy = true; }: SP|HT)*  a = name_addr   (SP|HT)*
	  {
		r = new Route(); 
		r.setAddress(a); 
		al.add(r); 
	  }
	  ( 
           SEMICOLON (SP|HT)* nv = generic_param (SP|HT)*
	    {  
	        r.addParam(nv);
		nv = null;
	    } 
	  )* 
	)*
RETURN
	{ 
	  al.setInputText(stopTracking());
	  selectLexer("command_keywordLexer"); 
	}
;
exception 
catch [ TokenStreamException ex ] {
	 al = (RouteList) 
		handleParseException(ex, HEADER_PARSE_EXCEPTION, 
			al,SIPHeaderNames.ROUTE, true);
	
}
catch [ RecognitionException ex ] {

	 al = (RouteList) 
	handleParseException(ex, HEADER_PARSE_EXCEPTION, 
			al,SIPHeaderNames.ROUTE, true);
}

//---------------------------------------------------------------------------//

require returns [ RequireList req ] 
{
	req = new RequireList ();
	String op;
	startTracking();
} 
: REQUIRE_COLON
	op = option_tag (SP|HT)* { req.add(new Require(op)); }
	(  COMMA (SP|HT)* op = option_tag (SP|HT)*
	  { req.add(new Require(op)); } )* 
  RETURN
{ 
	req.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	req = (RequireList) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			req,SIPHeaderNames.REQUIRE, true);
}
catch [ RecognitionException ex ] {
	req = (RequireList) 
	handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			req,SIPHeaderNames.REQUIRE, true);
}

//-------------------------------------------------------------------------//

response_key returns [ ResponseKey k ] 
{
	k = new ResponseKey();
	NameValue nv = null ; 
	String s;
	startTracking();
}
: RESPONSE_KEY_COLON
    s = key_scheme 
    ( (SP|HT)+ nv = key_param (SP|HT)*  { k.getKeyParam().add(nv); }
    ( COMMA (SP|HT)* nv = key_param 
	{ k.getKeyParam().add(nv); } (SP|HT)* )* )? 
  RETURN
{ 
   k.setKeyScheme(s);
   k.setInputText(stopTracking());
   selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	k = (ResponseKey) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
		k,SIPHeaderNames.RESPONSE_KEY, true);
}
catch [ RecognitionException ex ] {
	k = (ResponseKey) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			k,SIPHeaderNames.RESPONSE_KEY, true);
}


key_param returns [ NameValue nv ]
{
	nv = new NameValue();
	String n;
	String v;
	startTracking();
}
: n = ttoken (SP|HT)*  EQUALS (SP|HT)*  v = quoted_string_or_ttoken
{
	nv.setName(n);
	nv.setValue(v);
	nv.setInputText(stopTracking());
}
;
//------------------------------------------------------------------------//

subject returns [ Subject s ] 
{
	s = new Subject();
	startTracking();
	String r;
}
: SUBJECT_COLON
	r = textUTF8
  (SP|HT)* RETURN
{ 
	
	s.setSubject(r);
	s.setInputText(stopTracking());
	selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	s = (Subject) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
		s,SIPHeaderNames.SUBJECT, true);
}
catch [ RecognitionException ex ] {
	s = (Subject) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
		s,SIPHeaderNames.SUBJECT, true);
}

//-----------------------------------------------------------------------//

user_agent returns [ UserAgent ua ] 
{
	ua  = null;
        String s = null;
	Product p;
	startTracking();
	
}
: USER_AGENT_COLON
    ( (LPAREN)=> s = comment  { ua = new UserAgent(); ua.setComment(s);}  | 
    p =  product { ua = new UserAgent(); ua.setProduct(p); } )
  (SP|HT)* RETURN
{ 
   ua.setInputText(stopTracking());
   selectLexer("command_keywordLexer");  
}
;
exception 
catch [ TokenStreamException ex ] {
	ua = (UserAgent) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
		ua,SIPHeaderNames.USER_AGENT, true);
}
catch [ RecognitionException ex ] {
	ua = (UserAgent) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
		ua,SIPHeaderNames.USER_AGENT, true);
}

//-------------------------------------------------------------------------//


contact returns [ ContactList cl ]
{
	cl = null;
	startTracking();
}
:CONTACT_COLON
cl = contact_body   RETURN
{
	cl.setInputText(stopTracking());
	selectLexer("command_keywordLexer");
}
;
exception 
catch [ TokenStreamException ex ] {
	cl = (ContactList ) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
		cl,SIPHeaderNames.CONTACT, true);
}
catch [ RecognitionException ex ] {
	cl = (ContactList) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
		cl,SIPHeaderNames.CONTACT, true);
}

contact_body returns [ ContactList cl ] 
{
	cl = new ContactList();
	Contact c ;
	startTracking();
}
: c = contact_item 
{ 
    try {
       cl.add(c);
    } catch (IllegalArgumentException ex) { 
	throw new RecognitionException(ex.getMessage());
    }  
    c.setContactList(cl); 
} 
(options{greedy = true; }: SP|HT)* 
(
 COMMA 
 { selectLexer("sip_urlLexer", sip_urlLexer.INIT_STATE); } 
 (options{greedy = true; }: SP|HT)* 
  c = contact_item 
  { 
    try { 
	cl.add(c); 
     } catch (IllegalArgumentException ex) { 
	throw new RecognitionException(ex.getMessage());
     }  
     c.setContactList(cl);
  }
 (options{greedy = true; }: SP|HT)* 
)* 
{
	cl.setInputText(stopTracking());
}
;


contact_item returns [ Contact c ]
{

	c = new Contact();
	Address a = null;
	String com = null;
	NameValue p;
	startTracking();
}
:
(STAR_FLAG {  c.setWildCardFlag(true);  }  
| ( a = address { c.setAddress(a); } 
     { selectLexer("contact_parmsLexer"); }
     (options{greedy = true;}: SP|HT)*
     ( (SEMICOLON) => 
	(SEMICOLON 
	{ selectLexer("contact_parmsLexer"); }
        (options{greedy = true;}: SP|HT)*  
	p = contact_parm { c.getContactParms().add(p); } 
        (options{greedy = true;}: SP|HT)* )+ 
     )?
     { selectLexer("charLexer"); } 
     ((LPAREN)=> com = comment { c.setComment(com); } )? 
   )
) 
{ 
   c.setInputText(stopTracking());
   // Change made in accordance with the following...
   // The Contact, From and To header fields contain a URL. If the URL
   // contains a comma, question mark or semicolon, the URL MUST be
   // enclosed in angle brackets (< and >). Any URL parameters are
   // contained within these brackets. If the URL is not enclosed in angle
   // brackets, any semicolon-delimited parameters are header-parameters,
   // not URL parameters.
   //
   if (a != null && a.getAddressType() == Address.ADDRESS_SPEC) {
	c.getContactParms().concatenate(a.getAddrSpec().getUriParms());
	a.getAddrSpec().clearUriParms();
   }
}
;

address returns [ Address a ] 
{
   URI  u ;
   a = null;
   int k = 1;
   String prefix = "";
   startTracking();
   while( LA(k) != RETURN ) {
	String n = LT(k).getText();
	prefix += n;
	if ( LA(k) ==  LESS_THAN ) {
	  	break;
	} else if ( LA(k) == DOUBLEQUOTE) {
		break; 
	} else if ( LA(k) == COLON ) {
	  	break;
	} else if (LA(k) == SLASH) {
		break;
	}
        k++;
   }
}

: ( { LA(k) == LESS_THAN || LA(k) == DOUBLEQUOTE }?  a = name_addr   
| {LA(k) == COLON || LA(k) == SLASH }? u = addr_spec_noparms  {
	// This is a relative uri or an absolute uri.
	a = new Address();
	a.setAddressType(Address.ADDRESS_SPEC);
	a.setAddrSpec(u);
} ) 
{   
	a.setInputText(stopTracking());  
}
;

contact_parm returns [ NameValue p ] 
{
	p =  null;
	double qv = 0.0;
        String ac = null;
        SIPDateOrDeltaSeconds d = null;
	NameValue nv = null;
	startTracking();
	
}
: ( Q (SP|HT)* EQUALS  { selectLexer("charLexer"); } 
	(SP|HT)*   qv = qvalue  
	{ p = new NameValue(SIPKeywords.Q,new Double(qv)); } 
|  ACTION (SP|HT)* EQUALS (SP|HT)*  ( PROXY { ac = SIPKeywords.PROXY; } |  
   	REDIRECT { ac = SIPKeywords.REDIRECT; } ) 
     	{ p = new NameValue (SIPKeywords.ACTION,ac) ; } 
| EXPIRES (SP|HT)* EQUALS { selectLexer("charLexer"); }  
	(SP|HT)* d = date_or_delta_seconds1
     { 
	// For some strange reason the RFC specifies a quote around the date
	// so we have to make a new rule (sigh!).
	p = new NameValue(SIPKeywords.EXPIRES, d );
      } 
| p =   extension_attribute ) 
{ 
	p.setInputText(stopTracking()); 
} 
;


extension_attribute returns [ NameValue nv ] 
{
	nv = new NameValue();
	String n ;
	String val  = null;
	startTracking();
}
: t:ID (options{greedy = true;}: SP|HT)* 
  (
	(EQUALS)=>  EQUALS 
	(options{greedy = true;}: SP|HT)* 
	{selectLexer("charLexer"); } 
	val = extension_value 
   )?
{
	nv.setName(t.getText());
	nv.setValue(val);
	nv.setInputText(stopTracking());
}
;



name_addr returns [ Address a ] 
{
	String d = null;
	URI    u;
        a = new Address();
	startTracking();
} 
: (  LESS_THAN  
	{ selectLexer("sip_urlLexer", sip_urlLexer.INIT_STATE);  } 
	(options{greedy=true;}: SP|HT)*
	u = addr_spec 
	(options{greedy=true;}: SP|HT)*
	GREATER_THAN 
| d = display_name  LESS_THAN 
	{ selectLexer("sip_urlLexer", sip_urlLexer.INIT_STATE); } 
	(options{greedy=true;}: SP|HT)*
	u = addr_spec 
	(options{greedy=true;}: SP|HT)*
	GREATER_THAN )
{
    String dname = null;
    if (d != null)  dname = d.trim();
    a.setDisplayName(dname);
    a.setAddrSpec(u);
    a.setAddressType(Address.NAME_ADDR); 
    a.setInputText(stopTracking());
}
;

// Display names do not need to have quotes around them!
// This rule has to have a number of hacks because of the way in which
// lookaheads are processed in antlr. This rule can be processed when 
// it is in the sip_urlLexer context and therefore the lookahead can
// include the token SIP. (UGH!!)
display_name returns [ String s ] 
{
	s= null;
}
: s = quoted_string (options{greedy =true;}:SP|HT)*
| 
( options{greedy = true;}:
   s13:ID       { if ( s == null) s = s13.getText(); else s += s13.getText(); } 
|  s15:SIP      { if ( s == null) s = s15.getText(); else s += s15.getText(); } 
|  s2:SP        { if ( s == null) s = s2.getText(); else s += s2.getText(); }  
|  s3:HT        { if ( s == null) s = s3.getText(); else s += s3.getText(); } 
)+ 
;


addr_spec returns [ URI u ] :
	u = uri_reference
;

addr_spec_noparms returns [ URI u ] :
	u = uri_noparms
;


//-----------------------------------------------------------------------//



warning returns [ WarningList wvlist ]
{
	wvlist = new WarningList();
	Warning wv = null;
	startTracking();
}
: WARNING_COLON wv = warning_value { wvlist.add(wv);  }  (SP|HT)*
	(  COMMA  (SP|HT)* wv = warning_value { wvlist.add(wv); } )*
RETURN
{ 
wvlist.setInputText(stopTracking()); 
}
;
exception 
catch [ TokenStreamException ex ] {
	wvlist = (WarningList) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			wvlist,SIPHeaderNames.WARNING, true);
}
catch [ RecognitionException ex ] {
	wvlist = (WarningList) 
		handleParseException(ex,HEADER_PARSE_EXCEPTION, 
			wvlist,SIPHeaderNames.WARNING, true);
}


warning_value returns [ Warning w ] 
{
	int wc;
	WarnAgent wa;
	String wt;
	w = new Warning();
	startTracking();
}
: wc = warn_code SP wa = warn_agent  SP  wt = warn_text
{
	w.setWarnCode(wc);
	w.setWarnAgent(wa);
	w.setWarnText(wt);
	w.setInputText(stopTracking());
}
;

warn_code returns [ int wc ] 
{
	wc = -1;
	String s;
}
: 	d1:DIGIT d2:DIGIT d3:DIGIT
{
	s = d1.getText() + d2.getText() + d3.getText();
	wc = Integer.parseInt(s);
}
;

warn_agent returns [ WarnAgent wa ]
{
	wa = null;
	HostPort hp;
	String p;
	startTracking();
}
: hp = host_port 
{ 
  wa = new WarnAgent(hp);  
  wa.setInputText(stopTracking());
}
;
exception 
catch [ RecognitionException ex ] {
 String pmatch = stopTracking();
 boolean done = false;
 while(!done) {
	Token nexttok = LT(1);
	done = nexttok == null ||
	       nexttok.getText().equals(" ") ||
	       nexttok.getText().equals("\n");
	pmatch += nexttok.getText();
 }
 wa = new WarnAgent(pmatch); 
}




pseudonym returns [ String p ]:
        p = ttoken
;


warn_text returns [ String t ]:
        t = quoted_string
;



//--------------------------------------------------------------------//

key_scheme returns [ String s ] :
     s = ttoken
;


 pgp_params returns [ NameValue nv ] 
{ 
	nv = null;
	parserMain.setEnclosingLexer("pgp_Lexer");
}
:(  nv = realm 
| nv  = pgp_version
| nv  = algorithm
| nv = pgp_pubalgorithm 
| nv = nonce )
;
 
pgp_version returns [ NameValue nv ]
{
	startTracking();
 	String s = "";
	nv = null;
}
:VERSION (SP|HT)* EQUALS  (SP|HT)* s = quoted_string 
{
	nv = new NameValue( SIPKeywords.VERSION, s);
	nv.setInputText(stopTracking());
}
;
 



algorithm returns [ NameValue nv ]
{
	nv = new NameValue();
	startTracking();
	String s;
}
: ALGORITHM (SP|HT)* EQUALS (SP|HT)* s=ttoken
{
	nv.setName(SIPKeywords.ALGORITHM);
	nv.setValue(s);
	nv.setInputText(stopTracking());
}
;

nonce returns [ NameValue nv ]
{
	nv = new NameValue();
	String s;
	startTracking();
}
: NONCE (SP|HT)* EQUALS  
(SP|HT)* s = quoted_string 
{
	nv.setName(SIPKeywords.NONCE);
	nv.setValue(s);
	nv.setInputText(stopTracking());
}
;

pgp_pubalgorithm returns [ NameValue nv ]
{
	nv = new NameValue();
	String s;
	startTracking();
}
: PUBKEY (SP|HT)* EQUALS (SP|HT)* s = ttoken
{
	nv.setName(SIPKeywords.PUBKEY);
	nv.setValue(s);
	nv.setInputText(stopTracking());
}
;



//-------------------------------------------------------------------------//


www_authenticate returns [ WWWAuthenticateList cl ] 
{
	cl = new WWWAuthenticateList();
	Challenge c;
	startTracking();
} 
:WWW_AUTHENTICATE_COLON
( PGP  (SP|HT)+  c = pgp_challenge { cl.add(new WWWAuthenticate(c));  }
| BASIC  (SP|HT)+ c = basic_challenge { cl.add(new WWWAuthenticate(c)); }
| DIGEST  (SP|HT)+ c = digest_challenge { cl.add(new WWWAuthenticate(c)); }
| t:ID (SP|HT)+ c = challenge 
	{ c.setScheme(t.getText()); cl.add (new WWWAuthenticate(c)); } )
(SP|HT)* RETURN
{ 
  cl.setInputText(stopTracking());
  selectLexer("command_keywordLexer"); 
}
;
exception 
catch [ TokenStreamException ex ] {
	cl = (WWWAuthenticateList) 
		handleParseException(ex, HEADER_PARSE_EXCEPTION, 
		cl,SIPHeaderNames.WWW_AUTHENTICATE, true);
}
catch [ RecognitionException ex ] {
	cl = (WWWAuthenticateList) 
		handleParseException(ex, HEADER_PARSE_EXCEPTION, 
			cl,SIPHeaderNames.WWW_AUTHENTICATE, true);
}


basic_challenge returns [ Challenge ch ] 
{
	startTracking();
	NameValue r = null;
	String t;
	ch = null;
}
: ((REALM) => r = realm (SP|HT)* COMMA (SP|HT)*)?  t = ttoken 
{
	ch = new Challenge();
	ch.setScheme(SIPKeywords.BASIC);
	if (r!= null) ch.setParam(r);
	ch.setParam(new NameValue(null,t));
	ch.setInputText(stopTracking());

}
;

digest_challenge returns [ Challenge ch ]
{ 
	ch = new Challenge() ;  
	ch.setScheme(SIPKeywords.DIGEST);
	NameValue nv; 
	startTracking();
}
:nv = digest_param { ch.setParam(nv); } 
(options{greedy = true;}: SP|HT)* 
(
  COMMA 
  (options{greedy = true;}: SP|HT)* 
   nv = digest_param 
   { ch.setParam(nv); } 
  (options{greedy = true;}: SP|HT)*
)* 
{ ch.setInputText(stopTracking()); } 
;


digest_param returns [ NameValue r ] 
{
	r = null;
	startTracking();
	parserMain.setEnclosingLexer("digest_Lexer"); 
}
: (  r = realm	   |
     r = domain    |
     r = nonce     |
     r = opaque    |
     r = stale     |
     r = algorithm |
     r = qop_value )  
{ r.setInputText(stopTracking()); }
;

qop_value returns [ NameValue nv ] 
{
	startTracking();
	String t;
	nv = null;
}
: QOP (SP|HT)* EQUALS (SP|HT)* t = quoted_string
{
	nv = new NameValue(SIPKeywords.QOP, t);
	nv.setInputText(stopTracking());
}
;
	

opaque returns [ NameValue nv ]
{
	startTracking(); 
	String t;
	nv = null;
}
: OPAQUE (SP|HT)* EQUALS (SP|HT)* t = quoted_string
{ 
      nv  = new NameValue(SIPKeywords.OPAQUE, t); 
      nv.setInputText(stopTracking());
}
;

domain returns [ NameValue nv ]
{
	startTracking(); 
	String t;
	nv = null;
}
: DOMAIN (SP|HT)* EQUALS (SP|HT)* t = quoted_string
{ 
      nv  = new NameValue(SIPKeywords.DOMAIN, t); 
      nv.setInputText(stopTracking());
}
;

stale returns [ NameValue nv ] 
{
	startTracking();
	String t = null;
	nv = null;
}
: STALE (SP|HT)* EQUALS (SP|HT)* t = ttoken
{
	if (t.toLowerCase().compareTo("true") != 0 &&
	    t.toLowerCase().compareTo("false") != 0 ) {
		throw new RecognitionException("Expecting true or false");
	}
	nv = new NameValue(SIPKeywords.STALE,t.toLowerCase());
	nv.setInputText(stopTracking());
}
;


     

pgp_challenge returns [ Challenge ch ]
{
  startTracking();
  ch = new Challenge();
  NameValue c = null;
}
:   c = pgp_params { ch.getAuthParams().add(c); } 
(  
  COMMA 
  { selectLexer("pgp_Lexer"); } 
  (options{greedy = true;}: SP|HT)* 
	c = pgp_params { ch.getAuthParams().add(c); } 
   (options{greedy = true; }: SP|HT)* 
 )* 
{
   ch.setScheme(SIPKeywords.PGP);
   ch.setInputText(stopTracking());
}
;



challenge returns [ Challenge c ] 
{
	c = new Challenge();
	NameValue r = null;
	NameValue ap = null;
	startTracking();
	selectLexer("charLexer");
}
:r = realm { c.getAuthParams().add(r); } 
     (options{greedy = true;}:SP|HT)* 
  ( COMMA 
    (options{greedy = true;}: SP|HT)*  
     ap = auth_params 
     { c.getAuthParams().add(ap); } 
    (options{greedy = true;}: SP|HT)* 
  )*
{
	c.setInputText(stopTracking());
}
;

realm returns [ NameValue nv ] 
{
	nv = new NameValue();
	String rv;
}
: REALM (SP|HT)* EQUALS (options{greedy = true;}: SP|HT)*  rv = realmvalue
{
	nv.setName(SIPKeywords.REALM);
	nv.setValue(rv);

}
;

realmvalue returns [ String rv ] :
      rv = quoted_string
;




delta_seconds returns [ DeltaSeconds ds ] 
{
	String s = "";
	int i = 0;
	ds = null;
}
:
       ( d:DIGIT { s += d.getText(); } )+
{
	i = Integer.parseInt(s);
	ds = new DeltaSeconds();
	ds.setInputText(s);
	ds.setDeltaSeconds(i);
}
;


generic_param returns [ NameValue nv ] 
{
	nv = null;
	String n = null;
	String v = null;
	startTracking();
	nv = new NameValue();

}
:( n = ttoken (options { greedy = true; }: SP|HT)*  
	((EQUALS)=> EQUALS (options { greedy = true; } : SP|HT)* 
         ((DOUBLEQUOTE) => v = quoted_string | 
	  (options{ greedy = true; } : SP|HT)* v = ttoken ))?
| ( t1:ID { n = t1.getText(); } 
	( options{greedy = true; }:  SP|HT)* 
	((EQUALS)=> EQUALS 
	 (options { greedy = true; } : SP|HT)* 
         (
	  (DOUBLEQUOTE) => v = quoted_string  | 
	  t2:ID { v = t2.getText(); } ))? 
  ) 
)
{ 
	nv.setName(n);
	nv.setValue(v);
	nv.setInputText(stopTracking());
}
;

