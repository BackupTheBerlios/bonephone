/******************************************************
 * File: ContactMethod.java
 * created 16-Sep-00 7:51:53 AM by mranga
 */


package examples.proxy;
import  gov.nist.sip.*;
import  gov.nist.sip.stack.*;
import  gov.nist.sip.net.*;
import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.msgparser.*;
import  java.util.LinkedList;

/**
* Contact method for the given user. Contact methods are trusted
* pieces of code that are inserted by the systems administrator on behalf of the
* user. This points to some jpython code that can be executed for the user in 
* order to find him. This is a placeholder and needs to be extended by the 
* server implementer.
*/

public interface ContactMethod
{
	/**
	* This corresponds to the constructor.
	*/
	public void loadMethod(String script);

	/**
	* unload the method (i.e. call its finalizer .. to be defined) Maybe
	* we dont need this method.
	*/
	public void unloadMethod ();

	/**
	* run the jpython user method and return an address where the user 
	* may be contacted (or null if the user cannot be contacted via this 
	* method).
	* @return A linked list giving the user location corresponding to
 	* the given sip URI or null if the method throws an exception or 
	* if the end of the list is reached.
	*/
	public LinkedList  getUserLocation(URI sipURI ) throws Exception ;


}
