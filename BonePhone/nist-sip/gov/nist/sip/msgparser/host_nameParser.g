header {
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.*;
}

class host_nameParser extends parser_utilParser;

options {
	k=1;
	defaultErrorHandler=false;
	importVocab = parser_utilParser;
}

host_port returns [HostPort hostPort] 
{

	hostPort = new HostPort();
	hostPort.setHost(null);
	Host h;
	int p =  0;
	startTracking();
}
: h = host ( (COLON) => (COLON  p=port) { hostPort.setPort(p); }  )? 
{
	hostPort.setHost(h);
	hostPort.setInputText(stopTracking());
}
;

host returns [ Host hostID ] 
{ 
	hostID = null;
	String hname;
	boolean isIPV4;
	startTracking();
	// scan ahead and see if this is an ipv4 address
	int nints = 0;
	int k = 1;
	while(LA(k) == DIGIT) {
	    if (LA(k + 1) == DIGIT) k++;
	    else if (LA(k+1) == DOT) {
		 k += 2; // skip over the dot.
		 nints ++;
	    } else {
		nints ++;
		break;
	    }
	}
	// should have seen 4 digits (eg. 129.6.55.62
	// If so set the semantic lookahed flag....
	if (nints == 4) isIPV4 = true;
	else isIPV4 = false;
	
}
: { isIPV4 }?  ( hname=ipv4_address 
{
  
   hostID = new Host(hname,HostAddrTypes.IPV4ADDRESS);
   hostID.setInputText(stopTracking());
} )
// (sfo) rule to match IPv6 references
| hname=ipv6_reference
{
     hostID = new Host(hname,HostAddrTypes.IPV6REFERENCE);
     hostID.setInputText(stopTracking());
}
| hname=hostname 
{
     hostID = new Host(hname,HostAddrTypes.HOSTNAME);
     hostID.setInputText(stopTracking());
}
;


hostname  returns [String hname]
{ 
  hname = new String(""); 
  String s1 = null;
  String s2 = null;
  pushLexer("charLexer");
}
: 
s1 = domainlabel { hname = s1 + hname; }  
(  options { greedy = true; } : 
{ LA(1) ==  DOT && (LA(2) == ALPHA || LA(2) == DIGIT || LA(3) == MINUS)  }?
   DOT s2 = domainlabel { hname += "." + s2; } )*  (DOT)?
{
   // check if the toplabel starts with a alpha else throw an exception...
   int lindex = hname.lastIndexOf('.');
   if (lindex == -1) {
	// only toplabel - make sure it starts with an alpha
	char ch1 = hname.charAt(0);
	if ( isDigit(ch1) || ch1 == '-' ) {
	   throw new RecognitionException("Invalid host name " + hname);
	}
   } else { 
	// s2 better not be null
        // s2 is the last piece of the segment - it had better start with
	// an alpha or it is not a domain name
	char ch1 = s2.charAt(0);
	if ( isDigit(ch1) || ch1  ==  '-' ) {
	   throw new RecognitionException("Invalid host name " + hname);
	}
   }
   // Hostnames are not case sensitive.
   hname = hname.toLowerCase();
   popLexer();
}
;
	


domainlabel returns [ String dlabel ] 
	{ dlabel = new String(""); }
:
         ( a:ALPHA {dlabel += a.getText(); } | 
	   d:DIGIT {dlabel += d.getText();}   | 
	   m:MINUS {dlabel += m.getText();} )+  {
	     if ( dlabel.charAt(dlabel.length() -1 ) == '-')
	         throw new RecognitionException("Bad domain label!");
	     else if ( dlabel.charAt(0) == '-') 
	         throw new RecognitionException("Bad domain label!");
	   }
;


port returns [ int portNum ] 
{
	portNum = -1;
	String portString = "";
}
: ( d1:DIGIT {portString += d1.getText();}  ) + 
{
	portNum = Integer.parseInt(portString);
}
| d:NUMBER
{
// This alternative is necesasry because of nondeterminism in the 
// parser.
	portNum = Integer.parseInt(d.getText()); 
}
;



ipv4_address returns [ String ipv4address ] 
{
	ipv4address = "";
}
:
 ( d1:DIGIT { ipv4address += d1.getText();} )+ DOT { ipv4address += "."; }
 ( d2:DIGIT { ipv4address += d2.getText();} )+ DOT { ipv4address += "."; } 
 ( d3:DIGIT { ipv4address += d3.getText();} )+ DOT { ipv4address += "."; } 
 ( d4:DIGIT { ipv4address += d4.getText();} )+  
;


// (sfo) resolv IPv6 reference
ipv6_reference returns [ String ipv6reference ]
{
	ipv6reference = "";
}
:
l:L_SQUARE_BRACKET { ipv6reference += l.getText(); }
( a:ALPHA { ipv6reference += a.getText(); } |
  d:DIGIT { ipv6reference += d.getText(); } |
  c:COLON { ipv6reference += c.getText(); } )+
r:R_SQUARE_BRACKET { ipv6reference += r.getText(); }
;

