package org.mbus;

import java.util.*;
import java.net.*;

/**
 * This address defines an MBus-Entity. It also contains a
 * IP-Address and portnumber for unicast-optimization.
 * <p>
 * With additions from Roman Kurmanowytsch <romank@infosys.tuwien.ac.at><br>
 *
 * @author Ralf Kollmann, Stefan Prellle
 * @version $Revision: 1.1 $ $Date: 2002/02/04 13:23:34 $
 */

public class Address{

    private String[][]  address;
    private InetAddress ip;
    private int         port;
    
    //--------------------------------------------------------------
    public Address(String[][] mbaddress){
	address=mbaddress;
	ip   = null;
	port = -1;
    }
    
    //--------------------------------------------------------------
    public Address(String[][] mbaddress, InetAddress ip, int port){
	address=mbaddress;
	this.ip   = ip;
	this.port = port;
    }
    
    //--------------------------------------------------------------
    public Address(String[][] mbaddress, String ip, int port) throws UnknownHostException {
	address=mbaddress;
	this.ip   = InetAddress.getByName(ip);
	this.port = port;
    }

    //--------------------------------------------------------------
    /**
     * Create an address from a text-string like<br>
     * <tt>(media:audio module:engine)</tt>
     */
    public Address(String textAddr){
	if(textAddr.length()<2){
	    address = new String[0][];
	    ip   = null;
	    port = -1;
	    return;
	}
	
	int start = textAddr.indexOf("(")+1;
	int end   = textAddr.length();
	String addr = textAddr.substring(start, end);
	
	StringTokenizer str = new StringTokenizer(addr, " ");
	Vector data = new Vector();
	while (str.hasMoreTokens()) 
	    data.addElement(str.nextToken());
	    
	address = new String[data.size()][2];
	for (int i=0; i<data.size(); i++) {
	    String pair = (String)data.elementAt(i);
	    int pos = pair.indexOf(':');
	    try {
		String tag = pair.substring(0,pos);
		String val = pair.substring(pos+1,pair.length());
		address[i][0] = tag;
		address[i][1] = val;
	    } catch (StringIndexOutOfBoundsException sie) {
		System.out.println("Address.<init>: pair = "+pair);
		System.out.println("             in text = "+textAddr);
		sie.printStackTrace();
		throw sie;
	    }
	}
    }
    
    //--------------------------------------------------------------
    /**
     * Checks, if a given destination address matches the own address,
     * by searching elements whose values do not coincide, or whose
     * tags are not defined.
     * Used for checking for recipients and compentence for a certain
     * message. ("AddressFilter")
     */

    public boolean matches(Address dest){
	String[][] values=dest.getValues();
	
	boolean keymatch=false;
	
	if(values.length==0 ||
	   address.length==0)return true;

	for(int i=0;i<values.length;i++){
	    for(int k=0;k<address.length;k++){

		if(values[i][0].equals(address[k][0])){
		    keymatch=true;
		    if(!values[i][1].equals(address[k][1])){
			return false;}
		    else break;
		}
	    }
	}
	return keymatch;
    }

    //--------------------------------------------------------------
    /**
     * Checks, if two addresses are equal concerning the elements
     * and their values.
     */
    public boolean equals(Address target){
	String[][] values=target.getValues();
	if(address.length!=values.length){
	    return false;}
	boolean foundmatch;
	for(int i=0;i<values.length;i++){
	    foundmatch=false;
	    for(int k=0;k<address.length;k++){
		if(values[i][0].equals(address[k][0]) &&
		   values[i][1].equals(address[k][1])){
		    foundmatch=true;
		}
	    }
	    if(!foundmatch){
		return false;}
	}
	return true;
    }
    
    //--------------------------------------------------------------
    public String toString(){
 	StringBuffer ret=new StringBuffer("(");
 	for(int i=0;i<address.length; i++){
	    ret.append(address[i][0]+":");
	    ret.append(address[i][1]);
 	    if((i+1)<address.length)
 		ret.append(' ');
 	}
 	ret.append(")");
 	return ret.toString();	
     }
    
    //--------------------------------------------------------------
    public static int getHashCode(Object o){
	return o.hashCode();
    }
    
    //--------------------------------------------------------------
    /**
     * @deprecated Use <tt>setID</tt> instead.
     */
    public void setInstanceID(String inst, boolean overwrite) {
	setValue("id",inst,overwrite);
    }
    
    //--------------------------------------------------------------
    public void setID(String id) {
	setValue("id",id,true);
    }
   
    //--------------------------------------------------------------
    public void setValue(String key, String value, boolean overwrite) {
	for (int i=0; i<address.length; i++) {
	    if (address[i][0].equalsIgnoreCase(key)) {
		if (overwrite) {
		    address[i][1] = value;
		}
		return;
	    }
	}
	String[][] tmp = new String[address.length+1][2];
	for (int i=0; i<address.length; i++) {
	    tmp[i][0] = address[i][0];
	    tmp[i][1] = address[i][1];
	}
	tmp[tmp.length-1][0] = key;
	tmp[tmp.length-1][1] = value;
	address = tmp;
    }

    //--------------------------------------------------------------
    public String[][] getValues(){
	return address;
    }
   
    //--------------------------------------------------------------
    public String getValue(String field) {
	for (int i=0; i<address.length; i++)
	    if (address[i][0].equals(field))
		return address[i][1];
	return "";
    }
   
    //--------------------------------------------------------------
    public String getApp()    {return getValue("app"); }
    public String getModule() {return getValue("module"); }
    public String getMedia()  {return getValue("media"); }
    public String getID()     {return getValue("id"); }
    
    
}
