package org.mbus;

import java.text.*;
import java.util.*;

/**
 * LogEntry.java
 *
 * @author Stefan Prelle
 * @version $id: LogEntry.java 1.0 Tue Nov  2 20:52:02 1999 prelle Exp $
 */

public class LogEntry extends Object implements Comparable {
    
    public final static int SENT = 0;
    public final static int RECEIVED = 1;
    public final static int INFO = 2;

    Message mess;
    long timestamp;
    int  type;
    String text;

    //---------------------------------------------------------------
    public LogEntry(int type, long time, Message m) {
	timestamp = time;
	this.type = type;
	mess      = m;
	text      = null;
    }

    //---------------------------------------------------------------
    public LogEntry(int type, Message m) {
	timestamp = m.getHeader().getTimeStamp();
	this.type = type;
	mess      = m;
	text      = null;
    }

    //---------------------------------------------------------------
    public LogEntry(String text) {
	timestamp = System.currentTimeMillis()/1000;
	this.type = INFO;
	mess      = null;
	this.text = text;
    }

    //---------------------------------------------------------------
    public String toString() {
	StringBuffer buf = new StringBuffer(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date(timestamp*1000)));
	buf.append( (type==SENT)?" S ":" R ");
	if (mess!=null) {
	    buf.append(mess.getHeader().getSourceAddress().getApp()+" \t");
	    buf.append(mess.getFirstCommandName()+" \t");
	    buf.append(mess.getHeader().getDestinationAddress().getApp()+" \t");
	} else {
	    buf.append(text);
	}
	return buf.toString();
    }

    //---------------------------------------------------------------
    public int compareTo(Object o) {
	LogEntry le = (LogEntry)o;
	if (timestamp < le.timestamp) return -1;
	if (timestamp > le.timestamp) return 1;
	return 0;
    }
    
} // LogEntry
