/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;

/**
* A list of error codes for SIP
*/
public interface SIPErrorCodes {
 	public static final int  INFORMATIONAL_TRYING	 		= 100;
	public static final int  INFORMATIONAL_RINGING 	 		= 180;
	public static final int  INFORMATIONAL_CALL_FORWARDING 		= 181;
        public static final int  INFORMATIONAL_QUEUED 	 		= 182;
        public static final int  INFORMATIONAL_SESSION_PROGRESS		= 183; 
        public static final int  INFORMATIONAL_OK  			= 200; 

        public static final int REDIRECTION_MULTIPLE_CHOICES  		= 300;
        public static final int REDIRECTION_MOVED_PERMANENTLY 		= 301;
        public static final int REDIRECTION_MOVED_TEMPORARILY 	 	= 302;
        public static final int REDIRECTION_USE_PROXY	       	= 305;
        public static final int REDIRECTION_ALTERNATIVE_SERVICE 	= 380;

        public static final int CLIENT_ERROR_BAD_REQUEST         	= 400; 
        public static final int CLIENT_ERROR_UNAUTHORIZED        	= 401;
        public static final int CLIENT_ERROR_PAYMENT_REQUIRED    	= 402  ; 
        public static final int CLIENT_ERROR_FORBIDDEN           	= 403 ; 
        public static final int CLIENT_ERROR_NOT_FOUND           	= 404  ;  
        public static final int CLIENT_ERROR_METHOD_NOT_ALLOWED  	= 405;
        public static final int CLIENT_ERROR_NOT_ACCEPTABLE  		= 406;
        public static final int CLIENT_ERROR_PROXY_AUTHENTICATION_REQUIRED = 407; 
        public static final int CLIENT_ERROR_REQUEST_TIMEOUT     	= 408;
        public static final int CLIENT_ERROR_CONFLICT             	= 409;
        public static final int CLIENT_ERROR_GONE                	= 410;
        public static final int CLIENT_ERROR_LENGTH_REQUIRED     	= 411;
        public static final int CLIENT_ERROR_REQUEST_ENTITY_TOO_LARGE = 413;
        public static final int CLIENT_ERROR_REQUEST_URI_TOO_LARGE 	= 414;
        public static final int CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE 	= 415;
        public static final int CLIENT_ERROR_BAD_EXTENSION          	= 420;
        public static final int CLIENT_ERROR_TEMPORARILY_UNAVAILABLE 	= 480;
        public static final int CLIENT_ERROR_CALL_OR_TRANSITION_DOES_NOT_EXIST = 481;
        public static final int CLIENT_ERROR_LOOP_DETECTED         = 482;
        public static final int CLIENT_ERROR_TOO_MANY_HOPS         = 483;
        public static final int CLIENT_ERROR_ADDRESS_INCOMPLETE    = 484;
        public static final int CLIENT_ERROR_AMBIGUOUS             = 485;
        public static final int CLIENT_ERROR_BUSY_HERE             = 486;
        public static final int CLIENT_ERROR_REQUEST_CANCELLED     = 487;
        public static final int CLIENT_ERROR_NOT_ACCEPTABLE_HERE   = 488;

        public static final int SERVER_ERROR_INTERNAL_FAILURE        = 500 ;  
        public static final int SERVER_ERROR_NOT_IMPLEMENTED          = 501;
        public static final int SERVER_ERROR_BAD_GATEWAY              = 502;
        public static final int SERVER_ERROR_SERVICE_UNAVAILABLE      = 503;
        public static final int SERVER_ERROR_GATEWAY_TIMEOUT          = 504;
        public static final int SERVER_ERROR_SIP_VERSION_NOT_SUPPORTED = 505;

        public static final int GLOBAL_ERROR_BUSY_EVERYWHERE         = 600;  
        public static final int GLOBAL_ERROR_DECLINE                 = 603;
        public static final int GLOBAL_ERROR_DOES_NOT_EXIST_ANYWHERE = 604;
        public static final int GLOBAL_ERROR_NOT_ACCEPTABLE         = 606;

}
