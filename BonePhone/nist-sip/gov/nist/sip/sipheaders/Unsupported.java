/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) , added JAVADOC                *  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/** The unsupported response-header field lists
 * the features not supported by the  server
 */
public class Unsupported extends SIPHeader {

        /** option-Tag field.
         */
    protected String  optionTag;
    
        /** Default Constructor.
         */
    public Unsupported() {
        super(UNSUPPORTED);
    }
    
        /** Constructor
         * @param ot String to set
         */
    public Unsupported(String ot) {
        super(UNSUPPORTED);
        optionTag = ot;
    }
    
        /**
         * Return a canonical value.
         * @return String.
         */
    public String encode() {
        return headerName + COLON + SP + optionTag+ NEWLINE;
    }
    
        /** get the option tag field
         * @return option Tag field
         */
    public String getOptionTag() {
        return optionTag ;
    }
    
        /**
         * Set the option member
         * @param o String to set
         */
    public void setOptionTag(String o) {
        optionTag = o ;
    }
    
}

