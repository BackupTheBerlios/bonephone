package bonephone;

/**
 * Class to hold data about a codec. 
 *
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
public class CodecDescriptor {

	/** short name of the codec. e.g. "PCMU", "L16", "G726", ... */
	public String shortname;

	/** frequency the codec is working at in Herz */
	public int freq;

	/** RTP payload type this codec shall be mapped to. -1 for no explicit mapping. */
	public int pt;

	/** number of channels the codec uses. 1 for mono, 2 for stereo. */
	public int channels;

	/** convert to a string of the form "shortname-frequency-(mono|stereo):channels @ payloadtype" */
	public String toString() {
		return shortname+"-"+(freq/1000)+"k-"+
		       ((channels==1)?"mono":((channels==2)?"stereo":""+channels))+
		       " @ "+pt;
	}

	public CodecDescriptor () { }

	/** Construct from short name, frequency, number of channels and payload type. */
    public CodecDescriptor(String shortname, int freq, int channels, int pt) {
		this.shortname=shortname;
		this.freq=freq;
		this.channels=channels;
		this.pt=pt;
	}
}
