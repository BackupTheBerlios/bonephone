/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;
import gov.nist.sip.net.*;

public class URIField extends SDPField {
	protected URI uri;

	public URIField() { super(URI_FIELD) ; }
	public URI getURI() { return uri; }
	public void setURI(URI u) {uri = u; }

    /**
     *  Get the string encoded version of this object
     * @since v1.0
     */
    public String encode() {
	return URI_FIELD + uri.encode() + Separators.NEWLINE;
    }
	
}
