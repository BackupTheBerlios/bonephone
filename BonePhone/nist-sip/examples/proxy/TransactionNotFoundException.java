/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: TransactionNotFoundException.java
 * created 24-Oct-00 12:04:21 PM by mranga
 */


package examples.proxy;
import  gov.nist.sip.stack.*;


public class TransactionNotFoundException extends Exception
{
	public TransactionNotFoundException() { super(); }
	public TransactionNotFoundException( String  message ) {
		super(message);
		ServerLog.logException(this);
	}
		

}
