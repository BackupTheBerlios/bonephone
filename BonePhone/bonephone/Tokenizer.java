package bonephone;

import java.util.*;



/** Tokenizer that breaks Strings into items in a Vector. */
public class Tokenizer {


	private static String trim (String data, String sep) {
		int i,p;
		String t;

		if (data.length()==0) return "";

		// vorne
		while (true) {
			t=data.substring(0,1);
			p=sep.indexOf(t);
			if (p<0) break;
			data=data.substring(1);
		}
		
		// hinten
		while (true) {
			i=data.length()-1;
			t=data.substring(i);
			p=sep.indexOf(t);
			if (p<0) break;
			data=data.substring(0,i);
		}
		
		return data;
	}


	private static int firstSep (String data, String sep) {
		int i,x;

		for (i=0; i<sep.length(); i++) {
			x=data.indexOf(sep.substring(i,i+1));
			if (x>=0) return x;
		}
		return -1;
	}


	/** break a string into items.
	* @param data the raw String to examine.
	* @param sep String of separating characters.
	* @return a Vector of String objects. Each String has none of the separator characters. 
	*/
	public static Vector split(String data, String sep) {
		int x;
		Vector v=new Vector(1);
		String token;

		do {
			data=trim(data,sep);
			if (data.length()==0) break;
			x=firstSep(data,sep);
			if (x<0) {
				v.addElement(data);
				break;
			}
			token=trim(data.substring(0,x),sep);
			v.addElement(token);
			data=trim(data.substring(x+1),sep);
		} while (x>=0);

		return v;
	}

	/** empty constructor */
	public Tokenizer () {}

}
