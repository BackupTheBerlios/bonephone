package traceviewer ;

import java.util.* ;
import java.io.* ;

public class TraceMessage implements Serializable{

	String messageFrom = null ;
	String messageTo = null  ;
	String messageTime = null  ;
	String messageString = null  ;
	String messageFirstLine = null ;
	
	public TraceMessage(){
	}
	
	public TraceMessage(String messageFrom,
				String messageTo,
				String messageTime,
				String messageFirstLine,
				String messageString
				){
	this.messageFrom = messageFrom ;
	this.messageTo = messageTo ;
	this.messageTime = messageTime ;
	this.messageString = messageString ;
	this.messageFirstLine = messageFirstLine ;				
					
	}
	
	public void setFrom(String from){
		messageFrom = from ;	
	}

	public void setTo(String to){
		messageTo = to ;	
	}

	public void setTime(String time){
		messageTime = time ;	
	}

	public void setMessageString(String str){
		messageString = str ;	
	}

	public void setFirstLine(String FirstLine){
		messageFirstLine = FirstLine ;	
	}

	public String getFrom(){
		return messageFrom ;	
	}
	
	public String getTo(){
		return messageTo ;	
	}
	
	public String getTime(){
		return messageTime ;	
	}
	
	public String getMessageString(){
		return messageString ;	
	}

	public String getFirstLine(){
		return messageFirstLine ;	
	}
}