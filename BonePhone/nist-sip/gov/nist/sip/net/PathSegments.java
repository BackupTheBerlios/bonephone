/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;
import gov.nist.sip.*;

/**
* Path segment component of an absolute path.
*/
public class PathSegments  extends NetObjectList {
    
        /** Default constructor
         */    
	public PathSegments() {
		super( "pathSegments", NET_PACKAGE + ".Segment");
		setSeparator(SLASH);
	}
        
}
