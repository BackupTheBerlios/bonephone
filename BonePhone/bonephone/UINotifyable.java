package bonephone;

/** interface to be implemented by classes who want to be notified by the PhoneController
 * about any changes in Call states, Call appearance/removal, codec change.
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
interface UINotifyable {

	public void codecChanged (CodecDescriptor cd); // Re-Display current codec
	public void newCall (Call c);                  // A new call appeared
	public void deletedCall (Call c);              // That call was deleted

}
