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
* Implementation of the MaxForwardsHeader interface of jain-sip.
*/
public final class MaxForwardsHeaderImpl extends HeaderImpl
implements MaxForwardsHeader, NistSIPHeaderMapping {
    
    
    /** Default constructor.
     */    
    public MaxForwardsHeaderImpl() { 
        super();
      this.headerName = name;
    }

    /** constructor
     * @param maxforwards MaxForwards to set
     */    
    public MaxForwardsHeaderImpl(MaxForwards maxforwards) {
        super(maxforwards);
      this.headerName = name;
    }
    
    /**
     * Sets max-forwards of MaxForwardsHeader
     * @param maxforwards int to set
     * @throws SipParseException if maxForwards is not accepted by implementation
     */
    public void setMaxForwards(int maxforwards) throws SipParseException {
        MaxForwards maxForwards=(MaxForwards)sipHeader;
        maxForwards.setMaxForwards(maxforwards);
    }

    
    /**
    * Decrements the number of max-forwards by one
    * @throws SipException if implementation cannot decrement max-fowards i.e.
    * max-forwards has reached zero
    */
    public void decrementMaxForwards() throws SipException {
           MaxForwards maxForwards=(MaxForwards)sipHeader;
            if ( maxForwards.hasReachedZero() )
                    throw new SipParseException
                    ("JAIN-SIP EXCEPTION: max-forwards has reached zero");
            else maxForwards.decrementMaxForwards();
    }

    
    /**
    * Gets max-forwards of MaxForwardsHeader
    * @return max-forwards of MaxForwardsHeader
    */
    public int getMaxForwards() {
       MaxForwards maxForwards=(MaxForwards)sipHeader;
       return maxForwards.getMaxForwards();
    } 
    
}



