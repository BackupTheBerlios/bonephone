package jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.address.*;

/**
 * <p>
 * This interface represents any header that contains a NameAddress value. It is
 * the super-interface of ContactHeader, EndPointHeader, RouteHeader and
 * RecordRouteHeader.
 * </p>
 *
 * @see ContactHeader
 * @see EndPointHeader
 * @see RouteHeader
 * @see RecordRouteHeader
 *
 * @version 1.0
 *
 */

public interface NameAddressHeader extends ParametersHeader
{
    
    /**
     * Sets NameAddress of NameAddressHeader
     * @param <var>nameAddress</var> NameAddress
     * @throws IllegalArgumentException if nameAddress is null or not from the
     * same JAIN SIP implementation
     */
    public void setNameAddress(NameAddress nameAddress)
                 throws IllegalArgumentException;
    
    /**
     * Gets NameAddress of NameAddressHeader
     * (Returns null if NameAddress does not exist - i.e. wildcard ContactHeader)
     * @return NameAddress of NameAddressHeader
     */
    public NameAddress getNameAddress();
}
