package org.mbus;

/**
 * VirtualMBusListener.java
 *
 * @author Stefan Prelle
 * @version $Id: VirtualMBusListener.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public interface VirtualMBusListener extends MBusListener {
    
    //------------------------------------------------------------
    public Address getAddress();
    
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
    
} // VirtualMBusListener
