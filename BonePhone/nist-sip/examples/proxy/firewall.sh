#! /bin/sh

###############################################################################
# Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        #
# See ../../../../doc/uncopyright.html for conditions of use.                  #
# Author: Marc Bednarek (bednarek@nist.gov) 			               #
# Questions/Comments: nist-sip-dev@antd.nist.gov                               #
################################################################################

# Example of a Linux 2.4 firewall
# openPort.pl, closePort.pl, DNATBegin.pl and DNATEnd.pl are made 
# to be used with this firewall

##############
# NAT (SNAT) #
##############

/usr/local/sbin/iptables -t nat -F POSTROUTING
/usr/local/sbin/iptables -t nat -A POSTROUTING -o eth0 -j SNAT --to-source 129.6.55.81
/usr/local/sbin/iptables -t nat -F PREROUTING

##############
# New chains #
##############

# cleaning 
/usr/local/sbin/iptables -F INPUT
/usr/local/sbin/iptables -F FORWARD

# new chain IN_ETH0
/usr/local/sbin/iptables -F IN_ETH0
/usr/local/sbin/iptables -X IN_ETH0
/usr/local/sbin/iptables -N IN_ETH0
/usr/local/sbin/iptables -F IN_ETH0

# every input message from eth0 goes to IN_ETH0
/usr/local/sbin/iptables -A INPUT -i eth0 -j IN_ETH0

##############################
# Behaviour of IN_ETH0 chain #
##############################

# Accept and do not log anything coming from the connections 
# initiated from inside
/usr/local/sbin/iptables -A IN_ETH0 -m state --state ESTABLISHED,RELATED -j ACCEPT

# Accept and do not log access to the web server
# already logged in /etc/httpd/logs/access_log*
/usr/local/sbin/iptables -A IN_ETH0 -p tcp --dport 80 -j ACCEPT

# Accept and do not log acces to the SIP proxy
# already logged in /tmp/sipserverlog.txt
/usr/local/sbin/iptables -A IN_ETH0 -p tcp --dport 5060:5070 -j ACCEPT
/usr/local/sbin/iptables -A IN_ETH0 -p udp --dport 5060:5070 -j ACCEPT

# Accept and do not log access to ssh server
# already logged by syslogd
/usr/local/sbin/iptables -A IN_ETH0 -p tcp --dport 22 -j ACCEPT

# Accept and do not log everything coming from the NFS servers
/usr/local/sbin/iptables -A IN_ETH0 -p udp -s 129.6.55.250 -j ACCEPT
/usr/local/sbin/iptables -A IN_ETH0 -p udp -s 129.6.50.250 -j ACCEPT

# Accept and do not log RMI traffic
/usr/local/sbin/iptables -A IN_ETH0 -p tcp --dport 1099 -j ACCEPT
/usr/local/sbin/iptables -A IN_ETH0 -p tcp --dport 25674 -j ACCEPT

# drop and do not log broadcast
/usr/local/sbin/iptables -A IN_ETH0 -d 255.255.255.255 -j DROP
/usr/local/sbin/iptables -A IN_ETH0 -d 129.6.55.255 -j DROP

# drop and do not log multicast packets
/usr/local/sbin/iptables -A IN_ETH0 -d 224.0.0.1 -j DROP
/usr/local/sbin/iptables -A IN_ETH0 -d 224.0.0.2 -j DROP

# accept and do not log incoming pings, but not too much
# defense against Ping Of Death
/usr/local/sbin/iptables -A IN_ETH0 -p icmp --icmp-type echo-request -m limit --limit 1/s -j ACCEPT

# log and drop everything else
/usr/local/sbin/iptables -A IN_ETH0 -j LOG
/usr/local/sbin/iptables -A IN_ETH0 -j DROP

##############################
# Behaviour of FORWARD chain #
##############################

# accept and do not log DNS traffic
/usr/local/sbin/iptables -A FORWARD -p tcp --sport 53 -j ACCEPT
/usr/local/sbin/iptables -A FORWARD -p udp --sport 53 -j ACCEPT

# accept and do not log NFS traffic
/usr/local/sbin/iptables -A FORWARD -s 129.6.55.250 -j ACCEPT
/usr/local/sbin/iptables -A FORWARD -s 129.6.50.250 -j ACCEPT
/usr/local/sbin/iptables -A FORWARD -d 129.6.55.250 -j ACCEPT
/usr/local/sbin/iptables -A FORWARD -d 129.6.50.250 -j ACCEPT

# accept and do not ICMP traffic
/usr/local/sbin/iptables -A FORWARD -p icmp -j ACCEPT

# log and drop everything else
/usr/local/sbin/iptables -A FORWARD -j LOG
/usr/local/sbin/iptables -A FORWARD -j DROP

#################################
# Behaviour of PREROUTING chain #
#################################

# SIP media traffic shaping
# This is an example of what one can do with the port translation method
# Here, we limit to 10 packets/s
/usr/local/sbin/iptables -t nat -A PREROUTING -p udp --dport 50600 -m limit --limit 10/s -j ACCEPT

