/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov) 			       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.*;
import gov.nist.sip.sipheaders.*;
import java.util.LinkedList;
import antlr.RecognitionException;
import java.io.UnsupportedEncodingException;

/**
* The SIP Request structure-- this belongs to the parser who fills it up.
* @since 0.9
* @version 1.0
* @author M. Ranganathan mailto:mranga@nist.gov
*/
public final class SIPRequest extends SIPMessage   {
	public static final String DEFAULT_USER = IP;
	public static final int	   DEFAULT_TTL  = 1;
	public static final String DEFAULT_TRANSPORT = UDP;
	public static final String DEFAULT_METHOD = INVITE;

        protected RequestLine requestLine;

	/**
	* Replace a portion of this response with a new structure (given by
	* newObj). This method finds a sub-structure that encodes to cText
	* and has the same type as the second arguement and replaces this
	* portion with the second argument.
	* @param cText is the text that we want to replace.
	* @param newObj is the new object that we want to put in place of
	* 	cText.
	*/

        public void replace
		(String ctext, GenericObject newObject, 
		boolean matchSubstring) {
		if (ctext == null || newObject == null)  {
			throw new IllegalArgumentException
				("Illegal argument null");
		}
	     
		requestLine.replace(ctext,newObject,matchSubstring);
		super.replace(ctext,newObject,matchSubstring);
	
	}

	
	public RequestLine getRequestLine() { 
            return requestLine;
        }
        
        public void setRequestLine(RequestLine requestLine) { 
             this.requestLine=requestLine; 
        }

	public SIPRequest() { super(); }
	
	public String toString() {
		String superstring =  super.toString();
		stringRepresentation = "";
		sprint(MSGPARSER_PACKAGE + ".SIPRequest");
		sprint("{");
		if(requestLine != null) sprint(requestLine.toString());
		sprint(superstring);
		sprint("}");
		return stringRepresentation;
	}

	
	/**
	* Check header for constraints. 
	* (1) Invite options and bye requests can only have SIP URIs in the
	* contact headers. 
	* (2) Request must have cseq, to and from and via headers.
	* (3) Method in request URI must match that in CSEQ.
	*/
	protected void checkHeaders() throws SIPParseException {
		/** BUGBUG -- Per sec 2.2 this check should be OK but
		* torture test messages that violate  this check.

		if (requestLine != null && requestLine.method != null) {
		   if (   requestLine.method.compareTo(INVITE) == 0    ||
			  requestLine.method.compareTo(OPTIONS)== 0 ||
			  requestLine.method.compareTo(BYE) == 0  ) {
			ContactBody c;
			for (c = (ContactBody) this.contactHeaders.next(); 
				c != null; c = (ContactBody) 
				this.contactHeaders.next()) {
				URI u = c.address.addrSpec;
				if (u.scheme.compareTo(SIP) != 0 ) {
				   SIPIllegalMessageException ex = 
				     new SIPIllegalMessageException
				        ("ONLY SIP URL allowed here" );
				    ex.setHeader(c.getContactList().
						getTrimmedInputText());
				    throw ex;
				}
			}
		   } 
		}

		if (requestLine != null && requestLine.getUri() != null &&
		    requestLine.getUri().getScheme().compareTo(getToHeader().
		    getAddress().getAddrSpec().getScheme()) != 0) {
			SIPIllegalMessageException ex = 
		            new SIPIllegalMessageException
				("URI Scheme Mismatch with Request-Line ");
			ex.setHeader(getToHeader().getTrimmedInputText());
			throw ex;
	        }

		**/

		/* Check for required headers */

		if (getCSeqHeader() == null) {
			throw new SIPMissingHeaderException(CSEQ);
		}
		if (getToHeader() == null) {
			throw new SIPMissingHeaderException(TO);
		}
		if (getFromHeader() == null) {
			throw new SIPMissingHeaderException(FROM);
		}
		if (getViaHeaders() == null) {
			throw new SIPMissingHeaderException(VIA);
		}

		/*  BUGBUG
		* Need to revisit this check later... 
                * for now we just leave this to the
 		* application to catch.
	        *
		* if ( requestLine != null && requestLine.getMethod() != null &&
		*      getCSeqHeader().getMethod() != null &&
		*	requestLine.getMethod().compareTo
		*	(getCSeqHeader().getMethod()) != 0 ) {
		*	SIPIllegalMessageException ex = 
		*            new SIPIllegalMessageException
		*		("CSEQ method mismatch with  Request-Line ");
		*	ex.setHeaderText(getCSeqHeader().getInputText());
		*	ex.setHeader(getCSeqHeader());
	 	*  	throw ex;
	        * }
		*/
			
	}
		
	/**
	* Set the default values in the request URI if necessary.
	*/
	protected void setDefaults() {
		// The request line may be unparseable (set to null by the
		// exception handler.
		if (requestLine == null) return;
		String method = requestLine.getMethod();
		// The requestLine may be malformed!
		if (method == null) return;
		URI u = requestLine.getUri();
		if (u == null) return;
		if (method.compareTo(REGISTER) == 0 
			|| method.compareTo(INVITE) == 0) {
			if (u.getScheme().compareTo(SIP) == 0 ) {
				u.setDefaultParm(USER, DEFAULT_USER);
				u.setDefaultParm(METHOD, DEFAULT_METHOD);
				// u.setDefaultParm(TTL,
			    	//  new Integer(DEFAULT_TTL));
				u.setDefaultParm(TRANSPORT,DEFAULT_TRANSPORT);
			}
		}
	}

	/**
	* Patch up the request line as necessary.
	*/
	protected void setRequestLineDefaults() {
		String method = requestLine.getMethod();
		if (method == null) {
		  CSeq cseq = this.getCSeqHeader();
		  if (cseq != null) {
		      method = cseq.getMethod();
		      requestLine.setMethod(method);
		  }
		}
	}
	
	/**
	* A conveniance function to access the Request URI.
	*@return the requestURI if it exists.
	*/
	public URI getRequestURI() {
	        if (this.requestLine == null) return null;
		else return this.requestLine.getUri();
	}
	

	/**
	* Set the requestURI.
	*@param uri is the request URI to set.
	*@throws IllegalArgumentException if the uri is null
	*/
	public void setRequestURI(URI uri) {
		if (this.requestLine == null) {
			this.requestLine = new RequestLine();
		}
		this.requestLine.setUri(uri);
	}

	/** Set the method.
	*@param method is the method to set.
	*@throws IllegalArgumentException if the method is null
	*/
	public void setMethod(String method) {
		if (method == null) 
		   throw new IllegalArgumentException("null method");
		if (this.requestLine == null) {
			this.requestLine = new RequestLine();
		}
		this.requestLine.setMethod(method);
	}

	/** Get the method from the request line.
	*@return the method from the request line if the method exits and
	* null if the request line or the method does not exist.
	*/
	public String getMethod() {
		if (requestLine == null) return null;
		else return requestLine.getMethod();
	}
		


	
	/**
	* Generate a branch identifier (for forwarding requests). The
	* server needs to put this through a message digest to generate
	* the actual branch identifier. Here is the relevant text from
	* the RFC 2543 (revision bis 02) which this API addresses:
	*
	* The "branch" parameter is included by every proxy. The token MUST be
	* the parameter SHOULD consist of two parts separable by the 
	* implementation. One part, used for loop detection (Section 12.3.1), 
	* MAY be computed as a cryptographic hash of the To, From, 
	* Call-ID header fields, the Request-URI of the request received 
	* (before translation) and the sequence number from the CSeq header 
	* field. The algorithm used to compute the hash is 
	* implementation-dependent, but MD5 [36], expressed in hexadecimal, 
	* is a reasonable choice.
	*
	* @return String containing the generated branch identifier.
	*
	*/
	public  String getBranchIdentifier() {
		URI requestURI = this.requestLine.getUri();
		CallID callid = this.getCallIdHeader();
		CSeq cseq = this.getCSeqHeader();
		From from = this.getFromHeader();
		To   to   = this.getToHeader();
		return requestURI.getAuthority().encode() + ":"
		        + callid.getCallID() + ":"
		        + from.getAddress().encode() + ":"
			+ to.getAddress().encode() + ":"
			+ cseq.getSeqno();
			
		
	}
	
	/**
	*  Encode the SIP Request as a string.
	*/
	
	public String encode() {
		String retval;
		if (requestLine != null)  {
		    this.setRequestLineDefaults();
		    retval = requestLine.encode() + super.encode();
		}
		else retval = super.encode();
		return retval + Separators.NEWLINE;
	}

	/**
	* Make a clone (deep copy) of this object.
	*/

        public Object clone() {
            SIPRequest retval = (SIPRequest) super.clone();
	    if (this.requestLine != null) {
              retval.requestLine = (RequestLine) this.requestLine.clone();
	      retval.setRequestLineDefaults();
	    }
            return retval;
        }
	
	/**
	* Compare for equality.
	*@param other object to compare ourselves with.
	*/
	public boolean equals(Object other) {
	    if ( ! this.getClass().equals(other.getClass())) return false;
	    SIPRequest that = (SIPRequest) other;
	    return requestLine.equals(that.requestLine) &&
		    super.equals(other);
	}
	
	/**
	* Get the message as a linked list of strings.
	*/
	public LinkedList getMessageAsEncodedStrings() {
		LinkedList retval = super.getMessageAsEncodedStrings();
		if (requestLine != null)  {
		       this.setRequestLineDefaults();
			retval.addFirst(requestLine.encode());
		}
		return retval;

	}

	/** Get a specific header as an encoded string.
	*@param linenum is the line number of the message.
	*/
	public String getMessageAsEncodedStrings(int linenum) {
	    if (linenum < 0 ) 
		 throw new IndexOutOfBoundsException();
	    if (linenum == 0) {
		 if (requestLine != null) return requestLine.encode();
		 else return null;
	    } else {
		LinkedList ll = super.getMessageAsEncodedStrings();
		return (String) ll.get(linenum - 1);
	    }
	}


	/**
	* Match with a template.
	*@param matchObj object to match ourselves with (null matches wildcard)
	*/
	public boolean match(Object matchObj) {
	   if (matchObj == null) return true;
	   else if ( ! matchObj.getClass().equals(this.getClass())) 
				return false;
	   else if (matchObj == this) return true;
	   SIPRequest that = (SIPRequest) matchObj;
	   RequestLine rline = that.requestLine;
	   if (this.requestLine == null && rline != null) return false;
	   else if (this.requestLine == rline) return super.match(matchObj);
	   return requestLine.match(that.requestLine) && super.match(matchObj);

	}

	/** Generate an ID for cancel requests.
	* This generates an id for matching cancel requests on the basis of
	* the RFC.
	*
	* Important Note: this must match the 
	* <b> sipstack.SIPMessageFormatter.getCancelID()</b> algorithm exactly.
	*
	*/

	public String getCancelID() {
		CallID cid = this.getCallIdHeader();
		CSeq cseq = this.getCSeqHeader();
		From from = this.getFromHeader();
		To  to = this.getToHeader();
		URI requestURI = this.getRequestURI();
		Via via = (Via) this.getViaHeaders().getFirst();
		int port = via.hasPort() ? via.getPort():
			    5060; // DEFAULT port.
		String encoding = via.getHost() + COLON + port;
		if (via.getBranch() != null) {
			encoding += COLON + via.getBranch();
		}
		

		return  ( cid.getCallID() + COLON + 
			to.getHostPort().encode() + COLON +
			from.getHostPort().encode() + COLON +
			encoding + COLON + cseq.getSeqno() ).toLowerCase();
			
	
	}

	/** Generate an ID for BYE request (call leg idenifier).
	* This generates an id for matching BYE requests on the basis of
	* the RFC.
	*
	* Important Note: this must match the 
	* <b> sipstack.SIPMessageFormatter.getCallLegID()</b> 
	* algorithm exactly.
	*
	*/

	public String getCallLegID() {
		CallID cid = this.getCallIdHeader();
		CSeq cseq = this.getCSeqHeader();
		From from = this.getFromHeader();
		To  to = this.getToHeader();
		URI requestURI = this.getRequestURI();
		Via via = (Via) this.getViaHeaders().getFirst();
		int port = via.hasPort() ? via.getPort():
			    5060; // DEFAULT port.
		String encoding = via.getHost() + COLON + port;
		if (via.getBranch() != null) {
			encoding += COLON + via.getBranch();
		}
		
	        String retval = cid.getCallID();
		if (from.getTag() != null) retval += COLON + from.getTag();
		if (to.getTag() != null)    retval += COLON + to.getTag();
	        return retval;
	}

	/** Encode this into a byte array.
	* This is used when the body has been set as a binary array 
	* and you want to encode the body as a byte array for transmission.
	*
	*@return a byte array containing the SIPRequest encoded as a byte
	*  array.
	*@throws UnsupportedEncodingException if the request cannot be
	* encoded into UTF-8 format.
	*/

	public byte[] encodeAsBytes() {
		byte[] rlbytes = null;
		if (requestLine != null) {
		   try {
		      rlbytes = requestLine.encode().getBytes("UTF-8");
		   } catch (UnsupportedEncodingException ex) {
			InternalError.handleException(ex);
		   }
	        }
		byte[] superbytes = super.encodeAsBytes();
		byte[] retval = new byte[rlbytes.length + superbytes.length];
		int i = 0;
		if (rlbytes != null) {
		   for (i = 0; i < rlbytes.length; i++) {
			retval[i] = rlbytes[i];
		   }
		}

		for (int  j = 0 ; j < superbytes.length; j++, i++) {
			retval[i] = superbytes[j];
		}
		return retval;
	}


		


}
