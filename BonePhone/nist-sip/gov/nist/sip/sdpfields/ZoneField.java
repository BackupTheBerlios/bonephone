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
* Z= SDP field.
*/

public final class ZoneField  extends SDPField {

	protected SDPObjectList zoneAdjustments;

	/**
	* Constructor.
	*/
	public ZoneField() {
		super(ZONE_FIELD);
		zoneAdjustments = new SDPObjectList();
	}

	/**
	* Add an element to the zone adjustment list.
	*@param za zone adjustment to add.
	*/
	public void addZoneAdjustment( ZoneAdjustment za ) {
		zoneAdjustments.add(za);
	}


	/**
	* Get the zone adjustment list.
	*@return the list of zone adjustments.
	*/

	public SDPObjectList getZoneAdjustments() {
		return zoneAdjustments;
	}


	/**
	* Encode this structure into a canonical form.
	*/
	public String encode() {
		String retval = ZONE_FIELD;
		ListIterator li = zoneAdjustments.listIterator();
	 	int num = 0;
		while(li.hasNext()) {
		   ZoneAdjustment za = (ZoneAdjustment)li.next();
		   if (num > 0) retval += Separators.SP;
		   retval += za.encode();
		}
		retval += Separators.NEWLINE;
		return retval;
	}

	


}

