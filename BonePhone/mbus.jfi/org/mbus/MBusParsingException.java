package org.mbus;

/**
 * MBusParsingException.java
 *
 * @author Stefan Prelle
 * @version $id: MBusParsingException.java 1.0 Wed Sep 15 17:32:25 1999 prelle Exp $
 */

public class MBusParsingException extends RuntimeException {
    
    private int pos = -1;
    private String data = null;

    //---------------------------------------------------------------
    public MBusParsingException(String mess) {
	super(mess);
    }
    
    //---------------------------------------------------------------
    public MBusParsingException(String mess, String data, int pos) {
	super(mess);
	this.data = data;
	this.pos  = pos;
    }

    //---------------------------------------------------------------
    public String toString() {
	if (pos==-1)
	    return super.toString();
	
	StringBuffer buf = new StringBuffer(super.toString()+"\n");
	buf.append(data+"\n");
	for (int i=0; i<pos; i++)
	    buf.append(' ');
	buf.append('^');
	return buf.toString();
    }
    
} // MBusParsingException
