package jain.protocol.ip.sip.header;

/**
 * This interface represents the Proxy-Require request-header.
 * The ProxyRequireHeader is used to indicate proxy-sensitive
 * features that must be supported by the proxy. Any ProxyRequireHeader
 * features that are not supported by the proxy must be
 * negatively acknowledged by the proxy to the client if not supported.
 * Proxy servers treat this field identically to the RequireHeader.
 *
 * @see RequireHeader
 *
 * @version 1.0
 *
 */

public interface ProxyRequireHeader extends OptionTagHeader
{
    
    /**
     * Name of ProxyRequireHeader
     */
    public final static String name = "Proxy-Require";
}
