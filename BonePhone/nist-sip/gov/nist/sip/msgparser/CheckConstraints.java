/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.*;
import gov.nist.sip.sipheaders.*;

/**
* A class that checks SIP Headers for constraints. 
*/
public class CheckConstraints  implements SIPKeywords, PackageNames {
    public static void check( SIPHeader hdr) 
    throws IllegalArgumentException {
    if (hdr.getClass().getName().compareTo(SIPHEADERS_PACKAGE + ".To") == 0 ) {
	To t = (To)hdr;	
     	if (t.getAddress().getAddrSpec().getScheme().compareTo(SIP) == 0 ) {
               	Object meth = 
		t.getAddress().getAddrSpec().getUriParms().getValue(METHOD);
        	if (meth != null) 
			throw new IllegalArgumentException
				("Parm method not allowed in URI");
		String transport = 
			(String) t.getAddress().
			getAddrSpec().getUriParms().getValue(TRANSPORT);

		if (transport != null) 
			throw new IllegalArgumentException
				("Parm transport not allowed in URI");
		Integer ttl = (Integer) t.getAddress().getAddrSpec().
				getUriParms().getValue(SIPKeywords.TTL);
        	if (ttl != null) 
			throw new IllegalArgumentException
				("Parm ttl not allowed here in URI");
        	String  maddr_parm = (String) 
			t.getAddress().getAddrSpec().
					getUriParms().getValue(MADDR);
		if (maddr_parm != null) 
			throw new IllegalArgumentException
				("maddr not allowed in URI");
		if (!t.getAddress().getAddrSpec().getQheaders().isEmpty()) 
			throw new IllegalArgumentException
				("Headers not allowed in URI");
     }
   } else if (hdr.getClass().getName().
		compareTo(SIPHEADERS_PACKAGE + ".From") == 0) {
	From t = (From)hdr;	
     	if (t.getAddress().getAddrSpec().getScheme().compareTo(SIP) == 0 ) {
               	Object meth = 
		t.getAddress().getAddrSpec().getUriParms().getValue(METHOD);
        	if (meth != null) 
		    throw new IllegalArgumentException
				("Parm method not allowed in URI");
		String transport = 
			(String) t.getAddress().getAddrSpec().
				getUriParms().getValue(TRANSPORT);

		if (transport != null) 
			throw new IllegalArgumentException
				("Parm transport not allowed in URI");
		Integer ttl = (Integer) t.getAddress().getAddrSpec().
				getUriParms().getValue(TTL);
        	if (ttl != null) 
			throw new IllegalArgumentException
				("Parm ttl not allowed here in URI");
        	String  maddr_parm = (String) 
			t.getAddress().getAddrSpec().getUriParms().
						getValue(MADDR);
		if (maddr_parm != null) 
				throw new IllegalArgumentException
					("maddr not allowed in URI");
		if (!t.getAddress().getAddrSpec().getQheaders().isEmpty()) 
			throw new IllegalArgumentException
					("Headers not allowed in URI");
     }
   } 
  } 

  /**
  * Check the given Request URI for constraints.
  */

  public static void checkRequestURI( URI uri, String method) 
  throws  IllegalArgumentException  {
	if (! uri.getQheaders().isEmpty()) {
		 throw new IllegalArgumentException
				("No hdrs allowed in req URI");
          }
	 String scheme = uri.getScheme();
	 if (scheme == null) 
		 throw new IllegalArgumentException
			("URI scheme is missing");

         if (method.compareTo(REGISTER) == 0  &&
		uri.getScheme().compareTo(SIP) != 0 ) {
			new SIPIllegalRequestLineException
				("Only SIP url allowed");
	 } 
  }
}
