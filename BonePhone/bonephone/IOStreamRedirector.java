package bonephone;

import java.io.*;

/** Class to redirect the data read from an OutputStream to another InputStream.
 * The used Thread terminates when there is no more data available from the InputStream.*/
public class IOStreamRedirector extends Thread {

	private	InputStream is;
	private OutputStream os;


	public void run() {
		int r=0;
		while (r>=0) {
			try {
				r=is.read();
			} catch (IOException ioe) {
				try { 
					is.close();
				} catch (IOException ioe2) {}
				break;
			}
			if (r>=0) 
				try {
					os.write(r);
				} catch (IOException ioe) {
					break;
				}
		}	
	}

	public IOStreamRedirector (InputStream is, OutputStream os) {
		super();
		this.os=os;
		this.is=is;
		this.start();
	}

}
