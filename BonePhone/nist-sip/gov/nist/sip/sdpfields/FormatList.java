
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
public class FormatList  extends SDPObjectList {
	public void add(Format fmt) { super.add(fmt); }

	public String encode() {
		String retval = null;
		for (Format f = (Format) this.first();
			f != null; f = (Format) this.next() ) {
		     if (retval == null) retval = f.encode();
		     else retval += Separators.SP + f.encode();
		}
		return retval;
	}
}
