/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) ,added JAVADOC                 *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import java.lang.reflect.*;

/**
*  Root class from which all SIPHeader objects are subclassed.
*@version 1.0
*@since 0.9
*/
public abstract class SIPHeader extends SIPObject implements SIPHeaderNames {
	
	/**
	*Message line where the SIPHeader originally was found (for errors).
	*/
	protected int lineNumber;
        
        /** name of this header
         */        
	protected String headerName;

	/** Value of the header.
	*/

        /** Constructor
         * @param hname String to set
         */        
	protected SIPHeader( String hname) {
		headerName = hname;
	}
        
        /** Default constructor
         */        
        public SIPHeader() {}

        /**
         * Name of the SIPHeader
         * @return String
         */
	public String getHeaderName() {
		return headerName;
	}	
        
	/**
         * Get the message body minus the trailing linefeed (if one exists).
         * @return String
         */
	public String getTrimmedInputText() {
		String inp = super.getInputText();
		// Check if we are recording the input text.
		if (inp == null) return null;
		// Get rid of the CRLF at the end.
		int len = inp.length();
		String retval;
		if (inp.charAt(len-1) == '\n' ) {
		  	retval = new String(inp.substring(0,len-1));
		} else retval = inp;
		return retval.trim();
	}

	/**
         * Message line where the SIPHeader originally was found (for errors).
         * @return int
         */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
         * Set the name of the header .
         * @param hdrname String to set
         */
	public void setHeaderName(String hdrname) {
		headerName = hdrname;
	}


	/** Get the header value (i.e. what follows the name:).
	* This merely goes through and lops off the portion that follows
	* the headerName:
	*/
	public String getHeaderValue() {
		String encodedHdr = null;
		try {
		   encodedHdr = this.encode();
		} catch (Exception ex) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(encodedHdr);
		while (buffer.length() > 0 && buffer.charAt(0) != ':') {
			buffer.deleteCharAt(0);
		}
		buffer.deleteCharAt(0);
		return buffer.toString().trim();
	}
		



}
