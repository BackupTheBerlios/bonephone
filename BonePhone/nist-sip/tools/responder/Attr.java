/*
 * Attributes.java
 *
 * Created on July 5, 2001, 3:17 PM
 */

package tools.responder;

/**
 *
 * @author  mranga
 * @version 
 */
public interface Attr {

        // Following can appear inside of a SIPHeader structure.
        public static final String headerName = "headerName";
                
        // Following can appear inside of a REQUEST_LINE block.
        public static final String method        = "method";
        
        // Following can appear inside of a STATUS_LINE tag
        public static final String statusCode  = "statusCode";
        
     	// Description of the call flow graph.
	public static final String description    = "description";
        
        // Destination of the messagee
        public static final String destination    = "destination";
        
        
        // message id of the message to send.
        public static final String messageId = "messageId";
        
        // Event id of the event node.
        public static final String eventId = "eventId";
        
        // message type (SipRequest/SipResponse)
        public static final String messageType = "messageType";
        
        //Template id to do template matching on reveived msgs.
        public static final String templateId = "templateId";

        // SIPRequest
        public static final String SipRequest = "SipRequest";
        
        // SIPResponse
        public static final String SipResponse = "SipResponse";
        
        //Retransmit flag.
        public static final String retransmit  = "retransmit";
        
        //RecordRoute flag
        public static final String recordRoute = "recordRoute";
        
        //Instantiate on.
        public static final String instantiateOn = "instantiateOn";
        
	//oneShot flag
	public static final String oneShot = "oneShot";
        
        //requestURI
        public static final String requestURI  = "requestURI";
        
        //Display name
        public static final String displayName = "displayName";
        
        //User Name
        public static final String userName = "userName";
        
        //host
        public static final String host  = "host";
        
        //Port
        public static final String port = "port";
        
        //action
        public static final String action = "action";
        
        //proxy
        public static final String proxy = "proxy";
        
        //redirect
        public static final String redirect="redirect";
        
        //Expiry time.
        public static final String expires = "expires";
        
        //Expiry date 
        public static final String expiryDate = "expiryDate";
        
        //Callid
        public static final String localId = "localId";

	// Callee request URI
	public static final String calleeRequestURI = "calleeRequestURI";


	// Agent ID
	public static final String agentId = "agentId";

	// contact port for contact addresses.
	public static final String contactPort = "contactPort";

	// contact port for contact addresses
	public static final String contactHost = "contactHost";

	// on trigger
	public static final String onTrigger = "executeOnTrigger";

	// on completion
	public static final String onCompletion = "executeOnCompletion";

	// Enable on event
	public static final String enablingEvent = "enablingEvent";

	public static final String generatedEvent = "generatedEvent";

        public static final String triggerMessage = "triggerMessage";

	// delay
	public static final String delay = "delay";

	public static final String nodeId = "nodeId";
        

}
