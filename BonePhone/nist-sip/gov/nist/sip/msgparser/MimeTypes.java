/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
/**
* A list of mime types that can be present in the content body and that 
* will be parsed by the parser and returned as imbedded structure (to grow
* with time). TODO: add itnerfaces to the parser to make this a configuration
* parameter. For now this is only referenced in this package (until we have
* a more general mechanism figured out).
*/

interface  MimeTypes {
	static final String application_sdp = "application/sdp";
}
