package org.mbus;

/**
 * RetransmitterListener.java
 *
 * @author Stefan Prelle
 * @version $id RetransmitterListener.java 1999/09/02 10:46:18 prelle Exp $
 */

interface RetransmitterListener extends MessageSender {
    
    //------------------------------------------------------------
    void deliveryFailed(int seqNum, Message mess);
    
    
} // RetransmitterListener
