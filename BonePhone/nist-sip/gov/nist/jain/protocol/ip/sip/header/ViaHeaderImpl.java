/***************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)     * 
* See ../../../../../../../../doc/uncopyright.html for conditions of use   *
* Creator: Deruelle Olivier (deruelle@nist.gov)                            *
* Questions/Comments: nist-sip-dev@antd.nist.gov                           *
*******************************************************************************/

package gov.nist.jain.protocol.ip.sip.header;


import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import java.util.Iterator;
import java.net.*;
import gov.nist.sip.msgparser.*;
import gov.nist.log.*;

/**
* Implementation of the ViaHeader interface of jain-sip.
*/
public final class ViaHeaderImpl extends HeaderImpl
implements ViaHeader , NistSIPHeaderMapping {
  
    /** constant field
     */    
    public static final int TTL_MIN = 1;
    
    /** constant field
     */    
    public static final int TTL_MAX = 255;

    /** Default constructor
     */    
    public ViaHeaderImpl() {
        super();
        this.headerName = name;
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"ViaHeaderImpl() ");
    }

    /** constructor
     * @param via gov.nist.sip.Via to set
     */    
    public ViaHeaderImpl( Via via) { 
        super(via);
        this.headerName = name;
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"ViaHeaderImpl(via) ");
    }
    
    /**
    * Returns boolean value indicating if ViaHeader is hidden
    * @return boolean value indicating if ViaHeader is hidden
    */
    public boolean isHidden() {
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"isHidden()");
        Via via=(Via)sipHeader;
        return via.isHidden();
    }

    /**
     * Sets whether ViaHeader is hidden or not
     * @param hidden boolean to set
     */
    public void setHidden(boolean hidden) {
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setHidden() " + hidden);
        Via via=(Via)sipHeader;
        if (hidden) {
            if ( ! this.hasParameter(Via.HIDDEN) ) 
                       via.setParameter(Via.HIDDEN,"hidden");
        }
        else via.removeParameter(Via.HIDDEN);          
    }

    
    /**
    * Returns boolean value indicating if ViaHeader has port
    * @return boolean value indicating if ViaHeader has port
    */
    public boolean hasPort() {
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"hasPort()");
        Via via=(Via)sipHeader;
        return via.hasPort();
    }

    
    /**
    * Gets port of ViaHeader
    * @return port of ViaHeader
    */
    public int getPort() {
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"getPort()");
         Via via=(Via)sipHeader;
         return  via.getPort();
    }

    
    /**
     * Sets port of ViaHeader
     * @param port int to set
     * @throws SipParseException if port is not accepted by implementation
     */
    public void setPort(int port) throws SipParseException {
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setPort()" + port);
        Via via=(Via)sipHeader;
        
        if (port <= 0)
                throw new SipParseException
                ("port is not accepted by implementation");
        via.setPort(port);
    }

    
    /**
    * Removes port from ViaHeader (if it exists)
    */
    public void removePort()    {
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"removePort()");
        Via via=(Via)sipHeader;
        via.removePort();
    }

    
    /**
    * Gets protocol version of ViaHeader
    * @return protocol version of ViaHeader
    */
    public String getProtocolVersion() {
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"getProtocolVersion()");
        Via via=(Via)sipHeader;
        return via.getProtocolVersion();
    }

    
    /**
     * Sets protocol version of ViaHeader
     * @param protocolVersion String to set
     * @throws IllegalArgumentException if protocolVersion is null
     * @throws SipParseException if protocolVersion is not accepted by 
     * implementation
     */
    public void setProtocolVersion(String protocolVersion) 
    throws IllegalArgumentException, SipParseException {
        Via via=(Via)sipHeader;
        
        if ( protocolVersion==null )
                throw new IllegalArgumentException
                ("JAIN-EXCEPTION: argument is not accepted by implementation");
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setProtocolVersion() " 
			+ protocolVersion);
        via.setProtocolVersion(protocolVersion);
    }

    
    /**
    * Gets transport of ViaHeader
    * @return transport of ViaHeader
    */
    public String getTransport() {
	 if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
	     LogWriter.logMessage(LogWriter.TRACE_DEBUG,"getTransport()");
         Via via=(Via)sipHeader;
         return via.getTransport();
    }

    
    /**
     * Sets transport of ViaHeader
     * @param transport String to set
     * @throws IllegalArgumentException if transport is null
     * @throws SipParseException if transport is not accepted by
     * implementation
     */
    public void setTransport(String transport) throws IllegalArgumentException, 
    SipParseException {
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
	    LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setTransport() " 
					+ transport);
         Via via=(Via)sipHeader;
         if ( transport==null )
                throw new IllegalArgumentException
                ("JAIN-EXCEPTION: transport argument is null");
	 else if (  transport.compareToIgnoreCase(TCP) != 0 
		 && transport.compareToIgnoreCase(UDP) != 0 )
	   throw new SipParseException("Bad transport string");
         else via.setTransport(transport.toUpperCase());
    }

    
    /**
    * Gets host of ViaHeader
    * @return host of ViaHeader
    */
    public String getHost() {
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"getHost()");
        Via via=(Via)sipHeader;
        return via.getHost();
    }

    
    /**
     * Sets host of ViaHeader
     * @param host String to set
     * @throws IllegalArgumentException if host is null
     * @throws SipParseException if host is not accepted by implementation
     */
    public void setHost(String host) throws IllegalArgumentException,
    SipParseException {
      
	  LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setHost() " + host);
          Via via=(Via)sipHeader;
          if ( host==null )
                throw new IllegalArgumentException
                ("JAIN-EXCEPTION: host argument is null");
          else via.setHost(host); 
    }

    
    /**
     * Sets host of ViaHeader
     * @param host InetAddress to set
     * @throws IllegalArgumentException if host is null
     * @throws SipParseException if host is not accepted by implementation
     */
    public void setHost(InetAddress host) throws IllegalArgumentException,
    SipParseException{
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setHost() " + host);
        Via via=(Via)sipHeader;
      
        if (host==null)
            throw new IllegalArgumentException
            ("JAIN-SIP EXCEPTION: host is null");
        else {
             String hname = host.getHostAddress();
             gov.nist.sip.net.Host h = new gov.nist.sip.net.Host();
             h.setHostAddress(hname);
             via.setHost(h);
        }
    }

    
    /**
    * Gets comment of ViaHeader
    * @return comment of ViaHeader
    */
    public String getComment() {
	 LogWriter.logMessage(LogWriter.TRACE_DEBUG,"getComment() " );
         Via via=(Via)sipHeader;
         return via.getComment();
    }

    
    /**
    * Gets boolean value to indicate if ViaHeader
    * has comment
    * @return boolean value to indicate if ViaHeader
    * has comment
    */
    public boolean hasComment() {
	 LogWriter.logMessage(LogWriter.TRACE_DEBUG,"hasComment()" );
        Via via=(Via)sipHeader;
        return via.hasComment();
    }

    
    /**
     * Sets comment of ViaHeader
     * @param comment String to set
     * @throws IllegalArgumentException if comment is null
     * @throws SipParseException if comment is not accepted by implementation
     */
    public void setComment(String comment) throws IllegalArgumentException,
    SipParseException  {
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setComment() " + comment );
        Via via=(Via)sipHeader;
      
        if (comment==null) 
                throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null comment" );
        else if (comment.length() == 0) 
                throw new IllegalArgumentException
                ("JAIN-EXCEPTION: 0 length comment" );
        else    via.setComment(comment);
    }

    
    /**
    * Removes comment from ViaHeader (if it exists)
    */
    public void removeComment() {  
	  LogWriter.logMessage(LogWriter.TRACE_DEBUG,"removeComment() " );
          Via via=(Via)sipHeader;
          via.removeComment();
    }

    
    /**
     * Sets TTL of ViaHeader
     * @param <var>ttl</var> TTL
     * @throws SipParseException if ttl is not accepted by implementation
     */
    public void setTTL(int ttl)   throws SipParseException{
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setTTL() " + ttl );
        Via via=(Via)sipHeader;
       
        if ( ( TTL_MIN <= ttl ) && (ttl <= TTL_MAX) ) via.setTTL(ttl);
        else throw new IllegalArgumentException
         ("JAIN-EXCEPTION: ttl is not between TTL_MIN and TTL_MAX inclusive"); 
    }

    
    /**
    * Removes TTL from ViaHeader (if it exists)
    */
    public void removeTTL() {
	LogWriter.logMessage(LogWriter.TRACE_DEBUG,"removeTTL() ");
        Via via=(Via)sipHeader;
        via.removeParameter(Via.TTL);
    }

    
    /**
    * Gets TTL of ViaHeader
    * @return TTL of ViaHeader
    */
    public int getTTL() {
       LogWriter.logMessage(LogWriter.TRACE_DEBUG,"getTTL() ");
       Via via=(Via)sipHeader;
       return via.getTTL();
    }

    
    /**
    * Gets boolean value to indicate if ViaHeader
    * has TTL
    * @return boolean value to indicate if ViaHeader
    * has TTL
    */
    public boolean hasTTL() {
        LogWriter.logMessage(LogWriter.TRACE_DEBUG,"hasTTL() ");
        Via via=(Via)sipHeader;
        return via.hasParameter(Via.TTL);
    }

    
    /**
     * Sets MAddr of ViaHeader
     * @param mAddr String to set
     * @throws IllegalArgumentException if mAddr is null
     * @throws SipParseException if mAddr is not accepted by implementation
     */
    public void setMAddr(String mAddr) throws IllegalArgumentException, 
    SipParseException {
        LogWriter.logMessage(LogWriter.TRACE_DEBUG,"setMaddr () " + mAddr);
        Via via=(Via)sipHeader;
       
        if (mAddr==null) 
                throw new IllegalArgumentException
                ("JAIN-EXCEPTION: mAddr is null");
        else    via.setMAddr(mAddr);
    }

    
    /**
     * Sets MAddr of ViaHeader
     * @param mAddr InetAddress to set
     * @throws IllegalArgumentException if mAddr is null
     * @throws SipParseException if mAddr is not accepted by implementation
     */
    public void setMAddr(InetAddress mAddr) throws IllegalArgumentException,
    SipParseException {
         LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"setMaddr () " + mAddr);
         Via via=(Via)sipHeader;
        
         if (mAddr==null) 
                throw new IllegalArgumentException
                ("JAIN-EXCEPTION: mAddr is null");
         else  {  
             // if (mAddr.isMulticastAddress() ) {
                 String hostAddress=mAddr.getHostAddress();
                 if ( hostAddress==null) 
                      throw new IllegalArgumentException
                               ("JAIN-EXCEPTION: host address is null");
                 else via.setMAddr(hostAddress); 
             // } else 
             //   throw new SipParseException
             //        ("JAIN-EXCEPTION: mAddr is not a multicast address");
         }
    }

    /**
    * Gets boolean value to indicate if ViaHeader
    * has MAddr
    * @return boolean value to indicate if ViaHeader
    * has MAddr
    */
    public boolean hasMAddr() {
         LogWriter.logMessage(LogWriter.TRACE_DEBUG, "hasMaddr () ");
         Via via=(Via)sipHeader;
         
         return via.hasParameter(Via.MADDR);
    }

    
    /**
    * Removes MAddr from ViaHeader (if it exists)
    */
    public void removeMAddr() {
        LogWriter.logMessage(LogWriter.TRACE_DEBUG, "removeMaddr () ");
         Via via=(Via)sipHeader;
        
         via.removeParameter(Via.MADDR);
    }

    
    /**
    * Gets MAddr of ViaHeader
    * @return MAddr of ViaHeader
    */
    public String getMAddr() {
        LogWriter.logMessage(LogWriter.TRACE_DEBUG, "getMaddr () ");
        Via via=(Via)sipHeader;
       
        Host host=via.getMaddr();
        if ( host == null) return null;
        else return host.getIpAddress(); 
    }

    
    /**
     * Sets received of ViaHeader
     * @param received String to set
     * @throws IllegalArgumentException if received is null
     * @throws SipParseException if received is not accepted by the implementation
     */
    public void setReceived(String received) throws IllegalArgumentException,
    SipParseException {
	  if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
            LogWriter.logMessage(LogWriter.TRACE_DEBUG, 
			"setReceived() " + received);
          Via via=(Via)sipHeader;
         
          if (received==null) 
                throw new IllegalArgumentException
                ("JAIN-EXCEPTION: received is null");
          else   
                 via.setReceived(received);
    }

    /**
     * Sets received of ViaHeader
     * @param received InetAddress to set
     * @throws IllegalArgumentException if received is null
     * @throws SipParseException if received is not accepted by implementation
     */
    public void setReceived(InetAddress received)
    throws IllegalArgumentException, SipParseException {
	 if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
            LogWriter.logMessage(LogWriter.TRACE_DEBUG, 
			"setReceived() " + received);
         Via via=(Via)sipHeader;
         if (received==null) 
                throw new IllegalArgumentException
                ("JAIN-EXCEPTION: received is null");
         else  {  
                 String hostName=received.getHostName();
                 if (hostName==null) 
                         throw new IllegalArgumentException
                            ("JAIN-EXCEPTION: hostName is null");
                 else via.setReceived(hostName); 
              }
    }

    
    /**
    * Removes received from ViaHeader (if it exists)
    */
    public void removeReceived() {
	 if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
            LogWriter.logMessage(LogWriter.TRACE_DEBUG, "removeReceived()" );
         Via via=(Via)sipHeader;
         via.removeParameter(Via.RECEIVED);
    }

    
    /**
    * Gets received of ViaHeader
    * @return received of ViaHeader
    */
    public String getReceived() {
	 if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
            LogWriter.logMessage(LogWriter.TRACE_DEBUG, "getReceived()" );
         Via via=(Via)sipHeader;
         return via.getReceived();
    }

    
    /**
    * Gets boolean value to indicate if ViaHeader
    * has received
    * @return boolean value to indicate if ViaHeader
    * has received
    */
    public boolean hasReceived() {
	 if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
            LogWriter.logMessage(LogWriter.TRACE_DEBUG, "hasReceived()" );
        Via via=(Via)sipHeader;
        return via.hasParameter(Via.RECEIVED);
    }

    
    /**
     * Sets branch of ViaHeader
     * @param branch String to set
     * @throws IllegalArgumentException if branch is null
     * @throws SipParseException if branch is not accepted by implementation
     */
    public void setBranch(String branch) throws IllegalArgumentException, 
    SipParseException{
         if (branch==null) 
                throw new IllegalArgumentException
                ("JAIN-EXCEPTION: branch is null");
         if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
            LogWriter.logMessage(LogWriter.TRACE_DEBUG, "setBranch()" +branch);

         Via via=(Via)sipHeader;
         via.setBranch(branch);
    }

    
    /**
    * Removes branch from ViaHeader (if it exists)
    */
    public void removeBranch() {
         if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
            LogWriter.logMessage(LogWriter.TRACE_DEBUG, "removeBranch()");
         Via via=(Via)sipHeader;
        
         via.removeParameter(Via.BRANCH);
    }

    
    /**
    * Gets branch of ViaHeader
    * @return branch of ViaHeader
    */
    public String getBranch() {
         if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
            LogWriter.logMessage(LogWriter.TRACE_DEBUG, "getBranch()");
         Via via=(Via)sipHeader;
         return via.getBranch();
    }

    
    /**
    * Gets boolean value to indicate if ViaHeader
    * has branch
    * @return boolean value to indicate if ViaHeader
    * has branch
    */
    public boolean hasBranch() {
	 if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
            LogWriter.logMessage(LogWriter.TRACE_DEBUG, "hasBranch() ");
         Via via=(Via)sipHeader;
        
         return via.hasParameter(Via.BRANCH); 
    }

        
    /**
    * Gets Iterator of parameter names
    * (Note - objects returned by Iterator are Strings)
    * (Returns null if no parameters exist)
    * @return Iterator of parameter names
    */
    public Iterator getParameters() {
         LogWriter.logMessage(LogWriter.TRACE_DEBUG, "hasParameters() ");
         Via via=(Via)sipHeader;
        
         Iterator iterator=   via.getParameters(); 
         if ( iterator==null) return null;
         if ( iterator.hasNext() ) return iterator;
         else return null;  
    }
    
            
    /**
     * Gets the value of specified parameter
     * (Note - zero-length String indicates flag parameter)
     * (Returns null if parameter does not exist)
     * @return the value of specified parameter
     * @param name String to set
     * @throws IllegalArgumentException if name is null
     */
    public String getParameter(String name) throws IllegalArgumentException {
	  if ( LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
               LogWriter.logMessage(LogWriter.TRACE_DEBUG, "getParameter() " 
		+ name);
            Via via=(Via)sipHeader;
           
            if (name == null) throw new
                IllegalArgumentException("JAIN-EXCEPTION: null argument");
            return via.getParameter(name);
    }
    
        
    /**
     * Sets value of parameter
     * (Note - zero-length value String indicates flag parameter)
     * @param name String to set
     * @param value String to set
     * @throws IllegalArgumentException if name or value is null
     * @throws SipParseException if name or value is not accepted 
     * by implementation
     */
    public void setParameter(String name,String value) throws 
        IllegalArgumentException, SipParseException {
	  if ( LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
               LogWriter.logMessage(LogWriter.TRACE_DEBUG, "hasParameter() " 
			+ name + " value " + value);
           Via via=(Via)sipHeader;
          
           if (name == null || value == null) {
                throw new 
                    IllegalArgumentException
                    ("JAIN-EXCEPTION: Name or value is null!");
                }
            else    via.setParameter(name,value);
     }
    
        
     /**
     * Gets boolean value to indicate if Parameters
     * has any parameters
     * @return boolean value to indicate if Parameters
     * has any parameters
     */
     public boolean hasParameters() {
          LogWriter.logMessage(LogWriter.TRACE_DEBUG, "hasParameters()");
          Via via=(Via)sipHeader;
          
            return via.hasParameters();
     }
    
    
     /**
      * Gets boolean value to indicate if Parameters
      * has specified parameter
      * @return boolean value to indicate if Parameters
      * has specified parameter
      * @param name String to set
      * @throws IllegalArgumentException if name is null
      */
     public boolean hasParameter(String name) throws IllegalArgumentException {
          LogWriter.logMessage(LogWriter.TRACE_DEBUG, "hasParameter() ");
          Via via=(Via)sipHeader;
         
            if(name == null) throw new IllegalArgumentException
                ("JAIN-EXCEPTION: null parameter");
            return via.hasParameter(name);
     }
    
            
     /**
      * Removes specified parameter from Parameters (if it exists)
      * @param name String to set
      * @throws IllegalArgumentException if parameter is null
      */
     public void removeParameter(String name) throws IllegalArgumentException {
          LogWriter.logMessage(LogWriter.TRACE_DEBUG, "removeParameter() ");
          Via via=(Via)sipHeader;
       
          if( name==null )
                throw new IllegalArgumentException
                                        ("JAIN-EXCEPTION: parameter is null");
          else  via.removeParameter(name); 
     }
    
        
     /**
     * Removes all parameters from Parameters (if any exist)
     */
     public void removeParameters() {
          LogWriter.logMessage(LogWriter.TRACE_DEBUG, "removeParameters() ");
          Via via=(Via)sipHeader;
        
          via.removeParameters();
     }

     
}
    
