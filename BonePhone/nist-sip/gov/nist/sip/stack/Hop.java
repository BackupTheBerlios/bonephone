/*
 * Hop.java
 *
 * Created on July 15, 2001, 2:28 PM
 */

package gov.nist.sip.stack;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

/**
 *
 * @author  M. Ranganathan
 * @version 
 */

/** Routing algorithms return a list of hops to which the request is
* routed.
*/
public class Hop extends Object {
    protected String host;
    protected int port;
    protected String transport;
    protected boolean explicitRoute; // this is generated from a ROUTE header.
    protected boolean defaultRoute; // This is generated from the proxy addr
    protected boolean uriRoute; // This is extracted from the requestURI.



    /** Create new hop given host, port and transport.
    *@param hostName hostname
    *@param portNumber port
    *@param trans transport
    */
    public Hop(String hostName, int portNumber, String trans) {
	host = hostName;
	port = portNumber;
	transport = trans;
    }

    /** Creates new Hop 
     *@param hop is a hop string in the form of host:port/Transport
     *@throws IllegalArgument exception if string is not properly formatted or
     * null.
     */
    public Hop(String hop) throws IllegalArgumentException {
        if (hop == null) throw new IllegalArgumentException("Null arg!");
        StringTokenizer stringTokenizer = new StringTokenizer(hop + "/");
        String hostPort = stringTokenizer.nextToken("/");
        transport = stringTokenizer.nextToken();
        if (transport == null) transport = "UDP";
        else if (transport == "") transport = "UDP";
        if (transport.compareToIgnoreCase("UDP") != 0 &&
        transport.compareToIgnoreCase("TCP") != 0) 
            throw new IllegalArgumentException(hop);
        
        stringTokenizer = new StringTokenizer(hostPort+":");
        host = stringTokenizer.nextToken(":");
        if (host == null || host.equals( "") )
                throw new IllegalArgumentException("no host!");
	String portString = null;
	try {
          portString = stringTokenizer.nextToken(":");
	} catch (NoSuchElementException ex) {
		// Ignore.
	}
        if (portString == null || portString.equals("")) {
            port = 5060;
        } else {
            try {
                port = Integer.parseInt(portString);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Bad port spec");
            }
        }
	defaultRoute = true;
    }
        
    /**
     *Retruns the host string.
     *@return host String
     */
    public String getHost() {
        return host;
    }
    
    /**
     *Returns the port.
     *@return port integer.
     */
    public int getPort() {
        return port;
    }
    
    /** returns the transport string.
     */
    public String getTransport() {
        return transport;
    }

    /** Return true if this is an explicit route (ie. extrcted from a ROUTE
    * Header)
    */
    public boolean isExplicitRoute() {
	return explicitRoute;
    }

    /** Return true if this is a default route (ie. next hop proxy address)
     */
     public boolean isDefaultRoute() {
	return defaultRoute;
     }

     /** Return true if this is uriRoute
     */
     public boolean isURIRoute() { return uriRoute; }
	
     /** Set the URIRoute flag.
     */
     public void setURIRouteFlag() { uriRoute = true; }

	
     /** Set the defaultRouteFlag.
     */
     public void setDefaultRouteFlag() { defaultRoute = true; }

     /** Set the explicitRoute flag.
     */
     public void setExplicitRouteFlag() { explicitRoute = true; }
	

}
