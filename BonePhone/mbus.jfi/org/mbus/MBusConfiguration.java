package org.mbus;

import java.util.*;
import java.io.*;
import java.net.*;
import java.security.*;

/**
 * This class encapsulates the configuration of the MBus. It reads
 * the <tt>.mbus</tt>-File from the user's home-directory. In this 
 * file, the following keys in the [MBUS]-section are supported:
 * <ul>
 * <li><tt>HASHKEY</tt> 
 * <li><tt>ENCRYPTIONKEY</tt>  
 * <li><tt>PRESENCE</tt> (unused)
 * <li><tt>SCOPE</tt> - LINKLOCAL or HOSTLOCAL (default: HOSTLOCAL)
 * <li><tt>PORT</tt> - (default: 47000)
 * <li><tt>ADDRESS</tt> - (default: 224.255.222.239)
 * <li><tt>CHECK_DIGEST</tt> - enable digestchecking (default: true)
 * <li><tt>SEND_UNICAST</tt> - enable unicast optimization  (default: true)
 * <li><tt>TIMER_R</tt> - Milliseconds between message retransmissions (default: 100)
 * <li><tt>RETRIES</tt> - number of retries to retransmit a message (default: 3)
 * <li><tt>cryptoProvider</tt> - Providerclass of the JCE
 * </ul>
 * These configurations may be overridden by system properties which
 * have the same key with a leading "mbus.". E.g.: <tt>mbus.PORT</tt> 
 * instead of <tt>PORT</tt>.
 *
 * @author Stefan Prelle  <prelle@tzi.de>
 * @version $Id: MBusConfiguration.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public class MBusConfiguration {

    private final static int MD    = 0;
    private final static int MAC   = 1;
    private final static int CIPHER= 2;
    
    public final static int HOSTLOCAL = 0;
    public final static int LINKLOCAL = 1;

    private String  mcGroup="224.255.222.239";
    private InetAddress group;
    private int     port;
    private byte    ttl;
    private boolean sendUnicast;
    private String  uci;   // Universal Communication Identifier (Presence)
    private int     timer_r; // Retransmission-Timer
    private int     timer_c; // Maximum time to ACK
    private int     retries; // Number of retries for reliable messages
	private MBusKey hashKey;

    //---------------------------------------------------------------
    public MBusConfiguration() throws FileNotFoundException, IOException {
		sendUnicast = true;
		ttl         = 0;
		port        = 47000;
		mcGroup     = "224.255.222.239";
		timer_r     = 100;
		retries     = 3;

		parseConfigFile();
		int cnt = parseProperties(System.getProperties());
		if (port==-1)
		    throw new MBusParsingException("Missing Portnumber in MBus-Configuration.");
	
		try {
		    group = InetAddress.getByName(mcGroup);
		} catch (UnknownHostException uhe) {
		    throw new MBusParsingException("Unknown Multicast-Group: "+mcGroup);
		}
    }


    //---------------------------------------------------------------
    private void parseConfigFile() throws FileNotFoundException, IOException {
		// Read config-file
		String userhome = System.getProperty("user.home");
		File confFile = new File(userhome, ".mbus");
		if (!confFile.exists()) {
	    	System.out.println("Could not find a config-file at "+confFile.getAbsolutePath());
		    return;
		}
		BufferedReader in = new BufferedReader(new FileReader(confFile));
		// Find first line with parameters
		int lineNo = 1;
		while (in.ready()) {
	    	String line = in.readLine();
		    if (line.equalsIgnoreCase("[MBUS]")) break;
		}
	
		String line = in.readLine();
		while (line!=null && (line.indexOf('[')!=0 || line.indexOf(']')<0)) {
	    	if (!line.startsWith("#") && line.indexOf('=')>0) {
				int pos = line.indexOf('=');
				String key = line.substring(0,pos);
				String val = line.substring(pos+1,line.length());
				evaluate(key.trim(), val.trim());
		    } // if not #
	    	line = in.readLine();
		} // while
	
    }

    //---------------------------------------------------------------
    private int parseProperties(Properties pro) {
	Enumeration e = pro.propertyNames();
	int count = 0;
	while (e.hasMoreElements()) {
	    String prevKey = (String)e.nextElement();
	    if (prevKey.startsWith("mbus.")) {
		count++;
		String val = pro.getProperty(prevKey);
		String key = prevKey.substring(4,prevKey.length());
		evaluate(key, val);
	    }
	}
	return count;
    }


	//---------------------------------------------------------------
	private MBusKey parseAsKey(String val) throws InvalidKeyException {
		int bgn = val.indexOf('(')+1;
		int end = val.indexOf(')');
		String newVal = val.substring(bgn,end);
		String key = null;
 
		try {
			StringTokenizer str = new StringTokenizer(newVal, ",");
			String algo = str.nextToken().trim();
//			String exp  = str.nextToken().trim();
			if (str.hasMoreTokens())
				key  = str.nextToken().trim();
			else {
				key = null;
				// The key may only be missing if ALGO was NOENCR
				if (!algo.equals("NOENCR"))
					throw new MBusParsingException("Key-Value for alogrithm "+algo+" is missing.");
			}
			MBusKey ret = new MBusKey(algo, key);
			return ret;
		} catch (NoSuchElementException nse) {
			throw new MBusParsingException("MBus-Config: Malformed key-entry: "+val);
		}
	}
	    
    //---------------------------------------------------------------
    /**
     * Evaluate the config-entry
     */
    private void evaluate(String key, String val) {
		if (key.equalsIgnoreCase("HASHKEY")) { 
			try { hashKey = parseAsKey(val); return; }
			catch (InvalidKeyException ike) {
				System.err.println(ike.getMessage());
			}
			return;
		}
		if (key.equalsIgnoreCase("PRESENCE")) { uci = val; return; }
		if (key.equalsIgnoreCase("SCOPE")) {
			if (val.equalsIgnoreCase("HOSTLOCAL")) { ttl = HOSTLOCAL; return; }
			if (val.equalsIgnoreCase("LINKLOCAL")) { ttl = LINKLOCAL; return; }
			System.err.println("Unknown scope: "+val+" ... using HOSTLOCAL");
			ttl = HOSTLOCAL; return;
		}
		if (key.equalsIgnoreCase("PORT")) {
			try { port = Integer.parseInt(val); } 
			catch (NumberFormatException nfe) {
				System.err.println("Invalid portnumber: "+val);
			}
			return;
    	}
		if (key.equalsIgnoreCase("ADDRESS")) { mcGroup = val; return; }
		if (key.equalsIgnoreCase("SEND_UNICAST")) {
			sendUnicast = true;
			if (val.equalsIgnoreCase("no") || val.equals("0") || val.equals("false")) {
				sendUnicast = false;
			} 
			return;
		}
		if (key.equalsIgnoreCase("TIMER_R")) {
			try { timer_r = Integer.parseInt(val); }
			catch (NumberFormatException nfe) {
				System.err.println("Invalid retransmission-timer: "+val);
			}
	    	return;
		}
		if (key.equalsIgnoreCase("RETRIES")) {
			try { retries = Integer.parseInt(val); }
			catch (NumberFormatException nfe) {
				System.err.println("Invalid amount of retries: "+val);
			}
	    	return;
		}
		System.err.println("Unsupported Key: "+key);
    } 
    

    //---------------------------------------------------------------
    public String toString() {
	StringBuffer ret = new StringBuffer();
	ret.append("Address    : "+mcGroup+"\n");
	ret.append("Port       : "+port+"\n");
	ret.append("TTL        : "+ttl+"\n");
	ret.append("Presence   : "+uci+"\n");
	ret.append("sendUnicast: "+sendUnicast+"\n");
	ret.append("HashKey    : "+hashKey+"\n");
	ret.append("Timer_R    : "+timer_r+"\n");
	ret.append("Retries    : "+retries+"\n");
	return ret.toString();
    }

	//---------------------------------------------------------------
	public MBusKey getHashKey() {
		return hashKey;
	}

    //---------------------------------------------------------------
    public boolean getSendUnicast() {
	return sendUnicast;
    }

    //---------------------------------------------------------------
    public byte getScope() {
	return ttl;
    }

    //---------------------------------------------------------------
    public int getPort() {
	return port;
    }

    //---------------------------------------------------------------
    public InetAddress getMulticastGroup() {
	return group;
    }

    //---------------------------------------------------------------
    public void changeScope(byte scope) {
	ttl = scope;
    }

    //---------------------------------------------------------------
    public int getTimerR() {
	return timer_r;
    }

    //---------------------------------------------------------------
    public int getTimerC() {
	return timer_c;
    }

    //---------------------------------------------------------------
    public int getRetries() {
	return retries;
    }
}

// MBusConfiguration
