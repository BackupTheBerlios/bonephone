package org.mbus;

import java.security.*;
// import javax.crypto.*;
//  import cryptix.jce12.provider.cipher.RawSecretKey;
// import javax.crypto.spec.IvParameterSpec;

/**
 * Crypter.java
 *
 *
 * <br>Created: Thu Sep 23 14:08:18 1999
 *
 * @author Stefan Prelle
 * @version $id: Crypter.java 1.0 Thu Sep 23 14:08:18 1999 prelle Exp $
 */

public class Crypter  {
    
//      private RawSecretKey key;
    private Key          key2;
    private int          blockLength;
    private MBusConfiguration config;

    //---------------------------------------------------------------
    public Crypter(MBusConfiguration config) throws MBusException {
		this.config = config;
    }
    
    //---------------------------------------------------------------
    public byte[] encrypt(String data) {
		int len2 = data.getBytes().length%8;
		byte[] foo;
		foo = new byte[data.getBytes().length+(8-len2)];
		System.arraycopy(data.getBytes(),0,foo,0,data.getBytes().length);
	    return foo;
    }
    
    //---------------------------------------------------------------
    public byte[] decrypt(byte[] data) {
	    // Remove padding
	    int i = data.length-1;
	    while (data[i]==0) i--;
	    byte [] nData = new byte[i+1];
	    System.arraycopy(data,0,nData,0,i+1);
	    return nData;
    }
    
    
} // Crypter
