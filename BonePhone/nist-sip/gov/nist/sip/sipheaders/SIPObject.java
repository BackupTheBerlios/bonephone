/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
*   O. Deruelle (deruelle@nist.gov) ,added JAVADOC                 	       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import gov.nist.sip.net.*;
import java.lang.reflect.*;

/**
* Root class for all singleton objects in this package:
* specializes the gov.nist.sip.header.GenericObject class for SIPHeader 
* related objects. 
*/

public abstract class SIPObject extends GenericObject implements Separators,
SIPCloneable  {

        /** default Constructor
         */        
	protected  SIPObject() {
            super();
        }
    
	/**
         * Generic clone method. 
         * For any object in the object (like SIPHeaders) that are cloneable
         * clone the object and add it to the returned List. 
         * Strings and wrappers of basic types are 
         * cloned by creating new objects. For other objects, if there is
         * a clone method, then this is invoked and the cloned object
         * appears in the result. Otherwise, this just copies the 
         * object reference over.  NOTE that this method cannot be moved
         * to the superclass because the superclass is in a different package
         * (and we need to access protected fields from here).
         * @since 1.0
         * @return Object
         */
        public Object clone()  {
             Object newObject  = super.clone();
	     Class myclass = this.getClass();
             Field[] fields = myclass.getDeclaredFields();
             for (int i = 0; i < fields.length; i++) {
                Field f = fields [i];
                int modifier =  f.getModifiers();
            	if (Modifier.isPrivate(modifier)) {
			continue;
		} else if (Modifier.isStatic(modifier)) {
			continue;
		} else if (Modifier.isInterface(modifier)) {
			continue;
		}
                Class fieldType = f.getType();
                String fieldName = f.getName();
                String fname = fieldType.toString();
                try {
                    // Primitive fields are printed with type: value 
                    if (fieldType.isPrimitive()) {
                        if (fname.compareTo("int") == 0) {
                            int intfield = f.getInt(this);
                            f.setInt(newObject,intfield);
                        } else if (fname.compareTo("short") == 0) {
                            short shortField = f.getShort(this);
                            f.setShort(newObject,shortField);
                        } else if (fname.compareTo("char") == 0) {
                            char charField = f.getChar(this);
                            f.setChar(newObject,charField);
                        } else if (fname.compareTo("long") == 0) {
                            long longField = f.getLong(this);
                            f.setLong(newObject,longField);
                        } else if (fname.compareTo("boolean") == 0) {
                            boolean booleanField = f.getBoolean(this);
                            f.setBoolean(newObject,booleanField);
                        } else if (fname.compareTo("double") == 0) {
                            double doubleField = f.getDouble(this);
                            f.setDouble(newObject,doubleField);
                        } else if (fname.compareTo("float") == 0) {
                            float floatField = f.getFloat(this);
                            f.setFloat(newObject,floatField);
                        } 
		    } else {
                        Object obj = f.get(this);
			if (obj == null){
				f.set(newObject,null);
				continue;
			}
			Object cloneObj = GenericObject.makeClone(obj);
               	        f.set(newObject,cloneObj);
                    }
                }  catch (IllegalAccessException ex1 ) {
		    ex1.printStackTrace();
                    continue; // we are accessing a private field...
                }
            }
            return (Object) newObject;
        }

         /** Debug function
         */        
	public void dbgPrint() { 
            super.dbgPrint();
        }
        
        /** Encode the header into a String.
         * @return String
         */            
	public abstract String encode();
        
        /** get the input Text of the header
         * @return String
         */        
	public String getInputText() {
            return super.getInputText();
        }

        /** set the input text of the header.
         * @param s String to set
         */        
	public void setInputText( String s ) { 
            super.setInputText(s);
        }


	/**
	* An introspection based equality predicate for SIPObjects.
	*@other the other object to test against.
	*/
	public boolean equals(Object other) {
	   if (! this.getClass().equals(other.getClass())) return false;
	   SIPObject that = (SIPObject) other;	
	   Class myclass = this.getClass();
	   Field[] fields = myclass.getDeclaredFields();
	   Class hisclass = other.getClass();
	   if (! hisclass.equals(myclass)) return false;
	   Field[] hisfields = hisclass.getDeclaredFields();
	   for (int i = 0; i < fields.length; i++) {
		Field f = fields [i];
		Field g = hisfields[i];
		// Only print protected and public members.
		int modifier =  f.getModifiers();
		if ((modifier & Modifier.PRIVATE) == Modifier.PRIVATE ) continue;
		Class fieldType = f.getType();
		String fieldName = f.getName();
	        if (fieldName.compareTo("stringRepresentation") == 0 ) {
		    continue;
		} 
	        if (fieldName.compareTo("indentation") == 0 ) {
		    continue;
		} 
		if (fieldName.compareTo("inputText") == 0) {
		    continue;
	        }
		try {
		  // Primitive fields are printed with type: value 
		  if (fieldType.isPrimitive()) {
		    String fname = fieldType.toString();
		    if (fname.compareTo("int") == 0) {
			if (f.getInt(this) != g.getInt(that)) return false;
		    } else if (fname.compareTo("short") == 0) {
			if (f.getShort(this) != g.getShort(that)) return false;
		    } else if (fname.compareTo("char") == 0) {
			if (f.getChar(this) != g.getChar(that)) return false;
		    } else if (fname.compareTo("long") == 0) {
			if (f.getLong(this) != g.getLong(that)) return false;
		    } else if (fname.compareTo("boolean") == 0) {
			if (f.getBoolean(this) != g.getBoolean(that)) 
				return false;
		    } else if (fname.compareTo("double") == 0) {
			if (f.getDouble(this) != g.getDouble(that)) 
					return false;
		    } else if (fname.compareTo("float") == 0) {
			if (f.getFloat(this) != g.getFloat(that)) return false;
		    } 
		  } else if (g.get(that) == f.get(this)) continue;
		  else if (f.get(this) == null && g.get(that) != null ) 
					return false;
		  else if (g.get(that) == null && f.get(this) != null ) 
					return false;
		  else if (! f.get(this).equals(g.get(that))) return false;
		}  catch (IllegalAccessException ex1 ) {
		    System.out.println("accessed field " + fieldName);
		    System.out.println("modifier  " + modifier);
		    System.out.println("modifier.private  " + Modifier.PRIVATE);
		    InternalError.handleException(ex1);
		} 
	   }
	   return true;
	}

	/** An introspection based predicate matching using a template
	* object. Allows for partial match of two protocl Objects. 
	*@other the match pattern to test against. The match object
	* has to be of the same type (class). Primitive types 
	* and non-sip fields that are non null are matched for equality.
	* Null in any field  matches anything. Some book-keeping fields
	* are ignored when making the comparison.
	*/
	public boolean match(Object other) {
	   if (other == null) return true;
	   if ( ! this.getClass().equals(other.getClass())) 
			return false;
	   GenericObject that = (GenericObject)other;
	   Class myclass = this.getClass();
	   Field[] fields = myclass.getDeclaredFields();
	   Class hisclass = other.getClass();
	   Field[] hisfields = hisclass.getDeclaredFields();
	   for (int i = 0; i < fields.length; i++) {
		Field f = fields [i];
		Field g = hisfields[i];
		// Only print protected and public members.
		int modifier =  f.getModifiers();
		if ( (modifier & Modifier.PRIVATE) == Modifier.PRIVATE ) 
			continue;
		Class fieldType = f.getType();
		String fieldName = f.getName();
	        if (fieldName.compareTo("stringRepresentation") == 0 ) {
		    continue;
		} 
	        if (fieldName.compareTo("indentation") == 0 ) {
		    continue;
		} 
		if (fieldName.compareTo("inputText") == 0) {
		    continue;
	        }
		try {
		  // Primitive fields are printed with type: value 
		  if (fieldType.isPrimitive()) {
		    String fname = fieldType.toString();
		    if (fname.compareTo("int") == 0) {
			if (f.getInt(this) != g.getInt(that)) return false;
		    } else if (fname.compareTo("short") == 0) {
			if (f.getShort(this) != g.getShort(that)) return false;
		    } else if (fname.compareTo("char") == 0) {
			if (f.getChar(this) != g.getChar(that)) return false;
		    } else if (fname.compareTo("long") == 0) {
			if (f.getLong(this) != g.getLong(that)) return false;
		    } else if (fname.compareTo("boolean") == 0) {
			if (f.getBoolean(this) != g.getBoolean(that)) 
				return false;
		    } else if (fname.compareTo("double") == 0) {
			if (f.getDouble(this) != g.getDouble(that)) 
					return false;
		    } else if (fname.compareTo("float") == 0) {
			if (f.getFloat(this) != g.getFloat(that)) return false;
		    }   else {
			InternalError.handleException("unknown type");
		     }
		 } else {
		     Object myObj = f.get(this);
		     Object hisObj = g.get(that);
		     if (myObj == hisObj) return true;
		     else if (hisObj != null && myObj == null) return false;
		     else if (hisObj instanceof java.lang.String &&
				myObj instanceof java.lang.String) {
			if (((String)myObj).compareToIgnoreCase
				((String)hisObj) != 0) return false;
		     }
		     else if (GenericObject.isMySubclass(myObj.getClass())
			   && ! ((GenericObject) myObj).match(hisObj)) 
                         return false;
		     else if (GenericObjectList.isMySubclass(myObj.getClass()) 
			   && ! ((GenericObjectList) myObj).match(hisObj)) 
                          return false;
		     
		   }
		}  catch (IllegalAccessException ex1 ) {
		    InternalError.handleException(ex1);
		} 
	   }
	   return true;
	}


	/**
         * An introspection based string formatting method. We need this because
         * in this package (although it is an exact duplicate of the one in
         * the superclass) because it needs to access the protected members
         * of the other objects in this class.
         * @return String
         */
	public String toString() {
	   stringRepresentation = "";
	   Class myclass = getClass();
	   sprint(myclass.getName());
	   sprint ("{");
	   sprint("inputText:");
	   sprint(inputText);
	   Field[] fields = myclass.getDeclaredFields();
	   for (int i = 0; i < fields.length; i++) {
		Field f = fields [i];
		// Only print protected and public members.
		int modifier =  f.getModifiers();
		if ( (modifier & Modifier.PRIVATE) == Modifier.PRIVATE ) continue;
		Class fieldType = f.getType();
		String fieldName = f.getName();
	        if (fieldName.compareTo("stringRepresentation") == 0 ) {
		    // avoid nasty recursions...
		    continue;
		} 
	        if (fieldName.compareTo("indentation") == 0 ) {
		    // formatting stuff - not relevant here.
		    continue;
		} 
		sprint(fieldName + ":"  );
		try {
		  // Primitive fields are printed with type: value 
		  if (fieldType.isPrimitive()) {
		    String fname = fieldType.toString();
		    sprint(fname + ":");
		    if (fname.compareTo("int") == 0) {
			int intfield = f.getInt(this);
		        sprint(intfield);
		    } else if (fname.compareTo("short") == 0) {
			short shortField = f.getShort(this);
		        sprint(shortField);
		    } else if (fname.compareTo("char") == 0) {
			char charField = f.getChar(this);
		        sprint(charField);
		    } else if (fname.compareTo("long") == 0) {
			long longField = f.getLong(this);
			sprint(longField);
		    } else if (fname.compareTo("boolean") == 0) {
			boolean booleanField = f.getBoolean(this);
			sprint(booleanField);
		    } else if (fname.compareTo("double") == 0) {
			double doubleField = f.getDouble(this);
			sprint(doubleField);
		    } else if (fname.compareTo("float") == 0) {
			float floatField = f.getFloat(this);
			sprint(floatField);
		    } 
		  } else if (getClassFromName(SIP_PACKAGE+".GenericObject").
					isAssignableFrom(fieldType) ) {
	            if (f.get(this) != null) {
		      sprint(((GenericObject)f.get(this)).
				toString(indentation + 1));
		    } else {
			sprint("<null>");
		    }

		  } else if 
		    ( getClassFromName(SIP_PACKAGE+".GenericObjectList").
				isAssignableFrom(fieldType))  { 
	            if (f.get(this) != null) {
		      sprint(((GenericObjectList)f.get(this)).
			 toString(indentation + 1));
		    } else {
			sprint("<null>");
		    }

		  }  else {
			// Dont do recursion on things that are not
			// of our header type...
			   if (f.get(this) != null ) {  
			      sprint(f.get(this).getClass().getName() + ":");
			   } else {
			      sprint(fieldType.getName() + ":");
			   }
			       
			   sprint("{");
			   if (f.get(this) != null) {
			      sprint(f.get(this).toString());
			   } else {
			      sprint("<null>");
			   }
			   sprint("}");
		  } 
		}  catch (IllegalAccessException ex1 ) {
			continue; // we are accessing a private field...
		} 
	   }
	   sprint ("}");
	   return stringRepresentation;
	}


	/**
         * Formatter with a given starting indentation (for nested structs).
         * @param indent int to set
         * @return String
         */
	public String toString( int indent) {
		int save = indentation;
		indentation = indent;
		String retval =  this.toString();
		indentation = save;
		return retval;
	}


        /**
         * Do a recursive find and replace of objects pointed to by this
	 * object.
         * @since v1.0
         * @param objectText is the canonical string representation of
         *		the object that we want to replace.
         * @param replacement is the object that we want to replace it
         *		with.
         * @param matchSubstring a boolean which tells if we should match
         * 		a substring of the target object
         * A replacement will occur if a portion of the structure is found
         * with matching encoded text as objectText and with the same class
         * as replacement.
	 * (i.e. if matchSubstring is true an object is a  candidate for 
	 *  replacement if objectText is a substring of 
	 *  candidate.encode() && candidate.class.equals(replacement.class) 
	 * otherwise the match test is an equality test.)
	 *@exception IllegalArgumentException on null args and 
	 * if replacementObject does not derive from GenericObject or 
	 * GenericObjectList
         */
    public void replace(String objectText,
    GenericObject replacement,
    boolean matchSubstring )
    throws IllegalArgumentException {
        if (objectText == null || replacement == null) {
            throw new IllegalArgumentException("null argument!");
        }
        Class replacementClass = replacement.getClass();
        Class myclass = getClass();
        Field[] fields = myclass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Class fieldType = f.getType();
            if (! getClassFromName(SIP_PACKAGE+".GenericObject")
            .isAssignableFrom(fieldType) &&
            ! getClassFromName(SIP_PACKAGE+".GenericObjectList")
            .isAssignableFrom(fieldType)) {
                continue;
            } else if ( (f.getModifiers() & Modifier.PRIVATE) == Modifier.PRIVATE) {
                continue;
            }
            
            try {
                if (fieldType.equals(replacementClass)) {
                    
                    if (GenericObject.isMySubclass(replacementClass)) {
                        GenericObject  obj =
                        (GenericObject)f.get(this);
                        if (! matchSubstring) {
                            if (objectText.compareTo(obj.encode())== 0 ) {
                                f.set(this,replacement);
                            }
                        } else {
                            // Substring match is specified
                            if (obj.encode().indexOf(objectText) >= 0 ) {
                                f.set(this,replacement);
                            }
                        }
                    }
                } else if (GenericObjectList.isMySubclass
                (replacementClass)) {
                    GenericObjectList  obj =
                    (GenericObjectList)f.get(this);
                    if (! matchSubstring) {
                        if (objectText.compareTo(obj.encode())== 0 ) {
                            f.set(this,replacement);
                        }
                    } else  {
                        if (obj.encode().indexOf(objectText) >= 0 ) {
                            f.set(this,replacement);
                        }
                    }
                } else if (getClassFromName
                (SIP_PACKAGE +".GenericObject")
                .isAssignableFrom(fieldType)){
                    GenericObject g =
                    (GenericObject) f.get(this);
                    g.replace(objectText, replacement,matchSubstring);
                } else if (getClassFromName
                (SIP_PACKAGE + ".GenericObjectList")
                .isAssignableFrom(fieldType)){
                    GenericObjectList g =
                    (GenericObjectList) f.get(this);
                    g.replace( objectText, replacement,matchSubstring);
                }
            } catch (IllegalAccessException ex) {
                InternalError.handleException(ex);
            }
        }
        
    }
        /**
         * Do a find and replace of objects.
         * @since v1.0
         *@param objectText Canonical string representation of the
         *  portion we want to replace.
         *@param replacement object we want to replace this portion with.
         * A replacement will occur if a portion of the structure is found
         * with the matching encoded text as objectText and with the same class
         * as the replacement.
         *@param matchSubstring is true if we want to match the encoded
	 * text of a candidate object as a substring of the encoded 
	 * target text. ( match occurs is objectText is a substring of
	 * the encoded text of an object with the same class as replacement.)
	 *@exception IllegalArgumentException on null args and if 
	 * replacementObject
	 * does not derive from GenericObject or GenericObjectList
         */
    public void replace (String objectText,
    GenericObjectList replacement,
    boolean matchSubstring )
    throws IllegalArgumentException {
        
        if (objectText == null || replacement == null) {
            throw new IllegalArgumentException("null argument!");
        }
        Class replacementClass = replacement.getClass();
        Class myclass = getClass();
        Field[] fields = myclass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Class fieldType = f.getType();
            if (! getClassFromName
            (SIP_PACKAGE+".GenericObject")
            .isAssignableFrom(fieldType) &&
            ! getClassFromName
            (SIP_PACKAGE+".GenericObjectList")
            .isAssignableFrom(fieldType)) {
                continue;
            } else if (f.getModifiers() == Modifier.PRIVATE) {
                continue;
            }
            try {
                if (fieldType.equals(replacementClass)) {
                    if (GenericObject.isMySubclass(replacementClass)) {
                        GenericObject  obj =
                        (GenericObject)f.get(this);
                        if (! matchSubstring) {
                            if (objectText.compareTo(obj.encode())== 0 ) {
                                f.set(this,replacement);
                            }
                        } else {
                            if (obj.encode().indexOf(objectText) >= 0 ) {
                                f.set(this,replacement);
                            }
                        }
                    } else if (GenericObjectList.isMySubclass
                    (replacementClass)) {
                        GenericObjectList  obj =
                        (GenericObjectList)f.get(this);
                        if (! matchSubstring) {
                            if (objectText.compareTo(obj.encode())== 0 ) {
                                f.set(this,replacement);
                            }
                        } else  {
                            if (obj.encode().indexOf(objectText) >= 0 ) {
                                f.set(this,replacement);
                            }
                        }
                    }
                    
                } else if (getClassFromName
                (SIP_PACKAGE + ".GenericObject")
                .isAssignableFrom(fieldType)){
                    GenericObject g = (GenericObject) f.get(this);
                    g.replace(objectText, replacement,matchSubstring);
                }else if (getClassFromName
                (SIP_PACKAGE + ".GenericObjectList")
                .isAssignableFrom(fieldType)){
                    GenericObjectList g =
                    (GenericObjectList) f.get(this);
                    g.replace(objectText, replacement,matchSubstring);
                }
            } catch (IllegalAccessException ex) {
                InternalError.handleException(ex);
            }
        }
        
    }



        
}
