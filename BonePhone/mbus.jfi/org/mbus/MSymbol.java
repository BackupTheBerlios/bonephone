package org.mbus;

import java.io.*;

/**
 * MSymbol.java
 *
 *
 * <br>Created: Wed Sep 15 18:43:17 1999
 *
 * @author Stefan Prelle
 * @version $id: MSymbol.java 1.0 Wed Sep 15 18:43:17 1999 prelle Exp $
 */

public class MSymbol extends MDataType {
   
    private String data;
 
    //---------------------------------------------------------------
    public MSymbol() {
	data = null;
    }
  
    //---------------------------------------------------------------
    public MSymbol(String foo) {
	data = foo;
    }
  
    //---------------------------------------------------------------
    public String toString() {
	return data;
    }
  
    //---------------------------------------------------------------
    public int compareTo(Object o) {
	String foo = ((MSymbol)o).toString();
	return data.compareTo(foo);
    }
   
} // MSymbol
