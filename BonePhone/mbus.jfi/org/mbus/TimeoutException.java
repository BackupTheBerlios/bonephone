package org.mbus;

/**
 * TimeoutException.java
 *
 *
 * <br>Created: Tue Jul 13 14:18:49 1999
 *
 * @author Stefan Prelle
 * @version $id: TimeoutException.java 1.0 Tue Jul 13 14:18:49 1999 prelle Exp $
 */

public class TimeoutException extends Exception {
    
    //---------------------------------------------------------------
    public TimeoutException() {
	super();
    }
    
    //---------------------------------------------------------------
    public TimeoutException(String mess) {
	super(mess);
    }
    
} // TimeoutException
