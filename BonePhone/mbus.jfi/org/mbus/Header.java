package org.mbus;

import java.util.*;

/**
 * Header.java
 *
 * @author Stefan Prelle  <prelle@tzi.de>
 * @version $Id: Header.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public class Header {
    
    private String  digest;
    private int     seqNum;
    private long    time;
    private boolean reliable;
    private Address source;
    private Address destination;
    private int[]   ackList;

    //---------------------------------------------------------------
    /**
     * Create a header from a two strings
     *
     * @param dig  First header-line containing base64-digest
     * @param data Rest of header
     */
    public Header(String dig, String data) throws MBusParsingException {
	digest = dig;
	
	StringTokenizer str = new StringTokenizer(data, " \n");
	String protID = str.nextToken();
	String seq    = str.nextToken();
	String tstamp = str.nextToken();
	String type   = str.nextToken();
	String srcad  = str.nextToken(")").trim();
	String dstad  = str.nextToken(")").trim();
	String acks   = str.nextToken().trim();

	if (!protID.equals("mbus/1.0")) 
	    System.err.println("Warning: Unknown protocol-id: "+protID);

	try {
	    seqNum = Integer.parseInt(seq);
	} catch (NumberFormatException nfe) {
	    throw new MBusParsingException("Malformed sequence-number: "+seq);
	}

	try {
	    time = Long.parseLong(tstamp);
	} catch (NumberFormatException nfe) {
	    throw new MBusParsingException("Malformed timestamp: "+tstamp);
	}

	if (type.equalsIgnoreCase("r"))
	    reliable = true;
	else if (type.equalsIgnoreCase("u"))
	    reliable = false;
	else
	    throw new MBusParsingException("Illegal messagetype: "+type);

	source      = new Address(srcad);
	destination = new Address(dstad);

	// Acks
	acks = acks.substring(1, acks.length());
	Vector s = new Vector();
	StringTokenizer ackTok = new StringTokenizer(acks, " ");
	while (ackTok.hasMoreTokens()) {
	    String elem = ackTok.nextToken();
	    try {
		s.addElement( new Integer(elem) );
	    } catch (NumberFormatException nfe) {
		throw new MBusParsingException("Error parsing ack-list: "+elem+" is not a number.");
	    }
	}
	ackList = new int[s.size()];
	for (int i=0; i<s.size(); i++) 
	    ackList[i] = ((Integer)s.elementAt(i)).intValue();
    }
    

    //---------------------------------------------------------------
    /**
     * @param seq Sequencenumber
     */
    public Header(Address src, Address dest, int seq, boolean reliable) {
	seqNum      = seq;
	time        = System.currentTimeMillis()/1000;
	this.reliable = reliable;
	source      = src;
	destination = dest;
	ackList     = new int[0];
    }

    //---------------------------------------------------------------
    /**
     * @param seq Sequencenumber
     */
    public Header(Address src, Address dest, int seq, boolean reliable, int[] acks) {
	seqNum      = seq;
	time        = System.currentTimeMillis()/1000;
	this.reliable = reliable;
	source      = src;
	destination = dest;
	ackList     = acks;
    }

    //---------------------------------------------------------------
    public int getSequenceNumber() {
	return seqNum;
    }

    //---------------------------------------------------------------
    public Address getSourceAddress() {
	return source;
    }

    //---------------------------------------------------------------
    public Address getDestinationAddress() {
	return destination;
    }

    //---------------------------------------------------------------
    public int[] getAckList() {
	return ackList;
    }

    //---------------------------------------------------------------
    public long getTimeStamp() {
	return time;
    }
	
    //---------------------------------------------------------------
    public String getDigest() {
	return digest;
    }
	
    //---------------------------------------------------------------
    public void setDigest(String dig) {
	digest = dig;
    }
    
    //---------------------------------------------------------------
    public boolean isReliable() {
	return reliable;
    }
    
    //---------------------------------------------------------------
    public String toString() {
	StringBuffer ret = new StringBuffer("mbus/1.0 ");
	ret.append(seqNum+" ");
	ret.append(time+" ");
	ret.append(reliable?"R ":"U ");
	ret.append(source+" ");
	ret.append(destination+" ");
	ret.append('(');
	for (int i=0; i<ackList.length; i++) 
	    ret.append(ackList[i]+" ");
	ret.append(')');
	return ret.toString();
    }
    
} // Header
