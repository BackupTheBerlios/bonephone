header {
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
}
class accept_languageLexer extends Lexer;

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
Q :'q'
;

// inherited from grammar charLexer
AT :'@' 			;

// inherited from grammar charLexer
SP :' '   			;

// inherited from grammar charLexer
HT :'\t'			;

// inherited from grammar charLexer
COLON :':'			;

// inherited from grammar charLexer
STAR :'*' 			;

// inherited from grammar charLexer
DOLLAR :'$'			;

// inherited from grammar charLexer
PLUS :'+'			;

// inherited from grammar charLexer
POUND :'#'			;

// inherited from grammar charLexer
MINUS :'-'			;

// inherited from grammar charLexer
DOUBLEQUOTE :'\"' 		;

QUOTE :'\''			;

TILDE :'~'			;

BACK_QUOTE :'`' 		;

NULL :'\0'			;

EQUALS :'='			
{ selectLexer("charLexer"); }
;

SEMICOLON :";"			;

SLASH :'/'			;

BACKSLASH :'\\' 		;

L_SQUARE_BRACKET :'['		;

R_SQUARE_BRACKET :']'		;

R_CURLY :'}'			;

L_CURLY :'{'			;

HAT :'^'			;

BAR :'|'			;

DOT :'.'       			;

EXCLAMATION :'!'		;

LPAREN :'('			;

RPAREN :')'			;

GREATER_THAN :'>'		;

LESS_THAN :'<' 			;

PERCENT :'%'   			;

QUESTION :'?' 			;

AND :'&'  			;

UNDERSCORE :'_'  		;

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

