/**************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).    *
* See ../../../../doc/uncopyright.html for conditions of use.              *
* Author: M. Ranganathan (mranga@nist.gov)                                 *
* Questions/Comments: nist-sip-dev@antd.nist.gov                           *
***************************************************************************/
package gov.nist.sip.sdpfields;

public class TypedTime extends SDPObject {
	String unit;
	int    time;

	public String encode() {
		String retval = "";
		retval += new Integer(time).toString();
		if (unit != null) retval += unit;
		return retval;
	}
	
	public void setTime( int t ) {
		time = t;
	}

	public int getTime() {
		return time;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String u) {
		unit = u;
	}

}
