header {
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/**
* Implements a parser for SIP URLS (see rfc 2543) and generic URIs 
* (see RFC 2396)
* Release version 0.9
* Current version 1.0
*/

package gov.nist.sip.msgparser;
import java.net.URLDecoder; 
import gov.nist.sip.net.*;
import gov.nist.sip.*;
}

class sip_urlParser extends host_nameParser;

options {
	k=1;
	defaultErrorHandler=false;
	importVocab = host_nameParser;
	importVocab = parser_utilParser;
	importVocab = tel_Parser;
	exportVocab = sip_urlParser;
}


unreserved returns [ String s ] 
{ s  = null ; }
:
(  a:ALPHA { s = a.getText(); } 
| d:DIGIT  { s = d.getText(); }
| s = mark_ )
;

// This is a non-terminal because it is really tricky to combine the
// definitions of RFCs 2543 and 2396 and still get everything right!
reserved returns [ String s ] 
{
	s = null;
}
:
   ( a:SEMICOLON { s = a.getText(); } | 
     b:SLASH     { s = b.getText(); } | 
     c:QUESTION  { s = c.getText(); } | 
     d:COLON     { s = d.getText(); } | 
     e:AT 	 { s = e.getText(); } | 
     f:AND  	 { s = f.getText(); } |  
     g:PLUS 	 { s = g.getText(); } | 
     h:DOLLAR 	 { s = h.getText(); } | 
     i:COMMA 	 { s = i.getText(); } )
;

escaped returns [ String s ]
{
	s = null;
	String a ;
	String b ;
}
: ( PERCENT a = hexdigit b = hexdigit { s = "%" + a + b ; } )
;

hexdigit returns [ String s ] 
{
	s = null;
}
: d:DIGIT { s = d.getText(); } | a:ALPHA { 
	   String t = a.getText() ; 
	   String ttok = t.toUpperCase();
	   char   tok = ttok.charAt(0);
	   if ( tok != 'A' && tok != 'B' && tok != 'C' && tok != 'D' &&
	        tok != 'E' && tok != 'F'   &&
	        tok != 'a' && tok != 'b' && tok != 'c' && tok != 'd' &&
	        tok != 'e' && tok != 'f'   ) {
	         throw new RecognitionException("Bad Hex Number!");
	   }
	   s = a.getText();
	}
;

mark_ returns [ String s ] 
{
	s = null;
} 
:
	( m:MINUS 	{ s = m.getText(); }  | 
	u:UNDERSCORE  	{ s = u.getText(); }  | 
	d:DOT  		{ s = d.getText(); }  | 
	e:EXCLAMATION   { s = e.getText(); }  | 
	t:TILDE 	{ s = t.getText(); }  | 
	v:STAR 		{ s = v.getText(); }  | 
	q:QUOTE 	{ s = q.getText(); }  | 
	l:LPAREN 	{ s = l.getText(); }  | 
	r:RPAREN 	{ s = r.getText(); }  )
;

uric returns [ String s ] :
        ( s = unreserved | s = reserved | s = escaped )
;

uric_no_slash returns [ String s ] 
{
	s = null;
}
:   (   s = unreserved | s = escaped | 
	a:SEMICOLON { s = a.getText(); } | 
	b:QUESTION  { s = b.getText(); } | 
	c:COLON     { s = c.getText(); } | 
	d:AT 	    { s = d.getText(); } | 
	e:AND 	    { s = e.getText(); } | 
 	f:PLUS 	    { s = f.getText(); } | 
	g:DOLLAR    { s = g.getText(); } | 
	h:COMMA	    { s = h.getText(); }   )
;





alphanum returns [ String t ]  
{
	t = null;
}
:
	( a:ALPHA { t = a.getText(); } | d:DIGIT { t = d.getText(); } ) 
;

h_name returns [ String s ] 
{
	s = "";
	String v;
}
:
	( v = uric { s += v; }  )+
;

hvalue returns [ String s ] 
{
	s = "";
	String v;
}
: ( options { greedy = true; } : v = uric  { s += v; } )*
;

// Select in the lexer, which path to pick before entering this
// rule. This should be done in the enclosing context.

uri_reference returns [ URI uri_ptr ] 
: ((SIP) =>  uri_ptr = sip_url |   uri_ptr = uri )
;


// A URI with no parameters. for FROM and TO headers
uri_noparms returns [ URI uri_ptr ]
: ((SIP) =>  uri_ptr = sip_url_noparms |   uri_ptr = uri )
;

// RFC 2396 
uri returns [ URI uri_ptr ] 
{ 
  startTracking(); 
  String frag = null;
}
: ( {LA(1) == ID && LA(2) == COLON }? uri_ptr = absolute_uri 
| uri_ptr = relative_uri ) ( (POUND)=> POUND frag = fragment)?  
{ 
  uri_ptr.setFragment(frag);
  uri_ptr.setInputText(stopTracking()); 
}
;

// RFC 2396
absolute_uri returns [ URI uri_ptr ] 
{
	String s;
	String o;
	uri_ptr = null;
}
: s = scheme COLON 
( uri_ptr = hier_part 
{
	uri_ptr.setScheme(s);
}
| o = opaque_part 
{
	uri_ptr = new URI();
	uri_ptr.setUriType(URITypes.ABSOLUTE_URI);
	uri_ptr.setScheme(s);
	uri_ptr.setOpaquePart(o);
}
)
;

// RFC 2396
scheme returns [ String s ] 
{
	s = "";
	String r;
}
:( t:ID
{ // check for validity of scheme.
  s = t.getText();
} 
| s = ttoken() )
{
	if  ( ! Character.isLetter(s.charAt(0))) {
		throw new RecognitionException 
			("URI scheme must start with letter");
	} 
	for (int k = 1; k < s.length(); k++) {
		if ( ! Character.isLetter(s.charAt(k))
		     && !Character.isDigit(s.charAt(k))
		     && s.charAt(k) != '+'
		     && s.charAt(k) != '-'
		     && s.charAt(k) != '.')  {
		     throw new RecognitionException 
			( "Bad URI scheme character " + s.charAt(k));
		}

	}
}
;

// RFC 2396
hier_part returns [ URI u ]  
{
	u = new URI();
	NetPath n;
	AbsPath a;
	String q = null;
}
:
( { LA(1) == SLASH && LA(2) == SLASH }?  n = net_path 
{
 	u.setAuthority(n.getAuthority());
	u.setPath(n);
}
 | a = abs_path 
{
	u.setPath(a);
}
) ( options { greedy = true; } :  QUESTION q = query )?
{
	u.setQuery(q);
}
;


// RFC 2396
net_path returns [ NetPath netpath ]
{
	Authority auth;
	AbsPath a = null;
	netpath = new NetPath();
	startTracking();
	
}
:  
SLASH SLASH auth = authority ( a = abs_path )?
{
	netpath.setAuthority(auth);
	netpath.setAbsPath(a);
	netpath.setInputText(stopTracking());
}
;

// RFC 2396
authority returns [ Authority a ] 
{
	String  r = null;
	a = null;
	int k = 1;
	while ( LA(k) != SP     && 
		LA(k) != HT     &&
	        LA(k) != DOT    && 
		LA(k) != AT     && 
	        LA(k) != RETURN &&
		LA(k) != COLON   ) {
	   k++;
	}
	startTracking();
}
:  ( {   LA(k) == DOT || LA(k) == AT || LA(k) == COLON }? a = server_h | 
	a = reg_name )
{
	a.setInputText(stopTracking());
}
;

// RFC 2396
reg_name returns [ AuthorityRegname  a ] 
{
  a = null;
  String s = "";
  String r;
  startTracking();
}
: ( options{greedy = true;}: 
     r = unreserved   { s += r; 	 } | 
     r = escaped     { s += r; 		 } |  
     t:DOLLAR  	     { s += t.getText(); } |  
     b:COMMA  	     { s += b.getText(); } |  
     c:SEMICOLON     { s += c.getText(); } |  
     d:COLON         { s += d.getText(); } |  
     e:AT  	     { s += e.getText(); } |  
     f:AND  	     { s += f.getText(); } |  
     g:EQUALS  	     { s += g.getText(); } |  
     h:PLUS  	     { s += h.getText(); } )+ 
{
	a = new AuthorityRegname();
	a.setRegName(s);
	a.setInputText(stopTracking());
}
;

// RFC 2396
server_h returns [ AuthorityServer h ]
{
	h = new AuthorityServer();
	String s = null;
        HostPort p;
	UserInfo u = null;
	pushLexer("charLexer");
	startTracking();
}
: ( (userinfo AT) => (u =  userinfo  AT  ))? p = host_port 
{
	h.setUserInfo(u);
	h.setHostPort(p);
	h.setInputText(stopTracking());
	popLexer();
}
;



// RFC 2396
abs_path returns [ AbsPath a ] 
{
	a = new AbsPath();
	PathSegments s;
	startTracking();
} 
: SLASH s = path_segments
{
	a.setPathSegments(s);
	a.setInputText(stopTracking());
}
;


path_segments returns [ PathSegments s ] 
{
	Segment p = null;
	s = new PathSegments();
	startTracking();
}
:   p = segment { s.add(p); }
    ((SLASH) => ( SLASH  p = segment { s.add(p); })+ )?
{
	s.setInputText(stopTracking());
}
;

// RFC 2396
segment returns [ Segment s ] 
{
	s = new Segment() ; 
	String q = "";
	String p = null;
	String r ;
	startTracking();
} 
: ( options { greedy = true; } :
	 r =  pchar { if (p == null) p = r ; else p += r ; }  )*   
    { s.setPath(p); }
    ( (SEMICOLON)=> 
      ( options {greedy = true; } :
	   SEMICOLON ( options {greedy = true; } : r = pchar { q += r ; } )+ 
	  { s.getParmList().add(q); })+ 
    )? 

{ 
  s.setInputText(stopTracking());
}
;


// RFC 2396
query returns [ String s ]  
{
	s = "";
	String r;
}
: (options{greedy = true;}: r = uric { s += r; } )+
;

// RFC 2396
pchar returns [ String s ]  
{
	s = null;
	String r;
	
}
:
	( r = unreserved { s = r; 		} | 
	  r = escaped    { s = r; 		} | 
	  a:COLON 	 { s = a.getText(); 	} | 
	  b:AT 		 { s = b.getText();     } | 
	  c:AND 	 { s = c.getText();	} | 
	  d:EQUALS 	 { s = d.getText(); 	} | 
	  e:PLUS 	 { s = e.getText();     } | 
	  f:DOLLAR 	 { s = f.getText(); 	} | 
	  g:COMMA 	 { s = g.getText(); 	} )
;


// RFC 2396
opaque_part returns [ String s ] 
{
	s = "";
	String v;
}
: s = uric_no_slash ( options{greedy = true;}: v = uric { s += v; } )*
;


// RFC 2396
// Note that we turn off ambiguity warnings in the rule below
// because we have a local lookahead of 2 for which we use
// semantic lookahead.
relative_uri returns [ URI u ] 
{
	u = new URI();
	u.setUriType(URITypes.RELATIVE_URI);
	NetPath n = null;
	AbsPath s = null;
	RelPath r = null;
	String  q = null;
}
:( options { generateAmbigWarnings = false; } :
  ( { LA(1) == SLASH && LA(2) == SLASH}?   n = net_path  
  {    
     u.setAuthority(n.getAuthority());
     u.setPath((Path) n);
  } )
  | ( options { generateAmbigWarnings = false; } :
	 { LA(1) == SLASH && LA(2) != SLASH }? s =  abs_path   )
  {
     u.setAuthority(null);
     u.setPath((Path) s);
  } 
  | r = rel_path 
  {
    u.setAuthority(null);	
    u.setPath((Path) r);
  } ) 
  ( QUESTION q = query )?
  {
   u.setQuery(q);
  }
;


// RFC 2396
rel_path returns [ RelPath s ]
{
	s = new RelPath();
	String r = null;
	AbsPath a = null;
	startTracking();
}
:     r = rel_segment 
	( (SLASH) => a = abs_path )? {
	  s.setRelSegment(r);
	  s.setAbsPath(a);
	  s.setInputText(stopTracking());
	}
;

// RFC 2396
rel_segment returns [ String s ] 
{
	s  = "";
        String r = null;
}
: ( options { greedy = false; } : 
     r = unreserved 	{ s += r; 		} |  
     r = escaped   	{ s += r; 		} | 
     a:SEMICOLON 	{ s += a.getText(); 	} | 
     b:AT		{ s += b.getText();	} | 
     c:AND 		{ s += c.getText(); 	} |
     d:EQUALS 		{ s += d.getText(); 	} |
     e:PLUS 	        { s += e.getText(); 	} |
     f:DOLLAR 	        { s += f.getText(); 	} |
     g:COMMA 		{ s += g.getText(); 	} )+
;

// RFC 2396
fragment returns [ String s ] 
{
	s = "";
	String r;
}
:(options{greedy = true;}: r = uric  { s += r; } )+
;
	

sip_url returns [ URI uri ] 
{
	uri =  null;
	startTracking();
}
: SIP COLON { selectLexer("charLexer"); } uri=sip_urlbody   
{ 
       uri.setUriType(URITypes.SIP_URL);
       uri.setScheme(SIPKeywords.SIP);  
       uri.setInputText(stopTracking());
       if (uri.isUserTelephoneSubscriber()) {
	  String usr = uri.getUser();
	  // This is a performance hit -- need a lightweight parser for this.
	  StringMsgParser smp = new StringMsgParser();
	  try {
	    TelephoneNumber tel = smp.parseTelephoneNumber(usr);
	    uri.setTelephoneSubscriber(tel);
	  } catch ( SIPParseException ex) {
	     throw new RecognitionException("Invalid Phone number spec "+ usr);
	  }
       }
} 
;


// RFC 2543 for FROM and TO headers 
sip_url_noparms returns [ URI uri ] 
{
	uri =  null;
	startTracking();
}
: SIP COLON { selectLexer("charLexer"); } uri=sip_urlbody_noparms   
{ 
       uri.setUriType(URITypes.SIP_URL);
       uri.setScheme(SIPKeywords.SIP);  
       uri.setInputText(stopTracking());
       if (uri.isUserTelephoneSubscriber()) {
	  String usr = uri.getUser();
	  // This is a performance hit -- need a lightweight parser for this.
	  StringMsgParser smp = new StringMsgParser();
	  try {
	    TelephoneNumber tel = smp.parseTelephoneNumber(usr);
	    uri.setTelephoneSubscriber(tel);
	  } catch ( SIPParseException ex) {
	     throw new RecognitionException("Invalid Phone number spec "+ usr);
	  }
       }
} 
;


// RFC 2543 for FROM and TO headers 
sip_urlbody_noparms returns [ URI uri ] 
{
	uri =  new URI();
	startTracking(); 
	Authority auth;
}
: auth = sip_authority
{
	uri.setAuthority(auth);
	uri.setInputText(stopTracking());
}
;


// RFC 2543
sip_urlbody returns [ URI uri ] 
{
	uri =  new URI();
	NameValueList parms = null;
	NameValueList qhdrs = null;
	startTracking(); 
	Authority auth;
}
: auth = sip_authority
	( (SEMICOLON)=> SEMICOLON 
	{ selectLexer("sip_urlLexer",sip_urlLexer.PARMS_LHS_STATE); }
	 parms = url_parms { uri.setUriParms(parms); } ) ?
	 ( (QUESTION) => 
	   { selectLexer("charLexer"); } 
		qhdrs = qheaders 
	{ uri.setQheaders(qhdrs);  uri.setQuery(qhdrs.getInputText()); }  )? 
{
	uri.setAuthority(auth);
	uri.setInputText(stopTracking());
}
;

// RFC 2543
sip_authority returns [ AuthorityServer auth ]
{
	startTracking();
}
:	auth = server_h
{
	if (auth.getHostPort().getPort() == 0) 
		auth.getHostPort().setPort(SIPDefaults.DEFAULT_PORT);
	auth.setInputText(stopTracking());
}
;


// RFC 2543
userinfo returns [ UserInfo uinfo ] 
{
	String u;
	String p = null ; // Default is no password
	uinfo = new UserInfo();
	startTracking();
}
: u =  user ( (COLON) => COLON p=password)? 
{
	uinfo.setUser(u);
	uinfo.setPassword(p);
	uinfo.setInputText(stopTracking());
	if (u.indexOf(Separators.POUND) >= 0  || 
		u.indexOf(Separators.SEMICOLON) >= 0) {
		uinfo.setUserType(UserInfo.TELEPHONE_SUBSCRIBER);
	} else uinfo.setUserType(UserInfo.USER);

}
;


// RFC 2543
// Note - the POUND and the SEMICOLON  below had to be added to allow 
// for legal telephone subscriber strings. 
// This is decidedly a hack but I am not parsing
// for legal telephone subscriber strings so it is OK (for now).
//
//	  Removed  sc:SEMICOLON	 

user returns [ String s ] 
{
	s = "" ;
	String r;
} 
: 
	( r = unreserved { s += r; 	     } | 
	  r = escaped    { s += r; 	     } | 
          a:AND 	 { s += a.getText(); } | 
	  b:EQUALS 	 { s += b.getText(); } | 
	  c:PLUS   	 { s += c.getText(); } | 
	  d:DOLLAR  	 { s += d.getText(); } | 
	  p:POUND	 { s += p.getText(); } |
	  q:QUESTION     { s += q.getText(); } |
	  sl:SLASH	 { s += sl.getText();} |
	  e:COMMA  	 { s += e.getText(); } )+
;



// RFC 2543
qheader returns [ NameValue v ]
{
	String name;
	String value;
	v = new NameValue();
	int s;
	startTracking();
}
:      name = h_name EQUALS value = hvalue 
{
	v.setName(name.toLowerCase());
	// Could be URL encoded values here.
	v.setValue(value);
	v.setInputText(stopTracking());
}
;

// RFC 2543
qheaders returns [ NameValueList  nv ] 
{
	NameValue n = null ;
	nv = new NameValueList("qheaders");
	nv.setSeparator(Separators.AND);
}
:  QUESTION n = qheader  { nv.add(n); } 
	( AND n =  qheader { nv.add(n); } )*  
;





// RFC 2543
url_parms returns [ NameValueList nvlist ] 
{
	nvlist = new NameValueList("url_parms");
	NameValue nv = null;
} 
: nv = url_parameter { nvlist.add(nv); } 
  (options { greedy = true; }: 
	SEMICOLON { selectLexer("sip_urlLexer",sip_urlLexer.PARMS_LHS_STATE); }
	nv = url_parameter { nvlist.add(nv); } )*
;


// RFC 2543
url_parameter returns [ NameValue nv ] 
: 	(   nv = transport_param  
	|   nv = user_param  
	|   nv = method_param  
	|   nv = ttl_param 
        |   nv = maddr_param 
	|   nv = other_param )
;

// RFC 2543 (the hack below is because you can be in different contexts
// in this rule! 
other_param returns [ NameValue nv ] 
{
	nv = new NameValue();
	startTracking();
	String p;
}
: t3:ID EQUALS t4:ID
{
	nv.setName(t3.getText());
	nv.setValue(t4.getText());
	nv.setInputText(stopTracking());
}
| p = ttoken EQUALS t5:ID
{
	nv.setName(p);
	nv.setValue(t5.getText());
	nv.setInputText(stopTracking());
}
;




// RFC 2543
transport_param returns [ NameValue nv ] 
{
	nv = new NameValue();
	nv.setName(URIKeywords.TRANSPORT);
	startTracking();
}
: TRANSPORT EQUALS {selectLexer("transport_Lexer"); }
( UDP {nv.setValue(URIKeywords.UDP); } 
| TCP  {nv.setValue(URIKeywords.TCP); }) 
{
	nv.setInputText(stopTracking());
}
;

// RFC 2543
user_param returns [ NameValue nv ] 
{
	nv = new NameValue();
	nv.setName(SIPKeywords.USER);
	startTracking();
}
: USER EQUALS 
{ selectLexer("transport_Lexer"); }
( PHONE { nv.setValue(URIKeywords.PHONE); } 
| IP {nv.setValue(SIPKeywords.IP);} )
{
	nv.setInputText(stopTracking());
}
;


// RFC 2543
method_param returns [ NameValue nv ] 
{
	nv = new NameValue();
	nv.setName(SIPKeywords.METHOD);
	startTracking();
	String s;
}
: METHOD EQUALS 
s = method 
{
	nv.setValue(s);
	nv.setInputText(stopTracking());
}
;

// RFC 2543
method returns [ String m ] 
{
	pushLexer("method_keywordLexer");
	m = null;
}
:  (     REGISTER { m = SIPKeywords.REGISTER;  } 
	| ACK     { m = SIPKeywords.ACK;       }
	| OPTIONS { m = SIPKeywords.OPTIONS;   }
	| BYE	  { m = SIPKeywords.BYE;       }
	| INVITE  { m = SIPKeywords.INVITE;    }
	| CANCEL  { m = SIPKeywords.CANCEL;    } 
	| m = extension_method )
{
	popLexer();
}
;


extension_method returns [ String m ] 
{ m = null ; }
: t:ID
{ m = t.getText(); }
;


// RFC 2543
ttl_param  returns [ NameValue nv ] 
{
	nv = new NameValue();
	nv.setName(SIPKeywords.TTL);
	startTracking();
	Integer t;
}
: TTL EQUALS { selectLexer ("charLexer"); }  t = ttlval  
{
	nv.setValue(t);
	nv.setInputText(stopTracking());
}
;

// RFC 2543
ttlval returns [ Integer val ] 
{
	val = null;
	String ttlval = "";

}
: ( d:DIGIT { ttlval += d.getText(); } ) + 
{
	val =  new Integer(ttlval);
}
;

// RFC 2543
maddr_param returns [ NameValue nv ] 
{
	nv = new NameValue();
	nv.setName(SIPKeywords.MADDR);
	Host h;
	startTracking();
}
:  MADDR {selectLexer("charLexer"); } EQUALS  h=host
{
	nv.setValue(h);
	nv.setInputText(stopTracking());
}
;


// RFC 2543
password returns [ String p ] 
{
  p = "";  
  String s;
}
:
 	 ( s = unreserved { p += s; } 	    | 
	 s = escaped      { p += s; }	    | 
	 a:AND            { p += a.getText(); }   | 
	 b:EQUALS 	  { p += b.getText();    }   | 
	 c:PLUS		  { p += c.getText(); 	}   | 
	 d:DOLLAR 	  { p += d.getText() ;	}   | 
	 e:COMMA  	  { p += e.getText(); }  ) *
;

