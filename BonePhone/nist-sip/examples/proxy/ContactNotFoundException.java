/******************************************************
 * File: ContactNotFoundException.java
 * created 01-Dec-00 3:31:04 PM by mranga
 */


package examples.proxy;
import  gov.nist.sip.stack.*;
import  gov.nist.sip.msgparser.SIPErrorCodes;


public class ContactNotFoundException extends 
	SIPServerException implements SIPErrorCodes
{
		
    /*
     * Constructor
     * @param exceptionString String carried by the exception
     */
      
    public ContactNotFoundException ( String exceptionString ) {
	super(CLIENT_ERROR_NOT_FOUND,exceptionString);
    }

}
