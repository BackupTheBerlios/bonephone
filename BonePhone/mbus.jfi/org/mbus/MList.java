package org.mbus;

import java.util.*;
import java.io.*;

/**
 * Implementation of the MBus list-type.
 *
 * @author Stefan Prelle
 * @version $Id: MList.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public class MList extends MDataType {

    private Vector data;
    
    //-----------------------------------------------------------------
    public MList() {
	data = new Vector();
    }
    
    //-----------------------------------------------------------------
    public MList(MDataType[] param) {
	data = new Vector(param.length);
	for (int i=0; i<param.length; i++)
	    data.addElement(param[i]);
    }
    
    //-----------------------------------------------------------------
    public MList(Vector param) {
	data = param;
    }
    
    //-----------------------------------------------------------------
    public int compareTo(Object o) {
	MList ml = (MList)o;
	Vector f = ml.getData();
	
	int i=0;
	while (true) {
	    if (data.size()==f.size() && f.size()>i) return 0;
	    if (i>=data.size() && f.size()>i) return -1;
	    if (i>=f.size() && data.size()>i) return  1;
	    
	    Comparable c1 = (Comparable)data.elementAt(i);
	    Comparable c2 = (Comparable)f.elementAt(i);
	    int val = c1.compareTo(c2);
	    if (val!=0)
		return val;
	    i++;
	}
    }
    
    //-----------------------------------------------------------------
    public String toString() {
	StringBuffer buf = new StringBuffer("(");
	Enumeration e = data.elements();
	while (e.hasMoreElements()) {
	    buf.append( ((MDataType)e.nextElement())+"" );
	    if (e.hasMoreElements())
		buf.append(' ');
	}
	buf.append(')');
	return buf.toString();
    }
    
    //-----------------------------------------------------------------
    public Enumeration elements() {
	return data.elements();
    }
    
    //-----------------------------------------------------------------
    Vector getData() {
	return data;
    }
    
    //-----------------------------------------------------------------
    public int size() {
	return data.size();
    }
    
    //-----------------------------------------------------------------
    public MDataType elementAt(int idx) {
	return (MDataType)data.elementAt(idx);
    }
    
    //-----------------------------------------------------------------
    static MList parse(StreamTokenizer tok) throws IOException, MBusParsingException {
	Vector tmp = new Vector();
	tok.quoteChar('\"');
	int type = tok.nextToken();
	while (type!=')' && type!=StreamTokenizer.TT_EOL && type!=StreamTokenizer.TT_EOF) {
	    switch (type) {
	    case '(':
		MList list = (MList)MList.parse(tok);
		tmp.addElement(list);
		break;
	    case '"':
		MString str = MString.parseAsString(tok.sval);
		tmp.addElement(str);
		break;
	    case '<':
		MData dat = MData.parse(tok);
		tmp.addElement(dat);
		tok.resetSyntax();
		tok.parseNumbers();
		tok.whitespaceChars(32,32);
		tok.wordChars(33,33); // !
		tok.quoteChar('\"');
		tok.wordChars(35,39); // # .. ;
		//tok.wordChars(40,41); // (..)
		tok.wordChars(42,47); // Spare digits
		tok.wordChars(57,59); // Spare digits
		//tok.wordChars(60,60); // <
		tok.wordChars(61,61); // =
		//tok.wordChars(62,62); // >
		tok.wordChars(63,94); // ? .. ^
		tok.wordChars(95,95); // _
		tok.wordChars(96,16385);
		break;
	    case StreamTokenizer.TT_NUMBER:
		int tmp1 = (int)tok.nval;
		double tmp2 = tok.nval - tmp1;
		if (tmp2>0) {
		    MFloat fl = new MFloat((float)tok.nval);
		    tmp.addElement(fl);
		} else {
		    MInteger inte = new MInteger(tmp1);
		    tmp.addElement(inte);
		}
		break;
	    case StreamTokenizer.TT_WORD:
		// Symbol
		MSymbol sym = new MSymbol(tok.sval);
		tmp.addElement(sym);
		break;
	    default:
		throw new MBusParsingException("Couldn't determine datatype: "+(char)type+"/"+tok.sval+"/"+type+"\nUntil now: "+(new MList(tmp)));
	    }
	    type = tok.nextToken();
	}

	MList ret = new MList(tmp);
//  	System.out.println("/////////"+ret);
	return ret;
    }
    
    //-----------------------------------------------------------------
    public void add(MDataType type) {
	data.addElement(type);
    }
    
    //-----------------------------------------------------------------
    public void add(int num) {
	data.addElement(new MInteger(new Integer(num)));
    }
    
    //-----------------------------------------------------------------
    public void add(String str) {
	data.addElement(new MString(str));
    }
    
    //-----------------------------------------------------------------
    public String getString(int idx) {
	return ((MString)data.elementAt(idx)).getData();
    }
    
    //-----------------------------------------------------------------
    public MDataType getType(int idx) {
	return (MDataType)data.elementAt(idx);
    }
    
    //-----------------------------------------------------------------
    public int getInt(int idx) {
	return ((MInteger)data.elementAt(idx)).intValue();
    }
    
    //-----------------------------------------------------------------
    public MDataType[] toArray() {
	MDataType[] ret = new MDataType[data.size()];
	for (int i=0; i<ret.length; i++)
	    ret[i] = getType(i);
	return ret;
    }


} // MList
