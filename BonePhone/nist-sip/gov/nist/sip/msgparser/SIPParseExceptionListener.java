/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
/**
* A listener interface that enables customization of parse error handling.
* An class that implements this interface is registered with the 
* parser and is called back from the parser handle parse errors.
*/

public interface SIPParseExceptionListener {
	/**
	* This gets called from the parser when a parse error is generated.
	* The handler is supposed to introspect on the error class and 
	* header name to handle the error appropriately. The error can
	* be handled by :
	* 1. Re-throwing an exception and aborting the parse.
	* 2. Ignoring the header (setting it to null).
	* 3. Re-Parsing the bad header by fetching it from the 
	*  exception header.
	*/
	public void handleException(SIPParseException ex ) 
			throws SIPParseException;
}
