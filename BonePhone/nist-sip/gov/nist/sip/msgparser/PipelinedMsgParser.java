/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
 * See ../../../../doc/uncopyright.html for conditions of use                   *
 * Author: M. Ranganathan (mranga@nist.gov)                                     *
 * Questions/Comments: nist-sip-dev@antd.nist.gov                               *
 *******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.net.*;
import gov.nist.sip.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.sdpfields.*;
import java.util.zip.ZipInputStream;
import java.util.zip.GZIPInputStream;
import java.io.*;
import antlr.*;


/**
 * This implements a pipelined message parser suitable for use
 * with a stream - oriented input such as TCP. The client uses
 * this class by instatiating with an input stream from which
 * input is read and fed to a message parser.
 * It keeps reading from the input stream and process messages in a
 * never ending interpreter loop. The message listener interface gets called
 * for processing messages or for processing errors. The payload specified
 * by the content-length header is read directly from the input stream.
 * This can be accessed from the SIPMessage using the getContent and
 * getContentBytes methods provided by the SIPMessage class. If SDP parsing
 * is enabled using the parseContent method, then the SDP body is also parsed
 * and can be accessed from the message using the getSDPAnnounce method.
 *
 * @see  SIPMessageListener
 * @author <A href=mailto:mranga@nist.gov > M. Ranganathan  </A>
 */

public final class PipelinedMsgParser extends
MsgParser implements Runnable, SIPErrorCodes   {
    
   /** A filter to read the input */
    class MyFilterInputStream extends FilterInputStream {
        public MyFilterInputStream(InputStream in) { super(in); }
    }
    
/** The message listener that is registered with this parser.
 * (The message listener has methods that can process correct
 * and erroneous messages.)
 */
    protected  SIPMessageListener msg_handler;
    private	PipedOutputStream pout = null;
    private      Thread mythread; // Preprocessor thread
    private byte[]  messageBody;
    private int   notifyCount; // For debugging.
    private ParserThread parserThread;
    private boolean toNotify;
    private boolean errorFlag;
    
  /**
   * default constructor.
   */
    protected PipelinedMsgParser() {
        trackInput = true;
        debugFlag = 0;
        parseMessageContent = false;
    }
    
    
    
/** Constructor when we are given a message listener and an input stream
 * (could be a TCP connection or a file)
 * @param mhandler Message listener which has methods that  get called
 * back from the parser when a parse is complete
 * @param in Input stream from which to read the input.
 * @param debug Enable/disable tracing or lexical analyser switch.
 */
    public PipelinedMsgParser
    (SIPMessageListener mhandler, InputStream in, boolean debug ) {
        msg_handler = mhandler;
        trackInput = true;
        debugFlag = 0;
        input_stream = in;
        parseMessageContent = false;
        if (debug) super.setDebugFlag();
        mythread = new Thread(this);
        mythread.setName("PipelineThread");
    }
    
  /**
   * This is the constructor for the pipelined parser.
   * @param mhandler a SIPMessageListener implementation that
   *	provides the message handlers to
   * 	handle correctly and incorrectly parsed messages.
   * @param in An input stream to read messages from.
   */
    
    public PipelinedMsgParser (SIPMessageListener mhandler, InputStream in ) {
        this(mhandler,in,false);
    }
    
  /**
   * This is the constructor for the pipelined parser.
   * @param in An input stream to read messages from.
   */
    
    public PipelinedMsgParser( InputStream in) {
        this(null,in,false);
    }
    
   /**
    * Start reading and processing input.
    */
    public void processInput() {
        mythread.start();
    }
    
    
  /**
   * Create a new pipelined parser from an existing one.
   * @return A new pipelined parser that reads from the same input
   * stream.
   */
    protected Object clone() {
        PipelinedMsgParser p = new PipelinedMsgParser();
        p.debugFlag = this.debugFlag;
        p.input_stream = this.input_stream;
        p.parseMessageContent = this.parseMessageContent;
        p.trackInput = this.trackInput;
        p.msg_handler = this.msg_handler;
        Thread mythread = new Thread(p);
        mythread.setName("PipelineThread");
        return p;
    }
    
    
  /**
   * Handle a parse exception.
   * @param ex SIPParseException to handle.
   * @throws SIPParseException Throw a parse exception if you want to pass the
   * error back to the caller.
   */
    protected  void
    handleParseException ( SIPParseException ex)
    throws SIPParseException {
        try {
            if (msg_handler != null) {
                msg_handler.handleException(ex);
            } else {
                super.handleParseException(ex);
            }
        } catch (SIPParseException e) {
            // Skip over the content length header.
            this.sendNotify ();
	    this.errorFlag = true;
        } catch (Exception e) {
	    this.sendNotify();
	    Debug.Abort(e);
	}
    }
    
  /**
   * Add a class that implements a SIPMessageListener interface whose
   * methods get called * on successful parse and error conditons.
   * @param mlistener a SIPMessageListener
   *	implementation that can react to correct and incorrect
   * 	pars.
   */
    
    public void setMessageListener( SIPMessageListener mlistener) {
        msg_handler = mlistener;
    }
    
  /**
   * Process the message.
   * @param sip_msg The callback that gets called when a correct message
   * has been processed. (This is the default action).
   */
    protected void processMessage(SIPMessage sip_msg) {
        Debug.println("processing message");
        if (msg_handler != null) {
            msg_handler.processMessage(sip_msg);
        }
        resetLineCounter();
    }
    
  /** Called from the parser when the message has been completely parsed.
   * @param sipMessage The parsed SIP Message.
   */
    protected void messageDone(SIPMessage sipMessage ) {
        Debug.println("Message done!");
        if ( parseMessageContent
        && sipMessage.contentTypeHeader != null
        && sipMessage.contentTypeHeader.compareMediaRange
        (MimeTypes.application_sdp) == 0 ){
            StringMsgParser smp = new StringMsgParser();
            smp.setParseExceptionListener(this.msg_handler);
            smp.setEOLLexer("sdpLexer");
            smp.setParseExceptionListener(msg_handler);
            try {
                sipMessage.sdpAnnounce = smp.parseSDPAnnounce
                (sipMessage.getMessageContent());
            } catch (SIPParseException ex) {
                sipMessage.sdpAnnounce = null;
            } catch (UnsupportedEncodingException ex) {
                sipMessage.sdpAnnounce = null;
            }
        }
        
        if ( ! errorFlag) {
		processMessage(sipMessage);
	}
	errorFlag = false;
    }
    
    /** Set the content length.
     */
    protected synchronized void setContentLength( int length) {
        // Note that this method is synchronized because of interaction with
        // the parser thread (we cannot know how much content to read before
        // we know whether there exists a contentLength header.
        Debug.println("Content length has been set " + length);
        this.contentLength = length;
        this.notify();
    }
    
  /**
   * Wait till the parser thread has consumed message and notifies us.
   */
    private synchronized boolean waitForNotify() {
        if (Debug.debug) {
            Debug.println(
            Thread.currentThread().getName()
            + "Wait for notify " + notifyCount );
            // new Exception().printStackTrace();
        }
        try {
            this.wait();
            // Hack alert
        } catch (InterruptedException ex) {
            Debug.println("Interrupted!!");
            return false;
        }
        notifyCount --;
        Debug.println(Thread.currentThread().getName()
        + " Got Notify" + notifyCount );
        return true;
    }
    
    /** Write to the parser thread and maybe wait for some notification
     */
    private synchronized boolean writeAndWait(String line) {
        // this.toNotify = true;
        byte[] bytes = line.getBytes();
        currentHeader = line;
        try {
            pout.write(bytes,0,bytes.length);
            pout.flush();
        } catch (IOException ex) {
            InternalError.handleException(ex);
        }
        
        if( this.toNotify) {
            try {
                
                Debug.println("writeAndWait: waiting ");
                this.wait();
                // Hack alert
            } catch (InterruptedException ex) {
                Debug.println("Interrupted!!");
                return false;
            }
            notifyCount --;
            Debug.println(Thread.currentThread().getName()
            + " Got Notify" + notifyCount );
        }
        return true;
        
    }
    
    // Do an unsynchronized write.
    private void write(String line) {
        this.toNotify = false;
        byte[] bytes = line.getBytes();
        currentHeader = line;
        try {
            pout.write(bytes,0,bytes.length);
            pout.flush();
        } catch (IOException ex) {
            InternalError.handleException(ex);
        }
    }
    
    protected synchronized void sendNotify() {
        if (this.toNotify) {
            this.notify();
            notifyCount ++;
            if (Debug.debug) {
                Debug.println(Thread.currentThread().getName() +
                " sendNotify " + notifyCount);
                // new Exception().printStackTrace();
            }
        }
        
    }
    
    
    /** Get the body as an array of bytes.
     */
    protected synchronized  byte[] getBodyAsBytes() {
        Debug.println( Thread.currentThread().getName() +
        " Get body as bytes! ");
        if (this.contentLength == 0) {
            Debug.println("getting null body");
            return null;
        } else {
            this.contentLength = 0;
            Debug.println(" Got body as bytes!");
            return this.messageBody;
        }
    }
    
    /** Set the body as an array of bytes.
     */
    private void setBodyAsBytes(byte[] body) {
        Debug.println
        (Thread.currentThread().getName() + " Set body as bytes ");
        this.messageBody = body;
    }
    
    
   /** read a line of input (I cannot use buffered reader because we
    *may need to switch encodings mid-stream!
    */
    private String readLine(FilterInputStream inputStream )
    throws IOException {
        StringBuffer retval = new StringBuffer("");
        while(true) {
            try {
                char ch;
                int i  = inputStream.read();
                if (i == -1) break;
                else  ch = (char) i;
                if (ch != '\r'   ) retval.append(ch);
                if (ch == '\n') {
                    break;
                }
            } catch (IOException ex) {
                throw ex;
            }
        }
        return retval.toString();
    }
    
    /** Read to the next break (CRLFCRLF sequence)
     */
    private String readToBreak (FilterInputStream inputStream)
    throws IOException {
        StringBuffer retval = new StringBuffer("");
        boolean flag = false;
        while(true) {
            try {
                char ch;
                int i = inputStream.read();
                if (i == -1) break;
                else ch = (char)i;
                if (ch != '\r') retval.append(ch);
                if (ch == '\n') {
                    if ( flag) break;
                    else flag = true;
                }
            } catch (IOException ex) {
                throw ex;
            }
            
        }
        return retval.toString();        
    }
    
    
    
    
  /**
   * This is input reading thread for the pipelined parser.
   * You feed it input through the input stream (see the constructor)
   * and it calls back an event listener interface for message processing or
   * error.
   * It cleans up the input - dealing with things like line continuation and
   * feeds the cleaned up input to another thread for actual parsing.
   *TODO -- add support for limiting the content length.
   */
    
    public  void run( ) {
        // open a simple stream to the input
        // THe platform may have some other default but sip messages are
        // always Text-UTF8
        
        MyFilterInputStream inputStream = null;
        inputStream =
        new MyFilterInputStream(this.input_stream);
        // I cannot use buffered reader here because we may need to switch
        // encodings to read the message body.
        
        try {
            pout = new PipedOutputStream();
        } catch (Exception ex) {
	    InternalError.handleException(ex);
        }
        parserThread =  new ParserThread(this,pout);
        parserThread.setName("parserThread");
        parserThread.start();
        outer:
            while( true) {
                
                Debug.println("Starting parse!");
                
                
                String line1;
                String line2 = null;
                this.handleContinuations = true;
                
                // ignore blank lines.
                while( true) {
                    try {
                        line1 = readLine(inputStream);
                        if (line1.equals("\n")) {
                            Debug.println("Discarding " + line1);
                            // writeAndWait(line1);
                            continue;
                        }
                        else break;
                    } catch (IOException ex) {
                        try {
                            pout.close();
                            return;
                        } catch (IOException e) {
                            return;
                        }
                        
                    }
                }
                
                
                while (true) {
                    
                    try {
                        line2 = readLine(inputStream);
                        
                        if (line2.trim().equals("")) {
                            line2 = "\n";
                        }
                    } catch (  IOException ex) {
                        try {
                            pout.close();
                            return;
                        } catch (IOException e) {
                            return;
                        }
                        
                        
                    }
                    // Read and process input a line at a time,
                    // checking for continuation.
                    // need the carriage return to be preserved.
                    // line2 += "\n";
                    // Handling continuations.
                    if (line2.charAt(0) == ' '
                    || line2.charAt(0) == '\t') {
                        line1 = line1.trim();
                        // Ignore the first blank or tab.
                        line1 = line1 + line2.substring(1);
                        continue;
                    } else {
                        // Not a continuation line.
                        // Line 1 is the previous line2 is
                        // the current line.
                        
                        currentHeader = line1;
                        
                        // Did not read a blank line.
                        if ( ! line2.equals("\n") ) {
                            // Buffer the current line
                            if (! writeAndWait(line1)) {
                                break outer;
                            }
                            line1 = line2;
                        }  else {
                            // blank line read.
                            // Seen the end of the message.
                            // Process the body.
                            line1 = line1 + "\n\n\n";
                            
                            write(line1);
                            
                            synchronized(this) {
                                try {
                                    Debug.println("Waiting for content Length");
                                    this.wait();
                                } catch (InterruptedException ex)  {
                                    break outer;
                                }
                                if (this.contentLength == 0) {
                                    // seen a blank line
                                    line1 = "";// seen a blank line
                                    Debug.println
                                    ("content length " + contentLength);
                                    this.messageBody = null;
                                    break;
                                } else  { // deal with the message body.
                                    Debug.println("contentLenth " +
                                    contentLength);
                                    byte[] message_body =
                                    new byte[this.contentLength];
                                    int nread = 0;
                                    
                                    while (nread < this.contentLength) {
                                        this.contentEncoding = null;
                                        try {
                                            int readlength =
                                            inputStream.read
                                            (message_body,nread,
                                            this.contentLength - nread);
                                            if (readlength > 0 ) {
                                                nread += readlength;
                                                Debug.println("read = " +
                                                nread);
                                            } else break;
                                        } catch (IOException ex) {
                                            break;
                                        }
                                    }
                                    
                                    setBodyAsBytes(message_body);
                                    
                                    break;
                                    
                                }
                            }
                        }
                    }
                    
                }
            }
            try {
                pout.close();
            } catch (IOException ex) {
                return;
            }
    }
    
    
  /**
   * This is the thread that gets the pre-processed input from
   * the pre-processor thread and feeds this processed input to
   * the parser.
   */
    
    private  class ParserThread extends Thread  {
        PipelinedMsgParser parserMain;
        
        ParserThread( PipelinedMsgParser pmain,
        PipedOutputStream pout ) {
            parserMain = pmain;
            PipedInputStream input = null;
            // create ourselves an input reader
            try  {
                input = new PipedInputStream(pout);
            } catch ( Exception ex) {
                InternalError.handleException(ex);
                System.exit(0);
            }
            parserMain.initParser(input);
        }
        
/** Run method for the pipelined parser.
 */
        public void run() {
            while(true) {
                parserMain.sip_messageparser
                = new sip_messageParser(selector);
                parserMain.sip_messageparser.parserMain
                = parserMain;
                try {
                    parserMain.select
                    ("method_keywordLexer");
                    parserMain.setEOLLexer
                    ("command_keywordLexer");
                    SIPMessage sip_msg = parserMain.
                    sip_messageparser.sip_message();
                    // Wait thill the message has been fully processed.
                    /**
                     * synchronized(this) {
                     *
                     * try{
                     * this.wait();
                     * } catch (InterruptedException ex) {
                     * continue;
                     * }
                     * }
                     **/
                    
                } catch (antlr.ANTLRException ex) {
                    if(debugFlag  == 1)
                        System.err.println
                        ("exception: "+ex);
                    if (debugFlag == 1)
                        ex.printStackTrace(System.err);
                    if (msg_handler != null) {
                        try {
                            if (Debug.debug) ex.printStackTrace();
                            msg_handler.handleException(
                            new SIPIllegalMessageException
                            (ex.getMessage()));
                        } catch ( SIPParseException e) {
                            if (debugFlag == 1)  {
                                System.out.println
                                ("CAUGHT EXCEPTION!");
                                e.printStackTrace();
                            }
                            // Ignore the exception.
                        }
                    }
                } catch (Exception e) {
                    if (Debug.debug)
                        InternalError.handleException(e);
                    else return;
                }
            }
        }
    }
    
   /**
    * enable the debug flag in the superclass (which is protected).
    * This is for debugging purposes only (enables elaborate stack
    * dumps on error conditions).
    */
    public void enableDebugFlag() {
        Debug.debug = true;
        super.setDebugFlag();
    }
    
}
