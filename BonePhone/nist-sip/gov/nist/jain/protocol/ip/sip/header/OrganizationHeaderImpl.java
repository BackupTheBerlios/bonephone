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
* Implementation of the OrganizationHeader interface of jain-sip.
*/
public final class OrganizationHeaderImpl extends HeaderImpl
implements OrganizationHeader, NistSIPHeaderMapping {
    
    /** Default constructor
     */    
    public OrganizationHeaderImpl() { 
        super();
      this.headerName = name;
    }

    /** constructor
     * @param org Organization to set
     */    
    public OrganizationHeaderImpl(Organization org) {
        super(org);
      this.headerName = name;
    }
   
    /**
    * Gets organization of OrganizationHeader
    * @return organization of OrganizationHeader
    */
    public String getOrganization() {
         Organization organization=(Organization)sipHeader;
         return  organization.getOrganization();
    }
    
    
    /**
     * Sets organization of OrganizationHeader
     * @param org String to set
     * @throws IllegalArgumentException if organization is null
     * @throws SipParseException if organization is not accepted 
     * by implementation
     */
    public void setOrganization(String org)
    throws IllegalArgumentException, SipParseException {
        Organization organization=(Organization)sipHeader;
          if (org==null) 
               throw new IllegalArgumentException
               ("JAIN-SIP EXCEPTION: priority is null ");
        else organization.setOrganization(org);
    }       
    
}

