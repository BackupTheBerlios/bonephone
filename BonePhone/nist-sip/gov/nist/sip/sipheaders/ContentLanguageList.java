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
* ContentLanguage list of headers. (Should this be a list?)
*/
public final class ContentLanguageList extends SIPHeaderList {
    
        /** Default constructor
         */    
	public ContentLanguageList () {
		super("ContentLangauge",SIPHEADERS_PACKAGE + ".ContentLanguage",
			CONTENT_LANGUAGE);
	}
        
}
