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
*  Relative path segment of a URI
*/
public class RelPath extends Path {
    
        /** relSegment field
         */    
	protected String  relSegment;

        /** absPath field
         */        
        protected AbsPath absPath;

        /**
         * Encode into canonical form.
         * @return String
         */        
        public String encode() {
		String retval = relSegment;
		if (absPath != null) {
			retval += SLASH + absPath.encode();
		}
		return retval;
	}
		
        /** get the AbsPath field
         * @return AbsPath
         */        
	public AbsPath getAbsPath() { 
            return absPath;
        }
	
        /** get the RelSegment field
         * @return String
         */        
        public String  getRelSegment() { 
            return relSegment;
        }
        
	/**
         * Set the relSegment member
         * @param r String to set
         */
	public	 void setRelSegment(String r) {
            relSegment = r ;
        }
        
	/**
         * Set the absPath member
         * @param a AbsPath to set
         */
	public	 void setAbsPath(AbsPath a) {
            absPath = a ;
        }
        
}
