/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../doc/uncopyright.html for conditions of use.                     *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/


package tools.responder;
import  java.util.LinkedList;
import  java.util.ListIterator;
import  jain.protocol.ip.sip.*;
import  jain.protocol.ip.sip.address.*;
import  jain.protocol.ip.sip.header.*;
import  gov.nist.sip.msgparser.*;
import  gov.nist.sip.sipheaders.*;
import gov.nist.sip.stack.*;



/**
 * There is one node like this for each EXPECT element of the test description.
 * The expect node is triggered when a message is recieved that matches the
 * EXPECT specification.
 */


class Expect
{
   protected String nodeId;
   /** routine that gets run when the event triggers.
   */
    protected String onTrigger;

   /** Routine that gets run when the event completes.
   */
   protected String onCompletion;
    
    protected CallFlow callFlow;

   /** The trigger event set for this expect node.
    */
    protected String enablingEvent;

    /** The event that is generated after this mesasge is sent.
     */
     protected String generatedEvent;

    /** The message that will trigger this expect node */
    protected String  triggerMessage;

    private boolean enablingEventSeen;
    
    private boolean fired;
    
    private boolean isOneShot;
    
   /**
    * Messages to be sent when the trigger is satisfied.
    */
    protected  LinkedList sendList;
    

     protected boolean checkExpectNode( String enablingEvent, 
			String triggerMessage) {
	Debug.println("checkExpectNode : enablingEvent = " + enablingEvent + 
		" triggerMessage = " + triggerMessage);
        if (this.fired) return false;
	if (enablingEvent == null && this.enablingEvent == null ) {
	      	if (triggerMessage.equals(this.triggerMessage)) {
			fired = true;
			return true;
		} else return false;
	} else if (this.enablingEvent == null) {
	      	if( this.triggerMessage == null ||
		    this.triggerMessage.equals(triggerMessage)) {
		    fired = true;
		    return true;
		 } else return false;
	}
	if (! enablingEventSeen)
            enablingEventSeen = ( enablingEvent == this.enablingEvent ||
                		this.enablingEvent == null ||
                		this.enablingEvent.equals(enablingEvent));
        if (!enablingEventSeen) return false;
	if ( this.triggerMessage == triggerMessage)  return true;
        else if (this.triggerMessage == null) return true;
	else if (this.triggerMessage.equals(triggerMessage)) return true;
	else if ( this.triggerMessage == triggerMessage) return true;
	else return this.triggerMessage.equals(triggerMessage);
    
     }
     
       

    /**
     *Construct an expect node.
     */
    
    protected  Expect 
        (  CallFlow cflow,  
	   String enablingEvent, 
	   String generatedEvent,
	   String triggerMessage   )  {
        this.callFlow = cflow;
        this.enablingEvent  = enablingEvent;
	this.triggerMessage = triggerMessage;
	this.generatedEvent = generatedEvent;
        this.isOneShot = true;
	sendList = new LinkedList();
    }
	
    
    protected void  setJythonCode(String jythonCode) {
	if (jythonCode == null) throw new IllegalArgumentException("null arg!");
	JythonInterp jythonInterp = callFlow.getJythonInterp();
	jythonInterp.exec(jythonCode);
    }
    
        
    /**
     *Add a generated message to the send list.
     */
    protected void addGeneratedMessage(GeneratedMessage gmsg) {
        sendList.add(gmsg);
    }
    
    /**
     *Generate a list of messages to send.
     */
    protected LinkedList 
        generateMessages(SIPMessage triggerMessage) {
        // If this is one shot then mark it fired.
        if (this.isOneShot) this.fired = true;
	Debug.println("Expect eventId = " + enablingEvent);
	if (triggerMessage != null)  {
	  if (ServerLog.needsLogging())  {
	      Debug.println("Generating message for " + 
			triggerMessage.encode());
	  }
	} else  {
	  if (ServerLog.needsLogging())  
	      Debug.println("Generating message for  NULL " );
	}
    
        LinkedList retval = new LinkedList();
        ListIterator li = sendList.listIterator();
        while(li.hasNext()) {
            GeneratedMessage gmsg = (GeneratedMessage) li.next();
            String genMsg = gmsg.generateMessage(triggerMessage);
            if (genMsg != null) retval.add(genMsg);
        }
	
	if (onTrigger != null) {
		Trigger trigger = new Trigger();
	        trigger.jythonInterp = callFlow.getJythonInterp();
		trigger.method = onTrigger;
		trigger.message = triggerMessage;
		callFlow.addTrigger(trigger);
	}
        return retval;
        
    }
    
    
}
