
header {
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.*;
//  These are common parser routines that the various parsers inherit.
/**
* Revisions since V0.9 Release
*@version 1.0
*@revision (Ranga) 
* Added support to call into the main parser to arrest and release line counter.
*/
}
class parser_utilParser extends Parser;

options {
	k=1;
	defaultErrorHandler=false;
	exportVocab = parser_utilParser;
}

{

MsgParser parserMain;

/**
* This code is to track the portion of the input that
* matches non-terminals.
*/

private java.util.Vector   vbuf = new java.util.Vector(10,10);
private int 	 topOfStack = 0;
private java.util.Vector   currentRule = new java.util.Vector(10,10);
private int 	 traceTopOfStack = 0;
private boolean traceEnabled;
private final int HEADER_PARSE_EXCEPTION = 1;
private final int ILLEGAL_REQUEST_LINE_EXCEPTION = 2;
private final int ILLEGAL_STATUS_LINE_EXCEPTION = 3;
private final int SDP_PARSE_EXCEPTION = 4;
private final int UNRECOGNIZED_EXTENSION_EXCEPTION = 5;
private final int UNEXPECTED_HEADER_EXCEPTION = 6;
private final int INVALID_HEADER_EXCEPTION = 7;

   public void match(int t) throws  MismatchedTokenException,
    TokenStreamException {
         if ( parserMain.trackInput && inputState.guessing == 0 ) {
	    for (int i = 0; i < topOfStack; i ++) {
	        StringBuffer rbuf = (StringBuffer) vbuf.elementAt(i);
	        rbuf.append(LT(1).getText());
	    }
           }
           super.match(t);
    }
   public void traceIn(String rname) 
	throws TokenStreamException 
	{
		traceEnabled = true;
		currentRule.add(rname);
		super.traceIn(rname);
	}

   public void traceOut(String rname)
	throws TokenStreamException 
	{
		traceEnabled = true;
		Object lastElement = currentRule.lastElement();
		currentRule.remove(lastElement);
		super.traceOut(rname);
	}
	

   public void match(BitSet b) throws  MismatchedTokenException,
       TokenStreamException {
       if ( parserMain.trackInput  && (inputState.guessing == 0) ) {
	    for (int i = 0; i < topOfStack; i ++) {
	        StringBuffer rbuf = (StringBuffer) vbuf.elementAt(i);
	        rbuf.append(LT(1).getText());
	    }
       }
       super.match(b);
  }

  private void track ( String str)  {
      if ( parserMain.trackInput && (inputState.guessing == 0) ) {
	    for (int i = 0; i < topOfStack; i ++) {
	        StringBuffer rbuf = (StringBuffer) vbuf.elementAt(i);
	        rbuf.append(str);
           }
      }
  }
		
  private void startTracking() { 
      
      if ( parserMain.trackInput  && inputState.guessing == 0 ) {
	 if (parserMain.debugFlag == 1 && traceEnabled ) {
	    String cur = (String) currentRule.lastElement();
	    String dbgStr = "startTracking{ " + "(" + cur + "/" +  
				topOfStack + ")";
	    Debug.print(dbgStr);
	 }
         vbuf.add(new StringBuffer());
         topOfStack++;
       }
  }

  public void consume() {
	try {
	  if (LA(1) == RETURN) {
		parserMain.newLine(); 
	  }
	} catch ( TokenStreamException ex) {}
	super.consume();
  }

  private String stopTracking() {
     if ( parserMain.trackInput  && inputState.guessing == 0 ) {
        if (topOfStack - 1  < 0) {
	   InternalError.handleException("Fatal error tracking stack!");
        }
        StringBuffer rbuf = (StringBuffer) vbuf.elementAt(topOfStack - 1 );
        vbuf.remove(topOfStack -1 );
        topOfStack --;
	
	if (parserMain.debugFlag == 1 && traceEnabled ) {
	    String cur = (String) currentRule.lastElement();
	    String dbgStr = "}stopTracking " + "(" + cur + "/" + 
				topOfStack + ")" ;
	    Debug.print(dbgStr);
	}
        return rbuf.toString();
     } else return null;
 }

 private boolean isDigit( char ch) {
	if (ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' ||
	    ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9')
	    return true;
	else return false;
 }

 
 private void selectLexer( String lname ) {
	parserMain.select(lname);
 }

 private void selectLexer( String lname, int state  ) {
	parserMain.select(lname, state);
 }

 private void pushLexer( String lname ) {
	parserMain.push(lname);
 }

 private void popLexer() {
	parserMain.pop();
	if (parserMain.enclosingLexer != null) {
		parserMain.select(parserMain.enclosingLexer);
		parserMain.enclosingLexer = null;
	}
 }

 private String getCurrentLexerName() {
	return parserMain.current_lexer_name;
 }

 /**
 * Set the lexer to switch to when hitting end of line.
 */
 private void setEOLLexer( String lname) {
	parserMain.setEOLLexer(lname);
 }

 /**
 * Arrest the line counter from progressing (when we are looking ahead)
 */
 private void arrestLineCounter() {
	parserMain.arrestLineCounter();
 }
 
 /**
 * Allow the line counter to proceed again.
 */
 private void releaseLineCounter() {
	parserMain.releaseLineCounter();
 }

 private void resetLineCounter() {
	parserMain.resetLineCounter();
 }


/**
* Handle a parse error by calling the registered error handler.
*
* If the error handler throws an exception, then this is passed back
* to the parser. This should be called only from the exception handler of the
* parser (note that stopTracking() is called).
*/
private GenericObject  handleParseException(
		RecognitionException ex, 
		int exceptionType, 
		GenericObject h,
		String headerName,  
		boolean trackFlag) 
	throws RecognitionException {
	String currentHeader = parserMain.getCurrentHeader();
	String input_text = null;
	selectLexer("charLexer");
	while (true) {
		Token tok;
		try {
			tok = LT(1);
		} catch (TokenStreamException tse) {
			if (trackFlag && parserMain.trackInput) {
				input_text = stopTracking();
			}
			ex.fillInStackTrace();
			throw ex;
		}
		if (tok.getType() == Token.EOF_TYPE) {
			 if ( trackFlag) stopTracking();
			 break;
		}
		consume();
		if (tok.getType() == RETURN ) break;
	}
	if (trackFlag)  {
		input_text = stopTracking();
	}
	try {
        	SIPParseException e = null;
 		if (exceptionType == UNRECOGNIZED_EXTENSION_EXCEPTION )  {
			e = new SIPUnrecognizedExtensionException(ex);
			selectLexer("command_keywordLexer");
		} else if (exceptionType == HEADER_PARSE_EXCEPTION)  {
			e = new SIPHeaderParseException(ex);
			selectLexer("command_keywordLexer");
        	} else if (exceptionType == SDP_PARSE_EXCEPTION) {
			e = new SDPParseException(ex);
			selectLexer("sdpLexer");
  		} else if (exceptionType == ILLEGAL_REQUEST_LINE_EXCEPTION) {
			e = new SIPIllegalRequestLineException(ex);
			selectLexer("command_keywordLexer");
        	} else if (exceptionType == ILLEGAL_STATUS_LINE_EXCEPTION) {
	       		e = new SIPIllegalStatusLineException(ex);
	       		selectLexer("command_keywordLexer");
  		}  else {
			InternalError.handleException(ex);
		}
		e.setErrorObject(h);
		e.fillInStackTrace();
		e.setErrorObjectName(headerName);
        	e.setText(currentHeader);
	   	parserMain.handleParseException(e);
	   	return e.getErrorObject();
	} catch (SIPParseException pex) {
		ex.fillInStackTrace();
		throw  ex;
	}
}

/**
* Handle a parse error by calling the registered error handler.
*
* If the error handler throws an exception, then this is passed back
* to the parser. This should be called only from the exception handler of the
* parser.
*/
private GenericObject  handleParseException(TokenStreamException ex, 
		int exceptionType, 
		GenericObject h, 
		String headerName, 
		boolean trackFlag ) throws TokenStreamException  {
	String input_text = null;
	String currentHeader = parserMain.getCurrentHeader();
	selectLexer("charLexer");
        while (true) {
		Token tok;
		try {
			tok = LT(1);
		} catch (TokenStreamException tse) {
			if (trackFlag) {
				input_text = stopTracking();
			}
			ex.fillInStackTrace();
			throw ex;
		}
		consume();
		if (tok.getType() == RETURN ) break;
        }

	if (trackFlag) {
		input_text =  stopTracking();
	}
	try {
		// If we are not at EOL, then advance to the end of line
	 	SIPParseException e = null;
         	if (exceptionType == HEADER_PARSE_EXCEPTION)  {
			e = new SIPHeaderParseException(ex);
			selectLexer("command_keywordLexer");
         	} else if (exceptionType == SDP_PARSE_EXCEPTION) {
			e = new SDPParseException(ex);
			selectLexer("sdpLexer");
         	} else if (exceptionType == ILLEGAL_REQUEST_LINE_EXCEPTION) {
			e = new SIPIllegalRequestLineException(ex);
			e.fillInStackTrace();
			selectLexer("command_keywordLexer");
        	} else if (exceptionType == ILLEGAL_STATUS_LINE_EXCEPTION) {
	       		e = new SIPIllegalStatusLineException(ex);
	       		selectLexer("command_keywordLexer");
        	}  else {
			InternalError.handleException(ex);
        	}
		e.fillInStackTrace();
		e.setErrorObjectName(headerName);
		e.setText(currentHeader);
		e.setErrorObject(h);
	   	parserMain.handleParseException(e);
	   	return e.getErrorObject();
	} catch (SIPParseException pex) {
		 ex.fillInStackTrace(); 
		 throw ex;
	}
}

/**
* Handle a parse error by calling the registered error handler.
*
* If the error handler throws an exception, then this is passed back
* to the parser. This should be called only from the exception handler of the
* parser.
*/
private GenericObject  handleParseException(NoViableAltException ex, 
		int exceptionType, 
		GenericObject h, 
		String headerName, 
		boolean trackFlag ) throws NoViableAltException {
	String input_text = null;
	String currentHeader = parserMain.getCurrentHeader();
	selectLexer("charLexer");
        while (true) {
		Token tok;
		try {
			tok = LT(1);
		} catch (TokenStreamException tse) {
			if (trackFlag) {
				input_text = stopTracking();
			}
			ex.fillInStackTrace();
			throw ex;
		}
		consume();
		if (tok.getType() == RETURN ) break;
        }

	if (trackFlag) {
		input_text =  stopTracking();
	}
	try {
		// If we are not at EOL, then advance to the end of line
	 	SIPParseException e = null;
 		if (exceptionType == INVALID_HEADER_EXCEPTION )  {
	       		e = new SIPInvalidHeaderException(ex);
	       		selectLexer("command_keywordLexer");
        	}  else {
			InternalError.handleException(ex);
        	}
		e.fillInStackTrace();
		e.setErrorObjectName(headerName);
		e.setText(currentHeader);
		e.setErrorObject(h);
	   	parserMain.handleParseException(e);
	   	return e.getErrorObject();
	} catch (SIPParseException pex) {
		 ex.fillInStackTrace(); 
		 throw ex;
	}
}

 private void Assert( boolean condition) {
	if ( ! condition ) {
		InternalError.handleException ("Assertion Failure");
	}
 }

 private void Assert( boolean condition, String msg ) {
	if ( ! condition ) {
		System.err.println("Current lexer is " 
				+ parserMain.current_lexer_name );
		InternalError.handleException ("Assertion Failure " + msg);
	}
 }

 
}


/**
* This is useful for tracking token strings from within the parser.
* We do things this way to reduce the overhead of tracking individual 
* characters on the  parse stack.
*/
ttoken returns [ String s ]
{
    s = "";
}
:( t1:ALPHA 	   { s = t1.getText(); }
 | t2:DIGIT 	   { s = t2.getText(); } 
 | t3:UNDERSCORE   { s = t3.getText(); }
 | t4:PLUS 	   { s = t4.getText(); } 
 | t5:MINUS 	   { s = t5.getText(); }
 | t6:QUOTE 	   { s = t6.getText(); }
 | t7:EXCLAMATION  { s = t7.getText(); }
 | t8:PERCENT 	   { s = t8.getText(); }
 | t9:TILDE 	   { s = t9.getText(); } 
 | t10:BACK_QUOTE  { s = t10.getText(); }
 | t11:STAR	   { s = t11.getText(); }
 | t12:DOT         { s = t12.getText(); } ) 
{
        pushLexer("charLexer");
	String r = "";
	while(true) {
		Token d = LT(1);
		if (d == null || d.getType() == Token.EOF_TYPE ) {
			throw new RecognitionException("Premature EOF");
		}		
		int ttype = d.getType();

		if (  ttype == ALPHA   || ttype == DIGIT || ttype == UNDERSCORE
		   || ttype == PLUS    || ttype == QUOTE || ttype == EXCLAMATION
		   || ttype == PERCENT || ttype == TILDE || ttype == BACK_QUOTE 
		   || ttype == DOT     || ttype == MINUS || ttype == STAR ) {
			r += d.getText();
			consume();
		} else {
			track(r);
			break;
		}
       }
       s += r;
       popLexer();
}
;

/**
* The following is for product strings.
*/
ttoken_allow_space returns [ String s ]
{
    s = "";
}
:( t1:ALPHA 	   { s = t1.getText(); }
 | t2:DIGIT 	   { s = t2.getText(); } 
 | t3:UNDERSCORE   { s = t3.getText(); }
 | t4:PLUS 	   { s = t4.getText(); } 
 | t5:MINUS 	   { s = t5.getText(); }
 | t6:QUOTE 	   { s = t6.getText(); }
 | t7:EXCLAMATION  { s = t7.getText(); }
 | t8:PERCENT 	   { s = t8.getText(); }
 | t9:TILDE 	   { s = t9.getText(); } 
 | t10:BACK_QUOTE  { s = t10.getText(); }
 | t11:STAR	   { s = t11.getText(); }
 | t12:DOT         { s = t12.getText(); }  
 | t13:SP	   { s = t13.getText(); }
 | t14:HT	   { s = t14.getText(); } )
{
        pushLexer("charLexer");
	String r = "";
	while(true) {
		Token d = LT(1);
		if (d == null || d.getType() == Token.EOF_TYPE ) {
			throw new RecognitionException("Premature EOF");
		}		
		int ttype = d.getType();

		if (  ttype == ALPHA   || ttype == DIGIT || ttype == UNDERSCORE
		   || ttype == PLUS    || ttype == QUOTE || ttype == EXCLAMATION
		   || ttype == PERCENT || ttype == TILDE || ttype == BACK_QUOTE 
		   || ttype == DOT     || ttype == MINUS || ttype == STAR 
		   || ttype == HT	|| ttype == SP ) {
			r += d.getText();
			consume();
		} else {
			track(r);
			break;
		}
       }
       s += r;
       popLexer();
}
;

/**
* Note that we are assuming that there is a higher level function to deal with 
* checking encoding. For this function, we just return a contiguous array of 
* chars. Note that base 64 strings are padded at the end with a bunch of 
* == characters.
*/

base64string returns [ String s ] 
{
	s = "";
	pushLexer("charLexer");
}
:( t1:ALPHA 	   { s = t1.getText(); }
 | t2:DIGIT 	   { s = t2.getText(); } 
 | t3:SLASH	   { s = t3.getText(); } 
 | t4:EQUALS	   { s = t4.getText(); } 
 )
{
        pushLexer("charLexer");
	String r = "";
	while(true) {
		Token d = LT(1);
		if (d == null || d.getType() == Token.EOF_TYPE ) {
			throw new RecognitionException("Premature EOF");
		}		
		int ttype = d.getType();

		if (  ttype == ALPHA || ttype == DIGIT || ttype == SLASH ||
		      ttype == EQUALS ) {
			r += d.getText();
			consume();
		} else {
			track(r);
			break;
		}
       }
       s += r;
       popLexer();
}
;


number returns [ long i ] 
{
	String s = "";
	i = 0;
}
:t:NUMBER { 
	// This alternative is necessary because of non-determinism in
	// the parser.
	i = Long.parseLong(t.getText()); 
}
|( d:DIGIT { s += d.getText(); } )+
{
	i = Long.parseLong(s);
}
;


intnumber returns [ int i ] 
{
	String s = "";
	i = 0;
}
:	( d:DIGIT { s += d.getText(); } )+
{
	i = Integer.parseInt(s);
}
| d1:NUMBER
{
	// This alternative is necessary because of non-determinism in
	// the parser.
	i = Integer.parseInt(d1.getText());
}
;


textUTF8 returns [ String s ]
{
	s = null;
}
:( options{generateAmbigWarnings = false;}:
 ( d:~RETURN 
{ 
   pushLexer("charLexer");
   while ( true ) {
	if (s == null) s = d.getText();
	else s += d.getText();
	d = LT(1);
	if (d == null || d.getType() == Token.EOF_TYPE) {
		throw new RecognitionException("Premature EOF");
	}	
	if (d.getType() == RETURN ) break;
	consume();
   }
   track(s);
   popLexer();
}
) | 
)
;

byte_string_no_comma returns [ String s ]
{
	s = "";
}
: d:~RETURN 
{ 
   pushLexer("charLexer");
   while ( true ) {
	s += d.getText();
	d = LT(1);
	if (d == null || d.getType() == Token.EOF_TYPE) {
		throw new RecognitionException("Premature EOF");
	}	
	if (d.getType() == RETURN ) break;
	else if (d.getType() == charLexerTokenTypes.COMMA) break;
	consume();
   }
   track(s);
   popLexer();
}
;

byte_string_no_semicolon returns [ String s ]
{
	s = "";
}
: d:~RETURN 
{ 
   pushLexer("charLexer");
   while ( true ) {
	s += d.getText();
	d = LT(1);
	if (d == null || d.getType() == Token.EOF_TYPE) {
		throw new RecognitionException("Premature EOF");
	}	
	if (d.getType() == RETURN ) break;
	else if (d.getType() == charLexerTokenTypes.SEMICOLON) break;
	consume();
   }
   track(s);
   popLexer();
}
;

byte_string returns [ String s ]
{
	s = "";
}
: d:~RETURN 
{ 
   pushLexer("charLexer");
   while ( true ) {
	s += d.getText();
	d = LT(1);
	if (d == null || d.getType() == Token.EOF_TYPE) {
		throw new RecognitionException("Premature EOF");
	}	
	if (d.getType() == RETURN ) break;
	consume();
   }
   track(s);
   popLexer();
}
;

fpnum returns [ double f ]{
	String s = "";
	f = 1.0;
}
:a:DIGIT { s = a.getText(); } ( (DOT)=>  DOT { s += "."; } 
	 ( b:DIGIT { s += b.getText(); } )+  )? 
{
	f = Double.parseDouble(s);
}
;

//
// Return a string that includes the quotes.
//
quoted_string returns [ String qs ]
{
	qs = "";
}
: DOUBLEQUOTE  {
	pushLexer("charLexer");
	Token tok = null;
	while(true) {
		tok = LT(1);
		consume();
		if (tok == null) throw 
			new TokenStreamException("Invalid quoted string");
		if (tok.getType() == charLexerTokenTypes.BACKSLASH) {
			// Skip over escape sequences.
			qs += tok.getText();
			tok = LT(1); 
			consume();
			qs += tok.getText();
		} else if (tok.getType() == DOUBLEQUOTE) {
			break;
		} else {
			qs += tok.getText();
		}
	}
	// If tracking is enabled, then track the input string.
	track(qs + tok.getText());
	popLexer();
}
;
