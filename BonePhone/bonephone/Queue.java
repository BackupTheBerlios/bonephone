package bonephone;

import java.util.*;

/** Class to implement a queue (FIFO). Needed to be re-written due to synchonization */
public class Queue {

	private Vector data;

	/** insert an item to the queue. internally synchronized. */
	public void insert(Object o) { 
		synchronized (data) {
			data.addElement(o);
		}
	}

	/** retrieve an item from the queue. internally synchronized. */
	public Object fetch() { 
		Object o;
		synchronized (data) {
			if (data.size()==0) return null;
			o=data.elementAt(0);
			data.removeElementAt(0);
		}
		return o;
	}

	/** internally synchronized.
	 * @return number of elements in the queue.
	*/
	public int size() {
		synchronized (data) {
			return data.size(); 
		}
	}

	public Queue() { data=new Vector(5); }

}
