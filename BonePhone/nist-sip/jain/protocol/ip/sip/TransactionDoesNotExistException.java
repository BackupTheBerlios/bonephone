package jain.protocol.ip.sip;

/**
 * This Exception is thrown when a user attempts to reference
 * a transaction that does not exist
 *
 * @version 1.0
 *
 */

public class TransactionDoesNotExistException extends SipException
{
    
    /**
     * Constructs a new TransactionDoesNotExistException
     */
    public TransactionDoesNotExistException() 
    {
        super();
    }
    
    /**
     * Constructs a new TransactionDoesNotExistException with
     * the specified detail message.
     * @param <var>msg</var> the detail message
     */
    public TransactionDoesNotExistException(String msg) 
    {
        super(msg);
    }
}
