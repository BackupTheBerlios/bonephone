package tools.responder;
import java.util.LinkedList;
import java.util.Hashtable;
import gov.nist.sip.stack.*;
import gov.nist.sip.msgparser.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Enumeration;
import java.io.IOException;
import java.util.Collection;

/**
 * Implements an event engine. Keeps a list of ready nodes (i.e. nodes for
 * which there are no dependencies) and when these become ready, computes
 * nodes for which depedencies have been satisfied and moves these up
 * to the firedNodes list.
 */

public class CallFlow implements
SIPServerRequestInterface,
SIPServerResponseInterface   {
   /** Jython interpreter that is registered here.
    */
    
    private JythonInterp jythonInterp;
    
    MessageChannel messageChannel;
    
        /** Transaction repeat interval. */
    protected int repeatInterval;
    
        /** Description **/
    protected String description;
    
    
        /** Instantiate on **/
    protected String instantiateOn;
    
        /** SIPMessage to process. */
    protected SIPMessage sipMessage;
    
        /** Match nodes */
    protected Hashtable messageTemplates;
    
        /**/
    
        /** Agent table */
    protected Hashtable agentTable;
    
        /** Nodes that are ready to fire.  (i.e. that have dependencies
         * fulfilled).
         */
    protected LinkedList readyNodes;
        /** A list of event nodes that have unfulfilled dependencies.
         */
    protected LinkedList expectNodes;

    protected String uriString;
    
    private LinkedList toSend;
    
    private LinkedList triggers;
    
    // A list of templates that have matched for the given incoming message.
    private LinkedList matchList;
    
    private HashSet generatedEvents;

    private HashSet expectedEvents;
    
    
    /** Get the imbedded jython interpreter.
     */
    protected JythonInterp getJythonInterp() {
        if (this.jythonInterp == null) this.jythonInterp = new JythonInterp();
        return this.jythonInterp;
    }
    
    /** Check the expect nodes list. If any expect node has its dependency
     *satisfied, move it over to the readyNodes list.
     */
    
    private LinkedList checkExpectNodes (SIPMessage trigger) {
        LinkedList retval = new LinkedList();
        Iterator expectIterator = expectNodes.iterator();
        Iterator it = this.matchList.listIterator();
	LinkedList deleteList = new LinkedList();
        boolean newEventAdded  = true;
        while (newEventAdded) {
            newEventAdded = false;
            while (it.hasNext()){
                String tid = (String) it.next();
                // For each expect node, look for generated messages.
                // if a message is generated then add to our return list.
                while (expectIterator.hasNext()) {
		    // check for nodes that are fired by just this
		    // message.
                    LinkedList newevents = new LinkedList();
                    Expect expect = (Expect) expectIterator.next();
		    Debug.println("Checking expect node " + expect.nodeId);
		    if (expect.checkExpectNode(null,tid)) {
                       if (expect.generatedEvent != null) {
                            newevents.add(expect.generatedEvent);
                            newEventAdded = true;
                        }
                        LinkedList ll = expect.generateMessages(trigger);
                        retval.addAll(ll);
		    }
                    Iterator it1 = generatedEvents.iterator();
                    while(it1.hasNext()) {
                        String eid = (String) it1.next();
                        if (expect.checkExpectNode(eid,tid)) {
                            if (expect.generatedEvent != null) {
                                newevents.add(expect.generatedEvent);
                                newEventAdded = true;
				deleteList.add(tid);
                            }
                            LinkedList ll = expect.generateMessages(trigger);
                            retval.addAll(ll);
                        }
                    }
                    this.generatedEvents.addAll(newevents);
                }
            }
        }
	ListIterator dit = deleteList.listIterator();
	while(dit.hasNext()) {
	     String id = (String) dit.next();
	     this.matchList.remove(id);
	}
	return retval;
    }
    
        /** Check start nodes. (nodes for which there are no dependencies).
         */
    protected LinkedList checkStartNodes() {
        matchList = new LinkedList();
        Iterator iterator = readyNodes.iterator();
        LinkedList retval = new LinkedList();
        while(iterator.hasNext()) {
            Expect expect = (Expect) iterator.next();
            Debug.println("Checking " + expect.nodeId);
            LinkedList llist = expect.generateMessages(null);
            retval.addAll(llist);
            String generatedEvent = expect.generatedEvent;
            if (generatedEvent != null) {
                generatedEvents.add(generatedEvent);
            }
        }
        return retval;
    }
        /** Try to match with one of our templates and return the one
         *that matches (or null if there is no match).
         *@param sipMessage incoming message to search for a match.
         */
    protected void matchRecieve(SIPMessage message) {
        Enumeration en = messageTemplates.keys();
        while (en.hasMoreElements()) {
            String tid = (String) en.nextElement();
            MessageTemplate template = (MessageTemplate)
            messageTemplates.get(tid);
            Debug.println("matching received message against " + tid);
            if (template.match(message)) {
                Debug.println("match found for template" + tid);
                this.matchList.add(tid);
            }
            
        }
        
    }
    
    
    
    /** Call flow constructor.
     */
    
    protected CallFlow() {
        readyNodes = new LinkedList();
        messageTemplates = new Hashtable();
        expectNodes = new LinkedList();
        matchList = new LinkedList();
        toSend = new LinkedList();
        triggers = new LinkedList();
        agentTable = new Hashtable();
	generatedEvents = new HashSet();
    }
    
    
    
    
    
        /**
         *Add an expect node to our event graph.
         *@param expect ExpectNode to add to our list of expect nodes.
         */
    protected void
    addExpectNode(Expect expect) {
        
        if (expect.enablingEvent == null && expect.triggerMessage == null) { 
		Debug.println("Adding to start node " + expect.nodeId);
		readyNodes.add(expect);
        } else {
                expectNodes.add(expect);
        }
	if (expect.generatedEvent != null) {
	    generatedEvents.add(expect.generatedEvent);
	}
    }
    
    
        /** Add a message template to the call flow graph.
         *@param template is the template (SIPRequest/response) with
         *which we want to match.
         */
    protected void
    addMessageTemplate (MessageTemplate template) {
        if (messageTemplates == null) {
            throw new IllegalArgumentException("null arg");
        }
        String templateId = template.id;
        if (messageTemplates.containsKey(templateId)) {
            throw new IllegalArgumentException
            ("duplicate key: " + templateId);
        }
        // Store this in our template table.
        messageTemplates.put(templateId, template);
    }
    
    
        /** Set the transaction repeat interval.
         */
    protected void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }
    
    
    
    
    private synchronized void
    processTriggers () {
        ListIterator it = triggers.listIterator();
        while (it.hasNext()) {
            Trigger trigger = (Trigger) it.next();
            String method = trigger.method;
            SIPMessage message = trigger.message;
            JythonInterp jythonInterp = trigger.jythonInterp;
            jythonInterp.runMethod(method,message);
        }
        triggers  = new LinkedList();
    }
    
    
    /**
     * Process the message.
     * @throws SIPServerException Exception that gets thrown by
     * this processor when an exception is encountered in the
     * message processing.
     */
    public synchronized void processRequest() throws SIPServerException {
        Debug.println("processRequest: ", sipMessage);
        this.matchRecieve(sipMessage);
        LinkedList messages = this.checkExpectNodes(sipMessage);
        ListIterator li = messages.listIterator();
        sendMessages(li);
        processTriggers();
    }
    
    /**
     * Process the Response.
     * @throws SIPServerException Exception that gets thrown by
     * this processor when an exception is encountered in the
     * message processing.
     */
    public synchronized void processResponse() throws SIPServerException {
        Debug.println("processResponse: ", sipMessage);
        this.matchRecieve(sipMessage);
        LinkedList messages = this.checkExpectNodes(this.sipMessage);
        ListIterator li = messages.listIterator();
        sendMessages(li);
        processTriggers();
        // Check the tansaction table.
        SIPResponse response = (SIPResponse) sipMessage;
        if (response.isFinalResponse()) {
            Debug.println("Is Final Repsponse!");
            String tid = response.getTransactionId();
            Debug.println("transaction id = " + tid);
            Transaction transaction = EventEngine.theStack.getTransaction(tid);
            if (transaction != null) {
            	Debug.println("Transaction  Retreived");
                Expect expect = transaction.expectNode;
                String completionScript = expect.onCompletion;
		Debug.println("node id = " + expect.nodeId +
			" completionScript = " + completionScript);
                if (completionScript != null) {
                    JythonInterp jinterp  = getJythonInterp();
                    jinterp.runMethod(completionScript);
                }
            }
            EventEngine.theStack.removeTransaction(tid);
        }
    }
    
    
    /** Message pump.
     * Send out messages and match the sent messages until no new messages
     * are generated.
     */
    
    protected void sendMessages(ListIterator li) throws SIPServerException {
        // if (messageChannel == null)
        messageChannel = EventEngine.theStack.getDefaultChannel();
        while(li.hasNext()) {
            String message = (String) li.next();
            Debug.println("Sending = " + message);
            StringMsgParser parser = new StringMsgParser();
            
            // See if this message has caused a cascade of messages
            // to be sent out.
            try {
                SIPMessage msg[] = parser.parseSIPMessage(message);
                try {
                    messageChannel.sendMessage(msg[0]);
                } catch (IOException ex) {
                    throw new SIPServerException (ex.getMessage());
                }
               
            } catch (SIPParseException ex) {
                // Generated a dud message!
                ex.printStackTrace();
                System.exit(0);
                
            }
        }
    }
    
    /** Register a message to send later */
    
    protected void addToSend(String message) {
        Debug.println("addToSend = " + message);
        toSend.add(message);
    }
    
    /** Add a new trigger to fire after all messages are sent */
    protected synchronized void addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
    }
    
    /** Send a message. */
    protected void sendMessages() throws SIPServerException {
        
        ListIterator li = toSend.listIterator();
        toSend = new    LinkedList();
        this.sendMessages(li);
    }
    
  
    
    /** Get the expect nodes. */
    protected Collection getExpectNodes() {
        return expectNodes;
    }
    
    /** Get the message templates */
    protected Collection getMessageTemplates() {
        return messageTemplates.values();
    }
    
    /** Return true if a message template of a given name exists. */
    protected boolean hasMessageTemplate(String key) {
        return messageTemplates.contains(key);
    }
    
    /** CHeck the graph integrity **/
    
    protected void checkIntegrity()
    throws Exception { 
	ListIterator it = expectNodes.listIterator();
	while(it.hasNext()) {
		Expect expect = (Expect) it.next();
		String enablingEvent = expect.enablingEvent;
		if (enablingEvent != null &&
			! generatedEvents.contains(enablingEvent))  
			throw new Exception("Could not find generated Event "
				+ enablingEvent);
		String triggerMessage = expect.triggerMessage;
		if (triggerMessage != null &&
			! messageTemplates.containsKey(triggerMessage) )
			throw new Exception("Could not find message template "
				+ triggerMessage);
	}

    }
    
    
    public MessageChannel getResponseChannel() { return messageChannel; }
    
    public MessageChannel getRequestChannel() { return messageChannel; }
    
    
}
