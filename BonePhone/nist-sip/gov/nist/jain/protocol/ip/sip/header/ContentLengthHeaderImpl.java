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
import gov.nist.log.*;
import gov.nist.sip.msgparser.*;

import java.util.*;


/**
* Implementation of the ContentLengthHeader interface of jain-sip.
*/
public final class ContentLengthHeaderImpl extends HeaderImpl 
implements ContentLengthHeader, NistSIPHeaderMapping {

  
    /** Default constructor
     */    
    public ContentLengthHeaderImpl() { 
        super();
      this.headerName = name;
    }

    /** constructor
     * @param cl ContentLength to set
     */    
    public ContentLengthHeaderImpl(ContentLength cl) { 
        super(cl);
      this.headerName = name;
    }

    
    /**
     * Set content-length of ContentLengthHeader
     * @param contentlength int to set
     * @throws SipParseException if contentLength is not accepted 
     * by implementation
     */
    public void setContentLength(int contentlength) throws SipParseException {
        ContentLength contentLength=(ContentLength)sipHeader;
        contentLength.setContentLength(contentlength);
    }
    
    /**
     * Gets content-length of ContentLengthHeader
     * @return content-length of ContentLengthHeader
     */
    public int getContentLength() {
        ContentLength contentLength=(ContentLength)sipHeader;
        return contentLength.getContentLength();
    }
    
}
