package jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;

/**
 * <p>
 * This interface represents any header that contains an option tag value.
 * It is the super-interface of ProxyRequireHeader, RequireHeader and
 * UnsupportedHeader.
 * </p>
 *
 * @see ProxyRequireHeader
 * @see RequireHeader
 * @see UnsupportedHeader
 *
 * @version 1.0
 *
 */

public interface OptionTagHeader extends Header
{
    
    /**
     * Sets option tag of OptionTagHeader
     * @param <var>optionTag</var> option tag
     * @throws IllegalArgumentException if optionTag is null
     * @throws SipParseException if optionTag is not accepted by implementation
     */
    public void setOptionTag(String optionTag)
                 throws IllegalArgumentException,SipParseException;
    
    /**
     * Gets option tag of OptionTagHeader
     * @return option tag of OptionTagHeader
     */
    public String getOptionTag();
}
