/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;
import gov.nist.sip.*;

/**
* User information part of a URL
* @see AuthorityServer
*/
public final class UserInfo  extends NetObject {
    
        /** user field
         */    
	protected String  user;

        /** password field
         */        
        protected String  password;
        
	/** userType field
         */        
        protected int     userType;
	
        /** Constant field
         */        
        public final  static  int TELEPHONE_SUBSCRIBER = 1 ;
        
        /** constant field
         */        
	public final  static  int USER = 2; 

        /** Default constructor
         */        
        public UserInfo() { 
            super();
        }
              
        /**
         * Compare for equality.
         * @param obj Object to set
         * @return true if the two headers are equals, false otherwise.
         */
        public boolean equals(Object obj) {
            if (! getClass().getName().equals(obj.getClass().getName())) {
                return false;
            }
            UserInfo other = (UserInfo) obj;
            if (this.userType != other.userType) {
                return false;
            }
            if (! this.user.equals(other.user)) {
                return false;
            }
            if (this.password != null &&
                other.password == null)  return false;
            
            if (other.password != null && this.password == null) return false;
            
	    if (this.password == other.password ) return true;

            return (this.password.equals(other.password));
        }
        
        /**
         * Encode the user information as a string.
         * @return String
         */
	public String encode() {
		if (password != null) return user + COLON + password;
		else return user;
	}
        
        /**
         * Gets the user type (which can be set to TELEPHONE_SUBSCRIBER or USER)
         * @return int
         */
	public int getUserType() {
		return userType;
	}

        /** get the user field.
         * @return String
         */        
	public	 String getUser() { 
            return user ;
        } 

        /** get the password field.
         * @return String
         */        
	public	 String getPassword() { 
            return password ;
        } 

	/**
         * Set the user member
         * @param u String to set
         */
	public	 void setUser(String u) { 
            user = u ;
        } 

	/**
         * Set the password member
         * @param p String to set
         */
	public	 void setPassword(String p) { 
            password = p ;
        }      
	
	/**
         * Set the user type (to TELEPHONE_SUBSCRIBER or USER).
         * @param type int to set
         * @throws IllegalArgumentException if type is not in range.
         */
	public void setUserType(int type) 
	throws IllegalArgumentException
	{
		if (type != TELEPHONE_SUBSCRIBER && type != USER ) {
		   throw new IllegalArgumentException
			("Parameter not in range");
		}
		userType = type;
	}
	
}
