/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package examples.proxy;
import java.util.LinkedList;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;


/**
*  Implements a contact method for finding users. 
*  For now, this is just a placeholder.  We will extend this to talk to a 
*  directory server to contact the user.
*/


public class DefaultContactMethod implements ContactMethod
{
	
	/**
	* Loads the contact method.
	*/
	public void loadMethod(String script) {
		   return; 
	}
	
	/**
	*   Place holder - unloads the method.
	*/
	public void unloadMethod() {
		return;
	}
	/**
	*  get addres list from the url parameters supplied by the user. 
	* Gets a HostPort list of locations where to forward the request. 
	* This method will typically talk to LDAP or
	*  some other external method of locating the user 
	* (possibly registered here by the user or administrator).
	*/
	public LinkedList  getUserLocation(URI sipURI) {
		return null;
	}

}
