package org.mbus;

import java.net.InetAddress;

/**
 * ReceiverListener.java
 *
 * @author Stefan Prelle
 * @version $Id: ReceiverListener.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

interface ReceiverListener extends MessageSender {
    
    //------------------------------------------------------------
    void logIncoming(Message mess, InetAddress addr, int port);
    
    //------------------------------------------------------------
    void logGeneral(String text);
    
    //------------------------------------------------------------
    void incomingMessage(Message mess, Address target);

    //------------------------------------------------------------
    void removeACKs(int[] ackList, Message mess, boolean successful);
    
    //------------------------------------------------------------
    void delAddress(Address ad);
    
    //------------------------------------------------------------
    void checkAddress(Address src, InetAddress ip, int port);
    
	
} // ReceiverListener
