#! /usr/bin/env perl

###############################################################################
# Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        #
# See ../../../../doc/uncopyright.html for conditions of use.                  #
# Author: Marc Bednarek (bednarek@nist.gov) 			               #
# Questions/Comments: nist-sip-dev@antd.nist.gov                               #
################################################################################

# Example of firewall control on server side
# Listen on port 24674 for trusted clients
# receives a command line and execute it,
# if it is in the list of allowed scripts.
# Here we use 192.168.3.1 as the firewall address
# remember to put your own instead

use IO::Socket;

# List of trusted clients
# Put your own list here
@trusted_clients = ('192.168.3.1',
		    '192.168.2.4',
		    '192.168.2.2',
		    );

# Utility hashtable. Gives a quick way to know if a client is trusted or not
foreach (@trusted_clients) {
    $TRUSTED_CLIENT{$_}++;
}

# List of allowed scripts
# Put your own here
@allowed_scripts = ('openPort.pl',
		    'closePort.pl',
		    'DNATBegin.pl',
		    'DNATEnd.pl',
		    );

# Utility hashtable. Gives a quick way to know if a command is allowed or not
foreach (@allowed_scripts) {
    $ALLOWED_SCRIPT{$_}++;
}

# Create the listening socket on port 24674
$socket = IO::Socket::INET->new(
                              Listen    => 1,
                              Reuse     => 1,
                              LocalAddr => '192.168.3.1',
                              LocalPort => 24674,
                              Proto     => 'tcp'
                              );

# Server code
while (1) {
    # Accepting connections
    print "Accepting connections ...\n";
    $peer_socket = $socket->accept;
    print "Connection from ", $peer_socket->peerhost(), "\n";

    # If the connection comes from a trusted client, ...
    if ($TRUSTED_CLIENT{$peer_socket->peerhost()}) {
	# ... read its message, ...
	$message = <$peer_socket>;
	# ... accept it only if it begin with the name of an allowed script ...
	($script, @args) = split / /, $message;
	if ($ALLOWED_SCRIPT{$script}) {
	    # (remove from the arguments anything that is not a digit 
	    # or a colon)
	    foreach (@args) {
		s/[\d:]//g;
	    }
	    print "Executing $message";
	    # ... execute it, send back the output ...
	    $peer_socket->print(`$message`);
	} else {
	    $peer_socket->print("Cannot execute $script\n");
	    print "Cannot execute $script\n";
	}
	# ... and close the connection.
	$peer_socket->close;
    } else {
	$peer_socket->print("Unauthorized client\n");
	print "Unauthorized client\n";
    }
}
