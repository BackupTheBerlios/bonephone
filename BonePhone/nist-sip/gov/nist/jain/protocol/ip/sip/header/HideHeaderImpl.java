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
* Implementation of the HideHeader interface of jain-sip.
*/
public final class HideHeaderImpl extends HeaderImpl
implements HideHeader, NistSIPHeaderMapping {
    
   
    /** Default constructor
     */    
   public HideHeaderImpl() { 
       super();
      this.headerName = name;
   }

    /** constructor
     * @param hide Hide to set
     */   
   public HideHeaderImpl(Hide hide) { 
       super(hide);
      this.headerName = name;
   }
    
    /**
    * Returns hide value of HideHeader
    * @return hide value of HideHeader
    */
    public String getHide() {
        Hide hide=(Hide)sipHeader;
        return hide.getHide();
    }

    
    /**
     * Sets hide value of HideHeader
     * @param h String to set
     * @throws IllegalArgumentException if hide is null
     * @throws SipParseException if hide is not accepted by implementation
     */
    public void setHide(String h)
    throws IllegalArgumentException, SipParseException {
        Hide hide=(Hide)sipHeader;
        if (h==null)
               throw new IllegalArgumentException
               ("JAIN-SIP EXCEPTION: hide is null");
        else hide.setHide(h);
    }
                                               
    
}


