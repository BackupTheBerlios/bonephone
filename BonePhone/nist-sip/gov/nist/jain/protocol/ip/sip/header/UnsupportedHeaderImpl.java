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

/**
* Implementation of the UnsupportedHeader interface of jain-sip.
*/

public final class UnsupportedHeaderImpl extends HeaderImpl
implements UnsupportedHeader, NistSIPHeaderMapping {
   
    /** Default constructor
     */    
    public UnsupportedHeaderImpl() { 
        super();
            this.headerName = name;
    }

    /** constructor
     * @param unsupported Unsupported to set
     */    
    public UnsupportedHeaderImpl(Unsupported unsupported) { 
        super(unsupported);
            this.headerName = name;
    }
    
    
    /**
    * Gets option tag of OptionTagHeader
    * @return option tag of OptionTagHeader
    */
    public String getOptionTag() {
        Unsupported unsupported=( Unsupported) sipHeader;
        return unsupported.getOptionTag();
    }

    
    /**
     * Sets option tag of OptionTagHeader
     * @param optionTag String to set
     * @throws IllegalArgumentException if optionTag is null
     * @throws SipParseException if optionTag is not accepted by implementation
     */
    public void setOptionTag(String optionTag) throws IllegalArgumentException,
    SipParseException {
        Unsupported unsupported=( Unsupported) sipHeader;
        if (optionTag==null) 
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: optionTag is null");
        else unsupported.setOptionTag(optionTag);
    }
    
    
}


