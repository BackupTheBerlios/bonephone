package bonephone;

import java.util.*;

/** Class to cycle through all key/value pairs in a Configration object.
 * This is done when not specific keys are queried, but one wants to read
 * lists, like a phonebook, where keys are not unique.
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
public class ConfigIterator {

	/** 
	 * @return next key/value pair as NamedValue object, null when the end is reached.
	*/
	public NamedValue next() {
		int i;
		NamedValue r=null;

		if (data.size()==0) return null; 
				
		for (i=index; i<data.size(); ) {
			Object o=data.elementAt(i);   i++;
			if (! (o instanceof NamedValue)) continue;
			NamedValue nv=(NamedValue) o;
			if (nv.name.equals("-empty-") || nv.name.equals("#")) continue;
			r=nv;
			break;
		}
		index=i;
		return r;
	}
	
	/** rewind to the beginning of the Configuration. */
	public void rewind() { index=0; }
	
	/** Create ConfigIterator from a Vector of NamedValue objects. Only the
	 * Configuration class should create ConfigIterator instances.
	 * @param x Vector of NamedValue objects.
	*/
	public ConfigIterator(Vector x) { 
		data=x; 
		index=0;
	}
	
	private Vector data;
	private int index;

}
