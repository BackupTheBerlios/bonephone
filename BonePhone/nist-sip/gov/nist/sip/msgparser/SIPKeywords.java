/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;

/**
* A context sensitve list of keywords, tokens  and separators 
* that are used by the  lexical analyzers and the classes that implement 
* the SIP headers and messages.
*/
public interface SIPKeywords extends 
	SIPHeaderNames, DateKeywords, AuthorizationKeywords,
	ViaKeywords, PriorityKeywords , SIPRequestTypes , 
	ContactKeywords, RetryAfterKeywords, CallInfoKeywords,
	URIKeywords, HideKeywords, ContentDispositionKeywords ,
	AddressKeywords, SIPVersion, Separators {}
	
