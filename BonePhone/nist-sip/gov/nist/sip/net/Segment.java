/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;
import gov.nist.sip.*;

/**
* Segment portion of a URI path.
*
*@version 1.0
*
*/
public class Segment extends NetObject {
    
        /** path field
         */    
	protected String  path;

        /** parmList list
         */        
        protected NetObjectList parmList;

        /** default constructor
         */        
	public Segment() {
		parmList = new NetObjectList("parmList");
	}
         
	/**
         * Encode into canonical form.
         * @return String to set
         */
	public String encode() {
		String encoding = path;
		if (! parmList.isEmpty()) {
			encoding += SEMICOLON + parmList.encode();
		}
		return encoding;
	}
	       
        /** get the path field.
         * @return String
         */        
	public	 String getPath() {
            return path ;
        } 
        
        /** get the parmList list
         * @return NetObjectList
         */        
	public	 NetObjectList getParmList()  {
            return parmList ;
        }
        
	/**
         * Set the path member
         * @param p String to set
         */
	public	 void setPath(String p) {
            path = p ;
        }
        
	/**
         * Set the parmList member
         * @param p NetObjectList to set
         */
	public	 void setParmList(NetObjectList p) {
            parmList = p ;
        }
       
}
