/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modifications by: O. Deruelle (deruelle@antd.nist.gov added JAVADOC)         *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import java.util.List;

/**
* User-Agent SIPHeader Object.
* 
*@since 1.0 (Removed one level of class hierarchy)
*/
public class UserAgent extends SIPHeader {
    
        /** constant product type
         */    
    protected   final int PRODUCT_TYPE = 1;
    
        /** constant comment type
         */        
    protected   final int COMMENT_TYPE = 2;
    
        /** type field.
         */        
    protected   int type;
    
        /** comment field.
         */        
    protected String comment;
    
        /** product field.
         */        
    protected Product product;
    
        /**
         * Constructor.
         */
    public UserAgent() { 
        super(USER_AGENT); 
        product = new Product(); 
        type = PRODUCT_TYPE;
    }
    
        /**
         * Encode to cannonical form.
         * @return String  encoding of the header into canonical form.
         */
    public String encode() {
        if (type == COMMENT_TYPE) 
            return headerName + COLON + SP + LPAREN + comment + RPAREN 
            + NEWLINE;
        else 
            return headerName + COLON + SP + product.encode() + NEWLINE;  
    }
    
        /** get the comment field
         * @return Comment field
         */         
    public String  getComment() { 
        return comment;
    }       
    
        /**
         * Get the product string.
         * @return Product field
         */
    public Product getProduct() {
        return product;
    }

	
        /**
	* Get a list of product tokens.
	* @return product token list (if one exists) or null if none exists
	*/
    public List getProductTokenList() {
	  if (product == null) return null;
	  else return product.getProductTokenList();
    }


        /** Boolean function.
         * @return true if the User agent is a Product.
         */
    public boolean isProduct() {
        return type == PRODUCT_TYPE; 
    }
    
        /** Boolean function.
         * @return true if user agent is a comment.
         */
    public boolean isComment() {
        return type == COMMENT_TYPE;
    }
     
        /**
         * Set the comment field.
         * @param comment String to set
         */
    public void setComment( String comment ) {
        this.comment = comment;
        type = COMMENT_TYPE;
    }
    
        /**
         * Set the product type.
         * @param product  Product to set. 
         */
    public void setProduct(Product product) {
        this.product = product ;
        type = PRODUCT_TYPE;
    } 
    
        /**
         * set the product token list.
         * @since 1.0
         * @param tokens List to set
         */
    public void setProductTokenList(List tokens ) {
        product.setProductTokenList(tokens);
    }
    
}
