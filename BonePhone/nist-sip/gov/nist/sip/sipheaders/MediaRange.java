/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov)                                * 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;

import java.util.*;
/**
*   Media Range 
* @see Accept
* @since 0.9
* @version 1.0
* <pre>
* Revisions:
*
* Version 1.0
*    1. Added encode method.
*
* media-range    = ( "STAR/STAR"
*                        | ( type "/" STAR )
*                        | ( type "/" subtype )
*                        ) *( ";" parameter )       
* 
* HTTP RFC 2616 Section 14.1
* </pre>
*/
public class MediaRange  extends SIPObject implements Cloneable {
    
        /** type field
         */    
	protected String  type;
        
        /** subtype field
         */        
	protected String  subtype;

        /** parameters list
         */        
	protected NameValueList parameters;

        /** Default constructor
         */        
	public  MediaRange() {
		parameters = new NameValueList("parameter");
	}

        /** get type field
         * @return String
         */        
	public String getType() {
            return type ;
        }
            
        /** get the subType field.
         * @return String
         */                
	public String getSubtype() {
            return subtype ;
        } 
   
        /** get the parameters list
         * @return Iterator
         */                
	public Iterator getIterator() {
            return parameters.getNames() ;
        }
        
	/** get a parameter of a given name.
	*/
	public String getParameter(String name) {
		return (String) parameters.getValue(name);
	}

	/** Get the parameters as a namevalue list.
	*/
	public NameValueList getParameters() { return parameters; }
        
	/**
         * Set the type member
         * @param t String to set
         */
	public void setType(String t) {
            type = t ;
        }
        
	/**
         * Set the subtype member
         * @param s String to set
         */
	public void setSubtype(String s) {
            subtype = s ;
        }
        
	/**
         * Set the parameter member
         * @param p NameValueList to set
         */
	public void setParameters(NameValueList p) {
            parameters = p ;
        } 

	/**
         * Encode the object.
         * @return String
         */
	public String encode() {
		String encoding = type + SLASH + subtype;
		if ( !parameters.isEmpty() )  {
			encoding += SEMICOLON + parameters.encode();
		}
		return encoding;
	}
       
        /** set the specified Parameter
         * @param name String to set
         * @param value String to set
         */        
        public void setParameter(String name, String value)  {
		NameValue nv = new NameValue(name,value);
		parameters.set(nv);
	}                         
       
        /** Boolean function
         * @return true if this header has one or more Parameters, false 
         * otherwise.
         */        
        public boolean hasParameters() {
                return !parameters.isEmpty();
        }
       
        /** Boolean function
         *
         * @param name String to set
         * @return returns true if this header has the specified Parameter,
         * false otherwise.
         */        
        public boolean hasParameter(String name) {
                return parameters.getNameValue(name)!=null; 
        }
        
        /** Boolean function.
         * @param name String to set
         * @return returns true if the specified Parameter has been deleted,
         * false otherwise.
         */        
        public boolean removeParameter (String name) {
		return parameters.delete(name);
	}        
        
        /**
	* Remove the parameters.
	* @since 1.0
	*/
         public void removeParameters() {
            parameters = new NameValueList("parameter");
	}
        
}
