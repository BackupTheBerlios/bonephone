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
* Implementation of the SubjectHeader interface of jain-sip.
*/

public final class SubjectHeaderImpl extends HeaderImpl 
implements SubjectHeader,NistSIPHeaderMapping  {
    
     /** Default constructor
     */ 
    public SubjectHeaderImpl() {
        super();
            this.headerName = name;
    }
    
    /** constructor
     * @param subject Subject to set
     */    
    public SubjectHeaderImpl(Subject subject) { 
        super(subject);
            this.headerName = name;
    }   
    
    /**
    * Gets subject of SubjectHeader
    * @return subject of SubjectHeader
    */
    public String getSubject() {
        Subject subject=(Subject)sipHeader;
        return  subject.getSubject();
    }

    
    /**
     * Sets subject of SubjectHeader
     * @param sub String to set
     * @throws IllegalArgumentException if subject is null
     * @throws SipParseException if subject is not accepted by implementation
     */
    public void setSubject(String sub) 
    throws IllegalArgumentException, SipParseException {
        Subject subject=(Subject)sipHeader;
        if (sub==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: subject is null");
        else subject.setSubject(sub);
    }
     
}

