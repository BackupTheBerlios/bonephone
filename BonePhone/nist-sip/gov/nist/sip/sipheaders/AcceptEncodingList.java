/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                    
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* AcceptEncodingList of AccepEncoding headers.
*/
public class AcceptEncodingList extends SIPHeaderList {
    
        /** default constructor
         */    
	public AcceptEncodingList () {
    	  super ("AcceptEncoding",SIPHEADERS_PACKAGE + ".AcceptEncoding", 
		  ACCEPT_ENCODING);
	}

        /** add the specified parameter
         * @param cp AcceptEncoding to set
         */        
	public void add(AcceptEncoding cp) {
		super.add(cp);
	}

}

