package jain.protocol.ip.sip;

/**
 * The SipPeerUnavailableException indicates that the JAIN SIP Peer class (a particular
 * implementation of JAIN SIP) could not be located in the classpath.
 *
 * @version 1.0
 *
 */

public class SipPeerUnavailableException extends SipException
{
    
    /**
     * Constructs a new JAIN SIP Peer Unavailable Exception.
     */
    public SipPeerUnavailableException() 
    {
        super();
    }
    
    /**
     * Constructs a new JAIN SIP Peer Unavailable Exception with
     * the specified message detail.
     * @param <var>msg</var> the message detail of this Exception.
     */
    public SipPeerUnavailableException(String msg) 
    {
        super(msg);
    }
}
