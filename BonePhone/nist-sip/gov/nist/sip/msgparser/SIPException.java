/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: SIPException.java
 * created 30-Sep-00 12:32:58 PM by mranga
 * @version 1.0
 * @author M. Ranganathan mailto:mranga@nist.gov
 */


package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;


public class SIPException extends Exception implements SIPErrorCodes
{
	 
	 protected SIPMessage sipMessage;
	 protected int	sipRC;

  	/**
  	* Convert a return code to a SIP error message.
        * @param rc is the SIP error code to convert to a string.
 	* @return the string error message or null if there is no match.
  	*/
	public static String getMessageString(int rc ) {
		String retval = null;
		switch ( rc ) {

                    /* Informational and success status codes */
                    
                    /* Informational */
                    
                    case INFORMATIONAL_TRYING:
                        retval = "Trying";
                        break ;

                    case INFORMATIONAL_RINGING:
                        retval = "Ringing";
                        break ;
                    
                    case INFORMATIONAL_CALL_FORWARDING:
                        retval = "Call is being forwarded" ;
                        break ;
                        
                    case INFORMATIONAL_QUEUED:
                        retval = "Queued" ;
                        break ;

                    case INFORMATIONAL_SESSION_PROGRESS:
                        retval = "Session progress" ;
                        break ;

                    /* Success */
                        
                    case INFORMATIONAL_OK:
                        retval = "OK" ;
                        break ;
                        
                    /* Redirection status codes */
                        
                    case REDIRECTION_MULTIPLE_CHOICES:
                        retval = "Multiple choices" ;
                        break ;

                    case REDIRECTION_MOVED_PERMANENTLY:
                        retval = "Moved permanently" ;
                        break ;                        
                
                    case REDIRECTION_MOVED_TEMPORARILY:
                        retval = "Moved Temporarily" ;
                        break ; 
                        
                    case REDIRECTION_USE_PROXY:
                        retval = "Use proxy" ;
                        break ;
                        
                    case REDIRECTION_ALTERNATIVE_SERVICE:
                        retval = "Alternative service" ;
                        break ;
                        
                    /* Client error status codes */    
                        
                     case CLIENT_ERROR_BAD_REQUEST:
                        retval = "Bad request" ;
                        break ;
                        
                     case CLIENT_ERROR_UNAUTHORIZED:
                        retval = "Unauthorized" ;
                        break ;
                        
                     case CLIENT_ERROR_PAYMENT_REQUIRED:
                        retval = "Payment required" ;
                        break ;
                        
                     case CLIENT_ERROR_FORBIDDEN:
                        retval = "Forbidden" ;
                        break ;
                        
                     case CLIENT_ERROR_NOT_FOUND:
                        retval = "Not found" ;
                        break ;
                        
                     case CLIENT_ERROR_METHOD_NOT_ALLOWED:
                        retval = "Method not allowed" ;
                        break ;
                        
                     case CLIENT_ERROR_NOT_ACCEPTABLE:
                        retval = "Not acceptable" ;
                        break ;
                        
                     case CLIENT_ERROR_PROXY_AUTHENTICATION_REQUIRED:
                        retval = "Proxy Authentication required" ;
                        break ;
                        
                     case CLIENT_ERROR_REQUEST_TIMEOUT:
                        retval = "Request timeout" ;
                        break ;
                        
                     case CLIENT_ERROR_CONFLICT:
                        retval = "Conflict" ;
                        break ;
                        
                     case CLIENT_ERROR_GONE:
                        retval = "Gone" ;
                        break ;
                        
                     case CLIENT_ERROR_LENGTH_REQUIRED:
                        retval = "Length required" ;
                        break ;
                        
                     case CLIENT_ERROR_REQUEST_ENTITY_TOO_LARGE:
                        retval = "Request entity too large" ;
                        break ;
                        
                     case CLIENT_ERROR_REQUEST_URI_TOO_LARGE:
                        retval = "Request-URI too large" ;
                        break ;
                        
                     case CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE:
                        retval = "Unsupported media type" ;
                        break ;
                        
                     case CLIENT_ERROR_BAD_EXTENSION:
                        retval = "Bad extension" ;
                        break ;
                        
                     case CLIENT_ERROR_TEMPORARILY_UNAVAILABLE:
                        retval = "Temporarily not available" ;
                        break ;
                        
                     case CLIENT_ERROR_CALL_OR_TRANSITION_DOES_NOT_EXIST:
                        retval = "Call leg/Transaction does not exist" ;
                        break ;
                        
                     case CLIENT_ERROR_LOOP_DETECTED:
                        retval = "Loop detected" ;
                        break ;
                        
                     case CLIENT_ERROR_TOO_MANY_HOPS:
                        retval = "Too many hops" ;
                        break ;
                        
                     case CLIENT_ERROR_ADDRESS_INCOMPLETE:
                        retval = "Address incomplete" ;
                        break ;
                        
                     case CLIENT_ERROR_AMBIGUOUS:
                        retval = "Ambiguous" ;
                        break ;
                        
                     case CLIENT_ERROR_BUSY_HERE:
                        retval = "Busy here" ;
                        break ;
                        
                     case CLIENT_ERROR_REQUEST_CANCELLED:
                        retval = "Request cancelled" ;
                        break ;
                        
                     case CLIENT_ERROR_NOT_ACCEPTABLE_HERE:
                        retval = "Not Accpetable here" ;
                        break ;
                        
                     /* Server error status codes */   
                        
                     case SERVER_ERROR_INTERNAL_FAILURE:
                        retval = "Internal server error" ;
                        break ;
                        
                     case SERVER_ERROR_NOT_IMPLEMENTED:
                        retval = "Not implemented" ;
                        break ;
                        
                     case SERVER_ERROR_BAD_GATEWAY:
                        retval = "Bad gateway" ;
                        break ;
                        
                     case SERVER_ERROR_SERVICE_UNAVAILABLE:
                        retval = "Service unavailable" ;
                        break ;
                        
                      case SERVER_ERROR_GATEWAY_TIMEOUT:
                        retval = "Gateway timeout" ;
                        break ;
                        
                      case SERVER_ERROR_SIP_VERSION_NOT_SUPPORTED:
                        retval = "SIP version not supported" ;
                        break ;
                        
                      /* Global failure status codes */  
                        
                      case GLOBAL_ERROR_BUSY_EVERYWHERE:
                        retval = "Busy everywhere" ;
                        break ;
                        
                      case GLOBAL_ERROR_DECLINE:
                        retval = "Decline" ;
                        break ;
                         
                      case GLOBAL_ERROR_DOES_NOT_EXIST_ANYWHERE:
                        retval = "Does not exist anywhere" ;
                        break ;
                        
                      case GLOBAL_ERROR_NOT_ACCEPTABLE:
                        retval = "Not acceptable" ;
                        break ;
                        
       		default:
			retval = null;
            
		}
		return retval;

	 }

	 
	/**
	*  get the SIPMessage structure for this exception.
	*/
	public SIPMessage getSIPMessage() {
		return sipMessage;
	}

	/**
	* set the SIPMessage structure for this exception.
	*/
	public void setSIPMessage( SIPMessage sipmessage) {
		sipMessage = sipmessage;
	}

	/**
	* get the return code for this exception. 
	*/
	public int getSipRC () { 
		return sipRC;
	}
	
	/**
	* Constructor when we are given only the error code
	*/
	public SIPException ( int rc) {
		// (sfo) pass null-String
		 super((String)null);
		 sipRC = rc;
	}
	/**
	* Constructor for when we have the error code and some error info.
	*/
	public SIPException ( int rc, String msg) {
		super (  msg);
		sipRC = rc;
	}
	/**
	* Constructor for when we have a return code and a SIPMessage.
	* @param SIPMessage message is the message (request or response)
	* for this exception.
	* @param SIPMessage message is the SIP message structure that caused
	*	this exception.
	* @param String msg is the additional message specific information.
	*/
	public SIPException ( int rc, SIPMessage message, String msg ) {
		 super(getMessageString(rc)  + 
				"--" + msg );  	
		 sipMessage = message;
		 sipRC = rc;

	}
	
	/**
	* Constructor when we have a pre-formatted response.
	*/
	public SIPException( String response) {
		super (response);
	}


}
