package bonephone;

import java.util.*;

/**
 * A CodecProfile is nothing more than a set of codecs which match a particular 
 * aspect as bandwidth, audio quality, or simply user preference.
 *
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
public class CodecProfileManager {

	/** Get the Codecs in a profile.
	 * 
	 * @param number Number of queried profile.
	 * @return Vector of CodecDescriptor objects.
	*/
	public Vector getProfile(int number) { return (Vector)( profiles.elementAt(number)); }
	
	/** Retrieve a Profiles Display name. This name is implicit for the default profiles, explicit for
     * the ones defined in the "profile" config file.
	 * @param number Number of queried profile.
	*/
	public String getDisplayName(int number) { return (String)(names.elementAt(number)); }
	
	/** retrieve number of profiles available.
	 * @return number of profile.
	*/
	public int getNumberOfProfiles() { return names.size(); }

	/** Set the current upstream codec profile to use in sent SDP announcements. 
	 * @param usp Vector of CodecDescriptor Objects.
	*/	
	public void   setUpstreamProfile(Vector usp) { upstreamprofile=usp; }

	/** Retrieve the current upstream codec profile.
	 * @return Vector of CodecDescriptor objects.
	*/
	public Vector getUpstreamProfile()           { return upstreamprofile; }

	/** Set the current downstream codec profile to check in read SDP announcements. 
	 * @param usp Vector of CodecDescriptor Objects.
	*/	
	public void   setDownstreamProfile(Vector dsp) { downstreamprofile=dsp; }

	/** Retrieve the current downstream codec profile.
	 * @return Vector of CodecDescriptor objects.
	*/
	public Vector getDownstreamProfile()           { return downstreamprofile; }

	// - - - - - - - - - Contructors - - - - - - - -
	
	/** Build a new CodecProfileManager by specifying *all* supported codecs and a
     * configfile style file with more profiles to read. Usually this is ".bonephone/profiles".
	 * Seven default profiles are build. They represent all supported codecs by bandwidth. (7 Groups).
	 * @param supported Vector of CodecDescriptor objects. These codecs are understood by the audio engine.
	 * @param profilename Filename where to look for additional user supplied profiles.
	*/
	public CodecProfileManager(Vector supported, String profilename) { 
		init(supported,profilename); 
	}
	
	public CodecProfileManager() { init(null,null); } 


	// - - - - - - - - - privates - - - - - - - - - 

	private void init(Vector supported, String pfname) {
		int n=0;
		
		names=new Vector(6);
		profiles=new Vector(6);
		Vector Codecs=new Vector(50);

		// First we init our default profiles
		
		names.addElement("<= 33.6 Kbps"); // Best codec at end
		Codecs.insertElementAt(new CodecDescriptor("LPC",     8000, 1,  7), 0);  // 3
		Codecs.insertElementAt(new CodecDescriptor("GSM",     8000, 1,  3), 0);  // 13
		Codecs.insertElementAt(new CodecDescriptor("DVI",     8000, 1,  5), 0);  // 32
		Codecs.insertElementAt(new CodecDescriptor("VDVI",    8000, 1, 77), 0);  // 25
		Codecs.insertElementAt(new CodecDescriptor("G726-16", 8000, 1, 96), 0);  // 16
		Codecs.insertElementAt(new CodecDescriptor("G726-24", 8000, 1,100), 0);  // 24
		Codecs.insertElementAt(new CodecDescriptor("G726-32", 8000, 1,  2), 0);  // 32
		Codecs.insertElementAt(new CodecDescriptor("G726-16",16000, 1, 97), 0);  // 32
		Codecs.insertElementAt(new CodecDescriptor("GSM",    16000, 1,118), 0);  // 26
		profiles.addElement(Codecs.clone());
		
		names.addElement("<= 64 Kbps");   // Best codec at end
		Codecs.insertElementAt(new CodecDescriptor("PCMA",    8000, 1,  8), 0);  // 64
		Codecs.insertElementAt(new CodecDescriptor("PCMU",    8000, 1,  0), 0);  // 64
		Codecs.insertElementAt(new CodecDescriptor("G726-40", 8000, 1,107), 0);  // 40
		Codecs.insertElementAt(new CodecDescriptor("DVI",    11025, 1, 16), 0);  // 44
		Codecs.insertElementAt(new CodecDescriptor("DVI",    16000, 1,  6), 0);  // 64
		Codecs.insertElementAt(new CodecDescriptor("VDVI",   16000, 1, 78), 0);  // 50
		Codecs.insertElementAt(new CodecDescriptor("G726-24",16000, 1,101), 0);  // 48
		Codecs.insertElementAt(new CodecDescriptor("G726-32",16000, 1,104), 0);  // 64
		Codecs.insertElementAt(new CodecDescriptor("GSM",    32000, 1,119), 0);  // 52 
		Codecs.insertElementAt(new CodecDescriptor("G726-16",32000, 1, 98), 0);  // 64
		profiles.addElement(Codecs.clone());

		names.addElement("<= 128 Kbps");  // Best codec at end
		Codecs.insertElementAt(new CodecDescriptor("PCMA",    8000, 2, 90), 0);  // 128
		Codecs.insertElementAt(new CodecDescriptor("PCMA",   16000, 1, 91), 0);  // 128
		Codecs.insertElementAt(new CodecDescriptor("L16",     8000, 1,122), 0);  // 128
		Codecs.insertElementAt(new CodecDescriptor("PCMU",    8000, 2, 83), 0);  // 128
		Codecs.insertElementAt(new CodecDescriptor("PCMU",   16000, 1, 84), 0);  // 128
		Codecs.insertElementAt(new CodecDescriptor("G726-40",16000, 1,108), 0);  // 80
		Codecs.insertElementAt(new CodecDescriptor("DVI",    22050, 1, 17), 0);  // 88
		Codecs.insertElementAt(new CodecDescriptor("DVI",    32000, 1, 81), 0);  // 128
		Codecs.insertElementAt(new CodecDescriptor("G726-24",32000, 1,102), 0);  // 96
		Codecs.insertElementAt(new CodecDescriptor("GSM",    48000, 1,123), 0);  // 78
		Codecs.insertElementAt(new CodecDescriptor("G726-16",48000, 1, 99), 0);  // 96
		profiles.addElement(Codecs.clone());
		
		names.addElement("<= 256 Kbps");  // Best codec at end
		Codecs.insertElementAt(new CodecDescriptor("L16",     8000, 2,111), 0);  // 256
		Codecs.insertElementAt(new CodecDescriptor("WBS",    16000, 1,109), 0);  // 128
		Codecs.insertElementAt(new CodecDescriptor("L16",    16000, 1,112), 0);  // 256
		Codecs.insertElementAt(new CodecDescriptor("PCMA",   32000, 1, 92), 0);  // 256
		Codecs.insertElementAt(new CodecDescriptor("PCMA",   16000, 2,124), 0);  // 256
		Codecs.insertElementAt(new CodecDescriptor("PCMU",   16000, 2, 85), 0);  // 256
		Codecs.insertElementAt(new CodecDescriptor("PCMU",   32000, 1, 86), 0);  // 256
		Codecs.insertElementAt(new CodecDescriptor("VDVI",   32000, 1, 79), 0);  // 100
		Codecs.insertElementAt(new CodecDescriptor("G726-32",32000, 1,105), 0);  // 128
		Codecs.insertElementAt(new CodecDescriptor("DVI",    48000, 1, 82), 0);  // 196
		Codecs.insertElementAt(new CodecDescriptor("G726-40",32000, 1,110), 0);  // 160
		Codecs.insertElementAt(new CodecDescriptor("VDVI",   48000, 1, 80), 0);  // 150
		Codecs.insertElementAt(new CodecDescriptor("G726-24",48000, 1,103), 0);  // 144
		Codecs.insertElementAt(new CodecDescriptor("G726-32",48000, 1,106), 0);  // 192
		Codecs.insertElementAt(new CodecDescriptor("G726-40",48000, 1,119), 0);  // 240
		profiles.addElement(Codecs.clone());

		names.addElement("<= 768 Kbps");  // Best codec at end
		Codecs.insertElementAt(new CodecDescriptor("L16",    16000, 2,113), 0);  // 512
		Codecs.insertElementAt(new CodecDescriptor("L16",    32000, 1,114), 0);  // 512
		Codecs.insertElementAt(new CodecDescriptor("L16",    44000, 1, 10), 0);  // 704
		Codecs.insertElementAt(new CodecDescriptor("L16",    48000, 1,116), 0);  // 768
		Codecs.insertElementAt(new CodecDescriptor("PCMA",   32000, 2, 93), 0);  // 512
		Codecs.insertElementAt(new CodecDescriptor("PCMA",   48000, 2, 95), 0);  // 768
		Codecs.insertElementAt(new CodecDescriptor("PCMA",   48000, 1, 94), 0);  // 384
		Codecs.insertElementAt(new CodecDescriptor("PCMU",   32000, 2, 87), 0);  // 512
		Codecs.insertElementAt(new CodecDescriptor("PCMU",   48000, 1, 88), 0);  // 384
		Codecs.insertElementAt(new CodecDescriptor("PCMU",   48000, 2, 89), 0);  // 768
		profiles.addElement(Codecs.clone());
		
		names.addElement("<= 1 Mbps");    // Best codec at end
		Codecs.insertElementAt(new CodecDescriptor("L16",    32000, 2,115), 0);  // 1024
		profiles.addElement(Codecs.clone());
		
		names.addElement("> 1 Mbps");     // Best codec at end
		Codecs.insertElementAt(new CodecDescriptor("L16",    44000, 2, 11), 0);  // 1408
		Codecs.insertElementAt(new CodecDescriptor("L16",    48000, 2,117), 0);  // 1536
		profiles.addElement(Codecs.clone());

		
		// Now read the profiles file for more profiles
		if (pfname!=null) {
			Codecs=null;
			Configuration cfg=new Configuration(pfname);
			ConfigIterator it=cfg.getIterator();
			NamedValue nv;
			for (nv=it.next(); nv!=null; nv=it.next()) {
				if (nv.name.equalsIgnoreCase("Profile")) {
					if (Codecs!=null && Codecs.size()>0) profiles.addElement(Codecs);
					names.addElement(nv.value);
					Codecs=new Vector(5);
					continue;
				}
				if (nv.name.equalsIgnoreCase("Codec")) {
					Vector tok=Tokenizer.split(nv.value," ");
					if (tok.size()<4) {
						System.err.println("Incomplete codec description: "+nv.name+": "+nv.value);
						continue;
					}
					String shortname=(String)(tok.elementAt(0));
					int freq, chan, pt;
					try { freq=Integer.parseInt((String)(tok.elementAt(1))); }
					catch (Exception ex) { 
						System.err.println("Invalid frequency in codec description: "+nv.name+": "+nv.value);
						continue;
					}
					try { chan=Integer.parseInt((String)(tok.elementAt(2))); }
					catch (Exception ex) { 
						System.err.println("Invalid channels in codec description: "+nv.name+": "+nv.value);
						continue;
					}
					try { pt=Integer.parseInt((String)(tok.elementAt(3))); }
					catch (Exception ex) { 
						System.err.println("Invalid payload type in codec description: "+nv.name+": "+nv.value);
						continue;
					}
					Codecs.addElement(new CodecDescriptor(shortname, freq, chan, pt));
				}
			}
			if (Codecs!=null && Codecs.size()>0) profiles.addElement(Codecs);
		}


		if (supported==null) return;


		// remove all non-supported codecs from the profiles
		int p;
		for (p=0; p<profiles.size(); p++) {
			Vector pr=(Vector)(profiles.elementAt(p));
			int i;
			for (i=0; i<pr.size(); i++) {
				CodecDescriptor cdp=(CodecDescriptor)(pr.elementAt(i));
				int j; 
				boolean found=false;
				for (j=0; j<supported.size(); j++) {
					CodecDescriptor cds=(CodecDescriptor)(supported.elementAt(j));
					if (! cds.shortname.equalsIgnoreCase(cdp.shortname)) continue;
					if (cds.freq != cdp.freq) continue; 
					if (cds.channels != cdp.channels) continue; 
					found=true; 
					break;
				}
				if (!found) pr.removeElementAt(i);
			}
		}
	}

	private Vector names; // Vector of String

	private Vector profiles; // Vector of Vector of CodecDescriptor

	private Vector upstreamprofile=null;     // Vector of CodecDescriptor 
	private Vector downstreamprofile=null;   // Vector of CodecDescriptor
}
