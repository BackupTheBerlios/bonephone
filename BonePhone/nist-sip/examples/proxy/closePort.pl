#! /usr/bin/env perl

###############################################################################
# Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        #
# See ../../../../doc/uncopyright.html for conditions of use.                  #
# Author: Marc Bednarek (bednarek@nist.gov) 			               #
# Questions/Comments: nist-sip-dev@antd.nist.gov                               #
###############################################################################/

# Example of a script to close down a previously opened up port in the firewall
# Takes a list of peers as arguments, in 'address:port' form
# NB: Here, we assume that all media traffic is on top of UDP

foreach $peer1 (@ARGV) {
    ($addr1, $port1) = split /:/, $peer1;
    foreach $peer2 (@ARGV) {
	next if ($peer1 eq $peer2);
	($addr2, $port2) = split /:/, $peer2;
	# Build the command line
	# Here, we remove a forwarding rule previously added 
	$command = "/usr/local/sbin/iptables -D FORWARD -p udp -s $addr1 --sport $port1 -d $addr2 --dport $port2 -j ACCEPT";

	# Print it out, for debugging purpose
	print "Executing: $command\n";

	# Execute it print it out
	print `$command`;
    }
}

