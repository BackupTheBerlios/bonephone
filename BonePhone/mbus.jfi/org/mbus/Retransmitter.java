package org.mbus;

/**
 * The Retransmitter is activated once a reliable message was sent. 
 * After a predefined timeinterval, it resends the message until
 * the maximum number of retries is reached. Afterwards, it sends
 * a deliverFailure() to its mbus.
 * If the message was acknowledged correctly by its receiver, the
 * mbus discards the Retransmitter.
 *
 * @author Ralf Kollmann, Stefan Prelle
 * @version $Revision: 1.1 $ $Date: 2002/02/04 13:23:34 $
 */


public class Retransmitter extends Thread{
    
    private RetransmitterListener listener;
    private Message mess;
    private byte[] data;
    private Address dest;
    private int N=1;
    private int T=0;
    private final int T_k;
    private boolean stop=false;
    private MBusConfiguration config;

    //------------------------------------------------------------
    public Retransmitter(RetransmitterListener list, Address dest, byte[] data, Message mess, MBusConfiguration config){
	listener = list;
	this.mess= mess;
	this.dest= dest;
	this.data= data;
	this.config = config;
	T_k=(config.getRetries()*(config.getRetries()+1)/2)*config.getRetries();
    }
    
    //------------------------------------------------------------
    public void run(){
	if(!stop){
	    
	    for(N=1;N<=config.getRetries();N++){
		try{
		    sleep(config.getTimerR());
		} catch(Exception e) {
		    System.out.println("Retransmitter: Insomnia");
		}
		if(stop) return;
		System.out.println("Retransmitter: Send "+mess.getHeader().getSequenceNumber());
		((SimpleTransportLayer)listener).sendMessage(dest, data, mess);
	    }
	    listener.deliveryFailed(mess.getHeader().getSequenceNumber(), mess);
	    
	}
	//stop();
    }
    
    //------------------------------------------------------------
    public Message getMessage(){return mess;}
    
    //------------------------------------------------------------
    public Address getDestination(){return dest;}
    
    //------------------------------------------------------------
    public void halt(){stop=true;}

    //------------------------------------------------------------
    public int getSeqNum(){
	return mess.getHeader().getSequenceNumber();
    }
    
}
