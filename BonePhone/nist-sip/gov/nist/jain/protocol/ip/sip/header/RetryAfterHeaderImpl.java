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
* Implementation of the RetryAfterHeader interface of jain-sip.
*/
public final class RetryAfterHeaderImpl extends HeaderImpl 
implements RetryAfterHeader, NistSIPHeaderMapping {

  
    /** Default constructor
     */    
    public RetryAfterHeaderImpl() { 
        super(); 
            this.headerName = name;
    }

    /** constructor
     * @param retryAfter RetryAfter to set
     */    
    public RetryAfterHeaderImpl(RetryAfter retryAfter) { 
        super(retryAfter);
            this.headerName = name;
    }
    
    /**
    * Gets comment of RetryAfterHeader
    * (Returns null if comment does not exist)
    * @return comment of RetryAfterHeader
    */
    public String getComment() {
        RetryAfter retryAfter=(RetryAfter)sipHeader;
        return retryAfter.getComment();
    }

    
    /**
    * Gets boolean value to indicate if RetryAfterHeader
    * has comment
    * @return boolean value to indicate if RetryAfterHeader
    * has comment
    */
    public boolean hasComment() {
         RetryAfter retryAfter=(RetryAfter)sipHeader;
         return retryAfter.hasComment();
    }

    
    /**
    * Removes comment from RetryAfterHeader (if it exists)
    */
    public void removeComment() {
         RetryAfter retryAfter=(RetryAfter)sipHeader;
         retryAfter.removeComment();
    }

    
    /**
    * Gets duration of RetryAfterHeader
    * (Returns negative long if duration does not exist)
    * @return duration of RetryAfterHeader
    */
    public long getDuration() {
        RetryAfter retryAfter=(RetryAfter)sipHeader;
        DeltaSeconds ds=retryAfter.getDuration();
        if (ds==null) return -1;
        else    return ds.getDeltaSeconds();
    }

    
    /**
    * Gets boolean value to indicate if RetryAfterHeader
    * has duration
    * @return boolean value to indicate if RetryAfterHeader
    * has duration
    */
    public boolean hasDuration() {
         RetryAfter retryAfter=(RetryAfter)sipHeader;
         return retryAfter.hasDuration();
    }

    
    /**
    * Removes duration from RetryAfterHeader (if it exists)
    */
    public void removeDuration() {
        RetryAfter retryAfter=(RetryAfter)sipHeader;
        retryAfter.removeDuration();
    }

    
    /**
     * Sets comment of RetryAfterHeader
     * @param comment String to set
     * @throws IllegalArgumentException if comment is null
     * @throws SipParseException if comment is not accepted by implementation
     */
    public void setComment(String comment)
    throws IllegalArgumentException, SipParseException {
         RetryAfter retryAfter=(RetryAfter)sipHeader;
         if (comment==null)
             throw new IllegalArgumentException
             ("JAIN-SIP EXCEPTION: comment is null");
         else   retryAfter.setComment(comment);
    }

    
    /**
     * Sets duration of RetryAfterHeader
     * @param duration long to set
     * @throws SipParseException if duration is not accepted by implementation
     */
    public void setDuration(long duration) throws SipParseException {
            RetryAfter retryAfter=(RetryAfter)sipHeader;
            DeltaSeconds ds=retryAfter.getDuration();
            if (ds==null) {
                ds=new DeltaSeconds();
                ds.setDeltaSeconds(duration);
                retryAfter.setDuration(ds);
            }
            else   ds.setDeltaSeconds(duration);
    }

    
    /**
    * Gets value of ExpiresHeader as delta-seconds
    * (Returns -1 if expires value is not in delta-second format)
    * @return value of ExpiresHeader as delta-seconds
    */
    public long getDeltaSeconds() {
        RetryAfter retryAfter=(RetryAfter)sipHeader;
        SIPDateOrDeltaSeconds s=retryAfter.getExpiryDate();
        if (s==null) return -1;
        if (s.isDeltaSeconds()) {
                return  ((DeltaSeconds)s).getDeltaSeconds();
        }
        else return -1;
    }

    
    /**
     * Sets expires of ExpiresHeader as delta-seconds
     * @param deltaSeconds
     * @throws SipParseException if deltaSeconds is not accepted 
     * by implementation
     */
    public void setDeltaSeconds(long deltaSeconds) throws SipParseException {
            RetryAfter retryAfter=(RetryAfter)sipHeader;
            DeltaSeconds ds=new DeltaSeconds();
            ds.setDeltaSeconds(deltaSeconds);
            retryAfter.setExpiryDate(ds);
    }

    
    /**
    * Gets boolean value to indicate if expiry value of ExpiresHeader
    * is in date format
    * @return boolean value to indicate if expiry value of ExpiresHeader
    * is in date format
    */
    public boolean isDate() {
          RetryAfter retryAfter=(RetryAfter)sipHeader;
          return retryAfter.isDate();
    }

    
    /**
    * Gets date of DateHeader
    * (Returns null if date does not exist (this can only apply
    * for ExpiresHeader subinterface - when the expires value is in delta
    * seconds format)
    * @return date of DateHeader
    */
    public Date getDate() {
        RetryAfter retryAfter=(RetryAfter)sipHeader;
        return retryAfter.getDate();
    }

    
    /**
    * Sets date of DateHeader
    * @param <var>date</var> date
    * @throws IllegalArgumentException if date is null
    * @throws SipParseException if date is not accepted by implementation
    */
    public void setDate(Date date)
    throws IllegalArgumentException, SipParseException {
        RetryAfter retryAfter=(RetryAfter)sipHeader;
        if (date==null)
                throw new IllegalArgumentException
                    ("JAIN-SIP EXCEPTION: date is null");
        else  {
            long d=date.getTime();
            SIPDate da=new SIPDate(d);
            retryAfter.setExpiryDate(da);
            
        }
    }

    
    /**
    * Sets date of DateHeader
    * @param <var>date</var> date String
    * @throws IllegalArgumentException if date is null
    * @throws SipParseException if date is not accepted by implementation
    */
    public void setDate(String date)
    throws IllegalArgumentException, SipParseException {
        RetryAfter retryAfter=(RetryAfter)sipHeader;
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
                retryAfter.setExpiryDate(da);
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
                         retryAfter.setExpiryDate(da);
                    }
                 }
             }
        }
    }
    
    
}
