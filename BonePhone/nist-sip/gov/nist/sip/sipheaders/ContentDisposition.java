/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) , added JAVADOC                *                                                                                    
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import  gov.nist.sip.*;

/**
* Content Dispositon SIP Header.
*  
*<pre>
* From SIP rfc2543 bis 02 draft
*6.16 Content-Disposition
*
*
*
*        Content-Disposition   =  "Content-Disposition" ":"
*                                 disposition-type *( ";" disposition-param )
*        disposition-type      =  "render" | "session" | "icon" | "alert"
*                             |   disp-extension-token
*       disposition-param     =  "handling" "="
*                                 ( "optional" | "required" | other-handling )
*                             |   generic-param
*        other-handling        =  token
*        disp-extension-token  =  token
*
*
*   The Content-Disposition header field describes how the message body
*   or, in the case of multipart messages, a message body part is to be
*   interpreted by the UAC or UAS. The SIP header extends the MIME
*   Content-Type (RFC 1806 [33]).
*
*   The value "session" indicates that the body part describes a session,
*   for either calls or early (pre-call) media. The value "render"
*   indicates that the body part should be displayed or otherwise
*   rendered to the user. For backward-compatibility, if the Content-
*   Disposition header is not missing, bodies of Content-Type
*   application/sdp imply the disposition "session", while other content
*   types imply "render".
*
*   The disposition type "icon" indicates that the body part contains an
*   image suitable as an iconic representation of the caller or callee.
*   The value "alert" indicates that the body part contains information,
*   such as an audio clip, that should be rendered instead of ring tone.
*
*   The handling parameter, handling-parm, describes how the UAS should
*   react if it receives a message body whose content type or disposition
*   type it does not understand. If the parameter has the value
*   "optional", the UAS MUST ignore the message body; if it has the value
*   "required", the UAS MUST return 415 (Unsupported Media Type).  If the
*   handling parameter is missing, the value "required" is to be assumed.
*
*   If this header field is missing, the MIME type determines the default
*   content disposition. If there is none, "render" is assumed.
*</pre>
*
*/
public final class ContentDisposition extends SIPHeader 
	implements ContentDispositionKeywords {
            
        /** dispositionType field.  
         */            
	protected String dispositionType;

        /** dispositionParam field.
         */        
        protected NameValueList dispositionParam;

        /** Default constructor.
         */        
	public ContentDisposition() {
		super(CONTENT_DISPOSITION);
		dispositionParam = new NameValueList("dispositionParam");
	}
 
        /** add the specified parameter.
         * @param nv NameValue to set
         */        
	public void addDispositionParam( NameValue nv) {
            dispositionParam.add(nv);
        }
       
	/**
         * Encode into canonical string.
         * @return String
         *
         */	
        public String encode() { 
		String encoding =  headerName + COLON + SP + dispositionType;
		if ( ! dispositionParam.isEmpty() ) {
			encoding += SEMICOLON + dispositionParam.encode();
		}
		encoding += NEWLINE;
		return encoding;
	}
                    
        /** get the dispositionParam list.
         * @return NameValueList
         */        
	public NameValueList getDispositionParam() {
            return dispositionParam;
        }
 
        /** get the dispositionType field.
         * @return String
         */        
	public String getDispositionType() {
            return dispositionType;
        }

        /** get the value of the specified parameter.
         * @param parmName String to set
         * @return String.
         */        
        public String getValue( String parmName) {
            return (String) dispositionParam.getValue(parmName);
        }
        
        /** set the dispositionType field.
         * @param type String to set.
         */        
	public void setDispositionType( String type ) {
            dispositionType = type;
        }

        /** set the dispositionParam list.
         * @param nv NameValueList
         */        
	public void setDispositionParam( NameValueList nv ) {
            dispositionParam = nv;
        }

}
		
	
