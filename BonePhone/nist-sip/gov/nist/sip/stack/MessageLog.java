/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov)                                    *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package gov.nist.sip.stack;

/**
 * This class stores a message along with some other informations
 * Used to log messages.
 */

class MessageLog {

    String message;

    String source;

    String destination;

    String timeStamp;

    boolean isSender;

    String cseq;

    String firstLine;

    /**
     * Constructor
     */
 
    public MessageLog(String message, String source, String destination,
		      String timeStamp, boolean isSender, String cseq, 
		      String firstLine) {
	if (message == null
	  || message.equals("")) 
		throw new IllegalArgumentException("null msg");
        this.message = message;
        this.source = source;
        this.destination = destination;
        this.timeStamp = timeStamp;
        this.isSender = isSender;
        this.cseq = cseq;
	this.firstLine = firstLine;
    }

    /**
     * Get an XML String for this message
     */
 
    public String flush() {
	String log;

	log = "<message from=\"" + source + 
	    "\" to=\"" + destination + 
	    "\" time=\"" + timeStamp + 
	    //	    "\" sender=\"" + isSender + 
	    //	    "\" Cseq=\"" + cseq + 
	    "\" firstLine=\"" + firstLine + 
	    "\">\n";
	log += "<![CDATA[";	
	log += message;
	log += "]]>\n";
	log += "</message>\n";	

	return log;
    }
    
}
