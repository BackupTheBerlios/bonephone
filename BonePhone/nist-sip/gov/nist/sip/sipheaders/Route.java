/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) ,added JAVADOC                 *                                                                                    
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;
import gov.nist.sip.net.Address;

import java.util.*;

/**
* Route  SIPHeader Object
*@since 1.0
*/
public class Route extends SIPHeader {
    
	protected Address address;
        
        protected NameValueList params;
        
        /** Default constructor
         */        
	public Route() { 
            super(ROUTE);
            params = new NameValueList("route_params");
        }

        /** set the Address field
         * @param addr Address to set
         */        
	public void setAddress( Address addr) { 
            address = addr;
        }

        /** get the Address
         * @return Address
         */        
	public Address getAddress() {
            return address;
        }
        
        /**
         * Set a parameter.
         * @param name name of the parameter to set.
         * @param value value of the parameter to set.
         */
        public void setParameter(String name, String value) {
            NameValue nv = new NameValue(name,value);
            if ( params==null) params = new  NameValueList("route_params");
            if ( params.hasNameValue(name) ) removeParameter(name) ;
            params.add(nv);
        }
        
        
          /** return true if the address has any parameters.
           * @return boolean value indicating whether the address has
           * parameters.
           */        
        public boolean hasParameters() {
           if ( params==null) return false;
           else return !params.isEmpty(); 
        }
        
        /** Return true if the parameter of a given name exists.
         */
        public boolean hasParameter(String parmName) {
             if ( params==null) return false;
             else return params.getValue(parmName) != null; 
        }
        
        /** Remove the parameter of a given name.
         */
        public void removeParameter(String name) {
            if ( params !=null) params.delete(name);
        }
        
        /** Remove all parameters.
         */
        public void removeParameters() {
            params = new NameValueList("route_params");
        }
        
        /** Get the parameter list of the uri.
         * @return Iterator;
         */
        public Iterator getParameters() {
            if ( params==null) return null;
            else return params.getNames();
        }
        
        public void  addParam(NameValue nv) {
            if (nv == null) throw new IllegalArgumentException("null arg!");
            if ( params==null) params = new NameValueList("route_params");
            params.add(nv);
        }
        
        public String getParameter(String name) {
             if ( params==null) return null;
             return (String) params.getValue(name);
        }

	/** Set the parameter list.
	*@param params is the parameter list to set.
	*/
	public void setParams(NameValueList params) {
		this.params = params;
        }
        
        /** Encode into canonical form.
         */
        public String encode() {
            String encoding = headerName + COLON + SP + address.encode();
            if (!params.isEmpty()) encoding += SEMICOLON + params.encode();
            encoding += NEWLINE;
            return encoding;
        }      
        
}
	
