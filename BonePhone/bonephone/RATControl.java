package bonephone;

import org.mbus.*;
import java.util.*;
import java.net.*;


class MBusAdapter implements MBusListener {
	public void deliveryFailed(int n, Message m) {}
	public void deliverySuccessful(int n, Message m) {}
	public void incomingMessage(Message m) {}
	public void entityDied(Address a) {}
	public void entityShutdown(Address a) {}
	public void newEntity(Address a) {}
}


/** This class presents basic functionality to start/stop/control a RAT media engine.
 *  This is done by implementing the basic MBUS messages spoken/understood by the 
 *  engine. 
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
public class RATControl extends Observable {

	/** TransportLayers used for MBUS communication. */
	private TransportLayer tl_ui      = null;
	private TransportLayer tl_control = null;


	private Process RATproc;
	private String RATpath;
	private String boneid;

	private Vector InputChannels;
	private Vector OutputChannels;

	private Vector AudioDevices;

	private String[][] ControlAddressStr = {{"media","audio"},
	                                        {"module", "control"},
	                                        {"app","bonephone"},
	                                        {"boneid","bid"}
	                                       };
	private String[][] UiAddressStr = {{"media","audio"},
	                                   {"module", "ui"},
	                                   {"app","bonephone"},
	                                   {"boneid","bid"}
	                                  };
	private String[][] EngineAddressStr = {{"media","audio"},
	                                       {"module", "engine"},
	                                       {"app","rat"}
	                                      };

	private Address UiAddress;
	private Address ControlAddress;
	private Address EngineAddress;

	private int ControllerState;
	private int UiState;

	private boolean ready=false;
	private boolean running=false; // true when tool.rat.bps.in is received.
	private boolean started=false; // true if go() was called and succeeded
	private boolean released=false;

	private String token1;
	private String token3="rat-ui-requested";

	private Vector codecs;
	private CodecDescriptor currentcodec= new CodecDescriptor(null, 0, 0, -1);

	class RAT_UI extends MBusAdapter {

		private String extractCodecname(String RATcd) {
			Vector items=Tokenizer.split(RATcd,"-");
			if (items.size()==3) {
				return (String)(items.elementAt(0));
			}
			if (items.size()==4) {
				return (String)(items.elementAt(0))+"-"+(String)(items.elementAt(1));
			}
			return "error";
		}


   		public void incomingMessage(Message m) {
			Enumeration e = m.getCommands();
			int i;
			MDataType[] p;
			Vector v=new Vector(1);
			
			// We are interested only in messages from the engine
			if (! m.getHeader().getSourceAddress().matches(EngineAddress)) return;

			while(e.hasMoreElements()) {
				Command com = (Command)e.nextElement();   
				String cmd = com.getCommand();
				p=com.getParameters();
				switch (UiState) {
					case 0: {
						if ( cmd.equals("mbus.waiting") ) {
							if ( ((MString)(p[0])).getData().equals(token3) ) {
								MultipleCommandSender mcs=new MultipleCommandSender(UiAddress, EngineAddress, tl_ui);
								mcs.add("mbus.go(\""+token3+"\")");
								mcs.add("tool.rat.settings()");
								mcs.add("audio.query()");
								mcs.add("rtp.query()");
								mcs.send();
								UiState=1;
							}
						}
						break;
					}
					case 1: {
						if ( cmd.equals("tool.rat.codec") ) {
							currentcodec.shortname=((MString)(p[0])).getData();
							setChanged();
							notifyObservers(currentcodec);
							break;
						}
						// if ( cmd.equals("tool.rat.bps.in") ) {
						if ( cmd.equals("session.title") ) {
							running=true;
							break;
						}
						if ( cmd.equals("audio.output.ports.flush") ) {
							OutputChannels.removeAllElements();
							break;
						}
						if ( cmd.equals("audio.output.ports.add") ) {
							String port=((MString)(p[0])).getData();
							OutputChannels.addElement(port);
							break;
						}
						if ( cmd.equals("audio.devices.flush") ) {
							AudioDevices.removeAllElements();
							break;
						}
						if ( cmd.equals("audio.devices.add") ) {
							String device=((MString)(p[0])).getData();
							AudioDevices.addElement(device);
							break;
						}
						if ( cmd.equals("audio.input.ports.flush") ) {
							InputChannels.removeAllElements();
							break;
						}
						if ( cmd.equals("audio.input.ports.add") ) {
							String port=((MString)(p[0])).getData();
							InputChannels.addElement(port);
							break;
						}
						if ( cmd.equals("tool.rat.codecs.flush") ) {
							codecs.removeAllElements();
							break;
						}
						if ( cmd.equals("tool.rat.codecs.add") ) {
							String pts;
							CodecDescriptor cd=new CodecDescriptor();
							pts=((MString)(p[0])).getData();
							try { cd.pt=Integer.parseInt(pts); }
							catch (Exception exc) { cd.pt=-1; }
							cd.shortname=extractCodecname(((MString)(p[1])).getData());
							cd.freq=((MInteger)(p[4])).intValue();
							cd.channels=((MInteger)(p[3])).intValue();
							codecs.addElement(cd);
						}
						break;
					}
				}
			}
		}

		RAT_UI() {}
	}

	class RAT_CONTROL extends MBusAdapter implements BPTimerTaskIF {

		BPTimer bptimer;
		Message mbuswaiting;

		public void action() {
			if (tl_control!=null) tl_control.send(mbuswaiting);
		}

		public void newEntity(Address a) {
			System.out.println("__ NEW ENTITY: "+a);
			EngineAddress=new Address(EngineAddressStr);
			if (EngineAddress.matches(a)) {
				System.out.println("Engine entered the stage.");
				EngineAddress=a;  // Remember full engine address
				Vector v=new Vector(1);
				v.addElement(new Command("mbus.waiting (\""+token1+"\")"));
				mbuswaiting=new Message (new Header(ControlAddress, EngineAddress, 0, false), v);
				ControllerState=1;
				bptimer= new BPTimer(this, 2000, true);
				bptimer.start();
			}			
		}

		public void incomingMessage(Message m) {
			Enumeration e = m.getCommands();
			int i;
			MDataType[] p;
			Vector v=new Vector(1);
			
			// We are interested only in messages from the engine
			if (! m.getHeader().getSourceAddress().matches(EngineAddress)) return;

			// Store the full engine address
			EngineAddress=m.getHeader().getSourceAddress();

			while(e.hasMoreElements()) {
				Command com = (Command)e.nextElement();   
				String cmd = com.getCommand();
				p=com.getParameters();
				switch (ControllerState) {
					case 1: {
						if ( cmd.equals("mbus.go") ) {
							System.out.println("received mbus.go");
							if ( ((MString)(p[0])).getData().equals(token1) ) {
								bptimer.stopTimer();
								bptimer=null;
								ControllerState=2;
							} else {
								System.out.println("Wrong token");
							}
						}
						break;
					}
					case 2: {
						if (cmd.equals("mbus.waiting")) {
							// System.out.println("received mbus.waiting");
							// System.out.println("released=="+(released?"true":"false"));
						}
						if ( cmd.equals("mbus.waiting") && released ) {
							if ( ((MString)(p[0])).getData().equals(token1) ) {
								// send rtp.addr()   mbus.go()
								MultipleCommandSender mcs=new MultipleCommandSender(ControlAddress, EngineAddress, tl_control);
								mcs.add("rtp.addr (\""+peer.getHostAddress()+"\" "+lport+" "+rport+" 15)");
								mcs.send();
								// mcs.removeAll();
								// mcs.add("tool.rat.codec(\"LPC\" \"mono\" \"8\")");
								// mcs.send();
								mcs.removeAll();
								mcs.add("mbus.go(\""+token1+"\")");
								mcs.send();
								ControllerState=3;
							}
						}
						break;
					}
					case 3: {
						System.out.println("CONTROL <-- "+com);
						break;
					}
				}
			}
		}

		RAT_CONTROL() {}
	}


	/** set the released flag.
	 *  at the next mbus.waiting the early conguration will take place
	 *  regardless of the RTP Information being complete or correct
	*/
	public void release() {
		released=true;
	}


	/** While startup, This class has collected RATs announcements about its supported codecs.
     * This can be retreived by this function.
	 * @return Vector of CodecDescriptor objects representing all found codecs.
	*/
	public Vector getSupportedCodecs() {
		if (!started) return null;
		waitUntilRunning();
	
		if (codecs==null) { return null; }
		return codecs;
	}


	/** While startup, This class has collected RATs announcements about the input channels at the
	 * audio device. This can be retreived by this function.
	 * @return Vector of String objects representing all found input chanels.
	*/
	public Vector getInputChannels() { return InputChannels; }

	/** While startup, This class has collected RATs announcements about the available audio devices.
	 * This can be retreived by this function.
	 * @return Vector of String objects representing all found audio devices.
	*/
	public Vector getAudioDevices() { return AudioDevices; }

	/** While startup, This class has collected RATs announcements about the output channels at the
	 * audio device. This can be retreived by this function.
	 * @return Vector of String objects representing all found output chanels.
	*/
	public Vector getOutputChannels() { return OutputChannels; }


	private void waitUntilRunning() {
		while (!running) { 
			try {
				Thread.sleep(50); 
			} catch (InterruptedException ie) {}
		}
	}		


	private boolean isRATRunning() {
		try {
			RATproc.exitValue();
		} catch (IllegalThreadStateException itse) {
			return true;
		}
		return false;
	}

		
	private boolean isReady() { 
		if (!ready) {
			if (ControllerState==3 && UiState==1) ready=true;
		}
		return ready;
	}


	/** retrieve codec which is being used for sending right now. */
	public CodecDescriptor getCodec() { return currentcodec; }


	/** Set the input channel name to use.
	 * @param chan Channel name.
	*/
	public void setInputChannel(String chan) {
		if (!isReady()) return;

		MultipleCommandSender mcs=new MultipleCommandSender(UiAddress, EngineAddress, tl_ui);
		mcs.add("audio.input.port (\""+chan+"\")");
		mcs.send();
	}


	/** Set the output channel name to use.
	 * @param chan Channel name.
	*/
	public void setOutputChannel(String chan) {
		if (!isReady()) return;

		MultipleCommandSender mcs=new MultipleCommandSender(UiAddress, EngineAddress, tl_ui);
		mcs.add("audio.output.port (\""+chan+"\")");
		mcs.send();
	}

	/** set the input volume.
	 * @param percentage value of 1..100.
	*/
	public void setInputVolume(int percentage) {
		if (!isReady()) return;

		percentage=(percentage<0)?0:((percentage>100)?100:percentage);
		MultipleCommandSender mcs=new MultipleCommandSender(UiAddress, EngineAddress, tl_ui);
		mcs.add("audio.input.gain ("+percentage+")");
		mcs.send();
	}


	/** set the output volume.
	 * @param percentage value of 1..100.
	*/
	public void setOutputVolume(int percentage) {
		if (!isReady()) return;

		percentage=(percentage<0)?0:((percentage>100)?100:percentage);
		MultipleCommandSender mcs=new MultipleCommandSender(UiAddress, EngineAddress, tl_ui);
		mcs.add("audio.output.gain ("+percentage+")");
		mcs.send();
	}


	/** Waits until rat in in its transmission phase (bps.in/bps.out),
	 * then sets the codec to use for transmission. 
	 * @param cd CodecDecsriptor specifying the codec to use.
	*/
	public boolean setCodec(CodecDescriptor cd) {
		return (setCodec(cd.shortname, cd.freq, cd.channels, cd.pt));
	}

	/** Waits until rat in in its transmission phase (bps.in/bps.out),
	 * then sets the codec to use for transmission. 
	 * @param shortname name of the codec.
	 * @param freq ferquency of the codec.
	 * @param channels number of channels for the codec (1 or 2).
	 * @param pt RTP-payload type to map this codec.
	*/
	private boolean setCodec(String shortname, int freq, int channels, int pt) {
		if (!started) return false;
		waitUntilRunning();

		MultipleCommandSender mcs=new MultipleCommandSender(UiAddress, EngineAddress, tl_ui);
		// System.out.println("MAPPING: "+shortname+"-"+(freq/1000)+"k-"+((channels==1)?"mono":"stereo")+" "+pt);
		mcs.add("tool.rat.payload.set (\""+shortname+"-"+(freq/1000)+"k-"+((channels==1)?"mono":"stereo")+"\" "+pt+")");
		mcs.send();
		mcs.removeAll();
		mcs.add("tool.rat.codec (\""+shortname+"\" \""+((channels==1)?"mono":"stereo")+"\" \""+(freq/1000)+"\")");
		mcs.send();
		mcs.removeAll();
		mcs.add("tool.rat.settings ()");
		mcs.send();
		currentcodec.shortname=shortname;
		currentcodec.freq=freq;
		currentcodec.channels=channels;
		currentcodec.pt=pt;
		return true;
	}


	/** mute/unmute input channel.
	 @param mute true: mute, false: unmute.
	*/
	public void muteInput(boolean mute) {
		if (!isReady()) return;

		MultipleCommandSender mcs=new MultipleCommandSender(UiAddress, EngineAddress, tl_ui);
		mcs.add("audio.input.mute ("+(mute?1:0)+")");
		mcs.send();
	}


	/** mute/unmute output channel.
	 @param mute true: mute, false: unmute.
	*/
	public void muteOutput(boolean mute) {
		if (!isReady()) return;

		MultipleCommandSender mcs=new MultipleCommandSender(UiAddress, EngineAddress, tl_ui);
		mcs.add("audio.output.mute ("+(mute?1:0)+")");
		mcs.send();
	}


	/** Starts up RAT and talks it via MBUS to its early configuration point. After this the 
	 * peer addres, local/remote port must be set, then release() is used.
	*/	
	public int go() {
		boneid=""+System.currentTimeMillis();
		ControlAddressStr[3][1]=boneid;
		UiAddressStr[3][1]=boneid;
		ControlAddress=new Address(ControlAddressStr);
		UiAddress=new Address(UiAddressStr);

		// Init MBUS entities (control & ui)

		try {
			tl_ui = new SimpleTransportLayer(UiAddress, new RAT_UI());
		} catch (MBusException me) {
			System.err.println("--> "+me.getMessage());
			tl_ui=null;
		}

		try {
			tl_control = new SimpleTransportLayer(ControlAddress, new RAT_CONTROL());
		} catch (MBusException me) {
			System.err.println("--> "+me.getMessage());
			tl_control=null;
		}

		// Start RAT process

		token1="token-"+System.currentTimeMillis();

		String[] cmd={ RATpath,
		               "-ctrl", ControlAddress.toString(),
		               "-token", token1
		             };

		try {
			RATproc=Runtime.getRuntime().exec(cmd);
		} catch (java.io.IOException ioe) {
			System.out.println("--> "+ioe.getMessage());
			RATproc=null;
		}

		if (RATproc==null) { 
			System.out.println("RATproc is null");
		}
		
		new IOStreamRedirector(RATproc.getInputStream(), System.out);
		new IOStreamRedirector(RATproc.getErrorStream(), System.err);

		while (ControllerState<2) { 
			try {
				Thread.sleep(10); 
			} catch (InterruptedException ie) {}
		}
	
		started=true; // RAT is now at its early configuration point

		return 0;
	}


	/** local port number where RTP is received. */
	private int lport=0;

	/** remote port number where RTP is sent to. */
	private int rport=0;

	/** remote address. */
	private InetAddress peer=null;

	/** set UDP port to bind to. On this port RTP is received. 
	 * @param port local port number.
	*/
	synchronized public void setLocalPort(int port) { this.lport=port; }

	/** get UDP port to bind to. On this port RTP is received.
	 * @return local port number.
	*/
	public int  getLocalPort() { return lport; }

	/** set UDP port to send RTP stream to. 
	 * @param port remote hosts port number.
	*/
	synchronized public void setRemotePort(int port) { this.rport=port; }
	
	/** get UDP port where RTP information is sent to. 
	 * @return remote hosts port number.
	*/
	public int  getRemotePort() { return rport; }

	/** set IP Address to send RTP stream to.
	 * @param adr remote host address.
	*/
	synchronized public void setPeerAddress(InetAddress adr) { this.peer=adr; }

	/** get IP Address to send RTP stream to.
	 * @return remote host address.
	*/
	public InetAddress getPeerAddress() { return this.peer; }

	/** Terminate the session. 
	 * Termination is done by <br> 1. send mbus.quit() to the engine<br>
	 * 2. wait for termination of the RAT-process<br>3.shutdown/close the ui & controller 
	 * MBUS Entities. 
	**/     
	public void terminate() {
		if (!started) return;

		started=false;
		
		// 1. send mbus.quit()
		MultipleCommandSender mcs=new MultipleCommandSender(UiAddress, EngineAddress, tl_ui);
		mcs.add("mbus.quit ()");
		mcs.send();

		// 2. wait for RAT to die
		int n=0;
		while (isRATRunning() && n<40) {
			try {
				Thread.sleep(50); 
			} catch (InterruptedException ie) {}
			n++;
		}
		RATproc.destroy();

		// 3. destroy TransportLayers
		tl_ui.shutdown(UiAddress);
		tl_ui.close();
		tl_ui=null;
		tl_control.shutdown(ControlAddress);
		tl_control.close();
		tl_control=null;

	}

	/** @param path Path to the rat-4.2.20-media executable file */ 
	RATControl(String path) {
		ControllerState=0;
		UiState=0;
		codecs=new Vector(20);
		ready=false;
		running=false;
		RATpath=path;
		InputChannels=new Vector(4);
		OutputChannels=new Vector(4);
		AudioDevices=new Vector(2);
	}
}
