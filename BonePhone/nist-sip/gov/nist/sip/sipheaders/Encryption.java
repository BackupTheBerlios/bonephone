/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;

import java.util.*;

/**
* Encryption SIP Header..
* <pre>
*6.23 Encryption
*
*   The Encryption general-header field specifies that the content has
*   been encrypted. Section 13 describes the overall SIP security
*   architecture and algorithms. This header field is intended for end-
*
*
*
*Handley/Schulzrinne/Schooler/Rosenberg                       [Page 60]
*
*Internet Draft                    SIP                  November 24, 2000
*
*
*   to-end encryption of requests and responses. Requests are encrypted
*   based on the public key belonging to the entity named in the To
*   header field. Responses are encrypted based on the public key
*   conveyed in the Response-Key header field. Note that the public keys
*   themselves may not be used for the encryption. This depends on the
*   particular algorithms used.
*
*   For any encrypted message, at least the message body and possibly
*   other message header fields are encrypted. An application receiving a
*   request or response containing an Encryption header field decrypts
*   the body and then concatenates the plaintext to the request line and
*   headers of the original message. Message headers in the decrypted
*   part completely replace those with the same field name in the
*   plaintext part.  (Note: If only the body of the message is to be
*   encrypted, the body has to be prefixed with CRLF to allow proper
*   concatenation.) Note that the request method and Request-URI cannot
*   be encrypted.
*
*
*        Encryption only provides privacy; the recipient has no
*        guarantee that the request or response came from the party
*        listed in the From message header, only that the sender
*        used the recipient's public key. However, proxies will not
*        be able to modify the request or response.
*
*
*
*        Encryption         =  "Encryption" ":" encryption-scheme 1*SP
*                              #encryption-params
*        encryption-scheme  =  token
*        encryption-params  =  generic-param
*
*        The token indicates the form of encryption used; it is
*        described in Section 13.
*
*   The example in Figure 9 shows a message encrypted with ASCII-armored
*   PGP that was generated by applying "pgp -ea" to the payload to be
*   encrypted.
*
*
*
*   Figure 9: PGP Encryption Example
*
*
*
*   Since proxies can base their forwarding decision on any combination
*   of SIP header fields, there is no guarantee that an encrypted request
*   "hiding" header fields will reach the same destination as an
*
*
*
*Handley/Schulzrinne/Schooler/Rosenberg                       [Page 61]
*
*Internet Draft                    SIP                  November 24, 2000
*
*
*
*   INVITE sip:watson@boston.bell-telephone.com SIP/2.0
*   Via: SIP/2.0/UDP 169.130.12.5
*   From: <sip:a.g.bell@bell-telephone.com>
*   To: T. A. Watson <sip:watson@bell-telephone.com>
*   Call-ID: 187602141351@worcester.bell-telephone.com
*   Cseq: 1 INVITE
*   Content-Length: 829
*   Encryption: PGP version=2.6.2,encoding=ascii
*
*   hQEMAxkp5GPd+j5xAQf/ZDIfGD/PDOM1wayvwdQAKgGgjmZWe+MTy9NEX8O25Red
*   h0/pyrd/+DV5C2BYs7yzSOSXaj1C/tTK/4do6rtjhP8QA3vbDdVdaFciwEVAcuXs
*   ODxlNAVqyDi1RqFC28BJIvQ5KfEkPuACKTK7WlRSBc7vNPEA3nyqZGBTwhxRSbIR
*   RuFEsHSVojdCam4htcqxGnFwD9sksqs6LIyCFaiTAhWtwcCaN437G7mUYzy2KLcA
*   zPVGq1VQg83b99zPzIxRdlZ+K7+bAnu8Rtu+ohOCMLV3TPXbyp+err1YiThCZHIu
*   X9dOVj3CMjCP66RSHa/ea0wYTRRNYA/G+kdP8DSUcqYAAAE/hZPX6nFIqk7AVnf6
*   IpWHUPTelNUJpzUp5Ou+q/5P7ZAsn+cSAuF2YWtVjCf+SQmBR13p2EYYWHoxlA2/
*   GgKADYe4M3JSwOtqwU8zUJF3FIfk7vsxmSqtUQrRQaiIhqNyG7KxJt4YjWnEjF5E
*   WUIPhvyGFMJaeQXIyGRYZAYvKKklyAJcm29zLACxU5alX4M25lHQd9FR9Zmq6Jed
*   wbWvia6cAIfsvlZ9JGocmQYF7pcuz5pnczqP+/yvRqFJtDGD/v3s++G2R+ViVYJO
*   z/lxGUZaM4IWBCf+4DUjNanZM0oxAE28NjaIZ0rrldDQmO8V9FtPKdHxkqA5iJP+
*   6vGOFti1Ak4kmEz0vM/Nsv7kkubTFhRl05OiJIGr9S1UhenlZv9l6RuXsOY/EwH2
*   z8X9N4MhMyXEVuC9rt8/AUhmVQ==
*   =bOW+
*
*   otherwise identical un-encrypted request.
*</pre>
*/
public class Encryption  extends SIPHeader {
    
        /** encryptionScheme field.
         */    
	protected String encryptionScheme;
	
        /** encryptionParms list.
         */        
        protected NameValueList  encryptionParms;

        /** Default constructor
         */        
	public Encryption() {
		super(ENCRYPTION);
		encryptionParms = new NameValueList("EncryptionParms");
		encryptionParms.setSeparator(Separators.COMMA);
	}
             
	/**
         * Encode into a canonical string.
         * @return String
         */
	public String encode() {
		return headerName + COLON + SP + encryptionScheme + 
			SP + encryptionParms.encode() + NEWLINE;
	}

        /** get the encryptionParms list.
         * @return NameValueList
         */        
	public NameValueList getEncryptionParms() {
            return encryptionParms ;
        }
        
        /**
         * Get the encryptionScheme member
         * @return String
         */
        public	String  getEncryptionScheme() {
            return encryptionScheme  ;
        }     
            
         /**
          * Get a particular parameter encoded as a string.
          * @param parmName String to set
          * @return String
          */
        public String getParameter(String parmName) {
            if (encryptionParms==null) return null;
            Object value = encryptionParms.getValue(parmName);
            if (value == null) return null;
            if (value instanceof GenericObject) {
                return ((GenericObject ) value).encode();
            } else {
                return value.toString();
            }
        }
        
         /** get the parameters.
         * @return Iterator
         */
	public Iterator getParameters() {
		if ( encryptionParms==null) return null;
                else return encryptionParms.getNames();
	}
        
        /** Boolean function .
         * @return true if this header has Parameters, false otherwise.
         */        
        public boolean hasParameters() {
            if ( encryptionParms==null) return false;
            else return !encryptionParms.isEmpty();
        }
       
        /** Boolean function.
         * @param name String to set.
         * @return String.
         */        
        public boolean hasParameter(String name) {
              if ( encryptionParms==null) return false;
              else  return encryptionParms.getNameValue(name)!=null; 
        }
        
        /** Boolean function.
         * @param name String to set.
         * @return true if the specified Parameter removed, false otherwise.
         */        
        public boolean removeParameter(String name) {
              if ( encryptionParms==null) return false;
              else return encryptionParms.delete(name);
	}        
        
        /**
	* Remove the parameters.
	* @since 1.0
	*/
         public void removeParameters() {
             encryptionParms = new NameValueList("EncryptionParms");
             encryptionParms.setSeparator(Separators.COMMA);
	}
        
	/**
         * Set the encryptionScheme member
         * @param e String to set
         */
	public void setEncryptionScheme(String e) {
            encryptionScheme = e ;
        }
          
	/**
         * Set the encryptionParms member
         * @param e NameValueList to set
         */
	public void setEncryptionParms(NameValueList e) {
            encryptionParms = e ;
        } 
                       
        /** set the specified Parameter.
         * @param name String to set
         * @param value String to set
         */        
        public void setParameter(String name, String value)  {
		NameValue nv = new NameValue(name,value);
                if ( encryptionParms==null) encryptionParms = new 
                                              NameValueList("EncryptionParms");
                if (encryptionParms.hasNameValue(name) )
		                                removeParameter(name) ;
		encryptionParms.add(nv);
	}                         
         
}