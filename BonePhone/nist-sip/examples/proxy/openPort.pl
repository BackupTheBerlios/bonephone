#! /usr/bin/env perl

###############################################################################
# Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        #
# See ../../../../doc/uncopyright.html for conditions of use.                  #
# Author: Marc Bednarek (bednarek@nist.gov) 			               #
# Questions/Comments: nist-sip-dev@antd.nist.gov                               #
################################################################################

# Example of a script to open up a port in the firewall for media traffic
# Takes a list of peers as arguments, in 'address:port' form
# NB: Here, we assume that all media traffic is on top of UDP

foreach $peer1 (@ARGV) {
    ($addr1, $port1) = split /:/, $peer1;
    foreach $peer2 (@ARGV) {
	next if ($peer1 eq $peer2);
	($addr2, $port2) = split /:/, $peer2;

	# Build the command line
	# Here, we add a rule allowing forwarding of UDP packets coming from a
	# specified addr/port and going to a specified addr/port
	$command = "/usr/local/sbin/iptables -I FORWARD 1 -p udp -s $addr1 --sport $port1 -d $addr2 --dport $port2 -j ACCEPT";
	
	# Print it out, for debugging purpose
	print "Executing: $command\n";
	
	# Execute it, and print the result
	print `$command`;
    }
}
