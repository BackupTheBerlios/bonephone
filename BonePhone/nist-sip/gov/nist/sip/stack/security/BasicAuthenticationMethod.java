/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../../doc/uncopyright.html for conditions of use.               *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

/******************************************************
 * File: BasicAuthenticationMethod.java
 * created 04-Oct-00 4:46:42 PM by mranga
 * Implements the HTTP basic authentication method as outlined
 * in RFC 2617
 */


package gov.nist.sip.stack.security;
import gov.nist.sip.stack.*;
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



public class BasicAuthenticationMethod implements AuthenticationMethod, SIPErrorCodes
{
	public static final String DEFAULT_REALM = "";
	public static final String DEFAULT_DOMAIN = "nist.gov";
	public static final String NULL_PASSWORD = "";
	Hashtable passwordTable;
	

	public BasicAuthenticationMethod() {
		passwordTable = new Hashtable();
	}

	public String getScheme() {
		return "Basic";
	}
	
	public String getRealm(String resource) {
		return  DEFAULT_REALM;
	}
	
	public String getDomain() {
		return DEFAULT_DOMAIN;
	}
	/**
	* Initialize -- load password files etc.
	* Password file format is name:authentication domain:password
	*/
	public void initialize (String pwFileName )
       	throws Exception {
		ServerLog.traceMsg(ServerLog.TRACE_INITIALIZATION,
				"BasicAuthentication ... Reading password Entries from " + pwFileName);
		BufferedReader pwfile = new BufferedReader(new FileReader(pwFileName));
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
				throw new Exception("Could not find or open Password file");
			}
			try {
				MyStringTokenizer  st = new MyStringTokenizer(input,":");
				String name = st.nextToken();
				name.trim();
				String realm = st.nextToken();
				if (realm == null) realm = DEFAULT_REALM;
				String password =  st.nextToken();
				if (password == null) password = NULL_PASSWORD;
				ServerLog.traceMsg(ServerLog.TRACE_INITIALIZATION,
					"BasicAuthentication.Initialize:" + 
							name+":"+realm + "<" + password);
				passwordTable.put(name+":"+realm, password);
			} catch ( NoSuchElementException ex) {
				throw new Exception("Bad Password File entry in " 
					+ pwFileName + " at line " + line);
			}
		}
	}
	
	
	public String generateNonce() {
		return null;
	}
	
	public String getAlgorithm() {
		return null;
	}
	public boolean doAuthenticate(String username, 
					  Authorization authHeader,
					  RequestLine requestLine) 
					  throws SIPAuthenticationException
	{
		URI requestURI = requestLine.getUri();
		String uriString = requestURI.getInputText();
		NameValueList nvList = 	authHeader.getCredentials();
		String encodedNameAndPassword = null;
	 	if (nvList != null) {
		   encodedNameAndPassword =  (String) 
					        ((NameValue) nvList.first()).
							getValue();
		}
		ServerLog.logMessage("Basic auth method " + encodedNameAndPassword);
		if (encodedNameAndPassword == null)  {
			throw new SIPAuthenticationException( "no password!");
		}
		Base64Decoder decoder =  new Base64Decoder(encodedNameAndPassword);
		String nameAndPassword = null;
		try {
			nameAndPassword = decoder.processString();
		} catch (Base64FormatException ex) {
			throw new SIPAuthenticationException("Bad base 64 encoding " + ex.getMessage());
		}
		StringTokenizer st = new MyStringTokenizer(nameAndPassword,":");
		String name = st.nextToken();
		String sentPassword = st.nextToken();
		if (name == null || sentPassword == null) return false;
		name.trim();
		ServerLog.logMessage("BASIC: Username = " + name + " password = " + sentPassword);
		String key = name+":"+getRealm(uriString);
		ServerLog.traceMsg(ServerLog.TRACE_AUTHENTICATION,key);
		String password = (String) passwordTable.get(key);
		if (password == null) {
		    ServerLog.traceMsg(ServerLog.TRACE_AUTHENTICATION, 
				" password is null. User not known! " + name);
		    return false;
		}
		return password.compareTo(sentPassword) == 0;
	}

		

}
