package org.mbus;

import java.util.*;

/**
 * Command.java
 *
 *
 * <br>Created: Wed Sep 22 19:21:23 1999
 *
 * @author Stefan Prelle
 * @version $id: Command.java 1.0 Wed Sep 22 19:21:23 1999 prelle Exp $
 */

public class Command extends Object {
    
    private String      command;
    private MDataType[] params;

    //---------------------------------------------------------------
    /**
     * Parse a string like "<tt>mbus.foo (myparam 2)</tt>" as a command.
     *
     * @param data String to parse
     */
    public Command(String data) {
	StringTokenizer str = new StringTokenizer(data, "(");
	if (!str.hasMoreTokens() || data.length()==0) {
	    command = null;
	    params  = null;
	} else {
	    command = str.nextToken().trim();
	    Parser parser = new Parser();
	    MList  tmp = parser.parse(str.nextToken("\n"));
	    params = tmp.toArray();
//  	    params  = MDataType.parse(str.nextToken("\n"));
	}
    }

    //---------------------------------------------------------------
    /**
     * Create a new command with a command-string and parameters.
     *
     * @param name Command-string
     * @param param Parameter-Array
     */
    public Command(String name, MDataType[] param) {
	command = name;
	params  = param;
    }

    //---------------------------------------------------------------
    public String getCommand() {
	return command;
    }

    //---------------------------------------------------------------
    public MDataType[] getParameters() {
	return params;
    }

    //---------------------------------------------------------------
    public String toString() {
	return command+" "+(new MList(params));
    }
    
} // Command
