package gov.nist.sip.stack;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import java.util.Iterator;
import java.util.LinkedList;

/** This is the default router. When the implementation wants to forward
* a request and  had run out of othe options, then it calls this method
* to figure out where to send the request. The default router implements
* a simple "default routing algorithm" which just forwards to the configured
* proxy address.
*/

public class DefaultRouter implements Router {
	protected SIPStack sipStack;
        
        protected Hop defaultRoute;

	/**
	* Constructor.
	*/
        public DefaultRouter() {
	}

        /**
         *Set the next hop address.
         *@param hopString is a string which is interpreted
         *  by us in the following fashion :
         *   host:port/TRANSPORT determines the next hop.
         */
        public void setNextHop(String hopString) 
            throws IllegalArgumentException {
            defaultRoute = new Hop(hopString);
	    defaultRoute.setDefaultRouteFlag();
        }
        
	/**
	* Return  addresses for default proxy to forward the request to. 
	* The list is organized in the following priority.
	* If the requestURI refers directly to a host, the host and port
	* information are extracted from it and made the first hop on the
	* list. The second element in the list is the default route, if
	* such a route is specified int the configuration of the stack.
	*@param method is the method of the request.
	*@param requestURI is the request URI of the request.
	*/
	public Iterator getNextHop(SIPRequest sipRequest) 
	throws IllegalArgumentException {
	    
	    RequestLine requestLine = sipRequest.getRequestLine();
	    if (requestLine == null) 
		throw new IllegalArgumentException("Bad message");
	    URI requestURI = requestLine.getUri();
	    if (requestURI == null) 
		throw new IllegalArgumentException
			("Bad message: Null requestURI");
	    
	    
	    Authority auth = requestURI.getAuthority();
            LinkedList ll = null ;
	    if (auth != null &&
		auth instanceof AuthorityServer)  {
		AuthorityServer server = (AuthorityServer) auth;
		HostPort hostPort = server.getHostPort();
		if (hostPort != null) {
	 	   int port;
		   if ( ! hostPort.hasPort())  port = 5060;
		   else port = hostPort.getPort();
		   Host h = hostPort.getHost();
		   if (h == null) throw new 
			IllegalArgumentException("Malformed requestURI");
		   String host = h.encode();
		   String transport = requestURI.getTransport();
		   if (transport == null) transport = "UDP";
		   Hop hop = new Hop(host,port,transport);
		   hop.setURIRouteFlag();
                   ll = new LinkedList();
		   ll.add(hop);
		} else throw 
		    new IllegalArgumentException("Malformed requestURI");

	    }

            if (defaultRoute != null) {
		if (ll == null) ll = new LinkedList();
		ll.add(defaultRoute);
	    }

	    return ll == null? null: ll.listIterator();
               
        }

	/** Get the default hop.
	*@return defaultRoute is the default route.
	*/
	public Hop getDefaultRoute() { return this.defaultRoute; }

        
}
		
