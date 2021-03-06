/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;
import gov.nist.sip.*;

/**
* Authority part of a URI structure. Section 3.2.2 RFC2396
*/
public class AuthorityServer  extends Authority {
    
        /** hostport field
         */    
	protected HostPort hostPort;

        /** userInfo field
         */        
        protected UserInfo userInfo;
         	
        /**
         * Return the host name in encoded form.
         * @return String
         */        
	public String encode() {	
		if (userInfo != null) {
	       	  return userInfo.encode() + Separators.AT +
		        hostPort.encode();
		} else {
			return hostPort.encode();
		}
	}
        
        /** retruns true if the two Objects are equals , false otherwise.
         * @param other Object to set
         * @return boolean
         */        
        public boolean equals(Object other) {
            if (!other.getClass().getName().equals(this.getClass().getName())){
                return false;
            }
            AuthorityServer otherAuth = (AuthorityServer) other;
            if (! this.hostPort.equals(otherAuth.hostPort) ) {
                return false;
            }
	    if (this.userInfo != null && otherAuth.userInfo != null) {
		if (! this.userInfo.equals(otherAuth.userInfo)) {
		    return false;
		}
	    }
            return true;
        }
        
        /**
         * get the hostPort member.
         * @return HostPort
         */
	public	 HostPort getHostPort() { 
            return hostPort ;
        } 
                
        /**
         * get the userInfo memnber.
         * @return UserInfo
         */
	public  UserInfo getUserInfo() { 
            return userInfo;
        }
        
	/**
         * Get password from the user info.
         * @return String
         */
	public String  getPassword() { 
	    if (userInfo == null) return null;
            else return userInfo.password ;
        }
               
        /**
         * Get the user name if it exists.
         * @return String user or null if not set.
         */
        public String  getUser() { 
            return userInfo != null ?  userInfo.user : null;
        }
        
         /**
          * Get the host name.
          * @return Host (null if not set)
          */
         public Host getHost() { 
	    if (hostPort == null) return null;
            else return hostPort.host;
         }                      
                           
          /**
           * Get the port.
           * @return int port (-1) if port is not set.
           */
	public int getPort() { 
	    if (hostPort == null) return -1;
            else return hostPort.getPort();
        }
              
        /** remove the port.
         */        
        public void removePort() {
	    if (hostPort != null) hostPort.removePort();
        }
        
        /**
         * set the password.
         * @param passwd String to set
         */
        public void setPassword(String passwd) {
	    if (userInfo == null) userInfo = new UserInfo();
            userInfo.setPassword(passwd);
        }
         
         /**
          * Set the user name of the userInfo member.
          * @param user String to set
          */
        public void setUser( String user) {
	    if (userInfo == null) userInfo = new UserInfo();
            this.userInfo.setUser(user);
        }

          /**
           * set the host.
           * @param h Host to set
           */
         public void setHost( Host h) { 
	     if (hostPort == null) hostPort = new HostPort();
             hostPort.host = h;
         }
        
           /**
            * Set the port.
            * @param port int to set
            */
        public void setPort (int port) {
	    if (hostPort == null) hostPort = new HostPort();
            hostPort.setPort(port);
        }
                    
	/**
         * Set the hostPort member
         * @param h HostPort to set
         */
	public	 void setHostPort(HostPort h) { 
            hostPort = h ;
        }
        
	/**
         * Set the userInfo member
         * @param u UserInfo to set
         */
	public	 void setUserInfo(UserInfo u) { 
            userInfo = u ;
        } 
	
}
