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
 *  A tagging class
 */
public  class WarnAgent extends SIPObject {
    
    private static final int WARN_AGENT_HOST_PORT = 1;
    private static final int WARN_AGENT_PSEUDONYM = 2;
    
        /** type of the WarnAgent.
         */
    protected int type;
    
    
        /** Hostport of the WarnAgent.
         */
    protected HostPort hostPort;
    
        /** the pseudonym of the server adding the Warning Header.
         */
    protected String pseudonym;
    
        /**
         * Default constructor.
         */
    public WarnAgent() {
        type = WARN_AGENT_HOST_PORT;
    }
    
        /**
         * Constructor given the warn agent as host:port value.
         * @param hp HostPort to set
         */
    public WarnAgent( HostPort hp ) {
        hostPort = hp;
        type = WARN_AGENT_HOST_PORT;
    }
    
        /**
         * Constructor given the warn agent as pseudonym value.
         * @param p String to set
         */
    public WarnAgent( String p ) {
        pseudonym = p;
        type = WARN_AGENT_PSEUDONYM;
    }
    
        /**
         * Encode into canonical form.
         * @return canonical string.
         */
    public String encode() {
        if (type == WARN_AGENT_HOST_PORT) {
            if (hostPort != null) return hostPort.encode();
            else return "";
        } else {
            return pseudonym;
        }
    }
    
         /** Hostport of the warn Agent
          * @return true if the type has been set (false otherwise).
          */
    public boolean isHostPort() {
        return type == WARN_AGENT_HOST_PORT;
    }
    
        /** get the Hostport field
         * @return Hostport field of this WarnAgent.
         */
    public HostPort getHostPort() {
        return hostPort;
    }
    
         /** get the Pseudonym field
          * @return Pseudonym field of this WarnAgent.
          */
    public String getPseudonym() {
        return pseudonym;
    }
    
        /**
         * get the port.
         * @return port field of this WarnAgent. 
         * @throws SIPParameterNotSetException if port does not exist
         */
    public int getPort() throws SIPParameterNotSetException {
        if (!hostPort.hasPort()) {
            throw new SIPParameterNotSetException("Port not set!");
        }
        return hostPort.getPort();
    }
    
        /**
         * Get the host.
         * @return host field of this WarnAgent. 
         * @throws SIPParameterNotSetException if Host does not exist
         */
    public Host getHost() throws SIPParameterNotSetException {
        if (type != WARN_AGENT_HOST_PORT) {
            throw new SIPParameterNotSetException("Host value not set");
        }
        return hostPort.getHost();
    }
    
        /**
         * Set the pseudonym member
         * @param p String to set
         */
    public void setPseudonym(String p) {
        pseudonym = p ;
        type = WARN_AGENT_PSEUDONYM;
    }
    
        /**
         * Set the hostPort member
         *@param h Hostport to set
         */
    public void setHostPort(HostPort h) {
        hostPort = h ;
        type = WARN_AGENT_HOST_PORT;
    }
    
        /**
         * Set the port.
         *@param p int to set
         */
    public void setPort( int p ) {
	if (hostPort == null) hostPort = new HostPort();
        hostPort.setPort(p);
        type = WARN_AGENT_HOST_PORT;
    }
    
        /**
         * Set the host.
         *@param h Host to set
         */
    public void setHost(Host h) {
	if (hostPort == null) hostPort = new HostPort();
        hostPort.setHost(h);
        type = WARN_AGENT_HOST_PORT;
    }
    
}
