/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) ,added JAVADOC                 *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import java.util.List;
/**
* Server response header.
*@since 1.0
*Removed ServerComment and ServerProduct under the hierachy removal act of
*2001.
*
* <pre>
*
* RFC 2616                        HTTP/1.1                       June 1999
*
* 3.8 Product Tokens
* 
*   Product tokens are used to allow communicating applications to
*  identify themselves by software name and version. Most fields using
*  product tokens also allow sub-products which form a significant part
*  of the application to be listed, separated by white space. By
*  convention, the products are listed in order of their significance
*  for identifying the application.
*
*       product         = token ["/" product-version]
*       product-version = token
*
*   Examples:
*
*       User-Agent: CERN-LineMode/2.15 libwww/2.17b3
*       Server: Apache/0.8.4
*
*
*
*
*   Product tokens SHOULD be short and to the point. They MUST NOT be
*   used for advertising or other non-essential information. Although any
*   token character MAY appear in a product-version, this token SHOULD
*   only be used for a version identifier (i.e., successive versions of
*   the same product SHOULD only differ in the product-version portion of
*   the product value).
*
* </pre>
*
*
*/

public class Server extends SIPHeader {
    
        /** product type constant field
         */    
	protected static final int PRODUCT_TYPE = 1;
        
        /** comment type constant field
         */        
	protected static final int COMMENT_TYPE = 2;
        
        /** type field
         */        
	protected int type;
        
        /** product field
         */        
	protected Product product;
        
        /** comment field
         */        
	protected String  comment;

        /** default Constructor
         */        
 	public Server() { 
            super(SERVER);
        } 

        /**
         * Encode this into cannonical form.
         * @return String
         */
	public String encode() {
	   if (type == PRODUCT_TYPE) 
		return headerName + COLON + SP + product.encode() + NEWLINE;
	   else
		return headerName + COLON + SP + LPAREN + comment + 
					RPAREN + NEWLINE;
	}
        
        /** Boolean function
         * @return true if this server header is a list of product tokens.
         */
	public boolean isProduct() {
		return type == PRODUCT_TYPE;
	}

	/** Boolean function
         * @return  true if this server header has an imbedded comment.
         */
	public boolean isComment() {
		return type == COMMENT_TYPE;
	}
        
        /** get the product field
         * @return product field
         */        
	public Product getProduct() {
            return product ;
        }
        
        /**
         * Get the comment value.
         * @return comment field
         */
	public String getComment() {
            return comment;
        }
        
	/**
         * Set the product member
         * @param p Product to set
         */
	public void setProduct(Product p) {
            product = p ;
            type = PRODUCT_TYPE;
        } 
      
	/**
         * Set the token list of product tokens.
         * @param products token List to set
         * @throws Exception if product list is not consistent
	 *@see  gov.nist.sip.sipheaders.Product
         */
        public void setProductTokenList(List products) 
	throws IllegalArgumentException {
              if (product  == null) {
                product=new Product();
                type = PRODUCT_TYPE;
            }
            product.setProductTokenList(products);
        }

	/**
	* Get the product token list.
	* @return linked list containing the product tokens.
	*/
	public List getProductTokenList() {
		if (product  == null) return null;
		else return product.getProductTokenList();
	}


	/**
         * set the comment value.
         * @param c String to set
         */
	public void setComment (String c ) {
            comment = c;
            type = COMMENT_TYPE;
        }
	
}
