/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) , added JAVADOC                *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
 * TimeStamp SIPObject
 */

public class TimeStamp extends SIPHeader {
    
        /** timeStamp field
         */
    protected float timeStamp;
    
    /** delay field
     */
    protected float delay;
    
       /** Default Constructor
         */
    public TimeStamp() {
        super(TIMESTAMP);
        delay=-1;
    }
    
        /**
         * Return canonical form of the header.
         * @return String
         */
    public String encode() {
        return headerName + COLON + SP + timeStamp + NEWLINE;
    }
    
        /** get the TimeStamp
         * @return TimeStamp field
         */
    public float getTimeStamp () {
        return timeStamp;
    }
    
        /** get the TimeStamp
         * @return TimeStamp field
         */
    public float getDelay () {
        return delay;
    }
    
     /** return true if delay exists
      * @return boolean
      */
    public boolean hasDelay () {
        return delay!=-1;
    }
    
    /* remove the Delay field
     */
    public void removeDelay() {
        delay=-1;
    }
    
        /**
         * Set the timeStamp member
         * @param t float to set
         */
    public void setTimeStamp(float t) { 
        timeStamp= t ;
    }
    
       /**
         * Set the delay member
         * @param d float to set
         */
    public void setDelay(float d) { 
        delay=d ;
    }
    
}
