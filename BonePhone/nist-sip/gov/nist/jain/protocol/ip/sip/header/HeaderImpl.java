/***************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)     *
* See ../../../../../../../../doc/uncopyright.html for conditions of use   *
* Creator: M. Ranganathan (mranga@nist.gov)                                *
* Modifications:							   *
*	Olivier Deruelle -- added self testing code			   *
* 	Major Fixes were made because of bugs reported 			   *
* 	by Chris Mills of Nortel Networks.  				   *
* Questions/Comments: nist-sip-dev@antd.nist.gov                           *
****************************************************************************/

package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;
import gov.nist.jain.protocol.ip.sip.address.*;
import gov.nist.log.*;

import java.util.*;
import java.lang.reflect.*;
import java.lang.*;
import java.net.*;
import java.text.*;
import java.io.*;

/**
* Implementation of the Header interface of jain-sip.
*/
public  class HeaderImpl 
implements Header, NistSIPHeaderMapping {
        
    String 	headerName;
    SIPHeader sipHeader;
    String value;
    

    /** Parse a name value pair for this Header. Call this after you set
    * both name and value.
    */
    private void parse()
    throws SIPParseException
    {
          StringMsgParser smp = new StringMsgParser();
          Class cl = 
	     HeaderMap.getNISTHeaderClassFromJAINHeader(headerName);
	   try {
	       this.sipHeader= (SIPHeader) smp.parseSIPHeader
					(headerName + ":" + value);
               if (cl != null && ! cl.equals(sipHeader.getClass()))
		   throw new SIPParseException(headerName + ":" + value);
	       cl = HeaderMap.getJAINHeaderClassFromNISTHeader
		   (sipHeader.getClass());
	       if (cl != null && !cl.equals(this.getClass()))
		   throw new SIPParseException(headerName + ":" + value);
	   } catch (SIPParseException ex) {
	        if (cl == null) {
		   // Mapping does not exist .. this must be an extension.
		   ExtensionHeader hdr = new ExtensionHeader(headerName);
		   hdr.setInputText(headerName + ":" + value);
		   hdr.setValue(value);
		   sipHeader = hdr;
		} else throw ex;
	  }
    }

    public HeaderImpl()
    {
	sipHeader = HeaderMap.getNISTHeaderFromJAINHeader(this.getClass());
    }
        
   

    public HeaderImpl(String name, String value)
    throws IllegalArgumentException, SipParseException
    {
	if (LogWriter.needsLogging()) 
	    LogWriter.logMessage("HeaderImpl() name =" + name + " value = " +
				value);
	if (name == null || value == null) 
		throw new IllegalArgumentException("null arg!");

/**
        if(HeaderMap.getNISTHeaderClassFromJAINHeader(name) != null) {
              throw new SipParseException
		("Cannot use generic Header object to represent a " 
			+ name + "header");
        }
**/
    	setName(name);
        setValue(value);

    }

    public HeaderImpl(SIPHeader hdr) {
	Class hdrClass = 
		HeaderMap.getNISTHeaderClassFromJAINHeader(this.getClass());
	if (!hdrClass.equals(hdr.getClass())) {
		throw new IllegalArgumentException("Class mismatch!");
	}
	sipHeader = hdr;
	this.headerName = hdr.getHeaderName();
	this.value = hdr.getHeaderValue();
    }

    
    public void setName(String name) 
    throws IllegalArgumentException, SipParseException
    {
       if (LogWriter.needsLogging())  
       	   LogWriter.logMessage("setName() "  + ":" + name);
        
    	if(name == null) 
		throw new IllegalArgumentException("Name cannot be null");
        else if(name.length() == 0) 
            throw new SipParseException("Name cannot be zero-length");
	headerName = name;
	if (this.value != null) {
                    try {
			       this.parse();  
	                }
                    catch (ClassCastException ex) 
                        {
        	            throw new SipParseException(name);
	                }
                    catch (SIPParseException ex) 
                        {
		            throw new SipParseException
				(headerName + ":" + value,"unparsable");
	                }
	}
	
    }

    
    public String getName()
    {
       
    	return headerName;
    }

    
    public void setValue(String value)
    throws IllegalArgumentException, SipParseException
    {
    	if(value == null)
        	throw new IllegalArgumentException("Value cannot be null");
        else if ( value.length() == 0)
                    throw new SipParseException("Value cannot be zero-length");
        else {
		if (LogWriter.needsLogging(LogWriter.TRACE_DEBUG))
		    LogWriter.logMessage(LogWriter.TRACE_DEBUG,
			"setValue " + value);
                    this.value=value;
                    try {
			    if ( headerName != null ) {
			       this.parse();  
			    }
	                }
                    catch (ClassCastException ex) 
                        {
        	            throw new SipParseException(value);
	                }
                    catch (SIPParseException ex) 
                        {
		            throw new SipParseException
				(headerName + ":" + value,"unparsable");
	                }
         }
    }

    
    public String getValue() {
       if (LogWriter.needsLogging())  
       	   LogWriter.logMessage("getValue() "  + ":" + this.value +
			" myClass = " + this.getClass().getName());
       if (this.value == null && this.sipHeader != null)  {
		this.value =  this.sipHeader.getHeaderValue();
		return this.value;
       } else return this.value;
    }

    
    public Object clone()  {
        LogWriter.logMessage(LogWriter.TRACE_DEBUG,"clone()");
	
	Class myClass = this.getClass();
	HeaderImpl hi;
	try {
            hi = (HeaderImpl) myClass.newInstance();
	} catch (InstantiationException ex ) {
		return null;
	} catch (IllegalAccessException ex ) {
		return null;
	}
	if (this.sipHeader != null) {
		hi.sipHeader = (SIPHeader) this.sipHeader.clone();
	}
	hi.value = this.value;
	hi.headerName = this.headerName;
        return hi;
    }

    
    public String toString() {
	if (sipHeader == null && this.value == null ) return null;
	else if (sipHeader != null) return  sipHeader.encode();
	else return this.headerName + ":" + this.value;
    }

    
    /** Equality testing predicate.
    */
    public boolean equals(Object obj) {
	 if (LogWriter.needsLogging())  {
	     LogWriter.logMessage ("equals this  =" + this );
	     LogWriter.logMessage ("equals other =" + obj );
	 }
	 if (obj == this) return true;
	 else if (obj == null) return false;
	 else if (! obj.getClass().equals(this.getClass())) return false;
	 HeaderImpl that = (HeaderImpl) obj;
	 String myName = this.getName();
	 String hisName = that.getName();
	 if (LogWriter.needsLogging())  {
	     LogWriter.logMessage("myName/hisName =" + myName + "/" + hisName );
	 }
	 if (myName == null && hisName != null ||
	     myName != null && hisName == null ||
	     myName.compareToIgnoreCase(hisName) != 0 ) return false;
	 
	 SIPHeader myHeader = this.getImplementationObject();
	 SIPHeader hisHeader = that.getImplementationObject();
	 if (myHeader != null && hisHeader != null) {
		return myHeader.equals(hisHeader);
	 }
	 String myValue = this.getValue();
	 String hisValue = that.getValue();
	 if (myValue == hisValue ) return true;
	 if (LogWriter.needsLogging())  {
	     LogWriter.logMessage("myValue/hisValue =" 
			+ myValue + "/" + hisValue );
	 }

	 if ( myValue != null && hisValue    == null     
	      || myValue == null && hisValue != null   
	      || myValue.compareTo(hisValue) != 0   ) return false;
	 return true;
    }
    
	
    /**
      *Set the NIST-SIP implementation object.
      */
    public void setImplementationObject(SIPHeader sipHeader) {
       this.sipHeader = sipHeader;
       if (sipHeader instanceof SIPHeaderList) 
		throw new IllegalArgumentException( sipHeader.getClass().getName());
       // BUGBUG -- this compensates for a bug in the interface
       if (sipHeader instanceof Allow) 
		this.headerName = AllowHeader.name;
       else this.headerName = sipHeader.getHeaderName();
       this.value = sipHeader.getHeaderValue();
    }

    /**
    * Set the NIST-SIP implementation object.
    */
    public SIPHeader getImplementationObject() {
        return sipHeader;
    }           
    
   /** 
    * This method checks if the "get" methods of the JAIN-SIP headers  
    * are implemented right. We use the java.lang.reflect package to do some
    * generic tests.
    * This is not part of the JAIN implementation.
    */    
    public void testGetMethods(PrintStream testResults) {
        Class c= this.getClass();
        Method[] theMethods = c.getMethods();
        String className=c.getName();
        testResults.println("    Test of the Header : "+className);
        testResults.println();
        try {
        for (int i = 0; i < theMethods.length; i++) {
             String methodString = theMethods[i].getName();
                    
             // test the get<Field> methods:
             if (  methodString.startsWith("get") &&
                     ! methodString.equals("getValue") &&
                     ! methodString.equals("getClass") &&
                     ! methodString.equals("getParameter") &&
                     ! methodString.equals("getName") 
                ) {   
                  Object returnObject;
                  returnObject = theMethods[i].invoke(this,null);  
                  if ( returnObject == null ) {
                       //    testResults.println
                       //     ("    Return value : null        TEST NOT OK");
                  }
                  else {
                       testResults.println("    Name method: " + methodString);
                       if ( methodString.equals("getParameters") ) {
                             Iterator iterator=(Iterator)returnObject;
                             int j=0;
                             while ( iterator.hasNext() ) {
                                  Object o=iterator.next();j++;
                                  if ( o instanceof NameValue) {
                                      NameValue nv=(NameValue)o;
                                      testResults.println
                                        ("      element "+j+" : "+nv.getName());
                                  }
                                  else 
                                  testResults.println
                                        ("      element "+j+" : "+o.toString());
                             }
                             if (j==0)   
                                  testResults.println
                                            ("  no parameters         TEST OK");
                             else
                                  testResults.println("               TEST OK");
                       } else 
                       if ( methodString.equals("getNameAddress") ) {
                           NameAddressImpl na=(NameAddressImpl)returnObject;
                           String displayName=na.getDisplayName();
                           testResults.println("   DisplayName: "+displayName);
                           URIImpl uri=(URIImpl)na.getAddress();
                           String schemeData=uri.getSchemeData();
                           String scheme=uri.getScheme();
                           testResults.println("   schemeData: "+schemeData);
                           testResults.println("   scheme: "+scheme);
                       } else 
                       if ( methodString.equals("getProducts") )  {
                             Iterator iterator=(Iterator)returnObject;
                             int j=0;
                             while ( iterator.hasNext() ) {
                                  String s=(String)iterator.next();j++;
                                  testResults.println
                                        ("      element "+j+" : "+s);
                             }
                             if (j==0)   
                                  testResults.println
                                            ("  no products      TEST OK");
                             else
                                  testResults.println("           TEST OK");      
                       } else 
                       if ( methodString.equals("getImplementationObject") )  { 
                           Class classHeader=
				((SIPHeader)returnObject).getClass();
                           testResults.print
                                ("   Return Value: "+classHeader.getName() );
                           testResults.println("   TEST OK");
                       } else
                       if ( methodString.equals("getDate") )  { 
                            Date date=(Date)returnObject;
                            testResults.println(date.toString());
                       } else {
                             testResults.print
                              ("    Return value : "+ returnObject );  
                             testResults.println("               TEST OK");
                         }
                       testResults.println();    
                    }
                }
             }
        }
        catch (IllegalAccessException e) {
                 testResults.println(e);
        }
        catch (InvocationTargetException e) {
                 testResults.println(e);
                 e.printStackTrace();
                 Throwable t=e.getTargetException(); 
                 testResults.println(((SipParseException)t).getUnparsable());
        }
    }

    
      
   /** 
    * This method checks if the "set" methods of the JAIN-SIP headers  
    * are implemented right. So, we use the java.lang.reflect package to do some
    * generic tests.
    */    
    public void testSetMethods(PrintStream testResults) {
        Class c= this.getClass();
        Method[] theMethods = c.getMethods();
        String className=c.getName();
        testResults.println("    Test of the Header : "+className);
       
        // variable using to differentiate the parameters
        int numero=0;
        
        // variable using as a parameter for the "getParameter(name)" method
        String parameterSet="";
        
        try {
             for (int i = 0; i < theMethods.length; i++) {
                 String methodString = theMethods[i].getName();
                      
                 // test the set<Field> methods:
                 if ( methodString.startsWith("set") &&
                      ! methodString.equals("setValue") &&
                      ! methodString.equals("setImplementationObject")
                    ) 
                 {
                    Class [] parameterTypes= theMethods[i].getParameterTypes(); 
                    Object [] arguments = new Object[parameterTypes.length];
                    testResults.print("    Name method : " + methodString+"( ");       
                    if ( methodString.equals("setParameter") ) {
                        arguments[0]=("variable"+numero);numero++;
                        parameterSet=(String)arguments[0];
                        arguments[1]=("value"+numero);numero++;
                        testResults.print
                            ( (String)arguments[0]+" , "+ (String)arguments[1] );
                    } else
                    for (int j = 0; j < parameterTypes.length; j++) {
                         Class parameterClass = parameterTypes[j];
                         String parameterName=parameterClass.getName();
                         //testResults.println(parameterName);
                         if ( parameterName.equals("long") ) {
                             arguments[j]= new Integer(numero);
                             testResults.print(numero);
                         } else
                         if ( parameterName.equals("int") ) {                                
                             arguments[j]= new Integer(numero);
                             testResults.print(numero);
                         } else
                         if ( parameterName.equals("java.lang.String") ) {
                             if ( methodString.equals("setDate") ) {
                                  // Format the current time.
                                  SimpleDateFormat formatter=new SimpleDateFormat
                                            ("M/d/yy h:mm a");
                                  Date currentTime_1 = new Date();
                                  String dateString = 
                                                formatter.format(currentTime_1);
                                  
                                   arguments[j]=dateString;
                                   testResults.print(dateString);
                             } else {
                                arguments[j]= new String("variable"+numero);
                                testResults.print("variable"+numero);
                             }
                         } else
                         if ( parameterName.equals("float") ) {                                
                             arguments[j]= new Integer(1);
                             testResults.print("1");
                         } else
                         if ( parameterName.equals("boolean") ) { 
                             Boolean bool=new Boolean("true");
                             arguments[j]=bool;
                             testResults.print("true");
                         } else 
                         if ( parameterName.equals("java.net.InetAddress") ) {
                             arguments[j]=InetAddress.getLocalHost(); 
                             testResults.print("inetAddress"); 
                         } else 
                         if ( parameterName.equals("java.util.Date") ) {
                             long date=0;
                             arguments[j]=new Date(date);
                             testResults.print(date);
                         } else
                         if ( parameterName.equals("java.util.List") ) {
                             Vector list=new Vector();
                             list.add("variable"+numero);
                             arguments[j]=list;
                             testResults.print("list"); 
                         } else
                         if ( parameterName.equals
                         ("jain.protocol.ip.sip.address.NameAddress") ) {
                             NameAddressImpl na=new NameAddressImpl();
                             na.setDisplayName("variable"+numero);
                             testResults.print("variable"+numero); numero++;
                             URIImpl u=new  URIImpl();
                             u.setSchemeData("variable"+numero);numero++;
                             u.setScheme("variable"+numero);
                             na.setAddress(u);
                             arguments[j]=na;
                         } else
                         { 
                            arguments[j]= c.newInstance();
                         }
                         numero++;
                         if ( j != (parameterTypes.length-1) ) 
                                                        testResults.print(" , ");
                    }
                    testResults.print(" )");
                    Object returnObject = theMethods[i].invoke(this,arguments);
                    testResults.println("               TEST OK");
                 }
             }
             testResults.println();
             for (int i = 0; i < theMethods.length; i++) {
                String methodString = theMethods[i].getName();
                    
                // test the get<Field> methods:
                if (  methodString.startsWith("get") &&
                     ! methodString.equals("getValue") &&
                     ! methodString.equals("getClass")
                   ) {   
                    Object returnObject;
                    if ( methodString.equals("getParameter") ) {
                         Object [] arguments = new Object[1];
                         arguments[0]=parameterSet; 
                         returnObject = theMethods[i].invoke(this,arguments);  
                    } else
                         returnObject = theMethods[i].invoke(this,null);  
                           
                    if ( returnObject == null ) {
                          testResults.println("    Name method: " + methodString);
                          testResults.println
                               ("    Return value : null        TEST NOT OK");
                          testResults.println();   
                    }
                    else {
                       testResults.println("    Name method: " + methodString);
                       if ( methodString.equals("getParameters") ) {
                             Iterator iterator=(Iterator)returnObject;
                             int j=0;
                             while ( iterator.hasNext() ) {
                                  Object o=iterator.next();j++;
                                  testResults.println
                                        ("      element "+j+" : "+o.toString());
                             }
                             if (j==0)   
                                  testResults.println
                                            ("  no parameters         TEST OK");
                             else
                                  testResults.println("               TEST OK");
                       } else 
                       if ( methodString.equals("getNameAddress") ) {
                           NameAddressImpl na=(NameAddressImpl)returnObject;
                           String displayName=na.getDisplayName();
                           testResults.println("    DisplayName: "+displayName);
                           URIImpl uri=(URIImpl)na.getAddress();
                           String schemeData=uri.getSchemeData();
                           String scheme=uri.getScheme();
                           testResults.println("    schemeData: "+schemeData);
                           testResults.println("    scheme: "+scheme);
                       } else 
                       if ( methodString.equals("getProducts") )  {
                             Iterator iterator=(Iterator)returnObject;
                             int j=0;
                             if (iterator!=null) {
                                while ( iterator.hasNext() ) {
                                  String s=(String)iterator.next();j++;
                                  testResults.println
                                        ("      element "+j+" : "+s);
                                }
                             }
                             if (j==0)   
                                  testResults.println
                                            ("  no products         TEST OK");
                             else
                                  testResults.println("               TEST OK");      
                       } else 
                       if ( methodString.equals("getImplementationObject") )  {      
                            testResults.println
                        ("    Return Value: "+((SIPHeader)returnObject).getClass());
                          // testResults.println("   TEST OK");
                       } else
                       if ( methodString.equals("getDate") )  { 
                            Date date=(Date)returnObject;
                            testResults.println(date.toString());
                       } else {
                             testResults.print
                              ("    Return value : "+ returnObject );  
                             testResults.println("               TEST OK");
                         }
                       testResults.println();    
                    }
                }
             }
        }
        catch (IllegalAccessException e) {
                 testResults.println(e);
        }
        catch (InvocationTargetException e) {
                 testResults.println(e);
                 e.printStackTrace();
                 Throwable t=e.getTargetException(); 
                 testResults.println(((SipParseException)t).getUnparsable());
        }
        catch (UnknownHostException e) {
                 testResults.println(e);
        }
        catch (InstantiationException e) {
                testResults.println(e);
                e.printStackTrace(); 
        }
        catch (SipParseException e) {
                testResults.println(e);
                e.printStackTrace(); 
        }
    }
    
}



