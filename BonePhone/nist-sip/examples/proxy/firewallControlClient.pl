#! /usr/bin/env perl

###############################################################################
# Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        #
# See ../../../../doc/uncopyright.html for conditions of use.                  #
# Author: Marc Bednarek (bednarek@nist.gov) 			               #
# Questions/Comments: nist-sip-dev@antd.nist.gov                               #
################################################################################

# Example of firewall control on client side
# Connect on the firewall control server (port 24674)
# and send the command line as is.
# Here we use 192.168.3.1 as the firewall address
# remember to put your own instead

# Usage example: 
# firewallControlClient.pl openPort.pl 129.6.55.159:6524 192.168.3.3:8074

use IO::Socket;

# Open a socket on the server
# Retry (at most 10 times) every 3 seconds until it succeeds
until ($socket or $n++ >= 10) {
    print "Trying to connect ...\n";
    $socket = IO::Socket::INET->new(
				    PeerAddr => '192.168.3.1',
				    PeerPort => 24674,
				    Proto    => 'tcp'
				    );
    sleep(3);
}
if ($socket) {
    print "Ok\n";
} else {
    die "Unable to connect\n";
}

# Send the command to the server
$socket->print(join(" ", @ARGV));
$socket->print("\n");

# Read and print the output of the server
while (<$socket>) {
    print;
}
