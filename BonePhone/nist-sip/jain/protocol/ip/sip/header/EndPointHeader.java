package jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;

/**
 * <p>
 * This interface represents any header that indicates an endpoint of a Message's
 * path. It extends NameAddressHeader and is the super-interface of FromHeader
 * and ToHeader.
 * </p>
 *
 * @see NameAddressHeader
 * @see FromHeader
 * @see ToHeader
 *
 * @version 1.0
 *
 */

public interface EndPointHeader extends NameAddressHeader
{
    
    /**
     * Gets boolean value to indicate if EndPointHeader
     * has tag
     * @return boolean value to indicate if EndPointHeader
     * has tag
     */
    public boolean hasTag();
    
    /**
     * Sets tag of EndPointHeader
     * @param <var>tag</var> tag
     * @throws IllegalArgumentException if tag is null
     * @throws SipParseException if tag is not accepted by implementation
     */
    public void setTag(String tag)
                 throws IllegalArgumentException,SipParseException;
    
    /**
     * Removes tag from EndPointHeader (if it exists)
     */
    public void removeTag();
    
    /**
     * Gets tag of EndPointHeader
     * (Returns null if tag does not exist)
     * @return tag of EndPointHeader
     */
    public String getTag();
}
