/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modifications by: O. Deruelle (deruelle@antd.nist.gov added JAVADOC)         *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/** Key words for Via header
 *
 */
public interface ViaKeywords   {
    
    /** The branch parameter is included by every forking proxy.
     */
    public static final String BRANCH="branch";
    
    /** The "hidden" paramter is included if this header field
     * was hidden by the upstream proxy.
     */
    public static final String HIDDEN="hidden";
    
    /** The "received" parameter is added only for receiver-added Via Fields.
     */
    public static final String RECEIVED="received";
    
    /** The "maddr" paramter is designating the multicast address.
     */
    public static final String MADDR="maddr";
    
    /** The "TTL" parameter is designating the time-to-live value.
     */
    public static final String TTL="ttl";
    
}
