package bonephone;

interface PhoneController {

	public VectorRead getCalls ();

	public void       muteCall   (Call c);
	public void       unmuteCall (Call c);
	public void       hangupCall (Call c);
	public Call       issueCall  (String To, String Subject);
	public void       fakeCall   ();

	public CodecProfileManager getCodecProfileManager();

	public void       setOutputPort   (String port);
	public Vector     getOutputPorts  ();
	public void       setInputPort    (String port);
	public Vector     getInputPorts   ();
	public Vector     getAudioDevices ();
	public void       setOutputVolume (int percentage);
	public void       setInputVolume  (int percentage);
	public void       setOutputMute   (boolean mute);
	public void       setInputMute    (boolean mute);

	public void       addPhoneObserver    (UINotifyable ui);
	public void       removePhoneObserver (UINotifyable ui);

}

