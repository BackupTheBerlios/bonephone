/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) , added JAVADOC                *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import java.util.Iterator;

/**
*  To SIP Header
*@version 1.0
* Revisions:
* 1. added encoding.
* 2. improved documentation.
*
*<pre>
*       To        =  ( "To" | "t" ) ":" ( name-addr | addr-spec )
*                     *( ";" to-param )
*       to-param  =  tag-param | generic-param
*</pre>
* <A href="../../../ietf/rfc2543.txt#6.44%20To">Section 6.24</A>
*
*/
public final class To extends SIPHeader implements AddressKeywords {

        /** address field
         */
    protected Address address;
    
        /** parameters fields
         */
    protected NameValueList parms;
    
        /** default Constructor.
         */
    public To() {
        super(TO);
        parms = new NameValueList("toParams");
    }
	/** Generate a TO header from a FROM header
	*/
     public To (From from) {
	super(TO);
	address = from.address;
	parms = from.parms;
     }
    
    /**
     * Compare two To headers for equality.
     * @param otherHeader Object to set
     * @return true if the two headers are the same.
     */
    public boolean equals(Object otherHeader) {
	int exitpoint = 0;
	try {
          if (!otherHeader.getClass().equals(this.getClass())){
	      exitpoint = 1;
	      return false;
           }
        
          To otherTo = (To) otherHeader;
          if (! otherTo.getAddress().equals(address)) {
	      exitpoint = 2;
	      return false;
          }
	  exitpoint = 3;
          return parms.equals(otherTo.parms);
	} finally {
	    // System.out.println("equals " + retval + exitpoint);
	}
    }
    
   /**
    * Encode the header into a String.
    * @since 1.0
    * @return String
    */
    public String encode() {
        return headerName + COLON + SP + encodeBody() + NEWLINE;
    }

   /**
    * Encode the header content into a String.
    * @return String
    */
    public String encodeBody() {
	String retval = "";
        if (address.getAddressType() == Address.ADDRESS_SPEC) {
            retval += LESS_THAN;
        }
        retval += address.encode();
        if (address.getAddressType() == Address.ADDRESS_SPEC) {
            retval += GREATER_THAN;
        }
        if (!parms.isEmpty() ) {
            retval += SEMICOLON + parms.encode();
        }
        return retval;
    }
    
   /**
    * Get the address field.
    * @return Adress field
    */
    public Address getAddress() { 
        return address;
    }
    
   /**
    * get the parameter list.
    * @return parameter list
    */
    public NameValueList getParms () {
        return parms;
    }
    
   /**
    * Conveniance accessor function to get the hostPort field from the address
    * @return hostport field
    */
    public HostPort getHostPort() {
        return address.getHostPort();
    }

   /**
    * Conveniance accessor function to get the hostPort field from the address
    * @return hostport field
    */
    public String getUserAtHostPort() {
        return address.getUserAtHostPort();
    }
    
   /**
    * Get the display name from the address.
    * @return Display name
    */
    public String getDisplayName() {
        return address.getDisplayName();
    }
    
        /** get the Name Parameter
         * @param parmName String to set
         * @return the Parameter or null if it does not exist
         */
    public String getParameter( String parmName) {
        if ( parms==null) return null;
        return (String) parms.getValue(parmName);
    }
    
   /**
    * Get the tag parameter from the address parm list.
    * @return tag field
    */
    public String getTag() {
        if ( parms==null) return null;
        return (String) parms.getValue(TAG);
    }
    
   /** Return an iterator having the parameter names.
    *@return an iterator with the parameter names.
    */
    public Iterator getParmNames() {
        if ( parms==null) return null;
	return parms.getNames();
    }
    
     /**
     * Return true if the to header has any parameters.
     * @return True if we have any parameters false otherwise
     */
    public boolean hasParameters() {
            if ( parms==null) return false;
            return !parms.isEmpty();
    }
    
    /** Boolean function
     * @return true if the Tag exist
     */
    public boolean hasTag() {
        if ( parms==null) return false;
        return parms.hasNameValue(TAG);
    }
  
   /** Return true if a parameter of the given name exists.
   *@param <var> name </var> parameter name.
   */
   public boolean hasParameter(String paramName) {
        if ( parms==null) return false;
	return parms.getValue(paramName) != null;
   }
    
      /** remove Tag member
       */
    public void removeTag() {
        if ( parms !=null) parms.delete(TAG);
    }   
    
   /**
    *Remove a parameter.
    */
   public void removeParameter(String name) {
      if ( parms !=null) parms.delete(name);
   }
   
   /**
    *Remove all parameters.
    */
   public void removeParameters() {
       parms = new NameValueList("fromParms");
   }
   
   /**
    * Set the address member
    * @param address Address to set
    */
    public void setAddress(Address address) {
       this.address = (Address) address;
	// The following are not allowed in To and From headers...
       this.address.removeParameter("transport");
       this.address.removeParameter("maddr");
       this.address.removeParameter("ttl");
    }
    
   /**
    * Set the tag member
    * @param t String to set
    */
    public void setTag(String t) {
        if ( parms !=null ) parms.delete(TAG);
        else parms = new NameValueList("fromParms");
        NameValue nv = new NameValue(TAG,t);
        parms.add(nv);
    }    
         
   /**
    *Set a parameter in the from header.
    */
   public void setParameter(String name, String value) {
       NameValue nv = new NameValue(name,value);
       if ( parms==null) parms = new NameValueList("fromParms");
       if ( parms.hasNameValue(name) ) removeParameter(name) ;
       parms.add(nv);
   }

   /** Set a parameter in the header.
   */
   public void setParameter(NameValue nameValue) {
	parms.add(nameValue);
   }

}
