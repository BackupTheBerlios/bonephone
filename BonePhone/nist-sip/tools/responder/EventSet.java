/*
 * Event.java
 *
 * Created on July 5, 2001, 5:06 PM
 */

package tools.responder;
import java.util.LinkedList;
import java.util.HashSet;


/**
 *
 * @author  mranga
 * @version 
 */
public  class EventSet extends Object {
   
   
   private boolean fired;
   protected boolean oneShot;
   protected String eventId;
   protected HashSet sentMessages;
   protected HashSet receivedMessages;
   private   HashSet saveSent;
   private   HashSet saveReceived;
   
   
   /** Get the identifier for this event node.
    */
   
   protected String getEventId() { return eventId; }
   
   /** Constructor.
    */
   
   protected EventSet (String id, boolean oneShotFlag ) {
       eventId = id;
       sentMessages = new HashSet();
       receivedMessages = new HashSet();
       saveSent = new HashSet();
       saveReceived = new HashSet();
       this.oneShot = oneShotFlag;
   }
   
   /** Add a send tag to the send set.
    *@param msgId tag to add.
    */
   protected void addSend(String msgId) {
       sentMessages.add(msgId);
   }
   
   /** Add a receive tag to the expected recv. set.
    *@param msgId tag to add to the receive set.
    */
   protected void addReceive(String msgId) {
	Debug.println("adding received event to eventId " +
		eventId + " template Id = " + msgId);
       receivedMessages.add(msgId);
   }
   
   /** Note that a message of a given id has been sent and
    *return true if there are no more messages to send/recv for this
    *expect node.
    *@param msgId message id of the message that was sent.
    */
   
   protected boolean messageSent(String msgId) {
       if ((!oneShot) && fired)  return true;
       if ( sentMessages.remove(msgId) )  {
	  saveSent.add(msgId);
       }
       fired =  sentMessages.isEmpty() && receivedMessages.isEmpty();
       Debug.println(   " eventId = " + eventId + 
			" Message sent " + msgId + 
			" returning " + fired);
  	// Event has fired so reset it.
       if (oneShot && fired) {
	 fired = false;
	 sentMessages.addAll(saveSent);
	 saveSent = new HashSet();
	 return true;
       } else return fired;
   }
   
   /** Note that a message of a given id has been received and
    *return true if all the dependencies of this event node have been
    *fulfilled.
    *@param msgId message id of the message that was sent/received.
    */
   
   protected boolean messageReceived(String msgId) {
	
       if ((!oneShot) && fired)  return true;
       // One shot only fires once.
       if (oneShot && fired) return false;
       if( receivedMessages.remove(msgId) ) {
	 saveReceived.add(msgId);
       }
       fired =  receivedMessages.isEmpty() && sentMessages.isEmpty();
       Debug.println(   " eventId = " + eventId + 
			" Message received " + msgId + 
			" returning " + fired);
       
       if (oneShot && fired) {
	// fired = false;
	receivedMessages.addAll(saveReceived);
	saveReceived = new HashSet();
	return true;
       } else  {
	  return fired;
       }
   }

   /** Return true if the event is a one-shot event.
   *@return the oneShot flag.
   */

   protected boolean isOneShot() { return oneShot; }
   
}
