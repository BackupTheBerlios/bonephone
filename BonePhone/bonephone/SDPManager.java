package bonephone;

import java.util.*;
import java.net.*;
import gov.nist.sip.*;
import gov.nist.sip.sdpfields.*;
import gov.nist.sip.msgparser.*;
import gov.nist.sip.net.*;

/**
 * class to build and parse SDPAnnounce objects.
 *
 * @author	Jens Fiedler
 * @version	1.0
*/
public class SDPManager {

	                                    // RAT         IANA
	private String[][] RATtranslate = { 
	                                    { "DVI"      , "DVI4" } , 
	                                    { "L16"      , "L16" }
	                                  };

	private CodecDescriptor[] ianalist = { 
	                              new CodecDescriptor("PCMU",   8000,1, 0 ),
	                              new CodecDescriptor("1016",   8000,1, 1 ),
	                              new CodecDescriptor("G726-32",8000,1, 2 ),
	                              new CodecDescriptor("GSM",    8000,1, 3 ),
	                              new CodecDescriptor("G723",   8000,1, 4 ),
	                              new CodecDescriptor("DVI4",   8000,1, 5 ),
	                              new CodecDescriptor("DVI4",  16000,1, 6 ),
	                              new CodecDescriptor("LPC",    8000,1, 7 ),
	                              new CodecDescriptor("PCMA",   8000,1, 8 ),
	                              new CodecDescriptor("G722",   8000,1, 9 ),
	                              new CodecDescriptor("L16",   44100,2, 10),
	                              new CodecDescriptor("L16",   44100,1, 11),
	                              new CodecDescriptor("QCELP",  8000,1, 12),
	                              new CodecDescriptor("MPA",   90000,2, 14),
	                              new CodecDescriptor("G728",   8000,1, 15),
	                              new CodecDescriptor("DVI4",  11025,1, 16),
	                              new CodecDescriptor("DVI4",  22050,1, 17),
	                              new CodecDescriptor("G729",   8000,1, 18)
	                            };

	private CodecDescriptorDB knownCodecs;
	private String username;

	/**
	* Build an SDPAnnounce Object from all supported codecs. The resulting SDPAnnounce object is used 
	* to announce the supported codecs on this side of the RTP communication.
	*
	* @param md_in profile Vector of CodecDescriptor objects to use for a new SDPAnnouce Object.
	**/
	public SDPAnnounce build (MediaDescriptor md_in) { 
		int i;
		SDPAnnounce sdpa=new SDPAnnounce();
		ConnectionField cf=new ConnectionField();
		ConnectionAddress ca=new ConnectionAddress();
		Host h=new Host();
		OriginField of=new OriginField();
		ProtoVersionField pvf=new ProtoVersionField();
		TimeFieldList tfl=new TimeFieldList();
		TimeField tf=new TimeField();
		SessionNameField snf=new SessionNameField();
		MediaDescriptionList mdl=new MediaDescriptionList();
		MediaDescription md=new MediaDescription();
		MediaField mf=new MediaField();
		FormatList fl=new FormatList();
		Format f;
		AttributeFields afl=new AttributeFields();
		AttributeField af;
		NameValue nv;
		String addrtype="IP4";
		InetAddress local;

		if (md_in==null) return null;

		pvf.setProtoVersion(0);
		sdpa.setProtoVersion(pvf);

		local=main.getLocalAddress();
		if (local instanceof Inet6Address) { 
			addrtype="IP6";
			h=new Host("["+local.getHostAddress()+"]",HostAddrTypes.IPV6REFERENCE);
		}
		if (local instanceof Inet4Address) { 
			addrtype="IP4";
			h=new Host(local.getHostAddress(),HostAddrTypes.IPV4ADDRESS);
		}

		of.setAddress(h);
		of.setNettype("IN"); 
		of.setAddrtype(addrtype);
		of.setUsername(username);
		of.setSessId(System.currentTimeMillis());
		of.setSessVersion(System.currentTimeMillis()+1);
		sdpa.setOriginField(of);

		snf.setSessionName("BonePhone");
		sdpa.setSessionName(snf);

		if (md_in.peer instanceof Inet6Address) {
			addrtype="IP6";
			h=new Host("["+md_in.peer.getHostAddress()+"]",HostAddrTypes.IPV6REFERENCE);
		}
		if (md_in.peer instanceof Inet4Address) {
			addrtype="IP4";
			h=new Host(md_in.peer.getHostAddress(),HostAddrTypes.IPV4ADDRESS);
		}
		ca.setTtl(md_in.ttl);
		ca.setAddress(h);
		System.out.println("ConnectionAddress: "+ca.encode());
		cf.setNettype("IN"); 
		cf.setAddrtype(addrtype);
		cf.setAddress(ca);
		System.out.println("ConnectionField: "+cf.encode());
		sdpa.setConnectionField(cf);

		tf.setStartTime(0);
		tf.setStopTime(0);
		tfl.add(tf);
		sdpa.setTimeFields(tfl);

		mf.setProto("RTP/AVP");
		mf.setPort(md_in.port);
		if (md_in.nports>1) mf.setNports(md_in.nports);
		if (md_in.Type==MediaDescriptor.AUDIO) mf.setMedia("audio");

		// Comment this "if" to avoid a= lines and to fix to a m= line with format 0
		if (md_in.Codecs!=null) {		
			for (i=0; i<md_in.Codecs.size(); i++) {
				CodecDescriptor cd=(CodecDescriptor)(md_in.Codecs.elementAt(i));
				RATtoIANA(cd);
				if (cd.pt<0) continue;
				f=new Format(""+cd.pt);
				fl.add(f);
				String rtpmap=""+cd.pt+" "+cd.shortname+"/"+cd.freq;
				if (cd.channels>1) rtpmap=rtpmap+"/"+cd.channels;
				nv=new NameValue("rtpmap", rtpmap );
				nv.setSeparator(":");
				af=new AttributeField();
				af.setAttribute(nv);
				afl.add(af);
			}
		}
		// f=new Format("0");   // uncomment these 2 line to s.a.
 		// fl.add(f);           // 
		mf.setFmt(fl);
		md.setMediaField(mf);

		// comment this to s.a.
		md.setAttributeFields(afl);

		mdl.add(md);
		sdpa.setMediaDescriptions(mdl);
		return sdpa;
	}


	private void IANAtoRAT(CodecDescriptor cd) {
		int i;
		for  (i=0; i<RATtranslate.length; i++) {
			if (cd.shortname.equalsIgnoreCase(RATtranslate[i][1])) {
				cd.shortname=RATtranslate[i][0];
				return;
			}
		}
	}


	private void RATtoIANA(CodecDescriptor cd) {
		int i;
		for  (i=0; i<RATtranslate.length; i++) {
			if (cd.shortname.equalsIgnoreCase(RATtranslate[i][0])) {
				cd.shortname=RATtranslate[i][1];
				return;
			}
		}
	}


	private CodecDescriptor convertRtpmap(String rtpmap) {
		int chan,freq,pti;
		String codecname;
		Vector Token1, Token2;

		Token1=Tokenizer.split(rtpmap," ");
		if (Token1.size()!=2) { return null; }
		Token2=Tokenizer.split((String)(Token1.elementAt(1)),"/");

		try { pti=Integer.parseInt((String)(Token1.elementAt(0))); }
		catch (Exception e) { return null; }

		if (Token2.size()<1) { return null; }
		codecname=(String)(Token2.elementAt(0));

		if (Token2.size()<2) { return new CodecDescriptor(codecname, 8000, 1, pti); }
		try { freq=Integer.parseInt((String)(Token2.elementAt(1))); }
		catch (Exception e) { return null; }
				
		if (Token2.size()<3) { return new CodecDescriptor(codecname, freq, 1, pti); }
		try { chan=Integer.parseInt((String)(Token2.elementAt(2))); }
		catch (Exception e) { return null; }

		return new CodecDescriptor (codecname, freq, chan, pti);
	}

	/** @param username username to encode in the SDP block. */
	public SDPManager (String username) { 
		this.username=username;
		knownCodecs=new CodecDescriptorDB(ianalist);
	}


	/**
	* Extract a Vector of CodecDescriptor objects from an SDPAnnounce Object.
	*
	* If there are no codecs defined in the SDPAnnouce, null is returned.
	*
	* @param sdp SDPAnnounce object to examine.
	* @return a MediaDescriptor with the found codecs, address, port as its profile member.
	**/
	public MediaDescriptor extractAllCodecs (SDPAnnounce sdp) 
	throws Exception {

		int port=-1, nports=1, ttl=127;
		String peer=null;
		Host h=null;
		MediaDescriptionList mdl;
		SDPObject sdpo_md, sdpo_af;
		GenericObject sdpo_f;
		MediaDescription md;
		MediaField mf;
		String media, proto;
		FormatList fl;
		Format f;
		String pt;
		Vector codecs;      // Vector of CodecDescriptors found in the SDP Block
		CodecDescriptor cd;
		AttributeFields afs;
		AttributeField af;
		NameValue nv;
		ConnectionField cf;
		ConnectionAddress ca;

		if (sdp==null) { throw new Exception("no sdp"); }
		mdl=sdp.getMediaDescriptions();
		codecs=new Vector(5);

		// global c= field
		cf=sdp.getConnectionField();
		if (cf==null) { throw new Exception("No global c= line in SDP block"); }
		ca=cf.getAddress();
		ttl=ca.getTtl();
		if (ttl==0) ttl=127;
		h=ca.getAddress();
		peer=h.getAddress();

		for (sdpo_md=mdl.first(); sdpo_md!=null; sdpo_md=mdl.next()) {
			if (sdpo_md instanceof MediaDescription) {
				md=(MediaDescription) sdpo_md;
				// first we need to examine the a= lines for rtpmap: definitions
				afs=md.getAttributeFields();
				if (afs!=null) {
					for (sdpo_af=afs.first(); sdpo_af!=null; sdpo_af=afs.next()) {
						if (sdpo_af instanceof AttributeField) {
							af=(AttributeField) sdpo_af;
							nv=af.getAttribute();
							if (nv.getName().equalsIgnoreCase("rtpmap")) {
								String v=(String) (nv.getValue());    // Format: {pt}{WSP}{Name[/freq[/chan]]}
								cd=convertRtpmap(v);
								if (cd==null) { throw new Exception("Unparsable a= line "+af.encode()); }
								knownCodecs.update(cd);
							}
						}
					}
				}
				// now read the listed payload types in the m= line
				mf=md.getMediaField();
				media=mf.getMedia();
				if (! media.equalsIgnoreCase("audio")) continue;
				proto=mf.getProto();
				if (! proto.equalsIgnoreCase("RTP/AVP")) continue;
				port=mf.getPort();   nports=mf.getNports();
				fl=mf.getFmt();
				for (sdpo_f=fl.first(); sdpo_f!=null; sdpo_f=fl.next()) {
					if (sdpo_f instanceof Format) {
						f=(Format) sdpo_f;
						pt=f.getFormat();
						int pti;
						try { pti=Integer.parseInt(pt); } 
						catch (Exception e) { throw new Exception("Can't parse Payloadtype "+pt); }
						cd=knownCodecs.getByPayloadType(pti);
						if (cd==null) { throw new Exception("Can't assign payload type "+pti); }
						codecs.addElement(cd);
					}
				}
				cf=md.getConnectionField();
				if (cf!=null) {
					ca=cf.getAddress();
					ttl=ca.getTtl();
					h=ca.getAddress();
					peer=h.getAddress();
				}
			} else continue;
		}		
		if (nports==0) nports=1;
		knownCodecs.dump();
		System.out.println("We found in MediaField: ");
		int i;
		for (i=0; i<codecs.size(); i++) {
			cd=(CodecDescriptor)(codecs.elementAt(i));
			IANAtoRAT(cd);
			System.out.println(""+cd.pt+": "+cd.shortname+"/"+cd.freq+"/"+cd.channels);
		}
		System.out.println("Port:"+port+" nports:"+nports+" ttl:"+ttl+" Address:"+h.getAddress());
		if (port==-1) return null;
		InetAddress peeraddr;
		try { peeraddr=InetAddress.getByName(peer); }
		catch (UnknownHostException uhe) { return null; }
		return new MediaDescriptor(MediaDescriptor.AUDIO, codecs, port, nports, peeraddr, ttl, null);
	}


	/**
	* Return the first supported CodecDescriptor object which is found in the MediaDescriptor.
	*
	* If there are no supported codes found or there are no codecs defined in the MediaDesciptor, null is returned.
	*
	* @param md MediaDescriptor object to examine
	**/
	public CodecDescriptor getFirstSupported (MediaDescriptor md, Vector profile) {
		Vector v=md.Codecs;
		int i;
		CodecDescriptor cd;

		for (i=0; i<v.size(); i++) {
			cd=(CodecDescriptor)(v.elementAt(i));
			int j;
			for (j=0; j<profile.size(); j++) {
				CodecDescriptor cd2=(CodecDescriptor)(profile.elementAt(j));
				if (cd.shortname.equalsIgnoreCase(cd2.shortname) &&
				    cd.freq==cd2.freq && cd.channels==cd2.channels ) {
					return cd;
				}
			}
		}
		return null;
	}

}




class CodecDescriptorDB {
	
	Vector db;

	public CodecDescriptorDB(CodecDescriptor[] cda) {
		int i;

		db=new Vector(cda.length);
		for (i=0; i<cda.length; i++) db.addElement(cda[i]);
	}

	public void update (CodecDescriptor cd) {
		int pt=cd.pt;
		int i;

		for (i=0; i<db.size(); i++) {
			CodecDescriptor cd2=(CodecDescriptor)(db.elementAt(i));
			if (cd2.pt==pt) { db.setElementAt(cd,i); return; }
		}
		db.addElement(cd);
	}

	public CodecDescriptor getByPayloadType(int pt) {
		int i;

		for (i=0; i<db.size(); i++) {
			CodecDescriptor cd2=(CodecDescriptor)(db.elementAt(i));
			if (cd2.pt==pt) { return cd2; }
		}
		return null;
	}

	
	public void dump() {
		int i;

		for (i=0; i<db.size(); i++) {
			CodecDescriptor cd2=(CodecDescriptor)(db.elementAt(i));
			System.out.println(""+cd2.pt+": "+cd2.shortname+"/"+cd2.freq+"/"+cd2.channels);
		}
	}

}

