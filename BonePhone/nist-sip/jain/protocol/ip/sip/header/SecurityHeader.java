package jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;

/**
 * <p>
 * This interface represents headers used in SIP security. It is comprised
 * of a security scheme and associated parameters. It is the
 * super-interface of AuthorizationHeader, EncryptionHeader,
 * ProxyAuthenticateHeader, ProxyAuthorizationHeader,
 * ResponseKeyHeader and WWWAuthenticateHeader.
 * </p>
 *
 * @see AuthorizationHeader
 * @see EncryptionHeader
 * @see ProxyAuthenticateHeader
 * @see ProxyAuthorizationHeader
 * @see ResponseKeyHeader
 * @see WWWAuthenticateHeader
 *
 * @version 1.0
 *
 */

public interface SecurityHeader extends ParametersHeader
{
    
    /**
     * Method used to get the scheme
     * @return the scheme
     */
    public String getScheme();
    
    /**
     * Method used to set the scheme
     * @param String the scheme
     * @throws IllegalArgumentException if scheme is null
     * @throws SipParseException if scheme is not accepted by implementation
     */
    public void setScheme(String scheme)
                 throws IllegalArgumentException,SipParseException;
}
