/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: C. Chazeau (christophe.chazeau@nist.gov)                            *
* Modified by: M. Ranganathan (mranga@nist.gov) -- changed the inheritance     *
*  hierarcy and got rid of some methods.                                       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.jain.protocol.ip.sip.*;
import gov.nist.sip.msgparser.*;

import java.util.Iterator ;

/**
* Implementation of the CallId interface of jain-sip.
* Builds a wapper around the NIST-SIP Contact class.
*@see gov.nist.sip.CallId
*/
public final class CallIdHeaderImpl extends HeaderImpl implements 
CallIdHeader,NistSIPHeaderMapping {
    
    /** Default constructor
     */    
   public CallIdHeaderImpl() { 
       super();
      this.headerName = name;
   }

    /** constructor
     * @param cid CallID to set
     */   
   public CallIdHeaderImpl(CallID cid) { 
       super(cid);
      this.headerName = name;
   }
    
   /**
    * Gets Call-Id of CallIdHeader
    * @return Call-Id of CallIdHeader
    */
    public String getCallId(){
        CallID callid = (CallID) sipHeader;
        return callid.getCallID() ;
    }
    
   /**
    * Sets Call-Id of CallIdHeader
    * @param callId String to set
    * @throws IllegalArgumentException if callId is null
    * @throws SipParseException if callId is not accepted by implementation
    */
    public void setCallId(String callId)
    throws IllegalArgumentException, SipParseException{
        CallID callID = (CallID) sipHeader;
        if(callId == null)
            throw new IllegalArgumentException
            ("setCallID error : callId parameter is null") ; 
        callID.setCallID(callId) ;
    }

}
