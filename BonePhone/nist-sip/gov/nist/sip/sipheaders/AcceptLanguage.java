/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By: Christophe Chazeau                                              *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* Accept Language body. 
* <pre>
*
* Accept-Language = "Accept-Language" ":"
*                         1#( language-range [ ";" "q" "=" qvalue ] )
*       language-range  = ( ( 1*8ALPHA *( "-" 1*8ALPHA ) ) | "*" )  
*
* HTTP RFC 2616 Section 14.4
* </pre>
*
* @see AcceptLanguageList
*/
public class AcceptLanguage extends SIPHeader {
    
	private boolean qValueIsSet;

        /** languageRange field
         */        
        protected String languageRange;
	
        /** qValue field
         */        
        protected double qValue;

        /** default constructor
         */        
	public AcceptLanguage() {
	    super(ACCEPT_LANGUAGE);
	    qValue = -1;
	    qValueIsSet = false;	
	}

        /**
         * Encode the header into a string
         * @return encoded header as a String.
         */
	public String encode() {
		String encoding = headerName + COLON;
		if (languageRange != null ) {
			encoding += SP + languageRange;
		}
		if (qValueIsSet) {
			encoding += SEMICOLON + "q" + EQUALS + qValue;
		}
		encoding += NEWLINE;
		return encoding;
	}

	/** Encode the value of this header to a string.
	*@return  encoded header as a string.
	*/
	public String encodeBody() {
		String encoding = "";
		if (languageRange != null ) {
			encoding += SP + languageRange;
		}
		if (qValueIsSet) {
			encoding += SEMICOLON + "q" + EQUALS + qValue;
		}
		return encoding;
	}
        
        /** get the LanguageRange field
         * @return String
         */        
	public	 String getLanguageRange() {
            return languageRange ;
        } 

        /** get the QValue field
         * @return float
         */        
	public	 float getQValue() {
            return (float) qValue ;
        }
                
        /**
         * Return true if the q value has been set.
         * @since 1.0
         * @return boolean
         */
 	public boolean hasQValue() {
            return qValueIsSet ;
        } 
       
        /**
         * Remove the q value.
         * @since 1.0
         */
         public void removeQValue() {
		qValue = -1;
	    qValueIsSet = false;	
         }
       
	/**
         * Set the languageRange member
         * @param l String to set
         */
	public	 void setLanguageRange(String l) {
            languageRange = l ; 
        }
        
	/**
         * Set the qValue member
         * @param q double to set
         */
	public void setQValue(double q) {
            qValue = q ;
            qValueIsSet = true;
        } 

}
