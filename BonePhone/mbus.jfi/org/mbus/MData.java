package org.mbus;

import java.io.*;

/**
 * MData.java
 *
 *
 * <br>Created: Wed Sep 15 18:15:16 1999
 *
 * @author Stefan Prelle
 * @version $id: MData.java 1.0 Wed Sep 15 18:15:16 1999 prelle Exp $
 */

public class MData extends MDataType {
    
    private String data;

    //---------------------------------------------------------------
    public MData() {
	data = null;
    }

    //---------------------------------------------------------------
    public MData(byte[] dat) {
	data = new String(dat);
    }

    //---------------------------------------------------------------
    public MData(String dat) {
	data = dat;
    }

    //---------------------------------------------------------------
    public String getData() {
	return data;
    }

    //---------------------------------------------------------------
    public byte[] getBytes() {
	return data.getBytes();
    }
    
    //---------------------------------------------------------------
    public int compareTo(Object obj) {
	String s = ((MData)obj).getData();
	return data.compareTo(s);
    }

    //---------------------------------------------------------------
    public String toString() {
	byte[] code = Base64Codec.encode(data.toString().getBytes());
	return "<"+(new String(code))+">";
    }

    //---------------------------------------------------------------
    static MData parse(StreamTokenizer tok) throws IOException {
	tok.resetSyntax();
	tok.wordChars(64,127);
	tok.wordChars(33,61);
	
	StringBuffer data = new StringBuffer();
	int type = StreamTokenizer.TT_WORD;
	type = tok.nextToken();
//  	do {
//  	    System.out.println("&&&&&&!&&&&& "+type+" / "+tok.sval);
	    try {
		data.append(tok.sval);
	    } catch (OutOfMemoryError oom) {
		throw new MBusParsingException("Not enough memory for data - length is "+data.length()+"\n"+data.toString().substring(0,100));
	    }
  	    type = tok.nextToken();
//  	} while (type!='>' && type!=StreamTokenizer.TT_EOL);
	
	MData ret = null;
	try {
	    ret = new MData(Base64Codec.decode(data.toString().getBytes()));
	} catch (ArrayIndexOutOfBoundsException aie) {
	    throw new MBusParsingException("Error parsing MData '"+data.toString()+"': "+aie);
	}
	tok.resetSyntax();
	tok.wordChars(64,127);
	tok.whitespaceChars(0,32);
	tok.parseNumbers();
	return ret;
    }
    
} // MData
