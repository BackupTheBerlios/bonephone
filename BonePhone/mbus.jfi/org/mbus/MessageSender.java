package org.mbus;

/**
 * MessageSender.java
 *
 * @author Stefan Prelle
 * @version $Id: MessageSender.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

interface MessageSender  {
    
    //------------------------------------------------------------
    int send(Command com, Address source, Address target);
 
   //------------------------------------------------------------
    int send(Command com, Address source, Address target, boolean reliable);
    
    //------------------------------------------------------------
    int send(Message mess);
    
    //------------------------------------------------------------
    int broadcast(Address source, Command com);

    //------------------------------------------------------------
    void sendACKs(Address source, Address target, int[] ackList);
    
} // MessageSender
