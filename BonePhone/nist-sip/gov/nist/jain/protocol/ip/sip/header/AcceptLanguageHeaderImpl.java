/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: C. Chazeau (christophe.chazeau@nist.gov)                            *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.log.*;
import gov.nist.sip.*;

/**
* Implementation of the AcceptLanguageHeader interface of jain-sip.
*/
public final class AcceptLanguageHeaderImpl extends HeaderImpl 
implements AcceptLanguageHeader , NistSIPHeaderMapping{

    /** Default constructor
     */    
    public AcceptLanguageHeaderImpl() { 
        super();
      this.headerName = name;
    }

    /** constructor
     * @param acceptLanguage AcceptLanguage to set
     */    
    public AcceptLanguageHeaderImpl(AcceptLanguage acceptLanguage) {
        super(acceptLanguage);
      this.headerName = name;
    }
   
    /** return true if this header has a QValue,
     * false otherwise.
     * @return boolean
     */        
    public boolean hasQValue() {
	AcceptLanguage acceptLanguage = (AcceptLanguage) sipHeader;
           
        return acceptLanguage.hasQValue() ;
    }

   
    /**
     * Gets the q-value of language-range in AcceptLanguageHeader
     * (returns negative float if no q-value exists)
     * @return the q-value of language-range in AcceptLanguageHeader
     */
    public float getQValue() {
	AcceptLanguage acceptLanguage = (AcceptLanguage) sipHeader;
        
        return (float) acceptLanguage.getQValue();
    }

    /**
     * Sets q-value for media-range in AcceptLanguageHeader
     * Q-values allow the user to indicate the relative degree of
     * preference for that language-range, using the qvalue scale from 0 to 1.
     * (If no q-value is present, the language-range should be treated as having a q-value of 1.)
     * @param <var>qValue</var> q-value
     * @throws SipParseException if qValue is not accepted by implementation
     */
    public void setQValue(float qValue) throws SipParseException {
	AcceptLanguage acceptLanguage = (AcceptLanguage) sipHeader;
      
        if (qValue < 0.0) throw new SipParseException
            ("JAIN-EXCEPTION: Invalid Q Value < 0");
        else if (qValue > 1.0) 
             throw new SipParseException
             ("JAIN-EXCEPTION: Invalid Q value > 1.0");
        try {
            acceptLanguage.setQValue(qValue);
        } 
        catch (Exception ex) {
            throw new SipParseException("Invalid q value");
        }
    }

   /**
    * Removes q-value in AcceptLanguageHeader
    */
    public void removeQValue() {
	AcceptLanguage acceptLanguage = (AcceptLanguage) sipHeader;
	acceptLanguage.removeQValue();
    }
	
    /**
     * Sets the language-range of AcceptLanguageHeader
     * @param <var>languageRange</var> language-range of AcceptLanguageHeader
     * @throws IllegalArgumentException if languageRange is null
     * @throws SipParseException if languageRange is not accepted by implementation
     */
    public void setLanguageRange(String languageRange) { 
       
	AcceptLanguage acceptLanguage = (AcceptLanguage) sipHeader; 
       
        if (languageRange == null) 
            throw new IllegalArgumentException("null languageRange!");
	acceptLanguage.setLanguageRange(languageRange) ;
    } 

   /**
    * Get the languageRange member
    * @return String 
    */
    public String getLanguageRange() { 
        
	AcceptLanguage acceptLanguage = (AcceptLanguage) sipHeader;
       
	return acceptLanguage.getLanguageRange()  ; 
    } 
		
}
