package jain.protocol.ip.sip.address;
import java.net.*;
import jain.protocol.ip.sip.*;

/**
 * This interface provides factory methods to allow an application create address
 * objects from a particular JAIN SIP implementation.
 *
 * @version 1.0
 */

public interface AddressFactory
{
    
    /**
     * Creates a SipURL based on given host
     * @param <var>host</var> host
     * @throws IllegalArgumentException if host is null
     */
    public SipURL createSipURL(InetAddress host)
                   throws IllegalArgumentException,SipParseException;
    
    /**
     * Creates a SipURL based on given host
     * @param <var>host</var> host
     * @throws IllegalArgumentException if host is null
     * @throws SipParseException if host is not accepted by implementation
     */
    public SipURL createSipURL(String host)
                   throws IllegalArgumentException,SipParseException;
    
    /**
     * Creates a SipURL based on given user and host
     * @param <var>user</var> user
     * @param <var>host</var> host
     * @throws IllegalArgumentException if user or host is null
     * @throws SipParseException if user or host is not accepted by implementation
     */
    public SipURL createSipURL(String user, InetAddress host)
                   throws IllegalArgumentException,SipParseException;
    
    /**
     * Creates a SipURL based on given user and host
     * @param <var>user</var> user
     * @param <var>host</var> host
     * @throws IllegalArgumentException if user or host is null
     * @throws SipParseException if user or host is not accepted by implementation
     */
    public SipURL createSipURL(String user, String host)
                   throws IllegalArgumentException,SipParseException;
    
    /**
     * Creates a NameAddress based on given address
     * @param <var>address</var> address URI
     * @throws IllegalArgumentException if address is null or not from same
     * JAIN SIP implementation
     */
    public NameAddress createNameAddress(URI address)
                        throws IllegalArgumentException;
    
    /**
     * Creates a NameAddress based on given diaplay name and address
     * @param <var>displayName</var> display name
     * @param <var>address</var> address URI
     * @throws IllegalArgumentException if displayName or address is null, or
     * address is not from same JAIN SIP implementation
     * @throws SipParseException if displayName is not accepted by implementation
     */
    public NameAddress createNameAddress(String displayName, URI address)
                        throws IllegalArgumentException,SipParseException;
    
    /**
     * Creates a URI based on given scheme and data
     * @param <var>scheme</var> scheme
     * @param <var>schemeData</var> scheme data
     * @throws IllegalArgumentException if scheme or schemeData are null
     * @throws SipParseException if scheme or schemeData is not accepted by implementation
     */
    public URI createURI(String scheme, String schemeData)
                throws IllegalArgumentException,SipParseException;
}
