header {
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
}
class contact_parmsLexer extends Lexer;

options {
	charVocabulary= '\0'..'\377';
	importVocab= sdp_announceParser;
	caseSensitive= false;
	caseSensitiveLiterals= false;
	filter= false;
	k= 1;
}

{

MsgParser parserMain;
boolean keywordsInitialized;
private int	lexerState;
Hashtable mySIPKeywords;
	// Lexer states 
	protected  final static int INIT_STATE         =  0;
	protected  final static int PARMS_LHS_STATE    =  1;
	protected  final static int PARMS_RHS_STATE    =  2;
	protected  final static int KEYWORD_SEEN_STATE =  3;

      private void addKeyword(int tokenval, String token) {
		String token_string = token.toLowerCase();
		SIPKeyword kwd = new SIPKeyword(tokenval, token_string);
		mySIPKeywords.put(token_string,kwd);
      }
      private void addKeyword(int tokenval, String token, int context) {
		String token_string = token.toLowerCase();
		SIPKeyword kwd = 
			new SIPKeyword(tokenval, token_string, context);
		mySIPKeywords.put(token_string,kwd);
      }


      protected void selectLexer(String lexername) {
		parserMain.select(lexername);
      }

      protected void selectLexer(String lexername, int state ) {
		parserMain.select(lexername, state);
      }
	

     public void setParserMain ( MsgParser msgParser) {
		parserMain = msgParser;
     }

     public void initLexer() { 
	lexerState = INIT_STATE; 
     }
     
     public void setState( int state) {
		lexerState = state;
     }
     
}

ID :( ALPHA | DIGIT | UNDERSCORE | PLUS | MINUS | QUOTE | 
EXCLAMATION | PERCENT |  BACK_QUOTE | DOT | TILDE | STAR   )+ {
	/**
	* keywords for contact parameters in the contact header.
	* This defines the token set when we are parsing the contact header.
	*/
	if (! keywordsInitialized) {
		keywordsInitialized = true;
		mySIPKeywords = new Hashtable();
		addKeyword (Q, SIPKeywords.Q); 
		addKeyword (PROXY, SIPKeywords.PROXY);
		addKeyword (REDIRECT, SIPKeywords.REDIRECT);
		addKeyword (EXPIRES, SIPKeywords.EXPIRES) ;
		addKeyword (ACTION, SIPKeywords.ACTION);
		addKeyword (STAR_FLAG, SIPKeywords.STAR);
	}
	String ltext = $getText;
	String ntext = ltext.toLowerCase();

	SIPKeyword sipKeyword = (SIPKeyword)mySIPKeywords.get(ntext);
	if (sipKeyword != null) {
		int tokenval = sipKeyword.tokenVal;
		$setType(tokenval);
		if (tokenval == EXPIRES) {
		   selectLexer("dateLexer");
		}
	} else selectLexer("charLexer");
}
;

// inherited from grammar charLexer
AT :'@' 			;

// inherited from grammar charLexer
SP :' '   			;

// inherited from grammar charLexer
HT :'\t'			;

// inherited from grammar charLexer
COLON :':'			;

protected
STAR :'*' 			;

DOLLAR :'$'			;

protected
PLUS :'+'			;

// inherited from grammar charLexer
POUND :'#'			;

protected
MINUS :'-'			;

DOUBLEQUOTE :'\"' 		;

protected
QUOTE :'\''			;

protected
TILDE :'~'			;

protected
BACK_QUOTE :'`' 		;

NULL :'\0'			;

EQUALS :'='			;

SEMICOLON :";"			;

SLASH :'/'			;

BACKSLASH :'\\' 		;

L_SQUARE_BRACKET :'['		;

R_SQUARE_BRACKET :']'		;

R_CURLY :'}'			;

L_CURLY :'{'			;

HAT :'^'			;

BAR :'|'			;

protected
DOT :'.'       			;

protected
EXCLAMATION :'!'		;

LPAREN :'('			;

RPAREN :')'			;

GREATER_THAN :'>'		;

LESS_THAN :'<' 			;

protected
PERCENT :'%'   			;

QUESTION :'?' 			;

AND :'&'  			;

protected
UNDERSCORE :'_'  		;

protected ALPHA :('a'..'z') 	;

protected
DIGIT :('0'..'9')		;

RETURN :( '\n' )  { 
	newline(); 
	// Lexical analyzer to select after a lexer pop.
	parserMain.setEnclosingLexer(null);
	if (parserMain instanceof PipelinedMsgParser ) {
		// notify parser main to read a new line.
	        ((PipelinedMsgParser) parserMain).sendNotify();
	}
};

COMMA :',' 			;


