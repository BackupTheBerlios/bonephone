package jain.protocol.ip.sip.header;
import java.util.*;

/**
 * This interface provides an Iterator over a list of Headers. There are two differences
 * between HeaderIterator and java.util.Iterator:
 * <ol>
 * <li>HeaderIterator contains no remove() method</li>
 * <li>HeaderIterator's next() method can throw a HeaderParseException. This is because
 * the next header's value may not have been parsed until the next() method is invoked.
 * </li>
 * </ol>
 *
 * @version 1.0
 */

public interface HeaderIterator
{
    public boolean hasNext();
    public Header next()
                   throws HeaderParseException,NoSuchElementException;
}
