package bonephone;

import java.net.*;
import java.util.*;

/**
 * This class is the public interface to everyone wanting to do something with
 * the underlying media engine.
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
public class MediaControl extends Observable implements Observer {

	private Configuration CFG;	
	private RATControl rat;
	private Vector codecs=null;  // of CodecDescriptor
	private int ncd=0;
	private int iv,ov;
	private boolean mi=true,mo=false;
	private boolean sp=false,hp=true;
	private Call owner=null;
	private Vector inputchannels;
	private Vector outputchannels;
	private Vector audiodevices;

	/* --------------------- functions for Call management ------------------- */

	/** check media engine for being available.
	 * @param referrer Call who wants to get the engine.
	 * @return true on succes, the media engine is reserved for the referrer.<br>
	 *         false if media engine is in use by another call.
	*/
	public synchronized boolean isAvailable(Call referrer) {
		if (rat!=null && owner==referrer) return true;
		if (rat!=null) return false;
		rat=new RATControl(CFG.getAsString("RATPath"));
		rat.addObserver(this);
		owner=referrer;
		return true;
	}


	/** Start the media engine and make it connect to the remote address/port, bind to
	 * the specified local port and use the transmit codec in the remote media description.
	 * when this function return, the medi engine is running and working.
	 * @param remote Description of the remote side.
	 * @param remote Description of the local side.
	*/
	public void start(MediaDescriptor remote, MediaDescriptor local) throws Exception {
		InetAddress peer;
		// try { peer=InetAddress.getByName(remote.peer); }
		// catch (Exception ex) { 
		// 	throw new Exception("remote.peer="+remote.peer+":"+ex.getMessage()); 
		// }
		peer=remote.peer;
		rat.go(); 
		rat.setLocalPort(local.port);
		rat.setPeerAddress(peer);
		rat.setRemotePort(remote.port);
		rat.release();
		rat.setCodec(remote.txcodec);
		rat.muteInput(mi); 
		rat.muteOutput(mo); 
		rat.setOutputVolume(ov); 
		rat.setInputVolume(iv); 
		rat.setInputChannel(CFG.getAsString("InputChannel"));
		rat.setOutputChannel(CFG.getAsString("OutputChannel"));
	}

	/** terminate the media engine for this side. */
	public void stop(MediaDescriptor local) { 
		if (rat!=null) rat.terminate(); 
		rat=null; 
		owner=null;
	}


	/* --------------------- Observable/Observer ------------------- */

	/** the media engine is observed for codec changes.
	 * @param o is a RATControl object.
	 * @param arg is a CodecDescriptor.
	*/
	public void update (Observable o, Object arg) {
		setChanged();
		notifyObservers((CodecDescriptor)arg);
	}

	/* --------------------- GUI buildup functions ----------------- */

	/** @return Vector of CodecDescriptor objects. */
	public Vector getAllSupportedCodecs() { return codecs; }


	/** @return Vactor of String objects with available audio devices. */
	public Vector getAudioDevices() {
		return audiodevices;
	}


	/** @return Vactor of String objects with available input ports. */
	public Vector getInputPorts() {
		return inputchannels;
	}


	/** @return Vactor of String objects with available output ports. */
	public Vector getOutputPorts() {
		return outputchannels;
	}


	/* ----------------- functions for GUI widgets ------------------------- */
	
	/** @param percentage Value 0..100 to set the output volume to. */
	public void setOutputVolume(int percentage) { 
		ov=percentage;
		if (rat==null) return; 
		rat.setOutputVolume(percentage); 
	}


	/** @param percentage Value 0..100 to set the input volume to. */
	public void setInputVolume(int percentage)  { 
		iv=percentage;
		if (rat==null) return;
		rat.setInputVolume(percentage); 
	}


	/** @param mute true to mute, false to unmute audio input. */
	public void muteInput(boolean mute) { 
		mi=mute;
		if (rat==null) return;
		rat.muteInput(mute); 
	}


	/** @param mute true to mute, false to unmute audio output. */
	public void muteOutput(boolean mute) { 
		mo=mute;
		if (rat==null) return;
		rat.muteOutput(mute); 
	}

	/** @param chan name of output channel to use. Must be obtained by getOutputPorts(). */
	public void setOutputChannel(String chan) {
		if (rat==null) return;
		rat.setOutputChannel(chan);
	}	


	/** @param chan name of output channel to use. Must be obtained by getInputPorts(). */
	public void setInputChannel(String chan) {
		if (rat==null) return;
		rat.setInputChannel(chan);
	}	


	/* ------------------- constructor ------------------------ */

	/** starts RAT for the local host to obtain codecs **/
	private void ReadSupportedCodecs() throws Exception {
		int port=CFG.getAsInt("RTPFirstPort");
		rat=new RATControl(CFG.getAsString("RATPath"));
		rat.go();
		rat.setLocalPort(port);
		rat.setPeerAddress(main.getLocalAddress());
		rat.setRemotePort(port);
		rat.release();
		codecs=rat.getSupportedCodecs();
		audiodevices=rat.getAudioDevices();
		inputchannels=rat.getInputChannels();
		outputchannels=rat.getOutputChannels();
		rat.terminate(); 
		rat=null; 
		owner=null;
	}

	public MediaControl(Configuration cfg) throws Exception { 
		CFG=cfg;
		ReadSupportedCodecs();
	}
}
