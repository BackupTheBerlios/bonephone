/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.stack;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;

/**
*An interface which gets implemented by the application for whatever
*extensions it wants to handle.
*/

public interface ExtensionParser {
	/**
	*Parse the extension header and return a Header of 
	* the appropriate class.
	*/
	public SIPHeader parseExtensionHeader(String extension)
		throws SIPParseException ;
}
