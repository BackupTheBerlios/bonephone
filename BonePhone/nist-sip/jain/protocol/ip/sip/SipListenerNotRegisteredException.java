package jain.protocol.ip.sip;

/**
 * This exception is thrown if the
 * removeSipListener method of a SipProvider is invoked
 * to remove a SipListener from he list of registered SipListeners,
 * and the SipListener to be removed is not
 * currently a registered SipListener.
 *
 * @version 1.0
 *
 */

public class SipListenerNotRegisteredException extends SipException
{
    
    /**
     * Constructs a new <code> SipListenerNotRegisteredException</code>
     */
    public SipListenerNotRegisteredException() 
    {
        super();
    }
    
    /**
     * Constructs a new SipListenerNotRegisteredException
     * with the specified detail message.
     * @param <var>msg</var> the detail message
     */
    public SipListenerNotRegisteredException(String msg) 
    {
        super(msg);
    }
}
