/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;

public class InformationField extends SDPField {
	protected String information;

	public InformationField() 
		{ super(INFORMATION_FIELD); }

	public String getInformation() 
		{ return information; }

	public void setInformation( String info ) 
		{ information = info; } 

    /**
     *  Get the string encoded version of this object
     * @since v1.0
     */
    public String encode() {
	return INFORMATION_FIELD + information + Separators.NEWLINE;
    }
}

