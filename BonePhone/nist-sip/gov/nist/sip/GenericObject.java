/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
 * See ../../../doc/uncopyright.html for conditions of use.                     *
 * Author: M. Ranganathan (mranga@nist.gov)                                     *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                               *
 *******************************************************************************/
package gov.nist.sip;
import java.lang.reflect.*;
import java.util.LinkedList;
/**
 * The base class from which all the other classes in the
 * sipheader, sdpfields and sipmessage packages are extended.
 * Provides a few utility funcitons such as indentation and
 * pretty printing that all other classes benifit from.
 * @version 1.0
 */

public abstract class GenericObject  implements PackageNames, SIPCloneable {
    protected   int indentation;
    protected String inputText;
    protected String stringRepresentation;
    
    public static Class getClassFromName( String className) {
        try {
            return Class.forName(className);
        } catch ( Exception ex) {
            InternalErrorHandler.handleException(ex);
            return null;
        }
    }
    
    public static boolean isMySubclass(Class other) {
        try {
            return Class.forName(SIP_PACKAGE+".GenericObject")
            .isAssignableFrom(other);
        } catch ( Exception ex) {
            InternalErrorHandler.handleException(ex);
        }
        return false;
    }
    
    protected static boolean isSipCloneable(Object obj) {
        return obj instanceof GenericObject ||
        obj instanceof GenericObjectList;
    }
    
        /**
         *Make a clone of the given object.
         */
    protected static Object makeClone( Object obj) {
        Object clone_obj = obj;
        if (obj instanceof String ) {
            String string = (String) obj;
            clone_obj = (Object) new String(string);
        } else if (obj instanceof Integer) {
            clone_obj = new Integer(((Integer)obj).intValue());
        } else if ( obj instanceof Float ) {
            clone_obj = new Float(((Float) obj).floatValue());
        } else if (obj instanceof Double) {
            clone_obj = new Double(((Double) obj).doubleValue());
        } else if (obj instanceof Long) {
            clone_obj = new Long(((Long)obj).longValue());
        } else {
            // If a clone method exists for the object, then
            // invoke it
            try {
                Class cl = obj.getClass();
                Method meth = cl.getMethod("clone",null);
                clone_obj = meth.invoke(obj,null);
            } catch (SecurityException ex) {
                clone_obj = obj;
            } catch (IllegalArgumentException ex) {
                InternalErrorHandler.handleException(ex);
            } catch (IllegalAccessException ex) {
                clone_obj = obj;
            } catch (InvocationTargetException ex) {
                clone_obj = obj;
            } catch (NoSuchMethodException ex) {
                clone_obj = obj;
            }
        }
        return clone_obj;
    }
    
        /**
         *Make a clone of this object.
         */
    public Object clone() {
        Class myclass = this.getClass();
        Object newObject = null;
        try {
            newObject = myclass.newInstance();
        } catch (Exception ex) {
            InternalErrorHandler.handleException(ex);
        }
        GenericObject gobj = (GenericObject) newObject;
        if (this.inputText != null)
            gobj.inputText = new String(this.inputText);
        return newObject;
    }
    
    
    
    protected GenericObject() {
        indentation =  0;
        inputText = null;
        stringRepresentation = "";
    }
    
    
    
    protected String getIndentation() {
        String retval = "";
        for (int i = 0; i < indentation; i++) retval += " ";
        return retval;
    }
    
        /**
         * Add a new string to the accumulated string representation.
         */
    
    protected void sprint( String a ) {
        if (a == null) {
            stringRepresentation += getIndentation();
            stringRepresentation += "<null>\n";
            return;
        }
        if (a.compareTo("}") == 0 || a.compareTo("]") == 0 ) {
            indentation--;
        }
        stringRepresentation += getIndentation();
        stringRepresentation += a;
        stringRepresentation += "\n";
        if (a.compareTo("{") == 0 || a.compareTo("[") == 0) {
            indentation++;
        }
        
    }
    
        /**
         * Pretty printing function accumulator for objects.
         */
    
    protected void sprint( Object o ) {
        sprint(o.toString());
    }
    
        /**
         * Pretty printing accumulator function for ints
         */
    
    protected void sprint( int intField) {
        sprint((new Integer(intField)).toString());
    }
    
        /**
         * Pretty printing accumulator function for shorts
         */
    protected void sprint( short shortField) {
        sprint((new Short(shortField)).toString());
    }
    
        /**
         * Pretty printing accumulator function for chars
         */
    
    protected void sprint( char charField) {
        sprint((new Character(charField)).toString());
        
    }
    
        /**
         * Pretty printing accumulator function for longs
         */
    
    protected void sprint (long longField) {
        sprint((new Long(longField)).toString());
    }
    
        /**
         * Pretty printing accumulator function for booleans
         */
    
    protected void sprint (boolean booleanField) {
        sprint((new Boolean(booleanField)).toString());
    }
    
        /**
         * Pretty printing accumulator function for doubles
         */
    
    protected void sprint (double doubleField) {
        sprint((new Double(doubleField)).toString());
    }
    
        /**
         * Pretty printing accumulator function for floats
         */
    
    protected void sprint ( float floatField) {
        sprint((new Float(floatField)).toString());
    }
    
        /**
         * Debug printing function.
         */
    
       protected void dbgPrint() {
          String stringrep = toString();
          Debug.println(stringrep);
       }

        /**
         * Debug printing function.
         */
	protected void dbgPrint(String s) {
           Debug.println(s);
	}

	/**
	* An introspection based equality predicate for GenericObjects.
	*@other the other object to test against.
	*/
	public boolean equals(Object that) {
	   if (! this.getClass().equals(that.getClass())) return false;
	   Class myclass = this.getClass();
	   Field[] fields = myclass.getDeclaredFields();
	   Class hisclass = that.getClass();
	   Field[] hisfields = hisclass.getDeclaredFields();
	   for (int i = 0; i < fields.length; i++) {
		Field f = fields [i];
		Field g = hisfields[i];
		// Only print protected and public members.
		int modifier =  f.getModifiers();
		if ( (modifier & Modifier.PRIVATE) == Modifier.PRIVATE) 
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
		    } 
		  } else if (g.get(that) == f.get(this)) return true;
		  else if (f.get(this) == null) return false;
		  else if (g.get(that) == null) return false;
		  else return f.get(this).equals(g.get(that));
		}  catch (IllegalAccessException ex1 ) {
		    InternalErrorHandler.handleException(ex1);
		} 
	   }
	   return false;
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
		if ( (modifier & Modifier.PRIVATE) == Modifier.PRIVATE)  
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
		    }  
		 } else {
		     Object myObj = f.get(this);
		     Object hisObj = g.get(that);
		     if (hisObj == myObj ) return true;
		     else if (hisObj != null && myObj == null) return false;
		     else if (GenericObject.isMySubclass(myObj.getClass())
			   && ! ((GenericObject) myObj).match(hisObj)) 
                         return false;
		     else if (hisObj instanceof java.lang.String &&
				myObj instanceof java.lang.String) {
			if (((String)myObj).compareToIgnoreCase
				((String)hisObj) != 0) return false;
		     }
		     else if (GenericObjectList.isMySubclass(myObj.getClass()) 
			   && ! ((GenericObjectList) myObj).match(hisObj)) 
                          return false;
		    
		   }
		}  catch (IllegalAccessException ex1 ) {
		    InternalErrorHandler.handleException(ex1);
		} 
	   }
	   return true;
	}
    
        /**
         * Generic print formatting function:
         * Does depth-first descent of the structure and
         * recursively prints all non-private objects pointed to
         * by this object.
         * <bf>
         * Warning - the following generic string routine will
         * bomb (go into infinite loop) if there are any circularly linked
         * structures so if you have these, they had better be private!
         * </bf>
         * We dont have to worry about such things for our structures
         *(we never use circular linked structures).
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
            if ( (modifier & Modifier.PRIVATE)  == Modifier.PRIVATE) 
			continue;
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
                } else if (Class.forName(SIP_PACKAGE+".GenericObject").
                isAssignableFrom(fieldType) ) {
                    if (f.get(this) != null) {
                        sprint(((GenericObject)f.get(this)).
                        toString(indentation + 1));
                    } else {
                        sprint("<null>");
                    }
                    
                } else if
                ( Class.forName(SIP_PACKAGE+".GenericObjectList").
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
            } catch (Exception ex) {
                InternalErrorHandler.handleException(ex);
            }
        }
        sprint ("}");
        return stringRepresentation;
    }
    
        /**
         * Formatter with a given starting indentation.
         */
    public String toString( int indent) {
        indentation = indent;
        String retval =  this.toString();
        indentation = 0;
        return retval;
    }
    
        /**
         * Returns the input text that matched for the parse.
         */
    public String getInputText() {
        return inputText;
    }
    
        /**
         * Set the inputText field.
         */
    
    protected void setInputText( String text ) {
        inputText = text;
    }
    
        /**
         * An assertion checking utility.
         */
    
    protected void Assert (boolean condition, String msg) {
        if ( ! condition ) InternalErrorHandler.handleException(msg);
    }
    
        /**
         *  Get the string encoded version of this object
         * @since v1.0
         */
    public abstract String  encode();
    
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
         * with matching encoded text (a substring if matchSubstring is true)
         * as objectText and with the same class as replacement.
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
            } else if ( (f.getModifiers() & Modifier.PRIVATE)  
			== Modifier.PRIVATE) {
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
                InternalErrorHandler.handleException(ex);
            }
        }
        
    }
    
        /**
         * Do a recursive find and replace of objects pointed to by this
	 * object.
         * @since v1.0
         *@param objectText Canonical string representation of the
         *  portion we want to replace.
         *@param replacement object we want to replace this portion with.
         * A replacement will occur if a portion of the structure is found
         * with a match of the  encoded text with objectText and with the same class
         * as replacement.
         *@param matchSubstring is true if we want to match objectText
         * 	as a substring of the encoded target text. 
	 * (i.e. an object is a  candidate for replacement if objectText is a substring of 
	 *  candidate.encode() && candidate.class.equals(replacement.class) 
	 * otherwise the match test is an equality test.)
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
            } else if ( (f.getModifiers() & Modifier.PRIVATE)  
		== Modifier.PRIVATE) {
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
                InternalErrorHandler.handleException(ex);
            }
        }
        
    }
    
}
