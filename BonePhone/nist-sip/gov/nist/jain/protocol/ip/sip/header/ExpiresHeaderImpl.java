/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../../../../../doc/uncopyright.html for conditions of use       *
* Creator: O. Deruelle (deruelle@nist.gov)                                     *
* Modified by: M. Ranganathan (mranga@Nist.gov) removed use of deprecated      *
*  method.								       *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.jain.protocol.ip.sip.header;

import jain.protocol.ip.sip.*;
import jain.protocol.ip.sip.header.*;

import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import gov.nist.sip.msgparser.*;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

/**
* Implementation of the ExpiresHeader interface of jain-sip.
*/
public final class ExpiresHeaderImpl extends HeaderImpl
implements ExpiresHeader, NistSIPHeaderMapping {
    
    /** Default constructor
     */    
   public ExpiresHeaderImpl() {
       super();
      this.headerName = name;
   }

    /** constructor
     * @param exp Expires to set
     */   
   public ExpiresHeaderImpl(Expires exp) { 
       super(exp);
      this.headerName = name;
   }
     
    
    /**
    * Gets value of ExpiresHeader as delta-seconds
    * (Returns -1 if expires value is not in delta-second format)
    * @return value of ExpiresHeader as delta-seconds
    */
    public long getDeltaSeconds() {
        Expires expires=(Expires)sipHeader;
        SIPDateOrDeltaSeconds s=expires.getExpiryTime();
        if (s==null) return -1;
        if (s.isDeltaSeconds()) {
                return  ((DeltaSeconds)s).getDeltaSeconds();
        } else return -1;
    }

    
   

    
    /**
    * Gets boolean value to indicate if expiry value of ExpiresHeader
    * is in date format
    * @return boolean value to indicate if expiry value of ExpiresHeader
    * is in date format
    */
    public boolean isDate() {
          Expires expires=(Expires)sipHeader;
          return expires.isDate();
    }

    
    /**
    * Gets date of DateHeader
    * (Returns null if date does not exist (this can only apply
    * for ExpiresHeader subinterface - when the expires value is in delta
    * seconds format)
    * @return date of DateHeader
    */
    public Date getDate() {
        Expires expires=(Expires)sipHeader;
        return expires.getDate();
    }

    
     /**
     * Sets expires of ExpiresHeader as delta-seconds
     * @param deltaSeconds long to set
     * @throws SipParseException if deltaSeconds is not accepted 
     * by implementation
     */
    public void setDeltaSeconds(long deltaSeconds) throws SipParseException {
      Expires expires=(Expires)sipHeader;
            DeltaSeconds ds=new DeltaSeconds();
            ds.setDeltaSeconds(deltaSeconds);
            expires.setExpiryTime(ds);
    }
   
    
    
    
    /**
     * Sets date of DateHeader
     * @param date Date to set
     * @throws IllegalArgumentException if date is null
     * @throws SipParseException if date is not accepted by implementation
     */
    public void setDate(Date date)
    throws IllegalArgumentException, SipParseException {
        Expires expires=(Expires)sipHeader;
        if (date==null)
                throw new IllegalArgumentException
                    ("JAIN-SIP EXCEPTION: date is null");
        else  {
                    long d=date.getTime();
                    SIPDate da=new SIPDate(d);
                    expires.setExpiryTime(da);
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
         Expires expires=(Expires)sipHeader;
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
                 expires.setExpiryTime(da);
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
                         expires.setExpiryTime(da);
                     }
                 }
             }
        }
    }
    
    
    
}


