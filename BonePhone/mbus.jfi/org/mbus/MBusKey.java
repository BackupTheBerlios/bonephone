package org.mbus;

import java.io.*;
import java.util.*;
import java.text.*;
import java.security.*;
import javax.crypto.*;

/**
 * MBusKey.java
 *
 * @author Stefan Prelle  <prelle@tzi.de>
 * @version $Id: MBusKey.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public class MBusKey implements Key, SecretKey {
    
    public final static String ALGO_NONE = "NOENCR";
    public final static String ALGO_DES  = "DES";
    public final static String ALGO_3DES = "3DES";
    public final static String ALGO_IDEA = "IDEA";
    public final static String ALGO_MD5  = "HMAC-MD5-96";
    public final static String ALGO_SHA1 = "HMAC-SHA1-96";

    private String algo;
//      private Date   expire;
    private byte[] key;
//      private String textExpireDate;

    //---------------------------------------------------------------
    /**
     * Create a new key-entry.
     *
     * @param algo Algorithm-ID
     * @param expire Expire-date in seconds since epoch
     * @param key    Base64-encoded key.
     * @exception java.security.InvalidKeyException
     */
    public MBusKey(String algo, String key) throws InvalidKeyException {
//    	System.out.println("MBusKey.<init>: "+algo+" ["+key+"]");
	// Check the algorithm
	if (algo.equalsIgnoreCase(ALGO_NONE))
	    this.algo = "No encryption";
	else if (algo.equalsIgnoreCase(ALGO_DES))
	    this.algo = ALGO_DES;
	else if (algo.equalsIgnoreCase(ALGO_3DES))
	    this.algo = ALGO_3DES;
	else if (algo.equalsIgnoreCase(ALGO_IDEA))
	    this.algo = ALGO_IDEA;
	else if (algo.equalsIgnoreCase(ALGO_MD5))
	    this.algo = "HmacMD5";
	else if (algo.equalsIgnoreCase(ALGO_SHA1))
	    this.algo = "HMAC-SHA";
//  	else 
//  	    throw new InvalidKeyException("Unsupported algorithm.");

//  	// Expire-Date
//  	try {
//  	    long seconds = Long.parseLong(expire);
//  	    this.expire  = new Date(seconds*(long)1000);
//  	} catch (NumberFormatException nfe) {
//  	    throw new InvalidKeyException("Malformed expire-date: "+expire);
//  	}
//      textExpireDate = DateFormat.getDateTimeInstance(DateFormat.SHORT,
//  			 DateFormat.MEDIUM).format(this.expire);

//  	Date currentDate = new Date();
//  	if (this.expire.before(currentDate))
//  	    throw new InvalidKeyException("Key already expired at "+textExpireDate);
	
	// Get Key-data
	if (key==null) {
	    this.key = new byte[0];
	    return;
	}
	try {    
	    byte[] tmp = Base64Codec.decode(key.getBytes());
//  	    System.out.println("MBusKey.<init2>: "+HexDumpMaker.makeHexDump(tmp));
	    this.key = tmp;
		// System.out.println("MBUS: decoded key: "+Authenticator.dumpBytes(tmp));
//  	    if (algo.equals("DES")) 
//  		this.key = new byte[8];
//  	    else if (algo.equals("IDEA"))
//  		this.key = new byte[16];
//  	    else {
//  		if (tmp!=null) {
//  		    this.key = new byte[tmp.length];
//  		    System.arraycopy(tmp,0,this.key,0,this.key.length);
//  		} else
//  		    this.key = new byte[0];
//  	    }
//  	    try {
//  		FileOutputStream b64key = new FileOutputStream("b64.key");
//  		b64key.write(key.getBytes());
//  		b64key.close();
//  		FileOutputStream bytekey = new FileOutputStream("byte.key");
//  		bytekey.write(this.key);
//  		bytekey.close();
//  	    } catch (IOException i) {
//  		i.printStackTrace();
//  	    }
	} catch (ArrayIndexOutOfBoundsException aie) {
	    throw new InvalidKeyException("Key too short: "+key+" ("+key.getBytes().length+")");
	}
    }

    //---------------------------------------------------------------
    public String getAlgorithm() {
	return algo;
    }

//      //---------------------------------------------------------------
//      public Date getExpireDate() {
//  	return expire;
//      }

//      //---------------------------------------------------------------
//      public String getFullExpireDate() {
//  	return textExpireDate;
//      }

    //---------------------------------------------------------------
    public byte[] getEncoded() {
	return (byte[])key.clone();
    }

    //---------------------------------------------------------------
    public String getFormat() {
	return "RAW";
    }

    //---------------------------------------------------------------
    public String toString() {
	return "Key using "+algo;
    }

    //---------------------------------------------------------------
    public String dump() {
	return HexDumpMaker.makeHexDump(key);
    }
    
} // MBusKey

