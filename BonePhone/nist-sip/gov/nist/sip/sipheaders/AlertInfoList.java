/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                    
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* AlertInfo SIPHeader - there can be several AlertInfo headers.
*/
public class AlertInfoList extends SIPHeaderList {
    
        /** default constructor
         */    
	public AlertInfoList() {
		super("AlertInfo", SIPHEADERS_PACKAGE+ ".AlertInfo",ALERT_INFO);
	}
        
        /** add the specified parameter
         * @param al AlertInfo to set
         */        
	public void add(AlertInfo al) { 
            super.add(al);
        }
        
}
