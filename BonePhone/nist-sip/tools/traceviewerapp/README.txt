
This is the trace viewer app. The NIST-SIP Stack and consequently JAIN stack 
include a message trace capture facility. You can enable trace capture by
setting the line accessLogViaRMI in the configfile. Then start the trace
viewer specifying the host via the -rmihost flag and the port via the -rmiport flag.

For example


java TraceViewer -rmihost jitterbug.antd.nist.gov -rmiport 1099

The default values for rmihost and rmiport are 127.0.0.1 and 1099

Author:

Christophe Chazeau (christophe.chazeau@antd.nist.gov)

