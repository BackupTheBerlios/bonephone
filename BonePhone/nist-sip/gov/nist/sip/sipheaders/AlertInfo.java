/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modifications by: O. Deruelle (deruelle@antd.nist.gov added JAVADOC)         *                                              *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

import gov.nist.sip.*;
import gov.nist.sip.net.*;

/**
* AlertInfo SIP Header.
* 
* <pre>
*
* 6.9 Alert-Info
* 
*    The Alert-Info header field indicates that the content indicated in
*    the URLs should be rendered instead of ring tone. A user SHOULD be
*    able to disable this feature selectively to prevent unauthorized
*    disruptions.
* 
*
*
*     Alert-Info  =  "Alert-Info" ":" # ( "<" URI ">" *( ";" generic-param ))
*     generic-param  =  token [ "=" ( token | host | quoted-string ) ]
*
*
*   Example:
*
*   Alert-Info: <http://wwww.example.com/sounds/moo.wav>
*
* </pre>
*/
public class AlertInfo  extends SIPHeader {
    
    /** URI field
     */
    protected URI uri;

    /** Parmeter list for the header.
     */
    protected NameValueList parms;
    
     
    /** Constructor
     */
    public AlertInfo() {
        super(ALERT_INFO);
        parms = new NameValueList("parms");
    }
    
        /**
         * Return in canonical form.
         * @return The String encoded canonical version of the header.
         */
    public String encode() {
        String encoding = headerName + COLON + SP;
        encoding += LESS_THAN + uri.encode() + GREATER_THAN;
        if (!parms.isEmpty()) {
            encoding += SEMICOLON + parms.encode();
        }
        encoding += NEWLINE;
        return encoding;
    }
   
    /** Get the URI field.
    * @return URI field of this AlertInfo header.
    */
    public URI getUri() {
        return uri ;
    }
    
    /** Get the parameter list for this header.
    * @return A name-value list containing the parameters.
    */
    public NameValueList getParms() {
        return parms ;
    }
    
        /**
         * Set the uri member
         * @param u URI to set
         */
    public void setUri(URI u) {
        uri = u ;
    }
    
        /**
         * Set the parms member
         * @param p Parameters list to set.
         */
    public void setParms(NameValueList p) {
        parms = p ;
    }
    
}
