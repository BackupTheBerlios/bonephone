#! /usr/bin/env perl

###############################################################################
# Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        #
# See ../../../../doc/uncopyright.html for conditions of use.                  #
# Author: Marc Bednarek (bednarek@nist.gov) 			               #
# Questions/Comments: nist-sip-dev@antd.nist.gov                               #
################################################################################

# Example of a script to close down a port in the firewall for media traffic
# and to remove rerouting rules previously added
# Takes a list of peers as arguments, in 'address:port' form
# NB: Here, we assume that all media traffic is on top of UDP
# and that traffic is redirected to port 50600

foreach $peer1 (@ARGV) {
    ($addr1, $port1) = split /:/, $peer1;
    foreach $peer2 (@ARGV) {
	next if ($peer1 eq $peer2);
	($addr2, $port2) = split /:/, $peer2;

	# Build and execute the command lines
	# Here, we remove the rerouting rule previously added
	$command = "/usr/local/sbin/iptables -D PREROUTING -t nat -p udp -s $addr1 --sport $port1 --dport 50600 -j DNAT --to-destination $addr2:$port2";
	&exec_command($command);
	
	# Here, we remove the forwarding rule previously added
	$command = "/usr/local/sbin/iptables -D FORWARD -p udp -s $addr1 --sport $port1 -d $addr2 --dport $port2 -j ACCEPT";
	&exec_command($command);
    }
}

sub exec_command {
    # Print it out, for debugging purpose
    print "Executing: $_[0]\n";

    # Execute it, and print the result
    print `$_[0]`;
}
