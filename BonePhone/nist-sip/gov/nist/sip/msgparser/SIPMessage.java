/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD)        *
 * See ../../../../doc/uncopyright.html for conditions of use                  *
 * Author: M. Ranganathan (mranga@nist.gov)                                    *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                              *
 * Important bug fixes submitted by Chris Mills (Nortel Networks)	       *
 ******************************************************************************/
package gov.nist.sip.msgparser;
import java.io.UnsupportedEncodingException;
import gov.nist.sip.net.*;
import java.util.*;
import java.lang.reflect.*;
import gov.nist.sip.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.log.LogWriter;

/**
 * This is the main SIP Message structure.
 * The fields are extracted from the headers by the parser and
 * are available to the Server/Client for further processing.
 * The fields correspond to RFC 2616 BIS and need no further explanation.
 * The parser is invoked from either a StringMsgParser or a
 * PipelinedMsg parser.
 * The pipelined parser is meant to be used when reading from a stream
 * input such as a tcp connection.
 * The String message parser is meant to be used when parsing string input
 * from memory
 * @since 0.9
 * @see StringMsgParser
 * @see PipelinedMsgParser
 * @author M. Ranganathan
 * @version 1.0
 */

public abstract class SIPMessage extends MessageObject
implements SIPKeywords, PackageNames  {

    protected static final String DEFAULT_ENCODING = "UTF-8";

/** List of headers (in the order they were added)
 */
    protected SIPHeaderList    		 headers;
    
    // Mutltiple headers of the following kinds can legally appear in
    // a message.
    protected WWWAuthenticateList	wwwAuthenticateHeaders;
    protected AcceptList		acceptHeaders;
    protected AcceptEncodingList	acceptEncodingHeaders;
    protected AcceptLanguageList	acceptLanguageHeaders;
    protected ProxyRequireList		proxyRequireHeaders;
    protected RouteList			routeHeaders;
    protected RequireList		requireHeaders;
    protected WarningList		warningHeaders;
    protected UnsupportedList		unsupportedHeaders;
    protected AlertInfoList		alertInfoHeaders;
    protected CallInfoList		callInfoHeaders;
    protected ProxyAuthenticateList	proxyAuthenticateHeaders;
    protected AllowList			allowHeaders;
    protected RecordRouteList		recordRouteHeaders;
    protected ViaList			viaHeaders;
    protected ContentEncodingList	contentEncodingHeaders;
    protected ContactList		contactHeaders;
    protected ContentLanguageList	contentLanguageHeaders;
    protected InReplyToList		inReplyToHeaders;
    protected AlsoList			alsoHeaders;
    protected ErrorInfoList		errorInfoHeaders;
    protected SupportedList		supportedHeaders;
    
    // The following headers are singletons in a message.
    protected MimeVersion		mimeVersionHeader;
    protected UserAgent			userAgentHeader;
    protected Authorization		authorizationHeader;
    protected Hide			hideHeader;
    protected MaxForwards		maxForwardsHeader;
    protected Organization		organizationHeader;
    protected Priority			priorityHeader;
    protected ProxyAuthorization	proxyAuthorizationHeader;
    protected ResponseKey		responseKeyHeader;
    protected Subject	  		subjectHeader;
    protected CallID	  		callIdHeader;
    protected CSeq			cSeqHeader;
    protected SIPDateHeader		dateHeader;
    protected Encryption		encryptionHeader;
    protected Expires			expiresHeader;
    protected From			fromHeader;
    protected TimeStamp			timestampHeader;
    protected To			toHeader;
    protected ContentLength		contentLengthHeader;
    protected ContentType		contentTypeHeader;
    protected RetryAfter		retryAfterHeader;
    protected Server			serverHeader;
    
    // Payload
    protected String			messageContent;
    protected byte[]			messageContentBytes;

	
    
    // Ptr to parsed sdp structure (If there is an sdp announce part
    // of the message).
    protected SDPAnnounce		sdpAnnounce;
    
    // A table of extension headers indexed by class.
    private Hashtable	 	extensionTable;
    // Table of headers indexed by name.
    private Hashtable		nameTable;
    
    // A table for quick lookup of field offset by type
    // AKA lazy man's approach to dealing with 50 odd headers.
    private Hashtable 	 	fieldHash;
    private Field[]	 	myFields;

	/** get the headers as a linked list of encoded Strings 
	*@return a linked list with each element of the list containing a
	* string encoded header in canonical form.
	*/
	public LinkedList getMessageAsEncodedStrings() {
		LinkedList retval = new LinkedList();
		ListIterator li = headers.listIterator();
		while (li.hasNext()) {
		    SIPHeader sipHeader = (SIPHeader) li.next();
	 	    if (sipHeader instanceof SIPHeaderList)  {
	 		SIPHeaderList shl = (SIPHeaderList) sipHeader;
			retval.addAll(shl.getHeadersAsEncodedStrings());
		    } else  {
		      retval.add(sipHeader.encode());
		    }
		}
		return retval;
	}

	/** Get a given line of the message as an encoded string.
	*/
	public abstract String getMessageAsEncodedStrings(int line);

	/**
	* Template match for SIP messages.
        * The matchObj is a SIPMessage template to match against. 
        * Null matches wild card. 
        * 
	*/
	public boolean match(Object other) {
                if (other == null) return true;
		if (! other.getClass().equals(this.getClass())) return false;
		SIPMessage matchObj  = (SIPMessage) other;
		SIPHeaderList matchHeaders = matchObj.headers;
                boolean retval =  headers.match(matchHeaders);
                if (sdpAnnounce != null ) return retval && 
                    sdpAnnounce.match(matchObj.sdpAnnounce);
                else return retval;

	}
    
        /**
         * Recursively replace a portion of this object with a new Object.
         * You cannot use this function for replacing sipheaders in
         * a message (for that, use the remove and attach functions).
         * Its intended use is for global find and replace of poritons of
         * headers such as addresses.
         * @param cText canonical representation of object that has to be
         * 	replaced.
         * @param newObject object that replaces the object that has the
         * 	text cText
         * @param matchSubstring if true then if cText is a substring of the
         * encoded text of the Object then a match is flagged.
         * @exception IllegalArgumentException on null args and if
         * replacementObject does not derive from GenericObject or
         * GenericObjectList
         */
    public void replace(String cText, GenericObject newObject,
    boolean matchSubstring )
    throws IllegalArgumentException {
        SIPHeader siphdr;
        if (cText == null || newObject == null) {
            throw new IllegalArgumentException("null arguments");
        }
        if (getClassFromName(SIPHEADERS_PACKAGE + ".SIPHeader").
        isAssignableFrom(newObject.getClass()))  {
            throw new IllegalArgumentException
            ("Cannot replace object of class" + newObject.getClass());
        } else if (getClassFromName(SIPHEADERS_PACKAGE + ".SIPHeaderList").
        isAssignableFrom(newObject.getClass()))  {
            throw new IllegalArgumentException
            ("Cannot replace object of class " + newObject.getClass());
        } else {
            // not a sipheader or a sipheaderlist so do a find and replace.
            for (siphdr = (SIPHeader) this.headers.first(); siphdr != null;
            siphdr = (SIPHeader) this.headers.next() ) {
                siphdr.replace(cText,newObject,matchSubstring);
            }
        }
    }
    
        /**
         * Recursively replace a portion of this object with a new  Object.
         * You cannot use this function for replacing sipheaders in
         * a message (for that, use the remove and attach functions).
         * Its intended use is for global find and replace of poritons of
         * headers such as addresses.
         * @param cText canonical representation of object that has to be
         * 	replaced.
         * @param newObject object that replaces the object that has the
         * 	text cText
         * @param matchSubstring if true then flag a match if cText is a
         * substring of the encoded text of the object.
         * @exception IllegalArgumentException on null args and if
         *  replacementObject does not derive from GenericObject or
         *  GenericObjectList
         */
    public void replace(String cText, GenericObjectList newObject,
    boolean matchSubstring )
    throws IllegalArgumentException {
        SIPHeader siphdr;
        if (cText == null || newObject == null) {
            throw new IllegalArgumentException("null arguments");
        }
        if (getClassFromName(SIPHEADERS_PACKAGE + ".SIPHeaderList").
        isAssignableFrom(newObject.getClass()))  {
            throw new IllegalArgumentException
            ("Cannot replace object of class " + newObject.getClass());
        } else if (getClassFromName(SIPHEADERS_PACKAGE + ".SIPHeader").
        isAssignableFrom(newObject.getClass()))  {
            throw new IllegalArgumentException
            ("Cannot replace object of class " + newObject.getClass());
        } else {
            // not a sipheader.
            for (siphdr = (SIPHeader) this.headers.first(); siphdr != null;
            siphdr = (SIPHeader) this.headers.next() ) {
                siphdr.replace(cText,newObject,matchSubstring);
            }
        }
    }
    
        /**
         * Encode this message as a string. This is more efficient when 
         * the payload is a string (rather than a binary array of bytes).
         * If the payload cannot be encoded as a UTF-8 string then it is
	 * simply ignored (will not appear in the encoded message).
         * @return The Canonical String representation of the message
         * (including the canonical string representation of
         * the SDP payload if it exists).
         */
    public String encode() {
        String encoding = "";
	ListIterator it = this.headers.listIterator();

        while (it.hasNext())  {
             SIPHeader siphdr = (SIPHeader) it.next();
             if (! (siphdr instanceof ContentLength)  ) 
	        encoding += siphdr.encode();
        }



        if (this.sdpAnnounce != null) {
            String mbody = sdpAnnounce.encode();
            encoding += SIPHeaderNames.CONTENT_LENGTH + Separators.COLON +
            Separators.SP + mbody.length() + Separators.NEWLINE;
            encoding += Separators.NEWLINE;
            encoding += mbody;
        } else if (this.messageContent != null || 
		   this.messageContentBytes != null  ) {
	    String content = null;
	    try {
	      content = getMessageContent();
	    } catch (UnsupportedEncodingException ex) {
		content = "";
	    }
	    // Add the content-length header
            encoding += SIPHeaderNames.CONTENT_LENGTH + Separators.COLON +
            		Separators.SP + content.length() + Separators.NEWLINE;
	    // Append the content 
	    encoding += Separators.NEWLINE;
            encoding += content;
        }else  {
	    // Message content does not exist.
            encoding += SIPHeaderNames.CONTENT_LENGTH + Separators.COLON +
            Separators.SP + '0' + Separators.NEWLINE;
            encoding += Separators.NEWLINE;
        }
        return encoding;
    }
	/** Encode the message as a byte array.
	 * Use this when the message payload is a binary byte array.
         * @return The Canonical byte array representation of the message
         * (including the canonical byte array representation of
         * the SDP payload if it exists).
	 * @throws UnsupportedEncodingException if the body was specified
	 *  as a string with an encoding that is not supported on this 
	 *   platform.
         */
	public byte[] encodeAsBytes() {
	    String encoding = "";
	    ListIterator it = this.headers.listIterator();

            while (it.hasNext())  {
                SIPHeader siphdr = (SIPHeader) it.next();
                if (! (siphdr instanceof ContentLength)  ) 
	        encoding += siphdr.encode();
	     
            }
	    byte[] retval = null;
            if (this.sdpAnnounce != null) {
               String mbody = sdpAnnounce.encode();
               encoding += SIPHeaderNames.CONTENT_LENGTH + Separators.COLON +
               Separators.SP + mbody.length() + Separators.NEWLINE;
               encoding += Separators.NEWLINE;
               encoding += mbody;
	       try {
	          retval = encoding.getBytes("UTF-8");
		} catch (UnsupportedEncodingException ex) {
		   InternalError.handleException(ex);
		}
           } else if (this.messageContent != null || 
		   this.messageContentBytes != null  ) {
	       byte[] content =  getContentAsBytes();
	       // Add the content-length header
               encoding += SIPHeaderNames.CONTENT_LENGTH + Separators.COLON +
            		Separators.SP + content.length + Separators.NEWLINE;
	       encoding += Separators.NEWLINE;
	       // Append the content 
	       byte[] msgarray = null;
	       try {
	            msgarray = encoding.getBytes("UTF-8");
		} catch (UnsupportedEncodingException ex) {
			InternalError.handleException(ex);
		}
		
	       retval = new byte[msgarray.length + content.length];
	       int i;
	       for (i = 0; i < msgarray.length; i++) {
		   retval[i] = msgarray[i];
	       }
	       for (int j = 0, k = i ; 
		    k < msgarray.length + content.length; k++ , j++) {
		    retval[k] = content[j];
	       }
           } else  {
	      // Message content does not exist.
              encoding += SIPHeaderNames.CONTENT_LENGTH + Separators.COLON +
              Separators.SP + '0' + Separators.NEWLINE;
              encoding += Separators.NEWLINE;
	      try {
	        retval = encoding.getBytes("UTF-8");
	      } catch (UnsupportedEncodingException ex) {
			InternalError.handleException(ex);
	     }
           }
	   return retval;
	}
    
        /**
         * clone this message (create a new physical copy). All headers in the
         * message are cloned. You can modify the cloned copy without affecting
         * the original.
         * @return A cloned copy of this object.
         */
    public Object clone() {
        SIPMessage retval = null;
        try {
            retval = (SIPMessage) this.getClass().newInstance();
            retval.headers = new SIPHeaderList("MessageHeaders",
            SIPHEADERS_PACKAGE + ".SIPHeader",  null );
            retval.extensionTable = new Hashtable();
	    retval.nameTable = new Hashtable();
        } catch ( IllegalAccessException ex) {
            InternalError.handleException(ex);
        } catch (InstantiationException ex) {
            InternalError.handleException(ex);
        }
        Class sipmessageClass = getClassFromName
        (MSGPARSER_PACKAGE + ".SIPMessage");
        retval.inputText = this.inputText;
        Field fields[] = sipmessageClass.getDeclaredFields();
        Class siphdrlistClass = getClassFromName(SIPHEADERS_PACKAGE +
        ".SIPHeaderList");
        Class siphdrClass = getClassFromName(SIPHEADERS_PACKAGE +
        ".SIPHeader");
        
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            int modifier =  f.getModifiers();
            if (Modifier.isPrivate(modifier)) {
		continue;
	   } else if (Modifier.isStatic(modifier)) {
		continue;
	    } else if (Modifier.isInterface(modifier)) {
		continue;
	    }
            try {
                Object obj = f.get(this);
                if (obj == null) continue;
		if (f.getName().equals("headers")) continue;
		Object cloneObj = GenericObject.makeClone(obj);
		
                if (siphdrClass.isAssignableFrom(obj.getClass())) {
		    SIPHeader sipHdr = (SIPHeader) cloneObj;
		    retval.attachHeader(sipHdr);
                } else {
		    f.set(retval,cloneObj);
                }
            } catch ( IllegalAccessException ex) {
		System.out.println("Error accessing field " + f.getName());
                InternalError.handleException(ex);
            }
        }
        if (this.sdpAnnounce != null) {
            retval.sdpAnnounce = (SDPAnnounce)this.sdpAnnounce.clone();
        }
        Enumeration records = extensionTable.elements();
        while(records.hasMoreElements()) {
            Object extObj = records.nextElement();
            if (extObj instanceof SIPHeaderList) {
                SIPHeaderList extHdrList = (SIPHeaderList) extObj;
                retval.attachHeader((SIPHeaderList)extHdrList.clone());
            }else if (extObj instanceof SIPHeader) {
                SIPHeader extHdr = (SIPHeader) extObj;
                retval.attachHeader((SIPHeader)extHdr.clone());
            }
        }
        return retval;
    }
    
        /**
         * Get the string representation of this header (for pretty printing the
         * generated structure).
         * @return Formatted string representation of the object.
         */
    
    public String toString() {
        stringRepresentation = "";
        sprint("SIPMessage:");
        sprint("{");
        sprint("inputText:");
        sprint(inputText);
        try {
            
            Field[] fields = this.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields [i];
                Class fieldType = f.getType();
                String fieldName = f.getName();
                if (f.get(this) != null  &&
                Class.forName(SIPHEADERS_PACKAGE + ".SIPHeader").
                isAssignableFrom(fieldType) &&
                fieldName.compareTo("headers") != 0 ) {
                    sprint(fieldName + "=");
                    sprint(f.get(this).toString());
                }
            }
        } catch ( Exception ex ) {
            InternalError.handleException(ex);
        }
        
        
        sprint("List of headers : ");
        sprint(headers.toString());
        sprint("messageContent = ");
        sprint("{");
        sprint(messageContent);
        sprint("}");
        if (sdpAnnounce != null) {
            sprint(sdpAnnounce.toString());
        }
        sprint("}");
        return stringRepresentation;
    }
    
    
        /**
         * Internal class used to track fields of the sip message
         * (we do a type based match for assignment).
         */
    
    class HdrField {
        int offset;
        boolean isList;
        SIPHeader header;
        HdrField( int off, boolean l ) {
            offset = off; isList = l; header = null;
        }
    }
    
    
        /**
         * Constructor: Initializes lists and list headers.
         * All the headers for which there can be multiple occurances in
         * a message are  derived from the SIPHeaderListClass
         * @since 0.9
         */
    public SIPMessage() {
        inputText = null;
        headers = new SIPHeaderList("MessageHeaders",
        SIPHEADERS_PACKAGE + ".SIPHeader",  null );
        
        
        // Build a hash table based on the type of header for quick
        // type based lookup.
        fieldHash = new Hashtable();
        // Build a table where extension headers  stored by classname
        // for a quick lookup.
        extensionTable = new Hashtable();
	nameTable = new Hashtable();
        try {
            Class sipmessageClass =
            getClassFromName(MSGPARSER_PACKAGE + ".SIPMessage");
            myFields = sipmessageClass.getDeclaredFields();
            for ( int i = 0 ; i < myFields.length; i++) {
                boolean isList = false;
                // Find the type that matches our header type.
                Class cls = myFields[i].getType();
                if ( getClassFromName(SIPHEADERS_PACKAGE+
                ".SIPHeaderList").
                isAssignableFrom(cls) ) {
                    HdrField hf = new HdrField(i,true);
                    hf.header = (SIPHeaderList) myFields[i].
                    get(this);
                    fieldHash.put(cls.getName(), hf );
                } else  if
                ( getClassFromName(SIPHEADERS_PACKAGE+".SIPHeader")
                .isAssignableFrom(cls)) {
                    fieldHash.put
                    (cls.getName(), (new HdrField(i,false)));
                }
            }
        } catch (IllegalAccessException ex) {
            InternalError.handleException(ex);
        }
        
    }
    
    
   /**
    * Attach a header and die if you get a duplicate header exception.
    * @param h SIPHeader to attach.
    */
    private void attachHeader (SIPHeader h) {
        if (h == null) throw new IllegalArgumentException("null header!");
        try {
            if (h instanceof SIPHeaderList) {
                SIPHeaderList hl = (SIPHeaderList) h;
                if (hl.isEmpty()) return;
            }
            attachHeader(h,false,false);
        } catch ( SIPDuplicateHeaderException ex) {
            InternalError.handleException(ex);
        }
    }

	/** Add this header to the list of already existing headers.
	*/
	private void addNameTable( SIPHeader h, boolean top ) {
		LinkedList llist = (LinkedList) nameTable.get
			(h.getHeaderName().toLowerCase());
		if (llist == null) {
			llist = new LinkedList();
			nameTable.put(h.getHeaderName().toLowerCase(),llist);
		 }
			
	         if (h instanceof SIPHeaderList) {
			SIPHeaderList shl = (SIPHeaderList)h;
		        SIPHeader[] headers = shl.toArray();
			for (int i = 0 ; i < headers.length; i++) {
			    if (top) llist.addFirst(headers[i]);
			    else llist.add(headers[i]);
			}
		 }  else {
		        if (top) llist.addFirst(h);
		        else llist.add(h);
		 }
	}

		

    
        /**
         * Attach a header (replacing the original header).
         * @param header Header that replaces a header of the same type.
         */
    public void setHeader(SIPHeader header) {
        if (header == null)
            throw new IllegalArgumentException("null header!");
        try {
            if (header instanceof SIPHeaderList) {
                SIPHeaderList hl = (SIPHeaderList) header;
                // Ignore empty lists.
                if (hl.isEmpty()) return;
            }
            this.removeAll(header.getClass());
	    this.removeAll(header.getHeaderName());
            attachHeader(header,true,false);
        } catch ( SIPDuplicateHeaderException ex) {
            InternalError.handleException(ex);
        }
    }
    
    
        /**
         * Attach a header to the end of the existing headers in
         * this SIPMessage structure.
         * This is equivalent to the attachHeader(SIPHeader,replaceflag,false);
         * which is the normal way in which headers are attached.
         * This was added in support of JAIN-SIP.
         * @since 1.0 (made this public)
         * @param h header to attach.
         * @param replaceflag if true then replace a header if it exists.
         * @throws SIPDuplicateHeaderException If replaceFlag is false and
         * only a singleton header is allowed (fpr example CSeq).
         */
    public void attachHeader( SIPHeader h, boolean replaceflag )
    throws SIPDuplicateHeaderException {
        this.attachHeader(h, replaceflag, false);
    }
        /**
         * Attach the header to the SIP Message structure at a specified
         * position in its list of headers.
         * @since 1.0
         * @param header Header to attach.
         * @param replaceFlag If true then replace the existing header.
         * @param index Location in the header list to insert the header.
         * @exception SIPDuplicateHeaderException if the header is of a type
         * that cannot tolerate duplicates and one of this type already exists
         * (e.g. CSeq header).
         * @throws IndexOutOfBoundsException If the index specified is
         * greater than the number of headers that are in this message.
         */
    
    public void attachHeader( SIPHeader header,
    boolean replaceFlag, int  index )
    throws SIPDuplicateHeaderException, IndexOutOfBoundsException {
        if (header == null) {
            Debug.print("******Ignoring extension header******");
            return;
        }
        
        if (index < 0 ) throw new IndexOutOfBoundsException("Bad index!");
        Class hdrListClass =
        getClassFromName(SIPHEADERS_PACKAGE+
        ".SIPHeaderList");
        SIPHeader h;
        
        if (ListMap.hasList(header)  &&
        ! hdrListClass.isAssignableFrom(header.getClass())) {
            SIPHeaderList hdrList = ListMap.getList(header);
            hdrList.add(header);
            h = hdrList;
        } else {
            h = header;
        }
        
        Class cl = h.getClass();
        HdrField hField = (HdrField) fieldHash.get(cl.getName());
        if (hField == null) {
            // This must be an extension header.
            SIPHeader extHdr =
            (SIPHeader) extensionTable.get(cl.getName());
            // Extension of this name has not yet been registered
            if (extHdr == null || replaceFlag) {
                extensionTable.put(cl.getName(),h);
                headers.add(index,h);
            } else {
                if ( hdrListClass.isAssignableFrom(cl) ) {
                    SIPHeaderList extHdrList = (SIPHeaderList) extHdr;
                    headers.add(index,h);
                    extHdrList.concatenate((SIPHeaderList) h, false);
                } else {
                    SIPDuplicateHeaderException ex =
                    new SIPDuplicateHeaderException
                    ("Duplicate extension hdr");
                    ex.setHeaderText(h.getTrimmedInputText());
                    ex.setHeaderName(h.getHeaderName());
                    ex.setSIPMessage(this);
                    ex.setHeader(h);
                    throw ex;
                }
            }
        } else {
            // This is not an extension header.
            int i = hField.offset;
            try {
                if ( hField.isList) {
                    SIPHeaderList hl = (SIPHeaderList) h;
                    SIPHeaderList hlist =
                    (SIPHeaderList) myFields[i].get(this);
                    if (hlist != null)  {
                        hlist.concatenate(hl, false );
                    } else {
                        myFields[i].set(this,h);
                    }
                    headers.add(index,h);
                } else if (myFields[i].get(this) == null
                || replaceFlag )  {
                    // Field has not been set
                    headers.add(index,h);
                    myFields[i].set(this,h);
                    hField.header = h;
                } else {
                    SIPDuplicateHeaderException ex =
                    new SIPDuplicateHeaderException
                    ("Duplicate header in message ");
                    ex.setHeaderText(h.getTrimmedInputText());
                    ex.setHeaderName(h.getHeaderName());
                    ex.setHeaderText(h.getTrimmedInputText());
                    ex.setSIPMessage(this);
                    ex.setHeader(h);
                    throw ex;
                }
            } catch (IllegalAccessException ex) {
                InternalError.handleException(ex);
            }
        }
        
    }
    
        /** Attach the header to the SIP Message structure
         * @since 1.0 (made this public in support of JAIN-SIP).
         * @param top is a boolean field that indicates whether to add to the
         * 	start or the end of the header list.
         * @param header Header to attach.
         * @param replaceflag If true, then replace header if it exists.
         * @throws SIPDuplicateHeaderException If replaceFlag
         * is false and only a singleton header
         * of this kind is allowed.
         */
    
    public void attachHeader( SIPHeader header,
    boolean replaceflag, boolean top )
    throws SIPDuplicateHeaderException {
        if (header == null) {
            Debug.print("******Ignoring extension header******");
            return;
        }
        // Add to linear list of headers that we keep for serial access.
        Class hdrListClass =
          getClassFromName(SIPHEADERS_PACKAGE+ ".SIPHeaderList");
	if (hdrListClass.isAssignableFrom(header.getClass())) {
	   SIPHeaderList hlist = (SIPHeaderList) header;
	   if (hlist.isEmpty()) {
		throw new IllegalArgumentException("Empty Hdr list " + 
			header.getClass());
	   }
	}
		
        SIPHeader h;
	SIPHeader nameTableEntry;
        
        // Check if multiple of such headers can be supported in a message
        // and if this header is not in the form of a list, allocate a list
        // and insert it into the list.
        if (ListMap.hasList(header)
        && ! hdrListClass.isAssignableFrom(header.getClass())) {
	    // Construct a list of the given type of header.
            SIPHeaderList hdrList = ListMap.getList(header);
	    // Add this header to the list.
            hdrList.add(header);
	    hdrList.setHeaderName(header.getHeaderName());
            h = hdrList;
        } else {
            h = header;
        }
        Class cl = h.getClass();
        HdrField hField = (HdrField) fieldHash.get(cl.getName());
        if (hField == null) {
            // This must be an extension header.
            SIPHeader extHdr =
            (SIPHeader) extensionTable.get(cl.getName());
            // Extension of this name has not yet been registered
            if (extHdr == null || replaceflag) {
               // Field has not been set
	        SIPHeader hdr = (SIPHeader) extensionTable.get(cl.getName());
	        if (hdr != null) {
		  headers.remove(hdr);
	        }
                extensionTable.put(cl.getName(),h);
                if (!top ) headers.add(h);
                else headers.addFirst(h);
		addNameTable(h,top);
            } else {
                if ( hdrListClass.isAssignableFrom(cl) ) {
                    SIPHeaderList extHdrList = (SIPHeaderList) extHdr;
                    extHdrList.concatenate((SIPHeaderList) h, top );
                    // if (!top ) headers.add(h);
                    // else headers.addFirst(h);
		    addNameTable(h,top);
                } else {
                    SIPDuplicateHeaderException ex =
                    new SIPDuplicateHeaderException
                    ("Duplicate extension hdr");
                    ex.setHeaderText(h.getTrimmedInputText());
                    ex.setHeaderName(h.getHeaderName());
                    ex.setSIPMessage(this);
                    ex.setHeader(h);
                    throw ex;
                }
            }
        } else {
            // This is not an extension header.
            int i = hField.offset;
            try {
                if ( hField.isList) {
                    SIPHeaderList hl = (SIPHeaderList) h;
                    SIPHeaderList hlist =
                    (SIPHeaderList) myFields[i].get(this);
                    if (hlist != null)  {
			// Top level header is already in the list of headers
			// so we dont need to add it again to the list.
			// the concatenation below will add it to the list.
                        hlist.concatenate(hl,top);
                    } else {
                        myFields[i].set(this,h);
			hField.header = h;
                        if (!top ) headers.add(h);
                        else headers.addFirst(h);
                    }
		    addNameTable(hl,top);
                    
                } else if (myFields[i].get(this) == null || replaceflag )  {
                    // Field has not been set
		    SIPHeader hdr = (SIPHeader) myFields[i].get(this);
		    if (hdr != null) {
			headers.remove(hdr);
		    }
                    myFields[i].set(this,h);
                    hField.header = h;
                    if (!top ) headers.add(h);
                    else headers.addFirst(h);
		    addNameTable(h,top);
                } else {
                    SIPDuplicateHeaderException ex =
                    new SIPDuplicateHeaderException
                    ("Duplicate header in message ");
                    ex.setHeaderName(h.getHeaderName());
                    ex.setHeaderText(h.getTrimmedInputText());
                    ex.setSIPMessage(this);
                    ex.setHeader(h);
                    throw ex;
                }
            } catch (IllegalAccessException ex) {
                InternalError.handleException(ex);
            }
        }
        
    }
        /**Remove the first header of a given class from the Message
         * header list.
         * @param headerClass Class of the header list to remove.
         * @param top The end of the header list from which we want to
         * remove the header.
         * @exception IllegalArgumentException if the headerClass arg is null
         * or the class is not supported by this SIPMessage.
         */
    public void removeHeader(Class headerClass, boolean top)
    throws IllegalArgumentException {
        if (headerClass == null) {
            throw new IllegalArgumentException("Null header class!");
        }
        HdrField hField = (HdrField) fieldHash.get(headerClass.getName());
        if (hField == null) {
            // Could be an extension header.
            SIPHeader sh = (SIPHeader) extensionTable.get(headerClass.getName());
            if (sh == null) return;
	    LinkedList llist = (LinkedList) nameTable.get(sh.getHeaderName());
            if (sh instanceof SIPHeaderList) {
                SIPHeaderList shl = (SIPHeaderList) sh;
                if (top) {
			SIPHeader hdr = shl.getFirst();
			shl.removeFirst();
			llist.remove(hdr);
			if (llist.isEmpty()) nameTable.remove(sh.getHeaderName());
		} else {
			SIPHeader hdr = shl.getLast();
			shl.removeLast();
			llist.remove(hdr);
			if (llist.isEmpty()) nameTable.remove(sh.getHeaderName());
		}
			
                if (shl.isEmpty()) {
			headers.remove(sh);
                        extensionTable.remove(headerClass.getName());
		}
            }else {
                headers.remove(sh);
                extensionTable.remove(headerClass.getName());
            }
        } else {
            // This is not an extension header
            try {
                int i = hField.offset;
                SIPHeader sh = (SIPHeader)  myFields[i].get(this);
                if (sh == null) return;
                
	        LinkedList llist = (LinkedList) nameTable.get(sh.getHeaderName());
                if (hField.isList) {
                    SIPHeaderList shl = (SIPHeaderList) sh;
                    // If the top flag is set remove the first header
                    // otherwise remove the last header.
                    if (top) {
			 SIPHeader hdr = shl.getFirst();
			 shl.removeFirst();
			 llist.remove(hdr);
			 if (llist.isEmpty()) 
				nameTable.remove(sh.getHeaderName());
                    } else {
			 SIPHeader hdr = shl.getLast();
			 shl.removeLast();
			 llist.remove(hdr);
			 if (llist.isEmpty()) 
				nameTable.remove(sh.getHeaderName());
		    }
                    if (shl.isEmpty()) {
                        headers.remove(shl);
                        // shl = (SIPHeaderList) headerClass.newInstance();
                       	myFields[i].set(this,null);
                    }
                } else {
                    myFields[i].set(this,null);
                }
            } catch (IllegalAccessException ex) {
                InternalError.handleException(ex);
            } 
        }
        
    }

	/** Remove a header given its name.
	*@param headerName is the name of the header to remove.
	*/

	public void 
	removeHeader(String headerName, boolean top) {
	     LinkedList llist = 
		(LinkedList) nameTable.get(headerName.toLowerCase());
	     if (llist == null) return;
	     SIPHeader toremove = null;
	     if (top) {
		toremove = (SIPHeader)llist.getFirst();
	     } else {
	 	toremove = (SIPHeader) llist.getLast();
	     }
	     if (toremove != null) removeHeader(toremove);
	}
    
        /** Remove all headers of a given class.
         * @since v1.0
         * @param hClass Class of the header that we want to remove.
         * @throws IllegalArgumentException If the
         */
    public void removeAll(Class hClass )
    throws IllegalArgumentException {
        if (hClass == null)
            throw new IllegalArgumentException
            ("null header class specified!");
        Class headerClass;
        // Check if this has an associated list class.
        if (ListMap.hasList(hClass) ) {
            headerClass = ListMap.getListClass(hClass);
        } else headerClass = hClass;
        
        HdrField hField = (HdrField) fieldHash.get(headerClass.getName());
        if (hField == null) {
            // Could be an extension header.
            SIPHeader sh = (SIPHeader)
            extensionTable.get(headerClass.getName());
            if (sh == null)
                throw new IllegalArgumentException ("Extension not found");
            headers.remove(sh);
            extensionTable.remove(headerClass.getName());
        } else {
            // This is not an extension header
            try {
                int i = hField.offset;
                SIPHeader sh = (SIPHeader)  myFields[i].get(this);
                if (sh == null) return; // Already removed so bail out.
                headers.removeAll(headerClass);
                myFields[i].set(this,null);
		hField.header = null;
            } catch ( IllegalAccessException ex) {
                InternalError.handleException(ex);
            }
        }
        
    }
     /** Remove extension by name.
      *@param headerName is the name of the header to remove.
      */
     public void removeAll(String headerName) {
	// Remove all headers of a given name.
	Class headerClass = NameMap.getClassFromName(headerName);
	if (headerClass != null) removeAll(headerClass);
	nameTable.remove(headerName.toLowerCase());
     }
    
        /**
         * Get rid of a message header from this message.
         * @since v1.0
         * @param h Header or list of headers to remove.
         * @return true if the header was removed (i.e. if it existed)
         */
    public boolean removeHeader(SIPHeader h) {
        if (h == null)
            throw new
            IllegalArgumentException("null header");
        Class cl = h.getClass();
	Class listClass = ListMap.getListClass(cl);
	if (listClass != null) cl = listClass;
        HdrField hField = (HdrField) fieldHash.get(cl.getName());
        if (hField == null) {
            if ( h instanceof SIPHeaderList) {
                SIPHeaderList shl =
                (SIPHeaderList) extensionTable.get(cl.getName());
                if (shl == null) {
                    InternalError.handleException("Header not found");
                } else {
                    SIPHeaderList thislist = (SIPHeaderList) h;
                    for (SIPHeader sh = (SIPHeader) thislist.first(); sh != null ;
                         sh = (SIPHeader) thislist.next() ) {
                        shl.remove(sh);
                    }
                }
            } else {
		SIPHeader exthdr = (SIPHeader) extensionTable.get(cl.getName());
		if (exthdr instanceof SIPHeaderList) {
			SIPHeaderList shl = (SIPHeaderList) exthdr;
			shl.remove(h);
			if (shl.isEmpty()) extensionTable.remove(cl.getName());
		} else if (exthdr == h) extensionTable.remove(cl.getName());
            }
        } else {
            // This is not an extension header.
            try {
                int i = hField.offset;
                
                if ( hField.isList) {
                    SIPHeaderList shl =
                    (SIPHeaderList)( myFields[i].get(this));
                    SIPHeaderList thislist = (SIPHeaderList) h;
                    for (SIPHeader sh = (SIPHeader) thislist.first();
                    	sh != null ; sh = (SIPHeader) thislist.next() ) {
                        shl.remove(sh);
                    }
		    if (shl.isEmpty()) {
			 myFields[i].set(this,null);
			 hField.header = null;
		    }
                    
                } else {
                    // Field has not been set
                    myFields[i].set(this,null);
                    hField.header = h;
                }
            } catch (IllegalAccessException ex) {
                InternalError.handleException(ex);
            }
        }

	headers.remove(h);
	ListIterator li = headers.listIterator();
	LinkedList deleteList = new LinkedList();
	while(li.hasNext()) {
	      SIPHeader sh = (SIPHeader) li.next();
	      if (sh instanceof SIPHeaderList) {
		  SIPHeaderList shl = (SIPHeaderList) sh;
		  if (shl.isEmpty()) deleteList.add(shl);
	      }
	}
	li = deleteList.listIterator();
	while(li.hasNext()) {
	     headers.remove((SIPHeader)li.next());
	}

	LinkedList llist = (LinkedList) 
		nameTable.get(h.getHeaderName().toLowerCase());
	if (llist != null) {
	    llist.remove(h);
	    if (llist.isEmpty()) 
		nameTable.remove(h.getHeaderName().toLowerCase());
	}
        return true;
    }
    
         /**
          * get a SIP Header extension given an extension type.
          * Extensions are identified by
          * class name of the extension type.
          * @param hdrClassName Class for which we want to retrieve
          * extension header
          * @return Extension header (which should derive from SIPHeader
          * or SIPHeaderList)
          */
    public SIPHeader getExtensionHdr( String  hdrClassName  ) {
        return (SIPHeader) extensionTable.get(hdrClassName);
    }
    
        /**
         * Generate (compute) a transaction ID for this SIP message.
         * @return A string containing the concatenation of various
         * portions of the From,To,Via and RequestURI portions
         * of this message as specified in RFC 2543:
	 * All responses to a request contain the same values in
   	 * the Call-ID, CSeq, To, and From fields 
	 * (with the possible addition of  a tag in the To field 
	 * (section 10.43)). This allows responses to be matched with requests.
	 *@return a string that can be used as a transaction identifier
         *  for this message.
         */
    public String getTransactionId()  {
        String retval = "";
	From from = this.getFromHeader();
	To to = this.getToHeader();
        String  hpFrom = from.getUserAtHostPort();
        retval += hpFrom + ":";
        String  hpTo   = to.getUserAtHostPort();
	// if (to.getTag() != null) hpTo += to.getTag();
        retval += hpTo +  ":";
        String cid = this.callIdHeader.getCallID();
        retval += cid + ":";
        retval += this.cSeqHeader.getSeqno() + ":" +
        this.cSeqHeader.getMethod();
        if (! this.viaHeaders.isEmpty() ) {
            Via v = (Via) this.viaHeaders.first();
            retval += ":" + v.getSentBy().encode();
        }
        return retval;
        
    }
    
   /** Get the input text (that was read from the input stream to generate
   * this message.
   * @return   String  containing the input text (that was parsed to generate
   *		this message).
   */
    public	 String getInputText()
    { return inputText ; }

    /** Return true if this message has a body.
    */
    public boolean hasContent() {
	return messageContent != null || messageContentBytes != null;
    }
	
    
    /**Return an iterator for the list of headers in this message.
     *@return an Iterator for the headers of this message.
     */
    public  Iterator getHeaderIterator()
    {   return headers.listIterator(); }
    
    /**
     *Return the headers in this message as an array of headers.
     *@return an Array containing the SIPHeaders of this message.
     */
    public	 SIPHeader[] getHeaders()
    { return (headers != null ? headers.toArray(): null); }
    
    /**
     * Get the contentType header (null if one does not exist).
     *@return contentType header
     */
    public	 ContentType getContentTypeHeader()
    { return contentTypeHeader ; }
    
    /**
     * Get the From header (null if one does not exist).
     *@return from header
     */
    public	 From getFromHeader()
    { return fromHeader ; }
    
    /**
     * Get the ErrorInfo list of headers (null if one does not exist).
     * @return List containing ErrorInfo headers.
     */
    public	 ErrorInfoList getErrorInfoHeaders()
    { return  errorInfoHeaders; }
    
    /**
     * Get the Contact list of headers (null if one does not exist).
     * @return List containing Contact headers.
     */
    public	 ContactList getContactHeaders()
    { return  contactHeaders; }
    
    /**
     * Get the Via list of headers (null if one does not exist).
     * @return List containing Via headers.
     */
    public	 ViaList getViaHeaders()
    { return  viaHeaders == null || viaHeaders.isEmpty()? null: viaHeaders ; }
    
    /**
     * Get the CSeq list of header (null if one does not exist).
     * @return CSeq header
     */
    public	 CSeq getCSeqHeader()
    { return cSeqHeader ; }
    
    /**
     * Get the Authorization header (null if one does not exist).
     * @return Authorization header.
     */
    public	 Authorization getAuthorizationHeader()
    { return authorizationHeader ; }
    
    
    /**
     * Get the Hide header (null if one does not exist).
     * @return Hide header
     */
    public	 Hide getHideHeader()
    { return hideHeader ; }
    
    
    /**
     * Get the MaxForwards header (null if one does not exist).
     * @return Max-Forwards header
     */
    public	 MaxForwards getMaxForwardsHeader()
    { return maxForwardsHeader ; }
    
    
    /**
     * Get the Organization header (null if one does not exist).
     * @return Orgnaization header.
     */
    public	 Organization getOrganizationHeader ()
    { return organizationHeader ; }
    
    /**
     * Get the Priority header (null if one does not exist).
     * @return Priority header
     */
    public	 Priority getPriorityHeader()
    { return priorityHeader ; }
    
    /**
     * Get the ProxyAuthorization header (null if one does not exist).
     * @return List containing Proxy-Authorization headers.
     */
    public	 ProxyAuthorization getProxyAuthorizationHeader()
    { return proxyAuthorizationHeader; }
    
    
    /**
     * Get the ProxyRequire List of headers (null if one does not exist).
     * @return List contianing Proxy-Require headers.
     */
    public	 ProxyRequireList getProxyRequireHeaders()
    { return proxyRequireHeaders ; }
    
    
    /**
     * Get the Route List of headers (null if one does not exist).
     * @return List containing Route headers
     */
    public	 RouteList getRouteHeaders()
    { return routeHeaders ; }
    
    
    /**
     * Get the Require List of headers (null if one does not exist).
     * @return List containing Require headers
     */
    public	 RequireList getRequireHeaders()
    { return requireHeaders ; }
    
    
    /**
     * Get the ResponseKey List of headers (null if one does not exist).
     * @return Response-Key header
     */
    public	 ResponseKey getResponseKeyHeader()
    { return responseKeyHeader ; }
    
    
    /**
     * Get the Subjectf header (null if one does not exist).
     * @return Subject header
     */
    public	 Subject getSubjectHeader()
    { return subjectHeader ; }
    
    
    /**
     * Get the UserAgent headers (null if one does not exist).
     * @return User-Agent header
     */
    public	 UserAgent getUserAgentHeader()
    { return userAgentHeader ; }
    
    /**
     * Get the WWWAuthenticate list of headers (null if one does not exist).
     * @return List containing WWWAuthenticate headers.
     */
    public	 WWWAuthenticateList  getWWWAuthenticateHeaders()
    { return    wwwAuthenticateHeaders == null || 
		wwwAuthenticateHeaders.isEmpty()? null : 
		wwwAuthenticateHeaders ; }
    
    /**
     * Get the Accept list of headers (null if one does not exist).
     * @return List containing Accept headers
     */
    public	 AcceptList getAcceptHeaders()
    { return acceptHeaders == null || acceptHeaders.isEmpty()? null:
		acceptHeaders ; }
    
    /**
     * Get the ContentEncoding list of headers (null if one does not exist).
     * @return List containing Content-Encoding headers.
     */
    public  ContentEncodingList getContentEncodingHeaders()
    { return    contentEncodingHeaders == null || 
		contentEncodingHeaders.isEmpty()? null: 
		contentEncodingHeaders; }
    
    /**
     * Get the AcceptEncoding list of headers (null if one does not exist).
     * @return List containing Accept-Encoding headers.
     */
    public	 AcceptEncodingList getAcceptEncodingHeaders()
    { return acceptEncodingHeaders ; }
    
    /**
     * Get the AcceptLanguage list of headers (null if one does not exist).
     * @return Accept-Language header
     */
    public	 AcceptLanguageList getAcceptLanguageHeaders()
    { return acceptLanguageHeaders ; }
    
    /**
     * Get the CallID header (null if one does not exist).
     * @return Call-ID header 
     */
    public	 CallID getCallIdHeader()
    { return callIdHeader ; }
    
    /**
     * Get the Date header (null if one does not exist).
     * @return Date header.
     */
    public	SIPDateHeader getDateHeader()
    { return dateHeader ; }
    
    /**
     * Get the Encryption header (null if one does not exist).
     * @return Encryption header
     */
    public	 Encryption getEncryptionHeader()
    { return encryptionHeader ; }
    
    /**
     * Get the Expires header (null if one does not exist).
     * @return Exipres header
     */
    public	 Expires getExpiresHeader()
    { return expiresHeader ; }
    
    /**
     * Get the RecordRoute header list (null if one does not exist).
     * @return Record-Route header
     */
    public	 RecordRouteList getRecordRouteHeaders()
    { return recordRouteHeaders ; }
    
    /**
     * Get the TimeStamp header (null if one does not exist).
     * @return Time-Stamp header
     */
    public	 TimeStamp getTimestampHeader()
    { return timestampHeader ; }
    
    /**
     * Get the To header (null if one does not exist).
     * @return To header
     */
    public	 To getToHeader()
    { return toHeader ; }
    
    /**
     * Get the ContentLength header (null if one does not exist).
     * @return content-length header.
     */
    public	 ContentLength getContentLengthHeader()
    { return contentLengthHeader ; }
    
    /**
     * Get the ProxyAuthenticate header list (null if one does not exist).
     * @return A list containing Proxy-Authenticate headers.
     */
    public	 ProxyAuthenticateList getProxyAuthenticateHeaders()
    { return proxyAuthenticateHeaders ; }
    
    /** Get the list of allow headers.
     * @return A list containing Allow headers.
     */
    public	 AllowList getAllowHeaders()
    
    /**
     * Get the Allow header list (null if one does not exist).
     */
    { return allowHeaders ; }

    /** Get the retry-after header
     * @return Retry after header (null if none exists)
     */
    public	 RetryAfter getRetryAfterHeader()
    
    /**
     * Get the RetryAfter header  (null if one does not exist).
     */
    { return retryAfterHeader ; }
    
    /**
     * Get the Server header (null if one does not exist).
     * This was previously getServerHeaders. It has been changed
     * because a message can only have a single Server header.
     *@return Server header
     *@since 1.0
     */
    public	 Server getServerHeader()
    { return serverHeader; }
    
    /**
     * Get the Warning header list (null if one does not exist).
     * @return List containing Warning Headers.
     */
    public	 WarningList getWarningHeaders()
    { return warningHeaders ; }
    
    /**
     * Get the Unsupported header list (null if one does not exist).
     * @return List containing Unsupported headers.
     */
    public	 UnsupportedList getUnsupportedHeaders()
    { return unsupportedHeaders ; }
    
    /**
     * Get the AlertInfo header list (null if one does not exist).
     * @return List containing ALert-Info headers
     */
    public	 AlertInfoList getAlertInfoHeaders()
    { return alertInfoHeaders ; }
    
    /**
     * Get the CallInfo header list (null if one does not exist).
     * @return List containing Call-Info headers.
     */
    public	 CallInfoList getCallInfoHeaders()
    { return callInfoHeaders ; }
    
    
    /**
     * Get the ContentLanguage header list (null if one does not exist).
     * @return List containing Content-Language headers.
     */
    public	 ContentLanguageList getContentLanguageHeaders()
    { return contentLanguageHeaders ; }
    
    /**
     * Get the InReplyTo header list (null if one does not exist).
     * @return List containing InReplyTo headers.
     */
    public  InReplyToList getInReplyToHeaders()
    { return inReplyToHeaders; }
    
    /**
     * Get the MimeVersion header  (null if one does not exist).
     * @return Mime-Version header
     */
    public  MimeVersion	getMimeVersionHeader()
    { return mimeVersionHeader; }
    
    /**
     * Get the Also header list (null if one does not exist).
     * @return List containing Also headers.
     */
    public  AlsoList getAlsoHeaders()
    { return alsoHeaders; }
    
    /**
     * Get the Supported header list (null if one does not exist).
     * @return list containing Supported headers.
     */
    public  SupportedList getSupportedHeaders()
    { return supportedHeaders; }
    
    /**
     * Get the message body as a string.
     *	If the message contains a content type header with a specified
     *  charset, and if the payload has been read as a byte array, then
     *  it is returned encoded into this charset.
     * @return Message body (as a string)
     * @throws UnsupportedEncodingException if the platform does not
     *  support the charset specified in the content type header.
     */
    public String getMessageContent ()
     throws UnsupportedEncodingException {  
	if ( this.messageContent == null && this.messageContentBytes == null ) 
		return null;
	else if (this.messageContent == null) {
	     if (this.contentTypeHeader != null) {
		String charset = this.contentTypeHeader.getCharset();
		if (charset != null) {
	           this.messageContent = 
			new String(messageContentBytes,charset);
		} else {
		    this.messageContent = 
			new String(messageContentBytes,DEFAULT_ENCODING);
		}
	      } else this.messageContent = 
			new String(messageContentBytes,DEFAULT_ENCODING);
	}
	return this.messageContent;
    }

    /**
     * Get the message content as an array of bytes.
     * If the payload has been read as a String then it is decoded using
     * the charset specified in the content type header if it exists. 
     * Otherwise, it is encoded using the default encoding which is 
     * UTF-8.
     *@return an array of bytes that is the message payload. 
     *@throws UnsupportedEncodingException if the platform does not support
     * the encoding specified in the content encoding header.
     */
     public byte[] getContentAsBytes() {
	try {
	  if (this.messageContent == null && this.messageContentBytes == null)
		return null;
	  else if ( this.messageContentBytes == null) {
	     if (this.contentTypeHeader != null) {
		String charset = this.contentTypeHeader.getCharset();
		if (charset != null) {
	           this.messageContentBytes = 
			this.messageContent.getBytes(charset);
		} else {
		    this.messageContentBytes = 
				this.messageContent.getBytes(DEFAULT_ENCODING);
		}
	      } else this.messageContentBytes = 
				this.messageContent.getBytes(DEFAULT_ENCODING);
	  } 
	} catch (UnsupportedEncodingException ex) {
	   InternalError.handleException(ex);
	}
	return this.messageContentBytes;
     }
	
    
    /**
     * Set the message content for this message.
     * @param content Message body to set.
     */
    public void setMessageContent(String content) {
        messageContent = content;
    }

	
	/** Set the message content as an array of bytes.
	*/
    public void setMessageContent (byte[] content) {
	messageContentBytes = content;
    }

    
   /** Remove the message content if it exists.
   */    
    public void removeMessageContent() { 
	messageContent = null; 
	sdpAnnounce = null;
	messageContentBytes = null;
     }




    
    /**
     * Get the SDP Announce message parsed into astructure.
     * (or null if one does not exist).
     * @return Class containing parsed SDP poriton of message.
     */
    public SDPAnnounce getSdpAnnounce()
    { return sdpAnnounce ; }
    
    
    /**
     * Get the header from the header class. Each Header has its own class 
     * and therefore, given the class of a header, we can retrieve the header.
     * (note that for headers that can appear as a list, all headers of this 
     * class will be returned as a list).
     * @return a list of headers (or a single header) of the given class.
     * @param hClass Class that we want to retrieve 
     */
    public SIPHeader getHeader(Class hClass) {
	Class headerClass = hClass;
	if (ListMap.hasList(hClass))  {
	  headerClass = ListMap.getListClass(hClass);
        }
        HdrField hField = (HdrField) fieldHash.get(headerClass.getName());
        if (hField == null) {
            SIPHeader extHdr = 
		(SIPHeader) extensionTable.get(headerClass.getName());
            return extHdr;
        } else {
            int i = hField.offset;
            try {
                SIPHeader sh = (SIPHeader)( myFields[i].get(this) );
                return sh;
            } catch (IllegalAccessException ex) {
                InternalError.handleException(ex);
            }
        }
        return null;
        
    }
	/** Get a SIP header or Header list given its name.
	*@param headerName is the name of the header to get.
	*@return a header or header list that contians the retrieved header.
	*/
	public LinkedList getHeaders(String headerName) {
		return (LinkedList) nameTable.get(headerName.toLowerCase());
	}

	/** Return true if the SIPMessage has a header of the given class.
	*@param headerClass is the class of the header for which we want to
	*  test.
	*@return true if  header(s) of the given class exist in the message
	*/
	public boolean hasHeader(Class headerClass) {
		if (headerClass == null) 
			throw new IllegalArgumentException("null arg");
		return getHeader(headerClass)  != null;
	}

	/** Return true if the SIPMessage has a header of the given name.
	*@param headerName is the header name for which we are testing.
	*@return true if the header is present in the message
	*/

	public boolean hasHeader(String headerName) {
		return nameTable.containsKey(headerName.toLowerCase());
	}
	
	
	/**
	* Equality comparison operator.
	*@param other the other object with which we want to compare.
	*  Note that this only compares the SIP portion of the message.
	*  If you want to compare the SDP portion of the message, extract
	*  and compare it separately.
	*/
      public boolean equals(Object other) {
	int exit = 0;
	try {
	if ( ! this.getClass().equals(other.getClass()) ) {
		exit = 1;
		return false;
	}
	SIPMessage that = (SIPMessage)other;
	Collection hdrs = this.nameTable.values();
 	Iterator iterator = hdrs.iterator();
	while(iterator.hasNext()) {
	   LinkedList myList = (LinkedList) iterator.next();
	   ListIterator innerIterator = myList.listIterator();
	   while(innerIterator.hasNext()) {
	         SIPHeader myHdr = (SIPHeader) innerIterator.next();
	         String headerName = myHdr.getHeaderName();
	         LinkedList hisList  = that.getHeaders(headerName);
	         if (hisList == null) {
		     if (myHdr instanceof ContentLength) {
		      	int length = ((ContentLength) myHdr).getContentLength();
		      	if (length == 0) continue;
		     }
		     if (Debug.debug) Debug.println("headerName = " + headerName);
		     exit = 2;
		     return false;
		 }
	         if (!hisList.contains(myHdr)) {
		     if (Debug.debug) {
		         Debug.println("myHdr = " + myHdr);
		         Debug.println("hisHdr = " + hisList.getFirst());
		         Debug.println("test = " + myHdr.equals(hisList.getFirst()));
		     }
		     exit = 3;
		     return false; 
		 }
	    }
	}
	return true;
	} finally { 
	     if (Debug.debug) {
		Debug.println("Exit  = " + exit);
	     }
	}
      }
    
}
