/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sdpfields;
import gov.nist.sip.*;
/**
* Internal utility class for pretty printing and header formatting.
*/
class Indentation {
	private int indentation;
	protected Indentation() { indentation = 0; }
	protected Indentation( int initval) { indentation = initval; }
	protected void setIndentation( int initval ) { indentation = initval; }
	protected int  getCount()  { return indentation; }
	protected void increment() { indentation ++ ; }
	protected void decrement() { indentation --;  }
	protected String getIndentation() {
		String retval = "";
		for (int i = 0; i < indentation; i++) retval += " ";
		return retval;
	}
}
