/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle (deruelle@nist.gov)                           *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import java.util.*;
/**
* Response-Key SIPSIPObject.
*/
public class ResponseKey  extends SIPHeader {
    
        /** keyScheme field
         */    
	protected String keyScheme;
        
        /** KeyParam list
         */        
	protected NameValueList keyParam;

        /** Default constructor
         */        
	public ResponseKey() {
		super(RESPONSE_KEY);
		keyParam = new NameValueList("keyParam");
		keyParam.setSeparator(Separators.COMMA);
	}

        /** delete KeyParam field
         * @param name String to set
         * @return true if KeyParam field has been removed
         */        
        public boolean removeParameter (String name) {
            if ( keyParam == null) return false;
            return keyParam.delete(name);
	}
        
        /** Encode this into cannonical form.
         * @return String
         */        
        public String encode() {
		return headerName + COLON + SP + keyScheme + SP +
			keyParam.encode() + NEWLINE;
	}
        
        /** get the KeyScheme field
         * @return String
         */        
	public	 String getKeyScheme() {
            return keyScheme ;
        } 

        /** get the specified parameter
         * @param parmName String to set
         * @return Object
         */        
	public Object getParameter( String parmName) {
            if ( keyParam==null) return null;
            return keyParam.getValue(parmName);
        }

	/** Get the key parameter list.
	* @return NameValueList
        */
	public NameValueList getKeyParam() {
		if ( keyParam==null) return null;
                else return keyParam;
	}
	
        /** get the parameters.
         * @return Iterator to the name value list of parameters.
         */
	public Iterator getParameters() {
		if ( keyParam==null) return null;
                else return keyParam.getNames();
	}
                
        /** Boolean function
         * @return true if KeyParameters exist
         */        
        public boolean hasParameters() {
            if ( keyParam==null) return false;
            return !keyParam.isEmpty();
        }
        
        /** Boolean function
         * @param name String to set
         * @return true if specified KeyParameter exist
         */        
        public boolean hasParameter(String name) {
            if ( keyParam==null) return false;
            return keyParam.getNameValue(name)!=null; 
        }
             
        /** remove all parameters
         */        
        public void removeParameters() {
             keyParam= new NameValueList("keyParam");
             keyParam.setSeparator(Separators.COMMA);
        }
        
	/**
         * Set the keyScheme member
         * @param k String to set
         */
	public void setKeyScheme(String k) {
            keyScheme = k ;
        }
        
	/**
         * Set the keyParam member
         * @param k NameValueList to set
         */
	public void setParameter(NameValueList k) {
            keyParam = k ;
        } 
                
        /** set the KeyParameter field with the value
         * @param name String to set
         * @param value String to set
         */        
        public void setParameter(String name, String value)  {
		NameValue nv = new NameValue(name,value);
                if ( keyParam==null) keyParam = new 
                                                NameValueList("keyParam");
                if (keyParam.hasNameValue(name) )
		                                removeParameter(name) ;
		keyParam.add(nv);
	}              
        
}
