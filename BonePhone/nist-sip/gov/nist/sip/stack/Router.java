package gov.nist.sip.stack;
import gov.nist.sip.stack.*;
import gov.nist.sip.net.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
import java.util.Iterator;

public interface Router {
	/** Return a linked list of addresses corresponding to a requestURI.
	* This is called for sending out outbound messages for which we do
	* not directly have the request URI. The implementaion function 
	* is expected to return a linked list of addresses to which the
	* request is forwarded. The implementation may use this method
	* to perform location searches etc.
	*
	*@param sipRequest is the message to route.
	*/
	public Iterator getNextHop(SIPRequest sipRequest);

        
        /** 
         * The hop parameter is interpreted by the implementation 
         * in an implementation dependent manner. 
	 *
	 *@param hop is a an un-interpreted parameter string.
	 *@see gov.nist.sip.stack.DefaultRouter
         */
        public void  setNextHop(String hop);

	/** Get the default route.
	*@return the default route.
	*/
	public Hop getDefaultRoute();
        
        
}
	
