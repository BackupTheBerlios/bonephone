/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Modified By:  O. Deruelle (deruelle@nist.gov) , added JAVADOC                *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;

import java.util.*;
/**
*  ContentType SIP Header 
* <pre>
*14.17 Content-Type
* 
*   The Content-Type entity-header field indicates the media type of the
*   entity-body sent to the recipient or, in the case of the HEAD method,
*   the media type that would have been sent had the request been a GET.
* 
*   Content-Type   = "Content-Type" ":" media-type
* 
*   Media types are defined in section 3.7. An example of the field is
* 
*       Content-Type: text/html; charset=ISO-8859-4
* 
*   Further discussion of methods for identifying the media type of an
*   entity is provided in section 7.2.1.   
*
* From  HTTP RFC 2616
* </pre>
*
*/
public class ContentType extends SIPHeader {
    
        /** mediaRange field.
         */    
	protected MediaRange mediaRange;

        /** Default constructor.        
         */        
        public ContentType() {
            super(CONTENT_TYPE);
        }
        
        /** compare two MediaRange headers.
         * @param media String to set
         * @return int.
         */        
	public int compareMediaRange (String media ) {
		return (mediaRange.type+"/"+mediaRange.subtype).
				compareToIgnoreCase(media);
	}
        
        /**
         * Encode into a canonical string.
         * @return String.
         */    
        public String encode() { 
		return CONTENT_TYPE + COLON + SP + mediaRange.encode() + 
					NEWLINE ; 
	}

        /** get the mediaRange field.
         * @return MediaRange.
         */        
        public MediaRange getMediaRange() {
            return mediaRange;
        }
        
        /** get the Media Type.
         * @return String.
         */        
	public String getMediaType() { 
            return mediaRange.type;
        }

        /** get the MediaSubType field.
         * @return String.
         */        
	public String getMediaSubType() { 
            return mediaRange.subtype;
        }
        
       /** Get the content subtype.
	*@return the content subtype string (or null if not set).
	*/
	public String getContentSubType() {
		return mediaRange == null ? null : mediaRange.getSubtype();
	}

	/** Get the content subtype.
	*@return the content tyep string (or null if not set).
	*/

	public String getContentType() {
		return mediaRange == null ? null : mediaRange.getType();
	}

	/** Get a parameter.
         * @param name String to set
         * @return String
         */
	public String getParameter(String name) {
		if (mediaRange == null) return null;
		else return (String)mediaRange.getParameter(name);
	}
	
	/** Get the charset parameter.
	*/
	public String getCharset() {
		return this.mediaRange.getParameter("charset");
	}

	/** get the parameters.
         * @return Iterator
         */
	public Iterator getParameters() {
		if (mediaRange== null) return  null;
                else return mediaRange.getIterator() ; 
	}
        
        /**
         * Gets boolean value to indicate if Parameters
         * has any parameters
         * @return boolean value to indicate if Parameters
         * has any parameters
         */   
	public boolean hasParameters() { 
		return mediaRange == null ? false: mediaRange.hasParameters();
	}

        /**
         * Gets boolean value to indicate if Parameters
         * has specified parameter
         * @return boolean value to indicate if Parameters
         * has specified parameter
         * @param name String to set
         */
	public boolean hasParameter(String name) {
		return mediaRange != null && mediaRange.hasParameter(name);
	}
        
         /**
          * Removes all parameters from Parameters (if any exist)
          */
        public void removeParameters() { 
	     if (mediaRange!= null) mediaRange.removeParameters();
	}
      
        /**
         * Removes specified parameter from Parameters (if it exists)
         * @param name String to set
         */
	public void removeParameter(String name) {
	     if (mediaRange!= null) mediaRange.removeParameter(name);	
	}
        
        /**
         * Set the mediaRange member
         * @param m mediaRange field.
         */
	public void setMediaRange(MediaRange m) {
            mediaRange = m ;
        } 

	/**
	* set the content type and subtype.
	*@param contentType Content type string.
	*@param contentSubType content subtype string
	*/
	public void setContentType(String contentType, String contentSubType) {
		if (mediaRange == null) mediaRange = new MediaRange();
		mediaRange.setType(contentType);
		mediaRange.setSubtype(contentSubType);
	}

	/**
	* set the content type.
	*@param contentType Content type string.
	*/

	public void setContentType(String contentType) {
		if (mediaRange == null) mediaRange = new MediaRange();
		mediaRange.setType(contentType);

	}

	/** Set the content subtype.
         * @param contentType String to set
         */
	public void setContentSubType(String contentType) {
		if (mediaRange == null) mediaRange = new MediaRange();
		mediaRange.setSubtype(contentType);

	}

	
        /** set the specified Parameter
         * @param name String to set
         * @param value String to set
         */        
	public void setParameter(String name, String value) {
		if (mediaRange == null) mediaRange = new MediaRange();
                if (mediaRange.hasParameter(name) )
		                mediaRange.removeParameter(name) ;
		mediaRange.setParameter(name,value);
	}

}
