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
class dateLexer extends Lexer;

options {
	charVocabulary= '\0'..'\377';
	importVocab= sdp_announceParser;
	caseSensitive= false;
	k= 1;
	filter=false;
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
ID :( ALPHA )+ {
	/**
	* Initialize the date lexer keywords.
	*/
	if (! keywordsInitialized) {
		keywordsInitialized = true;
		mySIPKeywords = new Hashtable();
		addKeyword(GMT, DateKeywords.GMT);
		addKeyword(MON, DateKeywords.MON);
		addKeyword(TUE, DateKeywords.TUE);
		addKeyword(WED, DateKeywords.WED);
		addKeyword(THU, DateKeywords.THU);
		addKeyword(FRI, DateKeywords.FRI);
		addKeyword(SAT, DateKeywords.SAT);
		addKeyword(SUN, DateKeywords.SUN);
		addKeyword(JAN, DateKeywords.JAN);	
		addKeyword(FEB, DateKeywords.FEB);
		addKeyword(MAR, DateKeywords.MAR);
		addKeyword(APR, DateKeywords.APR);
		addKeyword(MAY, DateKeywords.MAY);
		addKeyword(JUN, DateKeywords.JUN);
		addKeyword(JUL, DateKeywords.JUL);
		addKeyword(AUG, DateKeywords.AUG);
		addKeyword(SEP, DateKeywords.SEP);
		addKeyword(OCT, DateKeywords.OCT);
		addKeyword(NOV, DateKeywords.NOV);
		addKeyword(DEC, DateKeywords.DEC);
	}
	// retrieve the matched text.
	String ntext = $getText;
	String ttext = ntext.toLowerCase();
	SIPKeyword lit = (SIPKeyword) mySIPKeywords.get(ttext);
	if (lit != null) {
		$setType(lit.tokenVal);
	}
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

// inherited from grammar charLexer
QUOTE :'\''			;

// inherited from grammar charLexer
TILDE :'~'			;

// inherited from grammar charLexer
BACK_QUOTE :'`' 		;

// inherited from grammar charLexer
NULL :'\0'			;

// inherited from grammar charLexer
EQUALS :'='			;

// inherited from grammar charLexer
SEMICOLON :";"			;

// inherited from grammar charLexer
SLASH :'/'			;

// inherited from grammar charLexer
BACKSLASH :'\\' 		;

// inherited from grammar charLexer
L_SQUARE_BRACKET :'['		;

// inherited from grammar charLexer
R_SQUARE_BRACKET :']'		;

// inherited from grammar charLexer
R_CURLY :'}'			;

// inherited from grammar charLexer
L_CURLY :'{'			;

// inherited from grammar charLexer
HAT :'^'			;

// inherited from grammar charLexer
BAR :'|'			;

// inherited from grammar charLexer
DOT :'.'       			;

// inherited from grammar charLexer
EXCLAMATION :'!'		;

// inherited from grammar charLexer
LPAREN :'('			;

// inherited from grammar charLexer
RPAREN :')'			;

// inherited from grammar charLexer
GREATER_THAN :'>'		;

// inherited from grammar charLexer
LESS_THAN :'<' 			;

// inherited from grammar charLexer
PERCENT :'%'   			;

// inherited from grammar charLexer
QUESTION :'?' 			;

// inherited from grammar charLexer
AND :'&'  			;

// inherited from grammar charLexer
UNDERSCORE :'_'  		;

// inherited from grammar charLexer
protected
ALPHA :('a'..'z') 	;

// inherited from grammar charLexer
DIGIT :('0'..'9')		;

// inherited from grammar charLexer
RETURN :( '\n' )  { 
	newline(); 
	// Lexical analyzer to select after a lexer pop.
	parserMain.setEnclosingLexer(null);
	if (parserMain instanceof PipelinedMsgParser ) {
		// notify parser main to read a new line.
	        ((PipelinedMsgParser) parserMain).sendNotify();
	}
};

// inherited from grammar charLexer
COMMA :',' 			;


