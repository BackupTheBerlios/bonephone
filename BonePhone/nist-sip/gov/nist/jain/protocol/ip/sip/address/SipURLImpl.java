/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: M. Ranganathan (mranga@nist.gov)                                    *
* Modified By:  O. Deruelle (deruelle@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

/*
 * SipURLImpl.java
 *
 * Created on April 4, 2001, 9:29 PM
 */

package gov.nist.jain.protocol.ip.sip.address;

import  jain.protocol.ip.sip.*;
import  jain.protocol.ip.sip.address.*;

import  gov.nist.sip.net.*;
import  gov.nist.sip.*;
import  gov.nist.sip.sipheaders.*;
import  gov.nist.sip.msgparser.*;


import  java.lang.reflect.*;
import  java.net.*;
import  java.util.*;
import  java.lang.*;
import  java.text.*;
import  java.io.*;

/**
 *
 * @author  M. Ranganathan
 * @version 1.0
 */
public class SipURLImpl extends URIImpl implements SipURL {
    
    
    /** Creates new SipURLImpl */
    public SipURLImpl() {
      
        uri = new gov.nist.sip.net.URI();
    }
    
    public SipURLImpl(gov.nist.sip.net.URI u) {
        uri = u;
	u.setScheme(SIPKeywords.SIP);
    }
    
    /**
     * Gets ISDN subaddress of SipURL
     * (Returns null if ISDN subaddress does not exist)
     * @return ISDN subaddress of SipURL
     */
    public String getIsdnSubAddress() {
      
        if (!uri.isUserTelephoneSubscriber()) return null;
        return uri.getTelephoneSubscriber().getIsdnSubaddress();
    }
    
    /**
     * Gets boolean value to indicate if SipURL
     * has ISDN subaddress
     * @return boolean value to indicate if SipURL
     * has ISDN subaddress
     */
    public boolean hasIsdnSubAddress() {
       
        if (! uri.isUserTelephoneSubscriber() ) {
            return false;
        }
        TelephoneNumber tel = uri.getTelephoneSubscriber();
        if (tel == null) return false;
        return tel.hasIsdnSubaddress();
        
    }
    
    /**
     * Removes ISDN subaddress from SipURL (if it exists)
     */
    public void removeIsdnSubAddress() {
        
        if ( !uri.isUserTelephoneSubscriber()) return;
        TelephoneNumber tel = uri.getTelephoneSubscriber();
        tel.removeIsdnSubaddress();
        
    }
    
    /**
     * Gets post dial of SipURL
     * (Returns null if post dial does not exist)
     * @return post dial of SipURL
     */
    public String getPostDial() {
   
        if (!uri.isUserTelephoneSubscriber()) {
            
            return null;
        }
        TelephoneNumber tel = uri.getTelephoneSubscriber();
        if (tel != null) {
           
            return tel.getPostDial();
        }
        else {
         
            return null;
        }
        
    }
    
    /**
     * Gets boolean value to indicate if SipURL
     * has post dial
     * @return boolean value to indicate if SipURL
     * has post dial
     */
    public boolean hasPostDial() {
        if (!uri.isUserTelephoneSubscriber()) {
           
            return false;
        }
        TelephoneNumber tel = uri.getTelephoneSubscriber();

         if (tel != null) { 
               
             return tel.hasPostDial();
         }
         else {
              
             return false;
         }
    }
    
     /**
     * Sets post dial of SipURL
     * @param <var>postDial</var> post dial
     * @throws IllegalArgumentException if postDial is null
     * @throws SipException if user type is not USER_TYPE_PHONE
     * @throws SipParseException if postDial is not accepted by implementation
     */
    public void setPostDial(String postDial)
    throws IllegalArgumentException, SipException, SipParseException {
        if (postDial==null) {
         
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION : postDial is null");
        }
        else {
            if (!uri.isUserTelephoneSubscriber()) {
              
                throw new SipException
                ("JAIN-SIP EXCEPTION :user type is not USER_TYPE_PHONE ");
            }
            else {
              
                TelephoneNumber tel=uri.getTelephoneSubscriber();
                tel.setPostDial(postDial);
            }
        }
    }
    
    /**
     * Removes post dial from SipURL (if it exists)
     */
    public void removePostDial() {
       
        if (!uri.isUserTelephoneSubscriber()) return;
        TelephoneNumber tel = uri.getTelephoneSubscriber();
        if (tel != null) {
         
            tel.removePostDial();
        }
    }
    
    /**
     * Gets user name of SipURL
     * (Returns null if user name does not exist)
     * @return user name of SipURL
     */
    public String getUserName() {
      
        String user=uri.getUser();
      
        return user;
    }
    
    /**
     * Gets boolean value to indicate if SipURL
     * has user name
     * @return boolean value to indicate if SipURL
     * has user name
     */
    public boolean hasUserName() {
     
        return uri.getUser() != null;
    }
    
    /**
     * Removes user name from SipURL (if it exists)
     */
    public void removeUserName() {
     
        uri.setUser(null);
    }
    
    /**
     * Sets user name of SipURL
     * @param <var>userName</var> user name
     * @throws IllegalArgumentException if userName is null
     * @throws SipParseException if userName is not accepted by implementation
     */
    public void setUserName(String userName)
    throws IllegalArgumentException, SipParseException {
      
        if (userName == null) {
           
            throw new IllegalArgumentException("Null user name");
        }
        else { 
      
            uri.setUser(userName);
        }
    }
    
    /**
     * Gets user password of SipURL
     * (Returns null if user pasword does not exist)
     * @return user password of SipURL
     */
    public String getUserPassword() {
     
        return uri.getUserPassword();
    }
    
    /**
     * Gets boolean value to indicate if SipURL
     * has user password
     * @return boolean value to indicate if SipURL
     * has user password
     */
    public boolean hasUserPassword() {
     
        return uri.getUserPassword() != null;
    }
    
    /**
     * Removes user password from SipURL (if it exists)
     */
    public void removeUserPassword() {
     
        uri.setUserPassword(null);
    }
    
    /**
     * Sets user password of SipURL
     * @param <var>userPassword</var> user password
     * @throws IllegalArgumentException if userPassword is null
     * @throws SipException if user name does not exist
     * @throws SipParseException if userPassword is not accepted
     * by implementation
     */
    public void setUserPassword(String userPassword)
    throws IllegalArgumentException, SipException, SipParseException {
      
        if (userPassword == null) {
            throw new IllegalArgumentException("Null password!");
        }
        uri.setUserPassword(userPassword);
    }
    
    /**
     * Gets host of SipURL
     * @return host of SipURL
     */
    public String getHost() {
       
        gov.nist.sip.net.HostPort hp = uri.getHostPort();
      
        if (hp == null) return null;
       
        //String res=hp.getHost().encode();
        String res=hp.getHost().getHostname();
       
        return res;
    }
    
    /**
     * Sets host of SipURL
     * @param <var>host</var> host
     * @throws IllegalArgumentException if host is null
     * @throws SipParseException if host is not accepted by implementation
     */
    public void setHost(String host)
    throws IllegalArgumentException, SipParseException {
        
      
        if (host == null) {
           
            throw new IllegalArgumentException("Null Host!");
        }
        try {
            StringMsgParser smp = new StringMsgParser();
	    // (sfo) hack to get get around the parseHost errors
	    gov.nist.sip.net.URI u = smp.parseSIPUrl("sip:sfo@" + host);
	    Host h = u.getHostPort().getHost();
            //Host h = smp.parseHost(host);
            uri.setHost(h);
          
        } catch (SIPParseException ex) {
            throw new SipParseException(host);
        }
        
    }
    
    /**
     * Sets host of SipURL
     * @param <var>host</var> host
     * @throws IllegalArgumentException if host is null
     * @throws SipParseException if host is not accepted by implementation
     */
    public void setHost(InetAddress host)
    throws IllegalArgumentException, SipParseException {
        
        if (host == null) throw new IllegalArgumentException("Null host!");
        try {
            
            String hname = host.getHostName();
            String hostAddress=host.getHostAddress();
            
         
            gov.nist.sip.net.Host h =
            new gov.nist.sip.net.Host
            (hname,gov.nist.sip.net.HostAddrTypes.HOSTNAME);
            h.setHostAddress(hostAddress);
            uri.setHost(h);
           
        } catch (Exception ex) {
            throw new SipParseException (host.toString());
        }
        
    }
    
    /**
     * Gets port of SipURL
     * (Returns negative int if port does not exist)
     * @return port of SipURL
     */
    public int getPort() {
        HostPort hp=uri.getHostPort();
     
        return hp.getPort();
    }
    
    /**
     * Gets boolean value to indicate if SipURL
     * has port
     * @return boolean value to indicate if SipURL
     * has port
     */
    public boolean hasPort() {
        HostPort hp=uri.getHostPort();
        return hp.hasPort();
    }
    
    /**
     * Removes port from SipURL (if it exists)
     */
    public void removePort() {
        uri.removePort();
    }
    
    /**
     * Sets port of SipURL
     * @param <var>port</var> port
     * @throws SipParseException if port is not accepted by implementation
     */
    public void setPort(int port)
    throws SipParseException {
     
        uri.setPort(port);
    }
    
    /**
     * Gets TTL of SipURL
     * (Returns negative int if TTL does not exist)
     * @return TTL of SipURL
     */
    public int getTTL() {
        
        return uri.getTTL();
    }
    
    /**
     * Gets boolean value to indicate if SipURL
     * has TTL
     * @return boolean value to indicate if SipURL
     * has TTL
     */
    public boolean hasTTL() {
           return uri.hasTTL();
    }
    
    /**
     * Removes TTL from SipURL (if it exists)
     */
    public void removeTTL() {
        uri.removeTTL();
    }
    
    /**
     * Sets TTL of SipURL
     * @param <var>ttl</var> TTL
     * @throws SipParseException if ttl is not accepted by implementation
     */
    public void setTTL(int ttl) throws SipParseException {
       
        uri.setTTL(ttl);
    }
    
    /**
     * Gets transport of SipURL
     * (Returns null if transport does not exist)
     * @return transport of SipURL
     */
    public String getTransport() {
        return uri.getTransport();
    }
    
    /**
     * Gets boolean value to indicate if SipURL
     * has transport
     * @return boolean value to indicate if SipURL
     * has transport
     */
    public boolean hasTransport() {
        return uri.hasTransport();
        
    }
    
    /**
     * Removes transport from SipURL (if it exists)
     */
    public void removeTransport() {
        uri.removeTransport();
    }
    
    /**
     * Sets transport of SipURL
     * @param <var>transport</var> transport
     * @throws IllegalArgumentException if transport is null
     * @throws SipParseException if transport is not accepted by
     * implementation
     */
    public void setTransport(String transport)
    throws IllegalArgumentException, SipParseException {
        if (transport == null)
            throw new IllegalArgumentException("null transport");
        uri.setTransport(transport);
    }
    
    /**
     * Gets user type of SipURL
     * (Returns null if user type does not exist)
     * @return user type of SipURL
     */
    public String getUserType() {
      
        return  uri.getUserType();
    }
    
    /**
     * Gets boolean value to indicate if SipURL
     * has user type
     * @return boolean value to indicate if SipURL
     * has user type
     */
    public boolean hasUserType() {
      
        return uri.hasUserType();
    }
    
    /**
     * Removes user type from SipURL (if it exists)
     */
    public void removeUserType() {
       
        uri.removeUserType();
    }
    
    /**
     * Sets user type of SipURL
     * @param <var>userType</var> user type
     * @throws IllegalArgumentException if userType is null
     * @throws SipParseException if userType is not accepted by implementation
     */
    public void setUserType(String userType) throws
    IllegalArgumentException, SipParseException {
        if (userType==null) {
               
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION : userType is null");
        }
        else { 
            uri.setUserType(userType);
          
        }
    }
    
    /**
     * Gets method of SipURL
     * (Returns null if method does not exist)
     * @return method of SipURL
     */
    public String getMethod() {
        return uri.getMethod();
    }
    
    /**
     * Gets boolean value to indicate if SipURL
     * has method
     * @return boolean value to indicate if SipURL
     * has method
     */
    public boolean hasMethod() {
        return uri.getMethod()!=null;
    }
    
    /**
     * Removes method from SipURL (if it exists)
     */
    public void removeMethod() {
        uri.removeMethod();
    }
    
    /**
     * Sets method of SipURL
     * @param <var>method</var> method
     * @throws IllegalArgumentException if method is null
     * @throws SipParseException if method is not accepted by implementation
     */
    public void setMethod(String method)
    throws IllegalArgumentException, SipParseException {
        if ( method==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: method is null");
        else {
            removeMethod();
            uri.setMethod(method);
        }
    }
    
    /**
     * Sets ISDN subaddress of SipURL
     * @param <var>isdnSubAddress</var> ISDN subaddress
     * @throws IllegalArgumentException if isdnSubAddress is null
     * @throws SipException if user type is not USER_TYPE_PHONE
     * @throws SipParseException if isdnSubAddress is not accepted
     * by implementation
     */
    public void setIsdnSubAddress(String isdnSubAddress)
    throws IllegalArgumentException, SipException, SipParseException {
        if ( isdnSubAddress==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: isdnSubAddress is null");
        else {
            if (!uri.isUserTelephoneSubscriber())
               throw new SipException
               ("JAIN-SIP EXCEPTION :user type is not USER_TYPE_PHONE ");
            else {
                uri.setIsdnSubAddress(isdnSubAddress);
            }
        }
    }
    
    
   
    
    /**
     * Gets MAddr of SipURL
     * (Returns null if MAddr does not exist)
     * @return MAddr of SipURL
     */
    public String getMAddr() {
        return uri.getMAddr();
    }
    
    /**
     * Gets boolean value to indicate if SipURL
     * has MAddr
     * @return boolean value to indicate if SipURL
     * has MAddr
     */
    public boolean hasMAddr() {
        return uri.hasMAddr();
    }
    
    /**
     * Removes MAddr from SipURL (if it exists)
     */
    public void removeMAddr() {
        uri.removeMAddr();
    }
    
    /**
     * Sets MAddr of SipURL
     * @param <var>mAddr</var> MAddr
     * @throws IllegalArgumentException if mAddr is null
     * @throws SipParseException if mAddr is not accepted by implementation
     */
    public void setMAddr(String mAddr)
    throws IllegalArgumentException, SipParseException {
        if (mAddr==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: mAddr is null ");
        else uri.setMAddr(mAddr);
    }
    
    /**
     * Sets MAddr of SipURL
     * @param <var>mAddr</var> MAddr
     * @throws IllegalArgumentException if mAddr is null
     * @throws SipParseException if mAddr is not accepted by implementation
     */
    public void setMAddr(InetAddress mAddr) throws
    IllegalArgumentException, SipParseException {
       if (mAddr==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: mAddr is null ");
       else {
            String hostName=mAddr.getHostName();
            uri.setMAddr(hostName);
       }
    }
    
    /**
     * Returns boolean value to indicate if the SipURL
     * has a global phone user
     * @return boolean value to indicate if the SipURL
     * has a global phone user
     * @throws SipException if user type is not USER_TYPE_PHONE
     */
    public boolean isGlobal() throws SipException {
        if (!uri.isUserTelephoneSubscriber())
            throw new SipException
            ("JAIN-SIP EXCEPTION :user type is not USER_TYPE_PHONE ");
        else return uri.getTelephoneSubscriber().isGlobal();
    }
    
    /**
     * Sets phone user of SipURL to be global or local
     * @param <var>global</var> boolean value indicating
     * if phone user should be global
     * @throws SipException if user type is not USER_TYPE_PHONE
     */
    public void setGlobal(boolean global)
    throws SipException, SipParseException {
        if (!uri.isUserTelephoneSubscriber())
            throw new SipException
            ("JAIN-SIP EXCEPTION :user type is not USER_TYPE_PHONE ");
        else  uri.getTelephoneSubscriber().setGlobal(global);
    }
    
    /**
     * Gets Iterator of header names
     * (Returns null if no headers exist)
     * @return Iterator of header names
     */
    public Iterator getHeaders() {
     
        Iterator iterator=uri.getQheaders().getIterator();
        if ( iterator.hasNext() ) return iterator;
        else return null;
    }
    
    /**
     * Gets the value of specified header
     * (Returns null if header does not exist)
     * @param <var>name</var> name of header to retrieve
     * @return the value of specified header
     * @throws IllegalArgumentException if header is null
     */
    public String getHeader(String name) throws IllegalArgumentException {
        if (name==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: parameter is null");
        else {
            NameValueList nvl=uri.getQheaders();
          
            return (String)nvl.getValue(name);
        }
    }
    
    /**
     * Sets value of header
     * @param <var>name</var> name of header
     * @param <var>value</var> value of header
     * @throws IllegalArgumentException if name or value is null
     * @throws SipParseException if name or value is not accepted by
     * implementation
     */
    public void setHeader(String name,String value)
    throws IllegalArgumentException, SipParseException {
        if (name==null || value==null )
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION : name or value is null");
        else { 
         
                uri.setQHeadersParm(name,value);
        }
    }
    
    
    /**
     * Gets boolean value to indicate if SipURL
     * has any headers
     * @return boolean value to indicate if SipURL
     * has any headers
     */
    public boolean hasHeaders() {
       
        return getHeaders()!=null;
    }
    
    /**
     * Gets boolean value to indicate if SipUrl
     * has specified header
     * @return boolean value to indicate if SipUrl
     * has specified header
     * @throws IllegalArgumentException if name is null
     */
    public boolean hasHeader(String name) throws IllegalArgumentException {
        if( name==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION : name is null");
        else 
        {
           
            return uri.hasQheaders(name);
        }
    }
    
    /**
     * Removes specified header from SipURL (if it exists)
     * @param <var>name</var> name of header
     * @throws IllegalArgumentException if name is null
     */
    public void removeHeader(String name) throws IllegalArgumentException {
        if (name==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION : name is null");
        else {
         
            uri.removeHeader(name);
        }
    }
    
    /**
     * Removes all parameters from Parameters (if any exist)
     */
    public void removeHeaders() {
       
        uri.removeHeaders();
    }
    
    
    /**
     * Gets string representation of URI
     * @return string representation of URI
     */
    public String toString() {
        return uri.encode();
    }
    
    
    /**
     * Sets scheme of URI
     * @param <var>scheme</var> scheme
     * @throws IllegalArgumentException if scheme is null
     * @throws SipParseException if scheme is not accepted by implementation
     */
    public void setScheme(String scheme)
    throws IllegalArgumentException, SipParseException {
        if (scheme==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION : scheme is null");
        else uri.setScheme(scheme);
    }
    
    /**
     * Gets scheme of URI
     * @return scheme of URI
     */
    public String getScheme() {
        return uri.getScheme();
    }
    
    /**
     * Indicates whether some other Object is "equal to" this URI
     * (Note that obj must have the same Class as this URI - this means that it
     * must be from the same JAIN SIP implementation)
     * @param <var>obj</var> the Object with which to compare this URI
     * @returns true if this URI is "equal to" the obj
     * argument; false otherwise (equality of URI's is defined in RFC 2068)
     */
    public boolean equals(Object obj) {
        if ( obj!=null) {
            if ( obj instanceof SipURLImpl) {
                SipURLImpl sipURLImpl=(SipURLImpl)obj;   
                boolean b=uri.equals( sipURLImpl.uri );
                return b;
            }
        }
        return false; 
    }
    
    /**
     * Creates and returns a copy of URI
     * @returns a copy of URI
     */
    public Object clone() {
        SipURLImpl cloneobj = new SipURLImpl();
        cloneobj.uri = (gov.nist.sip.net.URI) this.uri.clone();
        return cloneobj;
    }
    
    /**
     * Gets Iterator of parameter names
     * (Note - objects returned by Iterator are Strings)
     * (Returns null if no parameters exist)
     * @return Iterator of parameter names
     */
    public Iterator getParameters() {
        
        Iterator iterator=uri.getUriParms().getIterator();
        if ( iterator.hasNext() ) return iterator;
        else return null;
    }
    
    /**
     * Gets the value of specified parameter
     * (Note - zero-length String indicates flag parameter)
     * (Returns null if parameter does not exist)
     * @param <var>name</var> name of parameter to retrieve
     * @return the value of specified parameter
     * @throws IllegalArgumentException if name is null
     */
    public String getParameter(String name) throws IllegalArgumentException {
        if (name == null) throw new
        IllegalArgumentException("JAIN-EXCEPTION: null argument");
       
        return (String) uri.getUriParms().getValue(name);
    }
    
    /**
     * Sets value of parameter
     * (Note - zero-length value String indicates flag parameter)
     * @param <var>name</var> name of parameter
     * @param <var>value</var> value of parameter
     * @throws IllegalArgumentException if name or value is null
     * @throws SipParseException if name or value is not accepted
     * by implementation
     */
    public void setParameter(String name,String value)
    throws IllegalArgumentException, SipParseException {
        if (name == null || value == null) {
            throw new
            IllegalArgumentException
            ("JAIN-EXCEPTION: Name or value is null!");
        } else{
          
            uri.setDefaultParm(name,value);
        }
    }
    
    /**
     * Gets boolean value to indicate if Parameters
     * has any parameters
     * @return boolean value to indicate if Parameters
     * has any parameters
     */
    public boolean hasParameters() {
     
        return  uri.hasParameters();
    }
    
    /**
     * Gets boolean value to indicate if Parameters
     * has specified parameter
     * @return boolean value to indicate if Parameters
     * has specified parameter
     * @throws IllegalArgumentException if name is null
     */
    public boolean hasParameter(String name) throws IllegalArgumentException {
        if(name == null) throw new IllegalArgumentException
        ("JAIN-EXCEPTION: null parameter");
         
        
        return uri.hasParameter(name);
    }
    
    /**
     * Removes specified parameter from Parameters (if it exists)
     * @param <var>name</var> name of parameter
     * @throws IllegalArgumentException if parameter is null
     */
    public void removeParameter(String name) throws IllegalArgumentException {
        
        if ( name==null) {
            throw new IllegalArgumentException("JAIN-EXCEPTION: " + name);
        }
       
        uri.removeParameter(name);
    }
    
    /**
     * Removes all parameters from Parameters (if any exist)
     */
    public void removeParameters() {
        
        uri.clearUriParms();
    }
    
   /** 
    * This method checks if the "get" methods of this JAIN-SIP implementation are 
    * implemented right. So, we use the java.lang.reflect 
    * package to do some generic tests.
    */    
    public void testGetMethods(PrintStream testResults) {
        Class c= this.getClass();
        Method[] theMethods = c.getMethods();
        
        try { 
            // test the get<Field> methods:
            for (int i = 0; i < theMethods.length; i++) {
                String methodString = theMethods[i].getName();
                Class classType= theMethods[i].getReturnType();  
                String returnString=classType.getName();
            
                if ( methodString.startsWith("get") &&
                     !methodString.equals("getHeader") &&
                     !methodString.equals("getParameter") &&
                     !methodString.equals("getClass")
                   ) {
                   
                    Object returnObject = theMethods[i].invoke(this,null);
                   
                    if ( returnObject != null ) {
                        testResults.println
                            ("    Name method : " + methodString);
                        if ( methodString.endsWith("Headers") ) {
                            // for Iterator returnType
                            Iterator iterator=(Iterator) returnObject;
                            int j=0;
                            while ( iterator.hasNext() ) {
                                Object o=iterator.next();j++;
                                if ( o instanceof NameValue) {
                                      NameValue nv=(NameValue)o;
                                      testResults.println
                                        ("      element "+j+" : "+nv.getName());
                                }
                                else 
                                      testResults.println
                                        ("      element "+j+" : "+o.toString());
                            }
                            if (j==0) testResults.print("   no headers");
                            testResults.println("    TEST OK");
                        } else 
                        if ( methodString.equals("getImplementationObject") )
                                 testResults.println("    TEST OK");
                        else
                        if ( methodString.equals("getParameters") ) {
                             Iterator iterator=(Iterator)returnObject;
                             int j=0;
                             while ( iterator.hasNext() ) {
                                  Object o=iterator.next();j++;
                                  if ( o instanceof NameValue) {
                                      NameValue nv=(NameValue)o;
                                      testResults.println
                                        ("      element "+j+" : "+nv.getName());
                                  }
                                  else 
                                  testResults.println
                                        ("      element "+j+" : "+o.toString());
                             }
                             if (j==0)   
                                  testResults.println
                                            ("  no parameters         TEST OK");
                             else
                                  testResults.println("               TEST OK");
                        } else 
                                  testResults.println
                                ("   return value: "+returnObject+"   TEST OK");
                        testResults.println();
                    }
                }
            }
        }
        catch (IllegalAccessException e) {
              testResults.println(e);
        }
        catch (InvocationTargetException e) {
               testResults.println(e);
               e.printStackTrace();
               Throwable t=e.getTargetException(); 
               testResults.println(((SipParseException)t).getUnparsable());
        }
        catch (NoSuchElementException e) {
              testResults.println(e);
        }
    }
    
   
   /** 
    * This method checks if the "set" methods of the SipURL message 
    * are implemented right. So, we use the java.lang.reflect package to do some
    * generic tests.
    */    
    public void testSetMethods(PrintStream testResults) {
        try 
        {
            Class c= this.getClass();
            Method[] theMethods = c.getMethods();
            String className=c.getName();
            testResults.println("    Test of the SipURLImpl : ");
       
            // variable using to differentiate the parameters
            int numero=0;
        
            // variable using as a parameter for the "getParameter(name)" method
            String parameterSet="";
        
       
             for (int i = 0; i < theMethods.length; i++) {
                 String methodString = theMethods[i].getName();
                      
                 // test the set<Field> methods:
                 if ( methodString.startsWith("set") &&
                      ! methodString.equals("setImplementationObject")
                    ) 
                 {
                    Class [] parameterTypes= theMethods[i].getParameterTypes(); 
                    Object [] arguments = new Object[parameterTypes.length];
                    testResults.print("    Name method : " + methodString+"( ");       
                    if ( methodString.equals("setParameter") ) {
                        arguments[0]=("variable"+numero);numero++;
                        parameterSet=(String)arguments[0];
                        arguments[1]=("value"+numero);numero++;
                        testResults.print
                            ( (String)arguments[0]+" , "+ (String)arguments[1] );
                    } else
                    for (int j = 0; j < parameterTypes.length; j++) {
                         Class parameterClass = parameterTypes[j];
                         String parameterName=parameterClass.getName();
                         //testResults.println(parameterName);
                         if ( parameterName.equals("long") ) {
                             arguments[j]= new Integer(numero);
                             testResults.print(numero);
                         } else
                         if ( parameterName.equals("int") ) {                                
                             arguments[j]= new Integer(numero);
                             testResults.print(numero);
                         } else
                         if ( parameterName.equals("java.lang.String") ) {
                             if ( methodString.equals("setDate") ) {
                                  // Format the current time.
                                  SimpleDateFormat formatter=new SimpleDateFormat
                                            ("M/d/yy h:mm a");
                                  Date currentTime_1 = new Date();
                                  String dateString = 
                                                formatter.format(currentTime_1);
                                  
                                   arguments[j]=dateString;
                                   testResults.print(dateString);
                             } if ( methodString.equals("setUserType") ) {                                
                                   arguments[j]="phone";
                                   testResults.print("phone");
                             } else {
                                arguments[j]= new String("variable"+numero);
                                testResults.print("variable"+numero);
                             }
                         } else
                         if ( parameterName.equals("float") ) {                                
                             arguments[j]= new Integer(1);
                             testResults.print("1");
                         } else
                         if ( parameterName.equals("boolean") ) { 
                             Boolean bool=new Boolean("true");
                             arguments[j]=bool;
                             testResults.print("true");
                         } else 
                         if ( parameterName.equals("java.net.InetAddress") ) {
                             arguments[j]=InetAddress.getLocalHost(); 
                             testResults.print("inetAddress"); 
                         } else 
                         if ( parameterName.equals("java.util.Date") ) {
                             long date=0;
                             arguments[j]=new Date(date);
                             testResults.print(date);
                         } else
                         if ( parameterName.equals("java.util.List") ) {
                             Vector list=new Vector();
                             list.add("variable"+numero);
                             arguments[j]=list;
                             testResults.print("list"); 
                         } else
                         if ( parameterName.equals
                         ("jain.protocol.ip.sip.address.NameAddress") ) {
                             NameAddressImpl na=new NameAddressImpl();
                             na.setDisplayName("variable"+numero);
                             testResults.print("variable"+numero); numero++;
                             URIImpl u=new  URIImpl();
                             u.setSchemeData("variable"+numero);numero++;
                             u.setScheme("variable"+numero);
                             na.setAddress(u);
                             arguments[j]=na;
                         } else
                         { 
                            arguments[j]= c.newInstance();
                         }
                         numero++;
                         if ( j != (parameterTypes.length-1) ) 
                                                        testResults.print(" , ");
                    }
                    testResults.print(" )");
                    Object returnObject = theMethods[i].invoke(this,arguments);
                    testResults.println("               TEST OK");
                 }
             }
             testResults.println();
             for (int i = 0; i < theMethods.length; i++) {
                String methodString = theMethods[i].getName();
                    
                // test the get<Field> methods:
                if (  methodString.startsWith("get")  &&
                      ! methodString.equals("getHeader") &&
                      ! methodString.equals("getHeaders") &&
                      ! methodString.equals("getClass")
                   ) {   
                    Object returnObject;
                    if ( methodString.equals("getParameter") ) {
                         Object [] arguments = new Object[1];
                         arguments[0]=parameterSet; 
                         returnObject = theMethods[i].invoke(this,arguments);  
                    } else
                         returnObject = theMethods[i].invoke(this,null);  
                           
                    if ( returnObject == null ) {
                          testResults.println("    Name method: " + methodString);
                          testResults.println
                               ("    Return value : null        TEST NOT OK");
                          testResults.println();   
                    }
                    else {
                       testResults.println("    Name method: " + methodString);
                       if ( methodString.equals("getParameters") ) {
                             Iterator iterator=(Iterator)returnObject;
                             int j=0;
                             while ( iterator.hasNext() ) {
                                  NameValue nv=(NameValue)iterator.next();j++;
                                  testResults.println
                                        ("      element "+j+" : "+nv.getName());
                             }
                             if (j==0)   
                                  testResults.println
                                            ("  no parameters         TEST OK");
                             else
                                  testResults.println("               TEST OK");
                       } else 
                       if ( methodString.equals("getNameAddress") ) {
                           NameAddressImpl na=(NameAddressImpl)returnObject;
                           String displayName=na.getDisplayName();
                           testResults.println("    DisplayName: "+displayName);
                           URIImpl uri=(URIImpl)na.getAddress();
                           String schemeData=uri.getSchemeData();
                           String scheme=uri.getScheme();
                           testResults.println("    schemeData: "+schemeData);
                           testResults.println("    scheme: "+scheme);
                       } else 
                       if ( methodString.equals("getProducts") )  {
                             Iterator iterator=(Iterator)returnObject;
                             int j=0;
                             if (iterator!=null) {
                                while ( iterator.hasNext() ) {
                                  String s=(String)iterator.next();j++;
                                  testResults.println
                                        ("      element "+j+" : "+s);
                                }
                             }
                             if (j==0)   
                                  testResults.println
                                            ("  no products         TEST OK");
                             else
                                  testResults.println("               TEST OK");      
                       } else 
                       if ( methodString.equals("getImplementationObject") )  {      
                            testResults.println
                        ("    Return Value: "+((NetObject)returnObject).getClass());
                          testResults.println("   TEST OK");
                       } else
                       if ( methodString.equals("getDate") )  { 
                            Date date=(Date)returnObject;
                            testResults.println(date.toString());
                       } else {
                             testResults.print
                              ("    Return value : "+ returnObject );  
                             testResults.println("               TEST OK");
                         }
                       testResults.println();    
                    }
                }
             }
        }
        catch (IllegalAccessException e) {
                testResults.println(e);
        }
        catch (InvocationTargetException e) {
                 testResults.println(e);
                 e.printStackTrace();
                 Throwable t=e.getTargetException(); 
                 testResults.println(((SipParseException)t).getUnparsable());
        }
        catch (UnknownHostException e) {
                testResults.println(e);
        }
        catch (InstantiationException e) {
                testResults.println(e);
                e.printStackTrace(); 
        }
        catch (SipParseException e) {
                testResults.println(e);
                e.printStackTrace(); 
        }
    }
    
}
