/*
 * Copyright(c) 1996-1998 Gregory M. Messner
 * All rights reserved
 *
 * Permission to use, copy, modify and distribute this material for
 * non-commercial personal and educational use without fee is hereby
 * granted, provided that the above copyright notice and this permission
 * notice appear in all copies, and that the name of Gregory M. Messner
 * not be used in advertising or publicity pertaining to this material
 * without the specific, prior written permission of Gregory M. Messner
 * or an authorized representative.
 *
 * GREGORY M. MESSNER MAKES NO REPRESENTATIONS AND EXTENDS NO WARRANTIES,
 * EXPRESSED OR IMPLIED, WITH RESPECT TO THE SOFTWARE, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * ANY PARTICULAR PURPOSE, AND THE WARRANTY AGAINST INFRINGEMENT OF PATENTS
 * OR OTHER INTELLECTUAL PROPERTY RIGHTS. THE SOFTWARE IS PROVIDED "AS IS",
 * AND IN NO EVENT SHALL GREGORY M. MESSNER BE LIABLE FOR ANY DAMAGES,
 * INCLUDING ANY LOST PROFITS OR OTHER INCIDENTAL OR CONSEQUENTIAL DAMAGES
 * RELATING TO THE SOFTWARE.
 */

/* 
 * This class is part of the com.messners.mail package
 */
package org.mbus;

/**
 * This class defines static methods for encoding and decoding Base64 data
 * specified in RFC-2045.
 *
 * @author         Gregory M. Messner <gmessner@messners.com>
 */

public class Base64Codec {


    protected static char[] _char_map = {
	'A', 'B', 'C', 'D', 'E', 'F', 'G',
	'H', 'I', 'J', 'K', 'L', 'M', 'N',
	'O', 'P', 'Q', 'R', 'S', 'T', 'U',
	'V', 'W', 'X', 'Y', 'Z',

	'a', 'b', 'c', 'd', 'e', 'f', 'g',
	'h', 'i', 'j', 'k', 'l', 'm', 'n',
	'o', 'p', 'q', 'r', 's', 't', 'u',
	'v', 'w', 'x', 'y', 'z',

	'0', '1', '2', '3', '4', 
	'5', '6', '7', '8', '9',

	'+', '/' };
    
    protected static byte[] _encode_map=new String(_char_map).getBytes();

    protected static byte _decode_map[] = new byte[128];
    static {
	/*
	 * Fill in the decode map
	 */
	for (int i = 0; i < _encode_map.length; i++) {
	    _decode_map[_encode_map[i]] = (byte)i;
	}
    }

    /**
     * This class isn't meant to be instantiated.
     */
    private Base64Codec () {
    }

    /**
     * This method encodes the given byte[] using the Base64 encoding
     * specified in RFC-2045.
     *
     * @param  data the data to encode.
     * @return the Base64 encoded <var>data</var>
     */
    public final static byte[] encode (byte[] data) {

	if (data == null) {
	    return (null);
	}

	/*
	 * Craete a buffer to hold the results
	 */
	byte dest[] = new byte[((data.length + 2) / 3) * 4];


	/*
	 * 3-byte to 4-byte conversion and 
	 * 0-63 to ascii printable conversion
	 */
	int i, j;
	int data_len = data.length - 2;
	for (i = 0, j = 0; i < data_len; i += 3) {

	    dest[j++] = _encode_map[(data[i] >>> 2) & 077];
	    dest[j++] = _encode_map[(data[i + 1] >>> 4) & 017 |
				   (data[i] << 4) & 077];
	    dest[j++] = _encode_map[(data[i + 2] >>> 6) & 003 |
				   (data[i + 1] << 2) & 077];
	    dest[j++] = _encode_map[data[i + 2] & 077];
	}
	
	if (i < data.length) {
	    dest[j++] = _encode_map[(data[i] >>> 2) & 077];

	    if (i < data.length-1) {
		dest[j++] = _encode_map[(data[i + 1] >>> 4) & 017 |
				       (data[i] << 4) & 077];
		dest[j++] = _encode_map[(data[i + 1] << 2) & 077];
	    } else {
		dest[j++] = _encode_map[(data[i] << 4) & 077];
	    }
	}


	/*
	 * Pad with "=" characters
	 */
	for ( ; j < dest.length; j++) {
	    dest[j] = (byte)'=';
	}

	return (dest);
    }


    /**
     * This method decodes the given byte[] using the Base64 encoding
     * specified in RFC-2045.
     *
     * @param  data the Base64 encoded data to decode.
     * @return the decoded <var>data</var>.
     */
    public final static byte[] decode (byte[] data) {

	if (data == null)
	    return (null);

	/*
	 * Remove the padding on the end
	 */
	int ending = data.length;
	if (ending < 1) {
	    return (null);
	}
	while (data[ending - 1] == '=')
	    ending--;

	/*
	 * Create a buffer to hold the results
	 */
	byte dest[] = new byte[ending - data.length / 4];


	/*
	 * ASCII printable to 0-63 conversion
	 */
	for (int i = 0; i < data.length; i++) {
	    data[i] = _decode_map[data[i]];
	}

	
	/*
	 * 4-byte to 3-byte conversion
	 */
	int i, j;
	int dest_len = dest.length - 2;
	for (i = 0, j = 0; j < dest_len; i += 4, j += 3) {
	    dest[j] = (byte) (((data[i] << 2) & 255) |
			      ((data[i + 1] >>> 4) & 003));
	    dest[j + 1] = (byte) (((data[i + 1] << 4) & 255) |
				  ((data[i + 2] >>> 2) & 017));
	    dest[j + 2] = (byte) (((data[i + 2] << 6) & 255) |
				  (data[i + 3] & 077));
	}

	if (j < dest.length) {
	    dest[j] = (byte) (((data[i] << 2) & 255) |
			      ((data[i + 1] >>> 4) & 003));
	}

	j++;
	if (j < dest.length) {
	    dest[j] = (byte) (((data[i + 1] << 4) & 255) |
			      ((data[i + 2] >>> 2) & 017));
	}

	return (dest);
    }
}
