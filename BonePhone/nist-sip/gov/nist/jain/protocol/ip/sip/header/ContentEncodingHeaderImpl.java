/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: C. Chazeau (chazeau@antd.nist.gov)                                  *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.log.*;

import java.util.Iterator;

/**
* Implementation of the ContentEncodingHeader interface of jain-sip.
*/
public final class ContentEncodingHeaderImpl  extends HeaderImpl
implements ContentEncodingHeader , NistSIPHeaderMapping {


    /** Default constructor
     */    
   public ContentEncodingHeaderImpl() { 
       super();
      this.headerName = name;
   }

    /** constructor
     * @param ce ContentEncoding to set
     */   
   public ContentEncodingHeaderImpl(ContentEncoding ce) { 
       super (ce);
      this.headerName = name;
   }

       /**
	* Gets the encoding of EncodingHeader
	* @return encoding of EncodingHeader
	*/
    public String getEncoding(){
          ContentEncoding contentEncoding = (ContentEncoding) sipHeader ;
          return contentEncoding.getContentEncoding() ;
    }

      /**
       * Sets the encoding of EncodingHeader
       * @param encoding String to set
       * @throws IllegalArgumentException if encoding is null
       * @throws SipParseException if encoding is not accepted by implementation
       */
    public void setEncoding(String encoding)
    throws IllegalArgumentException, SipParseException{ 	
     	ContentEncoding contentEncoding = (ContentEncoding) sipHeader ;
     	if (encoding == null) throw new
            IllegalArgumentException("JAIN-EXCEPTION: null argument");
        contentEncoding.setContentEncoding(encoding) ;
     }
     
}
