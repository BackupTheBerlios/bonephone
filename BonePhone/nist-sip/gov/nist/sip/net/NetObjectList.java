/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                          
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.net;

import gov.nist.sip.*;
import java.util.ListIterator;
import java.util.LinkedList;
import java.lang.reflect.*;

/**
* Root class for all the collection objects in this list:
* a wrapper class on the GenericObjectList class for lists of objects
* that can appear in NetObjects.
* IMPORTANT NOTE: NetObjectList cannot derive from NetObject as this 
* will screw up the way in which we attach objects to headers.
*/
public class NetObjectList 
	extends GenericObjectList  implements Separators {
                  
        /**
         * Construct a NetObject List given a list name.
         * @param lname String to set
         */
    public NetObjectList( String lname) { 
        super(lname);
    }
      
        /**
         * Construct a NetObject List given a list name and a class for
         * the objects that go into the list.
         * @param lname String to set
         * @param cname Class to set
         */
    public NetObjectList(String lname, Class cname) { 
        super(lname,cname);
    }
    
        /**
         * Construct a NetObject List given a list name and a class for
         * the objects that go into the list.
         * @param lname String to set
         * @param cname String to set
         */
    public NetObjectList(String lname, String cname) { 
        super(lname,cname);
    }
    
        /**
         * Construct an empty NetObjectList.
         */
    public NetObjectList() { 
        super();
    }
          
        /**
         * Add a new object to the list.
         * @param obj NetObject to set
         */
    public void add(NetObject obj) {
        super.add(obj);
    } 
    
        /** concatenate the two Lists
         * @param net_obj_list NetObjectList to set
         */    
    public void concatenate( NetObjectList net_obj_list) {
        super.concatenate(net_obj_list);
    }
    
        /**
         * Make a clone of this header list and return it.
         * For any object in the list (like SIPHeaders) that inherits from
         * GenericObject or GenericObjectList, clone the object and add it
         * to the returned List. For objects that do  not,
         * this just copies the reference over. WARNING.. NO CIRCULAR
         * REFERENCES that are accessible are tolerated (will throw this
         * method into an infinite loop). However, we dont (shouldnt) have such
         * worries because the parser generates tree sturctures.
         * @since 1.0
         * @return Object
         */  
    public Object  clone()  {
        NetObjectList newObject = (NetObjectList) super.clone();
        ListIterator li = this.listIterator();
        while(li.hasNext()) {
            Object listObj = li.next();
            Object clone_obj = makeClone(listObj);
            newObject.add(clone_obj);
            
        }
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
            System.out.println("Field type = " + fieldType.getName());
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
                    if (obj == null) {
			f.set(newObject,null);
			continue;
		    }
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
 
        /** returns the first element
         * @return GenericObject
         */    
    public GenericObject first() { 
        return (NetObject) super.first();
    }
 
        /**
         * Get the class for all objects in my list.
         * @return Class
         */
    public Class getMyClass() { 
        return super.getMyClass();
    }
  
        /**
         * Get the input text that corresponds to this list of headers.
         * @return string 
         */
    public String getInputText() {
        return super.getInputText();
    }
       
        /** returns the next element
         * @return GenericObject
         */    
    public GenericObject next() { 
        return (NetObject) super.next();
    }
    
        /** returns the next element
         * @param li ListIterator to set
         * @return GenericObject
         */    
    public GenericObject next(ListIterator li) { 
        return (NetObject) super.next(li);
    }
    
        /**
         * Do a recursive find and replace of objects.
	 *@param objectText text of the object to find.
         *@param replacementObject object to replace the target with (
	 * in case a target is found).
	 *@param matchSubstring boolean that indicates whether to flag a
         * match when objectText is a substring of a candidate object's 
	 * encoded text.
	 *@exception IllegalArgumentException on null args and if replacementObject
	 * does not derive from GenericObject or GenericObjectList
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
         * Do a recursive find and replace of objects in this list.
         *@since v1.0
	 *@param objectText text of the object to find.
         *@param replacementObject object to replace the target with (in
	 * case a target is found).
	 *@param matchSubstring boolean that indicates whether to flag a
         * match when objectText is a substring of a candidate object's 
	 * encoded text.
	 *@exception IllegalArgumentException on null args and if replacementObject
	 * does not derive from GenericObject or GenericObjectList
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
     
    /** set the class
     * @param cl Class to set
     */    
    public void setMyClass( Class cl) { 
        super.setMyClass(cl);
    }
      
        /**
         * Convert to a string given an indentation(for pretty printing).
         * @param indent int to set
         * @return String
         */
    public String toString(int indent) {
        return super.toString(indent);
    }   
    
}
