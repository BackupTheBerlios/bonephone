/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: O. Deruelle (deruelle@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.address;

import jain.protocol.ip.sip.header.*;
import jain.protocol.ip.sip.SipParseException;

import gov.nist.jain.protocol.ip.sip.header.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.net.*;

/**
* Implementation of the URI interface of jain-sip.
*/

public class URIImpl
implements jain.protocol.ip.sip.address.URI {
    
    protected gov.nist.sip.net.URI uri;
    
    /**
     *Constructor.
     */
    public URIImpl() { uri = new gov.nist.sip.net.URI(); }
    
    /**
     *COnstruct the wrapper given a URI.
     */
    public URIImpl( gov.nist.sip.net.URI u) { uri = u; }
    
    /**
     * Sets scheme data of URI
     * @param <var>schemeData</var> scheme data
     * @throws IllegalArgumentException if schemeData is null
     * @throws SipParseException if schemeData is not accepted by implementation
     */
    public void setSchemeData(String schemeData)
    throws IllegalArgumentException, SipParseException {
	if (schemeData == null) {
		throw new IllegalArgumentException("null arg!");
	}
	StringMsgParser smp = new StringMsgParser();
	try {
	    Authority auth = smp.parseAuthority(schemeData);
	    uri.setAuthority(auth);
	} catch (SIPParseException ex) {
		throw new SipParseException(schemeData);
	}
    }

    
    /**
    * Gets string representation of URI
    * @return string representation of URI
    */
    public String toString(){
        return uri.encode();
    }

    
    /**
    * Gets scheme data of URI
    * @return scheme data of URI
    * BUGBUG -- need to revisit this.
    */
    public String getSchemeData() {
        return uri.getSchemeData();
    }

    
    /**
    * Sets scheme of URI
    * @param <var>scheme</var> scheme
    * @throws IllegalArgumentException if scheme is null
    * @throws SipParseException if scheme is not accepted by implementation
    */
    public void setScheme(String scheme) 
    throws IllegalArgumentException, SipParseException {
         if (scheme==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: scheme is null");
        else    uri.setScheme(scheme);
    }

    
    /**
    * Gets scheme of URI
    * @return scheme of URI
    */
    public String getScheme() {
        return uri.getScheme();
    }

    
    
    /**
    * Indicates whether some other Object is "equal to" this URI
    * (Note that obj must have the same Class as this URI - this means that it
    * must be from the same JAIN SIP implementation)
    * @param <var>obj</var> the Object with which to compare this URI
    * @returns true if this URI is "equal to" the obj
    * argument; false otherwise (equality of URI's is defined in RFC 2068) 
    */
    public boolean equals(Object obj) {
        // obj is an instance of URIImpl, and we want an URI object !!
        if ( obj!=null) {
            if ( obj instanceof URIImpl) {
                URIImpl uriImpl=(URIImpl)obj;   
                boolean b=uri.equals( uriImpl.getImplementationObject() );
                return b;
            }
        }
        return false; 
    }

    
    /**
    * Creates and returns a copy of URI
    * @returns a copy of URI
    */
    public Object clone() { 
        URIImpl uh = new URIImpl();
        uh.uri = (gov.nist.sip.net.URI) this.uri.clone();
        return uh;
    }
    
    
     /**
      *Set the NIST-SIP implementation object.
      */
    public void setImplementationObject(gov.nist.sip.net.URI h) {
      uri= (gov.nist.sip.net.URI)h;
    }
    
    public gov.nist.sip.net.URI getImplementationObject() {
        return uri;
    }                 
    
    
}

