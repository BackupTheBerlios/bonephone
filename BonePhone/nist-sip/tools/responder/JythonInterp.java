
/**
* A Jython interpreter to interpret the code fragments for pattern matching.
*/

package tools.responder;

import org.python.util.*;
import org.python.core.*;
import gov.nist.sip.msgparser.*;

public class JythonInterp extends PythonInterpreter {

        protected String filterSpec;
      
	public JythonInterp() {
		super();
                this.exec("from java.lang import *");
                this.exec("from java.io import *");
                this.exec("from gov.nist.sip.stack.security import *");
                this.exec("from gov.nist.sip.stack import *");
                this.exec("from gov.nist.sip.msgparser import *");
                this.exec("from gov.nist.sip.sipheaders import *");
                this.exec("from gov.nist.sip.sdpfields import *");
		this.exec("from tools.responder import *");
	}
		
        
	/**
	* Constructor -- set up global variables.
	*@param filter is the filter. It consists of a 
	* single function match(SIPMessage) that matches
	* with an incoming SIP message and returns true
	* or false.
	*/
	protected JythonInterp(String filter)
        throws PyException {
		this();
		if (filter == null) 
			throw new IllegalArgumentException("null filter");
		this.filterSpec = filter;
		System.out.println("Installing  " + filter);
                this.exec(filterSpec);
	}

	/** Evaluate a boolean expression.
	*/
	protected synchronized boolean evalBoolean(String expression) {
		try {
			this.exec("retval = " + expression);
                	PyObject pyObj = this.get("retval");
                	String retString = pyObj.toString();
			Debug.println("retString = " + retString);
                	if (retString.equals("1")) return true;
                	else return false;
		} catch (PyException ex) {
		    ex.printStackTrace();
		    System.exit(0);
		    return false;
		}
	}
       
        
        /**
         *Run this filter on the given SIP message (represented as an array
         *canonical headers) and return true or false.
         */
        protected synchronized  boolean match(SIPMessage sipMsg) {
            this.set("sipMessage",sipMsg);
            try {
                this.exec("retval = match(sipMessage)");
                PyObject pyObj = this.get("retval");
                String retString = pyObj.toString();
                if (retString.equals("1")) return true;
                else return false;
            } catch (PyException ex) {
                ex.printStackTrace();
                System.out.println("Error in filter spec. "  +  
				ex.getMessage());
                System.exit(0);
            }
            return false;
                          
        }
	
	/** Run a python method.
	*/
	protected synchronized void 
	    runMethod(String method, SIPMessage sipMsg) {
	     this.set("sipMessage",sipMsg);
	     try {
		 this.exec(method + "(sipMessage)");
	      } catch (PyException ex) {
		   ex.printStackTrace();
		   System.out.println("Error in filter spec " +
				 ex.getMessage());
	      }
	}

	

	protected  synchronized void runMethod(String method) {
	     try {
		 this.exec(method + "()");
	      } catch (PyException ex) {
		   ex.printStackTrace();
		   System.out.println("Error in filter spec " +
				 ex.getMessage());
	      }
	}
		

}
		

