package jain.protocol.ip.sip;

/**
 * A SipException is thrown when a general JAIN SIP exception is encountered,
 * and is used when no other subclass is appropriate.
 *
 * @version 1.0
 *
 */

public class SipException extends Exception
{
    
    /**
     * Constructs a new SipException
     */
    public SipException() 
    {
        super();
    }
    
    /**
     * Constructs a new SipException with the specified
     * detail message.
     * @param <var>msg</var> the message detail of this Exception.
     */
    public SipException(String msg) 
    {
        super(msg);
    }
}
