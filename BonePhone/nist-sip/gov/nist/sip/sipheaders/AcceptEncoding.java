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
* Accept-Encoding SIP (HTTP) Header.
*
* <pre>
* From HTTP RFC 2616
*
* 
*   The Accept-Encoding request-header field is similar to Accept, but
*   restricts the content-codings (section 3.5) that are acceptable in
*   the response.
*
* 
*       Accept-Encoding  = "Accept-Encoding" ":"
* 
* 
*                          1#( codings [ ";" "q" "=" qvalue ] )
*       codings          = ( content-coding | "*" )
* 
*   Examples of its use are:
* 
*       Accept-Encoding: compress, gzip
*       Accept-Encoding:
*       Accept-Encoding: *
*       Accept-Encoding: compress;q=0.5, gzip;q=1.0
*       Accept-Encoding: gzip;q=1.0, identity; q=0.5, *;q=0
* </pre>
* 
*/
public class AcceptEncoding extends SIPHeader {
    
	private boolean qValueIsSet;

        /** qvalue field
         */        
        protected double qvalue;
	
        /** contentEncoding field
         */        
        protected String contentCoding;
	
        /** default constructor
         */        
        public AcceptEncoding() {
		super(ACCEPT_ENCODING);
		qvalue = 1.0;
		qValueIsSet = false;
	}
	
        /**
         * Encode the header in canonical form.
         * @return encoded header.
         */
	public String encode() {
		String encoding =  headerName + COLON;
		if (contentCoding != null) {
		   encoding  += SP + contentCoding;
		 }
		if (qValueIsSet) {
			encoding += SEMICOLON + "q" + EQUALS + qvalue;
		}
		encoding += NEWLINE;
		return encoding;
	}        

	/** Encode the value of this header.
	*@return the value of this header encoded into a string.
	*/
	public String encodeBody() {
		String encoding = "";
		if (contentCoding != null) {
		   encoding  += SP + contentCoding;
		 }
		if (qValueIsSet) {
			encoding += SEMICOLON + "q" + EQUALS + qvalue;
		}
		return encoding;
	}        
        
        /** get QValue field
         * @return float
         */        
        public float getQvalue() { 
            return (float) qvalue;
        }
	
        /** get ContentEncoding field
         * @return String
         */        
        public String getContentCoding() { 
            return contentCoding;
        }
        
	/**
         * Set the qvalue member
         * @param q double to set
         */
	public	 void setQvalue(double q) { 
            qvalue = q ;
            qValueIsSet = true;
        }
        
	/**
         * Set the contentCoding member
         * @param c String to set
         */
	public	 void setContentCoding(String c) {
            contentCoding = c ;
        }
        
}
