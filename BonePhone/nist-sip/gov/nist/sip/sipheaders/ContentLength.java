/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) , added JAVADOC                *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* ContentLength SIPHeader (of which there can be only one in a SIPMessage).
*<pre>
*Fielding, et al.            Standards Track                   [Page 119]
*RFC 2616                        HTTP/1.1                       June 1999
*
*
*      14.13 Content-Length
*
*   The Content-Length entity-header field indicates the size of the
*   entity-body, in decimal number of OCTETs, sent to the recipient or,
*   in the case of the HEAD method, the size of the entity-body that
*   would have been sent had the request been a GET.
*
*       Content-Length    = "Content-Length" ":" 1*DIGIT
*
*   An example is
*
*       Content-Length: 3495
*
*   Applications SHOULD use this field to indicate the transfer-length of
*   the message-body, unless this is prohibited by the rules in section
*   4.4.
*
*   Any Content-Length greater than or equal to zero is a valid value.
*   Section 4.4 describes how to determine the length of a message-body
*   if a Content-Length is not given.
*
*   Note that the meaning of this field is significantly different from
*   the corresponding definition in MIME, where it is an optional field
*   used within the "message/external-body" content-type. In HTTP, it
*   SHOULD be sent whenever the message's length can be determined prior
*   to being transferred, unless this is prohibited by the rules in
*   section 4.4.
* </pre>
*
*@see gov.nist.sip.msgparser.StringMsgParser
*@see gov.nist.sip.msgparser.PipelinedMsgParser
*/
public class ContentLength extends SIPHeader {
    
        /** contentLength field.
         */    
        protected int contentLength;
	
        /** Default constructor.
         */        
        public ContentLength() { 
            super(CONTENT_LENGTH);
        }
        
        /** 
         *Constructor given a length.
         */
        public ContentLength(int length) {
            super(CONTENT_LENGTH);
            this.contentLength = length;
        }

        /** get the ContentLength field.
         * @return int
         */        
	public int getContentLength() { 
            return contentLength;
        }
        
	/**
         * Set the contentLength member
         * @param c int to set
         */
	public void setContentLength(int c) {
            contentLength = c ;
        } 

        /**
         * Encode into a canonical string.
         * @return String
         */        
	public String encode() { 
		return headerName + COLON + SP + 
				contentLength + NEWLINE; 
	}
        
}
