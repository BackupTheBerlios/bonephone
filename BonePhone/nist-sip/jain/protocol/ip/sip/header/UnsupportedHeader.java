package jain.protocol.ip.sip.header;

/**
 * This interface represents the Unsupported response-header.
 * UnsupportedHeaders list the features not
 * supported by the server. See RequireHeader for more information.
 *
 * @see RequireHeader
 *
 * @version 1.0
 *
 */

public interface UnsupportedHeader extends OptionTagHeader
{
    
    /**
     * Name of UnsupportedHeader
     */
    public final static String name = "Unsupported";
}
