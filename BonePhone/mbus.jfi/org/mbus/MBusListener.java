package org.mbus;

/**
 * MBusListener.java
 *
 * @author Stefan Prelle
 * @version $Id: MBusListener.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public interface MBusListener  {
    
    //------------------------------------------------------------
    public void incomingMessage(Message mess);
    
    //------------------------------------------------------------
    public void deliveryFailed(int seqNum, Message mess);
    
    //------------------------------------------------------------
    public void deliverySuccessful(int seqNums, Message mess);
    
    //------------------------------------------------------------
    /**
     * Indicate a new entity on the MBus.
     *
     * @param addr Address of the entity
     */
    public void newEntity(Address addr);
    
    //------------------------------------------------------------
    /**
     * Indicate that an entity unexpectedly left the MBus.
     *
     * @param addr Address of the entity
     */
    public void entityDied(Address addr);
    
    //------------------------------------------------------------
    /**
     * Indicate that an entity was shut down correctly.
     *
     * @param addr Address of the entity
     */
    public void entityShutdown(Address addr);
    
} // MBusListener
