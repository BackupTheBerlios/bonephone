package bonephone;

import java.util.*;
import java.net.*;
import gov.nist.sip.msgparser.*;
import SipUserAgent.*;


/**
 * The CallCenter is the central place where Calls reside.
 *
 * A CallCenter implements several interfaces. It gets notified by the SIP-UA through the 
 * CallListener interface. To reduce response times, the CallCenter creates a worker thread
 * which handles the request, so that the interface function can return immediately.
 * A CallCenter uses the Observer/Observable mechanism to observe its Calls. Change in a call
 * is signalized to its own observers. At last it implements the VectorRead interface.
 * This enables external classes to access (read-only) the internal Vector of Call objects.
 *
 * @author Jens Fiedler (jfi@fokus.gmd.de)
*/
public class CallCenter extends Observable implements Observer, VectorRead, CallListener {

	private Vector data;

	private Configuration CFG;
	private CallManager SIP;
	private CodecProfileManager CPM;
	private PortManager PM;
	private MediaControl MED;

	/**
	 * Four byte array filled with 0. Used to create empty IPv4 address.
	*/
	public final static byte null4[]  = { 0, 0, 0, 0 };

	/**
	 * Sixteen byte array filled with 0. Used to create empty IPv6 address.
	*/
	public final static byte null6[]  = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	// Corresponding InetAddress objects for checking mute requests.
	private static Inet4Address zero4;
	private static Inet6Address zero6;

	/**
	 * get the Configuration object which is used by the CallCenter.
	 *
	 * @return the Configuration object.
	*/
	public  Configuration       getConfiguration()       { return CFG; }

	/**
	 * get the CallManager object (SIP-UA) which is used by the CallCenter.
	 *
	 * This is created by the CallCenter at construction time.
	 *
	 * @return the CallManager object.
	*/
	public  CallManager         getCallManager()         { return SIP; }

	/**
	 * get the PortManager object which is used by the CallCenter.
	 *
	 * @return the PortManager object.
	*/
	public  PortManager         getPortManager()         { return PM;  }

	/**
	 * get the CodecProfileManager object which is used by the CallCenter.
	 *
	 * @return the CodecProfileM;anager object.
	*/
	public  CodecProfileManager getCodecProfileManager() { return CPM; }

	/**
	 * get the MediaControl object which is used by the CallCenter.
	 *
	 * @return the MediaControl object.
	*/
	public  MediaControl        getMediaControl()        { return MED; }


	/**
	 * Standard constructor.
	 *
	 * @param CFG Configuration object to use.
	 * @param CPM CodecProfileManager to use.
	 * @param PM  PortManager to use for obtaining IP-ports.
	 * @param MED MediaControl object to use.
	*/
	CallCenter (Configuration CFG, CodecProfileManager CPM, PortManager PM, MediaControl MED) { 
		super(); 
		data=new Vector(); 
		this.CFG=CFG; 
		this.CPM=CPM; 
		this.PM=PM; 
		this.MED=MED; 

		try { zero4=(Inet4Address)(Inet4Address.getByAddress(null4)); }
		catch (UnknownHostException uhe) {
			System.err.println("Can't create empty address (0.0.0.0) for checking.");
			System.exit(-1);
		}
		try { zero6=(Inet6Address)(Inet6Address.getByAddress(null6)); }
		catch (UnknownHostException uhe) {
			System.err.println("Can't create empty address (::0) for checking.");
			System.exit(-1);
		}

    	// Register with SIP user agent
		try {
			SIP = new CallManager(CFG.getAsString("SIPDialDomain"), this);
		} catch (Exception e) {
			System.err.println(""+e.getMessage());   
			e.printStackTrace();
			System.exit(-1);
		}
		try {
			SIP.Login(CFG.getAsString("SIPIdentity"), CFG.getAsString("SIPDisplayName"));
		} catch (Exception e) {
			System.err.println("login: "+e.getMessage());   
			e.printStackTrace();
			System.exit(-1);
		}
	}

	//////////////////////////
	// CallListener interface:

	/** 
	 * constants to use with EventWorker
	*/
	private final static int CL_Notify   = 0;
	private final static int CL_Error    = 1;
	private final static int CL_Hangup   = 2;

	/** 
	 * <b>Part of the CallListener interface.</b><br>
	 *
	 * This function is called whenever a CallContext changes in a kind the application has to be notified about it. 
	 * This includes the creation of a CallContext, but not its death.
	 * Other examples: remote user has accepted the call, remote user accepts or initiates peer-to-peer muting.
	 *
	 * @param cc affected CallContext.
	*/
	public void notify   (CallContext cc) { 
		System.out.println("notify()");
		Thread worker=new EventWorker(cc,CL_Notify);   worker.start();
	}

	/** 
	 * <b>Part of the CallListener interface.</b><br>
	 *
	 * This function is called whenever an error occurs which is related to the CallContext.
	 * For example when the address is rejected by the SIP stack or the remote user is not found at the remote site.
	 *
	 * @param cc affected CallContext.
	*/
	public void error    (CallContext cc) {  
		System.out.println("error()");
		Thread worker=new EventWorker(cc,CL_Error);   worker.start();
	}

	/** 
	 * <b>Part of the CallListener interface.</b><br>
	 *
	 * This function is called whenever the corresponding call is to be terminated. Means
	 * the remote user has hung up the phone.
	 *
	 * @param cc affected CallContext.
	*/
	public void hangup   (CallContext cc) {  
		System.out.println("hangup()");
		Thread worker=new EventWorker(cc,CL_Hangup);   worker.start();
	}
	
	
	// VectorRead interface :
	//

	/**
	 * <b>Part of the VectorRead interface !</b>
	 *
	 * @return the number of elements in the internal Vector of Call objects.
	*/
	public int size() { return data.size(); }

	/**
	 * <b>Part of the VectorRead interface !</b>
	 *
	 * Access the internal Vector of Call objects.
	 *
	 * @param i index to read at.
	 * @return Call object at the position i.
	*/
	public Object elementAt(int i) {
		return data.elementAt(i); 
	}
	
	/**
	 * <b>Part of the Observer interface !</b>
	 *
	 * Gets called when a Call object changes.
	 *
	 * @param o affected Call object.
	 * @param arg not used.
	*/
	public void update (Observable o, Object arg) 	{ 
		if (o instanceof Call) {
			Call a=(Call)o;
			if (a.getState()==IPT_State_Call.ST_DELETE) {
				this.remove(a);
			} else {
				touch(new CallMod((Call)o, 0)); 
			}
		}
	}

	/////////////////////
	// new functions :

	/**
	 * Developers function, don't use !
	 *
	 * This creates some feked calls in this CallCenter. Each Call is notified to the observers.
	*/
	public void fakeCall () {
		String tos[]={ "sip:satan@hell.org", "sip:nero@roman-empire.it", "sip:psycho@bates-motel.org",
		               "sip:sexbomb@my-bed.de", "sip:b.gates@microsoft.com", "sip:g.w.bush@whitehouse.gov",
		               "sip:stranger@unity-clan.de", "sip:tux@linux.org", "sip:ron.sommer@iptel.org" };
		String sus[]={ "Your soul", "where are the matches", "my knife order",
		               "free this nite ?", "where is my linux ?", "about that red button",
		               "Clanwar", "feed me", "my iptel.org account" };
		Call a; int x;
		for (x=0; x<8; x++) {
			if (x==IPT_State_Call.ST_DELETE) continue;
			if (x==IPT_State_Call.ST_MUTING) continue;
			a=new Call(tos[x],this);
			a.setSubject(sus[x]);
			a.setState(x);
			this.add(a);
		}
	}

	/**
	 * initiate an outgoing call. 
	 *
	 * @param To Address to call (e.g. sip:nobody@maybe.yes.no)
	 * @param Subject Subject string 
	*/
	public Call issueCall (String To, String Subject) {
		Call a=new Call(To,this);
		a.setIncoming(Call.OUTGOING);
		if (Subject==null) Subject="empty";
		if (Subject.trim().equals("")) Subject="empty";
		a.setSubject(Subject);
		add(a);
		a.sendEvent(IPT_Event_Call.EV_GUI_UNMUTE);
		return a;
	}

	/**
	 * Remove a Call from the CallCenter.
	 *
	 * @param d Call object to remove.
	 * @return success of operation.
	*/
	public boolean remove (Call d) { 
		boolean b=data.removeElement(d);
		d.deleteObserver(this); 
		touch(new CallMod(d,-1));
		return b;
	}
	
//	public Call get(int i) {
//		return (Call)(data.elementAt(i));
//	}


	private void add (Call d) { data.add(d); d.addObserver(this); touch(new CallMod(d,1)); }

	private void touch(CallMod cm) { setChanged(); notifyObservers(cm); }	
	
	private Call findCallContext (CallContext cc) {
		int i;
		Call a;
		synchronized (data) {
			for (i=0; i<data.size(); i++) {
				a=(Call)(elementAt(i));
				if (a.getCallContext()==cc) return a;
			}
		}
		return null;
	}

	/**
	 * Retrieve currently active Call.
	 *
	 * @return reference to current call, null if no active call is present.
	*/
	public Call getActiveCall () {
		int i;
		Call a;
		synchronized (data) {
			for (i=0; i<data.size(); i++) {
				a=(Call)(elementAt(i));
				if (a.getState()==IPT_State_Call.ST_ACTIVE) return a;
			}
		}
		return null;
	}

	/**
	 * The EventWorker class extends Thread to process an incoming event from the
     * SIP-UA. This must be done to guarantee an instant return to the SIP-UA after
     * It calls our interface functions.
	*/
	class EventWorker extends Thread {
		private int job;
		private CallContext sipcc;    // set by contructor

		public void run() {
			Call call;             // corresponding Call
			int event=-1;
			SDPManager sdpm;

			// System.out.println("CC-Status: "+sipcc.getStatusCode());
			// System.out.println("CC-Reason: "+sipcc.getReasonPhrase());
			// System.out.println("CC-RSDP: ");
			// System.out.println(sipcc.getRemoteSDP().encode());
		
			call=findCallContext(sipcc);		

			switch (job) {
				case CL_Notify: { 
					SDPAnnounce sdpa;
					// System.out.println("was CL_Notify"); 
					if (call==null) { 
						call=new Call(sipcc,CallCenter.this);
						call.setIncoming(Call.INCOMING);
						add(call);
					}	
					sdpa=sipcc.getRemoteSDP();
					if (sdpa==null) { 
						event=IPT_Event_Call.EV_SIP_N0;
						call.setRemoteMedia(null);
						break;
					}
					MediaDescriptor md;
					sdpm=new SDPManager(CallCenter.this.CFG.getAsString("SIPIdentity"));
					try {md=sdpm.extractAllCodecs(sdpa); }
					catch (Exception ex) { 
						System.out.println("Exception from exctractAllCodecs()");
						ex.printStackTrace();
						event=IPT_Event_Call.EV_GEN_ERROR; 
						call.setError(ex.getMessage());
						call.setRemoteMedia(null);
						break;
					}
					call.setRemoteMedia(md);
					if (md.peer.equals(zero4) || md.peer.equals(zero6) ) {
						event=IPT_Event_Call.EV_SIP_N0000;
						break;
					}					
					CodecDescriptor cd=sdpm.getFirstSupported(md,CPM.getUpstreamProfile());
					if (cd==null) {
						event=IPT_Event_Call.EV_SIP_NN;
						call.setError("no supported codec in upstream profile");
						break;
					} else {
						md.txcodec=cd;
						event=IPT_Event_Call.EV_SIP_NY;
					}
					break; 
				}
				case CL_Error: { 
					// System.out.println("was CL_Error"); 
					if (call==null) { 
						// System.out.println("      No matching Call found (strange)"); 
						break; 
					}
					event=IPT_Event_Call.EV_SIP_ERROR;
					break; 
				}
				case CL_Hangup: { 
					// System.out.println("was CL_HangUp"); 
					if (call==null) { 
						// System.out.println("      No matching Call found (strange)"); 
						break; 
					}
					event=IPT_Event_Call.EV_SIP_HANGUP;
					break; 
				}
			}
			if (event>=0) call.sendEvent(event);
			job=-1;
		}    

		EventWorker (CallContext cc, int job) {
			this.job=job;
			this.sipcc=cc;
		}
	}

}




