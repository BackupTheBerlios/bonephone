package bonephone;

import java.util.*;

/** class to implement state machine functionality. */
public class StateMachine implements Observer {
	private IPT_State state;
	private Vector stable; 			// Vector of Vectors of StateTableEntry
	private int states,events;
	private int last_state, last_event;

	private ActionProvider actpro;

	/** Standard constructor. 
	* @param s the state object to use for this instance.
	* @param s the event object to use for this instance.
	* @param act the ActionProvider that is queried for actions upon state transition.
	*/
	public StateMachine (IPT_State s, IPT_Event e, ActionProvider act) {
		actpro=act;
		state=s;		         // remember state object
		e.addObserver(this);     // get called when event is posted
		int i,j;
		states=s.states();     
		events=e.events();
		stable=new Vector(events,1);
		for (i=0; i<events; i++) {
			Vector evv=new Vector(states,1);
			stable.addElement(evv);
			for (j=0; j<states; j++) {
				StateTableEntry sd=new StateTableEntry();
				evv.addElement(sd);
				sd.actions=new Vector (1,1);
				sd.next_state=-1;
			}
		}
		last_event=-1;
		last_state=-1;
	}


	/** @return the internal state table. Its structure is not documented here because thou shalt not modify it outside
	* this class.
	*/
	public Vector getStateTable() { return stable; }


	/** Set the internal state table.
	* @param stab state table obtained from another StateMachine instance.
	* the dimension of the table is not checked for compatibility. So if you insert a table that has more/less
	* states/events, this instance will screw up. */
	public void   setStateTable(Vector stab) { 
		// We should check the dimensions of the table for
		// compatibility here ...
		stable=stab; 
	}
		

	/** add a new state transition. If the same source-state/event pair already exists, it is overwritten.
	* @param state source state.
	* @param event event-number that triggers this transition. Event numbers can be obtained from the used IPT_Event class.
	* @param nextstate destination state.
	*/
	public void addPath (int state, int event, int nextstate) {
		if (state>=states || event>=events) { return; }
		StateTableEntry sd=(StateTableEntry)(((Vector)(stable.elementAt(event))).elementAt(state));
		sd.next_state=nextstate;
		while (sd.actions.size()>0) sd.actions.removeElementAt(0);
		last_event=event; 
		last_state=state;
	}


	/** add global state transitions. All state numbers defined in the used IPT_State object get this transition.
	* @param event event-number that triggers this transition. Event numbers can be obtained from the used IPT_Event class.
	* @param dst destination state.
	*/
	public void addGlobalPath(int event, int dst) {
		if (dst>=states || event>=events) { return; }
		int src;
		for (src=0; src<states; src++) {
			StateTableEntry sd=(StateTableEntry)(((Vector)(stable.elementAt(event))).elementAt(src));
			sd.next_state=dst;
			sd.actions.removeAllElements();
		}
	}


	/** add an action to an existing state transition.
	* @param st source state of the meant transition.
	* @param ev event number of the meant transition.
	* @param action parameter to pass to the ActionProvider when this transition is triggered.
	*/
	public void addAction(int st, int ev, Object action) {
		if (st>=states || ev>=events) { return; }
		StateTableEntry sd=(StateTableEntry)(((Vector)(stable.elementAt(ev))).elementAt(st));
		sd.actions.addElement(action);
	}


	/** add an action to the latest state transition defined with addPath().
	* @param action parameter to pass to the ActionProvider when this transition is triggered.
	*/
	public void addAction(Object action) {
		StateTableEntry sd=(StateTableEntry)(((Vector)(stable.elementAt(last_event))).elementAt(last_state));
		sd.actions.addElement(action);
	}


	/** Observer interface. called by the used IPT_Event object to indicate the arrival of an event. 
	* @param o unused.
	* @param arg unused.
	*/
	public void update (Observable o, Object arg) {
		int e;
		e=((IPT_Event)arg).get();
		trigger(e);
	}

	/** Executes actions, then sets new state */
	private int trigger (int event) {
		// System.out.println("trigger() in StateMachine - 1");
		int i,s;
		int a;
		boolean success=true;
		if (event>=events) { return 0; }
		// System.out.println("trigger() in StateMachine - 2");
		s=state.get(); // remember the state
		StateTableEntry sd=(StateTableEntry)(((Vector)(stable.elementAt(event))).elementAt(s));
		// System.out.println("    :"+sd);
		for (i=0; i<sd.actions.size(); i++) {
			success=actpro.action(sd.actions.elementAt(i));
			if (!success) break;
		}
		// If state is still the same, set new one
		// If not, an action has altered the state either directly or by
		// an event, causing trigger() to run again
		if (s==state.get() && sd.next_state>=0) state.set(sd.next_state);
		return 1;
	}


}



class StateTableEntry {
	public Vector actions;	// vector of actions to take
	public int next_state;  // non-negative state or -1 for no change
	public String toString() {
		int i;
		String r="";
		if (actions!=null) 
			for (i=0; i<actions.size(); i++)
				r=r+actions.elementAt(i);
		else
			r="  ";
		r=r+" -> "+next_state;
		return r;
	}	
}

