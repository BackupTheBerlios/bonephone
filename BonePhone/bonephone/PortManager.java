package bonephone;

/** Manage usage of RTP/RTCP ports in a defined port range. */
public class PortManager {
	private boolean portbusy[];
	private int first;
	private int count;
	private int next; // index in portbusy array

	/** obtain two free consecutive ports in the defined port range. 
	 * @return RTP port to use. This port number will be even.
	*/
	public synchronized int getPort() throws Exception {
		int p;
		for (p=next; p<count; p+=2) {
			if (!portbusy[p]) {
				portbusy[p]=true;
				portbusy[p+1]=true;
				next=p+2;
				return p+first;
			}
		}
		for (p=0; p<next; p+=2) {
			if (!portbusy[p]) {
				portbusy[p]=true;
				portbusy[p+1]=true;
				next=p+2;
				return p+first;
			}
		}
		throw (new Exception("No free port between "+first+" and "+(first+count-1)));
	}
	
	/** free (release) a former obtained pair of ports. 
	 * @param port RTP port to release. This port number must be even.
	*/
	public synchronized void freePort(int port) {
		if (port>first && port<count+first) {
			int p=port-first;
			portbusy[p]=false;
			portbusy[p+1]=false;
		}
	}

	/** Defines a Portmanager for a port range.
	 * @param first First port in range.
	 * @param count Number of ports in range,
	*/
	public PortManager(int first, int count) {
		next=0;
		first=first&65534; 
		if (count<2) count=2;
		count=count&65534;
		portbusy=new boolean[count];
		int i;
		for (i=0; i<count; i++) portbusy[i]=false;
		this.first=first;
		this.count=count;
	}


}

