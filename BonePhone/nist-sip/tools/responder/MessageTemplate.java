/*
 * Message.java
 *
 * Created on July 2, 2001, 2:37 PM
 */

package tools.responder;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.net.*;
import org.python.util.*;
import org.python.core.*;
import org.xml.sax.SAXException;

/** This defines a message template with which we can match incoming messages.
 *
 * @author  mranga
 * @version 
 */
public class MessageTemplate extends Object {
    
    
    /** Identifier to assign to this message node. */
    protected String id;
    
    /** Match code that further defines the template */
    protected JythonInterp jythonCode;
    
    /** match template */
    protected SIPMessage template;
    
   
    
    protected
        MessageTemplate(SIPMessage sipmsg, String tid, String jcode) 
        throws SAXException {
        template = sipmsg;
        id  = tid;
        if (jcode != null) {
            try {
                jythonCode = new JythonInterp(jcode);
            } catch (PyException ex) {
                throw new SAXException("syntax error in jython filter "+
                                ex.getMessage());
            }
                                
        }
    }
    
   
    /** Match this sipMessage with a template. 
     *@param sipMessage message that we want to match with our template.
     */
    protected boolean match(SIPMessage message) {
        if (jythonCode == null) 
            return message.match(template);   
        else 
            return message.match(template) && jythonCode.match(message); 
    }

}
