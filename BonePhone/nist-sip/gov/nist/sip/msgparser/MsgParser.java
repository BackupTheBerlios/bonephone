/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.*;
import java.util.Hashtable;
import java.lang.reflect.*;
import java.io.*;
import antlr.*;


/**
* This is the class from which two types of message parsers are 
* derived (StringMsgParser and PipelinedMsgParser). 
* The former is of parsing in-memory strings and the latter
* for parsing a stream of characters (as read from a TCP connection).
* This class is not exported outside this package so it is not public. 
* @since 0.9
* @version 1.0
* Revision List:
* 1. Added interface to arrest and release the line counter (used when
* 	manually looking ahead in the parser).
*/

abstract class MsgParser  implements PackageNames {
   protected boolean strict; // Implement strict checking of constraints.
   protected int currentLine;
   protected boolean lineCounterArrested;
   protected String currentHeader;
   protected int debugFlag;
   protected boolean trackInput; //  record text when rules are applied 
   protected boolean checkURI;   // Do URI checking in the parser 
   protected boolean parseMessageContent;
   protected boolean trapParseErrors; // abort parsing if parse error.
   protected String enclosingLexer; // Lexer to select after a pop.
   protected TokenStreamSelector selector;
   protected sip_urlParser      sip_urlparser;
   protected sip_messageParser  sip_messageparser;
   protected sdp_announceParser sdp_announceparser;
 
   protected boolean handleContinuations;
   protected int	contentLength;
   protected String    contentEncoding; // To read the content body.
  

   protected String eolLexer; // lexer to select at end of line.
   protected  String[] lexers;
   protected  int     tos ;
   protected  InputStream input_stream;
   protected  String current_lexer_name;
   private    String current_lexer_save;
   private    Hashtable lexerTable;
   private    int nlexers;
   private    LexerSharedInputState inputState;


   

   /** Dispable inputTracking.
    *This makes the parser run faster but the parser does not capture
    * the portions of the message that matched iput.
    */
    public void disableInputTracking() { 
	trackInput = false;
    }

    /** Enable strict checking of constraints.
    */
    public void setStrict() { strict = true; }


   /** Set the content encoding.
   */ 
   protected void setContentCoding(String encoding) {
	contentEncoding = encoding;
   }

   /**
   *  Add lexer to our selector and set up back pointer to ourselves.
   */
   private void addLexer( antlr.CharScanner lexer, String lexerName) {
	selector.addInputStream(lexer,lexerName);
	Class cl = lexer.getClass();
	try {
	   Class args[] = new Class[1];
	   args[0] = this.getClass();
	   Object objs[] = new Object[1];
	   objs[0] = this;
	   // This should work - I dont know why it does not (bug??)
	   // Method meth = cl.getDeclaredMethod("setParserMain",args);
	   Method methods [] = cl.getDeclaredMethods();
	   Method meth = null;
	   for( int i = 0; i < methods.length; i++) {
		  meth = methods[i];
		  if (meth.getName().compareTo("setParserMain") == 0) break;
	   }
	   Class parms[] = meth.getParameterTypes();
	   meth.invoke(lexer,objs);
	   lexerTable.put(lexerName, lexer);
	} catch (Exception ex) {
		InternalError.handleException(ex);
	}
   }

	
	
   /**
   *  Common initializatin code 
   *  but we cannot throw this in a constructor because
   *  the string parser needs to be re-initalized for each new string.
   *  To add a new lexer to this set, create it and add it via
   * addLexer above.
   */

   protected void initParser (InputStream input ) {
         tos = 0;
         lexers = new String[256];
         selector = new TokenStreamSelector();
	 checkURI = true;
	 inputState = new LexerSharedInputState(input);
	 eolLexer = "command_keywordLexer";
	 lexerTable = new Hashtable();
	 handleContinuations = true;
	 // This is a special case to skip over input.
	 
	 // skipLexer skiplexer = new skipLexer(inputState);
         // selector.addInputStream(skiplexer,"skipLexer");
	 // lexerTable.put("skipLexer",skiplexer);

   }

   /**
   * Set the lexical analyser to switch to on end of line.
   */
   protected void setEOLLexer( String lexer) {
	eolLexer = lexer;
   }

   protected void setHandleContinuations(boolean flag) {
		handleContinuations = flag;
   }

	
   /**
   * Switch to the starting lexer for this end of line.
   */

   protected void selectEOLLexer() {
	select(eolLexer);
   }

   /**
   * Debug printing code.
   */
   protected  void dbgprint( String s) {
	if (debugFlag == 1 ) System.out.println("> " + s);
   }

   protected void dbgprint(Exception ex) {
	if (debugFlag == 1 ) {
		System.err.println(ex.getMessage());
		ex.printStackTrace();
	}

   }

   /**
   * Push the current lexer on the stack and load a new lexer.
   */
   protected  void push(String lexername) {
	dbgprint("push " + lexername);
	initLexer(lexername);
	selector.push(lexername);
	lexers[tos] = lexername;
	if (tos == 0) {
	   current_lexer_save = current_lexer_name;
	}
	current_lexer_name = lexername;
	tos++;
   }

   /**
   * Pop the previous lexer off the stack.
   */
   protected  void pop() {
	dbgprint("pop");
	tos --;
	if (tos > 0) {
	   dbgprint("Current Lexer is " + lexers[tos - 1]);
	   current_lexer_name = lexers[tos - 1];
	} else {
	   current_lexer_name = current_lexer_save;
	}
	selector.pop();
    }

   /**
   * Initialize the currently selected lexer.
   */
   protected void initLexer() {
        antlr.CharScanner lexer = (antlr.CharScanner) 
			lexerTable.get(current_lexer_name);
	Class cl = lexer.getClass();
	try {
	   Method meth = cl.getMethod("initLexer",null);
	   meth.invoke(lexer,null);
	} catch (SecurityException ex) {
		InternalError.handleException(ex);	
	} catch (IllegalArgumentException ex) {
		InternalError.handleException(ex);	
	} catch (IllegalAccessException ex) {
		InternalError.handleException(ex);	
	} catch (InvocationTargetException ex) {
		InternalError.handleException(ex);	
	} catch (NoSuchMethodException ex) {
		System.out.println
			("could not find initLexer for " + current_lexer_name);
	}
   }

   /**
   * Set the state of a lexer. This uses a painful instrospection
   * to achieve this because antlr does not support true inheritence
   * and I want to avoid a huge switch statement.
   */
   protected void setLexerState(int state) {
        antlr.CharScanner lexer = (antlr.CharScanner) 
			lexerTable.get(current_lexer_name);
	Class cl = lexer.getClass();
	try {
	   Class args[] = new Class[1];
	   args[0] = Integer.TYPE;
	   Method meth = cl.getMethod("setState",args);
	   Object objs[] = new Object[1];
	   objs[0] = new Integer(state);
	   meth.invoke(lexer,objs);
	} catch (SecurityException ex) {
		InternalError.handleException(ex);	
	} catch (IllegalArgumentException ex) {
		InternalError.handleException(ex);	
	} catch (IllegalAccessException ex) {
		InternalError.handleException(ex);	
	} catch (InvocationTargetException ex) {
		InternalError.handleException(ex);	
	} catch (NoSuchMethodException ex) {
		System.out.println
		("could not find initLexer for " + current_lexer_name);
	}

   }

    private void initLexer(String lexerName) {

	 antlr.CharScanner lexer = (antlr.CharScanner) 
			lexerTable.get(lexerName); 
	 if (lexer == null) {
	  try {
	     Class lexerClass = Class.forName(PackageNames.MSGPARSER_PACKAGE +
			"." + lexerName);
	     Class[] parameterTypes = new Class[1];
	     parameterTypes[0] = inputState.getClass();
	     Constructor constructor =  
		lexerClass.getConstructor(parameterTypes);
	     Object[] args = new Object[1];
	     args[0] = inputState;
	     lexer = (antlr.CharScanner) constructor.newInstance(args);
	   } catch (Exception ex) {
		InternalError.handleException(ex);
	   }
	   // Add the lexers to our stream.
           addLexer(lexer,lexerName);
	 }
    }

  
    /**
    * Callback from the parser to indicate that the parser 
    * is done processing the message.
    */

    protected  abstract  void messageDone(SIPMessage sipMessage);

    protected  void select(String lexername) {
	dbgprint("Main.Select " + lexername);
	initLexer(lexername);
	selector.select(lexername);
	current_lexer_name = lexername;
    }

    protected  void select(String lexername, int state) {
	dbgprint("Main.Select " + lexername);
	initLexer(lexername);
	selector.select(lexername);
	current_lexer_name = lexername;
	setLexerState(state);
    }

    protected antlr.CharScanner getCurrentLexer() {
	return (antlr.CharScanner) lexerTable.get(current_lexer_name);
    }

    protected String getCurrentLexerName() {
	 return current_lexer_name;
    }

    protected void setDebugFlag () {
	 debugFlag = 1;
	 trapParseErrors = true;
	 Debug.debug = true;
    }

    /**
    * Enable parsing of SDP message contents.
    */
    public void parseContent() {
		parseMessageContent = true;
    }

    /**
    * Enable trapping of parse Errors. Default behavior is to ignore them.
    */

    protected void setTrapParseErrors() {
	trapParseErrors = true;
    }
    /**
    * Enable/disable the checking or request URIs 
    */
    public void setCheckURI(boolean c) 
 	 	{ checkURI = c ; } 

    /**
    * Silently ignore parse errors (default action).
    */
    protected void  handleParseException(SIPParseException ex) 
	throws SIPParseException {
	if (trapParseErrors) {
		throw ex;
	}
	// Just silently reject the error object.
	ex.rejectErrorObject();
    }

    /**
    * Increment the line number counter.
    */
    protected void newLine() {
	if (! lineCounterArrested ) currentLine ++;
	// Select the registered lexer for the beginning of the next line.
	select(eolLexer);
    }

    /**
    * Arrest the line counter (we do this when we are looking ahead 
    * with semantic lookahead.
    */
    protected void arrestLineCounter() {
	 lineCounterArrested = true;
    }

    /**
    * Allow the line counter to progress (we do this after we are looking ahead 
    * with semantic lookahead).
    */

    protected void releaseLineCounter() {
	 lineCounterArrested = false;
    }


   protected void resetLineCounter() {
	 currentLine = 0;
   }

   /**
   * Get the current line being parsed.
   */
   protected String getCurrentHeader() {
	return currentHeader;
   }

   /**
   * Get the current line number.
   */
   protected int getCurrentLineNumber() {
		return currentLine;
   }
   

   /**
   * Set the lexer for the enclosing scope (that is selected on a pop lexer from the parser).
   */
   protected void setEnclosingLexer( String lexerName) {
	enclosingLexer = lexerName;

   }

   protected void setContentLength(int length) {
	contentLength = length;
   }
   


}
