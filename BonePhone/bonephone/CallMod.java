package bonephone;

/**
 * Data class to hold information about the way a call is modfied.
 *
 * @author Jens Fiedler (jfi@fokus.gmd.de)
*/
class CallMod {
	
	/**
	 * Standard constructor.
	 *
	 * @param c affected Call
	 * @param mod identifier to define the type of modification. Usually -1 for call deletion, 
	 * 1 for call creation, 0 for content change.
	*/
	public CallMod (Call c, int mod) {
		this.call=c; 
		this.mod=mod;
	}

	public int mod;
	public Call call;
}
