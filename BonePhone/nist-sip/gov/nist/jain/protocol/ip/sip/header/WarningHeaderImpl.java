/***************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)     *
* See ../../../../../../../../doc/uncopyright.html for conditions of use   *
* Creator: O. Deruelle (deruelle@nist.gov)                                 *
* Modifications by : M. Ranganathan (mranga@nist.gov) 			   *
* Fixed up for jain-sip 1.0				                   *
* Questions/Comments: nist-sip-dev@antd.nist.gov                           *
****************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.net.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;
import gov.nist.log.*;

import java.net.*;
import java.util.Iterator;



/**
* Implementation of the WarningHeader interface of jain-sip.
*/
public final class WarningHeaderImpl extends HeaderImpl
implements WarningHeader , NistSIPHeaderMapping  {
   
    
    /** Default constructor
     */    
   public WarningHeaderImpl() { 
       super();
       this.headerName = name;
	
   }

    /** Constructor
     * @param warning Warning to set
     */   
   public WarningHeaderImpl(Warning warning) { 
       super(warning);
       this.headerName = name;
   }
   
    /**
    * Gets code of WarningHeader
    * @return code of WarningHeader
    */
    public int getCode() {
        Warning warning = (Warning) sipHeader;
        return warning.getWarnCode();
    }

    
    /**
    * Gets agent host of WarningHeader
    * @return agent host of WarningHeader
    */
    public String getHost() {
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG)) 
		LogWriter.logMessage("getPort ");
         Warning warning = (Warning) sipHeader;
         String host;
         try {
            host=warning.getHost(); 
	} catch (SIPParameterNotSetException e) { return null; }
        return host;
    }

    
    /**
    * Gets agent port of WarningHeader
    * (Returns negative int if port does not exist)
    * @return agent port of WarningHeader
    */
    public int getPort() {
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG)) 
		LogWriter.logMessage("getPort ");
         Warning warning = (Warning) sipHeader;
        int port;
        try {
            port=warning.getPort(); }
        catch (SIPParameterNotSetException e) 
            { return -1; }
        return port;
    }

    
    /**
    * Returns boolean value indicating if WarningHeader has port
    * @return boolean value indicating if WarningHeader has port
    */
    public boolean hasPort() {
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG)) 
		LogWriter.logMessage("hasPort ");
        Warning warning = (Warning) sipHeader;
        return warning.hasPort();
    }

    
    /**
    * Gets text of WarningHeader
    * @return text of WarningHeader
    */
    public String getText() {
       Warning warning = (Warning) sipHeader;
       return warning.getWarnText();
    }

    
    /**
     * Sets the agent of a warning header.
     * @param host String to set
     * @throws SipParseException if host has a bad specification
     * @throws IllegalArgumentException if host is null
     */
    public void setAgent(String host) 
        throws SipParseException, IllegalArgumentException {
        if (host == null) throw new IllegalArgumentException("null param!");
	
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
		LogWriter.logMessage(LogWriter.TRACE_DEBUG,
					"setAgent() " + host);
       
        Warning warning = (Warning)sipHeader;
        WarnAgent wa = new WarnAgent();
	HostPort hp = null;
        try {
            StringMsgParser smp = new StringMsgParser();
            hp = smp.parseHostPort(host);
        } catch (SIPParseException ex ) {
	    try {
               StringMsgParser smp = new StringMsgParser();
	       Host h = smp.parseHost(host);
	       hp = new HostPort();
	       hp.setHost(h);
           } catch (SIPParseException e) {
	       if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG)) 
		   LogWriter.logException(e);
               throw new SipParseException("bad host spec: " 
				+ e.getMessage());
	   }
	}
        wa.setHostPort(hp);
        warning.setWarnAgent(wa);
    }
    
    /**
     * Sets code of WarningHeader
     * @param code int to set
     * @throws SipParseException if code is not accepted by implementation
     */
    public void setCode(int code) throws SipParseException {
         Warning warning = (Warning) sipHeader;
         warning.setWarnCode(code);
    }

    
    /**
     * Sets agent host of WarningHeader
     * @param host String to set
     * @throws IllegalArgumentException if host is null
     * @throws SipParseException if host is not accepted by implementation
     */
    public void setHost(String host) throws IllegalArgumentException,
    SipParseException {
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG)) 
		LogWriter.logMessage("setHost " + host);
        Warning warning = (Warning) sipHeader;
        if (host==null) 
               throw new IllegalArgumentException
               ("JAIN-SIP EXCEPTION: host is null ");
        else      warning.setHost(host); 
    }

        
    /**
     * Sets agent port of WarningHeader
     * @param port int to set
     * @throws SipParseException if agentPort is not accepted by implementation
     */
    public void setPort(int port) throws SipParseException {
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG)) 
		LogWriter.logMessage("setPort " + port);
        Warning warning = (Warning) sipHeader;
        warning.setPort(port);
    }

    
    /**
    * Removes port from WarningHeader (if it exists)
    */
    public void removePort() {
        Warning warning = (Warning) sipHeader;
        try {
           warning.removePort();}
        catch (SIPParameterNotSetException e) {  }  
    }

    
    /**
     * Sets text of WarningHeader
     * @param text String to set
     * @throws IllegalArgumentException if text is null
     * @throws SipParseException if text is not accepted by implementation
     */
    public void setText(String text) throws IllegalArgumentException, 
    SipParseException {
            Warning warning = (Warning) sipHeader;
            if (text==null)
                throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION: text is null");
            else warning.setWarnText(text);
    }
    
     /**
     * Gets agent of WarningHeader
     * @return agent of WarningHeader
     */
    public String getAgent() {
	if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG)) 
		LogWriter.logMessage("getAgent()");
        Warning warning = (Warning) sipHeader;
        WarnAgent warnAgent=warning.getWarnAgent();
        if ( warnAgent==null) return null;
        else return warnAgent.encode();
    }
    
    
            
    
}
