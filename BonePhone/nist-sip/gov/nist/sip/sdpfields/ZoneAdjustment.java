/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import  gov.nist.sip.*;

/**
* Zone adjustment class.
*@see gov.nist.sip.sdpfields.ZoneField
*/
public class ZoneAdjustment extends SDPObject {
	protected long time;
	protected String sign;
	protected TypedTime offset;

	/**
	* Set the time.
	*@param t time to set.
	*/
	public void setTime(long t) {
		time = t;
	}

	/**
	* Get the time.
	*/
	public long getTime() {
		return time;
	}

	/**
	* get the offset.
	*/
	public TypedTime getOffset() {
		return offset;
	}

	/**
	* Set the offset.
	*@param off typed time offset to set.
	*/
	public void setOffset(TypedTime off) {
		offset = off;
	}

	/**
	* Set the sign.
	*@param s sign for the offset.
	*/
	public void setSign(String s) {
		sign = s;
	}
		

	/**
	* Encode this structure into canonical form.
	*@return encoded form of the header.
	*/

	public String encode() {
		String retval = new Long(time).toString();
		retval += Separators.SP;
		if (sign != null) retval += sign;
		retval += offset.encode();
		return retval;
	}
	


}
