/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov)                                *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
*  Hide SIPObject
*/
public class Hide extends SIPHeader {
    
        /** hide field
         */    
	protected String hide;
        
        /** Default constructor
         */        
	public Hide() { 
            super(HIDE);
        }
        
        /** get the hide field
         * @return String
         */        
	public String getHide() {
            return hide;
        }
        
	/**
         * Set the hide member
         * @param h String to set
         */
	public void setHide(String h) {
            hide = h ;
        } 

        /** Generate canonical form of the header.
         * @return String
         */        
	public String encode() {
            return headerName + COLON + SP + hide + NEWLINE;
        }
        
}
