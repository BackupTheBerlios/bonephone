package org.mbus;

/**
 * MessageHandler.java
 *
 * @author Stefan Prelle
 * @version $Id: MessageHandler.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $ 
 */

public abstract class MessageHandler  {
    
    protected MessageBus mbus;

    //-----------------------------------------------------------
    /**
     * Instantiate a new class that handles incoming messages.
     * It automatically attaches itself to the MessageBus.
     *
     * @param mbus MessageBus to Handler should attach to
     */
    public MessageHandler(MessageBus mbus) {
	this.mbus = mbus;
	mbus.attachMessageHandler(this);
    }
    
    //-----------------------------------------------------------
    /**
     * Handle an incoming message and return an eventual reply.
     * 
     * @param mess Incoming Message
     */
    public abstract void handleMessage(Message mess);
    
    //-----------------------------------------------------------
    /**
     * Notify of a failed delivery.
     * 
     * @param seqnum Sequencenumber
     * @param mess Incoming Message
     */
    public abstract void deliveryFailed(int seqnum, Message mess);
    
    //------------------------------------------------------------
    /**
     * Indicate a new entity on the MBus.
     *
     * @param addr Address of the entity
     */
    public abstract void newEntity(Address addr);
    
    //------------------------------------------------------------
    /**
     * Indicate that an entity unexpectedly left the MBus.
     *
     * @param addr Address of the entity
     */
    public abstract void entityDied(Address addr);
    
    //------------------------------------------------------------
    /**
     * Indicate that an entity was shut down correctly.
     *
     * @param addr Address of the entity
     */
    public abstract void entityShutdown(Address addr);
    
} // MessageHandler
