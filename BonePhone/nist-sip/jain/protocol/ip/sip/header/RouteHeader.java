package jain.protocol.ip.sip.header;

/**
 * This interface represents the Route request-header.
 * RouteHeaders determine the route taken by a
 * Request. Each host removes the first entry and then
 * proxies the Request to the host listed in that entry,
 * also using it as the RequestURI. The operation is further
 * described in RecordRouteHeader.
 *
 * @see RecordRouteHeader
 *
 * @version 1.0
 *
 */

public interface RouteHeader extends NameAddressHeader
{
    
    /**
     * Name of RouteHeader
     */
    public final static String name = "Route";
}
