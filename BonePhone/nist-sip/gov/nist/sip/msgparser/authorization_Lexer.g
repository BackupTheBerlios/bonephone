header {
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
}
class authorization_Lexer extends Lexer;

options {
	k= 1;
	charVocabulary= '\0'..'\377';
	importVocab= sdp_announceParser;
	caseSensitive= false;
	caseSensitiveLiterals= false;
	filter= false;
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
EXCLAMATION | PERCENT |  BACK_QUOTE | DOT | TILDE  | STAR  )+
{
	/**
	* Initializes the token set for recognition of SIP headers.
	*/

	if (! keywordsInitialized) {
		keywordsInitialized = true;
		mySIPKeywords = new Hashtable();
		addKeyword ( BASIC, AuthorizationKeywords.BASIC );
		addKeyword ( DIGEST, AuthorizationKeywords.DIGEST );
		addKeyword ( PGP, AuthorizationKeywords.PGP );
	}
	String ltext = $getText;
	String ntext = ltext.toLowerCase();

	SIPKeyword lit = (SIPKeyword) mySIPKeywords.get(ntext);
	if (lit != null) {
		$setType(lit.tokenVal);
		if (lit.tokenVal == PGP)  selectLexer("pgp_Lexer");
		else if (lit.tokenVal == DIGEST) selectLexer("digest_Lexer");
		else if (lit.tokenVal == BASIC)  selectLexer("charLexer");
	} else selectLexer("charLexer");

}
;

AT :'@' 			;

SP :' '   			;

HT :'\t'			;

COLON :':'			;

protected STAR :'*' 			;

DOLLAR :'$'			;

protected PLUS :'+'			;

POUND :'#'			;

protected MINUS :'-'			;

DOUBLEQUOTE :'\"' 		;

protected QUOTE :'\''			;

protected TILDE :'~'			;

protected BACK_QUOTE :'`' 		;

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

protected DOT :'.'       			;

protected EXCLAMATION :'!'		;

LPAREN :'('			;

RPAREN :')'			;

GREATER_THAN :'>'		;

LESS_THAN :'<' 			;

protected PERCENT :'%'   			;

QUESTION :'?' 			;

AND :'&'  			;

protected UNDERSCORE :'_'  		;

protected ALPHA :  ('a'..'z')  	;

protected DIGIT :('0'..'9')		;

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


