package org.mbus;

import java.net.*;

/**
 * This class defines a logging-class for outgoing messages
 *
 * @author Stefan Prelle
 * @version $id: OutgoinLogger.java 1.0 Wed Nov  3 13:49:13 1999 prelle Exp $
 */

public interface OutgoingLogger  {
    
    //---------------------------------------------------------------
    public void helloSent(Message mess, InetAddress ip, int port);
    public void byeSent(Message mess, InetAddress ip, int port);
    public void messageSent(Message mess, InetAddress ip, int port);
    
    
} // OutgoingLogger
