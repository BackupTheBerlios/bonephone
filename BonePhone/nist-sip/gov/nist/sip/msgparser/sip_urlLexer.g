header {
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
}
class sip_urlLexer extends Lexer;

options {
	k= 1;
	importVocab= sdp_announceParser;
	filter= false;
	caseSensitive= false;
	caseSensitiveLiterals= false;
	charVocabulary= '\0'..'\377';
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
COLON :':' 
{ 
	// Already seen the SIP keyword so SIP is not a keyword any longer
	if (lexerState == 0) selectLexer("charLexer");
}
;

EQUALS :'='
{ 
	// State 3 means we are on the RHS of a parameter.
	lexerState = PARMS_RHS_STATE; 
}
;

SLASH :'/'
{
	selectLexer("charLexer");
}
;

QUESTION :'?'
{
	selectLexer("charLexer");
}
;

ID :('a'..'z' | '0'..'9' | 
  UNDERSCORE | PLUS | MINUS | QUOTE | 
EXCLAMATION | PERCENT |  BACK_QUOTE | DOT | TILDE  | STAR  )+  
{
	// Stat 0 is when we are at the start of the url.
	// State 1 is when we are not at the parameters section.
	// State 2 is when we are at the parameter section.
	// State 3 is when we are thr RHS of the parameters.
	if (! keywordsInitialized) {
		keywordsInitialized = true;
		mySIPKeywords = new Hashtable()			   ;
		addKeyword ( SIP, SIPKeywords.SIP,  INIT_STATE);
                addKeyword(TRANSPORT,SIPKeywords.TRANSPORT,PARMS_LHS_STATE);
                addKeyword(METHOD, SIPKeywords.METHOD, PARMS_LHS_STATE);
                addKeyword(TTL, SIPKeywords.TTL, PARMS_LHS_STATE);
                addKeyword(MADDR, SIPKeywords.MADDR, PARMS_LHS_STATE);
                addKeyword(DURATION, SIPKeywords.DURATION, PARMS_LHS_STATE);
                addKeyword(USER, SIPKeywords.USER, PARMS_LHS_STATE);      
	}
	String ttext = $getText;
	String ntext = ttext.toLowerCase();
	SIPKeyword lit = (SIPKeyword) mySIPKeywords.get(ntext);
	if (lit != null && lexerState == lit.lexerContext ) {
		int tok = lit.tokenVal;
		$setType(tok);
	} 
		
}
;

AT :'@' 			;

SP :' '   			;

HT :'\t'			;

protected
STAR :'*' 			;

DOLLAR :'$'			;

protected
PLUS :'+'			;

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

SEMICOLON :";"			;

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

AND :'&'  			;

protected
UNDERSCORE :'_'  		;


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
