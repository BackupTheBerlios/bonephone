package gov.nist.sip;
/**
 *A tagging interface that allows implementations to check if a deep copy of an
 *object will be created when a given object is cloned. The cloning of sip objects
 *is as follows:
 *For each object or basic type in the clonee the following appears in the
 * corresponding field of the cloned:
 *  1. All primitive types are copied.
 *  2. All SIPCloneable objects are deep copied.
 *  3. All cloneable objects are cloned (i.e. their clone method is called).
 *  4. All wrapper types (Integer, Double etc.) are deep copied.
 *  5. A reference is copied from the clonee.
 */
public interface SIPCloneable extends Cloneable{ public Object clone(); }
