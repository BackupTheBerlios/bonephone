package org.mbus;

/**
 * Occurs if a dependent Mbus-entity reported an error.
 *
 *
 * <br>Created: Wed Jul 14 14:32:12 1999
 *
 * @author Stefan Prelle
 * @version $id: MBusException.java 1.0 Wed Jul 14 14:32:12 1999 prelle Exp $
 */

public class MBusException extends Exception {
    
    //---------------------------------------------------------------
    public MBusException(String mess) {
	super(mess);
    }

} // MBusException
