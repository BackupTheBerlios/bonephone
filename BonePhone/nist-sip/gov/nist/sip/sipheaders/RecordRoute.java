/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import java.util.Iterator;

/** The Request-Route header is added to a request by any proxy that insists on
 * being in the path of subsequent requests for the same call leg.
 */
public class RecordRoute extends SIPHeader {
    
	Address address;
        NameValueList params;
        
        /**  constructor
         * @param addr address to set
         */        
	public RecordRoute(Address addr ) {
		super(RECORD_ROUTE);
		address = addr;
                params = new NameValueList("record_route_params");
	}
        
	 /** default constructor
         */        
       public RecordRoute() {
		super(RECORD_ROUTE);
                params = new NameValueList("record_route_params");
	}

        /** get the Address field
         * @return Address
         */        
        public Address getAddress() {
		return address;
	}
        
        /** set the Address field
         * @param addr Address to set
         */        
	public void setAddress( Address addr) {
		address = addr;
	}

	/**
	* Get a parameter value from the address.
	*@param parmName Name of the parameter to get.
	*/
	public String getParameter(String parmName) {
                if (params==null) return null;
		return (String) params.getValue(parmName);
	}

	
         /** Set a parameter.
         * @param name name of the parameter to set.
         * @param value value of the parameter to set.
         */
        public void setParameter(String name, String value) {
            NameValue nv = new NameValue(name,value);
            if (params==null) params = new NameValueList("record_route_params");
            if (params.hasNameValue(name) ) removeParameter(name) ;
            params.add(nv);
        }
        
        /** return true if the address has any parameters.
         * @return boolean value indicating whether the address has
         * parameters.
         */        
        public boolean hasParameters() {
             if (params==null) return false;
             return !params.isEmpty();
        }
        
        /** Return true if the parameter of a given name exists.
         */
        public boolean hasParameter(String parmName) {
            if (params==null) return false;
            return params.getValue(parmName) != null;
        }
        
        /** Remove the parameter of a given name.
         */
        public void removeParameter(String name) {
             if (params!=null) params.delete(name);
        }
        
        /** Remove all parameters.
         */
        public void removeParameters() {
            params = new NameValueList("route_params");
        }
        
        /** Get the parameter list of the header.
         */
        public NameValueList getParams() {
            return params;
        }
        
        /** Get the parameter names.
         */
        public Iterator getParamNames() {
            if (params==null) return null;
            return params.getNames();
        }
        
        /** Add a parameter given as a NameValue pair.
         *@param nv NameValue representing the parameter to add.
         */
        public void  addParam(NameValue nv) {
            if (nv == null) throw new IllegalArgumentException("null arg!");
            if (params==null)  params = new NameValueList("route_params");
            params.add(nv);
        }

	/** Set the parameter list.
	*@param params is the parameter list to set.
	*/
	public void setParams(NameValueList params) {
		this.params = params;
        }
        
        /** Encode into canonical form.
         *@return String containing the canonicaly encoded header.
         */
        public String encode() {
            String encoding = headerName + COLON + SP + address.encode();
            if (!params.isEmpty()) encoding += params.encode();
            encoding += NEWLINE;
            return encoding ;
        }

}
