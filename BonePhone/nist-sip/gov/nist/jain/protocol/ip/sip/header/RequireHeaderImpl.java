/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: O. Deruelle (deruelle@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.*;

import java.util.*;


/**
* Implementation of the RequireHeader interface of jain-sip.
*/
public final class RequireHeaderImpl extends HeaderImpl
implements RequireHeader, NistSIPHeaderMapping {

    /** Default constructor
     */    
   public RequireHeaderImpl() { 
       super();
            this.headerName = name;
   }

    /** constructor
     * @param require Require to set
     */   
   public RequireHeaderImpl(Require require) { 
       super(require);
            this.headerName = name;
   }
     
    /**
    * Gets option tag of OptionTagHeader
    * @return option tag of OptionTagHeader
    */
    public String getOptionTag() {
         Require require=(Require)sipHeader;
         return require.getOptionTag();
    }

    
    /**
     * Sets option tag of OptionTagHeader
     * @param optionTag String to set
     * @throws IllegalArgumentException if optionTag is null
     * @throws SipParseException if optionTag is not accepted by implementation
     */
    public void setOptionTag(String optionTag) 
    throws IllegalArgumentException, SipParseException {
        Require require=(Require)sipHeader;
        if ( optionTag==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: optionTag is null");
        else require.setOptionTag(optionTag);
    }
    
}



