package org.mbus;

import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Message.java
 *
 * @author Stefan Prelle  <prelle@tzi.de>
 * @version $Id: Message.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public class Message {
    
    private Header header;
    private Vector commands;
    
    //---------------------------------------------------------------
    /**
     * Parses a message from a string
     */
    public Message(String data) {
	StringTokenizer str = new StringTokenizer(data, "\n");
	String digest       = str.nextToken();
	header              = new Header(digest, str.nextToken());
	
	commands = new Vector();
	while (str.hasMoreTokens()) {
	    String tmp = str.nextToken().trim();
	    try {
		if (tmp.length()>0)
		    commands.addElement( new Command(tmp) );
		else
		    break;
	    } catch (Exception e) {
		System.err.println("Unparsable MBus-Command: "+tmp);
		e.printStackTrace();
	    }
	}
    }

    //---------------------------------------------------------------
    public Message(int seq, Address src, Address dest, boolean rel) {
	MList params = new MList();
	header = new Header(src, dest, seq, rel);
	commands = new Vector();    }

    //---------------------------------------------------------------
    public Message(Header header, Vector commands) {
	this.header = header;
	this.commands = commands;
    }
	
    //---------------------------------------------------------------
    public String getDigest() {
	return header.getDigest();
    }
    
    //---------------------------------------------------------------
    public boolean isReliable() {
	return header.isReliable();
    }
    
    //---------------------------------------------------------------
    public String toString() {
	return getWithoutDigest();
    }

    //---------------------------------------------------------------
    /**
     * Return the message-string without the digest
     */
    public String getWithoutDigest() {
	StringBuffer toHash = new StringBuffer(100);
	toHash.append(header.toString());
	toHash.append('\n');
	Enumeration e = commands.elements();
	while (e.hasMoreElements()) {
	    Command c = (Command)e.nextElement();
	    toHash.append(c+"\n");
	}
	return toHash.toString();
    }
    
    //---------------------------------------------------------------
    public Header getHeader() {
	return header;
    }

    //------------------------------------------------------------
    public Enumeration getCommands() {
	return commands.elements();
    }

    //------------------------------------------------------------
    /**
     * Returns the number of commands in this message
     */
    public int getSize() {
	return commands.size();
    }

    //------------------------------------------------------------
    public String getFirstCommandName() {
	if (commands.size()>0) 
	    return ((Command)commands.firstElement()).getCommand();
	else
	    return "";
    }

    //------------------------------------------------------------
    public boolean isHello() {
	Enumeration e = commands.elements();
	while (e.hasMoreElements()) {
	    Command co = (Command)e.nextElement();
	    if (co.getCommand().equals("mbus.hello"))
		return true;
	}
	return false;
    }

    //------------------------------------------------------------
    public boolean isBye() {
	Enumeration e = commands.elements();
	while (e.hasMoreElements()) {
	    Command co = (Command)e.nextElement();
	    if (co.getCommand().equals("mbus.bye"))
		return true;
	}
	return false;
    }

    //------------------------------------------------------------
    /**
     * Returns the number of commands in the message
     *
     * @return Number of commands
     */
    public int size() {
	return commands.size();
    }
    
} // Message
