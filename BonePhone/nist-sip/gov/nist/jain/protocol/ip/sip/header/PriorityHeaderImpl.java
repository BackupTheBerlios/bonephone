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
* Implementation of the PriorityHeader interface of jain-sip.
*/
public final class PriorityHeaderImpl extends HeaderImpl
implements PriorityHeader, NistSIPHeaderMapping {
    
    
    /** Default constructor
     */    
    public PriorityHeaderImpl() { 
        super();
      this.headerName = name;
    }

    /** constructor
     * @param priority Priority to set
     */    
    public PriorityHeaderImpl(Priority priority) {
        super(priority);
      this.headerName = name;
    }
   
    /**
    * Gets priority of PriorityHeader
    * @return priority of PriorityHeader
    */
    public String getPriority() {
        Priority priority=(Priority)sipHeader;
        return priority.getPriority();    
    }

    
    /**
     * Set priority of PriorityHeader
     * @param prio String to set
     * @throws IllegalArgumentException if priority is null
     * @throws SipParseException if priority is not accepted by implementation
     */
    public void setPriority(String prio) 
    throws IllegalArgumentException, SipParseException {
        Priority priority=(Priority)sipHeader;
        if (prio==null) 
               throw new IllegalArgumentException
               ("JAIN-SIP EXCEPTION: priority is null ");
        else priority.setPriority(prio);
    }
   
}





