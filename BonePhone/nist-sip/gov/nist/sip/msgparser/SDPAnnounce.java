/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified by: Marc Bednarek (bednarek@nist.gov)                               *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.*;
import gov.nist.sip.sdpfields.*;
import java.lang.reflect.*;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;
import java.util.Hashtable;

/**
* SDP Headers structure.
*/

public final class SDPAnnounce extends MessageObject {
/** Origin field
 */
    protected OriginField 	   	   originField;
/** Proto version field
 */
    protected ProtoVersionField    	   protoVersion;
/** session name field
 */
    protected SessionNameField     	   sessionName;
/** information field
 */
    protected InformationField  	   informationField;
/** URI Field
 */
    protected URIField     	   	   uriField;
/** Email fields
 */
    protected EmailFieldList       	   emailFields;
/** phone fields
 */
    protected PhoneFieldList       	   phoneFields;
/** Connection field.
 */
    protected ConnectionField  	   	connectionField;
/** bandwidth field
 */
    protected BandwidthField   	   	bandwidthField;
/** time fileds
 */
    protected TimeFieldList       	  timeFields;
/**Zone adjustment field
*/
    protected ZoneAdjustment		zoneAdjustment;
/** key field
 */
    protected KeyField         	   	  keyField;
/** attribute fields
 */
    protected AttributeFields    	   attributeFields;
/** media description fields
 */
    protected MediaDescriptionList     	   mediaDescriptions;

/** list of sdp fields
 */
    protected LinkedList		  sdpFields;

    private Field 	myFields[];
    private Hashtable 	fieldHash;
    
    
/** match against a template.
 *  template must have the same class. Null in a field in the template matches
 *   anything.
 */
    public boolean match(Object template) {
        if (template == null) return true;
        if (! template.getClass().equals(this.getClass()))  return false;
        SDPAnnounce that = (SDPAnnounce) template;
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields [i];
                Class fieldType = f.getType();
                Object myObj = f.get(this);
                Object hisObj = f.get(that);
                if (myObj == hisObj ) continue;
                if (f.get(that) != null && f.get(this) == null) return false;
                else if (GenericObject.isMySubclass(myObj.getClass())
			   && ! ((GenericObject) myObj).match(hisObj)) 
                         return false;
	        else if (GenericObjectList.isMySubclass(myObj.getClass()) && 
		         ! ((GenericObjectList) myObj).match(hisObj)) 
                            return false;
                   
            }
        } catch (Exception ex) {
            InternalError.handleException(ex);
            
        }
        return true;
        
    }
    
/** A formatter for pretty printing the structure.
 * @return the string representation of the structure.
 */
    public String toString() {
        stringRepresentation = "";
        sprint("SDPInvite:");
        sprint("{");
        sprint("inputText:");
        sprint(inputText);
        try {
            
            Field[] fields = this.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields [i];
                Class fieldType = f.getType();
                String fieldName = f.getName();
                if (f.get(this) != null  &&
                Class.forName(SDPFIELDS_PACKAGE + ".SDPField").
                isAssignableFrom(fieldType) ) {
                    sprint(fieldName + "=");
                    sprint(f.get(this).toString());
                }
            }
        } catch ( Exception ex ) {
            InternalError.handleException(ex);
        }
        sprint("}");
        return stringRepresentation;
    }

    class HdrField {
	int offset;
	boolean isList;
	HdrField(int off, boolean l) { offset = off; isList = l; }
    }
	
    
/** public default constructor
 */
    public SDPAnnounce() { 
	    sdpFields = new LinkedList(); 
	    fieldHash = new Hashtable();
	    Class sdpAnnounceClass = getClassFromName(MSGPARSER_PACKAGE +
					".SDPAnnounce");
              myFields = sdpAnnounceClass.getDeclaredFields();
              for ( int i = 0 ; i < myFields.length; i++) {
                   boolean isList = false;
                   // Find the type that matches our header type.
                   Class cls = myFields[i].getType();
                   if ( getClassFromName(SDPFIELDS_PACKAGE+
                		".SDPFieldList").
                		isAssignableFrom(cls) ) {
                    		HdrField hf = new HdrField(i,true);
                    	     fieldHash.put(cls, hf );
                } else  if ( getClassFromName(SDPFIELDS_PACKAGE+".SDPField")
                	.isAssignableFrom(cls)) {
                    	fieldHash.put(cls, (new HdrField(i,false)));
                }
              }
    }
    
        /**
         * Encocde into canonical form.
         * @return the encoded (canonical) string.
         * @since 1.0
         */
    public String encode() {
        String retval = "";
        SDPField sdpfield;
        ListIterator li = sdpFields.listIterator();
        while(li.hasNext()) {
            sdpfield = (SDPField) li.next();
            String encoded_field = sdpfield.encode();
            retval += encoded_field;
        }
        return retval;
    }
    
    
/** Get the origin field
 * @return the origin field
 */
    public	 OriginField getOriginField()
    { return originField ; }
/** get the proto version field
 * @return the proto version field.
 */
    public	 ProtoVersionField getProtoVersion()
    { return protoVersion ; }
/** get the session name field (could be null)
 * @return the session name field.
 */
    public	 SessionNameField getSessionName()
    { return sessionName ; }
/** Get the information field
 * @return the information field.
 */
    public	 InformationField getInformationField()
    { return informationField ; }
/** Get the URI field.
 * @return the URI field.
 */
    public	 URIField getUriField()
    { return uriField ; }
/** Get the email fields.
 * @return the email fields.
 */
    public	 SDPFieldList getEmailFields()
    { return emailFields ; }
/** Get the phone fields.
 * @return The phone fields.
 */
    public	 SDPFieldList getPhoneFields()
    { return phoneFields ; }
/** Get the connection fields.
 * @return The connection fields.
 */
    public	 ConnectionField getConnectionField()
    { return connectionField ; }
/** Get the bandwidth fields.
 * @return The bandwidth fields.
 */
    public	 BandwidthField getBandwidthField()
    { return bandwidthField ; }

/** Get the zone adjustment field.
*/
      public ZoneAdjustment getZoneAdjustment() { return zoneAdjustment; }

/** Get the time fields 
 * @return The time fields
 */
public	 SDPFieldList getTimeFields()
    { return timeFields ; }

/** Get the key fields
 * @return A list containing the key fields.
 */
    public	 KeyField getKeyField()
    { return keyField ; }
/** Get the attribute fields.
 * @return A list containing the attribute fields.
 */
    public	 AttributeFields getAttributeFields()
    { return attributeFields ; }
/** Get the media description fields.
 * @return A list containing the media description fields.
 */
    public	 MediaDescriptionList getMediaDescriptions()
    { return mediaDescriptions ; }
        /** Set the originField member
         * @param o Origin field to set.
         */
    public	 void setOriginField(OriginField o)
    { originField = o ;  if (o!= null) sdpFields.add(o);}
        /**
         * Set the protoVersion member
         * @param p Proto version field to set.
         */
    public	 void setProtoVersion(ProtoVersionField p)
    { protoVersion = p ; if (p!= null)  sdpFields.add(p); }
        /**
         * Set the sessionName member
         * @param s Session name field to set.
         */
    public	 void setSessionName(SessionNameField s)
    {  sessionName = s ; if (s != null) sdpFields.add(s);}
        /**
         * Set the informationField member
         * @param i Information field to set.
         */
    public	 void setInformationField(InformationField i)
    {  informationField = i ; if (i != null) sdpFields.add(i); }
        /**
         * Set the uriField member
         * @param u URI Field to set.
         */
    public	 void setUriField(URIField u)
    { uriField = u ; if (u != null) sdpFields.add(u); }
        /**
         * Set the emailFields member
         * @param e A list of Email fields to set.
         */
    public	 void setEmailFields(EmailFieldList e)
    {  emailFields = e ;  if (e != null) sdpFields.add(e); }
        /**
         * Set the phoneFields member
         * @param p Phone fields to set.
         */
    public	 void setPhoneFields(PhoneFieldList p)
    {  phoneFields = p ; if (p != null) sdpFields.add(p);  }
        /**
         * Set the connectionField member
         * @param c Connection field to set.
         */
    public	 void setConnectionField(ConnectionField c)
    {  connectionField = c ; if (c != null) sdpFields.add(c); }
        /**
         * Set the bandwidthField member
         * @param b bandwidth field to set.
         */
    public	 void setBandwidthField(BandwidthField b)
    { bandwidthField = b ; if (b != null) sdpFields.add(b); }
        /**
         * Set the timeFields member
         * @param t a list of time fields to set.
         */
    public	 void setTimeFields(TimeFieldList t)
    {  timeFields = t ; if (t != null) sdpFields.add(t); }
        /**
         * Set the keyField member
         * @param k key field to set.
         */
    public	 void setKeyField(KeyField k)
    {  keyField = k ;  if (k != null) sdpFields.add(k); }
        /**
         * Set the attributeFields member
         * @param a attribute field to set.
         */
    public	 void setAttributeFields(AttributeFields a)
    { attributeFields = a ;  if (a != null)  sdpFields.add(a); }
        /**
         * Set the mediaDescriptions member
         * @param m Media description fields (list) to set.
         */
    public	 void setMediaDescriptions(MediaDescriptionList m)
    { mediaDescriptions = m ; if (m != null) sdpFields.add(m); }

	/**
	* Set the zone adjustment field.
	*/
     public void setZoneAdjustment(ZoneAdjustment za) 
     { zoneAdjustment = za; if (za!= null) sdpFields.add(za); }

    
        /**
         * Get valid fields as a list of fields.
         * @since 1.0
         * @return A linked list containing the valid sdp fields in
         * this structure.
         */
    public LinkedList getSdpFields () { return sdpFields; }
         
        /**
         * Set the input text that we parsed.
         * @param txt Set the input text (i.e. the text from the input
         * stream) that was parsed to generate this strucutre.
         */
    public void setInputText( String txt) {
             super.setInputText(txt);
    }


        /**
         *Replace a field with another of the same type.
         *This does not work for the top level fields (i.e only for the
         *members). 
         * @since v1.0
         * @param objectText is the canonical string representation of
         *		the object that we want to replace.
         * @param replacement is the object that we want to replace it
         *		with.
         * @param matchSubstring a boolean which tells if we should match
         * 		a substring of the target object
	 * @exception IllegalArgumentException if null arguments or
         * 	newObject is subclassed from SDPFields
         * A replacement will occur if a portion of the structure is found
         * with matching encoded text (a substring if matchSubstring is true)
         * as objectText and with the same class as replacement.
         */
    public void replace(String cText, GenericObject newObject, 
		boolean matchSubstring) {
	if (cText == null || newObject == null) 
		throw new IllegalArgumentException("null arguments");
        if (getClassFromName(PackageNames.SDPFIELDS_PACKAGE + ".SDPField")
            .isAssignableFrom(newObject.getClass())){
            throw new IllegalArgumentException
		("invalid class for replacement object");
        }
        ListIterator li = sdpFields.listIterator();
        while(li.hasNext()) {
            Object obj = li.next();
            if (getClassFromName(PackageNames.SDPFIELDS_PACKAGE + ".SDPField")
                .isAssignableFrom(obj.getClass())) {
                SDPField sdpfield = (SDPField)obj;
                sdpfield.replace(cText,newObject,matchSubstring);
            } else {
                SDPFieldList sdpfield = (SDPFieldList) obj;
                sdpfield.replace(cText,newObject,matchSubstring);
            }
        }
    }
    	 /**
         * Do a find and replace of objects.
         *This does not work for the top level fields (i.e only for the
         *members). 
         * @since v1.0
         * @param objectText is the canonical string representation of
         *		the object that we want to replace.
         * @param replacement is the object that we want to replace it
         *		with.
         * @param matchSubstring a boolean which tells if we should match
         * 		a substring of the target object
	 * @exception IllegalArgumentException if null arguments or
         * 	newObject is subclassed from SDPFields
         * A replacement will occur if a portion of the structure is found
         * with matching encoded text (a substring if matchSubstring is true)
         * as objectText and with the same class as replacement.
         */
    public void replace(String cText, GenericObjectList newObject, 
	boolean matchSubstring ) 
    throws IllegalArgumentException {
	if (cText == null || newObject == null) 
		throw new IllegalArgumentException("null arguments");
        if (getClassFromName(PackageNames.SDPFIELDS_PACKAGE + ".SDPField")
            .isAssignableFrom(newObject.getClass())){
            throw new IllegalArgumentException
		("invalid class for replacement object");
        }
        ListIterator li = sdpFields.listIterator();
        while(li.hasNext()) {
            Object obj = li.next();
            if (getClassFromName(PackageNames.SDPFIELDS_PACKAGE + ".SDPField")
                .isAssignableFrom(obj.getClass())) {
                SDPField sdpfield = (SDPField)obj;
                sdpfield.replace(cText,newObject,matchSubstring);
            } else {
                SDPFieldList sdpfield = (SDPFieldList) obj;
                sdpfield.replace(cText,newObject,matchSubstring);
            }
        }
    }

    protected void attachField(SDPField sdpField) 
	throws DuplicateFieldException, IllegalArgumentException {
	if (sdpField == null) {
		throw new IllegalArgumentException("Null sdp field");
	}
	HdrField hfield  = (HdrField) fieldHash.get(sdpField.getClass());
	if (hfield == null) {
		throw new IllegalArgumentException("Unsupported field " + 
			sdpField.getClass().getName());
	} 
	int i = hfield.offset;
	try {
	  Object obj = myFields[i].get(this);
	  if (obj == null) { 
		myFields[i].set(this,sdpField);
		sdpFields.add(sdpField);
	  }  else {
		throw new DuplicateFieldException("Field already set!");
	  }
	} catch (IllegalAccessException ex) {
		InternalError.handleException(ex);
	}
    }


}
