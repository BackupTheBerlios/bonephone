/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov)                                *  
* Modifications by: O. Deruelle (deruelle@antd.nist.gov added JAVADOC)         *                                                                                
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;
import gov.nist.sip.net.*;

import jain.protocol.ip.sip.*;

import java.util.*;

/**
* Via SIPHeader (these are strung together in a ViaList).
*
* <pre>
* RFC 2543 Section : 6.47  (Nov 24 bis 02 document):
* Handley/Schulzrinne/Schooler/Rosenberg                       
* 6.47 Via
*
*   The Via field indicates the path taken by the request so far.  This
*   prevents request looping and ensures replies take the same path as
*   the requests, which assists in firewall traversal and other unusual
*   routing situations.
*
* 6.47.1 Requests
*
*   The client originating the request MUST insert into the request a Via
*   field containing the transport protocol used to send the message, the
*   client's host name or network address and, if not the default port
*   number, the port number at which it wishes to receive responses.
*   (Note that this port number can differ from the UDP source port
*   number of the request.) A fully-qualified domain name is RECOMMENDED.
*   Each subsequent proxy server that sends the request onwards MUST add
*   its own additional Via field before any existing Via fields. A proxy
*   that receives a redirection (3xx) response and then searches
*   recursively, MUST use the same Via headers as on the original proxied
*   request.
*
*   A proxy SHOULD check the top-most Via header field to ensure that it
*   contains the sender's correct network address, as seen from that
*   proxy. If the sender's address is incorrect, the proxy MUST add an
*   additional "received" attribute, as described in Section 6.47.2.
*
*
*        A multi-homed host may not be able to insert a network
*        address into the Via header field that can be reached by
*        the next hop, for example because if one of the networks is
*        private. The address placed into the Via header may differ
*        from the interface actually used, as that interface is
*        selected only at packet sending time by the IP layer.
*
*
*   A client that sends a request to a multicast address MUST add the
*   "maddr" parameter to its Via header field, and SHOULD add the "ttl"
*   parameter. (In that case, the maddr parameter SHOULD contain the
*   destination multicast address, although under exceptional
*   circumstances it MAY contain a unicast address.) If a server receives
*   a request which contained an "maddr" parameter in the topmost Via
*   field, it SHOULD send the response to the address listed in the
*   "maddr" parameter.
*
*   Loop detection is described in Section 12.3.1.
*
*   Every host that sends or forwards a SIP request adds a Via field
*   indicating the host's address. However, it is possible that Network
*   Address Translators (NATs) change the source address and port of the
*   request (e.g., from a net-10 to a globally routable address), in
*   which case the Via header field cannot be relied on to route replies.
*   To prevent this, a proxy SHOULD check the top-most Via header field
*   to ensure that it contains the sender's correct network address, as
*   seen from that proxy. If the sender's address is incorrect, the proxy
*   MUST add a "received" parameter to the Via header field inserted by
*   the previous hop. Such a modified Via header field is known as a
*   receiver-tagged Via header field.
*
*   An example is:
*
*
*     Via: SIP/2.0/UDP erlang.bell-telephone.com:5060
*     Via: SIP/2.0/UDP 10.0.0.1:5060 ;received=199.172.136.3
*
*
*
*   In this example, the message originated from 10.0.0.1 and traversed a
*   NAT with the external address border.ieee.org (199.172.136.3) to
*   reach erlang.bell-telephone.com.  The latter noticed the mismatch,
*   and added a parameter to the previous hop's Via header field,
*   containing the address that the packet actually came from. (Note that
*   the NAT border.ieee.org is not a SIP server.)
*
*   Via header fields in responses are processed by a proxy or UAC
*   according to the following rules:
*
*        1.   The first Via header field should indicate the proxy or
*             client processing this response. If it does not, discard
*             the message.  Otherwise, remove this Via field.
*
*
*        2.   If there is no second Via header field, this response is
*             destined for this client. Otherwise, use this Via field as
*             the destination, as described in Section 6.47.5.
*
* 6.47.4 User Agent and Redirect Servers
*
*   A UAS or redirect server copies the Via header fields into the
*   response, without changing their order, and uses the top (first) Via
*   element as the destination, as described in the next section.
*
* 6.47.5 Forwarding Responses
*
*   Given a destination described by a Via header field, the response is
*   sent according to the following rules:
*
*        o If the "sent-protocol" is a reliable transport protocol such
*          as TCP, TLS or SCTP, send the response using the existing TCP
*          connection to the source of the original request.
*
*        o Otherwise, if the Via header field contains a "maddr"
*          parameter, forward the response to the address listed there,
*          using the port indicated in "sent-by", or port 5060 if none is
*          present. If the address is a multicast address, the response
*          SHOULD be sent using the TTL indicated in the "ttl" parameter,
*          or with a TTL of 1 if that parameter is not present.
*
*        o Otherwise, if it is a receiver-tagged field (Section 6.47.2),
*          send the response to the address in the "received" parameter,
*          using the port indicated in the "sent-by" value, or using port
*          5060 if none is specified explicitly.
*
*        o Otherwise, if it is not receiver-tagged, send the response to
*          the address indicated by the "sent-by" value in the second Via
*          header field.
*
*   Note that the response to an unreliable datagram request is not
*   returned to the port from which the request came.
*
* 6.47.6 Syntax
*
*   The format for a Via header field is shown in Fig. 10. The "maddr"
*   parameter, designating the multicast address, and the "ttl"
*   parameter, designating the time-to-live (TTL) value, are included
*   only if the request was sent via multicast. The "received" parameter
*   is added only for receiver-added Via fields (Section 6.47.2).
*
*
*   The "branch" parameter is included by every proxy. The token MUST be
*
*  Via              = ( "Via" | "v") ":" 1#( sent-protocol sent-by
*                     *( ";" via-params ) [ comment ] )
*  via-params       = via-hidden | via-ttl | via-maddr 
*                   | via-received | via-branch | via-extension
*  via-hidden       = "hidden"
*  via-ttl          = "ttl" "=" ttl
*  via-maddr        = "maddr" "=" host
*  via-received	   = "received" "=" host
*  via-branch       = "branch" "=" token
*  via-extension    = generic-param
*  sent-protocol    = protocol-name "/" protocol-version "/" transport
*  protocol-name    = "SIP" | token
*  protocol-version = token
*  transport        = "UDP" | "TCP" | token
*  sent-by          = host [ ":" port ]
*
*
*   Figure 10: Syntax of Via header field
*
*
*   unique for each distinct request. The precise format of the token is
*   implementation-defined. In order to be able to both detect loops and
*   associate responses with the corresponding request, the parameter
*   SHOULD consist of two parts separable by the implementation. One
*   part, used for loop detection (Section 12.3.1), MAY be computed as a
*   cryptographic hash of the To, From, Call-ID header fields, the
*   Request-URI of the request received (before translation) and the
*   sequence number from the CSeq header field. The algorithm used to
*   compute the hash is implementation-dependent, but MD5 [36], expressed
*   in hexadecimal, is a reasonable choice. (Note that base64 is not
*   permissible for a token.) The other part, used for matching responses
*   to requests, is a globally unique function of the branch taken, for
*   example, a hash of a sequence number, local IP address and request-
*   URI of the request sent on the branch.
*
*   For example: 7a83e5750418bce23d5106b4c06cc632.1
*
*
*        The "branch" parameter MUST depend on the incoming
*        request-URI to distinguish looped requests from requests
*        whose request-URI is changed and which then reach a server
*        visited earlier.
*
*   CANCEL requests MUST have the same branch value as the corresponding
*   forked request. When a response arrives at the proxy it can use the
*   branch value to figure out which branch the response corresponds to.
*
*
*
*
*     Via: SIP/2.0/UDP first.example.com:4000;ttl=16
*       ;maddr=224.2.0.1 ;branch=a7c6a8dlze.1 (Acme server)
*     Via: SIP/2.0/UDP adk8
*
*
* 
* </pre>   
*
* @see gov.nist.sip.msgparser.SIPMessage
* @see ViaList
*/


public class Via extends SIPHeader implements ViaKeywords {
        
    /** sentProtocol field.
     */    
    protected Protocol sentProtocol;
    
    /** sentBy field.
     */        
    protected HostPort   sentBy;
    
    /** viaPams field.
     */        
    protected NameValueList viaParms;
    
    /** comment field
     */        
    protected String   comment;
    
     /** Default constructor
     */        
    public Via() {
        super(VIA);
        viaParms = new NameValueList("viaParms");
        sentProtocol=new Protocol();
    }
    
        /**
         *Compare two via headers for equaltiy.
         * @param other Object to set.
         * @return true if the two via headers are the same.
         */
     public boolean equals(Object other) {
        if (! this.getClass().equals(other.getClass())) {
            return false;
        }
        Via that = (Via) other;
        
        if (! this.sentProtocol.equals(that.sentProtocol)) {
            return false;
        }
        if (! this.viaParms.equals(that.viaParms)) {
            return false;
        }
        if ( ! this.sentBy.equals(that.sentBy)) {
            return false;
        }
        return true;
    }
    
        /**
         * Encode the via header into a cannonical string.
         * @return String containing cannonical encoding of via header.
         */
    public String encode() {
        String encoding = headerName + COLON + SP;
        encoding += sentProtocol.encode() + SP + sentBy.encode();
	// Add the default port if there is no port specified.
	if ( ! sentBy.hasPort()) encoding += COLON + "5060";
        if (comment != null) {
            encoding += LPAREN + comment + RPAREN;
        }
	if (! viaParms.isEmpty() ) {
		encoding += SEMICOLON + viaParms.encode();
	}
        encoding += NEWLINE;
        return encoding;
    }
       
    /** get the Protocol Version
     * @return String
     */    
    public String getProtocolVersion() {
        if (sentProtocol==null) return null;
        else return sentProtocol.getProtocolVersion();
    }
    
        /**
         * Accessor for the sentProtocol field.
         * @return Protocol field
         */
    public Protocol getSentProtocol() {
     
        return sentProtocol ;
    }
    
        /**
         * Accessor for the sentBy field
         *@return SentBy field
         */
    public HostPort getSentBy() { 
        return sentBy ;
    } 
    
        /**
         * Accessor for the viaParms field
         * @return viaParms field
         */
    public NameValueList getViaParms() {
        return viaParms ;
    }
    
        /**
         * Accessor for the comment field.
         * @return comment field.
         */
    public String getComment() {
        return comment ;
    } 
       
        /**
         *  Get the Branch parameter if it exists.
         * @return Branch field.
         */
    public String getBranch()  {
        if (viaParms==null) return null;
        else return (String)viaParms.getValue(Via.BRANCH);   
    }
        
        /**
         *  get the received parameter if it exists
         * @return received parameter.
         */
    public String getReceived()  {
        return (String)viaParms.getValue(RECEIVED);       
    }
    
        /**
         *  Get the maddr parameter if it exists.
         * @return maddr parameter.
         */	
    public Host getMaddr()  {
        return (Host)viaParms.getValue(MADDR);     
    }
       
        /**
         * get the ttl parameter if it exists.
         * @return ttl parameter.
         */
    public int getTTL()  { 
        if (viaParms==null) return -1;
        else {
            Integer ttl=(Integer)viaParms.getValue(TTL);
            if (ttl==null) return -1;
            else return  ttl.intValue();
        }
    }
    
        /**
         * Get the "Name" parameter if it exists. 
         * @return String.
         * @param name String to set.
         */
    public String getParameter(String name) {
        if ( viaParms==null) return null;
        else return (String) viaParms.getValue(name) ;
    } 
    
        /**
         * Get the parameters if it exists. 
         * @return Iterator.
         */
    public Iterator getParameters() {
        if ( viaParms==null) return null;
        else return viaParms.getNames();
    } 
    
        /** port of the Via header.
         * @return  port field.
         */        
    public int getPort() {
        if (sentBy==null) return -1;
        return sentBy.getPort(); 
    }

    	/**
	* Get the host name. (null if not yet set).
	*@return host name from the via header.
	*/
    public String getHost() {
	if (sentBy == null) return null;
        else {
                Host host=sentBy.getHost();
                if (host==null) return null;
                else return host.getHostname();
        }
     }
       
       /**
        * get the transport from the via header.
        * @return the transport String
        */
    public String getTransport() {

	if (sentProtocol == null) 
        {
            return null;
        }
	else return sentProtocol.getTransport();
    }
    
        /** port of the Via Header.
         * @return true if Port exists.
         */        
    public boolean hasPort() {
        return (getSentBy()).hasPort();
    }
    
          /**
           *  Parameters of the Via Header.
           * @return true if Parameters exists. 
           */  
    public boolean hasParameters() {
        if ( viaParms==null) return false;
        return !viaParms.isEmpty();
    }
    
       
        /** Specified Parameter name
         * @param name String to set
         * @return true if Parameter name exists.
         */        
    public boolean hasParameter(String name) {
         if ( viaParms==null) return false;
         else return viaParms.getValue(name)!=null;  
    }
    
        /** comment of the Via Header.
         * 
         * @return false if comment does not exist and true otherwise.
         */
    public boolean hasComment() { 
        return comment !=null;
    }
    
    /**
    * Returns boolean value indicating if  hidden field exists
    * @return boolean 
    */
    public boolean isHidden() {
         if (viaParms==null) return false;
         else {
             Object object= viaParms.getValue(Via.HIDDEN);
             return (object!=null);
         }
    }
    
      /** delete the specified Parameter.
         * @param name String to set
         * @return true if the Parameter has been deleted
         */        
    public boolean removeParameter (String name) {
         if ( viaParms==null) return false;
         else return viaParms.delete(name);
    }
    
        /** remove the port.
         */        
    public void removePort() {
        if (sentBy!=null) sentBy=new HostPort(); 
    }
    
       /** remove the comment field.
        */        
    public void removeComment() {
        comment=null; 
    }
    
        /** remove all the parameters
         */        
    public void removeParameters() {
        viaParms = new NameValueList("viaParms");
    }
       
    /** set the Protocol Version
     * @param protocolVersion String to set
     */    
    public void setProtocolVersion(String protocolVersion) {
        if ( sentProtocol==null) sentProtocol=new Protocol();
        sentProtocol.setProtocolVersion(protocolVersion);
    }
    
        /** port of the Via Header.
         * @param port int to set
         */        
    public void setPort(int port) {
        if (sentBy==null) sentBy=new HostPort();
        sentBy.setPort(port); 
    }
    
        /**
         * set the ttl parameter.
         * @param ttl to set
         */		
    public void setTTL(int ttl) {
        NameValue namevalue=viaParms.getNameValue(TTL);
        if (namevalue!=null)
            namevalue.setValue(new Integer(ttl));
        else viaParms.add(TTL,new Integer(ttl));
    }
    
        /** set the Host of the Via Header
         * @param host String to set
         */        
    public void setHost(String host) {
	if (sentBy == null) sentBy = new HostPort();
        Host h=new Host(host);
        sentBy.setHost(h);
    }

    /** set the Host of the Via Header
         * @param host String to set
         */        
    public void setHost(Host host) {
        if (sentBy == null) {
            sentBy = new HostPort();
        }
        sentBy.setHost(host);
    }
    
        /**
         * Set the MADDR parameter .
         * @param mAddr String to set
         */	
    public void setMAddr(String mAddr)  {  
        NameValue nameValue=viaParms.getNameValue(MADDR);
        Host host=new Host();
        host.setAddress(mAddr);
        if (nameValue!=null) 
           nameValue.setValue(host); 
        else {
                nameValue= new NameValue(MADDR,host);
                viaParms.add(nameValue);     
        }
    }
    
        /**
         * set the received parameter
         * @param received to set.
         */
    public void setReceived(String received)  {
        NameValue nameValue=viaParms.getNameValue(RECEIVED);
        if (nameValue!=null)
            nameValue.setValue(received); 
        else viaParms.add(RECEIVED,received);
    }
    
         /**
          * set the branch parameter
          * @param branch to set.
          */
    public void setBranch(String branch)  {
        NameValue namevalue=viaParms.getNameValue(Via.BRANCH);
        if (namevalue!=null)
            namevalue.setValue(branch);
        else viaParms.add(Via.BRANCH,branch);
    }   
    
       /**
        * Set the "Name-Value" parameter. 
        * @param name String to set.
        * @param value String to set.
        */  
    public void setParameter(String name, String value)  {
        NameValue nv = new NameValue(name,value);
        if ( viaParms==null) viaParms= new NameValueList("viaParms");
        if ( viaParms.hasNameValue(name) )removeParameter(name) ;
        viaParms.add(nv);
    }         
    
        /**
         * Set the sentProtocol member  
         * @param s Protocol to set.
         */
    public void setSentProtocol(Protocol s) {
        sentProtocol = s ;
    }

    	/**
         * set the transport string.
         * @param transport String to set
         */
    public void setTransport(String transport) {
	if (sentProtocol == null) sentProtocol = new Protocol();
	sentProtocol.setTransport(transport);
    }
    
        /**
         * Set the sentBy member  
         * @param s HostPort to set.
         */
    public void setSentBy(HostPort s) { 
        sentBy = s ;
    }
    
        /**
         * Set the viaParms member  
         * @param v NameValuelist to set.
         */
    public void setViaParms(NameValueList v) {
        viaParms = v ;
    }
    
        /**
         * Set the comment member  
         * @param c String to set.
         */
    public void setComment(String c) {
        comment = c ;
    } 
       
}
