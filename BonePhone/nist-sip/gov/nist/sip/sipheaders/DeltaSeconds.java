/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* SIP Dates can have either Date or DeltaSeconds as the parameter.
*/
public class DeltaSeconds extends SIPDateOrDeltaSeconds {
    
        /** deltaSeconds field.
         */    
	protected int deltaSeconds=-1;
        
        /** get the delatSeconds field.
         * @return long
         */        
        public long getDeltaSeconds () {
		return (long)deltaSeconds;
	}
	
        /**
         * Encode into a canonical string.
         * @return String.
         */        
	public String encode() {
		return new Integer(deltaSeconds).toString();
	}
        
        /**
         * Set the deltaSeconds member
         * @param d long to set.
         */
	public void setDeltaSeconds(long d) {
            deltaSeconds = (int)d ;
        }
	          
}
