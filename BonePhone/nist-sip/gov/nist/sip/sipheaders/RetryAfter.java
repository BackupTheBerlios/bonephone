/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

import java.util.*;

/**
*   RetryAfter SIPSIPObject.
*/
public class RetryAfter extends SIPHeader implements RetryAfterKeywords {
    
        /** SIPDateOrDeltaSeconds field
         *
         */    
	protected SIPDateOrDeltaSeconds expiryDate;
        
        /** duration field
         */        
	protected DeltaSeconds duration;
        
        /** comment field
         */        
	protected String comment;
        
        /** Default constructor
         */        
	public RetryAfter() {
	    super(RETRY_AFTER);
	    this.duration= null;
	}
        
        /** Encode this into cannonical form.
         * @return String
         */                
	public String encode() {
		return headerName + COLON + SP + expiryDate.encode() + NEWLINE;
	}
        
        /** get the expiryDate field
         * @return SIPDateOrDeltaSeconds
         */        
	public SIPDateOrDeltaSeconds getExpiryDate() {
            return expiryDate ;
        } 
                
        /** get the duration field
         * @return Deltaseconds
         */                
	public DeltaSeconds getDuration() { 
            return duration ;
        } 
                
        /** get the comment field
         * @return String
         */                
	public String getComment() {
            return comment ;
        }
        
         /**
          * Gets date of DateHeader
          * @return null if date does not exist
          */ 
        public Date getDate() {
             if ( expiryDate.isSIPDate() ) {
                    Calendar c=( (SIPDate)expiryDate ).getJavaCal();
                    return c.getTime();
            }
            else return null;  
        }
        
        /** Boolean function
         * @return true if comment exist, false otherwise
         */        
        public boolean hasComment(){
            return comment!=null;
        }
        
        /** Boolean function
         * @return true if duration exists, false otherwise
         */        
        public boolean hasDuration(){
            if (duration==null) return false;
            else {
                long deltaSeconds=duration.getDeltaSeconds();
                if ( deltaSeconds==-1) return true;
                else return true;
            }
        }
        
        /*  Gets boolean value to indicate if expiry value of ExpiresHeader
         * is in date format
         */
        public boolean isDate() {
            if ( expiryDate==null) return false;
            else return expiryDate.isSIPDate();
        }
        
        /** remove comment field
         */        
        public void removeComment() {
            comment=null;
        }
        
        /** remove duration field
         */        
        public void removeDuration() {
            duration=null;
        }
        
	/**
         * Set the expiryDate member
         * @param e SIPDateOrDeltaSeconds to set
         */
	public void setExpiryDate(SIPDateOrDeltaSeconds e) {
            expiryDate = e ;
        }
        
	/**
         * Set the duration member
         * @param d DeltaSeconds to set
         */
	public void setDuration(DeltaSeconds d) {
            duration = d ;
        }
        
	/**
         * Set the comment member
         * @param c String to set
         */
	public void setComment(String c) {
            comment = c ;
        }    
        
}
