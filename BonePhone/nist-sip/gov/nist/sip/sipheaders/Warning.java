/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modifications by: O. Deruelle (deruelle@antd.nist.gov added JAVADOC)         *  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import gov.nist.sip.net.*;

/**
* the WarningValue SIPObject. 
*
*<pre>
* Warning        =  "Warning" ":" 1#warning-value
*        warning-value  =  warn-code SP warn-agent SP warn-text
*        warn-code      =  3DIGIT
*        warn-agent     =  ( host [ ":" port ] ) | pseudonym
*                          ;  the name or pseudonym of the server adding
*                          ;  the Warning header, for use in debugging
*        warn-text      =  quoted-string
*        pseudonym      =  token        
*</pre>
*
* @see WarningList SIPHeader which strings these together.
*/
public class Warning extends SIPHeader {

        /** warn code field, the warn code consists of three digits.
         */
    protected int warnCode;
    
        /** the name or pseudonym of the server adding
         * the Warning header, for use in debugging
         */
    protected WarnAgent warnAgent;
    
        /** warn-text field
         */
    protected String warnText;
    
        /**
         * constructor.
         */
    public Warning() {
        super(WARNING);
    }
    
        /**
         * Encode into canonical form.
         * @return the String encoded canonical version of the header
         */
    public String encode() {
        return headerName + COLON + SP + warnAgent.encode() + SP +
        warnText + NEWLINE;
    }
    
        /** get the warnCode field
         * @return WarnCode field of this Warning header.
         */
    public int getWarnCode() { 
        return warnCode;
    }
    
        /** get the warnAgent field
         * @return WarnAgent field of this Warning header.
         */
    public WarnAgent getWarnAgent() {
        return warnAgent;
    }
    
        /** get the warnText field
         * @return WarnText field of this Warning header.
         */
    public String getWarnText() {
        return warnText;
    }
    
        /**
         * Get the port.
         * @return int field of this Warning header.
         * @throws SIPParameterNotSetException if port does not exist
         */
    public int getPort()
    throws SIPParameterNotSetException {
        if ( warnAgent==null) 
            throw new SIPParameterNotSetException("port does not exist");
        return warnAgent.getPort();  
    }
        
        /**
         * Get the host.
         * @return Host field of this Warning header.
         * @throws SIPParameterNotSetException if Host does not exist
         */
    public String getHost() throws SIPParameterNotSetException {
          if ( warnAgent==null) 
               throw new SIPParameterNotSetException("port does not exist");
          else {
                Host host=warnAgent.getHost();
                if (host==null) 
                    throw new SIPParameterNotSetException("port does not exist");
                return host.encode();
          }
    }
    
         /** port of the warn Agent
         * @return true if the port has been set (false otherwise).
         */
    public boolean hasPort() {
        if (warnAgent==null) return false;
        if (! warnAgent.isHostPort()) return false;
        else {
            HostPort hostPort=warnAgent.getHostPort();
            if (hostPort==null) return false;
            else return hostPort.hasPort(); 
        }
    }
    
        /**
         * Remove the port setting.
         *@exception SIPParameterNotSetException if the port has not been
         * set.
         */
    public void removePort()
    throws SIPParameterNotSetException {
        if (warnAgent!=null) 
            if (! warnAgent.isHostPort())
                throw new SIPParameterNotSetException("Port not Set!");
            else {
                 HostPort hostPort= warnAgent.getHostPort();
                 if (hostPort!=null)
                         hostPort.removePort();
            }
    }
    
        /**
         * Set the warnCode member
         * @param w int to set
         */
    public void setWarnCode(int w) { 
        warnCode = w ;
    }
    
        /**
         * Set the warnAgent member
         * @param w WarnAgent to set
         */
    public	 void setWarnAgent(WarnAgent w) { 
        warnAgent = w ;
    }
    
        /**
         * Set the warnText member
         * @param w String to set
         */
    public	 void setWarnText(String w) {
        warnText = w ;
    }
    
        /**
         * set the port if the warning is of type hostport.
         * @param port int to set
         * @throws IllegalArgumentException if port is negative
         */
    public void setPort(int port)
    throws IllegalArgumentException {
        if (port < 0) throw new IllegalArgumentException("Bad port!");
        if (warnAgent==null) {
            warnAgent=new WarnAgent();
        }
        if (warnAgent.isHostPort()) {
            warnAgent.setPort(port);
        }  
    }
    
        /**
         * Set the host.
         * @param host String to set
         */
    public void setHost(String host) {
        Host h=new Host(host,HostAddrTypes.HOSTNAME);
        if (warnAgent==null) warnAgent=new WarnAgent();
        warnAgent.setHost(h);
    }
    
}
