

package bonephone;

import java.util.*;
import java.net.*;
import gov.nist.sip.msgparser.*;
import SipUserAgent.*;


// ----------------- Call ------------------------------

/**
 * Class to hold application-side information of a call.<br>
 *
 * The Call class holds information as the "To" and "From" for a call
 * and its current state. A call uses a programmable state machine to
 * realize the functionality of a telephone
 *
 * @author Jens Fiedler (jfi@fokus.gmd.de)
*/
public class Call extends Observable implements Observer, ActionProvider {

	// some constants

	public static final boolean INCOMING=true;
	public static final boolean OUTGOING=false;

	private static final int MED_LSDP       = 0;
	private static final int MED_AVL        = 1;
	private static final int MED_START      = 2;
	private static final int MED_STOP       = 3;
	private static final int SIP_HANGUP     = 4;
	private static final int SIP_DECLINE    = 5;
	private static final int SIP_INVITE     = 6;
	private static final int SIP_CALLBACK   = 7;
	private static final int SIP_ACCEPT     = 8;
	private static final int SIP_MUTE       = 9;

	// MED_LSDP     get the local RTP/RTCP ports
	// MED_AVL      check if the media engine is free (i.e. is not running right now)
	// MED_START    start media engine with parameters from CallContext
	// MED_STOP     Terminate audio connection and engine and free allocated ports.
	// SIP_HANGUP   Hangup
	// SIP_DECLINE  Reject last request
	// SIP_INVITE   Initiate outgoing call
	// SIP_CALLBACK Use the "from" field as "to" and initiate outgoing call
	// SIP_ACCEPT   signalize that an incoming call is being accepted, or that the response for a
	//              former outgoing call is beeing accepted.
	// SIP_MUTE     signalize that the partner is being muted.


	/**
	 * Callback function for the state machine.<br>
	 *
	 * This implements the ActionProvider interface for use with a state machine.
	 * It is called whenever the state machine wishes to execute an action
	 * defined in a event/action(s) description.
	 *
	 * @param cmd generic action to take. Must be of class Integer.
	 * @return true on succesfull operation, otherwise false.
	*/
	public boolean action (Object cmd) {
		System.out.println("action() in Call - 1");
		if (!(cmd instanceof Integer)) return false;
		System.out.println("action() in Call - 2");
		switch (((Integer)cmd).intValue()) {
		case MED_LSDP: {
			try { getLSDP(); }
			catch (Exception ex) { 
				setError(ex.getMessage()); 
				System.out.println("Error in MED_LSDP");
				sendEvent(IPT_Event_Call.EV_GEN_ERROR);
				return false;
			}
			return true;
		}
		case MED_START: {
			try { mycac.getMediaControl().start(remotemedia,localmedia); }
			catch (Exception ex) { 
				setError(ex.getMessage());
				System.out.println("Error in MED_START");
				sendEvent(IPT_Event_Call.EV_GEN_ERROR);
				return false;
			}
			return true; 
		}
		case MED_AVL: {
			if (mycac.getMediaControl().isAvailable(this)) {
				sendEvent(IPT_Event_Call.EV_MED_AVL);
			} else {
				sendEvent(IPT_Event_Call.EV_MED_UNAVL);
			}
			return true; 
		}
		case MED_STOP: {
			mycac.getMediaControl().stop(localmedia);
			setError("TERMINATING");
			freeLSDP();
			return true; 
		}
		case SIP_HANGUP: {
			System.out.println("doing sip_hangup");
			try { mycac.getCallManager().hangup(cc); } catch (Exception ex) {}
			return true; 
		}
		case SIP_ACCEPT: {
			System.out.println("doing sip_accept");
			Configuration CFG=mycac.getConfiguration();
			SDPManager sdpm=new SDPManager(CFG.getAsString("SIPIdentity"));
			SDPAnnounce sdpa;
			sdpa=sdpm.build(getLocalMedia());
			cc.setLocalSDP(sdpa);
			try { mycac.getCallManager().accept(cc); } catch (Exception ex) {} 
			return true; 
		}
		case SIP_DECLINE: {
			System.out.println("doing sip_decline");
			try { mycac.getCallManager().decline(cc); } catch (Exception ex) {} 
			return true; 
		}
		case SIP_CALLBACK: {
			System.out.println("doing sip_callback");
			CallContext ncc;
			try {
				ncc=mycac.getCallManager().createCallContext(mycac.getConfiguration().getAsString("SIPIdentity"),
				                                             cc.getFromAddress());
			} catch (Exception e) {
				setError(""+e.getMessage());
				System.out.println("Error in SIP_CALLBACK");
				sendEvent(IPT_Event_Call.EV_GEN_ERROR);
				return false;			
			}
			ncc.setSubject(cc.getSubject());
			setCallContext(ncc);
			return true; 
		}
		case SIP_INVITE: {
			System.out.println("doing sip_invite");
			Configuration CFG=mycac.getConfiguration();
			SDPManager sdpm=new SDPManager(CFG.getAsString("SIPIdentity"));
			SDPAnnounce sdpa;

			cc.setSubject(getSubject());
			System.err.println(""+getLocalMedia());
			sdpa=sdpm.build(getLocalMedia());
			cc.setLocalSDP(sdpa);
			mycac.getCallManager().invite(cc);
			System.out.println(sdpa.encode());
			return true; 
		}
		case SIP_MUTE: {
			System.out.println("doing sip_mute");
			Configuration CFG=mycac.getConfiguration();
			SDPManager sdpm=new SDPManager(CFG.getAsString("SIPIdentity"));
			InetAddress local=main.getLocalAddress();
			InetAddress zero=null;
			if (local instanceof Inet4Address) { 
				try { zero=Inet4Address.getByAddress( CallCenter.null4 ); }
				catch (UnknownHostException uhe) { 
					System.err.println("Can't create empty address (0.0.0.0) for muting.");
					return false;
				}
			}
			if (local instanceof Inet6Address) { 
				try { zero=Inet6Address.getByAddress( CallCenter.null6 ); }
				catch (UnknownHostException uhe) { 
					System.err.println("Can't create empty address (::0) for muting.");
					return false;
				}
			}
			MediaDescriptor md_new=new MediaDescriptor(MediaDescriptor.AUDIO, 
			                                           mycac.getCodecProfileManager().getDownstreamProfile(), 
			                                           0, 1, zero, 0, null);

			cc.setLocalSDP(sdpm.build(md_new));
			mycac.getCallManager().invite(cc);

			return true; 
		}
		}
		return false;
	}


	private void freeLSDP() {
		mycac.getPortManager().freePort(getLocalMedia().port);		
		setLocalMedia(null);
	}

	
	// Obtain a pair of local UDP ports to signalize as our
	// RTP/RTCP ports for this call.
	private void getLSDP() throws Exception {
		// use the stored RTP information from THIS class
		int lport=mycac.getPortManager().getPort();
		MediaDescriptor med=new MediaDescriptor(MediaDescriptor.AUDIO,
		                                        mycac.getCodecProfileManager().getDownstreamProfile(),
		                                        lport,1,
		                                        main.getLocalAddress(),
		                                        mycac.getConfiguration().getAsInt("RTPTTL"),
		                                        null);
		setLocalMedia(med);
	}

	// StateMachine for this Call
	private        StateMachine stm;

	// reference StateMachine used to copy the StateTable for
	// subsequent instances.
	private static StateMachine ref=null;

	// Application side error string.
	private String _error="";
	
	private IPT_Event_Call evObj;
	private IPT_State_Call stObj;

	// MediaDescriptor instances for local and remote side.
	// For conferencing the remote side should be a Vector.
	private MediaDescriptor remotemedia;
	private MediaDescriptor localmedia;

	// encapsulated CallContext 
	private CallContext cc=null;

	// The CallCenter instance, this Call belongs to.
	private CallCenter mycac;

	private boolean incomingcall;

	/**
	 * Mark this call as incoming or outgoing
	 * 
	 * @param incoming true -> call is an incoming one. false -> call is outgoing
	*/
	public void setIncoming(boolean incoming) { incomingcall=incoming; }

	/**
	 * Get the subject string.
	 * 
	 * @return Subject string of the call.
	*/
	public String getSubject() { return (cc!=null)?cc.getSubject():"no subject"; }

	/**
	 * Get the error string.
	 *
	 * When there is an underlying SIP error, it is appended to the application side error string.
	 * 
	 * @return Error string of the call.
	*/
	public String getError()   { return _error+" SIP:"+((cc!=null)?cc.getReasonPhrase():"no information"); }

	/**
	 * Get the peer address of this call.
	 * 
	 * @return Peer address string of the call.
	*/
	public String getPeer()    { return (cc==null)?"no CallContext":(incomingcall?cc.getFromAddress():cc.getToAddress()); }

	/**
	 * Set the subject string.
	 * 
	 * @param Subject The subject string for the call.
	*/
	public void   setSubject(String Subject) { if (cc != null) cc.setSubject(Subject); }

	/**
	 * Set the error string.
	 *
	 * Sets the application side error string.
	 * 
	 * @param Error error string to set.
	*/
	public void   setError  (String Error  ) { _error=Error; }

	/** 
	 * Get the CallContext Object from this call.
	 *
	 * This function is only needed by the CallCenter class to lookup calls by their
	 * CallContext in its database.
	 *
	 * @return The Callcontext Object for this call.
	*/
	public  CallContext getCallContext()                { return cc; }
	private void        setCallContext(CallContext ncc) { cc=ncc; }	

	/**
	 * Get the MediaDescriptor for the remote side.
	 *
	 * @return The remote MediaDescriptor
	*/
	public MediaDescriptor getRemoteMedia ()                   { return remotemedia; }

	/**
	 * Set the MediaDescriptor for the remote side.
	 *
	 * @param md The remote MediaDescriptor to use.
	*/
	public void            setRemoteMedia (MediaDescriptor md) { remotemedia=md; }

	/**
	 * Get the MediaDescriptor for the local side.
	 *
	 * @return The local MediaDescriptor
	*/
	public MediaDescriptor getLocalMedia ()                   { return localmedia; }

	/**
	 * Set the MediaDescriptor for the local side.
	 *
	 * @param md The local MediaDescriptor to use.
	*/
	public void            setLocalMedia (MediaDescriptor md) { localmedia=md; }

	/**
	 * Get the current state of the Call.
	 *
	 * @return the state as defined in the IPT_State_Call class.
	*/
	public int getState() { return stObj.get();	}
	
	/**
	 * Set the state for this Call.
	 *
	 * @param s The new state as defined in the IPT_State_Call class.
	*/
	public void setState(int s) { stObj.set(s); }

	/**
	 * <b><i>Observer interface, no need to use !!!</i></b><br>
	 *
	 * Calls observe their state object. This function is called when the state
     * object changes. 
	*/
	public void update (Observable o, Object arg) 	{ 
		setChanged(); notifyObservers();
	}	

	/**
	 * Hangup this call.
	 * 
	 * A EV_GUI_HUP is send to the state machine of this call.
	*/
	public void hangup()   { sendEvent(IPT_Event_Call.EV_GUI_HUP); }

	/**
	 * Cancel this call.
	 * 
	 * A EV_GUI_CANCEL is send to the state machine of this call.
	 * This works only if the call is not established, means is being build up.
	*/
	public void cancel()   { sendEvent(IPT_Event_Call.EV_GUI_CANCEL); }

	/**
	 * Mute this call.
	 * 
	 * A EV_GUI_MUTE is send to the state machine of this call.
	 * This is a SIP signaled peer-to-peer muting, not only a muting of the microphone.
	*/
	public void mute()     { sendEvent(IPT_Event_Call.EV_GUI_MUTE); }

	/**
	 * Unmute this call.
	 * 
	 * A EV_GUI_UNMUTE is send to the state machine of this call.
	*/
	public void unmute()   { sendEvent(IPT_Event_Call.EV_GUI_UNMUTE); }

	/**
	 * Accept an incoming call which is ringing right now.
	 * 
	 * A EV_GUI_ACCEPT is send to the state machine of this call.
	*/
	public void accept()   { sendEvent(IPT_Event_Call.EV_GUI_ACCEPT); }

	/**
	 * Call back a missed call.
	 * 
	 * A EV_GUI_CALLBACK is send to the state machine of this call.
	*/
	public void callback() { sendEvent(IPT_Event_Call.EV_GUI_CALLBACK); }

	
	private void init () { 
	 	evObj=new IPT_Event_Call();
		stObj.addObserver(this);

		if (ref==null) { // create and configure reference statemachine
			
			int cst;  // current state to make statetable configuration more readable
			// create the action commands first
			Integer act_sip_callback = new Integer(SIP_CALLBACK);
			Integer act_sip_invite   = new Integer(SIP_INVITE);
			Integer act_sip_decline  = new Integer(SIP_DECLINE);
			Integer act_sip_hangup   = new Integer(SIP_HANGUP);
			Integer act_sip_accept   = new Integer(SIP_ACCEPT);
			Integer act_sip_mute     = new Integer(SIP_MUTE);
			Integer act_med_lsdp     = new Integer(MED_LSDP);
			Integer act_med_avl      = new Integer(MED_AVL);
			Integer act_med_start    = new Integer(MED_START);
			Integer act_med_stop     = new Integer(MED_STOP);

			ref=new StateMachine(stObj, evObj, this);
	
			// configure it

			ref.addGlobalPath(IPT_Event_Call.EV_GEN_ERROR, IPT_State_Call.ST_ERROR);

			// Source=INACTIVE
			cst=IPT_State_Call.ST_INACTIVE;
			ref.addPath(cst, IPT_Event_Call.EV_SIP_N0, -1);
			ref.addAction( act_med_lsdp );
			ref.addAction( act_sip_accept );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_NY, IPT_State_Call.ST_RINGING);
			ref.addAction( act_med_lsdp );
			ref.addPath(cst, IPT_Event_Call.EV_GUI_CANCEL,  IPT_State_Call.ST_DELETE);
			ref.addAction( act_sip_hangup );
			ref.addPath(cst, IPT_Event_Call.EV_GUI_HUP,  IPT_State_Call.ST_DELETE);
			ref.addAction( act_sip_hangup );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_NN, IPT_State_Call.ST_ERROR);
			ref.addAction( act_sip_decline );
			ref.addPath(cst, IPT_Event_Call.EV_GUI_UNMUTE, IPT_State_Call.ST_CALLING);
			ref.addAction( act_med_lsdp );
			ref.addAction( act_sip_invite );
			
			// Source=CALLING
			cst=IPT_State_Call.ST_CALLING;
			ref.addPath(cst, IPT_Event_Call.EV_MED_UNAVL,  IPT_State_Call.ST_INACTIVE);
			ref.addAction( act_sip_mute );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_ERROR,  IPT_State_Call.ST_ERROR);
			ref.addAction( act_sip_hangup );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_NN,  IPT_State_Call.ST_ERROR);
			ref.addAction( act_sip_hangup );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_HANGUP,  IPT_State_Call.ST_ERROR);
			ref.addPath(cst, IPT_Event_Call.EV_GUI_CANCEL,  IPT_State_Call.ST_DELETE);
			ref.addAction( act_sip_hangup );
			ref.addPath(cst, IPT_Event_Call.EV_MED_AVL,  IPT_State_Call.ST_ACTIVE);
			ref.addAction( act_med_start );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_NY,   -1);
			ref.addAction( act_med_avl );

			// Source=RINGING
			cst=IPT_State_Call.ST_RINGING;
			ref.addPath(cst, IPT_Event_Call.EV_MED_UNAVL,  IPT_State_Call.ST_INACTIVE);
			ref.addAction( act_sip_mute );
			ref.addPath(cst, IPT_Event_Call.EV_GUI_ACCEPT, -1);
			ref.addAction( act_sip_accept );
			ref.addAction( act_med_avl );
			ref.addPath(cst, IPT_Event_Call.EV_MED_AVL, IPT_State_Call.ST_ACTIVE);
			ref.addAction( act_med_start );
			ref.addPath(cst, IPT_Event_Call.EV_GUI_CANCEL, IPT_State_Call.ST_DELETE);
			ref.addAction( act_sip_decline );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_ERROR, IPT_State_Call.ST_ERROR);
			ref.addAction( act_sip_hangup );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_HANGUP, IPT_State_Call.ST_MISSED);
			
			// Source=ACTIVE
			cst=IPT_State_Call.ST_ACTIVE;
			ref.addPath(cst, IPT_Event_Call.EV_GUI_MUTE, IPT_State_Call.ST_MUTING);
			ref.addAction( act_sip_mute );
			ref.addAction( act_med_stop );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_N0000, IPT_State_Call.ST_INACTIVE);
			ref.addAction( act_sip_accept );
			ref.addAction( act_med_stop );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_ERROR, IPT_State_Call.ST_ERROR);
			ref.addAction( act_med_stop );
			ref.addAction( act_sip_hangup );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_HANGUP, IPT_State_Call.ST_DELETE);
			ref.addAction( act_med_stop );
			ref.addPath(cst, IPT_Event_Call.EV_GUI_HUP, IPT_State_Call.ST_DELETE);
			ref.addAction( act_med_stop );
			ref.addAction( act_sip_hangup );

			// Source=MUTING
			cst=IPT_State_Call.ST_MUTING;
			ref.addPath(cst, IPT_Event_Call.EV_SIP_N0, IPT_State_Call.ST_INACTIVE);
			ref.addAction( act_sip_accept );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_N0000, IPT_State_Call.ST_INACTIVE);
			ref.addAction( act_sip_accept );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_NY, IPT_State_Call.ST_INACTIVE);
			ref.addAction( act_sip_accept );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_NN, IPT_State_Call.ST_INACTIVE);
			ref.addAction( act_sip_accept );
			ref.addPath(cst, IPT_Event_Call.EV_SIP_ERROR, IPT_State_Call.ST_ERROR);
			ref.addAction( act_sip_hangup );
			ref.addPath(cst, IPT_Event_Call.EV_GUI_HUP, IPT_State_Call.ST_DELETE);
			ref.addAction( act_sip_hangup );

			// Source=MISSED
			cst=IPT_State_Call.ST_MISSED;
			ref.addPath(cst, IPT_Event_Call.EV_GUI_CALLBACK, IPT_State_Call.ST_CALLING);
			ref.addAction( act_sip_callback );
			ref.addAction( act_med_lsdp );
			ref.addAction( act_sip_invite );
			ref.addPath(cst, IPT_Event_Call.EV_GUI_CANCEL, IPT_State_Call.ST_DELETE);
			ref.addPath(cst, IPT_Event_Call.EV_SIP_ERROR, IPT_State_Call.ST_ERROR);

			// Source=ERROR
			ref.addPath(IPT_State_Call.ST_ERROR, IPT_Event_Call.EV_GUI_CANCEL, IPT_State_Call.ST_DELETE);

			// Source=DELETE
			// NOTHING escapes the DELETE state

			// done

			stm=ref; // We are first, we will use the ref machine
		} else {     // get statetable from reference statemachine and load it into own one
			stm=new StateMachine(stObj, evObj, this);
			stm.setStateTable(ref.getStateTable());			
		}
	}
	

	/** 
	 * Send an event to this Call.
	 *
	 * The event is processed via the underlying state machine.
	 * 
	 * @param ev Event as defined in IPT_Event_Call
	*/
	public void sendEvent(int ev) {
		System.out.println("sendEvent("+IPT_Event_Call.eventname[ev]+")");
		evObj.set(ev);
	}


	/**
	 * Constructor to create an outgoing Call.
	 *
	 * The CallContext object is obtained by the underlying SIP-UA.
	 *
	 * @param To Address to call. Must be of the form [sip:]user@(dialdomain|host)
	 * @param mycac reference to the CallCenter which creates and handles the call.
	*/
	public Call (String To, CallCenter mycac ) 
	{ 
		this.mycac=mycac;
		stObj=new IPT_State_Call();
		try {
			this.cc=mycac.getCallManager().createCallContext(mycac.getConfiguration().getAsString("SIPIdentity"),To);
		} catch (Exception e) {
			stObj.set(IPT_State_Call.ST_ERROR);
			setError("Can't Allocate CallContext: "+e.getMessage());
			e.printStackTrace();
		}
		init(); 
	}

	/** 
	 * Constructor to create a Call object for an incoming call.
	 *
	 * The CallContext for this call must exist prior to its creation.
	 * This happens when an incoming call is signalizied by the SIP-UA.
	 *
	 * @param cc The CallContext from the SIP-UA.
	 * @param mycac reference to the CallCenter which creates and handles the call.
	*/
	public Call (CallContext cc, CallCenter mycac)
	{ 
		stObj=new IPT_State_Call();
		this.cc=cc;
		this.mycac=mycac;
		init(); 
	}
}

