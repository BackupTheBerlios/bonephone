/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: C. Chazeau (chazeau@antd.nist.gov)                                  *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.jain.protocol.ip.sip.address.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.log.*;

import java.util.Iterator;

/**
* Implementation of the ContentEncodingHeader interface of jain-sip.
*/
public final class CSeqHeaderImpl  extends HeaderImpl
    implements CSeqHeader , NistSIPHeaderMapping {

        /** Default constructor
         */        
	public CSeqHeaderImpl() { 
            super();
      this.headerName = name;
        }

        /** constructor
         * @param cseq CSeq to set
         */        
	public CSeqHeaderImpl(CSeq cseq) { 
            super(cseq);
      this.headerName = name;
        }
    	
  	/**
         * Set sequence number of CSeqHeader
         * @param sequenceNumber long to set
         * @throws SipParseException if sequenceNumber is not 
         * accepted by implementation
         */
    	public void setSequenceNumber(long sequenceNumber) 
    	throws SipParseException{
          CSeq cSeq = (CSeq) sipHeader ;
          cSeq.setSeqno(sequenceNumber) ;
    	}

       /**
        * Set method of CSeqHeader
        * @param meth String to set
        * @throws IllegalArgumentException if method is null
        * @throws SipParseException if method is not accepted by implementation
        */
    	public void setMethod(String meth)
     	 throws IllegalArgumentException, SipParseException{
     		CSeq cSeq = (CSeq) sipHeader ;
     		if (meth == null) throw new
          	  IllegalArgumentException("JAIN-EXCEPTION: null argument");
	        cSeq.setMethod(meth) ;
        }

       /**
	* Gets sequence number of CSeqHeader
	* @return sequence number of CSeqHeader
	*/
    	public long getSequenceNumber() {
    		CSeq cSeq = (CSeq) sipHeader ;
    		return cSeq.getSeqno() ;
    	}


       /**
	* Gets method of CSeqHeader
	* @return method of CSeqHeader
	*/
    	public String getMethod(){
    		CSeq cSeq = (CSeq) sipHeader ;
    		return cSeq.getMethod() ;
    	}
        
}
