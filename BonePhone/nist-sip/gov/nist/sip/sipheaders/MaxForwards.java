/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                      
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
* MaxForwards SIPHeader
*<pre>
*   Max-Forwards  =  "Max-Forwards" ":" 1*DIGIT       
*
* Section 6.28 RFC 2543 bis 02
*<pre>
*
*/
public  class MaxForwards extends SIPHeader {
    
        /** maxForwards field.
         */    
	protected int maxForwards;
        
        /** Default constructor.
         */        
	public MaxForwards() {
            super(MAX_FORWARDS);
        }
        
        /** get the MaxForwards field.
         * @return int
         */        
	public int getMaxForwards() {
            return maxForwards;
        }
	
	/**
         * Set the maxForwards member
         * @param m int to set
         */
	public void setMaxForwards(int m) {
            maxForwards= m ;
        }
        
	/**
         * Encode into a string.
         * @return String
         *
         */	
         public String encode() {
		return headerName + COLON + maxForwards;
	}
          
        /** Boolean function
         * @return true if MaxForwards field reached zero.
         */        
        public boolean hasReachedZero() {
            return maxForwards==0;
        }
        
        /** decrement MaxForwards field one by one.
         */        
        public void decrementMaxForwards() {
            maxForwards--;
        }
        
}
