package bonephone;


/** Data class to hold pairs of name/SIP-URL. */
class PhoneBookEntry {
	private String name;
	private String SIPURL;

	/** Contructor.
	 * @param n Name for this entry (e.g. "Erwin Mueller").
	 * @param u SIP-URL to call (e.g. "sip:e.mueller@torfnase.de")
	*/
	PhoneBookEntry (String n, String u) { 
		name=n; SIPURL=u;
	}

	/** @return name member of this entry. */
	public String getName()   { return name;   }

	/** @return SIP-URL member of this entry. */
	public String getSIPURL() { return SIPURL; }
}

