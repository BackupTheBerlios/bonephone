package bonephone;

import java.util.*;

/** re-definition of the standard Vector class from java.util. 
 * java.util.Vector imlicitly implements our VectorRead interface but because it does
 * not declare it (how could it ?) it does not match VectorRead. So we re-define the Vector class with 
 * an explicit declaration of the implementation of the VectorRead interface,
 * @author Jens Fiedler (fiedler@fokus.gmd.de)
*/
class Vector extends java.util.Vector implements VectorRead {
	public Vector () { super(); }
	public Vector (int a) { super(a); }
	public Vector (int a, int b) { super(a,b); }
	public Vector (Collection c) { super(c); }
}
