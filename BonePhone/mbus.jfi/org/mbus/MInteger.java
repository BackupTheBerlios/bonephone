package org.mbus;

import java.util.*;
import java.io.*;

/**
 * MInteger.java
 *
 *
 * <br>Created: Wed Sep 15 18:34:38 1999
 *
 * @author Stefan Prelle
 * @version $id: MInteger.java 1.0 Wed Sep 15 18:34:39 1999 prelle Exp $
 */

public class MInteger extends MDataType {
    
    private Integer data;

    //---------------------------------------------------------------
    public MInteger(int dat) {
	data = new Integer(dat);
    }

    //---------------------------------------------------------------
    public MInteger(Integer dat) {
	data = dat;
    }

    //---------------------------------------------------------------
    public Integer getData() {
	return data;
    }

    //---------------------------------------------------------------
    public int intValue() {
	return data.intValue();
    }

    //---------------------------------------------------------------
    public int compareTo(Object o) {
	MInteger f = (MInteger)o;
	return data.compareTo(f.getData());
    }

    //---------------------------------------------------------------
    public String toString() {
	return ""+data;
    }
    
} // MInteger
