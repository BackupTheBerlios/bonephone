/*
 * HeaderIteratorImpl.java
 *
 * Created on April 20, 2001, 10:07 AM
 */

package gov.nist.jain.protocol.ip.sip.header;
import  jain.protocol.ip.sip.header.*;
import  gov.nist.sip.sipheaders.*;
import  java.util.LinkedList;
import  java.util.List;
import  java.util.ListIterator;
import  java.util.Iterator;
import  java.util.NoSuchElementException;

/**
 *
 * @author  mranga
 * @version 
 */
public class HeaderIteratorImpl implements HeaderIterator {
    protected ListIterator listIterator;
    protected ListIterator innerIterator;

    public HeaderIteratorImpl (SIPHeader sipHdr) {
		LinkedList llist = new LinkedList();
		this.listIterator = llist.listIterator();
    }

    public HeaderIteratorImpl (SIPHeaderList llist) {
		this.listIterator = llist.listIterator();
    }
    
    /** 
     * creates a new HeaderIteratorImpl given a gov.nist.sip.SIPHeader 
     *@param <var> llist </var> list of  SIPHeader from which to create this
     *  HeaderIteratorImpl.
     */
    public HeaderIteratorImpl (LinkedList llist) {
		this.listIterator = llist.listIterator();
    }
    
    /** 
     * creates a new HeaderIteratorImpl given a gov.nist.sip.SIPHeader 
     *@param <var> Iterator </var> iterator  from which to create this
     *  HeaderIteratorImpl.
     */
    public HeaderIteratorImpl (Iterator iterator) {
		this.listIterator = (ListIterator) iterator;
    }

    /**
     *return the next header structure.
     */
    public Header next() throws 
        HeaderParseException, 
        NoSuchElementException {

            if (innerIterator != null) {
                if (innerIterator.hasNext()) {
                    SIPHeader sh = (SIPHeader) innerIterator.next();
                    return HeaderMap.getJAINHeaderFromNISTHeader(sh);
                } else innerIterator = null;
            }   
            SIPHeader sipheader = (SIPHeader) listIterator.next();
            if (sipheader instanceof SIPHeaderList) {
                SIPHeaderList shl = (SIPHeaderList) sipheader;
                innerIterator = shl.listIterator(); 
                return this.next();
            } else {
                innerIterator = null;
                return    HeaderMap.getJAINHeaderFromNISTHeader
                        (sipheader);
            }


    }
    
    /**
     *Return a boolean indicating whether this has a next item.
     */
    public boolean hasNext() {
        return this.listIterator.hasNext();
    }

}
