package jain.protocol.ip.sip.header;
import java.util.*;
import jain.protocol.ip.sip.*;

/**
 * <p>
 * This interface represents the Response-Key request-header.
 * The ResponseKeyHeader can be used by a client to
 * request the key that the called user agent should use to encrypt the
 * Response with.
 * </p><p>
 * The scheme gives the type of encryption to be used for the
 * Response. If the client insists that the server return an
 * encrypted Response, it includes a RequireHeader with an option
 * tag of "org.ietf.sip.encrypt-response" in its Request.
 * If the server cannot encrypt for whatever reason, it must follow normal
 * RequireHeader procedures and return a BAD_EXTENSION Response.
 * If this RequireHeader is not present, a server should still encrypt if it can.
 *
 * @see RequireHeader
 *
 * @version 1.0
 *
 */

public interface ResponseKeyHeader extends SecurityHeader
{
    
    /**
     * Name of ResponseKeyHeader
     */
    public final static String name = "Response-Key";
}
