/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
/******************************************************
 * File: MyStringTokenizer.java
 * created 09-Oct-00 11:45:04 PM by mranga
 */


package gov.nist.sip.stack.security;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;


public class MyStringTokenizer extends StringTokenizer
{
	private String myDelimiter;
	
	public MyStringTokenizer( String string, String delim) {
		super(string,delim,true);
		myDelimiter = delim;
	}
	
	public String nextToken() {
		String dl1;
		String dl2;
		try {
			dl1 = super.nextToken();
		} catch (NoSuchElementException ex1) {
			return null;
		}
		if (dl1.compareTo(myDelimiter) == 0) {
		  return null;
		} else {
		    // Consume a delimeter.
		    try {
		   	 dl2 = super.nextToken();
		    } catch (NoSuchElementException ex2) {
		    }   
		    return dl1;
		}
	}

}
