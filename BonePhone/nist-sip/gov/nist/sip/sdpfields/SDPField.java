/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;

/**
* Placeholder root class for SDP headers.
*/

public abstract class SDPField extends SDPObject implements SDPFieldNames {
	protected String fieldName;

	public abstract String encode();

	protected SDPField( String hname ) {
		fieldName = hname;
	}

	public String getFieldName() { return fieldName; }

	public SDPField() {}

} 
