package bonephone;

import java.util.*;

/** base class for all Event objects in the bonephone. The idea is that
 * Events are queued at first. Then we notify all observers about this event.
 * If in order one observer tries to send another event over the same Event object, this event
 * is queued as usual, but the observer notification is deferred until the previous observer
 * notification is completed.
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
public abstract class IPT_Event extends Observable {
  
	private int event = 0 ;
	private Queue evq;
	private Boolean locked;

	/** number of events available for this class. subclasses must initialize this variable to the
	 * correct value. 
	*/
	protected  int nevents;

	private void init(int ev) { event=ev; nevents=ev+1; evq=new Queue(); locked=Boolean.FALSE; }
	IPT_Event() { super(); init(0); }
	IPT_Event(int ev) { super(); init(ev); }

	/** return number of events available for this class. */
	public int events() { return nevents; }

	/** set (send) an event to the listening observers using the described queue mechanism.
	 * @param set_event event number to send.
	 * @return 0 on failure, 1 on success.
	*/
	public int set (int set_event) {
		if ( set_event < 0 || set_event >= nevents ) return 0;
		evq.insert(new Integer(set_event));
		System.out.println("locked="+locked.booleanValue());
		synchronized (locked) {
			if (locked.booleanValue()) return 1;
			locked=Boolean.TRUE;
		}
		while (evq.size()>0) {
			event=((Integer)(evq.fetch())).intValue();
			setChanged();
			notifyObservers(this);
		}
		locked=Boolean.FALSE;
		return 1;
	}  

	/** get current event value. */
	public int get () {
		return event;
	}

}
