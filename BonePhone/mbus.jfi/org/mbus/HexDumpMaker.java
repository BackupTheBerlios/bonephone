package org.mbus;

/**
 * HexDumpMaker.java
 *
 *
 * Created: Thu Jan 28 14:25:55 1999
 *
 * @author Stefan Prelle
 * @version 1.0
 */

public class HexDumpMaker  {
    
    //------------------------------------------------
    /**
     * Resizes a String to a higher length. This enlargement 
     * is received by appending a certain amount of characters
     * to the string.<br>
     * If the string is larger than the wanted size no 
     * modification is done.
     *
     * @param data String to enlarge
     * @param width Wanted length
     * @param dummy Character to append
     * @return Modified string.
     */ 
    public static String enlarge(String data, int width, char dummy) {
	if (data.length()>=width)
	    return data;
	
	StringBuffer foo = new StringBuffer();
	for (int i=0; i<(width-data.length()); i++)
	    foo.append(dummy);
	foo.append(data);
	return foo.toString();
    }
    
    //------------------------------------------------
    /** 
     * Converts a byte (-127 .. +127) into an integer
     * (0..255).
     *
     * @param b Byte to convert
     * @return Converted number.
     */
    public static int convertByteToInt(byte b) {
	if (b<0)
	    return (256+b);
	else
	    return b;
    }
    
    //------------------------------------------------
    /**
     * Converts an integer into an ASCII-Representation
     * suitable for using it into a HexDump. Control-Codes
     * are represented as '.'.
     *
     * @param i Value to display
     * @return Character-Representation
     */
    protected static char getCharFor(int i) {
	if (i<0) i= convertByteToInt( (byte)i );
	
	char c = (char)i;
	if (Character.isISOControl(c))
	    return '.';
	else if (c<32)
	    return '.';
	else
	    return c;
    }

    //------------------------------------------------
    /**
     * Build a string containing a HexDump of a bytebuffer.
     * It is ensured that a maximum length is not ...
     * 
     * @param data Bytebuffer that shall be dumped
     * @param max  Maximum number of bytes to display
     * @return String containing the hexdump
     */
    public static String makeHexDump(byte[] data, int max) {
	StringBuffer buf = new StringBuffer();
	
	int noLines = max / 16 +1;
	int toRead = max;

	for (int line=0; line<noLines; line++) {
	    StringBuffer out = new StringBuffer();
	    for (int i=0; i<16; i++) {
		int tmp = 0;
		int val = 0;
		if ((line*16+i)<data.length) {
		    tmp = (i<=toRead)?data[line*16+i]:0;
		    val = convertByteToInt((byte)tmp);
		    buf.append( enlarge( Integer.toHexString(val), 2, '0'));
		} else 
		    buf.append( "--" );
		
		if ((i%2)==1)
		    buf.append(" ");

		out.append(getCharFor(val));
	    }
	    buf.append(" "+out+"\n");
	    toRead -= 16;
	}
	return buf.toString();
    }
    
    //------------------------------------------------
    /**
     * Build a string containing a HexDump of a bytebuffer.
     * 
     * @param data Bytebuffer that shall be dumped
     * @return String containing the hexdump
     */
    public static String makeHexDump(byte[] data) {
	return makeHexDump(data, data.length);
    }
    
} // HexDumpMaker
