/*****************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).      *
 * See ../../../doc/uncopyright.html for conditions of use.                   *
 * Author: M. Ranganathan (mranga@nist.gov)             		      *
 * Questions/Comments: nist-sip-dev@antd.nist.gov			      *
 ******************************************************************************/

/**
 * This is a test user agent (not a real user agent).
 * It is used in the sip test system
 * to generate responses by matching requests and generating a response
 * in response to that request.
 */

package tools.responder;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.python.util.*;
import org.python.core.*;
import java.io.IOException;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.net.*;
import gov.nist.sip.*;
import java.util.*;


public class XMLContentHandler extends DefaultHandler implements TagNames
{
    
    
    
    private   CallFlow    callFlow;
    private   Expect      currentExpectNode;
    private   EventEngine myEngine;
    private   SIPMessage   messageTemplate;
    private   RequestLine requestLine;
    private   String      jythonCode;
    private   String      id;
    private   boolean     jythonContext;
    private   GeneratedMessage generatedMessage;
    private   boolean  messageTemplateContext;
    private   boolean  generateContext;
    private   Hashtable agentTable;
    
        /** Add an agent node 
         */
    private void 
    addAgent (Agent agent) {
            agentTable.put(agent.agentId,agent);
    }

	
    
    
        /** Get the agent id 
         */
    
    private Agent 
    getAgent(String agentId) {
         return (Agent) agentTable.get(agentId);
    }
    
        /**
         *Called by the sax parser when the start of an element is encountered.
         */
    public void startElement(String nameSpaceURI,
    String local, String name, Attributes attrs)
    throws SAXException {
        try{
	    Debug.println("processing " + name);
            if (name.compareTo(TagNames.CALLFLOW)== 0 ) {
                callFlow = new CallFlow();
                callFlow.instantiateOn =
                attrs.getValue(Attr.instantiateOn);
                callFlow.description =
                attrs.getValue(Attr.description);

	    } else if (name.compareTo(TagNames.AGENT) == 0) {
		Agent agent = new Agent();
		agent.agentId = attrs.getValue(Attr.agentId);
		agent.userName = attrs.getValue(Attr.userName);
		agent.requestURI  = attrs.getValue(Attr.requestURI);
                agent.host = attrs.getValue(Attr.host);
		agent.contactPort = attrs.getValue(Attr.contactPort);
		agent.contactHost = attrs.getValue(Attr.contactHost);
		addAgent(agent);
            } else if (name.compareTo(TagNames.EXPECT) == 0) {
	        // Make sure there are no unexpected attributes.
		for (int i = 0; i < attrs.getLength(); i++) {
			String attrname = attrs.getLocalName(i);
			if (   attrname.equals(Attr.enablingEvent) 
			    || attrname.equals(Attr.triggerMessage) 
			    || attrname.equals(Attr.generatedEvent)
			    || attrname.equals(Attr.nodeId)
			    || attrname.equals(Attr.onTrigger)
			    || attrname.equals(Attr.onCompletion)) continue;
			 else throw new SAXException
				("Unkown attribute in expect: nodeId = " 
				 + attrs.getValue(Attr.nodeId) +
				  " attribute name =  " + attrname);
	 	}
                String enablingEvent = attrs.getValue(Attr.enablingEvent);
	        String triggerMessage = attrs.getValue(Attr.triggerMessage);
		String generatedEvent = attrs.getValue(Attr.generatedEvent);
		String nodeId  = attrs.getValue(Attr.nodeId);
                currentExpectNode = new Expect(callFlow,enablingEvent,
			generatedEvent, triggerMessage);
		String onTrigger = attrs.getValue(Attr.onTrigger);
		String onCompletion = attrs.getValue(Attr.onCompletion);
		currentExpectNode.onTrigger = onTrigger;
		currentExpectNode.onCompletion = onCompletion;
		currentExpectNode.nodeId = nodeId;
            } else if (name.compareTo(TagNames.SIP_REQUEST) == 0) {
                if (this.messageTemplateContext) {
                    // This is a SIPRequest template node.
		    for (int i = 0; i < attrs.getLength(); i++) {
			String attrname = attrs.getLocalName(i);
			if (! attrs.getLocalName(i).equals(Attr.templateId))
			  throw new SAXException
				("Unkown attribute in SIP_REQUEST node " 
				 + " attribute name =  " + attrname);
		    }
                    messageTemplate = new SIPRequest();
                    id = attrs.getValue(Attr.templateId);
                    jythonCode = null;
                }
            } else if (name.compareTo(TagNames.SIP_RESPONSE) == 0) {
                if (this.messageTemplateContext) {
                    messageTemplate = new SIPResponse();
                    jythonCode = null;
		    for (int i = 0; i < attrs.getLength(); i++) {
			String attrname = attrs.getLocalName(i);
			if (! attrs.getLocalName(i).equals(Attr.templateId))
			  throw new SAXException
				("Unkown attribute in SIP_REQUEST node " 
				 + " attribute name =  " + attrname);
		    }
                    id = attrs.getValue(Attr.templateId);
                }
            } else if (name.compareTo(TagNames.STATUS_LINE) == 0) {
                String scode = attrs.getValue(Attr.statusCode);
                if (messageTemplateContext) {
                    StatusLine statusLine = new StatusLine();
                    try {
                        int statusCode = Integer.parseInt(scode);
                        statusLine.setStatusCode(statusCode);
                    } catch (NumberFormatException ex) {
                        throw new SAXException(ex.getMessage());
                    }
                    SIPResponse response = (SIPResponse) this.messageTemplate;
                    response.setStatusLine(statusLine);
                } else {
                    int statusCode = Integer.parseInt(scode);
                    generatedMessage.addStatusLine(statusCode);
                }
            } else if (name.compareTo(TagNames.REQUEST_LINE) == 0) {
                String method = attrs.getValue(Attr.method);
	        Debug.println("tagname = " + TagNames.REQUEST_LINE);
		Debug.println("messageTemplateContext = " + 
				messageTemplateContext);
                if (messageTemplateContext) {
		    for (int i = 0; i < attrs.getLength(); i++) {
			String attrname = attrs.getLocalName(i);
			if ( attrs.getLocalName(i).equals(Attr.requestURI) ||
			     attrs.getLocalName(i).equals(Attr.method)) 
				continue;
			  else throw new SAXException
				("Unkown attribute in REQUEST_LINE node " 
				 + " attribute name =  " + attrname);
		    }
                    requestLine = new RequestLine();
                    requestLine.setMethod(method);
                    String requestURI = attrs.getValue(Attr.requestURI);
		    StringMsgParser smp = new StringMsgParser();
		    if (requestURI != null) {
			try {
		    	   URI uri = smp.parseSIPUrl(requestURI);
			   requestLine.setUri(uri);
			} catch (SIPParseException e) {
			   throw new SAXException("Bad URL " + requestURI);
			}
		    }
                    SIPRequest request = (SIPRequest) messageTemplate;
                    request.setRequestLine(requestLine);
                } else  {
		    for (int i = 0; i < attrs.getLength(); i++) {
			String attrname = attrs.getLocalName(i);
			if ( attrs.getLocalName(i).equals(Attr.method) ||
			     attrs.getLocalName(i).equals(Attr.templateId) ||
			     attrs.getLocalName(i).equals(Attr.agentId) )
			   continue;
			   else throw new SAXException
				("Unkown attribute in REQUEST_LINE node " 
				 + " attribute name =  " + attrname);
		    }
                    String requestURI = attrs.getValue(Attr.requestURI);
		    if (requestURI == null) {
			String agentId = attrs.getValue(Attr.agentId);
			if (agentId != null)  {
			    Agent agent = getAgent(agentId);
			    if (agent== null) 
				throw new SAXException
				("Missing requestURI or agent attribute");
			    requestURI = agent.requestURI;
			}
		    }
                    generatedMessage.addRequestLine(method,requestURI);
                }
            } else if (name.compareTo(TagNames.FROM) == 0) {
	        for (int i = 0; i < attrs.getLength(); i++) {
		     String attrname = attrs.getLocalName(i);
		     if ( attrs.getLocalName(i).equals(Attr.displayName) ||
			     attrs.getLocalName(i).equals(Attr.userName) ||
			     attrs.getLocalName(i).equals(Attr.host)     ||
			     attrs.getLocalName(i).equals(Attr.agentId)  ) 
			  continue;
		     	  else throw new SAXException
				("Unkown attribute in FROM node " 
				 + " attribute name =  " + attrname);
	        }
                String displayName = attrs.getValue(Attr.displayName);
                String userName = attrs.getValue(Attr.userName);
                String hostName = attrs.getValue(Attr.host);
                String agentId  = attrs.getValue(Attr.agentId);
                if (agentId != null) {
                    Agent agent = getAgent(agentId);
                    if (agent == null) throw new SAXException
                            ("agent not found "+agentId);
                    if (displayName == null) 
                        displayName = agent.displayName;
                    if (userName == null) 
                        userName = agent.userName;
                    if (hostName == null)
                        hostName = agent.host;
                }
                    
                if(this.messageTemplateContext) {
                    From from = new From();
                    Address address = new Address();
                    address.setDisplayName(displayName);
                    URI uri = new URI();
                    Host host = new Host();
                    host.setHostname(hostName);
                    uri.setHost(host);
                    uri.setUser(userName);
                    address.setAddrSpec(uri);
                    from.setAddress(address);
                    try {
                        messageTemplate.attachHeader(from,false);
                    } catch (SIPDuplicateHeaderException ex) {
                        throw new SAXException(ex.getMessage());
                    }
                } else {
                    generatedMessage.addFromHeader
			(displayName,userName,hostName);
                }
                
                
            } else if (name.compareTo(TagNames.TO) == 0) {
	        for (int i = 0; i < attrs.getLength(); i++) {
		     String attrname = attrs.getLocalName(i);
		     if ( attrs.getLocalName(i).equals(Attr.templateId) ||
			     attrs.getLocalName(i).equals(Attr.host)       ||
			     attrs.getLocalName(i).equals(Attr.agentId)     ||
			     attrs.getLocalName(i).equals(Attr.userName)  ) 
		         continue;
		     	 else throw new SAXException
				("Unkown attribute in FROM node " 
				 + " attribute name =  " + attrname);
	        }
                String displayName = attrs.getValue(Attr.displayName);
                String userName = attrs.getValue(Attr.userName);
                String hostName = attrs.getValue(Attr.host);
                String agentId = attrs.getValue(Attr.agentId);
                if (agentId != null) {
                    Agent agent = getAgent(agentId);
                    if (agent == null) throw new SAXException
                            ("agent not found "+agentId);
                    if (displayName == null) 
                        displayName = agent.displayName;
                    if (userName == null) 
                        userName = agent.userName;
                    if (hostName == null)
                        hostName = agent.host;
                }
                if(this.messageTemplateContext) {
                    To to = new To();
                    Address address = new Address();
                    address.setDisplayName(displayName);
                    URI uri = new URI();
                    Host host = new Host();
                    host.setHostname(hostName);
                    uri.setHost(host);
                    uri.setUser(userName);
                    address.setAddrSpec(uri);
                    to.setAddress(address);
                    try {
                        messageTemplate.attachHeader(to,false);
                    } catch (SIPDuplicateHeaderException ex) {
                        throw new SAXException(ex.getMessage());
                    }
                } else {
                    generatedMessage.addToHeader(displayName,userName,hostName);
                }
            } else if (name.compareTo(TagNames.CALLID) == 0 ) {
                String lid = attrs.getValue(Attr.localId);
                String host = attrs.getValue(Attr.host);
                if (this.messageTemplateContext) {
                    CallID cid = new CallID();
                    CallIdentifier cidf = new CallIdentifier(lid,host);
                    cid.setCallIdentifier(cidf);
                } else {
                    generatedMessage.addCallIdHeader();
                }
            } else if (name.compareTo(TagNames.CONTACT) == 0) {
	        for (int i = 0; i < attrs.getLength(); i++) {
		     String attrname = attrs.getLocalName(i);
		     if ( attrs.getLocalName(i).equals(Attr.displayName)    ||
			     attrs.getLocalName(i).equals(Attr.userName)    ||
			     attrs.getLocalName(i).equals(Attr.action)      ||
			     attrs.getLocalName(i).equals(Attr.contactHost) ||
			     attrs.getLocalName(i).equals(Attr.contactPort) ||
			     attrs.getLocalName(i).equals(Attr.agentId)     ||
			     attrs.getLocalName(i).equals(Attr.expires) ) 
			     continue;
		     else    throw new SAXException
				("Unkown attribute in CONTACT node " 
				 + " attribute name =  " + attrname);
	        }
                String displayName = attrs.getValue(Attr.displayName);
                String userName = attrs.getValue(Attr.userName);
                String hostName = attrs.getValue(Attr.contactHost);
                String portString = attrs.getValue(Attr.contactPort);
                String expiryTimeString = attrs.getValue(Attr.expires);
                String action = attrs.getValue(Attr.action);
		String agentId = attrs.getValue(Attr.agentId);
		if (action == null) action = "proxy";
		if (agentId != null) {
			Agent agent = getAgent(agentId);
                        if (displayName == null) 
                            displayName = agent.displayName;
                        if (userName == null) 
                            userName = agent.userName;
                        if (hostName == null)
                            hostName = agent.contactHost;
			if (portString == null)
			    portString = agent.contactPort;
		}
                
                
                if (this.messageTemplateContext) {
                    // Generating a message template for the expires header.
                    ContactList clist = new ContactList();
                    Contact contact = new Contact();
                    clist.add(contact);
                    URI uri = new URI();
                    Host host = new Host();
                    host.setHostname(hostName);
                    uri.setHost(host);
                    uri.setUser(userName);
                    
                    if (portString != null) {
                        int port = new Integer(portString).intValue();
                        uri.setPort(port);
                    }
                    Address address = new Address();
                    address.setAddrSpec(uri);
                    contact.setAddress(address);
                    if (expiryTimeString != null) {
                        long expiryTimeSec = 
                            new Long(expiryTimeString).longValue();
                        contact.setExpires(expiryTimeSec);
                    }
                    messageTemplate.attachHeader(clist,false);
                    
                } else {
                    int port = 5060;
                    if (portString != null) port = 
			new Integer(portString).intValue();
                    long expiryTimeSec = 3600;
                    if (expiryTimeString != null) {
                        expiryTimeSec = new Long(expiryTimeString).longValue();
                    }
                    if (userName == null)
                        throw new Exception("Missing attribute userName");
                    if (action == null) 
                        action = Attr.proxy;
                    String uri;
                    if (hostName == null) {
                        uri = SIPKeywords.SIP+ Separators.COLON +
                            userName + Separators.AT +
                            EventEngine.theStack.getHostAddress() +
                            Separators.COLON + 
                            EventEngine.theStack.getDefaultPort() +
                            Separators.SEMICOLON +
                            SIPKeywords.TRANSPORT + 
                            Separators.EQUALS +
                            EventEngine.theStack.getDefaultTransport();
                    } else uri = SIPKeywords.SIP    +   Separators.COLON 
                                    +   userName    +   Separators.AT 
                                    +   hostName    +   ":" +  port;
                    generatedMessage.addContactHeader(displayName,uri,
                            expiryTimeSec,action);
                }
                
                
            } else if (name.compareTo(TagNames.GENERATE) == 0 ) {
                generateContext = true;
                
                if (currentExpectNode == null) {
                    throw new SAXException ("Bad element nesting.");
                }
                String id = attrs.getValue(Attr.messageId);
                String retransmit = attrs.getValue(Attr.retransmit);
		String delayString = attrs.getValue(Attr.delay);
		int delay = 0;
		if (delayString != null) {
		   try {
		       delay = Integer.parseInt(delayString);
		   } catch (NumberFormatException ex) {
			throw new SAXException ("Bad integer value " 
				+ delayString);
		   }
		}

                generatedMessage = new GeneratedMessage(id,callFlow,
                		currentExpectNode,retransmit);
		generatedMessage.delay = delay;
                currentExpectNode.addGeneratedMessage(generatedMessage);
            } else if (name.compareTo(TagNames.MESSAGE_TEMPLATES) == 0 ) {
                messageTemplateContext = true;
	    } else if (name.compareTo(TagNames.JYTHON_CODE) == 0) {
		this.jythonCode = null;
	    } else if (name.compareTo(TagNames.STATE_MACHINE) == 0) {
            } else if (name.compareTo(TagNames.AGENTS) == 0) {
            } else {
		throw new SAXException("Unkown tag " + name);
	    }
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.fillInStackTrace();
            throw new SAXException(ex.getMessage());
        }
    }
    
        /**
         *Called by the sax parser when the end of an element is encountered.
         */
    public void endElement(String namespaceURI,
    String local, String name)
    throws SAXException {
        if (name.equals(TagNames.CALLFLOW)) {
            //  Do an integrity check.
            // For every Expect node, there must be
            // a valid event template.
            try {
                callFlow.checkIntegrity();
            } catch (Exception ex) {
		System.out.println(ex.getMessage());
		ex.printStackTrace();
                throw new SAXException(ex.getMessage());
            }
	} else if (name.equals(TagNames.JYTHON_CODE)) {
	    if (this.jythonCode != null) {
	       JythonInterp jythonInterp = callFlow.getJythonInterp();
		try {
	       	    jythonInterp.exec(this.jythonCode);
		} catch (PyException ex) {
		    throw new SAXException("error in evalScript \n"  
			+ this.jythonCode+ "\n" + 
			ex.getMessage());
		}
	       this.jythonCode = null;
	    }
        } else if (name.equals(TagNames.EXPECT)) {
	    if (this.jythonCode != null) 
	         currentExpectNode.setJythonCode(this.jythonCode);
	    this.jythonCode = null;
            callFlow.addExpectNode(currentExpectNode);
            currentExpectNode = null;
        } else if (name.equals(TagNames.SIP_REQUEST)) {
            if (this.messageTemplateContext) {
                MessageTemplate msgTemplate =
                new MessageTemplate(messageTemplate,id,
                this.jythonCode);
                callFlow.addMessageTemplate(msgTemplate);
                messageTemplate = null;
		this.jythonCode = null;
            }
        } else if (name.equals(TagNames.SIP_RESPONSE)) {
            if (this.messageTemplateContext) {
                MessageTemplate msgTemplate =
                new MessageTemplate(this.messageTemplate,this.id, 
		  this.jythonCode);
                callFlow.addMessageTemplate(msgTemplate);
                messageTemplate = null;
		this.jythonCode = null;
            }
        } else if (name.equals(TagNames.GENERATE)) {
            generateContext = false;
	    if (generatedMessage.messageType.equals(Attr.SipRequest) &&
		generatedMessage.method == null)  {
		System.out.println("Current message node = "  +
			generatedMessage.messageId);
		throw new SAXException("Missing method");
	     }
        } else if (name.equals(TagNames.MESSAGE_TEMPLATES)) {
            messageTemplateContext = false;
        }
        
    }
    
         /**
          *Called when characters are encountered.
          */
    public void characters(char[] ch, int start, int length) {
         String str = new String(ch,start,length);
         if (str.trim().equals("")) return;
         if (this.jythonCode == null) this.jythonCode = str;
         else this.jythonCode += str;
    }
         /** Get the generated call flow event graph.
          *This is the parsed structure that is generated from the xml spec.
          */
    protected CallFlow getCallFlow() {
        return this.callFlow;
    }
    
        /** Constructor.
         */
    protected XMLContentHandler ( String eventFile, String agentFile) {
        myEngine = EventEngine.theStack;
	agentTable = new Hashtable();
        try {
            XMLReader saxParser = (XMLReader)Class.forName
            ("org.apache.xerces.parsers.SAXParser").newInstance();
            saxParser.setContentHandler(this);
            saxParser.setFeature
            ("http://xml.org/sax/features/validation",true);
            // parse the xml specification for the event tags.
	    saxParser.parse(agentFile);
            saxParser.parse(eventFile);
        } catch (SAXParseException spe) {
            // Error generated by the parser
            System.out.println ("\n** Parsing error"
            + ", line " + spe.getLineNumber ()
            + ", uri " + spe.getSystemId ());
            System.out.println("   " + spe.getMessage() );
            // Use the contained exception, if any
            Exception  x = spe;
            if (spe.getException() != null) x = spe.getException();
            x.printStackTrace();
	    System.exit(0);
        } catch (SAXException sxe) {
            // Error generated by this application
            // (or a parser-initialization error)
            Exception  x = sxe;
            if (sxe.getException() != null) x = sxe.getException();
            x.printStackTrace();
	    System.exit(0);
        } catch (IOException ioe) {
            // I/O error
            ioe.printStackTrace();
	    System.exit(0);
        } catch (Exception pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();
	    System.exit(0);
        }
    }
    
    
    
    
    
}



