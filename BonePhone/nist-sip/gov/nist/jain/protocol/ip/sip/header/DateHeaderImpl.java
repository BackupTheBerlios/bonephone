/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: O. Deruelle (deruelle@nist.gov)                                     *
* Modified by: M. Ranganathan (mragna@nist.gov) - changed Header name for      *
*   NIST-SIP header from DateHeader to SIPDateHeader		               *
* Modified by: M. Ranganathan (mranga@Nist.gov) removed use of deprecated      *
*  method.								       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.log.*;
import gov.nist.sip.msgparser.*;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

/**
* Implementation of the DateHeader interface of jain-sip.
*/
public final class DateHeaderImpl  extends HeaderImpl
implements jain.protocol.ip.sip.header.DateHeader, NistSIPHeaderMapping {

    
    /** Default constructor
     */    
    public DateHeaderImpl() { 
         super();
      this.headerName = name;
       
    }

    /** constructor
     * @param sdh SIPDateHeader to set
     */    
    public DateHeaderImpl(SIPDateHeader sdh) { 
      super(sdh);
      this.headerName = name;
    }
  
    /**
     * Gets date of DateHeader
     * (Returns null if date does not exist (this can only apply
     * for ExpiresHeader subinterface - when the expires value is in delta
     * seconds format)
     * @return date of DateHeader
     */
    public Date getDate() { 
        SIPDateHeader dateHeader=(SIPDateHeader)sipHeader;
        SIPDate sipdate=dateHeader.getDate();
        if (sipdate==null) return null;
        else return (sipdate.getJavaCal() ).getTime();
    }
    
    /**
     * Sets date of DateHeader
     * @param date Date to set
     * @throws IllegalArgumentException if date is null
     * @throws SipParseException if date is not accepted by implementation
     */
    public void setDate(Date date)
    throws IllegalArgumentException, SipParseException {
      
        SIPDateHeader dateHeader=(SIPDateHeader)sipHeader;
        if (date==null)
                throw new IllegalArgumentException
                ("JAIN-SIP EXCEPTION : date is null");
        else {
           
                SIPDate sipdate=new SIPDate(date.getTime()); 
                dateHeader.setDate(sipdate);
                 
        }           
    }
    
    /**
     * Sets date of DateHeader
     * @param date String to set
     * @throws IllegalArgumentException if date is null
     * @throws SipParseException if date is not accepted by implementation
     */
    public void setDate(String date)
    throws IllegalArgumentException, SipParseException {
        
         SIPDateHeader dateHeader =(SIPDateHeader)sipHeader;
         if (date==null)
                throw new IllegalArgumentException
                    ("JAIN-SIP EXCEPTION: date is null");
         else  {
             
             try {
                 SimpleDateFormat formatter=new SimpleDateFormat();
                 Date d=formatter.parse(date,new ParsePosition(0));
                 if (d == null)
                     throw new SipParseException("Bad date format!");
                 long time = d.getTime();
                 
                 SIPDate da=new SIPDate(time);
                 dateHeader.setDate(da);
             }
             catch (SipParseException s) {
                 // date format of TCK test !!!!
                 TimeZone t=TimeZone.getTimeZone(date);
                 if (t != null) {
                     Calendar c=Calendar.getInstance(t);
                     if (c !=null) {
                         Date d=c.getTime();
                         long time = d.getTime();
                         SIPDate da=new SIPDate(time);
                         dateHeader.setDate(da);
                     }
                 }
             }
         }
    }

}



