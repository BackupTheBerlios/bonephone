package org.mbus;

import java.util.*;
import java.security.*;
import javax.crypto.*;  // Needed for JCE

/**
 * Authenticator.java
 *
 *
 * <br>Created: Thu Sep 23 14:30:05 1999
 *
 * @author Stefan Prelle
 * @version $id: Authenticator.java 1.0 Thu Sep 23 14:30:05 1999 prelle Exp $
 */

public class Authenticator  {

    private Mac     mac;
    private boolean checkDigest;
    private Key     key;
    
    //---------------------------------------------------------------
    public Authenticator(MBusConfiguration config) throws MBusException {
		checkDigest = true; // config.getCheckDigest();
//		String algo = "HMAC-MD5-96";
		String algo = "HmacMD5";
//		String algo = config.getHashKey().getAlgorithm();
		if (!checkDigest)
	    	return;

		try {
		    key = config.getHashKey();
		    mac = Mac.getInstance(algo);
	    	mac.init(key);
			// System.out.println("mbus: MAC has length "+(mac.getMacLength()));
		} catch (InvalidKeyException ike) {
		    //config.setCheckDigest(false);
	    	ike.printStackTrace();
		} catch (NoSuchAlgorithmException nsa) {
		    nsa.printStackTrace();
	    	//config.setCheckDigest(false);
		    System.err.println("\nCould not find algorithm for Message Digest.");
		    System.err.println("Maybe you have an unsupported algorithm ("+algo+") in your config-file,");
	    	System.err.println("or there is no Security-Provider available.");
		    System.err.println("Continuing without digest-checking - set CHECK_DIGEST=no in your config-file.\n");
		} catch (Throwable e) {
		    //config.setCheckDigest(false);
		    e.printStackTrace();
	    	System.err.println("\nCouldn't instantiate MACs.");
		}
    }

    //---------------------------------------------------------------
    /**
     * Create a message-digest for the data that is not Base64-encoded
     *
     * @param data String to create a digest for
     * @return Digest
     */
    public String getDigest(String data) {
	// If there is no Message Digest-support return something silly :) 
	if (mac==null) {
	    byte[] enc = Base64Codec.decode(("NoDigestAvailabl").getBytes());
	    return new String(enc);
	}
	// .. otherwise
	mac.reset();
	mac.update(data.getBytes());
	byte[] dig = mac.doFinal();
	return new String(dig);
    }


	public static String dumpBytes(byte[] ba) {
		int i;
		String r="";
		for (i=0; i<ba.length; i++) {
			int x=(int)(ba[i]);
			if (x<0) x=x+256;
			if (x<16) r=r+"0";
			r=r+Integer.toString(x,16);
		}
		return r;
	}


    //---------------------------------------------------------------
    /**
     * Create a message-digest for the data that is Base64-encoded
     *
     * @param data String to create a digest for
     * @return Digest
     */
    public String getBase64Digest(String data) {
	// If there is no Message Digest-support return something silly :) 
	if (mac==null)
	    return "NoDigestAvailabl";
	// .. otherwise
	mac.reset();
	mac.update(data.getBytes());
	byte[] dig = mac.doFinal();
	String b64 = new String(Base64Codec.encode(dig));
	// System.out.println("MBUS: computed: "+(b64.substring(0,16))+" : "+dumpBytes(dig));
	return b64.substring(0,16);
    }

    //---------------------------------------------------------------
    public boolean checkDigest(String digest, String toCheck) {
	if (!checkDigest)
	    return true;
	String computed  = getBase64Digest(toCheck);
	return digest.equals(computed);
    }
    
} // Authenticator
