/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;
import gov.nist.sip.*;
/**
* Media Description SDP header
*/
public class Format  extends SDPObject {
	String format;
	
	public void setFormat(String fmt) { format = fmt; }

	public String getFormat() { return format; }

	public Format(String s) {
		format = s;
	}
	
	public Format() {}

	public String encode() { return format; }


}

	
