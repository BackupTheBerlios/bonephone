/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan						       *
* (mranga@nist.gov)  Created on April 18, 2001, 2:26 PM 		       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.message;
import  jain.protocol.ip.sip.message.*;
import  gov.nist.sip.msgparser.*;

/**
* Get and set the embedded SIPMessages (we construct the JAIN wrapper objects
* by embedding corresponding SIPMessage structures).
*/
public interface NistJAINMessage  {
	/**
	* Sets the implementation object (i.e. embedded SIPMessage structure)
	*/
	public void setImplementationObject(SIPMessage sipMessage);
	/**
	* Gets the implementation object (i.e. embedded SIPMessage structure).
	*/
	public SIPMessage getImplementationObject();
}
