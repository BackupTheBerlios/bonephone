package jain.protocol.ip.sip;

/**
 * This Exception is thrown when an attempt is made to
 * create a SipProvider with a ListeningPoint which
 * is not owned by the SipStack or is being used byb another SipProvider
 *
 * @version 1.0
 *
 */

public final class ListeningPointUnavailableException extends SipException
{
    
    /**
     * Constructs a new ListeningPointUnavailableException
     */
    public ListeningPointUnavailableException() 
    {
        super();
    }
    
    /**
     * Constructs a new ListeningPointUnavailableException with
     * the specified detail message.
     * @param <var>msg</var> the detail message
     */
    public ListeningPointUnavailableException(String msg) 
    {
        super(msg);
    }
}
