package jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;
import java.util.*;

/**
 * <p>
 * This interface represents a header that contains product information.
 * It is the super-interface of ServerHeader and UserAgentHeader.
 * </p>
 *
 * @see ServerHeader
 * @see UserAgentHeader
 *
 * @version 1.0
 *
 */

public interface ProductHeader extends Header
{
    
    /**
     * Set products of ProductHeader
     * (Note that the Objects in the List must be Strings)
     * @param <var>products</var> products
     * @throws IllegalArgumentException if products is null, empty, or contains
     * any null elements, or contains any non-String objects
     * @throws SipParseException if any element of products is not accepted by implementation
     */
    public void setProducts(List products)
                 throws IllegalArgumentException,SipParseException;
    
    /**
     * Gets products of ProductHeader
     * (Note that the Objects returned by the Iterator are Strings)
     * @return products of ProductHeader
     */
    public Iterator getProducts();
}
