/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;

public class ProtoVersionField extends SDPField {
	protected int protoVersion;
	
	public ProtoVersionField() {
		super(PROTO_VERSION_FIELD); 
	}

	public int getProtoVersion() {
		return protoVersion;
	}

	/**
	* Set the protoVersion member  
	*/
	public void setProtoVersion( int pv ) {
		protoVersion = pv;
	}

    /**
     *  Get the string encoded version of this object
     * @since v1.0
     */
    public String encode() {
	return PROTO_VERSION_FIELD + protoVersion + Separators.NEWLINE;
    }
	
}
