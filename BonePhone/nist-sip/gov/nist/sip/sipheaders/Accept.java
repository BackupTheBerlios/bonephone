/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                   
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;

import java.util.*;

/**
*Accept header : The top level header is actually AcceptList which is a list of
*Accept headers.
*<pre>
*Accept  = "Accept" ":" #( media-range [ accept-params ] )
*
* Note STAR is * below!
*
*       media-range    = ( "STAR/STAR"
*                        | ( type "/" "*" )
*                        | ( type "/" subtype )
*                        ) *( ";" parameter )
*       accept-params  = ";" "q" "=" qvalue *( accept-extension )
*       accept-extension = ";" token [ "=" ( token | quoted-string ) ]       
*</pre>
*
*/
public class Accept extends SIPHeader implements Cloneable {
    
        /** mediaRange field
         */    
	protected MediaRange     mediaRange;

        /** acceptParams field
         */        
        protected AcceptParams   acceptParams;

        /** default constructor
         */        
	public Accept() { 
            super(ACCEPT);
        }         
        
       /** returns true if this header allows all ContentTypes,
         * false otherwise.
         * @return Boolean
         */        
	public boolean allowsAllContentTypes() {
             if (mediaRange==null) return false;
             else return mediaRange.type.compareTo("*") == 0 ;
	}
	
        /**
         * returns true if this header allows all ContentSubTypes,
         * false otherwise.
         * @return boolean
         */        
	public boolean allowsAllContentSubtypes() {
            if (mediaRange==null) { 
                System.out.println("mediaRange est  null");
                return false;
            }
             else return mediaRange.getSubtype().compareTo("*") == 0;
	}	
        
        /**
         * Encode the header in canonical form.
         * @return encoded header as a string.
         */        
        public String encode() {
	  return this.headerName + COLON + mediaRange.encode() + SP + 
	     ( acceptParams == null? NEWLINE : 
			acceptParams.encode() + NEWLINE);
	}        

	/** Encode the value of this header into cannonical form.
	*@return encoded value of the header as a string.
	*/
        public String encodeBody() {
	  return (mediaRange != null? mediaRange.encode():"") + SP + 
	         (acceptParams == null? "" : acceptParams.encode());
	}        
    
        /** get the MediaRange field
         * @return MediaRange
         */        
	public	 MediaRange getMediaRange() {
            return mediaRange ;
        }
        
        /** get the AcceptParams field
         * @return AcceptParams
         */        
	public	 AcceptParams getAcceptParams() {
            return acceptParams ;
        }
	
        /** get the contentType field
         * @return String
         */        
	public String getContentType() {
            if (mediaRange==null) return null;
            else return mediaRange.getType();
	}
	
        /** get the ContentSubType fiels
         * @return String
         */        
	public String getContentSubType() {
           if (mediaRange==null) return null;
           else return mediaRange.getSubtype(); 
	}
         
         /**
          * Get the q value.
          * @return float
          */
	public float getQValue() {
            if (acceptParams==null) return -1;
            else return (float) acceptParams.getQValue();
	}
        
        /** Get a parameter.
         * @param name String to set
         * @return String
         */
	public String getParameter(String name) {
		if (acceptParams== null) return null;
		else return (String)acceptParams.getValue(name);
	}

	/** get the parameters.
         * @return Iterator
         */
	public Iterator getParameters() {
		if ( acceptParams==null) return null;
                else return acceptParams.getIterator();
	}
        
        /** boolean function
         * @return true if this header has parameters, false otherwise.
         */        
        public boolean hasParameters() {
            if ( acceptParams==null) return false;
            else return !acceptParams.isEmpty();
        }
        
        /**
         * Checks if a parameter exists.
         * @param name String to set
         * @return boolean
         */
        public boolean hasParameter(String name) {
            return acceptParams != null && acceptParams.hasParameter(name);
        }
        
        /**
         * Return true if the q value has been set.
         * @return boolean
         */
	public boolean hasQValue() {
             if (acceptParams==null) return false;
             else return acceptParams.qValueIsSet;
	}
          
        /**
         *Remove the q value.
         */
        public void removeQValue() {
            if (acceptParams!=null) {
                acceptParams.qValueIsSet = false;
		acceptParams.qValue = -1.0;
            }
        }
        
        /**
	* Remove the parameters.
	* @since 1.0
	*/
         public void removeParameters() {
             acceptParams = new AcceptParams();
	}       
        
        /**
         * remove a parameter if it exists.
         * @param name String to set
         */
        public void removeParameter(String name) {
            if (acceptParams!=null)
             acceptParams.removeParameter(name);
        }        
        
        /** set the ContentSubType field
         * @param subtype String to set
         */        
        public void setContentSubType(String subtype ) {
            if (mediaRange==null) mediaRange=new MediaRange();
            mediaRange.setSubtype(subtype);
        }
           
         
        /** set the ContentType field
         * @param type String to set
         */        
        public void setContentType(String type ) {
            if (mediaRange==null) mediaRange=new MediaRange();
            mediaRange.setType(type);
        }
        
        /**
         * Set the q value 
         * @param qValue float to set
         * @throws IllegalArgumentException if qValue is <0.0 or >1.0
         */
        public void setQValue(float qValue) throws IllegalArgumentException {
                double qval = qValue;
                if (this.acceptParams == null) {
                    this.acceptParams = new AcceptParams();
                }
		if (qValue < 0.0 || qValue > 1.0) 
			throw new IllegalArgumentException
				("qvalue out of range!");
                this.acceptParams.setQValue(qval);
        }
      
	/**
         * Set the mediaRange member
         * @param m MediaRange field
         */
	public	 void setMediaRange(MediaRange m) {
            mediaRange = m ;
        }
        
	/**
         * Set the acceptParams member
         * @param a AcceptParams to set
         */
	public	 void setAcceptParams(AcceptParams a) {
            acceptParams = a ;
        } 
	
        /**
         * Set the "Name-Value" parameter.
         * @param name String to set
         * @param value String to set
         */  
        public void setParameter(String name, String value)  {
             if (acceptParams==null) acceptParams = new AcceptParams();
             if (acceptParams.hasParameter(name) )
		                                removeParameter(name) ;
             acceptParams.setParameter(name,value);
	}         
            
}
