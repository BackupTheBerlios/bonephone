package jain.protocol.ip.sip;
import java.net.*;
import java.io.*;

/**
 * This interface represents a unique IP network listening point,
 * and consists of host, port and transport
 *
 * @version 1.0
 *
 */

public interface ListeningPoint extends Cloneable, Serializable
{
    
    /**
     * Gets port of ListeningPoint
     * @return port of ListeningPoint
     */
    public int getPort();
    
    /**
     * Gets transport of ListeningPoint
     * @return transport of ListeningPoint
     */
    public String getTransport();
    
    /**
     * Indicates whether some other Object is "equal to" this ListeningPoint
     * (Note that obj must have the same Class as this ListeningPoint - this means that it
     * must be from the same JAIN SIP implementation)
     * @param <var>obj</var> the Object with which to compare this ListeningPoint
     * @returns true if this ListeningPoint is "equal to" the obj
     * argument; false otherwise
     */
    public boolean equals(Object obj);
    
    /**
     * Creates and returns a copy of ListeningPoint
     * @returns a copy of ListeningPoint
     */
    public Object clone();
    
    /**
     * Gets host of ListeningPoint
     * @return host of ListeningPoint
     */
    public String getHost();
    
    /**
     * TCP Transport constant
     */
    public static final String TRANSPORT_TCP = "tcp";
    
    /**
     * Default port constant
     */
    public static final int DEFAULT_PORT = 5060;
    
    /**
     * UDP Transport constant
     */
    public static final String TRANSPORT_UDP = "udp";
}
