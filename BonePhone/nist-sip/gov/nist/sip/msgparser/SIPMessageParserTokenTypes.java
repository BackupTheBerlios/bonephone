/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;

/**
* A union of all the token types that the parser generates:
* a good way of checking whether the token types are consistent 
* (not re-defined in different parsers with different token values).
* We need to add some consistency checking code here!
*/
interface SIPMessageParserTokenTypes extends 
accept_languageLexerTokenTypes,  retry_afterLexerTokenTypes,
addr_parmsLexerTokenTypes,       sdpLexerTokenTypes,
authorization_LexerTokenTypes,   sdp_announceParserTokenTypes,
charLexerTokenTypes,             sip_messageParserTokenTypes,
command_keywordLexerTokenTypes,  sip_urlLexerTokenTypes,
contact_parmsLexerTokenTypes,    sip_urlParserTokenTypes,
dateLexerTokenTypes,             
hide_LexerTokenTypes,            skipLexerTokenTypes,
host_nameParserTokenTypes,       status_lineLexerTokenTypes,
key_fieldLexerTokenTypes,        timeLexerTokenTypes,
method_keywordLexerTokenTypes,   content_dispositionLexerTokenTypes,
parser_utilParserTokenTypes,     
pgp_LexerTokenTypes,             via_parmsLexerTokenTypes,
priority_LexerTokenTypes
{}
