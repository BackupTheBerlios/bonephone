/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: DigestAuthenticationcMethod.java
 * created 26-Sep-00 2:17:37 PM by mranga
 */

package gov.nist.sip.stack.security;
import  gov.nist.sip.stack.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Hashtable;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.msgparser.*;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.util.StringTokenizer;

/**
*  Implements the HTTP digest authentication method.
*/

public class DigestAuthenticationMethod 
implements AuthenticationMethod, SIPKeywords, SIPErrorCodes
{
	
	public static final String DEFAULT_SCHEME = "Digest";
	public static final String DEFAULT_DOMAIN = "nist.gov";
	public static final String DEFAULT_ALGORITHM = "MD5";
	public static final String DEFAULT_REALM = "";
	public static final String NULL_PASSWORD = "";
	private Hashtable passwordTable;
	private MessageDigest messageDigest;
	// Contains passwords and other acct info
	
	/**
	*  Default constructor.
	*/
	public DigestAuthenticationMethod() {
	   try {
		messageDigest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
	   } catch ( NoSuchAlgorithmException ex ) {
	   	ServerLog.logMessage("Algorithm not found " + ex);
		ServerInternalError.handleException(ex);
	   }
	   passwordTable = new Hashtable();
	}
	

	/**
	* Initialize -- load password files etc.
	* Password file format is name:authentication domain:password
	*/
	public void initialize (String pwFileName )
       	throws Exception {
		ServerLog.traceMsg(ServerLog.TRACE_INITIALIZATION,
		"DigestAuthentication ... Reading password Entries from " 
			+ pwFileName);
		BufferedReader pwfile = new 
			BufferedReader(new FileReader(pwFileName));
		int line = 0;
		while( true) {
			String input = null;
			try {
				input = pwfile.readLine();
				if (input == null) break;
				if (input.charAt(0) == '#') continue;
				if (input.trim().compareTo("") == 0) continue;
				line ++;
			} catch (IOException ex) {
				throw new Exception
				("Could not find or open Password file");
			}
			try {
				MyStringTokenizer  st = 
				   new MyStringTokenizer(input,":");
				String name = st.nextToken();
				name.trim();
				String realm = st.nextToken();
				if (realm == null) realm = DEFAULT_REALM;
				String password =  st.nextToken();
				if (password == null) password = NULL_PASSWORD;
				ServerLog.traceMsg
				(ServerLog.TRACE_INITIALIZATION,
				"DigestAuthenticationMethod.Initialize:" + 
					name+":"+realm + "<" + password);
				passwordTable.put(name+":"+realm, password);
			} catch ( NoSuchElementException ex) {
				throw new Exception
					("Bad Password File entry in " 
					+ pwFileName + " at line " + line);
			}
		}
	}
	
	
	    	
	/**
	*  Get the authentication scheme
	*/
	public String getScheme() {
		return DEFAULT_SCHEME;
	}
	/**
	*  get the authentication realm
	*/
	public String getRealm(String resource) {
		return  DEFAULT_REALM;
	}
	/**
	*  get the authentication domain.
	*/
	public String getDomain() {
		return DEFAULT_DOMAIN;
	}
	/**
	*  Get the authentication Algorithm
	*/
	public String getAlgorithm() {
		return DEFAULT_ALGORITHM;
	}
	/**
	*  Generate the challenge string.
	*/
	public String generateNonce() {
		// Get the time of day and run MD5 over it.
		Date date = new Date();   
		long time = date.getTime();
		Random rand = new Random();
		long pad = rand.nextLong();
		String nonceString = (new Long(time)).toString() + 
				(new Long(pad)).toString();
		byte mdbytes[] = messageDigest.digest(nonceString.getBytes());
		// Convert the mdbytes array into a hex string.
		return ServerUtils.toHexString(mdbytes);
	}
	
	/**
	*  Check the response and answer true if authentication succeeds.
	*  We are making simplifying assumptions here and assuming that 
	*  the password is available  to us for computation of the MD5 hash. 
	*  We also dont cache authentications so that the
	*  user has to authenticate on each registration.
	* @param user is the username 
	* @param authHeader is the Authroization header from the SIP request.
	* @param requestLine is the SIP Request line from the SIP request.
	* @exception SIPException is thrown when authentication fails 
	*  	or message is bad
	*/
	public boolean doAuthenticate( String user,
		    Authorization authHeader,
		    RequestLine  requestLine ) 
		    throws SIPAuthenticationException 
	{
		URI requestURI = requestLine.getUri();
		String uriString = requestURI.getInputText();
		String realm =  authHeader.getRealm();
		String username = authHeader.getValue(USERNAME);
		if (username == null) {
		  username = user;
		}
		if (realm == null) realm = DEFAULT_REALM; 
		ServerLog.traceMsg(ServerLog.TRACE_DEBUG, 
		    "Username = " + username + " realm = " + realm);
		//  This should not be stored in the password table as 
		//  clear text but for now
		//  keep it simple.
		String realmString = authHeader.getRealm();
		// BUGBUG -- should this be done this way?
		if (realmString == null) {
			realmString = getRealm(uriString);
		}
		String password = (String) 
			passwordTable.get(username+":"+ realmString);
		if (password == null)  {
			throw new 
			SIPAuthenticationException("password not found");
		}
		String nonce = authHeader.getValue(NONCE);
		// If there is a URI parameter in the Authorization header, 
		// then use it.
		String uri = authHeader.getValue(URI);
		// There must be a URI parameter in the authorization header.
		if (uri == null) throw new SIPAuthenticationException
				( "no URI parameter");
		String algorithm = authHeader.getValue(ALGORITHM);
		if (algorithm.compareTo("MD5") == 0) {
			String A1 = username + ":" + realm+ ":" +   password ;
			String A2 = 
			   requestLine.getMethod().toUpperCase() + ":" + uri ;
			byte mdbytes[] = messageDigest.digest(A1.getBytes());
			String HA1 = ServerUtils.toHexString(mdbytes);
			mdbytes = messageDigest.digest(A2.getBytes());
			String HA2 = ServerUtils.toHexString(mdbytes);
			String cnonce = authHeader.getValue(CNONCE);
			String KD = HA1 + ":" + nonce;
			if (cnonce != null) {
				KD += ":" + cnonce;
			} 
			KD += ":" + HA2;
			mdbytes = messageDigest.digest(KD.getBytes());
			String mdString = ServerUtils.toHexString(mdbytes);
			String response = authHeader.getValue(RESPONSE);
			return (mdString.compareTo(response) == 0) ;
		} else {
			throw new SIPAuthenticationException
				("Algorithm not supported");
		}
		
	}
					   
	
}
