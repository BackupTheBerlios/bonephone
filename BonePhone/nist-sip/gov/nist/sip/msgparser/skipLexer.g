
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
class skipLexer extends Lexer;
options {
	charVocabulary  = '\0'..'\377';
	importVocab = sdp_announceParser;
	caseSensitive = true;
	k = 1;
	filter=false;
}
{

    MsgParser parserMain;

     public void setParserMain ( MsgParser msgParser) {
		parserMain = msgParser;
     }

}


RETURN :   ( '\n' )  { 
	newline(); 
	// Lexical analyzer to select after a lexer pop.
	parserMain.setEnclosingLexer(null);
	if (parserMain instanceof PipelinedMsgParser ) {
		// notify parser main to read a new line.
	        ((PipelinedMsgParser) parserMain).sendNotify();
	}
};

ANYTHING: .
;
