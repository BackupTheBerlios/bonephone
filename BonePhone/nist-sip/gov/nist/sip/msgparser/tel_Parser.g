header {
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

/**
* Since 1.0
* The grammar in this file is a simplified version of the grammar from
* RFC 2806.
*/

package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import java.lang.reflect.*;
import gov.nist.sip.*;
import gov.nist.sip.sipheaders.*;
}

class tel_Parser extends host_nameParser;

options {
	k=1;
	defaultErrorHandler=false;
	importVocab = parser_utilParser;
	exportVocab = tel_Parser;
}


base_phone_number returns [ String s ] 
{ 
	s = "";
	String t;
}
: (w:DIGIT { s += w.getText(); }  | t = visual_separator { s += t; } )+
;

local_number returns [ String s ]
{
	s = "";
	String t;
	String d;
}
: (w:DIGIT { s += w.getText(); }  
   | t = visual_separator { s += t; } 
   | d = dtmf_digit { s += d; }
)+
;


visual_separator returns [ String s ]
{ s = null; }
: m:MINUS  { s = m.getText(); }
| d:DOT	   { s = d.getText(); }
| l:LPAREN { s = l.getText(); }
| r:RPAREN { s = r.getText(); }
;

dtmf_digit returns [ String s ] 
{ s = null; }
: t1:STAR  { s = t1.getText(); }
| t2:POUND { s  = t2.getText(); }
| t3:ALPHA { s = t3.getText(); }
;




telephone_subscriber returns [ TelephoneNumber tn ] 
{
	startTracking();

}
: tn = global_phone_number 
| tn = local_phone_number
{
	tn.setInputText(stopTracking());

}
;

local_phone_number returns [ TelephoneNumber tn ]
{
	tn = new TelephoneNumber();
	tn.setGlobal(false);
	NameValueList nv = null;
	String b = null;
}
: b = local_number { tn.setPhoneNumber(b); }
( (SEMICOLON)=> SEMICOLON  { selectLexer("tel_urlLexer"); }
nv = tel_parameters { tn.setParameters(nv); } )?
;


global_phone_number returns [ TelephoneNumber tn ]
{
	tn = new TelephoneNumber();
	tn.setGlobal(true);
	NameValueList nv = null;
	String b = null;
}
:PLUS b = base_phone_number  { tn.setPhoneNumber(b); }
( (SEMICOLON)=> SEMICOLON { selectLexer("tel_urlLexer"); }
	 nv = tel_parameters { tn.setParameters(nv); } )?
;

tel_parameters returns [ NameValueList nv ]
{
	nv  = new NameValueList("telparms");
	String s = null;
	NameValueList nv1;
	startTracking();
}
:( (ISUB)=>  ISUB EQUALS s = byte_string_no_semicolon 
	{ nv.add(TelKeywords.ISUB, s); } 
    ( (SEMICOLON) => SEMICOLON nv1 = tel_parameters1 
	{nv.concatenate(nv1); } )?

| nv = tel_parameters1 )
{ nv.setInputText(stopTracking()); }
;



tel_parameters1 returns [ NameValueList nv ]
{
	nv = new NameValueList("telparms");
	NameValueList nv1 = null;
	String s  = null;
	startTracking();
}
: ( (POSTDIAL)=>  POSTDIAL EQUALS s = byte_string_no_semicolon
	{ nv.add(TelKeywords.POSTDIAL, s); }
    ((SEMICOLON) => SEMICOLON nv1 = tel_parameters2
	{nv.concatenate(nv1); })? 
| nv = tel_parameters2 )
{
	nv.setInputText(stopTracking());
}
;

tel_parameters2 returns [ NameValueList nv ]
{

	nv = new NameValueList("telparms");
	NameValue n = null;
	startTracking();
}
: n = tel_parameter { nv.add(n); } 
	(SEMICOLON  n = tel_parameter { nv.add(n); } )* 
{
	nv.setInputText(stopTracking());
}
;

tel_parameter returns [ NameValue nv ] 
: nv = service_provider
| nv = area_specifier
| nv = future_extension
;


service_provider returns [ NameValue nv ]
{
	nv = new NameValue();
	Host h;
	startTracking();
	
}

: PROVIDER_TAG EQUALS h = host
{
	nv.setName(TelKeywords.PROVIDER_TAG);
	nv.setValue(h);
	nv.setInputText(stopTracking());
}
;

area_specifier returns [ NameValue nv ]
{
	nv = new NameValue();
	String s = null;
	startTracking();

}
: PHONE_CONTEXT_TAG EQUALS s = byte_string_no_semicolon
{ 
	nv.setName(TelKeywords.PHONE_CONTEXT_TAG);
	nv.setValue(s);
	nv.setInputText(stopTracking());
}
;



future_extension returns [ NameValue nv ]
{

	nv = new NameValue();
	startTracking();
	String s;
}
: t:ID EQUALS s = byte_string_no_semicolon
{

	nv.setName(t.getText());
	nv.setValue(s);
	nv.setInputText(stopTracking());
}
;
