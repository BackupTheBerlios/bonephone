/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: O. Deruelle (deruelle@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import java.util.*;
import gov.nist.sip.msgparser.*;

/**
* Implementation of the ServerHeader interface of jain-sip.
*/
public final class ServerHeaderImpl extends HeaderImpl
implements ServerHeader, NistSIPHeaderMapping {

    /** Default constructor
     */    
    public ServerHeaderImpl() { 
        super();
            this.headerName = name;
    }

    /** constructor
     * @param server Server to set
     */    
    public ServerHeaderImpl(Server server) { 
        super(server);
            this.headerName = name;
    }  
    
    /**
    * Gets products of ProductHeader
    * (Note that the Objects returned by the Iterator are Strings)
    * @return products of ProductHeader
    */
    public Iterator getProducts() {
      
        Server server=(Server)sipHeader;
        if (! server.isProduct() ) {
         
		return null;
        }
        Product product=server.getProduct();
        if ( product==null) {
         
            return null;
        }
        else {
             LinkedList l=product.getProductTokenList();
            
             return l.iterator();
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
    public void setProducts(List products) 
    throws IllegalArgumentException, SipParseException {
        
           Server server=(Server)sipHeader;
           if (  products==null || products.isEmpty())
              throw new IllegalArgumentException
              ("JAIN-SIP EXCEPTION: products is null");
          else {
              try {
                 
                  server.setProductTokenList(products);
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


