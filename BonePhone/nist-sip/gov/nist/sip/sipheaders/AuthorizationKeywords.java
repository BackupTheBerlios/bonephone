/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

public interface AuthorizationKeywords {
	public static final String USERNAME="username";
	public static final String URI="uri";
	public static final String DOMAIN="domain";
	public static final String CNONCE="cnonce";
	public static final String PASSWORD="password";
	public static final String RESPONSE="response";
	public static final String OPAQUE="opaque";
	public static final String ALGORITHM="algorithm";
	public static final String DIGEST="Digest";
	public static final String BASIC="Basic";
	public static final String PGP="pgp";
	public static final String SIGNED_BY="signed-by";
	public static final String SIGNATURE="signature";
	public static final String NONCE="nonce";
	public static final String PUBKEY="pubkey";
	public static final String COOKIE="cookie";
	public static final String REALM="realm";
	public static final String VERSION="version";
	public static final String STALE="stale";
	public static final String QOP="qop";
	public static final String NC="nc";
}
