package jain.protocol.ip.sip.header;
import java.util.*;
import jain.protocol.ip.sip.*;

/**
 * <p>
 * This interface represents the Server response-header.
 * A ServerHeader contains information about the
 * software used by the server to handle the Request. A Response
 * can contain multiple ServerHeaders identifying the server and any
 * significant subproducts. The ServerHeaders are listed in order of
 * their significance for identifying the application.
 * </p><p>
 * If the Response is being forwarded through a proxy, the proxy
 * application must not modify the ServerHeaders. Instead, it
 * should include a ViaHeader
 * </p><p>
 * Note: Revealing the specific software version of the server may
 * allow the server machine to become more vulnerable to attacks
 * against software that is known to contain security holes. Server
 * implementers are encouraged to make this field a configurable
 * option.
 *
 * @see ViaHeader
 *
 * @version 1.0
 *
 */

public interface ServerHeader extends ProductHeader
{
    
    /**
     * Name of ServerHeader
     */
    public final static String name = "Server";
}
