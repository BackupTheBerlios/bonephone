header {
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)          *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.*;
import gov.nist.sip.sdpfields.*;
}
class sdp_announceParser extends sip_urlParser;

options {
	k=1;
	defaultErrorHandler=false;
	importVocab = sip_messageParser;
	exportVocab = sdp_announceParser;
}

// Note the RFC for sip says that s= can be left out but sdp requires it.

sdp_announce returns [ SDPAnnounce sdp]
{
	ProtoVersionField pv;
	MediaDescriptionList md = null;
	OriginField of;
	sdp = new SDPAnnounce();
	SDPField sfield = null;
	startTracking();
	

}
:    pv = proto_version
	{ sdp.setProtoVersion(pv); }
     of =  origin_field
	{ sdp.setOriginField(of); }
     ( sfield = sdp_field { 
  	 try {
	     if (sfield != null) sdp.attachField(sfield); 
	  } catch ( SDPParseException ex) {
	     try {
	       // give the app a chance to recover.
	       parserMain.handleParseException(ex);
	     } catch (SIPParseException e ) {
		throw new RecognitionException(e.getMessage());
	     }

	  }
	}
       )+
    (md = media_descriptions
	{ sdp.setMediaDescriptions(md);} )?
    RETURN
{
	sdp.setInputText(stopTracking());
}
;
exception 
catch [ RecognitionException ex ] {
	stopTracking();
	int curLine = parserMain.getCurrentLineNumber();
	RecognitionException e = new RecognitionException
			("Unkown SDP Field at line " + curLine);
	e.fillInStackTrace();
	throw e;
	

}
catch [ TokenStreamException ex ] {
	stopTracking();
	int curLine = parserMain.getCurrentLineNumber();
	TokenStreamException e = new TokenStreamException
		("Unkown Token in SDP at line "  + curLine);
	e.fillInStackTrace();
	throw e;
}

sdp_field returns [SDPField sf] 
{
    sf = null;
}
: ( (S_EQUALS)=> sf = session_name_field   |
    (I_EQUALS)=> sf = information_field    |
    (U_EQUALS)=> sf = uri_field 	   |
    (E_EQUALS)=> sf = email_fields 	   |
    (P_EQUALS)=> sf = phone_fields         |
    (C_EQUALS)=> sf = connection_field     |
    (B_EQUALS)=> sf = bandwidth_field      |
    (T_EQUALS)=> sf = time_fields          |
    (K_EQUALS)=> sf = key_field            |
    (A_EQUALS)=> sf = attribute_fields     )
;
exception 
catch [ RecognitionException ex ] {
	stopTracking();
	int curLine = parserMain.getCurrentLineNumber();
	RecognitionException e = new RecognitionException
			("Unkown SDP Field at line " + curLine);
	e.fillInStackTrace();
	throw e;
	

}
catch [ TokenStreamException ex ] {
	stopTracking();
	int curLine = parserMain.getCurrentLineNumber();
	TokenStreamException e = new TokenStreamException
		("Unkown Token in SDP at line "  + curLine);
	e.fillInStackTrace();
	throw e;
}

origin_field returns [ OriginField of ] 
{
	of = new OriginField();
	String un;
	long sid;
	long  sver;
	String nt;
	String at;
	Host ad;
	startTracking();
}
: O_EQUALS un = username 
	  { selectLexer("sdpLexer"); }
	  SP sid = sess_id SP sver = sess_version SP 
          nt =  nettype SP at =  addrtype SP ad =  host
          (SP|HT)* RETURN 
	  { 
	    of.setInputText(stopTracking());
	    of.setSessId(sid);
	    of.setSessVersion(sver);
	    of.setNettype(nt);
	    of.setAddrtype(at);
	    of.setAddress(ad);
	    of.setUsername(un);
	    selectLexer("sdpLexer"); 
	  }
;
exception 
catch [ RecognitionException ex ] {
   	of = (OriginField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			of,SDPFieldNames.ORIGIN_FIELD, true);

}
catch [ TokenStreamException ex ] {
	 of = (OriginField) handleParseException(ex,SDP_PARSE_EXCEPTION, 
			of,SDPFieldNames.ORIGIN_FIELD, true);
}

session_name_field returns [ SessionNameField  sn ]
{
	// Session name field can be empty!
	sn = new SessionNameField();
	String s = null;
	startTracking();
}
:S_EQUALS ( (RETURN) => RETURN { s = ""; } 
|   {selectLexer("charLexer"); } s = byte_string 
(SP|HT)* RETURN  )
{ 
	sn.setInputText(stopTracking());
	sn.setSessionName(s);
	selectLexer("sdpLexer"); 
}
;
exception 
catch [ RecognitionException ex ] {
	 sn = (SessionNameField) handleParseException(ex,SDP_PARSE_EXCEPTION, 
			sn,SDPFieldNames.SESSION_NAME_FIELD, true);

}
catch [ TokenStreamException ex ] {
	sn  = (SessionNameField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			sn,SDPFieldNames.SESSION_NAME_FIELD, true); 
}


proto_version returns [ ProtoVersionField  pvf ] 
{
	int pv = 0;
	String pvs = "";
	pvf = new ProtoVersionField();
	startTracking();
}
: V_EQUALS (d:DIGIT { pvs += d.getText(); } )+ 
                 (SP|HT)* RETURN 
{
	pvf.setInputText(stopTracking());
	pv = Integer.parseInt(pvs);
	pvf.setProtoVersion(pv);
	selectLexer("sdpLexer");
}
;
exception 
catch [ RecognitionException ex ] {
	pvf = (ProtoVersionField) 
		handleParseException(ex, SDP_PARSE_EXCEPTION,
		pvf,SDPFieldNames.PROTO_VERSION_FIELD, true);

}
catch [ TokenStreamException ex ] {
	pvf = (ProtoVersionField) 
		handleParseException(ex, SDP_PARSE_EXCEPTION,
		pvf,SDPFieldNames.PROTO_VERSION_FIELD, true);
}


uri_field returns [ URIField u ]
{
	URI uri;
	u = new URIField();
}
:   
	U_EQUALS  uri = uri_reference 
        (SP|HT)* RETURN 
{ 
	u.setURI(uri);
	selectLexer("sdpLexer");  
}
;
exception 
catch [ RecognitionException ex ] {
   u = (URIField) handleParseException(ex, SDP_PARSE_EXCEPTION,
				u,SDPFieldNames.URI_FIELD, true);

}
catch [ TokenStreamException ex ] {
   u = (URIField) handleParseException(ex, SDP_PARSE_EXCEPTION,
				u,SDPFieldNames.URI_FIELD, true);
}






information_field returns [ InformationField iff ]
{
	iff = new InformationField();
	String s = null;
	startTracking();
}
:
         I_EQUALS s = byte_string 
         (SP|HT)* RETURN 
{ 
	iff.setInputText(stopTracking());
	iff.setInformation(s);
	selectLexer("sdpLexer");  
}
;
exception 
catch [ RecognitionException ex ] {
   iff = (InformationField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			iff,SDPFieldNames.INFORMATION_FIELD, true);

}
catch [ TokenStreamException ex ] {
   iff = (InformationField) handleParseException(ex, SDP_PARSE_EXCEPTION,
				iff,SDPFieldNames.INFORMATION_FIELD, true);
}



email_fields returns [ EmailFieldList e ]
{
	e = new EmailFieldList();
	startTracking();
	EmailField ef;

}
:(options{greedy=true;}: ef = email_field  { e.add(ef); } )+
{
	e.setInputText(stopTracking());
	selectLexer("sdpLexer");
}
;


email_field returns [ EmailField e ] 
{
    startTracking();
    e = new EmailField();
    EmailAddress ea = null;
}
:    E_EQUALS  { selectLexer("charLexer"); }
      ea  = email_address   
     (SP|HT)* RETURN 
{
	selectLexer("sdpLexer");
	e.setEmailAddress(ea);
	e.setInputText(stopTracking());
}
;
exception 
catch [ RecognitionException ex ] {
  e = (EmailField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			e,SDPFieldNames.EMAIL_FIELD, true);

}
catch [ TokenStreamException ex ] {
   e = (EmailField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			e,SDPFieldNames.EMAIL_FIELD,true);
}


phone_fields returns [ PhoneFieldList hlist ]
{
	hlist = new PhoneFieldList();
	PhoneField p;
	startTracking();
}
:(options{greedy=true;}: p = phone_field { hlist.add(p); } )+
{
	hlist.setInputText(stopTracking());
         selectLexer("sdpLexer");
}
;

phone_field  returns [  PhoneField p ]
{
	p =  new PhoneField();
	startTracking();
	NameValue v;
}
:  P_EQUALS   v = phone_number (SP|HT)* RETURN 
{ 
   p.setName(v.getName());
   p.setPhoneNumber((String) v.getValue());
   p.setInputText(stopTracking());
   selectLexer("sdpLexer");
}
;
exception 
catch [ RecognitionException ex ] {
   p = (PhoneField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			p,SDPFieldNames.PHONE_FIELD, true);

}
catch [ TokenStreamException ex ] {
   p = (PhoneField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			p,SDPFieldNames.PHONE_FIELD, true);
}


connection_field returns [ ConnectionField cf ] 
{
	startTracking();
	cf = new ConnectionField();
	String nt;
	String at;
	ConnectionAddress  ad;
}
:   
         C_EQUALS nt = nettype SP at = addrtype SP 
         ad = connection_address 
         (SP|HT)* RETURN
	 { 
	  cf.setInputText(stopTracking());
	  cf.setNettype(nt);
	  cf.setAddrtype(at);
	  cf.setAddress(ad);
	  selectLexer("sdpLexer");  
	 }
;
exception 
catch [ RecognitionException ex ] {
   cf = (ConnectionField) 
		handleParseException(ex,SDP_PARSE_EXCEPTION,
			cf,SDPFieldNames.CONNECTION_FIELD, true);

}
catch [ TokenStreamException ex ] {
   cf = (ConnectionField) 
		handleParseException(ex,SDP_PARSE_EXCEPTION,
			cf,SDPFieldNames.CONNECTION_FIELD, true);
}

bandwidth_field returns [ BandwidthField  b ]
{
	startTracking();
	b = new BandwidthField();
	String s = null;
	int bw;

}
:
           B_EQUALS  s = bwtype COLON bw = bandwidth 
            (SP|HT)* RETURN 
	   {
	     b.setBwtype(s);
	     b.setBandwidth(bw);
	     b.setInputText(stopTracking());
	     selectLexer("sdpLexer");
	    } 
;
exception 
catch [ RecognitionException ex ] {
   b = (BandwidthField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			b,SDPFieldNames.BANDWIDTH_FIELD, true);

}
catch [ TokenStreamException ex ] {
   b = (BandwidthField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			b,SDPFieldNames.BANDWIDTH_FIELD, true);
}

zone_field returns [ ZoneField z ]
{
	z = new ZoneField();
	startTracking();
	ZoneAdjustment at = null ;
	long t;
	TypedTime tt;
	String sign = null;
	
}
:Z_EQUALS (t = time SP 
  ((MINUS)=> MINUS { sign = "-"; } )? 
   tt = offset 
   { 
	at = new ZoneAdjustment();  
	at.setTime(t); 
	at.setSign(sign); 
	at.setOffset(tt);  
	z.addZoneAdjustment(at);
	sign = null;
   } 
)+ (SP|HT)* RETURN
{
	z.setInputText(stopTracking());
        selectLexer("sdpLexer");

}
;
exception 
catch [ RecognitionException ex ] {
   z = (ZoneField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			z,SDPFieldNames.ZONE_FIELD, true);

}
catch [ TokenStreamException ex ] {
   z = (ZoneField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			z,SDPFieldNames.ZONE_FIELD, true);
}



// BUGBUG simplified specification.
time_fields returns [ TimeFieldList hl ] 
{
	hl = new TimeFieldList();
	startTracking();
	TimeField tf = null;
	RepeatField rf = null;
}
: ( options{greedy=true;}: tf = time_field { hl.add(tf);  } 
   (options{greedy=true;}: rf = repeat_field 
	{tf.addRepeatField(rf); hl.add(tf);})*)+

{
	hl.setInputText(stopTracking());
	selectLexer("sdpLexer");
}
;


repeat_field returns [ RepeatField rf ]
{
	startTracking();
	TypedTime tt = null;
	rf = new RepeatField();
}
:R_EQUALS tt = repeat_interval {rf.setRepeatInterval(tt); } 
	 SP tt = active_duration {rf.setActiveDuration(tt);}  
	( options{greedy = true; }: SP tt = offset {rf.addOffset(tt);} )+ 
	(SP|HT)* RETURN
{
	rf.setInputText(stopTracking());
	selectLexer("sdpLexer");
}
;
exception 
catch [ RecognitionException ex ] {
   rf = (RepeatField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			rf,SDPFieldNames.REPEAT_FIELD, true);

}
catch [ TokenStreamException ex ] {
   rf = (RepeatField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			rf,SDPFieldNames.REPEAT_FIELD, true);

}

active_duration returns [TypedTime tt] 
{ tt = null; }
:  tt = typed_time
;

offset returns [TypedTime tt]
{ tt = null; }
: tt = typed_time
;


repeat_interval returns [TypedTime tt ]
: tt =  typed_time
;

typed_time returns [ TypedTime tt ]
{
	startTracking();
	String  s = "";
	String u = null;
	tt = new TypedTime();
}
:(options{greedy=true;}: d:DIGIT { s += d.getText();} )+ 
( (D|H|M|S) => u = time_unit)?
{
	int time = Integer.parseInt(s);
	tt.setInputText(stopTracking());
	tt.setTime(time);
	tt.setUnit(u);
}
;

time_unit returns [ String s ] 
{ s = null; }
: D { s = "d"; } 
| H { s = "h"; } 
| M { s = "m"; }
| S { s = "s"; }
;

time_field returns [ TimeField t ]
{
	long s;
	long st;
	t = new TimeField();
	startTracking();
}
:  T_EQUALS s = start_time SP st = stop_time  
   { t.setStartTime(s); t.setStopTime(st); }
  (SP|HT)* RETURN 
  { 
	selectLexer("sdpLexer");
	t.setInputText(stopTracking()); 
  }
;
exception 
catch [ RecognitionException ex ] {
   t = (TimeField) handleParseException(ex,SDP_PARSE_EXCEPTION,
		t,SDPFieldNames.TIME_FIELD, true);
}
catch [ TokenStreamException ex ] {
   t = (TimeField) handleParseException(ex,SDP_PARSE_EXCEPTION,
				t,SDPFieldNames.TIME_FIELD, true);
}




key_field returns [ KeyField k ] 
{
	startTracking();
	k = new KeyField();
	String d;
	URI u;
}
: K_EQUALS 
( PROMPT 
{ 
	k.setInputText(stopTracking());
	k.setType(SDPKeywords.PROMPT); 
} 		  
|  BASE64 COLON d = key_data   
{
	k.setInputText(stopTracking());
	k.setType(SDPKeywords.BASE64);
	k.setKeyData(d);
}
|  URI COLON  u = uri_reference 
{
	k.setInputText(stopTracking());
	k.setType(SDPKeywords.URI);
	k.setURI(u);
} ) RETURN
{
   selectLexer("sdpLexer");
}

;
exception 
catch [ RecognitionException ex ] {
   k = (KeyField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			k,SDPFieldNames.KEY_FIELD, true);
}
catch [ TokenStreamException ex ] {
   k = (KeyField) handleParseException(ex,SDP_PARSE_EXCEPTION,
				k,SDPFieldNames.KEY_FIELD, true);
}

key_data returns [ String s ]:
	s = email_safe
;

attribute_fields returns [ AttributeFields nvlist ]
{
	nvlist = null;
	AttributeField nv = null;
	startTracking();
}
:(options{greedy=true;}: nv = attribute_field 
  {  
    if (nv != null)  {
	if (nvlist == null) nvlist = new AttributeFields();
	 nvlist.add(nv);
    }
  } 
)+
{ 
	nvlist.setInputText(stopTracking());
	selectLexer("sdpLexer");
}
;

attribute_field returns [ AttributeField af ] 
{ 
	af = new AttributeField();
	startTracking(); 
	NameValue nv = null;
}
: A_EQUALS nv = attribute 
(SP|HT)* RETURN
{ 

   selectLexer("sdpLexer");
   af.setInputText(stopTracking());
   af.setAttribute(nv);
}
;
exception 
catch [ RecognitionException ex ] {
   af = (AttributeField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			af,SDPFieldNames.ATTRIBUTE_FIELD, true);

}
catch [ TokenStreamException ex ] {
  af = (AttributeField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			af,SDPFieldNames.ATTRIBUTE_FIELD, true);
}


attribute returns [ NameValue nv ]
{
	nv = new NameValue();
	nv.setSeparator(Separators.COLON);
	String n = null;
	String v = null;
	startTracking();
}
: ((att_field COLON) =>  
	( n = att_field COLON   v = att_value) | n = att_field )
{
	nv.setName(n);
	nv.setValue(v);
	nv.setInputText(stopTracking());
}
;


media_field returns [ MediaField mf ] 
{
	mf = new MediaField();
	String m ;
	int  np = 0;
	String p = null;
	FormatList f;
	int po;
	startTracking();
}
: M_EQUALS m = media SP po = port ((SLASH)=> SLASH np = intnumber )?
  SP p = proto SP f = fmtlist
  (SP | HT )* RETURN 
   { 
     mf.setInputText(stopTracking());
     mf.setMedia(m);
     mf.setPort(po);
     mf.setNports(np);
     mf.setProto(p);
     mf.setFmt(f);
     selectLexer("sdpLexer"); 
   }
;
exception 
catch [ RecognitionException ex ] {
   mf = (MediaField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			mf,SDPFieldNames.MEDIA_FIELD, true);
}
catch [ TokenStreamException ex ] {
   mf = (MediaField) handleParseException(ex,SDP_PARSE_EXCEPTION,
			mf,SDPFieldNames.MEDIA_FIELD, true);
}



media_descriptions returns [ MediaDescriptionList hlist ]
{
	hlist  = new MediaDescriptionList();
	MediaField mf = null;
	InformationField iff = null;
	BandwidthField bf = null;
	ConnectionField cf = null;
	KeyField kf = null;
	AttributeFields af = null;
	startTracking();
}
: (
   (mf = media_field)
   (iff = information_field)?
   (cf = connection_field)?
   (bf = bandwidth_field)?
   (kf = key_field)?
   (af = attribute_fields)?  {
	MediaDescription m = new MediaDescription();
	m.setMediaField(mf);
	if (iff != null) m.setInformationField(iff);
	if (cf != null ) m.setConnectionField(cf);
	if (bf != null ) m.setBandwidthField(bf);
	if (kf != null ) m.setKeyField(kf);
	if (af != null ) m.setAttributeFields(af);
	hlist.add(m);
    }
)+
{
	hlist.setInputText(stopTracking());
}
;


// ;typically "audio", "video", "application" "data"
media returns [ String s ]
{
	s = "";
}
: t:ID { s = t.getText(); }
| (a:ALPHA {  s += a.getText(); } | d:DIGIT { s += d.getText(); } )+
;

ttl returns [ int i ] 
{
	String s = "";
	i = 0;
}
: (d:DIGIT { s += d.getText(); })+
{
	i = Integer.parseInt(s);
}
;

fmtlist returns [ FormatList f ]
{
	f = new FormatList();
	Format t;
	startTracking();
}
: t = fmt { f.add(t); } 
  ( options { greedy = true; } : SP t = fmt { f.add(t); } )*
{ f.setInputText(stopTracking()); }
;
                                                                  
fmt returns [ Format f ]
{
	f = new Format();
	String s = "";
} 
: ( t:ID { s = t.getText(); }
| n:NUMBER { s = n.getText(); }
| (a:ALPHA { s += a.getText(); }  | d:DIGIT { s += d.getText(); } )+ )
{ 
	f.setFormat(s);
	f.setInputText(s);
}
;


// typically "RTP/AVP" or "udp" for IP4
proto returns [ String p ]
{
	p = "";
}
:   ( a:ALPHA  { p += a.getText();} 
    | t:ID    { p += t.getText(); }
    | s:SLASH { p += s.getText(); }
    | d:DIGIT { p += d.getText(); } )+
;



att_field returns [ String s ]
{
	s = "";
}
:  t:ID { s = t.getText(); }
| (a:ALPHA { s += a.getText(); }  | n:NUM { s += n.getText(); } )+
;


att_value returns [ String s ] :
          s = byte_string
;


// should be unique for this originating username/host
sess_id returns [ long i ] :
	i = number
;

// 0 is a new session
sess_version returns [ long i ]:
	i = number
;

// check for multicast address here
connection_address returns [ ConnectionAddress a ]
{
  a  = new ConnectionAddress();
  Host ad;
  int t = 0;
  int p = 0;
}
: ad = host ((SLASH t = ttl) ( SLASH p = port)?)?
{
	a.setAddress(ad);
	a.setTtl(t);
	a.setPort(p);

}
;

start_time returns [ long t ] :
                    t = time 
;

stop_time returns [ long t ] :
                    t = time 
;

time returns [ long t ]
{
	t = 0;
	String s = "";
}
:  (d:DIGIT { s += d.getText(); } )+
{
     //check for 9 digits or 0 (special case)  sufficient for 2 more centuries
     if ( s.length() == 1) {
        if (s.compareTo("0") != 0 ) {
		throw new RecognitionException("Invalid time spec");
         } 
      } else if (s.length() > 10  ) {
		throw new RecognitionException("Invalid time spec should be at most 10 digits " + s );
      } 
      t =  Long.parseLong(s);
}
;






// TODO - this has to be compliant with the email address in the 
// RFC 

email returns [ Email a ]
{
	a = new Email();
	String n = "";
	String h = null;
	startTracking();
	
}
: (t1:ALPHA { n += t1.getText(); }  | t2:DIGIT { n += t2.getText(); } )+ 
	AT h = hostname
{
	a.setUserName(n);
	a.setHostName(h);
	a.setInputText(stopTracking());
}
;

email_address returns [ EmailAddress e ] 
{
	startTracking();
	e = new EmailAddress();
	String dn = null;
	Email ea  = null;
}
: ( 
  (email_safe LESS_THAN) => dn = email_safe LESS_THAN ea = email GREATER_THAN |
  (email LPAREN) => ea = email LPAREN dn = email_safe RPAREN 	              |
   ea = email        )
{
	e.setEmail(ea);
	e.setDisplayName(dn);
	e.setInputText(stopTracking());
}
;


safe returns [ String s ]
{
	s =  null;
} : 	s1:ALPHA 	{ s = s1.getText(); }
	| s3:DIGIT 	{ s = s3.getText(); }
	| s4:QUOTE 	{ s = s4.getText(); }
	| s5:BACK_QUOTE { s = s5.getText(); }
        | s6:MINUS      { s = s6.getText(); }
	| s7:DOT 	{ s = s7.getText(); }
	| s8:SLASH 	{ s = s8.getText(); }
	| s9:COLON 	{ s = s9.getText(); }
	| s10:QUESTION  { s = s10.getText(); }
        | s11:DOUBLE_QUOTE  { s = s11.getText(); }
	| s12:POUND 	{ s = s12.getText(); }
	| s13:DOLLAR	{ s = s13.getText(); }
        | s14:STAR 	{ s = s14.getText(); }
	| s15:SEMICOLON { s = s15.getText(); }
	| s16:EQUALS 	{ s = s16.getText(); }
	| s17:AND 	{ s = s17.getText(); }
	| s18:L_SQUARE_BRACKET { s = s18.getText(); }
        | s19:R_SQUARE_BRACKET { s = s19.getText(); }
	| s20:HAT 	{ s = s20.getText(); }
	| s21:UNDERSCORE { s = s21.getText(); }
	| s22:L_CURLY	{ s = s22.getText(); }
        | s23:BAR 	{ s = s23.getText(); }
	| s24:R_CURLY 	{ s = s24.getText(); }
	| s25:PLUS 	{ s = s25.getText(); }
	| s26:TILDE  	{ s = s26.getText(); }
;

email_safe returns [String s]
{
	s = "";
	String s1;
}
:
	( s1 = safe { s += s1; } 
	| t1:SP { s += t1.getText();} 
	| t2:HT { s += t2.getText();} )+
;

username returns [ String s ] 
{
	s = "";
	String s1;
}
: ( s1 = safe { s += s1; } )+
;

// List to be expanded
nettype returns  [ String s ] 
{
	s = null;
}
: IN
{
	s = SDPKeywords.IN;
}
;

// List to be expanded
addrtype returns [ String s ] 
{
	s = null;
}
:
	IPV4 { s = SDPKeywords.IPV4; } | IPV6 { s = SDPKeywords.IPV6; }
;





// b1 =less than "224"; not "0" or "127"
// b4 != 0  -- check these in action routine.

phone_number returns [ NameValue v ] 
{
	String n  = null;
	String p = null;
	v = new NameValue();
}
: ( 
    p = phone  
	( (LPAREN) => LPAREN n = email_safe RPAREN )?
   | (email_safe LESS_THAN) => 
	n = email_safe LESS_THAN p = phone GREATER_THAN 
 )
{
	v.setName(n);
	v.setValue(p);
}
;

phone returns [ String p ] 
{
	p = "";
}
:t1:PLUS { p += t1.getText(); }
 t2:DIGIT { p += t2.getText(); } 
( options { greedy = true; } : 
	t3:SP { p += t3.getText(); } | 
	t4:MINUS  { p += t4.getText(); } |
	t5:DIGIT { p += t5.getText(); } )+
;

bwtype  returns [ String p ] 
{
	p = "";
}
: ( a:ALPHA { p += a.getText(); } | d:DIGIT { p += d.getText(); }  )+
;

bandwidth returns [ int bw ] 
{
	bw = 0;
	String s = "";
}
: ( d:DIGIT  { s += d.getText(); } )+
{
	bw = Integer.parseInt(s);
}
;

