/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) ,added JAVADOC                 *                                                                                    
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* A list of Route Headers.
*@since 0.9
*@version 1.0
*Revisions:
* 1. Added a new Class called Route for uniformity.
*/
public class RouteList extends SIPHeaderList {
        
        
        /** default constructor
         */    
	public RouteList () {
		super("Route",SIPHEADERS_PACKAGE+ ".Route" , ROUTE);
	}
        
        /** Constructor
         * @param sip SIPObjectList to set
         */    
	public RouteList (SIPObjectList sip) {
		super(sip, ROUTE);
	}
        
}
