/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov)                                    *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/ 

package gov.nist.sip.stack;

/**
 * This interface must be implemented by object which require
 * periodical cleaning by the Janitor
 */
 
public interface Cleanable {
 
    /** 
     * The function called by the Janitor to clean the object
     */
    
    public void clean();
 
}
