package bonephone;

public class IPT_State_Call extends IPT_State {

  /** The call is inactive/muted. */
  final public static int ST_INACTIVE = 0;

  /** The call is being established (outgoing), ringing the remote side */
  final public static int ST_CALLING  = 1;

  /** The call is no more used and may be deleted from all lists. */
  final public static int ST_DELETE   = 2;

  /** The call is going to be muted, confirmation is avaited from remote. */
  final public static int ST_MUTING   = 3;

  /** The call is active, means that people are speaking. */
  final public static int ST_ACTIVE   = 4;

  /** An incoming call waits for users acceptance. */
  final public static int ST_RINGING  = 5;

  /** remote side has cancelled its request and the call stays to be called back. */
  final public static int ST_MISSED   = 6;

  /** some kind of error occured. */
  final public static int ST_ERROR    = 7;
  
  public IPT_State_Call () { super(); nstates=8; }
  public IPT_State_Call (int set_state) { super(set_state); nstates=8; }
}

