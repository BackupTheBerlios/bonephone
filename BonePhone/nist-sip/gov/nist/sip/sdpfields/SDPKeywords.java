/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;

/**
* A context sensitive list of keyowrds for SDP Headers.
*/
public interface SDPKeywords {
	public static final String BASE64="base64";
	public static final String PROMPT="prompt";
	public static final String CLEAR = "clear";
	public static final String URI="URI";
	public static final String IPV4="IP4";
	public static final String IPV6="IP6";
	public static final String IN="IN";
}
