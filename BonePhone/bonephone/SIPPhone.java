package bonephone;

import java.util.*;

/** This class implements the PhoneController interface and is the direct 
 * phone functionality provider to the GUI.
*/
class SIPPhone implements PhoneController, Observer {

	public VectorRead getCalls () { return CAC; }

	public void       muteCall     (Call c) { c.sendEvent(IPT_Event_Call.EV_GUI_MUTE); }
	public void       unmuteCall   (Call c) { c.sendEvent(IPT_Event_Call.EV_GUI_UNMUTE); }
	public void       hangupCall   (Call c) { c.sendEvent(IPT_Event_Call.EV_GUI_HUP); }
	public void       cancelCall   (Call c) { c.sendEvent(IPT_Event_Call.EV_GUI_CANCEL); }
	public void       acceptCall   (Call c) { c.sendEvent(IPT_Event_Call.EV_GUI_ACCEPT); }
	public void       callbackCall (Call c) { c.sendEvent(IPT_Event_Call.EV_GUI_CALLBACK); }
	public void       deleteCall   (Call c) { CAC.remove(c); }
	public Call       issueCall    (String To, String Subject) {
		return CAC.issueCall(To,Subject);
	}
	public void       fakeCall     () { CAC.fakeCall(); }

	public CodecProfileManager getCodecProfileManager() {
		return CAC.getCodecProfileManager(); 
	}

	public void       setOutputPort   (String port) {
		CAC.getMediaControl().setOutputChannel(port);
	}
	public Vector     getOutputPorts  () { return CAC.getMediaControl().getOutputPorts(); }
	public void       setInputPort    (String port) {
		CAC.getMediaControl().setInputChannel(port);
	}
	public Vector     getInputPorts   () { return CAC.getMediaControl().getInputPorts(); }
	public Vector     getAudioDevices () { return CAC.getMediaControl().getAudioDevices(); }
	public void       setOutputVolume (int percentage) { 
		CAC.getMediaControl().setOutputVolume(percentage);
	}
	public void       setInputVolume  (int percentage) {
		CAC.getMediaControl().setInputVolume(percentage);
	}
	public void       setOutputMute   (boolean mute) {
		CAC.getMediaControl().muteOutput(mute);
	}
	public void       setInputMute    (boolean mute) {
		CAC.getMediaControl().muteInput(mute);
	}

	public void       addPhoneObserver    (UINotifyable ui) { PhoneObservers.addElement(ui); }
	public void       removePhoneObserver (UINotifyable ui) { PhoneObservers.removeElement(ui); }


	/** Observer interface. The SipPhone is notified about new and deleted calls by the CallCenter and about
	* changes in the used codec. It will then notify its registered UINotifyable objects about the change. 
	* @param o unused.
	* @param arg either a CallMod object or a CodecDescriptor.
	*/
	public void update(Observable o, Object arg) {
		if (arg instanceof CallMod) {
			CallMod cm=(CallMod) arg;
			switch (cm.mod) {
				case -1: { notifyAllDeleted(cm.call); break; }
				case  1: { notifyAllNew(cm.call); break; }
			}
			return;
		}
		if (arg instanceof CodecDescriptor) {
			notifyAllCodecChanged((CodecDescriptor)arg);
		}
	}


	private void notifyAllCodecChanged(CodecDescriptor cd) {
		int i;
		for (i=0; i<PhoneObservers.size(); i++) {
			((UINotifyable)(PhoneObservers.elementAt(i))).codecChanged(cd);
		}
	}


	private void notifyAllNew(Call c) {
		int i;
		for (i=0; i<PhoneObservers.size(); i++) {
			((UINotifyable)(PhoneObservers.elementAt(i))).newCall(c);
		}
	}


	private void notifyAllDeleted(Call c) {
		int i;
		for (i=0; i<PhoneObservers.size(); i++) {
			((UINotifyable)(PhoneObservers.elementAt(i))).deletedCall(c);
		}
	}


	/** Standard contructor. 
	* @param CAC CallCenter to use for phone calls.
	*/
	public SIPPhone(CallCenter CAC) {
		this.CAC=CAC;
		PhoneObservers=new Vector();
		CAC.addObserver(this);
		CAC.getMediaControl().addObserver(this);
	}

	private CallCenter CAC;
	private Vector PhoneObservers;

}

