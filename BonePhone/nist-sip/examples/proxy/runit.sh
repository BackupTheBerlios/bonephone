#!/bin/sh

# Start the proxy and also start the rmiregistry on the specified
# port if there is one argument.
echo "Make sure classpath includes the root"

\rm  -f sipserverlog.txt
if [ "$#" != 0 ]
then
    if ! netstat -ape | grep rmiregistry | grep $1 > /dev/null ; then
        rmiregistry $1 &
    fi
fi
java -Djava.security.policy=test.policy -classpath "../../:../../lib/antlr/antlrall.jar" examples.proxy.ServerMain -configFile configfile
