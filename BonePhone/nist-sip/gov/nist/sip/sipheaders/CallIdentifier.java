/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  Olivier Deruelle, added JAVADOC                                *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

/**
* The call identifer that goes into a callID header and a in-reply-to header.
* @see CallID
* @see InReplyTo
*/
public final class CallIdentifier extends SIPObject {
    
        /** localId field
         */    
        protected String localId;
	
        /** host field
         */        
        protected String   host;
        
        /** Default constructor
         */        
        public CallIdentifier() {}
        
        /** Constructor
         *@param local id is the local id.
         *@param host is the host.
         */
        public CallIdentifier(String localId, String host) {
            this.localId = localId;
            this.host = host;
        }

        /** constructor
         * @param cid String to set
         * @throws IllegalArgumentException if cid is null or is not a token, 
         * or token@token
         */        
	public CallIdentifier(String cid) throws IllegalArgumentException {
		setCallID(cid);
	}        
        
	/**
         * Get the encoded version of this id.
         * @return String to set
         */
	public String encode() {
		if (host != null) {
			return localId + AT + host;
		} else {
			return localId;
		}
	}
        
        /**
         * Compare two call identifiers for equality.
         * @param other Object to set
         * @return true if the two call identifiers are equals, false
         * otherwise
         */        
        public boolean equals( Object other) {
            if (! other.getClass().equals(this.getClass())) {
                return false;
            }   
            CallIdentifier that = (CallIdentifier) other;
            if (this.localId.compareTo(that.localId) != 0) {
                return false;
            }
            if (this.host == that.host) return true;
            if ( (this.host == null && that.host != null) ||
                 (this.host != null && that.host == null) ) return false;
            if (host.compareToIgnoreCase(that.host) != 0 ) {
                return false;
            }
            return true;
        }

        /** get the LocalId field
         * @return String
         */        
	public	 String getLocalId() {
            return localId ;
        } 

        /** get the host field
         * @return host member String
         */        
	public	 String getHost() {
            return host ;
        }
        
	/**
         * Set the localId member
         * @param localId String to set
         */
	public	 void setLocalId(String localId) {
            this.localId = localId;
        }
        
        /** set the callId field
         * @param cid Strimg to set
         * @throws IllegalArgumentException if cid is null or is not a token or 
         * token@token
         */        
        public void setCallID(String cid ) throws IllegalArgumentException {
		if (cid == null) throw new IllegalArgumentException("NULL!");
		int index = cid.indexOf('@');
		if (index == -1 ) {
			localId = cid;	
			host = null;
		} else {
			localId = cid.substring(0,index);
			host = cid.substring(index+1,cid.length());
			if (localId == null || host == null) {
				throw new IllegalArgumentException
				("CallID  must be token@token or token");
			}
		}
	}
        
	/**
         * Set the host member
         * @param host String to set
         */
	public	 void setHost(String host) {
            this.host = host ;
        } 

}
