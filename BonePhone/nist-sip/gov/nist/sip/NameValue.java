/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../doc/uncopyright.html for conditions of use.                     *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip;
/**
*  Generic structure for storing name-value pairs.
*@version 1.0 
*/
public class NameValue  extends GenericObject {
	protected String separator;
        protected String name;
        protected Object value;
	public NameValue() { name = null; value = null;  
			separator = Separators.EQUALS; }
	public NameValue( String n, Object v) 
		{ name = n; value = v;  separator = Separators.EQUALS; }
	/**
	* Set the separator for the encoding method below.
	*/
	public void setSeparator( String sep) {
		separator = sep;
	}

	public	 String getName() 
 	 	{ return name ; } 
	public	 Object getValue() 
 	 	{ return value ; } 
	/**
	* Set the name member  
	*/
	public	 void setName(String n) 
 	 	{ name = n ; } 
	/**
	* Set the value member  
	*/
	public	 void setValue(Object v) 
 	 	{ value = v ; } 

	public void setInputText( String text) { super.setInputText(text); }
	public String getInputText() { return super.getInputText(); }
	/**
	* Get the encoded representation of this namevalue object
	*@since 1.0
	*/
	public String encode() { 
		if ( name != null && value != null) {
		    if (GenericObject.isMySubclass(value.getClass())) {
		     GenericObject gv = (GenericObject) value;
		     return name + separator + gv.encode();
		    } else 
		      if (GenericObjectList.isMySubclass(value.getClass())) {
		      GenericObjectList gvlist = (GenericObjectList) value;
		      return name + separator + gvlist.encode();
		    } else
			 return name + separator + value.toString();
		 } else if (name == null && value != null ) {
		    if (GenericObject.isMySubclass(value.getClass())) {
		     GenericObject gv = (GenericObject) value;
		     return  gv.encode();
		    } else 
		      if (GenericObjectList.isMySubclass(value.getClass())) {
		      GenericObjectList gvlist = (GenericObjectList) value;
		      return gvlist.encode();
		    } else
			 return value.toString();
		} else if (name != null && value == null) {
		     return name;
		} else return "";
	}

	public Object clone() {
		NameValue retval = new NameValue();
		retval.separator = this.separator;
		retval.name = this.name;
		retval.value = makeClone(this.value);
		return retval;
	}

	/** 
	* Equality comparison predicate.
	*/
	public boolean equals( Object other) {
		if (! other.getClass().equals(this.getClass()))  return false;
	        NameValue that = (NameValue) other;
		if (this == that) return true;
		if (this.name  == null && that.name != null ||
		   this.name != null && that.name == null) return false;
		if (this.name != null && that.name != null &&
			this.name.compareToIgnoreCase(that.name) != 0) return false;
		if ( this.value != null && that.value == null ||
		     this.value == null && that.value != null) return false;
		if (this.value == that.value) return true;
		return this.value.equals(that.value);
	}

}
