/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
 * See ../../../../doc/uncopyright.html for conditions of use.                  *
 * Author: M. Ranganathan (mranga@nist.gov)                                     *
 * Modified By:  O. Deruelle (deruelle@nist.gov) ,added JAVADOC                 *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                               *
 *******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;

/**
 * Status Line (for SIPReply) messages.
 */

public final class StatusLine extends SIPHeader {
    
    protected boolean matchStatusClass;
    
    /** sipVersion field
     */
    protected String sipVersion;
    
    /** status code field
     */
    protected int statusCode;
    
    /** reasonPhrase field
     */
    protected String  reasonPhrase;
    
    /** Match with a template.
     * Match only the response class if the last two digits of the
     * match templates are 0's
     */
    
    public boolean match( Object matchObj) {
        if (! (matchObj instanceof StatusLine)) return false;
        StatusLine sl = (StatusLine) matchObj;
        if (sl.sipVersion != null && ! sl.sipVersion.equals(sipVersion))
            return false;
        if ( sl.statusCode != 0) {
            if (matchStatusClass) {
                int hiscode = sl.statusCode;
                String codeString = new Integer(sl.statusCode).toString();
                String mycode = new Integer(statusCode).toString();
                if (codeString.charAt(0) != mycode.charAt(0)) return false;
            } else {
                if (statusCode != sl.statusCode) return false;
            }
        }
        if (sl.reasonPhrase == null ||
        reasonPhrase == sl.reasonPhrase) return true;
        return reasonPhrase.equals(sl.reasonPhrase);       
        
    }
    
    
   /** set the flag on a match template.
    *If this set to true, then the whole status code is matched (default
    * behavior) else only the class of the response is matched.
    */
    public void setMatchStatusClass(boolean flag) {
        matchStatusClass = flag;
    }
    
    
    /** Default Constructor
     */
    public  StatusLine() {
        super(STATUS_LINE);
        reasonPhrase = null;
        sipVersion = SIPVersion.SIP_VERSION_STRING;
    }
    
        /**
         * Encode into a canonical form.
         * @return String
         */
    public String encode() {
        String encoding = SIPVersion.SIP_VERSION_STRING + SP +
        statusCode ;
        if (reasonPhrase != null) encoding += SP + reasonPhrase;
        encoding += NEWLINE;
        return encoding;
    }
    
    /** get the Sip Version
     * @return SipVersion
     */
    public String getSipVersion() {
        return sipVersion ;
    }
    
    /** get the Status Code
     * @return StatusCode
     */
    public int getStatusCode() {
        return statusCode ;
    }
    
    /** get the ReasonPhrase field
     * @return  ReasonPhrase field
     */
    public String getReasonPhrase() {
        return reasonPhrase ;
    }
    
        /**
         * Set the sipVersion member
         * @param s String to set
         */
    public void setSipVersion(String s) {
        sipVersion = s ;
    }
    
        /**
         * Set the statusCode member
         * @param statusCode int to set
         */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode ;
    }
    
        /**
         * Set the reasonPhrase member
         * @param reasonPhrase String to set
         */
    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase ;
    }
    
        /**
         * Get the major version number.
         *@return String major version number
         */
    public String getVersionMajor() {
        if (sipVersion == null) return null;
        String major = null;
        boolean slash = false;
        for (int i = 0; i < sipVersion.length(); i++) {
            if (sipVersion.charAt(i) == '.') slash = false;
            if (slash) {
                if (major == null)
                    major = "" + sipVersion.charAt(i) ;
                else major += sipVersion.charAt(i);
            }
            if (sipVersion.charAt(i) == '/') slash = true;
        }
        return major;
    }
    
        /**
         * Get the minor version number.
         *@return String minor version number
         */
    public String getVersionMinor() {
        if (sipVersion == null) return null;
        String minor = null;
        boolean dot = false;
        for (int i = 0; i < sipVersion.length(); i++) {
            if (dot) {
                if (minor == null)
                    minor = "" + sipVersion.charAt(i);
                else minor += sipVersion.charAt(i);
            }
            if (sipVersion.charAt(i) == '.') dot = true;
        }
        return minor;
    }
    
    
    
}
