package gov.nist.jain.protocol.ip.sip.address;

import  jain.protocol.ip.sip.header.*;
import  jain.protocol.ip.sip.*;
import  jain.protocol.ip.sip.address.*;

import  gov.nist.sip.sipheaders.*;
import  gov.nist.log.*;

import  java.net.*;
import  java.util.*;

/**
 *
 * @author  olivier Deruelle ( deruelle@nist.gov )
 * Modifications by M. Ranganathan 
 *  -- various fixes to make things pass the tck!
 * @version 1.0
 */
public class AddressFactoryImpl implements AddressFactory {

    /**
     * Creates a SipURL based on given host
     * @param <var>host</var> host
     * @throws IllegalArgumentException if host is null
     */
    public SipURL createSipURL(InetAddress host)
    throws IllegalArgumentException,SipParseException {
	if (LogWriter.needsLogging()) 
	    LogWriter.logMessage("createSipURL () : " + host);
        if ( host==null)
            throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: host is null");
		
        SipURLImpl sipUrlImpl=new SipURLImpl();
        sipUrlImpl.setHost(host);
        
        return sipUrlImpl;
    }
    
    /**
     * Creates a SipURL based on given host
     * @param <var>host</var> host
     * @throws IllegalArgumentException if host is null
     * @throws SipParseException if host is not accepted by implementation
     */
    public SipURL createSipURL(String host)
    throws IllegalArgumentException,SipParseException {
        if ( host==null)
            throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: host is null");
        SipURLImpl sipUrlImpl=new SipURLImpl();
        sipUrlImpl.setHost(host);
        
        return sipUrlImpl;
    }
    
    /**
     * Creates a SipURL based on given user and host
     * @param <var>user</var> user
     * @param <var>host</var> host
     * @throws IllegalArgumentException if user or host is null
     * @throws SipParseException if user or host is not accepted
     * by implementation
     */
    public SipURL createSipURL(String user, InetAddress host)
    throws IllegalArgumentException,SipParseException {
	   if (LogWriter.needsLogging()) 
	       LogWriter.logMessage("createSipURL () : " +  
			"user = " + user + " host = " + host);
           if ( user==null)
            throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: user is null");
           if ( host==null)
            throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: host is null");
           SipURLImpl sipUrlImpl=new SipURLImpl();
           sipUrlImpl.setHost(host);
           sipUrlImpl.setUserName(user);
        
           return sipUrlImpl;
    }
    
    /**
     * Creates a SipURL based on given user and host
     * @param <var>user</var> user
     * @param <var>host</var> host
     * @throws IllegalArgumentException if user or host is null
     * @throws SipParseException if user or host is not accepted 
     * by implementation
     */
    public SipURL createSipURL(String user, String host)
    throws IllegalArgumentException,SipParseException {
	   if (LogWriter.needsLogging()) 
	       LogWriter.logMessage("createSipURL () : " +  
			"user = " + user +  " host = " + host);
           if ( user==null)
            throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: user is null");
           if ( host==null)
            throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: host is null");
           SipURLImpl sipUrlImpl=new SipURLImpl();
           sipUrlImpl.setHost(host);
           sipUrlImpl.setUserName(user);
        
           return sipUrlImpl;
    }
    
    /**
     * Creates a NameAddress based on given address
     * @param <var>address</var> address URI
     * @throws IllegalArgumentException if address is null or not from same
     * JAIN SIP implementation
     */
    public NameAddress createNameAddress(jain.protocol.ip.sip.address.URI address)
    throws IllegalArgumentException {
         if ( address==null)
            throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: address is null");
         NameAddressImpl nameAddressImpl=new NameAddressImpl();
         nameAddressImpl.setAddress(address);
         
         return nameAddressImpl;
    }
    
    /**
     * Creates a NameAddress based on given diaplay name and address
     * @param <var>displayName</var> display name
     * @param <var>address</var> address URI
     * @throws IllegalArgumentException if displayName or address is null, or
     * address is not from same JAIN SIP implementation
     * @throws SipParseException if displayName is not accepted
     * by implementation
     */
    public NameAddress createNameAddress(String displayName, jain.protocol.ip.sip.address.URI address)
    throws IllegalArgumentException,SipParseException {
         if ( address==null)
            throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: address is null");
         if ( displayName==null)
            throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: displayName is null");
         NameAddressImpl nameAddressImpl=new NameAddressImpl();
         nameAddressImpl.setAddress(address);
         nameAddressImpl.setDisplayName(displayName);
         
         return nameAddressImpl;
    }
    
    /**
     * Creates a URI based on given scheme and data
     * @param <var>scheme</var> scheme
     * @param <var>schemeData</var> scheme data
     * @throws IllegalArgumentException if scheme or schemeData are null
     * @throws SipParseException if scheme or schemeData is not accepted by 
     * implementation
     */
    public jain.protocol.ip.sip.address.URI createURI(String scheme, String schemeData)
    throws IllegalArgumentException,SipParseException {
         if ( scheme==null)
            throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: scheme is null");
         if ( schemeData==null)
            throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: schemeData is null");
         URIImpl uriImpl=new URIImpl();
         uriImpl.setScheme(scheme);
         uriImpl.setSchemeData(schemeData);
         
         return uriImpl;
    }

}
