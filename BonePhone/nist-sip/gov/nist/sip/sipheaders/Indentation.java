/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;

/**
* Internal utility class for pretty printing and header formatting.
*/
class Indentation {
    
	private int indentation;
        
        /** Default constructor
         */        
	protected Indentation() { 
            indentation = 0;
        }
        
        /** Constructor
         * @param initval int to set
         */        
	protected Indentation( int initval) { 
            indentation = initval;
        }
        
        /** set the indentation field
         * @param initval int to set
         */        
	protected void setIndentation( int initval ) {
            indentation = initval;
        }
        
        /** get the number of indentation.
         * @return int
         */        
	protected int  getCount()  { 
            return indentation; 
        }
            
        /** increment the indentation field
         */        
	protected void increment() { 
            indentation ++ ;
        }
        
        /** decrement the indentation field
         */        
	protected void decrement() { 
            indentation --;
        }
        
        /** get the indentation
         * @return String
         */        
	protected String getIndentation() {
		String retval = "";
		for (int i = 0; i < indentation; i++) retval += " ";
		return retval;
	}
        
}
