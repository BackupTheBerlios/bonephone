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
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;

import java.util.*;


/**
* Implementation of the ProxyRequireHeader interface of jain-sip.
*/
public final class ProxyRequireHeaderImpl extends HeaderImpl 
implements ProxyRequireHeader, NistSIPHeaderMapping {

     
    /** Default constructor
     */    
   public ProxyRequireHeaderImpl() { 
       super();
            this.headerName = name;
   }

    /** constructor
     * @param pr ProxyRequire to set
     */   
   public ProxyRequireHeaderImpl(ProxyRequire pr ) { 
       super(pr);
            this.headerName = name;
   }
     
    /**
    * Gets option tag of OptionTagHeader
    * @return option tag of OptionTagHeader
    */
    public String getOptionTag() {
        ProxyRequire proxyRequire=(ProxyRequire)sipHeader;
        return proxyRequire.getOptionTag();
    }

    
    /**
     * Set option tag of OptionTagHeader
     * @param optionTag String to set
     * @throws IllegalArgumentException if optionTag is null
     * @throws SipParseException if optionTag is not accepted by implementation
     */
    public void setOptionTag(String optionTag) 
    throws IllegalArgumentException, SipParseException {
         ProxyRequire proxyRequire=(ProxyRequire)sipHeader;
        if ( optionTag==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: optionTag is null");
        else proxyRequire.setOptionTag(optionTag);
    }
    
}


