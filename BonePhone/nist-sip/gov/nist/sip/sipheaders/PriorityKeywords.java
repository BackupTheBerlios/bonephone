/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                   
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/** Constant Keywords for te Priority class
 */
public interface PriorityKeywords {
    
        /** constant EMERGENCY field
         */    
	public static final String EMERGENCY="emergency";
        
        /** constant URGENT field
         */    
	public static final String URGENT="urgent";
        
        /** constant NORMAL field
         */    
	public static final String NORMAL="normal";
       
        /** constant NON_URGENT field
         */    
	public static final String NON_URGENT="non-urgent";
        
}
