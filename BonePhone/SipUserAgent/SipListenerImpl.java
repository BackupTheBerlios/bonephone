/*
*
* $Id: SipListenerImpl.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;


import java.util.*;
import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.address.*;
import jain.protocol.ip.sip.header.*;
import jain.protocol.ip.sip.message.*;

/**
* class SipListenerImpl
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
public abstract class SipListenerImpl extends JainSipEnv implements SipListener
{
	protected static final Random randomGenerator = new Random((new Date()).getTime());
	protected static final String LOCAL_TAG = Integer.toHexString(randomGenerator.nextInt());
	protected static final Hashtable transactionListeners = new Hashtable();
	
    /**
     * Processes a Response received on one of the SipListener's ListeningPoints.
     * @param <var>responseReceivedEvent</var> SipEvent received because Response was received
     */
    public void processResponse(SipEvent responseReceivedEvent)
    {
    }
    
    /**
     * Processes the time out of a transaction specified by
     * the transactionId.
     * @param <var>transactionTimeOutEvent</var> SipEvent received because transaction timed out
     */
    public void processTimeOut(SipEvent transactionTimeOutEvent)
    {
    }
    
    /**
     * Processes a Request received on one of the SipListener's ListeningPoints.
     * @param <var>requestReceivedEvent</var> SipEvent received because Request was received
     */
    public void processRequest(SipEvent requestReceivedEvent)
    {
    }
}

