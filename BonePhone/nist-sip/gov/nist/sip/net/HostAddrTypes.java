/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;
/**
* Identifies whether the host is a host name or an IPV4 Addr.
*/
public interface HostAddrTypes {
		public static final  int HOSTNAME = 1;
		public static  final int IPV4ADDRESS = 2;
		public static final int IPV6REFERENCE = 3;
}
