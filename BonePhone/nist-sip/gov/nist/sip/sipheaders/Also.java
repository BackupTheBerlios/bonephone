/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import  gov.nist.sip.net.*;

/**
* Also header
* @since 1.0
* <pre>
* This mechanism allows unsupervised call transfer.
*
*        Also  =  "Also" ":" 1# (( name-addr | addr-spec )
* </pre>
*/
public class Also extends SIPHeader {
    
        /** address field
         */    
	protected Address address;

        /** constructor
         * @param addr Address to set
         */        
	public Also (Address addr) {
		super(ALSO);
		address = addr;
	}

        /** set the Address field
         * @param addr Address to set
         */        
	public void setAddress( Address addr ) {
		address = addr;
	}

        /** get the Address field
         * @return Address
         */        
	public Address getAddress() {
            return address;
        }

        /** Return canonical header.
         * @return String
         */        
	public String encode() {
		return headerName + COLON + SP + address.encode() + NEWLINE;
	}

}
