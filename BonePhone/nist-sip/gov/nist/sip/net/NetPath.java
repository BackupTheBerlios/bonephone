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
*  This is a class that records the NetPath component of a URL
* 
* <pre>
* From RFC 2396
*
* 3. URI Syntactic Components
* 
*    The URI syntax is dependent upon the scheme.  In general, absolute
*    URI are written as follows:
* 
*       <scheme>:<scheme-specific-part>
* 
*    An absolute URI contains the name of the scheme being used (<scheme>)
*    followed by a colon (":") and then a string (the <scheme-specific-
*     part>) whose interpretation depends on the scheme.
*
*  The URI syntax does not require that the scheme-specific-part have
*  any general structure or set of semantics which is common among all
*  URI.  However, a subset of URI do share a common syntax for
*  representing hierarchical relationships within the namespace.  This
*  "generic URI" syntax consists of a sequence of four main components:
*
*     <scheme>://<authority><path>?<query>
*
*   each of which, except <scheme>, may be absent from a particular URI.
*   For example, some URI schemes do not allow an <authority> component,
*   and others do not use a <query> component.
*
*      absoluteURI   = scheme ":" ( hier_part | opaque_part )
*
*   URI that are hierarchical in nature use the slash "/" character for
*   separating hierarchical components.  For some file systems, a "/"
*   character (used to denote the hierarchical structure of a URI) is the
*   delimiter used to construct a file name hierarchy, and thus the URI
*   path will look similar to a file pathname.  This does NOT imply that
*   the resource is a file or that the URI maps to an actual filesystem
*   pathname.
*
*      hier_part     = ( net_path | abs_path ) [ "?" query ]
*
*      net_path      = "//" authority [ abs_path ]
*
*      abs_path      = "/"  path_segments
*
*
*
*
* Berners-Lee, et. al.        Standards Track                    [Page 11]
* RFC 2396                   URI Generic Syntax                August 1998
*
*
*   URI that do not make use of the slash "/" character for separating
*   hierarchical components are considered opaque by the generic URI
*   parser.
*
*      opaque_part   = uric_no_slash *uric
*
*      uric_no_slash = unreserved | escaped | ";" | "?" | ":" | "@" |
*                      "&" | "=" | "+" | "$" | ","
*
*   We use the term <path> to refer to both the <abs_path> and
*   <opaque_part> constructs, since they are mutually exclusive for any
*   given URI and can be parsed as a single component.
* </pre>
*
*/
public class NetPath extends Path {
    
	/**
	*  Authority for the net path
	*/
	protected Authority authority;
	/**
	*  Absolute path component of the path
	*/
	protected AbsPath   absPath;
           
	/**
         * Encode into canonical form.
         * @return String
         */
	public String encode() {
		return SLASH + SLASH + authority.encode() + absPath.encode();
	}
        
	/**
         * Accessor function for the authority component.
         * @return Authority
         */
	public	 Authority getAuthority() {
            return authority ;
        }
        
	/**
         * Accessor function for the absolute path component.
         * @return AbsPath
         */
	public	 AbsPath getAbsPath() {
            return absPath ;
        }
        
	/**
         * Set the authority member
         * @param a Authority to set
         */
	public	 void setAuthority(Authority a) { 
            authority = a ;
        }
        
	/**
         * Set the absPath member
         * @param a AbsPath to set
         *
         */
        public void setAbsPath(AbsPath a) {
            absPath = a ;
        }
     
}
