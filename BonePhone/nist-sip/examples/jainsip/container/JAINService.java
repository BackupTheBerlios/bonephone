package examples.jainsip.container;
import jain.protocol.ip.sip.*; 
import jain.protocol.ip.sip.address.*; 
import jain.protocol.ip.sip.header.*; 
import jain.protocol.ip.sip.message.*; 
/**
* A very simple container to build JAIN services.
* A service is built by extending this class.
*/

public abstract class JAINService implements SipListener {
	private   SipProvider sipProvider;
	protected ListeningPoint listeningPoint;
	protected SipStack 	sipStack;
	
	/** 
        * We are running in our own thread and rely on our engine to
        * set our sipProvider. 
        */
	protected 
	   void setSipProvider(SipProvider provider) {
		if (provider == null) 
			throw new IllegalArgumentException("null Arg!");
		this.sipProvider = provider;
		
	}

	protected void setListeningPoint( ListeningPoint lpoint) {
		listeningPoint = lpoint;
	}

	protected SipProvider getSipProvider() { 
		return sipProvider;
	}

	protected ListeningPoint getListeningPoint() { return listeningPoint; }

	protected void setSipStack(SipStack stack) { sipStack = stack; }

	protected SipStack getSipStack()  { return sipStack; }
}
