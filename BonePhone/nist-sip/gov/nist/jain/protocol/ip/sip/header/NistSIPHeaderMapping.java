/*
 * NistSIPMapping.java
 *
 * Created on April 7, 2001, 11:10 AM
 */

package gov.nist.jain.protocol.ip.sip.header;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;

/**
 *
 * @author  M. Ranganathan
 * @version 
 */
public interface NistSIPHeaderMapping {
    /**
     *Set the underlying NIST-SIP implementation object.
     */
    public void setImplementationObject( SIPHeader h );
    /**
     *Get the underlying NIST-SIP umplementation object.
     */
    public SIPHeader getImplementationObject ();

}

