/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

/**
* The MIME-VERSION sip header.
*/
public class MimeVersion extends SIPHeader {
    
        /** majorNumber field
         */    
	protected int majorNumber;
        
        /** minorNumber field.
         */        
	protected int minorNumber;

        /** Default constructor
         */        
	public MimeVersion() {
            super(MIME_VERSION) ;
        }

        /** get the majorNumber field.
         * @return int
         */                
	public int getMajorNumber() {
            return majorNumber;
        }

        /** get the minorNumber
         * @return int
         */                
	public int getMinorNumber() {
            return minorNumber;
        }

        /** set the majorNumber
         * @param major int to set
         */                
	public void setMajorNumber(int major) {
            majorNumber = major;
        }
	
        /** set the minorNumber.
         * @param minor int to set
         */                
	public void setMinorNumber(int minor) {
            minorNumber = minor;
        }

        /**
         * Return canonical form.
         * @return String
         */                  
	public String encode() {
            return headerName + COLON + SP + majorNumber + DOT +
				minorNumber + NEWLINE ;
        }
		
}
