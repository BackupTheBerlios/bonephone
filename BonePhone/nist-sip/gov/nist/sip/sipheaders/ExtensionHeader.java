/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.msgparser.*;

/**
* A generic extension header for the stack. 
* The input text of the header just gets recorded here.
* The MessageHandler builds a list of unrecongized extensions that can be processed 
* by the application logic.
*/

public class ExtensionHeader extends SIPHeader { 

	protected String value;

	public ExtensionHeader(String headerName) {
		super(headerName);
	}


	/** Set the name of the header.
	*@param headerName is the name of the header to set.
	*/

	public void setName(String headerName) {
		this.headerName = headerName;
	}

	/** Set the value of the header.
	*/
	public void setValue(String value) {
		this.value = value;
	}

	/** Get the value of the extension header.
	*@return the value of the extension header.
	*/
	public String getHeaderValue() { return this.value; }

	public String encode() {
		return this.headerName + COLON + this.value;
	}
        
        
}
