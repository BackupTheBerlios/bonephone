#!/bin/sh

# Starts the trace viewer
# takes the xml files containing the traces as arguments
echo $OS

if test $OS = "Windows_NT" ; then
	java -classpath ".;../../lib/xerces/xerces.jar;../../;../traceviewer.jar" tools.traceviewerapp.TraceViewer $*
else
	java -classpath ".:../../lib/xerces/xerces.jar:../../:../traceviewer.jar" tools.traceviewerapp.TraceViewer $*

fi
