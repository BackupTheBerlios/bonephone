package bonephone;

import java.util.*;

/** A state is also observable by any other interested party who wants to be informed
 * on a state change.
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
public abstract class IPT_State extends Observable {
  
  private int state ;

  /** Number of states this object can have. subclasses have to set this value. */
  protected int nstates;

  /** @return number of states this class can have. */
  int states() { return nstates; }

  IPT_State () { super(); state=0;  nstates=0; }
  IPT_State (int set_state) { super(); state=set_state; nstates=state+1; }

  /** set the state of this object.
   * @param set_state state to set to.
  */
  public synchronized int set (int set_state) {
    if ( set_state < 0 || set_state >=nstates ) return 0;
    if ( state == set_state ) return 1;
    state=set_state;
    setChanged();
    notifyObservers();
    return 1;
  }  

  /** @return current state */
  public int get () {
    return state;
  }
}
