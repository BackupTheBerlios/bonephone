/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: C. Chazeau (christophe.chazeau@nist.gov)                            *
* Modified by: M. Ranganathan (mranga@nist.gov)				       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;
import gov.nist.log.*;

/**
* Implementation of the AcceptEncodingHeader interface of jain-sip.
* Builds a wapper around the NIST-SIP accept encoding class.
*@see gov.nist.sip.AcceptEncoding
*/
public final class AcceptEncodingHeaderImpl extends HeaderImpl
implements AcceptEncodingHeader,NistSIPHeaderMapping {
        
   /**
   * Default constructor.
   */
   public AcceptEncodingHeaderImpl() { 
      super();
      this.headerName = name;
   }
    
    /** constructor
     * @param ae AcceptEncoding to set
     */   
   public AcceptEncodingHeaderImpl(AcceptEncoding ae) { 
       super(ae);
       this.headerName = name;
   }
    
    
        /**
         * Gets The Encoding
         * @return the encoding
         */ 
    public String getEncoding(){
	AcceptEncoding acceptEncoding = (AcceptEncoding) sipHeader;
        return acceptEncoding.getContentCoding();
    }
    
        /**
         * Sets The Encoding
         * @param encoding String to set
         * @throws IllegalArgumentException if the parameter is null.
         * @throws SipParseException if Encoding is 
	 *   not accepted by implementation
         */
    public void setEncoding(String encoding) 
    throws IllegalArgumentException , SipParseException {
	AcceptEncoding acceptEncoding = (AcceptEncoding) sipHeader; 
        if (encoding != null)
            acceptEncoding.setContentCoding(encoding);
        else 
            throw new java.lang.IllegalArgumentException
                ("setEncoding(String encoding) - encoding is null") ;
    }
   
}
