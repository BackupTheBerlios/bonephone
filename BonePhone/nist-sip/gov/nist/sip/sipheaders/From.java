/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import java.util.Iterator;

/**
* From SIP Header
* @version 1.0
* @since 0.9
* Revisions:
*	Removed getAddressExtension
*	Added annotation and grammar documentation.
*
*<pre>
*6.26 From
*
*   Requests and responses MUST contain a From general-header field,
*   indicating the initiator of the request.  (Note that this may be
*   different from the initiator of the call leg. Requests sent by the
*   callee to the caller use the callee's address in the From header
*   field.)  The From field MAY contain the "tag" parameter.  The server
*   copies the From header field from the request to the response. The
*
*
*
*Handley/Schulzrinne/Schooler/Rosenberg                       [Page 63]
*
*Internet Draft                    SIP                  November 24, 2000
*
*
*   optional "display-name" is meant to be rendered by a human-user
*   interface. A system SHOULD use the display name "Anonymous" if the
*   identity of the client is to remain hidden.
*
*   The SIP-URL MUSTNOT contain the "transport-param", "maddr-param",
*   "ttl-param", or "headers" elements. A server that receives a SIP-URL
*   with these elements ignores them.
*
*   Even if the "display-name" is empty, the "name-addr" form MUST be
*   used if the "addr-spec" contains a comma, question mark, or
*   semicolon.  Syntax issues are discussed in Section 6.5.
*
*
*
*        From        =  ( "From" | "f" ) ":" ( name-addr | addr-spec )
*                       *( ";" from-param )
*        from-param  =  tag-param | generic-param
*        tag-param   =  "tag" "=" token
*
*
*   Examples:
*
*
*     From: "A. G. Bell" <sip:agb@bell-telephone.com> ;tag=a48s
*    From: sip:+12125551212@server.phone2net.com
*     From: Anonymous <sip:c8oqz84zk7z@privacy.org>
*
*
*
*   The "tag" MAY appear in the From field of a request. It MUST be
*   present when it is possible that two instances of a user sharing a
*   SIP address can make call invitations with the same Call-ID.
*
*   The "tag" value MUST be globally unique and cryptographically random
*   with at least 32 bits of randomness.  The UA SHOULD use different
*   tags for From and To header fields, but use the same pair of tags at
*   least within the same Call-ID. It is RECOMMENDED to maintain the same
*   tag pair across calls and instances of the UA application.
*
*
*        Maintaining the same tag pair allow restarting of a user
*        agent within interrupting existing calls. Using different
*        tag values for From and To header fields simplifies users
*        calling themselves.
*
*   For the purpose of identifying call legs, two From or To header
*   fields are equal if and only if:
*
*
*
*
*Handley/Schulzrinne/Schooler/Rosenberg                       [Page 64]
*
*Internet Draft                    SIP                  November 24, 2000
*
*
*        o The addr-spec component is equal, according to the rules in
*          Section 2.1.
*
*        o Any "tag" and "generic-param" parameters are equal, compared
*          according to the case-sensitivity rules in Section 6. Only
*          parameters that appear in both header fields are compared.
*
*
*        Call-ID, To and From are needed to identify a call leg.
*        The distinction between call and call leg matters in calls
*        with multiple responses to a forked request. The format is
*        similar to the equivalent RFC 822 [26] header, but with a
*        URI instead of just an email address.
*   
*</pre>
*
*/

public final class From extends SIPHeader implements AddressKeywords {
    
    /** address field
     */    
   protected Address address;
   
    /** parameters list
     */   
   protected NameValueList parms;
   
   
    /** Default constructor
     */   
   public From() {
       super(FROM);
       parms = new NameValueList("addressParms");
   }

    /** Generate a FROM header from a TO header
    */
   public From(To to) {
	super(FROM);
	address = to.address;
	parms = to.parms;
   }
   
   /**
    * Compare two from headers for equality.
    * @param otherHeader Object to set
    * @return true if the two headers are the same, false otherwise.
    */
   public boolean equals(Object otherHeader) {
       if (!otherHeader.getClass().equals(this.getClass())){
           return false;
       }
       
       From otherFrom = (From) otherHeader;
       if (! otherFrom.getAddress().equals(address)) {
           return false;
       }
       if (! parms.equals(otherFrom.parms) ) {
	   if (Debug.debug) {
	      Debug.println("this.parms = " + this.parms);
	      Debug.println("other.parms = " + otherFrom.parms);
	    }
	    return false;
	} else return true;
   }
   
   /**
    * Encode the header into a String.
    * @return String
    */
   public String encode() {
       return headerName + COLON + SP + encodeBody() + NEWLINE;
   }

   /**
    * Encode the header content into a String.
    * @return String
    */
    public String encodeBody() {
       String retval = "";
       if (address.getAddressType() == Address.ADDRESS_SPEC) {
           retval += LESS_THAN;
       }
       retval += address.encode();
       if (address.getAddressType() == Address.ADDRESS_SPEC) {
           retval += GREATER_THAN;
       }
       if (!parms.isEmpty() ) {
           retval += SEMICOLON + parms.encode();
       }
       return retval;
   }
   
   /**
    * Get the address field.
    * @return Address
    */
   public Address getAddress()  {
       return address;
   }
   
   /**
    * Conveniance accessor function to get the hostPort field from the address
    * @return HostPort
    */
   public HostPort getHostPort() {
       return address.getHostPort();
   }

   /**
    * Conveniance accessor function to get the hostPort field from the address
    * @return string containing user@host:port if something like this exists.
    */
   public String getUserAtHostPort() {
       return address.getUserAtHostPort();
   }
   
   /**
    * Get the parameter list.
    * @return get the Parameters List.
    */
   public NameValueList getParms() {
       return parms;
   }

    /** Add to the parameter list.
   *@param nameValue NameValue parameter to add.
   */
   public void setParameter (NameValue nameValue) {
	parms.add(nameValue);
   }
   
   /**
    * Get a particular parameter encoded as a string.
    * @param parmName String to set
    * @return String
    */
   public String getParameter(String parmName) {
       Object value = parms.getValue(parmName);
       if (value == null) return null;
       if (value instanceof GenericObject) {
           return ((GenericObject ) value).encode();
       } else {
           return value.toString();
       }
   }
   
    /**
     * Get the display name from the address.
     * @return String
     */
   public String getDisplayName() {
       return address.getDisplayName();
   }
   
   /** Return an iterator having the parameter names.
    * @return an iterator with the parameter names.
    */
    public Iterator getParmNames() {
        if (parms==null) return null;
	return parms.getNames();
    }
   
   /**
    * Get the tag parameter from the address parm list.
    * @return String
    */
   public String getTag() {
       return (String) parms.getValue(TAG);
   }
   
    /** Boolean function
     * @return true if the structure has  parameters ,
     * false otherwise.
     */
    public boolean hasParameters() {
       if (parms==null) return false;
       else return !parms.isEmpty();
   }
   
    /** Boolean function
     *
     * @param name String to set
     * @return true if the structure has a parameter of a given name,
     * false otherwise.
     */
    public boolean hasParameter(String name ) {
         if (parms==null) return false;
         return parms.getValue(name) != null;
   }
   
    /** Boolean function
     * @return true if this header has a Tag, false otherwise.
     */   
     public boolean hasTag() {
        if (parms==null) return false;
        return parms.hasNameValue(TAG);
    }
   
   /**
    * Remove a parameter.
    * @param name String to set
    */
   public void removeParameter(String name) {
       if (parms !=null) parms.delete(name);
   }
   
   /**
    * Remove all parameters.
    */
   public void removeParameters() {
       parms = new NameValueList("addressParms");
   }
    
    /** remove the Tag field.
     */   
    public void removeTag() {
         if (parms!=null) parms.delete(TAG);
    }
   
   /**
    * Set a parameter in the from header.
    * @param name String to set
    * @param value Strimg to set
    */
   public void setParameter(String name, String value) {
       NameValue nv = new NameValue(name,value);
       if (parms==null) parms = new NameValueList("addressParms");
       if (parms.hasNameValue(name) )
		            removeParameter(name) ;
       parms.add(nv);
   }
   
    /**
     * Set the address member
     * @param address Address to set
     */
   public void setAddress(Address address) {
       this.address = address;
       this.address.removeParameter("transport");
       this.address.removeParameter("maddr");
       this.address.removeParameter("ttl");
   } 
   
   /**
    * Set the tag member
    * @param t String to set.
    */
   public void setTag(String t) { 
       if (parms==null) parms = new NameValueList("addressParms");
       else parms.delete(TAG);
       NameValue nv = new NameValue(TAG,t);
       parms.add(nv);
   } 
   
}
