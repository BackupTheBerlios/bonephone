package bonephone;

/** Events that can be send to a Call to initiate actions. */
public class IPT_Event_Call extends IPT_Event {
  
  /** User wants to cancel the call. */
  final public static int EV_GUI_CANCEL   =  0;

  /** User wants to accept the incoming call. */
  final public static int EV_GUI_ACCEPT   =  1;

  /** User wants to unmute the call. */
  final public static int EV_GUI_UNMUTE   =  2;

  /** User wants to mute the call. */
  final public static int EV_GUI_MUTE     =  3;

  /** User wants to hangup the call. */
  final public static int EV_GUI_HUP      =  4;

  /** User wants to callback the call (ie. call the originator of a missed call.). */
  final public static int EV_GUI_CALLBACK =  5;

  /** A SIP notfication without SDP block was received. */
  final public static int EV_SIP_N0       =  6;  // no RSDP

  /** A SIP notfication with an SDP block carrying an empty RTP address was received.
   * Empty addresses are 0.0.0.0 for IPv4 or ::0 for IPv6
  */
  final public static int EV_SIP_N0000    =  7;  // remote address=0.0.0.0

  /** A SIP notfication with an SDP block carrying no supported codec was received.*/
  final public static int EV_SIP_NN       =  8;  // no supported codec in RSDP

  /** A SIP notfication with an SDP block carrying at least one supported codec was received.*/
  final public static int EV_SIP_NY       =  9;  // found supported codec(s) in RSDP

  /** A SIP request to hangup the call was received. */
  final public static int EV_SIP_HANGUP   = 10;

  /** A SIP request indicating an error was received. */
  final public static int EV_SIP_ERROR    = 11;

  /** Media engine is available and reserved. */
  final public static int EV_MED_AVL      = 12;

  /** Media engine is currently unavailable (used by another call) */
  final public static int EV_MED_UNAVL    = 13;
  final public static int EV_GEN_ERROR    = 14;

  final public static String[] eventname = {
                                             "GUI_CANCEL", "GUI_ACCEPT", "GUI_UNMUTE", "GUI_MUTE", "GUI_HUP",
                                             "GUI_CALLBACK", "SIP_NNOSDP", "SIP_NNULL", "SIP_NUNSUPP", "SIP_NSUPP", 
                                             "SIP_HANGUP", "SIP_ERROR", "MED_AVL", "MED_UNAVL", "GEN_ERROR" };
  public IPT_Event_Call () { nevents=15; }
}

