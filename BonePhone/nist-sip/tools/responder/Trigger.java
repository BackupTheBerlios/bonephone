
package tools.responder;
import gov.nist.sip.msgparser.*;

/** A trigger message that gets run after the message and all responses
*   have been processed.
*/
public class Trigger {
	protected JythonInterp jythonInterp;
	protected String method;
	protected SIPMessage message;
}
