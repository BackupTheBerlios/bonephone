/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
 * See ../../../../../../../../doc/uncopyright.html for conditions of use       *
 * Creator: M. Ranganathan (mranga@nist.gov)                                    *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                               *
 *******************************************************************************/

/*
 * ListeningPointImpl.java
 *
 * Created on April 17, 2001, 1:50 PM
 */

package gov.nist.jain.protocol.ip.sip;

import jain.protocol.ip.sip.*;
import gov.nist.sip.stack.*;
import gov.nist.sip.msgparser.*;
import gov.nist.jain.protocol.ip.sip.message.*;
import java.util.LinkedList;
import java.net.*;
import java.io.IOException;


/**
 *
 * @author  M. Ranganathan
 * @version 1.0
 */
public class ListeningPointImpl  implements ListeningPoint  {
    
/** NIST-SIP object that sends and receives messages.
 */
    protected LinkedList sipProviders;
/** MessageProcessor corresponding to this ListeningPointImpl
 * @see gov.nist.sip.stack.MesageProcessor
 */
    protected SipStackImpl     sipStack;
    protected int 		port;
    protected String		transport;
    protected String 		host;
    
    /**
     *Default constructor.
     */
    protected ListeningPointImpl() {}
    
    
    /**
     *Constructor.
     *@param mproc message processor.
     */
    protected ListeningPointImpl(MessageProcessor mproc) {
        this.sipStack = (SipStackImpl) mproc.getSIPStack();
        this.port = mproc.getPort();
        if (mproc instanceof TCPMessageProcessor) transport = TRANSPORT_TCP;
        else transport = TRANSPORT_UDP;
        this.host = this.sipStack.getHostAddress();
        this.sipProviders = new LinkedList();
    }
    
    
    
    
   /** Get an available provider.
    * @return An available provider.
    */
    public synchronized SipProviderImpl getSipProvider() {
	SipProviderImpl retval = new SipProviderImpl(this,sipStack);
	this.sipProviders.add(retval);
	return retval;
    }

    /** Remove a provider from the list of providers.
    */
     protected synchronized boolean  removeSipProvider(SipProviderImpl provider){
	return sipProviders.remove(provider);
     }

     /** Get all the providers for this listening point.
     */
     protected LinkedList getSipProviders() { return sipProviders; }
    
    
    
    
    /**
     * Gets host of ListeningPoint
     * @return host of ListeningPoint
     */
    public String getHost() {
        return host;
    }
    
    /**
     * Gets port of ListeningPoint
     * @return port of ListeningPoint
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Gets transport of ListeningPoint
     * @return transport of ListeningPoint
     */
    public String getTransport() {
        return transport;
    }
    
    /**
     * Indicates whether some other Object is "equal to" this ListeningPoint
     * (Note that obj must have the same Class as this ListeningPoint -
     * this means that it must be from the same JAIN SIP implementation)
     * @returns true if this ListeningPoint is "equal to" the obj
     * argument; false otherwise
     * @param obj The object with which to compare this object.
     * @return true if the object is equal to this object.
     */
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass()) ){
            ListeningPointImpl lpi = (ListeningPointImpl) obj;
            return lpi.sipStack == this.sipStack        &&
            lpi.transport.equals(this.transport) &&
            lpi.host.equals(this.host)           &&
            lpi.port == this.port;
        } else return false;
    }
    
    /**
     * Creates and returns a copy of ListeningPoint
     * @returns a copy of ListeningPoint
     * @return A clone of this listening point.
     * (not sure what cloning a listening point means in this context)
     */
    public Object clone() {
        ListeningPointImpl lip = new ListeningPointImpl();
        lip.sipStack = sipStack;
        lip.transport = transport;
        lip.host = host;
        return lip;
    }
    
}
