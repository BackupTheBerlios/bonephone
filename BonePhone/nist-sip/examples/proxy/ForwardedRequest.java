/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package examples.proxy;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.stack.*;
import gov.nist.sip.net.*;

/**
*  This keeps track or requests that we send to other hosts
* (for retransmission, sending cancels,  whatever...)
*/ 

class ForwardedRequest  {
    protected String     newRequest;
    protected String     branchID;
    protected SIPRequest originalRequest;
    protected HostPort   sentTo;
    protected String     transport;
    protected String getNewRequest() { return newRequest; }
    protected  String getBranchID()  { return branchID;   }
    protected HostPort  getSentTo()  { return sentTo;     }
    protected String getTransport()  { return transport;  }
    public String toString()  {
		String retval = "ForwardedRequest = { ";
		retval += "branchID = " + branchID + "\n";
		retval += "sentTo = " + sentTo.encode() + "\n";
		retval += "originalRequest = "  + 
			(originalRequest != null? originalRequest.encode() 
			: null) + "\n";
		retval += "newRequest = " + newRequest + "\n }";
		return  retval;
    }
		
}
