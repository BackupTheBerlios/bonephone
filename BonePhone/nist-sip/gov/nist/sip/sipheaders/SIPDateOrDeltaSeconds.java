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
* A placeholder class to please the type system so that Date can be 
* either SIPDate or DeltaSeconds.
*/
public abstract class SIPDateOrDeltaSeconds extends SIPObject {
    
        /** boolean function
         * @return true if Date is an instance of SIPDate, false otherwise
         */    
 	public boolean isSIPDate() {
		return this instanceof SIPDate;
	}
        
        /** boolean function
         * @return true if Date is an instance of DeltaSeconds, false
         * otherwise.
         */        
	public boolean isDeltaSeconds() {
		return this instanceof DeltaSeconds;
	}
			
}
