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
import java.util.ListIterator;
import java.util.LinkedList;
import java.lang.reflect.*;
import java.util.NoSuchElementException;


/**
*  This is the root class for all lists of SIP headers. 
*  It imbeds a SIPObjectList object and extends SIPHeader
*  Lists of ContactSIPObjects etc. derive from this class. 
*  This supports homogeneous  lists (all elements in the list are of 
*  the same class). We use this for building type homogeneous lists of 
*  SIPObjects that appear in SIPHeaders
*/
public class SIPHeaderList extends SIPHeader {
    
    /** hlist field.
     */    
    protected SIPObjectList hlist;
    
    /** Constructor
     * @param hl SIPObjectList to set
     * @param hname String to set
     */    
    public SIPHeaderList (SIPObjectList hl, String hname  )  {
        super(hname);
        hlist = hl;
        inputText = hl.getInputText();
    }
    
    /** Constructor
     * @param hName String to set
     */    
    public SIPHeaderList (String hName) {
        super(hName);
        hlist = new SIPObjectList (null);
        inputText = null;
    }
    
    /** Constructor
     * @param listname String to set
     * @param objclass Class to set
     * @param hname String to set
     */    
    public SIPHeaderList (String listname,
    Class objclass, String hname) {
        super(hname);
        hlist = new SIPObjectList (listname, objclass);
    }
    
    /** Constructor
     * @param listname String to set
     * @param classname String to set
     * @param hname String to set
     */    
    public SIPHeaderList
    (String listname, String classname, String hname ) {
        super(hname);
        hlist = new SIPObjectList (listname, classname );
    }
     
        /**
         * Concatenate the list of stuff that we are keeping around and also
         * the text corresponding to these structures (that we parsed).
         * @param obj GenericObject to set
         */
    public void add(GenericObject obj) {
        if (obj.getInputText() != null) {
            if (this.inputText != null) 
                this.inputText += obj.getInputText();
            else this.inputText = obj.getInputText();
        }
        hlist.add((Object)obj);
    }
    
        /**
         * Concatenate the list of stuff that we are keeping around and also
         * the text corresponding to these structures (that we parsed).
         * @param obj Genericobject to set
         */
    public void addFirst(GenericObject obj) {
        if (obj.getInputText() != null) {
            if (this.inputText != null) 
                this.inputText = obj.getInputText() + this.inputText;
            else this.inputText = obj.getInputText();
        }
        hlist.addFirst(obj);
    }

     /** Add to this list.
     *@param header SIPHeader to add.
     *@param top is true if we want to add to the top of the list.
     */
     public void add(SIPHeader sipheader, boolean top) {
	   if (top) this.addFirst(sipheader);
	   else this.add(sipheader);
     }
    
    /**
     * Concatenate two compatible lists. This appends or prepends the new list 
     * to the end of this list.
     * @param other SIPHeaderList to set
     * @param top boolean to set
     * @throws IllegalArgumentException if the two lists are not compatible
     */
    public void concatenate( SIPHeaderList other, boolean top) 
    throws IllegalArgumentException  {
        if (! hlist.getMyClass().equals(other.hlist.getMyClass()))
            throw new IllegalArgumentException (
            "SIPHeaderList concatenation " +
            hlist.getMyClass().getName() + "/" +
            other.hlist.getMyClass().getName());
        hlist.concatenate(other.hlist,top);
        if (other.inputText != null) {
            if (! top ) {
                if (this.inputText == null) this.inputText = other.inputText;
                else this.inputText += other.inputText;
            } else {
                if (this.inputText == null) this.inputText = other.inputText;
                else this.inputText = other.inputText + this.inputText;
            }
        }
    }
    
      /**
       * Concatenate two compatible lists. This appends  the new list to the end
       * of this list (which is the most common mode for this operation).
       * @param other SIPHeaderList
       * @throws IllegalArgumentException if the two lists are not compatible
       */
    public void concatenate( SIPHeaderList other)  
    throws IllegalArgumentException {
        this.concatenate(other,false);
    }
    
        /**
         * Encode a list of sip headers.
         * Headers are returned in cannonical form.
         * @return String encoded string representation of this list of
	 * 	 headers. (Contains string append of each encoded header).
         */
    public String encode() {
        if (hlist.isEmpty()) return "";
        String encoding = "";
	ListIterator li = hlist.listIterator();
	while(li.hasNext()) {
	    SIPHeader sipheader = (SIPHeader) li.next();
            encoding += sipheader.encode();
	}
        return encoding;
    }

	/** Return a list of encoded strings (one for each sipheader).
	*@return LinkedList containing encoded strings in this header list.
	*	an empty list is returned if this header list contains no
	*	sip headers.
	*/
    public LinkedList getHeadersAsEncodedStrings() {
	LinkedList retval = new LinkedList();

	ListIterator li = hlist.listIterator();
	while(li.hasNext()) {
		SIPHeader sipheader = (SIPHeader) li.next();
           retval.add(sipheader.encode());

	}

	return retval;
	
    }
    
         /**
         * Initialize the iterator for a loop
         * @return SIPObject first element of the list.
         */
    public SIPObject first() {
        return (SIPObject) this.hlist.first();
    }
     
    /**
     * Get the first element of this list.
     * @return SIPHeader first element of the list.
     */
    public SIPHeader getFirst() {
        return (SIPHeader) hlist.getFirst();
    }
    
    /**
     * Get the last element of this list.
     * @return SIPHeader last element of the list.
     */
    public SIPHeader getLast() {
        return (SIPHeader) hlist.getLast();
    }
    
     /**
     * Get the class for the headers of this list.
     * @return Class  of header supported by this list.
     */
    public Class getMyClass() {
        return hlist.getMyClass();
    }
      
        /**
         * Empty check
         * @return boolean true if list is empty
         */
    public boolean isEmpty() {
        return hlist.isEmpty();
    }
    
        /**
         * Get an initialized iterator for my imbedded list
         * @return the generated ListIterator
         */
    public ListIterator listIterator() {
        return hlist.listIterator(0);
    }  

	/** Get the list iterator for a given position.
	*@param position position for the list iterator to return
	*@return the generated list iterator
	*/
	public ListIterator listIterator (int position) {
		return hlist.listIterator(position);
	}
    
        /**
         * Get the next element in the list .
         * This is not thread safe and cannot handle nesting
         * @return SIPObject next object in this list.
         */
    public SIPObject next()  {
        return (SIPObject) this.hlist.next();
    }
       
        /**
         * Get the next item for an iterative scan of the list
         * @param iterator ListIterator
         * @return SIPObject next object in this list.
         */
    public SIPObject next(ListIterator iterator) {
        return  (SIPObject) this.hlist.next(iterator);
    }
      
        /**
         * Remove all occurances of a given class of SIPObject from
         * the SIP object list.
         * @param cl Class to set
         */
    public void removeAll(Class cl) {
        LinkedList ll = new LinkedList();
        for (SIPHeader sh = (SIPHeader) hlist.first() ;
        sh != null; sh = (SIPHeader) hlist.next() ) {
            if (sh.getClass().equals(cl)) {
                ll.add(sh);
            }
        }
        ListIterator li = ll.listIterator();
        while(li.hasNext()) {
            SIPHeader sh = (SIPHeader) li.next();
            hlist.remove(sh);
        }
    }
    
    /**
     *Remove the first element of this list.
     */
    public void removeFirst() {
        hlist.removeFirst();
        
    }
    
        /**
         *Remove the last element of this list.
         */
    public void removeLast() {
        hlist.removeLast();
    }
    
       /**
         * Remove a sip header from this list of sip headers.
         * @param obj SIPHeader to set
         * @return boolean
         */
    public boolean remove(SIPHeader obj) {
        return hlist.remove(obj);
    }
    
        /**
         * Set the root class for all objects inserted into my list
         * (for assertion check)
         * @param cl class to set
         */
    protected void setMyClass( Class cl) {
        hlist.setMyClass(cl);
    }      
    
        /**
         * convert to a string representation (for printing).
         * @param indentation int to set
         * @return String string representation of object (for printing).
         */
    public String toString(int indentation) {
        stringRepresentation = "";
        String indent  =
        new Indentation(indentation).getIndentation();;
        String className = this.getClass().getName();
        sprint(indent + className);
        sprint(indent + "{");
        sprint(indent + "inputText");
        sprint(indent + inputText);
        sprint(indent + hlist.toString(indentation));
        sprint(indent + "}");
        return stringRepresentation;
    }
    
        /** convert to a string representation
         * @return String
         */    
    public String toString() { 
        return toString(0);
    }
    
        /**
         * Array conversion.
         * @return SIPHeader []
         */
    public SIPHeader[] toArray() {
        SIPHeader retval[] = new SIPHeader[hlist.size()];
        return (SIPHeader[])hlist.toArray(retval);
    }

	/** index of an element.
	*@return index of the given element (-1) if element does not exist.
	*/
    public int indexOf (GenericObject gobj) {
	return hlist.indexOf(gobj);
    }

	/** insert at a location.
	*@param index location where to add the sipHeader.
	*@param sipHeader SIPHeader structure to add.
	*/

    public void add(int index, SIPHeader sipHeader) 
	throws IndexOutOfBoundsException {
	hlist.add(index,sipHeader);
    }
	

    	/**
	* Equality comparison operator.
	*@param other the other object to compare with. true is returned
        * iff the classes match and list of headers herein is equal to
        * the list of headers in the target (order of the headers is
	* not important).
	*/
    public boolean equals(Object other) {
	
	if ( ! this.getClass().equals(other.getClass()) ) return false;
	SIPHeaderList that = (SIPHeaderList) other;
	if (this.hlist == that.hlist) return true;
	else if (this.hlist == null) return false;
	return this.hlist.equals(that.hlist);
	
    }

	/** 
	* Template match against a template. 
	* null field in template indicates wild card match.
	*/
	public boolean match (Object template) {
		if (template == null) return true;
		if (! this.getClass().equals(template.getClass())) return false;
		SIPHeaderList that = (SIPHeaderList) template;
		if (this.hlist == that.hlist) return true;
		else if (this.hlist == null) return false;
		else return this.hlist.match(that.hlist);
	}

	/**
	* Get the number of headers in the list.
	*/
	public int size() { return hlist.size(); }
         		
}
