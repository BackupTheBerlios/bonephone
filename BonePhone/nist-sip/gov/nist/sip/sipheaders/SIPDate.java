/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov) ,added JAVADOC                 *                                                                                 
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Locale;
import java.util.GregorianCalendar;
import java.lang.IllegalArgumentException;
/**
* Implements a parser class for tracking expiration time 
* when specified as a Date value.
*<pre>
* From the HTTP 1.1 spec
*14.18 Date
*
*   The Date general-header field represents the date and time at which
*   the message was originated, having the same semantics as orig-date in
*   RFC 822. The field value is an HTTP-date, as described in section
*   3.3.1; it MUST be sent in RFC 1123 [8]-date format.
*
*       Date  = "Date" ":" HTTP-date
*
*   An example is
*
*       Date: Tue, 15 Nov 1994 08:12:31 GMT
*</pre>
*/

public class SIPDate  extends SIPDateOrDeltaSeconds implements DateKeywords {


        /** sipWkDay member
         */    
	protected String sipWkDay;
        
         /** sipMonth member
         */ 
	protected String sipMonth;
        
         /** wkday member
         */ 
	protected int	 wkday;
        
         /** day member
         */ 
	protected int    day;
        
         /** month member
         */ 
	protected int    month;
        
         /** year member
         */ 
	protected int    year;
        
         /** hour member
         */ 
	protected int    hour;
        
         /** minute member
         */ 
	protected int    minute;
        
         /** second member
         */ 
	protected int    second;
        
         /** javaCal member
         */ 
	protected java.util.Calendar javaCal;
		
	/**
	* Initializer, sets all the fields to invalid values. 
	*/
	public SIPDate () {
		wkday = -1;
		day = -1;
		month = -1;
		year = -1;
		hour = -1;
		minute = -1;
		second = -1;
		javaCal = null;
	}
        
	/**
         * Construct a SIP date from the time offset given in miliseconds
         * @param timeMillis long to set
         */
	public SIPDate( long timeMillis) {
		javaCal = new GregorianCalendar
		 (TimeZone.getTimeZone("GMT:0"), Locale.getDefault());
		java.util.Date date = new java.util.Date(timeMillis);
		javaCal.setTime(date);
		wkday = javaCal.get(Calendar.DAY_OF_WEEK);
		switch (wkday) {
		  case  Calendar.MONDAY: sipWkDay = DateKeywords.MON; break;
		  case  Calendar.TUESDAY: sipWkDay = DateKeywords.TUE; break;
		  case  Calendar.WEDNESDAY: sipWkDay = DateKeywords.WED; break;
		  case  Calendar.THURSDAY: sipWkDay = DateKeywords.THU; break;
		  case  Calendar.FRIDAY: sipWkDay = DateKeywords.FRI; break;
		  case  Calendar.SATURDAY: sipWkDay = DateKeywords.SAT; break;
		  case  Calendar.SUNDAY: sipWkDay = DateKeywords.SUN; break;
		  default: InternalError.handleException
			("No date map for wkday "+ wkday);
		}
		
		day   = javaCal.get(Calendar.DAY_OF_MONTH);
		month = javaCal.get(Calendar.MONTH);
		switch (month) {
		  case Calendar.JANUARY:  sipMonth = DateKeywords.JAN; break;
		  case Calendar.FEBRUARY: sipMonth= DateKeywords.FEB; break;
		  case Calendar.MARCH: sipMonth= DateKeywords.MAR; break;
		  case Calendar.APRIL: sipMonth= DateKeywords.APR; break;
		  case Calendar.MAY: sipMonth= DateKeywords.MAY; break;
		  case Calendar.JUNE: sipMonth= DateKeywords.JUN;break; 
		  case Calendar.JULY: sipMonth= DateKeywords.JUL;break;
		  case Calendar.AUGUST: sipMonth= DateKeywords.AUG;break;
		  case Calendar.SEPTEMBER: sipMonth= DateKeywords.SEP;break;
		  case Calendar.OCTOBER: sipMonth= DateKeywords.OCT;break;
		  case Calendar.NOVEMBER: sipMonth= DateKeywords.NOV;break;
		  case Calendar.DECEMBER: sipMonth= DateKeywords.DEC;break;
		  default: InternalError.handleException
			("No date map for month "+ month);
		}
		year  = javaCal.get(Calendar.YEAR);
		hour  = javaCal.get(Calendar.HOUR);
		minute = javaCal.get(Calendar.MINUTE);
		second = javaCal.get(Calendar.SECOND);
	}

        /**
         * Get canonical string representation.
         * @return String 
         */	
	public String encode()  {
		
		String dayString;
		if (day < 10 ) {
			dayString = "0" + day;
		} else dayString = "" + day;
		
		String hourString;
		if (hour< 10) {
			hourString = "0" + hour;
		} else hourString = ""+ hour;
		
		String minuteString;
		if (minute< 10) {
			minuteString = "0" + minute;
		} else minuteString = ""+minute;
		
		String secondString;
		if (second < 10 ) {
			secondString = "0" + second;
		} else secondString = "" + second;

		String encoding = "";


		if (sipWkDay != null) encoding += sipWkDay + COMMA + SP;
		
		encoding += dayString + SP;

		if (sipMonth != null) encoding += sipMonth + SP;

		encoding += year + SP + hourString + COLON + minuteString +
			COLON + secondString + SP + GMT;
		
		
		return encoding;
	}
        
	/**
         * The only accessor we allow is to the java calendar record.
         * All other fields are for this package only.
         * @return Calendar
         */
	public java.util.Calendar getJavaCal() {
		if (javaCal == null) setJavaCal();
		return javaCal;
	}
	
        /** get the WkDay field
         * @return String
         */        
	public String getWkday() { 
            return sipWkDay;
        }
        
        /** get the month
         * @return String
         */        
	public String getMonth() {
            return sipMonth;
        }
        
        /** get the hour
         * @return int
         */        
	public int getHour() { 
            return hour;
        }
        
        /** get the minute
         * @return int
         */        
	public int getMinute() { 
            return minute;
        }
        
        /** get the second
         *  @return int
         */        
	public int getSecond() {
            return second;
        }

	/**
	* convert the SIP Date of this structure to a Java Date.
	* SIP Dates are forced to be GMT. Stores the converted time
	* as a java Calendar class.
	*/
	private void setJavaCal() {
		javaCal = new GregorianCalendar
		 (TimeZone.getTimeZone("GMT:0"), Locale.getDefault());
		if (year   != -1 )  javaCal.set(Calendar.YEAR,year);
		if (day    != -1 )  javaCal.set(Calendar.DAY_OF_MONTH,day);
		if (month  != -1 )  javaCal.set(Calendar.MONTH,month);
		if (wkday  != -1 )  javaCal.set(Calendar.DAY_OF_WEEK,wkday);
		if (hour   != -1 )  javaCal.set(Calendar.HOUR, hour);
		if (minute != -1 )  javaCal.set(Calendar.MINUTE,minute);
		if (second != -1 )  javaCal.set(Calendar.SECOND,second);
	}

	/**
         * Set the wkday member
         * @param w String to set
         * @throws IllegalArgumentException if w is not a valid day.
         */
	public	 void setWkday(String w ) 
	throws IllegalArgumentException { 
		sipWkDay = w ;
		if (sipWkDay.compareToIgnoreCase(DateKeywords.MON) == 0 ) {
			wkday = Calendar.MONDAY;
		} else if 
			(sipWkDay.compareToIgnoreCase(DateKeywords.TUE) == 0) {
			wkday = Calendar.TUESDAY;
		} else if 
			(sipWkDay.compareToIgnoreCase(DateKeywords.WED) == 0) {
			wkday = Calendar.WEDNESDAY;
		} else if 
			(sipWkDay.compareToIgnoreCase(DateKeywords.THU) == 0) {
			wkday = Calendar.THURSDAY;
		} else if 
			(sipWkDay.compareToIgnoreCase(DateKeywords.FRI) == 0 ) {
			wkday = Calendar.FRIDAY;
		} else if 
			(sipWkDay.compareToIgnoreCase(DateKeywords.SAT) == 0) {
			wkday = Calendar.SATURDAY;
		} else if 
			(sipWkDay.compareToIgnoreCase(DateKeywords.SUN) == 0) {
			wkday = Calendar.SUNDAY;
		} else {
		     throw new 
			IllegalArgumentException( "Illegal Week day :" + w);
		}
	} 

	/**
         * Set the day member
         * @param d int to set
         * @throws IllegalArgumentException if d is not a valid day
         */
	public	 void setDay(int d) 
	throws IllegalArgumentException { 
		if (d < 1 || d > 31) 
			throw new IllegalArgumentException
				( "Illegal Day of the month " +
					new Integer(d).toString());
		day = d;
	} 

	/**
         * Set the month member
         * @param m String to set.
         * @throws IllegalArgumentException if m is not a valid month
         */
	public	 void setMonth(String m) 
	throws IllegalArgumentException { 
		sipMonth = m ; 
	        if ( sipMonth.compareToIgnoreCase(DateKeywords.JAN) == 0 ) {
			month =  Calendar.JANUARY;
	         } else if ( 
		  sipMonth.compareToIgnoreCase(DateKeywords.FEB)  == 0 ) {
			month =  Calendar.FEBRUARY;
		 } else if ( 
		    sipMonth.compareToIgnoreCase(DateKeywords.MAR) == 0 ) {
			month =  Calendar.MARCH;
		 } else if 
		     ( sipMonth.compareToIgnoreCase(DateKeywords.APR)== 0  ) {
			month =  Calendar.APRIL;
		 } else if 
		    ( sipMonth.compareToIgnoreCase(DateKeywords.MAY)== 0  ) {
			month =  Calendar.MAY;
		 } else if 
		     ( sipMonth.compareToIgnoreCase(DateKeywords.JUN)== 0  ) {
			month =  Calendar.JUNE;
		 } else if 
		     ( sipMonth.compareToIgnoreCase(DateKeywords.JUL)== 0  ) {
			month =  Calendar.JULY;
		 } else if 
		    ( sipMonth.compareToIgnoreCase(DateKeywords.AUG)== 0  ) {
			month =  Calendar.AUGUST;
		 } else if 
		    ( sipMonth.compareToIgnoreCase(DateKeywords.SEP)== 0  ) {
			month =  Calendar.SEPTEMBER;
		 } else if 
          	    ( sipMonth.compareToIgnoreCase(DateKeywords.OCT)== 0  ) {
			month =  Calendar.OCTOBER;
		 } else if 
		    ( sipMonth.compareToIgnoreCase(DateKeywords.NOV)== 0  ) {
			month =  Calendar.NOVEMBER;
		 } else if 
		    ( sipMonth.compareToIgnoreCase(DateKeywords.DEC)== 0  ) {
			month =  Calendar.DECEMBER;
		 } else {
			throw new IllegalArgumentException
				( "Illegal Month :" + m);
		 }
	} 

	/**
         * Set the year member
         * @param y int to set
         * @throws IllegalArgumentException if y is not a valid year.
         */
	public	 void setYear(int y) 
	throws IllegalArgumentException { 
		if (y < 0 ) throw new IllegalArgumentException
			 ("Illegal year : " + y);
		 javaCal = null;
 	 	 year = y ; 
	} 

	/**
	* Get the year member.
	*/
	public int getYear()  {
		return year;
	}
        
	/**
         * Set the hour member
         * @param h int to set
         * @throws IllegalArgumentException if h is not a valid hour.
         */
	public	 void setHour(int h) 
	throws IllegalArgumentException { 
		if (h < 0 || h > 24) 
			throw new IllegalArgumentException 
				("Illegal hour : " + h);
		javaCal = null;
		hour = h ; 
	} 
        
	/**
         * Set the minute member
         * @param m int to set
         * @throws IllegalArgumentException if m is not a valid minute
         */
	public	 void setMinute(int m)  
	throws IllegalArgumentException { 
		if (m < 0 || m >= 60) 
			throw new IllegalArgumentException
			 ("Illegal minute : " + 
				(new Integer(m).toString()));
		javaCal = null;
		minute = m ; 
	} 
        
	/**
         * Set the second member
         * @param s int to set
         * @throws IllegalArgumentException if s is not a valid second
         */
	public	 void setSecond(int s)  
	throws IllegalArgumentException { 
		if (s < 0 || s >= 60) 
		throw new IllegalArgumentException
			("Illegal second : " + 
			new Integer(s).toString());
		javaCal = null;
		second = s ; 
	} 
        
        /**
         * Use the java class for pretty printing.
         * @return String
         */
	public String toString() {
		String retval = "";
		return sipWkDay + ", "  +
		       day + " " + sipMonth + " " +
		       year + " " + hour + ":" + minute + ":" + 
		       second + " " + "GMT";
		       
	}

}

