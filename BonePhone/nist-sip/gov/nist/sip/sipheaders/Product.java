/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                    
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import java.util.List;
import java.util.*;

/**
* Product specification.
* @see UserAgentProduct
*/
public class Product extends SIPObject {
    
        /** productToken field
         */    
	protected String productToken;
       
        /**
         * Return canonical form.
         * @return String
         */      
	public String encode() {
		return productToken;
	}

	/**
         * This returns the product token as a list of strings 
         * (that are suppoed to be delimited by slashes).
         * Product tokens are not supposed to have spaces in them but they
         * tend to be very free form and so we will parse by hand.
         * This is extremely painful but we go by the adage - "be generous
	 * in what you accept and be stingy in what you send (or something
	 * of that nature)"
         * @return LinkedList
         */
	public LinkedList getProductTokenList() {
		String component;
		int counter = 0;
		int startIndex = 0;
		int endIndex ;
		int ind ;

		if (productToken == null) return null;
		LinkedList retval = new LinkedList();

		while (startIndex < productToken.length()) {
			ind = productToken.indexOf('/',startIndex);
			if (ind == -1)
			   endIndex = productToken.length();
			else 
			   endIndex = ind;
			component = 
				productToken.substring(startIndex,endIndex);
			// Odd entry, so furthrer split this by
			// spaces (unless this is the last entry!) :-(
			if (counter % 2 == 1 && 
			    endIndex != productToken.length()) {
			    StringBuffer sb = new StringBuffer();
			    int i  = 0;
			    while( i < component.length() ) {
			        while(component.charAt(i) == ' ' ||
				      component.charAt(i) == '\t') {
				      i++;
				      if (i >= component.length()) break;
			        }
				while(component.charAt(i) != ' '  &&
				      component.charAt(i) != '\t' ) {
				      sb.append(component.charAt(i));
				      i++;
				      if (i >= component.length()) break;
				 }
				 if ( sb.length() != 0) 
					retval.add(sb.toString());
			     }
			} else {
			    retval.add(component);
			}
		        startIndex = endIndex + 1;
			counter ++;
		}
		return retval;

	}

        /** get the ProductToken field
         * @return String
         */        
        public String getProductToken() {
		return productToken;
	}

        /** set the productToken field
         * @param pt String to set
         */        
        public void setProductToken(String pt ) {
		productToken = pt;
	}

        
	/**
         * Set the product token list.
         * @param products list to set
         * @throws IllegalArgumentException if List contains null product
         */    
        public void setProductTokenList(List products) throws 
	  IllegalArgumentException{
            StringBuffer sb=new StringBuffer();
            for (int i=0;i<products.size();i++)
            {
                Object o=products.get(i);
                if (o==null || ( !(o instanceof String) ) ) {
                   
                    throw new IllegalArgumentException("Null product");
                }
                else{
                           sb.append(o+"/"); 
		}
            }
           
            setProductToken(sb.toString());
        }
        
	/**
	* Encode into canonical form.
	* public String encode() {
	*	String encoding = null;
	*	ListIterator li = productTokens.listIterator();
	*	while(li.hasNext()) {
	*	   ProductToken pt = (ProductToken) li.next();
	*	   if (encoding  != null) {
	*	     encoding = pt.encode();
	*	   } else {
	*		encoding += SP + pt.encode();
	*	   }
	*	}
	*	return encoding;
	* }
	*
	*/

	
}
