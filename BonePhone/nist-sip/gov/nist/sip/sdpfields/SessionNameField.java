/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;

public class SessionNameField  extends SDPField {
	protected String sessionName;

	public SessionNameField() {
		super(SDPFieldNames.SESSION_NAME_FIELD);
	}
	public String getSessionName() { return sessionName; }
	/**
	* Set the sessionName member  
	*/
	public	 void setSessionName(String s) 
 	 	{ sessionName = s ; } 

    /**
     *  Get the string encoded version of this object
     * @since v1.0
     */
    public String encode() {
	return SESSION_NAME_FIELD + sessionName + Separators.NEWLINE;
    }
	
}
