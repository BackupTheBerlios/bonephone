/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* - Added JAVADOC                                                              *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package examples.proxy;
import gov.nist.sip.stack.*;
import gov.nist.sip.msgparser.*;

public class SIPStackMessageFactoryImpl implements SIPStackMessageFactory {

    /**
     * Constructor
     * Generate a new SIPServerRequest.
     * @param sipRequest A SIP request
     * @param mshChan The MessageChannel used by the request
     */

    public SIPServerRequestInterface
	newSIPServerRequest(SIPRequest sipRequest, MessageChannel msgChan) {
	return (SIPServerRequestInterface) 
	    new SIPServerRequest(sipRequest,msgChan);
    }
    
    /**
     * Constructor
     * Generate a new SIPServerResponse.
     * @param sipResponse A SIP response
     * @param mshChan The MessageChannel used by the response
     */

    public SIPServerResponseInterface  
	newSIPServerResponse(SIPResponse sipResponse, MessageChannel msgChan) {
	return new SIPServerResponse(sipResponse,msgChan);
    }

}
