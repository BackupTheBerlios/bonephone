/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;

import gov.nist.sip.*;
import java.util.*;
import java.net.URLDecoder;

/**
* The URI structure - works for both SIP URLs and regular URIs (Non SIP)
* @since v0.9
* @version 1.0
*Revisions:
* 1. Added encoding method.
* 2. Added JAIN-SIP support.
*/
public class URI  extends NetObject implements URIKeywords, URITypes {
        
    /** uriType field
     */    
    protected int uriType;
    
    /** scheme field
     */    
    protected String scheme;
    
    /** authority field
     */    
    protected Authority authority;
    
    /** opaquePart field
     */    
    protected String opaquePart;
    
    /** path field
     */    
    protected Path path;
    
    /** fragment field
     */    
    protected String fragment;
    // protected String		 search;
    
    /** query field
     */    
    protected String query;
    
    /** uriParms list
     */    
    protected NameValueList uriParms;
    
    /** qheaders list
     */    
    protected NameValueList qheaders;
    
    /** telephoneSubscriber field
     */    
    protected TelephoneNumber  telephoneSubscriber;
    
    /** Default constructor
     */    
    public URI() {
	uriType = URITypes.SIP_URL;
	scheme = "sip";
        uriParms = new NameValueList("uriParms");
        qheaders = new NameValueList("qheaders");
    }
       
       /**
        * clear all URI Parameters.
        * @since v1.0
        */
    public void clearUriParms() {
        uriParms = new NameValueList("uriParms");
    }
    
        /**
         * Clear all Qheaders.
         */
    public void clearQheaders() { 
        qheaders = new NameValueList("qheaders");
    }
    
   /**
    *     Decode two strings and compare.
    */
    private static int CompareDecoded( String s1, String s2) {
        String ds1 = URLDecoder.decode(s1);
        String ds2 = URLDecoder.decode(s2);
        return ds1.compareToIgnoreCase(ds2);
    }
    
   /**
    * Compare two URIs and return true if they are equal.
    * Overrides the object equality comparison test.
    * From the SIP bis 02 draft specification:
    * 2.1 SIP URL Comparison
    *
    * SIP URLs are compared for equality according to the following rules:
    *
    *   o Comparisons of scheme name ("sip"), domain names, parameter
    *      names and header names are case-insensitive, all other
    *      comparisons are case-sensitive.
    *
    *   o The ordering of parameters and headers is not significant in
    *     comparing SIP URLs.
    *
    *   o user or telephone-subscriber, password, host, port and any
    *     url-parameter parameters of the URI must match. If a component
    *     is omitted, it matches based on its default value. (For
    *     example, otherwise equivalent URLs without a port
    *     specification and with port 5060 match.) Components not found
    *     in both URLs being compared are ignored.
    *
    *   o Characters other than those in the "reserved" and "unsafe"
    *     sets (see RFC 2396 [9]) are equivalent to their ""%" HEX HEX"
    *     encoding.
    *
    *   o An IP address that is the result of a DNS lookup of a host
    *     name does not match that host name.
    *
    * Thus, the following URLs are equivalent:
    *
    * sip:juser@
    * sip:juser@ExAmPlE.CoM;Transport=udp
    *
    *
    * while
    *
    * SIP:JUSER@ExAmPlE.CoM;Transport=udp
    * sip:juser@ExAmPlE.CoM;Transport=UDP
    * are not.
    *
    * Header fields such as Contact, From and To are equal if and only if
    * their URIs match under the rules above and if their header parameters
    * (such as contact-param, from-param and to-param) match in name and
    * parameter value, where parameter names and token parameter values are
    * compared ignoring case and quoted-string parameter values are case-
    * sensitive.
    * @param that the object to compare to.
    * @return true if the object is equal to this object.
    */
    public boolean   equals ( Object that ) {    
        
	if (that == null) return false;
      
        if (!this.getClass().equals(that.getClass())){
            return false;
        }
        
        URI other = (URI) that;
        
        if (CompareDecoded(this.scheme,other.scheme) != 0 )  return false;
        // Not a sip uri, just do a byte by byte compare of the decoded
        // URIs.
        if (this.scheme.toLowerCase().compareTo("sip") != 0) {
            if(CompareDecoded(this.encode(),other.encode()) != 0)
                return false;
            else return true;
        }
        
        
        if ( ! this.authority.equals(other.authority) ) return false;
        
        if ( (this.path == null && other.path != null) ||
        (this.path != null && other.path == null)) return false;
        
        if ( this.path != null && other.path != null &&
        CompareDecoded(this.path.encode(),
        other.path.encode()) != 0) {
            return false;
        }
        
        // compare the parameter lists.
	ListIterator li = this.uriParms.listIterator();
	NameValueList hisParms = other.uriParms;
	while(li.hasNext()) {
		NameValue nv = (NameValue) li.next();
		// transport string defaults to udp.
		if (nv.getName().equals(TRANSPORT) ) {
			String value = (String) nv.getValue();
			String hisTransport = 
			   (String) hisParms.getValue(TRANSPORT);
			if (hisTransport == null && 
				value.compareToIgnoreCase("UDP") == 0)  {
			        continue; 
			} else if ( hisTransport == null) {
				 return false;
			} else if 
			    (hisTransport.compareToIgnoreCase(value) == 0)  {
				continue;
			}
		} else {
			NameValue hisnv = hisParms.getNameValue(nv.getName());
			if (hisnv == null) return false;
			else if (! hisnv.equals(nv)) return false;
		}
	}

        // leave headers alone - they are just a screwy way of constructing
        // an entire sip message header as part of a URL.
        return true;
    }
  
    /**
     * Construct a URL from the parsed structure.
     * @return String 
     */
    public String encode() {
        String retval = "";
        if (uriType == ABSOLUTE_URI || uriType == RELATIVE_URI) {
            if (scheme != null) {
                retval += scheme + Separators.COLON;
            }
            if (authority != null) {
                retval += Separators.SLASH + Separators.SLASH;
                retval += authority.encode();
            }
            
            if (path != null) {
                retval += path.encode();
            }
            
            if (opaquePart != null) {
                retval += opaquePart;
            }
            if (!uriParms.isEmpty()) {
                retval += Separators.SEMICOLON+ uriParms.encode();
            }
            if (!qheaders.isEmpty()) {
                retval += Separators.QUESTION +
                qheaders.encode();
            }
            if (fragment != null) {
                retval += Separators.POUND + fragment;
            }
        }  else {
            // This is a SIP url
            retval += scheme + Separators.COLON;
            retval += authority.encode();
            if ( !uriParms.isEmpty()) {
                retval += Separators.SEMICOLON+ uriParms.encode();
            }
            if (!qheaders.isEmpty() ) {
                retval += Separators.QUESTION + qheaders.encode();
            }
        }
        return retval;
    }
    
    
        /** Get the type of URI.
         * @return A string giving the URI type.
         */
    public int getUriType() { 
        return uriType;
    }
    
   /** get the user name.
    * @return The user portion of the uri (null if none exists).
    */
    public String getUser() {
        AuthorityServer thisauth = (AuthorityServer) this.authority;
	if (thisauth == null) return null;
        else return thisauth.getUser();
    }
    
        /**
         * getUser@host
         * @return user@host portion of the uri (null if none exists).
         */
    public  String getUserAtHost () {
        AuthorityServer thisauth = (AuthorityServer) this.authority;
	if (thisauth == null) return null;
        else {
	   if (thisauth.getUser() != null) 
	   return 
 	        thisauth.getUser() + "@" + 
		thisauth.getHostPort().encode();      
	   else return  thisauth.getHostPort().encode();      
	}
    }
    
     /**
      * get the parameter (do a name lookup) and return null if none exists.
      * @param parmname Name of the parameter to get.
      * @return Parameter of the given name (null if none exists).
      */
    public Object getParm(String parmname ) {
        Object obj = uriParms.getValue(parmname);
        return obj;
    }
    
     /**
      * Accessor for the scheme field
      * @return Get the scheme of the URL
      */
    public String getScheme() { 
        return scheme ;
    }
    
      
     /**
      * Accessor for the schemeData field
      * @return Get the scheme data of the URL
      */
    public String getSchemeData() { 
        if (authority==null) return null;
        else return authority.encode();
    }
    
    /**
     * Accessor for authority field
     * @return Get the Authority portion of the URL.
     */
    public	 Authority getAuthority() { 
        return authority ;
    }
    
   /**
    * Return the authority as a Server authority.
    * This is just a conveniance function to avoid class casting at the caller
    * @return The authority poriton of the URL.
    */
    public AuthorityServer getServerAuthority() { 
        return (AuthorityServer) authority;
    }
    
   /**
    * Return the authority as a registry name authority.
    * This is just a conveniance function to avoid class casting at the caller
    * @return The authority poriton of the url if it is in the form
    * of a registry name.
    */
    public AuthorityRegname getRegnameAuthority() { 
        return (AuthorityRegname) authority;
    }
    
       /**
        * Accessor for opaquePart
        * @return Opaque part of the URL.
        */
    public  String getOpaquePart() { 
        return opaquePart ;
    }
    
        /**
         * Get the method parameter.
         * @return Method parameter.
         */
    public String getMethod() {
        return (String) getParm(URIKeywords.METHOD);
    }
    
        /**
         * Accessor for path
         * @return Path portion of the url.
         */
    public  Path getPath() { 
        return path;
    }
    
        /**
         *  Accessor for fragment
         * @return Fragment portion of the url.
         */
    public String getFragment() { 
        return fragment ;
    }
    
        /**
         * Accessor for query string
         * @return The stuff that appears after a ? in the url.
         */
    public String getQuery() { 
        return query ;
    }
    
        /**
         * Accessor for URI parameters
         * @return A name-value list containing the parameters.
         */
    public  NameValueList getUriParms() { 
        return uriParms ;
    }
    
         /**
          * Accessor forSIPObjects
          * @return Get the query headers (that appear after the ? in
          * the URL)
          */
    public NameValueList getQheaders() { 
        return qheaders ;
    }
    
      /**
       * Get the integer ttl value.
       * @return The TTL parameter.
       */
    public int getTTL() {
        Integer ttl = (Integer) uriParms.getValue("ttl");
        if (ttl != null) return ttl.intValue();
        else return -1;
    }
    
     /**
      * get the transport parameter.
      * @return The transport string.
      */
    public String getTransport() {
        if (uriParms != null) {
            return (String) uriParms.getValue(TRANSPORT);
        } else return null;
    }
    
     /**
      * Get the urse parameter.
      * @return User parameter (user= phone or user=ip).
      */
    public String getUserType() {
        return (String) uriParms.getValue(USER);
    }
    
       /**
        * get the maddr value.
        * @return maddr parameter.
        */
    public String getMAddr() {
        String maddr = (String) uriParms.getValue("maddr");
        return maddr;
    }
    
    /**
     * Get the password of the user.
     * @return User password when it embedded as part of the uri
     * ( a very bad idea).
     */
    public String getUserPassword() {
        if (authority == null) return null;
        if (authority instanceof AuthorityServer ) {
            AuthorityServer authServer = (AuthorityServer) authority;
            return authServer.getPassword();
        } else return null;
    }
    
     /**
      * Returns the stucture corresponding to the telephone number
      * provided that the user is a telephone subscriber.
      * @return TelephoneNumber part of the url (only makes sense
      * when user = phone is specified)
      */
    public TelephoneNumber getTelephoneSubscriber() {
        if ( telephoneSubscriber==null ) { 
          
            telephoneSubscriber=new TelephoneNumber();
        }
        return telephoneSubscriber;
    }
    
    /**
     * Get the host and port of the server.
     * @return get the host:port part of the url parsed into a
     * structure.
     */
    public HostPort getHostPort() {
     
        if (authority == null) return null;
        else if (authority instanceof AuthorityRegname) return null;
        else {
          
            AuthorityServer authServer = (AuthorityServer) authority;
            return authServer.getHostPort();
        }
    }
    
    /** Return true if the url has stuff after the ?
     * @param name name of the qheader to fetch.
     * @return value of the qheader.
     */
    public boolean hasQheaders(String name) {
        return qheaders.getValue(name) != null ;
    }
    
     /**
      * return true if the uri has a ttl value in the parameter list.
      * @return true if the uri has a ttl value.
      */
    public boolean hasTTL() {
       
        return   uriParms.getValue("ttl")!= null ;
    }
    
     /**
      * return true if there is a transport string.
      * @return true if the uri has a transport parameter.
      */
    public boolean hasTransport() {
        return (uriParms != null
        && uriParms.getValue(TRANSPORT) != null );
    }
    
     /**
      * return true if the uri has a user parameter.
      * @return True if the user= parameter exists.
      */
    public boolean hasUserType() {
        return uriParms.getValue(USER) != null;
    }
    
     /**
      * Return true if the URL had maddr param.
      * @return true if the maddr= parameter exists.
      */
    public boolean hasMAddr() {
        return uriParms.getNameValue("maddr") != null;
    }
    
        /** Check if the uri has any parameters at all.
         * @return true if the uri has any parameters.
         */
    public boolean hasParameters() {
        return qheaders.size()!=0;
    }
    
     /**
      * Return true if the authority part is a registry name.
      * @return true if the authority field is a registry name.
      */
    public boolean isAuthorityRegistryName() {
        if (authority == null) return false;
        if (authority instanceof AuthorityServer) return false;
        return true;
    }
    
    /**
     * returns true if the user is a telephone subscriber.
     *  If the host is an Internet telephony
     * gateway, a telephone-subscriber field MAY be used instead
     * of a user field. The telephone-subscriber field uses the
     * notation of RFC 2806 [19]. Any characters of the un-escaped
     * "telephone-subscriber" that are not either in the set
     * "unreserved" or "user-unreserved" MUST be escaped. The set
     * of characters not reserved in the RFC 2806 description of
     * telephone-subscriber contains a number of characters in
     * various syntax elements that need to be escaped when used
     * in SIP URLs, for example quotation marks (%22), hash (%23),
     * colon (%3a), at-sign (%40) and the "unwise" characters,
     * i.e., punctuation of %5b and above.
     *
     * The telephone number is a special case of a user name and
     * cannot be distinguished by a BNF. Thus, a URL parameter,
     * user, is added to distinguish telephone numbers from user
     * names.
     *
     * The user parameter value "phone" indicates that the user
     * part contains a telephone number. Even without this
     * parameter, recipients of SIP URLs MAY interpret the pre-@
     * part as a telephone number if local restrictions on the
     * @return true if the user is a telephone subscriber.
     */
    public boolean isUserTelephoneSubscriber() {
        String usrtype = (String) uriParms.getValue(URIKeywords.USER);
        if (usrtype == null) return false;
        return usrtype.equals(URIKeywords.PHONE);
    }
    
    
      /**
       *remove the ttl value from the parameter list if it exists.
       */
    public void removeTTL() {
        if (uriParms != null) uriParms.delete("ttl");
    }
    
    /**
     *Remove the maddr param if it exists.
     */
    public void removeMAddr() {
        if (uriParms != null) uriParms.delete("maddr");
    }
    
    /**
     *Delete the transport string.
     */
    public void removeTransport() {
        if (uriParms != null) uriParms.delete(TRANSPORT);
    }
    
    /** Remove a header given its name (provided it exists).
     * @param name name of the header to remove.
     */
    public void removeHeader(String name) {
        if (qheaders != null) qheaders.delete(name);
    }
    
        /** Remove all headers.
         */
    public void removeHeaders() {
        qheaders = new NameValueList("qheaders");
    }
    
    /**
     * Set the user type.
     */
    public void removeUserType() {
        if (uriParms != null) uriParms.delete(USER);
    }
    
     /**
      *remove the port setting.
      */
    public void removePort() {
        if (authority instanceof AuthorityServer ) {
            AuthorityServer auth_server = (AuthorityServer) authority;
            auth_server.removePort();
        }
    }
    
    /**
     * remove the Method.
     */
    public void removeMethod() {
        if (uriParms != null) uriParms.delete(METHOD);
    }
        
    /** set the type of a uri.
     * @param type type of the uri to set.
     */
    public void setUriType( int type) { 
        uriType = type;
    }
    
    /**
     * Set the user name
     * @param uname user name to set.
     */
    public void setUser(String uname)  {
	if (this.authority == null) {
		this.authority = new AuthorityServer();
	}
	if (this.authority instanceof AuthorityServer)  {
          AuthorityServer thisauth = (AuthorityServer) this.authority;
        
          thisauth.setUser(uname);
	} else {
	   throw new IllegalArgumentException
			("Authority is not of type Server");
        }
    }
    
     /** Set the default parameters for this URI. Do nothing if the parameter is
      * already set to some value. Other wise set it to the given value.
      * @param name Name of the parameter to set.
      * @param value value of the parameter to set.
      */
    public void setDefaultParm( String name, Object value) {
        if (uriParms.getValue(name) == null) {
            NameValue nv = new NameValue(name,value);
            uriParms.add(nv);
        }
    }
    
     /** Set the QHeaders. 
      * @param name Name of the header to set.
      * @param value value of the header to set.
      */
    public void setQHeadersParm( String name, String value) {
        if (qheaders.getValue(name) == null) {
            NameValue nv = new NameValue(name,value);
            qheaders.add(nv);
        }
    }
    
        /** Set the scheme member
         * @param s Scheme to set.
         */
    public void setScheme(String s) { 
        scheme = s ;
    }
    
        /** Set the authority member
         * @param a Authority to set.
         */
    public void setAuthority(Authority a) {
        authority = a ;
    }
    
    /** Set the host for this URI.
     * @param h host to set.
     */
    public void setHost(Host h) {
	if (this.authority == null) {
            
		this.authority = new AuthorityServer();
	}
        if (authority instanceof AuthorityServer) {
       
            AuthorityServer auth_server = (AuthorityServer) authority;
            //auth_server = (AuthorityServer) authority;
            auth_server.setHost(h);
	} else {
            
	   throw new IllegalArgumentException
			("Authority is not of type Server");
        }
    }
    
      /** Set the opaquePart member
       * @param o Opaque part to set.
       */
    public void setOpaquePart(String o) { 
        opaquePart = o ;
    }
    
        /** Set the path member
         * @param p path member to set.
         */
    public void setPath(Path p) { 
        path = p ;
    }
    
        /** Set the fragment member
         * @param f Fragment to set.
         */
    public void setFragment(String f) { 
        fragment = f ;
    }
    
        /** Set the query member
         * @param q query string to set.
         */
    public void setQuery(String q) { 
        query = q ;
    }
    
        /** Set the uriParms member
         * @param parms URI parameters to set.
         */
    public void setUriParms(NameValueList parms ) { 
        uriParms = parms ;
    }
    
        /**
         * Set a given URI parameter. Note - parameter must be properly
	 *  encoded before the function is called.
         * @param name Name of the parameter to set.
         * @param value value of the parameter to set.
         */
    public  void setUriParm(String name, Object value) {
        NameValue nv = new NameValue(name,value);
        uriParms.add(nv);
    }
    
        /** Set the qheaders member
         * @param parms query headers to set.
         */
    public void setQheaders(NameValueList parms) { 
        qheaders = parms ;
    }
    
    /** set the ttl value to the given value.
     * @param ttl set the ttl parameter
     */
    public void setTTL( int ttl) {
        if (uriParms != null) {
            uriParms.delete("ttl");
            NameValue nv = new NameValue("ttl", new Integer(ttl));
            uriParms.add(nv);
          
        }
    }
    
      /** set the transport parameter.
       * @param transport set the transport parameter
       */
    public void setTransport(String  transport) {
        NameValue nv = new NameValue(TRANSPORT,transport.toLowerCase());
        uriParms.delete(TRANSPORT);
        uriParms.add(nv);
    }
    
      /** set the mAddr parameter.
       * @param maddr maddr to set.
       */
    public void setMAddr(String  maddr) {
        NameValue nv = new NameValue("maddr",maddr);
        uriParms.delete("maddr");
        uriParms.add(nv);
    }
    
   /** set the user type.
    * @param usertype user (IP or Phone)
    */
    public void setUserType( String usertype) {
        uriParms.delete(USER);
        uriParms.add(USER,usertype);
    }
    
     /** set the Method
      * @param method method parameter
      */
    public void setMethod(String method) {
        uriParms.add(METHOD,method);
    }
    
     /**
     * Sets ISDN subaddress of SipURL
     * @param <var>isdnSubAddress</var> ISDN subaddress
     */
    public void setIsdnSubAddress(String isdnSubAddress) {
       if ( telephoneSubscriber==null) telephoneSubscriber=new TelephoneNumber();
       telephoneSubscriber.setIsdnSubaddress(isdnSubAddress);
    }
    
    /** Set the telephone subscriber field.
     * @param tel Telephone subscriber field to set.
     */
    public void setTelephoneSubscriber( TelephoneNumber tel) {
        telephoneSubscriber = tel;
    }
    
    /** set the user password
     * @param password password to set.
     */
    public void setUserPassword(String password) {
        if (authority == null) return;
        if (authority instanceof AuthorityRegname) return;
        AuthorityServer authServer = (AuthorityServer) authority;
        authServer.setPassword(password);
    }
    
    /** set the port to a given value.
     * @param p Port to set.
     */
    public void setPort(int p ) {
        if (authority instanceof AuthorityServer) {
            AuthorityServer auth_server = (AuthorityServer) authority;
            auth_server.setPort(p);
        }
    }
    
    /** Boolean to check if a parameter of a given name exists.
     * @param name Name of the parameter to check on.
     * @return a boolean indicating whether the parameter exists.
     */
    public boolean hasParameter( String name) {
        
        return uriParms.getValue(name) != null;
    }
    
    /**
     * Remove a parameter given its name
     * @param name -- name of the parameter to remove.
     */
    public void removeParameter(String name) {
 	Debug.println("URI.remove() " + name);
        uriParms.delete(name);
    }
    
}
