/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     * 
* Modified: C. Chazeau (chazeau@antd.nist.gov)                                 *
* Modified By:  O. Deruelle (deruelle@nist.gov) , added JAVADOC                *                                                                                   
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;
import gov.nist.sip.net.*;

import java.util.Date ;
import java.util.*;

/**
* Contact Item. There can be several (strung together in a ContactList).
* @see ContactList
* @version 1.0
* @since 0.9
* <pre>
* From SIP RFC 2543 bis 02 draft spec:
*
*6.15 Contact
*
*   The Contact general-header field can appear in INVITE, OPTIONS, ACK,
*   and REGISTER requests, and in 1xx, 2xx, 3xx, and 485 responses. In
*   general, it provides a URL where the user can be reached for further
*   communications.
*
*   In some of the cases below, the client uses information from the
*   Contact header field in Request-URI of future requests. In these
*   cases, the client copies all but the "method-param" and "header"
*   elements of the addr-spec part of the Contact header field into the
*   Request-URI of the request. It uses the "header" parameters to create
*   headers for the request, replacing any default headers normally used.
*   Unless the client is configured to use a default proxy for all
*   outgoing requests, it then directs the request to the address and
*   port specified by the "maddr" and "port" parameters, using the
*   transport protocol given in the "transport" parameter. If "maddr" is
*   a multicast address, the value of "ttl" is used as the time-to-live
*   value.
*
*        INVITE, OPTIONS and ACK requests: INVITE requests MUST and ACK
*             requests MAY contain Contact headers indicating from which
*            location the request is originating. The URL in the Contact
*             header field is then used by subsequent requests from the
*             callee. For OPTIONS, Contact provides a hint where future
*             SIP requests can be sent or the user can be contacted via
*             non-SIP means.
*
*
*             This allows the callee to send future requests, such
*             as BYE, directly to the caller instead of through a
*             series of proxies.  The Via header is not sufficient
*             since the desired address may be that of a proxy.
*
*        INVITE 1xx responses: A UAS sending a provisional response (1xx)
*            MAY insert a Contact response header. It has the same
*             semantics in a 1xx response as a 2xx INVITE response. Note
*             that CANCEL requests MUSTNOT be sent to that address, but
*             rather follow the same path as the original request.
*
*        INVITE and OPTIONS 2xx responses: A user agent server sending a
*             definitive, positive response (2xx) MUST insert a Contact
*             response header field indicating the SIP address under
*             which it is reachable most directly for future SIP
*             requests, such as ACK, within the same Call-ID. The Contact
*             header field contains the address of the server itself or
*             that of a proxy, e.g., if the host is behind a firewall.
*             The value of this Contact header is copied into the
*             Request-URI of subsequent requests for this call if the
*             response did not also contain a Record-Route header. If the
*             response also contains a Record-Route header field, the
*             address in the Contact header field is added as the last
*             item in the Route header field. See Section 6.35 for
*              details.
*
*             If a UA supports both UDP and TCP, it SHOULDNOT indicate a
*             transport parameter in the URI.
*
*
*             The Contact value SHOULDNOT be cached across calls, as
*             it may not represent the most desirable location for a
*             particular destination address.
*
*        REGISTER requests and responses: See Section 4.2.6.
*
*        3xx and 485 responses: The Contact response-header field can be
*             used with a 3xx or 485 (Ambiguous) response codes to
*             indicate one or more alternate addresses to try. It can
*             appear in responses to BYE, INVITE and OPTIONS methods. The
*             Contact header field contains URIs giving the new locations
*             or user names to try, or may simply specify additional
*             transport parameters. A 300 (Multiple Choices), 301 (Moved
*             Permanently), 302 (Moved Temporarily) or 485 (Ambiguous)
*             response SHOULD contain a Contact field containing URIs of
*             new addresses to be tried. A 301 or 302 response may also
*             give the same location and username that was being tried
*             but specify additional transport parameters such as a
*             different server or multicast address to try or a change of
*             SIP transport from UDP to TCP or vice versa.  The client
*             copies information from the Contact header field into the
*             Request-URI as described above.
*
*        4xx, 5xx and 6xx responses: The Contact response-header field
*             can be used with a 4xx, 5xx or 6xx response to indicate the
*             location where additional information about the error can
*             be found.
*
*   Note that the Contact header field MAY also refer to a different
*   entity than the one originally called. For example, a SIP call
*   connected to GSTN gateway may need to deliver a special information
*   announcement such as "The number you have dialed has been changed."
*
*   A Contact response header field can contain any suitable URI
*   indicating where the called party can be reached, not limited to SIP
*   URLs. For example, it could contain URL's for phones, fax, or irc (if
*   they were defined) or a mailto: (RFC 2368, [32]) URL.
*
*   The following parameters are defined. Additional parameters may be
*   defined in other specifications.
*
*        q: The "qvalue" indicates the relative preference among the
*             locations given. "qvalue" values are decimal numbers from 0
*             to 1, with higher values indicating higher preference. The
*             default value is 0.5.
*
*        action: The "action" parameter is used only when registering
*             with the REGISTER request. It indicates whether the client
*             wishes that the server proxy or redirect future requests
*             intended for the client. If this parameter is not specified
*             the action taken depends on server configuration. In its
*             response, the registrar SHOULD indicate the mode used. This
*             parameter is ignored for other requests.
*
*        expires: The "expires" parameter indicates how long the URI is
*             valid. The parameter is either a number indicating seconds
*             or a quoted string containing a SIP-date. If this parameter
*             is not provided, the value of the Expires header field
*             determines how long the URI is valid. Implementations MAY
*             treat values larger than 2**32-1 (4294967295 seconds or 136
*             years) as equivalent to 2**32-1.
*
*
*   Contact = ( "Contact" | "m" ) ":" 
*             ("*" | (1# (( name-addr | addr-spec )
*             *( ";" contact-params ) )))
*
*   name-addr      = [ display-name ] "<" addr-spec ">"
*   addr-spec      = SIP-URL | URI
*   display-name   = *token | quoted-string
*
*   contact-params = "q"       "=" qvalue
*                  | "action"  "=" "proxy" | "redirect"
*                  | "expires" "=" delta-seconds | <"> SIP-date <">
*                  | contact-extension
*
*   contact-extension = generic-param
*   qvalue            = ( "0" [ "." 0*3DIGIT ] )
*                     | ( "1" [ "." 0*3("0") ] )
*
*
*   Even if the "display-name" is empty, the "name-addr" form MUST be
*   used if the "addr-spec" contains a comma, semicolon or question mark.
*   Note that there may or may not be LWS between the display-name and
*   the "<".
*
*
*        The Contact header field fulfills functionality similar to
*        the Location header field in HTTP. However, the HTTP header
*        only allows one address, unquoted. Since URIs can contain
*        commas and semicolons as reserved characters, they can be
*        mistaken for header or parameter delimiters, respectively.
*        The current syntax corresponds to that for the To and From
*        header, which also allows the use of display names.
*
*   Example:
*
*
*     Contact: "Mr. Watson" <sip:watson@worcester.bell-telephone.com>
*        ;q=0.7; expires=3600,
*        "Mr. Watson" <mailto:watson@bell-telephone.com> ;q=0.1
*
*
* </pre>
*
*
*/
public final class Contact extends SIPHeader implements ContactKeywords {
    

	// This must be private or the toString will go for a loop! 
	private ContactList  contactList;
   	
        /** wildCardFlag field.
         */        
        protected boolean wildCardFlag;
   	
        /** address field
         */        
        protected Address  address; 	       
   	
        /** contactParms field.
         */        
        protected NameValueList contactParms;
   	
        /** comment field.
         */        
        protected String comment;
   
        /** Default constructor.
         */        
	public Contact() {
		super(CONTACT);
		contactParms = new NameValueList("contactParms");
	}
        
	/**
         * Encode this into a cannonical String.
         * @return String
         */
	public String encode() {
		String encoding = headerName + COLON + SP;
		if (wildCardFlag)  {
			return encoding +"*" + NEWLINE;
		}
		encoding += address.encode();
		if (! contactParms.isEmpty()) {
			ListIterator li = contactParms.listIterator();
			while (li.hasNext()) {
			   encoding += SEMICOLON;  
			   NameValue nv = (NameValue) li.next();
			   if (nv.getName() != null) 
				encoding += nv.getName().toLowerCase();
			   if (nv.getValue() != null) {
			      if (nv.getName().compareToIgnoreCase
				  (ContactKeywords.EXPIRE) == 0 ) {
				   if (nv.getValue() instanceof SIPDate) {
				       encoding +=  Separators.EQUALS +
					  Separators.DOUBLE_QUOTE +
			                  ((SIPDate)nv.getValue()).encode() 
				          + Separators.DOUBLE_QUOTE;
				    } else {
					if (nv.getValue() 
					  instanceof GenericObject)
					  encoding +=  EQUALS +
					    ((GenericObject) nv.getValue())
						.encode();
					else encoding +=  EQUALS +
						nv.getValue().toString();
				    }
			      } else {
				   if (nv.getValue() instanceof GenericObject)
					  encoding += EQUALS +
						((GenericObject) 
						nv.getValue()).encode();
				    else encoding +=  EQUALS +
						nv.getValue().toString();
			      }
			   }
			}
		}
		if (comment != null) {
			encoding += LPAREN + comment + RPAREN;
		}
		encoding += NEWLINE;
		return encoding;
	}
		
        /** get the Contact list.
         * @return ContactList
         */        
	public  ContactList getContactList() {
		return contactList;
	}


        /** get the WildCardFlag field
         * @return boolean
         */        
	public boolean getWildCardFlag() {
            return wildCardFlag;
        } 
        
        /** get the Action field.
         * @return String
         */        
        public String getAction() {
		return (String) getParameter(ACTION);
	}
            
        /** get the address field.
         * @return Address
         */        
	public Address getAddress() {
            return address ;
        } 

        /** get the contactParms List
         * @return NameValueList
         */        
	public NameValueList getContactParms() { 
            return contactParms ;
        } 
		
        /** get the specified parameter
         * @param parmName String to set
         * @return Object
         */        
	public Object getParameter( String parmName) {
            if ( contactParms==null) return null;
            return contactParms.getValue(parmName);
        }
	
        /** get the parameters.
         * @return Iterator
         */
	public Iterator getParameters() {
		if ( contactParms==null) return null;
                else return contactParms.getNames();
	}
        
        /** get the comment field       
         * @return String
         */        
	public	 String getComment() {
            return comment ;
        } 
	
        /** get Expires field
         * @return SIPDateOrDeltaSeconds
         */        
	public SIPDateOrDeltaSeconds getExpires() {
		return (SIPDateOrDeltaSeconds) 
			getParameter(EXPIRE);
	}
	
	/** Set the expiry time in seconds.
	*@param expiryDeltaSeconds exipry time.
	*/
	
	public void setExpires(long expiryDeltaSeconds){
		DeltaSeconds deltaSeconds = new DeltaSeconds() ;
		deltaSeconds.setDeltaSeconds(expiryDeltaSeconds) ;
		setParameter(EXPIRE,deltaSeconds) ;
	}
        
        /** get the Q-value
         * @return float
         */        
        public float getQValue(){
                Object o=getParameter(Q);
                if (o instanceof Double) {
                    Double qValue=(Double)o;
                    if (qValue==null) return -1;
                    else 
                        return qValue.floatValue();
                } else {
                    String qValue=(String)o;
                    if (qValue==null) return -1;
                    else 
                        return Float.parseFloat(qValue);
                }
	}
	
        /** Boolean function
         * @return true if this header has a Q-value, false otherwise.
         */        
	public boolean hasQValue(){
		return hasParameter(Q) ;
	}
        
        /** boolean function
         * @return true if this header has an Expires parameter, false
         * otherwise.
         */        
        public boolean hasExpires() { 
		return hasParameter(EXPIRE) ;
	}
                    
        /** boolean function
         * @return true if this header has a Comment field, false
         * otherwise.
         */        
        public boolean hasComment() { 
		return comment!=null;
	}
        
         /** boolean function
         * @return true if this header has an Action parameter, false
         * otherwise.
         */        
        public boolean hasAction() { 
		return hasParameter(Contact.ACTION);
	}
        
        /** boolean function
         * @return true if this header has Parameters, false otherwise.
         */        
        public boolean hasParameters() {
            if ( contactParms==null) return false;
            return !contactParms.isEmpty();
        }		
        
        /** Boolean function
         * @param name String to set        
         * @return true if this header has the specified parameter.
         */        
        public boolean hasParameter(String name) {
            if ( contactParms==null) return false;
            return contactParms.getNameValue(name)!=null; 
        }
	
        /** remove Expires parameter
         */        
	public void removeExpires() {
		removeParameter(EXPIRE) ;
	}
               
        /** remove comment field
         */        
	public void removeComment() {
		comment = null ;
	}		
        	
        /** remove Action parameter.
         */        
	public void removeAction() {
		removeParameter(ACTION);
	}
		
        /** remove all Parameters.
         */        
         public void removeParameters() {
            contactParms = new NameValueList("contactParms");
        }        
        
        /** remove Q-value parameter
         */        
	public void removeQValue(){
		removeParameter(Q) ;
	}
        
         /** delete the specified parameter.
         * @param name String to set
         * @return true if the specified parameter has been removed, false
         * otherwise.
         */        
        public boolean removeParameter (String name) {
                if ( contactParms==null) return false;
		return contactParms.delete(name);
	}
        
        /** set the Contact List
         * @param cl ContactList to set
         */        
	public void setContactList( ContactList cl ) {
		contactList = cl;
	}
        
	
        /** set the Expires parameter
         * @param expiryDate Date to set
         */        
	public void setExpires(Date expiryDate) {
		long date = expiryDate.getTime() ;
		SIPDate sipDate = new SIPDate(date) ;
		setParameter(EXPIRE,sipDate) ;
	}
	
        /**
         * Set the "Name-Value" parameter.
         * @param name String to set
         * @param value Object to set
         */  
        public void setParameter(String name,Object value)  {
	    NameValue nv = new NameValue(name,value);
            if ( contactParms==null) contactParms = new 
                                                NameValueList("contactParms");
            if (contactParms.hasNameValue(name) )
		                                removeParameter(name) ;
	    contactParms.add(nv);
	}         

	/**
         * Set the wildCardFlag member
         * @param w boolean to set
         */
	public void setWildCardFlag(boolean w) {
            wildCardFlag = w ;
        }
        
	/**
         * Set the address member
         * @param a Address to set
         */
	public void setAddress(Address a) {
            address = a ;
        }
        
	/**
         * Set the contactParms member
         * @param c NameValueList to set
         */
	public	 void setContactParms(NameValueList c) {
            contactParms = c ;
        }
        
	/**
         * Set the comment member
         * @param c String to set
         */
	public	 void setComment(String c) {
            comment = c ;
        } 
		
        /**
         * Set the action parameter
         * @param a String to set
         */
  	public	 void setAction(String a) { 
            setParameter(ACTION,a);
        } 
	
        /** set the Q-value parameter
         * @param qValue float to set
         */        
	public void setQValue(float qValue){
		setParameter(Q,String.valueOf(qValue)) ;
        }
        
}
