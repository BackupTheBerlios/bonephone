package jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;

/**
 * This interface represents the Allow entity-header.
 * The AllowHeader specifies a method supported by
 * the resource identified by the Request-URI of a Request.
 * An AllowHeader must be present in a Response with a status code
 * METHOD_NOT_ALLOWED, and should be present in a
 * Response to an OPTIONS Request
 *
 * @version 1.0
 *
 */

public interface AllowHeader extends Header
{
    
    /**
     * Sets method of AllowHeader
     * @param <var>method</var> method
     * @throws IllegalArgumentException if method is null
     * @throws SipParseException if method is not accepted by implementation
     */
    public void setMethod(String method)
                 throws IllegalArgumentException,SipParseException;
    
    /**
     * Gets method of AllowHeader
     * @return method of AllowHeader
     */
    public String getMethod();
    
    //////////////////////////////////////////////////////////////
    
    /**
     * Name of AllowHeader
     */
    public final static String name = "Accept";
}
