package org.mbus;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * This class is responsible for the reception of messages from the
 * mbus. It listens permanently on a well-known multicast address.
 *
 * @author Ralf Kollmann, Stefan Prelle
 * @version $Revision: 1.1 $ $Date: 2002/02/04 13:23:34 $
 */

public class Receiver extends Thread{

    private final static boolean DEBUG = false;
    private final static boolean DEBUG2 = false;

	private boolean dodump=false;    
    private ReceiverListener list;
    /** Addresses the Receiver listens to */
    private Vector        myAddresses;
    private Crypter       crypter;
    private Authenticator authenticator;
    
    private DatagramSocket socket; 
    private boolean ever=true;

    private byte[]  msg;
    private boolean wiretapping;

    // For unicast-optimization
    private Vector knownAddresses;

    //------------------------------------------------------------
    /**
     * Create a new receiver that listens to one or more 
     * addresses/interfaces.
     *
     * @param rlist Listener that should be informed about incoming
     *              or undeliverable messages
     * @param interfaces    List of addresses the Receiver should 
     *                      listen to
     * @param ia            IP-Address of the used Multicast-Group
     * @param port          Portnumber to listen at
     * @param crypter       Class that does encryption.
     * @param authenticator Class that does authentication.
     */
    public Receiver(ReceiverListener rlist, Address[] interfaces, 
		            DatagramSocket sock, Authenticator authenticator, Crypter crypter){
		super("Receiver");
	
	this.list=rlist;
	this.wiretapping = false;
	myAddresses=new Vector();
  	this.authenticator=authenticator;
  	this.crypter=crypter;
	
	socket=sock;
	for (int i=0; i<interfaces.length; i++)
	    addInterface(interfaces[i]);

	knownAddresses = new Vector();
   }

    //---------------------------------------------------------------
    private void log(String mess) {
	if (DEBUG) {
	    System.out.println(System.currentTimeMillis()+" Receiver: "+mess);
	}
    }

    //------------------------------------------------------------
    /**
     * Enable or disable the Wiretapping-mode of the receiver.
     *
     * @param wiretapping Wiretapping-mode on?
     */
    public void setWiretapping(boolean wiretapping) {
		this.wiretapping = wiretapping;
    }

    //------------------------------------------------------------
    /**
     * Enable or disable the dump-mode of the receiver.
     *
     * @param dodump dump-mode on?
     */
	public void setDump(boolean dodump) {
		this.dodump = dodump;
	}

    //------------------------------------------------------------
    public void addInterface(Address addr) {
		myAddresses.addElement(addr);
    }

    //------------------------------------------------------------
    public void removeInterface(Address addr) {
	for (int i=0; i<myAddresses.size(); i++) {
	    Address ad = (Address)myAddresses.elementAt(i);
	    if (ad.equals(addr)) {
		myAddresses.removeElement(ad);
		return;
	    }
	}
    }
    
    //------------------------------------------------------------
    public void halt(){
		ever=false;
	}

    //------------------------------------------------------------
    private boolean doWeListenTo(Address addr) {
	if (wiretapping) {
	    return true;
	}
	for (int i=0; i<myAddresses.size(); i++) {
	    Address ad = (Address)myAddresses.elementAt(i);
	    if (ad.matches(addr)) {
		return true;
	    }
	}
	return false;
    }

    //------------------------------------------------------------
    private boolean passesEchoCompensation(Address addr) {
		for (int i=0; i<myAddresses.size(); i++) {
		    Address ad = (Address)myAddresses.elementAt(i);
	    	if (ad.matches(addr)) {
				return false;
		    }
		}
		return true;
    }
    

    //------------------------------------------------------------
    /**
     * Find all address that match a given address.
     *
     * @param addr Address to check
     * @return List of address
     */
    private Address[] getMatchingTargets(Address addr) {
	Vector results = new Vector();
	Enumeration e = myAddresses.elements();
	while (e.hasMoreElements()) {
	    Address k = (Address)e.nextElement();
	    if (k.matches(addr)) 
		results.addElement(k);
	}
	
	Address[] ret = new Address[results.size()];
	for (int i=0; i<results.size(); i++)
	    ret[i] = (Address)results.elementAt(i);
	if (DEBUG2) System.out.println(ret.length);
	return ret;
    }
	
    //------------------------------------------------------------
    private void acknowledge(int[] ackList, Address src, Address dest) {
	list.sendACKs(src, dest, ackList);
    }
    
    //------------------------------------------------------------
    public void run(){
	byte[] buffer= new byte[4096];
	byte[] buf   = new byte[0];
	DatagramPacket dp = null;

	for(;ever;){
	    String all = null;
	    int length = 0;
	    try{
			Arrays.fill(buffer, (byte)0);
			dp = new DatagramPacket(buffer, buffer.length);
//			System.err.println("entering socket.receive()");
			socket.receive(dp);
			length = dp.getLength();
			if (dodump) {
				System.out.println("Received ("+length+"):");
				System.out.println(HexDumpMaker.makeHexDump(dp.getData(),length));
			}
			buf = new byte[dp.getLength()];
			System.arraycopy(buffer,0,buf,0,dp.getLength());

			all = new String(crypter.decrypt(buf));
//			if ((length%8)>0) {
//		    	log("----> Datagram has invalid length!\n"+all);
//			    break;
//			}
//  		int lastI = all.lastIndexOf((char)0);
//  		System.out.println(lastI+" / "+all.length()+" / "+(int)all.charAt(all.length()-1));
		

			// Prepare Digest-checking
			int trenn = all.indexOf('\n');
			if (trenn!=16) 
		    	throw new NoSuchElementException("Invalid digest-length: "+trenn);
			String digest = all.substring(0, trenn);
			String body   = all.substring(trenn+1, all.length());
			if (DEBUG && body.indexOf("mbus.hello")<0)
		    	System.out.println("1-----------------\nReceiver.run: "+body);

			Message m=new Message(all);
			if (DEBUG && body.indexOf("mbus.hello")<0)
			    System.out.println("2-----------------\nReceiver.run: Message = \n"+m+"\n3---------------------");
		
			Header header = m.getHeader();
			// echo compensation
			if (passesEchoCompensation(header.getSourceAddress())) {
		    	if (DEBUG && body.indexOf("mbus.hello")<0)
					System.out.println("4-----------------\n");
			    // Do we want to handle only the message for us
			    // or every received message (wiretapping)
			    Address destination = header.getDestinationAddress();
		    	Address source      = header.getSourceAddress();
			    if (DEBUG && body.indexOf("mbus.hello")<0) {
					System.out.println("Dest: "+destination);
					System.out.println("Src.: "+source);
		    	}
			    if (doWeListenTo(destination)) {
					if (DEBUG && body.indexOf("mbus.hello")<0)
					    System.out.println("5-----------------\nReceiver.run: HABEN WILL");

					// Check the digest
					if (authenticator.checkDigest(digest, body)) {
				    	log("Received from "+dp.getAddress()+": "+m);
					    if (DEBUG && body.indexOf("mbus.hello")<0)
							System.out.println("6-----------------\nReceiver.run: RECEIVED");
					    list.logIncoming(m, dp.getAddress(), dp.getPort());
					    // remove ACKs from queue
					    list.removeACKs(header.getAckList(), m, true);

				    	// For reliable messages send acknowledgements
				    	if(m.isReliable()){
							int[] i={header.getSequenceNumber()};
							// We should only answer to reliable messages if we are not wiretapping
							if (!wiretapping)
								acknowledge(i, destination, source);
				    	}
				    	// Handle incoming message
					    list.incomingMessage(m, destination);
					} else {
						// System.out.println("checkDigest() failed,"); 
					    list.logGeneral("Authentication Failure. Sender was "+source);
					    log("Message for us thrown away");
			    		if (DEBUG){
							System.out.println("["+all+"]");
							System.out.println("["+(new String(buf))+"]");
							System.out.println(HexDumpMaker.makeHexDump(body.getBytes()));
							System.out.println("--------------------------------------------\n");
					    }
					}
			
		    	} else {
					// System.out.println("doWeListenTo() failed,"); 
				} // doWeListenTo
			} else { 
				// System.err.println("Echo compensation failed."); 
			} // Echo Compensation	
	    
	    } catch(ArrayIndexOutOfBoundsException aie){
			if (true) {
		    	System.out.println("Decrypt2-Error receiving from "+dp.getAddress()+", port "+dp.getPort());
			    System.out.println(all);
			    aie.printStackTrace();
			}
	    } catch(StringIndexOutOfBoundsException sie){
			if (true) {
			    if (false) 
					System.out.println("---------------------------------------");
			    System.out.println("Decrypt-Error receiving from "+dp.getAddress()+", port "+dp.getPort());
		    	if (false) {
					System.out.println("Received :\n"+HexDumpMaker.makeHexDump(buf));
					byte[] tmp = all.getBytes();
					System.out.println("Decrypted:\n"+HexDumpMaker.makeHexDump(tmp));
		    	}
			}
	    } catch(NoSuchElementException nse) {
			if (true) {
			    list.logGeneral("Decode error. Received from "+dp.getAddress()+", port "+dp.getPort());
			    if (false) 
					System.out.println("---------------------------------------");
			    System.out.println("Decode-Error receiving from "+dp.getAddress()+", port "+dp.getPort());
		    	if (true) {
					nse.printStackTrace();
					System.out.println("Received :\n"+HexDumpMaker.makeHexDump(buf));
					byte[] tmp = all.getBytes();
					System.out.println("Decrypted:\n"+HexDumpMaker.makeHexDump(tmp));
		    	}
			}
	    } catch(Exception e) {
			System.out.println(all);
			e.printStackTrace();
//  		System.out.println("Receiver: Error while receiving.");
//  		System.out.println(e.toString());
	    } // try
	    
	} // for

	System.err.println("THREAD TERMINATING !!! (ever="+ever+")");
    }
}

