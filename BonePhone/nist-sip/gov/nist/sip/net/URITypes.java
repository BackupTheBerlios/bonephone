/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;
/**
* Identifiers for the types of URIs
*/
public interface URITypes {
    /**
    * URI is an absolute URI.
    */
    public static final int	ABSOLUTE_URI = 1;
    /**
    * URI is a relative URI.
    */
    public static final int	RELATIVE_URI = 2;
    /**
    * URI is a SIP URL.
    */
    public static final int	SIP_URL	     = 3;
}
