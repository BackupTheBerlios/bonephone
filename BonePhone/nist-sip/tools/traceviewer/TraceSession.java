package traceviewer ;

import java.util.* ;
import java.io.* ;

public class TraceSession extends Vector implements Serializable{

	String name = null ;
	String info = null ;

	public TraceSession(){
		super() ;	
	}

	public TraceSession(String name,String info){
		this() ;
		this.name = name ;
		this.info = info ;
	}

	public void setName(String name){
		this.name = name ;	
	}
	
	public String getName(){
		return name ;
	}
	
	public void setInfo(String info){
		this.info = info ;	
	}
	
	public String getInfo(){
		return info ;
	}
}