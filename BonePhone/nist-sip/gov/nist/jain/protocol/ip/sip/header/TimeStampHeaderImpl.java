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
* Implementation of the TimeStampHeader interface of jain-sip.
*/
public final class TimeStampHeaderImpl  extends HeaderImpl
implements TimeStampHeader,NistSIPHeaderMapping {
    
   
    /** Default constructor
     */    
    public TimeStampHeaderImpl() { 
        super();
            this.headerName = name;
    }

    /** constructor
     * @param timeStamp TimeStamp to set
     */    
    public TimeStampHeaderImpl(TimeStamp timeStamp) { 
        super(timeStamp);
            this.headerName = name;
    }

    /**
    * Gets timestamp of TimeStampHeader
    * @return timestamp of TimeStampHeader
    */
    public float getTimeStamp() {
         TimeStamp timeStamp=(TimeStamp)sipHeader;
         return timeStamp.getTimeStamp();
    }

    
    /**
    * Gets delay of TimeStampHeader
    * (Returns negative float if delay does not exist)
    * @return delay of TimeStampHeader
    */
    public float getDelay() {
        TimeStamp timeStamp=(TimeStamp)sipHeader;
         return timeStamp.getDelay();
    }

    
    /**
    * Gets boolean value to indicate if TimeStampHeader
    * has delay
    * @return boolean value to indicate if TimeStampHeader
    * has delay
    */
    public boolean hasDelay() {
         TimeStamp timeStamp=(TimeStamp)sipHeader;
         return timeStamp.hasDelay();
    }

    
    /**
     * Sets timestamp of TimeStampHeader
     * @param timestamp float to set
     * @throws SipParseException if timeStamp is not accepted by implementation
     */
    public void setTimeStamp(float timestamp) throws SipParseException {
        TimeStamp timeStamp=(TimeStamp)sipHeader;
        timeStamp.setTimeStamp(timestamp);
    }

    
    /**
     * Sets delay of TimeStampHeader
     * @param delay float to set
     * @throws SipParseException if delay is not accepted by implementation
     */
    public void setDelay(float delay) throws SipParseException {
        TimeStamp timeStamp=(TimeStamp)sipHeader;
        timeStamp.setDelay(delay); 
    }

    
    /**
    * Removes delay from TimeStampHeader (if it exists)
    */
    public void removeDelay() {
        TimeStamp timeStamp=(TimeStamp)sipHeader;
        timeStamp.removeDelay();  
    }
      
}




