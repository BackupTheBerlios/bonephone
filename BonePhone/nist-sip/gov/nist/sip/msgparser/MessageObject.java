/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD)        *
 * See ../../../../doc/uncopyright.html for conditions of use                  *
 * Author: M. Ranganathan (mranga@nist.gov)                                    *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                              *
 ******************************************************************************/

package gov.nist.sip.msgparser;

import gov.nist.sip.*;
import java.lang.reflect.*;


/**
* This is the root object from which all other objects in this package
* are derived. This class is never directly instantiated (and hence it
* is abstract).
*/

public abstract class MessageObject extends GenericObject 
	implements SIPCloneable {
	public abstract String encode() ;

	public void dbgPrint() { super.dbgPrint(); }

	/**
	* Generic clone method. This cannot be in the superclass because
	* we need to access protected fields.
	*/
        public Object clone()  {
             MessageObject newObject  = (MessageObject) super.clone();
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
			if (obj == null) continue;
			Object clone_obj = makeClone(obj);
               		f.set(newObject,clone_obj);
                    }
                }  catch (IllegalAccessException ex1 ) {
		    ex1.printStackTrace();
                    continue; // we are accessing a private field...
                }
            }
            return (Object) newObject;
        }

	protected  MessageObject() { super(); }

	public String getInputText() { return super.getInputText(); }

	public void setInputText( String s ) { super.setInputText(s); }

	/**
	* An introspection based string formatting method. We need this because
	* in this package (although it is an exact duplicate of the one in
	* the superclass) because it needs to access the protected members
	* of the other objects in this class.
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
		if (modifier == Modifier.PRIVATE) continue;
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
	*/
	public String toString( int indent) {
		int save = indentation;
		indentation = indent;
		String retval =  this.toString();
		indentation = save;
		return retval;
	}
        

}
