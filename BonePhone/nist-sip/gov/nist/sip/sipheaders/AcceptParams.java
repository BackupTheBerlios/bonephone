/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ListIterator;

/**
* AcceptParams - parameters of the Accept message.
*@since 0.9
*@version 1.0
*@see Accept
*@see AcceptList
*
*<pre>
*	Generated from the following grammar:
*
*       accept-params  = ";" "q" "=" qvalue *( accept-extension )
*        accept-extension = ";" token [ "=" ( token | quoted-string ) ]       
*Revisions : 
* Version 1.0 
*	accept extension changed to namevalue list.
*</pre>
*/
public class AcceptParams extends SIPObject  {
    
        /** qValueIsSet field
         */    
	protected boolean qValueIsSet;

        /** qvalue field
         */        
        protected double qValue;
	
        /** acceptExtension list
         */        
        protected NameValueList acceptExtension;

        /** default constructor
         */        
	public AcceptParams() {
		acceptExtension = new NameValueList("accept_extension");
		qValue = -1.0; // default q value.
		qValueIsSet = false;
	}
        
	/**
         * Add a new parameter to accept extension list.
         * @param nv NameValueList to set
         */
	public void addExtension(NameValue nv) {
		acceptExtension.add(nv);
	}        
         
        /**
         * Create a clone of this object.
         * @return Object 
         */
        public Object clone()  { 
            AcceptParams retval = (AcceptParams) super.clone();
            retval.qValue = this.qValue;
	    retval.qValueIsSet = this.qValueIsSet;
            retval.acceptExtension = 
                (NameValueList) this.acceptExtension.clone();
            return (Object) retval;
        
        }	
              
        /**
         * Encode into a string;
         * @return String
         */
	public String encode() {
		String encoding = "q" + EQUALS + qValue;
		if (acceptExtension != null) {
			encoding += 
				SEMICOLON + acceptExtension.encode();
		}
		return encoding;
	}
       
        /** get the QValue field
         * @return double
         */        
	public	 double getQValue() {
            return qValue ;
        } 

        /** get the AcceptExtension list
         * @return NameValueList
         */        
	public	 NameValueList getAcceptExtension() {
            return acceptExtension ;
        }
     
        /** get the value of the specified parameter
         * @since 1.0
         * @param name String to set
         * @return String
         */
        public String getValue(String name) {
            return (String) acceptExtension.getValue(name);
        }
        
          /**
         *Get an iterator for the name value list.
	 *@return an iterator for a linked list having the parameter
	 * names.
         */
        public Iterator getIterator() {
            return acceptExtension.getNames();
        }
        
         /**
         * returns true if the list is empty, false otherwise.
	 * @return boolean
         */
        public boolean isEmpty() {
            return acceptExtension.isEmpty();
        }
        
         /**
          * Return true if the parameter exists.
          * @param param String to set
          * @return boolean
          */
        public boolean hasParameter(String param) {
            return acceptExtension.getValue(param) != null;
        }        
        
        /**
         * Remove the parameter if it exists.
         * @param param String to set
         */
        public void removeParameter(String param) {
            acceptExtension.delete(param);
        }        
         
	/**
         * Set the qValue member
         * @param q double to set
         */
	public	 void setQValue(double q) { 
		qValue = q; 
		qValueIsSet = true;
	}
        
	/**
         * Set the acceptExtension member
         * @param a NameValueList to set
         */
	public	 void setAcceptExtension(NameValueList a) {
            acceptExtension = a ;
        } 
	
        /**
         * Set the "Name-Value" parameter.
         * @param name String to set
         * @param value String to set
         */  
        public void setParameter(String name, String value)  {
                NameValue nv = new NameValue(name,value);
		acceptExtension.add(nv);
	}         
        
}
