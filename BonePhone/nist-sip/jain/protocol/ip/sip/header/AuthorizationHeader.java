package jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;

/**
 * This interface represents the Authorization request-header.
 * A user agent that wishes to authenticate itself with a server -
 * usually, but not necessarily, after receiving an UNAUTHORIZED
 * Response - may do so by including an AuthorizationHeader with the
 * Request. The AuthorizationHeader consists of credentials
 * containing the authentication information of the user agent for the
 * realm of the resource being requested.
 *
 * @see SecurityHeader
 *
 * @version 1.0
 *
 */

public interface AuthorizationHeader extends SecurityHeader
{
    
    /**
     * Name of AuthorizationHeader
     */
    public final static String name = "Authorization";
}
