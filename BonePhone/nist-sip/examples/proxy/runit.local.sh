#!/bin/sh
echo "Start rmiregistry please!"
\rm  -f sipserverlog.txt
java -Djava.security.policy=test.policy -classpath "../../:../../lib/antlr/antlrall.jar" examples.proxy.ServerMain -configFile configfile.local
