/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;

import gov.nist.sip.*;
import java.util.LinkedList;
import java.util.ListIterator;
/**
* Repeat SDP Field (part of the time field).
*/
public final class RepeatField  extends SDPField {
	protected TypedTime repeatInterval;
	protected TypedTime activeDuration;
	protected SDPObjectList offsets;

	public RepeatField() { 
		super(REPEAT_FIELD); 
	        offsets = new SDPObjectList();
	}

	
	public void setRepeatInterval( TypedTime interval) 
	{ repeatInterval = interval; }


	public TypedTime getRepeatInterval() 
	{ return repeatInterval; }

	public void setActiveDuration( TypedTime duration)  
	{ activeDuration = duration; }


	public TypedTime getActiveDuration() 
	{ return activeDuration; }


	public void addOffset(TypedTime offset) {
	   offsets.add(offset);
	}

	
	public LinkedList getOffsets() 
	{ return offsets; }

	public String encode() {
		String retval =  REPEAT_FIELD + repeatInterval.encode() 
			+ Separators.SP +
			activeDuration.encode();
		ListIterator li = offsets.listIterator();
		while (li.hasNext()) {
		   TypedTime off = (TypedTime) li.next();
		   retval += Separators.SP + off.encode();
		}
		retval += Separators.NEWLINE;
		return retval;
	}


}
