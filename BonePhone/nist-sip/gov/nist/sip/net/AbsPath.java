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
* Absolute path component of the URL.
*
* <pre>
* From RFC 2396
*
*
*3.3. Path Component
*
*   The path component contains data, specific to the authority (or the
*   scheme if there is no authority component), identifying the resource
*   within the scope of that scheme and authority.
*
*      path          = [ abs_path | opaque_part ]
*
*      path_segments = segment *( "/" segment )
*      segment       = *pchar *( ";" param )
*      param         = *pchar
*
*      pchar         = unreserved | escaped |
*                      ":" | "@" | "&" | "=" | "+" | "$" | ","
*
*   The path may consist of a sequence of path segments separated by a
*   single slash "/" character.  Within a path segment, the characters
*   "/", ";", "=", and "?" are reserved.  Each path segment may include a
*   sequence of parameters, indicated by the semicolon ";" character.
*   The parameters are not significant to the parsing of relative
*   references.
* </pre>
*
*/
public class AbsPath extends Path {
    
        /** pathSegments field
         */    
	protected PathSegments pathSegments;

        /** default constructor
         */        
	public AbsPath() {
		super();
	}

        /**
         * Encode into canonical form.
         * @return String
         */
	public String encode() {
		return SLASH + pathSegments.encode();
	}
        
        /** get the PathSegments field
         * @return PathSegments
         */        
        public PathSegments getPathSegments() {
	     return pathSegments;
	}
        
	/**
         * Set the pathSegments member
         * @param p PathSegments to set
         */
	public	 void setPathSegments(PathSegments p) {
            pathSegments = p ;
        } 
		
}
