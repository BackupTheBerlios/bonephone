package org.mbus;

import java.util.*;
import java.io.*;

/**
 * Parser.java
 *
 * @author Stefan Prelle  <prelle@tzi.de>
 * @version $id: Parser.java 1.0 Mon Apr 17 09:36:54 2000 prelle Exp $
 */

public class Parser  {
    
    int pos;
    private MList ret;

    //---------------------------------------------------------------
    public Parser() throws MBusParsingException {
	pos = 1;
	ret = new MList();
    }

    //---------------------------------------------------------------
    public Parser(int pos) throws MBusParsingException {
	this.pos = pos;
	ret = new MList();
    }

    //---------------------------------------------------------------
    public MList parse(String data) throws MBusParsingException {
	while (pos<data.length()) {
	    char c = data.charAt(pos);
	    switch(c) {
	    case ')':
		pos++;
		return ret;
	    case ' ': // Whitespace
		pos++;
		break;
	    case '\"':
		pos++;
		ret.add(parseAsString(data));
		break;
	    case '(':
		pos++;
		Parser intern = new Parser(pos);
		ret.add(intern.parse(data));
		pos = intern.pos;
		break;
	    case '<':
		pos++;
		ret.add(parseAsData(data));
		break;
	    case '0': case '1': case '2': case '3': case '4':
	    case '5': case '6': case '7': case '8': case '9':
		ret.add(parseAsNumber(data));
		break;
	    default:
		if (Character.isLetterOrDigit(c) ||
		    c=='-' || c=='_' || c=='.') {
		    ret.add(parseAsSymbol(data));
		} else
		    throw new MBusParsingException("Invalid character: "+c,data,pos);
	    }
	} // while
	throw new MBusParsingException("Unexpected end of data",data,pos);
    }

    //---------------------------------------------------------------
    private MString parseAsString(String data) throws MBusParsingException {
	StringBuffer ret = new StringBuffer();
	boolean escape = false;
	while (pos<data.length()) {
	    if (escape) {
		// We are in ESCAPE-Mode
		escape = false;
		switch (data.charAt(pos)) {
		case '\"':  // append "
		    // End of String
		    ret.append('\"');
		    break;
		case 'n': // append CR
		    // Return code
		    ret.append('\n');
		    break;
		case '\\': // append backslash
		    // Escape sign
		    ret.append('\\');
		    break;
		case ' ': // append space
		    // Escape sign
		    ret.append(' ');
		    break;
		default:
		    throw new MBusParsingException("Unknown escape char: \\"+data.charAt(pos),
					    data, pos);
		}
		pos++;
	    } else {
		// We are in NORMAL-Mode
		switch (data.charAt(pos)) {
		case '\"':
		    // End of String
		    pos++;
		    return new MString(ret.toString());
		case '\\':
		    // Enter ESCAPE-Mode
		    escape = true;
		    break;
		default:
		    ret.append(data.charAt(pos));
		}
		pos++;
	    } // if (mode) 
	} // while
	throw new MBusParsingException("Unexpected end of string",data,pos);
    }

    //---------------------------------------------------------------
    private MDataType parseAsNumber(String data) throws MBusParsingException {
	boolean flo = false;
	StringBuffer ret = new StringBuffer();
	while (pos<data.length()) {
	    char c = data.charAt(pos);
	    if (Character.isDigit(c)) {
		ret.append(c);
		pos++;
	    } else if (c=='.') {
		if (flo)
		    throw new MBusParsingException("No more than one . in floats", data, pos);
		flo = true;
		ret.append(c);
		pos++;
	    } else {
		String num = ret.toString();
		try {
		    if (flo) {
			float d = Float.valueOf(num).floatValue();
			return new MFloat(d);
		    } else {
			int i = Integer.parseInt(num);
			return new MInteger(i);
		    }
		} catch (NumberFormatException nfe) {
		    throw new MBusParsingException("Not a float: "+num, data, pos);
		}
	    }
	}
	throw new MBusParsingException("Unexpected end of number",data,pos);
    }

    //---------------------------------------------------------------
    private MSymbol parseAsSymbol(String data) throws MBusParsingException {
	StringBuffer ret = new StringBuffer();
	while (pos<data.length()) {
	    char c = data.charAt(pos);
	    if (Character.isLetterOrDigit(c) || c=='-' || c=='_' || c=='.') {
		ret.append(c);
		pos++;
	    } else if (c==' ') {
		pos++;
		return new MSymbol(ret.toString());
	    } else if (c==')') {
		return new MSymbol(ret.toString());
	    } else {
		throw new MBusParsingException("Character isn't allowed: "+c,data,pos);
	    }
	}
	throw new MBusParsingException("Unexpected end of symbol",data,pos);
    }

    //---------------------------------------------------------------
    private MData parseAsData(String data) throws MBusParsingException {
	StringBuffer ret = new StringBuffer();
	while (pos<data.length()) {
	    char c = data.charAt(pos);
	    if (c=='>') {
		try {
		    byte[] tmp = Base64Codec.decode(ret.toString().getBytes());
		    pos++;
		    return new MData(tmp);
		} catch (ArrayIndexOutOfBoundsException aie) {
		    throw new MBusParsingException("Error parsing MData '"+data.toString()+"': "+aie, data,pos);
		}
	    } else {
		ret.append(c);
		pos++;		
	    }
	}
	throw new MBusParsingException("Unexpected end of data",data,pos);
    }

    //---------------------------------------------------------------
    public static void main(String[] args) throws MBusParsingException {
	String data = "\"Hallo\\\" Welt\" (\"Te\\nst\" 1234) Test_Foo 1234.567 <ABCDEFGH>)";
	Parser parser = new Parser();
	System.out.println("Origin: ("+data);
	System.out.println("Parsed: "+parser.parse(data));
    }
    
} // Parser
