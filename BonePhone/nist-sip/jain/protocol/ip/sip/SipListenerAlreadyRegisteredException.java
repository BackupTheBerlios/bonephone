package jain.protocol.ip.sip;

/**
 * This exception is thrown if the
 * addSipListener method of a SipProvider is invoked
 * to add a SipListener to the list of registered SipListeners,
 * and the SipListener to be added is already a current registered SipListener.
 *
 * @version 1.0
 *
 */

public class SipListenerAlreadyRegisteredException extends SipException
{
    
    /**
     * Constructs a new <code> SipListenerAlreadyRegisteredException</code>
     */
    public SipListenerAlreadyRegisteredException() 
    {
        super();
    }
    
    /**
     * Constructs a new SipListenerAlreadyRegisteredException
     * with the specified detail message.
     * @param <var>msg</var> the detail message
     */
    public SipListenerAlreadyRegisteredException(String msg) 
    {
        super(msg);
    }
}
