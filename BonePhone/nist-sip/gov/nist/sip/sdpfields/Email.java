/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;

public class Email extends SDPObject {
	protected String userName;
	protected String hostName;
	public String getUserName() { return userName; }
	public String getHostName() { return hostName; }
	/**
	* Set the userName member  
	*/
	public	 void setUserName(String u) 
 	 	{ userName = u ; } 
	/**
	* Set the hostName member  
	*/
	public	 void setHostName(String h) 
 	 	{ hostName = h ; } 

      /**
       *  Get the string encoded version of this object
       * @since v1.0
       */
       public String encode() {
	return userName + Separators.AT + hostName;
      }

}
