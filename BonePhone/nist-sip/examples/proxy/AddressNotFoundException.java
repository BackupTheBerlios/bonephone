/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* - Added JAVADOC                                                              *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: AddressNotFoundException.java
 * created 24-Oct-00 10:49:59 AM by mranga
 */


package examples.proxy;
import gov.nist.sip.stack.*;


public class AddressNotFoundException extends Exception
{

    /*
     * Constructor
     * @param exceptionString String carried by the exception
     */
      
    public AddressNotFoundException( String address) {
	super(address);
	ServerLog.logException(this);
    }

}
