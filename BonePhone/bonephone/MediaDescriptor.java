package bonephone;

import java.util.*;
import java.net.*;

/** Data class to describe which codecs are understood / used to send at which address/port, etc. */
public class MediaDescriptor {

	/** This is the only supported media type currently. */
	public static int AUDIO=0;

	/** Codecs found in SDP / getting signaled in SDP (CodecDescriptor objects). */
	public Vector      Codecs;

	/** Type of media, currently only AUDIO */
	public int         Type;

	/** port number to send to / receive at. */
	public int         port;

	/** number of ports used per connection, currently always 1. */
	public int         nports;

	/** address to send data to. can be instanceof Inet4Address or Inet6Address. */
	public InetAddress peer;   // can be v4 or v6

	/** time-ti-live for multicast sessions, currently not used. */
	public int         ttl;

	/** Codec used for transmission. */
	public CodecDescriptor txcodec;  // only used by remote media description

	public MediaDescriptor () {}
	public MediaDescriptor ( int Type, Vector Codecs, int port, int nports, InetAddress peer, int ttl, CodecDescriptor txcodec) {
		this.Codecs=Codecs;
		this.Type=Type;
		this.port=port;
		this.nports=nports;
		this.peer=peer;
		this.ttl=ttl;
		this.txcodec=txcodec;
	}

}
