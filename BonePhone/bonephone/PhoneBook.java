package bonephone;

import java.util.*;


/**
 * This class reads the phonebook config file and hold its data.
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
public class PhoneBook implements VectorRead {


	/**
	 * @param i index in the phonebook (i.e. number of entry).
	 * @return PhoneBookEntry Objects at specified position or null if index does not exist.
	*/
	public Object elementAt(int i) {
		return getPhoneBookEntry(i);
	}


	/** same as elementAt() but with appropriate return type. */
	public PhoneBookEntry getPhoneBookEntry(int i) {
		if (i>=pb.size()) return null;
		return (PhoneBookEntry)(pb.elementAt(i));
	}


	/** 
	 * find an entry with the specified address.
	 * @param To SIP-URL to find name for.
	 * @return matching PhoneBookEntry or null if the SIP-URL does not exist in the phonebook.
	*/
	public PhoneBookEntry find(String To) {
		int i,n;
		n=pb.size();
		for (i=0; i<n; i++) {
			PhoneBookEntry p=getPhoneBookEntry(i);
			if (To.equals(p.getSIPURL())) {
				return p;
			}
		}
		return null;
	}


	/** @return number of elements in the phonebook. */
	public int size() { return pb.size(); }


	/** Default constructor.
	 * @param filename filename to build phonebook from.
	*/
	public PhoneBook (String filename) {
		pb=new Vector(10);
		Configuration pbc=new Configuration(filename);
		ConfigIterator pbi=pbc.getIterator();
		NamedValue nv;
		int i;
		for (nv=pbi.next(); nv!=null; nv=pbi.next()) {
			if (! nv.name.equalsIgnoreCase("Entry")) continue;
			Vector tok=Tokenizer.split(nv.value," ");
			String sipurl=(String)(tok.elementAt(0));
			String beg=sipurl.substring(0,4);
			if (! beg.equalsIgnoreCase("sip:")) {
				System.out.println("Invalid PhoneBook entry: "+nv.name+": "+nv.value);
				continue;
			}
			String name="";
			for (i=1; i<tok.size(); i++) {
				name=name+(String)(tok.elementAt(i));
				if (i<tok.size()-1) name=name+" ";
			}
			if ("".equals(name)) name=sipurl.substring(4);
			PhoneBookEntry pbe=new PhoneBookEntry(name,sipurl);
			pb.addElement(pbe);
		}
	}

	private Vector pb;
}
