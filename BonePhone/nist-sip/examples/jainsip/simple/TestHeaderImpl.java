package examples.jainsip.simple;

import gov.nist.jain.protocol.ip.sip.header.*;
import gov.nist.sip.sipheaders.*;
import gov.nist.jain.protocol.ip.sip.message.*;
import gov.nist.jain.protocol.ip.sip.address.*;
import gov.nist.jain.protocol.ip.sip.*;

import jain.protocol.ip.sip.*;

import java.io.*;

public class TestHeaderImpl{ 

     // Main 
     public static void main(String[] args) throws SipParseException{ 
        PrintStream testResults;
        String testLog="SetTestResults";
     
        try 
        {
	    testResults= new PrintStream(new FileOutputStream(testLog,true));
            
            testResults.println("TEST of the set methods for a response message :");
            ResponseImpl response=new ResponseImpl();
            response.testSetMethods(testResults);
       
            testResults.println("TEST of the set methods for a request message :");
            RequestImpl request=new RequestImpl();
            request.testSetMethods(testResults);
         
            SipURLImpl sipURL=new SipURLImpl();
            sipURL.testSetMethods(testResults);
            
            testResults.close();
        } 
        catch (IOException ex) {
	     System.err.println("Cannot open "+testLog+" for write");
        }
     }
         
}
