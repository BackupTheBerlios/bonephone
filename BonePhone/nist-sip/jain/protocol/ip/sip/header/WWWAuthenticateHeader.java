package jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;

/**
 * This interface represents the WWW-Authenticate response-header.
 * At least one WWWAuthenticateHeader must be included in UNAUTHORIZED
 * Responses. The header value consists of a
 * challenge that indicates the authentication scheme(s) and
 * parameters applicable to the RequestURI.
 *
 * @see AuthorizationHeader
 *
 * @version 1.0
 *
 */

public interface WWWAuthenticateHeader extends SecurityHeader
{
    
    /**
     * Name of WWWAuthenticateHeader
     */
    public final static String name = "WWW-Authenticate";
}
