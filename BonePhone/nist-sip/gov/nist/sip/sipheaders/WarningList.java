/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modifications by: O. Deruelle (deruelle@antd.nist.gov added JAVADOC)         *  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
 * A Warning SIPObject. (A list of WarningValue headers).
 */
public class WarningList extends SIPHeaderList {
    
        /** Constructor.
         *
         */    
    public WarningList() {
        super("Warning", 
        SIPHEADERS_PACKAGE+".Warning", WARNING);
    }
    
}

