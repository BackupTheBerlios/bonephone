/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../doc/uncopyright.html for conditions of use.                     *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip;
import java.util.*;
import java.lang.reflect.*;


/**
* Implements a homogenous consistent linked list. 
* All the objects in the linked list
* must derive from the same root class. This is a useful constraint to place on
* our code as this property is invariant.The list is created with the superclass
* which can be specified as either a class name or a Class. 
*/
public abstract class GenericObjectList extends  LinkedList implements 
PackageNames, SIPCloneable {
    protected int  indentation;
    protected String listName; // For debugging
    protected String inputText;
    private ListIterator myListIterator;
    private String stringRep;
    protected Class myClass;
    protected String separator;
    
    protected String getIndentation() {
        String retval = "";
        for (int i = 0; i < indentation; i++) retval += " ";
        return retval;
    }
    protected static boolean isSipCloneable(Object obj) {
        return obj instanceof GenericObject ||
        obj instanceof GenericObjectList;
    }
    
    
    
        public static boolean isMySubclass(Class other) {
            try {
               return Class.forName(SIP_PACKAGE+".GenericObjectList")
			.isAssignableFrom(other);
            } catch ( Exception ex) {
                InternalErrorHandler.handleException(ex);
            }
	    return false;
	}
    
        /**
         *Make a clone of the given object.
         *Cloning rules are as follows:
         *Strings and wrapped basic types are cloned.
         *If the object supports a clone method then it is called
         *Otherwise the original object is returned.
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
         * Implement the clone method.
         */
    public Object clone() {
        Class myclass = this.getClass();
        Object newObject = null;
        try {
            newObject = myclass.newInstance();
        } catch (Exception ex) {
            InternalErrorHandler.handleException(ex);
        }
        GenericObjectList gobj = (GenericObjectList) newObject;
        gobj.clear();
        if (this.inputText != null)
            gobj.inputText = new String(this.inputText);
        gobj.separator = this.separator;
        gobj.myClass = this.myClass;
        gobj.listName = new String(this.listName);
        return newObject;
    }
    
        /**
         * set the matching text for this header list.
         */
    public void setInputText( String input) {
        inputText = input;
    }
    
    
        /**
         * Sets the class that all our elements derive from.
         */
    protected Class getMyClass() {
        return myClass;
    }
    
    protected void setMyClass( Class cl ) {
        myClass = cl;
    }
    
    protected GenericObjectList() {
        super();
        listName = null;
        inputText = null;
        stringRep = "";
        separator = Separators.SEMICOLON;
    }
    
    
    protected GenericObjectList (String lname ) {
	this();
        listName = lname;
    }
    
        /**
         * A Constructor which takes a list name and a
         * class name (for assertion checking).
         */
    
    protected GenericObjectList (String lname, String classname) {
	this(lname);
        try  {
            myClass = Class.forName(classname);
        } catch (ClassNotFoundException ex) {
            InternalErrorHandler.handleException(ex);
        }
        
    }
    
        /**
         * A Constructor which takes a list name and a class
         * (for assertion checking).
         */
    
    protected GenericObjectList (String lname, Class objclass) {
	this(lname);
        myClass = objclass;
    }
    
    
        /**
         * A utility that checks for assignability. (useful for
         * constructors)
         */
    protected void
    checkAssignability( String class1Name, String class2Name)
    throws ClassCastException {
        try  {
            Class class1 = Class.forName(class1Name);
            Class class2 = Class.forName(class2Name);
            if (! class1.isAssignableFrom(class2))  {
                throw new ClassCastException( class2.getName()  +
                " cannot be assigned from " + class1.getName());
            }
        } catch (ClassNotFoundException ex) {
            InternalErrorHandler.handleException(ex);
        }
        
    }
    
    
    
        /**
         *  Traverse the list given a list iterator
         */
    protected GenericObject next( ListIterator iterator) {
        try {
            return  (GenericObject) iterator.next();
        } catch (NoSuchElementException ex) {
            return null;
        }
    }
    
    
        /**
         *  This is the default list iterator.This will not handle
         * nested list traversal.
         */
    protected GenericObject first() {
        myListIterator = this.listIterator(0);
        try {
            return (GenericObject) myListIterator.next();
        } catch (NoSuchElementException ex) {
            return null;
        }
    }
    
        /**
         *  Fetch the next object from the list based on
         *  the default list iterator
         */
    protected GenericObject next()
    {
        if (myListIterator == null) {
            myListIterator = this.listIterator(0);
        }
        try {
            return  (GenericObject) myListIterator.next();
        } catch (NoSuchElementException ex) {
            myListIterator = null;
            return null;
        }
    }
    
        /**
         * Concatenate two compatible header lists, adding the argument to the
         * tail end of this list.
         * @param <var> topFlag </var> set to true to add items to top of list
         */
    protected void concatenate( GenericObjectList objList ) {
        concatenate(objList,false);
    }
    
        /**
         * Concatenate two compatible header lists, adding the argument 
	 * either to the beginning
         * or the tail end of this list.
         * A type check is done before concatenation.
         * @param <var> topFlag </var> set to true to add items to top of 
	 *  	list else add them to the tail end of the list.
         */
    protected void concatenate( GenericObjectList objList, boolean topFlag ) {
        if (! topFlag ) {
            if (objList.inputText != null) {
                if (this.inputText != null) this.inputText +=
                objList.inputText;
                else this.inputText  = objList.inputText;
                // add all the elements from the other header list.
            }
            this.addAll(objList);
        } else {
            // add given items to the top end of the list.
            if (objList.inputText != null) {
                if (this.inputText != null) this.inputText = 
			objList.inputText + this.inputText;
                else this.inputText  = objList.inputText;
                // add all the elements from the other header list.
            }
            this.addAll(0,objList);
        }
    }
    
    
        /**
         * Get the input text for this header list.
         */
    public String getInputText() {
        return inputText;
    }
    
    
    /**
     *Get the list iterator for this list.
     */
    public Iterator getIterator() { return this.listIterator(); }
    
        /**
         * string formatting function.
         */
    
    private void sprint( String s ) {
        if (s == null) {
            stringRep += getIndentation();
            stringRep += "<null>\n";
            return;
        }
        
        if (s.compareTo("}") == 0 || s.compareTo("]") == 0 ) {
            indentation--;
        }
        stringRep += getIndentation();
        stringRep += s;
        stringRep += "\n";
        if (s.compareTo("{") == 0 || s.compareTo("[") == 0 ) {
            indentation++;
        }
    }
    
        /**
         * Convert this list of headers to a formatted string.
         */
    
    public String toString() {
        stringRep = "";
        Object obj = this.first();
        if (obj == null) return "<null>";
        sprint("listName:");
        sprint(listName);
        sprint("{");
        if (inputText != null) {
            sprint("inputText:");
            sprint(inputText);
        }
        while(obj != null) {
            sprint("[");
            try {
                if ( Class.forName
                (SIP_PACKAGE + ".GenericObjectList").
                isAssignableFrom(obj.getClass()) ) {
                    sprint(((GenericObjectList) obj).
                    toString(this.indentation));
                } else
                    if ( Class.forName(SIP_PACKAGE+ ".GenericObject").
                    isAssignableFrom(obj.getClass()))  {
                        sprint(((GenericObject) obj).
                        toString(this.indentation));
                    }
            } catch ( ClassNotFoundException ex) {
                InternalErrorHandler.handleException(ex);
            }
            obj = next();
            sprint("]");
        }
        sprint("}");
        return stringRep;
    }
    
    
        /**
         * Convert this list of headers to a string
         * (for printing) with an indentation given.
         */
    
    public  String toString( int indent) {
        int save = indentation;
        indentation = indent;
        String retval =  this.toString();
        indentation = save;
        return retval;
    }
    
    public  boolean add (Object obj) {
        if (myClass == null) {
            myClass = obj.getClass();
            return super.add(obj);
        } else {
            Class newclass = obj.getClass();
            if ( ! myClass.isAssignableFrom(newclass)) {
                InternalErrorHandler.handleException
                ("Class mismatch list insertion  " +
                listName + " " +
                newclass.getName() + "/" + myClass.getName());
            }
            return super.add(obj);
        }
    }

    
        /**
         *  Type checked add operation.
         *  All objects in this list are assignable from a common
         *  superclass.  If the class is already set, then the new one
         *  is just compared with the existing class objects. Otherwise
         *  the first object that is added determines the class of the
         *  objects in the list.
         */
    
    protected  void add (GenericObject obj) {
        if (myClass == null) {
            myClass = obj.getClass();
            super.add(obj);
        } else {
            Class newclass = obj.getClass();
            if ( ! myClass.isAssignableFrom(newclass)) {
                InternalErrorHandler.handleException
                ("Class mismatch list insertion  " +
                listName + " " +
                newclass.getName() + "/" + myClass.getName());
            }
            super.add(obj);
        }
        if (this.inputText == null) {
            this.inputText = obj.inputText;
        } else this.inputText += obj.inputText;
    }
    
        /**
         * Do a find and replace of objects in this list.
	 *@param objectText text of the object to find.
         *@param replacementObject object to replace the target with (
	 * in case a target is found).
	 *@param matchSubstring boolean that indicates whether to flag a
         * match when objectText is a substring of a candidate object's 
	 * encoded text.
         */
    public void replace(String objectText,
       GenericObject replacementObject,
	boolean matchSubstring ) 
	throws IllegalArgumentException {
        
	if (objectText == null || replacementObject == null) {
		throw new IllegalArgumentException("null argument");
        }
        ListIterator listIterator = this.listIterator();
	LinkedList ll = new LinkedList();
        
        while(listIterator.hasNext()) {
            Object obj = listIterator.next();
            if (GenericObject.isMySubclass(obj.getClass())) {
                GenericObject gobj = (GenericObject)obj;
                if (gobj.getClass().equals
                    (replacementObject.getClass())) {
                     if ( (!matchSubstring) &&
		      gobj.encode().compareTo(objectText) == 0 ) {
                        // Found the object that we want,
		        ll.add(obj);
                    } else if ( 
		        matchSubstring && gobj.encode().indexOf(objectText)
		         >= 0 ) {
		        ll.add(obj);
		    } else {
                        gobj.replace(objectText,replacementObject,
                                matchSubstring);
                    }   
                }
            } else if (GenericObjectList.isMySubclass(obj.getClass())) {
                GenericObjectList gobj = (GenericObjectList)obj;
                if (gobj.getClass().equals
                (replacementObject.getClass())) {
                    if ((!matchSubstring) &&
		         gobj.encode().compareTo(objectText) == 0 ) {
                        // Found the object that we want,
		        ll.add(obj);
		    } else if (matchSubstring && 
			gobj.encode().indexOf(objectText) >= 0 ) {
		        ll.add(obj);
                    } else {
                         gobj.replace
                        (objectText,replacementObject,matchSubstring);
                    }
                }
            }
        }
	for (int i = 0; i < ll.size(); i++ ) {
		Object obj = ll.get(i);
                this.remove(obj);
                this.add(i,(Object)replacementObject);
	}
        
    }
    
        /**
         * Do a find and replace of objects in this list.
         *@since v1.0
	 *@param objectText text of the object to find.
         *@param replacementObject object to replace the target with (in
	 * case a target is found).
	 *@param matchSubstring boolean that indicates whether to flag a
         * match when objectText is a substring of a candidate object's 
	 * encoded text.
         */
    public void replace(String objectText,
    	GenericObjectList replacementObject,
	boolean matchSubstring )
        throws IllegalArgumentException {
	if (objectText == null || replacementObject == null) {
		throw new IllegalArgumentException("null argument");
        }
        
        ListIterator listIterator = this.listIterator();
	LinkedList ll = new LinkedList();
        
        while(listIterator.hasNext()) {
            Object obj = listIterator.next();
            if (GenericObject.isMySubclass(obj.getClass())) {
                GenericObject gobj = (GenericObject)obj;
                if (gobj.getClass().equals
                (replacementObject.getClass())) {
                	if ( (!matchSubstring) &&
		   	  gobj.encode().compareTo(objectText) == 0 ) {
                    	// Found the object that we want,
		    	ll.add(obj);
                	} else if (matchSubstring &&
		   	  gobj.encode().indexOf(objectText) >= 0) {
		    	  ll.add(obj);
			} else {
                    		gobj.replace
                    		(objectText,replacementObject,matchSubstring);
                	}
		}
            } else if (GenericObjectList.isMySubclass(obj.getClass())) {
                GenericObjectList gobj = (GenericObjectList)obj;
                if (gobj.getClass().equals
                (replacementObject.getClass())) {
                  if ( (!matchSubstring) &&
		      gobj.encode().compareTo(objectText) == 0 ) {
                      // Found the object that we want,
		      ll.add(obj);
                  } else if (matchSubstring &&
		      gobj.encode().indexOf(objectText) >= 0) {
		      ll.add(obj);
		  } else {
                      gobj.replace
                       (objectText,replacementObject,matchSubstring);
                  }
		
                }
            }
        }
	for (int i = 0; i < ll.size(); i++ ) {
		Object obj = ll.get(i);
                this.remove(obj);
                this.add(i,(Object)replacementObject);
	}
        
    }
    
    
        /**
         * Encode the list in semicolon separated form.
	 * @return an encoded string containing the objects in this list.
         * @since v1.0
         */
    public String encode() {
        if (this.isEmpty()) return "";
        StringBuffer encoding = new StringBuffer();
        ListIterator iterator = this.listIterator();
        if (iterator.hasNext()) {
            while(true) {
                Object obj = iterator.next();
                if (obj instanceof GenericObject) {
                    GenericObject gobj = (GenericObject) obj;
                    encoding.append(gobj.encode());
                } else {
                    encoding.append(obj.toString());
                }
                if (iterator.hasNext()) encoding.append(separator);
                else break;
            }
        }
        return encoding.toString();
    }
    
        /**
         *  Set the separator (for encoding the list)
         * @since v1.0
         * @param sep is the new seperator (default is semicolon)
         */
    public void setSeparator (String sep ) {
        separator = sep;
    }


	/**
	* Equality checking predicate.
	*@param that is the object to compare ourselves to.
	*/
     public boolean equals(Object other) {
	if ( ! this.getClass().equals(other.getClass()) ) return false;
	GenericObjectList that = (GenericObjectList) other;
	if (  this.size() != that.size()) return false;
	ListIterator myIterator = this.listIterator();
	while (myIterator.hasNext()) {
	    Object myobj = myIterator.next();
	    ListIterator hisIterator = that.listIterator();
	    try {
	      while(true) {
		Object hisobj = hisIterator.next();
		if (myobj.equals(hisobj)) break;
	      }
	    } catch (NoSuchElementException ex) {
		return false;
	    }
	}
	ListIterator hisIterator = that.listIterator();
	while (hisIterator.hasNext()) {
	    Object hisobj =  hisIterator.next();
	    myIterator = this.listIterator();
	    try {
	      while(true) {
		Object myobj =  myIterator.next();
		if (hisobj.equals(myobj)) break;
	      }
	    } catch (NoSuchElementException ex) {
		return false;
	    }
	}
	return true;
     }


	/** Match with a template (return true if we have a superset of the
	* given template. This can be used for partial match 
	* (template matching of SIP objects). Note -- this implementation is
	* not unnecessarily efficient  :-) 
	* @param other template object to compare against.
	*/

     public boolean match(Object other) {
	if ( ! this.getClass().equals(other.getClass()) ) return false;
	GenericObjectList that = (GenericObjectList) other;
	ListIterator hisIterator = that.listIterator();
	outer:
	while (hisIterator.hasNext()) {
	    Object hisobj =  hisIterator.next();
	    Object myobj = null;
	    ListIterator myIterator = this.listIterator();
	    try {
	      while(true) {
		myobj =  myIterator.next();
		if ( GenericObject.isMySubclass(myobj.getClass()) &&
		    ((GenericObject) myobj).match(hisobj) ) break outer;
		else if (GenericObjectList.isMySubclass(myobj.getClass()) &&
			((GenericObjectList) myobj).match(hisobj) ) break outer;
	      }
	    } catch (NoSuchElementException ex) {
		return false;
	    }
	}
	return true;
	
     }

  
    

}
		
