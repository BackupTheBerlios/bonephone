/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: O. Deruelle (deruelle@nist.gov)                                     *
* Modified by: M. Ranganathan (mranga@nist.gov) : Changes caused by removing   *
* one level of hierarcy from the UserAgent class (i.e. got rid of              *
* UserAgentProduct and UserAgentServer from the nist-sip header classes.)      *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Iterator;

/**
* Implementation of the UserAgentHeader interface of jain-sip.
*/
public final class UserAgentHeaderImpl extends HeaderImpl
implements UserAgentHeader, NistSIPHeaderMapping {

   
    /** Default constructor
     */    
   public UserAgentHeaderImpl() { 
       super();
            this.headerName = name;
   }

    /** Constructor
     * @param userAgent UserAgent to set
     */   
   public UserAgentHeaderImpl(UserAgent userAgent) { 
       super(userAgent);
            this.headerName = name;
   }
     
    /**
    * Gets products of ProductHeader
    * (Note that the Objects returned by the Iterator are Strings)
    * @return products of ProductHeader (null if no product tokens exist).
    */
    public Iterator getProducts() {
        UserAgent userAgent = (UserAgent) sipHeader;
	if (! userAgent.isProduct()) 
		return null;
        else {
            Product product=userAgent.getProduct();
            if (product==null) return null;
            else {
                 LinkedList list=product.getProductTokenList(); 
                 if (list==null) return null;
                 else return list.iterator();
            }
        }
    }

    
    /**
     * Set products of ProductHeader
     * (Note that the Objects in the List must be Strings)
     * @param products List to set
     * @throws IllegalArgumentException if products is null, empty, or contains
     * any null elements, or contains any non-String objects
     * @throws SipParseException if any element of products is not accepted by
     * implementation
     */
    public void setProducts(List products) throws IllegalArgumentException,
    SipParseException {
          UserAgent userAgent= (UserAgent) sipHeader;
          if (  products==null || products.isEmpty())
              throw new IllegalArgumentException
              ("JAIN-SIP EXCEPTION: products is null");
          else {
              try {
                     userAgent.setProductTokenList(products);
              }
              catch (Exception e) {
                    // Throws IllegalArgumentException if a element of products is not 
                    // a String.
                    throw new IllegalArgumentException
                 ("JAIN-SIP EXCEPTION:any element of products is not accepted");
                }
          }
    }
    
}
