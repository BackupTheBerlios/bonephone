package org.mbus;

import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.IOException;

/**
 * Parent-class for all MBus-Data.
 *
 * @author Stefan Prelle
 * @version $Id: MDataType.java,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

public abstract class MDataType extends Object implements Comparable {
    
    //-----------------------------------------------------------------
    public static MDataType[] parse(String data) {
	StreamTokenizer str = new StreamTokenizer(new StringReader(data));
	str.resetSyntax();
	str.parseNumbers();
	str.whitespaceChars(32,32);
	str.wordChars(33,33); // !
	str.quoteChar('\"');
	str.wordChars(35,39); // # .. ;
	//str.wordChars(40,41); // (..)
	str.wordChars(42,47); // Spare digits
	str.wordChars(57,59); // Spare digits
	//str.wordChars(60,60); // <
	str.wordChars(61,61); // =
	//tok.wordChars(62,62); // >
	str.wordChars(63,94); // ? .. ^
	str.wordChars(95,95); // _
	str.wordChars(96,16385);
	try {
	    MDataType mdt = MList.parse(str).elementAt(0);
	    MList foo = (MList)mdt;
	    MDataType[] ret = new MDataType[foo.size()];
	    for (int i=0; i<foo.size(); i++)
		ret[i] = foo.elementAt(i);
	    return ret;
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	    throw new MBusParsingException(ioe.toString());
	}
    }
    
    //-----------------------------------------------------------------
    public static void main(String[] args) {
	MDataType[] foo = (MDataType[])parse("(\"gui-00001\" POINT_TO_POINT (\"h323:mtx93@iptel1003\") () (\"h323:highman@informatik.uni-bremen.de\") 1280 1 <MQ==> <MQ==> 0  0 )");
	for (int i=0; i<foo.length; i++)
	    System.out.println(i+": "+foo[i].getClass().getName()+"  "+foo[i]);
    }
    

} // MDataType
