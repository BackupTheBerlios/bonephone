package jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.Parameters;

/**
 * <p>
 * This interface represents any header that contains parameters. It is
 * a super-interface of ContactHeader, ContentTypeHeader, SecurityHeader and ViaHeader.
 * </p>
 *
 * @see Parameters
 * @see ContactHeader
 * @see ContentTypeHeader
 * @see SecurityHeader
 * @see ViaHeader
 *
 * @version 1.0
 *
 */

public interface ParametersHeader extends Header, Parameters
{
    
}
