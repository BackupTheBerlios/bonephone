/**
*  Base64FormatException.java
* This code was part of the Jigsaw http server package.
* (c) COPYRIGHT MIT and INRIA, 1996.
*
* THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT HOLDERS
* MAKE NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT
* LIMITED TO, WARRANTIES OF MERCHANTABILITY OR FITNESS FOR ANY PARTICULAR
* PURPOSE OR THAT THE USE OF THE SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE
* ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.
*  
* COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT, SPECIAL OR
* CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE SOFTWARE OR
* DOCUMENTATION.
*/

package gov.nist.sip.stack.security;

/**
 * Exception for invalid BASE64 streams.
 */

public class Base64FormatException extends Exception {

  /**
   * Create that kind of exception
   * @param msg The associated error message 
   */
  
  public Base64FormatException(String msg) {
	super(msg) ;
  }

}
