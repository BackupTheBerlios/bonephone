package bonephone;

/** An instance that want to be notified when the user selects/uses an entry
 * from the PhoneBookPanel must implement this interface and register to the PhoneBookPanel.
*/
interface PhoneBookListener {

	/** user has selected (highlighted) an entry in the phonebokpanel.
	 * @param pbe entry that is highlighted.
	*/
	public void selectEntry (PhoneBookEntry pbe);

	/** user want to use the specified entry for a call.
	 * @param pbe entry that is highlighted.
	*/
	public void useEntry (PhoneBookEntry pbe);
}

