/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: O. Deruelle (deruelle@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.address;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.address.*;
import jain.protocol.ip.sip.header.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import java.util.Iterator;

/**
* Implementation of the NameAddress interface of jain-sip.
*/

public final class NameAddressImpl 
implements NameAddress {

     protected Address address;
    
     
    /**
    * Gets display name of NameAddress
    * (Returns null id display name does not exist)
    * @return display name of NameAddress
    */
    public String getDisplayName() {
        return address.getDisplayName();
    }

    
    /**
    * Gets boolean value to indicate if NameAddress
    * has display name
    * @return boolean value to indicate if NameAddress
    * has display name
    */
    public boolean hasDisplayName() {
        return address.hasDisplayName();
    }

    
    /**
    * Removes display name from NameAddress (if it exists)
    */
    public void removeDisplayName() {
        address.removeDisplayName();
    }

    
    /**
    * Sets display name of Header
    * @param <var>displayName</var> display name
    * @throws IllegalArgumentException if displayName is null
    * @throws SipParseException if displayName is not accepted by implementation
    */
    public void setDisplayName(String displayName) 
    throws IllegalArgumentException, SipParseException {
        if (displayName==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: display name is null");
        else    address.setDisplayName(displayName);
    }

    
    /**
    * Gets address of NameAddress
    * @return address of NameAddress
    */
    public jain.protocol.ip.sip.address.URI getAddress() {
         gov.nist.sip.net.URI uri=address.getAddrSpec();
	 if (uri.getUriType() == URITypes.SIP_URL) {
		SipURLImpl uriimpl = new SipURLImpl();
                uriimpl.setImplementationObject(uri);
		return uriimpl;
         } else {
		URIImpl uriimpl=new URIImpl();
                uriimpl.setImplementationObject(uri);
                return uriimpl;
	}
    }

    
    /**
    * Sets address of NameAddress
    * @param <var>address</var> address
    * @throws IllegalArgumentException if address is null or not from same
    * JAIN SIP implementation
    */
    public void setAddress(jain.protocol.ip.sip.address.URI  addr)
    throws IllegalArgumentException {
        if (addr==null)
            throw new IllegalArgumentException("address is null ");
        else  
            if (addr instanceof URIImpl)
            {
               URIImpl uri=(URIImpl)addr; 
               address.setAddrSpec(uri.getImplementationObject());
            }
            else throw new IllegalArgumentException
               ("Bad address class " + addr.getClass().getName()); 
    }

    
    /**
    * Gets string representation of NameAddress
    * @return string representation of NameAddress
    */
    public String toString() {
        return address.toString();
    }

    
    /**
    * Indicates whether some other Object is "equal to" this NameAddress
    * (Note that obj must have the same Class as this NameAddress - this means 
    * that it must be from the same JAIN SIP implementation)
    * @param <var>obj</var> the Object with which to compare this NameAddress
    * @returns true if this NameAddress is "equal to" the obj
    * argument; false otherwise
    */
    public boolean equals(Object obj) {
        if ( obj!=null) {
            if ( obj instanceof NameAddressImpl) {
                NameAddressImpl nameAddressImpl=(NameAddressImpl)obj;   
                boolean b=address.equals( nameAddressImpl.getImplementationObject() );
                return b;
            }
        }
        return false; 
    }

    
    /**
    * Creates and returns a copy of NameAddress
    * @returns a copy of NameAddress
    */
    public Object clone() {
        NameAddressImpl na = new NameAddressImpl();
        na.address = (Address) this.address.clone();
        return na;
    }
    
    
     /**
      *Set the NIST-SIP implementation object.
      */
    public void setImplementationObject(Address address) {
      this.address= address;
    }
    
    public Address getImplementationObject() {
        return address;
    }                  

    /**
    * Default constructor.
    */
    public NameAddressImpl() {
	address = new Address();
    }
    
    
}


