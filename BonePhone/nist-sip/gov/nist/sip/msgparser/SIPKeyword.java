
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

/**
* Package local class to track sip keywords in lexer contexts.
* @since v1.0
*/
package gov.nist.sip.msgparser;
import java.util.Hashtable;
class  SIPKeyword { 
	String name;
	int    tokenVal ;
	int	lexerContext; // context in which this keyword is valid
	SIPKeyword ( int t , String n  ) {
			name = n; 
			tokenVal = t;
			lexerContext = 0;
	}

	SIPKeyword ( int t , String n, int ctx ) {
			name = n; 
			tokenVal = t;
			lexerContext = ctx;
	}
}
