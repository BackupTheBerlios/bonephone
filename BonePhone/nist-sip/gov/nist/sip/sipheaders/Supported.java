/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) , added JAVADOC                *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
/**
* Supported SIP Header.
*
* 6.42 Supported
*
*   The Supported general-header field enumerates all the capabilities of
*   the client or server. This header field SHOULD be included in all
*   requests (except ACK) and in all responses.
*
*
*        Including the header field in all responses greatly
*        simplifies the use of extensions for call control in
*        subsequent transactions with the same server.
*
*   Syntax:
*
*
*       Supported  =  ( "Supported" | "k" ) ":" 1#option-tag
*
*@since 1.0
*@see SupportedList
*/

public class Supported extends SIPHeader {
	String optionTag;

        /** default constructor
         */
        public Supported() {
            super(SIPHeaderNames.SUPPORTED);
            optionTag = null;
        }
        
        /** Constructor
         * @param option_tag String to set
         */
        public Supported(String option_tag) {
            super(SIPHeaderNames.SUPPORTED);
            optionTag = option_tag;
        }
        
         /** Return canonical form of the header.
         * @return String
         */
        public String encode() {
            return headerName + COLON + SP + optionTag + NEWLINE;
        }
        
        /** get the option Tag field
         * @return OptionTag field
         */
        public String getOptionTag() { 
            return optionTag;
        }
        
        /** set the option Tag field
         * @param ot String to set
         */
        public void setOptionTag(String ot) { 
            optionTag = ot;
        }       
        
}
