package bonephone;

import java.util.*;
import org.mbus.*;
import java.net.*;


class main2 {

  main2() {

	Configuration cfg;
	MediaControl med;
	PhoneBook pb;
	PortManager pm;
	CodecProfileManager cpm;
	CallCenter cac;
	PhoneController pc;

	////////////////////////////////////////////
	// get config directory name. This is supported for unix-like systems only
	// but it is easy to extend for MickeySofts WinDOS
	String configdir=System.getProperty("file.separator");
	configdir=System.getProperty("user.home")+configdir+".bonephone"+configdir;

	////////////////////////////////////////////
	// some code to display all known System properties. 
	// Properties p=System.getProperties();
	// Enumeration en=p.propertyNames();
	// while (en.hasMoreElements()) {
	//	String prop=(String)(en.nextElement());
	//	System.out.println(prop+" : "+System.getProperty(prop));
	// }
	
	////////////////////////////////////////////
	// Read the bonerc and check for required configuration keys
	String[] req = { "RATPath" , "SIPIdentity", "SIPDialDomain" };
	cfg=new Configuration(configdir+"bonerc");
	int i;  boolean b=false;
	for (i=0; i<req.length; i++) {	
		if (! cfg.exists(req[i])) {
			System.err.println("Missing required configuration key: "+req[i]);
			b=true;
		}
	}
	cfg.setUnset("UpstreamProfile","0");
	cfg.setUnset("DownstreamProfile","0");
	cfg.setUnset("OutputVolume","76");
	cfg.setUnset("InputVolume","76");
	cfg.setUnset("InputMute","0");
	cfg.setUnset("OutputMute","0");
	cfg.setUnset("InputChannel","Microphone");
	cfg.setUnset("OutputChannel","Speaker");
	cfg.setUnset("RTPFirstPort","4720");
	cfg.setUnset("RTPPorts","20");
	cfg.setUnset("RTPTTL","15");
	cfg.setUnset("IPVersion","4");
	cfg.setUnset("AutoAnswer","0");
	if (cfg.exists("SIPIdentity")) { cfg.setUnset("SIPDisplayName",cfg.getAsString("SIPIdentity")); }
	try {
		cfg.writeOut(configdir+"bonerc");
	} catch (Exception e) {
		System.out.println(e.getMessage());
		System.exit(-1);
	}
	if (b) { System.exit(-1); }

	///////////////////
	// init PortManager
	pm=new PortManager(cfg.getAsInt("RTPFirstPort"), cfg.getAsInt("RTPPorts"));


	//////////////////////////////////
	// Set preference for IPv4 or IPv6
	int ipv=cfg.getAsInt("IPVersion");

//	System.setProperty("java.net.preferIPv6Addresses","true");
//	main.getLocalAddress();
//	main.removeSystemProperty("java.net.preferIPv6Addresses");
//	System.out.println(System.getProperty("java.net.preferIPv6Addresses"));
//	main.getLocalAddress();
//	System.setProperty("java.net.preferIPv6Addresses","false");
//	main.getLocalAddress();
//	main.removeSystemProperty("java.net.preferIPv6Addresses");
//	main.getLocalAddress();

	if (ipv==6) System.setProperty("java.net.preferIPv6Addresses","true");
//	main.useIPv(ipv);
	main.getLocalAddress();


	//////////////////////////////////////////////
	// Init MediaControl to learn installed codecs
	try {
		med=new MediaControl(cfg);
	} catch (Exception e) {
		med=null;
		System.err.println(""+e.getMessage());
		System.exit(-1);
	}
	
	//////////////////////////////////////////////
	// Init CodecProfileManager with presets and profiles from "profiles" file
	cpm=new CodecProfileManager(med.getAllSupportedCodecs(), configdir+"profiles");
	cpm.setUpstreamProfile(cpm.getProfile(cfg.getAsInt("UpstreamProfile")));
	cpm.setDownstreamProfile(cpm.getProfile(cfg.getAsInt("DownstreamProfile")));

	//////////////////////////////////////////////
	// Build CallCenter which holds a Vector of Calls
	cac= new CallCenter(cfg,cpm,pm,med);   // Class to maintain all existing calls

	////////////////////////////////////////
	// Build PhoneBook from "phonebook" file
	pb=new PhoneBook(configdir+"phonebook");

	pc=new SIPPhone(cac);

	/////////////
	// Launch GUI
	GUI gui= new GUI ( pc , pb, cfg);

  } 

}




public class main {

	private static boolean ipv6available=false;
	private static boolean ipv6checked=false;


	/** Hack to remove a System Property. This is done by assembling new Properties which 
	* lack the named property. These Properties then replace the System properties.
	* @param prop name of property to remove.
	*/
	public static void removeSystemProperty (String prop) {
		Properties all=System.getProperties();
		Properties all2=new Properties();
		Enumeration names=all.propertyNames();
		while (names.hasMoreElements()) {
			String key=(String)(names.nextElement());
			if (prop.equals(key)) continue;
			String val=all.getProperty(key);
			all2.setProperty(key,val);
		}
		System.setProperties(all2);
	}



//	public static synchronized void useIPv(int x) {
//		boolean usev6=(x==6)&&checkIPv6();
//		System.setProperty("java.net.preferIPv6Addresses",""+usev6);
//		System.setProperty("java.net.preferIPv6Addresses",""+(x==6));
//		System.out.println("Using IPv"+(usev6?"6":"4"));
//		getLocalAddress();
//		return;
//	}

	//////////////////////////////////////////////////////
	// Check if an ipv6 local host address can be obtained
//	public static synchronized boolean checkIPv6() {
//		if (ipv6checked) return ipv6available;

//		String prop=System.getProperty("java.net.preferIPv6Addresses");
//		if (prop==null) prop="false";
//		System.setProperty("java.net.preferIPv6Addresses","true");
//		System.out.println("checkIPv6(): property="+System.getProperty("java.net.preferIPv6Addresses"));

//		InetAddress local=null;
//		try {
//			local=InetAddress.getLocalHost();
//		} catch (UnknownHostException e) {
//			System.err.println("Cannot determine local host address.");
//		}
//		System.setProperty("java.net.preferIPv6Addresses",prop);
//		if (local instanceof Inet6Address) ipv6available=true;
//		System.out.println("checkIPv6(): "+local.getHostAddress()+" v6="+ipv6available);
//		ipv6checked=true;
//		return ipv6available;
//	}


	private static boolean las=false;  // Local Address showed

	/** determine local host address. can be wrong if host is misconfigured or multihomed. 
	* @return local host address.
	*/
	////////////////////////////
	// detect local host address
	public static InetAddress getLocalAddress() {
		InetAddress local=null;
		try {
			local=InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.err.println("Cannot determine local host address.");
			System.exit(-1);
		}
		if (!las) { 
			System.out.println("LOCALHOST: "+local.getHostAddress());
			las=true;
		}
		return local;
	}


	public static void main (String[] args) {
		new main2();
	} 

}  
