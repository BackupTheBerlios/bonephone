/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov)                                    *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package gov.nist.sip.stack;


import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

/**
 * This class stores all the message logs accessible via RMI
 */

public class MessageLogTableImpl extends UnicastRemoteObject 
    implements MessageLogTable, Cleanable {

    private Hashtable table;
    private final int defaultLifeTime = 3600; /* 1 hour */
    private int lifeTime;
    private Hashtable timeOut;

    /**
     * Constructor
     */
 
    public MessageLogTableImpl() throws RemoteException {
        super();
    }

    public MessageLogTableImpl(int port) throws RemoteException {
        super(port);
    }

    public void init(String stackName, int port)  throws RemoteException {
	init(stackName, port, defaultLifeTime);
    }

    public void init(String stackName, int port, int lifeTime) 
	throws RemoteException {
	table = new Hashtable();
	timeOut = new Hashtable();
	if (lifeTime == 0) {
	    this.lifeTime = defaultLifeTime;
	} else {
	    this.lifeTime = lifeTime;
	}
       /*
	try { 
	  UnicastRemoteObject.exportObject(this);
	} catch (Exception ex) {}
	*/
         if (System.getSecurityManager() == null) {
	    System.setSecurityManager(new RMISecurityManager());
	}
	String name = "//" + stackName + ":" + port + "/" + 
	    getClass().getName();
	try {
	    System.out.println("rebind : "  + name);
	    Naming.rebind(name, this);
	    System.out.println(getClass() + " bound");
	} catch (Exception e) {
	    System.err.println(getClass() + " exception: " + 
			       e.getMessage());
	    e.printStackTrace();
	}
    }

    public void add(String callId, MessageLogList logList) {
	table.put(callId, logList);
	Calendar date = GregorianCalendar.getInstance();
	date.add(Calendar.SECOND, lifeTime);
	timeOut.put(callId, date);
    }

    public MessageLogList get(String callId) {
	if (table.containsKey(callId)) {
	    return (MessageLogList) table.get(callId);
	} else {
	    return null;
	}
    }

    public String keys() throws RemoteException {
	Enumeration e = table.keys();
	String result = "";
	while (e.hasMoreElements()) {
	    result += e.nextElement() + " ";
	}
	return result;
    }

    public String flush(String key) throws RemoteException {
        return (String) ((MessageLogList) table.get(key)).flush();
    }

    public synchronized void clean() {
	Enumeration keys = timeOut.keys();
	while (keys.hasMoreElements()) {
	    String callId = (String) keys.nextElement();
	    Calendar timeOutDate = (Calendar) timeOut.get(callId);
	    Calendar date = GregorianCalendar.getInstance();
	    if (date.after(timeOutDate)) {
		ServerDebug.println("Cleaning " + callId);
		timeOut.remove(callId);
		table.remove(callId);
	    }
	}
    }

}
