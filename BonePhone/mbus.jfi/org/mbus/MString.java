package org.mbus;

import java.util.*;

/**
 * MString.java
 *
 * @author Stefan Prelle
 * @version $id: MString.java 1.0 Wed Sep 15 18:05:05 1999 prelle Exp $
 */

public class MString extends MDataType {
    
    private String data;

    //---------------------------------------------------------------
    public MString() {
	data = null;
    }

    //---------------------------------------------------------------
    public MString(String dat) {
	data = dat;
    }

    //---------------------------------------------------------------
    public static MString parseAsString(String tmp) {
	System.out.println("parseAsString: ["+tmp+"]");
	String fo1 = MString.replace(tmp,"\\\"","\"");
	String fo2 = MString.replace(fo1,"\\n","\n");
	System.out.println("parseAsString->["+fo2+"]");
	return new MString(fo2);
    }
    
    //---------------------------------------------------------------
    public String getData() {
	return data;
    }
    
    //-----------------------------------------------------------------
    public int compareTo(Object o) {
	String foo = ((MString)o).getData();
	return data.compareTo(foo);
    }

    //-----------------------------------------------------------------
    protected static String replace(String orig, String old, String neu) {
	StringBuffer buf = new StringBuffer();
	int pos = 0;
	while (orig.indexOf(old, pos)>=pos) {
	    int pos2 = orig.indexOf(old, pos);
	    String upTo = orig.substring(pos, pos2);
	    buf.append(upTo);
	    buf.append(neu);
	    pos = pos2+old.length();
	}
	buf.append(orig.substring(pos, orig.length()));
	return buf.toString();
    }

    //-----------------------------------------------------------------
    protected static String substAll(String orig, char old, String neu) {
	StringBuffer buf = new StringBuffer();
	StringTokenizer tok = new StringTokenizer(orig, ""+old);
	while (tok.hasMoreTokens()) {
	    String foo = tok.nextToken();
	    buf.append(foo);
	    if (tok.hasMoreTokens())
		buf.append(neu);
	}

	return buf.toString();
    }
    
    //-----------------------------------------------------------------
    public String toString() {
	String tmp1 = replace(data, "\"","\\\"");
	String tmp2 = replace(tmp1, "\n","\\n");
	return '"'+tmp2+'"';
    }
   
} // MString
