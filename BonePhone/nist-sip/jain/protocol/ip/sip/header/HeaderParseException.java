package jain.protocol.ip.sip.header;
import jain.protocol.ip.sip.*;

/**
 * <p>The HeaderParseException is thrown by an implementation that cannot parse a header
 * value when an application asks a Message for an API-defined Header object
 * (e.g. ContentTypeHeader). The application may wish to view the unparsable
 * header's value as a String (and it may not know the header name if it simply
 * called Message's getHeaders() method) so the HeaderParseException contains a generic
 * Header object that can be accessed with the getHeader() method.</p>
 * <p>Note that this
 * exception will never be thrown when an application asks for a FromHeader, ToHeader,
 * CallIdHeader or CSeqHeader - if a received Message does not contain these four
 * headers it must be handled appropriately by an implementation.</p>
 * <p>Also
 * note that this exception will never be thrown by an eager-parsing implementation,
 * because it will have fully parsed all Messages passed to an application and
 * handled all others itself.</p>
 *
 * @version 1.0
 */

public class HeaderParseException extends SipParseException
{
    
    /**
     * Constructs new HeaderParseException based on specified Header and a message
     * @param <var>msg</var> message of this HeaderParseException.
     * @param <var>header</var> Header to base HeaderParseException on
     */
    public HeaderParseException(String message, Header header) 
    {
        super(message, header.getValue());
    }
    
    /**
     * Gets Header of this HeaderParseException
     * @return Header of this HeaderParseException
     */
    public Header getHeader()
    {
        return header;
    }
    
    /**
     * Constructs new HeaderParseException based on specified Header
     * @param <var>header</var> Header to base HeaderParseException on
     */
    public HeaderParseException(Header header) 
    {
        super(header.getValue());
    }
    private Header header;
}
