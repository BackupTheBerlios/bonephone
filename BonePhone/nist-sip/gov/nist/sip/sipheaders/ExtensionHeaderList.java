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

public class ExtensionHeaderList extends SIPHeaderList { 

	public ExtensionHeaderList(String hName) {
		super(hName, getClassFromName(SIPHEADERS_PACKAGE + 
				".ExtensionHeader"), hName);
	}
	public ExtensionHeaderList() {
		this(null);
	}

        
        
}
