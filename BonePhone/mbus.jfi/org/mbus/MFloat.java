package org.mbus;

import java.util.*;
import java.io.*;

/**
 * MFloat.java
 *
 *
 * <br>Created: Wed Sep 15 18:34:38 1999
 *
 * @author Stefan Prelle
 * @version $id: MFloat.java 1.0 Wed Sep 15 18:34:39 1999 prelle Exp $
 */

public class MFloat extends MDataType {
    
    private Float data;

    //---------------------------------------------------------------
    public MFloat(float dat) {
	data = new Float(dat);
    }

    //---------------------------------------------------------------
    public MFloat(Float dat) {
	data = dat;
    }

    //---------------------------------------------------------------
    public Float getData() {
	return data;
    }

    //---------------------------------------------------------------
    public float floatValue() {
	return data.floatValue();
    }

    //---------------------------------------------------------------
    public int compareTo(Object o) {
	MFloat f = (MFloat)o;
	return data.compareTo(f.getData());
    }

    //---------------------------------------------------------------
    public String toString() {
	return ""+data;
    }
    
} // MInteger
