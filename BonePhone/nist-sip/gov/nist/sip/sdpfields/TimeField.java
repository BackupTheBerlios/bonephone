/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;
/**
* TimeField SDP header 
*/
public final class TimeField  extends SDPField {
	protected long startTime;
	protected long stopTime;
	protected RepeatFieldList repeatList;
	public TimeField() {
		super(TIME_FIELD);
		repeatList = new RepeatFieldList();
	}
	public	 long getStartTime() 
 	 	{ return startTime ; } 
	public	 long getStopTime() 
 	 	{ return stopTime ; } 
	/**
	* Set the startTime member  
	*/
	public	 void setStartTime(long s) 
 	 	{ startTime = s ; } 
	/**
	* Set the stopTime member  
	*/
	public	 void setStopTime(long s) 
 	 	{ stopTime = s ; } 
	/**
	* Get the repeat fields.
	*/
	public RepeatFieldList getRepeatList() {
	    return repeatList;
	}

	
	/**
	* Set the repeat fields list.
	*/
	public void setRepeatList(RepeatFieldList rlist) {
		repeatList = rlist;
	}

	
	/**
	* Add to the repeat fileds list.
	*/
	public void addRepeatField(RepeatField repeat) {
		repeatList.add(repeat);
	}


    /**
     *  Get the string encoded version of this object
     * @since v1.0
     */
    public String encode() {
	String retval = "";
	retval += TIME_FIELD + startTime + Separators.SP + stopTime + 
		Separators.NEWLINE;
	if (repeatList != null) retval += repeatList.encode();
	return retval;
    }
	
}
