package bonephone;

import java.util.*;
import java.io.*;

/** A Configuration object reads a file with lines of the form:<br>
 * key: value<br>
 * # comment<br>
 * empty-line<br>
 *
 * each key/value pair, empty line and comment is stored for data retrieval.
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
public class Configuration {

	/** Build Configuration from config file. */
	public Configuration (String cfgfilename) {
		init(cfgfilename);
	}

	/** Build empty Configuration. No filename is associated with this object. */
	public Configuration () {
		init(null);
	}

	private void init(String cfgfilename) {
		filename=cfgfilename;
		pairs=new Vector (20);
		FileInputStream fis;
		try {
			fis= new FileInputStream(filename);
		} catch (FileNotFoundException fnfe) {
			return;
		} catch (SecurityException se) {
			System.err.println(se.getMessage());
			return;
		}
		String key,value,line;
		line="";
		int b;
		while (true) {
			try { b=fis.read(); }
			catch (IOException ioe) {
				System.err.println(ioe.getMessage());
				try { fis.close(); } catch (IOException ioe2) {}
				return;
			}
			if (b==-1) break;
			byte[] bb= { (byte)b } ;
			String bbb=new String(bb);
			if (b==13) continue;
			if (b!=10) {
				line=line+bbb;
				continue;
			}
			line=line.trim();
			// parse line
			int i;
			if (line.length()==0) {
				NamedValue x=new NamedValue("-empty-",null);
				pairs.addElement(x);
				line="";
				continue;
			}
			if (line.charAt(0)=='#') {
				NamedValue x=new NamedValue("#",line.substring(1));
				pairs.addElement(x);
				line="";
				continue;
			}
			char c;
			for (i=0; i<line.length(); i++) {
				c=line.charAt(i);
				if (c==':') {
					String l,r;
					l=line.substring(0,i).trim();
					r=line.substring(i+1).trim();
					NamedValue x=new NamedValue(l,r);
					pairs.addElement(x);
					break;
				}
			}
			line="";
		}
		try { fis.close(); } catch (IOException ioe) {}
	}

	private int getIndex(String Key) {
		int i;
		for (i=0; i<pairs.size(); i++) {
			Object o=pairs.elementAt(i);
			NamedValue n=(NamedValue)o;
			if (n.name.equals(Key)) return i;
		}
		return -1;
	}

	private String get(String Key) {
		int i=getIndex(Key);
		if (i==-1) return "";
		Object o=pairs.elementAt(i);
		NamedValue n=(NamedValue)o;
		return n.value;
	}

	/** Does a certain key exist in this configuration ? 
	 * @param key key to test.
	 * @return true on success (key exists), false if key is not present.
	*/
	public boolean exists(String key) {
		if (getIndex(key)==-1) return false;
		return true;
	}

	/** retrieve String value of a key.
	 * @param key name of key to look up.
	 * @return value of key, empty String "" if it does not exist.
	*/
	public String getAsString(String key) {
		return get(key);
	}

	/** retrieve integer value of a key.
	 * @param key name of key to look up.
	 * @return value of key, 0 (zero) if it does not exist.
	*/
	public int    getAsInt(String key) {
		Integer x;
		try {
			x=new Integer(get(key));
		} catch (NumberFormatException y) {
			x=new Integer(0);
		}
		return x.intValue();
	}

	/** Set value for a key. If it exists, overwrite its value, if does not exit, create it. 
	 * @param key name of key to create/modify.
	 * @param value Value to associate with the key.
	*/
	public void   set(String key, String value) {
		int i=getIndex(key);
		if (i==-1) {
			NamedValue x=new NamedValue(key,value);
			pairs.addElement(x);
			return;
		}		
		Object o=pairs.elementAt(i);
		NamedValue n=(NamedValue)o;
		n.value=value;
	}

	/** Create a ConfigIterator for this Configuration. */
	public ConfigIterator getIterator() { return new ConfigIterator(pairs); }

	/** Sets a key only if it does not already exist.
	 * @param key name of key to modify.
	 * @param value Value to associate with the key.
	*/
	public boolean setUnset(String key, String value) {
		int i=getIndex(key);
		if (i!=-1) return false;
		NamedValue x=new NamedValue(key,value);
		pairs.addElement(x);
		return true;
	} 


	/** Write Configuration to a file in the above described format. 
	 * @param configfilename Filename to write to.
	*/
	public void   writeOut(String configfilename) throws FileNotFoundException {
		int i;
		FileOutputStream fos= new FileOutputStream(configfilename);
		PrintStream ps=new PrintStream(fos);
		for (i=0; i<pairs.size(); i++) {
			Object o=pairs.elementAt(i);
			NamedValue n=(NamedValue)o;
			if (n.name.equals("#")) {
				ps.println("#"+n.value);
				continue;
			}
			if (n.name.equals("-empty-")) {
				ps.println("");
				continue;
			}
			ps.println(n.name+": "+n.value);
		}
		ps.close();
	}

	/** Write Configuration to its default file, named at construction time.
	*/
	public void   writeOut() throws FileNotFoundException { 
		if (filename==null) return;
		writeOut(filename); 
	}

	private String filename;
	private Vector pairs;

}
