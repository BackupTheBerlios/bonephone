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
class parm_keywordLexer extends charLexer;
options {
	k = 1;
	charVocabulary  = '\0'..'\377';
	importVocab = sdp_announceParser;
	caseSensitive = false;
	caseSensitiveLiterals = false;
	filter = false;
}


ID : ( 'a'..'z' | '0'..'9' | UNDERSCORE | PLUS | MINUS | QUOTE | 
EXCLAMATION | PERCENT |  BACK_QUOTE | DOT | TILDE  | STAR  )+
{
	if (! keywordsInitialized) {
		keywordsInitialized = true;
		mySIPKeywords = new Hashtable();
		addKeyword(TRANSPORT,SIPKeywords.TRANSPORT) 	;
		addKeyword(METHOD, SIPKeywords.METHOD )		; 
		addKeyword(TTL,SIPKeywords.TTL);  			;
		addKeyword(USER, SIPKeywords.USER)      		;
		addKeyword(BRANCH,SIPKeywords.BRANCH)   		;
		addKeyword(MADDR,SIPKeywords.MADDR)  		;
		addKeyword(REALM,SIPKeywords.REALM) 		;
		addKeyword(DURATION,SIPKeywords.DURATION) 		;
	}
	String ltext = $getText;
	String ntext = ltext.toLowerCase();

	SIPKeyword sipKeyword = (SIPKeyword)mySIPKeywords.get(ntext);
	if (sipKeyword != null) {
		int tokenval = sipKeyword.tokenVal;
		$setType(tokenval);
		if ( tokenval == REALM) selectLexer("charLexer");
	}  
}
;


