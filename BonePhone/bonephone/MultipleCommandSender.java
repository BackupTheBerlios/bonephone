package bonephone;

import org.mbus.*;
import java.util.*;


/** Class to send multiple commands via MBUS to a dedicated target. */
public class MultipleCommandSender {
	private TransportLayer tl;
	private Address sender;
	private Address recipient;
	private Vector commands;
	private Header header;

	/** add a command to the bunch. 
	 * @param command String to add.
	*/
	public void add(String command) {
		commands.addElement(new Command(command));
	}

	/** reset this sender, means empty its command list. */
	public void removeAll() { commands.removeAllElements(); }

	/** trigger to send all added commands in one mbus message. */
	public void send() {
		Message m=new Message (header,commands);
		tl.send(m);
	}

	/** @param sender MBUS address this message comes from.
	 * @param recipient MBUS address this message goes to.
	 * @param t TransportLayer to use for sending.
	*/
	public MultipleCommandSender(Address sender, Address recipient, TransportLayer t) {
		header=new Header(sender, recipient, 0, false); 
		this.sender=sender;
		this.recipient=recipient;
		commands=new Vector(5);
		tl=t;
	}
}

