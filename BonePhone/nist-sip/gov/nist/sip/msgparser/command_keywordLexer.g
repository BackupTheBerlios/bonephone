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
class command_keywordLexer extends Lexer;

options {
	charVocabulary= '\0'..'\377';
	importVocab= sdp_announceParser;
	caseSensitive= false;
	filter= IGNORE;
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

protected IGNORE :(SP|HT) ;

protected ID :( 'a'..'z' | '0'..'9' | UNDERSCORE | PLUS | MINUS | QUOTE | 
 EXCLAMATION | PERCENT |  BACK_QUOTE | DOT | TILDE  | STAR  )+;

HEADER_NAME_COLON :t:ID 
{
	/**
	* Initializes the token set for recognition of SIP headers.
	*/

	if (! keywordsInitialized) {
		keywordsInitialized = true;
		mySIPKeywords = new Hashtable();

		addKeyword ( ERROR_INFO_COLON, 
					SIPHeaderNames.ERROR_INFO	);
		addKeyword ( ALSO_COLON, 
					SIPHeaderNames.ALSO		);
		addKeyword ( IN_REPLY_TO_COLON, 
					SIPHeaderNames.IN_REPLY_TO	);
		addKeyword ( MIME_VERSION_COLON, 
					SIPHeaderNames.MIME_VERSION 	);
		addKeyword ( ALERT_INFO_COLON, 
					SIPHeaderNames.ALERT_INFO 	);
		addKeyword ( FROM_COLON, 
					SIPHeaderNames.FROM		);
		addKeyword ( TO_COLON, 
					SIPHeaderNames.TO 		);
		addKeyword ( VIA_COLON, 
					SIPHeaderNames.VIA		);
		addKeyword ( USER_AGENT_COLON, 
					SIPHeaderNames.USER_AGENT	);
		addKeyword ( SERVER_COLON, 
					SIPHeaderNames.SERVER		);
		addKeyword ( ACCEPT_ENCODING_COLON, 
					SIPHeaderNames.ACCEPT_ENCODING 	);
		addKeyword ( ACCEPT_COLON, 
					SIPHeaderNames.ACCEPT 		);
		addKeyword ( ALLOW_COLON, 
					SIPHeaderNames.ALLOW		);
		addKeyword ( ROUTE_COLON, 
					SIPHeaderNames.ROUTE 		);
		addKeyword ( AUTHORIZATION_COLON, 
					SIPHeaderNames.AUTHORIZATION	);
		addKeyword ( PROXY_AUTHORIZATION_COLON, 
					SIPHeaderNames.PROXY_AUTHORIZATION);
	        addKeyword ( RETRY_AFTER_COLON, 
					SIPHeaderNames.RETRY_AFTER 	);
		addKeyword ( PROXY_REQUIRE_COLON, 
					SIPHeaderNames.PROXY_REQUIRE 	);
		addKeyword ( CONTENT_LANGUAGE_COLON, 
					SIPHeaderNames.CONTENT_LANGUAGE );
		addKeyword ( UNSUPPORTED_COLON, 
					SIPHeaderNames.UNSUPPORTED  	);
		addKeyword ( SUPPORTED_COLON, 
					SIPHeaderNames.SUPPORTED  	);
		addKeyword ( WARNING_COLON, 
					SIPHeaderNames.WARNING 		);
		addKeyword ( MAX_FORWARDS_COLON, 
					SIPHeaderNames.MAX_FORWARDS 	);
		addKeyword ( DATE_COLON, 
					SIPHeaderNames.DATE  		);
		addKeyword ( PRIORITY_COLON, 
					SIPHeaderNames.PRIORITY 	);
		addKeyword ( PROXY_AUTHENTICATE_COLON,
		 			SIPHeaderNames.PROXY_AUTHENTICATE);
		addKeyword ( CONTENT_ENCODING_COLON, 
					SIPHeaderNames.CONTENT_ENCODING );
		addKeyword ( CONTENT_LENGTH_COLON, 
					SIPHeaderNames.CONTENT_LENGTH 	);
		addKeyword ( SUBJECT_COLON, 
					SIPHeaderNames.SUBJECT 		);
		addKeyword ( CONTENT_TYPE_COLON, 
					SIPHeaderNames.CONTENT_TYPE 	);
		addKeyword ( CONTACT_COLON, 
					SIPHeaderNames.CONTACT		);
		addKeyword ( CALL_ID_COLON, 
					SIPHeaderNames.CALL_ID 		);
		addKeyword ( REQUIRE_COLON, 
					SIPHeaderNames.REQUIRE 		);
		addKeyword ( EXPIRES_COLON, 
					SIPHeaderNames.EXPIRES 		);
		addKeyword ( ENCRYPTION_COLON, 
					SIPHeaderNames.ENCRYPTION 	);
		addKeyword ( RECORD_ROUTE_COLON, 
					SIPHeaderNames.RECORD_ROUTE	);
		addKeyword ( ORGANIZATION_COLON, 
					SIPHeaderNames.ORGANIZATION 	);
		addKeyword ( CSEQ_COLON, 
					SIPHeaderNames.CSEQ 		);
		addKeyword ( ACCEPT_LANGUAGE_COLON, 
					SIPHeaderNames.ACCEPT_LANGUAGE  );
		addKeyword ( WWW_AUTHENTICATE_COLON, 
					SIPHeaderNames.WWW_AUTHENTICATE );
		addKeyword ( RESPONSE_KEY_COLON, 
					SIPHeaderNames.RESPONSE_KEY	);
		addKeyword ( HIDE_COLON, 
					SIPHeaderNames.HIDE 		);
		addKeyword ( CALL_INFO_COLON, 
					SIPHeaderNames.CALL_INFO 	);
		addKeyword ( CONTENT_DISPOSITION_COLON, 
					SIPHeaderNames.CONTENT_DISPOSITION);
		// And now the dreaded short forms....
		addKeyword ( SUPPORTED_COLON, "k" );
		addKeyword ( CONTENT_TYPE_COLON, "c" );
		addKeyword ( CONTENT_ENCODING_COLON, "e" );
		addKeyword ( FROM_COLON, "f" );
		addKeyword ( CALL_ID_COLON, "i");
		addKeyword ( CONTACT_COLON, "m" );
		addKeyword ( CONTENT_LENGTH_COLON, "l");
		addKeyword ( SUBJECT_COLON, "s");
		addKeyword ( TO_COLON, "t");
		addKeyword ( VIA_COLON, "v" );

	}
	String toktext = t.getText();
	while (true ) {
		// Checking to see if the COLON exists or things have just 
		// Gone to the end of line.
		if (LA(1) == ' ' ) {
			toktext += LA(1);
			 mSP(false);
		} else if (LA(1) == '\t') { 
			toktext += LA(1);
			mHT(false);
		} else if (LA(1) == '\n'){
			 break;
		} else if (LA(1) == ':') {
			mCOLON(false);
			break;
		}
		else break;
	}
	if (LA(0) == '\n' ) {
		$setType(ID);
		$setText(toktext);
	} else if (LA(0) == ':' ) {
	   // Eat white spaces.
	   while (true ) {
		if (LA(1) == ' ' ) mSP(false);
		else if (LA(1) == '\t') mHT(false);
		else break;
	   }

	  // String ltext = t.getText();
	  String ntext = t.getText().toLowerCase();

	  SIPKeyword lit = (SIPKeyword) mySIPKeywords.get(ntext);
	   if (lit != null) {
		int tok = lit.tokenVal;
		$setType(tok);
		switch(tok) {
			case CONTENT_DISPOSITION_COLON:
				selectLexer("content_dispositionLexer");
				initLexer();
				break;

			case RETRY_AFTER_COLON:
				{ 
					try {
					   String lookahead = "" + LA(1);
					   int  day  = 
						Integer.parseInt(lookahead);
					   selectLexer("charLexer");
				        }  catch (ANTLRException ex) {
					    ex.fillInStackTrace();
					    selectLexer("charLexer");
					} catch (NumberFormatException ex) {
					   selectLexer("dateLexer");
					}
				}
				break;

			case EXPIRES_COLON:
			case DATE_COLON: 
				selectLexer("dateLexer");
				break;

			case PRIORITY_COLON: 
				selectLexer("priority_Lexer");
				break;


			case HIDE_COLON:
				selectLexer("hide_Lexer");
				break;

			case WWW_AUTHENTICATE_COLON :   
			case PROXY_AUTHENTICATE_COLON:
				selectLexer("authentication_Lexer");
				break;

			case AUTHORIZATION_COLON  : 
				selectLexer("authorization_Lexer");
				break;

			case ALLOW_COLON:
				selectLexer("method_keywordLexer");
				break;

			case CONTACT_COLON: 
				{ 
					try {
					   String lookahead = "" + LA(1);
					    if (lookahead.equals("*")) 
							selectLexer
							("contact_parmsLexer");
					    else
     						selectLexer
						("sip_urlLexer", INIT_STATE );
				        }  catch (ANTLRException ex) {
					    ex.fillInStackTrace();
					    selectLexer("charLexer");
					}
				}
				break;

			// A URI can immediately follow these.
			case ERROR_INFO_COLON:
			case ALSO_COLON:
			case ALERT_INFO_COLON:
			case CALL_INFO_COLON:
			case FROM_COLON:
			case TO_COLON:
     				selectLexer("sip_urlLexer", INIT_STATE );
				break;


			default:
				selectLexer("charLexer");


		}
	   } else selectLexer("charLexer");
	 } else { 
	    // Set it to something that will not match any header.
	    $setType(ID);
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
protected STAR :'*' 			;

// inherited from grammar charLexer
DOLLAR :'$'			;

// inherited from grammar charLexer
protected PLUS :'+'			;

// inherited from grammar charLexer
POUND :'#'			;

// inherited from grammar charLexer
protected MINUS :'-'			;

// inherited from grammar charLexer
DOUBLEQUOTE :'\"' 		;

// inherited from grammar charLexer
protected QUOTE :'\''			;

// inherited from grammar charLexer
protected TILDE :'~'			;

// inherited from grammar charLexer
protected BACK_QUOTE :'`' 		;

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
protected DOT :'.'       			;

// inherited from grammar charLexer
protected EXCLAMATION :'!'		;

// inherited from grammar charLexer
LPAREN :'('			;

// inherited from grammar charLexer
RPAREN :')'			;

// inherited from grammar charLexer
GREATER_THAN :'>'		;

// inherited from grammar charLexer
LESS_THAN :'<' 			;

// inherited from grammar charLexer
protected PERCENT :'%'   			;

// inherited from grammar charLexer
QUESTION :'?' 			;

// inherited from grammar charLexer
AND :'&'  			;

// inherited from grammar charLexer
protected UNDERSCORE :'_'  		;

// inherited from grammar charLexer
protected ALPHA :('a'..'z' ) 	;

// inherited from grammar charLexer
protected DIGIT :('0'..'9')		;

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


