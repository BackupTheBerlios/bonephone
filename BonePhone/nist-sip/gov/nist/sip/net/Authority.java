/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;
import gov.nist.sip.*;
/**
* Authority part of a URI structure.
* This is just a place holder class and gets specialized  
* @see AuthorityServer  
* @see AuthorityRegname
*/
public abstract class Authority  extends NetObject { 
	public abstract String encode();
}
