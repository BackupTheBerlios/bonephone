/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Modified By:  O. Deruelle (deruelle@nist.gov) ,added JAVADOC                 * 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

/**
* Canonical names for SIP Methods.
*/

public interface SIPRequestTypes {
    
        /** constant REGISTER field
         */    
	public static final String REGISTER="REGISTER";
        
        /** constant ACK field
         */        
	public static final String ACK="ACK";
        
        /** constant OPTIONS field
         */        
	public static final String OPTIONS="OPTIONS";
        
        /** constant BYE field
         */        
	public static final String BYE="BYE";
        
        /** constant INVITE field
         */        
	public static final String INVITE="INVITE";
        
        /** constant CANCEL field
         */        
	public static final String CANCEL="CANCEL";
        
}
