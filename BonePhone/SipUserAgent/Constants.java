/*
*
* $Id: Constants.java,v 1.1 2002/02/04 13:23:31 Psycho Exp $
*
* this class will explode in your face
*
*/

package SipUserAgent;

import java.util.Date;
import java.util.Random;

/**
* interface Constants
*
* @version	$Revision: 1.1 $
* @author	$Author: Psycho $
**/
public interface Constants
{
	public static String LOCAL_TAG = Integer.toHexString((new Random((new Date()).getTime())).nextInt());
}

