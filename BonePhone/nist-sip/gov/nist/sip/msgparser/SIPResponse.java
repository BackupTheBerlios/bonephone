/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
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
* SIP Response structure. The parser fills in this structure.
* @since 0.9
* @version 1.0
* @author M. Ranganathan mailto:mranga@nist.gov
*/

public final class SIPResponse extends SIPMessage implements SIPKeywords {
	protected StatusLine statusLine;



	/** set the status code.
	*@param statusCode is the status code to set.
	*/
	public void setStatusCode( int statusCode) 
	throws IllegalArgumentException {
	    if (statusCode < 100 || statusCode > 800) 
		throw new IllegalArgumentException("bad status code");
	    if (this.statusLine == null) this.statusLine = new StatusLine();
	    this.statusLine.setStatusCode(statusCode);
	}
	
	
	/**
	* Get the status line of the response.
	*@return StatusLine 
	*/
        public StatusLine getStatusLine() { return statusLine; }

	/** Set the reason phrase.
	*@return the reason phrase.
	*@throws IllegalArgumentException if null string 
	*/
	public void setReasonPhrase(String reasonPhrase) {
	    if (reasonPhrase == null) 
		throw new IllegalArgumentException("Bad reason phrase");
	    if (this.statusLine == null) this.statusLine = new StatusLine();
	    this.statusLine.setReasonPhrase(reasonPhrase);
	}

	/** Get the reason phrase.
	*@return the reason phrase.
	*/
	public String getReasonPhrase() {
		if (statusLine == null || statusLine.getReasonPhrase() == null)
			return "";
		else return statusLine.getReasonPhrase();
	}
	
    	/** Return true if the response is a final response.
	*@param rc is the return code.
     	*/
     	public static boolean isFinalResponse(int rc) {
		return rc >= 200 && rc < 700;
     	}
        
        /** Is this a final response?
         *@return true if this is a final response.
         */
        public boolean isFinalResponse() {
            return isFinalResponse(statusLine.getStatusCode());
        }

	/**
	* Set the status line field.
	*@param sl Status line to set.
	*/
	public void setStatusLine(StatusLine sl) { statusLine = sl ; }
        
        public SIPResponse() { super(); }
	/**
	* Print formatting function.
	*/
	public String toString() {
		String superstring =  super.toString();
		stringRepresentation = "";
		sprint(MSGPARSER_PACKAGE + ".SIPResponse");
		sprint("{");
		if (statusLine != null) {
			sprint(statusLine.toString());
		}
		sprint(superstring);
		sprint("}");
		return stringRepresentation;
	}
	/**
	* Make sure that contact headers have only SIP URIs for
	*  2xx responses.
	*/
	protected void checkURI() 
	throws RecognitionException {
		int statusCode = statusLine.getStatusCode();
		if (statusCode >= 200 && statusCode < 300  ) { 
			// 2xx response...
			ContactList clist = this.contactHeaders;
			for ( Contact c = (Contact) clist.next(); 
				c != null; c=(Contact) clist.next()) {
				URI u = c.getAddress().getAddrSpec();
				if (u.getScheme().compareTo(SIP) != 0 ) {
				  throw new RecognitionException
				 ("Only SIP URI allowed in Contact headers !");
				}
			}
		}
	
	}

	/**
	* Check the response structure. Must have from, to CSEQ and VIA
	* headers.
	*/
	protected void checkHeaders() throws SIPParseException {
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
	}

	/**
	*  Encode the SIP Request as a string.
	*/
	
	public String encode() {
		String retval;
		if (statusLine != null) 
		   retval = statusLine.encode() + super.encode();
		else retval = super.encode();
		return retval + Separators.NEWLINE;
	}

	/** Get this message as a list of encoded strings.  
	*@return LinkedList containing encoded strings for each header in
	*   the message.
	*/

	public LinkedList getMessageAsEncodedStrings() {
		LinkedList retval = super.getMessageAsEncodedStrings();
	
		if (statusLine != null) retval.addFirst(statusLine.encode());
		return retval;


	}


	/** Get the specified line as an encoded string.
	*@param index line number to encode.
	*@return the encoded header.
	*/
	public String getMessageAsEncodedStrings(int index) {
	    if (index < 0 ) 
		 throw new IndexOutOfBoundsException();
	    if (index == 0) {
		 if (statusLine != null) return statusLine.encode();
		 else return null;
	    } else {
		LinkedList ll = super.getMessageAsEncodedStrings();
		return (String) ll.get(index - 1);
	    }
	}


	/**
	* Make a clone (deep copy) of this object.
	*@return a deep copy of this object.
	*/

        public Object clone() {
            SIPResponse retval = (SIPResponse) super.clone();
            retval.statusLine = (StatusLine) this.statusLine.clone();
            return retval;
        }
	/**
	* Replace a portion of this response with a new structure (given by
	* newObj). This method finds a sub-structure that encodes to cText
	* and has the same type as the second arguement and replaces this
	* portion with the second argument.
	* @param cText is the text that we want to replace.
	* @param newObj is the new object that we want to put in place of
	* 	cText.
	* @param matchSubstring boolean to indicate whether to match on
	*   substrings when searching for a replacement.
	*/
	public void replace(String cText, GenericObject newObj, 
		boolean matchSubstring ) {
	       if (cText == null || newObj == null) 
		 throw new 
		 IllegalArgumentException("null args!");
		if (newObj instanceof SIPHeader) 
		 throw new 
		 IllegalArgumentException("Bad replacement class " + 
				newObj.getClass().getName());

		if (statusLine != null) 
			statusLine.replace(cText,newObj,matchSubstring);
		super.replace(cText,newObj,matchSubstring);
	}

	/**
	* Compare for equality.
	*@param other other object to compare with.
	*/
	public boolean equals(Object other) {
	    if ( ! this.getClass().equals(other.getClass())) return false;
	    SIPResponse that = (SIPResponse) other;
	    return statusLine.equals(that.statusLine) &&
		    super.equals(other);
	}

	/**
	* Match with a template.
	*@param matchObj template object to match ourselves with (null 
	* in any position in the template object matches wildcard)
	*/
	public boolean match(Object matchObj) {
	   if (matchObj == null) return true;
	   else if ( ! matchObj.getClass().equals(this.getClass())) 
				return false;
	   else if (matchObj == this) return true;
	   SIPResponse that = (SIPResponse) matchObj;
	   StatusLine rline = that.statusLine;
	   if (this.statusLine == null && rline != null) return false;
	   else if (this.statusLine == rline) return super.match(matchObj);
	   else {
		return statusLine.match(that.statusLine) &&
			super.match(matchObj);
	   }

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

	public byte[] encodeAsBytes()  {
		byte[] slbytes = null;
		if (statusLine != null) {
		   try {
		      slbytes = statusLine.encode().getBytes("UTF-8");
		   } catch (UnsupportedEncodingException ex){
			InternalError.handleException(ex);
		   }
	        }
		byte[] superbytes = super.encodeAsBytes();
		byte[] retval = new byte[slbytes.length + superbytes.length];
		int i = 0;
		if (slbytes != null) {
		   for (i = 0; i < slbytes.length; i++) {
			retval[i] = slbytes[i];
		   }
		}

		for ( int j = 0 ; j < superbytes.length; j++, i++) {
			retval[i] = superbytes[j];
		}
		return retval;
	}

}    


