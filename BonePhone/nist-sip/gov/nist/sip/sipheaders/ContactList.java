/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) , added JAVADOC                *                                                                                   
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import java.util.Hashtable;

/**
* List of contact headers.ContactLists are also maintained in a hashtable
* for quick lookup.
* @author M. Ranganathan (mranga@nist.gov) 
* @since  0.9
* @version 1.0
* Revisions:
* @revision mranga Added conveniance accessor function to access contacts as
* an array
*/
public class ContactList extends SIPHeaderList {
    
	// Hashtable that is used to quickly access contact records.
	// The hash key for this hashtable is the address records.
	private Hashtable  contactHash;
        
        /** constructor
         * @param hl SIPObjectList
         */        
	public ContactList(SIPObjectList hl) {
		super(hl,CONTACT);
		contactHash = new Hashtable();
	}

	/**
	* Constructor. 
	*/
	public ContactList() {
	        super("ContactList", 
			SIPHEADERS_PACKAGE + ".Contact", CONTACT);
		contactHash = new Hashtable();
		// Set the headerlist field in our superclass.
	}

	/**
         * add a new contact header. Store it in the hashtable also
         * @param c Contact to set
         * @throws IllegalArgumentException if Duplicate Contact for same addr
         */
	protected  void add( Contact  c )
	throws  IllegalArgumentException {
		// Concatenate my lists.
		super.add(c);
		// All fields are valid for a contact address.
		Address a = ((Contact) c).address;
		// This is a delete request -- dont put in our hashtable.
		if (a == null) return;
		if (contactHash.containsKey(a)) {
		   throw new 
		    IllegalArgumentException("Duplicate Contact for same addr");
		}
		// put the contact in my hash table for quick lookup.
		contactHash.put(a.encode(),c);
	}

	/**
	* Replace an object from this contact list.
	*@param objectText Encoded form of the object that we want to 
	*  replace.
	*@param replacementObject object that we want to put in the place
	*   of this object.
	*@param matchSubstring substring match flag.
	*/
	public void replace(String objectText, 
			GenericObject replacementObject,
			boolean matchSubstring ) 
	 throws IllegalArgumentException {
		
	    if (replacementObject instanceof Contact) {
		Contact c = (Contact) replacementObject;
		Address a = ((Contact) c).address;
		if (a != null) {
			contactHash.remove(a.encode());
		}
	    } 
	    super.replace(objectText,replacementObject,matchSubstring);
	}


	/**
        * make a clone of this contact list.
        * @return Object cloned list.
        */
	public Object clone() {
		ContactList retval = new ContactList();
		if (this.inputText != null) 
			retval.inputText = new String(this.inputText);
		for (Contact c = (Contact) this.first(); c != null; 
			c = (Contact) this.next()) {
			Contact newc = (Contact) c.clone();
			retval.add(newc);
		}
		return retval;
	}

	/**
         * Concatenate two contact lists by appending the 
	 * argument to this list. 	
         * Concatenate the lists and union the
         * hash tables. 
         * @param clist SIPHeaderList to set
         * @throws IllegalArgumentException if the two lists are not compatible
         */		
	public void concatenate ( SIPHeaderList  clist ) 
	throws IllegalArgumentException {
		this.concatenate(clist,false);
	}
	
	/**
	* Concatenate two contact lists. Concatenate the lists and union the
	* hash tables. 
	* @param <var> clist </var>  is a list of contact headers to 
	* append to this list.
	* @param <var> topFlag </var> is a flag which indicates which 
	* 	end of the list attach the
	*	given list -- if true then prepend if flase then append.
	*@exception IllegalArgumentException if the clist is not a list 
	*    of contact headers.
	*/
	public void concatenate ( SIPHeaderList  clist, boolean topFlag ) 
	throws IllegalArgumentException {
		super.concatenate(clist,topFlag);
		ContactList otherList = (ContactList) clist;
		this.contactHash.putAll(otherList.contactHash);
	}

	/**
         * Get the contact for a given address.
         * @param a Address to set
         * @return Contact
         */
	protected Contact getContact (Address a) {
		return (Contact) contactHash.get(a.encode());
	}
	
	/**
         * Get an array of contact addresses.
         * @return contact []
         */
	public Contact[] getContacts() {
		Contact[] retval = new Contact[this.hlist.size()];
		return (Contact[])hlist.toArray(retval);
	}
	
}
