package org.mbus;

import java.net.*;

/**
 * This class defines a logging-class for incoming messages
 *
 * @author Stefan Prelle
 * @version $id: IncomingLogger.java 1.0 Wed Nov  3 13:49:13 1999 prelle Exp $
 */

public interface IncomingLogger  {
    
    //---------------------------------------------------------------
    public void helloReceived(Message mess, InetAddress ip, int port);
    public void byeReceived(Message mess, InetAddress ip, int port);
    public void messageReceived(Message mess, InetAddress ip, int port);
    
    
} // IncomingLogger
