/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).       *
 * See ../../../../doc/uncopyright.html for conditions of use.                 *
 * Author: M. Ranganathan (mranga@nist.gov)                                    *
 * Modifications by: O. Deruelle (deruelle@antd.nist.gov added JAVADOC)        *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                              *
 ******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import  java.util.Hashtable;
import  java.util.Enumeration;

/**
 *  Keeps a list and a hashtable of via header functions.
 *  @since 0.9
 */

public final class ViaList extends SIPHeaderList {
    
    private  Hashtable viaTable; // For quick address lookups.
    
    private String stringRep;
    
         /**
          * Constructor.
          * @param hl SIPObjectList to set
          */
    public ViaList(SIPObjectList hl) {
        super(hl,VIA);
        viaTable = new Hashtable();
    }
    
         /**
          * Default Constructor.
          */
    public ViaList() {
        super("Via",  SIPHEADERS_PACKAGE+".Via", VIA);
        viaTable = new Hashtable();
    }
    
        /**
         * Add to our linked list and also put it in our table.
         * @param viaHdr SIPObject to set.
         */
    public void add ( SIPObject viaHdr ) {
        super.add(viaHdr);
        Via via = (Via) viaHdr;
        viaTable.put(via.sentBy.encode(),via);
    }
    
        /**
         * Concatenation function - concatenates a given via list at either end
         * of this list.
         * The hash tables for quick access to via lists are merged and the
         * lists themselves are merged by using the concatenation method of the
         * superclass. This function supercedes the parent function of the same
         * signature.
         * @param otherVia other list to concatenate with this list.
         * @param topFlag if set to true other list is prepended to us.
         * @exception IllegalArgumentException if the given header list is not a
         * list of via headers.
         */
    public void concatenate( SIPHeaderList otherVia, boolean topFlag )
    throws IllegalArgumentException {
        ViaList other = (ViaList) otherVia;
        Enumeration keys = other.viaTable.keys();
        super.concatenate(otherVia,topFlag);
        viaTable.putAll(other.viaTable);
    }
    
        /**
         * make a clone of this header list. This supercedes the parent
         * function of the same signature.
         * @return clone of this Header.
         */
    public Object clone() {
        ViaList vlist = new ViaList();
        vlist.inputText = this.inputText;
        for(Via v = (Via)this.first(); v != null; v = (Via)this.next()) {
            vlist.add(v);
        }
        return (Object) vlist;
    }
    
        /**
         * Concatenation function - append the given list to  this list.
         * @param otherVia other list to concatenate with this list.
         * @exception IllegalArgumentException if the given header list is not a
         * list of via headers.
         */
    public void concatenate( SIPHeaderList otherVia)
    throws IllegalArgumentException  {
        this.concatenate(otherVia,false);
    }
    
        /**
         *  Is this address on the sent-by list?
         * A proxy server can use this function
         *  to aid in loop detection.
         * @param address String to set.
         * @return true if the Header contains the address.
         */
    public boolean isAddressOnViaList( String address ) {
        return viaTable.containsKey(address.trim().toLowerCase()) ;
    }
    
        /**
         * function for debug
         * @param str String to set.
         */
    public void sprint( String str) {
        stringRep += str  ;
        stringRep += "\n";
    }
    
        /**
         *  string formatting function.
         * @param indentation int to set.
         * @return string formatting.
         */
    public String toString(int indentation) {
        Enumeration keys = viaTable.keys();
        stringRep = "";
        String indent  =
        new Indentation(indentation).getIndentation();;
        this.sprint( indent + "Keys : ");
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            sprint(indent + key);
        }
        this.sprint(indent + "--------------------------");
        sprint(indent + "inputText = " + inputText);
        sprint(this.hlist.toString(indentation));
        return stringRep;
    }
    
        /**
         *  function for debug.
         *  @return String.
         */
    public String toString() {
        return this.toString(0);
    }

	/**
	* Replace an object from this contact list.
	*@param objectText Encoded form of the object that we want to 
	*  replace.
	*@param replacementObject object that we want to put in the place
	*   of this object.
	*@param matchSubstring flag that indicates whether we want to pick
	* targets on substring matches.
	*/
	public void replace(String objectText, 
			GenericObject replacementObject,
			boolean matchSubstring ) {
		
	    if (replacementObject instanceof Via) {
		Via via = (Via) replacementObject;
		HostPort hp = via.sentBy;
		if (hp != null) {
                	String sb = hp.encode();
			viaTable.remove(sb);
		}
	    } 
	    super.replace(objectText,replacementObject,matchSubstring);
	}
    
}
