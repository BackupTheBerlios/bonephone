package bonephone;

import java.util.*;

/** State class for the GUI. */
public class IPT_State_GUI extends IPT_State {

  final public static int ST_PBOOK  = 0;
  final public static int ST_IDLE   = 1;
  final public static int ST_CC     = 2;
  
  public IPT_State_GUI () { super(); nstates=3; }
  public IPT_State_GUI (int set_state) { super(set_state); nstates=5; }

}
