/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: C. Chazeau (christophe.chazeau@nist.gov)                            *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.log.*;
import gov.nist.sip.*;

/**
* Implementation of the AcceptLanguageHeader interface of jain-sip.
*/
public final class AllowHeaderImpl extends HeaderImpl implements 
AllowHeader,NistSIPHeaderMapping {

        /** Default constructor
         */    
	public AllowHeaderImpl() { 
            super();
	    // There is a bug with the header template.
            this.headerName = name;
        }

        /** constructor
         * @param allow Allow to set
         */        
	public AllowHeaderImpl(Allow allow) { 
            super(allow); 
            this.headerName = name;
        }
   
	
        /** get the method field
         * @return String
         */        
	public	 String getMethod() { 
	    Allow allow = (Allow) sipHeader;
            return allow.getMethod() ; 
	} 	

	/**
     * Sets method of AllowHeader
     * @param <var>method</var> method
     * @throws IllegalArgumentException if method is null
     * @throws SipParseException if method is not accepted by implementation
     */
	public	 void setMethod(String method){ 
	    Allow allow = (Allow) sipHeader;
             if (method == null) 
            throw new IllegalArgumentException("null method!");
	    allow.setMethod(method) ; 
	}
	
}
