package bonephone;

/** interface to shift around read-only vectors (or lookalikes) in our application. */
interface VectorRead {
	/** @return number of elements in the Vector. */
	int size();

	/** obtain element at specified position.
	* @param i index.
	* @return Object at index i.
	*/
	Object elementAt(int i);
}
