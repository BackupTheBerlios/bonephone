/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: QuotedString.java
 * created 27-Sep-00 7:01:13 PM by mranga
 *
 */


package gov.nist.sip.stack;

/**
* A few utilities that are used in various places by the stack.
*/

public class ServerUtils 
{
     /**
     * to hex converter
     */
    private static final char[] toHex = { '0', '1', '2', '3', '4', '5', '6',
                                          '7', '8', '9', 'a', 'b', 'c', 'd',
                                          'e', 'f' };
 
    /**
     * convert an array of bytes to an hexadecimal string
     * @return a string
     * @param b bytes array to convert to a hexadecimal
     * string
 */
 
    public static String toHexString(byte b[]) {
        int pos = 0;
        char[] c = new char[b.length*2];
        for (int i=0; i< b.length; i++) {
            c[pos++] = toHex[(b[i] >> 4) & 0x0F];
            c[pos++] = toHex[b[i] & 0x0f];
        }
        return new String(c);
    }  
    
    /**
     * Put quotes around a string and return it.
     * @return a quoted string
     * @param str string to be quoted
 */
    protected static String getQuotedString ( String str) {
	return  '"' + str + '"';
    }
    

    /**
    * Squeeze out white space from a string and return the reduced string.
    * @param input input string to sqeeze.
    * @return String a reduced string.	
    */
    protected static String reduceString( String input) {
	    String newString = input.toLowerCase();
	    int len = newString.length();
	    String retval = "";
	    for (int i = 0; i < len ; i++ ) { 
                if ( newString.charAt(i) == ' ' 
			|| newString.charAt(i) == '\t' ) 
		  	continue;
		  else retval += newString.charAt(i);
     	    }
	    return retval;
	}			  
    
	

}
